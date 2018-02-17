package com.example.bellvantage.scadd.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bellvantage.scadd.R;
import com.example.bellvantage.scadd.domains.ReviseSalesOrderList;

import java.util.ArrayList;

/**
 * Created by Sachika on 08/09/2017.
 */

public class AdapterForInvoiceLineItem extends RecyclerView.Adapter<AdapterForInvoiceLineItem.MyViewHolder> {


    ArrayList<ReviseSalesOrderList> lineItemArray = new ArrayList<>();
    Context context;

    public AdapterForInvoiceLineItem(ArrayList<ReviseSalesOrderList> lineItemArray, Context context) {
        this.lineItemArray = lineItemArray;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_for_invoice__line_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ReviseSalesOrderList reviseSalesOrderList = lineItemArray.get(position);
        int itemCode = reviseSalesOrderList.getProductId();
        String productName = reviseSalesOrderList.getProductName();
        int qty = reviseSalesOrderList.getQuantity();
        int freeQty = reviseSalesOrderList.getFreeQuantity();
        double unitPrice = reviseSalesOrderList.getUnitSellingPrice();
        double lineValue = qty*unitPrice;
        double freeValue = freeQty * unitPrice;
        holder.tvItemCode.setText(itemCode+"");
        holder.tvItemDescription.setText(productName);
        holder.tvQty.setText(""+qty);
        holder.tvFreeQty.setText(""+freeQty);
        holder.tvUnitPrice.setText(String.format("%.2f",unitPrice));
        holder.tvLineValue.setText(String.format("%.2f",lineValue));
        holder.tvFreeValue.setText(String.format("%.2f",freeValue));
        holder.tvTotal.setText(String.format("%.2f",freeValue+lineValue));

    }

    @Override
    public int getItemCount() {
        if (lineItemArray.size()!=0){
            return lineItemArray.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvItemCode,tvItemDescription,tvQty,tvUnitPrice,tvLineValue,tvFreeQty,tvFreeValue,tvTotal;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvItemCode = (TextView) itemView.findViewById(R.id.tvItemCode);
            tvItemDescription= (TextView) itemView.findViewById(R.id.tvItemDescription);
            tvQty= (TextView) itemView.findViewById(R.id.tvQty);
            tvUnitPrice= (TextView) itemView.findViewById(R.id.tvUnitPrice);
            tvLineValue= (TextView) itemView.findViewById(R.id.tvLineValue);
            tvFreeQty= (TextView) itemView.findViewById(R.id.tvFreeQty);
            tvFreeValue= (TextView) itemView.findViewById(R.id.tvFreeValue);
            tvTotal= (TextView) itemView.findViewById(R.id.tvTotal);
        }
    }

}
