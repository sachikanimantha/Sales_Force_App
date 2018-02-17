package com.example.bellvantage.scadd.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bellvantage.scadd.R;
import com.example.bellvantage.scadd.swf.InvoiceLineItem;

import java.util.ArrayList;

/**
 * Created by Sachika on 22/05/2017.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.OrderRequestViewHolder> {


    Context context;
    ArrayList<InvoiceLineItem> arrayList;

    public ItemAdapter(Context context, ArrayList<InvoiceLineItem> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public OrderRequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_for_item,parent,false);
        return new OrderRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderRequestViewHolder holder, int position) {

        final String itemName  = arrayList.get(position).getItemName();
        final int qty = arrayList.get(position).getQty();
        final int freeLineQty = arrayList.get(position).getFreeQu();
        final double price = arrayList.get(position).getPrice();
        //final double lineTotal = arrayList.get(position).getLineTotal();
        final double lineTotal = (qty+freeLineQty) * price;

        holder.tvItem.setText(itemName+"");
        holder.tvQty.setText(qty+"");
        holder.tvFreeLineQty.setText(""+freeLineQty);
        holder.tvPrice.setText(String.format("Rs. %.2f",price));
        holder.tvLineTotal.setText(String.format("Rs. %.2f",lineTotal));

    }

    @Override
    public int getItemCount() {
        if (arrayList != null){
            return  arrayList.size();
        }
        return 0;
    }

    public static class OrderRequestViewHolder extends  RecyclerView.ViewHolder{

        TextView tvItem, tvQty, tvPrice,tvLineTotal,tvFreeLineQty;

        public OrderRequestViewHolder(View itemView) {
            super(itemView);
            tvItem = (TextView)itemView.findViewById(R.id.tvItem);
            tvQty = (TextView)itemView.findViewById(R.id.tvQty);
            tvFreeLineQty = (TextView)itemView.findViewById(R.id.tvFreeLineQty);
            tvPrice = (TextView)itemView.findViewById(R.id.tvPrice);
            tvLineTotal = (TextView)itemView.findViewById(R.id.tvLineTotsl);
        }
    }
}
