package com.example.bellvantage.scadd.Adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.bellvantage.scadd.R;
import com.example.bellvantage.scadd.swf.MerchantOutstandSalesOrderList;

import java.util.ArrayList;

/**
 * Created by Bellvantage on 20/06/2017.
 */

public class MerchantOutstandSalesListAdapter extends ArrayAdapter {

    public ArrayList<MerchantOutstandSalesOrderList> merchantOutstandSalesOrderLists;

    public static class MyHolder{
        TextView PaymentID;
        TextView InvoiceID;
        TextView TotalAmount;
        TextView PaidAmount ;
        TextView PaymentType;
        TextView PaymentDate;
    }


    public MerchantOutstandSalesListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<MerchantOutstandSalesOrderList> objects) {
        super(context, resource, objects);
        this.merchantOutstandSalesOrderLists = objects;
        System.out.println("ar sixe - "+objects.size());
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        MyHolder myHolder;
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.layout_for_merchant_sales_outstanding_list_row,parent,false);

            myHolder = new MyHolder();
            myHolder.PaymentID = (TextView)convertView.findViewById(R.id.tv_outstand_paymentid);
            myHolder.InvoiceID = (TextView)convertView.findViewById(R.id.tv_outstand_invoiceid);
            myHolder.TotalAmount = (TextView)convertView.findViewById(R.id.tv_outstand_totalamount);
            myHolder.PaidAmount = (TextView)convertView.findViewById(R.id.tv_outstand_paidamount);
            myHolder.PaymentType = (TextView)convertView.findViewById(R.id.tv_outstand_paymenttype);
            myHolder.PaymentDate = (TextView)convertView.findViewById(R.id.tv_outstand_paymentdate);

            convertView.setTag(myHolder);
        }else{
            myHolder = (MyHolder)convertView.getTag();
        }

        myHolder.PaymentID.setText(String.valueOf(merchantOutstandSalesOrderLists.get(position).getPaymentID()));
        myHolder.InvoiceID.setText(String.valueOf(merchantOutstandSalesOrderLists.get(position).getInvoiceID()));
        myHolder.TotalAmount.setText(String.valueOf(merchantOutstandSalesOrderLists.get(position).getTotalAmount()));
        myHolder.PaidAmount.setText(String.valueOf(merchantOutstandSalesOrderLists.get(position).getPaidAmount()));
        myHolder.PaymentType.setText(merchantOutstandSalesOrderLists.get(position).getPaymentType());
        myHolder.PaymentDate.setText(merchantOutstandSalesOrderLists.get(position).getPaymentDate());

        return convertView;
    }
}
