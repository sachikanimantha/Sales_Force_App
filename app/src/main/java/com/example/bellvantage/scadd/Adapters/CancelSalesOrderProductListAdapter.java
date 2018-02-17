package com.example.bellvantage.scadd.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bellvantage.scadd.ActivityCancelSalesOrderContinue;
import com.example.bellvantage.scadd.R;
import com.example.bellvantage.scadd.domains.ReviseSalesOrderList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Sachika on 09/08/2017.
 */

public class CancelSalesOrderProductListAdapter extends RecyclerView.Adapter<CancelSalesOrderProductListAdapter.MyViewHolder> {

    Context context;
    ArrayList<ReviseSalesOrderList> arrayList  = new ArrayList();
    ArrayList<ReviseSalesOrderList> selectedArrayList  = new ArrayList();
    ArrayList<ReviseSalesOrderList> allArrayList  = new ArrayList();
    ActivityCancelSalesOrderContinue activityCancelSalesOrderContinue;
    static int method;


    public CancelSalesOrderProductListAdapter(Context context, ArrayList<ReviseSalesOrderList> arrayList,ArrayList<ReviseSalesOrderList> soArraylist,int method) {
        if (method==1){
            this.arrayList = arrayList;
        }else{
            this.arrayList = soArraylist;
        }
        this.context = context;
        this.selectedArrayList = soArraylist;
        this.allArrayList = arrayList;
        this.method = method;
        this.activityCancelSalesOrderContinue = (ActivityCancelSalesOrderContinue)context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_for_revise_sales_order,parent,false);
        return new MyViewHolder(view, activityCancelSalesOrderContinue);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ReviseSalesOrderList reviseSalesOrderList =arrayList.get(position);

        double amount = reviseSalesOrderList.getUnitSellingPrice()*reviseSalesOrderList.getQuantity();
        double freeAmount = reviseSalesOrderList.getUnitSellingPrice()*reviseSalesOrderList.getFreeQuantity();
        double totalAmount = amount+freeAmount;
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
        holder.tvTotalQty.setText(reviseSalesOrderList.getQuantity()+reviseSalesOrderList.getFreeQuantity()+"");
        holder.tvAmount.setText(String.format("Rs. %.2f",amount));
        holder.tvFreeAmount.setText(String.format("Rs. %.2f",freeAmount));
        holder.tvNetAmouont.setText(String.format("Rs. %.2f",totalAmount));

        holder.cbProducts.setEnabled(false);

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

        TextView tvProductName,tvProductPrice,tvExpireDate,tvStock,tvNetAmouont,tvQuantity,tvFreeQuantity,tvTotalQty,tvFreeAmount,tvAmount;
        LinearLayout llItemDetails;
        CheckBox cbProducts;
        ActivityCancelSalesOrderContinue sor_listItemActivity;
        public MyViewHolder(View itemView,ActivityCancelSalesOrderContinue activityCancelSalesOrderContinue) {
            super(itemView);
            this.sor_listItemActivity = activityCancelSalesOrderContinue;
            tvProductName = (TextView) itemView.findViewById(R.id.tvProductName);
            tvProductPrice = (TextView) itemView.findViewById(R.id.tvProductPrice);
            tvExpireDate = (TextView) itemView.findViewById(R.id.tvExpireDate);
            tvStock = (TextView) itemView.findViewById(R.id.tvStock);
            tvTotalQty = (TextView) itemView.findViewById(R.id.tvTotalQty);
            tvFreeAmount = (TextView) itemView.findViewById(R.id.tvFreeAmount);
            tvAmount = (TextView) itemView.findViewById(R.id.tvAmount);

            tvNetAmouont = (TextView) itemView.findViewById(R.id.tvNetAmouont);
            tvFreeQuantity = (TextView) itemView.findViewById(R.id.tvFreeQuantity);
            tvQuantity = (TextView) itemView.findViewById(R.id.tvQuantity);
            cbProducts = (CheckBox)itemView.findViewById(R.id.cbProducts);

            llItemDetails = (LinearLayout) itemView.findViewById(R.id.llItemDetails);
            llItemDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //popUp();

                }
            });

           // cbProducts.setOnClickListener(this);

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
