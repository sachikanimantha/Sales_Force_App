package com.example.bellvantage.scadd;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.example.bellvantage.scadd.DB.DbHelper;
import com.example.bellvantage.scadd.DB.MySingleton;
import com.example.bellvantage.scadd.Utils.DateManager;
import com.example.bellvantage.scadd.Utils.NetworkConnection;
import com.example.bellvantage.scadd.Utils.UtilityManager;
import com.example.bellvantage.scadd.web.HTTPPaths;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.bellvantage.scadd.DB.DbHelper.COL_INVOICE_ITEM_InvoiceId;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_INVOICE_ITEM_IsSync;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_INVOICE_JSON_IsSync;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_INVOICE_LINE_ITEM_invoiceID;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_INVOICE_LINE_ITEM_issync;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_LINE_ITEM_IsSync;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_LINE_ITEM_SalesOrderID;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_RETURN_INVENTORY_JSON_IsSync;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_RETURN_INVENTORY_LINEITEM_creditNoteNo;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_RETURN_INVENTORY_LINEITEM_isSync;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_RETURN_INVENTORY_isSync;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_REVISE_SALES_JSON_IsSync;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SALESORDER_SYNC_IsSync;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SALES_ORDER_IsSync;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SALES_ORDER_JSON_IsSync;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SALES_ORDER_JSON_SalesOrderId;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SALES_ORDER_SalesOrderId;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TABLE_INVOICE_UTILIZATION_IsSync;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_INVOICE_JSON;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_INVOICE_LINE_ITEM;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_INVOICE_UTILIZATION;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_RETURN_INVENTORY;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_RETURN_INVENTORY_JSON;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_RETURN_INVENTORY_LINEITEM;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_REVISE_ORDER_JSON;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_SALESORDERS_SYNC;

public class SyncSalesOrdersActivity extends AppCompatActivity {

    //Views
    Toolbar myToolBaar;
    ImageView ivSyncSalesOrders,ivSyncReviseSalesOrders,ivSyncCanceledConfirmedSalesOrders,ivSyncReturnNote,ivInvoice,ivInvoiceUtilization;
    TextView tvSOCount,tvRSCount,tvCCCount,tvSOSyncedCount,tvRSSyncedCount,tvCCSyncedCount,tvRNSyncedCount,tvSOSyncDate,tvRSSyncDate,tvRNSyncDate,tvCCSyncDate,tvRNCount,tvIyncDate,tvICount,tvISyncedCount,tvIUSyncDate,tvIUCount,tvIUSyncedCount;
    TextView tvAllSyncDate,tvAllCount,tvAllSyncedCount;
    ImageView ivAll;
    DbHelper db;

    //SharedPreferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    // AlertDialog
    AlertDialog dialog;
    AlertDialog.Builder mBuilder;

    String salesrepType;

