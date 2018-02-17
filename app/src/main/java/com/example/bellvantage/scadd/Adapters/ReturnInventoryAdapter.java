package com.example.bellvantage.scadd.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bellvantage.scadd.R;
import com.example.bellvantage.scadd.ReturnActivty;
import com.example.bellvantage.scadd.swf.ReturnInventoryLineItem;

import java.util.ArrayList;

/**
 * Created by Sachika on 17/08/2017 21:15
 */

public class ReturnInventoryAdapter extends RecyclerView.Adapter<ReturnInventoryAdapter.MyViewHoldder> {

    ArrayList<ReturnInventoryLineItem> returnInventoryLineItemArrayList;
    Context context;
    ReturnActivty returnActivty;

    public ReturnInventoryAdapter(ArrayList<ReturnInventoryLineItem> returnInventoryLineItemArrayList, Context context) {
        this.returnInventoryLineItemArrayList = returnInventoryLineItemArrayList;
        this.context = context;
        this.returnActivty = (ReturnActivty) context;
    }

    @Override
    public MyViewHoldder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_for_return,parent,false);
        return new MyViewHoldder(view,returnActivty);
    }

    @Override
    public void onBindViewHolder(MyViewHoldder holder, final int position) {

        final ReturnInventoryLineItem returnInventoryLineItem = returnInventoryLineItemArrayList.get(position);
        holder.tvBatchId.setText(returnInventoryLineItem.getBatchId()+"");
        holder.tvProductName.setText(returnInventoryLineItem.getProductName());
        holder.tvProductPrice.setText(String.format("Rs. %.2f",returnInventoryLineItem.getUnitPrice()));
        holder.tvQuantity.setText(returnInventoryLineItem.getQuantity()+"");


         /* ReturnType\":1,\"ReturnTypeName\":\"Expired\"}," +
                "{\"ReturnType\":2,\"ReturnTypeName\":\"Other\"}," +
                "{\"ReturnType\":3,\"ReturnTypeName\":\"Returns from SRN\"}," +
                "{\"ReturnType\":4,\"ReturnTypeName\":\"Damage\"}," +
                "{\"ReturnType\":5,\"ReturnTypeName\":\"50% Off\"*/

        if(returnInventoryLineItem.getReturnType()==1){
            holder.tvReturnType.setText("Expired");
        }
        else if(returnInventoryLineItem.getReturnType()==2){
            holder.tvReturnType.setText("Other");
        }
        else if(returnInventoryLineItem.getReturnType()==3){
            holder.tvReturnType.setText("Returns from SRN");
        } else if(returnInventoryLineItem.getReturnType()==4){
            holder.tvReturnType.setText("Damage");
        }else if(returnInventoryLineItem.getReturnType()==5){
            holder.tvReturnType.setText("50% Off");
        }
        //Set Product Status
        if(returnInventoryLineItem.getIsSellable()==1){
            holder.tvProductStatus.setText("Sellable");
        }else{
            holder.tvProductStatus.setText("None-Sellable");
        }

        holder.tvAmouont.setText(String.format("Rs. %.2f",returnInventoryLineItem.getTotalAmount()));


        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnActivty.deleteItem(returnInventoryLineItem,position);
            }
        });

        holder.llLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnActivty.setItemData(returnInventoryLineItem,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (returnInventoryLineItemArrayList.size()!=0){
            return returnInventoryLineItemArrayList.size();
        }
        return 0;
    }

    public static  class MyViewHoldder extends  RecyclerView.ViewHolder implements View.OnClickListener{

        ReturnActivty returnActivty;
        TextView tvBatchId,tvProductName,tvProductPrice,tvQuantity,tvProductStatus,tvReturnType,tvAmouont;
        ImageView ivDelete;
        LinearLayout llLayout;

        public MyViewHoldder(View itemView,ReturnActivty returnActivty) {
            super(itemView);
            this.returnActivty = returnActivty;

            //TextViews
            tvBatchId = (TextView) itemView.findViewById(R.id.tvBatchId);
            tvProductName = (TextView) itemView.findViewById(R.id.tvProductName);
            tvProductPrice = (TextView) itemView.findViewById(R.id.tvProductPrice);
            tvQuantity = (TextView) itemView.findViewById(R.id.tvQuantity);
            tvProductStatus = (TextView) itemView.findViewById(R.id.tvProductStatus);
            tvReturnType = (TextView) itemView.findViewById(R.id.tvReturnType);
            tvAmouont = (TextView) itemView.findViewById(R.id.tvAmouont);

            //ImageViews
            ivDelete = (ImageView) itemView.findViewById(R.id.ivDelete);

            //Layouts
            llLayout = (LinearLayout) itemView.findViewById(R.id.llLayout);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
