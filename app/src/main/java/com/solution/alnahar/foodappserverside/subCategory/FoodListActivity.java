package com.solution.alnahar.foodappserverside.subCategory;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.solution.alnahar.foodappserverside.Common.Common;
import com.solution.alnahar.foodappserverside.HomeActivity;
import com.solution.alnahar.foodappserverside.Interface.ItemClickListener;
import com.solution.alnahar.foodappserverside.R;
import com.solution.alnahar.foodappserverside.ViewHolder.FoodListViewHolder;
import com.solution.alnahar.foodappserverside.model.Category;
import com.solution.alnahar.foodappserverside.model.Food;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import dmax.dialog.SpotsDialog;

public class FoodListActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference foodList_db_ref;

    RecyclerView recyclerView_foodList;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Food, FoodListViewHolder> adapter;

    public static String categoryId = "";
    SpotsDialog dialog;


    FirebaseStorage storage;
    StorageReference storageReference;


    MaterialEditText editTextName,editTextDescription,editTextPrice,editTextDiscount;
    Button btnImageSelect,btnUpload;

    Uri saveUri;
    private int PICK_IMGAE_REQUEST = 12;

    Food newFood;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);


        // init firebase
        database = FirebaseDatabase.getInstance();
        foodList_db_ref = database.getReference("Foods");


        // init firebase storage
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        dialog = new SpotsDialog(FoodListActivity.this);
        dialog.setCancelable(false);

        recyclerView_foodList = findViewById(R.id.recyclerView_foodList);

        layoutManager = new LinearLayoutManager(this);
        recyclerView_foodList.setHasFixedSize(true);
        recyclerView_foodList.setLayoutManager(layoutManager);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 addCategoryDialog();
            }
        });

        if (getIntent() != null) {
            categoryId = getIntent().getExtras().getString("categoryId").toString();
            if (categoryId != null && !categoryId.isEmpty()) {

                loadListFood(categoryId);


            }

        }

    }

    private void addCategoryDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FoodListActivity.this);
        alertDialog.setTitle("Add New Food");
        alertDialog.setMessage("Please fill full information");

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.add_new_food_dialog, null);


        editTextName = view.findViewById(R.id.editName);
        editTextDescription = view.findViewById(R.id.editDescription);
        editTextPrice = view.findViewById(R.id.editPrice);
        editTextDiscount = view.findViewById(R.id.editDiscount);


        btnImageSelect = view.findViewById(R.id.btnSelect);
        btnUpload = view.findViewById(R.id.btnUpload);


        btnImageSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadImage();

            }
        });

        alertDialog.setView(view);
        alertDialog.setIcon(R.drawable.ic_cart);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // here just creat new Category
                if (newFood != null) {
                    foodList_db_ref.push().setValue(newFood);
                }


            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        alertDialog.show();

    }

    private void chooseImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMGAE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMGAE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            saveUri = data.getData();
            btnImageSelect.setText("Image Selected!!");
            //TODO: action
        }
    }


    private void uploadImage() {

        if (saveUri != null) {


            final ProgressDialog progressDialog = new ProgressDialog(FoodListActivity.this);
            progressDialog.setMessage("Uploading..");
            progressDialog.show();
            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/" + imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            Toast.makeText(FoodListActivity.this, "uploaded!!!", Toast.LENGTH_SHORT).show();

                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    // set value new category if image uploaded successfully and we get download link

                                    newFood=new Food();

                                    newFood.setName(editTextName.getText().toString());
                                    newFood.setDescription(editTextDescription.getText().toString());
                                    newFood.setPrice(editTextPrice.getText().toString());
                                    newFood.setDiscount(editTextDiscount.getText().toString());
                                    newFood.setMenuId(categoryId);
                                    newFood.setImage(uri.toString());



                                }
                            });

                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(FoodListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            double progress = (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded" + progress + "%");

                        }
                    });

        }
    }


    private void loadListFood ( final String categoryId){

            // DatabaseReference ref=FirebaseDatabase.getInstance().getReference();
            Query query = foodList_db_ref.orderByChild("menuId").equalTo(categoryId);


            FirebaseRecyclerOptions<Food> options = new FirebaseRecyclerOptions.Builder<Food>()
                    .setQuery(query, Food.class)
                    .build();

            adapter = new FirebaseRecyclerAdapter<Food, FoodListViewHolder>(options) {
                @Override
                public FoodListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.row_food_list, parent, false);

                    return new FoodListViewHolder(view);
                }

                @Override
                protected void onBindViewHolder(@NonNull FoodListViewHolder holder, int position, @NonNull Food model) {
                    holder.foodName.setText(model.getName());


                    Picasso.get().load(model.getImage()).into(holder.foodImage);

                    final Food object = model;

                    holder.setItemClickListner(new ItemClickListener() {
                        @Override
                        public void onClick(View view, int position, Boolean isLongClick) {

                        }
                    });


                }

            };

            adapter.notifyDataSetChanged();

            recyclerView_foodList.setAdapter(adapter);

        }



    // update and delete
    //ctrl+o


    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getTitle().equals(Common.UPDATE))
        {
            showUpdateDialog(adapter.getRef(item.getOrder()).getKey(),adapter.getItem(item.getOrder()));
        }
        else
        {
            deleteFood(adapter.getRef(item.getOrder()).getKey());
        }

        return super.onContextItemSelected(item);
    }

    private void deleteFood(String key) {

        foodList_db_ref.child(key).removeValue();
        Toast.makeText(FoodListActivity.this, "Item deleted successfully!!", Toast.LENGTH_SHORT).show();
    }

    private void showUpdateDialog(final String key, final Food item) {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FoodListActivity.this);
        alertDialog.setTitle("Update Food");
        alertDialog.setMessage("Please fill full information");

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.add_new_food_dialog, null);


        editTextName = view.findViewById(R.id.editName);
        editTextDescription = view.findViewById(R.id.editDescription);
        editTextPrice = view.findViewById(R.id.editPrice);
        editTextDiscount = view.findViewById(R.id.editDiscount);


        // default value set
        editTextName.setText(item.getName());
        editTextDescription.setText(item.getDescription());
        editTextPrice.setText(item.getPrice());
        editTextDiscount.setText(item.getDiscount());


        btnImageSelect = view.findViewById(R.id.btnSelect);
        btnUpload = view.findViewById(R.id.btnUpload);


        btnImageSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateImage(item);

            }
        });

        alertDialog.setView(view);
        alertDialog.setIcon(R.drawable.ic_cart);
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // here just creat new Category
                if (item != null) {
                    // set value new category if image uploaded successfully and we get download link

                    item.setName(editTextName.getText().toString());
                    item.setDescription(editTextDescription.getText().toString());
                    item.setPrice(editTextPrice.getText().toString());
                    item.setDiscount(editTextDiscount.getText().toString());

                    foodList_db_ref.child(key).setValue(item);

                    Toast.makeText(FoodListActivity.this, "updated successfully", Toast.LENGTH_SHORT).show();
                }


            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });

        alertDialog.show();
    }

    private void updateImage(final Food item) {

        if (saveUri != null) {


            final ProgressDialog progressDialog = new ProgressDialog(FoodListActivity.this);
            progressDialog.setMessage("Uploading..");
            progressDialog.show();
            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = storageReference.child("images/" + imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            Toast.makeText(FoodListActivity.this, "uploaded!!!", Toast.LENGTH_SHORT).show();

                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    item.setImage(uri.toString());



                                }
                            });

                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(FoodListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            double progress = (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded" + progress + "%");

                        }
                    });

        }
    }






    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();


    }



    }


