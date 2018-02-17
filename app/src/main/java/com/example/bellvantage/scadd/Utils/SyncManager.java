package com.example.bellvantage.scadd.Utils;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.example.bellvantage.scadd.DB.DbHelper;
import com.example.bellvantage.scadd.DB.MySingleton;
import com.example.bellvantage.scadd.R;
import com.example.bellvantage.scadd.domains.ReviseSalesOrderList;
import com.example.bellvantage.scadd.swf.Area;
import com.example.bellvantage.scadd.swf.CatalogueImage;
import com.example.bellvantage.scadd.swf.CategoryDetails;
import com.example.bellvantage.scadd.swf.DataBean;
import com.example.bellvantage.scadd.swf.DefProduct;
import com.example.bellvantage.scadd.swf.DeliveryLocations;
import com.example.bellvantage.scadd.swf.DeliveryPath;
import com.example.bellvantage.scadd.swf.DistributorDetails;
import com.example.bellvantage.scadd.swf.DistrictMerchant;
import com.example.bellvantage.scadd.swf.InvoiceLineItem;
import com.example.bellvantage.scadd.swf.InvoiceList2;
import com.example.bellvantage.scadd.swf.LoginUser;
import com.example.bellvantage.scadd.swf.Merchant;
import com.example.bellvantage.scadd.swf.MerchantClass;
import com.example.bellvantage.scadd.swf.MerchantDetails;
import com.example.bellvantage.scadd.swf.MerchantStock;
import com.example.bellvantage.scadd.swf.MerchantStockLineitems;
import com.example.bellvantage.scadd.swf.MerchantType;
import com.example.bellvantage.scadd.swf.MerchantVisit;
import com.example.bellvantage.scadd.swf.MerchantVisitReason;
import com.example.bellvantage.scadd.swf.OrderList;
import com.example.bellvantage.scadd.swf.PrimarySalesInvoice;
import com.example.bellvantage.scadd.swf.PrimarySalesInvoiceLineItemBean;
import com.example.bellvantage.scadd.swf.ProductBatch;
import com.example.bellvantage.scadd.swf.ProductList;
import com.example.bellvantage.scadd.swf.PromotionList;
import com.example.bellvantage.scadd.swf.ReturnInventory;
import com.example.bellvantage.scadd.swf.ReturnInventoryLineItem;
import com.example.bellvantage.scadd.swf.ReturnInventoryLineItemListBean;
import com.example.bellvantage.scadd.swf.ReturnTest;
import com.example.bellvantage.scadd.swf.ReturnType;
import com.example.bellvantage.scadd.swf.SalesOrderLineItem;
import com.example.bellvantage.scadd.swf.SalesRep;
import com.example.bellvantage.scadd.swf.SalesRepInventoryDetails;
import com.example.bellvantage.scadd.swf.SalesRepTarget;
import com.example.bellvantage.scadd.swf.ServiceCount;
import com.example.bellvantage.scadd.swf.SyncTables;
import com.example.bellvantage.scadd.swf.TargetCategory;
import com.example.bellvantage.scadd.swf.TargetCategoryLineItem;
import com.example.bellvantage.scadd.swf.VehicleInventry;
import com.example.bellvantage.scadd.web.HTTPPaths;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_INVOICE_ITEM_IsSync;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_INVOICE_LINE_ITEM_invoiceID;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_INVOICE_LINE_ITEM_issync;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_LINE_ITEM_IsSync;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_PRODUCT_BATCH_BatchID;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_PRODUCT_BATCH_ExpiryDate;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_PRODUCT_BATCH_ProductId;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_PRODUCT_BATCH_UnitSellingPrice;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_PRODUCT_CategoryId;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_PRODUCT_IsVat;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_PRODUCT_ProductId;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_PRODUCT_ProductName;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_PRODUCT_UnitSellingPrice;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_PRODUCT_VatRate;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_RETURN_INVENTORY_LINEITEM_creditNoteNo;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_RETURN_INVENTORY_LINEITEM_isSync;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_RETURN_INVENTORY_isSync;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_RETURN_INVENTORY_merchantId;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_RETURN_INVENTORY_returnDate;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_RETURN_INVENTORY_totalOutstanding;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SALES_ORDER_IsSync;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SALES_ORDER_JSON_IsSync;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SALES_REP_SalesRepId;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SRI_BATCH_ID;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SRI_PRODUCT_ID;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SRI_QTY_INHAND;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SYNC_LASTLOG_TIME;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SYNC_ROWCOUNT;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SYNC_ROWCOUNT_NEW;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SYNC_SALESREPID;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SYNC_STATUS;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SYNC_TABLE_NAME;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SYNC_TABLE_TIME;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TABLE_INVOICE_UTILIZATION_InvoiceId;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TABLE_INVOICE_UTILIZATION_UtilizedAmount;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_VEHICLE_INVENTORY_BatchID;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_VEHICLE_INVENTORY_LoadQuantity;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_VEHICLE_INVENTORY_ProductId;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_AREA;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_CATEGORY;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_DELIVERY_LOCATIONS;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_DISTRIBUTOR;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_DISTRICT;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_INVOICE_ITEM;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_INVOICE_LINE_ITEM;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_INVOICE_UTILIZATION;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_LINE_ITEM;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_MERCHANT;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_MERCHANT_CLASS;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_MERCHANT_STOCK;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_MERCHANT_TYPE;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_MVISIT;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_MVISIT_REASON;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_PRIMARY_SALES_INVOICE;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_PRIMARY_SALES_INVOICE_LINE_ITEM;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_PRODUCT;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_PRODUCT_BATCH;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_PROMOTION;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_PROMOTION_IMAGE;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_RETURN_INVENTORY;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_RETURN_INVENTORY_LINEITEM;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_RETURN_TYPE;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_REVISE_ORDER;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_SALESREP_INVENTORY;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_SALESREP_TARGET;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_SALES_ORDER;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_SALES_ORDER_JSON;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_SERVICES_COUNTS;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_TARGET_CATEGORY;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_TARGET_CATEGORY_LINE_ITEM;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_VEHICALE_INVENTORY;
import static com.example.bellvantage.scadd.DB.DbHelper.TBL_INVOICE_ITEM;
import static com.example.bellvantage.scadd.DB.DbHelper.TBL_INVOICE_LINE_ITEM;
import static com.example.bellvantage.scadd.DB.DbHelper.TBL_MERCHANT_MERCHANT_ID;
import static com.example.bellvantage.scadd.DB.DbHelper.TBL_MERCHANT_PATH_CODE;


/**
 * Created by Sachika on 27/06/2017.
 */

//http://113.59.211.246:9010/Service1.svc/GetProductCategoryList
public class SyncManager {


    private Context context;
    private DbHelper db;
    // SQLiteDatabase database = db.getWritableDatabase();
    int enteredUser;
    ProgressDialog dialog;
    String time_;
    int lineItemCount = 0;
    long today = DateManager.todayMillsecWithDateFormat("dd.MM.yyyy");
    int syncimg = R.mipmap.sync_img;


    int allRowCounts = 0;

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

    public static final String PrimaryInvoice = "Primary Invoice";
    public static final String Target = "Target";
    public static final String TargetCategory = "Target Category";

    public static final String salesorder_lineitem = "Sales Order Line Item";
    public static final String Invoice_line_item = "Invoice Line Item";

//
//    String[] string_table = {All, District, Category, Product, Product_batch, Return_type, Path,
//            Distributor, Salesrep, Merchant, Sales_order, Vehicle_inventory, Salesrep_inventory,
//            Invoice, salesorder_lineitem, Invoice_line_item};

    public SyncManager(Context context) {
        this.context = context;
        this.db = new DbHelper(context);

        Gson gson = new Gson();
        String getJson = SessionManager.pref.getString("user", "");
        LoginUser prefUser = gson.fromJson(getJson, LoginUser.class);
        this.enteredUser = prefUser.getUserTypeId();


    }


    //Sync Area Table-1
    public void syncArea(final int allsync, final int rowsCount, final int salesrep) {

        boolean areaDelete = db.deleteAllData(DbHelper.TABLE_AREA);
        if (areaDelete) {
            System.out.println("Succesfuly delete area table data");
        } else {
            System.out.println("Error occur deleting Area table");
        }
        final ArrayList<Area> areaArrayList = new ArrayList<>();
        //final Cursor cursor = db.getAllData(DbHelper.TABLE_AREA);
        String url = HTTPPaths.seriveUrl + "GetDEFAriaDetailsList";
        //if (cursor.getCount() == 0) {
        //System.out.println("No Data");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonObject object = Json.parse(response).asObject();
                        int id = object.get("ID").asInt();

                        if (id == 200) {
                            String newUrl = response.replace("{\"Data\":\"", "{\"Data\": ");
                            String new2Url = newUrl.replace("\",\"ID\":200}", "}");
                            String new3Url = new2Url.replace("\\", "");

                            if (response != null) {
                                try {
                                    JSONObject jsonObj_main = new JSONObject(new3Url);
                                    JSONArray jsonAry_main = jsonObj_main.getJSONArray("Data");
                                    for (int i = 0; i < jsonAry_main.length(); i++) {
                                        JSONObject jsonObj_2 = jsonAry_main.getJSONObject(i);
                                        // get Area
                                        String areaCode = jsonObj_2.getString("AriaCode");
                                        String areaName = jsonObj_2.getString("AriaName");
                                        Area area = new Area(areaCode, areaName);
                                        ContentValues cv = area.getAreaContentvalues();
                                        boolean success = db.insertDataAll(cv, DbHelper.TABLE_AREA);
                                        if (success) {

                                            System.out.println("Area syncing - " + i);
                                            if (i == jsonAry_main.length() - 1) {
                                                System.out.println("Area synced Completed , total - " + i);
                                                time_ = (new DateManager().getDateWithTime());
                                                //updateSyncTable(Area, time_, i + 1, salesrep, 1);
                                                if (allsync == 1) {
                                                    //    syncDistrict(1,0,salesrep);

                                                    //(new SyncListAdapter(context, R.layout.layout_for_sync_manually, (new ArrayList<SyncTables>()))).updateList((new ArrayList<SyncTables>()), 1, time_,i, 0);
                                                    //(new DisplayMessages.loginAsyncTask_newone()).execute(i+200);
                                                }
                                            }
                                        } else {
                                            System.out.println("Data is not inserted");
                                            //updateSyncTable(string_table[1], time_, rowsCount, salesrep, 0);
                                        }
                                        areaArrayList.add(area);
                                    }
                                } catch (JSONException e) {
                                    //Toast.makeText(context, "Json Error: ", Toast.LENGTH_LONG).show();
                                    System.out.println("Json Error" + e.getMessage());
                                }
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(context, "Volley Error ", Toast.LENGTH_LONG).show();
                        System.out.println("Area Error" + error.getMessage());
                    }
                });
        MySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(stringRequest);

//        } else {
//        }

    }

    public ArrayList<Area> getAllArea() {
        Cursor cursor = null;
        ArrayList<Area> areaArrayList = new ArrayList<>();
        cursor = db.getAllData(TABLE_AREA);
        if (cursor.getCount() == 0) {
            System.out.println("No Data");
            //syncArea();
        } else {
            while (cursor.moveToNext()) {
                String areaName = cursor.getString(cursor.getColumnIndex(DbHelper.TABLE_AREA_AREA_NAME));
                String areaCode = cursor.getString(cursor.getColumnIndex(DbHelper.TABLE_AREA_AREA_CODE));

                Area area = new Area(areaCode, areaName);
                areaArrayList.add(area);
            }
        }

        return areaArrayList;
    }


    //Sync District Table-2

    public void syncDistrict(final int allsync, final int salesrep) {

        boolean success = db.deleteAllData(TABLE_DISTRICT);
        if (success) {
            System.out.println("Succesfuly delete district table data");
        } else {
            System.out.println("Error occur deleting district table");
        }
        //Cursor cursor = db.getAllData(TABLE_DISTRICT);
        String url = HTTPPaths.seriveUrl + "GetDefDistrictList";
        //if (cursor.getCount() == 0) {
        //System.out.println("No Data");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonObject object = Json.parse(response).asObject();
                        int id = object.get("ID").asInt();

                        if (id == 200) {
                            String newUrl = response.replace("{\"Data\":\"", "{\"Data\": ");
                            String new2Url = newUrl.replace("\",\"ID\":200}", "}");
                            String new3Url = new2Url.replace("\\", "");

                            if (response != null) {
                                try {
                                    JSONObject jsonObj_main = new JSONObject(new3Url);
                                    JSONArray jsonAry_main = jsonObj_main.getJSONArray("Data");
                                    for (int i = 0; i < jsonAry_main.length(); i++) {
                                        JSONObject jsonObj_2 = jsonAry_main.getJSONObject(i);
                                        // get District
                                        String districtId = jsonObj_2.getString("DistrictID");
                                        String districtName = jsonObj_2.getString("DistrictName");
                                        DistrictMerchant districtObj = new DistrictMerchant(districtName, districtId);
                                        ContentValues cv = districtObj.getDistrictCcontetnValue();
                                        boolean success = db.insertDataAll(cv, TABLE_DISTRICT);
                                        if (success) {
                                            System.out.println("district syncing - " + i);
                                            if (i == jsonAry_main.length() - 1) {

                                                System.out.println("district synced Completed , total - " + i);
                                                Toast.makeText(context, "District table synced successfully", Toast.LENGTH_SHORT).show();
                                                time_ = (new DateManager().getDateWithTime());
                                                updateSyncTable(District, time_, i + 1, i + 1, salesrep, 1);
                                                if (allsync == 1) {
                                                    getCategoryFromServer(1, salesrep);
                                                }
                                                allRowCounts = allRowCounts + i + 1;
                                            }

                                        } else {
                                            System.out.println("Data is not inserted to district ");
                                            updateSyncTable(District, time_, 0, i + 1, salesrep, 0);
                                            //String table, String time, int rows,int rows_new, int salesrep, int status
                                        }
                                    }

                                } catch (JSONException e) {
                                    Toast.makeText(context, "Json Error: ", Toast.LENGTH_LONG).show();
                                    System.out.println("Json Error" + e.getMessage());
                                }
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Volley Error ", Toast.LENGTH_LONG).show();
                        System.out.println("Area Error" + error.getMessage());
                    }
                });
        MySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(stringRequest);

        getReturnInventory();
