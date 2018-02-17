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

import com.example.bellvantage.scadd.MC.MyMenu;
import com.example.bellvantage.scadd.R;
import com.example.bellvantage.scadd.ViewMerchantCard;
import com.example.bellvantage.scadd.ViewMerchantVisit;
import com.example.bellvantage.scadd.WebAppActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Sachika on 19/06/2017.
 */

public class PrimarySalesOrderMenuAdapter extends RecyclerView.Adapter<PrimarySalesOrderMenuAdapter.MenuViewHolder> {

    Context context;
    ArrayList<MyMenu> arrayList;

    public PrimarySalesOrderMenuAdapter(Context context, ArrayList<MyMenu> arrayList) {
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

//                    case "Confirm Sales Orders":
//
//                        Intent csoIntent = new Intent(context,ConfirmSalesOrder.class);
//                        csoIntent.putExtra("title",menuLabel);
//                        csoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        context.startActivity(csoIntent);
//                        break;

                    //"Create Primary Sales Orders", "Revise Primary Sale Orders",  "Cancel Primary Sale Orders","Primary Sale Orders Approved Report"

                    case "Merchant Card":
                        Intent mcIntent = new Intent(context,ViewMerchantCard.class);
                        mcIntent.putExtra("title",menuLabel);
                        mcIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(mcIntent);
                        break;

                    case "Merchant Visit":
                        Intent intent = new Intent(context,ViewMerchantVisit.class);
                        intent.putExtra("title",menuLabel);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
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
