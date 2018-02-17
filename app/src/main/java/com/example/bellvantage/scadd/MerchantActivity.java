package com.example.bellvantage.scadd;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MerchantActivity extends AppCompatActivity {

    Button btnRegister, btnUpdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant);

        //initialize Views
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnUpdate= (Button) findViewById(R.id.btnUpdate);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(getApplicationContext(),MerchantRegistrer.class);
                startActivity(registerIntent);
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent updateRegister = new Intent(getApplicationContext(),MerchantUpdateActivity.class);
                startActivity(updateRegister);
            }
        });
    }
}
