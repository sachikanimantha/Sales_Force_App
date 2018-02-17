package com.example.bellvantage.scadd;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.bellvantage.scadd.Adapters.PrimarySalesOrderMenuAdapter;
import com.example.bellvantage.scadd.MC.MyMenu;
import com.example.bellvantage.scadd.Utils.SessionManager;
import com.example.bellvantage.scadd.swf.LoginUser;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ReportsActivity extends AppCompatActivity {

    //Toolbar
    private Toolbar mToolBar;

    RecyclerView rvPrimarySalesOrderMenu;
    PrimarySalesOrderMenuAdapter adapter;

    String userType;
    String[] menuLabels;
    int[] menuImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        Gson gson = new Gson();
        String getJson = SessionManager.pref.getString("user", "");
        LoginUser prefUser = gson.fromJson(getJson, LoginUser.class);
        userType =prefUser.getUserType();

        mToolBar = (Toolbar) findViewById(R.id.tb_main);
        mToolBar.setTitle("Reports");

        //RecyclerView
        rvPrimarySalesOrderMenu=(RecyclerView)findViewById(R.id.rvPrimarySalesOrderMenu);
        //layoutManager = new LinearLayoutManager(this);
        LinearLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),3,LinearLayoutManager.VERTICAL,false);
        rvPrimarySalesOrderMenu.setLayoutManager(layoutManager);
        rvPrimarySalesOrderMenu.setHasFixedSize(true);

            menuLabels = new String[]{"SalesRep Inventory Report", "Vehicle Inventory Report","Merchant Card","Merchant Visit"};
            menuImages = new int[]{
                    R.drawable.mm_salesrep_inventory,
                    R.drawable.vehicle_report,
                    R.drawable.mm_merchant_card,
                    R.drawable.mm_merchant_visit
            };

        ArrayList<MyMenu> arrayList = new ArrayList<>();

        for(int i = 0; i < menuLabels.length; i++) {
            String menuName = menuLabels[i];
            int img = menuImages[i];
            arrayList.add(new MyMenu(menuName,img));
        }
        adapter = new PrimarySalesOrderMenuAdapter(getApplicationContext(),arrayList);
        rvPrimarySalesOrderMenu.setAdapter(adapter);
    }
}
