package com.solution.alnahar.foodappserverside.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.solution.alnahar.foodappserverside.Common.Common;
import com.solution.alnahar.foodappserverside.Interface.ItemClickListener;
import com.solution.alnahar.foodappserverside.R;

public class FoodListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnCreateContextMenuListener {


    public TextView foodName;
    public ImageView foodImage;
    public ItemClickListener itemClickListner;

    public FoodListViewHolder(View itemView) {
        super(itemView);

        foodName = itemView.findViewById(R.id.foodList_name);
        foodImage = itemView.findViewById(R.id.foodList_imge);


        itemView.setOnCreateContextMenuListener(this);
        itemView.setOnClickListener(this);
    }


    public void setItemClickListner(ItemClickListener itemClickListner) {

        this.itemClickListner = itemClickListner;
    }

    @Override
    public void onClick(View v) {

        itemClickListner.onClick(itemView, getAdapterPosition(), false);


    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        contextMenu.setHeaderTitle("Select the action");
        contextMenu.add(0,0,getAdapterPosition(), Common.UPDATE);
        contextMenu.add(0,1,getAdapterPosition(), Common.DELETE);
    }
}
