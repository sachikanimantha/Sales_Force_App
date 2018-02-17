package com.example.bellvantage.scadd.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bellvantage.scadd.R;
import com.example.bellvantage.scadd.SOR_ListItemActivity;
import com.example.bellvantage.scadd.domains.ReviseSalesOrderList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Sachika on 31/07/2017.
 */

public class ReviseSalesOrderProductListAdapter extends RecyclerView.Adapter<ReviseSalesOrderProductListAdapter.MyViewHolder> {

    Context context;
    ArrayList<ReviseSalesOrderList> arrayList  = new ArrayList();
    ArrayList<ReviseSalesOrderList> selectedArrayList  = new ArrayList();
    ArrayList<ReviseSalesOrderList> allArrayList  = new ArrayList();
    SOR_ListItemActivity sor_listItemActivity;
    static int method;


    public ReviseSalesOrderProductListAdapter(Context context, ArrayList<ReviseSalesOrderList> arrayList,ArrayList<ReviseSalesOrderList> soArraylist,int method) {
        if (method==1){
            this.arrayList = arrayList;
        }else{
            this.arrayList = soArraylist;
        }
        this.context = context;
        this.selectedArrayList = soArraylist;
        this.allArrayList = arrayList;
        this.method = method;
        this.sor_listItemActivity = (SOR_ListItemActivity)context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_for_revise_sales_order,parent,false);
        return new MyViewHolder(view,sor_listItemActivity);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ReviseSalesOrderList reviseSalesOrderList =arrayList.get(position);
        String date = reviseSalesOrderList.getExpiryDate();
        int start = date.indexOf("(");
        int end = date.indexOf(")");
        String iDate = date.substring(start+1,end);
        holder.tvExpireDate.setText(getDate(Long.parseLong(iDate), "dd/MM/yyyy"));
        holder.tvProductName.setText(reviseSalesOrderList.getProductName());
        holder.tvStock.setText(reviseSalesOrderList.getStock()+"");
        holder.tvProductPrice.setText(String.format("Rs. %.2f",reviseSalesOrderList.getUnitSellingPrice()));
        holder.tvFreeQuantity.setText(reviseSalesOrderList.getFreeQuantity()+"");
        holder.tvQuantity.setText(reviseSalesOrderList.getQuantity()+"");
        holder.tvAmouont.setText(String.format("Rs. %.2f",reviseSalesOrderList.getAmount()));




      if(method == 0){
            holder.cbProducts.setChecked(true);
      }

       if (method==1){
           if (reviseSalesOrderList.getIsCheckedItem()==true){
               holder.cbProducts.setChecked(true);
           }
        }


    }

    @Override
    public int getItemCount() {
       if (arrayList.size() != 0 ){
           return arrayList.size();
       }
       return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvProductName,tvProductPrice,tvExpireDate,tvStock,tvAmouont,tvQuantity,tvFreeQuantity;
        LinearLayout llItemDetails;
        CheckBox cbProducts;
        SOR_ListItemActivity sor_listItemActivity;
        public MyViewHolder(View itemView,SOR_ListItemActivity sor_listItemActivity) {
            super(itemView);
            this.sor_listItemActivity = sor_listItemActivity;
            tvProductName = (TextView) itemView.findViewById(R.id.tvProductName);
            tvProductPrice = (TextView) itemView.findViewById(R.id.tvProductPrice);
            tvExpireDate = (TextView) itemView.findViewById(R.id.tvExpireDate);
            tvStock = (TextView) itemView.findViewById(R.id.tvStock);

            tvAmouont = (TextView) itemView.findViewById(R.id.tvAmouont);
            tvFreeQuantity = (TextView) itemView.findViewById(R.id.tvFreeQuantity);
            tvQuantity = (TextView) itemView.findViewById(R.id.tvQuantity);
            cbProducts = (CheckBox)itemView.findViewById(R.id.cbProducts);

            llItemDetails = (LinearLayout) itemView.findViewById(R.id.llItemDetails);
            llItemDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popUp();

                }
            });

            cbProducts.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            sor_listItemActivity.prepare(v,getAdapterPosition(),method);
        }

        public  void popUp(){
            sor_listItemActivity.getQty(getAdapterPosition(),method);
            notifyDataSetChanged();
        }
    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }


    public void setFilter(ArrayList<ReviseSalesOrderList> arrayListF){
        arrayList = new ArrayList<>();
        arrayList.addAll(arrayListF);
        notifyDataSetChanged();
    }

}
