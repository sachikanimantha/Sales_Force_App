package com.example.bellvantage.scadd;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bellvantage.scadd.Utils.DateManager;
import com.example.bellvantage.scadd.Utils.SessionManager;
import com.example.bellvantage.scadd.Utils.SyncManager;
import com.example.bellvantage.scadd.swf.MerchantDetails;
import com.example.bellvantage.scadd.swf.DeliveryPath;
import com.example.bellvantage.scadd.swf.LoginUser;
import com.google.gson.Gson;

import java.util.ArrayList;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class MerchantUpdateActivity extends AppCompatActivity {
    //Toolbar
    private Toolbar mToolBar;

    String url,selectedPath;
    SearchView svProduct;

    TextView tvPath;

    RecyclerView rvSearchItems;
    SearchAdapter merchantSaveAdaper;
    Button btnGetMerchantList;
    LinearLayout llSearch;
    ArrayList<MerchantDetails> merchantArrayList = new ArrayList<>();
    ArrayList<DeliveryPath> pathArrayList = new ArrayList<>();

    int salesRepId,pathCode;

    ArrayList<String> pathItems=new ArrayList<>();
    SpinnerDialog pathSpinnerDialog;
    SyncManager syncManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_update);

        mToolBar = (Toolbar) findViewById(R.id.tb_main);
        mToolBar.setLogo(R.drawable.logos);
        btnGetMerchantList = (Button) findViewById(R.id.btnGetMerchant);
        tvPath = (TextView) findViewById(R.id.tvPath);
        llSearch = (LinearLayout) findViewById(R.id.llSearch);
        llSearch.setVisibility(View.GONE);
        Gson gson = new Gson();
        String getJson = SessionManager.pref.getString("user", "");
        LoginUser prefUser = gson.fromJson(getJson, LoginUser.class);
        salesRepId =prefUser.getUserTypeId();

        DateManager dateManager  =  new DateManager();
        String routeDate = dateManager.getTodayDateString();




        //path Spinner
        tvPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pathSpinnerDialog.showSpinerDialog();
            }
        });

        btnGetMerchantList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMerchantList();
            }
        });

        //set data to spinners
        syncManager = new SyncManager(getApplicationContext());
        pathArrayList=syncManager.getAllPath();

        loadPath(pathArrayList);


        svProduct = (SearchView) findViewById(R.id.svSearch);
        svProduct.setQueryHint("Search Merchant by Name");
        rvSearchItems = (RecyclerView)findViewById(R.id.rvSearchItems);



        //RecyclerView
        //LinearLayoutManager layoutManager = new LinearLayoutManager(this);



      /*  LinearLayoutManager layoutManager = new GridLayoutManager(this
                3,
                LinearLayoutManager.HORIZONTAL,
                true
        );


        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(
                3,
                LinearLayoutManager.HORIZONTAL
        );


        CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.HORIZONTAL);
        CarouselLayoutManager layoutManager = new CarouselLayoutManager(CarouselLayoutManager.VERTICAL);*/




        svProduct.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                    newText = newText.toLowerCase();
                    ArrayList<MerchantDetails> newArrayList = new ArrayList<>();
                    for(MerchantDetails merchant:merchantArrayList){
                        String productName = merchant.getMerchantName().toLowerCase();
                        if(productName.contains(newText)){
                            newArrayList.add(merchant);
                        }
                    }
                    merchantSaveAdaper.setFilter(newArrayList);

                return true;
            }
        });

    }

    private void loadMerchantList(){

        String pat = tvPath.getText().toString();

        if(pat.equals("Select your path")){
            Toast.makeText(getApplicationContext(), "You haven't selected a path.", Toast.LENGTH_SHORT).show();
            return;
        }else{
            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                    LinearLayoutManager.VERTICAL,
                    false
            );
            rvSearchItems.setLayoutManager(layoutManager);
            rvSearchItems.setHasFixedSize(true);
            SyncManager syncManager = new SyncManager(getApplicationContext());
            merchantArrayList = syncManager.getMerchantListByPathID(pathCode);
            merchantSaveAdaper = new SearchAdapter(getApplicationContext(),merchantArrayList);
            rvSearchItems.setAdapter(merchantSaveAdaper);
            tvPath.setText("Select your path");
            llSearch.setVisibility(View.VISIBLE);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        rvSearchItems.setLayoutManager(layoutManager);
        rvSearchItems.setHasFixedSize(true);
        SyncManager syncManager = new SyncManager(getApplicationContext());
        merchantArrayList = syncManager.getMerchantListByPathID(pathCode);
        merchantSaveAdaper = new SearchAdapter(getApplicationContext(),merchantArrayList);
        rvSearchItems.setAdapter(merchantSaveAdaper);
    }

    private void loadPath(final ArrayList<DeliveryPath> pathArrayList) {
        pathItems.clear();
        for (int i = 0; i < pathArrayList.size(); i++) {
            pathItems.add(pathArrayList.get(i).getPathName());
        }
        pathSpinnerDialog=new SpinnerDialog(MerchantUpdateActivity.this,pathItems,"Select or Search Path",R.style.DialogAnimations_SmileWindow);

        pathSpinnerDialog.bindOnSpinerListener(new OnSpinerItemClick()
        {
            @Override
            public void onClick(String item, int position)
            {

                tvPath.setText(item);
                selectedPath =item;

                for (int  i = 0 ;i<pathArrayList.size();i++){

                    if (pathArrayList.get(i).getPathName().equals(selectedPath)){
                        pathCode = pathArrayList.get(i).getDeliveryPathId();
                    }
                }

            }
        });

    }
}
