package com.example.bellvantage.scadd.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bellvantage.scadd.R;

/**
 * Created by Bellvantage on 17/11/2017.
 */

public class DailySummeryReportAdapter extends RecyclerView.Adapter<DailySummeryReportAdapter.MyViewHolderSummery> {


    Context context;
    int salesrepid;
    String date;
//    ArrayList<SalesOrder> salesOrders = new ArrayList<>();//according to today and salesrep id,here we having salesorders
//    ArrayList<MerchantVisit> merchantVisits = new ArrayList<>();//according to today and salesrep id,here we having visits


    public DailySummeryReportAdapter(Context context, int salesrepid, String date) {
        this.context = context;
        this.salesrepid = salesrepid;
        this.date = date;
    }

    @Override
    public MyViewHolderSummery onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_for_dailysummery_report,parent,false);
        return new MyViewHolderSummery(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolderSummery holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }




    public class MyViewHolderSummery extends RecyclerView.ViewHolder{


        TextView tv1,tv2,tv3,tv4,tv5,tv6,tv7;

        public MyViewHolderSummery(View itemView) {
            super(itemView);
            tv1 = (TextView)itemView.findViewById(R.id.tv_name_product_brand);
            tv2 = (TextView)itemView.findViewById(R.id.tv_cus_count_sales);
            tv3 = (TextView)itemView.findViewById(R.id.tv_cus_ava_stock);
            tv4 = (TextView)itemView.findViewById(R.id.tv_cus_sal_qty);
            tv5 = (TextView)itemView.findViewById(R.id.tv_cus_sal_amount);
            tv6 = (TextView)itemView.findViewById(R.id.tv_cus_count_visit);
            tv7 = (TextView)itemView.findViewById(R.id.tv_cus_stock_visit);

        }

        public void bind(){
            tv1.setText("");
        }
    }


}
