package com.solution.alnahar.foodappserverside.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.solution.alnahar.foodappserverside.R;
import com.solution.alnahar.foodappserverside.model.Order;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<MyViewHolder> {


    List<Order> listOrder;
    Context mContext;

    public OrderDetailAdapter(List<Order> listOrder,Context context)

    {
        this.listOrder = listOrder;
        this.mContext=context;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.row_order_detail, parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        Order order = listOrder.get(position);
        holder.name.setText(String.format("Name : %s",order.getProduct_name()));
        holder.qty.setText(String.format("Quantity : %s",order.getQty()));
        holder.price.setText(String.format("Price : %s",order.getPrice()));
        holder.discount.setText(String.format("Discount : %s",order.getDiscount()));


    }

    @Override
    public int getItemCount() {
        return listOrder.size();
    }
}


class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView name, price, qty, discount;


    public MyViewHolder(View itemView) {


        super(itemView);

        name = itemView.findViewById(R.id.product_name);
        price = itemView.findViewById(R.id.product_price);
        qty = itemView.findViewById(R.id.product_qty);
        discount = itemView.findViewById(R.id.product_discount);


    }
}
