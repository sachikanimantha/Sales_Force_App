package com.example.bellvantage.scadd;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bellvantage.scadd.DB.DbHelper;
import com.example.bellvantage.scadd.Utils.DateManager;
import com.example.bellvantage.scadd.Utils.SyncManager;
import com.example.bellvantage.scadd.swf.MerchantVisit;
import com.example.bellvantage.scadd.swf.MerchantVisitReason;

import java.util.ArrayList;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class CreateSalesOrder_VisitReasonSelect extends AppCompatActivity {

    TextView tv_visit;
    Button btn_submit;
    Toolbar mToolbar;
    int reason_id = 0;
    int merchantId = 0, isMercahantVat = 0;
    String merchantname;
    int pathid, distributor_id, salesrep_id;
    boolean b;

    SpinnerDialog spinnerDialogReason;

    MerchantVisit merchantVisit;

    ArrayList<MerchantVisitReason> arrayReason = new ArrayList<>();
    ArrayList<String> arrayStringReason = new ArrayList<>();
    String today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_sales_order_visitireason_select);

        init();

        arrayReason = (new SyncManager(getApplicationContext())).getAllMerchantVisitReasonData();
        System.out.println("arrayReason.size - " + arrayReason);
        arrayReason.add(0, new MerchantVisitReason(0, "Available", 1));

        loadVisitReason(arrayReason);

        tv_visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialogReason.showSpinerDialog();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("%%%%%%% merchantId - "+merchantId);
                merchantVisit = new MerchantVisit(merchantId, reason_id, salesrep_id, distributor_id, pathid,0, today, "");
                System.out.println("%%%%%%% merchantId - "+merchantId);

                //insert to sqlite db
                ContentValues contentValues = merchantVisit.getCVvaluesfromMerchantVisit();
                   b = (new DbHelper(getApplicationContext())).insertDataAll(contentValues, DbHelper.TABLE_MVISIT);

                if(b){
                    System.out.println("success insert MVISIT data to SQLite");
                }else{
                    System.out.println("Error occured insert MVISIT data to SQLite");
                }

                System.out.println("reason id - "+reason_id);

                if (reason_id == 0) {

                    Intent intent = new Intent(CreateSalesOrder_VisitReasonSelect.this, MerchantInventoryCard.class);
                    intent.putExtra("vatType", 2);
                    intent.putExtra("merchant_id", merchantId);
                    intent.putExtra("merchant_name", merchantname);
                    intent.putExtra("merchant_isvat", isMercahantVat);
                    intent.putExtra("merchant_visit", reason_id);
                    startActivity(intent);

                } else if (reason_id == 1 || reason_id == 2) {

                    displayStatusMessage1("You cant make any sales orders for this merchant", 2);


                } else if (reason_id == 3) {

                    Intent intent = new Intent(CreateSalesOrder_VisitReasonSelect.this, MerchantInventoryCard.class);
                    intent.putExtra("vatType", 2);
                    intent.putExtra("merchant_id", merchantId);
                    intent.putExtra("merchant_name", merchantname);
                    intent.putExtra("merchant_isvat", isMercahantVat);
                    intent.putExtra("merchant_visit", reason_id);
                    startActivity(intent);
                }
            }
        });
    }

    private void init() {

        mToolbar = (Toolbar) findViewById(R.id.tb_main);
        mToolbar.setTitle("Create Sales Order - Step 02");

        btn_submit = (Button) findViewById(R.id.btn_submit_visit);
        tv_visit = (TextView) findViewById(R.id.tv_visit);

        merchantId = getIntent().getExtras().getInt("merchant_id");
        isMercahantVat = getIntent().getExtras().getInt("merchant_isvat");
        merchantname = getIntent().getExtras().getString("merchant_name");
        salesrep_id = getIntent().getExtras().getInt("salesrep_id");
        distributor_id = getIntent().getExtras().getInt("distributor_id");
        pathid = getIntent().getExtras().getInt("path_id");

        System.out.println("merchantId - " + merchantId);
        System.out.println("isMercahantVat - " + isMercahantVat);
        System.out.println("merchantname - " + merchantname);
        System.out.println("distributor_id - " + distributor_id);
        System.out.println("salesrep_id - " + salesrep_id);

        today = DateManager.getDateWithTime2();

    }

    private void loadVisitReason(final ArrayList<MerchantVisitReason> merchantVisitReasons) {

        System.out.println("merchantVisitReasons.size - " + merchantVisitReasons.size());

        for (int h = 0; h < merchantVisitReasons.size(); h++) {
            arrayStringReason.add(merchantVisitReasons.get(h).getDescription());
        }
        spinnerDialogReason = new SpinnerDialog(CreateSalesOrder_VisitReasonSelect.this, arrayStringReason, "Select A Reason", R.style.DialogAnimations_SmileWindow);

        spinnerDialogReason.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                tv_visit.setText(s);

                for (int f = 0; f < merchantVisitReasons.size(); f++) {
                    if (merchantVisitReasons.get(f).getDescription().equals(s)) {
                        reason_id = merchantVisitReasons.get(f).getReason_id();
                    }
                }

                System.out.println("reason_id - "+reason_id);






            }
        });
    }

    private void displayStatusMessage1(String s, int colorValue) {

        AlertDialog.Builder builder;
        View view;
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

        builder = new AlertDialog.Builder(CreateSalesOrder_VisitReasonSelect.this);
        view = getLayoutInflater().inflate(R.layout.layout_for_custom_message, null);

        tvOk = (TextView) view.findViewById(R.id.tvOk);
        tvMessage = (TextView) view.findViewById(R.id.tvMessage);
        imageView = (ImageView) view.findViewById(R.id.iv_status_k);

        tvMessage.setTextColor(getResources().getColor(color));
        tvMessage.setText(s);
        imageView.setImageResource(img);

        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                finish();
                Intent i = new Intent(CreateSalesOrder_VisitReasonSelect.this,SalesOrderOfflineActivity.class);
                startActivity(i);
            }
        });
    }


}
