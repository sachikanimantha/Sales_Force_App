package com.example.bellvantage.scadd;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.example.bellvantage.scadd.DB.DbHelper;
import com.example.bellvantage.scadd.DB.MySingleton;
import com.example.bellvantage.scadd.Utils.DateManager;
import com.example.bellvantage.scadd.Utils.NetworkConnection;
import com.example.bellvantage.scadd.Utils.SessionManager;
import com.example.bellvantage.scadd.swf.LoginUser;
import com.example.bellvantage.scadd.swf.Mileage;
import com.example.bellvantage.scadd.web.HTTPPaths;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.bellvantage.scadd.DB.DbHelper.COL_SALES_REP_SalesRepId;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SALES_REP_SalesRepName;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TABLE_MILEAGE_markedTime;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TABLE_SYNC_MILEAGE_isSyncOut;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TABLE_SYNC_MILEAGE_mileageDate;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TABLE_SYNC_MILEAGE_mileageOut;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TABLE_SYNC_MILEAGE_mileageOutTime;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TABLE_SYNC_MILEAGE_salesRepId;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_MILEAGE;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_SYNC_MILEAGE;

public class MileageActivty extends AppCompatActivity {

    //ToolBar
    Toolbar toolbar;

    //Layouts
    LinearLayout llInMilege,llOutMileage;

    //TextInputLayouts
    TextInputLayout tilInMileage,tilOutMileage;

    //EditText
    EditText etInMileage,etOutMileage;

    //TextViews
    TextView tvSubmit,tvSalesRepName,tvDate;

    String enterdUser = "";

    private DbHelper dbh;

    //Set Mileage Type

