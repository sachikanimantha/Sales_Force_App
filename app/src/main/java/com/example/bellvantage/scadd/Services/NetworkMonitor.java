package com.example.bellvantage.scadd.Services;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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
import com.example.bellvantage.scadd.web.HTTPPaths;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.bellvantage.scadd.DB.DbHelper.COL_LINE_ITEM_IsSync;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_LINE_ITEM_SalesOrderID;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_RETURN_INVENTORY_JSON_IsSync;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_RETURN_INVENTORY_LINEITEM_creditNoteNo;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_RETURN_INVENTORY_creditNoteNo;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SALES_ORDER_IsSync;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SALES_ORDER_JSON_IsSync;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SALES_ORDER_JSON_SalesOrderId;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SALES_ORDER_SalesOrderId;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SYNC_ATTENDANCE_BATTERY_IN;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SYNC_ATTENDANCE_BATTERY_OUT;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SYNC_ATTENDANCE_DATE;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SYNC_ATTENDANCE_DISTRIBUTR_ID;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SYNC_ATTENDANCE_ID;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SYNC_ATTENDANCE_IS_SYNC_IN;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SYNC_ATTENDANCE_IS_SYNC_OUT;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SYNC_ATTENDANCE_LATITUDE_IN;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SYNC_ATTENDANCE_LATITUDE_OUT;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SYNC_ATTENDANCE_LONGITUDE_IN;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SYNC_ATTENDANCE_LONGITUDE_OUT;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SYNC_ATTENDANCE_SALESREP_ID;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SYNC_ATTENDANCE_TIME_IN;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SYNC_ATTENDANCE_TIME_OUT;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TABLE_SYNC_MILEAGE_enteredUser;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TABLE_SYNC_MILEAGE_isSyncIn;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TABLE_SYNC_MILEAGE_isSyncOut;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TABLE_SYNC_MILEAGE_mileageDate;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TABLE_SYNC_MILEAGE_mileageIn;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TABLE_SYNC_MILEAGE_mileageInTime;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TABLE_SYNC_MILEAGE_mileageOut;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TABLE_SYNC_MILEAGE_mileageOutTime;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TABLE_SYNC_MILEAGE_salesRepId;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TABLE_SYNC_MILEAGE_syncDate;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_SYNC_ATTENDANCE;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_SYNC_MILEAGE;

/**
 * Created by Sachika on 15/05/2017.
 */

public class NetworkMonitor extends BroadcastReceiver {

    DbHelper db;

