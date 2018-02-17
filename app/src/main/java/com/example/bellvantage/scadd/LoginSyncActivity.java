package com.example.bellvantage.scadd;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bellvantage.scadd.Adapters.SyncListAdapter;
import com.example.bellvantage.scadd.DB.DbHelper;
import com.example.bellvantage.scadd.Utils.DateManager;
import com.example.bellvantage.scadd.Utils.NetworkConnection;
import com.example.bellvantage.scadd.Utils.SessionManager;
import com.example.bellvantage.scadd.Utils.SyncManager;
import com.example.bellvantage.scadd.swf.ServiceCount;
import com.example.bellvantage.scadd.swf.SyncTables;

import java.util.ArrayList;

import static com.example.bellvantage.scadd.DB.DbHelper.COL_INVOICE_ITEM_SalesRepId;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_INVOICE_JSON_IsSync;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SALES_ORDER_SalesRepId;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SALES_REP_DistributorId;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SALES_REP_SalesRepId;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SRI_SALESREP_ID;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_VEHICLE_INVENTORY_SalesRepID;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_CATEGORY;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_DISTRIBUTOR;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_DISTRICT;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_INVOICE_ITEM;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_INVOICE_JSON;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_MERCHANT;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_MERCHANT_STOCK;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_MVISIT;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_MVISIT_REASON;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_PATH;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_PRIMARY_SALES_INVOICE;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_PRODUCT;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_PRODUCT_BATCH;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_RETURN_TYPE;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_SALESREP_INVENTORY;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_SALESREP_TARGET;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_SALES_ORDER;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_SALES_ORDER_JSON;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_SALES_REP;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_SYNC;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_TARGET_CATEGORY;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_VEHICALE_INVENTORY;


public class LoginSyncActivity extends AppCompatActivity {

    Toolbar mToolBar;
    Button btn_continue;
    private DbHelper db;
    ListView listView;
    ArrayList<SyncTables> ary_name = new ArrayList<>();
    SharedPreferences pref = null;
    SyncListAdapter syncListAdapter;
    int userTYPEID; //salesrep id
    int distributorID;
    String time_;
    String salesrepID_;    //sqlite data belongs user
    String userTYPE;    //s
    //int salesrepID = 0;
    SyncManager syncManager;

    int salesrep = 1, distributor = 1;

    AlertDialog.Builder builder_, builder2_, builder3_;

    Dialog dialog = null;
    AlertDialog.Builder mBuilder;

    TextView tv_status, tv_lastsalesrep, tv_lastdate, tv_lastlogdate, tv_lastsalesrepname;
    ProgressBar pb_status;
    Button btn_closeStatus;
    AlertDialog dialog_, d2, d3;


    public static final String All = "All";
    public static final String Area = "Area";
    public static final String District = "District";
    public static final String Category = "Category";
    public static final String Product = "Product";
    public static final String Product_batch = "Product Batch";
    public static final String Return_type = "Return Type";
    public static final String Path = "Path";
    public static final String Distributor = "Distributor";
    public static final String Salesrep = "Sales Rep";
    public static final String Merchant = "Merchant";
    public static final String Sales_order = "Sales Order";
    public static final String Vehicle_inventory = "Vehicle Inventory";
    public static final String Salesrep_inventory = "Salesrep Inventory";
    public static final String Invoice = "Invoice";
    public static final String MerchantStock = "Merchant Stock";
    public static final String MerchantReason = "Merchant Reason";
    public static final String MerchantVisitCounts = "Merchant Visit Count";
    public static final String salesorder_lineitem = "Sales Order Line Item";
    public static final String Invoice_line_item = "Invoice Line Item";

    int all_old, all_new;
    int district_old, district_new, district_diffence;
    int pro_cat_old, pro_cat_new, pro_cat_diffence;
    int product_old, product_new, product_diffence;
    int ProductBatch_old, ProductBatch_new, ProductBatch_diffence;
    int ReturnType_old, ReturnType_new, ReturnType_diffence;
    int Path_old, Path_new, Path_diffence;
    int Merchant_old, Merchant_new, Merchant_diffence;
    int SalesOrder_old, SalesOrder_new, SalesOrder_diffence;
    int VehicleInven_old, VehicleInven_new, VehicleInven_diffence;
    int SalesRepInven_old, SalesRepInven_new, SalesRepInven_diffence;
    int Invoice_old, Invoice_new, Invoice_diffence;
    int merchantstock_old, merchantstock_new, merchantstock_diffence;
    int merchantReason_old, merchantReason_new;
    int merchantVisitCount_old, merchantVisitCount_new;
    int primaryInvoice_old, primaryInvoice_new;
    int target_old, target_new;
    int targetCate_old, targetCate_new;


    ArrayList<ServiceCount> counts_new;
    int syncimg = R.mipmap.sync_img;
    int sync_probimg = R.mipmap.sync_prob_img;
    int sync_nodata = R.mipmap.sync_nodata;
    String time;
    int ROWS;

    String[] tbl_list = {"", TABLE_DISTRICT, TABLE_CATEGORY, TABLE_PRODUCT, TABLE_PRODUCT_BATCH, TABLE_RETURN_TYPE,
            TABLE_PATH, TABLE_DISTRIBUTOR, TABLE_SALES_REP, TABLE_MERCHANT, TABLE_SALES_ORDER, TABLE_VEHICALE_INVENTORY,
            TABLE_SALESREP_INVENTORY, TABLE_INVOICE_ITEM, TABLE_MERCHANT_STOCK, TABLE_MVISIT_REASON, TABLE_MVISIT,
            TABLE_PRIMARY_SALES_INVOICE, TABLE_SALESREP_TARGET, TABLE_TARGET_CATEGORY};

