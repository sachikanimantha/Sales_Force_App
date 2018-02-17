package com.example.bellvantage.scadd.Adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bellvantage.scadd.R;
import com.example.bellvantage.scadd.swf.ReturnInventory;
import com.example.bellvantage.scadd.swf.ReturnInventoryLineItem;

import java.util.ArrayList;

/**
 * Created by Bellvantage on 13/09/2017.
 */

public class PrintviewMainAdapter extends ArrayAdapter{

    ArrayList<ReturnInventory> returnInventories = new ArrayList<>();
    ArrayList<ReturnInventoryLineItem> returnInventoryLineItems = new ArrayList<>();

    MyViewHolder myViewHolder;

    //PrintviewMainAdapterItems printviewMainAdapterItems;


    public PrintviewMainAdapter(@NonNull Context context, @LayoutRes int resource,
                                @NonNull ArrayList<ReturnInventory> objects) {
        super(context, resource, objects);
        this.returnInventories = objects;
       // this.returnInventoryLineItems = objects1;

    }

    public static class MyViewHolder{

        TextView creditno;
        TextView returnDate;
        TextView totalamount;
        TextView totlOutstanding;
        TextView merchantId;
        TextView distrbutorId;
        TextView issync;
        TextView syncDate;
        ListView return_items;
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
            myViewHolder = new MyViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.layout_for_returnnote___,null);

            myViewHolder.creditno = (TextView)convertView.findViewById(R.id.tv_returnnote_id);
            myViewHolder.returnDate = (TextView)convertView.findViewById(R.id.tv_returnnote_delivery_date);
            myViewHolder.totalamount = (TextView)convertView.findViewById(R.id.tv_returnnote_amount);
//            myViewHolder.totlOutstanding = (TextView)convertView.findViewById(R.id.tv_returnnote_id);
//            myViewHolder.merchantId = (TextView)convertView.findViewById(R.id.tv_returnnote_id);
//            myViewHolder.distrbutorId = (TextView)convertView.findViewById(R.id.tv_returnnote_id);
//            myViewHolder.issync = (TextView)convertView.findViewById(R.id.tv_returnnote_id);
            myViewHolder.syncDate = (TextView)convertView.findViewById(R.id.tv_returnnote_credit_date);
            myViewHolder.return_items = (ListView)convertView.findViewById(R.id.lv__returnnote);


            convertView.setTag(myViewHolder);
        }else{
            myViewHolder = (MyViewHolder)convertView.getTag();
        }

        String DELIVERYdATA = returnInventories.get(position).getReturnDate();
        String CREDITdATE = returnInventories.get(position).getSyncDate();

        System.out.println("DELIVERYdATA - "+DELIVERYdATA);

        int start2 = CREDITdATE.indexOf("(");
        int end2 = CREDITdATE.indexOf(")");
        String date2 = CREDITdATE.substring(start2+1,end2);

        myViewHolder.creditno.setText("Return note - "+returnInventories.get(position).getCreditNoteNo());
        myViewHolder.returnDate.setText("Delivery Date : - "+DELIVERYdATA);
       // myViewHolder.syncDate.setText("Credit Date : - "+(new DateManager()).getDateAccordingToMilliSeconds(Long.parseLong(date2), "yyyy-MM-dd"));
        myViewHolder.totalamount.setText(String.format(" Rs : %.2f",returnInventories.get(position).getTotalAmount()));


        //returnInventoryLineItems = (new SyncManager(getContext())).getReturnNoteItemData(returnInventories.get(position).getCreditNoteNo());
        //System.out.println("returnInventoryLineItems.size - "+returnInventoryLineItems.size());

        //printviewMainAdapterItems = new PrintviewMainAdapterItems(getContext(),R.layout.layout_for_returnnote_items,returnInventoryLineItems,position);
        //myViewHolder.return_items.setAdapter(printviewMainAdapterItems);


        //myViewHolder.return_items.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, (new String[]{"Item 1","Item 2"})));
        return convertView;
    }
}
