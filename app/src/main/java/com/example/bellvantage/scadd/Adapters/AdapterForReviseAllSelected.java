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
import com.example.bellvantage.scadd.SOR__ListItemActivity_AllSelected;
import com.example.bellvantage.scadd.domains.ReviseSalesOrderList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Sachika on 09/08/2017.
 */

public class AdapterForReviseAllSelected extends RecyclerView.Adapter<AdapterForReviseAllSelected.MyViewHolder> {

    ArrayList<ReviseSalesOrderList> arrayList = new ArrayList<>();
    Context context;
    SOR__ListItemActivity_AllSelected sor__listItemActivity_All_selected;

    public AdapterForReviseAllSelected(ArrayList<ReviseSalesOrderList> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        this.sor__listItemActivity_All_selected = (SOR__ListItemActivity_AllSelected) context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_for_revise_sales_order,parent,false);
        return new MyViewHolder(view, sor__listItemActivity_All_selected);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final ReviseSalesOrderList reviseSalesOrderList =arrayList.get(position);


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

        if (reviseSalesOrderList.getIsCheckedItem()==true){
            holder.cbProducts.setChecked(true);
        }

        holder.llItemDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sor__listItemActivity_All_selected.getQty(reviseSalesOrderList);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (arrayList.size()!=0){
            return arrayList.size();
        }

        return 0;
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvProductName,tvProductPrice,tvExpireDate,tvStock,tvNetAmouont,tvQuantity,tvFreeQuantity,tvAmount,tvTotalQty,tvFreeAmount;
        LinearLayout llItemDetails;
        CheckBox cbProducts;
        SOR__ListItemActivity_AllSelected sor__listItemActivity_All_selected;
        public MyViewHolder(View itemView,SOR__ListItemActivity_AllSelected sor__listItemActivity_All_selected) {
            super(itemView);
            tvProductName = (TextView) itemView.findViewById(R.id.tvProductName);
            tvProductPrice = (TextView) itemView.findViewById(R.id.tvProductPrice);
            tvExpireDate = (TextView) itemView.findViewById(R.id.tvExpireDate);
            tvStock = (TextView) itemView.findViewById(R.id.tvStock);

            tvNetAmouont = (TextView) itemView.findViewById(R.id.tvNetAmouont);
            tvAmount = (TextView) itemView.findViewById(R.id.tvAmount);
            tvTotalQty= (TextView) itemView.findViewById(R.id.tvTotalQty);
            tvFreeAmount= (TextView) itemView.findViewById(R.id.tvFreeAmount);

            tvFreeQuantity = (TextView) itemView.findViewById(R.id.tvFreeQuantity);
            tvQuantity = (TextView) itemView.findViewById(R.id.tvQuantity);
            cbProducts = (CheckBox)itemView.findViewById(R.id.cbProducts);
            llItemDetails = (LinearLayout) itemView.findViewById(R.id.llItemDetails);

            this.sor__listItemActivity_All_selected = sor__listItemActivity_All_selected;

            /*llItemDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popUp();

                }
            });*/
            cbProducts.setOnClickListener(this);


        }


        @Override
        public void onClick(View v) {
            sor__listItemActivity_All_selected.prepare(v,getAdapterPosition());
        }
    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        //Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        //Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }


    public void update(ArrayList<ReviseSalesOrderList> updatedArray,ArrayList<ReviseSalesOrderList> allArray){
        arrayList = new ArrayList<>();
        arrayList = updatedArray;
        notifyDataSetChanged();
    }

    public void setFilter(ArrayList<ReviseSalesOrderList> arrayListF){
        arrayList = new ArrayList<>();
        arrayList.addAll(arrayListF);
        notifyDataSetChanged();
    }
}
