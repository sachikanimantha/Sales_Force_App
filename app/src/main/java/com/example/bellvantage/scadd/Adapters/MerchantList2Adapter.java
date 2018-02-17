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
import com.example.bellvantage.scadd.swf.Merchant2;

import java.util.ArrayList;

/**
 * Created by Bellvantage on 13/06/2017.
 */

public class MerchantList2Adapter extends ArrayAdapter<Merchant2> {

    ArrayList<Merchant2> merchant2 = new ArrayList<>();

    public MerchantList2Adapter(@NonNull Context context, @LayoutRes int resource, ArrayList<Merchant2> objects) {
        super(context, resource, objects);
        merchant2 = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    public View initView(int position, @Nullable View convertView, @NonNull ViewGroup parent){

        View v = convertView;
        LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = layoutInflater.inflate(R.layout.layout_for_merchants2,null);
        TextView tv_name = (TextView)v.findViewById(R.id.tv_merchant_name);
        tv_name.setText(merchant2.get(position).getUserName());
        return v;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        return initView(position,convertView,parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }
}
