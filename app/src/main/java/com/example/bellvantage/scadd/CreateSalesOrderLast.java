package com.example.bellvantage.scadd;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.example.bellvantage.scadd.Adapters.ProductListLastAdapter;
import com.example.bellvantage.scadd.DB.DbHelper;
import com.example.bellvantage.scadd.DB.MySingleton;
import com.example.bellvantage.scadd.Utils.DateManager;
import com.example.bellvantage.scadd.Utils.SessionManager;
import com.example.bellvantage.scadd.Utils.SyncManager;
import com.example.bellvantage.scadd.Utils.UtilityManager;
import com.example.bellvantage.scadd.swf.LoginUser;
import com.example.bellvantage.scadd.swf.OrderList;
import com.example.bellvantage.scadd.swf.ProductListLast;
import com.example.bellvantage.scadd.swf.ReturnInventoryLineItem;
import com.example.bellvantage.scadd.swf.ReviseOrderJson;
import com.example.bellvantage.scadd.swf.SalesOrder;
import com.example.bellvantage.scadd.swf.SalesOrderLineItem;
import com.example.bellvantage.scadd.web.HTTPPaths;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.bellvantage.scadd.DB.DbHelper.COL_LINE_ITEM_IsSync;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_LINE_ITEM_SalesOrderID;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SALES_ORDER_IsSync;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SALES_ORDER_JSON_InvoiceNumber;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SALES_ORDER_JSON_IsSync;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SALES_ORDER_JSON_JsonString;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SALES_ORDER_JSON_SalesOrderId;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SALES_ORDER_SalesOrderId;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SRI_BATCH_ID;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SRI_PRODUCT_ID;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SRI_QTY_INHAND;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SYNC_ID_NEXT_ID;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SYNC_ID_TABLE_ID;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_VEHICLE_INVENTORY_BatchID;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_VEHICLE_INVENTORY_LoadQuantity;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_VEHICLE_INVENTORY_ProductId;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_LINE_ITEM;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_SALES_ORDER;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_SALES_ORDER_ID;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_SYNC_ID;

public class CreateSalesOrderLast extends AppCompatActivity {

    ListView listView;
    ArrayList<ProductListLast> productListLasts = new ArrayList<>();
    ProductListLastAdapter productListLastAdapter;
    ArrayList<ReturnInventoryLineItem> productsReturnLast = new ArrayList<>();
    boolean isConfirmed = false;
    double totalDiscount = 0, vatAmount = 0;
    Button btn_create_sale_order;
    int enteredUser;
    LinearLayout layout;

    TextView tv_grandtotal, tv_free, tv_totalVat, tv_totalVat_header, tv_net;
    TextView tv_line_grandtotal, tv_line_freetotal, tv_line_nettotal;
    double totalGrand = 0.00, totalFree = 0.00, totalVat = 0.00, totalNet = 0.00;
    double lineTotal = 0.00, lineFree = 0.00, lineNet = 0.00;

    int isSync, salesOrderid, invoiceNo;
    int credittype,creditdays;
    //SharedPreferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    DbHelper db;
    int NextSalesOrderNo, NextInvoiceNo, previousSalesOrderID, salesRepId;
    boolean con_status;
    Toolbar mToolbar;

