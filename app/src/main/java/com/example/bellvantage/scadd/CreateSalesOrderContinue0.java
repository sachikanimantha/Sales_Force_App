package com.example.bellvantage.scadd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class CreateSalesOrderContinue0 extends AppCompatActivity {

    Button btn_tax,btn_nontax;
    Toolbar mToolbar;

    int mid_,misvat_,did_,disvat_;
    String mname_,dname_,srt_;

    int merchantType = 0,merchantDays= 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_sales_order_continue0);

        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.tb_main);
        mToolbar.setTitle("Sales Type");


        mid_ = getIntent().getExtras().getInt("merchantID");
        misvat_ = getIntent().getExtras().getInt("merchantISVAT");
        mname_ = getIntent().getExtras().getString("merchantNAME");
        did_ = getIntent().getExtras().getInt("distributorID");
        disvat_ = getIntent().getExtras().getInt("distributorISVAT");
        dname_ = getIntent().getExtras().getString("distributorNAME");
        srt_ = getIntent().getExtras().getString("salesrepTYPE");
        merchantType = getIntent().getExtras().getInt("merchant_payment_method_1");
        merchantDays = getIntent().getExtras().getInt("merchant_daycount_1");

        System.out.println("merchantType - "+merchantType);
        System.out.println("merchantDays - "+merchantDays);


        btn_tax = (Button) findViewById(R.id.btn_tax_so);
        btn_nontax = (Button)findViewById(R.id.btn_nontax_so);

        btn_tax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateSalesOrderContinue0.this,CreateSalesOrderContinue.class);
                intent.putExtra("vatType",1);

                intent.putExtra("merzchant_id_", mid_);
                intent.putExtra("merchant_name_", mname_);
                intent.putExtra("merchant_isvat_", misvat_);
                intent.putExtra("distributor_id", did_);
                intent.putExtra("distributor_name", dname_);
                intent.putExtra("distributor_isvat", disvat_);
                intent.putExtra("salesrep_type", srt_);

                intent.putExtra("merchant_payment_method", merchantType);
                intent.putExtra("merchant_daycount", merchantDays);
                CreateSalesOrderContinue0.this.finish();
                startActivity(intent);
            }
        });

        btn_nontax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateSalesOrderContinue0.this,CreateSalesOrderContinue.class);
                intent.putExtra("vatType",0);

                intent.putExtra("merchant_id", mid_);
                intent.putExtra("merchant_name", mname_);
                intent.putExtra("merchant_isvat", misvat_);
                intent.putExtra("distributor_id", did_);
                intent.putExtra("distributor_name", dname_);
                intent.putExtra("distributor_isvat", disvat_);
                intent.putExtra("salesrep_type", srt_);


                intent.putExtra("merchant_payment_method", merchantType);
                intent.putExtra("merchant_daycount", merchantDays);

                CreateSalesOrderContinue0.this.finish();
                startActivity(intent);
            }
        });
    }
}
