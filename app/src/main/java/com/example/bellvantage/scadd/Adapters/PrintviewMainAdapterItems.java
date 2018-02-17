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
import com.example.bellvantage.scadd.swf.ReturnInventoryLineItem;

import java.util.ArrayList;

/**
 * Created by Bellvantage on 13/09/2017.
 */

public class PrintviewMainAdapterItems extends ArrayAdapter{

    ArrayList<ReturnInventoryLineItem> returnInventoryLineItems = new ArrayList<>();
    int posi; ;

    MyViewHolder1 myViewHolder;

    public PrintviewMainAdapterItems(@NonNull Context context, @LayoutRes int resource,ArrayList<ReturnInventoryLineItem> objects1) {
        super(context, resource, objects1);
        this.returnInventoryLineItems = objects1;
    }

    public static class MyViewHolder1{

        TextView tri_name;
        TextView tri_reason;
        TextView tri_qty;
        TextView tri_linevalue;
        TextView tri_unitprice;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        if(convertView == null){
            myViewHolder = new MyViewHolder1();
            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.layout_for_returnnote_items,null);

            myViewHolder.tri_name = (TextView)convertView.findViewById(R.id.tv_rni_name_);
            myViewHolder.tri_reason = (TextView)convertView.findViewById(R.id.tv_rni_reson);
            myViewHolder.tri_qty = (TextView)convertView.findViewById(R.id.tv_rni_qty_);
            myViewHolder.tri_unitprice = (TextView)convertView.findViewById(R.id.tv_rni_unitprice_);
            myViewHolder.tri_linevalue = (TextView)convertView.findViewById(R.id.tv_rni_linevalue_);
            convertView.setTag(myViewHolder);

        }else{
            myViewHolder = (MyViewHolder1)convertView.getTag();
        }

        myViewHolder.tri_name.setText(""+returnInventoryLineItems.get(position).getProductName());
        myViewHolder.tri_qty.setText(""+returnInventoryLineItems.get(position).getQuantity());
        myViewHolder.tri_unitprice.setText(String.format("Rs %.2f",returnInventoryLineItems.get(position).getUnitPrice()));
        myViewHolder.tri_linevalue.setText(String.format("Rs %.2f",returnInventoryLineItems.get(position).getTotalAmount()));

       // int j = returnInventoryLineItems.get(position).getReturnType();

        if(returnInventoryLineItems.get(position).getReturnType() == 1){
            myViewHolder.tri_reason.setText("Expired");}
        else if(returnInventoryLineItems.get(position).getReturnType() == 2){
            myViewHolder.tri_reason.setText("Other");}
        else if(returnInventoryLineItems.get(position).getReturnType() == 3){
            myViewHolder.tri_reason.setText("SRN");
        } else if(returnInventoryLineItems.get(position).getReturnType() == 4){
            myViewHolder.tri_reason.setText("Damage");
        }else if(returnInventoryLineItems.get(position).getReturnType() == 5){
            myViewHolder.tri_reason.setText("50% Off");
        }

            //myViewHolder.tri_reason.setText(""+returnInventoryLineItems.get(position).getReturnType());


        return convertView;
    }


}