    String UserName, lastlogtime, logtime;
    int lastsalesrepid_;
    SharedPreferences sp_cou;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sync2);

        syncManager = new SyncManager(LoginSyncActivity.this);
        syncManager.getMerchantClass();
        mToolBar = (Toolbar) findViewById(R.id.tb_main2);
        mToolBar.setTitle("Synchronize Tables");


        time = (new DateManager()).getDateWithTime();
        tv_lastdate = (TextView) findViewById(R.id.tv_lastlogged_date2);
        tv_lastsalesrep = (TextView) findViewById(R.id.tv_lastsalesrep2);

        tv_lastsalesrepname = (TextView) findViewById(R.id.tv_lastsalesrepname2);
        tv_lastlogdate = (TextView) findViewById(R.id.tv_lastloggeddate2);


        btn_continue = (Button) findViewById(R.id.btn_continue2);
        listView = (ListView) findViewById(R.id.lv_sync_list2);

        userTYPE = getIntent().getExtras().getString("userTYPE");
        userTYPEID = getIntent().getExtras().getInt("userID");
        UserName = getIntent().getExtras().getString("salesrepName");
        logtime = getIntent().getExtras().getString("logTime");
        lastsalesrepid_ = getIntent().getExtras().getInt("lastSalesRep");
        lastlogtime = getIntent().getExtras().getString("lastSalesRepLogTime");

        System.out.println("userTYPE - " + userTYPE);
        System.out.println("userTYPEID - " + userTYPEID);
        System.out.println("UserName - " + UserName);
        System.out.println("logtime - " + logtime);
        System.out.println("lastsalesrepid_ - " + lastsalesrepid_);
        System.out.println("lastlogtime - " + lastlogtime);

        builder_ = new AlertDialog.Builder(LoginSyncActivity.this);
        View view_ = getLayoutInflater().inflate(R.layout.layout_for_progress, null);
        builder_.setView(view_);
        dialog_ = builder_.create();
        dialog_.setCancelable(false);
        dialog_.setCanceledOnTouchOutside(false);

        tv_status = (TextView) view_.findViewById(R.id.tv_status);
        pb_status = (ProgressBar) view_.findViewById(R.id.pb_status);
        btn_closeStatus = (Button) view_.findViewById(R.id.btn_close_status);


        (new SyncManager(getApplicationContext())).updateSyncTable_lastlogvalue(TABLE_SYNC, time, userTYPEID);

        ary_name = (new SyncManager(getApplicationContext())).getSyncTablesFromSQLite();
        for (int i = 0; i < 20; i++) {
            if (ary_name.get(i).getStatus() == 1) {

                if (ary_name.get(i).getRowcount() == 0) {
                    ary_name.get(i).setStatus(sync_nodata);
                } else {
                    ary_name.get(i).setStatus(syncimg);
                }

            } else if (ary_name.get(i).getStatus() == 0) {
                if (ary_name.get(i).getRowcount() == 0) {
                    ary_name.get(i).setStatus(sync_nodata);
                } else {
                    ary_name.get(i).setStatus(sync_probimg);
                }
            }
        }

        if (userTYPE.equalsIgnoreCase("D")) {
            distributorID = getIntent().getExtras().getInt("disID");

        } else if (userTYPE.equalsIgnoreCase("S")) {
            try {
                counts_new = (new SyncManager(getApplicationContext())).getServiceCountsDataFromSQLite();
                district_new = counts_new.get(0).getDistrictCount();
                pro_cat_new = counts_new.get(0).getProductCategoryCount();
                product_new = counts_new.get(0).getProductCount();
                ProductBatch_new = counts_new.get(0).getProductBatchCount();
                ReturnType_new = counts_new.get(0).getReturnTypeCount();
                Path_new = counts_new.get(0).getPathCount();
                Merchant_new = counts_new.get(0).getMerchantCount();
                SalesOrder_new = counts_new.get(0).getSalesOrderCount();
                VehicleInven_new = counts_new.get(0).getVehicleInventoryCount();
                SalesRepInven_new = counts_new.get(0).getSalesRepInventoryCount();
                Invoice_new = counts_new.get(0).getInvoiceCount();
                merchantstock_new = counts_new.get(0).getMerchantStockCount();
                merchantReason_new = counts_new.get(0).getMerchantVisitReasonCount();
                merchantVisitCount_new = counts_new.get(0).getMerchantVisitDetailsCount();

                primaryInvoice_new = counts_new.get(0).getPrimarySalesInvoiceCount();
                target_new = counts_new.get(0).getSalesRepTargetCount();
                targetCate_new = counts_new.get(0).getDefTargetCategoryCount();

                all_new = district_new + pro_cat_new + product_new + ProductBatch_new + ReturnType_new + distributor + salesrep +//for distributor and salesrep
                        Path_new + Merchant_new + SalesOrder_new + VehicleInven_new + SalesRepInven_new + Invoice_new + merchantstock_new
                        + merchantReason_new + merchantVisitCount_new +
                        primaryInvoice_new + target_new + targetCate_new;

                System.out.println("all new - first " + all_new);

            } catch (Exception e) {
                System.out.println("error - " + e.toString());
            }

            district_old = (new DbHelper(getApplicationContext())).totalROWS(TABLE_DISTRICT);
            pro_cat_old = (new DbHelper(getApplicationContext())).totalROWS(TABLE_CATEGORY);
            product_old = (new DbHelper(getApplicationContext())).totalROWS(TABLE_PRODUCT);
            ProductBatch_old = (new DbHelper(getApplicationContext())).totalROWS(TABLE_PRODUCT_BATCH);
            ReturnType_old = (new DbHelper(getApplicationContext())).totalROWS(TABLE_RETURN_TYPE);
            //no need to use salesrep id
            Path_old = (new DbHelper(getApplicationContext())).totalROWS(TABLE_PATH);
            Merchant_old = (new DbHelper(getApplicationContext())).totalROWS(TABLE_MERCHANT);
            SalesOrder_old = (new DbHelper(getApplicationContext())).totalROWS_where_Id(TABLE_SALES_ORDER, COL_SALES_ORDER_SalesRepId, userTYPEID);
            VehicleInven_old = (new DbHelper(getApplicationContext())).totalROWS_where_Id(TABLE_VEHICALE_INVENTORY, COL_VEHICLE_INVENTORY_SalesRepID, userTYPEID);
            SalesRepInven_old = (new DbHelper(getApplicationContext())).totalROWS_where_Id(TABLE_SALESREP_INVENTORY, COL_SRI_SALESREP_ID, userTYPEID);
            Invoice_old = (new DbHelper(getApplicationContext())).totalROWS_where_Id(TABLE_INVOICE_ITEM, COL_INVOICE_ITEM_SalesRepId, userTYPEID);
            merchantstock_old = (new DbHelper(getApplicationContext())).totalROWS(TABLE_MERCHANT_STOCK);
            merchantReason_old = (new DbHelper(getApplicationContext())).totalROWS(TABLE_MVISIT_REASON);
            merchantVisitCount_old = (new DbHelper(getApplicationContext())).totalROWS(TABLE_MVISIT);

            primaryInvoice_old = (new DbHelper(getApplicationContext())).totalROWS(TABLE_PRIMARY_SALES_INVOICE);
            target_old = (new DbHelper(getApplicationContext())).totalROWS(TABLE_SALESREP_TARGET);
            targetCate_old = (new DbHelper(getApplicationContext())).totalROWS(TABLE_TARGET_CATEGORY);

            all_old = district_old + pro_cat_old + product_old + ProductBatch_old + ReturnType_old + Path_old + distributor + salesrep + Merchant_old +
                    SalesOrder_old + VehicleInven_old + SalesRepInven_old + Invoice_old + merchantstock_old
                    + merchantReason_old + merchantVisitCount_old +
                    primaryInvoice_old + target_old + targetCate_old;

            if (userTYPEID == lastsalesrepid_) {
                tv_lastsalesrep.setText(": "+UserName);
                tv_lastsalesrepname.setText(" Full Name ");
                tv_lastdate.setText(": "+lastlogtime);
                tv_lastlogdate.setText(" Last time you log ");

            } else if (userTYPEID != lastsalesrepid_) {

                tv_lastsalesrep.setText(": " + lastsalesrepid_);
                tv_lastsalesrepname.setText(" Last Salesrep ID ");
                tv_lastdate.setText(": " + lastlogtime);
                tv_lastlogdate.setText(" Last logged time ");
            }

            String sql = "SELECT " + COL_SALES_REP_DistributorId + " FROM " + TABLE_SALES_REP + " WHERE " + COL_SALES_REP_SalesRepId + " = " + userTYPEID;
            //System.out.println("sql dis id- " + sql);
            try {
                distributorID = Integer.parseInt((new SyncManager(getApplicationContext())).getStringValueFromSQLite(sql, COL_SALES_REP_DistributorId));

            } catch (Exception e) {
                System.out.println("distributor id error - " + e.toString());
            }


            final int[] Rows_from_server = {all_new, district_new, pro_cat_new, product_new, ProductBatch_new, ReturnType_new, Path_new, 1,
                    1, Merchant_new, SalesOrder_new, VehicleInven_new, SalesRepInven_new,
                    Invoice_new, merchantstock_new, merchantReason_new, merchantVisitCount_new, primaryInvoice_new, target_new, targetCate_new};
            System.out.println("all_new  2nd - " + all_new);

            if (lastsalesrepid_ == userTYPEID) { //same user logged after last time logged out
                System.out.println("inside of old db");
                displayStatusMessage("You might not have latest data.\nPlease make sure to update", 3);

                final int[] Rows_from_sqlite = {all_old, district_old, pro_cat_old, product_old, ProductBatch_old, ReturnType_old, Path_old, distributor = 1,
                        salesrep = 1, Merchant_old, SalesOrder_old, VehicleInven_old, SalesRepInven_old,
                        Invoice_old, merchantstock_old, merchantReason_old, merchantVisitCount_old, primaryInvoice_old, target_old, targetCate_old};

                syncListAdapter = new SyncListAdapter(getApplicationContext(), R.layout.layout_for_sync_manually, ary_name);

                for (int i = 0; i < 20; i++) {
                    if (Rows_from_sqlite[i] == Rows_from_server[i]) {
                        syncListAdapter.updateList(ary_name, syncimg, time, Rows_from_sqlite[i], Rows_from_server[i], i);
                    } else {
                        syncListAdapter.updateList(ary_name, sync_probimg, time, Rows_from_sqlite[i], Rows_from_server[i], i);
                    }
                }
                listView.setAdapter(syncListAdapter);
                syncListAdapter.notifyDataSetChanged();

            } else if (lastsalesrepid_ != userTYPEID) {


                System.out.println("this salesrep wasn't logged before");
                displayStatusMessage("You have to sync tables Manually", 2);

                district_old = pro_cat_old = product_old = ProductBatch_old = ReturnType_old = Path_old = distributor =
                        salesrep = Merchant_old = SalesOrder_old = VehicleInven_old = SalesRepInven_old = Invoice_old
                                = merchantstock_old = merchantReason_old = merchantVisitCount_old =
                                primaryInvoice_old = target_old = targetCate_old = 0;

                boolean b_district = (new DbHelper(getApplicationContext())).deleteAllData(TABLE_DISTRICT);
                boolean b_cat = (new DbHelper(getApplicationContext())).deleteAllData(TABLE_CATEGORY);
                boolean b_pro = (new DbHelper(getApplicationContext())).deleteAllData(TABLE_PRODUCT);
                boolean b_pro_bat = (new DbHelper(getApplicationContext())).deleteAllData(TABLE_PRODUCT_BATCH);
                boolean b_return_type = (new DbHelper(getApplicationContext())).deleteAllData(TABLE_RETURN_TYPE);
                boolean b_path = (new DbHelper(getApplicationContext())).deleteAllData(TABLE_PATH);
                boolean b_merch = (new DbHelper(getApplicationContext())).deleteAllData(TABLE_MERCHANT);
                boolean b_salesorder = (new DbHelper(getApplicationContext())).deleteAllData(TABLE_SALES_ORDER);
                boolean b_vehicl = (new DbHelper(getApplicationContext())).deleteAllData(TABLE_VEHICALE_INVENTORY);
                boolean b_salesrepinv = (new DbHelper(getApplicationContext())).deleteAllData(TABLE_SALESREP_INVENTORY);
                boolean b_invoice = (new DbHelper(getApplicationContext())).deleteAllData(TABLE_INVOICE_ITEM);
                boolean b_merStock = (new DbHelper(getApplicationContext())).deleteAllData(TABLE_MERCHANT_STOCK);
                boolean b_merReason = (new DbHelper(getApplicationContext())).deleteAllData(TABLE_MVISIT_REASON);
                boolean b_merVisitCount = (new DbHelper(getApplicationContext())).deleteAllData(TABLE_MVISIT);

                boolean b_primaryinvoice = (new DbHelper(getApplicationContext())).deleteAllData(TABLE_PRIMARY_SALES_INVOICE);
                boolean b_target = (new DbHelper(getApplicationContext())).deleteAllData(TABLE_SALESREP_TARGET);
                boolean b_targetCate = (new DbHelper(getApplicationContext())).deleteAllData(TABLE_TARGET_CATEGORY);

                if (b_district && b_cat && b_pro && b_pro_bat && b_return_type && b_path && b_merch
                        && b_salesorder && b_vehicl && b_salesrepinv && b_invoice && b_merStock && b_merReason
                        && b_merVisitCount && b_primaryinvoice && b_target && b_targetCate) {
                    System.out.println("delete all data");
                }
                syncListAdapter = new SyncListAdapter(getApplicationContext(), R.layout.layout_for_sync_manually, ary_name);

                for (int i = 0; i < 20; i++) {

                    if (i == 0) {

                        syncListAdapter.updateList(ary_name, sync_probimg, time, 0, Rows_from_server[i], i);

                    } else if (i == 8) {
                        (new SyncManager(getApplicationContext())).updateSyncTable(TABLE_SALES_REP, time, 1, 1, userTYPEID, 1);
                        syncListAdapter.updateList(ary_name, syncimg, time, 1, 1, 8);
                        (new SyncManager(getApplicationContext())).updateSyncTable(TABLE_DISTRIBUTOR, time, 1, 1, userTYPEID, 1);
                        syncListAdapter.updateList(ary_name, syncimg, time, 1, 1, 7);

                    } else if (i != 7 || i != 8) {
                        try {
                            (new SyncManager(getApplicationContext())).updateSyncTable(tbl_list[i], time, 0, Rows_from_server[i], userTYPEID, 0);
                            syncListAdapter.updateList(ary_name, sync_probimg, time, 0, Rows_from_server[i], i);

                        } catch (Exception e) {
                            System.out.println("error - " + e.toString());
                        }
                    }

                    syncListAdapter.updateList(ary_name, syncimg, time, 1, 1, 7);
                }
            }
        }

        listView.setAdapter(syncListAdapter);
        syncListAdapter.notifyDataSetChanged();

        System.out.println("district_new - " + district_new);
        System.out.println("pro_cat_new - " + pro_cat_new);
        System.out.println("product_new - " + product_new);
        System.out.println("ProductBatch_new - " + ProductBatch_new);
        System.out.println("ReturnType_new - " + ReturnType_new);
        System.out.println("Path_new - " + Path_new);
        System.out.println("Merchant_new - " + Merchant_new);
        System.out.println("SalesOrder_new - " + SalesOrder_new);
        System.out.println("VehicleInven_new - " + VehicleInven_new);
        System.out.println("SalesRepInven_new - " + SalesRepInven_new);
        System.out.println("Invoice_new - " + Invoice_new);
        System.out.println("MerchantStock_new - " + merchantstock_new);
        System.out.println("MerchantReason_new - " + merchantReason_new);
        System.out.println("MerchantVisitCount_new - " + merchantVisitCount_new);
        System.out.println("primaryInvoice_new - " + primaryInvoice_new);
        System.out.println("target_new - " + target_new);
        System.out.println("targetCate_new - " + targetCate_new);
        System.out.println("all_new - " + all_new + " ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");

        System.out.println("district_old size - " + district_old);
        System.out.println("pro_cat_old size - " + pro_cat_old);
        System.out.println("product_old size - " + product_old);
        System.out.println("ProductBatch_old size - " + ProductBatch_old);
        System.out.println("ReturnType_old size - " + ReturnType_old);
        System.out.println("Path_old size - " + Path_old);
        System.out.println("distributor size - " + distributor);
        System.out.println("salesrep size - " + salesrep);
        System.out.println("Merchant_old size - " + Merchant_old);
        System.out.println("SalesOrder_old size - " + SalesOrder_old);
        System.out.println("VehicleInven_old size - " + VehicleInven_old);
        System.out.println("SalesRepInven_old size - " + SalesRepInven_old);
        System.out.println("Invoice_old size - " + Invoice_old);
        System.out.println("MerchantStock_old size - " + merchantstock_old);
        System.out.println("merchantReason_old size - " + merchantReason_old);
        System.out.println("MerchantVisitCount_old size - " + merchantVisitCount_old);
        System.out.println("primaryInvoice_old - " + primaryInvoice_old);
        System.out.println("target_old - " + target_old);
        System.out.println("targetCate_old - " + targetCate_old);
        System.out.println("all_old - " + all_old + " ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {

                final int[] Rows_from_server = {all_new, district_new, pro_cat_new, product_new, ProductBatch_new, ReturnType_new, Path_new, distributor = 1,
                        salesrep = 1, Merchant_new, SalesOrder_new, VehicleInven_new, SalesRepInven_new,
                        Invoice_new, merchantstock_new, merchantReason_new, merchantVisitCount_new,
                        primaryInvoice_new,
                        target_new, targetCate_new};


                final int[] Rows_from_sqlite = {all_old, district_old, pro_cat_old, product_old, ProductBatch_old, ReturnType_old, Path_old, 1,
                        1, Merchant_old, SalesOrder_old, VehicleInven_old, SalesRepInven_old,
                        Invoice_old, merchantstock_old, merchantReason_old, merchantVisitCount_old, primaryInvoice_old,
                        target_old, targetCate_old};


                int ID = ary_name.get(position).getId();
                String NAME = ary_name.get(position).getName();
                String TIME = ary_name.get(position).getTime();
                int ROWS = ary_name.get(position).getRowcount();
                int ROWS_NEW = ary_name.get(position).getRowcount_new();
                int STATUS = ary_name.get(position).getStatus();

                System.out.println("id - " + ID + ", name - " + NAME + ", TIME - " + TIME + ", ROWS - " + ROWS + ",STATUS - " + STATUS + ", ROWS_NEW - " + ROWS_NEW);

                btn_closeStatus.setEnabled(false);
                btn_closeStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (position == 0) {//all rows sync
                            for (int i = 0; i < 20; i++) {
                                if (i == 0) {
                                    if (Rows_from_sqlite[i] == Rows_from_server[i]) {
                                        syncListAdapter.updateList(ary_name, syncimg, time, Rows_from_sqlite[i], Rows_from_server[i], i);

                                    } else if (Rows_from_sqlite[i] == 0 && Rows_from_server[i] == 0) {
                                        syncListAdapter.updateList(ary_name, sync_nodata, time, Rows_from_sqlite[i], Rows_from_server[i], i);

                                    } else if (Rows_from_sqlite[i] != Rows_from_server[i]) {
                                        syncListAdapter.updateList(ary_name, sync_probimg, time, Rows_from_sqlite[i], Rows_from_server[i], i);
                                    }
                                }
                                if (i != 0) {
                                    Rows_from_sqlite[i] = (new DbHelper(getApplicationContext())).totalROWS(tbl_list[i]);
                                    System.out.println("Rows_sqlite[i] - " + Rows_from_sqlite[i]);
                                    System.out.println("Rows_server[i] - " + Rows_from_server[i]);

                                    if (Rows_from_sqlite[i] == Rows_from_server[i]) {
                                        syncListAdapter.updateList(ary_name, syncimg, time, Rows_from_sqlite[i], Rows_from_server[i], i);

                                    } else if (Rows_from_sqlite[i] == 0 && Rows_from_server[i] == 0) {
                                        syncListAdapter.updateList(ary_name, sync_nodata, time, Rows_from_sqlite[i], Rows_from_server[i], i);

                                    } else if (Rows_from_sqlite[i] != Rows_from_server[i]) {
                                        syncListAdapter.updateList(ary_name, sync_probimg, time, Rows_from_sqlite[i], Rows_from_server[i], i);

                                    }
                                }
                            }
                        }
                        if (position == 8) {

                            if (Rows_from_sqlite[position] == Rows_from_server[position]) {
                                syncListAdapter.updateList(ary_name, syncimg, time, Rows_from_sqlite[8], Rows_from_server[8], 8);
                                syncListAdapter.updateList(ary_name, syncimg, time, Rows_from_sqlite[7], Rows_from_server[7], 7);

                            } else if (Rows_from_sqlite[position] == 0 && Rows_from_server[position] == 0) {
                                syncListAdapter.updateList(ary_name, sync_nodata, time, Rows_from_sqlite[8], Rows_from_server[8], 8);
                                syncListAdapter.updateList(ary_name, sync_nodata, time, Rows_from_sqlite[7], Rows_from_server[7], 7);

                            } else if (Rows_from_sqlite[position] != Rows_from_server[position]) {
                                syncListAdapter.updateList(ary_name, sync_probimg, time, Rows_from_sqlite[8], Rows_from_server[8], 8);
                                syncListAdapter.updateList(ary_name, sync_probimg, time, Rows_from_sqlite[7], Rows_from_server[7], 7);

                            }


                        }
                        if (position != 0 && position != 8 && position != 7) {
                            Rows_from_sqlite[position] = (new DbHelper(getApplicationContext())).totalROWS(tbl_list[position]);
                            System.out.println("Rows_from_sqlite[position] - " + Rows_from_sqlite[position]);
                            System.out.println("Rows_from_server[position] - " + Rows_from_server[position]);

                            if (Rows_from_sqlite[position] == Rows_from_server[position]) {
                                syncListAdapter.updateList(ary_name, syncimg, time, Rows_from_sqlite[position], Rows_from_server[position], position);
                                syncListAdapter.updateList(ary_name, sync_probimg, time, Rows_from_sqlite[0], Rows_from_server[0], 0);

                            } else if (Rows_from_sqlite[position] == 0 && Rows_from_server[position] == 0) {
                                syncListAdapter.updateList(ary_name, sync_nodata, time, Rows_from_sqlite[position], Rows_from_server[position], position);
                                syncListAdapter.updateList(ary_name, sync_probimg, time, Rows_from_sqlite[0], Rows_from_server[0], 0);

                            } else if (Rows_from_sqlite[position] != Rows_from_server[position]) {
                                syncListAdapter.updateList(ary_name, sync_probimg, time, Rows_from_sqlite[position], Rows_from_server[position], position);
                                syncListAdapter.updateList(ary_name, sync_probimg, time, Rows_from_sqlite[0], Rows_from_server[0], 0);

                            }
                        }
                        dialog_.dismiss();
                    }
                });

                syncManager = new SyncManager(getApplicationContext());

                switch (position) {

                    case 0:
                        pb_status.setMax(all_new);
                        System.out.println("all new - " + all_new);
                        if (new NetworkConnection().checkNetworkConnection(getApplicationContext())) {
                            System.out.println("getUnsyncSalesOrderCount_forLogginSync() - " + getUnsyncSalesOrderCount_forLogginSync());
                            System.out.println("getInvoiceSyncCount_forLoggingSync() - " + getInvoiceSyncCount_forLoggingSync());
                            if (getUnsyncSalesOrderCount_forLogginSync() == 0 && getInvoiceSyncCount_forLoggingSync() == 0) {
                                syncManager.syncDistrict(1, userTYPEID);
                                (new loginAsyncTask()).execute(all_new);
                                dialog_.show();
                            } else {
                                displayMessage2("Please Sync Salesorders,Invoice ", 2);
                            }
                        } else {
                            displayStatusMessage("Network connection failure", 2);
                        }
                        break;

                    case 1:
                        pb_status.setMax(district_new);
                        if (new NetworkConnection().checkNetworkConnection(getApplicationContext())) {
                            (new loginAsyncTask()).execute(district_new);
                            syncManager.syncDistrict(0, userTYPEID);
                            dialog_.show();
                            //syncManager.syncMerchantStockData(userTYPEID);
                        } else {
                            displayStatusMessage("Network connection failure", 2);
                        }
                        break;

                    case 2:
                        pb_status.setMax(pro_cat_new);

                        if (new NetworkConnection().checkNetworkConnection(getApplicationContext())) {
                            (new loginAsyncTask()).execute(pro_cat_new);
                            syncManager.getCategoryFromServer(0, userTYPEID);
                            dialog_.show();
                        } else {
                            displayStatusMessage("Network connection failure", 2);
                        }
                        break;

                    case 3:
                        pb_status.setMax(product_new);

                        if (new NetworkConnection().checkNetworkConnection(getApplicationContext())) {
                            (new loginAsyncTask()).execute(product_new);
                            syncManager.getDefProduct(0, userTYPEID);
                            dialog_.show();
                        } else {
                            displayStatusMessage("Network connection failure", 2);
                        }

                        break;

                    case 4:
                        pb_status.setMax(ProductBatch_new);

                        if (new NetworkConnection().checkNetworkConnection(getApplicationContext())) {
                            (new loginAsyncTask()).execute(ProductBatch_new);
                            syncManager.getProductBatchList(0, userTYPEID);
                            dialog_.show();
                        } else {
                            displayStatusMessage("Network connection failure", 2);
                        }
                        break;

                    case 5:
                        pb_status.setMax(ReturnType_new);

                        if (new NetworkConnection().checkNetworkConnection(getApplicationContext())) {

                            (new loginAsyncTask()).execute(ReturnType_new);
                            syncManager.getReturnTypesFromServer(0, userTYPEID);
                            dialog_.show();
                        } else {
                            displayStatusMessage("Network connection failure", 2);
                        }
                        break;

                    case 6:

                        pb_status.setMax(Path_new);

                        if (new NetworkConnection().checkNetworkConnection(getApplicationContext())) {
                            (new loginAsyncTask()).execute(Path_new);
                            syncManager.syncPath(0, userTYPEID);
                            dialog_.show();
                        } else {
                            displayStatusMessage("Network connection failure", 2);
                        }
                        break;


                    case 7:

                        if (new NetworkConnection().checkNetworkConnection(getApplicationContext())) {
                            displayStatusMessage("Use SALESREP row for update this.", 3);
                        } else {
                            displayStatusMessage("Network connection failure", 2);
                        }
                        break;

                    case 8:

                        pb_status.setMax(salesrep);
                        if (new NetworkConnection().checkNetworkConnection(getApplicationContext())) {
                            (new loginAsyncTask()).execute(salesrep);
                            syncManager.getSalesRep_and_DistributorDetailsFromServer_accordingto_salesrep(0, userTYPEID);
                            dialog_.show();
                        } else {
                            displayStatusMessage("Network connection failure", 2);
                        }
                        break;

                    case 9:
                        pb_status.setMax(Merchant_new);
                        if (new NetworkConnection().checkNetworkConnection(getApplicationContext())) {
                            (new loginAsyncTask()).execute(Merchant_new);
                            syncManager.syncMerchantTableIntoSqlite(0, userTYPEID);
                            dialog_.show();
                        } else {
                            displayStatusMessage("Network connection failure", 2);
                        }

                        break;

                    case 10:
                        pb_status.setMax(SalesOrder_new);

                        if (new NetworkConnection().checkNetworkConnection(getApplicationContext())) {
                            if (getUnsyncSalesOrderCount_forLogginSync() == 0) {
                                (new loginAsyncTask()).execute(SalesOrder_new);
                                syncManager.getAllSalesOrdersFromServer(0, userTYPEID);
                                dialog_.show();
                            } else {
                                displayMessage2("Please Sync SalesOrders ", 2);
                            }
                        } else {
                            displayStatusMessage("Network connection failure", 2);
                        }
                        break;


                    case 11:
                        pb_status.setMax(VehicleInven_new);
                        if (new NetworkConnection().checkNetworkConnection(getApplicationContext())) {
                            (new loginAsyncTask()).execute(VehicleInven_new);
                            syncManager.getVehicleInventory(0, userTYPEID);
                            dialog_.show();
                        } else {
                            displayStatusMessage("Network connection failure", 2);
                        }
                        break;

                    case 12:
                        pb_status.setMax(SalesRepInven_new);
                        if (new NetworkConnection().checkNetworkConnection(getApplicationContext())) {
                            (new loginAsyncTask()).execute(SalesRepInven_new);
                            syncManager.getSalesRepInventoryFromServer(0, userTYPEID);
                            dialog_.show();
                        } else {
                            displayStatusMessage("Network connection failure", 2);
                        }
                        break;

                    case 13:
                        pb_status.setMax(Invoice_new);
                        if (new NetworkConnection().checkNetworkConnection(getApplicationContext())) {
                            if (getInvoiceSyncCount_forLoggingSync() == 0) {
                                (new loginAsyncTask()).execute(Invoice_new);
                                syncManager.geTSalesrepInvoice_fromSERVER(0, userTYPEID);
                                dialog_.show();
                            } else {
                                displayMessage2("Please Sync Invoice", 2);
                            }
                        } else {
                            displayStatusMessage("Network connection failure", 2);
                        }
                        break;

                    case 14:
                        pb_status.setMax(merchantstock_new);
                        if (new NetworkConnection().checkNetworkConnection(getApplicationContext())) {
                            (new loginAsyncTask()).execute(merchantstock_new);
                            //syncManager.syncMerchantStockData(0,userTYPEID);
                            syncManager.uploadMerchantStockData(0, userTYPEID);
                            dialog_.show();
                        } else {
                            displayStatusMessage("Network connection failure", 2);
                        }
                        break;

                    case 15:
                        pb_status.setMax(merchantReason_new);
                        if (new NetworkConnection().checkNetworkConnection(getApplicationContext())) {
                            (new loginAsyncTask()).execute(merchantReason_new);
                            syncManager.getMerchantVisitReasonsData(0, userTYPEID);
                            dialog_.show();
                        } else {
                            displayStatusMessage("Network connection failure", 2);
                        }
                        break;

                    case 16:
                        pb_status.setMax(merchantVisitCount_new);
                        if (new NetworkConnection().checkNetworkConnection(getApplicationContext())) {
                            (new loginAsyncTask()).execute(merchantVisitCount_new);
                            merchantVisitCount_new = syncManager.syncMerchantVisitData(0, userTYPEID);
                            dialog_.show();
                        } else {
                            displayStatusMessage("Network connection failure", 2);
                        }
                        break;


                    case 17:
                        pb_status.setMax(primaryInvoice_new);
                        if (new NetworkConnection().checkNetworkConnection(getApplicationContext())) {
                            (new loginAsyncTask()).execute(primaryInvoice_new);

                            syncManager.getPrimarySalesInvoiceBySalesRep(0, userTYPEID);//sales rep id

                            dialog_.show();
                        } else {
                            displayStatusMessage("Network connection failure", 2);
                        }
                        break;


                    case 18:
                        pb_status.setMax(target_new);
                        if (new NetworkConnection().checkNetworkConnection(getApplicationContext())) {
                            (new loginAsyncTask()).execute(target_new);
                            dialog_.show();
                            syncManager.getSalesRepTargetBySalesRepId(0, userTYPEID);//sales rep id
                        } else {
                            displayStatusMessage("Network connection failure", 2);
                        }
                        break;


                    case 19:
                        pb_status.setMax(targetCate_new);
                        if (new NetworkConnection().checkNetworkConnection(getApplicationContext())) {
                            (new loginAsyncTask()).execute(targetCate_new);

                            syncManager.getTargetProductCategoryList();
                            syncManager.getDefTargetCategoryLineItems();
                            dialog_.show();
                        } else {
                            displayStatusMessage("Network connection failure", 2);
                        }
                        break;


                }
            }
        });


        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginSyncActivity.this, MainActivity.class);
                //intent.putExtra("distributorID",)
                startActivity(intent);
            }
        });


    }

    @Override
    public void onBackPressed() {

        displayMessage("Are yoe sure want to exit? ", 3);
    }

    public class loginAsyncTask extends AsyncTask<Integer, Integer, Integer> {
        //param,pross,result

        int tablerows;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tv_status.setText("Starting ... ");
            pb_status.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            tablerows = params[0];
            int threadtime = 100;

            if (tablerows == 0) {
                tablerows = 100;
                threadtime = 100;
            }

            if (tablerows >= 3000) {
                threadtime = 10;
            }

            if (tablerows > 1000 && tablerows < 3000) {
                threadtime = 20;
            }


            int perce = 0;
            int k = 0;
            for (k = 0; k < tablerows; k++) {

                //perce = (k/tablerows)*100;
                //System.out.println(k);
                //System.out.println("percent - "+perce);

                try {
                    Thread.sleep(threadtime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(k);
            }
            return k;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            String text = "";
            int pbProgress = 0;
            String txtTABLE = "";

            if (tablerows == 0) {
                tablerows = 100;
                text = "Server Error ";
            }

            text = "Updating - (" + values[0] + "/" + tablerows + ")";
            //text = "Updating - ( " + values[0] + "% )";
            pbProgress = values[0];

            tv_status.setText(text);
            pb_status.setProgress(pbProgress);
        }

        @Override
        protected void onPostExecute(Integer integers) {
            super.onPostExecute(integers);
            pb_status.setVisibility(View.GONE);
            btn_closeStatus.setEnabled(true);
            tv_status.setText("Table synced completed.");
            tv_status.setTextColor(getResources().getColor(R.color.md_light_green_500));
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private int getInvoiceSyncCount_forLoggingSync() {
        int iCount = 0;
        try{
            SQLiteDatabase database = (new DbHelper(getApplicationContext())).getWritableDatabase();
            String query = "SELECT * FROM " + TABLE_INVOICE_JSON + " WHERE " + COL_INVOICE_JSON_IsSync + " = " + 0;
            Cursor cursor = database.rawQuery(query, null);
            iCount = cursor.getCount();
            return iCount;

        }catch(Exception e){
            return iCount;
        }
    }

    private int getUnsyncSalesOrderCount_forLogginSync() {
        String sql = "SELECT * FROM " + TABLE_SALES_ORDER_JSON + " WHERE " + DbHelper.COL_SALES_ORDER_JSON_IsSync + " = 0";
        Cursor cursor;
        try {
            cursor = (new DbHelper(getApplicationContext())).getReadableDatabase().rawQuery(sql, null);
            if (cursor.getCount() > 0) {
                System.out.println("no new salesorders");
                return cursor.getCount();
            } else {
                return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

    private void displayStatusMessage(String s, int colorValue) {

        AlertDialog.Builder builder = null;
        View view = null;
        TextView tvOk, tvMessage;
        ImageView ivstatus;

        int defaultColor = R.color.textGray;
        int successColor = R.color.successColor; // 1
        int errorColor = R.color.errorColor; // 2
        int warningColor = R.color.warningColor; // 3
        //1,2,3

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

        builder = new AlertDialog.Builder(LoginSyncActivity.this);
        view = getLayoutInflater().inflate(R.layout.layout_for_custom_message, null);

        tvOk = (TextView) view.findViewById(R.id.tvOk);
        tvMessage = (TextView) view.findViewById(R.id.tvMessage);
        tvMessage.setTextColor(getResources().getColor(color));
        tvMessage.setText(s);
        ivstatus = (ImageView) view.findViewById(R.id.iv_status_k);
        ivstatus.setImageResource(img);

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

    private void displayMessage(String s, int colorValue) {

        AlertDialog.Builder builder = null;
        View view = null;
        TextView tvOk, tvMessage, tvCancel;

        int defaultColor = R.color.textGray;
        int successColor = R.color.successColor; // 1
        int errorColor = R.color.errorColor; // 2
        int warningColor = R.color.warningColor; // 3
        //1,2,3

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
        builder = new AlertDialog.Builder(LoginSyncActivity.this);
        view = getLayoutInflater().inflate(R.layout.layout_for_custom_message_with_ok_cancel, null);

        tvOk = (TextView) view.findViewById(R.id.tvOk);
        tvCancel = (TextView) view.findViewById(R.id.tvCancel);
        tvMessage = (TextView) view.findViewById(R.id.tvMessage);
        tvMessage.setTextColor(getResources().getColor(color));
        tvMessage.setText(s);


        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                SessionManager.Logut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }


    private void displayMessage2(String s, int colorValue) {

        AlertDialog.Builder builder = null;
        View view = null;
        TextView tvOk, tvMessage, tvCancel;
        ImageView ivstatus;

        int defaultColor = R.color.textGray;
        int successColor = R.color.successColor; // 1
        int errorColor = R.color.errorColor; // 2
        int warningColor = R.color.warningColor; // 3
        //1,2,3

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

        builder = new AlertDialog.Builder(LoginSyncActivity.this);
        view = getLayoutInflater().inflate(R.layout.layout_for_custom_message_with_ok_cancel, null);

        tvOk = (TextView) view.findViewById(R.id.tvOk);
        tvOk.setText("Okay");
        tvCancel = (TextView) view.findViewById(R.id.tvCancel);
        tvCancel.setText("Cancel");
        tvMessage = (TextView) view.findViewById(R.id.tvMessage);
        tvMessage.setTextColor(getResources().getColor(color));
        tvMessage.setText(s);
        ivstatus = (ImageView) view.findViewById(R.id.iv_status_okay);
        ivstatus.setImageResource(img);


        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SyncSalesOrdersActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                alertDialog.dismiss();
            }
        });

    }
}