//        } else {
//        }
    }

    public ArrayList<DistrictMerchant> getAllDistrict() {
        Cursor cursor = null;
        ArrayList<DistrictMerchant> districtMerchantArrayList = new ArrayList<>();
        cursor = db.getAllData(TABLE_DISTRICT);
        if (cursor.getCount() == 0) {
            System.out.println("No Data");
            //syncArea();
        } else {
            while (cursor.moveToNext()) {
                String districtName = cursor.getString(cursor.getColumnIndex(DbHelper.TABLE_DISTRICT_DISTRICT_NAME));
                String districtCode = cursor.getString(cursor.getColumnIndex(DbHelper.TABLE_AREA_DISTRICT_CODE));

                DistrictMerchant districtMerchant = new DistrictMerchant(districtName, districtCode);
                districtMerchantArrayList.add(districtMerchant);
            }
        }

        return districtMerchantArrayList;
    }


    //Sync Path table-7
    public void syncPath(final int allsync, final int salesrep) {


        String url = HTTPPaths.seriveUrl + "GetDefSalesPathListBySalesId?salesRepID=" + salesrep;
        System.out.println("SyncPath URL " + url);

        Boolean success = db.deleteAllData(DbHelper.TABLE_PATH);

        if (success) {
            System.out.println("Succesfuly delete path table data");
        } else {
            System.out.println("Error occur deleting path table");
        }
        // if (success) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonObject object = Json.parse(response).asObject();
                        int id = object.get("ID").asInt();

                        if (id == 200) {
                            String newUrl = response.replace("{\"Data\":\"", "{\"Data\": ");
                            String new2Url = newUrl.replace("\",\"ID\":200}", "}");
                            String new3Url = new2Url.replace("\\", "");

                            if (response != null) {
                                try {
                                    JSONObject jsonObj_main = new JSONObject(new3Url);
                                    JSONArray jsonAry_main = jsonObj_main.getJSONArray("Data");
                                    for (int i = 0; i < jsonAry_main.length(); i++) {
                                        JSONObject jsonObj_2 = jsonAry_main.getJSONObject(i);

                                        // get path
                                        JSONObject _DeliveryPath = jsonObj_2.getJSONObject("_DeliveryPath");
                                        int DeliveryPathId = _DeliveryPath.getInt("DeliveryPathId");
                                        String PathName = _DeliveryPath.getString("PathName");

                                        DeliveryPath deliveryPath = new DeliveryPath(DeliveryPathId, PathName);
                                        ContentValues cv = deliveryPath.getDeliveryPathContentValues();
                                        boolean success = db.insertDataAll(cv, DbHelper.TABLE_PATH);
                                        if (success) {
                                            System.out.println("path syncing - " + i);
                                            if (i == jsonAry_main.length() - 1) {
                                                System.out.println("path synced Completed , total - " + i);
                                                Toast.makeText(context, "Path table synced successfully", Toast.LENGTH_SHORT).show();
                                                time_ = (new DateManager().getDateWithTime());
                                                updateSyncTable(Path, time_, i + 1, i + 1, salesrep, 1);
                                                if (allsync == 1) {
                                                    getSalesRep_and_DistributorDetailsFromServer_accordingto_salesrep(1, salesrep);
                                                }
                                                allRowCounts = allRowCounts + i + 1;
                                            }
                                            syncDeliveryLocations();
                                        } else {
                                            System.out.println("Data is not inserted to path");
                                            updateSyncTable(Path, time_, 0, i, salesrep, 0);
                                            //String table, String time, int rows,int rows_new, int salesrep, int status
                                        }

                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(context, "Json Error: ", Toast.LENGTH_LONG).show();
                                    System.out.println("Json Error" + e.getMessage());
                                }
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Volley Error ", Toast.LENGTH_LONG).show();
                        System.out.println("Area Error" + error.getMessage());
                    }
                });
        MySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(stringRequest);

        //}

    }

    public ArrayList<DeliveryPath> getAllPath() {
        Cursor cursor = null;
        ArrayList<DeliveryPath> deliveryPathArrayList = new ArrayList<>();
        cursor = db.getAllData(DbHelper.TABLE_PATH);
        if (cursor.getCount() == 0) {
            System.out.println("No Data");
            //syncArea();
        } else {
            while (cursor.moveToNext()) {
                int deliveryPathId = cursor.getInt(cursor.getColumnIndex(DbHelper.TABLE_PATH_DELIVERYPATHID));
                String pathName = cursor.getString(cursor.getColumnIndex(DbHelper.TABLE_PATH_PATHNAME));

                DeliveryPath deliveryPath = new DeliveryPath(deliveryPathId, pathName);
                deliveryPathArrayList.add(deliveryPath);
            }
        }

        return deliveryPathArrayList;
    }


    //Sync Merchant Table-10
    public void getMerchantListFromServer(final int allsync, final int userTypeId) {

        String url = HTTPPaths.seriveUrl + "GetAssignMerchantListToSalesRep?salesRepId=" + userTypeId;
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JsonObject object = Json.parse(response).asObject();
                            int id = object.get("ID").asInt();


                            if (id == 200) {
                                String newUrl = response.replace("{\"Data\":\"", "{\"Data\": ");
                                String new2Url = newUrl.replace("\",\"ID\":200}", "}");
                                String new3Url = new2Url.replace("\\", "");
                                System.out.println("Merchant JSON STRING : " + new3Url);
                                if (response != null) {
                                    try {

                                        MerchantDetails merchant, merchant0;

                                        JSONObject jsonObj_main = new JSONObject(new3Url);
                                        JSONArray jsonAry_main = jsonObj_main.getJSONArray("Data");
                                        for (int i = 0; i < jsonAry_main.length(); i++) {
                                            JSONObject jsonObj_2 = jsonAry_main.getJSONObject(i);
                                            // merchant details
                                            int MerchantId = jsonObj_2.getInt("MerchantId");
                                            String MerchantName = jsonObj_2.getString("MerchantName");
                                            String ContactNo1 = jsonObj_2.getString("ContactNo1");
                                            String ContactNo2 = jsonObj_2.getString("ContactNo2");
                                            String Longitude = jsonObj_2.getString("Longitude");
                                            String Latitude = jsonObj_2.getString("Latitude");
                                            int IsActive = jsonObj_2.getInt("IsActive");
                                            String EnteredDate = jsonObj_2.getString("EnteredDate");
                                            String EnteredUser = jsonObj_2.getString("EnteredUser");
                                            int DiscountRate = jsonObj_2.getInt("DiscountRate");
                                            int IsSync = 1;
                                            String SyncDate = jsonObj_2.getString("SyncDate");
                                            String BuildingNo = jsonObj_2.getString("BuildingNo");
                                            String Address1 = jsonObj_2.getString("Address1");
                                            String Address2 = jsonObj_2.getString("Address2");
                                            String City = jsonObj_2.getString("City");
                                            String ContactPerson = jsonObj_2.getString("ContactPerson");
                                            int AreaCode = jsonObj_2.getInt("AreaCode");
                                            String MerchantType = jsonObj_2.getString("MerchantType");
                                            String MerchantClass = jsonObj_2.getString("MerchantClass");
                                            int DistrictCode = jsonObj_2.getInt("DistrictCode");
                                            String UpdatedUser = jsonObj_2.getString("UpdatedUser");
                                            String UpdatedDate = jsonObj_2.getString("UpdatedDate");
                                            String ReferenceID = jsonObj_2.getString("ReferenceID");
                                            int IsVat = jsonObj_2.getInt("IsVat");
                                            String VatNo = jsonObj_2.getString("VatNo");
                                            String syncId = jsonObj_2.getString("SyncId");
                                            JSONArray deliveryloc = jsonObj_2.getJSONArray("_DeliveryLocations");
                                            int DeliveryPathID = deliveryloc.getJSONObject(0).getInt("DeliveryPathID");
                                            System.out.println("DeliveryPathID - " + DeliveryPathID);
                                            //int pathID = 0;
                                            //Get Return-Notes
                                            //GetReturnInventoryByMerchantWithOutStanding(MerchantId);

                                            if (!checkMerchantIsAvailable(MerchantId)) {
                                                merchant = new MerchantDetails(0, MerchantId, MerchantName, ContactNo1,
                                                        ContactNo2, Longitude, Latitude, IsActive, EnteredDate, EnteredUser, DiscountRate,
                                                        IsSync, SyncDate, BuildingNo, Address1, Address2, City, ContactPerson, AreaCode,
                                                        MerchantType, MerchantClass, DistrictCode, UpdatedUser, UpdatedDate, ReferenceID, IsVat,
                                                        VatNo, DeliveryPathID, syncId, 0, "");

                                                ContentValues cv = merchant.getMerchntDetailsContentValues();
                                                Boolean success = db.insertDataAll(cv, DbHelper.TABLE_MERCHANT);

                                                System.out.println(cv.getAsString("MerchantName"));

                                                if (success) {
                                                    if (i == jsonAry_main.length() - 1) {
                                                        System.out.println("merchant syncing last one - " + i);
                                                        Toast.makeText(context, "Merchant table synced successfully", Toast.LENGTH_SHORT).show();

                                                        time_ = (new DateManager().getDateWithTime());
                                                        updateSyncTable(Merchant, time_, i + 1, i + 1, userTypeId, 1);

                                                        allRowCounts = allRowCounts + i + 1;

                                                        if (allsync == 1) {   //because when we use this much longer thread ,it will get crash
                                                            getAllSalesOrdersFromServer(1, userTypeId);
                                                        }
                                                    }
                                                } else {
                                                    System.out.println("Data is not inserted to merchant");
                                                    updateSyncTable(Merchant, time_, 0, i, userTypeId, 0);
                                                }
                                            }
                                        }//for
                                    } catch (JSONException e) {
                                        System.out.println("Sync Merchant Json Error" + e.getMessage());
                                    }
                                }
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("Sync Merchant Volley Error " + error.getMessage());
                        }
                    }
            );
            MySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(stringRequest);


        } catch (Exception e) {
            System.out.println("Sync Merchant Error" + e.getMessage());
        }
    }

    private boolean checkMerchantIsAvailable(int merchantId) {
        boolean isAvailable = false;
        SQLiteDatabase database = db.getWritableDatabase();
        Cursor cursor = null;
        try {

            cursor = database.rawQuery("SELECT * FROM " + TABLE_MERCHANT + " WHERE " +
                    TBL_MERCHANT_MERCHANT_ID + " = " + merchantId, null);

            if (cursor.getCount() > 0) {
                isAvailable = true;
                System.out.println(merchantId + " merchant is Available");
            }
        } catch (Exception e) {
            System.out.println("Error at Merchant Is Available: " + e.getMessage());
        } finally {
            database.close();
            cursor.close();
        }

        return isAvailable;
    }


    //1
    public void syncMerchantTableIntoSqlite(final int allsync, final int userTypeId) {
        System.out.println("Entering syncMerchant main");
        try {
            SQLiteDatabase database = db.getReadableDatabase();
            Cursor cursor = null;
            //String selectQuery = "SELECT * FROM " + DbHelper.TABLE_MERCHANT + " WHERE " + DbHelper.TBL_MERCHANT_IS_SYNC + "=" + 0;
            //Cursor cursor = database.rawQuery(selectQuery, null);
            cursor = db.getAllData(DbHelper.TABLE_MERCHANT);
            System.out.println("Entering syncMerchant main cursor");
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    int IsSync = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_IS_SYNC));
                    int MerchantId = cursor.getInt(cursor.getColumnIndex(TBL_MERCHANT_MERCHANT_ID));
                    int SequenceId = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_SEQUENCE_ID));
                    String MerchantName = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_MERCHANT_NAME));
                    String ContactNo1 = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_CONTACT_NO1));
                    String ContactNo2 = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_CONTACT_NO2));
                    double Longitude = cursor.getDouble(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_LONGITUDE));
                    double Latitude = cursor.getDouble(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_LATITUDE));
                    int IsActive = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_IS_ACTIVE));
                    String EnteredDate = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_ENTERED_DATE));
                    String EnteredUser = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_ENTERED_USER));
                    int DiscountRate = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_DISCOUNT_RATE));
                    int IsSyncs = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_IS_SYNC));
                    String SyncDate = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_SYNC_DATE));
                    String BuildingNo = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_BUILDING_NO));
                    String Address1 = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_ADDRESS_1));
                    String Address2 = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_ADDRESS_2));
                    String City = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_CITY));
                    String ContactPerson = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_CONTACT_PERSON));
                    int AreaCode = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_AREA_CODE));
                    String MerchantType = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_MERCHANT_TYPE));
                    String MerchantClass = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_MERCHANT_CLASS));
                    int DistrictCode = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_DISTRICT_CODE));
                    int UpdatedUser = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_UPDATED_USER));
                    String UpdatedDate = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_UPDATED_DATE));
                    String ReferenceID = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_REFERENCE_ID));
                    int IsVat = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_ISVAT));
                    String VatNo = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_VAT_NO));
                    int pathCode = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_PATH_CODE));
                    String syncId = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_SYNC_ID));

                    MerchantName = MerchantName.replace(" ", "%20");
                    BuildingNo = BuildingNo.replace(" ", "%20");
                    Address1 = Address1.replace(" ", "%20");
                    Address2 = Address2.replace(" ", "%20");
                    ContactPerson = ContactPerson.replace(" ", "%20");


                    if (NetworkConnection.checkNetworkConnection(context) == true) {
                        if (IsSync != 1 && MerchantId == 0) {
                            System.out.println("Entering syncMerchant main cursor if");
                            registerMerchant(SequenceId, MerchantName, ContactNo1, ContactNo2, Longitude, Latitude, IsActive, EnteredDate, EnteredUser, DiscountRate, BuildingNo, Address1, Address2, City, ContactPerson, AreaCode, MerchantType, MerchantClass, DistrictCode, ReferenceID, IsVat, VatNo, pathCode, SequenceId, syncId);
                        }
                        if (IsSync != 1 && MerchantId != 0) {
                            UpdateDefMerachant("" + SequenceId, MerchantId, MerchantName, ContactNo1, ContactNo2, Longitude,
                                    Latitude, IsActive,
                                    DiscountRate, IsActive, SyncDate, BuildingNo, Address1, Address2, City, ContactPerson, AreaCode,
                                    MerchantType, MerchantClass, DistrictCode, UpdatedUser, ReferenceID, IsVat, VatNo, pathCode, context, syncId);
                        }
                    }

                }
                cursor.close();
                int afectedRows = database.delete(TABLE_MERCHANT, DbHelper.TBL_MERCHANT_IS_SYNC + " = ? ", new String[]{1 + ""});
                System.out.println(afectedRows + " row(s)  deleted in " + TABLE_MERCHANT);
                getMerchantListFromServer(allsync, userTypeId);
            } else {
                int afectedRows = database.delete(TABLE_MERCHANT, DbHelper.TBL_MERCHANT_IS_SYNC + " = ? ", new String[]{1 + ""});
                System.out.println(afectedRows + " row(s)  deleted in " + TABLE_MERCHANT);
                getMerchantListFromServer(allsync, userTypeId);
            }
        } catch (Exception e) {
            System.out.println("First Sync Error: " + e.getMessage());
        }


    }


    private void registerMerchant(final int sequencedId, String merchantName, String contact1, String contact2, double lon,
                                  double lat, int isActive, final String enteredDate, String enteredUser, int discountRate,
                                  String buildingNo, String address1, String address2, String city, String contactPerson,
                                  int areaCode, String merchantType, String merchantClass, int districCode, String referenceID,
                                  int vat, String vatNo, final int pathCode, int sequenceID, String syncId) {
        System.out.println("Entering server registration");
        String url = HTTPPaths.seriveUrl + "SaveDefMerachant?merchantName=" + merchantName + "&contactNo1=" + contact1 + "&contactNo2=" + contact2 + "&longitude=" + lon + "&latitude=" + lat + "&isActive=" + isActive + "&enteredUser=" + enteredUser + "&discountRate=" + discountRate + "&buildingNo=" + buildingNo + "&address1=" + address1 + "&address2=" + address2 + "&city=" + city + "&contactPerson=" + contactPerson + "&areaCode=" + areaCode + "&merchantType=" + merchantType + "&merchantClass=" + merchantClass + "&districtCode=" + districCode + "&referenceID=" + referenceID + "&isVat=" + vat + "&vatNo=" + vatNo + "&syncId=" + syncId;
        System.out.println("Registration URL " + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonObject object = Json.parse(response).asObject();
                        int id = object.get("ID").asInt();
                        String MerchantID = object.get("Data").asString();
                        System.out.println("ID of path " + id);
                        if (id == 200) {
                            registerPath(MerchantID, pathCode);
                            ContentValues cv = new ContentValues();
                            cv.put("MerchantId", MerchantID);
                            cv.put("IsSync", 1);
                            boolean success = db.updateTable("" + sequencedId, cv, DbHelper.TBL_MERCHANT_SEQUENCE_ID + " =?", DbHelper.TABLE_MERCHANT);
                            if (success == true) {
                                System.out.println("merchant sync and updated");
                            } else {
                                System.out.println("merchant sync , not  updated");
                            }
                            System.out.println("Merchant syncing is successful");
                        } else {
                            System.out.println("Merchant syncing is fail");
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Merchant syncing error " + error.getMessage());
                    }
                }
        );
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }

    private void registerPath(String merchantId, int pathCode) {
        String pathID = Integer.toString(pathCode);

        String url = HTTPPaths.seriveUrl + "SaveDeliveryLocation?merchantID=" + merchantId + "&deliveryPathID=" + pathID;
        System.out.println("Path URL" + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonObject object = Json.parse(response).asObject();
                        int id = object.get("ID").asInt();
                        System.out.println("Path ID " + id);
                        if (id == 200) {
                            System.out.println("Merchant path syncing is successful");
                        } else {
                            Toast.makeText(context, "Merchant syncing fail.. try again", Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Path sync error" + error.getMessage());
                    }
                }
        );
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    private void UpdateDefMerachant(String sequenceId, final int merchantId, String merchantName,
                                    String contact1, String contact2, double lon, double lat,
                                    int isActive, int discountRate, int isSync, String syncDate,
                                    String buildingNo, String address1, String address2, String city,
                                    String contactPerson, int areaCode, String merchantType, String merchantClass,
                                    int districCode, int updateUserd, String referenceID, int vat,
                                    String vatNo, int pathCode, final Context context, String syncId) {

        final ContentValues cv = new ContentValues();
        cv.put("IsSync", 1);
        if (NetworkConnection.checkNetworkConnection(context) == true) {
            String url = HTTPPaths.seriveUrl + "UpdateDefMerachant?merchantId=" + merchantId + "&merchantName="
                    + merchantName + "&contactNo1=" + contact1 + "&contactNo2="
                    + contact2 + "&longitude=" + lon + "&latitude=" + lat + "&isActive=" + isActive +
                    "&discountRate=" + discountRate + "&isSync=" + isSync + "&syncDate=" + syncDate +
                    "&buildingNo=" + buildingNo + "&address1=" + address1 + "&address2=" + address2 +
                    "&city=" + city + "&contactPerson=" + contactPerson + "&areaCode=" + areaCode + "&merchantType="
                    + merchantType + "&merchantClass=" + merchantClass + "&districtCode=" + districCode + "&updatedUser="
                    + updateUserd + "&referenceID=" + referenceID + "&isVat=" + vat + "&vatNo=" + vatNo + "&syncId=" + syncId;


            System.out.println("Update URL " + url);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JsonObject object = Json.parse(response).asObject();
                            int id = object.get("ID").asInt();
                            System.out.println("Response ID " + id);
                            if (id == 200) {

                                System.out.println("Sync Manager merchant update");
                                db.updateTable("" + merchantId, cv, TBL_MERCHANT_MERCHANT_ID + " =?", DbHelper.TABLE_MERCHANT);
                                System.out.println("network monitor merchant update SQLite");
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("Update Error" + error.getMessage());
                        }
                    }
            );
            MySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(stringRequest);
        }
    }


    public ArrayList<MerchantDetails> getMerchantList() {
        Cursor cursor = null;
        ArrayList<MerchantDetails> merchantDetailsArrayList = new ArrayList<>();
        SQLiteDatabase database = db.getReadableDatabase();
        //String selectQuery = "SELECT * FROM " + DbHelper.TABLE_MERCHANT + " WHERE " + DbHelper.TBL_MERCHANT_MERCHANT_ID + "!=" + 0+" AND "+TBL_MERCHANT_PATH_CODE+" = ";
        String selectQuery = "SELECT * FROM " + DbHelper.TABLE_MERCHANT + " WHERE " + TBL_MERCHANT_MERCHANT_ID + "!=" + 0;

        cursor = database.rawQuery(selectQuery, null);
        if (cursor.getCount() == 0) {
            System.out.println("No Data in  " + TABLE_MERCHANT);
            //syncMerchantTableIntoSqlite(enteredUser);
        } else {
            while (cursor.moveToNext()) {

                int SequenceId = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_SEQUENCE_ID));
                int MerchantId = cursor.getInt(cursor.getColumnIndex(TBL_MERCHANT_MERCHANT_ID));
                String MerchantName = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_MERCHANT_NAME));
                String ContactNo1 = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_CONTACT_NO1));
                String ContactNo2 = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_CONTACT_NO2));
                String Longitude = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_LONGITUDE));
                String Latitude = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_LATITUDE));
                int IsActive = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_IS_ACTIVE));
                String EnteredDate = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_ENTERED_DATE));
                String EnteredUser = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_ENTERED_USER));
                int DiscountRate = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_DISCOUNT_RATE));
                int IsSync = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_IS_SYNC));
                String SyncDate = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_SYNC_DATE));
                String BuildingNo = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_BUILDING_NO));
                String Address1 = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_ADDRESS_1));
                String Address2 = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_ADDRESS_2));
                String City = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_CITY));
                String ContactPerson = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_CONTACT_PERSON));
                int AreaCode = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_AREA_CODE));
                int pathCode = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_PATH_CODE));
                String MerchantType = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_MERCHANT_TYPE));
                String MerchantClass = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_MERCHANT_CLASS));
                int DistrictCode = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_DISTRICT_CODE));
                String UpdatedUser = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_UPDATED_USER));
                String UpdatedDate = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_UPDATED_DATE));
                String ReferenceID = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_REFERENCE_ID));
                int IsVat = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_ISVAT));
                String VatNo = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_VAT_NO));
                String SyncId = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_SYNC_ID));
                int isCredit = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_IS_CREDIT));
                String creditDays = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_CREDIT_DAYS));


                MerchantDetails merchant = new MerchantDetails(SequenceId, MerchantId, MerchantName,
                        ContactNo1, ContactNo2, Longitude, Latitude, IsActive, EnteredDate, EnteredUser,
                        DiscountRate, IsSync, SyncDate, BuildingNo, Address1, Address2, City, ContactPerson,
                        AreaCode, MerchantType, MerchantClass, DistrictCode, UpdatedUser, UpdatedDate,
                        ReferenceID, IsVat, VatNo, pathCode, SyncId, isCredit, creditDays);

                //System.out.println("merchant name - " +MerchantName);
                merchantDetailsArrayList.add(merchant);

            }
        }

        return merchantDetailsArrayList;
    }

    public ArrayList<MerchantDetails> getMerchantListVsPath(int pathid) {

        Cursor cursor = null;
        ArrayList<MerchantDetails> merchantDetailsArrayList = new ArrayList<>();
        SQLiteDatabase database = db.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + DbHelper.TABLE_MERCHANT + " WHERE " + TBL_MERCHANT_MERCHANT_ID + "!=" + 0 + " AND " + TBL_MERCHANT_PATH_CODE + " = " + pathid;
        System.out.println("selectQuery - " + selectQuery);

        cursor = database.rawQuery(selectQuery, null);
        if (cursor != null) {

            if (cursor.getCount() == 0) {
                System.out.println("No Data in  " + TABLE_MERCHANT);
                //syncMerchantTableIntoSqlite(enteredUser);
            } else {
                while (cursor.moveToNext()) {

                    int SequenceId = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_SEQUENCE_ID));
                    int MerchantId = cursor.getInt(cursor.getColumnIndex(TBL_MERCHANT_MERCHANT_ID));
                    String MerchantName = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_MERCHANT_NAME));
                    String ContactNo1 = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_CONTACT_NO1));
                    String ContactNo2 = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_CONTACT_NO2));
                    String Longitude = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_LONGITUDE));
                    String Latitude = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_LATITUDE));
                    int IsActive = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_IS_ACTIVE));
                    String EnteredDate = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_ENTERED_DATE));
                    String EnteredUser = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_ENTERED_USER));
                    int DiscountRate = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_DISCOUNT_RATE));
                    int IsSync = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_IS_SYNC));
                    String SyncDate = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_SYNC_DATE));
                    String BuildingNo = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_BUILDING_NO));
                    String Address1 = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_ADDRESS_1));
                    String Address2 = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_ADDRESS_2));
                    String City = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_CITY));
                    String ContactPerson = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_CONTACT_PERSON));
                    int AreaCode = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_AREA_CODE));
                    int pathCode = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_PATH_CODE));
                    String MerchantType = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_MERCHANT_TYPE));
                    String MerchantClass = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_MERCHANT_CLASS));
                    int DistrictCode = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_DISTRICT_CODE));
                    String UpdatedUser = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_UPDATED_USER));
                    String UpdatedDate = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_UPDATED_DATE));
                    String ReferenceID = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_REFERENCE_ID));
                    int IsVat = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_ISVAT));
                    String VatNo = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_VAT_NO));
                    String SyncId = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_SYNC_ID));
                    int isCredit = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_IS_CREDIT));
                    String creditDays = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_CREDIT_DAYS));


                    MerchantDetails merchant = new MerchantDetails(SequenceId, MerchantId, MerchantName,
                            ContactNo1, ContactNo2, Longitude, Latitude, IsActive, EnteredDate, EnteredUser,
                            DiscountRate, IsSync, SyncDate, BuildingNo, Address1, Address2, City, ContactPerson,
                            AreaCode, MerchantType, MerchantClass, DistrictCode, UpdatedUser, UpdatedDate,
                            ReferenceID, IsVat, VatNo, pathCode, SyncId, isCredit, creditDays);

                    //System.out.println("merchant name - " +MerchantName);
                    merchantDetailsArrayList.add(merchant);

                }
            }
        }

        return merchantDetailsArrayList;
    }

    public ArrayList<MerchantDetails> getMerchantDetailsAccordingtoMerchantId(int merchantid) {

        Cursor cursor = null;
        ArrayList<MerchantDetails> merchantDetailsArrayList = new ArrayList<>();
        SQLiteDatabase database = db.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + DbHelper.TABLE_MERCHANT + " WHERE " + TBL_MERCHANT_MERCHANT_ID + " = " + merchantid;
        System.out.println("selectQuery - " + selectQuery);

        cursor = database.rawQuery(selectQuery, null);
        if (cursor != null) {

            if (cursor.getCount() == 0) {
                System.out.println("No Data in  " + TABLE_MERCHANT);
                //syncMerchantTableIntoSqlite(enteredUser);
            } else {
                while (cursor.moveToNext()) {

                    int SequenceId = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_SEQUENCE_ID));
                    int MerchantId = cursor.getInt(cursor.getColumnIndex(TBL_MERCHANT_MERCHANT_ID));
                    String MerchantName = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_MERCHANT_NAME));
                    String ContactNo1 = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_CONTACT_NO1));
                    String ContactNo2 = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_CONTACT_NO2));
                    String Longitude = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_LONGITUDE));
                    String Latitude = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_LATITUDE));
                    int IsActive = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_IS_ACTIVE));
                    String EnteredDate = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_ENTERED_DATE));
                    String EnteredUser = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_ENTERED_USER));
                    int DiscountRate = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_DISCOUNT_RATE));
                    int IsSync = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_IS_SYNC));
                    String SyncDate = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_SYNC_DATE));
                    String BuildingNo = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_BUILDING_NO));
                    String Address1 = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_ADDRESS_1));
                    String Address2 = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_ADDRESS_2));
                    String City = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_CITY));
                    String ContactPerson = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_CONTACT_PERSON));
                    int AreaCode = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_AREA_CODE));
                    int pathCode = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_PATH_CODE));
                    String MerchantType = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_MERCHANT_TYPE));
                    String MerchantClass = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_MERCHANT_CLASS));
                    int DistrictCode = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_DISTRICT_CODE));
                    String UpdatedUser = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_UPDATED_USER));
                    String UpdatedDate = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_UPDATED_DATE));
                    String ReferenceID = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_REFERENCE_ID));
                    int IsVat = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_ISVAT));
                    String VatNo = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_VAT_NO));
                    String SyncId = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_SYNC_ID));
                    int isCredit = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_IS_CREDIT));
                    String creditDays = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_CREDIT_DAYS));

                    MerchantDetails merchant = new MerchantDetails(SequenceId, MerchantId, MerchantName,
                            ContactNo1, ContactNo2, Longitude, Latitude, IsActive, EnteredDate, EnteredUser,
                            DiscountRate, IsSync, SyncDate, BuildingNo, Address1, Address2, City, ContactPerson,
                            AreaCode, MerchantType, MerchantClass, DistrictCode, UpdatedUser, UpdatedDate,
                            ReferenceID, IsVat, VatNo, pathCode, SyncId, isCredit, creditDays);

                    //System.out.println("merchant name - " +MerchantName);
                    merchantDetailsArrayList.add(merchant);
                }
            }
        }
        return merchantDetailsArrayList;
    }


    //Merchants
    public void syncDeliveryLocations() {
        System.out.println("Sync Delivery Locations");
        Cursor cursor = db.getAllData(DbHelper.TABLE_DELIVERY_LOCATIONS);
        Boolean success;
        if (cursor.getCount() > 0) {
            success = db.deleteAllData(DbHelper.TABLE_DELIVERY_LOCATIONS);
            if (success == true) {
                getDeliveryLocationsFromServer();
            }
        } else {
            getDeliveryLocationsFromServer();
        }
    }

    private void getDeliveryLocationsFromServer() {

        final ArrayList<DeliveryPath> deliveryPaths = getAllPath();

        boolean success = db.deleteAllData(TABLE_DELIVERY_LOCATIONS);

        if (success) {
            System.out.println("Successfully delete delivery location table data");
        } else {
            System.out.println("Error occur deleting delivery location table");
        }

        //System.out.println("Delivery path location " + deliveryPaths.size());
        for (int i = 0; i < deliveryPaths.size(); i++) {
            int pathId = deliveryPaths.get(i).getDeliveryPathId();

            String url = HTTPPaths.seriveUrl + "GetDeliveryLocationByPathId?deliveryPathID=" + pathId;
            //System.out.println("SyncDeliveryLocation URL " + url);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JsonObject object = Json.parse(response).asObject();
                            int id = object.get("ID").asInt();

                            if (id == 200) {
                                String newUrl = response.replace("{\"Data\":\"", "{\"Data\": ");
                                String new2Url = newUrl.replace("\",\"ID\":200}", "}");
                                String new3Url = new2Url.replace("\\", "");


                                if (response != null) {
                                    try {
                                        JSONObject jsonObj_main = new JSONObject(new3Url);
                                        JSONArray jsonAry_main = jsonObj_main.getJSONArray("Data");
                                        for (int i = 0; i < jsonAry_main.length(); i++) {
                                            JSONObject jsonObj_2 = jsonAry_main.getJSONObject(i);
                                            // get daa
                                            int MerchantID = jsonObj_2.getInt("MerchantID");
                                            int DeliveryPathID = jsonObj_2.getInt("DeliveryPathID");
                                            System.out.println(" MerchantID " + MerchantID + " DeliveryPathID " + DeliveryPathID);
                                            DeliveryLocations deliveryLocations = new DeliveryLocations(MerchantID, DeliveryPathID);
                                            ContentValues cv = deliveryLocations.getDeliveryLocationsContentValues();
                                            boolean success = db.insertDataAll(cv, DbHelper.TABLE_DELIVERY_LOCATIONS);
                                            if (success) {
                                                System.out.println("delivery locations syncing -" + i);
                                                if (i == jsonAry_main.length() - 1) {
                                                    System.out.println("delivery locations synced Completed , total - " + i);
                                                }
                                            } else {
                                                System.out.println("Data is not inserted to delivery locations");
                                            }

                                        }
                                    } catch (JSONException e) {
                                        Toast.makeText(context, "Json Error: ", Toast.LENGTH_LONG).show();
                                        System.out.println(DbHelper.TABLE_DELIVERY_LOCATIONS + " Json Error" + e.getMessage());
                                    }
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, "Volley Error ", Toast.LENGTH_LONG).show();
                            System.out.println("Area Error" + error.getMessage());
                        }
                    });
            MySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(stringRequest);

        }
    }

    public ArrayList<MerchantDetails> getMerchantListByPathID(int pathId) {

        Cursor diliveryCursor = null;
        SQLiteDatabase database = db.getReadableDatabase();
        ArrayList<MerchantDetails> merchantDetailsArrayList = new ArrayList<>();
        ArrayList<DeliveryLocations> deliveryLocationsArrayList = new ArrayList<>();
        String deliveryLocationQuery = "SELECT * FROM " + DbHelper.TABLE_DELIVERY_LOCATIONS + " WHERE " + DbHelper.TABLE_DELIVERY_LOCATION_DELIVERY_PATH_ID + " = " + pathId;
        diliveryCursor = database.rawQuery(deliveryLocationQuery, null);
        if (diliveryCursor.getCount() == 0) {
            System.out.println("No Data");
            syncDeliveryLocations();
        } else {
            while (diliveryCursor.moveToNext()) {
                int MerchantID = diliveryCursor.getInt(diliveryCursor.getColumnIndex(DbHelper.TABLE_DELIVERY_LOCATION_MERCHANT_ID));
                int DeliveryPathID = diliveryCursor.getInt(diliveryCursor.getColumnIndex(DbHelper.TABLE_DELIVERY_LOCATION_DELIVERY_PATH_ID));
                DeliveryLocations deliveryLocations = new DeliveryLocations(MerchantID, DeliveryPathID);
                deliveryLocationsArrayList.add(deliveryLocations);
            }
        }

        for (int i = 0; i < deliveryLocationsArrayList.size(); i++) {
            int MerchantId = deliveryLocationsArrayList.get(i).getMerchantID();
            Cursor cursor = null;
            String selectQuery = "SELECT * FROM " + DbHelper.TABLE_MERCHANT + " WHERE " + TBL_MERCHANT_MERCHANT_ID + "=" + MerchantId;
            cursor = database.rawQuery(selectQuery, null);
            if (cursor.getCount() == 0) {
                System.out.println("No Data");
                //syncMerchantTableIntoSqlite(enteredUser);
            } else {
                while (cursor.moveToNext()) {
                    int SequenceId = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_SEQUENCE_ID));
                    int MerchantId1 = cursor.getInt(cursor.getColumnIndex(TBL_MERCHANT_MERCHANT_ID));
                    String MerchantName = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_MERCHANT_NAME));
                    String ContactNo1 = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_CONTACT_NO1));
                    String ContactNo2 = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_CONTACT_NO2));
                    String Longitude = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_LONGITUDE));
                    String Latitude = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_LATITUDE));
                    int IsActive = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_IS_ACTIVE));
                    String EnteredDate = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_ENTERED_DATE));
                    String EnteredUser = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_ENTERED_USER));
                    int DiscountRate = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_DISCOUNT_RATE));
                    int IsSync = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_IS_SYNC));
                    String SyncDate = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_SYNC_DATE));
                    String BuildingNo = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_BUILDING_NO));
                    String Address1 = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_ADDRESS_1));
                    String Address2 = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_ADDRESS_2));
                    String City = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_CITY));
                    String ContactPerson = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_CONTACT_PERSON));
                    int AreaCode = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_AREA_CODE));
                    int pathCode = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_PATH_CODE));
                    String MerchantType = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_MERCHANT_TYPE));
                    String MerchantClass = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_MERCHANT_CLASS));
                    int DistrictCode = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_DISTRICT_CODE));
                    String UpdatedUser = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_UPDATED_USER));
                    String UpdatedDate = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_UPDATED_DATE));
                    String ReferenceID = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_REFERENCE_ID));
                    int IsVat = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_ISVAT));
                    String VatNo = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_VAT_NO));
                    String SyncId = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_SYNC_ID));
                    int isCredit = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_IS_CREDIT));
                    String creditDays = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_CREDIT_DAYS));


                    MerchantDetails merchant = new MerchantDetails(SequenceId, MerchantId1,
                            MerchantName, ContactNo1, ContactNo2, Longitude,
                            Latitude, IsActive, EnteredDate, EnteredUser, DiscountRate,
                            IsSync, SyncDate, BuildingNo, Address1, Address2, City,
                            ContactPerson, AreaCode, MerchantType, MerchantClass,
                            DistrictCode, UpdatedUser, UpdatedDate, ReferenceID,
                            IsVat, VatNo, pathCode, SyncId, isCredit, creditDays);
                    merchantDetailsArrayList.add(merchant);
                }
            }

        }

        return merchantDetailsArrayList;
    }

    //sync SaleOrders
    public void syncReviseSalesOrder(int salesRepId) {

        /*Cursor cursor = db.getAllData(TABLE_LINE_ITEM);
        Cursor cursor2 = db.getAllData(TABLE_SALES_ORDER);

        if (cursor2.getCount() > 0) {
            db.deleteAllData(TABLE_SALES_ORDER);
            System.out.println(TABLE_SALES_ORDER + " data is Deleted ");
        }

        if (cursor.getCount() > 0) {
            db.deleteAllData(TABLE_LINE_ITEM);
            System.out.println(TABLE_LINE_ITEM + " data is Deleted ");
        }*/

        System.out.println("====================================================================================");
        int afectedRows;
        SQLiteDatabase database = db.getWritableDatabase();
        afectedRows = database.delete(TABLE_SALES_ORDER, COL_SALES_ORDER_IsSync + " = ?", new String[]{1 + ""});
        System.out.println(afectedRows + " row(s)  deleted in " + TABLE_SALES_ORDER);

        afectedRows = database.delete(TABLE_LINE_ITEM, COL_LINE_ITEM_IsSync + " = ?", new String[]{1 + ""});
        System.out.println(afectedRows + " row(s)  deleted in " + TABLE_LINE_ITEM);

        afectedRows = database.delete(TABLE_SALES_ORDER_JSON, COL_SALES_ORDER_JSON_IsSync + " = ?", new String[]{1 + ""});
        System.out.println(afectedRows + " row(s) deleted in " + TABLE_SALES_ORDER_JSON);
        System.out.println("====================================================================================");

        getAllSalesOrdersFromServer(0, salesRepId);

    }


    public void getAllSalesOrdersFromServer(final int allsync, final int salesRepId) {
        int afectedRows;
        SQLiteDatabase database = db.getWritableDatabase();
        afectedRows = database.delete(TABLE_SALES_ORDER, COL_SALES_ORDER_IsSync + " = ?", new String[]{1 + ""});
        System.out.println(afectedRows + " row(s)  deleted in " + TABLE_SALES_ORDER);

        afectedRows = database.delete(TABLE_LINE_ITEM, COL_LINE_ITEM_IsSync + " = ?", new String[]{1 + ""});
        System.out.println(afectedRows + " row(s)  deleted in " + TABLE_LINE_ITEM);

        String url = HTTPPaths.seriveUrl + "GetSalesOrderBySalesRepId?salesRepId=" + salesRepId + "&withSalesOrderLineItem=1&withSalesRep=0&withMerchant=1";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonObject object = Json.parse(response).asObject();
                        int id = object.get("ID").asInt();

                        if (id == 200) {
                            String newUrl = response.replace("{\"Data\":\"", "{\"Data\": ");
                            String new2Url = newUrl.replace("\",\"ID\":200}", "}");
                            String new3Url = new2Url.replace("\\", "");

                            if (response != null) {
                                try {
                                    JSONObject jsonObj_main = new JSONObject(new3Url);
                                    JSONArray jsonAry_main = jsonObj_main.getJSONArray("Data");
                                    for (int i = 0; i < jsonAry_main.length(); i++) {

                                        JSONObject jsonObj_2 = jsonAry_main.getJSONObject(i);

                                        //GET LINE ITEMS
                                        int InvoiceNumber = jsonObj_2.getInt("InvoiceNumber");
                                        String EstimateDeliveryDate = jsonObj_2.getString("EstimateDeliveryDate");
                                        double TotalDiscount = jsonObj_2.getDouble("TotalDiscount");
                                        double TotalAmount = jsonObj_2.getDouble("TotalAmount");
                                        String SyncDate = jsonObj_2.getString("SyncDate");
                                        int IsSync = 1;
                                        int SaleStatus = jsonObj_2.getInt("SaleStatus");
                                        String EnteredUser = jsonObj_2.getString("EnteredUser");
                                        String EnteredDate = jsonObj_2.getString("EnteredDate");
                                        String UpdatedUser = jsonObj_2.getString("UpdatedUser");
                                        String UpdatedDate = jsonObj_2.getString("UpdatedDate");
                                        String SaleDate = jsonObj_2.getString("SaleDate");
                                        int SaleTypeId = jsonObj_2.getInt("SaleTypeId");
                                        int SalesRepId = jsonObj_2.getInt("SalesRepId");
                                        int DitributorId = jsonObj_2.getInt("DitributorId");
                                        int MerchantId = jsonObj_2.getInt("MerchantId");
                                        int SalesOrderId = jsonObj_2.getInt("SalesOrderId");
                                        int TotalVAT = jsonObj_2.getInt("TotalVAT");
                                        int IsVat = jsonObj_2.getInt("IsVat");
                                        int IsReturn = jsonObj_2.getInt("IsReturn");
                                        int IsCredit = jsonObj_2.getInt("IsCredit");
                                        String CreditDays = jsonObj_2.getString("CreditDays");

                                        //System.out.println("Invoice Id: " + InvoiceNumber);
                                        //Merchant Object
                                        JSONObject merchantObj = jsonObj_2.getJSONObject("_DefMerchant");
                                        String MerchantName = merchantObj.getString("MerchantName");
                                        //System.out.println(" MerchantName Sales" + MerchantName);

                                        //INSERT SALES ORDER INTO SQLITE db
                                        OrderList orderList = new OrderList(InvoiceNumber, EstimateDeliveryDate, TotalDiscount, TotalAmount,
                                                SyncDate, IsSync, SaleStatus, EnteredUser, EnteredDate, UpdatedUser, UpdatedDate, SaleDate,
                                                SaleTypeId, SalesRepId, DitributorId, MerchantId, SalesOrderId, TotalVAT, IsVat, IsReturn, MerchantName, IsCredit, CreditDays);

                                        ContentValues cv = orderList.getOrderListContentValues();
                                        boolean success = db.insertDataAll(cv, DbHelper.TABLE_SALES_ORDER);
                                        if (success) {
                                            if (i == jsonAry_main.length() - 1) {
                                                time_ = (new DateManager().getDateWithTime());
                                                Toast.makeText(context, "Sales order table synced successfully", Toast.LENGTH_SHORT).show();
                                                System.out.println("sales order synced Completed , total - " + i);
                                                updateSyncTable(Sales_order, time_, i + 1, i + 1, salesRepId, 1);

                                                allRowCounts = allRowCounts + i + 1;

                                                if (allsync == 1) {
                                                    getVehicleInventory(1, salesRepId);
                                                }
                                            }

                                        } else {
                                            System.out.println("sales order Table not sync");
                                            //updateSyncTable(Sales_order, time_,0 ,i, salesRepId, 0);
                                            //String table, String time, int rows,int rows_new, int salesrep, int status
                                        }

                                        //Line Items

                                        JSONArray LineItemArray = jsonObj_2.getJSONArray("_ListSalesOrderLineItem");

                                        for (int j = 0; j < LineItemArray.length(); j++) {
                                            JSONObject lineItems = LineItemArray.getJSONObject(j);
                                            int SalesOrderID = lineItems.getInt("SalesOrderID");
                                            int BatchID = lineItems.getInt("BatchID");
                                            int ProductID = lineItems.getInt("ProductID");
                                            int Quantity = lineItems.getInt("Quantity");
                                            int FreeQuantity = lineItems.getInt("FreeQuantity");
                                            double UnitSellingPrice = lineItems.getDouble("UnitSellingPrice");
                                            double UnitSellingDiscount = lineItems.getDouble("UnitSellingDiscount");
                                            double TotalAmountL = lineItems.getDouble("TotalAmount");
                                            double TotalDiscountL = lineItems.getDouble("TotalDiscount");
                                            int IsSyncc = 1;//lineItems.getInt("IsSync");
                                            String SyncDatec = lineItems.getString("SyncDate");

                                            System.out.println("Invoice Id 2 : " + InvoiceNumber + " SalesOrderID " + SalesOrderID);

                                            SalesOrderLineItem salesOrderLineItem = new SalesOrderLineItem(SalesOrderID, BatchID, ProductID,
                                                    Quantity, FreeQuantity, UnitSellingPrice, UnitSellingDiscount, TotalAmountL, TotalDiscountL, IsSyncc, SyncDatec);

                                            ContentValues cvLineItem = salesOrderLineItem.getLineItemContentValues();
                                            boolean successL = db.insertDataAll(cvLineItem, DbHelper.TABLE_LINE_ITEM);
                                            if (!successL) {
                                                System.out.println("Data is not inserted to " + DbHelper.TABLE_LINE_ITEM);
                                                if (i == LineItemArray.length() - 1) {

                                                    time_ = (new DateManager().getDateWithTime());
                                                    System.out.println("sales order line item synced Completed , total - " + i);
                                                    //updateSyncTable(string_table[11], time_, 0,i + 1, salesRepId, 1);
                                                }
                                            } else {
                                                System.out.println(DbHelper.TABLE_LINE_ITEM + " Table syncing...");
                                            }
                                        }
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(context, "Json Error: ", Toast.LENGTH_LONG).show();
                                    System.out.println("Json Error" + e.getMessage());
                                }
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Volley Error ", Toast.LENGTH_LONG).show();
                        System.out.println("SalesOrder Revise Error" + error.getMessage());
                    }
                });
        MySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public ArrayList<OrderList> getSalesOrderList() {
        ArrayList<OrderList> orderListArrayList = new ArrayList<>();
        Cursor cursor = db.getAllData(TABLE_SALES_ORDER);
        if (cursor.getCount() == 0) {
            System.out.println("No Data");
        } else {
            while (cursor.moveToNext()) {
                int InvoiceNumber = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_InvoiceNumber));
                String EstimateDeliveryDate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_EstimateDeliveryDate));
                double TotalDiscount = cursor.getDouble(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_TotalDiscount));
                double TotalAmount = cursor.getDouble(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_TotalAmount));
                String SyncDate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_SyncDate));
                int IsSync = cursor.getInt(cursor.getColumnIndex(COL_SALES_ORDER_IsSync));
                int SaleStatus = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_SaleStatus));
                String EnteredUser = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_EnteredUser));
                String EnteredDate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_EnteredDate));
                String UpdatedUser = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_UpdatedUser));
                String UpdatedDate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_UpdatedDate));
                String SaleDate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_SaleDate));
                int SaleTypeId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_SaleTypeId));
                int SalesRepId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_SalesRepId));
                int DitributorId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_DitributorId));
                int MerchantId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_MerchantId));
                int SalesOrderId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_SalesOrderId));
                double TotalVAT = cursor.getDouble(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_TotalVAT));
                int IsVat = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_IsVat));
                int IsReturn = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_IsReturn));
                String MerchntName = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_MerchntName));
                int IsCredit = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_CreditType));
                String CreditDays = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_CreditDate));

                OrderList orderList = new OrderList(InvoiceNumber, EstimateDeliveryDate, TotalDiscount, TotalAmount,
                        SyncDate, IsSync, SaleStatus, EnteredUser, EnteredDate, UpdatedUser, UpdatedDate, SaleDate,
                        SaleTypeId, SalesRepId, DitributorId, MerchantId, SalesOrderId, TotalVAT, IsVat, IsReturn, MerchntName, IsCredit, CreditDays);
                orderListArrayList.add(orderList);
            }
        }


        return orderListArrayList;
    }

    public ArrayList<OrderList> getSalesOrderListByMerchant(int merchantId) {
        System.out.println("Merchant ID " + merchantId);
        long today = DateManager.todayMillsecWithDateFormat("dd.MM.yyyy");
        ArrayList<OrderList> orderListArrayList = new ArrayList<>();
        Cursor cursor = null;
        SQLiteDatabase database = db.getReadableDatabase();
        String deliveryLocationQuery = "SELECT * FROM " + TABLE_SALES_ORDER + " WHERE " + DbHelper.COL_SALES_ORDER_MerchantId + " = " + merchantId;
        cursor = database.rawQuery(deliveryLocationQuery, null);
        if (cursor.getCount() == 0) {
            System.out.println("No Data of " + TABLE_SALES_ORDER);

        } else {
            while (cursor.moveToNext()) {
                int InvoiceNumber = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_InvoiceNumber));
                String EstimateDeliveryDate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_EstimateDeliveryDate));
                double TotalDiscount = cursor.getDouble(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_TotalDiscount));
                double TotalAmount = cursor.getDouble(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_TotalAmount));
                String SyncDate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_SyncDate));
                int IsSync = cursor.getInt(cursor.getColumnIndex(COL_SALES_ORDER_IsSync));
                int SaleStatus = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_SaleStatus));
                String EnteredUser = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_EnteredUser));
                String EnteredDate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_EnteredDate));

                System.out.println(" ====== Entered Date: " + EnteredDate);
                int start = EnteredDate.indexOf("(");
                int end = EnteredDate.indexOf(")");
                String creteDate = EnteredDate.substring(start + 1, end);
                long createDateMil = Long.parseLong(creteDate);

                System.out.println(" ====== Entered Date mil : " + createDateMil);
                String d = DateManager.getDateAccordingToMilliSeconds(createDateMil, "dd.MM.yyyy");
                System.out.println("+++++++++++++++ DAte d " + d);

                createDateMil = DateManager.getMilSecAccordingToDate(d);
                System.out.println("========= Converted MilSec " + createDateMil);
                System.out.println("+++++++++++++++ Date millll " + DateManager.getDateAccordingToMilliSeconds(createDateMil, "dd.MM.yyyy"));

                String UpdatedUser = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_UpdatedUser));
                String UpdatedDate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_UpdatedDate));
                String SaleDate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_SaleDate));
                int SaleTypeId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_SaleTypeId));
                int SalesRepId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_SalesRepId));
                int DitributorId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_DitributorId));
                int MerchantId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_MerchantId));
                int SalesOrderId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_SalesOrderId));
                double TotalVAT = cursor.getDouble(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_TotalVAT));
                int IsVat = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_IsVat));
                int IsReturn = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_IsReturn));
                int IsCredit = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_CreditType));
                String CreditDays = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_CreditDate));
                String MerchntName = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_MerchntName));

                if (SaleStatus == 1 && createDateMil == today) {
                    OrderList orderList = new OrderList(InvoiceNumber, EstimateDeliveryDate, TotalDiscount, TotalAmount,
                            SyncDate, IsSync, SaleStatus, EnteredUser, EnteredDate, UpdatedUser, UpdatedDate, SaleDate,
                            SaleTypeId, SalesRepId, DitributorId, MerchantId, SalesOrderId, TotalVAT, IsVat, IsReturn, MerchntName, IsCredit, CreditDays);
                    orderListArrayList.add(orderList);
                }
            }
        }


        return orderListArrayList;
    }

    public ArrayList<OrderList> getSalesOrderListByMerchantAndDate(int merchantId, long startDate, long endDate, int status) {

        ArrayList<OrderList> orderListArrayList = new ArrayList<>();
        Cursor cursor = null;
        SQLiteDatabase database = db.getReadableDatabase();

        // String deliveryLocationQuery = "SELECT  * FROM " + TABLE_SALES_ORDER + " WHERE " + DbHelper.COL_SALES_ORDER_MerchantId + " = " + merchantId ;
        String deliveryLocationQuery = "SELECT  * FROM " + TABLE_SALES_ORDER + " WHERE " + DbHelper.COL_SALES_ORDER_MerchantId + " = " + merchantId;
        cursor = database.rawQuery(deliveryLocationQuery, null);
        if (cursor.getCount() == 0) {
            System.out.println("No Data");

        } else {
            while (cursor.moveToNext()) {
                int InvoiceNumber = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_InvoiceNumber));
                String EstimateDeliveryDate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_EstimateDeliveryDate));
                double TotalDiscount = cursor.getDouble(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_TotalDiscount));
                double TotalAmount = cursor.getDouble(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_TotalAmount));
                String SyncDate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_SyncDate));
                int IsSync = cursor.getInt(cursor.getColumnIndex(COL_SALES_ORDER_IsSync));
                int SaleStatus = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_SaleStatus));
                System.out.println("Sale Status = " + SaleStatus);
                String EnteredUser = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_EnteredUser));
                String EnteredDate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_EnteredDate));
                System.out.println(" ====== Entered Date: " + EnteredDate);
                int start = EnteredDate.indexOf("(");
                int end = EnteredDate.indexOf(")");
                String creteDate = EnteredDate.substring(start + 1, end);
                long createDateMil = Long.parseLong(creteDate);

                System.out.println(" ====== Entered Date mil : " + createDateMil);
                String d = DateManager.getDateAccordingToMilliSeconds(createDateMil, "dd.MM.yyyy");
                System.out.println("+++++++++++++++ DAte d " + d);

                createDateMil = DateManager.getMilSecAccordingToDate(d);
                System.out.println("========= Converted MilSec " + createDateMil);
                System.out.println("+++++++++++++++ Date millll " + DateManager.getDateAccordingToMilliSeconds(createDateMil, "dd.MM.yyyy"));
                String UpdatedUser = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_UpdatedUser));
                String UpdatedDate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_UpdatedDate));
                String SaleDate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_SaleDate));
                int SaleTypeId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_SaleTypeId));
                int SalesRepId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_SalesRepId));
                int DitributorId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_DitributorId));
                int MerchantId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_MerchantId));
                int SalesOrderId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_SalesOrderId));
                double TotalVAT = cursor.getDouble(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_TotalVAT));
                int IsVat = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_IsVat));
                int IsReturn = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_IsReturn));
                String MerchntName = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_MerchntName));
                int IsCredit = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_CreditType));
                String CreditDays = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_CreditDate));

                if (createDateMil >= startDate && createDateMil <= endDate && SaleStatus == status) {
                    OrderList orderList = new OrderList(InvoiceNumber, EstimateDeliveryDate, TotalDiscount, TotalAmount,
                            SyncDate, IsSync, SaleStatus, EnteredUser, EnteredDate, UpdatedUser, UpdatedDate, SaleDate,
                            SaleTypeId, SalesRepId, DitributorId, MerchantId, SalesOrderId, TotalVAT, IsVat, IsReturn, MerchntName, IsCredit, CreditDays);
                    orderListArrayList.add(orderList);
                }


            }
        }

        return orderListArrayList;
    }

    public ArrayList<OrderList> getSalesOrderListBySalesStatus(int status) {


        System.out.println("Sales Sttus = " + status);

        ArrayList<OrderList> orderListArrayList = new ArrayList<>();
        Cursor cursor = null;
        //long today = DateManager.todayMillsecWithDateFormat("dd.MM.yyyy");
        SQLiteDatabase database = db.getReadableDatabase();
        //String deliveryLocationQuery = "SELECT * FROM " + DbHelper.TABLE_SALES_ORDER + " WHERE " + DbHelper.COL_SALES_ORDER_MerchantId + " = " + merchantId +" AND "+ DbHelper.COL_SALES_ORDER_SaleStatus + " = 1";
        String deliveryLocationQuery = "SELECT  * FROM " + TABLE_SALES_ORDER;
        cursor = database.rawQuery(deliveryLocationQuery, null);
        if (cursor.getCount() == 0) {
            System.out.println("No Data");

        } else {
            while (cursor.moveToNext()) {
                int InvoiceNumber = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_InvoiceNumber));
                String EstimateDeliveryDate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_EstimateDeliveryDate));
                double TotalDiscount = cursor.getDouble(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_TotalDiscount));
                double TotalAmount = cursor.getDouble(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_TotalAmount));
                String SyncDate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_SyncDate));
                int IsSync = cursor.getInt(cursor.getColumnIndex(COL_SALES_ORDER_IsSync));
                int SaleStatus = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_SaleStatus));
                System.out.println("Sale Status = " + SaleStatus);
                String EnteredUser = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_EnteredUser));
                String EnteredDate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_EnteredDate));
                System.out.println(" ====== Entered Date: " + EnteredDate);
                int start = EnteredDate.indexOf("(");
                int end = EnteredDate.indexOf(")");
                String creteDate = EnteredDate.substring(start + 1, end);
                long createDateMil = Long.parseLong(creteDate);

                System.out.println(" ====== Entered Date mil : " + createDateMil);
                String d = DateManager.getDateAccordingToMilliSeconds(createDateMil, "dd.MM.yyyy");
                System.out.println("+++++++++++++++ DAte d " + d);

                createDateMil = DateManager.getMilSecAccordingToDate(d);
                System.out.println("========= Converted MilSec " + createDateMil);
                System.out.println("+++++++++++++++ Date millll " + DateManager.getDateAccordingToMilliSeconds(createDateMil, "dd.MM.yyyy"));
                String UpdatedUser = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_UpdatedUser));
                String UpdatedDate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_UpdatedDate));
                String SaleDate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_SaleDate));
                int SaleTypeId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_SaleTypeId));
                int SalesRepId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_SalesRepId));
                int DitributorId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_DitributorId));
                int MerchantId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_MerchantId));
                int SalesOrderId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_SalesOrderId));
                double TotalVAT = cursor.getDouble(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_TotalVAT));
                int IsVat = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_IsVat));
                int IsReturn = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_IsReturn));
                int IsCredit = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_CreditType));
                String CreditDays = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_CreditDate));
                String MerchntName = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_ORDER_MerchntName));

                if (createDateMil == today && SaleStatus == status) {
                    OrderList orderList = new OrderList(InvoiceNumber, EstimateDeliveryDate, TotalDiscount, TotalAmount,
                            SyncDate, IsSync, SaleStatus, EnteredUser, EnteredDate, UpdatedUser, UpdatedDate, SaleDate,
                            SaleTypeId, SalesRepId, DitributorId, MerchantId, SalesOrderId, TotalVAT, IsVat, IsReturn, MerchntName, IsCredit, CreditDays);
                    orderListArrayList.add(orderList);
                }


            }
        }


        System.out.printf("========= array size in sales atatus order list %d%n", orderListArrayList.size());

        return orderListArrayList;
    }


    //get product list
    public void getDefProduct(final int allsync, final int Salesrep) {

        boolean success = db.deleteAllData(TABLE_PRODUCT);
        if (success) {
            System.out.println("Succesfuly delete product table data");
        } else {
            System.out.println("Error occur deleting product table");
        }

        String url = HTTPPaths.seriveUrl + "GetDefProductList?withcategoryObject=0";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonObject object = Json.parse(response).asObject();
                        int id = object.get("ID").asInt();
                        ContentValues cv = null;
                        if (id == 200) {

                            String newUrl = response.replace("{\"Data\":\"", "{\"Data\": ");
                            String new2Url = newUrl.replace("\",\"ID\":200}", "}");
                            String new3Url = new2Url.replace("\\", "");

                            if (response != null) {
                                try {
                                    JSONObject jsonObj_main = new JSONObject(new3Url);
//                                    JSONObject jObjResponse = new JSONObject(String.valueOf(response.getJSONObject()));
                                    JSONArray jsonAry_main = jsonObj_main.getJSONArray("Data");
                                    for (int i = 0; i < jsonAry_main.length(); i++) {

                                        JSONObject jsonObj_2 = jsonAry_main.getJSONObject(i);
                                        //Products
                                        int ProductId = jsonObj_2.getInt("ProductId");
                                        int PackingTypeId = jsonObj_2.getInt("PackingTypeId");
                                        int SellingCategoryId = jsonObj_2.getInt("SellingCategoryId");
                                        int CompanyId = jsonObj_2.getInt("CompanyId");
                                        int CategoryId = jsonObj_2.getInt("CategoryId");
                                        String ProductName = jsonObj_2.getString("ProductName");
                                        int IsActive = jsonObj_2.getInt("IsActive");
                                        String EnteredDate = jsonObj_2.getString("EnteredDate");
                                        String EnteredUser = jsonObj_2.getString("EnteredUser");
                                        double UnitSellingPrice = jsonObj_2.getDouble("UnitSellingPrice");
                                        double UnitDistributorPrice = jsonObj_2.getDouble("UnitDistributorPrice");
                                        String ReferenceID = jsonObj_2.getString("ReferenceID");
                                        int IsVat = jsonObj_2.getInt("IsVat");
                                        int VatRate = jsonObj_2.getInt("VatRate");
                                        int Sizeofunit = jsonObj_2.getInt("SizeOfUnit");
                                        int Addtocart = jsonObj_2.getInt("AddToDCard");

                                        System.out.println("~~~~~~~ Product Name: " + ProductName + ", isvat - " + IsVat);

                                        DefProduct defProduct = new DefProduct
                                                (ProductId, PackingTypeId, SellingCategoryId, CompanyId, CategoryId,
                                                        ProductName, IsActive, EnteredDate, EnteredUser, UnitSellingPrice,
                                                        UnitDistributorPrice, ReferenceID, IsVat, VatRate, Sizeofunit, Addtocart);
                                        cv = defProduct.getProducListContentValues();

                                        //System.out.println("cv product size - "+cv.size());

                                        boolean success = db.insertDataAll(cv, TABLE_PRODUCT);
                                        if (success) {
                                            //System.out.println("product syncing - " + i);

                                            if (i == jsonAry_main.length() - 1) {
                                                System.out.println("product synced Completed , total - " + i);
                                                Toast.makeText(context, "Product table synced successfully", Toast.LENGTH_SHORT).show();
                                                time_ = (new DateManager().getDateWithTime());
                                                updateSyncTable(Product, time_, i + 1, i + 1, Salesrep, 1);
                                                if (allsync == 1) {
                                                    getProductBatchList(1, Salesrep);
                                                }
                                                allRowCounts = allRowCounts + i + 1;
                                            }
                                        } else {
                                            System.out.println("Data is not inserted to product");
                                            updateSyncTable(Product, time_, 0, i, Salesrep, 0);
                                        }
                                    }
                                    System.out.println("product list size - " + cv.size());

                                } catch (JSONException e) {
                                    Toast.makeText(context, "Json Error: ", Toast.LENGTH_LONG).show();
                                    System.out.println("Product Sync Json Error: " + e.getMessage());
                                    System.out.println("Product Sync Json Error: ====================\n ");
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Volley Error ", Toast.LENGTH_LONG).show();
                        System.out.println("DefProduct " + error.getMessage());
                    }
                });
        MySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(stringRequest);
    }

    //get Batch
    public void getProductBatchList(final int allsync, final int Salesrep) {

        boolean success = db.deleteAllData(TABLE_PRODUCT_BATCH);
        if (success) {
            System.out.println("Successfully delete product batch table data");
        } else {
            System.out.println("Error occur deleting product batch table");
        }

        String url = HTTPPaths.seriveUrl + "GetBatchList";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonObject object = Json.parse(response).asObject();
                        int id = object.get("ID").asInt();

                        if (id == 200) {
                            String newUrl = response.replace("{\"Data\":\"", "{\"Data\": ");
                            String new2Url = newUrl.replace("\",\"ID\":200}", "}");
                            String new3Url = new2Url.replace("\\", "");

                            if (response != null) {
                                try {
                                    JSONObject jsonObj_main = new JSONObject(new3Url);
                                    JSONArray jsonAry_main = jsonObj_main.getJSONArray("Data");
                                    for (int i = 0; i < jsonAry_main.length(); i++) {

                                        JSONObject jsonObj_2 = jsonAry_main.getJSONObject(i);
                                        //Product batch
                                        int ProductId = jsonObj_2.getInt("ProductId");
                                        int BatchId = jsonObj_2.getInt("BatchId");
                                        int Quantity = jsonObj_2.getInt("Quantity");
                                        int BlockQuantity = jsonObj_2.getInt("BlockQuantity");
                                        double UnitDealrePrice = jsonObj_2.getDouble("UnitDealrePrice");
                                        double UnitSellingPrice = jsonObj_2.getDouble("UnitSellingPrice");
                                        int IsAllSold = jsonObj_2.getInt("IsAllSold");
                                        String ExpiryDate = jsonObj_2.getString("ExpiryDate");
                                        String EnteredUser = jsonObj_2.getString("EnteredUser");
                                        String EnteredDate = jsonObj_2.getString("EnteredDate");

                                        ProductBatch productBatch = new ProductBatch(ProductId, BatchId, Quantity, BlockQuantity,
                                                UnitDealrePrice, UnitSellingPrice, IsAllSold,
                                                ExpiryDate, EnteredUser, EnteredDate);



                                        ContentValues cv = productBatch.getProductBatchContentvalues();

                                        //System.out.println("cv productBatch size - "+cv.size());
                                        //System.out.println("product batch qty - " + Quantity);


                                        boolean success = db.insertDataAll(cv, TABLE_PRODUCT_BATCH);
                                        if (success) {
                                            System.out.println("Area syncing - " + i);

                                            if (i == jsonAry_main.length() - 1) {
                                                System.out.println("product batch synced Completed , total - " + i);
                                                Toast.makeText(context, "Product batch table synced successfully", Toast.LENGTH_SHORT).show();
                                                time_ = (new DateManager().getDateWithTime());
                                                updateSyncTable(Product_batch, time_, i + 1, i + 1, Salesrep, 1);
                                                if (allsync == 1) {
                                                    getReturnTypesFromServer(1, Salesrep);
                                                }
                                                allRowCounts = allRowCounts + i + 1;
                                            }
                                        } else {
                                            System.out.println("Data is not inserted to product batch");
                                            updateSyncTable(Product_batch, time_, 0, i, Salesrep, 0);
                                        }
                                    }

                                } catch (JSONException e) {
                                    System.out.println("Json Error getProductBatchList" + e.getMessage());
                                }
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("getProductBatchList " + error.getMessage());
                    }
                });
        MySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(stringRequest);

        //get promotion list
        (new SyncManager(context)).getPromotionDataFromServer();


    }


    //get Vehicle Inventory
    public void getVehicleInventory(final int allsync, final int SalesrepId) {

        boolean success = db.deleteAllData(TABLE_VEHICALE_INVENTORY);
        if (success) {
            System.out.println("Succesfuly delete vehicle inventory table data");
        } else {
            System.out.println("Error occur deleting vehicle inventory  table");
        }

        boolean s = db.deleteAllData(TABLE_PROMOTION_IMAGE);
        if (s) {
            System.out.println("Successfully  delete "+TABLE_PROMOTION_IMAGE+" table data");
        } else {
            System.out.println("Error occur deleting "+TABLE_PROMOTION_IMAGE+"  table");
        }


        String date = DateManager.getTodayDateString();
        System.out.println("===Date " + date);
        String url = HTTPPaths.seriveUrl + "GetVehicleInventoryListBySalesRepID?SalesRepId=" + SalesrepId + "&date=" + date;
        System.out.println("url vehicle - " + url);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonObject object = Json.parse(response).asObject();
                        int id = object.get("ID").asInt();

                        if (id == 200) {
                            String newUrl = response.replace("{\"Data\":\"", "{\"Data\": ");
                            String new2Url = newUrl.replace("\",\"ID\":200}", "}");
                            String new3Url = new2Url.replace("\\", "");

                            if (response != null) {

                                try {
                                    JSONObject jsonObj_main = new JSONObject(new3Url);
                                    JSONArray jsonAry_main = jsonObj_main.getJSONArray("Data");
                                    for (int i = 0; i < jsonAry_main.length(); i++) {

                                        JSONObject jsonObj_2 = jsonAry_main.getJSONObject(i);
                                        //Products
                                        int ProductId = jsonObj_2.getInt("ProductId");
                                        int BatchId = jsonObj_2.getInt("BatchID");
                                        int DistributorID = jsonObj_2.getInt("DistributorID");
                                        int SalesRepID = jsonObj_2.getInt("SalesRepID");
                                        int LoadQuantity = jsonObj_2.getInt("LoadQuantity");
                                        int OutstandingQuantity = jsonObj_2.getInt("OutstandingQuantity");
                                        String DiscountRule = jsonObj_2.getString("DiscountRule");
                                        String FreeIssueRule = jsonObj_2.getString("FreeIssueRule");
                                        double UnitSellingPrice = jsonObj_2.getDouble("UnitSellingPrice");
                                        String InventoryDate = jsonObj_2.getString("InventoryDate");

                                        VehicleInventry vehicleInventry = new VehicleInventry(ProductId, BatchId, DistributorID, SalesRepID,
                                                LoadQuantity, OutstandingQuantity, DiscountRule,
                                                FreeIssueRule, UnitSellingPrice, InventoryDate);

                                        ContentValues cv = vehicleInventry.getVehicleInventoryContentvalues();
                                        //System.out.println("cv vehicle inventory - " + cv.size());


                                        //load promotion images from server
                                        //getPromotionImageFromServer(ProductId,BatchId);

                                        boolean success = db.insertDataAll(cv, TABLE_VEHICALE_INVENTORY);
                                        if (success) {
                                            System.out.println("vehicle inventory syncing - " + i);

                                            if (i == jsonAry_main.length() - 1) {
                                                System.out.println("vehicle inventory synced Completed , total - " + i);
                                                time_ = (new DateManager().getDateWithTime());
                                                updateSyncTable(Vehicle_inventory, time_, i + 1, i + 1, SalesrepId, 1);
                                                //vi[0] = 1;
                                                //Toast.makeText(context, "Vehicle table synced successfully", Toast.LENGTH_SHORT).show();

                                                allRowCounts = allRowCounts + i + 1;
                                                if (allsync == 1) {
                                                    getSalesRepInventoryFromServer(1, SalesrepId);
                                                }
                                            }


                                        } else {
                                            System.out.println("Data is not inserted to vehicle inventory");
                                            //updateSyncTable(Vehicle_inventory, time_,0, i, SalesrepId, 0);
                                        }

                                    }

                                } catch (JSONException e) {
                                    System.out.println("Json Error getVehicleInventory " + e.getMessage());
                                }
                            }
                        } else if (id == 300) {//these are for presales salesrep,.
                            System.out.println("no data- response code-300");
                            updateSyncTable(Vehicle_inventory, time_, 0, 0, SalesrepId, 1);
                            if (allsync == 1) {
                                getSalesRepInventoryFromServer(1, SalesrepId);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error getVehicleInventory " + error.getMessage());
                    }
                });
        MySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(stringRequest);
    }

    //brand Name loading method
    public void getCategoryFromServer(final int allsync, final int salesrep) {

        boolean successDelete_category = db.deleteAllData(TABLE_CATEGORY);
        if (successDelete_category) {
            System.out.println("Successfully delete category table data");
        } else {
            System.out.println("Error occur deleting category table");
        }

        String url = HTTPPaths.seriveUrl + "GetProductCategoryList";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonObject jsonObject = Json.parse(response).asObject();
                        int id = jsonObject.get("ID").asInt();
                        //Toast.makeText(CreateSalesOrderContinue.this, "ID - " + id, Toast.LENGTH_SHORT).show();
                        if (id == 200) {
                            String url2 = response.replace("{\"Data\":\"", "{\"Data\": ");
                            String url3 = url2.replace("\",\"ID\":200}", "}");
                            String url4 = url3.replace("\\", "");

                            if (response != null) {
                                try {
                                    JSONObject jsonObject_main = new JSONObject(url4);

                                    JSONArray jsonArray_main = jsonObject_main.getJSONArray("Data");

                                    for (int i = 0; i < jsonArray_main.length(); i++) {

                                        JSONObject jsonObject1 = jsonArray_main.getJSONObject(i);

                                        int CategoryId_ = jsonObject1.getInt("CategoryId");
                                        String CategoryName_ = jsonObject1.getString("CategoryName");
                                        String CategorySName_ = jsonObject1.getString("CategoryShortName");
                                        int CategoryIsactive = jsonObject1.getInt("IsActive");
                                        String CategoryEnteredUser_ = jsonObject1.getString("EnteredDate");
                                        String CategoryEnteredDate = jsonObject1.getString("EnteredUser");

                                        CategoryDetails categoryDetails = new CategoryDetails(CategoryId_, CategoryName_,
                                                CategorySName_, CategoryIsactive, CategoryEnteredUser_, CategoryEnteredDate);

                                        ContentValues contentValues = categoryDetails.getCategoryContents();
                                        //System.out.println("cv category size  - " + contentValues.size());
                                        boolean successInsert_category = db.insertDataAll(contentValues, TABLE_CATEGORY);

                                        if (successInsert_category) {
                                            System.out.println("category syncing - " + i);

                                            if (i == jsonArray_main.length() - 1) {
                                                System.out.println("category synced Completed , total - " + i);
                                                Toast.makeText(context, "Category table synced successfully", Toast.LENGTH_SHORT).show();
                                                time_ = (new DateManager().getDateWithTime());
                                                updateSyncTable(Category, time_, i + 1, i + 1, salesrep, 1);
                                                if (allsync == 1) {
                                                    getDefProduct(1, salesrep);
                                                }
                                                allRowCounts = allRowCounts + i + 1;
                                            }
                                        } else {
                                            System.out.println("Not insert to " + TABLE_CATEGORY);
                                            updateSyncTable(Category, time_, 0, i, salesrep, 0);
                                        }

                                    }
                                } catch (Exception e) {
                                    System.out.println("error - " + e.toString());
                                }
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(CreateSalesOrderContinue.this, "Volly Error - " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
        /* showBrandList(brandLists); */
    }

    public ArrayList<CategoryDetails> getCategoryDetailsFromSQLite(String sql) {
        Cursor cursor = null;
        ArrayList<CategoryDetails> categoryDetailses = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
        cursor = sqLiteDatabase.rawQuery(sql, null);

        if (cursor.getCount() == 0) {
            System.out.println("no data");
        } else {
            while (cursor.moveToNext()) {

                int Categoryid = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_CATEGORY_CATEGORY_ID));
                String CategoryName = cursor.getString(cursor.getColumnIndex(DbHelper.COL_CATEGORY_CATEGORY_NAME));
                String CategoryShortName = cursor.getString(cursor.getColumnIndex(DbHelper.COL_CATEGORY_CATEGORY_SNAME));
                int isactive = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_CATEGORY_CATEGORY_ISACTIVE));
                String enterddate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_CATEGORY_CATEGORY_ENTERED_DATE));
                String entereduser = cursor.getString(cursor.getColumnIndex(DbHelper.COL_CATEGORY_CATEGORY_ENTERED_USER));

                CategoryDetails categoryDetails = new CategoryDetails(Categoryid, CategoryName, CategoryShortName,
                        isactive, enterddate, entereduser);
                categoryDetailses.add(categoryDetails);
                System.out.println("categoryDetailses size in sync manager - " + categoryDetailses.size());
            }
        }
        return categoryDetailses;
    }

    public ArrayList<ProductList> getProductListFromSQLite2(String sql) {
        Cursor cursor = null;
        ArrayList<ProductList> productLists = new ArrayList<>();
        SQLiteDatabase database = db.getReadableDatabase();
        cursor = database.rawQuery(sql, null);
        if (cursor.getCount() == 0) {

        } else {
            while (cursor.moveToNext()) {

                int ProductId = cursor.getInt(0);
                int BatchID = cursor.getInt(1);
                double UnitSellingPrice = cursor.getDouble(2);
                int Quantity = cursor.getInt(3);
                double amount = cursor.getInt(4);
                int FreeQuantity = cursor.getInt(5);
                String ProductName = cursor.getString(6);
                int IsVat = cursor.getInt(7);
                //double VatRate =  cursor.getDouble(8);
                //int CategoryId =  cursor.getInt(9);
                int LoadQuantity = cursor.getInt(10);
                String ExpiryDate = cursor.getString(11);

                ProductList productList = new ProductList(ProductName, ExpiryDate, LoadQuantity,
                        UnitSellingPrice, amount, Quantity, FreeQuantity, 0, BatchID, ProductId, IsVat, 0.00, 0);
                productLists.add(productList);
            }
        }
        return productLists;
    }


    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //when usertype == S     //checked      ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public void getSalesRep_and_DistributorDetailsFromServer_accordingto_salesrep(final int allsync, final int userTypeId) {
        System.out.println("==================== Get Sales Rep Data method ====================================");

        boolean successDelete = db.deleteAllData(DbHelper.TABLE_SALES_REP);
        boolean successDelete2 = db.deleteAllData(DbHelper.TABLE_DISTRIBUTOR);

        if (successDelete && successDelete2) {
            System.out.println(DbHelper.TABLE_SALES_REP + " and " + DbHelper.TABLE_DISTRIBUTOR + " successfully deleted from beginning");
        } else {
            System.out.println("Tables not deleted ");
        }

        String dis_url = HTTPPaths.seriveUrl + "GetDefSalesRep?salesRepId=" + userTypeId;

        System.out.println("SalesRep URL: " + dis_url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, dis_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonObject jsonObject = Json.parse(response).asObject();
                        int id = jsonObject.get("ID").asInt();

                        if (id == 200) {
                            String url2 = response.replace("{\"Data\":\"", "{\"Data\": ");
                            String url3 = url2.replace("\",\"ID\":200}", "}");
                            String url4 = url3.replace("\\", "");

                            if (response != null) {

                                System.out.println("==================== Get Sales Rep Data respond====================================");
                                try {
                                    //data for salesrep table
                                    JSONObject jsonObject_main = new JSONObject(url4);
                                    JSONObject jsonoBJmain = jsonObject_main.getJSONObject("Data");
                                    int SalesRepId = jsonoBJmain.getInt("SalesRepId");
                                    String SalesRepName = jsonoBJmain.getString("SalesRepName");
                                    int EpfNumber = jsonoBJmain.getInt("EpfNumber");
                                    int IsActive = jsonoBJmain.getInt("IsActive");
                                    String EnteredUser = jsonoBJmain.getString("EnteredUser");
                                    String EnteredDate = jsonoBJmain.getString("EnteredDate");
                                    //String PresentAddress = jsonoBJmain.getString("PresentAddress");
                                    //String PermanentAddress = jsonoBJmain.getString("PermanentAddress");
                                    //String ContactNo = jsonoBJmain.getString("ContactNo");
                                    String UpdatedUserSalesrep = jsonoBJmain.getString("UpdatedUser");
                                    String UpdatedDateSalesrep = jsonoBJmain.getString("UpdatedDate");
                                    String ReferenceID = jsonoBJmain.getString("ReferenceID");
                                    int NextCreditNoteNo = jsonoBJmain.getInt("NextCreditNoteNo");
                                    int NextSalesOrderNo = jsonoBJmain.getInt("NextSalesOrderNo");
                                    int NextInvoiceNo = jsonoBJmain.getInt("NextInvoiceNo");
                                    String ContactNo = jsonoBJmain.getString("ContactNo");
                                    int Distributord = jsonoBJmain.getInt("Distributord");
                                    String salesRepType = jsonoBJmain.getString("SalesRepType");
                                    //SharedPreferences
                                    SharedPreferences pref = context.getSharedPreferences("invoice.conf", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = pref.edit();

                                    editor.putInt("NextSalesOrderNo", NextSalesOrderNo);
                                    editor.putInt("NextInvoiceNo", NextInvoiceNo);
                                    editor.putInt("NextCreditNoteNo", NextCreditNoteNo);
                                    editor.putString("SalesRepType", salesRepType);
                                    editor.apply();

                                    System.out.println("Next Invoice Number " + NextInvoiceNo);


                                    System.out.println("salesRepType - (from server ) " + salesRepType);
                                    //without reference id ,NextSalesOrderNo
                                    SalesRep salesRep = new SalesRep(SalesRepId, Distributord, SalesRepName, EpfNumber,
                                            IsActive, EnteredUser, EnteredDate, UpdatedUserSalesrep, UpdatedDateSalesrep,
                                            ReferenceID, NextCreditNoteNo, NextSalesOrderNo, NextInvoiceNo, salesRepType, ContactNo);

                                    //use 2nd method
                                    ContentValues cv = salesRep.getSalesRepContentValues();
                                    //System.out.println("cv.get(\"SalesRepType\").toString() = "+cv.get("SalesRepType").toString());

                                    boolean success = db.insertDataAll(cv, DbHelper.TABLE_SALES_REP);
                                    if (success) {
                                        System.out.println("salesrep synced Completed ");
                                        Toast.makeText(context, "Salesrep table synced successfully", Toast.LENGTH_SHORT).show();
                                        time_ = (new DateManager().getDateWithTime());
                                        updateSyncTable(Salesrep, time_, 1, 1, userTypeId, 1);

                                        allRowCounts = allRowCounts + 1;

                                    } else {
                                        System.out.println("Data is not inserted to salesrep");
                                    }
                                    //System.out.println("SALESrep cv2 size - " + cv.size());


                                    //data for distributor table
                                    JSONObject jsonObject1 = jsonoBJmain.getJSONObject("_DefDistributor");
                                    //int RegionId = jsonObject1.getInt("RegionId");
                                    //int AreaCode = jsonObject1.getInt("AreaCode");
                                    //int TeritoryId = jsonObject1.getInt("TeritoryId");
                                    String DistributorName = jsonObject1.getString("DistributorName");
                                    String ContactNumber = jsonObject1.getString("ContactNumber");
                                    String Address1 = jsonObject1.getString("Address1");
                                    String Address2 = jsonObject1.getString("Address2");
                                    String Address3 = jsonObject1.getString("Address3");
                                    String Address4 = jsonObject1.getString("Address4");
                                    String Email = jsonObject1.getString("Email");
                                    //int PartnerId = jsonObject1.getInt("PartnerId");
                                    int IsSync = jsonObject1.getInt("IsSync");
                                    String SyncDate = jsonObject1.getString("SyncDate");
                                    String ReferenceId = jsonObject1.getString("ReferenceId");
                                    int IsVat = jsonObject1.getInt("IsVat");
                                    String VatNo = jsonObject1.getString("VatNo");
                                    String UpdatedUserDistributor = jsonObject1.getString("UpdatedUser");
                                    String UpdatedDateDistributor = jsonObject1.getString("UpdatedDate");
                                    String EnteredUserDistributor = jsonObject1.getString("EnteredUser");
                                    String EnteredDateDistributor = jsonObject1.getString("EnteredDate");
                                    String DistributorType = jsonObject1.getString("DistributorType");

                                    DistributorDetails distributorDetails = new DistributorDetails
                                            (Distributord, DistributorName, ContactNumber, Address1, Address2, Address3, Address4, Email, IsSync, SyncDate, ReferenceId, IsVat, VatNo,
                                                    UpdatedUserDistributor, UpdatedDateDistributor, EnteredUserDistributor, EnteredDateDistributor, DistributorType);


                                    ContentValues cv2 = distributorDetails.getDistributorContentValues2();
                                    System.out.println("Distributor cv size - " + cv2.size());


                                    boolean successDis = db.insertDataAll(cv2, DbHelper.TABLE_DISTRIBUTOR);
                                    if (successDis) {
                                        System.out.println("distributor synced Completed");
                                        Toast.makeText(context, "Distributor table synced successfully", Toast.LENGTH_SHORT).show();
                                        time_ = (new DateManager().getDateWithTime());
                                        updateSyncTable(Distributor, time_, 1, 1, userTypeId, 1);

                                        if (allsync == 1) {
                                            //getMerchantListFromServer(1,0,userTypeId);
                                            syncMerchantTableIntoSqlite(1, userTypeId);
                                        }
                                        allRowCounts = allRowCounts + 1;
                                    } else {
                                        System.out.println("Data is not inserted to distributor");

                                    }

                                } catch (Exception e) {
                                    System.out.println("error - " + e.toString());

                                }
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error --findDistributorIsVat-- " + error.toString(), Toast.LENGTH_SHORT).show();


                    }
                });

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }


    //when usertype == D    // not checked ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    public void getSalesRep_and_DistributorDetailsFromServer_accordingto_distributor(int userTypeId) {
        getSalesRepDetailsFromServer_accordingto_distributor(userTypeId);
        getDistributorDetailsFromServer_accordingto_distributor(userTypeId);
    }

    //get salesreps details
    private void getSalesRepDetailsFromServer_accordingto_distributor(int disId) {
        String url = HTTPPaths.seriveUrl + "GetDefSalesRepByDistributorId?distributorId=" + disId;
        System.out.println("dis url - " + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonObject object = Json.parse(response).asObject();
                        int id = object.get("ID").asInt();
                        if (id == 200) {
                            String newUrl = response.replace("{\"Data\":\"", "{\"Data\": ");
                            String new2Url = newUrl.replace("\",\"ID\":200}", "}");
                            String new3Url = new2Url.replace("\\", "");

                            if (response != null) {
                                try {
                                    JSONObject jsonObj_main = new JSONObject(new3Url);
                                    JSONArray jsonAry_main = jsonObj_main.getJSONArray("Data");
                                    for (int i = 0; i < jsonAry_main.length(); i++) {
                                        JSONObject jsonObj_2 = jsonAry_main.getJSONObject(i);
                                        // get SalesREp Details
                                        int SalesRepId = jsonObj_2.getInt("SalesRepId");
                                        int DistributorId = jsonObj_2.getInt("DistributorId");
                                        String SalesRepName = jsonObj_2.getString("SalesRepName");
                                        int EpfNumber = jsonObj_2.getInt("EpfNumber");
                                        int IsActive = jsonObj_2.getInt("IsActive");
                                        String EnteredUser = jsonObj_2.getString("EnteredUser");
                                        String EnteredDate = jsonObj_2.getString("EnteredDate");
//                                        String PresentAddress = jsonObj_2.getString("PresentAddress");
//                                        String PermanentAddress = jsonObj_2.getString("PermanentAddress");
//                                        String ContactNo = jsonObj_2.getString("ContactNo");
                                        String UpdatedUser = jsonObj_2.getString("UpdatedUser");
                                        String UpdatedDate = jsonObj_2.getString("UpdatedDate");
                                        String ReferenceID = jsonObj_2.getString("ReferenceID");
                                        int NextCreditNoteNo = jsonObj_2.getInt("NextCreditNoteNo");
                                        int NextSalesOrderNo = jsonObj_2.getInt("NextSalesOrderNo");
                                        int NextInvoiceNo = jsonObj_2.getInt("NextInvoiceNo");

                                        //SharedPreferences
                                        SharedPreferences pref = context.getSharedPreferences("invoice.conf", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = pref.edit();

                                        editor.putInt("NextSalesOrderNo", NextSalesOrderNo);
                                        editor.putInt("NextInvoiceNo", NextInvoiceNo);
                                        editor.putInt("NextCreditNoteNo", NextCreditNoteNo);
                                        editor.apply();
                                        String SalesRepType = jsonObj_2.getString("SalesRepType");
                                        String ContactNo = jsonObj_2.getString("ContactNo");

                                        System.out.println("Next Invoice Number " + NextInvoiceNo);

                                        SalesRep salesRep = new SalesRep(SalesRepId, DistributorId, SalesRepName, EpfNumber, IsActive, EnteredUser,
                                                EnteredDate, UpdatedUser, UpdatedDate, ReferenceID, NextCreditNoteNo, NextSalesOrderNo, NextInvoiceNo, SalesRepType, ContactNo);
                                        //use 1st method
                                        ContentValues cv = salesRep.getSalesRepContentValues();

                                        System.out.println("SALESrep cv size - " + cv.size());
                                        boolean success = db.insertDataAll(cv, DbHelper.TABLE_SALES_REP);
                                        if (!success) {
                                            System.out.println(" Data is not inserted to " + DbHelper.TABLE_SALES_REP);
                                        } else {
                                            System.out.println(DbHelper.TABLE_SALES_REP + " Table syncing...");
                                        }
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(context, "Json Error: ", Toast.LENGTH_LONG).show();
                                    System.out.println("Json Error" + e.getMessage());
                                }
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Volley Error ", Toast.LENGTH_LONG).show();
                        System.out.println("Area Error" + error.getMessage());
                    }
                });
        MySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(stringRequest);

    }

    //get distributor details
    private void getDistributorDetailsFromServer_accordingto_distributor(int disId) {

        String url = HTTPPaths.seriveUrl + "GetDEFDistributorByDistributorId?distributorId=" + disId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonObject object = Json.parse(response).asObject();
                        int id = object.get("ID").asInt();
                        System.out.println("Enter res");
                        if (id == 200) {
                            String newUrl = response.replace("{\"Data\":\"", "{\"Data\": ");
                            String new2Url = newUrl.replace("\",\"ID\":200}", "}");
                            String new3Url = new2Url.replace("\\", "");

                            if (response != null) {
                                try {
                                    JSONObject jsonObj_main = new JSONObject(new3Url);
                                    JSONArray jsonAry_main = jsonObj_main.getJSONArray("Data");

                                    for (int i = 0; i < jsonAry_main.length(); i++) {
                                        JSONObject jsonObject1 = jsonAry_main.getJSONObject(i);

                                        //data for distributor table
                                        int Distributord = jsonObject1.getInt("Distributord");
                                        int RegionId = jsonObject1.getInt("RegionId");
                                        int AreaCode = jsonObject1.getInt("AreaCode");
                                        int TeritoryId = jsonObject1.getInt("TeritoryId");
                                        String DistributorName = jsonObject1.getString("DistributorName");
                                        String ContactNumber = jsonObject1.getString("ContactNumber");
                                        String Address1 = jsonObject1.getString("Address1");
                                        String Address2 = jsonObject1.getString("Address2");
                                        String Address3 = jsonObject1.getString("Address3");
                                        String Address4 = jsonObject1.getString("Address4");
                                        String Email = jsonObject1.getString("Email");
//                                        int PartnerId = jsonObject1.getInt("PartnerId");
                                        int IsSync = jsonObject1.getInt("IsSync");
                                        String SyncDate = jsonObject1.getString("SyncDate");
                                        String ReferenceId = jsonObject1.getString("ReferenceId");
                                        int IsVat = jsonObject1.getInt("IsVat");
                                        String VatNo = jsonObject1.getString("VatNo");
                                        String UpdatedUserDistributor = jsonObject1.getString("UpdatedUser");
                                        String UpdatedDateDistributor = jsonObject1.getString("UpdatedDate");
                                        String EnteredUserDistributor = jsonObject1.getString("EnteredUser");
                                        String EnteredDateDistributor = jsonObject1.getString("EnteredDate");
                                        String DistributorType = jsonObject1.getString("DistributorType");

//                                        SalesRep salesRep = new SalesRep(SalesRepId,DistributorId,SalesRepName,EpfNumber,IsActive,EnteredUser,
//                                                EnteredDate,PresentAddress,PermanentAddress,ContactNo,UpdatedUser,UpdatedDate,ReferenceID,NextSalesOrderNo,NextInvoiceNo,SalesRepType);
                                        DistributorDetails distributorDetails = new DistributorDetails
                                                (Distributord, DistributorName, ContactNumber, Address1, Address2, Address3, Address4, Email, IsSync, SyncDate, ReferenceId, IsVat, VatNo,
                                                        UpdatedUserDistributor, UpdatedDateDistributor, EnteredUserDistributor, EnteredDateDistributor, DistributorType);


                                        ContentValues cv = distributorDetails.getDistributorContentValues();
                                        System.out.println("DIStri cv size - " + cv.size());
                                        boolean success = db.insertDataAll(cv, DbHelper.TABLE_DISTRIBUTOR);
                                        if (!success) {
                                            System.out.println("Data is not inserted to " + DbHelper.TABLE_SALES_REP);
                                        } else {
                                            System.out.println(DbHelper.TABLE_SALES_REP + " Table syncing...");
                                        }
                                    }

                                } catch (JSONException e) {
                                    Toast.makeText(context, "Json Error: ", Toast.LENGTH_LONG).show();
                                    System.out.println("Json Error" + e.getMessage());
                                }
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Volley Error ", Toast.LENGTH_LONG).show();
                        System.out.println("Area Error" + error.getMessage());
                    }
                });
        MySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(stringRequest);

    }

    /*
    if salesrep login
    salesrep table got 1 row
    distributor table got 1 row

    distributor login
    salesrep table got many rows
    distributor table got 1 row
     */

    //distributor and salesrep
    public ArrayList<DistributorDetails> getDistributorDetailsFromSQLite(String query_sql, int userTypeId) {

        ArrayList<DistributorDetails> ds = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
        //String query = "SELECT * FROM "+DbHelper.TABLE_DISTRIBUTOR;

        Cursor cursor = sqLiteDatabase.rawQuery(query_sql, null);

        if (cursor.getCount() == 0) {
            //getDistributorDetailsFromServer(salesrepid);
            getSalesRep_and_DistributorDetailsFromServer_accordingto_distributor(userTypeId);

        } else {
            while (cursor.moveToNext()) {

                int DistributorId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_DISTRIBUTOR_DISTRIBUTOR_ID));
//                int RegionId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_DISTRIBUTOR_REGION_ID));
//                int AreaCode = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_DISTRIBUTOR_AREA_CODE));
//                int TeritoryId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_DISTRIBUTOR_TERITORY_ID));
                String DistributorName = cursor.getString(cursor.getColumnIndex(DbHelper.COL_DISTRIBUTOR_DISTRIBUTOR_NAME));
//                String ContactNumber = cursor.getString(cursor.getColumnIndex(DbHelper.COL_DISTRIBUTOR_CONTACT_NUMBER));
//                String Address1 = cursor.getString(cursor.getColumnIndex(DbHelper.COL_DISTRIBUTOR_ADDRESS_1));
//                String Address2 = cursor.getString(cursor.getColumnIndex(DbHelper.COL_DISTRIBUTOR_ADDRESS_2));
//                String Address3 = cursor.getString(cursor.getColumnIndex(DbHelper.COL_DISTRIBUTOR_ADDRESS_3));
//                String Address4 = cursor.getString(cursor.getColumnIndex(DbHelper.COL_DISTRIBUTOR_ADDRESS_4));
//                String Email = cursor.getString(cursor.getColumnIndex(DbHelper.COL_DISTRIBUTOR_EMAIL));
//                int PartnerId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_DISTRIBUTOR_PARTNER_ID));
                int IsSync = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_DISTRIBUTOR_IS_SYNC));
                String SyncDate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_DISTRIBUTOR_SYNC_DATE));
                String ReferenceId = cursor.getString(cursor.getColumnIndex(DbHelper.COL_DISTRIBUTOR_REFERENCE_ID));
                int IsVat = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_DISTRIBUTOR_IS_VAT));
                String VatNo = cursor.getString(cursor.getColumnIndex(DbHelper.COL_DISTRIBUTOR_VAT_NO));
                String UpdatedUser = cursor.getString(cursor.getColumnIndex(DbHelper.COL_DISTRIBUTOR_UPDATED_USER));
                String UpdatedDate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_DISTRIBUTOR_UPDATED_DATE));
                String EnteredUser = cursor.getString(cursor.getColumnIndex(DbHelper.COL_DISTRIBUTOR_ENTERED_USER));
                String EnteredDate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_DISTRIBUTOR_ENTERED_DATE));
                String DistributorType = cursor.getString(cursor.getColumnIndex(DbHelper.COL_DISTRIBUTOR_DISTRIBUTOR_TYPE));

                DistributorDetails distributorDetails = new DistributorDetails(DistributorId, DistributorName, IsSync, SyncDate, ReferenceId,
                        IsVat, VatNo, UpdatedUser, UpdatedDate, EnteredUser, EnteredDate, DistributorType);

                ds.add(distributorDetails);
            }
        }
        return ds;
    }

    public int getDitstributerId() {
        Cursor cursor = null;
        cursor = db.getAllData(TABLE_DISTRIBUTOR);
        int DistriButerId = 0;
        if (cursor.getCount() == 0) {
            System.out.println("No Data of " + TABLE_DISTRIBUTOR);
        } else {
            while (cursor.moveToNext()) {
                DistriButerId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_DISTRIBUTOR_DISTRIBUTOR_ID));
            }
        }
        return DistriButerId;
    }

    public ArrayList<SalesRep> getSalesRepDetailsFromSQLite(String query_sql) {
        Cursor cursor = null;
        ArrayList<SalesRep> salesRepArrayList = new ArrayList<>();
        //cursor = db.getAllData(DbHelper.TABLE_SALES_REP);

        SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
        cursor = sqLiteDatabase.rawQuery(query_sql, null);
        if (cursor.getCount() == 0) {
            System.out.println("No Data");
        } else {
            while (cursor.moveToNext()) {
                int SalesRepId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_REP_SalesRepId));
                int DistributorId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_REP_DistributorId));
                String SalesRepName = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_REP_SalesRepName));
                int EpfNumber = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_REP_EpfNumber));
                int IsActive = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_REP_IsActive));
                String EnteredUser = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_REP_EnteredUser));
                String EnteredDate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_REP_EnteredDate));
