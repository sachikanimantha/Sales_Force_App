package com.example.bellvantage.scadd;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.bellvantage.scadd.Adapters.CancelSalesOrdersAdapter;
import com.example.bellvantage.scadd.Utils.DateManager;
import com.example.bellvantage.scadd.Utils.SessionManager;
import com.example.bellvantage.scadd.Utils.SyncManager;
import com.example.bellvantage.scadd.swf.LoginUser;
import com.example.bellvantage.scadd.swf.MerchantDetails;
import com.example.bellvantage.scadd.swf.OrderList;
import com.example.bellvantage.scadd.swf.SalesRep;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class ActivityCancelSalesOrders extends AppCompatActivity {

    Toolbar toolbar;
    TextView tvMerchantName,tvSalesRep,tvNoResults,tvSearchButoon,tvDateFrom,tvDateTo;



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

    //date
    DatePicker datePicker;
    Calendar calendar;
    int year, month, day,id,mHour, mMinute;

    DateManager dateManager;
    SyncManager syncManager;
    long dateFrom, dateTo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_so__cacel_sales_orders);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tb_main);
        toolbar.setTitle("Cancel Sales Orders ");

        dateManager = new DateManager();
        syncManager = new SyncManager(getApplicationContext());
        initalizeViews();
        if (getIntent().getSerializableExtra("id")!=null){
            id = (int) getIntent().getSerializableExtra("id");
        }


        if(id==1){
            toolbar.setTitle("Cancel Sales Order");
        }

        if(id==2){
            toolbar.setTitle("Confirm Sales Order");
        }

        //final SpotsDialog waitingDialog = new SpotsDialog(ActivityCancelSalesOrders.this, R.style.Custom);
       // waitingDialog.show();
        // waitingDialog.dismiss();
        orderListArrayList = syncManager.getSalesOrderListBySalesStatus(1);

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
       // syncManager.genarateReviseOrder();
        merchantArrayList = syncManager.getMerchantList();
        //salesRepArrayList = syncManager.getSalesRepList();
        loadMerchant(merchantArrayList);
        loadSalesRep(salesRepArrayList);

        tvSearchButoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String merchantName = tvMerchantName.getText().toString();
                String dateTos = tvDateTo.getText().toString();
                String dateFroms = tvDateFrom.getText().toString();

                if(merchantName.equalsIgnoreCase("Select or Search Merchant")){
                    //Toast.makeText(getApplicationContext(),"Please select a merchant", Toast.LENGTH_LONG).show();
                    displayStatusMessage("Please select a merchant",2);
                    return;
                }

                if(dateTos.equalsIgnoreCase("Select a date") || dateFroms.equalsIgnoreCase("Select a date")){
                    //Toast.makeText(getApplicationContext(),"Please select a date", Toast.LENGTH_LONG).show();
                    displayStatusMessage("Please select a date",2);
                    return;
                }

                if (android.os.Build.VERSION.SDK_INT >= 11) {
                    dialog = new ProgressDialog(ActivityCancelSalesOrders.this, ProgressDialog.THEME_TRADITIONAL);
                }else{
                    dialog = new ProgressDialog(ActivityCancelSalesOrders.this);
                }
                dialog.setMessage(Html.fromHtml("<b>"+"Loading..."+"</b>"));
                dialog.setIndeterminate(true);
                dialog.setCancelable(false);
                dialog.show();

                System.out.println("Confirm Merchant ID " +merchantID);

                orderListArrayList = syncManager.getSalesOrderListByMerchantAndDate(merchantID,dateFrom,dateTo,1);

                loadRecyclerView(orderListArrayList);
                dialog.dismiss();
            }
        });
        loadRecyclerView(orderListArrayList);

        tvDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(67);
            }
        });

        tvDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(63);
            }
        });

    }

    private void loadRecyclerView(ArrayList<OrderList> orderListArrayList) {
        System.out.println("==================Recycler view=================");
        tvNoResults.setVisibility(View.GONE);
        llInvoiceDetails.setVisibility(View.GONE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL,
                true
        );

        rvSalesOrders.setLayoutManager(layoutManager);
        rvSalesOrders.setHasFixedSize(true);

        if (orderListArrayList.size()>0){
            llInvoiceDetails.setVisibility(View.VISIBLE);
            try {
                YoYo.with(Techniques.FlipInX)
                        .duration(700)
                        .playOn(llInvoiceDetails);
            }catch (Exception e){

            }
        }else{
            tvNoResults.setVisibility(View.VISIBLE);

            try {
                YoYo.with(Techniques.BounceInRight)
                        .duration(700)
                        .playOn(tvNoResults);
            }catch (Exception e){

            }
        }

        CancelSalesOrdersAdapter cancelSalesOrdersAdapter = new CancelSalesOrdersAdapter(getApplicationContext(),orderListArrayList,id);
        rvSalesOrders.setAdapter(cancelSalesOrdersAdapter);
    }

    private void loadMerchant(final ArrayList<MerchantDetails> merchantArrayList) {
        merchantList.clear();
        for (int i = 0; i < merchantArrayList.size(); i++) {
            merchantList.add(merchantArrayList.get(i).getMerchantName()+ " - " + merchantArrayList.get(i).getMerchantId());
        }
        spdMerchant=new SpinnerDialog(ActivityCancelSalesOrders.this,merchantList,"Select or Search Merchant",R.style.DialogAnimations_SmileWindow);

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
        spdSalesRep=new SpinnerDialog(ActivityCancelSalesOrders.this,salesRepList,"Select or Search SalesRep",R.style.DialogAnimations_SmileWindow);

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
        tvMerchantName = (TextView) findViewById(R.id.tvMerchant);
        tvSalesRep = (TextView) findViewById(R.id.tvSalesRepName);
        tvNoResults = (TextView) findViewById(R.id.tvEmptyResult);
        tvSearchButoon = (TextView) findViewById(R.id.tvSearchButoon);
        tvDateFrom = (TextView) findViewById(R.id.tvDateFrom);
        tvDateTo = (TextView) findViewById(R.id.tvDateTo);

        //RecyclerView
        rvSalesOrders = (RecyclerView) findViewById(R.id.rvSearchItems);

        //Layouts
        llInvoiceDetails = (LinearLayout) findViewById(R.id.llInvoiceDetails);

        //Calender
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

    }

    @Override
    protected Dialog onCreateDialog(final int id) {

        if(id == 67) {
            return new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                            showDate(year, month+1, dayOfMonth,id);
                        }
                    }, year, month, day);



        }

        /*final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();*/



        if(id == 63) {
            return new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            showDate(year, month+1, dayOfMonth,id);
                        }
                    }, year, month, day);
        }
        return null;
    }

    private void showDate(int Year, int month, int day,int id) {
        String mnt= null;
        String dy = null;
        if(month < 10){
            mnt = "0"+month;
        }else if(month >= 10){
            mnt = ""+month;
        }
        if(day < 10){
            dy = "0"+day;
        }else if(day >= 10){
            dy = ""+day;
        }

       String  dateString = Year +"-"+mnt+"-"+dy;


        //Date Foremat for compare
        //Fri 28 July 2017 10:47:03
        if (id==67){
            tvDateFrom.setText(dateString);
            dateFrom = dateManager.getMilSecAccordingToDate( dy+"."+mnt+"."+Year);
            System.out.println("==== Date From Milliseconds =====" + dateFrom);
            System.out.println("==== Date From  =====" + dy+"."+mnt+"."+Year);
        }
        if (id==63){
            tvDateTo.setText(dateString);
            dateTo = dateManager.getMilSecAccordingToDate(dy+"."+mnt+"."+Year);
            System.out.println("==== Date To Milliseconds =====" + dateTo);
            System.out.println("==== Date To  =====" + dy+"."+mnt+"."+Year);
        }

    }

    private void displayStatusMessage(String s, int colorValue) {

        AlertDialog.Builder builder = null;
        View view = null;
        TextView tvOk,tvMessage,tvCancel;



        int defaultColor = R.color.textGray;
        int successColor = R.color.successColor; // 1
        int errorColor = R.color.errorColor; // 2
        int warningColor = R.color.warningColor; // 3
        //1,2,3

        int color = defaultColor;
        if(colorValue == 1){
            color = successColor;
        }else if(colorValue == 2){
            color = errorColor;
        }else if(colorValue == 3){
            color = warningColor;
        }

        builder = new AlertDialog.Builder(ActivityCancelSalesOrders.this);
        view = getLayoutInflater().inflate(R.layout.layout_for_custom_message,null);

        tvOk = (TextView) view.findViewById(R.id.tvOk);
        tvMessage = (TextView) view.findViewById(R.id.tvMessage);
        tvMessage.setTextColor(getResources().getColor(color));
        tvMessage.setText(s);


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
