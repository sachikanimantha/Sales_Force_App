package com.example.bellvantage.scadd;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.bellvantage.scadd.Adapters.ReviceSalesOrdersAdapter;
import com.example.bellvantage.scadd.Utils.SessionManager;
import com.example.bellvantage.scadd.Utils.SyncManager;
import com.example.bellvantage.scadd.swf.LoginUser;
import com.example.bellvantage.scadd.swf.MerchantDetails;
import com.example.bellvantage.scadd.swf.OrderList;
import com.example.bellvantage.scadd.swf.SalesRep;
import com.google.gson.Gson;

import java.util.ArrayList;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class SO_ReviceSalesOrderavAtivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView tvMerchantName,tvSalesRep,tvNoResults,tvSearch;
    SyncManager syncManager;

    //Spinners
    ArrayList<String> merchantList=new ArrayList<>();
    ArrayList<String> salesRepList=new ArrayList<>();
    ArrayList<MerchantDetails> merchantArrayList = new ArrayList<>();
    ArrayList<SalesRep> salesRepArrayList = new ArrayList<>();
    SpinnerDialog spdMerchant,spdSalesRep;
    int merchantID,salesRepId ;

    LinearLayout llInvoiceDetails;

    //RecyclerView
    RecyclerView rvSalesOrders;
    ArrayList<OrderList> orderListArrayList = new ArrayList<>();

    ProgressDialog dialog;

    String salesRepName,userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_so__revice_sales_orderav_ativity);
        toolbar = (Toolbar) findViewById(R.id.tb_main);
        toolbar.setTitle("Revise Sales Orders");

        initalizeViews();

        Gson gson = new Gson();
        String getJson = SessionManager.pref.getString("user", "");
        LoginUser prefUser = gson.fromJson(getJson, LoginUser.class);
        salesRepName =prefUser.getName();
        userType = prefUser.getUserType();

        tvNoResults.setVisibility(View.GONE);
        llInvoiceDetails.setVisibility(View.GONE);

        if (userType.equals("D") || userType.equals("d")){

            tvSalesRep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    spdSalesRep.showSpinerDialog();
                }
            });

        }else{
            tvSalesRep.setText(salesRepName);
        }

        tvMerchantName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spdMerchant.showSpinerDialog();
            }
        });

        syncManager = new SyncManager(getApplicationContext());
        //syncManager.genarateReviseOrder();
        merchantArrayList = syncManager.getMerchantList();
        //salesRepArrayList = syncManager.getSalesRepList();
        loadMerchant(merchantArrayList);
        loadSalesRep(salesRepArrayList);


        orderListArrayList = syncManager.getSalesOrderListBySalesStatus(1);
        loadRecyclerView(orderListArrayList);

        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String merchantName = tvMerchantName.getText().toString();

                if(merchantName.equalsIgnoreCase("Select or Search Merchant")){
                    //Toast.makeText(getApplicationContext(),"Please select a merchant", Toast.LENGTH_LONG).show();
                    displayStatusMessage("Please select a merchant",2);
                    return;
                }

                if (android.os.Build.VERSION.SDK_INT >= 11) {
                    dialog = new ProgressDialog(SO_ReviceSalesOrderavAtivity.this, ProgressDialog.THEME_TRADITIONAL);
                }else{
                    dialog = new ProgressDialog(SO_ReviceSalesOrderavAtivity.this);
                }
                dialog.setMessage(Html.fromHtml("<b>"+"Loading..."+"</b>"));
                dialog.setIndeterminate(true);
                dialog.setCancelable(false);
                dialog.show();

                tvNoResults.setVisibility(View.GONE);
                llInvoiceDetails.setVisibility(View.GONE);


                SyncManager syncManager = new SyncManager(getApplicationContext());
                System.out.println("Revise Merchant ID " +merchantID);
                orderListArrayList = syncManager.getSalesOrderListByMerchant(merchantID);
                //orderListArrayList = syncManager.getSalesOrderListBySalesStatus(1);
                loadRecyclerView(orderListArrayList);

                dialog.dismiss();
            }
        });

    }

    private void loadRecyclerView(ArrayList<OrderList> orderListArrayList) {
        if (orderListArrayList.size()>0){
            llInvoiceDetails.setVisibility(View.VISIBLE);
            try {
                YoYo.with(Techniques.FlipInX)
                        .duration(700)
                        .playOn(llInvoiceDetails);
            }catch (Exception e){

            }

            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                    LinearLayoutManager.VERTICAL,
                    true
            );

            rvSalesOrders.setLayoutManager(layoutManager);
            rvSalesOrders.setHasFixedSize(true);

            ReviceSalesOrdersAdapter reviceSalesOrdersAdapter = new ReviceSalesOrdersAdapter(getApplicationContext(),orderListArrayList);
            rvSalesOrders.setAdapter(reviceSalesOrdersAdapter);

        }else{
            tvNoResults.setVisibility(View.VISIBLE);

            try {
                YoYo.with(Techniques.BounceInRight)
                        .duration(700)
                        .playOn(tvNoResults);
            }catch (Exception e){

            }
        }



    }

    private void loadMerchant(final ArrayList<MerchantDetails> merchantArrayList) {
        merchantList.clear();
        for (int i = 0; i < merchantArrayList.size(); i++) {
            merchantList.add(merchantArrayList.get(i).getMerchantName()+ " - " + merchantArrayList.get(i).getMerchantId());
        }
        spdMerchant=new SpinnerDialog(SO_ReviceSalesOrderavAtivity.this,merchantList,"Select or Search Merchant",R.style.DialogAnimations_SmileWindow);

        spdMerchant.bindOnSpinerListener(new OnSpinerItemClick()
        {
            @Override
            public void onClick(String item, int position)
            {

                tvMerchantName.setText(item);

                for (int  i = 0 ;i<merchantArrayList.size();i++){

                    String merchantName = merchantArrayList.get(i).getMerchantName()+ " - " + merchantArrayList.get(i).getMerchantId();
                    if (merchantName.equals(item)){
                        merchantID = merchantArrayList.get(i).getMerchantId();
                    }
                }

            }
        });
    }

    private void loadSalesRep(final ArrayList<SalesRep> salesRepArrayList) {
        salesRepList.clear();
        for (int i = 0; i < salesRepArrayList.size(); i++) {
            salesRepList.add(salesRepArrayList.get(i).getSalesRepName());
        }
        spdSalesRep=new SpinnerDialog(SO_ReviceSalesOrderavAtivity.this,salesRepList,"Select or Search SalesRep",R.style.DialogAnimations_SmileWindow);

        spdSalesRep.bindOnSpinerListener(new OnSpinerItemClick()
        {
            @Override
            public void onClick(String item, int position)
            {

                tvSalesRep.setText(item);

                for (int  i = 0 ;i<salesRepArrayList.size();i++){

                    if (salesRepArrayList.get(i).getSalesRepName().equals(item)){
                        salesRepId = salesRepArrayList.get(i).getSalesRepId();
                    }
                }

            }
        });
    }

    private void initalizeViews() {

        //TextViews
        tvMerchantName = (TextView) findViewById(R.id.tvMerchantSelect);
        tvSalesRep = (TextView) findViewById(R.id.tvSalesRep);
        tvNoResults = (TextView) findViewById(R.id.tvEmptyResult);
        tvSearch = (TextView) findViewById(R.id.tvSearch);


        //RecyclerView
        rvSalesOrders = (RecyclerView) findViewById(R.id.rvSalesOrders);

        //Layouts
        llInvoiceDetails = (LinearLayout) findViewById(R.id.llInvoiceDetails);

        //Buttons

    }



    private void displayStatusMessage(String s, int colorValue) {

        AlertDialog.Builder builder = null;
        View view = null;
        TextView tvOk, tvMessage;
        ImageView imageView;

        int defaultColor = R.color.textGray;
        int successColor = R.color.successColor; // 1
        int errorColor = R.color.errorColor; // 2
        int warningColor = R.color.warningColor; // 3

        int success = R.mipmap.ic_success;
        int error_image = R.mipmap.ic_error;
        int warning_image = R.drawable.ic_warning;
        //1,2,3

        int color = defaultColor;
        int img = success;
        if (colorValue == 1) {
            color = successColor;
            img = success;

        } else if (colorValue == 2) {
            color = errorColor;
            img = error_image;

        } else if (colorValue == 3) {
            color = warningColor;
            img = warning_image;
        }

        builder = new AlertDialog.Builder(SO_ReviceSalesOrderavAtivity.this);
        view = getLayoutInflater().inflate(R.layout.layout_for_custom_message, null);

        tvOk = (TextView) view.findViewById(R.id.tvOk);
        tvMessage = (TextView) view.findViewById(R.id.tvMessage);
        imageView = (ImageView)view.findViewById(R.id.iv_status_k);

        tvMessage.setTextColor(getResources().getColor(color));
        tvMessage.setText(s);
        imageView.setImageResource(img);

        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }
}
