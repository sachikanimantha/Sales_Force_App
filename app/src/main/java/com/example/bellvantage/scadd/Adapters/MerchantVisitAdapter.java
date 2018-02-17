package com.example.bellvantage.scadd.Adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bellvantage.scadd.DB.DbHelper;
import com.example.bellvantage.scadd.R;
import com.example.bellvantage.scadd.Utils.SyncManager;
import com.example.bellvantage.scadd.swf.MerchantVisit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Bellvantage on 05/11/2017.
 */

public class MerchantVisitAdapter extends RecyclerView.Adapter<MerchantVisitAdapter.MyviewHolder> {


    ArrayList<MerchantVisit> merchantVisits = new ArrayList<>();
    Context context;

    public MerchantVisitAdapter(ArrayList<MerchantVisit> merchantVisits, Context context) {
        this.merchantVisits = merchantVisits;
        this.context = context;
    }

    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_for_merchant_visit,parent,false);
        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyviewHolder holder, int position) {

        holder.bind(merchantVisits.get(position));

    }

    @Override
    public int getItemCount() {
        return merchantVisits.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder{

        TextView tv_merchantname,tv_reason,tv_date,tv_deliverypath;

        public MyviewHolder(View itemView) {
            super(itemView);
            tv_merchantname = (TextView)itemView.findViewById(R.id.tv_merchant_name);
            tv_reason = (TextView)itemView.findViewById(R.id.tv_reason);
            tv_date = (TextView)itemView.findViewById(R.id.tv_entered_date);
            tv_deliverypath = (TextView)itemView.findViewById(R.id.tv_delivery_path);
        }

        public void bind(MerchantVisit merchantVisit){


            String sql1 = "SELECT "+ DbHelper.TBL_MERCHANT_MERCHANT_NAME+" FROM "
                    +DbHelper.TABLE_MERCHANT+" WHERE "+DbHelper.TBL_MERCHANT_MERCHANT_ID+ " = "+merchantVisit.getMerchant_id();
            String Merchantname = (new SyncManager(context)).getStringValueFromSQLite(sql1,DbHelper.TBL_MERCHANT_MERCHANT_NAME);


            String sql2 = "SELECT "+ DbHelper.COL_MVISIT_REASON_DESCRIPTION+" FROM "
                    +DbHelper.TABLE_MVISIT_REASON+" WHERE "+DbHelper.COL_MVISIT_REASON_REASON_ID+ " = "+merchantVisit.getReason_id();
            String reason = (new SyncManager(context)).getStringValueFromSQLite(sql2,DbHelper.COL_MVISIT_REASON_DESCRIPTION);


            String edate = merchantVisit.getEntered_date();

//            int start2 = edate.indexOf("(");
//            int end2 = edate.indexOf(")");
//            String date2 = edate.substring(start2+1,end2);
//            String edate1 = (new DateManager()).getDateAccordingToMilliSeconds(Long.parseLong(date2), "yyyy-MM-dd");

            String sql3 = "SELECT "+ DbHelper.TABLE_PATH_PATHNAME+" FROM "
                    +DbHelper.TABLE_PATH+" WHERE "+DbHelper.TABLE_PATH_DELIVERYPATHID+ " = "+merchantVisit.getDeliverypath_id();
            String path = (new SyncManager(context)).getStringValueFromSQLite(sql3,DbHelper.TABLE_PATH_PATHNAME);

            tv_merchantname.setText(""+Merchantname);

            if(reason.equalsIgnoreCase("No Data")){
                tv_reason.setText("Available");
            }else{
                tv_reason.setText(""+reason);
            }

            if(reason.equalsIgnoreCase("1")){
                tv_reason.setTextColor(ContextCompat.getColor(context, R.color.errorColor));
            }
            tv_date.setText(""+edate);
            tv_deliverypath.setText("Path : "+path);
        }
    }


    public static String getDate(long milliSeconds, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

}
