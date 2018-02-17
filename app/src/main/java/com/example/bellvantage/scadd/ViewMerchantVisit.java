package com.example.bellvantage.scadd;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

import com.example.bellvantage.scadd.Adapters.MerchantVisitAdapter;
import com.example.bellvantage.scadd.Utils.SessionManager;
import com.example.bellvantage.scadd.Utils.SyncManager;
import com.example.bellvantage.scadd.swf.LoginUser;
import com.example.bellvantage.scadd.swf.Merchant;
import com.example.bellvantage.scadd.swf.MerchantVisit;
import com.google.gson.Gson;

import java.util.ArrayList;

import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class ViewMerchantVisit extends AppCompatActivity {

    RecyclerView rv_;
    SearchView sv_;
    Button btn_continue;
    int salesrepid;
    Toolbar mToolbar;
    String userName;
    int userId;
    TextView tv_start, tv_end, tv_merchant;
    DatePicker datePicker;
    String selected_startdate = "", selected_enddate = "";
    SpinnerDialog sd_merchant;
    ArrayList<String> arrayMerchant = new ArrayList<>();
    Button ok;

    String merchatID = "";
    ArrayList<MerchantVisit> merchantVisits = new ArrayList<>();
    ArrayList<Merchant> merchants = new ArrayList<>();
    MerchantVisitAdapter merchantVisitAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_merchant_visit);

        init();

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~copied
        tv_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewMerchantVisit.this);
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_for_datepicker, null);
                datePicker = (DatePicker) view.findViewById(R.id.dp);
                ok = (Button) view.findViewById(R.id.btn_dp);
                builder.setView(view);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int mon = datePicker.getMonth() + 1;
                        String month = "" + mon;
                        String day = "" + datePicker.getDayOfMonth();

                        tv_start.setText("From - " + datePicker.getYear() + "-" + month + "-" + day);

                        //2007-11-11
                        if (mon < 10) {
                            month = "0" + mon;
                        }
                        if (datePicker.getDayOfMonth() < 10) {
                            day = "0" + datePicker.getDayOfMonth();
                        }

                        //YYYY-MM-DD should be this format in sqlite
                        selected_startdate = datePicker.getYear() + "-" + month + "-" + day;
                        System.out.println("start date - " + selected_startdate);

                        arrayMerchant.clear();
                        //VISIT ARRAY
                        merchantVisits = (new SyncManager(getApplicationContext()))
                                .getAllMerchantVisitData_accordingTo_DateAndMerchant(selected_startdate, selected_enddate,merchatID);

                        //MERCHANT ARRAY
                        merchants = (new SyncManager(getApplicationContext())).getAllMerchantVisitMerchants_accordingToDate(selected_startdate, selected_enddate);

                        merchantVisitAdapter = new MerchantVisitAdapter( merchantVisits,getApplicationContext());
                        rv_.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        rv_.setItemAnimator(new DefaultItemAnimator());
                        rv_.setAdapter(merchantVisitAdapter);

                        tv_merchant.setEnabled(true);
                        //loadMerchant(merchantArrayList);

                        alertDialog.dismiss();
                    }
                });
            }
        });

        tv_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ViewMerchantVisit.this);
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_for_datepicker, null);
                datePicker = (DatePicker) view.findViewById(R.id.dp);
                ok = (Button) view.findViewById(R.id.btn_dp);
                builder.setView(view);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int mon = datePicker.getMonth() + 1;
                        String month = "" + mon;
                        String day = "" + datePicker.getDayOfMonth();

                        tv_end.setText("To - " + datePicker.getYear() + "-" + month + "-" + day);

                        //2007-11-11
                        if (mon < 10) {
                            month = "0" + mon;
                        }
                        if (datePicker.getDayOfMonth() < 10) {
                            day = "0" + datePicker.getDayOfMonth();
                        }

                        //YYYY-MM-DD should be this format in sqlite
                        selected_enddate = datePicker.getYear() + "-" + month + "-" + day;
                        System.out.println("end date - " + selected_enddate);

                        arrayMerchant.clear();
                        //STOCK ARRAY
                        merchantVisits = (new SyncManager(getApplicationContext())).
                                getAllMerchantVisitData_accordingTo_DateAndMerchant(selected_startdate, selected_enddate,merchatID);

                        //MERCHANT ARRAY
                        merchants = (new SyncManager(getApplicationContext())).getAllMerchantVisitMerchants_accordingToDate(selected_startdate, selected_enddate);

                        merchantVisitAdapter = new MerchantVisitAdapter(merchantVisits, getApplicationContext());
                        rv_.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        rv_.setItemAnimator(new DefaultItemAnimator());
                        rv_.setAdapter(merchantVisitAdapter);

                        //loadMerchant(merchantArrayList);

                        alertDialog.dismiss();
                    }
                });
            }
        });
//
//        tv_merchant.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                sd_merchant.showSpinerDialog();
//            }
//        });
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    }

    private void init() {

        Gson gson = new Gson();
        String getJson = SessionManager.pref.getString("user", "");
        LoginUser prefUser = gson.fromJson(getJson, LoginUser.class);
        userName =prefUser.getUserName();
        userId = prefUser.getUserTypeId();

        mToolbar = (Toolbar) findViewById(R.id.tb_main_mvisit);
        mToolbar.setTitle("Merchant Visit");

        salesrepid = getIntent().getExtras().getInt("salesrepid");

        //btn_continue = (Button)findViewById(R.id.btn_continue_mvisit);
        rv_ = (RecyclerView)findViewById(R.id.rv_merchant_visit);
        //sv_ = (SearchView)findViewById(R.id.sv_item_mvisit);
        tv_start = (TextView)findViewById(R.id.tv_mvc_since);
        tv_end = (TextView)findViewById(R.id.tv_mvc_to);
        tv_merchant = (TextView)findViewById(R.id.tv_merchant_visit);

        merchantVisits = (new SyncManager(getApplicationContext())).getAllMerchantVisitData();
        System.out.println("merchantVisits.size - "+merchantVisits.size());
        merchantVisitAdapter = new MerchantVisitAdapter(merchantVisits,getApplicationContext());

        rv_.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_.setItemAnimator(new DefaultItemAnimator());
        rv_.setAdapter(merchantVisitAdapter);
    }





}
