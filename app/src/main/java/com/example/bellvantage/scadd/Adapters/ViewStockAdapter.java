package com.example.bellvantage.scadd.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bellvantage.scadd.R;
import com.example.bellvantage.scadd.swf.ProductList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Bellvantage on 11/10/2017.
 */

public class ViewStockAdapter extends RecyclerView.Adapter<ViewStockAdapter.MyViewHolder> {

    ArrayList<ProductList> productListArrayList = new ArrayList<>();
    Context context;


    public ViewStockAdapter(ArrayList<ProductList> productListArrayList, Context context) {
        this.productListArrayList = productListArrayList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_for_viewstock_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        ProductList productList = productListArrayList.get(position);
        holder.bind(productList);

    }

    @Override
    public int getItemCount() {
        return productListArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_name,tv_stock,tv_unitprice,tv_expiredate;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_name = (TextView)itemView.findViewById(R.id.tv_row_itemname);
            tv_stock = (TextView)itemView.findViewById(R.id.tv_row_stock);
            tv_unitprice = (TextView)itemView.findViewById(R.id.tv_row_unitprice);
            tv_expiredate = (TextView)itemView.findViewById(R.id.tv_row_expiredate);
        }

        public void bind(ProductList productList){


            String iDate,ddate;
            String edate = productList.getExpireDate();
            int start = edate.indexOf("(");
            int end = edate.indexOf(")");
            iDate = edate.substring(start+1,end);
            ddate = getDate(Long.parseLong(iDate), "dd/MM/yyyy");

            tv_name.setText(productList.getName());
            tv_expiredate.setText(ddate);
            tv_stock.setText(""+productList.getStock());
            tv_unitprice.setText(""+String.format("%.2f", productList.getUnitPrice()));
        }
    }


    public static String getDate(long milliSeconds, String dateFormat)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

}
