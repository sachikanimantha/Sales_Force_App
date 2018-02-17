package com.example.bellvantage.scadd;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
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
import com.example.bellvantage.scadd.Utils.DateTimeFormatings;
import com.example.bellvantage.scadd.Utils.SessionManager;
import com.example.bellvantage.scadd.domains.Attendance;
import com.example.bellvantage.scadd.swf.LoginUser;
import com.example.bellvantage.scadd.web.HTTPPaths;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.Drawer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.bellvantage.scadd.DB.DbHelper.COL_DISTRIBUTOR_DISTRIBUTOR_ID;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SALES_REP_SalesRepId;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SYNC_ATTENDANCE_BATTERY_IN;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SYNC_ATTENDANCE_BATTERY_OUT;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SYNC_ATTENDANCE_DATE;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SYNC_ATTENDANCE_DISTRIBUTR_ID;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SYNC_ATTENDANCE_IS_SYNC_IN;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SYNC_ATTENDANCE_IS_SYNC_OUT;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SYNC_ATTENDANCE_LATITUDE_IN;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SYNC_ATTENDANCE_LATITUDE_OUT;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SYNC_ATTENDANCE_LONGITUDE_IN;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SYNC_ATTENDANCE_LONGITUDE_OUT;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SYNC_ATTENDANCE_SALESREP_ID;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SYNC_ATTENDANCE_TIME_IN;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SYNC_ATTENDANCE_TIME_OUT;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_SYNC_ATTENDANCE;

