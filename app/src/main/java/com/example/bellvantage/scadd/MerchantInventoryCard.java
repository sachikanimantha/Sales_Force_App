package com.example.bellvantage.scadd;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bellvantage.scadd.Adapters.MerchantInventoryCardAdpter;
import com.example.bellvantage.scadd.Utils.DateManager;
import com.example.bellvantage.scadd.swf.MerchantStock;
import com.example.bellvantage.scadd.DB.DbHelper;
import com.example.bellvantage.scadd.Utils.SessionManager;
import com.example.bellvantage.scadd.Utils.SyncManager;
import com.example.bellvantage.scadd.swf.DistributorDetails;
import com.example.bellvantage.scadd.swf.LoginUser;
import com.example.bellvantage.scadd.swf.MerchantStockLineitems;
import com.example.bellvantage.scadd.swf.ProductListItemPosition;
import com.example.bellvantage.scadd.swf.SalesRep;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MerchantInventoryCard extends AppCompatActivity {

    Toolbar mToolbar;
    RecyclerView recyclerView;
    SearchView searchView;
    Button btn_continue;
    MerchantInventoryCardAdpter merchantInventoryCardAdpter;

    ArrayList<MerchantStockLineitems> allstocklineitems = new ArrayList<>();
    ArrayList<MerchantStockLineitems> selected_items = new ArrayList<>();
    ArrayList<ProductListItemPosition> listItemPositions = new ArrayList<>();

    String merchant_name, userType, sql_d, sql_s;
    int merchant_id, merchant_isvat, userTypeId;

    AlertDialog dialog;
    ArrayList<DistributorDetails> distributorDetailsArray = new ArrayList<>();
    ArrayList<SalesRep> salesRepArray = new ArrayList<>();

    int distri_id, is_distri_vat;
    String distri_name;
    String salesrepType;
    SyncManager sm;
    int customerstock_new;
    int merchant_Stockid = 0,merchant_visit = 0;
    String merchant_stockid = "";
    String today_;
    int pos;
    int isItemChooseFromSV = 0;
    String max_merchantStockId = "";
    String salesrep_name = "";
    int merchantcredit_type = 0,merchantcerdit_dates= 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_inventory_card);

        mToolbar = (Toolbar) findViewById(R.id.tb_main);
        mToolbar.setTitle("Create Sales Order - Step 03");

        //long time = DateManager.todayMillsecWithDateFormat("dd/MM/yyyy hh:mm:ss");
        long time = DateManager.todayMillsecWithDateFormat("yyyy-MM-dd");
        //long time11 = DateManager.anyStringDatetoMills("2014-10-29","yyyy-MM-dd");

        today_ = (new SyncManager(getApplicationContext())).getDate(time, "yyyy-MM-dd");

        System.out.println("today_ - " + today_);





        Gson gson = new Gson();
        String getJson = SessionManager.pref.getString("user", "");
        LoginUser prefUser = gson.fromJson(getJson, LoginUser.class);
        userTypeId = prefUser.getUserTypeId();
        userType = prefUser.getUserType();

        findDistributorANDSalesrepDetails(userType);
        init();

        sm = new SyncManager(getApplicationContext());

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //inserting data to MS STOCK table
                if (selected_items.size() == 0) {
                    displayStatusMessage("You have to add values for Customer stock first", 2);
                    System.out.println("selected_items - "+selected_items);
                }
                if (selected_items.size() != 0) {

                    System.out.println("selected_items - "+selected_items);
                    MerchantStock merchantStock = new MerchantStock(
                            merchant_Stockid, merchant_id, distri_id, userTypeId, today_, salesrep_name/*salesrep name*/,
                            1, 0, "", "", "");

                    //merchant stock line item inserting to sqlite
                    for (int h = 0; h < selected_items.size(); h++) {
                        ContentValues cv2 = (new MerchantStockLineitems(
                                selected_items.get(h).getMerchentStockId(),
                                selected_items.get(h).getProductID(),
                                selected_items.get(h).getProduct_name(),
                                selected_items.get(h).getQuantity(),
                                0,
                                selected_items.get(h).getSyncDate()
                        )).setCVvalues_merchantStocklineitems();

                        boolean succes = (new DbHelper(getApplicationContext())).insertDataAll(cv2, DbHelper.TABLE_MERCHANT_STOCK_LINEITEM);
                        ;
                        if (succes) {
                            System.out.println("success insert MS STOCK LINE ITEM data to SQLite");

                        } else {
                            System.out.println("Error occured insert MS STOCK LINE ITEM data to SQLite");
                        }
                    }

                    //merchant stock line inserting to sqlite
                    ContentValues cv = merchantStock.getCVvalues_merchantStock();
                    boolean success4 = (new DbHelper(getApplicationContext())).insertDataAll(cv, DbHelper.TABLE_MERCHANT_STOCK);
                    if (success4) {
                        System.out.println("success insert MS STOCK data to SQLite");
                    } else {
                        System.out.println("Error occured insert MS STOCK data to SQLite");
                    }


                    if(merchant_visit == 3){
                        displayStatusMessage2("You cannot create sales order",3);


                    } else if (merchant_isvat == 1 && is_distri_vat == 1 && merchant_id != 0 && merchant_visit != 3) {

                        Intent intent = new Intent(MerchantInventoryCard.this, CreateSalesOrderContinue0.class);
                        intent.putExtra("merchantID", merchant_id);
                        intent.putExtra("merchantNAME", merchant_name);
                        intent.putExtra("merchantISVAT", merchant_isvat);
                        intent.putExtra("distributorID", distri_id);
                        intent.putExtra("distributorNAME", distri_name);
                        intent.putExtra("distributorISVAT", is_distri_vat);
                        intent.putExtra("salesrepTYPE", salesrepType);

                        intent.putExtra("merchant_payment_method_1", merchantcredit_type);
                        intent.putExtra("merchant_daycount_1", merchantcerdit_dates);

                        MerchantInventoryCard.this.finish();
                        startActivity(intent);

                    }else {

                        Intent intent = new Intent(MerchantInventoryCard.this, CreateSalesOrderContinue.class);
                        intent.putExtra("merchant_id_", merchant_id);
                        intent.putExtra("merchant_name_", merchant_name);
                        intent.putExtra("merchant_isvat_", merchant_isvat);
                        intent.putExtra("distributor_id", distri_id);
                        intent.putExtra("distributor_name", distri_name);
                        intent.putExtra("distributor_isvat", is_distri_vat);
                        intent.putExtra("salesrep_type", salesrepType);

                        intent.putExtra("merchant_payment_method", merchantcredit_type);
                        intent.putExtra("merchant_daycount", merchantcerdit_dates);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private void init(){

        recyclerView = (RecyclerView) findViewById(R.id.rv_merchant_card);
        searchView = (SearchView) findViewById(R.id.sv_item_mcard);
        btn_continue = (Button) findViewById(R.id.btn_continue_mcard);

        merchant_name = getIntent().getExtras().getString("merchant_name");
        merchant_id = getIntent().getExtras().getInt("merchant_id");
        merchant_isvat = getIntent().getExtras().getInt("merchant_isvat");
        merchant_visit = getIntent().getExtras().getInt("merchant_visit");

        System.out.println("merchant id - " + merchant_id);
        System.out.println("merchant name - " + merchant_name);
        System.out.println("merchant isvat - " + merchant_isvat);
        System.out.println("merchant_visit - " + merchant_visit);

        if ((new SyncManager(getApplicationContext())).getMerchantStockId_accordingToMerchnt(merchant_id).equals("")) {

            int k = 100000 + merchant_id;
            max_merchantStockId = k + "" + 1000;
        } else {
            max_merchantStockId = (new SyncManager(getApplicationContext())).getMerchantStockId_accordingToMerchnt(merchant_id);
        }

        System.out.println("max_merchantStockId -" + max_merchantStockId);

        String s1 = max_merchantStockId.substring(6, 10);
        int s10 = Integer.parseInt(s1);

        if (s10 >= 1000) {
            int s2 = s10 + 1;
            String s20 = String.valueOf(s2);

            merchant_stockid = max_merchantStockId.substring(0, 6) + "" + s20;
            System.out.println("merchant_stockid - " + merchant_stockid);
            merchant_Stockid = Integer.parseInt(merchant_stockid);
        }

        allstocklineitems = sm.getAllProductListFromSQLite();

        for (int i = 0; i < selected_items.size(); i++) {
            listItemPositions.add(new ProductListItemPosition(i, allstocklineitems.get(i).getProductID()));
        }

        merchantInventoryCardAdpter = new MerchantInventoryCardAdpter(allstocklineitems, allstocklineitems,
                getApplicationContext(), new MerchantInventoryCardAdpter.OnItemClickListener() {

            @Override
            public void onItemClick(MerchantStockLineitems lineitems, int position) {
                showCard(lineitems);
                pos = position;
                System.out.println("pos - " + pos);
            }
        });

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(merchantInventoryCardAdpter);

        search(searchView);
        System.out.println("isItemChooseFromSV - " + isItemChooseFromSV);

    }

    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                merchantInventoryCardAdpter.getFilter().filter(newText);
                isItemChooseFromSV = 1;
                return true;
            }
        });

    }

    private void findDistributorANDSalesrepDetails(String userType) {

        //find a distri id according to salesrep is
        String sql0 = "SELECT " + DbHelper.COL_SALES_REP_DistributorId + " FROM " + DbHelper.TABLE_SALES_REP +
                " where " + DbHelper.COL_SALES_REP_SalesRepId + " = " + userTypeId;
        System.out.println("sql0 - " + sql0);

        sm = new SyncManager(this);

        if (userType.equalsIgnoreCase("D")) {
            sql_d = "";
            sql_s = "";

        } else if (userType.equalsIgnoreCase("S")) {
            sql_d = "SELECT * FROM " + DbHelper.TABLE_DISTRIBUTOR;
            sql_s = "SELECT * FROM " + DbHelper.TABLE_SALES_REP;
        }

        try {

            salesRepArray = sm.getSalesRepDetailsFromSQLite(sql_s);
            distributorDetailsArray = sm.getDistributorDetailsFromSQLite(sql_d, userTypeId);

            //sizes should b 1 ,1 bcz only one sales rep and dev
            System.out.println("salesRepArray.size() - " + salesRepArray.size());
            System.out.println("distributorDetailsArray.size() - " + distributorDetailsArray.size());

            distri_id = distributorDetailsArray.get(0).getDistributorId();
            distri_name = distributorDetailsArray.get(0).getDistributorName();
            is_distri_vat = distributorDetailsArray.get(0).getIsVat();
            salesrepType = salesRepArray.get(0).getSalesRepType();
            salesrep_name = salesRepArray.get(0).getSalesRepName();


        } catch (Exception e) {

            displayStatusMessage("Sync again SALESREP and DISTRIBUTOR data", 3);
        }

        System.out.println("salesrepType - " + salesrepType);
        System.out.println("distributor_id - " + distri_id);
        System.out.println("distributor_name - " + distri_name);
        System.out.println("is_distributor_vat - " + is_distri_vat);
    }

    private void showCard(MerchantStockLineitems stockLineitems) {

        System.out.println("isItemChooseFromSV - " + isItemChooseFromSV);
        final int productid = stockLineitems.getProductID();
        final String name = stockLineitems.getProduct_name();
        final int customer_stock = stockLineitems.getQuantity();
        final int merchant_stockid = stockLineitems.getMerchentStockId();
        final int issync = stockLineitems.getIsSync();

        System.out.println("productid - " + productid);
        System.out.println("name - " + name);
        System.out.println("customer_stock - " + customer_stock);
        System.out.println("merchant_stockid - " + merchant_stockid);
        System.out.println("issync - " + issync);

        TextView tv_name, tv_productid;
        final EditText et_cus_qty;
        Button btn_submit, btn_remove;

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MerchantInventoryCard.this);
        View view = getLayoutInflater().inflate(R.layout.layout_for_merchant_card_data, null);
        alertDialog.setView(view);

        dialog = alertDialog.create();
        dialog.show();

        tv_name = (TextView) view.findViewById(R.id.tv_name_mcq);
        tv_productid = (TextView) view.findViewById(R.id.tv_productid_mcq);
        et_cus_qty = (EditText) view.findViewById(R.id.et_customer_stock_mcq);
        btn_submit = (Button) view.findViewById(R.id.btn_submit_mcq);
        btn_remove = (Button) view.findViewById(R.id.btn_remove_mcq);

        tv_name.setText("Product Name - " + name);
        tv_productid.setText("Product ID - " + productid);
        et_cus_qty.setHint("" + customer_stock);

        System.out.println("selected_items.size - " + selected_items.size());

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_cus_qty.getText().toString().equals("")) {
                    Toast.makeText(MerchantInventoryCard.this, "Enter Valid Value", Toast.LENGTH_SHORT).show();
                } else {
                    customerstock_new = Integer.parseInt(et_cus_qty.getText().toString());
                    System.out.println("customerstock - " + customerstock_new);

                    //if choose from searchview~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    if (isItemChooseFromSV == 1) {
                        for (int h = 0; h < allstocklineitems.size(); h++) {
                            if (allstocklineitems.get(h).getProductID() == productid) {
                                pos = h;
                            }
                        }
                        System.out.println("pos - " + pos);
                        allstocklineitems.remove(pos);
                        allstocklineitems.add(pos, new MerchantStockLineitems(
                                merchant_Stockid, productid, name, customerstock_new,
                                1/*this isnt about sync,just for indicate that item selected*/, today_));

                        //put in to a selected array
                        selected_items.add(new MerchantStockLineitems(merchant_Stockid, productid, name, customerstock_new, 0, today_));
                        /*
                        int merchant_stock_id, int product_id, String productname,int quantity, int is_sync, String date
                        */
                        merchantInventoryCardAdpter.update(allstocklineitems);

                        //insert MS LINEITEMS to sqlite
                        if(customer_stock > 0){
                            //should update
                            for(int g=0; g < selected_items.size() ; g++){
                                int po = 0;
                                if(selected_items.get(g).getProductID() == productid && selected_items.get(g).getMerchentStockId() == merchant_stockid ){
                                    po = g;
                                    selected_items.remove(po);
                                    selected_items.add(po,new MerchantStockLineitems(merchant_Stockid, productid, name, customerstock_new, 0, today_));
                                }
                            }
                        }else{
                            // should insert
                            selected_items.add(new MerchantStockLineitems(merchant_Stockid, productid, name, customerstock_new, 0, today_));
                        }
                        recyclerView.setAdapter(merchantInventoryCardAdpter);
                        isItemChooseFromSV = 0;
                    }
                    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    //if not in search view/array
                    if (isItemChooseFromSV == 0) {
                        System.out.println("pos - " + pos);
                        System.out.println("customer old stock - "+allstocklineitems.get(pos).getQuantity());

                        allstocklineitems.remove(pos);
                        allstocklineitems.add(pos, new MerchantStockLineitems(merchant_Stockid, productid, name, customerstock_new, 1, today_));
                        System.out.println("customer new stock - "+allstocklineitems.get(pos).getQuantity());

                        //insert MS LINEITEMS to sqlite
                        if(customer_stock > 0){
                            //should update
                            for(int g=0; g < selected_items.size() ; g++){
                                int po = 0;
                                if(selected_items.get(g).getProductID() == productid && selected_items.get(g).getMerchentStockId() == merchant_stockid ){
                                    po = g;
                                    selected_items.remove(po);
                                    selected_items.add(po,new MerchantStockLineitems(merchant_Stockid, productid, name, customerstock_new, 0, today_));
                                }
                            }
                        }else{
                            //should insert
                            selected_items.add(new MerchantStockLineitems(merchant_Stockid, productid, name, customerstock_new, 0, today_));
                        }
                        merchantInventoryCardAdpter.update(allstocklineitems);
                        recyclerView.setAdapter(merchantInventoryCardAdpter);
                        isItemChooseFromSV = 0;
                    }
                }
                dialog.dismiss();
            }
        });

        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isItemChooseFromSV == 1) {
                    for (int h = 0; h < allstocklineitems.size(); h++) {
                        if (allstocklineitems.get(h).getProductID() == productid) {
                            pos = h;
                        }
                    }
                    System.out.println("allstocklineitems pos - " + pos);
                    allstocklineitems.remove(pos);
                    allstocklineitems.add(pos, new MerchantStockLineitems(0, productid, name, 0, 0, today_));

                    for(int g=0;g < selected_items.size() ; g++){
                        if(selected_items.get(g).getProductID() == productid){
                            System.out.println("1 selected_items.size - "+selected_items.size());
                            selected_items.remove(g);
                            System.out.println("2 selected_items.size - "+selected_items.size());
                        }
                    }
                    merchantInventoryCardAdpter.update(allstocklineitems);
                    isItemChooseFromSV = 0;
                }
                if (isItemChooseFromSV == 0) {
                    //if not in search array
                    System.out.println("pos - " + pos);
                    allstocklineitems.remove(pos);
                    System.out.println("allstocklineitems pos - " + pos);
                    allstocklineitems.add(pos, new MerchantStockLineitems(0, productid, name, 0, 0, today_));

                    for(int g=0;g < selected_items.size() ; g++){
                        if(selected_items.get(g).getProductID() == productid){
                            System.out.println("1 selected_items.size - "+selected_items.size());
                            selected_items.remove(g);
                            System.out.println("2 selected_items.size - "+selected_items.size());
                        }
                    }
                    merchantInventoryCardAdpter.update(allstocklineitems);
                    recyclerView.setAdapter(merchantInventoryCardAdpter);
                    isItemChooseFromSV = 0;
                }
                dialog.dismiss();
            }
        });

    }

    private void displayStatusMessage(String s, int colorValue) {

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

        builder = new AlertDialog.Builder(MerchantInventoryCard.this);
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
            }
        });
    }

    /*

     */

    private void displayStatusMessage2(String s, int colorValue) {

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

        builder = new AlertDialog.Builder(MerchantInventoryCard.this);
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
                finish();
                Intent intent = new Intent(MerchantInventoryCard.this, CreateSalesOrder_PathMerchantSelect.class);
                startActivity(intent);
                alertDialog.dismiss();
            }
        });
    }

}