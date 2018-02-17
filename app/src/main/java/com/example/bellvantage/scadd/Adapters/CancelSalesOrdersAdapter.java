package com.example.bellvantage.scadd.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bellvantage.scadd.ActivityCancelSalesOrderContinue;
import com.example.bellvantage.scadd.CreateInvoiceActivity;
import com.example.bellvantage.scadd.R;
import com.example.bellvantage.scadd.swf.OrderList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Sachika on 26/07/2017.
 */

public class CancelSalesOrdersAdapter extends RecyclerView.Adapter<CancelSalesOrdersAdapter.ReviseViewHolder> {

    ArrayList<OrderList> orderListArrayList;
    Context context;
    int id;

    public CancelSalesOrdersAdapter(Context context, ArrayList<OrderList> orderListArrayList,int id) {
        this.orderListArrayList = orderListArrayList;
        this.context = context;
        this.id =id;
    }

    @Override
    public ReviseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_for_revise_sales,parent,false);
        return new ReviseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviseViewHolder holder, int position) {
        final OrderList orderList = orderListArrayList.get(position);


        try{
            if (orderList.getMerchntName().isEmpty()||orderList.getMerchntName().equals("")){
                holder.tvMerchantName.setText("Direct Sale");
            }else{
                holder.tvMerchantName.setText(orderList.getMerchntName());
            }

        }catch (Exception e){

        }



        holder.tvSOID.setText(orderList.getSalesOrderId()+"");
        String createdDate = orderList.getEnteredDate();
        int start = createdDate.indexOf("(");
        int end = createdDate.indexOf(")");
        String iDate = createdDate.substring(start+1,end);
        holder.tvCreateDate.setText(getDate(Long.parseLong(iDate), "dd/MM/yyyy hh:mm"));

        holder.tvView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(id ==3){
                    Intent intent = new Intent(context,CreateInvoiceActivity.class);
                    intent.putExtra("orderList",orderList);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }else {
                    Intent intent = new Intent(context,ActivityCancelSalesOrderContinue.class);
                    intent.putExtra("orderList",orderList);
                    intent.putExtra("id",id);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }


            }
        });

    }

    @Override
    public int getItemCount() {
        if(orderListArrayList !=null){
            return orderListArrayList.size();
        }
        return 0;
    }


    public static class ReviseViewHolder extends RecyclerView.ViewHolder{

        TextView tvSOID,tvMerchantName,tvView,tvCreateDate;

        public ReviseViewHolder(View itemView) {
            super(itemView);
            tvSOID = (TextView) itemView.findViewById(R.id.tvSOID);
            tvView = (TextView) itemView.findViewById(R.id.tvView);
            tvMerchantName = (TextView) itemView.findViewById(R.id.tvMerchantName);
            tvCreateDate = (TextView) itemView.findViewById(R.id.tvCreateDate);
        }
    }


    public void setFilter(ArrayList<OrderList> arrayListF){
        orderListArrayList = new ArrayList<>();
        orderListArrayList.addAll(arrayListF);
        notifyDataSetChanged();
    }


    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