public class AttendanceActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    //Toolbar
    private Toolbar mToolBar;

    //Navigation Drawer
    private AccountHeader headerResult = null;
    private Drawer result = null;

    //Spinner spLocation;

    //views
    TextView tvMobileTime,tvIn,tvOut;
    java.util.Date noteTS;
    Button btnSubmit;

    LinearLayout llMarkIn,llMarOut;

    CardView cvLocationIn;

    //Geo Location
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private  double lat;
    private  double lon;

    double inLon,inlat,outLon,outlat;

    private String selectedLocation;
    private int venueCode,voltage;

    private int attendanceCount = 0;
    private DbHelper dbh;

    String enteredUser,status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        dbh = new DbHelper(getApplicationContext());
        tvMobileTime = (TextView)findViewById(R.id.tvMobileTime);
        tvIn = (TextView)findViewById(R.id.tvIn);
        tvOut = (TextView)findViewById(R.id.tvOut);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        llMarkIn = (LinearLayout) findViewById(R.id.llMarkIn);
        llMarOut = (LinearLayout) findViewById(R.id.llMarkOut);

        cvLocationIn = (CardView) findViewById(R.id.cvLocationIn);

        //ToolBar
        mToolBar = (Toolbar) findViewById(R.id.tb_main);
        mToolBar.setTitle("Attendance");

        Gson gson = new Gson();
        String getJson = SessionManager.pref.getString("user", "");
        LoginUser prefUser = gson.fromJson(getJson, LoginUser.class);
        enteredUser =prefUser.getUserName();
        registerReceiver(mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        //Geo Location
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        //time
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateTextView();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();

        setAttendanceType();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LocationManager manager = (LocationManager) getSystemService(getApplicationContext().LOCATION_SERVICE );
                boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                if(statusOfGPS==false){
                    Toast.makeText(getApplicationContext(),"GPS is disabled.. Please Check your GPS", Toast.LENGTH_LONG).show();
                    return;
                }


                if(lat == 0.0 && lon == 0.0){
                    Toast.makeText(AttendanceActivity.this,getResources().getString(R.string.invalied_location), Toast.LENGTH_SHORT).show();
                    return;
                }

                if(selectedLocation == "Select Location"){
                    Toast.makeText(AttendanceActivity.this, "You haven't selected Location", Toast.LENGTH_SHORT).show();
                    return;
                }

                Attendance attendance = new Attendance();

                attendance.setIsSynced(0);
                attendance.setMarkedTime(new Date());
                attendance.setUserId(enteredUser);
                attendance.setLon(lon);
                attendance.setLat(lat);
                attendance.setType(attendanceCount + 1);
                attendance.setVenue(selectedLocation);
                attendance.setVenueCode(venueCode);


                System.out.println("Attendance OnButtonClick Count: "+attendanceCount);
                ContentValues cv =attendance.getContentvalues();

                boolean success = dbh.insertData(cv);
                if (success){
                    Toast.makeText(AttendanceActivity.this, "Your attendance is inserted", Toast.LENGTH_SHORT).show();
                    syncAttendance(attendance,attendanceCount);
                    setAttendanceType();
                }
                else {
                    Toast.makeText(AttendanceActivity.this, "Your attendance is not inserted", Toast.LENGTH_SHORT).show();
                }

                unregisterReceiver(mBatInfoReceiver);
                AttendanceActivity.this.finish();
                //spLocation.setSelection(0);

            }
        });


        //View Geo Locations
        llMarkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (inlat ==0 || inLon==0){
                    displayStatusMessage("Unable to get your IN time GPS location",3);
                    return;
                }

                Intent intent = new Intent(getApplicationContext(),LocationViewrActivity.class);
                intent.putExtra("longitude",inLon);
                intent.putExtra("latitude",inlat);
                startActivity(intent);
            }
        });


        llMarOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (outLon==0 || outlat==0){
                    displayStatusMessage("Unable to get your IN time GPS location",3);
                    return;
                }

                Intent intent = new Intent(getApplicationContext(),LocationViewrActivity.class);
                intent.putExtra("longitude",outLon);
                intent.putExtra("latitude",outlat);
                startActivity(intent);
            }
        });

    }//end onCreate

    private void syncAttendance(final Attendance attendance, int attendanceCount) {

        System.out.println("Atendance Count: "+attendanceCount);
        final String attendanceDate = DateManager.getTodayDateString();
        String distributorID = getDistributerId();
        String salesRepID = getSalesrepId();
        StringRequest stringRequest;
        if (attendanceCount==0){
            String timeIn = DateManager.getDateWithTime();
            timeIn=timeIn.replace(" ","%20");
            String url= HTTPPaths.seriveUrl+
                    "SaveAttendance?attendanceDate=" +attendanceDate+
                    "&distributorID=" +distributorID+
                    "&salesRepID=" +salesRepID+
                    "&timeIN=" +timeIn+
                    "&timeOut=" +timeIn+
                    "&batteryStatusIN=" +status+
                    "&batteryStatusOut=0" +
                    "&isSync=0" +
                    "&syncDate=" +attendanceDate+
                    "&longitudeIn=" +attendance.getLon()+
                    "&latitudeIn=" +attendance.getLat()+
                    "&longitudeOut=0.0" +
                    "&latitudeOut=0.0";

            System.out.println("Attendance Insertion URL: "+ url);

            //Insert MarkIn to db
            final ContentValues cv =new ContentValues();
            cv.put(COL_SYNC_ATTENDANCE_DATE,attendanceDate);
            cv.put(COL_SYNC_ATTENDANCE_DISTRIBUTR_ID,distributorID);
            cv.put(COL_SYNC_ATTENDANCE_SALESREP_ID,salesRepID);
            cv.put(COL_SYNC_ATTENDANCE_TIME_IN,timeIn);
            cv.put(COL_SYNC_ATTENDANCE_BATTERY_IN,status);
            cv.put(COL_SYNC_ATTENDANCE_LONGITUDE_IN,attendance.getLon());
            cv.put(COL_SYNC_ATTENDANCE_LATITUDE_IN,attendance.getLat());
            cv.put(COL_SYNC_ATTENDANCE_IS_SYNC_IN,0);

            stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JsonObject object = Json.parse(response).asObject();
                            int id = object.get("ID").asInt();
                            if (id == 200) {
                                System.out.println("Sync Successful at Attendance Activity");
                                AttendanceActivity.this.finish();
                                cv.put(COL_SYNC_ATTENDANCE_IS_SYNC_IN,1);
                                insertSyncData(cv);
                            }else{
                                 System.out.println("Sync Fail at Attendance Activity");
                                insertSyncData(cv);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("Error at syncing Attendance at Attendance Activity");
                            insertSyncData(cv);
                        }
                    }
            );
        }else{
            String timeOut = DateManager.getDateWithTime();
            timeOut=timeOut.replace(" ","%20");
            String url =    HTTPPaths.seriveUrl +
                            "UpdateAttendance?attendanceDate=" +attendanceDate+
                            "&salesRepID=" +salesRepID+
                            "&timeOut=" +timeOut+
                            "&batteryStatusOut=" +status+
                            "&longitudeOut=" +attendance.getLon()+
                            "&latitudeOut="+attendance.getLat();
            System.out.println("Attendance updated URL: "+ url);

            //Insert MarkOut to db
            final ContentValues cv =new ContentValues();

            cv.put(COL_SYNC_ATTENDANCE_TIME_OUT,timeOut);
            cv.put(COL_SYNC_ATTENDANCE_BATTERY_OUT,status);
            cv.put(COL_SYNC_ATTENDANCE_LONGITUDE_IN,attendance.getLon());
            cv.put(COL_SYNC_ATTENDANCE_LATITUDE_IN,attendance.getLat());
            cv.put(COL_SYNC_ATTENDANCE_LONGITUDE_OUT,attendance.getLon());
            cv.put(COL_SYNC_ATTENDANCE_LATITUDE_OUT,attendance.getLat());
            cv.put(COL_SYNC_ATTENDANCE_IS_SYNC_OUT,0);

            stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JsonObject object = Json.parse(response).asObject();
                            int id = object.get("ID").asInt();
                            System.out.println("++++++++++++ Attendance Response: "+ response.toString());
                            if(id == 200){
                                System.out.println("Successfully updated mark out time at Attendance SyncActivity");
                                cv.put(COL_SYNC_ATTENDANCE_IS_SYNC_OUT,1);
                                updateMarkOut(cv,attendanceDate);
                            }else{
                                System.out.println("SYNC FAIL Attendance SyncActivity");
                                updateMarkOut(cv,attendanceDate);
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("Error at update Attendance at Attendance Activity");
                            updateMarkOut(cv,attendanceDate);
                        }
                    }
            );
        }


        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void updateMarkOut(ContentValues cv, String attendanceDate) {
        SQLiteDatabase database = dbh.getWritableDatabase();
        try{
            int afectedRows = database.update(TABLE_SYNC_ATTENDANCE,cv,COL_SYNC_ATTENDANCE_DATE +" = ? " ,new String[]{""+attendanceDate});
            if(afectedRows>0){
                System.out.println("Attendance is  updated at row "+attendanceDate);
            }
        }catch (SQLException e){
            System.out.println("Error at data update at SYNC_ATTENDANCE "+ e.getMessage());
        }finally {
            database.close();
        }

    }

    private void insertSyncData(ContentValues cv) {
        boolean success = dbh.insertDataAll(cv,TABLE_SYNC_ATTENDANCE);
        if (success){
            System.out.println("Data is inserted to "+ TABLE_SYNC_ATTENDANCE+" IsSyncIn: "+ cv.get(COL_SYNC_ATTENDANCE_IS_SYNC_IN));
        }
    }

    private String getDistributerId() {
        Cursor cursor = dbh.getAllData(DbHelper.TABLE_DISTRIBUTOR);

        String distributerId = null;
        while (cursor.moveToNext()){
            distributerId = cursor.getString(cursor.getColumnIndex(COL_DISTRIBUTOR_DISTRIBUTOR_ID));
        }

        return distributerId;

    }

    private String getSalesrepId() {
        String salesRepId = null;
        Cursor cursor = dbh.getAllData(DbHelper.TABLE_SALES_REP);
        while (cursor.moveToNext()){
            salesRepId = cursor.getString(cursor.getColumnIndex(COL_SALES_REP_SalesRepId));
        }
        return salesRepId;
    }

    private void updateTextView() {
        noteTS = Calendar.getInstance().getTime();

        String time = "hh:mm a"; // 12:00
        tvMobileTime.setText(DateFormat.format(time, noteTS));
    }




   private void setAttendanceType() {
        ArrayList<Attendance> attendaces = getAttendaceCount();
        attendanceCount =attendaces.size();
        if (attendanceCount == 0) {
            btnSubmit.setText("MarK In");
            cvLocationIn.setVisibility(View.GONE);

        } else if (attendanceCount == 1) {
            btnSubmit.setText("MarK Out");
            tvIn.setText(DateTimeFormatings.getDateTime(attendaces.get(0).getMarkedTime()));
            inlat = attendaces.get(0).getLat();
            inLon = attendaces.get(0).getLon();

            cvLocationIn.setVisibility(View.VISIBLE);
            llMarOut.setVisibility(View.GONE);
        } else {
            btnSubmit.setText("Already Marked both");
            tvIn.setText(DateTimeFormatings.getDateTime(attendaces.get(0).getMarkedTime()));
            inlat = attendaces.get(0).getLat();
            inLon = attendaces.get(0).getLon();

            tvOut.setText(DateTimeFormatings.getDateTime(attendaces.get(1).getMarkedTime()));
            outlat= attendaces.get(1).getLat();
            outLon= attendaces.get(1).getLon();

            btnSubmit.setEnabled(false);
            cvLocationIn.setVisibility(View.VISIBLE);
            llMarOut.setVisibility(View.VISIBLE);
        }
    }
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this,"You haven't accepted the loaction permissions for this app. Please go to settings and enable permissions", Toast.LENGTH_LONG).show();
            //ActivityCompat.requestPermissions(this,new String[]);
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            lat = mLastLocation.getLatitude();
            lon = mLastLocation.getLongitude();
            //Toast.makeText(this,lat+ "", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    public ArrayList<Attendance> getAttendaceCount(){
        Calendar calOn = Calendar.getInstance();
        Calendar calOff = Calendar.getInstance();

        calOn.set(Calendar.HOUR_OF_DAY, 00);
        calOn.set(Calendar.MINUTE, 01);

        calOff.set(Calendar.HOUR_OF_DAY, 23);
        calOff.set(Calendar.MINUTE, 59);
        return  getAttendance(calOn.getTimeInMillis(),calOff.getTimeInMillis());
    }



    public ArrayList<Attendance> getAttendance(long min,long max){
        String sql = "select * from attendance where attendace_date >= " + min +" and attendace_date <= " + max;
        SQLiteDatabase db  = dbh.getWritableDatabase();
        Cursor cursor = null;
        ArrayList<Attendance> attendances = new ArrayList<Attendance>();
        try {
            cursor =  db.rawQuery(sql,null);
            if(cursor.moveToFirst()){
                do{
                    Attendance attendance = Attendance.getDBInstance(cursor);
                    attendances.add(attendance);
                }while(cursor.moveToNext());
            }
            return attendances;

        }catch (Exception e){
            throw  e;
        }finally {
            if(cursor != null){
                cursor.close();
            }
            db.close();
        }
    }



    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            voltage = intent.getIntExtra("voltage", 0);

            status =  String.valueOf(level);

            int temperature = intent.getIntExtra("temperature", 0);
            System.out.println("Battery Status: " + String.valueOf(level) + "%");
            System.out.println("Battery Voltage: " + String.valueOf(voltage));
            double temps = (double)temperature / 10;
            System.out.println("Battery Temperature: " + String.valueOf(temps));
        }
    };

    public void displayStatusMessage(String s,int colorValue) {

        AlertDialog.Builder builder = null;
        View view = null;
        TextView tvOk,tvMessage,tvCancel;

        int defaultColor = R.color.textGray;
        int successColor = R.color.successColor; // 1
        int errorColor = R.color.errorColor; // 2
        int warningColor = R.color.warningColor; // 3
        //1,2,3

        int color = defaultColor;
        if(colorValue == 1){
            color = successColor;
        }else if(colorValue == 2){
            color = errorColor;
        }else if(colorValue == 3){
            color = warningColor;
        }

        builder = new AlertDialog.Builder(AttendanceActivity.this);
        view = getLayoutInflater().inflate(R.layout.layout_for_custom_message_with_ok_cancel,null);

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
            }
        });

    }
}