//                String PresentAddress = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_REP_PresentAddress));
//                String PermanentAddress = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_REP_PermanentAddress));
//                String ContactNo = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_REP_ContactNo));
                String UpdatedUser = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_REP_UpdatedUser));
                String UpdatedDate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_REP_UpdatedDate));
                String ReferenceID = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_REP_ReferenceID));
                int NextCreditNoteNo = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_REP_NextCreditNoteNo));
                int NextSalesOrderNo = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_REP_NextSalesOrderNo));
                int NextInvoiceNo = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_REP_NextInvoiceNo));
                String SalesRepType = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_REP_SalesRepType));
                String ContactNo = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_REP_ContactNo));

                SalesRep salesRep = new SalesRep(SalesRepId, DistributorId, SalesRepName, EpfNumber, IsActive, EnteredUser,
                        EnteredDate, UpdatedUser, UpdatedDate, ReferenceID, NextCreditNoteNo, NextSalesOrderNo, NextInvoiceNo, SalesRepType, ContactNo);
                salesRepArrayList.add(salesRep);
            }
        }

        return salesRepArrayList;
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    //revise sales order
    public void genarateReviseOrder() {


        SQLiteDatabase database = db.getWritableDatabase();
        int deletedRows = database.delete(TABLE_REVISE_ORDER, null, null);
        System.out.println(deletedRows + " row(s) deleted.....");

        Cursor cursorProductBatch = null;
        Cursor cursorProduct = null;
        Cursor cursorVehicleInventory = null;

        cursorProductBatch = db.getAllData(TABLE_PRODUCT_BATCH);

        if (cursorProductBatch.getCount() == 0) {
            System.out.println("No Data of " + TABLE_PRODUCT_BATCH);
        } else {
            db.deleteAllData(DbHelper.TABLE_REVISE_ORDER);

            while (cursorProductBatch.moveToNext()) {

                int ProductId = cursorProductBatch.getInt(cursorProductBatch.getColumnIndex(COL_PRODUCT_BATCH_ProductId));
                int BatchID = cursorProductBatch.getInt(cursorProductBatch.getColumnIndex(COL_PRODUCT_BATCH_BatchID));
                double UnitSellingPrice = cursorProductBatch.getDouble(cursorProductBatch.getColumnIndex(DbHelper.COL_PRODUCT_BATCH_UnitSellingPrice));
                String ExpiryDate = cursorProductBatch.getString(cursorProductBatch.getColumnIndex(COL_PRODUCT_BATCH_ExpiryDate));

                ContentValues cv = new ContentValues();
                cv.put("ProductId", ProductId);
                cv.put("BatchID", BatchID);
                cv.put("UnitSellingPrice", UnitSellingPrice);
                cv.put("ExpiryDate", ExpiryDate);

                boolean success = db.insertDataAll(cv, DbHelper.TABLE_REVISE_ORDER);

                if (success) {
                    System.out.println("Data is inserted to " + DbHelper.TABLE_REVISE_ORDER);
                } else {
                    System.out.println("Data is not inserted to " + DbHelper.TABLE_REVISE_ORDER);
                }

            }

        }

        //update Product details
        cursorProduct = db.getAllData(TABLE_PRODUCT);
        if (cursorProduct.getCount() == 0) {
            System.out.println("No Data of " + TABLE_PRODUCT);
        } else {
            while (cursorProduct.moveToNext()) {
                int ProductId = cursorProduct.getInt(cursorProduct.getColumnIndex(COL_PRODUCT_ProductId));
                int CategoryId = cursorProduct.getInt(cursorProduct.getColumnIndex(COL_PRODUCT_CategoryId));
                String ProductName = cursorProduct.getString(cursorProduct.getColumnIndex(COL_PRODUCT_ProductName));
                int IsVat = cursorProduct.getInt(cursorProduct.getColumnIndex(COL_PRODUCT_IsVat));
                double vatRate = cursorProduct.getDouble(cursorProduct.getColumnIndex(COL_PRODUCT_VatRate));

                ContentValues cv = new ContentValues();
                cv.put("ProductId", ProductId);
                cv.put("CategoryId", CategoryId);
                cv.put("ProductName", ProductName);
                cv.put("IsVat", IsVat);
                cv.put("VatRate", vatRate);

                boolean success = db.updateTable(ProductId + "", cv, DbHelper.COL_REVISE_SALES_ORDER_ProductId + " =?", DbHelper.TABLE_REVISE_ORDER);
                if (success) {
                    System.out.println("Data is updated " + DbHelper.TABLE_REVISE_ORDER);
                } else {
                    System.out.println("Data is not updated " + DbHelper.TABLE_REVISE_ORDER);
                }
            }
        }

        //update Vehicle Stock

        //get SalesRep Type
        String SalesRepType = null;
        Cursor cursorSalesRep = db.getAllData(DbHelper.TABLE_SALES_REP);
        if (cursorSalesRep.getCount() == 0) {
            System.out.println("No Data of " + DbHelper.TABLE_SALES_REP);
        } else {
            while (cursorSalesRep.moveToNext()) {
                SalesRepType = cursorSalesRep.getString(cursorSalesRep.getColumnIndex(DbHelper.COL_SALES_REP_SalesRepType));
            }
        }

        if (SalesRepType.equalsIgnoreCase("R")) {
            cursorVehicleInventory = db.getAllData(DbHelper.TABLE_VEHICALE_INVENTORY);
            if (cursorVehicleInventory.getCount() == 0) {
                System.out.println("No Data of " + DbHelper.TABLE_VEHICALE_INVENTORY);
            } else {
                while (cursorVehicleInventory.moveToNext()) {
                    int ProductId = cursorVehicleInventory.getInt(cursorVehicleInventory.getColumnIndex(COL_VEHICLE_INVENTORY_ProductId));
                    int BatchId = cursorVehicleInventory.getInt(cursorVehicleInventory.getColumnIndex(DbHelper.COL_VEHICLE_INVENTORY_BatchID));
                    int LoadQuantity = cursorVehicleInventory.getInt(cursorVehicleInventory.getColumnIndex(COL_VEHICLE_INVENTORY_LoadQuantity));

                    ContentValues cv = new ContentValues();
                    cv.put(DbHelper.COL_REVISE_STOCK_R, LoadQuantity);
                    database.update(DbHelper.TABLE_REVISE_ORDER, cv, DbHelper.COL_REVISE_SALES_ORDER_ProductId + " = ? AND " + DbHelper.COL_REVISE_SALES_ORDER_Batch_Id + " = ?", new String[]{ProductId + "", BatchId + ""});
                    System.out.println("Data is updated " + DbHelper.TABLE_REVISE_ORDER);
                }
            }
        } else {
            cursorVehicleInventory = db.getAllData(DbHelper.TABLE_SALESREP_INVENTORY);
            if (cursorVehicleInventory.getCount() == 0) {
                System.out.println("No Data of " + DbHelper.TABLE_SALESREP_INVENTORY);
            } else {
                while (cursorVehicleInventory.moveToNext()) {
                    int ProductId = cursorVehicleInventory.getInt(cursorVehicleInventory.getColumnIndex(DbHelper.COL_SRI_PRODUCT_ID));
                    int BatchId = cursorVehicleInventory.getInt(cursorVehicleInventory.getColumnIndex(DbHelper.COL_SRI_BATCH_ID));
                    int LoadQuantity = cursorVehicleInventory.getInt(cursorVehicleInventory.getColumnIndex(DbHelper.COL_SRI_QTY_INHAND));

                    ContentValues cv = new ContentValues();
                    cv.put(DbHelper.COL_REVISE_STOCK_R, LoadQuantity);
                    database.update(DbHelper.TABLE_REVISE_ORDER, cv, DbHelper.COL_REVISE_SALES_ORDER_ProductId + " = ? AND " + DbHelper.COL_REVISE_SALES_ORDER_Batch_Id + " = ?", new String[]{ProductId + "", BatchId + ""});
                    System.out.println("Data is updated " + DbHelper.TABLE_REVISE_ORDER);
                }
            }
        }
    }

    public ArrayList<ReviseSalesOrderList> getReviseOrderLineItems(int isVat) {

        System.out.println("+++++++ SyncManager para IsVat " + isVat);

        if (isVat == 2) {
            isVat = 0;
        }

        ArrayList<ReviseSalesOrderList> reviceSalesOrderListArrayList = new ArrayList<>();
        Cursor cursor = db.getAllData(DbHelper.TABLE_REVISE_ORDER);
        if (cursor.getCount() == 0) {
            System.out.println("No Data");
        } else {
            while (cursor.moveToNext()) {
                int ProductId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_REVISE_SALES_ORDER_ProductId));
                int BatchID = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_REVISE_SALES_ORDER_Batch_Id));
                double UnitSellingPrice = cursor.getDouble(cursor.getColumnIndex(DbHelper.COL_REVISE_SALES_UnitSellingPrice));
                String ExpiryDate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_REVISE_SALES_ExpiryDate));
                String ProductName = cursor.getString(cursor.getColumnIndex(DbHelper.COL_REVISE_SALES_ProductName));
                int CategoryId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_REVISE_SALES_CategoryId));
                int IsVat = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_REVISE_SALES_IsVat));
                double VatRate = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_REVISE_SALES_VatRate));
                int Stock = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_REVISE_STOCK_R));
                int Quantity = 0;
                int FreeQuantity = 0;


                //System.out.println("Product Name: " + ProductName + " ProductId: " + ProductId + "UnitSellingPrice: " + UnitSellingPrice);

                //System.out.println("Product Name: "+ProductName+" ProductId: " + ProductId + "UnitSellingPrice: " + UnitSellingPrice);

                System.out.println("Product Name: " + ProductName + " ProductId: " + ProductId + "UnitSellingPrice: " + UnitSellingPrice + " Stock: " + Stock);

                //System.out.println("+++++++ SyncManager IsVat " + IsVat);
                //if (Stock != 0 && IsVat == isVat) {

                //System.out.println( "+++++++ SyncManager IsVat " + IsVat);
                //if (Stock != 0 && IsVat==isVat) {

                System.out.println("+++++++ SyncManager IsVat " + IsVat);
                // if (Stock != 0 && IsVat==isVat) {
                if (Stock != 0) {

                    ReviseSalesOrderList reviceSalesOrderList = new ReviseSalesOrderList(ProductId, BatchID, UnitSellingPrice, ExpiryDate, ProductName,
                            CategoryId, IsVat, VatRate, Stock, Quantity, FreeQuantity, false, 0, 0);
                    reviceSalesOrderListArrayList.add(reviceSalesOrderList);
                }
            }
        }

        return reviceSalesOrderListArrayList;
    }


    public ArrayList<ReviseSalesOrderList> getReviseOrderLineItemsBySalesOrderId(int salesOrderId) {

        System.out.println("======================================== Invoice LineItem =======================================");

        System.out.println("SALES ORDER ID (SYNC_MANAGER) = " + salesOrderId);
        SQLiteDatabase database = db.getReadableDatabase();
        ArrayList<ReviseSalesOrderList> reviceSalesOrderListArrayList = new ArrayList<>();
        Cursor cursor = null;

        //get SalesRep Type
        String SalesRepType = null;
        String query = null;
        Cursor cursorSalesRep = db.getAllData(DbHelper.TABLE_SALES_REP);
        if (cursorSalesRep.getCount() == 0) {
            System.out.println("No Data of " + DbHelper.TABLE_SALES_REP);
        } else {
            while (cursorSalesRep.moveToNext()) {
                SalesRepType = cursorSalesRep.getString(cursorSalesRep.getColumnIndex(DbHelper.COL_SALES_REP_SalesRepType));
            }
        }

        if (SalesRepType.equalsIgnoreCase("R")) {
            query = "SELECT DISTINCT " +

                    "li." + DbHelper.COL_LINE_ITEM_ProductID + "," +
                    "li." + DbHelper.COL_LINE_ITEM_BatchID + "," +
                    "li." + DbHelper.COL_LINE_ITEM_UnitSellingPrice + "," +
                    "li." + DbHelper.COL_LINE_ITEM_Quantity + "," +
                    "li." + DbHelper.COL_LINE_ITEM_TotalAmount + "," +
                    "li." + DbHelper.COL_LINE_ITEM_FreeQuantity + "," +
                    "p." + COL_PRODUCT_ProductName + "," +
                    "p." + COL_PRODUCT_IsVat + "," +
                    "p." + COL_PRODUCT_VatRate + "," +
                    "p." + COL_PRODUCT_CategoryId + "," +
                    "vi." + COL_VEHICLE_INVENTORY_LoadQuantity + "," +
                    "pb." + COL_PRODUCT_BATCH_ExpiryDate +
                    " FROM " +
                    TABLE_LINE_ITEM + " AS li " +
                    "INNER JOIN " + TABLE_PRODUCT + " AS p ON li." + DbHelper.COL_LINE_ITEM_ProductID + " = p." + COL_PRODUCT_ProductId + " " +
                    "INNER JOIN " + TABLE_PRODUCT_BATCH + " AS pb ON li." + DbHelper.COL_LINE_ITEM_ProductID + " = pb." + COL_PRODUCT_ProductId + " " +
                    "INNER JOIN " + TABLE_VEHICALE_INVENTORY + " AS vi ON li." + DbHelper.COL_LINE_ITEM_ProductID + " = vi." + COL_VEHICLE_INVENTORY_ProductId + " " +
                    "WHERE li." + DbHelper.COL_LINE_ITEM_SalesOrderID + " = " + salesOrderId + " AND li." + DbHelper.COL_LINE_ITEM_BatchID + " = pb." + COL_PRODUCT_BATCH_BatchID + " AND li." + DbHelper.COL_LINE_ITEM_BatchID + " = vi." + DbHelper.COL_VEHICLE_INVENTORY_BatchID;
            System.out.println("R query======================");

        } else {


            query = "SELECT DISTINCT " +

                    "li." + DbHelper.COL_LINE_ITEM_ProductID + "," +
                    "li." + DbHelper.COL_LINE_ITEM_BatchID + "," +
                    "li." + DbHelper.COL_LINE_ITEM_UnitSellingPrice + "," +
                    "li." + DbHelper.COL_LINE_ITEM_Quantity + "," +
                    "li." + DbHelper.COL_LINE_ITEM_TotalAmount + "," +
                    "li." + DbHelper.COL_LINE_ITEM_FreeQuantity + "," +
                    "p." + COL_PRODUCT_ProductName + "," +
                    "p." + COL_PRODUCT_IsVat + "," +
                    "p." + COL_PRODUCT_VatRate + "," +
                    "p." + COL_PRODUCT_CategoryId + "," +
                    "sri." + COL_SRI_QTY_INHAND + "," +
                    "pb." + COL_PRODUCT_BATCH_ExpiryDate +
                    " FROM " +
                    TABLE_LINE_ITEM + " AS li " +
                    "INNER JOIN " + TABLE_PRODUCT + " AS p ON li." + DbHelper.COL_LINE_ITEM_ProductID + " = p." + COL_PRODUCT_ProductId + " " +
                    "INNER JOIN " + TABLE_PRODUCT_BATCH + " AS pb ON li." + DbHelper.COL_LINE_ITEM_ProductID + " = pb." + COL_PRODUCT_ProductId + " " +
                    "INNER JOIN " + TABLE_SALESREP_INVENTORY + " AS sri ON li." + DbHelper.COL_LINE_ITEM_ProductID + " = sri." + COL_SRI_PRODUCT_ID + " " +
                    "WHERE li." + DbHelper.COL_LINE_ITEM_SalesOrderID + " = " + salesOrderId + " AND li." + DbHelper.COL_LINE_ITEM_BatchID + " = pb." + COL_PRODUCT_BATCH_BatchID + " AND li." + DbHelper.COL_LINE_ITEM_BatchID + " = sri." + DbHelper.COL_SRI_BATCH_ID;
            System.out.println("p query======================");

        }


        cursor = database.rawQuery(query, null);

        if (cursor.getCount() == 0) {
            System.out.println("No Data of Selected Products");
        } else {
            while (cursor.moveToNext()) {

                int ProductId = cursor.getInt(0);
                int BatchID = cursor.getInt(1);
                double UnitSellingPrice = cursor.getDouble(2);
                int Quantity = cursor.getInt(3);
                double amount = cursor.getInt(4);
                int FreeQuantity = cursor.getInt(5);
                String ProductName = cursor.getString(6);
                int IsVat = cursor.getInt(7);
                double VatRate = cursor.getDouble(8);
                int CategoryId = cursor.getInt(9);
                int LoadQuantity = cursor.getInt(10);
                String ExpiryDate = cursor.getString(11);

                // if (LoadQuantity != 0) {
                ReviseSalesOrderList reviseSalesOrderList = new ReviseSalesOrderList(ProductId, BatchID, UnitSellingPrice, ExpiryDate, ProductName, CategoryId, IsVat, VatRate, LoadQuantity, Quantity, FreeQuantity, true, amount, 0);
                reviceSalesOrderListArrayList.add(reviseSalesOrderList);
                System.out.println("============== ProductId " + ProductId + " UnitSellingPrice " + UnitSellingPrice + "ProductName " + ProductName + " ExpiryDate " + ExpiryDate + " LoadQuantity " + LoadQuantity);
                //  }


            }
        }

        System.out.println("selected array list size in SyncManager Method" + reviceSalesOrderListArrayList.size());

        return reviceSalesOrderListArrayList;
    }

    //salesrep inventory
    public void getSalesRepInventoryFromServer(final int allsync, final int salesrep_id) {

        boolean success = db.deleteAllData(TABLE_SALESREP_INVENTORY);
        if (success) {
            System.out.println("data deleted salesrep inventory");
        } else {
            System.out.println("data not delete salesrep inventory");
        }

        String url = HTTPPaths.seriveUrl + "GetSalesRepInventoryListBySalesRepId?salesRepId=" + salesrep_id + "&withMetirialDetailobj=0&withBatchDetails=0";
        System.out.println("url - " + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonObject object = Json.parse(response).asObject();
                        int id = object.get("ID").asInt();

                        if (id == 200) {
                            String newUrl = response.replace("{\"Data\":\"", "{\"Data\": ");
                            String new2Url = newUrl.replace("\",\"ID\":200}", "}");
                            String new3Url = new2Url.replace("\\", "");

                            if (response != null) {

                                try {
                                    JSONObject jsonObject = new JSONObject(new3Url);

                                    JSONArray jsonArray = jsonObject.getJSONArray("Data");

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                                        int ProductId = jsonObject1.getInt("ProductId");
                                        int BatchID = jsonObject1.getInt("BatchID");
                                        int DistributorId = jsonObject1.getInt("DistributorId");
                                        int SalesRepId = jsonObject1.getInt("SalesRepId");
                                        int QuantityInHand = jsonObject1.getInt("QuantityInHand");
                                        int ReorderLevel = jsonObject1.getInt("ReorderLevel");
                                        String LastInventoryUpdate = jsonObject1.getString("LastInventoryUpdate");

                                        SalesRepInventoryDetails salesRepInventoryDetails = new SalesRepInventoryDetails(ProductId, BatchID,
                                                DistributorId, SalesRepId, QuantityInHand, ReorderLevel, LastInventoryUpdate);
                                        ContentValues cv = salesRepInventoryDetails.getSRInventoryContents();

                                        Boolean success = db.insertDataAll(cv, TABLE_SALESREP_INVENTORY);
                                        if (success) {
                                            System.out.println("salesrep inventory syncing - " + i);

                                            if (i == jsonArray.length() - 1) {
                                                System.out.println("salesrep inventory synced Completed , total - " + i);
                                                Toast.makeText(context, "Salesrep Inventory table synced successfully", Toast.LENGTH_SHORT).show();

                                                time_ = (new DateManager().getDateWithTime());
                                                updateSyncTable(Salesrep_inventory, time_, i + 1, i + 1, salesrep_id, 1);

                                                allRowCounts = allRowCounts + i + 1;
                                                if (allsync == 1) {
                                                    geTSalesrepInvoice_fromSERVER(1, salesrep_id);
                                                }
                                            }
                                        } else {
                                            System.out.println("Data is not inserted to salesrep inventory");
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error - " + error.toString());
                    }
                });

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }

    public ArrayList<SalesRepInventoryDetails> salesRepInventoryDetailsFromSQLite(String sql) {

        Cursor cursor;
        ArrayList<SalesRepInventoryDetails> arry = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
        cursor = sqLiteDatabase.rawQuery(sql, null);
        if (cursor.getCount() == 0) {
            System.out.println("No data");
        } else {
            while (cursor.moveToNext()) {
                int ProductId = cursor.getInt(cursor.getColumnIndex(COL_SRI_PRODUCT_ID));
                int BatchID = cursor.getInt(cursor.getColumnIndex(COL_SRI_BATCH_ID));
                int DistributorId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SRI_DISTRIBUTOR_ID));
                int SalesRepId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SRI_SALESREP_ID));
                int QuantityInHand = cursor.getInt(cursor.getColumnIndex(COL_SRI_QTY_INHAND));
                int ReorderLevel = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SRI_REORDER_LEVEL));
                String LastInventoryUpdate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SRI_LAST_INVENTORYUPDATE));

                SalesRepInventoryDetails salesRepInventoryDetails = new SalesRepInventoryDetails(ProductId,
                        BatchID, DistributorId, SalesRepId, QuantityInHand, ReorderLevel, LastInventoryUpdate);
                arry.add(salesRepInventoryDetails);
            }
        }
        return arry;
    }

    public ArrayList<ProductList> getProductListFromSQLite(int vatvalue, int categoryid, String salesrepType) {
        Cursor cursor = null;
        ArrayList<ProductList> productLists = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();

        String sqlMain = "";
        String vatPart = TABLE_PRODUCT + "." + COL_PRODUCT_IsVat + " = " + vatvalue;
        String categoryPart = TABLE_PRODUCT + "." + COL_PRODUCT_CategoryId + " = " + categoryid;

        String sql_vehicle2 = "SELECT DISTINCT "
                + TABLE_PRODUCT + "." + COL_PRODUCT_ProductName + ","
                + TABLE_PRODUCT + "." + COL_PRODUCT_ProductId + ","
                + TABLE_PRODUCT + "." + COL_PRODUCT_IsVat + ","
                + TABLE_PRODUCT + "." + COL_PRODUCT_CategoryId + ","
                //+ TABLE_PRODUCT + "." + COL_PRODUCT_UnitSellingPrice + ","
                + TABLE_PRODUCT + "." + COL_PRODUCT_VatRate + ","
                + TABLE_PRODUCT_BATCH + "." + COL_PRODUCT_BATCH_ExpiryDate + ","
                + TABLE_PRODUCT_BATCH + "." + COL_PRODUCT_BATCH_BatchID + ","
                + TABLE_PRODUCT_BATCH + "." + COL_PRODUCT_BATCH_UnitSellingPrice + ","
                + TABLE_VEHICALE_INVENTORY + "." + COL_VEHICLE_INVENTORY_LoadQuantity
                + " FROM "
                + TABLE_PRODUCT +
                " INNER JOIN " + TABLE_PRODUCT_BATCH + " ON "
                + TABLE_PRODUCT + "." + COL_PRODUCT_ProductId + " = " + TABLE_PRODUCT_BATCH + "." + COL_PRODUCT_BATCH_ProductId
                + " INNER JOIN " + TABLE_VEHICALE_INVENTORY + " ON "
                + TABLE_PRODUCT_BATCH + "." + COL_PRODUCT_BATCH_BatchID + " = " + TABLE_VEHICALE_INVENTORY + "." + COL_VEHICLE_INVENTORY_BatchID
                + " AND " + TABLE_PRODUCT_BATCH + "." + COL_PRODUCT_BATCH_ProductId + " = " + TABLE_VEHICALE_INVENTORY + "." + COL_VEHICLE_INVENTORY_ProductId
                //+ " GROUP BY "
                + " WHERE " + COL_VEHICLE_INVENTORY_LoadQuantity + " > 0 ";

        String sql_salesrep2 = "SELECT DISTINCT "
                + TABLE_PRODUCT + "." + COL_PRODUCT_ProductName + ","
                + TABLE_PRODUCT + "." + COL_PRODUCT_ProductId + ","
                + TABLE_PRODUCT + "." + COL_PRODUCT_IsVat + ","
                + TABLE_PRODUCT + "." + COL_PRODUCT_CategoryId + ","
                //  + TABLE_PRODUCT + "." + COL_PRODUCT_UnitSellingPrice + ","
                + TABLE_PRODUCT + "." + COL_PRODUCT_VatRate + ","
                + TABLE_PRODUCT_BATCH + "." + COL_PRODUCT_BATCH_ExpiryDate + ","
                + TABLE_PRODUCT_BATCH + "." + COL_PRODUCT_BATCH_BatchID + ","
                + TABLE_PRODUCT_BATCH + "." + COL_PRODUCT_UnitSellingPrice + ","
                + TABLE_SALESREP_INVENTORY + "." + COL_SRI_QTY_INHAND
                + " FROM "
                + TABLE_PRODUCT +
                " INNER JOIN " + TABLE_PRODUCT_BATCH + " ON "
                + TABLE_PRODUCT + "." + COL_PRODUCT_ProductId + " = " + TABLE_PRODUCT_BATCH + "." + COL_PRODUCT_BATCH_ProductId
                + " INNER JOIN " + TABLE_SALESREP_INVENTORY + " ON "
                + TABLE_PRODUCT_BATCH + "." + COL_PRODUCT_BATCH_BatchID + " = " + TABLE_SALESREP_INVENTORY + "." + COL_SRI_BATCH_ID
                + " AND " + TABLE_PRODUCT_BATCH + "." + COL_PRODUCT_BATCH_ProductId + " = " + TABLE_SALESREP_INVENTORY + "." + COL_SRI_PRODUCT_ID
                + " WHERE " + COL_SRI_QTY_INHAND + " > 0 ";

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        //vatvalue = 0 - non-vat
        //vatvalue = 1 - vat
        //vatvalue = 2 - ignore vat part

        //cat_id = 0 - all


        if (salesrepType.equalsIgnoreCase("p")) {         //this should be p-pre sale

            if (vatvalue == 2 && categoryid == 0) {

                sqlMain = sql_salesrep2;

            } else if (vatvalue == 2 && categoryid != 0) {

                sqlMain = sql_salesrep2 + " AND " + categoryPart;
            } else if (vatvalue != 2 && categoryid == 0) {

                sqlMain = sql_salesrep2 + " AND " + vatPart;

            } else if (vatvalue != 2 && categoryid != 0) {
                sqlMain = sql_salesrep2 + " AND " + vatPart + " AND " + categoryPart;
            }
        }
        if (salesrepType.equalsIgnoreCase("r")) {         //this should be r-

            if (vatvalue == 2 && categoryid == 0) {

                sqlMain = sql_vehicle2;

            } else if (vatvalue == 2 && categoryid != 0) {

                sqlMain = sql_vehicle2 + " AND " + categoryPart;
            } else if (vatvalue != 2 && categoryid == 0) {

                sqlMain = sql_vehicle2 + " AND " + vatPart;

            } else if (vatvalue != 2 && categoryid != 0) {
                sqlMain = sql_vehicle2 + " AND " + vatPart + " AND " + categoryPart;
            }
        }


        System.out.println("sql MAIN - " + sqlMain);
        cursor = sqLiteDatabase.rawQuery(sqlMain, null);

        if (cursor.getCount() == 0) {
            System.out.println("no data");
        } else {
            while (cursor.moveToNext()) {

                String ProductName = cursor.getString(0);
                int ProductId = cursor.getInt(1);
                int IsVat = cursor.getInt(2);
                int catid = cursor.getInt(3);
                double vatrate = cursor.getDouble(4);
                String ExpiryDate = cursor.getString(5);
                int BatchID = cursor.getInt(6);
                double UnitSellingPrice = cursor.getDouble(7);
                int LoadQuantity = cursor.getInt(8);

                ProductList productList = new ProductList(ProductName, ExpiryDate, LoadQuantity, UnitSellingPrice,
                        0, 0, 0, R.drawable.ic_not_done, BatchID, ProductId, IsVat, vatrate, 0, 0, 0, 0, catid);

                System.out.println("~~~~~~~~~ ProductName - " + ProductName + ",IsVat - " + IsVat);

                /*
                String name, String expireDate, int stock, double unitPrice,
                       double subTotal, int qty, int freeQty, int img, int batchid, int productid,
                       int isVatHas, double vatRate, int returnqty,
                       double row_free, double row_net, double row_total,int catid
                 */

                productLists.add(productList);
            }

        }

        System.out.println("productList.size() - " + productLists.size());

        return productLists;
    }


    public ArrayList<ProductList> getProductList_Salesrep_FromSQLite() {
        Cursor cursor = null;
        ArrayList<ProductList> productLists = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();

        String sql_salesrep = "SELECT DISTINCT "
                + TABLE_PRODUCT + "." + COL_PRODUCT_ProductName + ","
                + TABLE_PRODUCT + "." + COL_PRODUCT_ProductId + ","
                + TABLE_PRODUCT + "." + COL_PRODUCT_IsVat + ","
                + TABLE_PRODUCT + "." + COL_PRODUCT_CategoryId + ","
                //  + TABLE_PRODUCT + "." + COL_PRODUCT_UnitSellingPrice + ","
                + TABLE_PRODUCT + "." + COL_PRODUCT_VatRate + ","
                + TABLE_PRODUCT_BATCH + "." + COL_PRODUCT_BATCH_ExpiryDate + ","
                + TABLE_PRODUCT_BATCH + "." + COL_PRODUCT_BATCH_BatchID + ","
                + TABLE_PRODUCT_BATCH + "." + COL_PRODUCT_UnitSellingPrice + ","
                + TABLE_SALESREP_INVENTORY + "." + COL_SRI_QTY_INHAND
                + " FROM "
                + TABLE_PRODUCT +
                " INNER JOIN " + TABLE_PRODUCT_BATCH + " ON "
                + TABLE_PRODUCT + "." + COL_PRODUCT_ProductId + " = " + TABLE_PRODUCT_BATCH + "." + COL_PRODUCT_BATCH_ProductId
                + " INNER JOIN " + TABLE_SALESREP_INVENTORY + " ON "
                + TABLE_PRODUCT_BATCH + "." + COL_PRODUCT_BATCH_BatchID + " = " + TABLE_SALESREP_INVENTORY + "." + COL_SRI_BATCH_ID
                + " AND " + TABLE_PRODUCT_BATCH + "." + COL_PRODUCT_BATCH_ProductId + " = " + TABLE_SALESREP_INVENTORY + "." + COL_SRI_PRODUCT_ID
                + " WHERE " + COL_SRI_QTY_INHAND + " > 0 ";

        System.out.println("sql sql_salesrep - " + sql_salesrep);
        cursor = sqLiteDatabase.rawQuery(sql_salesrep, null);

        if (cursor.getCount() == 0) {
            System.out.println("no data");
        } else {
            while (cursor.moveToNext()) {

                String ProductName = cursor.getString(0);
                int ProductId = cursor.getInt(1);
                int IsVat = cursor.getInt(2);
                int catid = cursor.getInt(3);
                double vatrate = cursor.getDouble(4);
                String ExpiryDate = cursor.getString(5);
                int BatchID = cursor.getInt(6);
                double UnitSellingPrice = cursor.getDouble(7);
                int LoadQuantity = cursor.getInt(8);

                ProductList productList = new ProductList(ProductName, ExpiryDate, LoadQuantity, UnitSellingPrice,
                        0, 0, 0, R.drawable.ic_not_done, BatchID, ProductId, IsVat, vatrate, 0, 0, 0, 0, catid);
                productLists.add(productList);
            }
        }
        System.out.println("productList.size() - " + productLists.size());
        return productLists;
    }


    public ArrayList<MerchantStockLineitems> getAllProductListFromSQLite() {
        Cursor cursor = null;
        ArrayList<MerchantStockLineitems> merchantStockLineitemses = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();

        String sql_ = "SELECT * FROM " + DbHelper.TABLE_PRODUCT + " WHERE " + DbHelper.COL_PRODUCT_AddToCard + " = " + 1;


        System.out.println("sql_ " + sql_);
        cursor = sqLiteDatabase.rawQuery(sql_, null);

        if (cursor.getCount() == 0) {
            System.out.println("no data");
        } else {
            while (cursor.moveToNext()) {

                int ProductId = cursor.getInt(0);
                String ProductName = cursor.getString(5);
                MerchantStockLineitems merchantStockLineitems = new MerchantStockLineitems(0, ProductId, ProductName, 0, 0, "");
                merchantStockLineitemses.add(merchantStockLineitems);
            }
        }
        System.out.println("merchantStockLineitemses.size() - " + merchantStockLineitemses.size());

        return merchantStockLineitemses;
    }


    //Get All Products
    public ArrayList<DefProduct> getProductArrayList() {
        ArrayList<DefProduct> productArrayList = new ArrayList<>();
        Cursor cursor = db.getAllData(DbHelper.TABLE_PRODUCT);
        if (cursor.getCount() == 0) {
            System.out.println("No Data in " + DbHelper.TABLE_PRODUCT);
        } else {
            while (cursor.moveToNext()) {
                //Products
                int ProductId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_PRODUCT_ProductId));
                int PackingTypeId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_PRODUCT_PackingTypeId));
                int SellingCategoryId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_PRODUCT_SellingCategoryId));
                int CompanyId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_PRODUCT_CompanyId));
                int CategoryId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_PRODUCT_CategoryId));
                String ProductName = cursor.getString(cursor.getColumnIndex(DbHelper.COL_PRODUCT_ProductName));
                int IsActive = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_PRODUCT_IsActive));
                String EnteredDate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_PRODUCT_EnteredDate));
                String EnteredUser = cursor.getString(cursor.getColumnIndex(DbHelper.COL_PRODUCT_EnteredUser));
                double UnitSellingPrice = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_PRODUCT_UnitSellingPrice));
                double UnitDistributorPrice = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_PRODUCT_UnitDistributorPrice));
                String ReferenceID = cursor.getString(cursor.getColumnIndex(DbHelper.COL_PRODUCT_ReferenceID));
                int IsVat = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_PRODUCT_IsVat));
                int VatRate = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_PRODUCT_VatRate));

                DefProduct defProduct = new DefProduct(ProductId, PackingTypeId, SellingCategoryId, CompanyId, CategoryId,
                        ProductName, IsActive, EnteredDate, EnteredUser, UnitSellingPrice, UnitDistributorPrice, ReferenceID, IsVat, VatRate);
                productArrayList.add(defProduct);
            }
        }

        return productArrayList;
    }

    //get Return Types from server
    public void getReturnTypesFromServer(final int allsync, final int salesrep) {
        boolean success = db.deleteAllData(TABLE_RETURN_TYPE);

        if (success) {
            System.out.println("Succesfuly delete return type table data");
        } else {
            System.out.println("Error occur deleting return type table");
        }

        String url = HTTPPaths.seriveUrl + "GetDefReturnTypeList";
        try {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            JsonObject object = Json.parse(response).asObject();
                            int id = object.get("ID").asInt();

                            if (id == 200) {
                                String newUrl = response.replace("{\"Data\":\"", "{\"Data\": ");
                                String new2Url = newUrl.replace("\",\"ID\":200}", "}");
                                String new3Url = new2Url.replace("\\", "");

                                if (response != null) {
                                    try {
                                        JSONObject jsonObj_main = new JSONObject(new3Url);
                                        JSONArray jsonAry_main = jsonObj_main.getJSONArray("Data");
                                        for (int i = 0; i < jsonAry_main.length(); i++) {
                                            JSONObject jsonObj_2 = jsonAry_main.getJSONObject(i);

                                            int ReturnType = jsonObj_2.getInt("ReturnType");
                                            String ReturnTypeName = jsonObj_2.getString("ReturnTypeName");

                                            ReturnType returnType = new ReturnType(ReturnType, ReturnTypeName);
                                            ContentValues cv = returnType.getReturnTypeCV();
                                            Boolean success = db.insertDataAll(cv, DbHelper.TABLE_RETURN_TYPE);
                                            if (success) {
                                                System.out.println("ReturnType syncing - " + i);

                                                if (i == jsonAry_main.length() - 1) {
                                                    System.out.println("ReturnType synced Completed , total - " + i);
                                                    Toast.makeText(context, "Return type table synced successfully", Toast.LENGTH_SHORT).show();
                                                    time_ = (new DateManager().getDateWithTime());
                                                    updateSyncTable(Return_type, time_, i + 1, i + 1, salesrep, 1);
                                                    if (allsync == 1) {
                                                        syncPath(1, salesrep);
                                                    }
                                                    allRowCounts = allRowCounts + i + 1;
                                                }
                                            } else {
                                                System.out.println("Data is not inserted to return type");
                                                updateSyncTable(Return_type, time_, 0, i, salesrep, 0);
                                            }
                                        }

                                    } catch (JSONException e) {
                                        System.out.println("Sync TABLE_RETURN_TYPE Json Error" + e.getMessage());
                                    }
                                }
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("Sync TABLE_RETURN_TYPE Volley Error " + error.getMessage());
                        }
                    }
            );
            MySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(stringRequest);


        } catch (Exception e) {
            System.out.println("Sync TABLE_RETURN_TYPE Error" + e.getMessage());
        }
    }


    public ArrayList<ReturnType> getReturnTypeArrayList() {

        ArrayList<ReturnType> returnTypeArrayList = new ArrayList<>();
        Cursor cursor = db.getAllData(DbHelper.TABLE_RETURN_TYPE);
        if (cursor.getCount() == 0) {
            System.out.println("No Data in " + DbHelper.TABLE_RETURN_TYPE);
        } else {
            while (cursor.moveToNext()) {
                int ReturnType = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_RETURN_TYPE_ReturnType));
                String ReturnTypeName = cursor.getString(cursor.getColumnIndex(DbHelper.COL_RETURN_TYPE_ReturnTypeName));
                ReturnType returnType = new ReturnType(ReturnType, ReturnTypeName);
                returnTypeArrayList.add(returnType);
            }
        }

        return returnTypeArrayList;
    }

    //Get ProducBatch ArrayList
    public ArrayList<ProductBatch> getProductBatchArrayList() {

        ArrayList<ProductBatch> productBatchArrayList = new ArrayList<>();
        Cursor cursor = db.getAllData(DbHelper.TABLE_PRODUCT_BATCH);
        if (cursor.getCount() == 0) {
            System.out.println("No Data in " + DbHelper.TABLE_PRODUCT_BATCH);
        } else {
            while (cursor.moveToNext()) {
                int productId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_PRODUCT_BATCH_ProductId));
                int BatchId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_PRODUCT_BATCH_BatchID));
                int Quantity = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_PRODUCT_BATCH_Quantity));
                int BlockQuantity = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_PRODUCT_BATCH_BlockQuantity));
                double UnitDealrePrice = cursor.getDouble(cursor.getColumnIndex(DbHelper.COL_PRODUCT_BATCH_UnitDealrePrice));
                double UnitSellingPrice = cursor.getDouble(cursor.getColumnIndex(DbHelper.COL_PRODUCT_BATCH_UnitSellingPrice));
                int IsAllSold = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_PRODUCT_BATCH_IsAllSold));
                String ExpiryDate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_PRODUCT_BATCH_ExpiryDate));
                String EnteredUser = cursor.getString(cursor.getColumnIndex(DbHelper.COL_PRODUCT_BATCH_EnteredUser));
                String EnteredDate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_PRODUCT_BATCH_EnteredDate));

                ProductBatch productBatch = new ProductBatch(productId, BatchId, Quantity, BlockQuantity,
                        UnitDealrePrice, UnitSellingPrice, IsAllSold,
                        ExpiryDate, EnteredUser, EnteredDate);
                System.out.println("Product ID " + productId + " BatchId " + BatchId + " ======== Unit Selling Price " + UnitSellingPrice);
                productBatchArrayList.add(productBatch);
            }
            System.out.println("======== Array List Size " + productBatchArrayList.size());

        }

        return productBatchArrayList;

    }

    //get credit notes line items according to current date


    //Get salesrep_INVOICE_DETAILS from
    public void geTSalesrepInvoice_fromSERVER(final int allsync, final int salesrep_id) {

        String table = TABLE_INVOICE_ITEM;
        String wherepart = COL_INVOICE_ITEM_IsSync + " = " + 1;
        (new DbHelper(context)).deleteFromAnyTable(table, wherepart);


        String table2 = TABLE_INVOICE_LINE_ITEM;
        String wherepart2 = COL_INVOICE_LINE_ITEM_issync + " = " + 1;
        (new DbHelper(context)).deleteFromAnyTable(table2, wherepart2);


        //boolean success = db.deleteAllData(TABLE_INVOICE_ITEM);
        System.out.println("Successfully delete invoice item table data");
        System.out.println("Successfully delete invoice line item table data");

        final String url = HTTPPaths.seriveUrl + "GetSalesInvoiceListBySalesRep?salesRepId=" + salesrep_id + "&withSalesRep=1&withMerchant=1";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JsonObject object = Json.parse(response).asObject();
                        int id = object.get("ID").asInt();

                        if (id == 200) {
                            String newUrl = response.replace("{\"Data\":\"", "{\"Data\": ");
                            String new2Url = newUrl.replace("\",\"ID\":200}", "}");
                            String new3Url = new2Url.replace("\\", "");
                            System.out.println(" sql invoice - " + url);

                            if (response != null) {
                                try {
                                    JSONObject jsonObj_main = new JSONObject(new3Url);
                                    JSONArray jsonAry_main = jsonObj_main.getJSONArray("Data");

                                    for (int i = 0; i < jsonAry_main.length(); i++) {
                                        JSONObject jsonObj_2 = jsonAry_main.getJSONObject(i);
                                        int VehicleId = jsonObj_2.getInt("VehicleId");
                                        String VehicleNumber = jsonObj_2.getString("VehicleNumber");
                                        String DeliveryDate = jsonObj_2.getString("DeliveryDate");
                                        int IsDelivered = jsonObj_2.getInt("IsDelivered");
                                        double OusAmount = jsonObj_2.getDouble("OusAmount");
                                        double InvoiceAmount = jsonObj_2.getDouble("InvoiceAmount");
                                        String SyncDate = jsonObj_2.getString("SyncDate");
                                        int IsSync = 1;//jsonObj_2.getInt("IsSync");
                                        int SaleStatus = jsonObj_2.getInt("SaleStatus");
                                        String EnteredUser = jsonObj_2.getString("EnteredUser");
                                        String EnteredDate = jsonObj_2.getString("EnteredDate");
                                        String InvoiceDate = jsonObj_2.getString("InvoiceDate");
                                        int SaleTypeId = jsonObj_2.getInt("SaleTypeId");
                                        int SalesRepId = jsonObj_2.getInt("SalesRepId");
                                        int DistributorId = jsonObj_2.getInt("DistributorId");
                                        int MerchantId = jsonObj_2.getInt("MerchantId");
                                        int InvoiceId = jsonObj_2.getInt("InvoiceId");
                                        double TotalVAT = jsonObj_2.getDouble("TotalVAT");
                                        JSONObject j_salesrep = jsonObj_2.getJSONObject("_DefSalesRef");
                                        String salesrep_name = j_salesrep.getString("SalesRepName");
                                        String salesrep_co_no = j_salesrep.getString("ContactNo");
                                        JSONObject j_merchant = jsonObj_2.getJSONObject("_DefMerchant");
                                        String merchantName = j_merchant.getString("MerchantName");
                                        String buildingNo = j_merchant.getString("BuildingNo");
                                        String address1 = j_merchant.getString("Address1");
                                        String address2 = j_merchant.getString("Address2");
                                        String city = j_merchant.getString("City");
                                        String merchantContactNo = j_merchant.getString("ContactNo1");
                                        int IsCredit = j_merchant.getInt("IsCredit");
                                        String CreditDays = j_merchant.getString("CreditDays");

                                        System.out.println("@@@@@@@@@@@@@@@ InvoiceDate " + InvoiceDate);

                                        InvoiceList2 invoiceList2 = new InvoiceList2(
                                                VehicleId, VehicleNumber, DeliveryDate, IsDelivered,
                                                OusAmount, InvoiceAmount, SyncDate, IsSync, SaleStatus, EnteredUser, EnteredDate, InvoiceDate, SaleTypeId,
                                                SalesRepId, DistributorId, MerchantId, InvoiceId, TotalVAT, salesrep_name, salesrep_co_no, merchantName, buildingNo,
                                                address1, address2, city, IsCredit, CreditDays, merchantContactNo
                                        );

                                        ContentValues cv = invoiceList2.getCVfromInvoicelist();
                                        //System.out.println(" cv  size invoice - " + cv.get("InvoiceId").toString());
                                        boolean success = db.insertDataAll(cv, TABLE_INVOICE_ITEM);

                                        if (success) {
                                            System.out.println("invoice syncing - " + i);
                                            if (i == jsonAry_main.length() - 1) {
                                                System.out.println("invoice synced Completed , total - " + i);
                                                Toast.makeText(context, "Invoice table synced successfully", Toast.LENGTH_SHORT).show();
                                                time_ = (new DateManager().getDateWithTime());
                                                updateSyncTable(Invoice, time_, i + 1, i + 1, salesrep_id, 1);
                                                //Toast.makeText(context, "completed", Toast.LENGTH_SHORT).show();
                                                allRowCounts = allRowCounts + i + 1;

                                                if (allsync == 1) {
                                                    //syncMerchantStockData(1, salesrep_id);
                                                    uploadMerchantStockData(1, salesrep_id);

                                                }

                                            }
                                        } else {
                                            System.out.println("Data not inserted");
                                        }
                                        getSalesrepInvoice_lineitems_fromSERVER(InvoiceId, salesrep_id);
                                    }

                                } catch (final JSONException e) {
                                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                                }
                            } else {
                                Log.e(TAG, "Couldn't get json from server.");

                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                });

        MySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(stringRequest);


    }


    public void getSalesrepInvoice_lineitems_fromSERVER(int invoice_ID, final int salesrep_id) {

        final String url = HTTPPaths.seriveUrl + "GetInvoiceLineItemsByInvoiceID?invoiceID=" + invoice_ID;

        System.out.println("url invoice line item- " + url);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonObject object = Json.parse(response).asObject();
                        int id = object.get("ID").asInt();
                        //finalInvoiceLineItemArrayList.clear();
                        if (id == 200) {
                            String newUrl = response.replace("{\"Data\":\"", "{\"Data\": ");
                            String new2Url = newUrl.replace("\",\"ID\":200}", "}");
                            String new3Url = new2Url.replace("\\", "");

                            if (response != null) {
                                try {
                                    JSONObject jsonObj_main = new JSONObject(new3Url);
                                    JSONArray jsonAry_main = jsonObj_main.getJSONArray("Data");

                                    for (int i = 0; i < jsonAry_main.length(); i++) {

                                        lineItemCount++;
                                        JSONObject jsonObj_2 = jsonAry_main.getJSONObject(i);

                                        // get invoice details
                                        int BatchID = jsonObj_2.getInt("BatchID");
                                        int ProductID = jsonObj_2.getInt("ProductID");
                                        int InvoiceID = jsonObj_2.getInt("InvoiceID");
                                        int Quantity = jsonObj_2.getInt("Quantity");
                                        int FreeQuantity = jsonObj_2.getInt("FreeQuantity");
                                        double UnitSellingPrice = jsonObj_2.getDouble("UnitSellingPrice");
                                        double TotalAmount = jsonObj_2.getDouble("TotalAmount");
                                        int IsSync = 1;//jsonObj_2.getInt("IsSync");
                                        String SyncDate = jsonObj_2.getString("SyncDate");

                                        //get Product Name
                                        JSONObject product = jsonObj_2.getJSONObject("_Product");
                                        String ProductName = product.getString("ProductName");

                                        System.out.println("ProductID - " + ProductID + ",lineItemCount - " + lineItemCount);

                                        InvoiceLineItem invoiceLineItem = new InvoiceLineItem
                                                (BatchID, ProductID, InvoiceID, Quantity, FreeQuantity, UnitSellingPrice, TotalAmount, IsSync, SyncDate, ProductName);
                                        //Toast.makeText(context, ""+invoiceLineItem.getItemName(), Toast.LENGTH_SHORT).show();
                                        //finalInvoiceLineItemArrayList.add(invoiceLineItem);
                                        //finalInvoiceLineItemArrayList.add(invoiceLineItem);
                                        ContentValues cv = invoiceLineItem.getCVvaluesFromInvoiceLineItem();
                                        boolean success = db.insertDataAll(cv, TBL_INVOICE_LINE_ITEM);
                                        if (success) {
                                            System.out.println("insert invoice line item - " + i);
                                            if (i == jsonAry_main.length() - 1) {
                                                System.out.println("completly inserted all invoice line items ");
                                                //time_ = (new DateManager().getDateWithTime());
                                                updateSyncTable(Invoice_line_item, time_, lineItemCount, lineItemCount, salesrep_id, 1);
                                                updateSyncTable(All, time_, 0, allRowCounts, salesrep_id, 0);
                                            }

                                        } else {
                                            System.out.println("data not insert to invoice line item ");
                                            updateSyncTable(Invoice_line_item, time_, 0, lineItemCount, salesrep_id, 0);
                                            updateSyncTable(All, time_, 1, allRowCounts, salesrep_id, 0);
                                        }

                                    }


                                } catch (final JSONException e) {
                                    Toast.makeText(context.getApplicationContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    System.out.println("Json parsing error: " + e.getMessage());
                                }
                            } else {
                                Toast.makeText(context.getApplicationContext(), "Couldn't get json from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(context, "No Data.. try again later", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Villey error.." + error.getMessage());
                    }
                }
        );
        MySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(stringRequest);


    }

    public ArrayList<InvoiceList2> getInvoiceDetailsFromSQLite() {

        Cursor cursor = null;
        //SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
        ArrayList<InvoiceList2> invoiceListArray = new ArrayList<>();

        //cursor = sqLiteDatabase.rawQuery(sql,null);
        cursor = db.getAllData(TBL_INVOICE_ITEM);
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {

                int VehicleId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_INVOICE_ITEM_VehicleID));
                String VehicleNumber = cursor.getString(cursor.getColumnIndex(DbHelper.COL_INVOICE_ITEM_VehicleNumber));
                String DeliveryDate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_INVOICE_ITEM_DeliveryDate));
                int IsDelivered = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_INVOICE_ITEM_isDelivered));
                int OusAmount = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_INVOICE_ITEM_OusAmount));
                int InvoiceAmount = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_INVOICE_ITEM_InvoiceAmount));
                String SyncDate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_INVOICE_ITEM_SyncDate));
                int IsSync = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_INVOICE_ITEM_IsSync));
                int SaleStatus = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_INVOICE_ITEM_SalesStatus));
                String EnteredUser = cursor.getString(cursor.getColumnIndex(DbHelper.COL_INVOICE_ITEM_EnteredUser));
                String EnteredDate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_INVOICE_ITEM_EnteredDate));
                String InvoiceDate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_INVOICE_ITEM_InvoiceDate));
                int SaleTypeId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_INVOICE_ITEM_SaleTypeId));
                int SalesRepId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_INVOICE_ITEM_SalesRepId));
                int DistributorId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_INVOICE_ITEM_DistributorId));
                int MerchantId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_INVOICE_ITEM_MerchantId));
                int InvoiceId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_INVOICE_ITEM_InvoiceId));
                int TotalVAT = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_INVOICE_ITEM_TotalVAT));

                String salesrep_name = cursor.getString(cursor.getColumnIndex(DbHelper.COL_INVOICE_ITEM_salesRepName));
                String salesrep_co_no = cursor.getString(cursor.getColumnIndex(DbHelper.COL_INVOICE_ITEM_salesRepContactNo));
                String merchantName = cursor.getString(cursor.getColumnIndex(DbHelper.COL_INVOICE_ITEM_merchantName));
                String buildingNo = cursor.getString(cursor.getColumnIndex(DbHelper.COL_INVOICE_ITEM_buildingNo));
                String address1 = cursor.getString(cursor.getColumnIndex(DbHelper.COL_INVOICE_ITEM_address1));
                String address2 = cursor.getString(cursor.getColumnIndex(DbHelper.COL_INVOICE_ITEM_address2));
                String city = cursor.getString(cursor.getColumnIndex(DbHelper.COL_INVOICE_ITEM_city));
                String merchantContactNo = cursor.getString(cursor.getColumnIndex(DbHelper.COL_INVOICE_ITEM_merchantContactNo));
                int IsCredit = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_INVOICE_ITEM_IsCredit));
                String CreditDays = cursor.getString(cursor.getColumnIndex(DbHelper.COL_INVOICE_ITEM_CreditDays));

                System.out.println("@@@@@@@@@@@@@@@ createdDate " + InvoiceDate);
                InvoiceList2 invoiceList2 = new InvoiceList2(VehicleId, VehicleNumber, DeliveryDate, IsDelivered, OusAmount, InvoiceAmount, SyncDate, IsSync,
                        SaleStatus, EnteredUser, EnteredDate, InvoiceDate, SaleTypeId, SalesRepId, DistributorId, MerchantId, InvoiceId, TotalVAT, salesrep_name,
                        salesrep_co_no, merchantName, buildingNo, address1, address2, city, IsCredit, CreditDays, merchantContactNo);

                invoiceListArray.add(invoiceList2);
            }
        } else {
            System.out.println("no data");
        }

        return invoiceListArray;
    }


    public ArrayList<String> getInvoiceDetails_merchantId_FromSQLite() {

        Cursor cursor = null;
        //SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
        ArrayList<String> invoiceMerchant = new ArrayList<>();

        //cursor = sqLiteDatabase.rawQuery(sql,null);
        cursor = db.getAllData(TBL_INVOICE_ITEM);
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {

                int MerchantId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_INVOICE_ITEM_MerchantId));
                System.out.println("@@@@@@@@@@@@@@@ createdDate " + MerchantId);
                invoiceMerchant.add(String.valueOf(MerchantId));
            }
        } else {
            System.out.println("no data");
        }

        return invoiceMerchant;
    }


    public void deleteSyncedInvoiceData() {

        //boolean b = false;

        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
        Cursor cursor = null;
        String sql = "DELETE FROM " + TBL_INVOICE_ITEM + " WHERE " + COL_INVOICE_ITEM_IsSync + " = 1 ";

        //cursor = sqLiteDatabase.rawQuery(sql,null);
        System.out.println("sql delete- " + sql);
        sqLiteDatabase.execSQL(sql);


        //return b;
    }


    public String getStringValueFromSQLite(String sql, String columnName) {

        Cursor cursor;
        String response = null;
        SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();

        cursor = sqLiteDatabase.rawQuery(sql, null);
        if (cursor.getCount() == 0) {
            response = "No Data";
        } else {
            while (cursor.moveToNext()) {
                response = cursor.getString(cursor.getColumnIndex(columnName));
            }
        }
        return response;
    }

    public ArrayList<InvoiceLineItem> getInvoiceLineItem_ACC_invoiceID(int invoiceid) {

        Cursor cursor;
        ArrayList<InvoiceLineItem> invoiceLineItems = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_INVOICE_LINE_ITEM + " WHERE " + COL_INVOICE_LINE_ITEM_invoiceID + " = " + invoiceid;
        cursor = sqLiteDatabase.rawQuery(sql, null);

        if (cursor.getCount() != 0) {
            System.out.println("Cursor size: " + cursor.getCount());
            while (cursor.moveToNext()) {

                int batchId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_INVOICE_LINE_ITEM_batchID));
                int productID = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_INVOICE_LINE_ITEM_productID));
                int invoiceId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_INVOICE_LINE_ITEM_invoiceID));
                int qty = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_INVOICE_LINE_ITEM_qty));
                int freeQu = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_INVOICE_LINE_ITEM_freeQuantity));
                double price = cursor.getDouble(cursor.getColumnIndex(DbHelper.COL_INVOICE_LINE_ITEM_price));
                double lineTotal = cursor.getDouble(cursor.getColumnIndex(DbHelper.COL_INVOICE_LINE_ITEM_lineTotal));
                int issync = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_INVOICE_LINE_ITEM_issync));
                String syncdate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_INVOICE_LINE_ITEM_syncDate));
                String itemname = cursor.getString(cursor.getColumnIndex(DbHelper.COL_INVOICE_LINE_ITEM_itemName));
                System.out.println("@@@@@@@@@@@@@@@ sync Date " + syncdate);
                invoiceLineItems.add(new InvoiceLineItem(
                        batchId, productID, invoiceId, qty, freeQu, price, lineTotal, issync, syncdate, itemname));
            }
        }
        return invoiceLineItems;

    }

    public ArrayList<ReturnInventory> getReturnNoteHeaderData(int merchantid, String date) {
        Cursor cursor = null;
        ArrayList<ReturnInventory> returnInventories = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();

        //String[] date1 = COL_RETURN_INVENTORY_returnDate.split(" ");
        //System.out.println("date1.[0] - "+date1[0]+",2 - "+date1[1]);

        String sql2 = "SELECT * FROM " + TABLE_RETURN_INVENTORY + " WHERE " + COL_RETURN_INVENTORY_merchantId +
                " = " + merchantid;
        /*
         String sql2 = "SELECT * FROM "+TABLE_RETURN_INVENTORY+ " WHERE "+COL_RETURN_INVENTORY_merchantId +
                " = "+merchantid;
         */

        System.out.println("sql2 - " + sql2);

        cursor = sqLiteDatabase.rawQuery(sql2, null);
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {

                int creditnote = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_RETURN_INVENTORY_creditNoteNo));
                String returnDate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_RETURN_INVENTORY_returnDate));
                double totalamount = cursor.getDouble(cursor.getColumnIndex(DbHelper.COL_RETURN_INVENTORY_totalAmount));
                double totalOut = cursor.getDouble(cursor.getColumnIndex(DbHelper.COL_RETURN_INVENTORY_totalOutstanding));
                int mercha = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_RETURN_INVENTORY_merchantId));
                int salesRep = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_RETURN_INVENTORY_SalesRepId));
                int disid = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_RETURN_INVENTORY_distributorId));
                int issync = cursor.getInt(cursor.getColumnIndex(COL_RETURN_INVENTORY_isSync));
                String syncdate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_RETURN_INVENTORY_syncDate));

                int start_r = returnDate.indexOf("(");
                int end_r = returnDate.indexOf(")");
                //String iDate_r = returnDate.substring(start_r+1,end_r);
                long iDate_r = Long.parseLong(returnDate.substring(start_r + 1, end_r));

                String onlyDate = DateManager.getDateAccordingToMilliSeconds(iDate_r, "dd.MM.yyyy");
                String todaytime = DateManager.getDateAccordingToMilliSeconds(today, "dd.MM.yyyy");

                System.out.println("return date  - " + onlyDate);
                System.out.println("today - " + todaytime);

                if (todaytime.equalsIgnoreCase(onlyDate)) {
                    returnInventories.add(new ReturnInventory(creditnote, onlyDate, totalamount, totalOut, mercha, salesRep, disid, issync, syncdate));
                }
            }
        }

        return returnInventories;
    }

    public ArrayList<String> getReturnNoteCreditId_vs_merchant(int merchantid, String date) {
        Cursor cursor = null;
        int creditnote = 0;
        ArrayList<String> value = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();

        String query = "SELECT  * FROM " + TABLE_RETURN_INVENTORY + " WHERE " +
                COL_RETURN_INVENTORY_merchantId + " = " + merchantid;

        cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor.getCount() == 0) {
            System.out.println(" ============= No data related to the " + merchantid);

        } else {
            while (cursor.moveToNext()) {
                int creditNoteNumber = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_RETURN_INVENTORY_creditNoteNo));
                String EnteredDate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_RETURN_INVENTORY_returnDate));

                System.out.println(" ====== Entered Date: " + EnteredDate);
                int start = EnteredDate.indexOf("(");
                int end = EnteredDate.indexOf(")");
                String creteDate = EnteredDate.substring(start + 1, end);
                long createDateMil = Long.parseLong(creteDate);

                String d = DateManager.getDateAccordingToMilliSeconds(createDateMil, "dd.MM.yyyy");

                createDateMil = DateManager.getMilSecAccordingToDate(d);
                System.out.println("+++++++++++++++ Date millll " + DateManager.getDateAccordingToMilliSeconds(createDateMil, "dd.MM.yyyy"));

                if (createDateMil == today) {
                    System.out.println("correct date - " + createDateMil);
                    value.add("" + creditNoteNumber);
                }
                if (createDateMil != today) {
                    System.out.println("wrong dates - " + createDateMil);
                }

            }
        }
        return value;
    }

    public ArrayList<String> getReturnNoteReturnDate_vs_merchant(int merchantid) {
        Cursor cursor = null;

        String returndate = "";
        ArrayList<String> value = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();

        String sql2 = "SELECT " + COL_RETURN_INVENTORY_returnDate + " FROM " + TABLE_RETURN_INVENTORY + " WHERE " + COL_RETURN_INVENTORY_merchantId +
                " = " + merchantid;

        System.out.println("sql2 - " + sql2);

        cursor = sqLiteDatabase.rawQuery(sql2, null);
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {
                value.add(cursor.getString(cursor.getColumnIndex(DbHelper.COL_RETURN_INVENTORY_returnDate)));
            }
        }

        return value;
    }

    public ArrayList<ReturnInventoryLineItem> getReturnNoteItemData(int creditNoteNo) {

        ArrayList<ReturnInventoryLineItem> returnInventoryLineItems = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
        Cursor cursor = null;

        String sql3 = "SELECT * FROM " + TABLE_RETURN_INVENTORY_LINEITEM + " WHERE " +
                COL_RETURN_INVENTORY_LINEITEM_creditNoteNo + " = " + creditNoteNo;

        System.out.println("sql3 - " + sql3);

        cursor = sqLiteDatabase.rawQuery(sql3, null);
        if (cursor.getCount() != 0) {
            while (cursor.moveToNext()) {


                int creditnoteNo = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_RETURN_INVENTORY_LINEITEM_creditNoteNo));
                int productId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_RETURN_INVENTORY_LINEITEM_productId));
                int batchid = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_RETURN_INVENTORY_LINEITEM_batchId));
                int quantity = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_RETURN_INVENTORY_LINEITEM_quantity));
                double totalAmount = cursor.getDouble(cursor.getColumnIndex(DbHelper.COL_RETURN_INVENTORY_LINEITEM_totalAmount));
                int returntype = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_RETURN_INVENTORY_LINEITEM_returnType));
                int issellable = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_RETURN_INVENTORY_LINEITEM_isSellable));
                double discountPrice = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_RETURN_INVENTORY_LINEITEM_discountedPrice));
                String productName = cursor.getString(cursor.getColumnIndex(DbHelper.COL_RETURN_INVENTORY_LINEITEM_productName));
                int issync = cursor.getInt(cursor.getColumnIndex(COL_RETURN_INVENTORY_LINEITEM_isSync));
                double unitPrice = cursor.getDouble(cursor.getColumnIndex(DbHelper.COL_RETURN_INVENTORY_LINEITEM_unitPrice));

                returnInventoryLineItems.add(new ReturnInventoryLineItem(creditnoteNo, productId, batchid,
                        quantity, totalAmount, returntype, issellable, discountPrice, productName, unitPrice, issync));


            }
        }

        return returnInventoryLineItems;
    }


    public ArrayList<SyncTables> getSyncTablesFromSQLite() {

        ArrayList<SyncTables> syncTables = new ArrayList<>();
        Cursor cursor = db.getAllData(DbHelper.TABLE_SYNC);
        if (cursor.getCount() == 0) {
            System.out.println("No Data in " + DbHelper.TABLE_SYNC);
        } else {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SYNC_ID));
                String name = cursor.getString(cursor.getColumnIndex(COL_SYNC_TABLE_NAME));
                String time = cursor.getString(cursor.getColumnIndex(COL_SYNC_TABLE_TIME));
                int rows = cursor.getInt(cursor.getColumnIndex(COL_SYNC_ROWCOUNT));
                int rows_news = cursor.getInt(cursor.getColumnIndex(COL_SYNC_ROWCOUNT_NEW));
                int salesrep = cursor.getInt(cursor.getColumnIndex(COL_SYNC_SALESREPID));
                int status = cursor.getInt(cursor.getColumnIndex(COL_SYNC_STATUS));

                SyncTables st = new SyncTables(id, name, time, rows, rows_news, salesrep, status);
                //System.out.println("st.name - "+st.getName());
                syncTables.add(st);

            }
        }
        return syncTables;
    }

    public void updateSyncTable_lastlogvalue(String table, String lastlog_time, int salesrepid) {


        String sql = "UPDATE " + DbHelper.TABLE_SYNC + " SET \"" +
                COL_SYNC_LASTLOG_TIME + "\" = \"" + lastlog_time + "\" , \"" +
                COL_SYNC_SALESREPID + "\" = " + salesrepid;

        System.out.println("sql update- " + sql);

        SQLiteDatabase sqLiteDatabase = (new DbHelper(context)).getWritableDatabase();
        sqLiteDatabase.execSQL(sql);
    }


    public void updateSyncTable(String table, String time, int rows, int rows_new, int salesrep, int status) {


        String sql = "UPDATE " + DbHelper.TABLE_SYNC + " SET \"" +
                COL_SYNC_TABLE_TIME + "\" = \"" + time + "\" , \"" +
                COL_SYNC_ROWCOUNT + "\" = " + rows + " , \"" +
                COL_SYNC_ROWCOUNT_NEW + "\" = " + rows_new + " , \"" +
                COL_SYNC_SALESREPID + "\" = " + salesrep + " , \"" +
                COL_SYNC_STATUS + "\" = " + status + " WHERE \"" + COL_SYNC_TABLE_NAME + "\" = \"" + table + "\"";

        System.out.println("sql update- " + sql);

        SQLiteDatabase sqLiteDatabase = (new DbHelper(context)).getWritableDatabase();
        sqLiteDatabase.execSQL(sql);
    }

    public void updateAnyTable(String sql) {
        SQLiteDatabase sqLiteDatabase = (new DbHelper(context)).getWritableDatabase();
        sqLiteDatabase.execSQL(sql);
        System.out.println("updated inventory");
    }


    public ArrayList<ReturnInventoryLineItem> getCreditNote(int merchantID) {
        System.out.println("======================================== Return LineItem =======================================");
        System.out.println("================== within getCreditNote merchantID =========== " + merchantID);
        SQLiteDatabase database = db.getReadableDatabase();
        ArrayList<ReturnInventoryLineItem> lineItemArrayList = new ArrayList<>();
        Cursor cursor = null;


        String query = "SELECT  * FROM " + TABLE_RETURN_INVENTORY + " WHERE " +
                COL_RETURN_INVENTORY_merchantId + " = " + merchantID + " AND " + COL_RETURN_INVENTORY_totalOutstanding + " > 0 ";

        cursor = database.rawQuery(query, null);
        if (cursor.getCount() == 0) {
            System.out.println(" ============= No data related to the " + merchantID);

        } else {
            while (cursor.moveToNext()) {
                int creditNoteNumber = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_RETURN_INVENTORY_creditNoteNo));
                String EnteredDate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_RETURN_INVENTORY_returnDate));

                System.out.println(" ====== Entered Date: " + EnteredDate);
                int start = EnteredDate.indexOf("(");
                int end = EnteredDate.indexOf(")");
                String creteDate = EnteredDate.substring(start + 1, end);
                long createDateMil = Long.parseLong(creteDate);

                System.out.println(" ====== Entered Date mil : " + createDateMil);
                String d = DateManager.getDateAccordingToMilliSeconds(createDateMil, "dd.MM.yyyy");
                System.out.println("+++++++++++++++ DAte d " + d);

                createDateMil = DateManager.getMilSecAccordingToDate(d);
                System.out.println("========= Converted MilSec " + createDateMil);
                System.out.println("+++++++++++++++ Date millll " + DateManager.getDateAccordingToMilliSeconds(createDateMil, "dd.MM.yyyy"));


                System.out.println(" ============= getCreditNote ===  creditNoteNumber " + creditNoteNumber);


                if (createDateMil == today) {
                    lineItemArrayList.addAll(getCreditNoteLineItem(creditNoteNumber, 2));
                }

            }
        }

        return lineItemArrayList;
    }

    public ArrayList<ReturnInventoryLineItem> getCreditNoteLineItem(int creditNoteNumber, int id) {
        System.out.println("======================================== Invoice LineItem  method =======================================");
        System.out.println(" ============= Invoice LineItem  method ===  creditNoteNumber " + creditNoteNumber);
        SQLiteDatabase database = db.getReadableDatabase();
        ArrayList<ReturnInventoryLineItem> lineItemArrayList = new ArrayList<>();
        Cursor cursor = null;

        String query = "SELECT " +
                "li." + DbHelper.COL_RETURN_INVENTORY_LINEITEM_productId + "," +
                "li." + DbHelper.COL_RETURN_INVENTORY_LINEITEM_unitPrice + "," +
                "li." + DbHelper.COL_RETURN_INVENTORY_LINEITEM_quantity + "," +
                "li." + DbHelper.COL_RETURN_INVENTORY_LINEITEM_totalAmount + "," +
                "li." + DbHelper.COL_RETURN_INVENTORY_LINEITEM_returnType + "," +
                "p." + COL_PRODUCT_ProductName +
                " FROM " +
                TABLE_RETURN_INVENTORY_LINEITEM + " AS li " +
                "INNER JOIN " + TABLE_PRODUCT + " AS p ON li." + DbHelper.COL_RETURN_INVENTORY_LINEITEM_productId + " = p." + COL_PRODUCT_ProductId + " " +
                "WHERE li." + DbHelper.COL_RETURN_INVENTORY_LINEITEM_creditNoteNo + " = " + creditNoteNumber;

        cursor = database.rawQuery(query, null);

        System.out.println("Cursor Count Line Itenm count " + cursor.getCount());

        if (cursor.getCount() == 0) {
            System.out.println("No Data of " + TABLE_RETURN_INVENTORY_LINEITEM);
        } else {

            while (cursor.moveToNext()) {
                int ProductId = cursor.getInt(0);
                double UnitSellingPrice = cursor.getDouble(1);
                int Quantity = cursor.getInt(2);
                double amount = cursor.getInt(3);
                int returntype = cursor.getInt(4);
                String ProductName = cursor.getString(5);

                if (id == 1) {
                    ReturnInventoryLineItem returnInventoryLineItem = new ReturnInventoryLineItem(ProductId, UnitSellingPrice, Quantity, amount, returntype, ProductName);
                    lineItemArrayList.add(returnInventoryLineItem);
                    System.out.println("Product Name " + ProductName);
                } else {
                    if (Quantity > 0 && UnitSellingPrice > 0) {

                        ReturnInventoryLineItem returnInventoryLineItem = new ReturnInventoryLineItem(ProductId, UnitSellingPrice, Quantity, amount, ProductName);
                        lineItemArrayList.add(returnInventoryLineItem);
                        System.out.println("Product Name " + ProductName);
                    }

                }


            }
        }

        System.out.println("Array list size in return line item : " + lineItemArrayList.size());

        return lineItemArrayList;
    }


    public void GetReturnInventoryByMerchantWithOutStanding(int merchantId) {
        System.out.println("================sync return inventory ======================= ");
        String url = HTTPPaths.seriveUrl + "GetReturnInventoryByMerchantWithOutStanding?merchantId=" + merchantId;
        System.out.println(url);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonObject object = Json.parse(response).asObject();
                        int id = object.get("ID").asInt();
                        System.out.println("================sync return inventory ======================= " + id);
                        if (id == 200) {
                            String newUrl = response.replace("{\"Data\":\"", "{\"Data\": ");
                            String new2Url = newUrl.replace("\",\"ID\":200}", "}");
                            String new3Url = new2Url.replace("\\", "");

                            System.out.println("================sync return inventory ======================= " + id);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj_main = new JSONObject(new3Url);
                                    JSONArray jsonAry_main = jsonObj_main.getJSONArray("Data");
                                    for (int i = 0; i < jsonAry_main.length(); i++) {

                                        JSONObject jsonObj_2 = jsonAry_main.getJSONObject(i);

                                        //GET Return Inventory LINE ITEMS
                                        int CreditNoteNo = jsonObj_2.getInt("CreditNoteNo");
                                        String ReturnDate = jsonObj_2.getString("ReturnDate");
                                        double TotalAmount = jsonObj_2.getDouble("TotalAmount");
                                        double TotalOutstanding = jsonObj_2.getDouble("TotalOutstanding");
                                        int MerchantId = jsonObj_2.getInt("MerchantId");
                                        int SalesRepID = jsonObj_2.getInt("SalesRepId");
                                        int DistributorId = jsonObj_2.getInt("DistributorId");
                                        String SyncDate = jsonObj_2.getString("SyncDate");
                                        String EnteredUser = jsonObj_2.getString("EnteredUser");
                                        String EnteredDate = jsonObj_2.getString("EnteredDate");

                                        ReturnInventory returnInventory = new ReturnInventory(CreditNoteNo,
                                                ReturnDate, TotalAmount, TotalOutstanding,
                                                MerchantId, SalesRepID, DistributorId, 1, SyncDate);


                                        ContentValues cv = returnInventory.getReturnInventoryContentValues();

                                        boolean success = db.insertDataAll(cv, TABLE_RETURN_INVENTORY);
                                        if (!success) {
                                            System.out.println("Not inserted to " + TABLE_RETURN_INVENTORY);
                                        } else {
                                            System.out.println("Inserted to " + TABLE_RETURN_INVENTORY);
                                        }

                                        //Line Items
                                        JSONArray LineItemArray = jsonObj_2.getJSONArray("_ReturnInventoryLineItemList");

                                        for (int j = 0; j < LineItemArray.length(); j++) {

                                            JSONObject lineItems = LineItemArray.getJSONObject(j);
                                            int CreditNoteNol = lineItems.getInt("CreditNoteNo");
                                            int BatchID = lineItems.getInt("BatchId");
                                            int ProductID = lineItems.getInt("ProductId");
                                            int Quantity = lineItems.getInt("Quantity");
                                            double TotalAmountL = lineItems.getDouble("TotalAmount");
                                            int ReturnType = lineItems.getInt("ReturnType");
                                            int IsSellable = lineItems.getInt("IsSellable");
                                            double DiscountedPrice = lineItems.getDouble("DiscountedPrice");
                                            String productName = lineItems.getString("productName");
                                            int IsSyncc = 1;

                                            ReturnInventoryLineItem returnInventoryLineItem = new ReturnInventoryLineItem(CreditNoteNol, ProductID, BatchID, Quantity, TotalAmountL,
                                                    ReturnType, IsSellable, DiscountedPrice, productName, 0, IsSyncc);
                                            ContentValues cv1 = returnInventoryLineItem.getReturnInventoryLineItemCv();
                                            boolean s = db.insertDataAll(cv1, TABLE_RETURN_INVENTORY_LINEITEM);

                                            System.out.println("Credit Note Number line item " + cv1.get("creditNoteNo"));

                                            if (!s) {
                                                System.out.println("Not inserted to " + TABLE_RETURN_INVENTORY_LINEITEM);
                                            } else {
                                                System.out.println("Inserted to " + TABLE_RETURN_INVENTORY_LINEITEM);
                                            }
                                        }
                                    }
                                } catch (JSONException e) {
                                    System.out.println(" @@@@@@@@@@@@ Return Inventory Json Error " + e.getMessage());
                                }
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@ Return Inventory Error" + error.getMessage());
                    }
                });
        MySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(stringRequest);
    }

    public String getLastMerchantID() {
        String syncId = null;
        Cursor cursor = null;
        SQLiteDatabase database = db.getReadableDatabase();
        String selectQuery = "SELECT " + DbHelper.TBL_MERCHANT_SEQUENCE_ID + " FROM " + DbHelper.TABLE_MERCHANT + " ORDER BY " + DbHelper.TBL_MERCHANT_SEQUENCE_ID + " DESC LIMIT 1";
        cursor = database.rawQuery(selectQuery, null);
        if (cursor.getCount() == 0) {
            System.out.println("No Data");
            syncId = "" + 0;
        } else {
            while (cursor.moveToNext()) {
                int SequenceId = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_SEQUENCE_ID));
                SequenceId++;
                syncId = SequenceId + "";
            }
        }

        cursor.close();
        db.close();

        return syncId;
    }

    //Get Merchant Classes From server
    public void getMerchantClass() {

        boolean areaDelete = db.deleteAllData(TABLE_MERCHANT_CLASS);
        if (areaDelete) {
            System.out.println("Successfully deleted " + TABLE_MERCHANT_CLASS);
        } else {
            System.out.println("Error occur deleting " + TABLE_MERCHANT_CLASS);
        }
        String url = HTTPPaths.seriveUrl + "GetDefMerchantClassList";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonObject jsonObject = Json.parse(response).asObject();
                        int id = jsonObject.get("ID").asInt();
                        //Toast.makeText(CreateSalesOrderContinue.this, "ID - " + id, Toast.LENGTH_SHORT).show();
                        if (id == 200) {
                            String url2 = response.replace("{\"Data\":\"", "{\"Data\": ");
                            String url3 = url2.replace("\",\"ID\":200}", "}");
                            String url4 = url3.replace("\\", "");

                            if (response != null) {
                                try {
                                    JSONObject jsonObject_main = new JSONObject(url4);

                                    JSONArray jsonArray_main = jsonObject_main.getJSONArray("Data");

                                    for (int i = 0; i < jsonArray_main.length(); i++) {

                                        JSONObject jsonObject1 = jsonArray_main.getJSONObject(i);

                                        int ClassID = jsonObject1.getInt("ClassID");
                                        String ClassDescription = jsonObject1.getString("ClassDescription");
                                        MerchantClass merchantClass = new MerchantClass(ClassID, ClassDescription);
                                        ContentValues cv = merchantClass.getMerchantClassCV();
                                        boolean success = db.insertDataAll(cv, TABLE_MERCHANT_CLASS);
                                        if (success) {
                                            if (i == jsonArray_main.length() - 1) {
                                                System.out.println("Data is inserted to " + TABLE_MERCHANT_CLASS);
                                            }
                                        } else {
                                            System.out.println("Data is not inserted to " + TABLE_MERCHANT_CLASS);
                                        }
                                    }
                                } catch (Exception e) {
                                    System.out.println("error at MerchantCLass - " + e.toString());
                                }
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(CreateSalesOrderContinue.this, "Volly Error - " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
        getMerchantTypes();
    }

    public ArrayList<MerchantClass> getAllMerchantClass() {
        Cursor cursor = null;
        ArrayList<MerchantClass> merchantClassArrayList = new ArrayList<>();
        cursor = db.getAllData(TABLE_MERCHANT_CLASS);
        if (cursor.getCount() == 0) {
            System.out.println("No Data");
            //syncArea();
        } else {
            while (cursor.moveToNext()) {
                int ClassID = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MERCHANT_CLASS_ClassID));
                String ClassDescription = cursor.getString(cursor.getColumnIndex(DbHelper.COL_MERCHANT_CLASS_ClassDescription));

                MerchantClass merchantClass = new MerchantClass(ClassID, ClassDescription);
                merchantClassArrayList.add(merchantClass);
            }
        }
        System.out.println(" <SyncManager> Merchant Class array size: " + merchantClassArrayList.size());
        return merchantClassArrayList;
    }


//    public void syncMerchantStockData() {
//
//        uploadMerchantStockData();
//    }

    public void uploadMerchantStockData(final int allsync, final int salesrepid) {

        ArrayList<MerchantStock> merchantStocks__ = new ArrayList<>();
        ArrayList<MerchantStockLineitems> merchantStockLineitemses__ = new ArrayList<>();

        JSONObject jsonObject = new JSONObject();
        merchantStocks__ = getAllunsyncMerchantStockData();
        System.out.println("new merchantStocks array size - " + merchantStocks__.size());

        if (merchantStocks__.size() > 0) {

            for (int h = 0; h < merchantStocks__.size(); h++) {
                String d1 = merchantStocks__.get(h).getEnteredDate();
                String d2 = merchantStocks__.get(h).getEnteredDate();
                String d3 = merchantStocks__.get(h).getEnteredDate();

                String syncdate = (new DateManager()).changeDateFormat("yyyy-MM-dd", d1);
                int issync = merchantStocks__.get(h).getIsSync(); //should be put 1
                int status = merchantStocks__.get(h).getStatus();
                String entereduser = merchantStocks__.get(h).getEnteredUser();
                String entereddate = (new DateManager()).changeDateFormat("yyyy-MM-dd", d2);
                String updateduser = merchantStocks__.get(h).getUpdatedUser();
                String updateddate = (new DateManager()).changeDateFormat("yyyy-MM-dd", d3);
                int salesrepid_ = merchantStocks__.get(h).getSalesRepId();
                int distributorid = merchantStocks__.get(h).getDitributorId();
                int merchatid = merchantStocks__.get(h).getMerchantId();
                final int merchantstockid = merchantStocks__.get(h).getMerchentStockId();

                //LINE ITEM SET OBJ ARRAY LIST
                merchantStockLineitemses__ = getAllunsyncMerchantStockLineitemData(merchantstockid);
                System.out.println("merchantStockLineitemses size - " + merchantStockLineitemses__.size());

                MerchantStock stock_header = new MerchantStock(
                        merchantstockid, merchatid, distributorid, salesrepid_, entereddate,
                        entereduser, status, 1, syncdate, updateduser, updateddate, merchantStockLineitemses__);
                Gson gson1 = new Gson();
                final String jsonStringOri = gson1.toJson(stock_header);
                System.out.println("Json merchant stock string" + jsonStringOri);

                try {
                    jsonObject = new JSONObject(jsonStringOri);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String url0 = HTTPPaths.seriveUrl + "SyncMerchantStock";
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url0,
                        jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                System.out.println("response - " + response.toString());

                                try {
                                    int id = response.getInt("ID");
                                    System.out.println("id - " + id);

                                    if (id == 200) {

                                        int k = merchantstockid;
                                        SQLiteDatabase sd = db.getWritableDatabase();

                                        String sql1 = "DELETE FROM " + DbHelper.TABLE_MERCHANT_STOCK + " WHERE " +
                                                DbHelper.COL_MSTOCK_merchant_stock_id + " = " + k;
                                        String sql2 = "DELETE FROM " + DbHelper.TABLE_MERCHANT_STOCK_LINEITEM + " WHERE " +
                                                DbHelper.COL_MS_LINEITEM_merchant_stock_id + " = " + k;

                                        sd.execSQL(sql2);
                                        System.out.println("delete after sending lineitems - sql - " + sql2);

                                        sd.execSQL(sql1);
                                        System.out.println("delete after sending header - sql - " + sql1);

                                        getMerchantStockData(allsync, salesrepid);

                                    } else if (id == 500) {
                                        System.out.println("Error 500");
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                System.out.println("error - " + error.toString());
                            }
                        }) {

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {

                        HashMap<String, String> headers = new HashMap<String, String>();
//                    headers.put("Content-Type", "application/json; charset=utf-8");
                        return headers;
                    }
                };
                MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
            }
        } else {
            System.out.println("No new mstock items to upload ");
            getMerchantStockData(allsync, salesrepid);
        }


    }

    public void getMerchantStockData(final int allsync, final int salesrepid) {

        String wherepart = DbHelper.COL_MSTOCK_is_sync + " = " + 1;
        db.deleteFromAnyTable(TABLE_MERCHANT_STOCK, wherepart);
        String url = HTTPPaths.seriveUrl + "GetMerchantStockListBySalesRepID?salesRepId=" + salesrepid;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JsonObject jsonObject = Json.parse(response).asObject();
                        int id = jsonObject.get("ID").asInt();
                        if (id == 200) {
                            String url2 = response.replace("{\"Data\":\"", "{\"Data\": ");
                            String url3 = url2.replace("\",\"ID\":200}", "}");
                            String url4 = url3.replace("\\", "");

                            if (response != null) {
                                try {
                                    JSONObject jsonObject_main = new JSONObject(url4);
                                    JSONArray jsonArray_main = jsonObject_main.getJSONArray("Data");

                                    System.out.println("jsonArray_main.length - " + jsonArray_main.length());
                                    for (int i = 0; i < jsonArray_main.length(); i++) {

                                        String syncDate = "", EnteredDate = "", UpdatedDate = "";

                                        JSONObject jsonObject1 = jsonArray_main.getJSONObject(i);

                                        String sd = jsonObject1.getString("SyncDate");
                                        String ed = jsonObject1.getString("EnteredDate");
                                        String ud = jsonObject1.getString("UpdatedDate");

                                        //int issync = jsonObject1.getInt("IsSync");
                                        int Status = jsonObject1.getInt("Status");
                                        String EnteredUser = jsonObject1.getString("EnteredUser");
                                        String UpdatedUser = jsonObject1.getString("UpdatedUser");
                                        int SalesRepId = jsonObject1.getInt("SalesRepId");
                                        int DitributorId = jsonObject1.getInt("DitributorId");
                                        int MerchantId = jsonObject1.getInt("MerchantId");
                                        int MerchentStockId = jsonObject1.getInt("MerchentStockId");


                                        System.out.println("sd - " + sd);
                                        System.out.println("ed - " + ed);
                                        System.out.println("ud - " + ud);


                                        if (sd.equals("")) {
                                            syncDate = "";
                                        } else {
                                            syncDate = (new DateManager()).changeDateFormat2("yyyy-MM-dd", sd);
                                        }

                                        if (ed.equals("")) {
                                            EnteredDate = "";
                                        } else {
                                            EnteredDate = (new DateManager()).changeDateFormat2("yyyy-MM-dd", ed);
                                        }

                                        if (ud.equals("")) {
                                            UpdatedDate = "";
                                        } else {
                                            UpdatedDate = (new DateManager()).changeDateFormat2("yyyy-MM-dd", ud);
                                        }

//                                        System.out.println("syncDate- " + syncDate);
//                                        System.out.println("Status- " + Status);
//                                        System.out.println("EnteredUser- " + EnteredUser);
//                                        System.out.println("EnteredDate- " + EnteredDate);
//                                        System.out.println("UpdatedUser- " + UpdatedUser);
//                                        System.out.println("UpdatedDate- " + UpdatedDate);
//                                        System.out.println("SalesRepId- " + SalesRepId);
//                                        System.out.println("DitributorId- " + DitributorId);
//                                        System.out.println("MerchantId- " + MerchantId);
//                                        System.out.println("MerchentStockId- " + MerchentStockId);


                                        JSONArray MerchantStockLineItem_json = jsonObject1.getJSONArray("_MerchantStockLineItem");
                                        for (int j = 0; j < MerchantStockLineItem_json.length(); j++) {

                                            JSONObject jsonObject2 = MerchantStockLineItem_json.getJSONObject(j);
                                            int MerchentStockId_items = jsonObject2.getInt("MerchentStockId");
                                            int ProductID = jsonObject2.getInt("ProductID");
                                            int qty = jsonObject2.getInt("Quantity");
                                            int issync = jsonObject2.getInt("IsSync");
                                            String syncdate = jsonObject2.getString("SyncDate");

                                            String sql = "SELECT " + DbHelper.COL_PRODUCT_ProductName +
                                                    " FROM " + DbHelper.TABLE_PRODUCT +
                                                    " WHERE " + DbHelper.COL_PRODUCT_ProductId + " = " + ProductID;

                                            String productname = (new SyncManager(context)).getStringValueFromSQLite(sql, DbHelper.COL_PRODUCT_ProductName);

                                            MerchantStockLineitems merchantStockLineitems = new MerchantStockLineitems(
                                                    MerchentStockId_items, ProductID, productname, qty, issync, syncdate);

                                            //System.out.println("MerchentStockId_items - " + MerchentStockId_items + ",ProductID - " + ProductID + ",qty - " + qty);
                                            ContentValues contentValues = merchantStockLineitems.setCVvalues_merchantStocklineitems();
                                            boolean success = db.insertDataAll(contentValues, DbHelper.TABLE_MERCHANT_STOCK_LINEITEM);
                                        }

                                        MerchantStock merchantStock = new MerchantStock(MerchentStockId, MerchantId, DitributorId,
                                                SalesRepId, EnteredDate, EnteredUser, Status, 1, syncDate, UpdatedUser, UpdatedDate);

                                        ContentValues contentValues = merchantStock.getCVvalues_merchantStock();
                                        boolean success = db.insertDataAll(contentValues, DbHelper.TABLE_MERCHANT_STOCK);

                                        if (success) {
                                            if (i == jsonArray_main.length() - 1) {
                                                Toast.makeText(context, "Merchant Stock table synced successfully", Toast.LENGTH_SHORT).show();
                                                System.out.println("Insert all merchant stock data ");

                                                time_ = (new DateManager().getDateWithTime());
                                                updateSyncTable(MerchantStock, time_, i + 1, i + 1, salesrepid, 1);
                                                //Toast.makeText(context, "completed", Toast.LENGTH_SHORT).show();
                                                allRowCounts = allRowCounts + i + 1;

                                                if (allsync == 1) {
                                                    getMerchantVisitReasonsData(1, salesrepid);
                                                }

                                            }
                                        } else {
                                            System.out.println("merchant stock data not inserted");
                                        }
                                    }
                                } catch (Exception e) {
                                    System.out.println("error - merchant stock data " + e.toString());
                                }
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Volly Error - " + error.toString());
                    }
                });


        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    public String getMerchantStockId_accordingToMerchnt(int merchant_id) {

        String sql = "SELECT MAX(" + DbHelper.COL_MSTOCK_merchant_stock_id +
                ") FROM " + DbHelper.TABLE_MERCHANT_STOCK +
                " WHERE " + DbHelper.COL_MSTOCK_merchant_id + " = " + merchant_id;

        String mer_stk_id = "";
        System.out.println("sql - " + sql);
        Cursor cursor;

        SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();

        cursor = sqLiteDatabase.rawQuery(sql, null);
        if (cursor.equals(null)) {
            mer_stk_id = "";
        } else if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            if (cursor.getString(cursor.getColumnIndex("MAX(" + DbHelper.COL_MSTOCK_merchant_stock_id + ")")) != null) {

                mer_stk_id = cursor.getString(cursor.getColumnIndex("MAX(" + DbHelper.COL_MSTOCK_merchant_stock_id + ")"));
            }
            System.out.println("!! mer_stk_id - " + mer_stk_id);
        }

        return mer_stk_id;
    }


    public ArrayList<MerchantStock> getAllMerchantStockData() {
        Cursor cursor = null;
        ArrayList<MerchantStock> merchantStocks = new ArrayList<>();
        cursor = db.getAllData(TABLE_MERCHANT_STOCK);
        if (cursor.getCount() == 0) {
            System.out.println("No Data");
        } else {
            while (cursor.moveToNext()) {
                System.out.println("from merchantStockData array");
                int merchantstock_id = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MSTOCK_merchant_stock_id));
                int merchant_id = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MSTOCK_merchant_id));
                int distributor_id = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MSTOCK_distributor_id));
                int salesrep_id = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MSTOCK_salesrep_id));
                String entered_date = cursor.getString(cursor.getColumnIndex(DbHelper.COL_MSTOCK_entered_date));
                String entered_user = cursor.getString(cursor.getColumnIndex(DbHelper.COL_MSTOCK_entered_user));
                int sales_status = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MSTOCK_sales_status));
                int is_sync = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MSTOCK_is_sync));
                String sync_date = cursor.getString(cursor.getColumnIndex(DbHelper.COL_MSTOCK_sync_date));
                String updated_user = cursor.getString(cursor.getColumnIndex(DbHelper.COL_MSTOCK_updated_user));
                String updated_date = cursor.getString(cursor.getColumnIndex(DbHelper.COL_MSTOCK_updated_date));

                System.out.println("entered_date - " + entered_date);
                System.out.println("sync_date - " + sync_date);
                System.out.println("updated_date - " + updated_date);

                merchantStocks.add(new MerchantStock(merchantstock_id, merchant_id,
                        distributor_id, salesrep_id, entered_date, entered_user, sales_status, is_sync,
                        sync_date, updated_user, updated_date));

                System.out.println("merchantStocks.size - " + merchantStocks.size());
            }
        }
        return merchantStocks;
    }

    public ArrayList<Merchant> getAllMerchantStockMerchants_accordingToDate(String stdate, String enddate) {
        Cursor cursor = null;
        ArrayList<Merchant> merchants = new ArrayList<>();
        String sql = "";

        if (!stdate.equalsIgnoreCase("") && enddate.equalsIgnoreCase("")) {

            sql = "SELECT DISTINCT " + TBL_MERCHANT_MERCHANT_ID + "," + DbHelper.TBL_MERCHANT_MERCHANT_NAME
                    + " FROM " + DbHelper.TABLE_MERCHANT + " INNER JOIN " + TABLE_MERCHANT_STOCK + " ON "
                    + TABLE_MERCHANT_STOCK + "." + DbHelper.COL_MSTOCK_merchant_id + " = "
                    + TABLE_MERCHANT + "." + TBL_MERCHANT_MERCHANT_ID + " WHERE " + DbHelper.COL_MSTOCK_entered_date + " > \"" + stdate + "\"";

        } else if (stdate.equalsIgnoreCase("") && !enddate.equalsIgnoreCase("")) {

            sql = "SELECT DISTINCT " + TBL_MERCHANT_MERCHANT_ID + "," + DbHelper.TBL_MERCHANT_MERCHANT_NAME
                    + " FROM " + DbHelper.TABLE_MERCHANT + " INNER JOIN " + TABLE_MERCHANT_STOCK + " ON "
                    + TABLE_MERCHANT_STOCK + "." + DbHelper.COL_MSTOCK_merchant_id + " = "
                    + TABLE_MERCHANT + "." + TBL_MERCHANT_MERCHANT_ID + " WHERE " + DbHelper.COL_MSTOCK_entered_date + " < \"" + enddate + "\"";

        } else if (!stdate.equalsIgnoreCase("") && !enddate.equalsIgnoreCase("")) {

            sql = "SELECT DISTINCT " + TBL_MERCHANT_MERCHANT_ID + "," + DbHelper.TBL_MERCHANT_MERCHANT_NAME
                    + " FROM " + DbHelper.TABLE_MERCHANT + " INNER JOIN " + TABLE_MERCHANT_STOCK + " ON "
                    + TABLE_MERCHANT_STOCK + "." + DbHelper.COL_MSTOCK_merchant_id + " = "
                    + TABLE_MERCHANT + "." + TBL_MERCHANT_MERCHANT_ID + " WHERE " + DbHelper.COL_MSTOCK_entered_date + " BETWEEN \"" + stdate + "\" AND \"" + enddate + "\"";
        }

        System.out.println("sql - " + sql);

        SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
        cursor = sqLiteDatabase.rawQuery(sql, null);

        if (cursor.getCount() == 0) {
            System.out.println("No Data");
        } else {
            while (cursor.moveToNext()) {

                String merchant_id = cursor.getString(cursor.getColumnIndex(TBL_MERCHANT_MERCHANT_ID));
                String merchant_name = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_MERCHANT_NAME));

                merchants.add(new Merchant(merchant_id, merchant_name));
                System.out.println("merchants.size - " + merchants.size());
            }
        }
        return merchants;
    }

    public ArrayList<MerchantStock> getAllMerchantStockData_accordingTo_DateAndMerchant(String stdate, String enddate, String merchantId) {
        Cursor cursor = null;
        ArrayList<MerchantStock> merchantStocks = new ArrayList<>();
        String sql = "";

        System.out.println("stdate - " + stdate);
        System.out.println("enddate - " + enddate);
        System.out.println("merchantId - " + merchantId);

        if (merchantId.equalsIgnoreCase("")) {

            if (!stdate.equalsIgnoreCase("") && enddate.equalsIgnoreCase("")) {

                sql = "SELECT * FROM " + DbHelper.TABLE_MERCHANT_STOCK +
                        " WHERE " + DbHelper.COL_MSTOCK_entered_date + " > \"" + stdate + "\"";

            } else if (stdate.equalsIgnoreCase("") && !enddate.equalsIgnoreCase("")) {

                sql = "SELECT * FROM " + DbHelper.TABLE_MERCHANT_STOCK +
                        " WHERE " + DbHelper.COL_MSTOCK_entered_date + " < \"" + enddate + "\"";

            } else if (!stdate.equalsIgnoreCase("") && !enddate.equalsIgnoreCase("")) {

                sql = "SELECT * FROM " + DbHelper.TABLE_MERCHANT_STOCK +
                        " WHERE " + DbHelper.COL_MSTOCK_entered_date + " BETWEEN \"" + stdate + "\" AND \"" + enddate + "\"";

            } else if (stdate.equalsIgnoreCase("") && enddate.equalsIgnoreCase("")) {

                sql = "SELECT * FROM " + DbHelper.TABLE_MERCHANT_STOCK;

            }

        }
        if (!merchantId.equalsIgnoreCase("")) {
            if (!stdate.equalsIgnoreCase("") && enddate.equalsIgnoreCase("")) {

                sql = "SELECT * FROM " + DbHelper.TABLE_MERCHANT_STOCK +
                        " WHERE " + DbHelper.COL_MSTOCK_entered_date + " > \"" + stdate + "\" AND "
                        + DbHelper.COL_MSTOCK_merchant_id + " = " + merchantId;

            } else if (stdate.equalsIgnoreCase("") && !enddate.equalsIgnoreCase("")) {

                sql = "SELECT * FROM " + DbHelper.TABLE_MERCHANT_STOCK +
                        " WHERE " + DbHelper.COL_MSTOCK_entered_date + " < \"" + enddate + "\"  AND "
                        + DbHelper.COL_MSTOCK_merchant_id + " = " + merchantId;

            } else if (!stdate.equalsIgnoreCase("") && !enddate.equalsIgnoreCase("")) {

                sql = "SELECT * FROM " + DbHelper.TABLE_MERCHANT_STOCK +
                        " WHERE " + DbHelper.COL_MSTOCK_entered_date + " BETWEEN \"" + stdate + "\" AND \"" + enddate + "\"  AND "
                        + DbHelper.COL_MSTOCK_merchant_id + " = " + merchantId;

            } else if (stdate.equalsIgnoreCase("") && enddate.equalsIgnoreCase("")) {

                sql = "SELECT * FROM " + DbHelper.TABLE_MERCHANT_STOCK +
                        " WHERE " + DbHelper.COL_MSTOCK_merchant_id + " = " + merchantId;

            }
        }

        System.out.println("sql - " + sql);

        SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
        cursor = sqLiteDatabase.rawQuery(sql, null);

        if (cursor.getCount() == 0) {
            System.out.println("No Data");
        } else {
            while (cursor.moveToNext()) {

                int merchantstock_id = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MSTOCK_merchant_stock_id));
                int merchant_id = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MSTOCK_merchant_id));
                int distributor_id = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MSTOCK_distributor_id));
                int salesrep_id = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MSTOCK_salesrep_id));
                String entered_date = cursor.getString(cursor.getColumnIndex(DbHelper.COL_MSTOCK_entered_date));

                String entered_user = cursor.getString(cursor.getColumnIndex(DbHelper.COL_MSTOCK_entered_user));
                int sales_status = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MSTOCK_sales_status));
                int is_sync = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MSTOCK_is_sync));
                String sync_date = cursor.getString(cursor.getColumnIndex(DbHelper.COL_MSTOCK_sync_date));
                String updated_user = cursor.getString(cursor.getColumnIndex(DbHelper.COL_MSTOCK_updated_user));
                String updated_date = cursor.getString(cursor.getColumnIndex(DbHelper.COL_MSTOCK_updated_date));

                System.out.println("entered_date ----- " + entered_date);
                merchantStocks.add(new MerchantStock(merchantstock_id, merchant_id,
                        distributor_id, salesrep_id, entered_date, entered_user, sales_status, is_sync,
                        sync_date, updated_user, updated_date));

                System.out.println("merchantStocks.size - " + merchantStocks.size());
            }
        }
        return merchantStocks;
    }

    public ArrayList<MerchantStockLineitems> getAllMerchantStockLineItemData(int merchantStockId) {
        Cursor cursor = null;
        ArrayList<MerchantStockLineitems> merchantStockLineitemses = new ArrayList<>();

        String sql = "SELECT DISTINCT * FROM " + DbHelper.TABLE_MERCHANT_STOCK_LINEITEM + " WHERE " + DbHelper.COL_MS_LINEITEM_merchant_stock_id + " = " + merchantStockId;

        SQLiteDatabase database = db.getReadableDatabase();

        cursor = database.rawQuery(sql, null);
        if (cursor.getCount() == 0) {
            System.out.println("No data");
        } else {
            while (cursor.moveToNext()) {
                int merStockId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MS_LINEITEM_merchant_stock_id));
                int productid = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MS_LINEITEM_product_id));
                String productname = cursor.getString(cursor.getColumnIndex(DbHelper.COL_MS_LINEITEM_product_name));
                int stock = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MS_LINEITEM_quantity));
                int issync = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MS_LINEITEM_is_sync));
                String syncdate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_MS_LINEITEM_sync_date));

                MerchantStockLineitems merchantStockLineitems = new MerchantStockLineitems(merStockId, productid, productname, stock, issync, syncdate);
                merchantStockLineitemses.add(merchantStockLineitems);
            }
        }
        return merchantStockLineitemses;
    }


    public ArrayList<MerchantStock> getAllunsyncMerchantStockData() {
        Cursor cursor = null;
        ArrayList<MerchantStock> merchantStocks = new ArrayList<>();

        String sql = "SELECT * FROM " + DbHelper.TABLE_MERCHANT_STOCK +
                " WHERE " + DbHelper.COL_MSTOCK_is_sync + " = " + 0;

        SQLiteDatabase readableDatabase = db.getReadableDatabase();
        cursor = readableDatabase.rawQuery(sql, null);

        if (cursor.getCount() == 0) {
            System.out.println("No Data merchant stock items");
        } else {
            while (cursor.moveToNext()) {

                int merchantstock_id = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MSTOCK_merchant_stock_id));
                int merchant_id = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MSTOCK_merchant_id));
                int distributor_id = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MSTOCK_distributor_id));
                int salesrep_id = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MSTOCK_salesrep_id));
                String entered_date = cursor.getString(cursor.getColumnIndex(DbHelper.COL_MSTOCK_entered_date));
                String entered_user = cursor.getString(cursor.getColumnIndex(DbHelper.COL_MSTOCK_entered_user));
                int sales_status = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MSTOCK_sales_status));
                int is_sync = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MSTOCK_is_sync));
                String sync_date = cursor.getString(cursor.getColumnIndex(DbHelper.COL_MSTOCK_sync_date));
                String updated_user = cursor.getString(cursor.getColumnIndex(DbHelper.COL_MSTOCK_updated_user));
                String updated_date = cursor.getString(cursor.getColumnIndex(DbHelper.COL_MSTOCK_updated_date));

                merchantStocks.add(new MerchantStock(merchantstock_id, merchant_id,
                        distributor_id, salesrep_id, entered_date, entered_user, sales_status, is_sync,
                        sync_date, updated_user, updated_date));

                System.out.println("merchantStocks.size - " + merchantStocks.size());
            }
        }
        return merchantStocks;
    }

    public ArrayList<MerchantStockLineitems> getAllunsyncMerchantStockLineitemData(int id) {
        Cursor cursor = null;
        ArrayList<MerchantStockLineitems> merchantStockLineitemses = new ArrayList<>();

        String sql = "SELECT * FROM " + DbHelper.TABLE_MERCHANT_STOCK_LINEITEM +
                " WHERE " + DbHelper.COL_MS_LINEITEM_merchant_stock_id + " = " + id;

        System.out.println("sql - " + sql);
        SQLiteDatabase readableDatabase = db.getReadableDatabase();
        cursor = readableDatabase.rawQuery(sql, null);

        if (cursor.getCount() == 0) {
            System.out.println("No Data ms line items");
        } else {
            while (cursor.moveToNext()) {

                int merStockId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MS_LINEITEM_merchant_stock_id));
                int productid = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MS_LINEITEM_product_id));
                String productname = cursor.getString(cursor.getColumnIndex(DbHelper.COL_MS_LINEITEM_product_name));
                int stock = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MS_LINEITEM_quantity));
                int issync = 1;//cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MS_LINEITEM_is_sync));
                String d1 = cursor.getString(cursor.getColumnIndex(DbHelper.COL_MS_LINEITEM_sync_date));

                // "\/Date(1508231013040)\/"
                //2017-11-14
                //String myDate = "2014/10/29 18:10:45";
                //String format = "yyyy/MM/dd HH:mm:ss"
                //String syncdate = "/Date(" + (new DateManager()).anyStringDatetoMills(d1, "yyyy-MM-dd") + ")/";


                MerchantStockLineitems merchantStockLineitems = new MerchantStockLineitems(merStockId, productid, productname, stock, issync, d1);
                merchantStockLineitemses.add(merchantStockLineitems);
            }
        }
        return merchantStockLineitemses;
    }


    //Get Merchant Classes From server
    public void getMerchantTypes() {

        boolean areaDelete = db.deleteAllData(TABLE_MERCHANT_TYPE);
        if (areaDelete) {
            System.out.println("Successfully deleted " + TABLE_MERCHANT_TYPE);
        } else {
            System.out.println("Error occur deleting " + TABLE_MERCHANT_TYPE);
        }
        String url = HTTPPaths.seriveUrl + "GetDefMerchantTypeList";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonObject jsonObject = Json.parse(response).asObject();
                        int id = jsonObject.get("ID").asInt();
                        if (id == 200) {
                            String url2 = response.replace("{\"Data\":\"", "{\"Data\": ");
                            String url3 = url2.replace("\",\"ID\":200}", "}");
                            String url4 = url3.replace("\\", "");

                            if (response != null) {
                                try {
                                    JSONObject jsonObject_main = new JSONObject(url4);

                                    JSONArray jsonArray_main = jsonObject_main.getJSONArray("Data");

                                    for (int i = 0; i < jsonArray_main.length(); i++) {

                                        JSONObject jsonObject1 = jsonArray_main.getJSONObject(i);

                                        String TypeCode = jsonObject1.getString("TypeCode");
                                        String TypeDescription = jsonObject1.getString("TypeDescription");

                                        MerchantType merchantType = new MerchantType(TypeCode, TypeDescription);
                                        ContentValues cv = merchantType.getMerchantTypeCV();
                                        boolean success = db.insertDataAll(cv, TABLE_MERCHANT_TYPE);
                                        if (success) {
                                            if (i == jsonArray_main.length() - 1) {
                                                System.out.println("Data is inserted to " + TABLE_MERCHANT_TYPE);
                                            }
                                        } else {
                                            System.out.println("Data is not inserted to " + TABLE_MERCHANT_TYPE);
                                        }

                                    }
                                } catch (Exception e) {
                                    System.out.println("error at MerchantType - " + e.toString());
                                }
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(CreateSalesOrderContinue.this, "Volly Error - " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }

    public ArrayList<MerchantType> getAllMerchantTypes() {
        Cursor cursor = null;
        ArrayList<MerchantType> merchantTypeArrayList = new ArrayList<>();
        cursor = db.getAllData(TABLE_MERCHANT_TYPE);
        if (cursor.getCount() == 0) {
            System.out.println("No Data in " + TABLE_MERCHANT_TYPE);
            //syncArea();
        } else {
            while (cursor.moveToNext()) {
                String TypeCode = cursor.getString(cursor.getColumnIndex(DbHelper.COL_MERCHANT_TYPE_TypeCode));
                String TypeDescription = cursor.getString(cursor.getColumnIndex(DbHelper.COL_MERCHANT_TYPE_TypeDescription));

                MerchantType merchantType = new MerchantType(TypeCode, TypeDescription);
                merchantTypeArrayList.add(merchantType);
            }
        }
        System.out.println(" <SyncManager> Merchant Type array size: " + merchantTypeArrayList.size());
        return merchantTypeArrayList;
    }

    public double getPreviousReturnAmount(int invoiceId) {
        double previousReturnAmount = 0;
        SQLiteDatabase database = db.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_INVOICE_UTILIZATION + " WHERE " + COL_TABLE_INVOICE_UTILIZATION_InvoiceId + " = " + invoiceId;
        Cursor cursor = database.rawQuery(query, null);

        while (cursor.moveToNext()) {
            int in = cursor.getInt(cursor.getColumnIndex(COL_TABLE_INVOICE_UTILIZATION_InvoiceId));
            System.out.println("Invoice id " + in);
            previousReturnAmount += cursor.getDouble(cursor.getColumnIndex(COL_TABLE_INVOICE_UTILIZATION_UtilizedAmount));
            //String.format("Rs. %.2f",cursor.getDouble(cursor.getColumnIndex(COL_TABLE_INVOICE_UTILIZATION_UtilizedAmount)));
        }

        System.out.println("Previous Return Amount " + previousReturnAmount);

        return previousReturnAmount;
    }

    public String getSalesOrders() {
        Cursor cursor = null;
        //long today = DateManager.todayMillsecWithDateFormat("dd.MM.yyyy");
        SQLiteDatabase database = db.getReadableDatabase();
        String deliveryLocationQuery = "SELECT * FROM " + TABLE_SALES_ORDER + " WHERE " + DbHelper.COL_SALES_ORDER_SaleStatus + " = 2";
        // String deliveryLocationQuery = "SELECT  * FROM " + TABLE_SALES_ORDER;
        cursor = database.rawQuery(deliveryLocationQuery, null);
        return "" + cursor.getCount();
    }


    //Get ReturnNotes
    public ArrayList<ReturnInventory> getReturnNoteListByMerchantAndDate(int merchantId, long startDate, long endDate) {

        ArrayList<ReturnInventory> returnInventoryArrayList = new ArrayList<>();
        Cursor cursor = null;
        //long today = DateManager.todayMillsecWithDateFormat("dd.MM.yyyy");
        SQLiteDatabase database = db.getReadableDatabase();
        //String deliveryLocationQuery = "SELECT * FROM " + DbHelper.TABLE_SALES_ORDER + " WHERE " + DbHelper.COL_SALES_ORDER_MerchantId + " = " + merchantId +" AND "+ DbHelper.COL_SALES_ORDER_SaleStatus + " = 1";
        String query = "SELECT  * FROM " + TABLE_RETURN_INVENTORY + " WHERE " + DbHelper.COL_RETURN_INVENTORY_merchantId + " = " + merchantId + " ORDER BY " + COL_RETURN_INVENTORY_LINEITEM_creditNoteNo;
        cursor = database.rawQuery(query, null);
        if (cursor.getCount() == 0) {
            System.out.println("No Data ");

        } else {
            while (cursor.moveToNext()) {
                int CreditNoteNo = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_RETURN_INVENTORY_creditNoteNo));
                String ReturnDate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_RETURN_INVENTORY_returnDate));
                double TotalAmount = cursor.getDouble(cursor.getColumnIndex(DbHelper.COL_RETURN_INVENTORY_totalAmount));
                double TotalOutstanding = cursor.getDouble(cursor.getColumnIndex(DbHelper.COL_RETURN_INVENTORY_totalOutstanding));
                int MerchantId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_RETURN_INVENTORY_merchantId));
                int SalesRepId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_RETURN_INVENTORY_SalesRepId));
                int DistributorId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_RETURN_INVENTORY_distributorId));
                String SyncDate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_RETURN_INVENTORY_syncDate));
                int IsSync = cursor.getInt(cursor.getColumnIndex(COL_RETURN_INVENTORY_isSync));

                System.out.println(" ====== Entered Date: " + ReturnDate);
                int start = ReturnDate.indexOf("(");
                int end = ReturnDate.indexOf(")");
                String creteDate = ReturnDate.substring(start + 1, end);
                long createDateMil = Long.parseLong(creteDate);

                System.out.println(" ====== Entered Date mil : " + createDateMil);
                String d = DateManager.getDateAccordingToMilliSeconds(createDateMil, "dd.MM.yyyy");
                System.out.println("+++++++++++++++ Date d " + d);

                createDateMil = DateManager.getMilSecAccordingToDate(d);
                System.out.println("========= Converted MilSec " + createDateMil);
                System.out.println("+++++++++++++++ Date millll " + DateManager.getDateAccordingToMilliSeconds(createDateMil, "dd.MM.yyyy"));


                if (createDateMil >= startDate && createDateMil <= endDate) {
                    ReturnInventory returnInventory = new ReturnInventory(CreditNoteNo,
                            ReturnDate, TotalAmount, TotalOutstanding,
                            MerchantId, SalesRepId, DistributorId, SyncDate, IsSync, null, "", null, null, null, null);
                    returnInventoryArrayList.add(returnInventory);
                }


            }
        }


        System.out.printf("========= array size in return today list %d%n", returnInventoryArrayList.size());

        return returnInventoryArrayList;
    }

    public ArrayList<ReturnInventory> getReturnNoteListBySalesStatus() {

        ArrayList<ReturnInventory> returnInventoryArrayList = new ArrayList<>();
        Cursor cursor = null;
        //long today = DateManager.todayMillsecWithDateFormat("dd.MM.yyyy");
        SQLiteDatabase database = db.getReadableDatabase();
        //String deliveryLocationQuery = "SELECT * FROM " + DbHelper.TABLE_SALES_ORDER + " WHERE " + DbHelper.COL_SALES_ORDER_MerchantId + " = " + merchantId +" AND "+ DbHelper.COL_SALES_ORDER_SaleStatus + " = 1";
        String query = "SELECT  * FROM " + TABLE_RETURN_INVENTORY + " ORDER BY " + COL_RETURN_INVENTORY_LINEITEM_creditNoteNo;
        cursor = database.rawQuery(query, null);
        if (cursor.getCount() == 0) {
            System.out.println("No Data");
        } else {
            while (cursor.moveToNext()) {
                int CreditNoteNo = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_RETURN_INVENTORY_creditNoteNo));
                String ReturnDate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_RETURN_INVENTORY_returnDate));
                double TotalAmount = cursor.getDouble(cursor.getColumnIndex(DbHelper.COL_RETURN_INVENTORY_totalAmount));
                double TotalOutstanding = cursor.getDouble(cursor.getColumnIndex(DbHelper.COL_RETURN_INVENTORY_totalOutstanding));
                int MerchantId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_RETURN_INVENTORY_merchantId));
                int SalesRepId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_RETURN_INVENTORY_SalesRepId));
                int DistributorId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_RETURN_INVENTORY_distributorId));
                String SyncDate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_RETURN_INVENTORY_syncDate));
                int IsSync = cursor.getInt(cursor.getColumnIndex(COL_RETURN_INVENTORY_isSync));

                System.out.println(" ====== Entered Date: " + ReturnDate);
                int start = ReturnDate.indexOf("(");
                int end = ReturnDate.indexOf(")");
                String creteDate = ReturnDate.substring(start + 1, end);
                long createDateMil = Long.parseLong(creteDate);

                System.out.println(" ====== Entered Date mil : " + createDateMil);
                String d = DateManager.getDateAccordingToMilliSeconds(createDateMil, "dd.MM.yyyy");
                System.out.println("+++++++++++++++ Date d " + d);

                createDateMil = DateManager.getMilSecAccordingToDate(d);
                System.out.println("========= Converted MilSec " + createDateMil);
                System.out.println("+++++++++++++++ Date millll " + DateManager.getDateAccordingToMilliSeconds(createDateMil, "dd.MM.yyyy"));

                if (createDateMil == today) {
                    ReturnInventory returnInventory = new ReturnInventory(CreditNoteNo,
                            ReturnDate, TotalAmount, TotalOutstanding,
                            MerchantId, SalesRepId, DistributorId, SyncDate, IsSync, null, "", null, null, null, null);
                    returnInventoryArrayList.add(returnInventory);
                }
            }
        }


        System.out.printf("========= array size in return today list %d%n", returnInventoryArrayList.size());

        return returnInventoryArrayList;
    }

    public void getServicesCounts(int salesrepid, String date) {
        boolean serviceCountDelete = db.deleteAllData(TABLE_SERVICES_COUNTS);
        if (serviceCountDelete) {
            System.out.println("Successfully deleted " + TABLE_SERVICES_COUNTS);
        } else {
            System.out.println("Error occur deleting " + TABLE_SERVICES_COUNTS);
        }

        //2017-09-22
        String url = HTTPPaths.seriveUrl + "GetSyncCount?salesRepId=" + salesrepid + "&requestDate=" + date;
        System.out.println("url - " + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonObject jsonObject = Json.parse(response).asObject();
                        int id = jsonObject.get("ID").asInt();
                        if (id == 200) {
                            String url2 = response.replace("{\"Data\":\"", "{\"Data\": ");
                            String url3 = url2.replace("\",\"ID\":200}", "}");
                            String url4 = url3.replace("\\", "");

                            if (response != null) {
                                try {
                                    JSONObject jsonObject_main = new JSONObject(url4);

                                    JSONObject jsonObject1 = jsonObject_main.getJSONObject("Data");
                                    int SalesrepID = jsonObject1.getInt("SalesRepId");
                                    String RequestDate = jsonObject1.getString("RequestDate");
                                    String updatedTime = "";
                                    int DistrictCount = jsonObject1.getInt("DistrictCount");
                                    int ProductCategoryCount = jsonObject1.getInt("ProductCategoryCount");
                                    int ProductCount = jsonObject1.getInt("ProductCount");
                                    int ProductBatchCount = jsonObject1.getInt("ProductBatchCount");
                                    int ReturnTypeCount = jsonObject1.getInt("ReturnTypeCount");
                                    int PathCount = jsonObject1.getInt("PathCount");
                                    int MerchantCount = jsonObject1.getInt("MerchantCount");
                                    int SalesOrderCount = jsonObject1.getInt("SalesOrderCount");
                                    int VehicleInventoryCount = jsonObject1.getInt("VehicleInventoryCount");
                                    int SalesRepInventoryCount = jsonObject1.getInt("SalesRepInventoryCount");
                                    int InvoiceCount = jsonObject1.getInt("InvoiceCount");
                                    int merchantstockCount = jsonObject1.getInt("MerchantStockCount");
                                    int MerchantVisitReasonCount = jsonObject1.getInt("MerchantVisitReasonCount");
                                    int MerchantVisitDetailsCount = jsonObject1.getInt("MerchantVisitDetailsCount");
                                    int PrimarySalesInvoiceCount = jsonObject1.getInt("PrimarySalesInvoiceCount");
                                    int SalesRepTargetCount = jsonObject1.getInt("SalesRepTargetCount");
                                    int DefTargetCategoryCount = jsonObject1.getInt("DefTargetCategoryCount");

                                    //PrimarySalesInvoiceCount":3,"SalesRepTargetCount":7,"DefTargetCategoryCount":7


                                    ServiceCount serviceCount = new ServiceCount(SalesrepID, RequestDate, updatedTime, DistrictCount,
                                            ProductCategoryCount, ProductCount, ProductBatchCount, ReturnTypeCount, PathCount,
                                            MerchantCount, SalesOrderCount, VehicleInventoryCount, SalesRepInventoryCount, InvoiceCount,
                                            merchantstockCount, MerchantVisitReasonCount, MerchantVisitDetailsCount,
                                            PrimarySalesInvoiceCount, SalesRepTargetCount, DefTargetCategoryCount);

                                    ContentValues cv = serviceCount.getCVServiceCountsValues();
                                    boolean success = db.insertDataAll(cv, TABLE_SERVICES_COUNTS);
                                    if (success) {
                                        System.out.println("Data is inserted to " + TABLE_SERVICES_COUNTS);
                                    } else {
                                        System.out.println("Data is not inserted to " + TABLE_SERVICES_COUNTS);
                                    }
                                } catch (Exception e) {
                                    System.out.println("error at ServiceCounts table - " + e.toString());
                                }
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(CreateSalesOrderContinue.this, "Volly Error - " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }

    public ArrayList<ServiceCount> getServiceCountsDataFromSQLite() {
        Cursor cursor = null;
        ArrayList<ServiceCount> serviceCounts_ = new ArrayList<>();
        cursor = db.getAllData(TABLE_SERVICES_COUNTS);
        if (cursor.getCount() == 0) {
            System.out.println("No Data");
            //serviceCounts_ = null;
        } else {
            while (cursor.moveToNext()) {

                int salesrepid = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SER_COUNT_SALESREP_ID));
                String updatedDate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SER_COUNT_ACCORDINTO_DATE));
                String timeDate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SER_COUNT_UPDATED_DATE_TIME));
                int district = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SER_COUNT_TBL_DISTRICT));
                int productCat = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SER_COUNT_TBL_PRODUCT_CAT));
                int product = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SER_COUNT_TBL_PRODUCT));
                int productBatch = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SER_COUNT_TBL_PRODUCT_BATCH));
                int returnType = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SER_COUNT_TBL_RETURN_TYPE));
                int path = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SER_COUNT_TBL_PATH));
                int merchant = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SER_COUNT_TBL_MERCHANT));
                int salesorder = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SER_COUNT_TBL_SALES_ORDER));
                int vehicleinven = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SER_COUNT_TBL_VEHICLE_INVEN));
                int salesrepinven = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SER_COUNT_TBL_SALESREP_INVEN));
                int invoice = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SER_COUNT_TBL_INVOICE));
                int merStock = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SER_COUNT_TBL_MERCHANT_STOCK));
                int merReason = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SER_COUNT_TBL_MERCHANT_REASON));
                int merVisitcount = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SER_COUNT_TBL_MERCHANT_VISIT_COUNTS));
                int merPrimaryInvoice = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SER_COUNT_TBL_PRIMARY_INVOICE_COUNT));
                int merTarget = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SER_COUNT_TBL_TARGET_COUNT));
                int merTargetCategory = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SER_COUNT_TBL_TARGET_CATEGORY_COUNT));

                serviceCounts_.add(new ServiceCount(salesrepid, updatedDate, timeDate, district, productCat,
                        product, productBatch, returnType, path, merchant, salesorder,
                        vehicleinven, salesrepinven, invoice, merStock, merReason, merVisitcount,
                        merPrimaryInvoice, merTarget, merTargetCategory));
            }
        }
        System.out.println("serviceCounts_ Class array size: " + serviceCounts_.size());
        return serviceCounts_;
    }


    public void getMerchantVisitReasonsData(final int allsync, final int salesrepid) {

        boolean merchantvisit_reason = db.deleteAllData(TABLE_MVISIT_REASON);
        if (merchantvisit_reason) {
            System.out.println("Successfully deleted " + TABLE_MVISIT_REASON);
        } else {
            System.out.println("Error occur deleting " + TABLE_MVISIT_REASON);
        }

        String url = HTTPPaths.seriveUrl + "GetDefMerchantVisitReasonList";
        System.out.println("url - " + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonObject jsonObject = Json.parse(response).asObject();
                        int id = jsonObject.get("ID").asInt();
                        if (id == 200) {
                            String url2 = response.replace("{\"Data\":\"", "{\"Data\": ");
                            String url3 = url2.replace("\",\"ID\":200}", "}");
                            String url4 = url3.replace("\\", "");

                            if (response != null) {
                                try {
                                    JSONObject jsonObject_main = new JSONObject(url4);
                                    JSONArray jsonArray = jsonObject_main.getJSONArray("Data");

                                    for (int t = 0; t < jsonArray.length(); t++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(t);
                                        int ReasonId = jsonObject1.getInt("ReasonId");
                                        String Description = jsonObject1.getString("Description");
                                        int AllowStock = jsonObject1.getInt("AllowStock");

                                        ContentValues cv = (new MerchantVisitReason(ReasonId, Description, AllowStock)).getCVvaluesMerchantVisitReason();
                                        boolean success = db.insertDataAll(cv, TABLE_MVISIT_REASON);
                                        if (success) {
                                            if (t == jsonArray.length() - 1) {
                                                System.out.println("Data is inserted to " + TABLE_MVISIT_REASON);

                                                time_ = (new DateManager().getDateWithTime());
                                                updateSyncTable(MerchantStock, time_, t + 1, t + 1, salesrepid, 1);
                                                //Toast.makeText(context, "completed", Toast.LENGTH_SHORT).show();
                                                allRowCounts = allRowCounts + t + 1;

                                                if (allsync == 1) {
                                                    syncMerchantVisitData(1, salesrepid);
                                                }
                                            }
                                        } else {
                                            System.out.println("Data is not inserted to " + TABLE_MVISIT_REASON);
                                        }
                                    }


                                } catch (Exception e) {
                                    System.out.println("error at merchant visit reason table - " + e.toString());
                                }
                            }
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }


    public ArrayList<MerchantVisit> getAllMerchantVisitData_accordingTo_DateAndMerchant(String stdate, String enddate, String merchantId) {
        Cursor cursor = null;
        ArrayList<MerchantVisit> merchantVisits = new ArrayList<>();
        String sql = "";

        System.out.println("stdate - " + stdate);
        System.out.println("enddate - " + enddate);
        System.out.println("merchantId - " + merchantId);

        if (merchantId.equalsIgnoreCase("")) {

            if (!stdate.equalsIgnoreCase("") && enddate.equalsIgnoreCase("")) {

                sql = "SELECT * FROM " + DbHelper.TABLE_MVISIT +
                        " WHERE " + DbHelper.COL_MVISIT_ENTERED_DATE + " > \"" + stdate + "\"";

            } else if (stdate.equalsIgnoreCase("") && !enddate.equalsIgnoreCase("")) {

                sql = "SELECT * FROM " + DbHelper.TABLE_MVISIT +
                        " WHERE " + DbHelper.COL_MVISIT_ENTERED_DATE + " < \"" + enddate + "\"";

            } else if (!stdate.equalsIgnoreCase("") && !enddate.equalsIgnoreCase("")) {

                sql = "SELECT * FROM " + DbHelper.TABLE_MVISIT +
                        " WHERE " + DbHelper.COL_MVISIT_ENTERED_DATE + " BETWEEN \"" + stdate + "\" AND \"" + enddate + "\"";

            } else if (stdate.equalsIgnoreCase("") && enddate.equalsIgnoreCase("")) {

                sql = "SELECT * FROM " + DbHelper.TABLE_MVISIT;

            }

        }
        if (!merchantId.equalsIgnoreCase("")) {
            if (!stdate.equalsIgnoreCase("") && enddate.equalsIgnoreCase("")) {

                sql = "SELECT * FROM " + DbHelper.TABLE_MVISIT +
                        " WHERE " + DbHelper.COL_MVISIT_ENTERED_DATE + " > \"" + stdate + "\" AND "
                        + DbHelper.COL_MVISIT_MERCHANT_ID + " = " + merchantId;

            } else if (stdate.equalsIgnoreCase("") && !enddate.equalsIgnoreCase("")) {

                sql = "SELECT * FROM " + DbHelper.TABLE_MVISIT +
                        " WHERE " + DbHelper.COL_MVISIT_ENTERED_DATE + " < \"" + enddate + "\"  AND "
                        + DbHelper.COL_MVISIT_MERCHANT_ID + " = " + merchantId;

            } else if (!stdate.equalsIgnoreCase("") && !enddate.equalsIgnoreCase("")) {

                sql = "SELECT * FROM " + DbHelper.TABLE_MVISIT +
                        " WHERE " + DbHelper.COL_MVISIT_ENTERED_DATE + " BETWEEN \"" + stdate + "\" AND \"" + enddate + "\"  AND "
                        + DbHelper.COL_MVISIT_MERCHANT_ID + " = " + merchantId;

            } else if (stdate.equalsIgnoreCase("") && enddate.equalsIgnoreCase("")) {

                sql = "SELECT * FROM " + DbHelper.TABLE_MVISIT +
                        " WHERE " + DbHelper.COL_MVISIT_MERCHANT_ID + " = " + merchantId;

            }
        }

        System.out.println("sql - " + sql);

        SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
        cursor = sqLiteDatabase.rawQuery(sql, null);

        if (cursor.getCount() == 0) {
            System.out.println("No Data");
        } else {
            while (cursor.moveToNext()) {
                merchantVisits.add(new MerchantVisit(
                        cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MVISIT_MERCHANT_ID)),
                        cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MVISIT_REASON_ID)),
                        cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MVISIT_SALESREP_ID)),
                        cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MVISIT_DISTRIBUTOR_ID)),
                        cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MVISIT_DELIVERYPATH_ID)),
                        cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MVISIT_ISSYNC)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.COL_MVISIT_ENTERED_DATE)),
                        cursor.getString(cursor.getColumnIndex(DbHelper.COL_MVISIT_ENTERED_USER))
                ));
            }
        }
        return merchantVisits;
    }


    public ArrayList<Merchant> getAllMerchantVisitMerchants_accordingToDate(String stdate, String enddate) {

        ArrayList<Merchant> merchants = new ArrayList<>();

        return merchants;
    }


    public ArrayList<MerchantVisitReason> getAllMerchantVisitReasonData() {

        ArrayList<MerchantVisitReason> merchantVisitReasons = new ArrayList<>();
        Cursor cursor = db.getAllData(TABLE_MVISIT_REASON);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                int reasonid = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MVISIT_REASON_REASON_ID));
                String description = cursor.getString(cursor.getColumnIndex(DbHelper.COL_MVISIT_REASON_DESCRIPTION));
                int allowstock = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MVISIT_REASON_ALLOWSTOCK));
                merchantVisitReasons.add(new MerchantVisitReason(reasonid, description, allowstock));
            }
        }
        return merchantVisitReasons;
    }

    public ArrayList<MerchantVisit> getUnsyncMerchantVisitData() {

        ArrayList<MerchantVisit> merchantVisits = new ArrayList<>();
        String sql = "SELECT * FROM " + DbHelper.TABLE_MVISIT + " WHERE " + DbHelper.COL_MVISIT_ISSYNC + " = 0 ";
        MerchantVisit visit;

        Cursor cursor = (new DbHelper(context)).getReadableDatabase().rawQuery(sql, null);
        if (cursor != null && cursor.getCount() > 0) {

            while (cursor.moveToNext()) {

                int merchantid = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MVISIT_MERCHANT_ID));
                int reasonid = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MVISIT_REASON_ID));
                int salesrepid = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MVISIT_SALESREP_ID));
                int distributorid = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MVISIT_DISTRIBUTOR_ID));
                int deliverypathid = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MVISIT_DELIVERYPATH_ID));
                int issync = 1;
                String enterdate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_MVISIT_ENTERED_DATE));
                String enteruser = cursor.getString(cursor.getColumnIndex(DbHelper.COL_MVISIT_ENTERED_USER));

                visit = new MerchantVisit
                        (merchantid, reasonid, salesrepid, distributorid, deliverypathid, issync, enterdate, enteruser);
                merchantVisits.add(visit);
            }
        }
        return merchantVisits;
    }

    public int syncMerchantVisitData(final int allsync, final int salesrep_id) {

        final int[] merchantvisit_servervalue = {0};
        ArrayList<MerchantVisit> merV = new ArrayList<>();

        //clear sqlite db
        String sql = "DELETE FROM " + DbHelper.TABLE_MVISIT + " WHERE " + DbHelper.COL_MVISIT_ISSYNC + " = 1 ";
        try {
            (new DbHelper(context)).getReadableDatabase().execSQL(sql);
            System.out.println("Successfully deleted " + TABLE_MVISIT);
        } catch (Exception e) {
            System.out.println("e - " + e.toString());
            System.out.println("Error occur deleting " + TABLE_MVISIT);
        }


        //check wether unsync data has or not
        merV = getUnsyncMerchantVisitData();
        System.out.println("size of unsync data array - " + merV.size());
        for (int h = 0; h < merV.size(); h++) {

            String url = HTTPPaths.seriveUrl + "SaveDefMerchantVisitDetails?merchantID=" + merV.get(h).getMerchant_id() +
                    "&reasonId=" + merV.get(h).getReason_id() +
                    "&salesRepId=" + salesrep_id +
                    "&destributorId=" + merV.get(h).getDistributor_id() +
                    "&pathId=" + merV.get(h).getDeliverypath_id() +
                    "&enteredUser=" + merV.get(h).getEntered_user() +
                    "&enteredDate=" + merV.get(h).getEntered_date();

            StringRequest jsonObjectRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("response for uploading - " + response.toString());

                            JsonObject jsonObject = Json.parse(response).asObject();
                            int id = jsonObject.get("ID").asInt();
                            if (id == 200) {
                                System.out.println("succeffully uploded");
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("error for uploading - " + error.toString());

                        }
                    });

            MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
        }

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~for download data from server ~~~~~~~~
        String url1 = HTTPPaths.seriveUrl + "GetDefMerchantVisitDetailsBySalesRep?salesRepId=" + salesrep_id;
        System.out.println("url - " + url1);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonObject jsonObject = Json.parse(response).asObject();
                        int id = jsonObject.get("ID").asInt();
                        if (id == 200) {
                            String url2 = response.replace("{\"Data\":\"", "{\"Data\": ");
                            String url3 = url2.replace("\",\"ID\":200}", "}");
                            String url4 = url3.replace("\\", "");

                            if (response != null) {
                                try {
                                    JSONObject jsonObject_main = new JSONObject(url4);
                                    JSONArray jsonArray = jsonObject_main.getJSONArray("Data");

                                    for (int b = 0; b < jsonArray.length(); b++) {
                                        JSONObject jsonObject1 = jsonArray.getJSONObject(b);
                                        int MerchantId = jsonObject1.getInt("MerchantId");
                                        int ReasonId = jsonObject1.getInt("ReasonId");
                                        int SalesRepId = jsonObject1.getInt("SalesRepId");
                                        int DistributorId = jsonObject1.getInt("DistributorId");
                                        int DeliveryPathId = jsonObject1.getInt("DeliveryPathId");
                                        String EnteredDate = jsonObject1.getString("EnteredDate");
                                        String EnteredUser = jsonObject1.getString("EnteredUser");

                                        String iDate0;
                                        int start = EnteredDate.indexOf("(");
                                        int end = EnteredDate.indexOf(")");
                                        iDate0 = EnteredDate.substring(start + 1, end);
                                        String EnteredDate_ = getDate(Long.parseLong(iDate0), "yyyy-MM-dd");

                                        ContentValues cv = (new MerchantVisit(MerchantId, ReasonId, SalesRepId, DistributorId,
                                                DeliveryPathId, 1, EnteredDate_, EnteredUser))
                                                .getCVvaluesfromMerchantVisit();

                                        //System.out.println("###MerchantId - "+MerchantId+",SalesRepId - "+SalesRepId);

                                        boolean success = db.insertDataAll(cv, TABLE_MVISIT);
                                        if (success) {
                                            if (b == jsonArray.length() - 1) {
                                                System.out.println("Data is inserted to " + TABLE_MVISIT);

                                                merchantvisit_servervalue[0] = b + 1;
                                                time_ = (new DateManager().getDateWithTime());
                                                updateSyncTable(MerchantVisitCounts, time_, b + 1, b + 1, salesrep_id, 1);
                                                //Toast.makeText(context, "completed", Toast.LENGTH_SHORT).show();
                                                allRowCounts = allRowCounts + b + 1;

                                                if (allsync == 1) {
                                                    getPrimarySalesInvoiceBySalesRep(1, salesrep_id);
                                                }
                                            }
                                        } else {
                                            System.out.println("Data is not inserted to " + TABLE_MVISIT);
                                        }
                                    }
                                } catch (Exception e) {
                                    System.out.println("error at merchant visit table - " + e.toString());
                                }
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

        return merchantvisit_servervalue[0];
    }

    public ArrayList<MerchantVisit> getAllMerchantVisitData() {

        ArrayList<MerchantVisit> merchantVisits = new ArrayList<>();

        String sql = "SELECT * FROM " + DbHelper.TABLE_MVISIT;
        System.out.println("sql - " + sql);
        SQLiteDatabase readableDatabase = db.getReadableDatabase();
        Cursor cursor = readableDatabase.rawQuery(sql, null);

        System.out.println("cursor.size - " + cursor.getCount());
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                int MerchantId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MVISIT_MERCHANT_ID));
                int ReasonId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MVISIT_REASON_ID));
                int SalesRepId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MVISIT_SALESREP_ID));
                int DistributorId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MVISIT_DISTRIBUTOR_ID));
                int DeliveryPathId = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MVISIT_DELIVERYPATH_ID));
                int issync = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MVISIT_ISSYNC));
                String EnteredDate = cursor.getString(cursor.getColumnIndex(DbHelper.COL_MVISIT_ENTERED_DATE));
                String EnteredUser = cursor.getString(cursor.getColumnIndex(DbHelper.COL_MVISIT_ENTERED_USER));

                merchantVisits.add(new MerchantVisit(MerchantId, ReasonId, SalesRepId,
                        DistributorId, DeliveryPathId, issync, EnteredDate, EnteredUser));

            }
        }
        return merchantVisits;
    }

    public ArrayList<String> getMerchantListVsVisittoday(String today) {
        //2017-03-23
        ArrayList<String> merchants = new ArrayList<>();
        String sql = "SELECT * FROM " + DbHelper.TABLE_MVISIT + " WHERE " + DbHelper.COL_MVISIT_ENTERED_DATE + " = " + today;
        SQLiteDatabase sqLiteDatabase = db.getReadableDatabase();
        Cursor cursor = null;

        if (cursor != null) {
            if (cursor.getCount() > 0) {

                cursor = sqLiteDatabase.rawQuery(sql, null);

                while (cursor.moveToNext()) {
                    //String f = cursor.getColumnName(cursor.getColumnIndex(DbHelper.COL_MVISIT_MERCHANT_ID));
                    merchants.add(new String(cursor.getColumnName(cursor.getColumnIndex(DbHelper.COL_MVISIT_MERCHANT_ID))));
                }
            }
        } else {
            merchants = null;
        }

        return merchants;
    }


    public static String getDate(long milliSeconds, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }


    //Target Achievement
    //==================================================================================


    public void getSalesRepTargetBySalesRepId(final int allsync, int salesRepId) {

        final boolean success = db.deleteAllData(TABLE_SALESREP_TARGET);
        if (success) {
            System.out.println("All the details are deleted from " + TABLE_SALESREP_TARGET);
        } else {
            System.out.println("No data deleted " + TABLE_SALESREP_TARGET);
        }

        String url = HTTPPaths.seriveUrl + "GetSalesRepTargetBySalesRepId?salesRepId=" + salesRepId;


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonObject object = Json.parse(response).asObject();
                        int id = object.get("ID").asInt();
                        if (id == 200) {
                            String newUrl = response.replace("{\"Data\":\"", "{\"Data\": ");
                            String new2Url = newUrl.replace("\",\"ID\":200}", "}");
                            String new3Url = new2Url.replace("\\", "");

                            if (response != null) {
                                try {
                                    JSONObject jsonObj_main = new JSONObject(new3Url);
                                    JSONArray targetCategory = jsonObj_main.getJSONArray("Data");

                                    for (int i = 0; i < targetCategory.length(); i++) {
                                        JSONObject tcli = targetCategory.getJSONObject(i);
                                        int SalesRepID = tcli.getInt("SalesRepID");
                                        String TargetCategoryId = tcli.getString("TargetCategoryId");
                                        int TargetQty = tcli.getInt("TargetQty");
                                        int Year = tcli.getInt("Year");
                                        int Month = tcli.getInt("Month");

                                        SalesRepTarget salesRepTarget = new SalesRepTarget(
                                                SalesRepID, TargetCategoryId, TargetQty, Year, Month
                                        );

                                        ContentValues cv = salesRepTarget.getContentValues();
                                        boolean success = db.insertDataAll(cv, TABLE_SALESREP_TARGET);
                                        if (i == targetCategory.length() - 1) {
                                            Toast.makeText(context, "Data synced successfully in Salesrep target table", Toast.LENGTH_SHORT).show();


                                            if (allsync == 1) {
                                                getTargetProductCategoryList();
                                            }
                                        }
                                        if (success) {
                                            System.out.println("Data is inserted to " + TABLE_SALESREP_TARGET);
                                        } else {
                                            System.out.println("Data is not inserted to " + TABLE_SALESREP_TARGET);
                                        }

                                    }


                                } catch (JSONException e) {
                                    System.out.println("Json Error at SyncManager --- getTargetProductCategoryList");
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            System.out.println("Something went wrong Invalid response " + response.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Volley Error at SyncManger --- getTargetProductCategoryList : ");
                        error.printStackTrace();
                    }
                }
        );
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }

    public void getTargetProductCategoryList() {

        final boolean success = db.deleteAllData(TABLE_TARGET_CATEGORY);
        if (success) {
            System.out.println("All the details are deleted from " + TABLE_TARGET_CATEGORY);
        } else {
            System.out.println("No data deleted " + TABLE_TARGET_CATEGORY);
        }

        String url = HTTPPaths.seriveUrl + "GetTargetProductCategoryList";


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonObject object = Json.parse(response).asObject();
                        int id = object.get("ID").asInt();
                        if (id == 200) {
                            String newUrl = response.replace("{\"Data\":\"", "{\"Data\": ");
                            String new2Url = newUrl.replace("\",\"ID\":200}", "}");
                            String new3Url = new2Url.replace("\\", "");

                            if (response != null) {
                                try {
                                    JSONObject jsonObj_main = new JSONObject(new3Url);
                                    JSONArray targetCategory = jsonObj_main.getJSONArray("Data");

                                    for (int i = 0; i < targetCategory.length(); i++) {
                                        JSONObject tcli = targetCategory.getJSONObject(i);
                                        String targetCategoryId = tcli.getString("TargetCategoryId");
                                        String targetCategoryName = tcli.getString("TargetCategoryName");
                                        String unitOfMeasurement = tcli.getString("UnitOfMeasurement");
                                        String enteredDate = tcli.getString("EnteredDate");
                                        String enteredUser = tcli.getString("EnteredUser");

                                        TargetCategory targetCategor = new TargetCategory(
                                                targetCategoryId, targetCategoryName, unitOfMeasurement, enteredDate, enteredUser
                                        );

                                        ContentValues cv = targetCategor.getContentValues();
                                        boolean success = db.insertDataAll(cv, TABLE_TARGET_CATEGORY);
                                        if (success) {
                                            System.out.println("Data is inserted to " + TABLE_TARGET_CATEGORY);
                                        } else {
                                            System.out.println("Data is not inserted to " + TABLE_TARGET_CATEGORY);
                                        }
                                    }

                                } catch (JSONException e) {
                                    System.out.println("Json Error at SyncManager --- getTargetProductCategoryList");
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            System.out.println("Something went wrong Invalid response " + response.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Volley Error at SyncManger --- getTargetProductCategoryList : ");
                        error.printStackTrace();
                    }
                }
        );
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }

    public void getDefTargetCategoryLineItems() {

        final boolean success = db.deleteAllData(TABLE_TARGET_CATEGORY_LINE_ITEM);
        if (success) {
            System.out.println("All the details are deleted from " + TABLE_TARGET_CATEGORY_LINE_ITEM);
        } else {
            System.out.println("No data deleted " + TABLE_TARGET_CATEGORY_LINE_ITEM);
        }

        String url = HTTPPaths.seriveUrl + "GetDefTargetCategoryLineItemsList";


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonObject object = Json.parse(response).asObject();
                        int id = object.get("ID").asInt();
                        if (id == 200) {
                            String newUrl = response.replace("{\"Data\":\"", "{\"Data\": ");
                            String new2Url = newUrl.replace("\",\"ID\":200}", "}");
                            String new3Url = new2Url.replace("\\", "");

                            if (response != null) {
                                try {
                                    JSONObject jsonObj_main = new JSONObject(new3Url);
                                    JSONArray targetLineItmjsonArray = jsonObj_main.getJSONArray("Data");

                                    for (int i = 0; i < targetLineItmjsonArray.length(); i++) {
                                        JSONObject tcli = targetLineItmjsonArray.getJSONObject(i);
                                        String targetCategoryId = tcli.getString("TargetCategoryId");
                                        int productId = tcli.getInt("ProductId");
                                        int unitPerQuantity = tcli.getInt("UnitPerQuantity");
                                        String enterdUser = tcli.getString("EnteredUser");
                                        String enteredDate = tcli.getString("EnteredDate");

                                        TargetCategoryLineItem targetCategoryLineItem = new TargetCategoryLineItem(
                                                targetCategoryId, productId, unitPerQuantity, enterdUser, enteredDate
                                        );

                                        ContentValues cv = targetCategoryLineItem.getContentValues();
                                        boolean success = db.insertDataAll(cv, TABLE_TARGET_CATEGORY_LINE_ITEM);
                                        if (success) {
                                            System.out.println("Data is inserted to " + TABLE_TARGET_CATEGORY_LINE_ITEM);
                                        } else {
                                            System.out.println("Data is not inserted to " + TABLE_TARGET_CATEGORY_LINE_ITEM);
                                        }
                                    }


                                } catch (JSONException e) {
                                    System.out.println("Json Error at SyncManager --- getDefTargetCategoryLineItems");
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            System.out.println("Something went wrong Invalid response " + response.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Volley Error at SyncManger --- getDefTargetCategoryLineItems : ");
                        error.printStackTrace();
                    }
                }
        );
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }

    public void getPrimarySalesInvoiceBySalesRep(final int allsync, int salesRepId) {

        final boolean success = db.deleteAllData(TABLE_PRIMARY_SALES_INVOICE);
        if (success) {
            System.out.println("All the details are deleted from " + TABLE_PRIMARY_SALES_INVOICE);
        } else {
            System.out.println("No data deleted " + TABLE_PRIMARY_SALES_INVOICE);
        }


        final boolean success1 = db.deleteAllData(TABLE_PRIMARY_SALES_INVOICE_LINE_ITEM);
        if (success1) {
            System.out.println("All the details are deleted from " + TABLE_PRIMARY_SALES_INVOICE_LINE_ITEM);
        } else {
            System.out.println("No data deleted " + TABLE_PRIMARY_SALES_INVOICE_LINE_ITEM);
        }


        String url = HTTPPaths.seriveUrl + "GetPrimarySalesInvoiceBySalesRep?salesRepID=" + salesRepId;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonObject object = Json.parse(response).asObject();
                        int id = object.get("ID").asInt();
                        if (id == 200) {
                            String newUrl = response.replace("{\"Data\":\"", "{\"Data\": ");
                            String new2Url = newUrl.replace("\",\"ID\":200}", "}");
                            String new3Url = new2Url.replace("\\", "");

                            if (response != null) {
                                try {
                                    JSONObject jsonObj_main = new JSONObject(new3Url);
                                    JSONArray targetLineItmjsonArray = jsonObj_main.getJSONArray("Data");

                                    for (int i = 0; i < targetLineItmjsonArray.length(); i++) {
                                        JSONObject primarySalesOrder = targetLineItmjsonArray.getJSONObject(i);
                                        String InvoiceNumber = primarySalesOrder.getString("InvoiceNumber");
                                        String OrderDate = primarySalesOrder.getString("OrderDate");
                                        int StatusId = primarySalesOrder.getInt("StatusId");
                                        String EstimatedDeliveryDate = primarySalesOrder.getString("EstimatedDeliveryDate");
                                        String ExpectedDeliveryDate = primarySalesOrder.getString("ExpectedDeliveryDate");
                                        String DeliveredDate = primarySalesOrder.getString("DeliveredDate");
                                        int SalesInvoiceNumber = primarySalesOrder.getInt("SalesInvoiceNumber");
                                        int SalesRepID = primarySalesOrder.getInt("SalesRepID");

                                        PrimarySalesInvoice primarySalesInvoice = new PrimarySalesInvoice(
                                                InvoiceNumber, OrderDate, StatusId, EstimatedDeliveryDate, ExpectedDeliveryDate, DeliveredDate, SalesInvoiceNumber, SalesRepID
                                        );

                                        ContentValues cv = primarySalesInvoice.getContentValues();
                                        boolean success = db.insertDataAll(cv, TABLE_PRIMARY_SALES_INVOICE);
                                        if (success) {
                                            System.out.println("Data is inserted to " + TABLE_PRIMARY_SALES_INVOICE);
                                        } else {
                                            System.out.println("Data is not inserted to " + TABLE_PRIMARY_SALES_INVOICE);
                                        }

                                        //Primary Sales Order Line Item
                                        JSONArray lineItem = primarySalesOrder.getJSONArray("ListPrimarySalesInvoiceLineItem");

                                        for (int j = 0; j < lineItem.length(); j++) {
                                            JSONObject lineItemObject = lineItem.getJSONObject(j);

                                            int ProductId = lineItemObject.getInt("ProductId");
                                            String InvoiceNumber1 = lineItemObject.getString("InvoiceNumber");
                                            int OrderQty = lineItemObject.getInt("OrderQty");
                                            double SizeOfUnit = lineItemObject.getDouble("SizeOfUnit");

                                            PrimarySalesInvoiceLineItemBean primarySalesInvoiceLineItemBean = new PrimarySalesInvoiceLineItemBean(
                                                    ProductId, InvoiceNumber1, OrderQty, SizeOfUnit
                                            );

                                            ContentValues cvLineItem = primarySalesInvoiceLineItemBean.getContentValues();
                                            boolean suc = db.insertDataAll(cvLineItem, TABLE_PRIMARY_SALES_INVOICE_LINE_ITEM);
                                            if (suc) {
                                                System.out.println("Data is inserted to " + TABLE_PRIMARY_SALES_INVOICE_LINE_ITEM);
                                            } else {
                                                System.out.println("Data is not inserted to " + TABLE_PRIMARY_SALES_INVOICE_LINE_ITEM);
                                            }

                                        }


                                    }


                                } catch (JSONException e) {
                                    System.out.println("Json Error at SyncManager --- getPrimarySalesInvoiceBySalesRep");
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            System.out.println("Something went wrong Invalid response " + response.toString());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Volley Error at SyncManger --- getPrimarySalesInvoiceBySalesRep : ");
                        error.printStackTrace();
                    }
                }
        );
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }


    private void getReturnInventory() {
        int afectedRows;
        SQLiteDatabase database = db.getWritableDatabase();
        afectedRows = database.delete(TABLE_RETURN_INVENTORY, COL_RETURN_INVENTORY_isSync + " = ?", new String[]{1 + ""});
        System.out.println(afectedRows + " row(s)  deleted in " + TABLE_RETURN_INVENTORY);

        afectedRows = database.delete(TABLE_RETURN_INVENTORY_LINEITEM, COL_RETURN_INVENTORY_LINEITEM_isSync + " = ?", new String[]{1 + ""});
        System.out.println(afectedRows + " row(s)  deleted in " + TABLE_RETURN_INVENTORY_LINEITEM);

        String url = HTTPPaths.seriveUrl + "GetReturnInventoryListBySalesRep?salesRepId=" + getSalesrepId();
        System.out.println("Return Inventory Line Item URL : " + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JsonObject object = Json.parse(response).asObject();
                        int id = object.get("ID").asInt();

                        if (id == 200) {
                            String newUrl = response.replace("{\"Data\":\"", "{\"Data\": ");
                            String new2Url = newUrl.replace("\",\"ID\":200}", "}");
                            String new3Url = new2Url.replace("\\", "");
                            System.out.println("new3Url " + new3Url);

                            if (response != null) {
                                try {

                                    Gson gson = new Gson();
                                    ReturnTest returnTest = gson.fromJson(new3Url, ReturnTest.class);
                                    ArrayList<DataBean> dataBeans = returnTest.getData();
                                    System.out.println("DataBean size,  " + dataBeans.size());

                                    for (int i = 0; i < dataBeans.size(); i++) {
                                        DataBean d = dataBeans.get(i);
                                        System.out.println(d.getCreditNoteNo() + " " + d.getTotalAmount() + " " + d.getTotalOutstanding());

                                        ReturnInventory returnInventory = new ReturnInventory(d.getCreditNoteNo(),
                                                d.getReturnDate(), d.getTotalAmount(), d.getTotalOutstanding(),
                                                d.getMerchantId(), d.getSalesRepId(), d.getDistributorId(), 1, d.getSyncDate());

                                        ContentValues cv = returnInventory.getReturnInventoryContentValues();

                                        boolean success = db.insertDataAll(cv, TABLE_RETURN_INVENTORY);
                                        if (!success) {
                                            System.out.println("Not inserted to " + TABLE_RETURN_INVENTORY);
                                        } else {
                                            System.out.println("Inserted to " + TABLE_RETURN_INVENTORY);
                                        }

                                        ArrayList<ReturnInventoryLineItemListBean> returnInventoryLineItemList = d.get_ReturnInventoryLineItemList();
                                        System.out.println("Return Inventory Line Item List size " + returnInventoryLineItemList.size());
                                        for (int j = 0; j < returnInventoryLineItemList.size(); j++) {
                                            ReturnInventoryLineItemListBean returnLineItem = returnInventoryLineItemList.get(j);
                                            ReturnInventoryLineItem returnInventoryLineItem = new ReturnInventoryLineItem(returnLineItem.getCreditNoteNo(),
                                                    returnLineItem.getProductId(), returnLineItem.getBatchId(), returnLineItem.getQuantity(),
                                                    returnLineItem.getTotalAmount(), returnLineItem.getReturnType(), returnLineItem.getIsSellable(),
                                                    returnLineItem.getDiscountedPrice(), returnLineItem.getProductName(), 0, 1);
                                            ContentValues cv1 = returnInventoryLineItem.getReturnInventoryLineItemCv();
                                            boolean s = db.insertDataAll(cv1, TABLE_RETURN_INVENTORY_LINEITEM);

                                            System.out.println("Credit Note Number line item " + cv1.get("creditNoteNo"));

                                            if (!s) {
                                                System.out.println("Not inserted to " + TABLE_RETURN_INVENTORY_LINEITEM);
                                            } else {
                                                System.out.println("Inserted to " + TABLE_RETURN_INVENTORY_LINEITEM);
                                            }
                                        }
                                    }

                                } catch (Exception e) {
                                    System.out.println("Error at converting return array into json");
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            System.out.println("Error Response");
                            System.out.println("ID " + id);
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Error at volley return notes response ");
                        error.printStackTrace();
                    }
                }
        );
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    private String getSalesrepId() {
        String salesRepId = null;
        Cursor cursor = db.getAllData(DbHelper.TABLE_SALES_REP);
        while (cursor.moveToNext()) {
            salesRepId = cursor.getString(cursor.getColumnIndex(COL_SALES_REP_SalesRepId));
        }
        return salesRepId;
    }

    public void getPromotionDataFromServer() {

        boolean s = db.deleteAllData(TABLE_PROMOTION);
        if (s) {
            System.out.println("Successfully  delete "+TABLE_PROMOTION+" table data");
        } else {
            System.out.println("Error occur deleting "+TABLE_PROMOTION+"  table");
        }

        String url2 = HTTPPaths.seriveUrl + "ProductCatalogueList";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("response - " + response.toString());

                        JsonObject object = Json.parse(response).asObject();
                        int id = object.get("ID").asInt();
                        if (id == 200) {
                            String newUrl = response.replace("{\"Data\":\"", "{\"Data\": ");
                            String new2Url = newUrl.replace("\",\"ID\":200}", "}");
                            String new3Url = new2Url.replace("\\", "");


                            if (response != null) {
                                try {
                                    JSONObject jsonObj_main = new JSONObject(new3Url);
                                    JSONArray ff = jsonObj_main.getJSONArray("Data");

                                    for (int i = 0; i < ff.length(); i++) {
                                        JSONObject obj = ff.getJSONObject(i);

                                        int productid = obj.getInt("ProductId");
                                        int BatchId = obj.getInt("BatchId");
                                        int CatalogueType = obj.getInt("CatalogueType");
                                        int ImageIndex = obj.getInt("ImageIndex");
                                        String StartDate = obj.getString("StartDate");
                                        String EndDate = obj.getString("EndDate");
                                        int EnteredUser = obj.getInt("EnteredUser");
                                        String EnteredDate = obj.getString("EnteredDate");

                                        System.out.println("Productt ID: "+productid + " Batch id: "+ BatchId + " Image Index: "+ ImageIndex);

                                        PromotionList promotionList = new PromotionList(
                                                productid,BatchId,CatalogueType,StartDate,EndDate,EnteredUser,EnteredDate,ImageIndex);

                                        Boolean b = db.insertDataAll(promotionList.getCVPromotionalData(), TABLE_PROMOTION);

                                        if(b){
                                            System.out.println("Promotion data insert to SQLite");
                                        }else{
                                            System.out.println("Promotion data not insert to SQLite");
                                        }

                                    }
                                } catch (Exception e) {
                                    System.out.println("error - " + e.toString());
                                }
                            }
                        }
                        //


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error - " + error.toString());
                    }
                });

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

    //get image from server
    //getImageFromServer(PRODUCTID,BATCHID,cat_type);

    //line 1920
    public void getPromotionImageFromServer(final int pid, final int bid){
        //GetProductCatalogueImage?productId=7&batchId=1&catalogueType=1

        //catalogueType == 1 then we got promotion image
        //catalogueType == 0 then we got catalogue image

        String url = HTTPPaths.seriveUrl+"GetProductCatalogueImage?productId="+pid+"&batchId="+bid+"&catalogueType=1";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("response - "+response.toString());

                        //
                        byte[] btarray = null;

                        JsonObject object = Json.parse(response).asObject();
                        int id = object.get("ID").asInt();

                        if (id == 200) {
                            //byte[] barray = res
                            String newUrl = response.replace("{\"Data\":\"", "\"");
                            String new2Url = newUrl.replace("\",\"ID\":200}", "\"");
                            //String new3Url = new2Url.replace("\\", "");
                            System.out.println("string - "+new2Url);
                            btarray = new2Url.getBytes();

                        }else if(id == 500){
                            btarray = null;
                        }

                        ContentValues cvs = (new CatalogueImage(pid,bid,btarray,1)).getCVImagevalues();
                        if(cvs != null){
                            boolean b = (new DbHelper(context)).insertDataAll(cvs,TABLE_PROMOTION_IMAGE);
                            if(b){
                                System.out.println("Product ID= "+ pid+" Batch ID = "+ bid);
                                System.out.println("successes : catalog image insertion");
                            }else{
                                System.out.println("error image load");
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

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);


        //extra
    }

    public void getCatalogueImageFromServer(int pid,int bid){
        //GetProductCatalogueImage?productId=7&batchId=1&catalogueType=1

        //catalogueType == 1 then we got promotion image
        //catalogueType == 0 then we got catalogue image

        String url = HTTPPaths.seriveUrl+"GetProductCatalogueImage?productId="+pid+"&batchId="+bid+"&catalogueType=0";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("response - "+response.toString());

                        //byte[] barray = res
                        String res = response.replace("\"200","\"");
                        byte[] btarray = res.getBytes();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("error - "+error.toString());
                    }
                });

        MySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }


//    public ArrayList<PromotionList> getPromotionlistFromDB(){
//
//        Cursor cursor;
//        //ArrayList<>
//        String sql = "SELECT * FROM "+DbHelper.TABLE_PROMOTION;
//        try{
//
//            cursor = db.getReadableDatabase().rawQuery(sql,null);
//            if(cursor != null && cursor.getCount() > 0){
//                while(cursor.moveToNext()){
//                    int productid = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_PROMOTION_ProductId));
//                    int batchid = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_PROMOTION_BatchId));
//                    int catalogtype = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_PROMOTION_CatalogueType));
//                }
//            }
//        }catch(Exception e){
//
//        }
//
//
//    }
}
