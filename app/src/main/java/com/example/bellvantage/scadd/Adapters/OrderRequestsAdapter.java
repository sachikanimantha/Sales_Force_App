package com.example.bellvantage.scadd.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bellvantage.scadd.PrintInvoiceActivity;
import com.example.bellvantage.scadd.R;
import com.example.bellvantage.scadd.Utils.DateManager;
import com.example.bellvantage.scadd.Utils.SyncManager;
import com.example.bellvantage.scadd.swf.InvoiceLineItem;
import com.example.bellvantage.scadd.swf.InvoiceList2;
import com.example.bellvantage.scadd.swf.ReturnInventory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Sachika on 22/05/2017.
 */

public class OrderRequestsAdapter extends RecyclerView.Adapter<OrderRequestsAdapter.OrderRequestViewHolder> {


    private Context context;
    private ArrayList<InvoiceList2> arrayList;
    ArrayList<InvoiceLineItem> finalInvoiceLineItemArrayList = new ArrayList<>();
    ArrayList<ReturnInventory> finalReturnInventories = new ArrayList<>();

    String iDate;

    long today_ = DateManager.todayMillsecWithDateFormat("dd.MM.yyyy");
    String today_mill = DateManager.getDateAccordingToMilliSeconds(today_,"dd.MM.yyyy");

    public OrderRequestsAdapter(Context context, ArrayList<InvoiceList2> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public OrderRequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_for_order_requests,parent,false);
        return new OrderRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderRequestViewHolder holder, final int position) {


        final int requestNo = arrayList.get(position).getInvoiceId();
        final String merchantname = arrayList.get(position).getMerchantName();
        final int merchantid = arrayList.get(position).getMerchantId();
        final String createdDate = arrayList.get(position).getInvoiceDate();
        final InvoiceList2 invoiceLis=arrayList.get(position);


        try {
            holder.tvRequestCode.setText(requestNo+"");
            holder.tvCustomerName.setText(merchantname);
            int start = createdDate.indexOf("(");
            int end = createdDate.indexOf(")");
            iDate = createdDate.substring(start+1,end);
            holder.tvCreatedDate.setText(getDate(Long.parseLong(iDate), "dd/MM/yyyy HH:mm"));

            System.out.println("created DATE - "+getDate(Long.parseLong(iDate), "dd/MM/yyyy HH:mm"));
            holder.tvAction.setText("View");

        }catch (Exception e){
            System.out.println("Error "+ e.getMessage());
        }

        holder.tvAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finalInvoiceLineItemArrayList = (new SyncManager(context)).getInvoiceLineItem_ACC_invoiceID(requestNo);
                finalReturnInventories = (new SyncManager(context)).getReturnNoteHeaderData(merchantid,getDate(Long.parseLong(iDate), "dd/MM/yyyy HH:mm"));

                ArrayList<String> creditnote_array = (new SyncManager(context)).getReturnNoteCreditId_vs_merchant(merchantid,today_mill);
                System.out.println("creditnote_array.size - "+creditnote_array.size());

                launchAtivity(finalInvoiceLineItemArrayList,invoiceLis,finalReturnInventories,creditnote_array);
            }
        });
    }




    @Override
    public int getItemCount() {
        if (arrayList != null){
            return  arrayList.size();
        }
        return 0;
    }

    public static  class OrderRequestViewHolder extends  RecyclerView.ViewHolder{

        TextView tvRequestCode, tvCustomerName, tvCreatedDate,tvAction;
        LinearLayout llLayout;

        public OrderRequestViewHolder(View itemView) {
            super(itemView);
            tvRequestCode = (TextView)itemView.findViewById(R.id.tvRequestNo);
            tvCustomerName = (TextView)itemView.findViewById(R.id.tvCustomerName);
            tvCreatedDate = (TextView)itemView.findViewById(R.id.tvCreatedDate);
            tvAction = (TextView)itemView.findViewById(R.id.tvAction);
            llLayout = (LinearLayout) itemView.findViewById(R.id.llLayout);
        }
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

    private void launchAtivity(ArrayList<InvoiceLineItem> productArray, InvoiceList2 invoiceLis,
                               ArrayList<ReturnInventory> returnInventories,ArrayList<String> cnote){
        Intent intent = new Intent(context,PrintInvoiceActivity.class);
        intent.putExtra("invoiceDetails",invoiceLis);
        intent.putExtra("itemArrayList",productArray);
        intent.putExtra("itemArrayReturnNote",returnInventories);
        intent.putExtra("creditNoteArray",cnote);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
