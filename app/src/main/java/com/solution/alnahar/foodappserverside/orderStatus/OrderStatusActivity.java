package com.solution.alnahar.foodappserverside.orderStatus;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.solution.alnahar.foodappserverside.Common.Common;
import com.solution.alnahar.foodappserverside.HomeActivity;
import com.solution.alnahar.foodappserverside.Interface.ItemClickListener;
import com.solution.alnahar.foodappserverside.R;
import com.solution.alnahar.foodappserverside.TrackOrderActivity;
import com.solution.alnahar.foodappserverside.ViewHolder.OrderViewHolder;
import com.solution.alnahar.foodappserverside.model.Request;
import com.solution.alnahar.foodappserverside.orderDetail.OrderDetailActivity;


public class OrderStatusActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference requests_db_ref;

    RecyclerView recyclerView_orderStatus;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    MaterialSpinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);


        // init firebase
        database = FirebaseDatabase.getInstance();
        requests_db_ref = database.getReference("Requests");

        recyclerView_orderStatus = findViewById(R.id.orderStatus);
        layoutManager = new LinearLayoutManager(this);
        recyclerView_orderStatus.setHasFixedSize(true);
        recyclerView_orderStatus.setLayoutManager(layoutManager);

        loadOrders();
    }


    private void loadOrders() {


        FirebaseRecyclerOptions<Request> options = new FirebaseRecyclerOptions.Builder<Request>()
                .setQuery(requests_db_ref, Request.class)
                .build();


        adapter = new FirebaseRecyclerAdapter<Request, OrderViewHolder>(options) {
            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_order_status, parent, false);

                return new OrderViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder holder, final int position, @NonNull final Request model) {

                holder.order_id.setText(adapter.getRef(position).getKey());
                holder.order_phone.setText(model.getPhone());
                holder.order_address.setText(model.getAddress());
                holder.order_status.setText(Common.convertCodeToStatus(model.getStatus()));

                holder.btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showUpdateOder(adapter.getRef(position).getKey(),adapter.getItem(position));

                    }
                });
                holder.btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        deleteOrder(adapter.getRef(position).getKey());
                    }
                });
                holder.btnDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(OrderStatusActivity.this, OrderDetailActivity.class);
                        Common.currentRequest = model;
                        intent.putExtra("OrderId",adapter.getRef(position).getKey());
                        startActivity(intent);

                    }
                });
                holder.btnDirection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Toast.makeText(OrderStatusActivity.this, "Still pending this feature,sorry :(", Toast.LENGTH_SHORT).show();
//                        Intent trackingOrder = new Intent(OrderStatusActivity.this, TrackOrderActivity.class);
//                        Common.currentRequest = model;
//                        startActivity(trackingOrder);

                    }
                });





            }
        };

        adapter.notifyDataSetChanged();
        recyclerView_orderStatus.setAdapter(adapter);
    }


    //ctrl+o




    private void deleteOrder(String key) {
        requests_db_ref.child(key).removeValue();
        adapter.notifyDataSetChanged();
    }


    private void showUpdateOder(String key, final Request item) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(OrderStatusActivity.this);
        alertDialog.setTitle("Update Order");
        alertDialog.setMessage("Please choose status");

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.order_update_dialog, null);



        spinner=view.findViewById(R.id.statusSpinner);

        spinner.setItems("Placed","On My Way","Shipped");
        alertDialog.setView(view);
        alertDialog.setIcon(R.drawable.ic_update_black_24dp);

       final String localKey=key;

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // update information
               item.setStatus(String.valueOf(spinner.getSelectedIndex()));

               requests_db_ref.child(localKey).setValue(item);
               adapter.notifyDataSetChanged();


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

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
