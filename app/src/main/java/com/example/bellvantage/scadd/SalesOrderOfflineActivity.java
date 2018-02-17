package com.example.bellvantage.scadd;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.bellvantage.scadd.Adapters.SalesOrderOfflineMenuAdapter;
import com.example.bellvantage.scadd.MC.MyMenu;

import java.util.ArrayList;

public class SalesOrderOfflineActivity extends AppCompatActivity {

    //Toolbar
    private Toolbar mToolBar;

    RecyclerView rvSalesOrderMenu;
    SalesOrderOfflineMenuAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_order);

        mToolBar = (Toolbar) findViewById(R.id.tb_main);
        mToolBar.setTitle("Sales Order");

        //RecyclerView
        rvSalesOrderMenu=(RecyclerView)findViewById(R.id.rvSalesOrderMenu);
        //layoutManager = new LinearLayoutManager(this);
        LinearLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),3,LinearLayoutManager.VERTICAL,false);
        rvSalesOrderMenu.setLayoutManager(layoutManager);
        rvSalesOrderMenu.setHasFixedSize(true);
        String[] menuLabels = { "Create Sales Orders","Revise Sale Orders","Confirm Sale Orders",
                                "Cancel Orders","Sales Order to Invoice"};
        int[] menuImages = {
                R.drawable.create_sales_order,
                R.drawable.mm_revise_sales_order,
                R.drawable.mm_confirm_sales_order,
                R.drawable.mm_cancel_sales_order,
                R.drawable.mm_invoice
        };


        ArrayList<MyMenu> arrayList = new ArrayList<>();

        for(int i = 0; i < menuLabels.length; i++) {
            String menuName = menuLabels[i];
            int img = menuImages[i];
            arrayList.add(new MyMenu(menuName,img));
        }
        adapter = new SalesOrderOfflineMenuAdapter(getApplicationContext(),arrayList);
        rvSalesOrderMenu.setAdapter(adapter);
    }
}