    @Override
    public void onReceive(final Context context, Intent intent) {
        db = new DbHelper(context);
       if (checkNetworkConnection(context)) {
           syncMerchantToServer(context);
           syncAttendanceToServer(context);
           syncMileageToServer(context);
          // syncReturnNotes(context);
          // syncSalesOrderJson(context);
          // syncSalesOrders(context);
            Cursor cursor = db.getAllData(DbHelper.TBL25);
            if (cursor.getCount() == 0) {
                System.out.println(DbHelper.TBL25 + " Table is empty");
                return;
            } else {
                while (cursor.moveToNext()) {
                    int syncStatus = cursor.getInt(cursor.getColumnIndex(DbHelper.COL25_IS_SYNC));
                    if(syncStatus == DbHelper.SYNC_STATUS_FAIL){

                        final String SEQUENCE_ID=cursor.getString(cursor.getColumnIndex(DbHelper.COL25_SEQUENCE_ID));
                        String ROUTE_DATE=cursor.getString(cursor.getColumnIndex(DbHelper.COL25_ROUTE_DATE));
                        int DISTRIBUTER_ID= cursor.getInt(cursor.getColumnIndex(DbHelper.COL25_DISTRIBUTER_ID));
                        int SALES_REP_ID = cursor.getInt(cursor.getColumnIndex(DbHelper.COL25_SALES_REP_ID));
                        String userName = cursor.getString(cursor.getColumnIndex(DbHelper.COL_25_USER_NAME));
                        String LOCATION_TIME = cursor.getString(cursor.getColumnIndex(DbHelper.COL25_LOCATION_TIME));
                        double LONGITUDE = cursor.getDouble(cursor.getColumnIndex(DbHelper.COL25_LONGITUDE));
                        double LATITUDE = cursor.getDouble(cursor.getColumnIndex(DbHelper.COL25_LATITUDE));
                        int IS_SYNC = cursor.getInt(cursor.getColumnIndex(DbHelper.COL25_IS_SYNC));
                        String SYNC_DATE = cursor.getString(cursor.getColumnIndex(DbHelper.COL25_SYNC_DATE));
                        float ACCURACY =cursor.getFloat(cursor.getColumnIndex(DbHelper.COL25_ACCURACY));

                        DateManager dateManager  =  new DateManager();
                        String SyncDate = dateManager.getDateWithTime();
                        SyncDate = SyncDate.replace(" ", "%20");

                        String actualPathurl = HTTPPaths.seriveUrl+"SaveActualPath?routeDate="+ROUTE_DATE+"&locationTime="+LOCATION_TIME+"&longitude="+LONGITUDE+"&latitude="+LATITUDE+"&isSync=1&syncDate="+SyncDate+"&accuracy="+ACCURACY+"&userName="+userName;
                        System.out.println("actual path url "+ actualPathurl);
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, actualPathurl,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        JsonObject object = Json.parse(response).asObject();
                                        int id = object.get("ID").asInt();
                                        try {
                                            if(id==200){
                                                db.updateActualPath(SEQUENCE_ID,DbHelper.SYNC_STATUS_OK);
                                                context.sendBroadcast(new Intent(HTTPPaths.UI_UPDATE_BROADCAST));
                                                System.out.println("server syncing");

                                            }else{
                                                System.out.println("server sync fail");
                                            }
                                        }catch (Exception e){
                                            System.out.println("Data response error "+ e.getMessage());
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        System.out.println("Volley error " + error.getMessage());
                                    }
                                });
                        MySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(stringRequest);
                    }
                }

            }
        }
    }

    private void syncAttendanceToServer(Context context) {

        Cursor scursor = db.getAllData(DbHelper.TABLE_SYNC_ATTENDANCE);
        StringRequest stringRequest = null;
        if (scursor.getCount() == 0) {
            System.out.println(DbHelper.TABLE_SYNC_ATTENDANCE + " Table is empty");
            return;
        } else {
            while (scursor.moveToNext()) {
                final String syncDate = DateManager.getTodayDateString();


                final int sid = scursor.getInt(scursor.getColumnIndex(COL_SYNC_ATTENDANCE_ID));
                String attendanceDate = scursor.getString(scursor.getColumnIndex(COL_SYNC_ATTENDANCE_DATE));
                String distributerId = scursor.getString(scursor.getColumnIndex(COL_SYNC_ATTENDANCE_DISTRIBUTR_ID));
                String salesRepId = scursor.getString(scursor.getColumnIndex(COL_SYNC_ATTENDANCE_SALESREP_ID));
                String timeIn = scursor.getString(scursor.getColumnIndex(COL_SYNC_ATTENDANCE_TIME_IN));
                String timeOut = scursor.getString(scursor.getColumnIndex(COL_SYNC_ATTENDANCE_TIME_OUT));
                String bateryIn = scursor.getString(scursor.getColumnIndex(COL_SYNC_ATTENDANCE_BATTERY_IN));
                String bateryOut = scursor.getString(scursor.getColumnIndex(COL_SYNC_ATTENDANCE_BATTERY_OUT));
                double logitudeIn = scursor.getDouble(scursor.getColumnIndex(COL_SYNC_ATTENDANCE_LONGITUDE_IN));
                double latitudeIn = scursor.getDouble(scursor.getColumnIndex(COL_SYNC_ATTENDANCE_LATITUDE_IN));
                double lonhitudeOut = scursor.getDouble(scursor.getColumnIndex(COL_SYNC_ATTENDANCE_LONGITUDE_OUT));
                double latitudeOut = scursor.getDouble(scursor.getColumnIndex(COL_SYNC_ATTENDANCE_LATITUDE_OUT));
                int syncIn = scursor.getInt(scursor.getColumnIndex(COL_SYNC_ATTENDANCE_IS_SYNC_IN));
                int syncOut = scursor.getInt(scursor.getColumnIndex(COL_SYNC_ATTENDANCE_IS_SYNC_OUT));
                
                if(syncIn == 0){
                    String url= HTTPPaths.seriveUrl+
                            "SaveAttendance?attendanceDate=" +attendanceDate+
                            "&distributorID=" +distributerId+
                            "&salesRepID=" +salesRepId+
                            "&timeIN=" +timeIn+
                            "&timeOut=" +timeOut+
                            "&batteryStatusIN=" +bateryIn+
                            "&batteryStatusOut="+bateryOut+
                            "&isSync=0" +
                            "&syncDate=" +syncDate+
                            "&longitudeIn=" +logitudeIn+
                            "&latitudeIn=" +latitudeIn+
                            "&longitudeOut="+lonhitudeOut +
                            "&latitudeOut="+latitudeOut;

                    System.out.println("Attendance Insertion URL: "+ url);

                    //Insert MarkIn to db
                    final ContentValues cv =new ContentValues();
                    cv.put(COL_SYNC_ATTENDANCE_IS_SYNC_IN,1);

                    stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    JsonObject object = Json.parse(response).asObject();
                                    int id = object.get("ID").asInt();
                                    if (id == 200) {
                                        System.out.println("Sync Successful at Attendance @ Network Monitor");
                                        updateIsSyncAtrrendance(cv,sid);

                                    }else{
                                        System.out.println("Sync Fail at Attendance @ Network Monitor");

                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    System.out.println("Error at syncing Attendance at Attendance @ Network Monitor");

                                }
                            }
                    );
                }

                if(syncOut == 0){
                    String url =    HTTPPaths.seriveUrl +
                            "UpdateAttendance?attendanceDate=" +attendanceDate+
                            "&salesRepID=" +salesRepId+
                            "&timeOut=" +timeOut+
                            "&batteryStatusOut=" +bateryOut+
                            "&longitudeOut=" +lonhitudeOut+
                            "&latitudeOut="+latitudeOut;
                    System.out.println("Attendance updated URL: "+ url);



                    final ContentValues cv =new ContentValues();
                    cv.put(COL_SYNC_ATTENDANCE_IS_SYNC_OUT,1);

                    stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    JsonObject object = Json.parse(response).asObject();
                                    int id = object.get("ID").asInt();
                                    System.out.println("++++++++++++Network Monitor Attendance Response: "+ response.toString());
                                    if(id == 200){
                                        System.out.println("Successfully updated mark out time at Attendance Network Monitor");
                                        updateIsSyncAtrrendance(cv,sid);
                                    }else{
                                        System.out.println("SYNC FAIL Attendance Network Monitor");

                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    System.out.println("Error at update Attendance at Attendance Network Monitor");

                                }
                            }
                    );


                }
            }

            MySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(stringRequest);
        }

    }

    private void syncMerchantToServer(Context context) {
        Cursor cursor = db.getAllData(DbHelper.TABLE_MERCHANT);
        if (cursor.getCount() == 0) {
            System.out.println(DbHelper.TABLE_MERCHANT + " Table is empty");
            return;
        } else {
            while (cursor.moveToNext()) {
                int IsSync = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_IS_SYNC));
                int MerchantId = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_MERCHANT_ID));
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
                int isCredit = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_IS_CREDIT));
                String creditDays = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_CREDIT_DAYS));


                MerchantName = MerchantName.replace(" ","%20");
                BuildingNo = BuildingNo.replace(" ","%20");
                Address1 = Address1.replace(" ","%20");
                Address2 = Address2.replace(" ","%20");
                ContactPerson = ContactPerson.replace(" ","%20");

                if(NetworkConnection.checkNetworkConnection(context)==true){
                    if (IsSync!=1 && MerchantId==0) {
                        System.out.println("Entering syncMerchant Network monitor register");
                        registerMerchant(SequenceId,MerchantName, ContactNo1, ContactNo2, Longitude, Latitude, IsActive, EnteredDate, EnteredUser, DiscountRate, BuildingNo, Address1, Address2, City, ContactPerson, AreaCode, MerchantType, MerchantClass, DistrictCode, ReferenceID, IsVat, VatNo, pathCode, SequenceId,context,syncId,isCredit,creditDays);
                    }
                    if(IsSync!=1 && MerchantId!=0){
                        UpdateDefMerachant(""+SequenceId,MerchantId,MerchantName,ContactNo1,ContactNo2,Longitude,
                                Latitude,IsActive,
                                DiscountRate,IsActive,SyncDate,BuildingNo,Address1,Address2,City,ContactPerson,AreaCode,
                                MerchantType,MerchantClass,DistrictCode,UpdatedUser,ReferenceID,IsVat,VatNo,pathCode,context,syncId,isCredit,creditDays);
                    }
                }

            }

        }
    }

    private void registerMerchant(final int sequencedId, String merchantName, String contact1, String contact2, double lon, double lat, int isActive, final String enteredDate, String enteredUser, int discountRate, String buildingNo, String address1, String address2, String city, String contactPerson, int areaCode, String merchantType, String merchantClass, int districCode, String referenceID, int vat, String vatNo, final int pathCode, int sequenceID,Context context,String syncId,int isCredit,String creditDays) {
        System.out.println("Entering server registration network monitor");
        String url = HTTPPaths.seriveUrl + "SaveDefMerachant?merchantName=" + merchantName + "&contactNo1=" + contact1 + "&contactNo2=" + contact2 + "&longitude=" + lon + "&latitude=" + lat + "&isActive=" + isActive + "&enteredUser=" + enteredUser + "&discountRate="
                + discountRate + "&buildingNo=" + buildingNo + "&address1=" + address1 + "&address2=" + address2 + "&city=" + city + "&contactPerson=" + contactPerson + "&areaCode=" + areaCode + "&merchantType=" + merchantType + "&merchantClass=" + merchantClass + "&districtCode=" + districCode + "&referenceID=" + referenceID + "&isVat=" + vat + "&vatNo=" + vatNo+"&syncId="+syncId+"&isCredit="+isCredit+"&creditDays="+creditDays;
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
                            //registerPath(MerchantID, pathCode);
                            ContentValues cv = new ContentValues();
                            cv.put("MerchantId",MerchantID);
                            cv.put("IsSync",1);
                            boolean success = db.updateTable(""+sequencedId,cv,DbHelper.TBL_MERCHANT_SEQUENCE_ID +" =?",DbHelper.TABLE_MERCHANT);
                            if(success==true){
                                System.out.println("merchant sync and updated network using network monitor");
                            }else{
                                System.out.println("merchant syn not  updated using network monitor");
                            }
                            System.out.println("Merchant syncing is successful using network monitor");
                        } else {
                            System.out.println("Merchant syncing is fail using network monitor");
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Merchant syncing error using network monitor" + error.getMessage());
                    }
                }
        );
        MySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }

    //Update Methods
    private void UpdateDefMerachant(String sequenceId, final int merchantId, String merchantName,
                                    String contact1, String contact2, double lon, double lat,
                                    int isActive, int discountRate, int isSync, String syncDate,
                                    String buildingNo, String address1, String address2, String city,
                                    String contactPerson, int areaCode, String merchantType, String merchantClass,
                                    int districCode, int updateUserd, String referenceID, int vat,
                                    String vatNo, int pathCode, final Context context,String syncId,int isCredit,String creditDays) {

        final ContentValues cv = new ContentValues();

        cv.put("IsSync",1);


        if(NetworkConnection.checkNetworkConnection(context)==true){
            String url = HTTPPaths.seriveUrl+"UpdateDefMerachant?merchantId="+merchantId+"&merchantName="
                    +merchantName+"&contactNo1="+contact1+"&contactNo2="
                    +contact2+"&longitude="+lon+"&latitude="+lat+"&isActive="+isActive+
                    "&discountRate="+discountRate+"&isSync="+isSync+"&syncDate="+syncDate+
                    "&buildingNo="+buildingNo+"&address1="+address1+"&address2="+address2+
                    "&city="+city+"&contactPerson="+contactPerson+"&areaCode="+areaCode+"&merchantType="
                    +merchantType+"&merchantClass="+merchantClass+"&districtCode="+districCode+"&updatedUser="
                    +updateUserd+"&referenceID="+referenceID+"&isVat="+vat+"&vatNo="+vatNo+"&syncId="+syncId+"&isCredit="+isCredit+"&creditDays="+creditDays;



            System.out.println("Update URL "+url);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JsonObject object = Json.parse(response).asObject();
                            int id = object.get("ID").asInt();
                            System.out.println("Response ID "+ id);
                            if(id==200){

                                System.out.println("network monitor merchant update");
                               // getSequenceID(""+merchantId);
                                // Toast.makeText(MRA_UpdateActivity.this, "Successfully Updated..", Toast.LENGTH_LONG).show();
                                db.updateTable(""+merchantId,cv,DbHelper.TBL_MERCHANT_MERCHANT_ID +" =?",DbHelper.TABLE_MERCHANT);
                                System.out.println("network monitor merchant update SQLite");
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("Update Error"+error.getMessage());
                        }
                    }
            );
            MySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(stringRequest);
        }
    }



    public void syncReturnNotes(Context context){
        SQLiteDatabase database = db.getWritableDatabase();
        Cursor cursor = db.getAllData(DbHelper.TABLE_RETURN_INVENTORY_JSON);
        if (cursor.getCount() == 0) {
            System.out.println(DbHelper.TABLE_RETURN_INVENTORY_JSON + " Table is empty");
            return;
        } else {
            while (cursor.moveToNext()) {
                final int creditNoteNo = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_RETURN_INVENTORY_JSON_creditNoteNo));
                final String jsonStrinh = cursor.getString(cursor.getColumnIndex(DbHelper.COL_RETURN_INVENTORY_JSON_JsonString));
                int IsSync = cursor.getInt(cursor.getColumnIndex(COL_RETURN_INVENTORY_JSON_IsSync));

                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(jsonStrinh);

                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("Json Error ReturnActivity : " + e.getMessage());
                }
                if (IsSync == 0){

                    if (NetworkConnection.checkNetworkConnection(context.getApplicationContext())==true){

                        String url = HTTPPaths.seriveUrl+"CreateCreditNoteMobile";

                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObj,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                        JsonObject object = Json.parse(String.valueOf(response)).asObject();
                                        int id = object.get("ID").asInt();
                                        int responseSalesOrderId = Integer.parseInt(object.get("Data").asString());
                                        if(id==200){

                                            System.out.println("== Network Monitor === Return Inventory Json table is Syncing.... ");
                                            ContentValues cv = new ContentValues();
                                            cv.put(COL_RETURN_INVENTORY_JSON_IsSync, 1);
                                            db.updateTable(creditNoteNo+"",cv,DbHelper.COL_RETURN_INVENTORY_JSON_creditNoteNo+" = ?",DbHelper.TABLE_RETURN_INVENTORY_JSON);

                                            ContentValues cvUpdate = new ContentValues();
                                            ContentValues cvUpdateLineItem = new ContentValues();
                                            cvUpdate.put(COL_RETURN_INVENTORY_creditNoteNo,responseSalesOrderId);
                                            cvUpdateLineItem.put(COL_RETURN_INVENTORY_LINEITEM_creditNoteNo,responseSalesOrderId);
                                            db.updateTable(creditNoteNo+"",cvUpdate,DbHelper.COL_RETURN_INVENTORY_creditNoteNo + " = ?",DbHelper.TABLE_RETURN_INVENTORY);
                                            db.updateTable(creditNoteNo+"",cvUpdateLineItem, COL_RETURN_INVENTORY_LINEITEM_creditNoteNo + " = ?",DbHelper.TABLE_RETURN_INVENTORY_LINEITEM);

                                        }else{
                                            System.out.println("== Network Monitor === Return Inventory Json table is Syncing fail.... ");
                                        }

                                        System.out.println("=========response - "+response.toString());

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        System.out.println("== Network Monitor === Return Inventory Json table is Syncing fail.... ");
                                    }
                                });
                        MySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(jsonObjectRequest);
                    }else {

                    }
                }
            }
        }

    }

    //Json sync ==
    public void syncSalesOrderJson(Context context){

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
                System.out.println("=== Create Sales Order ID NETWORK MONITOR " + salesOrderid);
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(salesOrderJson);

                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("Json Error in NetworkMonitor syncSalesOrderJson: " + e.getMessage());
                }

                if (IsSync == 0){
                    String url = HTTPPaths.seriveUrl+"CreateSalesOrderMobile";
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObj,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(final JSONObject response) {

                                    JsonObject object = Json.parse(String.valueOf(response)).asObject();
                                    int id = object.get("ID").asInt();
                                    int responseSalesOrderId = Integer.parseInt(object.get("Data").asString());
                                    System.out.println("=== Sales Order ID NETWORK MONITOR RESPONSE SalesOrderId " + responseSalesOrderId);
                                    System.out.println("response syncSalesOrderJson - "+response.toString());

                                    if (id==200){
                                        ContentValues cv = new ContentValues();
                                        cv.put(COL_SALES_ORDER_JSON_IsSync, 1);
                                        cv.put(COL_SALES_ORDER_JSON_SalesOrderId, responseSalesOrderId);
                                        db.updateTable(invoiceNumber+"",cv,DbHelper.COL_SALES_ORDER_JSON_InvoiceNumber+" = ?",DbHelper.TABLE_SALES_ORDER_JSON);
                                        System.out.println("=== Network Monitor ====== "+DbHelper.TABLE_SALES_ORDER_JSON+ " is Syncing");


                                        ContentValues cvUpdate = new ContentValues();
                                        ContentValues cvUpdateLineItem = new ContentValues();
                                        cvUpdate.put(COL_SALES_ORDER_SalesOrderId,responseSalesOrderId);
                                        cv.put(COL_SALES_ORDER_IsSync,1);
                                        cvUpdateLineItem.put(COL_LINE_ITEM_SalesOrderID,responseSalesOrderId);
                                        cvUpdateLineItem.put(COL_LINE_ITEM_IsSync,1);
                                        db.updateTable(invoiceNumber+"",cvUpdate,DbHelper.COL_SALES_ORDER_InvoiceNumber + " = ?",DbHelper.TABLE_SALES_ORDER);
                                        db.updateTable(salesOrderid+"",cvUpdateLineItem, COL_LINE_ITEM_SalesOrderID + " = ?",DbHelper.TABLE_LINE_ITEM);


                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    System.out.println("error - "+error.toString());
                                }
                            });

                    MySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(jsonObjectRequest);

                }
            }
        }

    }

    //sales order's status update, confirm and cancel ==
    public void syncSalesOrders(Context context){
        SQLiteDatabase database = db.getWritableDatabase();
        Cursor cursor = db.getAllData(DbHelper.TABLE_SALESORDERS_SYNC);
        if (cursor.getCount() == 0) {
            System.out.println(DbHelper.TABLE_SALESORDERS_SYNC + " Table is empty");
            return;
        } else {
            while (cursor.moveToNext()) {
                final int SalesOrderID = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALESORDER_SYNC_SalesOrderID));
                System.out.println("== Update Sales Status Sales OrderId " + SalesOrderID);
                final int SaleStatus = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALESORDER_SYNC_SaleStatus));
                int IsSync = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALESORDER_SYNC_IsSync));

                if (IsSync == 0){
                    String url = HTTPPaths.seriveUrl+"UpdateStatus?salesOrderID="+SalesOrderID+"&status="+SaleStatus;
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    JsonObject object = Json.parse(response).asObject();
                                    int id = object.get("ID").asInt();
                                    System.out.println("cancel ID "+ id);
                                    if(id==200){
                                        System.out.println("== Network Monitor === SalesOrderSync table is Syncing.... ");
                                        ContentValues cv = new ContentValues();
                                        cv.put("SaleStatus",SaleStatus);
                                        cv.put("IsSync", 1);

                                        System.out.println("== Update Sales Status Sales OrderId res " + SalesOrderID);
                                        db.updateTable(SalesOrderID+"",cv,DbHelper.COL_SALES_ORDER_SalesOrderId+" = ?",DbHelper.TABLE_SALES_ORDER);
                                        db.updateTable(SalesOrderID+"",cv,DbHelper.COL_SALESORDER_SYNC_SalesOrderID+" = ?",DbHelper.TABLE_SALESORDERS_SYNC);

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

                                    System.out.println("== Network Monitor ==" + " SyncSalesOrders " + "Volley Error +++ " + error.getMessage());
                                }
                            }
                    );
                    MySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(stringRequest);
                }else{
                    database.execSQL("DELETE FROM "+ DbHelper.TABLE_SALESORDERS_SYNC+ " WHERE " + DbHelper.COL_SALESORDER_SYNC_SalesOrderID + " = "+SalesOrderID+"");
                }
            }
        }

    }

    public boolean checkNetworkConnection(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private void updateIsSyncAtrrendance(ContentValues cv, int attendanceid) {
        SQLiteDatabase database = db.getWritableDatabase();
        try{
            int afectedRows = database.update(TABLE_SYNC_ATTENDANCE,cv,COL_SYNC_ATTENDANCE_ID +" = ? " ,new String[]{""+attendanceid});
            if(afectedRows>0){
                System.out.println("Attendance is  updated at row "+attendanceid);
            }
        }catch (SQLException e){
            System.out.println("Error at data update at SYNC_ATTENDANCE "+ e.getMessage());
        }finally {
            database.close();
        }

    }


    private void syncMileageToServer(Context context) {

        Cursor scursor = db.getAllData(DbHelper.TABLE_SYNC_MILEAGE);
        StringRequest stringRequest = null;
        if (scursor.getCount() == 0) {
            System.out.println(DbHelper.TABLE_SYNC_MILEAGE + " Table is empty");
            return;
        } else {
            while (scursor.moveToNext()) {
                final String syncDate = DateManager.getTodayDateString();


                final String mileageDate = scursor.getString(scursor.getColumnIndex(COL_TABLE_SYNC_MILEAGE_mileageDate));
                String salesRepId = scursor.getString(scursor.getColumnIndex(COL_TABLE_SYNC_MILEAGE_salesRepId));
                String mileageInTime = scursor.getString(scursor.getColumnIndex(COL_TABLE_SYNC_MILEAGE_mileageInTime));
                String mileageIn = scursor.getString(scursor.getColumnIndex(COL_TABLE_SYNC_MILEAGE_mileageIn));
                String MileageOutTime = scursor.getString(scursor.getColumnIndex(COL_TABLE_SYNC_MILEAGE_mileageOutTime));
                String mileageOut = scursor.getString(scursor.getColumnIndex(COL_TABLE_SYNC_MILEAGE_mileageOut));
                int isSyncIn = scursor.getInt(scursor.getColumnIndex(COL_TABLE_SYNC_MILEAGE_isSyncIn));
                int isSyncOut = scursor.getInt(scursor.getColumnIndex(COL_TABLE_SYNC_MILEAGE_isSyncOut));
                String syncDateMileage = scursor.getString(scursor.getColumnIndex(COL_TABLE_SYNC_MILEAGE_syncDate));
                String enteredUser = scursor.getString(scursor.getColumnIndex(COL_TABLE_SYNC_MILEAGE_enteredUser));

                if(isSyncIn == 0){
                    String url = HTTPPaths.seriveUrl+"SaveMileage?mileageDate="+mileageDate+"&salesRepID="+salesRepId+
                            "&mileageInTime="+mileageInTime+"&mileageIn="+mileageIn+"&mileageOutTime="+MileageOutTime +
                            "&mileageOut="+mileageOut+"&isSync=1&syncDate="+syncDateMileage+"&enteredUser="+enteredUser;

                    System.out.println("URL : "+ url);

                    //Insert MarkIn to db
                    final ContentValues cv =new ContentValues();
                    cv.put(COL_TABLE_SYNC_MILEAGE_isSyncIn,1);

                    stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    JsonObject object = Json.parse(response).asObject();
                                    int id = object.get("ID").asInt();
                                    if (id == 200) {
                                        System.out.println("Sync Successful at Mileage @ Network Monitor");
                                        updateMileage(cv,mileageDate);

                                    }else{
                                        System.out.println("Sync Fail at Mileage @ Network Monitor");

                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    System.out.println("Error at syncing Attendance at Attendance @ Network Monitor");
                                }
                            }
                    );
                }

                if(isSyncOut == 0){
                    String url = HTTPPaths.seriveUrl+"UpdateMileageOut?mileageDate="+mileageDate+
                            "&salesRepID="+salesRepId+"&mileageOutTime="+MileageOutTime+"&mileageOut="+mileageOut;

                    System.out.println("[NetworkMonitor] Mileage Insertion URL: "+ url);



                    final ContentValues cv =new ContentValues();
                    cv.put(COL_TABLE_SYNC_MILEAGE_isSyncOut,1);

                    stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    JsonObject object = Json.parse(response).asObject();
                                    int id = object.get("ID").asInt();
                                    System.out.println("++++++++++++Network Monitor Mileage Out update Response: "+ response.toString());
                                    if(id == 200){
                                        System.out.println("Successfully updated mark out time at Mileage Network Monitor");
                                        updateMileage(cv,mileageDate);
                                    }else{
                                        System.out.println("SYNC FAIL Mileage Network Monitor");

                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    System.out.println("Error at update Mileage at Attendance Network Monitor");

                                }
                            }
                    );


                }
            }

            MySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(stringRequest);
        }

    }


    private void updateMileage(ContentValues cv, String mileageDate) {
        SQLiteDatabase database = db.getWritableDatabase();
        try{
            int afectedRows = database.update(TABLE_SYNC_MILEAGE,cv,COL_TABLE_SYNC_MILEAGE_mileageDate +" = ? " ,new String[]{""+mileageDate});
            if(afectedRows>0){
                System.out.println("[Network Monitor] Mileage Out is  updated at row "+mileageDate);
            }
        }catch (SQLException e){
            System.out.println("[Network Monitor] Error at data update of Mileage Out "+ e.getMessage());
        }finally {
            database.close();
        }
    }
}
