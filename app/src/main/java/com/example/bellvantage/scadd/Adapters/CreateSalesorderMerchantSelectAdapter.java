package com.example.bellvantage.scadd.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bellvantage.scadd.R;
import com.example.bellvantage.scadd.swf.MerchantDetails;

import java.util.ArrayList;

/**
 * Created by Bellvantage on 29/11/2017.
 */

public class CreateSalesorderMerchantSelectAdapter extends RecyclerView.Adapter<CreateSalesorderMerchantSelectAdapter.myViewHolder3>{


    Context context;
    ArrayList<MerchantDetails> merchantArrayList = new ArrayList<>();
    static ArrayList<String> merchantArrayList_visited = new ArrayList<>();
    int merchantIsVisit;
    public OnItemClickListener listener;

    public interface OnItemClickListener{
        void OnItemClick(MerchantDetails merchant);
    }

    public CreateSalesorderMerchantSelectAdapter(Context context,
                                                 ArrayList<MerchantDetails> merchantArrayList ,
                                                 ArrayList<String> visited,
                                                 OnItemClickListener onItemClickListener) {
        this.context = context;
        this.merchantArrayList = merchantArrayList;
        merchantArrayList_visited = visited;
        listener = onItemClickListener;
        //merchantIsVisit = isVisit;

        System.out.println("merchantArrayList size - "+merchantArrayList.size());
        System.out.println("merchantArrayList_visited size - "+merchantArrayList_visited.size());
    }


    @Override
    public myViewHolder3 onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_for_merchantlist,parent,false);
        return new myViewHolder3(view);
    }




    @Override
    public void onBindViewHolder(myViewHolder3 holder, int position) {
        holder.bind(merchantArrayList.get(position),listener);
    }

    @Override
    public int getItemCount() {
        return merchantArrayList.size();
    }





    public static class myViewHolder3 extends RecyclerView.ViewHolder{

        TextView tv_cusname,tv_cusid,tv_cus_visit,tv_cus_notvisit,tv_select;
        LinearLayout linearLayout;

        public myViewHolder3(View itemView) {
            super(itemView);

            tv_cusname = (TextView)itemView.findViewById(R.id.tv_merchantname);
            tv_cusid = (TextView)itemView.findViewById(R.id.tv_merchantid);
//            tv_cus_visit = (TextView)itemView.findViewById(R.id.tv_visited);
//            tv_cus_notvisit = (TextView)itemView.findViewById(R.id.tv_notvisit);
            tv_select = (TextView)itemView.findViewById(R.id.tv_submit);
            linearLayout = (LinearLayout)itemView.findViewById(R.id.ll_cus_row);
        }
        public void bind(final MerchantDetails merchant,
                         final OnItemClickListener onItemClickListener){

            tv_cusname.setText(merchant.getMerchantName());
            tv_cusid.setText("Id : "+merchant.getMerchantId());


            if(merchant.getMerchantId() == 0){
                linearLayout.setBackgroundColor(Color.parseColor("#FFCFD8DC"));
            }

            for(int h=0; h< merchantArrayList_visited.size(); h++) {
                if (merchant.getMerchantId() == Integer.parseInt(merchantArrayList_visited.get(h))) {
                    linearLayout.setBackgroundColor(Color.parseColor("#983ea615"));
                }

            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.OnItemClick(merchant);
                }
            });


        }


    }

}
