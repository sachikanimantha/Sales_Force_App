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

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.bellvantage.scadd.AdminActivity;
import com.example.bellvantage.scadd.AttendanceActivity;
import com.example.bellvantage.scadd.DB.DbHelper;
import com.example.bellvantage.scadd.EvaluationActivity;
import com.example.bellvantage.scadd.ImageUploadPHPActivity;
import com.example.bellvantage.scadd.MC.MyMenu;
import com.example.bellvantage.scadd.MerchantActivity;
import com.example.bellvantage.scadd.MileageActivty;
import com.example.bellvantage.scadd.MyOutletListActivity;
import com.example.bellvantage.scadd.OrderRequestActivity;
import com.example.bellvantage.scadd.PrimarySalesOrderActivity;
import com.example.bellvantage.scadd.PrintReturnNoteActivity;
import com.example.bellvantage.scadd.R;
import com.example.bellvantage.scadd.ReportsActivity;
import com.example.bellvantage.scadd.ReturnActivty;
import com.example.bellvantage.scadd.ReturnNoteActivity;
import com.example.bellvantage.scadd.SalesOrderActivity;
import com.example.bellvantage.scadd.SalesOrderOfflineActivity;
import com.example.bellvantage.scadd.SyncSalesOrdersActivity;
import com.example.bellvantage.scadd.TargetAchievementActivity;
import com.example.bellvantage.scadd.Utils.NetworkConnection;
import com.example.bellvantage.scadd.Utils.SyncManager;
import com.example.bellvantage.scadd.ViewRedyStock;
import com.example.bellvantage.scadd.ViewSalesrepStock;
import com.example.bellvantage.scadd.WebAppActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Sachika on 17/05/2017.
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuAdapterViewHolder> {

    Context context;
    int SalesrepId;
    ArrayList<MyMenu> arrayList;
    String SalesrepType;

    public MenuAdapter(Context context, ArrayList<MyMenu> arrayList, int Salesrepid) {
        this.context = context;
        this.arrayList = arrayList;
        this.SalesrepId = Salesrepid;
    }

    @Override
    public MenuAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_for_menu, parent, false);
        return new MenuAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MenuAdapterViewHolder holder, final int position) {

        final String menuLabel = arrayList.get(position).getMenuName();
        int img = arrayList.get(position).getMenuImage();

        holder.tvMenuLabel.setText(menuLabel);

        //holder.tvMenuLabel.animate().setDuration(700).translationX(20);
        Picasso.with(context)
                .load(img)
                .error(android.R.drawable.stat_notify_error)
                .into(holder.ivMenuImage);
        checkSalesrepType();

        holder.ivMenuImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (new NetworkConnection().checkGPSConnection(context)) {
                    switch (menuLabel) {

                        case "Primary Sales Order":
                            Intent oIntent = new Intent(context, PrimarySalesOrderActivity.class);
                            oIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(oIntent);
                            break;

                        case "Sales Orders":
                            Intent sIntent = new Intent(context, SalesOrderActivity.class);
                            sIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(sIntent);
                            break;

                        case "camera":
                            Intent cameraIntent = new Intent(context, ImageUploadPHPActivity.class);
                            cameraIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(cameraIntent);
                            break;


                        case "Confirm Sales Order":
                            Intent OrderRequestActivity = new Intent(context, com.example.bellvantage.scadd.OrderRequestActivity.class);
                            OrderRequestActivity.putExtra("title", arrayList.get(position).getMenuName());
                            OrderRequestActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(OrderRequestActivity);
                            break;

                        case "Daily Plan":
                            Intent dpi = new Intent(context, com.example.bellvantage.scadd.DailyPlanActivity.class);
                            dpi.putExtra("title",arrayList.get(position).getMenuName());
                            dpi.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(dpi);
                            break;

                        case "Invoice Print":
                            Intent in = new Intent(context, OrderRequestActivity.class);
                            in.putExtra("title", arrayList.get(position).getMenuName());
                            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(in);
                            break;

                        case "Return-Note Print":
                            Intent printReturnNoteActivity = new Intent(context, PrintReturnNoteActivity.class);
                            printReturnNoteActivity.putExtra("title", arrayList.get(position).getMenuName());
                            printReturnNoteActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(printReturnNoteActivity);
                            break;

                        case "Return Note Print":
                            Intent rnp = new Intent(context, ReturnNoteActivity.class);
                            rnp.putExtra("title", arrayList.get(position).getMenuName());
                            rnp.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(rnp);
                            break;


                        case "Admin":
                            Intent intent = new Intent(context,AdminActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                            break;

                        case "Attendance":
                            Intent intentAttendance = new Intent(context,AttendanceActivity.class);
                            intentAttendance.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intentAttendance);
                            break;

                        case "Customer Registration":
                            Intent merchantIntent = new Intent(context.getApplicationContext(), MerchantActivity.class);
                            merchantIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(merchantIntent);
                            break;

                        case "Sales Orders Offline":
                            Intent salesOrderOfflineIntent = new Intent(context.getApplicationContext(), SalesOrderOfflineActivity.class);
                            salesOrderOfflineIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(salesOrderOfflineIntent);
                            break;

                        case "View Sales Rep Stock":
                            Intent viewstock = new Intent(context.getApplicationContext(), ViewSalesrepStock.class);
                            viewstock.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(viewstock);
                            break;


                        case "Sync Sales orders":
                            Intent syncSalesOrders = new Intent(context.getApplicationContext(), SyncSalesOrdersActivity.class);
                            syncSalesOrders.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(syncSalesOrders);
                            break;

                        case "My Outlet List":
                            Intent moli = new Intent(context.getApplicationContext(), MyOutletListActivity.class);
                            moli.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(moli);
                            break;

                    case "View Vehicle Stock":
                        if(SalesrepType.equalsIgnoreCase("P")){
                            //(new MainActivity()).displayStatusMessage("You Do not have a Vehicle Inventory",3,context.getApplicationContext());
                            //mainActivity.displayStatusMessage("You Do not have a Vehicle Inventory",3);
                            Toast.makeText(context, "You Do not have a Vehicle Inventory", Toast.LENGTH_SHORT).show();
                        }else if(SalesrepType.equalsIgnoreCase("R")){
                            Intent vvs = new Intent(context.getApplicationContext(), ViewRedyStock.class);
                            vvs.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(vvs);
                        }
                        break;

                        case "Reports":
                            Intent reportsActivity = new Intent(context, ReportsActivity.class);
                            reportsActivity.putExtra("title", arrayList.get(position).getMenuName());
                            reportsActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(reportsActivity);
                            break;

                        case "Returns":
                            Intent returnActivity = new Intent(context, ReturnActivty.class);
                            returnActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(returnActivity);
                            break;

                        case "Evaluation":
                            Intent evaluationActivity = new Intent(context,EvaluationActivity.class);
                            evaluationActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(evaluationActivity);
                            break;

                        case "Target Achievement":
                            Intent targetAchievement = new Intent(context,TargetAchievementActivity.class);
                            targetAchievement.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(targetAchievement);
                            break;

                        case "Mileage":
                            Intent mileageActivity = new Intent(context,MileageActivty.class);
                            mileageActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(mileageActivity);
                            break;
                        default:
                            if (!checkNetworkConnection()) {
                                Toast.makeText(context, "Please Check your Network Connection...", Toast.LENGTH_LONG).show();
                                return;
                            }

                            Intent defaultIntent = new Intent(context, WebAppActivity.class);
                            defaultIntent.putExtra("title", arrayList.get(position).getMenuName());
                            defaultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(defaultIntent);
                    }
                } else {
                    Toast.makeText(context, "Please Check GPS", Toast.LENGTH_SHORT).show();
                    return;
                }


            }
        });


        try {
            YoYo.with(Techniques.BounceInRight)
                    .duration(700)
                    .playOn(holder.ivMenuImage);
        } catch (Exception e) {

        }
        try {
            YoYo.with(Techniques.RollIn)
                    .duration(700)
                    .playOn(holder.tvMenuLabel);
        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        if (arrayList != null) {
            return arrayList.size();
        }
        return 0;
    }

    public static class MenuAdapterViewHolder extends RecyclerView.ViewHolder {

        ImageView ivMenuImage;
        TextView tvMenuLabel;

        public MenuAdapterViewHolder(View itemView) {
            super(itemView);

            ivMenuImage = (ImageView) itemView.findViewById(R.id.ivMenuImage);
            tvMenuLabel = (TextView) itemView.findViewById(R.id.tvMenulabel);

        }
    }

    public boolean checkNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public void checkSalesrepType() {

        String sql = "SELECT " + DbHelper.COL_SALES_REP_SalesRepType +
                " FROM " + DbHelper.TABLE_SALES_REP + " WHERE " + DbHelper.COL_SALES_REP_SalesRepId + " = " + SalesrepId;
        System.out.println("sql - " + sql);
        System.out.println("SalesrepId -" + SalesrepId);
        SalesrepType = (new SyncManager(context)).getStringValueFromSQLite(sql, DbHelper.COL_SALES_REP_SalesRepType);

        System.out.println("SalesrepType -" + SalesrepType);
    }

}
