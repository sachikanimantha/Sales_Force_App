package com.example.bellvantage.scadd;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class PrimarySalesOrderActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_primary_sales_order);

        Gson gson = new Gson();
        String getJson = SessionManager.pref.getString("user", "");
        LoginUser prefUser = gson.fromJson(getJson, LoginUser.class);
        userType =prefUser.getUserType();

        mToolBar = (Toolbar) findViewById(R.id.tb_main);
        mToolBar.setTitle("Primary Sales Order");

        //RecyclerView
        rvPrimarySalesOrderMenu=(RecyclerView)findViewById(R.id.rvPrimarySalesOrderMenu);
        //layoutManager = new LinearLayoutManager(this);
        LinearLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),3,LinearLayoutManager.VERTICAL,false);
        rvPrimarySalesOrderMenu.setLayoutManager(layoutManager);
        rvPrimarySalesOrderMenu.setHasFixedSize(true);

        if(userType.equals("S") || userType.equals("s")){

            menuLabels = new String[]{"Create Primary Sales Orders","Primary Sale Orders Approved Report"};

            menuImages = new int[]{

                    R.drawable.create_sales_order,
                    R.drawable.mm_confirm_sales_order,

            };
        }else{
            menuLabels = new String[]{"Create Primary Sales Orders", "Confirm Primary Sale Orders"};
           // menuLabels = new String[]{"Create Primary Sales Orders", "Revise Primary Sale Orders", "Confirm Primary Sale Orders", "Cancel Primary Orders"};
            menuImages = new int[]{

                    R.drawable.create_sales_order,

                    R.drawable.mm_confirm_sales_order

            };
        }



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
