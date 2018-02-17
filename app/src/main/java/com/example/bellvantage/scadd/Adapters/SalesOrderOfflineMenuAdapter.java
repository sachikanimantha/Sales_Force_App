package com.example.bellvantage.scadd.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bellvantage.scadd.ActivityCancelSalesOrders;
import com.example.bellvantage.scadd.CreateSalesOrder_PathMerchantSelect;
import com.example.bellvantage.scadd.MC.MyMenu;
import com.example.bellvantage.scadd.R;
import com.example.bellvantage.scadd.SO_ReviceSalesOrderavAtivity;
import com.example.bellvantage.scadd.SalesOrderToInvoice;
import com.example.bellvantage.scadd.WebAppActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Sachika on 19/06/2017.
 */

public class SalesOrderOfflineMenuAdapter extends RecyclerView.Adapter<SalesOrderOfflineMenuAdapter.MenuViewHolder> {

    Context context;
    ArrayList<MyMenu> arrayList;

    public SalesOrderOfflineMenuAdapter(Context context, ArrayList<MyMenu> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_for_menu,parent,false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MenuViewHolder holder, int position) {
        final String menuLabel = arrayList.get(position).getMenuName();
        int img = arrayList.get(position).getMenuImage();

        holder.tvMenuLabel.setText(menuLabel);

        //holder.tvMenuLabel.animate().setDuration(700).translationX(20);
        Picasso.with(context)
                .load(img)
                .error(android.R.drawable.stat_notify_error)
                .into(holder.ivMenuImage);

        holder.ivMenuImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(menuLabel){

                    case "Create Sales Orders":

                        Intent csooIntent = new Intent(context,CreateSalesOrder_PathMerchantSelect.class);
                        csooIntent.putExtra("title",menuLabel);
                        csooIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(csooIntent);
                        break;

                    case "Confirm Sale Orders":

                        Intent confIntent = new Intent(context, ActivityCancelSalesOrders.class);
                        confIntent.putExtra("id",2);
                        confIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(confIntent);
                        break;


                    case "Revise Sale Orders":
                        Intent revicIntent = new Intent(context, SO_ReviceSalesOrderavAtivity.class);
                        revicIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(revicIntent);
                        break;

                    case "Cancel Orders":
                        Intent cancelIntent = new Intent(context, ActivityCancelSalesOrders.class);
                        cancelIntent.putExtra("id",1);
                        cancelIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(cancelIntent);
                        break;


                    case "Sales Order to Invoice":
                        Intent invoiceIntent = new Intent(context, SalesOrderToInvoice.class);
                        invoiceIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(invoiceIntent);
                        break;

                    default :

                        Intent defaultIntent = new Intent(context,WebAppActivity.class);
                        defaultIntent.putExtra("title",menuLabel);
                        defaultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(defaultIntent);
                        break;
                }


                if(!checkNetworkConnection()){
                    Toast.makeText(context, "Please Check your Network Connection...", Toast.LENGTH_LONG).show();
                    return;
                }




            }
        });

    }

    @Override
    public int getItemCount() {
        if (arrayList != null){
            return  arrayList.size();
        }
        return 0;
    }

    public static class MenuViewHolder extends  RecyclerView.ViewHolder{

        ImageView ivMenuImage;
        TextView tvMenuLabel;

        public MenuViewHolder(View itemView) {
            super(itemView);

            ivMenuImage = (ImageView) itemView.findViewById(R.id.ivMenuImage);
            tvMenuLabel = (TextView) itemView.findViewById(R.id.tvMenulabel);
        }
    }

    public boolean checkNetworkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
