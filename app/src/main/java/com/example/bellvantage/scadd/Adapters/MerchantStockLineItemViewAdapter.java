package com.example.bellvantage.scadd.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bellvantage.scadd.R;
import com.example.bellvantage.scadd.swf.MerchantStockLineitems;

import java.util.ArrayList;

/**
 * Created by Bellvantage on 27/10/2017.
 */

public class MerchantStockLineItemViewAdapter extends RecyclerView.Adapter<MerchantStockLineItemViewAdapter.MyViewHolder2>{

    Context context;
    //ArrayList<MerchantStock> merchantStocks = new ArrayList<>();
    ArrayList<MerchantStockLineitems> lineitemses = new ArrayList<>();


    public MerchantStockLineItemViewAdapter(Context context,ArrayList<MerchantStockLineitems> lineitemses) {
        this.context = context;
        //this.merchantStocks = merchantStocks;
        this.lineitemses = lineitemses;
    }

    @Override
    public MyViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_for_msli,parent,false);
        return new MyViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder2 holder, int position) {

        holder.bind(lineitemses.get(position));

    }

    @Override
    public int getItemCount() {
        return lineitemses.size();
    }

    public static class MyViewHolder2 extends RecyclerView.ViewHolder{

        TextView tv_id,tv_product_name,tv_qty;

        public MyViewHolder2(View itemView) {
            super(itemView);
            tv_id = (TextView)itemView.findViewById(R.id.tv_msv_msli_id);
            tv_product_name = (TextView)itemView.findViewById(R.id.tv_msv_msli_productname);
            tv_qty = (TextView)itemView.findViewById(R.id.tv_msv_msli_qty);
        }

        public void bind(MerchantStockLineitems lineitems_){
            tv_id.setText(""+lineitems_.getProductID());
            tv_product_name.setText(lineitems_.getProduct_name());
            tv_qty.setText(""+lineitems_.getQuantity());
        }

    }
}
