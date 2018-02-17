package com.example.bellvantage.scadd.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bellvantage.scadd.MerchantLocation;
import com.example.bellvantage.scadd.MyOutletListActivity;
import com.example.bellvantage.scadd.R;
import com.example.bellvantage.scadd.swf.MerchantDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Sachika on 6/3/2017.
 */

public class OutletAdapter extends RecyclerView.Adapter<OutletAdapter.MyViewHolder> {

    ArrayList<MerchantDetails> arrayList;
    Context context;
    MyOutletListActivity myOutletListActivity;

    public OutletAdapter(Context context, ArrayList<MerchantDetails> productArrayList) {
        this.arrayList = productArrayList;
        this.context = context;
        this.myOutletListActivity = (MyOutletListActivity) context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_for_outlet,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final MerchantDetails merchantDetails  = arrayList.get(position);
       holder.tvOutletName.setText(merchantDetails.getMerchantName());

        holder.tvView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                myOutletListActivity.getQty();

                if (merchantDetails.getLongitude().isEmpty() || merchantDetails.getLatitude().isEmpty()||
                        merchantDetails.getLatitude().equals(" ")|| merchantDetails.getLongitude().equals(" ")){
                    Toast.makeText(context, "Please update merchant location. ", Toast.LENGTH_SHORT).show();
                }else{
                    Intent oIntent = new Intent(context,MerchantLocation.class);
                    oIntent.putExtra("merchantDetails",merchantDetails);
                    oIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(oIntent);
                }

            }
        });

        Picasso.with(context)
                .load( R.drawable.mm_mis)
                .error(android.R.drawable.stat_notify_error)
                .into(holder.ivBuilding);


    }

    @Override
    public int getItemCount() {
        if(arrayList !=null){
            return arrayList.size();
        }
        return 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvOutletName,tvView,tvUpdate;
        ImageView ivBuilding;

        public MyViewHolder(View itemView) {
            super(itemView);
           tvOutletName = (TextView)itemView.findViewById(R.id.tvOutletName);
            tvView = (TextView)itemView.findViewById(R.id.tvView);
           ivBuilding = (ImageView) itemView.findViewById(R.id.ivBuilding);

        }
    }

    public void setFilter(ArrayList<MerchantDetails> arrayListF){
        arrayList = new ArrayList<>();
        arrayList.addAll(arrayListF);
        notifyDataSetChanged();
    }

}
