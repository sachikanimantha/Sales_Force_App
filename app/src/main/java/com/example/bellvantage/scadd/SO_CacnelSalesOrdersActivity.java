package com.example.bellvantage.scadd;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class SO_CacnelSalesOrdersActivity extends AppCompatActivity {

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_so__cacel_sales_orders);
        toolbar = (Toolbar) findViewById(R.id.tb_main);
        toolbar.setTitle("Cancel Sales Orders");
    }
}
