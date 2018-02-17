package com.example.bellvantage.scadd.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bellvantage.scadd.R;
import com.example.bellvantage.scadd.swf.MerchantStockLineitems;

import java.util.ArrayList;

/**
 * Created by Bellvantage on 16/10/2017.
 */

public class MerchantInventoryCardAdpter extends RecyclerView.Adapter<MerchantInventoryCardAdpter.MyViewHolder> implements Filterable {


    ArrayList<MerchantStockLineitems> lineitemses = new ArrayList<>() ;
    ArrayList<MerchantStockLineitems> lineitemses_filter = new ArrayList<>();
    Context context;
    public OnItemClickListener listener;
    int posi;


    public interface OnItemClickListener{
        void onItemClick(MerchantStockLineitems productList,int position);
    }

    public MerchantInventoryCardAdpter(ArrayList<MerchantStockLineitems> productListArrayList,
                                       ArrayList<MerchantStockLineitems> filterList,
                                       Context context,
                                       OnItemClickListener onItemClickListener) {
        this.lineitemses = productListArrayList;
        this.lineitemses_filter = filterList;
        this.context = context;
        this.listener = onItemClickListener;


    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_for_merchant_card,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        MerchantStockLineitems stockLineitems = lineitemses_filter.get(position);
        holder.bind(stockLineitems,listener,position);
        this.posi = position;
    }

    @Override
    public int getItemCount() {
        return lineitemses_filter.size();
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String s = constraint.toString();
                if(s.isEmpty()){
                    lineitemses_filter = lineitemses;
                }else{
                    ArrayList<MerchantStockLineitems> filteredList = new ArrayList<>();
                    for(MerchantStockLineitems merchantStockLineitems: lineitemses){
                        if(merchantStockLineitems.getProduct_name().toLowerCase().contains(s)){
                            filteredList.add(merchantStockLineitems);
                        }
                    }
                    lineitemses_filter = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = lineitemses_filter;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                    lineitemses_filter = (ArrayList<MerchantStockLineitems>) results.values;
                    notifyDataSetChanged();
            }
        };
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_name,tv_qty;
        ImageView iv;
        int done = R.drawable.ic_done;
        int notdone = R.drawable.ic_not_done;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_name = (TextView) itemView.findViewById(R.id.tv_item_name_mcard);
            tv_qty = (TextView) itemView.findViewById(R.id.tv_item_qty_mcard);
            iv = (ImageView) itemView.findViewById(R.id.iv_item_mcard);
        }

        public void bind(final MerchantStockLineitems lineitems, final OnItemClickListener clickListener, final int position){

            if(lineitems.getIsSync() == 1){
                iv.setImageResource(done);
            }else if(lineitems.getIsSync() == 0){
                iv.setImageResource(notdone);
            }

            tv_name.setText(lineitems.getProduct_name());
            tv_qty.setText("Cus.stock - "+lineitems.getQuantity());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(lineitems,position);
                }
            });
        }
    }

    public void update(ArrayList<MerchantStockLineitems> list){

        lineitemses_filter = list;
        notifyDataSetChanged();
    }
}
