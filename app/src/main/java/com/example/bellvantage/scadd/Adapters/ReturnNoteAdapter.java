package com.example.bellvantage.scadd.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bellvantage.scadd.swf.ReturnInventory;
import com.example.bellvantage.scadd.swf.ReturnInventoryLineItem;

import java.util.ArrayList;

/**
 * Created by Bellvantage on 19/09/2017.
 */

public class ReturnNoteAdapter extends RecyclerView.Adapter<ReturnNoteAdapter.MyViewHolder_> {


    private ArrayList<ReturnInventory> returnInventories;
    private ArrayList<ReturnInventoryLineItem> returnInventoryLineItems;

    public class MyViewHolder_ extends RecyclerView.ViewHolder{

        public TextView title;
        public ImageView iconview;


        public MyViewHolder_(View itemView) {
            super(itemView);

        }
    }


    @Override
    public MyViewHolder_ onCreateViewHolder(ViewGroup parent, int viewType) {


        return null;
    }

    @Override
    public void onBindViewHolder(MyViewHolder_ holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