    private int mileageCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mileage_activty);

        //Initialize all views
        initialzeViews();
        Gson gson = new Gson();
        String getJson = SessionManager.pref.getString("user", "");
        LoginUser prefUser = gson.fromJson(getJson, LoginUser.class);
        enterdUser = prefUser.getUserName();
        dbh = new DbHelper(getApplicationContext());

        toolbar.setTitle("Mileage");


        setSalesRepName();


        setMileageType();
        //SendMileages
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String imMileage = etInMileage.getText().toString();
                String outMileage = etOutMileage.getText().toString();

                System.out.println("Mileage OnButtonClick Count: "+mileageCount);

                if (mileageCount==0 && imMileage.isEmpty()){
                    Toast.makeText(MileageActivty.this, "Please enter your  mileage", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mileageCount==1 && outMileage.isEmpty()){
                    Toast.makeText(MileageActivty.this, "Please enter your  mileage", Toast.LENGTH_SHORT).show();
                    return;
                }

                Mileage mileage = new Mileage();

                mileage.setId(1);
                mileage.setMarkedTime(new Date());
                mileage.setType(mileageCount+1);
                mileage.setSalesRepId(getSalesrepId());

                System.out.println("Mileage OnButtonClick Count: "+mileageCount);
                ContentValues cv =mileage.getContenValues();

                boolean success = dbh.insertDataAll(cv,TABLE_MILEAGE);
                if (success){
                    Toast.makeText(MileageActivty.this, "Your Mileage is inserted", Toast.LENGTH_SHORT).show();
                    syncAttendance(mileageCount);
                    setMileageType();
                }
                else {
                    Toast.makeText(MileageActivty.this, "Your Mileage is not inserted", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }



    private void syncAttendance(int mileageCount) {
        System.out.println("Mileage OnButtonClick Count: "+mileageCount);
        if (mileageCount==0){
            submitInMileage();
        }else{
            System.out.println("OUT Mileage");
            submitOutMileage();

        }
    }

    private void submitOutMileage() {
        String outMileage = etOutMileage.getText().toString();
        String salesRepId = getSalesrepId();
        String timeIn = DateManager.getDateWithTime();
        timeIn = timeIn.replace(" ","%20");
        final String mileageDate = DateManager.getTodayDateString();

        String url = HTTPPaths.seriveUrl+"UpdateMileageOut?mileageDate="+mileageDate+
                "&salesRepID="+salesRepId+"&mileageOutTime="+timeIn+"&mileageOut="+outMileage;
        System.out.println("URL : "+ url);


        final ContentValues cv = new ContentValues();
        cv.put(COL_TABLE_SYNC_MILEAGE_mileageDate,mileageDate);
        cv.put(COL_TABLE_SYNC_MILEAGE_salesRepId,salesRepId);
        cv.put(COL_TABLE_SYNC_MILEAGE_mileageOutTime,timeIn);
        cv.put(COL_TABLE_SYNC_MILEAGE_mileageOut,outMileage);
        cv.put(COL_TABLE_SYNC_MILEAGE_isSyncOut,0);

        if(NetworkConnection.checkNetworkConnection(getApplicationContext())){
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println(" ==== response :  ==========================");
                            System.out.println(response.toString());

                            JsonObject object = Json.parse(response).asObject();
                            int id = object.get("ID").asInt();
                            if(id == 200){
                                cv.put(COL_TABLE_SYNC_MILEAGE_isSyncOut,1);
                                updateMileage(cv,mileageDate);
                            }else{
                                updateMileage(cv,mileageDate);
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println(" ==== Error :  ==========================");
                            error.printStackTrace();
                            updateMileage(cv,mileageDate);
                        }
                    }
            );
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        }else {
            updateMileage(cv,mileageDate);
        }
    }

    private void updateMileage(ContentValues cv, String mileageDate) {
        SQLiteDatabase database = dbh.getWritableDatabase();
        try{
            int afectedRows = database.update(TABLE_SYNC_MILEAGE,cv,COL_TABLE_SYNC_MILEAGE_mileageDate +" = ? " ,new String[]{""+mileageDate});
            if(afectedRows>0){
                System.out.println("Mileage Out is  updated at row "+mileageDate);
            }
        }catch (SQLException e){
            System.out.println("Error at data update of Mileage Out "+ e.getMessage());
        }finally {
            database.close();
        }
    }

    private void submitInMileage() {
        String inMileage = etInMileage.getText().toString();
//        String outMileage = etOutMileage.getText().toString();
        String salesRepId = getSalesrepId();
        String timeIn = DateManager.getDateWithTime();
        timeIn = timeIn.replace(" ","%20");
        final String mileageDate = DateManager.getTodayDateString();

        String url = HTTPPaths.seriveUrl+"SaveMileage?mileageDate="+mileageDate+"&salesRepID="+salesRepId+
                "&mileageInTime="+timeIn+"&mileageIn="+inMileage+"&mileageOutTime="+timeIn +
                "&mileageOut="+0+"&isSync=1&syncDate="+timeIn+"&enteredUser="+enterdUser;

        System.out.println("URL : "+ url);



        final Mileage mileage = new Mileage(mileageDate,salesRepId,timeIn,inMileage,timeIn,"0",0,0,timeIn,enterdUser);

        if(NetworkConnection.checkNetworkConnection(getApplicationContext())){
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println(" ==== response :  ==========================");
                            System.out.println(response.toString());
                            JsonObject object = Json.parse(response).asObject();
                            int id = object.get("ID").asInt();
                            if(id == 200){
                                System.out.println("Mileage sync is successfully");
                                mileage.setIsSyncIn(1);
                                insertMileageToSQLite(mileage);
                            }else{
                                insertMileageToSQLite(mileage);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println(" ==== Error :  ==========================");
                            error.printStackTrace();
                            insertMileageToSQLite(mileage);
                        }
                    }
            );
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        }else{
            insertMileageToSQLite(mileage);
            System.out.println("No data connection");
        }

    }

    //for sync later
    private void insertMileageToSQLite(Mileage mileage) {
        ContentValues cv = mileage.getSyncContenValues();
        boolean success = dbh.insertDataAll(cv,TABLE_SYNC_MILEAGE);
        if (success){
            System.out.println("Data is inserted to "+ TABLE_SYNC_MILEAGE);
        }
        else {
            System.out.println("Data is not inserted to "+ TABLE_SYNC_MILEAGE);
        }
    }

    private void initialzeViews() {

        //ToolBar
        toolbar = (Toolbar) findViewById(R.id.tb_main);

        //Layouts
        llInMilege = (LinearLayout) findViewById(R.id.llInMileage);
        llOutMileage = (LinearLayout) findViewById(R.id.llOutMileage);

        //TextInputLayouts
        tilInMileage = (TextInputLayout) findViewById(R.id.tilInMileage);
        tilOutMileage = (TextInputLayout) findViewById(R.id.tilOutMileage);

        //EditText
        etInMileage = (EditText) findViewById(R.id.etInMileage);
        etOutMileage = (EditText) findViewById(R.id.etOutMileage);

        //TextViews
        tvSubmit = (TextView) findViewById(R.id.tvSubmit);
        tvSalesRepName = (TextView) findViewById(R.id.tvSalesRepName);
        tvDate = (TextView) findViewById(R.id.tvDate);
    }

    private String getSalesrepId() {
        String salesRepId = null;
        Cursor cursor = dbh.getAllData(DbHelper.TABLE_SALES_REP);
        while (cursor.moveToNext()){
            salesRepId = cursor.getString(cursor.getColumnIndex(COL_SALES_REP_SalesRepId));
        }
        return salesRepId;
    }
    private void setSalesRepName() {
        Cursor cursor = dbh.getAllData(DbHelper.TABLE_SALES_REP);
        String salesRepName=null;
        if (cursor.getCount()>0){
            while (cursor.moveToNext()){
                salesRepName = cursor.getString(cursor.getColumnIndex(COL_SALES_REP_SalesRepName));
            }
        }
        String date = DateManager.getTodayDateString();

        tvSalesRepName.setText("Name: "+salesRepName);
        tvDate.setText("Date: "+date);
    }

    //Set Mileage Type
    private void setMileageType() {
        ArrayList<Mileage> mileages = getAttendaceCount();
        mileageCount =mileages.size();
        if (mileageCount == 0) {
            tvSubmit.setText("Submit Mileage (IN)");
            llOutMileage.setVisibility(View.GONE);

        } else if (mileageCount == 1) {
            tvSubmit.setText("Submit Mileage (OUT)");
            llInMilege.setVisibility(View.GONE);
            llOutMileage.setVisibility(View.VISIBLE);
        } else {
            tvSubmit.setText("Already Marked both");

            tvSubmit.setEnabled(false);
            llInMilege.setVisibility(View.GONE);
            llOutMileage.setVisibility(View.GONE);
        }
    }
    public ArrayList<Mileage> getAttendaceCount(){
        Calendar calOn = Calendar.getInstance();
        Calendar calOff = Calendar.getInstance();

        calOn.set(Calendar.HOUR_OF_DAY, 00);
        calOn.set(Calendar.MINUTE, 01);

        calOff.set(Calendar.HOUR_OF_DAY, 23);
        calOff.set(Calendar.MINUTE, 59);
        return  getAttendance(calOn.getTimeInMillis(),calOff.getTimeInMillis());

    }
    public ArrayList<Mileage> getAttendance(long min,long max){
        String sql = "select * from "+TABLE_MILEAGE+" where " + COL_TABLE_MILEAGE_markedTime+ "  >= " + min +" and "+COL_TABLE_MILEAGE_markedTime+ " <= " + max;
        SQLiteDatabase db  = dbh.getWritableDatabase();
        Cursor cursor = null;
        ArrayList<Mileage> mileages = new ArrayList<Mileage>();
        try {
            cursor =  db.rawQuery(sql,null);
            if(cursor.moveToFirst()){
                do{
                    Mileage mileage = Mileage.getDBInstance(cursor);
                    mileages.add(mileage);
                }while(cursor.moveToNext());
            }
            return mileages;

        }catch (Exception e){
            throw  e;
        }finally {
            if(cursor != null){
                cursor.close();
            }
            db.close();
        }
    }
}
