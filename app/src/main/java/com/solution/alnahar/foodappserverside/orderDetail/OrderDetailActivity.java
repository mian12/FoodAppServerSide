package com.solution.alnahar.foodappserverside.orderDetail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.solution.alnahar.foodappserverside.Common.Common;
import com.solution.alnahar.foodappserverside.R;
import com.solution.alnahar.foodappserverside.adapter.OrderDetailAdapter;

public class OrderDetailActivity extends AppCompatActivity {


    TextView order_id,order_Phone,order_total,order_address,order_comment;
    RecyclerView recyclerView;

    String order_id_value="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);


        order_id=findViewById(R.id.order_id);
        order_Phone=findViewById(R.id.order_Phone);
        order_total=findViewById(R.id.order_total);
        order_address=findViewById(R.id.order_address);
        order_comment=findViewById(R.id.order_comment);

        recyclerView=findViewById(R.id.istFood);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        if (getIntent().getExtras()!=null)
        {

            order_id_value=getIntent().getStringExtra("OrderId");
            order_id.setText(order_id_value);

            order_Phone.setText(String.format("Phone No : %s",Common.currentRequest.getPhone()));
            order_total.setText(String.format("Total Amount : %s",Common.currentRequest.getTotal()));
            order_address.setText(String.format("Address : %s",Common.currentRequest.getAddress()));
            order_comment.setText(String.format("Comment : %s",Common.currentRequest.getComment()));

            OrderDetailAdapter adapter=new OrderDetailAdapter(Common.currentRequest.getOrderList(),OrderDetailActivity.this);

            adapter.notifyDataSetChanged();

            recyclerView.setAdapter(adapter);

        }

    }
}
