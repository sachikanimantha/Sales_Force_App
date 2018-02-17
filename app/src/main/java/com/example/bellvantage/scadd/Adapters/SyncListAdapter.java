package com.example.bellvantage.scadd.Adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bellvantage.scadd.R;
import com.example.bellvantage.scadd.swf.SyncTables;

import java.util.ArrayList;

/**
 * Created by Bellvantage on 25/08/2017.
 */

public class SyncListAdapter extends ArrayAdapter {


    ArrayList<SyncTables> syncTables = new ArrayList<>();

    ViewHolder viewHolder;
    int rowco = 0;
    int row_new = 0;

    public static class ViewHolder{

        TextView tv_date;
        TextView name;
        TextView rowcount;
        TextView rowcount_new;
        TextView newdata;
        TextView newserverdata;
        TextView nodata;
        ImageView iv_sync;

    }

    public SyncListAdapter(@NonNull Context context, @LayoutRes int resource,
                           @NonNull ArrayList<SyncTables> objects) {
        super(context, resource, objects);
        this.syncTables = objects;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.layout_for_sync_manually,null);

            viewHolder.name = (TextView)convertView.findViewById(R.id.tv_name);
            viewHolder.tv_date = (TextView)convertView.findViewById(R.id.tv_details);
            viewHolder.rowcount = (TextView)convertView.findViewById(R.id.tv_rowcounts);
            viewHolder.rowcount_new = (TextView)convertView.findViewById(R.id.tv_rowcounts_new);
            viewHolder.nodata = (TextView)convertView.findViewById(R.id.tv_rowcount_nodata);
            viewHolder.newdata = (TextView)convertView.findViewById(R.id.tv_indicator_new);
            viewHolder.newserverdata = (TextView)convertView.findViewById(R.id.tv_indicator_serverdata);
            viewHolder.iv_sync = (ImageView)convertView.findViewById(R.id.iv_sync);

            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.name.setText(syncTables.get(position).getName());
        viewHolder.tv_date.setText(syncTables.get(position).getTime());
        viewHolder.rowcount.setText(""+syncTables.get(position).getRowcount());
        viewHolder.rowcount_new.setText(""+syncTables.get(position).getRowcount_new());
        viewHolder.iv_sync.setImageResource(syncTables.get(position).getStatus());

        if(syncTables.get(position).getRowcount() == 0){
            viewHolder.nodata.setVisibility(View.VISIBLE);
        }else{
            viewHolder.nodata.setVisibility(View.INVISIBLE);
        }

        if(syncTables.get(position).getRowcount() == syncTables.get(position).getRowcount_new()){
            viewHolder.newdata.setVisibility(View.GONE);
            viewHolder.newserverdata.setVisibility(View.VISIBLE);

        }else{
            viewHolder.newdata.setVisibility(View.VISIBLE);
            viewHolder.newserverdata.setVisibility(View.GONE);
        }

        return convertView;
    }


    public void updateList(ArrayList<SyncTables> a, int status,String time,
                           int row_cnt,int row_nes,int position){
        System.out.println("time - "+time+",row_cnt - "+row_cnt+",row_nes - "+row_nes);
        this.syncTables = a;
//        this.rowco = row_cnt;
//        this.row_new = row_nes;
        try {


            syncTables.get(position).setStatus(status);
            syncTables.get(position).setRowcount(row_cnt);
            syncTables.get(position).setRowcount_new(row_nes);
            syncTables.get(position).setTime(time);
        }catch (Exception e){
            System.out.println(" =============== Error =========================");
            e.printStackTrace();
        }
//        viewHolder = new ViewHolder();
//        if(row_cnt == 0){
//            viewHolder.nodata.setVisibility(View.VISIBLE);
//        }else{
//            viewHolder.nodata.setVisibility(View.INVISIBLE);
//        }
        notifyDataSetChanged();
    }

}
