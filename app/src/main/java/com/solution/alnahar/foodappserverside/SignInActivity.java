package com.solution.alnahar.foodappserverside;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import com.solution.alnahar.foodappserverside.Common.Common;
import com.solution.alnahar.foodappserverside.model.User;

import dmax.dialog.SpotsDialog;


public class SignInActivity extends AppCompatActivity {


    Button btnSignIn;
    EditText edtPhone, edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_sign_in);

        edtPhone = (MaterialEditText) findViewById(R.id.editPhone);
        edtPassword = (MaterialEditText) findViewById(R.id.editPassword);

        btnSignIn = findViewById(R.id.btnSignIn);

        final SpotsDialog dialog = new SpotsDialog(SignInActivity.this);
        dialog.setCancelable(false);


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {

                            dialog.dismiss();


                            User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);

                            //for using in Request table,so that if shiper does not find path the he cal call the customer
                            user.setPhone(edtPhone.getText().toString());




                            if (Boolean.parseBoolean(user.getIsStaff())) {

                                if (user.getPassword().equalsIgnoreCase(edtPassword.getText().toString())) {
                                    //login ok
                                  //  Toast.makeText(SignInActivity.this, "Sign in Successfully!!", Toast.LENGTH_SHORT).show();
                                    Common.currentUser = user;
                                    Intent intent=new Intent(SignInActivity.this,HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(SignInActivity.this, "Wrong password ", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(SignInActivity.this, "Please login  with staff account", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            dialog.dismiss();
                            Toast.makeText(SignInActivity.this, "user does not exits in  database", Toast.LENGTH_SHORT).show();

                        }


                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        dialog.dismiss();

                    }
                });
            }
        });


    }
}
