package com.example.bellvantage.scadd.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bellvantage.scadd.DB.DbHelper;
import com.example.bellvantage.scadd.R;
import com.example.bellvantage.scadd.Utils.DateManager;
import com.example.bellvantage.scadd.Utils.SyncManager;
import com.example.bellvantage.scadd.swf.MerchantStock;
import com.example.bellvantage.scadd.swf.MerchantStockLineitems;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Bellvantage on 24/10/2017.
 */

public class MerchantStockViewAdapter extends RecyclerView.Adapter<MerchantStockViewAdapter.MyViewHolder> {


    Context context;
    ArrayList<MerchantStock> merchantStocks = new ArrayList<>();
    ArrayList<MerchantStockLineitems> merchantStockLineitemses = new ArrayList<>();
    public OnItemClickListener clickListener;
    int posi;
    String merchantName,iDate,ddate;

    public interface OnItemClickListener{
        void onItemClick(MerchantStock merchantStock,int position);
    }

    public MerchantStockViewAdapter(Context context,
                                    ArrayList<MerchantStock> merchantStocks,
                                    ArrayList<MerchantStockLineitems> merchantStockLineitemses, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.merchantStocks = merchantStocks;
        this.merchantStockLineitemses = merchantStockLineitemses;
        this.clickListener = onItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_for_viewmerchant_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        MerchantStock merchantStock = merchantStocks.get(position);
        holder.bind(merchantStock,clickListener,position);
        this.posi = position;

    }

    @Override
    public int getItemCount() {
        return merchantStocks.size();
    }



    public static String getDate(long milliSeconds, String dateFormat)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

     class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_merchant_stockid, tv_merchant_name, tv_insertdate;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_merchant_stockid = (TextView) itemView.findViewById(R.id.tv_mcv_msnumber);
            tv_merchant_name = (TextView) itemView.findViewById(R.id.tv_mcv_msname);
            tv_insertdate = (TextView) itemView.findViewById(R.id.tv_mcv_insertdate);
        }

        public void bind(final MerchantStock merchantStock, final OnItemClickListener listener, final int position){

            String s = merchantStock.getEnteredDate();
            System.out.println("s - "+s);
            //11/21/2017 4:01:56 PM
            String edate = (new DateManager()).changeDateFormat("yyyy-MM-dd",s);
            //String edate = (new DateManager()).changeDateFormat("MM/dd/yyyy",s);


//            int start = edate.indexOf("(");
//            int end = edate.indexOf(")");
//            iDate = edate.substring(start+1,end);
//            ddate = getDate(Long.parseLong(iDate), "dd/MM/yyyy");



            tv_merchant_stockid.setText("Dealer card id - "+merchantStock.getMerchentStockId());
            tv_merchant_name.setText(""+getMerchantName(merchantStock.getMerchantId()));
            tv_insertdate.setText("Inserted date - "+edate);
            //tv_insertdate.setText("Inserted date - "+merchantStock.getEntered_date());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(merchantStock,position);
                }
            });
        }

        public String getMerchantName(int mid){

            String s = "";
            String sql = "SELECT "+ DbHelper.TBL_MERCHANT_MERCHANT_NAME+" FROM "+DbHelper.TABLE_MERCHANT+" WHERE "+DbHelper.TBL_MERCHANT_MERCHANT_ID+" = "+mid;
            s = (new SyncManager(context)).getStringValueFromSQLite(sql,DbHelper.TBL_MERCHANT_MERCHANT_NAME);
            return s;
        }
    }

    public void update(ArrayList<MerchantStock> stocks){
            this.merchantStocks = stocks;
            notifyDataSetChanged();
    }

}
