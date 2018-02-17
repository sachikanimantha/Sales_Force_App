package com.example.bellvantage.scadd.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bellvantage.scadd.R;
import com.example.bellvantage.scadd.swf.ReturnInventoryLineItem;

import java.util.ArrayList;

/**
 * Created by Sachika on 08/09/2017.
 */

public class AdapterForReturnLineItem extends RecyclerView.Adapter<AdapterForReturnLineItem.MyViewHolder> {


    ArrayList<ReturnInventoryLineItem> lineItemArray = new ArrayList<>();
    Context context;

    public AdapterForReturnLineItem(ArrayList<ReturnInventoryLineItem> lineItemArray, Context context) {
        this.lineItemArray = lineItemArray;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_for_line_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ReturnInventoryLineItem returnInventoryLineItem = lineItemArray.get(position);
        int itemCode = returnInventoryLineItem.getProductId();
        String productName = returnInventoryLineItem.getProductName();
        int qty = returnInventoryLineItem.getQuantity();
        double unitPrice = returnInventoryLineItem.getUnitPrice();
        double lineValue = qty*unitPrice;

        holder.tvItemCode.setText(itemCode+"");
        holder.tvItemDescription.setText(productName);
        holder.tvQty.setText(""+qty);
        holder.tvUnitPrice.setText(String.format("%.2f",unitPrice));
        holder.tvLineValue.setText(String.format("%.2f",lineValue));

    }

    @Override
    public int getItemCount() {
        if (lineItemArray.size()!=0){
            return lineItemArray.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvItemCode,tvItemDescription,tvQty,tvUnitPrice,tvLineValue;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvItemCode = (TextView) itemView.findViewById(R.id.tvItemCode);
            tvItemDescription= (TextView) itemView.findViewById(R.id.tvItemDescription);
            tvQty= (TextView) itemView.findViewById(R.id.tvQty);
            tvUnitPrice= (TextView) itemView.findViewById(R.id.tvUnitPrice);
            tvLineValue= (TextView) itemView.findViewById(R.id.tvLineValue);
        }
    }

}
