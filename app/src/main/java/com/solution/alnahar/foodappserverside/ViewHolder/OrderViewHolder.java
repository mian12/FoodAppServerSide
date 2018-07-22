package com.solution.alnahar.foodappserverside.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.solution.alnahar.foodappserverside.Common.Common;
import com.solution.alnahar.foodappserverside.Interface.ItemClickListener;
import com.solution.alnahar.foodappserverside.R;

import info.hoang8f.widget.FButton;


public class OrderViewHolder extends RecyclerView.ViewHolder {


   public FButton  btnEdit,btnRemove,btnDetail,btnDirection;

    public TextView order_id, order_status, order_phone, order_address,order_date;
    public ImageView menuImage;

    public OrderViewHolder(View itemView) {
        super(itemView);

        order_id = itemView.findViewById(R.id.order_id);
        order_status = itemView.findViewById(R.id.order_status);
        order_phone = itemView.findViewById(R.id.order_phone);
        order_address = itemView.findViewById(R.id.order_address);

        order_date= itemView.findViewById(R.id.order_date);

        btnEdit= itemView.findViewById(R.id.btnEdit);
        btnRemove= itemView.findViewById(R.id.btRemove);
        btnDetail= itemView.findViewById(R.id.btnDetail);
        btnDirection= itemView.findViewById(R.id.btnDirection);



    }


}