    String syncDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_sales_order_last);

        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.tb_main);
        mToolbar.setTitle("Create Sales Order - Step 05");

        Gson gson = new Gson();
        String getJson = SessionManager.pref.getString("user", "");
        LoginUser prefUser = gson.fromJson(getJson, LoginUser.class);
        salesRepId = prefUser.getUserTypeId();

        db = new DbHelper(getApplicationContext());
        syncDate = "/Date(" + (new DateManager()).todayMillsec() + ")/";


        pref = getSharedPreferences("invoice.conf", Context.MODE_PRIVATE);
        editor = pref.edit();

        NextSalesOrderNo = pref.getInt("NextSalesOrderNo", -1);
        // NextInvoiceNo = pref.getInt("NextInvoiceNo",-1);

        System.out.println("Next Sales Order Number pref: " + NextSalesOrderNo);
        // System.out.println("Next Sales Invoice Number pref: " +NextInvoiceNo);

        if (getIntent().getSerializableExtra("obj") != null) {
            productListLasts = (ArrayList<ProductListLast>) getIntent().getSerializableExtra("obj");
        }
        if (getIntent().getSerializableExtra("objReturn") != null) {
            productsReturnLast = (ArrayList<ReturnInventoryLineItem>) getIntent().getSerializableExtra("objReturn");
        }

        final int merchant_id = getIntent().getExtras().getInt("merchant_id");
        final int distributor_id = getIntent().getExtras().getInt("distributor_id");
        final int salesrep_id = getIntent().getExtras().getInt("salesrep_id");
        final int salestype_id = getIntent().getExtras().getInt("salestype_id");
        final String enter_date = getIntent().getExtras().getString("enter_date");
        final String enter_user = getIntent().getExtras().getString("enter_user");
        final int sales_status = getIntent().getExtras().getInt("sales_status");
        final double vatAmountTotal = getIntent().getExtras().getDouble("totalVatAmount");
        final String sales_date = getIntent().getExtras().getString("sales_date");
        final int is_vat_all = getIntent().getExtras().getInt("is_vat_all");//commonly items has vat or not
        final int is_return = getIntent().getExtras().getInt("is_return");
        final String salesrepType = getIntent().getExtras().getString("salesrep_type");
        final String estimateDelivaryDate = getIntent().getExtras().getString("estimate_date");
        final int newStock = getIntent().getExtras().getInt("newStock");
        totalGrand = getIntent().getExtras().getDouble("grandtotal");
        totalFree = getIntent().getExtras().getDouble("totalFreeIssue");
        credittype = getIntent().getExtras().getInt("creditType");
        creditdays = getIntent().getExtras().getInt("creditDays");


        System.out.println(" merchant_id - " + merchant_id);
        System.out.println(" distributor_id - " + distributor_id);
        System.out.println(" salesrep_id - " + salesrep_id);
        System.out.println(" salestype_id - " + salestype_id);
        System.out.println(" enter_date - " + enter_date);
        System.out.println(" enter_user - " + enter_user);
        System.out.println(" sales_status - " + sales_status);
        System.out.println(" vatAmountTotal - " + vatAmountTotal);
        System.out.println(" sales_date - " + sales_date);
        System.out.println(" is_vat_all - " + is_vat_all);
        System.out.println(" is_return - " + is_return);
        System.out.println(" salesrepType - " + salesrepType);
        System.out.println(" estimateDelivaryDate - " + estimateDelivaryDate);
        System.out.println(" newStock - " + newStock);
        System.out.println(" grandTotal - " + totalGrand);
        System.out.println(" freeIsuee - " + totalFree);
        System.out.println(" credittype - " + credittype);
        System.out.println(" creditdays - " + creditdays);

       // layout = (LinearLayout) findViewById(R.id.ll_create_so);

        listView = (ListView) findViewById(R.id.lv_requested_list);
        productListLastAdapter = new ProductListLastAdapter(this, R.layout.layout_for_sales_items, productListLasts);
        listView.setAdapter(productListLastAdapter);

        for (int i = 0; i < productListLasts.size(); i++) {

            double unitprice = productListLasts.get(i).getUnitSellingPrice();
            int freequantity = productListLasts.get(i).getFreeQuantity();
            int qty = productListLasts.get(i).getQuantity();

            lineTotal += productListLasts.get(i).getTotalAmount();
            lineFree += (unitprice * freequantity);
            lineNet += (unitprice * qty);

            if (is_vat_all == 1) {

                double vatrate = productListLasts.get(i).getVatRate();
                double P_W_V = unitprice / (1 + vatrate);
                vatAmount += (unitprice - P_W_V) * qty;

                //price without vat(p.w.v) +vat rate * p.w.v = unit price
                //p.w.v = unit price/(1+vat rate)
                //vat amount= (unit price- p.w.v)*quantity
                //total vat = vat1 + vat2.. + vat3.. ;//every row

            }

        }


        tv_grandtotal = (TextView) findViewById(R.id.tv_total_grand_value_last);
        tv_totalVat = (TextView) findViewById(R.id.tv_total_vat_value_last);
        tv_totalVat_header = (TextView) findViewById(R.id.tv_total_vat_value_last_header);
        tv_free = (TextView) findViewById(R.id.tv_free_issued_value_last);
        tv_net = (TextView) findViewById(R.id.tv_netamount_value_last);


        if (is_vat_all == 1) {
            totalVat = vatAmountTotal;

            tv_totalVat.setVisibility(View.VISIBLE);
            tv_totalVat_header.setVisibility(View.VISIBLE);

            //tv_net.setText(String.format("Rs. %.2f",(totalGrand-totalFree - totalVat )));
            tv_net.setText(String.format("Rs. %.2f", (totalGrand - totalFree)));
            tv_totalVat.setText(String.format("Rs. %.2f", totalVat));
        } else if (is_vat_all != 1) {

            totalVat = 0;
            tv_totalVat.setVisibility(View.GONE);
            tv_totalVat_header.setVisibility(View.GONE);

            tv_net.setText(String.format("Rs. %.2f", (totalGrand - totalFree)));
            //tv_totalVat.setText(String.format("Rs. %.2f",totalVat));
        }


        tv_grandtotal.setText(String.format("Rs. %.2f", totalGrand));
        tv_free.setText(String.format("Rs. %.2f", totalFree));

        btn_create_sale_order = (Button) findViewById(R.id.btn_create_sales_order);
        btn_create_sale_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isConfirmed = true;

                SalesOrder salesOrder = null;
                final String[] res = new String[1];
                final int[] responseSalesOrderId = {0};
                SalesOrderLineItem salesOrderLineItem = null;
                final ReviseOrderJson[] reviseOrderJson = {null};

                String EstimateDeliveryDate, EnteredDate;
                EstimateDeliveryDate = EnteredDate = sales_date;

                previousSalesOrderID = getPreviousSalesOrderID();
                System.out.println("Previous Sales OrderId " + previousSalesOrderID);
                System.out.println("Next Sales OrderId " + NextSalesOrderNo);

                if (NextSalesOrderNo > previousSalesOrderID) {
                    NextSalesOrderNo++;
                    salesOrderid = NextSalesOrderNo;
                } else {
                    previousSalesOrderID++;
                    salesOrderid = previousSalesOrderID;
                }

                System.out.println("========== Sales OrderId " + salesOrderid);
                editor.putInt("NextSalesOrderNo", salesOrderid);
                editor.apply();
                editor.commit();

                //Update NextSalesOrderID of SYNC_NEXT_ID Table
                updateSyncNextID(salesOrderid);

                for (int i = 0; i < productListLasts.size(); i++) {
                    ProductListLast pro = productListLasts.get(i);
                    pro.setSalesOrderID(salesOrderid);
                    productListLasts.set(i, pro);
                }

                if (is_vat_all == 1) {

                    salesOrder = new SalesOrder(merchant_id, distributor_id, salesrep_id,
                            salestype_id, enter_date, enter_user, sales_status, totalGrand,
                            totalDiscount, totalVat, sales_date, is_vat_all, is_return,
                            invoiceNo, estimateDelivaryDate, syncDate, isSync, salesOrderid
                            , productListLasts, salesrepType,credittype,creditdays);

                } else if (is_vat_all == 0) {
                    salesOrder = new SalesOrder(merchant_id, distributor_id, salesrep_id,
                            salestype_id, enter_date, enter_user, sales_status, totalGrand,
                            totalDiscount, totalVat, sales_date, 0, is_return, invoiceNo,
                            estimateDelivaryDate, syncDate, isSync, salesOrderid
                            , productListLasts, salesrepType,credittype,creditdays);
                }

                Gson gson1 = new Gson();
                final String jsonStringOri = gson1.toJson(salesOrder);
                System.out.println("Json String ORI " + jsonStringOri);


                //Save Line Item to Sqlite

                for (int i = 0; i < productListLasts.size(); i++) {
                    salesOrderLineItem = new SalesOrderLineItem(
                            new UtilityManager().genarateInvoiceId(salesOrderid, salesRepId),
                            productListLasts.get(i).getBatchID(),
                            productListLasts.get(i).getProductID(),
                            productListLasts.get(i).getQuantity(),
                            productListLasts.get(i).getFreeQuantity(),
                            productListLasts.get(i).getUnitSellingPrice(),
                            productListLasts.get(i).getUnitSellingDiscount(),
                            productListLasts.get(i).getTotalAmount(),
                            productListLasts.get(i).getTotalDiscount(),
                            productListLasts.get(i).getIsSync(),
                            productListLasts.get(i).getStock());

                    ContentValues contentValues2 = salesOrderLineItem.getLineItemContentValues3();
                    boolean success2 = (new DbHelper(getApplicationContext())).insertDataAll(contentValues2, TABLE_LINE_ITEM);
                    if (success2) {
                        System.out.println("salesOrderLineItem data insert to sqlite db");
                        String s = "";
                        if (salesrepType.equalsIgnoreCase("r")) {
                            //vehicle,redy stock
                            s = "UPDATE " + DbHelper.TABLE_VEHICALE_INVENTORY
                                    + " SET "
                                    + COL_VEHICLE_INVENTORY_LoadQuantity + " = " + productListLasts.get(i).getStock()
                                    + " WHERE "
                                    + COL_VEHICLE_INVENTORY_ProductId + " = " + productListLasts.get(i).getProductID()
                                    +" AND "
                                    + COL_VEHICLE_INVENTORY_BatchID + " = " + productListLasts.get(i).getBatchID();

                            System.out.println("s - " + s);

                        } else if (salesrepType.equalsIgnoreCase("p")) {
                            //pre sales
                            s = "UPDATE " + DbHelper.TABLE_SALESREP_INVENTORY
                                    + " SET " + COL_SRI_QTY_INHAND + " = " + productListLasts.get(i).getStock()
                                    + " WHERE "
                                    + COL_SRI_PRODUCT_ID + " = " + productListLasts.get(i).getProductID()
                                    +" AND "
                                    + COL_SRI_BATCH_ID + " = " + productListLasts.get(i).getBatchID();

                            System.out.println("s - " + s);

                        }

                        (new SyncManager(getApplicationContext())).updateAnyTable(s);


                    } else {
                        System.out.println("salesOrderLineItem data not insert to sqlite db");
                    }
                }


                //  System.out.println("Next Invoice Number "+ NextInvoiceNo);

                final OrderList orderList = new OrderList(NextInvoiceNo, estimateDelivaryDate, totalDiscount, totalGrand,
                        syncDate, isSync, sales_status, enter_user, enter_date, "", "", sales_date,
                        salestype_id, salesrep_id, distributor_id, merchant_id,
                        new UtilityManager().genarateInvoiceId(salesOrderid, salesRepId),
                        totalVat, is_vat_all, is_return, getMerchantName(merchant_id),credittype,creditdays+"");
                System.out.println("Merchant Id in creaate Sales order " + merchant_id);


                ContentValues cv = orderList.getOrderListContentValues();
                System.out.println("========= cv MerchantId:  " + cv.get("MerchantId"));
                System.out.println("========= cv NextInvoiceNo:  " + cv.get("InvoiceNumber"));
                System.out.println("========= cv SalesOrderId:  " + cv.get("SalesOrderId"));
                System.out.println("========= cv Merchant Name:  " + cv.get("MerchntName"));
                boolean success = (new DbHelper(getApplicationContext())).insertDataAll(cv, TABLE_SALES_ORDER);
                if (!success) {
                    System.out.println("Data is not inserted to " + TABLE_SALES_ORDER);
                } else {
                    System.out.println(TABLE_SALES_ORDER + " Table syncing...");
                }

                //post send message

                ContentValues cvE = new ContentValues();
                cvE.put(COL_SALES_ORDER_JSON_InvoiceNumber, invoiceNo);
                cvE.put(COL_SALES_ORDER_JSON_SalesOrderId, new UtilityManager().genarateInvoiceId(salesOrderid, salesRepId));
                cvE.put(COL_SALES_ORDER_JSON_JsonString, jsonStringOri);
                cvE.put(COL_SALES_ORDER_JSON_IsSync, 0);

                boolean success3 = (new DbHelper(getApplicationContext())).insertDataAll(cvE, DbHelper.TABLE_SALES_ORDER_JSON);
                if (success3) {
                    System.out.println("Create Sales order json insert to sqlite db");
                } else {
                    System.out.println("Create Sales order json not insert to sqlite db");
                }
                //}

                AlertDialog.Builder builder = new AlertDialog.Builder(CreateSalesOrderLast.this);
                View view = getLayoutInflater().inflate(R.layout.layout_for_success_message, null);
                builder.setView(view);

                Button btn_confirm_menu = (Button) view.findViewById(R.id.btn_goto_confirm_menu);
                Button btn_go_to_returnmenu = (Button) view.findViewById(R.id.btn_go_to_returnmenu);
                TextView tv_succ_cre = (TextView) view.findViewById(R.id.tv_successufuly_created);
                final TextView tv_response = (TextView) view.findViewById(R.id.tv_response);
                final AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                if (res[0] == null) {
                    tv_response.setText("Sales Order ID - " + new UtilityManager().genarateInvoiceId(salesOrderid, salesRepId));
                } else {
                    tv_response.setText("Sales Order ID - " + responseSalesOrderId);
                }

                btn_confirm_menu.setVisibility(View.VISIBLE);
                btn_go_to_returnmenu.setVisibility(View.VISIBLE);

                if (productsReturnLast.size() > 0) {
                    btn_confirm_menu.setVisibility(View.GONE);
                    btn_go_to_returnmenu.setVisibility(View.VISIBLE);
                }

