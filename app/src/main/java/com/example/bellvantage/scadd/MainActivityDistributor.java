package com.example.bellvantage.scadd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bellvantage.scadd.DB.DbHelper;
import com.example.bellvantage.scadd.Utils.DateManager;
import com.example.bellvantage.scadd.Utils.SyncManager;
import com.example.bellvantage.scadd.swf.SalesRep;

import java.util.ArrayList;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

import static com.example.bellvantage.scadd.DB.DbHelper.COL_DISTRIBUTOR_DISTRIBUTOR_ID;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_DISTRIBUTOR_DISTRIBUTOR_NAME;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_DISTRIBUTOR;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_SALES_REP;
import static com.example.bellvantage.scadd.Utils.SyncManager.Distributor;
import static com.example.bellvantage.scadd.Utils.SyncManager.Salesrep;

public class MainActivityDistributor extends AppCompatActivity {


    Toolbar mToolbar;
    int disID_ = 0;//distributor id
    TextView tvsales,tvdis;
    Button btn_con;
    SpinnerDialog sd_salesrep;
    ArrayList<String> salesrep_Stringarray = new ArrayList<>();
    ArrayList<SalesRep> salesReps = new ArrayList<>();
    String salesrepName;
    int salesrepid;
    String usertype ;
    String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_distributor);

        mToolbar = (Toolbar) findViewById(R.id.tb_main);
        mToolbar.setTitle("Distributor");
        salesReps.clear();

        time = (new DateManager()).getDateWithTime();

        disID_ = getIntent().getExtras().getInt("userID_");
        usertype = getIntent().getExtras().getString("userTYPE_");
        System.out.println("userID_(distributor id) - "+disID_);

        tvdis = (TextView)findViewById(R.id.tv_dis_name_);
        tvsales = (TextView)findViewById(R.id.tv_salesrep_name_);
        btn_con = (Button)findViewById(R.id.btn_continue);
        btn_con.setEnabled(false);

        SyncManager syncManager = new SyncManager(getApplicationContext());

        String sql = "SELECT "+COL_DISTRIBUTOR_DISTRIBUTOR_NAME+" FROM "+TABLE_DISTRIBUTOR+" WHERE "+COL_DISTRIBUTOR_DISTRIBUTOR_ID+" = "+disID_;
        //System.out.println("sql - "+sql);
        String columnName = COL_DISTRIBUTOR_DISTRIBUTOR_NAME ;
        tvdis.setText(syncManager.getStringValueFromSQLite(sql,columnName));


        String sql2 = "SELECT * FROM "+TABLE_SALES_REP+" WHERE "+ DbHelper.COL_SALES_REP_DistributorId +" = "+disID_;
        //System.out.println("sql2 - "+sql2);
        salesReps = syncManager.getSalesRepDetailsFromSQLite(sql2);
        System.out.println("salesReps.size - outside "+salesReps.size());

        loadSalesrepList(salesReps);

        tvsales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sd_salesrep.showSpinerDialog();
            }
        });

        btn_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityDistributor.this,LoginSyncActivity.class);
                intent.putExtra("salesrepName",salesrepName);
                intent.putExtra("userTYPE",usertype);
                intent.putExtra("disID",disID_);

                (new SyncManager(getApplicationContext())).updateSyncTable(Salesrep,time,1,1, salesrepid, 1);
                (new SyncManager(getApplicationContext())).updateSyncTable(Distributor,time,1,1, salesrepid, 1);
                startActivity(intent);
            }
        });

    }

    private void loadSalesrepList(final ArrayList<SalesRep> salesReps) {

        System.out.println(" inside salesrep.side() - "+salesReps.size());
        for(int i=0; i < salesReps.size() ; i++){
            salesrep_Stringarray.add(salesReps.get(i).getSalesRepName()+" - "+salesReps.get(i).getSalesRepId());
        }

        sd_salesrep = new SpinnerDialog(MainActivityDistributor.this,salesrep_Stringarray,"Select A Salesrep",R.style.DialogAnimations_SmileWindow);


        sd_salesrep.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {

                tvsales.setText(s);
                for(int i2 = 0; i2 <salesReps.size() ; i2++ ){
                    if((salesReps.get(i2).getSalesRepName()+" - "+salesReps.get(i).getSalesRepId()).equals(s)){
                        salesrepName = salesReps.get(i2).getSalesRepName();
                        salesrepid = salesReps.get(i2).getSalesRepId();
                        Toast.makeText(MainActivityDistributor.this, "salesrepid - "+salesrepid, Toast.LENGTH_SHORT).show();
                        btn_con.setEnabled(true);
                    }
                }
            }
        });
    }



}
