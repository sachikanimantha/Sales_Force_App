package com.example.bellvantage.scadd.Adapters;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bellvantage.scadd.R;
import com.example.bellvantage.scadd.swf.BrandList;

import java.util.ArrayList;

/**
 * Created by Bellvantage on 13/06/2017.
 */

public class BrandNamelistAdapter extends ArrayAdapter<BrandList> {

    public ArrayList<BrandList> brandListArrayAdapter = new ArrayList<>() ;

    public BrandNamelistAdapter(@NonNull Context context, @LayoutRes int resource, @IdRes int textViewResourceId, @NonNull ArrayList<BrandList> objects) {
        super(context, resource, textViewResourceId, objects);
        brandListArrayAdapter = objects;
        Toast.makeText(context, "brandListArrayAdapter.size() --- "+brandListArrayAdapter.size(), Toast.LENGTH_SHORT).show();;
    }

    public BrandNamelistAdapter(@NonNull Context context, @LayoutRes int resource, ArrayList<BrandList> brandListArrayAdapter) {
        super(context, resource, brandListArrayAdapter);
        this.brandListArrayAdapter = brandListArrayAdapter;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    private View initView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.layout_for_brandnames,null);
        TextView tv_name = (TextView)convertView.findViewById(R.id.tv_brand_name);
        tv_name.setText(brandListArrayAdapter.get(position).getProductName());
        return convertView;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }
}