    int soCount,soSyncedCount,rsCount,rsSyncedCount, rnCount,rnSyncedCount, ccCount,ccSyncedCount,iCount,iSyncedCount,iuCount,iuSyncedCount,allCount=0,allSyncCount=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_sales_orders);

        db = new DbHelper(SyncSalesOrdersActivity.this);

        initializeViews();

        //SharedPreferences
        pref = getApplicationContext().getSharedPreferences("syncDate.conf", Context.MODE_PRIVATE);
        editor =pref.edit();

        //Get details from SharedPreferences
        pref = getSharedPreferences("invoice.conf", Context.MODE_PRIVATE);
        editor =pref.edit();
        salesrepType = pref.getString("SalesRepType","");
        System.out.println("Sales rep type: "+ salesrepType);

        String ccSyncDate = null,soSyncDate =null,rsSyncDate=null,rnSyncDate=null,iSyncDate=null,iuSyncDate=null,allSyncSate=null;
        try{
            ccSyncDate = pref.getString("ccSyncDate",null);
            soSyncDate = pref.getString("soSyncDate",null);
            rnSyncDate = pref.getString("rnSyncDate",null);
            rsSyncDate = pref.getString("rsSyncDate",null);
            iSyncDate = pref.getString("iSyncDate",null);
            iuSyncDate = pref.getString("iuSyncDate",null);
            allSyncSate = pref.getString("all",null);

            if (ccSyncDate==null){
                ccSyncDate="Not Available";
            }
            if (soSyncDate == null){
                soSyncDate = "Not Available";
            }
            if (rnSyncDate == null){
                rnSyncDate = "Not Available";
            }
            if (rsSyncDate == null){
                rsSyncDate = "Not Available";
            }
            if (iSyncDate == null){
                iSyncDate = "Not Available";
            }
            if (iuSyncDate == null){
                iSyncDate = "Not Available";
            }
            if (allSyncSate == null){
                allSyncSate = "Not Available";
            }

        }catch (Exception e){
            System.out.println("@@@@@@@@@@@ Error in Shared Preferences (SyncSalesOrderActivity) "+ e.getMessage());
        }

        myToolBaar.setTitle("Sync SalesOrders");

        setSOSyncedCount(0);
        setCcSyncedCount(0);
        setRNSyncedCount(0);
        setRSSyncedCount(0);
        setIyncedCount(0);
        setIvSyncedCount(0);

        setCcSyncDate(ccSyncDate);
        setSOSyncDate(soSyncDate);
        setRSSyncDate(rsSyncDate);
        setRNSyncDate(rnSyncDate);
        setISyncDate(iSyncDate);
        setIuSyncDate(iSyncDate);
        setAllSyncDate(allSyncSate);

        tvRSSyncedCount.setText("Synced - "+0);
        tvRNSyncedCount.setText("Synced - "+0);
        tvISyncedCount.setText("Synced - "+0);
        tvCCSyncedCount.setText("Synced - "+0);
        tvIUSyncedCount.setText("Synced - "+0);

        allCount+=getSalesOrderSyncCount();
        allCount+=getRevisedOrdersSyncCount();
        allCount+=getConfirmedCancedSyncCount();
        allCount+=getReturnNoteSyncCount();
        allCount+=getInvoiceSyncCount();
        allCount+=getInvoiceUtilizationSyncCount();

        setAllSyncDetails(allCount, allSyncCount);

        //Sync Sales Orders
        ivSyncSalesOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkConnection.checkNetworkConnection(getApplicationContext())==true){
                    syncSalesOrders();
                }else{
                    displayMessage("Please check your network connection before sync data");
                }

            }
        });

        //Sync Canceled / Confirmed sales orders
        ivSyncCanceledConfirmedSalesOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (NetworkConnection.checkNetworkConnection(getApplicationContext())==true){
                    syncConfirmCancelSalesOrders();
                }else{
                    displayMessage("Please check your network connection before sync data");
                }
            }
        });

        //Sync Revise sales orders
        ivSyncReviseSalesOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (NetworkConnection.checkNetworkConnection(getApplicationContext())==true){
                   syncReviseSalesOrders();
                }else{
                    displayMessage("Please check your network connection before sync data");
                }
            }
        });

        //Sync Return Note
        ivSyncReturnNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (NetworkConnection.checkNetworkConnection(getApplicationContext())==true){
                    syncReturnNotes();
                }else{
                    displayMessage("Please check your network connection before sync data");
                }
            }
        });

        //Sync Invoice
        ivInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (NetworkConnection.checkNetworkConnection(getApplicationContext())==true){
                    syncInvoice();
                }else{
                    displayMessage("Please check your network connection before sync data");
                }
            }
        });

        //Sync Invoice
        ivInvoiceUtilization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (NetworkConnection.checkNetworkConnection(getApplicationContext())==true){
                    syncInvoiceUtilization();
                }else{
                    displayMessage("Please check your network connection before sync data");
                }
            }
        });

        //Sync All Data
        ivAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkConnection.checkNetworkConnection(getApplicationContext())==true){
                    syncSalesOrders();
                    syncConfirmCancelSalesOrders();
                    syncReviseSalesOrders();
                    syncReturnNotes();
                    syncInvoice();
                    syncInvoiceUtilization();
                }else{
                    displayMessage("Please check your network connection before sync data");
                }
            }
        });
    }



    private void displayMessage(String s) {
        mBuilder = new AlertDialog.Builder(SyncSalesOrdersActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.layout_for_custom_message,null);

        TextView tvOk,tvMessage;
        tvOk = (TextView) mView.findViewById(R.id.tvOk);
        tvMessage = (TextView) mView.findViewById(R.id.tvMessage);
        tvMessage.setText(s);

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        mBuilder.setView(mView);
        dialog = mBuilder.create();
        dialog.show();
    }

    //set Count for TextViews...
    private void setCcSyncedCount(int i) {
        tvCCSyncedCount.setText("Synced - "+i);
    }
    private void setRNSyncedCount(int i) {
        tvRNSyncedCount.setText("Synced - "+i);
    }
    private void setRSSyncedCount(int i) {
        tvRSSyncedCount.setText("Synced - "+i);
    }
    private void setIyncedCount(int i) {
        tvISyncedCount.setText("Synced - "+i);
    }
    private void setIvSyncedCount(int i) {
        tvIUSyncedCount.setText("Synced - "+i);
    }
    private void setSOSyncedCount(int i) {
        tvSOSyncedCount.setText("Synced - "+i);
    }

    private void setCcCount(int ccCount) {
        tvCCCount.setText("To Sync - " + ccCount);
    }
    private void setSOCount(int soCount) {
        tvSOCount.setText("To Sync - " + soCount);
    }
    private void setRSCount(int rsCount) {
        tvRSCount.setText("To Sync - " + rsCount);
    }
    private void setRNCount(int rnCount) {
        tvRNCount.setText("To Sync - " + rnCount);
    }
    private void setICount(int iCount) {
        tvICount.setText("To Sync - " + iCount);
    }
    private void setIuCount(int iuCount) {
        tvIUCount.setText("To Sync - " + iuCount);
    }

    //Set Synced dates
    private void setCcSyncDate(String dateWithTime) {
        tvCCSyncDate.setText("Last Sync on " + dateWithTime);
    }
    private void setSOSyncDate(String dateWithTime) {
        tvSOSyncDate.setText("Last Sync on " + dateWithTime);
    }
    private void setRSSyncDate(String dateWithTime) {
        tvRSSyncDate.setText("Last Sync on " + dateWithTime);
    }
    private void setRNSyncDate(String dateWithTime) {
        tvRNSyncDate.setText("Last Sync on " + dateWithTime);
    }
    private void setISyncDate(String dateWithTime) {
        tvIyncDate.setText("Last Sync on " + dateWithTime);
    }
    private void setIuSyncDate(String dateWithTime) {
        tvIUSyncDate.setText("Last Sync on " + dateWithTime);
    }

    private void setAllSyncDate(String dateWithTime) {
        tvAllSyncDate.setText("Last Sync on " + dateWithTime);
    }

    //get Synced Count From Sqlite
    private int getSalesOrderSyncCount() {
        String query = null;
        SQLiteDatabase database = db.getWritableDatabase();

            query = "SELECT * FROM "+ DbHelper.TABLE_SALES_ORDER_JSON+ " WHERE "+ COL_SALES_ORDER_JSON_IsSync + " = " + 0;
      
        Cursor cursor = database.rawQuery(query,null);
        soCount = cursor.getCount();
        if (soCount>0){
            ivSyncSalesOrders.setImageResource(R.drawable.ic_sync_problem);
        }else {
            ivSyncSalesOrders.setImageResource(R.drawable.ic_sync);
        }
        setSOCount(soCount);
        return soCount;
    }
    private int getConfirmedCancedSyncCount() {
        SQLiteDatabase database = db.getWritableDatabase();
        String query = "SELECT * FROM "+ TABLE_SALESORDERS_SYNC+ " WHERE "+ COL_SALESORDER_SYNC_IsSync + " = " + 0;
        Cursor cursor = database.rawQuery(query,null);
        ccCount = cursor.getCount();
        if (ccCount>0){
            ivSyncCanceledConfirmedSalesOrders.setImageResource(R.drawable.ic_sync_problem);
        }else {
            ivSyncCanceledConfirmedSalesOrders.setImageResource(R.drawable.ic_sync);
        }
       setCcCount(ccCount);

        return ccCount;

    }
    private int getReturnNoteSyncCount() {
        SQLiteDatabase database = db.getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_RETURN_INVENTORY_JSON+ " WHERE "+ COL_RETURN_INVENTORY_JSON_IsSync + " = " + 0;
        Cursor cursor = database.rawQuery(query,null);
        rnCount = cursor.getCount();
        if (rnCount>0){
            ivSyncReturnNote.setImageResource(R.drawable.ic_sync_problem);
        }else {
            ivSyncReturnNote.setImageResource(R.drawable.ic_sync);
        }
        setRNCount(rnCount);
        return rnCount;
    }
    private int getRevisedOrdersSyncCount() {
        SQLiteDatabase database = db.getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_REVISE_ORDER_JSON+ " WHERE "+ COL_REVISE_SALES_JSON_IsSync + " = " + 0;
        Cursor cursor = database.rawQuery(query,null);
        rsCount = cursor.getCount();
        if (rsCount>0){
            ivSyncReviseSalesOrders.setImageResource(R.drawable.ic_sync_problem);
        }else {
            ivSyncReviseSalesOrders.setImageResource(R.drawable.ic_sync);
        }
        setRSCount(rsCount);
        return rsCount;

    }
    private int getInvoiceUtilizationSyncCount() {
        SQLiteDatabase database = db.getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_INVOICE_UTILIZATION+ " WHERE "+ COL_TABLE_INVOICE_UTILIZATION_IsSync + " = " + 0;
        Cursor cursor = database.rawQuery(query,null);
        iuCount = cursor.getCount();
        if (iuCount>0){
            ivInvoiceUtilization.setImageResource(R.drawable.ic_sync_problem);
        }else {
            ivInvoiceUtilization.setImageResource(R.drawable.ic_sync);
        }
        setIuCount(iuCount);

        return iuCount;
    }
    private int getInvoiceSyncCount() {
        SQLiteDatabase database = db.getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_INVOICE_JSON+ " WHERE "+ COL_INVOICE_JSON_IsSync + " = " + 0;
        Cursor cursor = database.rawQuery(query,null);
        iCount = cursor.getCount();
        if (iCount>0){
            ivInvoice.setImageResource(R.drawable.ic_sync_problem);
        }else {
            ivInvoice.setImageResource(R.drawable.ic_sync);
        }
        setICount(iCount);

        return iCount;
    }

    //sync data to server
    private void syncSalesOrders() {
        System.out.println("========================================== SYNC SALES ORDERS ====================================================");
        Cursor cursor = db.getAllData(DbHelper.TABLE_SALES_ORDER_JSON);
        if (cursor.getCount() == 0) {
            System.out.println(DbHelper.TABLE_SALES_ORDER_JSON + " Table is empty");
            return;
        } else {
            while (cursor.moveToNext()) {
                final int invoiceNumber = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_JSON_InvoiceNumber));
                final int salesOrderid = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_JSON_SalesOrderId));
                final String salesOrderJson = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_JSON_JsonString));
                int IsSync = cursor.getInt(cursor.getColumnIndex(COL_SALES_ORDER_JSON_IsSync));
                //System.out.println("SYNC SALES ORDER JSON: "+salesOrderJson);
                //System.out.println("=== Sales Orders ID of SQLite(Sync Sales Orders) " + salesOrderid);
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(salesOrderJson);

                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("Json Error in (Sync Sales Orders): " + e.getMessage());
                }

                if (IsSync == 0){
                    System.out.println("SYNC SALES ORDER JSON: "+salesOrderJson);
                   // String url = HTTPPaths.seriveUrl+"CreateSalesOrderMobile";
                   String url = HTTPPaths.seriveUrl+"SyncSalesOrder";

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObj,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(final JSONObject response) {
                                    int responseSalesOrderId = 0;
                                    JsonObject object = Json.parse(String.valueOf(response)).asObject();
                                    int id = object.get("ID").asInt();
                                    try{
                                        responseSalesOrderId = Integer.parseInt(object.get("Data").asString());
                                    }catch (Exception e){
                                        System.out.println("Error: " + e.getMessage());
                                    }
                                    System.out.println("=== Sales Order ID (Sync Sales Orders) RESPONSE SalesOrderId " + responseSalesOrderId);
                                    System.out.println("response (Sync Sales Orders) - "+response.toString());

                                    if (id==200){

                                        soCount--;
                                        soSyncedCount++;
                                        allCount--;
                                        allSyncCount++;
                                        setAllSyncDetails(allCount,allSyncCount);
                                        if (soCount>0){
                                            ivSyncSalesOrders.setImageResource(R.drawable.ic_sync_problem);
                                        }else {
                                            ivSyncSalesOrders.setImageResource(R.drawable.ic_sync);
                                        }


                                        setSOCount(soCount);
                                        setSOSyncedCount(soSyncedCount);


                                        setSOSyncDate(DateManager.getDateWithTime());
                                        setAllSyncDate(DateManager.getDateWithTime());

                                        //SharedPreferences
                                        editor.putString("soSyncDate",DateManager.getDateWithTime());
                                        editor.putString("all",DateManager.getDateWithTime());
                                        editor.apply();

                                        //Update IsSync as 1 of SalesOrderJson
                                        ContentValues cv = new ContentValues();
                                        cv.put(COL_SALES_ORDER_JSON_IsSync, 1);
                                        cv.put(COL_SALES_ORDER_JSON_SalesOrderId, responseSalesOrderId);
                                        db.updateTable(invoiceNumber+"",cv,DbHelper.COL_SALES_ORDER_JSON_InvoiceNumber+" = ?",DbHelper.TABLE_SALES_ORDER_JSON);
                                        System.out.println("=== (Sync Sales Orders) ====== "+DbHelper.TABLE_SALES_ORDER_JSON+ " is Syncing... ");

                                        //Update salesorderId as 1 of SalesOrder table and Line Item Table
                                        ContentValues cvUpdate = new ContentValues();
                                        ContentValues cvUpdateLineItem = new ContentValues();
                                        cvUpdate.put(COL_SALES_ORDER_SalesOrderId,responseSalesOrderId);
                                        cv.put(COL_SALES_ORDER_IsSync,1);
                                        cvUpdateLineItem.put(COL_LINE_ITEM_SalesOrderID,responseSalesOrderId);
                                        cvUpdateLineItem.put(COL_LINE_ITEM_IsSync,1);
                                        try{
                                            db.updateTable(invoiceNumber+"",cvUpdate,DbHelper.COL_SALES_ORDER_InvoiceNumber + " = ?",DbHelper.TABLE_SALES_ORDER);
                                            db.updateTable(salesOrderid+"",cvUpdateLineItem, COL_LINE_ITEM_SalesOrderID + " = ?",DbHelper.TABLE_LINE_ITEM);

                                        }catch(Exception e){
                                            System.out.println("Error Sales Order Syncing ===============================================================================");
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    System.out.println("error - "+error.toString());
                                }
                            });

                    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);

                }

            }
        }
    }

    private void setAllSyncDetails(int allCount, int allSyncCount) {
        tvAllCount.setText("To Sync - "+allCount);
        tvAllSyncedCount.setText("Synced - "+allSyncCount);
        if (allCount>0){
            ivAll.setImageResource(R.drawable.ic_sync_problem);
        }else {
            ivAll.setImageResource(R.drawable.ic_sync);
        }
    }

    private void syncConfirmCancelSalesOrders() {
        syncReviseSalesOrders();
        System.out.println(" ======================= Confirmation & Cancellation ================================");
        SQLiteDatabase database = db.getWritableDatabase();
        String query = "SELECT * FROM "+ TABLE_SALESORDERS_SYNC;

        Cursor cursor = database.rawQuery(query,null);
        if (cursor.getCount() == 0) {
            System.out.println("No Data in "+ TABLE_SALESORDERS_SYNC );
            return;
        } else {
            while (cursor.moveToNext()) {

                final int SalesOrderID = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALESORDER_SYNC_SalesOrderID));
                System.out.println("== Update Sales Status Sales OrderId " + SalesOrderID);
                System.out.println("== Update Sales Status Sales OrderId " + new UtilityManager().truncateInvoiceId(SalesOrderID));
                final int SaleStatus = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALESORDER_SYNC_SaleStatus));
                int IsSync = cursor.getInt(cursor.getColumnIndex(COL_SALESORDER_SYNC_IsSync));

                if (IsSync == 0){
                    //String url = HTTPPaths.seriveUrl+"UpdateStatus?salesOrderID="+new UtilityManager().truncateInvoiceId(SalesOrderID)+"&status="+SaleStatus;
                    String url = HTTPPaths.seriveUrl+"UpdateStatus?salesOrderID="+SalesOrderID+"&status="+SaleStatus;
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    JsonObject object = Json.parse(response).asObject();
                                    int id = object.get("ID").asInt();
                                    System.out.println("cancel ID "+ id);
                                    if(id==200){
                                        ccCount--;
                                        ccSyncedCount++;
                                        allCount--;
                                        allSyncCount++;

                                        setAllSyncDetails(allCount, allSyncCount);

                                        if (ccCount>0){
                                            ivSyncCanceledConfirmedSalesOrders.setImageResource(R.drawable.ic_sync_problem);
                                        }else {
                                            ivSyncCanceledConfirmedSalesOrders.setImageResource(R.drawable.ic_sync);
                                        }


                                        setCcCount(ccCount);
                                        setCcSyncedCount(ccSyncedCount);
                                        setCcSyncDate(DateManager.getDateWithTime());
                                        setAllSyncDate(DateManager.getDateWithTime());

                                        //SharedPreferences
                                        editor.putString("ccSyncDate",DateManager.getDateWithTime());
                                        editor.putString("all",DateManager.getDateWithTime());
                                        editor.apply();

                                        System.out.println("== SYNC CANCEL/CONFIRM SALES ORDERS === SalesOrderSync table is Syncing.... ");
                                        ContentValues cv = new ContentValues();
                                        cv.put("SaleStatus",SaleStatus);
                                        cv.put("IsSync", 1);

                                        System.out.println("== Update Sales Status and sync status...  Sales OrderId res " + SalesOrderID);
                                        db.updateTable(SalesOrderID+"",cv,DbHelper.COL_SALES_ORDER_SalesOrderId+" = ?",DbHelper.TABLE_SALES_ORDER);
                                        db.updateTable(SalesOrderID+"",cv,DbHelper.COL_SALESORDER_SYNC_SalesOrderID+" = ?", TABLE_SALESORDERS_SYNC);

                                        ContentValues cvIsSyncLineItem = new ContentValues();
                                        cv.put(COL_LINE_ITEM_IsSync,1);

                                        try {
                                            System.out.println("== Update Sales Status Sales OrderId Line Item " + SalesOrderID);
                                            db.updateTable(SalesOrderID+"",cvIsSyncLineItem,DbHelper.COL_LINE_ITEM_SalesOrderID+" = ?",DbHelper.TABLE_LINE_ITEM);
                                            ContentValues cvIsSyncSalesOrders = new ContentValues();
                                            cv.put(COL_SALES_ORDER_IsSync,1);
                                            db.updateTable(SalesOrderID+"",cvIsSyncSalesOrders,DbHelper.COL_SALES_ORDER_SalesOrderId+" = ?",DbHelper.TABLE_SALES_ORDER);

                                        }catch (Exception e){
                                            System.out.println("====Error " + e.getMessage());
                                        }

                                    }else{
                                        //Toast.makeText(ActivityCancelSalesOrderContinue.this, "Merchant Update fail.. try again", Toast.LENGTH_LONG).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    System.out.println("== SyncSalesOrders  ==" + " Cancel Confirmation " + "Volley Error +++ " + error.getMessage());
                                }
                            }
                    );
                    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
                }else{
                    database.execSQL("DELETE FROM "+ TABLE_SALESORDERS_SYNC+ " WHERE " + DbHelper.COL_SALESORDER_SYNC_SalesOrderID + " = "+SalesOrderID+"");
                }
            }
        }
    }

    public void syncReturnNotes(){
        Cursor cursor = db.getAllData(DbHelper.TABLE_RETURN_INVENTORY_JSON);
        if (cursor.getCount() == 0) {
            System.out.println(DbHelper.TABLE_RETURN_INVENTORY_JSON + " Table is empty");
            return;
        } else {
            while (cursor.moveToNext()) {
                final int creditNoteNo = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_RETURN_INVENTORY_JSON_creditNoteNo));
                System.out.println("Credit Note Number: "+creditNoteNo);

                final String jsonStrinh = cursor.getString(cursor.getColumnIndex(DbHelper.COL_RETURN_INVENTORY_JSON_JsonString));
                System.out.println("Credit Note Json: "+jsonStrinh);
                int IsSync = cursor.getInt(cursor.getColumnIndex(COL_RETURN_INVENTORY_JSON_IsSync));

                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(jsonStrinh);

                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("Json Error ReturnActivity : " + e.getMessage());
                }
                if (IsSync == 0){

                    if (NetworkConnection.checkNetworkConnection(getApplicationContext())==true){

                        String url = HTTPPaths.seriveUrl+"SyncCreditNote";

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObj,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                        JsonObject object = Json.parse(String.valueOf(response)).asObject();
                                        int id = object.get("ID").asInt();
                                        int responseReturnNoteNumber = 0;
                                        try{
                                           responseReturnNoteNumber = Integer.parseInt(object.get("Data").asString());
                                        }catch (Exception e){
                                            System.out.println("=================================================");
                                            System.out.println("Return Inventory Json Error: ");
                                            e.printStackTrace();
                                        }
                                       
                                        if(id==200){

                                            rnCount--;
                                            rnSyncedCount++;
                                            allCount--;
                                            allSyncCount++;

                                            setAllSyncDetails(allCount, allSyncCount);


                                            setRNCount(rnCount);
                                            setRNSyncedCount(rnSyncedCount);

                                            if (rnCount>0){
                                                ivSyncReturnNote.setImageResource(R.drawable.ic_sync_problem);
                                            }else {
                                                ivSyncReturnNote.setImageResource(R.drawable.ic_sync);
                                            }

                                            setRNSyncDate(DateManager.getDateWithTime());

                                            //SharedPreferences
                                            editor.putString("rnSyncDate",DateManager.getDateWithTime());
                                            editor.putString("all",DateManager.getDateWithTime());
                                            setAllSyncDate(DateManager.getDateWithTime());
                                            editor.apply();


                                            System.out.println("== SyncSalesOrdersActivity === Return Inventory Json table is Syncing.... ");
                                            ContentValues cv = new ContentValues();
                                            cv.put(COL_RETURN_INVENTORY_JSON_IsSync, 1);
                                            boolean s =db.updateTable(creditNoteNo+"",cv,DbHelper.COL_RETURN_INVENTORY_JSON_creditNoteNo+" = ?",DbHelper.TABLE_RETURN_INVENTORY_JSON);

                                            if (s){
                                                System.out.println("ISSYNC is updated " + TABLE_RETURN_INVENTORY_JSON);
                                            }else{
                                                System.out.println("ISSYNC is not updated " + TABLE_RETURN_INVENTORY_JSON);
                                            }

                                            ContentValues cvUpdate = new ContentValues();
                                           // cvUpdate.put(COL_RETURN_INVENTORY_creditNoteNo,responseSalesOrderId);
                                            cvUpdate.put(COL_RETURN_INVENTORY_isSync,1);
                                            boolean sri=db.updateTable(responseReturnNoteNumber+"",cvUpdate,DbHelper.COL_RETURN_INVENTORY_creditNoteNo + " = ?",DbHelper.TABLE_RETURN_INVENTORY);
                                            if (sri){
                                                System.out.println("ISSYNC is updated " + TABLE_RETURN_INVENTORY);
                                            }else{
                                                System.out.println("ISSYNC is not updated " + TABLE_RETURN_INVENTORY);
                                            }


                                            ContentValues cvUpdateLineItem = new ContentValues();
                                            //cvUpdateLineItem.put(COL_RETURN_INVENTORY_LINEITEM_creditNoteNo,responseSalesOrderId);
                                            cvUpdateLineItem.put(COL_RETURN_INVENTORY_LINEITEM_isSync,1);

                                            boolean sril = db.updateTable(responseReturnNoteNumber+"",cvUpdateLineItem, COL_RETURN_INVENTORY_LINEITEM_creditNoteNo + " = ?",DbHelper.TABLE_RETURN_INVENTORY_LINEITEM);
                                            if (sril){
                                                System.out.println("ISSYNC is updated " + TABLE_RETURN_INVENTORY_LINEITEM);
                                            }else{
                                                System.out.println("ISSYNC is not updated " + TABLE_RETURN_INVENTORY_LINEITEM);
                                            }

                                        }else{
                                            System.out.println("== SyncSalesOrdersActivity === Return Inventory Json table is Syncing fail.... ");
                                        }

                                        System.out.println("=========response - "+response.toString());

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        System.out.println("== SyncSalesOrdersActivity === Return Inventory Json table is Syncing fail.... ");
                                    }
                                });
                        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
                    }else {

                    }
                }
            }
        }

    }

    private void initializeViews() {
        //ToolBar
        myToolBaar = (Toolbar) findViewById(R.id.tb_main);

        //ImageViews
        ivSyncSalesOrders = (ImageView) findViewById(R.id.ivSyncSalesOrders);
        ivSyncCanceledConfirmedSalesOrders = (ImageView) findViewById(R.id.ivSyncCanceledConfirmedSalesOrders);
        ivSyncReviseSalesOrders = (ImageView) findViewById(R.id.ivSyncReviseSalesOrders);
        ivSyncReturnNote = (ImageView) findViewById(R.id.ivSyncReturnNote);
        ivInvoice = (ImageView) findViewById(R.id.ivInvoice);
        ivInvoiceUtilization = (ImageView) findViewById(R.id.ivInvoiceUtilization);
        ivAll = (ImageView) findViewById(R.id.ivAll);

        //TextViews
        tvSOCount = (TextView) findViewById(R.id.tvSOCount);
        tvRSCount = (TextView) findViewById(R.id.tvRSCount);
        tvCCCount = (TextView) findViewById(R.id.tvCCCount);
        tvRNCount = (TextView) findViewById(R.id.tvRNCount);
        tvICount = (TextView) findViewById(R.id.tvICount);
        tvIUCount = (TextView) findViewById(R.id.tvIUCount);

        tvISyncedCount = (TextView) findViewById(R.id.tvISyncedCount);
        tvCCSyncedCount = (TextView) findViewById(R.id.tvCCSyncedCount);
        tvSOSyncedCount = (TextView) findViewById(R.id.tvSOSyncedCount);
        tvRSSyncedCount = (TextView) findViewById(R.id.tvRSSyncedCount);
        tvRNSyncedCount = (TextView) findViewById(R.id.tvRNSyncedCount);
        tvIUSyncedCount = (TextView) findViewById(R.id.tvIUSyncedCount);

        tvSOSyncDate = (TextView) findViewById(R.id.tvSOSyncDate);
        tvRSSyncDate = (TextView) findViewById(R.id.tvRSSyncDate);
        tvCCSyncDate = (TextView) findViewById(R.id.tvCCSyncDate);
        tvRNSyncDate = (TextView) findViewById(R.id.tvRNSyncDate);
        tvIyncDate = (TextView) findViewById(R.id.tvIyncDate);
        tvIUSyncDate = (TextView) findViewById(R.id.tvIUSyncDate);


        tvAllSyncDate = (TextView) findViewById(R.id.tvAllSyncDate);
        tvAllCount = (TextView) findViewById(R.id.tvAllCount);
        tvAllSyncedCount = (TextView) findViewById(R.id.tvAllSyncedCount);



    }

    private void syncReviseSalesOrders() {
        syncSalesOrders();
        System.out.println("========================================== SYNC REVISE SALES ORDERS ====================================================");
        Cursor cursor = db.getAllData(DbHelper.TABLE_REVISE_ORDER_JSON);
        if (cursor.getCount() == 0) {
            System.out.println(DbHelper.TABLE_REVISE_ORDER_JSON + " Table is empty");
            return;
        } else {
            while (cursor.moveToNext()) {
                final int invoiceNumber = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_REVISE_SALES_ORDER_JSON_InvoiceNumber));
                final String salesOrderJson = cursor.getString(cursor.getColumnIndex(DbHelper.COL_REVISE_SALES_ORDER_JSON_JsonString));
                int IsSync = cursor.getInt(cursor.getColumnIndex(COL_REVISE_SALES_JSON_IsSync));
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(salesOrderJson);

                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("Json Error in (Sync Revise Sales Orders): " + e.getMessage());
                }

                if (IsSync == 0){


                    String url = HTTPPaths.seriveUrl+"UpdateSalesOrderMobile";

                    if (NetworkConnection.checkNetworkConnection(getApplicationContext())==true){
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObj,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        System.out.println("=========response - "+response.toString());
                                        JsonObject object = Json.parse(String.valueOf(response)).asObject();
                                        int id = object.get("ID").asInt();

                                        if (id==200) {
                                           rsCount--;
                                           rsSyncedCount++;
                                            allCount--;
                                            allSyncCount++;
                                            setAllSyncDetails(allCount, allSyncCount);

                                            if (rsCount>0){
                                                ivSyncReviseSalesOrders.setImageResource(R.drawable.ic_sync_problem);
                                            }else {
                                                ivSyncReviseSalesOrders.setImageResource(R.drawable.ic_sync);
                                            }


                                            setRSCount(rsCount);
                                            setRSSyncedCount(rsSyncedCount);

                                            setRSSyncDate(DateManager.getDateWithTime());
                                            setAllSyncDate(DateManager.getDateWithTime());

                                            //SharedPreferences
                                            editor.putString("rsSyncDate",DateManager.getDateWithTime());
                                            editor.putString("all",DateManager.getDateWithTime());
                                            editor.apply();

                                            ContentValues cv = new ContentValues();
                                            cv.put("IsSync",1);
                                            db.updateTable(invoiceNumber+"",cv,DbHelper.COL_REVISE_SALES_ORDER_JSON_InvoiceNumber+" = ?",DbHelper.TABLE_REVISE_ORDER_JSON);
                                        }

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        System.out.println("@@@@@ (SYNC_SALES_ORDERS) Revise error - "+error.toString());
                                    }
                                });
                        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
                    }
                }
            }
        }
    }

    private void syncInvoiceUtilization() {
        System.out.println("========================================== SYNC Invoice Utilization ====================================================");
        Cursor cursor = db.getAllData(DbHelper.TABLE_INVOICE_UTILIZATION);
        if (cursor.getCount() == 0) {
            System.out.println(DbHelper.TABLE_INVOICE_UTILIZATION + " Table is empty");
            return;
        } else {
            while (cursor.moveToNext()) {
                final int invoiceNumber = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_TABLE_INVOICE_UTILIZATION_InvoiceId));
                final int CreditNoteNo = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_TABLE_INVOICE_UTILIZATION_CreditNoteNo));
                final double UtilizedAmount = cursor.getDouble(cursor.getColumnIndex(DbHelper.COL_TABLE_INVOICE_UTILIZATION_UtilizedAmount));
                final String EnteredUser = cursor.getString(cursor.getColumnIndex(DbHelper.COL_TABLE_INVOICE_UTILIZATION_EnteredUser));
                final String EnteredDate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_TABLE_INVOICE_UTILIZATION_EnteredDate));
                int IsSync = cursor.getInt(cursor.getColumnIndex(COL_TABLE_INVOICE_UTILIZATION_IsSync));

                if (IsSync == 0){

                    String url = HTTPPaths.seriveUrl+   "InsertUtilization?invoiceNumber="+invoiceNumber+"&CreditNoteNo="+CreditNoteNo+
                                                        "&utilizedAmount="+UtilizedAmount+"&enterUser="+EnteredUser+"&enterDate="+EnteredDate;

                    if (NetworkConnection.checkNetworkConnection(getApplicationContext())==true){
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        JsonObject object = Json.parse(String.valueOf(response)).asObject();
                                        int id = object.get("ID").asInt();

                                        if (id==200) {
                                            System.out.println("Utilization table syncing...");
                                            iuCount--;
                                            iuSyncedCount++;
                                            allSyncCount++;
                                            allCount--;
                                            setAllSyncDetails(allCount, allSyncCount);


                                            if (iuCount>0){
                                                ivInvoiceUtilization.setImageResource(R.drawable.ic_sync_problem);
                                            }else {
                                                ivInvoiceUtilization.setImageResource(R.drawable.ic_sync);
                                            }


                                            setIuCount(iCount);
                                            setIvSyncedCount(iuSyncedCount);

                                            setIuSyncDate(DateManager.getDateWithTime());
                                            setAllSyncDate(DateManager.getDateWithTime());

                                            //SharedPreferences
                                            editor.putString("iuSyncDate",DateManager.getDateWithTime());
                                            editor.putString("all",DateManager.getDateWithTime());
                                            editor.apply();

                                            ContentValues cv = new ContentValues();
                                            cv.put(COL_TABLE_INVOICE_UTILIZATION_IsSync,1);
                                            db.updateTable(invoiceNumber+"",cv,DbHelper.COL_TABLE_INVOICE_UTILIZATION_InvoiceId+" = ?",DbHelper.TABLE_INVOICE_UTILIZATION);
                                        }

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {

                                    }
                                }
                        );
                        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
                    }
                }
            }
        }

    }
    private void syncInvoice() {
        syncSalesOrders();
        System.out.println("========================================== SYNC Invoice ====================================================");
        Cursor cursor = db.getAllData(DbHelper.TABLE_INVOICE_JSON);
        if (cursor.getCount() == 0) {
            System.out.println(DbHelper.TABLE_INVOICE_JSON + " Table is empty");
            return;
        } else {
            while (cursor.moveToNext()) {
                final int invoiceNumber = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_INVOICE_JSON_InvoiceNumber));
                final String invoicerJson = cursor.getString(cursor.getColumnIndex(DbHelper.COL_INVOICE_JSON_JsonString));
                int IsSync = cursor.getInt(cursor.getColumnIndex(COL_INVOICE_JSON_IsSync));

                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(invoicerJson);

                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("Json Error in (Sync Invoice): " + e.getMessage());

                }

                if (IsSync == 0){

                    // new UtilityManager().genarateInvoiceId(invoiceNumber,orderList.getSalesRepId())
                    System.out.println("Invoice Number JSON TABLE: " + invoiceNumber);
                    System.out.println("Invoice Json: " + invoicerJson);
                    String url = HTTPPaths.seriveUrl+"SyncSalesInvoice";


                    if (NetworkConnection.checkNetworkConnection(getApplicationContext())==true){
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObj,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        System.out.println("=========response - "+response.toString());
                                        JsonObject object = Json.parse(String.valueOf(response)).asObject();
                                        int id = object.get("ID").asInt();

                                        if (id==200) {
                                           iCount--;
                                           iSyncedCount++;
                                            allCount--;
                                            allSyncCount++;

                                            setAllSyncDetails(allCount, allSyncCount);

                                            if (iCount>0){
                                                ivInvoice.setImageResource(R.drawable.ic_sync_problem);
                                            }else {
                                                ivInvoice.setImageResource(R.drawable.ic_sync);
                                            }

                                            setICount(iCount);
                                            setIyncedCount(iSyncedCount);

                                            setISyncDate(DateManager.getDateWithTime());
                                            setAllSyncDate(DateManager.getDateWithTime());

                                            //SharedPreferences
                                            editor.putString("iSyncDate",DateManager.getDateWithTime());
                                            editor.putString("all",DateManager.getDateWithTime());
                                            editor.apply();

                                            ContentValues cv = new ContentValues();
                                            cv.put(COL_INVOICE_JSON_IsSync,1);
                                            db.updateTable(invoiceNumber+"",cv,DbHelper.COL_INVOICE_JSON_InvoiceNumber+" = ?",DbHelper.TABLE_INVOICE_JSON);

                                            //update isync as 1
                                            ContentValues cvInvoice = new ContentValues();
                                            cvInvoice.put(COL_INVOICE_ITEM_IsSync,1);

                                            ContentValues cvInvoiceLineIten = new ContentValues();
                                            cvInvoiceLineIten.put(COL_INVOICE_LINE_ITEM_issync,1);
                                            //SQLiteDatabase database = db.getWritableDatabase();
                                            try{
                                                db.updateTable(invoiceNumber+"",cvInvoice,COL_INVOICE_ITEM_InvoiceId +" = ? ",DbHelper.TABLE_INVOICE_ITEM);
                                                db.updateTable(invoiceNumber+"",cvInvoiceLineIten,COL_INVOICE_LINE_ITEM_invoiceID +" = ? ",TABLE_INVOICE_LINE_ITEM);


                                                /*int afectedRows = database.update(TABLE_INVOICE_ITEM,cvInvoice,COL_INVOICE_ITEM_InvoiceId +" = ? " ,new String[]{""+invoiceNumber});
                                                if(afectedRows>0){
                                                    System.out.println("Updated is sync as 1 related to [Invoice Table] "+invoiceNumber);
                                                    System.out.println("Affected Rows "+afectedRows);
                                                }

                                                afectedRows = database.update(TABLE_INVOICE_LINE_ITEM,cvInvoiceLineIten,COL_INVOICE_LINE_ITEM_invoiceID +" = ? " ,new String[]{""+invoiceNumber});
                                                if(afectedRows>0){
                                                    System.out.println("Updated is sync as 1 related to [Invoice Line Item Table] "+invoiceNumber);
                                                    System.out.println("Affected Rows "+afectedRows);
                                                }*/

                                            }catch (SQLException e){
                                                System.out.println("Error at data update invoice sync ");
                                                e.printStackTrace();
                                            }finally {
                                               // database.close();
                                            }

                                        }

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        System.out.println("@@@@@ (SYNC_INVOICE_JSON)  error - "+error.toString());
                                    }
                                });
                        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

}
