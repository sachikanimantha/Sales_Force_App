package com.example.bellvantage.scadd;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bellvantage.scadd.swf.MerchantDetails;

import java.util.ArrayList;

/**
 * Created by Sachika on 6/3/2017.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

    ArrayList<MerchantDetails> arrayList;
    Context context;


    public SearchAdapter(Context context, ArrayList<MerchantDetails> productArrayList) {
        this.arrayList = productArrayList;
        this.context = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_for_search,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final MerchantDetails merchantDetails  = arrayList.get(position);
        holder.tvMerchantName.setText(merchantDetails.getMerchantName());
        holder.tvMerchantAddress.setText(merchantDetails.getAddress1());

        holder.tvUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,MRA_UpdateActivity.class);
                intent.putExtra("merchant_details",  merchantDetails);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(arrayList !=null){
            return arrayList.size();
        }
        return 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvMerchantName,tvMerchantAddress,tvUpdate;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvMerchantName = (TextView)itemView.findViewById(R.id.tvMerchantName);
            tvMerchantAddress = (TextView)itemView.findViewById(R.id.tvMerchantAddress);
            tvUpdate = (TextView)itemView.findViewById(R.id.tvUpdate);

        }
    }

    public void setFilter(ArrayList<MerchantDetails> arrayListF){
        arrayList = new ArrayList<>();
        arrayList.addAll(arrayListF);
        notifyDataSetChanged();
    }
}
