package com.example.bellvantage.scadd;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bellvantage.scadd.Adapters.MerchantStockLineItemViewAdapter;
import com.example.bellvantage.scadd.Adapters.MerchantStockViewAdapter;
import com.example.bellvantage.scadd.DB.DbHelper;
import com.example.bellvantage.scadd.Utils.DateManager;
import com.example.bellvantage.scadd.Utils.SyncManager;
import com.example.bellvantage.scadd.swf.MerchantStock;
import com.example.bellvantage.scadd.swf.Merchant;
import com.example.bellvantage.scadd.swf.MerchantStockLineitems;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class ViewMerchantCard extends AppCompatActivity {

    Toolbar mToolbar;
    RecyclerView rv_;
    RecyclerView rv_lineItems;
    TextView tv_continue;
    TextView tv_start, tv_end, tv_merchant;
    DatePicker datePicker;
    Button ok;
    ArrayList<String> arrayDate = new ArrayList<>();
    long stdate = 0, eddate = 0;
    String salesrep_name, mer_name, entered_date;
    String iDate;
    TextView tv_merStockid_msli, tv_salesrepid_msli, tv_merchantid_msli, tv_syncdate_msli;
    String selected_startdate = "", selected_enddate = "";
    SpinnerDialog sd_merchant;
    ArrayList<String> arrayMerchant = new ArrayList<>();

    String merchatID = "";

    ArrayList<MerchantStock> stockArrayList = new ArrayList<>();
    ArrayList<MerchantStockLineitems> stockLineitemsArrayList = new ArrayList<>();
    ArrayList<Merchant> merchantArrayList = new ArrayList<>();
    MerchantStockViewAdapter merchantStockViewAdapter;
    MerchantStockLineItemViewAdapter merchantStockLineItemViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_merchant_card);

        init();

        tv_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewMerchantCard.this);
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
                        //STOCK ARRAY
                        stockArrayList = (new SyncManager(getApplicationContext()))
                                .getAllMerchantStockData_accordingTo_DateAndMerchant(selected_startdate, selected_enddate,merchatID);

                        //MERCHANT ARRAY
                        merchantArrayList = (new SyncManager(getApplicationContext())).getAllMerchantStockMerchants_accordingToDate(selected_startdate, selected_enddate);

                        merchantStockViewAdapter = new MerchantStockViewAdapter(getApplicationContext(), stockArrayList, stockLineitemsArrayList,
                                new MerchantStockViewAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(MerchantStock merchantStock, int position) {
                                        //Toast.makeText(ViewMerchantCard.this, "position - " + position, Toast.LENGTH_SHORT).show();
                                        int delarcardid = merchantStock.getMerchentStockId();

                                        ArrayList<MerchantStockLineitems> merchantStockLineitemses = new ArrayList<>();
                                        try {
                                            merchantStockLineitemses = (new SyncManager(getApplicationContext())).getAllMerchantStockLineItemData(delarcardid);
                                            if (merchantStockLineitemses.size() > 0) {
                                                System.out.println("merchantStockLineitemses.size() - " + merchantStockLineitemses.size());
                                                showLineItems(merchantStockLineitemses, merchantStock);
                                            }
                                        } catch (Exception e) {
                                            Toast.makeText(ViewMerchantCard.this, "No Items to view", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        rv_.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        rv_.setItemAnimator(new DefaultItemAnimator());
                        rv_.setAdapter(merchantStockViewAdapter);

                        tv_merchant.setEnabled(true);
                        loadMerchant(merchantArrayList);

                        alertDialog.dismiss();
                    }
                });
            }
        });

        tv_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ViewMerchantCard.this);
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
                        stockArrayList = (new SyncManager(getApplicationContext())).
                                getAllMerchantStockData_accordingTo_DateAndMerchant(selected_startdate, selected_enddate,merchatID);

                        //MERCHANT ARRAY
                        merchantArrayList = (new SyncManager(getApplicationContext())).getAllMerchantStockMerchants_accordingToDate(selected_startdate, selected_enddate);

                        merchantStockViewAdapter = new MerchantStockViewAdapter(getApplicationContext(), stockArrayList, stockLineitemsArrayList,
                                new MerchantStockViewAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(MerchantStock merchantStock, int position) {
                                        Toast.makeText(ViewMerchantCard.this, "position - " + position, Toast.LENGTH_SHORT).show();
                                        int delarcardid = merchantStock.getMerchentStockId();

                                        ArrayList<MerchantStockLineitems> merchantStockLineitemses = new ArrayList<>();
                                        try {
                                            merchantStockLineitemses = (new SyncManager(getApplicationContext())).getAllMerchantStockLineItemData(delarcardid);
                                            if (merchantStockLineitemses.size() > 0) {
                                                System.out.println("merchantStockLineitemses.size() - " + merchantStockLineitemses.size());
                                                showLineItems(merchantStockLineitemses, merchantStock);
                                            }
                                        } catch (Exception e) {
                                            Toast.makeText(ViewMerchantCard.this, "No Items to view", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        rv_.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        rv_.setItemAnimator(new DefaultItemAnimator());
                        rv_.setAdapter(merchantStockViewAdapter);

                        loadMerchant(merchantArrayList);

                        alertDialog.dismiss();
                    }
                });
            }
        });

        tv_merchant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sd_merchant.showSpinerDialog();
            }
        });
    }


    public void loadMerchant(final ArrayList<Merchant> merchants) {

        for (int i = 0; i < merchants.size(); i++) {
            arrayMerchant.add(merchants.get(i).getMerchantName() + " - " + merchants.get(i).getMerchantId());
        }

        sd_merchant = new SpinnerDialog(ViewMerchantCard.this, arrayMerchant,
                "Select A Merchant", R.style.DialogAnimations_SmileWindow);

        sd_merchant.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                tv_merchant.setText(s);
                stockArrayList.clear();

                //String mid = "";
                for (int h = 0; h < arrayMerchant.size(); h++) {
                    if (s.equalsIgnoreCase(merchants.get(h).getMerchantName() + " - " + merchants.get(h).getMerchantId())) {
                        merchatID = merchants.get(h).getMerchantId();
                    }
                }

                stockArrayList = (new SyncManager(getApplicationContext())).
                        getAllMerchantStockData_accordingTo_DateAndMerchant(selected_startdate, selected_enddate,merchatID);


                merchantStockViewAdapter = new MerchantStockViewAdapter(getApplicationContext(), stockArrayList, stockLineitemsArrayList,
                        new MerchantStockViewAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(MerchantStock merchantStock, int position) {
                                Toast.makeText(ViewMerchantCard.this, "position - " + position, Toast.LENGTH_SHORT).show();
                                int delarcardid = merchantStock.getMerchentStockId();

                                ArrayList<MerchantStockLineitems> merchantStockLineitemses = new ArrayList<>();
                                try {
                                    merchantStockLineitemses = (new SyncManager(getApplicationContext())).getAllMerchantStockLineItemData(delarcardid);
                                    if (merchantStockLineitemses.size() > 0) {
                                        System.out.println("merchantStockLineitemses.size() - " + merchantStockLineitemses.size());
                                        showLineItems(merchantStockLineitemses, merchantStock);
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(ViewMerchantCard.this, "No Items to view", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                rv_.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                rv_.setItemAnimator(new DefaultItemAnimator());
                rv_.setAdapter(merchantStockViewAdapter);
            }
        });
    }

    public void init() {

        mToolbar = (Toolbar) findViewById(R.id.tb_main_vm);
        mToolbar.setTitle("Merchant Stock");

        rv_ = (RecyclerView) findViewById(R.id.rv_view_merchantcard);

        tv_start = (TextView) findViewById(R.id.tv_msv_since);
        tv_end = (TextView) findViewById(R.id.tv_msv_to);

        tv_merchant = (TextView) findViewById(R.id.tv_merchant_merchantcardview);
        tv_merchant.setEnabled(false);


        if (eddate == 0 && stdate == 0) {

            stockArrayList = (new SyncManager(getApplicationContext())).getAllMerchantStockData();

        }

        System.out.println("inside activity stockArrayList.size - " + stockArrayList.size());

        merchantStockViewAdapter = new MerchantStockViewAdapter(this, stockArrayList, stockLineitemsArrayList,
                new MerchantStockViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(MerchantStock merchantStock, int position) {

                        int delarcardid = merchantStock.getMerchentStockId();
                        ArrayList<MerchantStockLineitems> merchantStockLineitemses = new ArrayList<>();
                        try {
                            merchantStockLineitemses = (new SyncManager(getApplicationContext())).getAllMerchantStockLineItemData(delarcardid);
                            if (merchantStockLineitemses.size() > 0) {
                                System.out.println("merchantStockLineitemses.size() - " + merchantStockLineitemses.size());
                                showLineItems(merchantStockLineitemses, merchantStock);
                            }
                        } catch (Exception e) {
                            Toast.makeText(ViewMerchantCard.this, "No Items to view", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        rv_.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_.setItemAnimator(new DefaultItemAnimator());
        rv_.setAdapter(merchantStockViewAdapter);
    }

    public void showLineItems(ArrayList<MerchantStockLineitems> list, MerchantStock stocks) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewMerchantCard.this);
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_for_merchantstock_line_item, null);

        tv_merStockid_msli = (TextView) view.findViewById(R.id.tv_dealercard_id_msli);
        tv_salesrepid_msli = (TextView) view.findViewById(R.id.tv_salesrep_id_msli);
        tv_merchantid_msli = (TextView) view.findViewById(R.id.tv_merchantname_msli);
        tv_syncdate_msli = (TextView) view.findViewById(R.id.tv_syncdate_msli);

        String sql1 = "SELECT " + DbHelper.COL_SALES_REP_SalesRepName +
                " FROM " + DbHelper.TABLE_SALES_REP + " WHERE " + DbHelper.COL_SALES_REP_SalesRepId + " = " + stocks.getSalesRepId();
        salesrep_name = (new SyncManager(getApplicationContext())).getStringValueFromSQLite(sql1, DbHelper.COL_SALES_REP_SalesRepName);

        String sql2 = "SELECT " + DbHelper.TBL_MERCHANT_MERCHANT_NAME +
                " FROM " + DbHelper.TABLE_MERCHANT + " WHERE " + DbHelper.TBL_MERCHANT_MERCHANT_ID + " = " + stocks.getMerchantId();
        mer_name = (new SyncManager(getApplicationContext())).getStringValueFromSQLite(sql2, DbHelper.TBL_MERCHANT_MERCHANT_NAME);


        String date_ = (new DateManager()).changeDateFormat("yyyy-MM-dd",stocks.getEnteredDate());

        tv_merStockid_msli.setText("ID - " + stocks.getMerchentStockId());
        tv_salesrepid_msli.setText(": " + salesrep_name);
        tv_merchantid_msli.setText(": " + mer_name);
        tv_syncdate_msli.setText(date_);

        rv_lineItems = (RecyclerView) view.findViewById(R.id.rv_lineitems_msli);

        merchantStockLineItemViewAdapter = new MerchantStockLineItemViewAdapter(getApplicationContext(), list);

        rv_lineItems.setItemAnimator(new DefaultItemAnimator());
        rv_lineItems.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_lineItems.setAdapter(merchantStockLineItemViewAdapter);

        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public static String getDate(long milliSeconds, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

}
