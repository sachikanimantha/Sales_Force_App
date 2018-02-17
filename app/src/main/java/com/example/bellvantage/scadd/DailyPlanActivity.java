package com.example.bellvantage.scadd;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.bellvantage.scadd.Adapters.MenuAdapter;
import com.example.bellvantage.scadd.MC.MyMenu;
import com.example.bellvantage.scadd.Utils.SessionManager;
import com.example.bellvantage.scadd.swf.LoginUser;
import com.google.gson.Gson;

import java.util.ArrayList;

public class DailyPlanActivity extends AppCompatActivity {

    //Toolbar
    Toolbar mToolBar;

    //RecycleView for menu
    RecyclerView rvMenuItem;
    MenuAdapter adapter;
    int salesRepId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_plan);

        mToolBar = (Toolbar) findViewById(R.id.tb_main);
        mToolBar.setTitle("Daily Plan");

        Gson gson = new Gson();
        String getJson = SessionManager.pref.getString("user", "");
        LoginUser prefUser = gson.fromJson(getJson, LoginUser.class);
        salesRepId = prefUser.getUserTypeId();

        //Create Menu
        insertMenu();
    }

    private void insertMenu() {

        //RecyclerView
        rvMenuItem =(RecyclerView)findViewById(R.id.rvMenuItem);
        //layoutManager = new LinearLayoutManager(this);
        LinearLayoutManager layoutManager = new GridLayoutManager(DailyPlanActivity.this,3,LinearLayoutManager.VERTICAL,false);
        rvMenuItem.setLayoutManager(layoutManager);
        rvMenuItem.setHasFixedSize(true);


        String[] menuLabels = { "My Outlet List","View Vehicle Stock","View Sales Rep Stock"};
        int[] menuImages = {
                R.drawable.attendance,
                R.drawable.mm_vehicle_load,
                R.drawable.mm_view_stock
        };

        ArrayList<MyMenu> arrayList = new ArrayList<>();

        for(int i = 0; i < menuLabels.length; i++) {
            String menuName = menuLabels[i];
            int img = menuImages[i];
            arrayList.add(new MyMenu(menuName,img));
        }
        adapter = new MenuAdapter(getApplicationContext(),arrayList,salesRepId);
        rvMenuItem.setAdapter(adapter);
    }
}