//                tv_response.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        tv_response.setVisibility(View.INVISIBLE);
//                    }
//                });

                btn_confirm_menu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (responseSalesOrderId[0] == 0) {
                            responseSalesOrderId[0] = new UtilityManager().genarateInvoiceId(salesOrderid, salesRepId);
                        }
                        Intent intent = new Intent(getApplicationContext(), ActivityCancelSalesOrderContinue.class);
                        intent.putExtra("orderList", orderList);
                        intent.putExtra("id", 2);
                        intent.putExtra("responseSalesOrderId", responseSalesOrderId[0]);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        CreateSalesOrderLast.this.finish();

                    }
                });

                btn_go_to_returnmenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent returnActivity = new Intent(CreateSalesOrderLast.this, ReturnActivty.class);
                        returnActivity.putExtra("merchantId", merchant_id);
                        returnActivity.putExtra("productsReturnLast", productsReturnLast);
                        returnActivity.putExtra("orderList", orderList);
                        returnActivity.putExtra("id", 2);
                        returnActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        returnActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        finish();
                        startActivity(returnActivity);
                        CreateSalesOrderLast.this.finish();
                    }
                });

                btn_create_sale_order.setEnabled(false);
            }
        });

    }

    private int getPreviousSalesOrderID() {
        int id = 0;
        SQLiteDatabase database = db.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_SYNC_ID + " WHERE " + COL_SYNC_ID_TABLE_ID + " = " + TABLE_SALES_ORDER_ID;
        Cursor cursor = database.rawQuery(query, null);

        while (cursor.moveToNext()) {
            id = cursor.getInt(cursor.getColumnIndex(COL_SYNC_ID_NEXT_ID));
        }

        return id;

    }

    private void updateSyncNextID(int salesOrderid) {
        SQLiteDatabase database = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_SYNC_ID_NEXT_ID, salesOrderid);
        int afectedRows = database.update(TABLE_SYNC_ID, cv, COL_SYNC_ID_TABLE_ID + " = ? ", new String[]{"" + TABLE_SALES_ORDER_ID});
        if (afectedRows > 0) {
            System.out.println(COL_SYNC_ID_TABLE_ID + " is Updated " + " SALES ORDERID " + salesOrderid);
        }
    }

    public String getMerchantName(int merchantId) {
        String MerchantName = null;
        SQLiteDatabase database = db.getReadableDatabase();
        Cursor cursor = null;
        String query = "SELECT  * FROM " + DbHelper.TABLE_MERCHANT + " WHERE " + DbHelper.TBL_MERCHANT_MERCHANT_ID + " = " + merchantId;

        cursor = database.rawQuery(query, null);
        if (cursor.getCount() == 0) {
            System.out.println("No Data of " + DbHelper.TABLE_MERCHANT);
        } else {
            while (cursor.moveToNext()) {
                MerchantName = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_MERCHANT_NAME));
            }
        }
        return MerchantName;
    }


    public void syncPreviousSalesOrders() {
        System.out.println("====== Start Syncing Previous Sales Orders...");
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

                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(salesOrderJson);

                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("Json Error in SALESORDER syncSalesOrderJson: " + e.getMessage());
                }

                if (IsSync == 0) {
                    String url = HTTPPaths.seriveUrl + "CreateSalesOrderMobile";
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObj,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(final JSONObject response) {

                                    JsonObject object = Json.parse(String.valueOf(response)).asObject();
                                    int id = object.get("ID").asInt();
                                    int responseSalesOrderId = Integer.parseInt(object.get("Data").asString());

                                    System.out.println("response syncSalesOrderJson - " + response.toString());

                                    if (id == 200) {
                                        ContentValues cv = new ContentValues();
                                        cv.put(COL_SALES_ORDER_JSON_IsSync, 1);
                                        cv.put(COL_SALES_ORDER_JSON_SalesOrderId, responseSalesOrderId);
                                        db.updateTable(invoiceNumber + "", cv, DbHelper.COL_SALES_ORDER_JSON_InvoiceNumber + " = ?", DbHelper.TABLE_SALES_ORDER_JSON);
                                        System.out.println("=== SALES ORDER SYNC PREVIOUS SALES ORDERS ====== " + DbHelper.TABLE_SALES_ORDER_JSON + " is Syncing");


                                        ContentValues cvUpdate = new ContentValues();
                                        ContentValues cvUpdateLineItem = new ContentValues();
                                        cvUpdate.put(COL_SALES_ORDER_SalesOrderId, responseSalesOrderId);
                                        cv.put(COL_SALES_ORDER_IsSync, 1);
                                        cvUpdateLineItem.put(COL_LINE_ITEM_SalesOrderID, responseSalesOrderId);
                                        cvUpdateLineItem.put(COL_LINE_ITEM_IsSync, 1);
                                        db.updateTable(invoiceNumber + "", cvUpdate, DbHelper.COL_SALES_ORDER_InvoiceNumber + " = ?", DbHelper.TABLE_SALES_ORDER);
                                        db.updateTable(salesOrderid + "", cvUpdateLineItem, COL_LINE_ITEM_SalesOrderID + " = ?", DbHelper.TABLE_LINE_ITEM);


                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    System.out.println("error - " + error.toString());
                                }
                            });

                    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);

                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (isConfirmed) {
            btn_create_sale_order.setEnabled(false);
            Intent intent = new Intent(getApplicationContext(), SalesOrderOfflineActivity.class);
            CreateSalesOrderLast.this.finish();
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }
}