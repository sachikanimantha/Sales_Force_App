package com.example.bellvantage.scadd;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.example.bellvantage.scadd.DB.DbHelper;
import com.example.bellvantage.scadd.DB.MySingleton;
import com.example.bellvantage.scadd.Utils.Cam;
import com.example.bellvantage.scadd.Utils.DateManager;
import com.example.bellvantage.scadd.Utils.ImageLoader;
import com.example.bellvantage.scadd.Utils.NetworkConnection;
import com.example.bellvantage.scadd.Utils.SessionManager;
import com.example.bellvantage.scadd.Utils.SyncManager;
import com.example.bellvantage.scadd.swf.Area;
import com.example.bellvantage.scadd.swf.DeliveryPath;
import com.example.bellvantage.scadd.swf.DistrictMerchant;
import com.example.bellvantage.scadd.swf.LoginUser;
import com.example.bellvantage.scadd.swf.MerchantDetails;
import com.example.bellvantage.scadd.swf.MerchantImage;
import com.example.bellvantage.scadd.web.HTTPPaths;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

import static com.example.bellvantage.scadd.DB.DbHelper.COL_IMAGE_ISSYNC;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_IMAGE_MERCHANT_ID;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_IMAGE_SEQUENCE_ID;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_AREA_DISTRICT_CODE;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_DISTRICT;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_IMAGE;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_PATH;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_PATH_DELIVERYPATHID;
import static com.example.bellvantage.scadd.DB.DbHelper.TBL_MERCHANT_CREDIT_DAYS;
import static com.example.bellvantage.scadd.DB.DbHelper.TBL_MERCHANT_IS_CREDIT;

public class MRA_UpdateActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{


    MerchantDetails merchantDetails;

    Button btnUpdate;
    EditText etMerchantName,etContact1,etContact2,etAddress1,etAddress2,etBuildingNo,etCity,etVat,etCreditDays,etContactPerson,etReferenceID,etDiscountRate;
    TextInputLayout tilVat,tilCredit;
    String merchantName,contact1,contact2,enteredUser,merchantType,merchantClass,buildingNo,address1,address2,city,vatN,enteredDte, enteredDate,syncDate;
    int vat=0,merchantId,pathCode,isActive,isSync,discountRate = 0,enteredUse,isCredit=0;
    ArrayList<DistrictMerchant> districtMerchantArrayList = new ArrayList<>();
    ArrayList<Area> areaArrayList = new ArrayList<>();
    ArrayList<DeliveryPath> pathArrayList = new ArrayList<>();
    CheckBox cbVat,cbCredit;
    String selectedDistrict,selectedArea,selectedPath,contactPerson,referenceID;
    String districCode,areaCode,vatNo,updateedUser,UpdatedDate,sequenceId,syncId,creditDays="";
    TextView tvPath,tvArea,tvDistrict,tvImage1,tvImage2;
    ImageView ivPhoto1,ivPhoto2;
    SyncManager syncManager;
    ArrayList<String> pathItems=new ArrayList<>();
    ArrayList<String> districtItems=new ArrayList<>();
    ArrayList<String> areaItems=new ArrayList<>();
    SpinnerDialog pathSpinnerDialog,areaSpinnerDialog,districtSpinnerDialog;
    //Spinners
    Spinner spArea,spDistrict,spPath;

    //Geo Location
    private GoogleApiClient mGoogleApiClient;
    private int PLACE_PICKER_REQUEST = 1;
    private Location mLastLocation;
    private  double lat,lati;
    private  double lon,loni;

    boolean isUpdate = false;

    Cam cameraPhoto;
    final  int CAMERA_REQUEST= 13323;
    final  int CAMERA_REQUEST1= 13324;


    String seqId;

    private DbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mra__update);

        db  = new DbHelper(getApplicationContext());
        cameraPhoto =new Cam(MRA_UpdateActivity.this);

       /* //Geo Location for place picker
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .enableAutoManage(this, this)
                    .build();
        }*/

        //Geo Location
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        initializeViews();

        Gson gson = new Gson();
        String getJson = SessionManager.pref.getString("user", "");
        LoginUser prefUser = gson.fromJson(getJson, LoginUser.class);
        enteredUse =prefUser.getUserTypeId();

        //set data to spinners
        syncManager = new SyncManager(getApplicationContext());
        areaArrayList = syncManager.getAllArea();
        districtMerchantArrayList = syncManager.getAllDistrict();
        pathArrayList=syncManager.getAllPath();

        loadDistrict(districtMerchantArrayList);
       // loadArea(areaArrayList);
        loadPath(pathArrayList);

        // etMerchantName.setEnabled(false);


        tvArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //areaSpinnerDialog.showSpinerDialog();
                pathSpinnerDialog.showSpinerDialog();
            }
        });
       /* tvPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pathSpinnerDialog.showSpinerDialog();
            }
        });*/
        tvDistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                districtSpinnerDialog.showSpinerDialog();
            }
        });

        //get data from intent
        if (getIntent().getSerializableExtra("merchant_details") != null) {
            merchantDetails = (MerchantDetails) getIntent().getSerializableExtra("merchant_details");
        }
        sequenceId  = Integer.toString(merchantDetails.getSequenceId());
        merchantId = merchantDetails.getMerchantId();
        System.out.println("MerchantID "+ merchantId);
        merchantName = merchantDetails.getMerchantName();
        contact1 = merchantDetails.getContactNo1();
        contact2 = merchantDetails.getContactNo2();

        String ln= merchantDetails.getLongitude();
        String lt = merchantDetails.getLatitude();
        if( ln.equals("")){
            ln=0+"";
        }

        if(lt.equals("")){
            lt=0+"";
        }

        lon =Double.parseDouble(ln);
        lat = Double.parseDouble(lt);

        System.out.println("first Lat: "+ lat + " second lon "+lon);
        isActive = merchantDetails.getIsActive();
        enteredUser = merchantDetails.getEnteredUser();
        enteredDate = merchantDetails.getEnteredDate();
        discountRate = merchantDetails.getDiscountRate();
        isSync = merchantDetails.getIsSync();
        syncDate = merchantDetails.getSyncDate();
        buildingNo = merchantDetails.getBuildingNo();
        address1 = merchantDetails.getAddress1();
        address2 = merchantDetails.getAddress2();
        city = merchantDetails.getCity();
        contactPerson = merchantDetails.getContactPerson();
        areaCode = Integer.toString(merchantDetails.getAreaCode());
        merchantType = merchantDetails.getMerchantType();
        merchantClass = merchantDetails.getMerchantClass();
        districCode = Integer.toString(merchantDetails.getDistrictCode());
        updateedUser = merchantDetails.getUpdatedUser();
        UpdatedDate = merchantDetails.getUpdatedDate();
        //referenceID = merchantDetails.getReferenceID();
        vat = merchantDetails.getIsVat();
        isCredit = merchantDetails.getIsCredit();
        vatN = merchantDetails.getVatNo();
        creditDays = merchantDetails.getCreditDays();
        seqId = merchantDetails.getSyncId();
        System.out.println("Sync Id of Update class: "+seqId);
        syncId = seqId;

        setAreaName(areaCode);
        try {
            pathCode= Integer.parseInt(areaCode);
            setDistrict(districCode);

        }catch (Exception e){
            System.out.println("Path code parsing error");
        }


       /* tvPlacePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //lat= 7.178789199999999;
                //lon =79.89050979999999;
                System.out.println("second Lat: "+ lat + " lon "+lon);
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
                            new LatLng(lat,lon), new LatLng(lat,lon));
                    builder.setLatLngBounds(BOUNDS_MOUNTAIN_VIEW);
                    startActivityForResult(builder.build(MRA_UpdateActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });*/



        //set selected merchant details to views
        etMerchantName.setText(merchantName);
        etContact1.setText(contact1);
        etContact2.setText(contact2);
        etBuildingNo.setText(buildingNo+"");
        etAddress1.setText(address1);
        etAddress2.setText(address2);
        etCity.setText(city);
        etContactPerson.setText(contactPerson);
       // etReferenceID.setText(referenceID);
        etDiscountRate.setText(discountRate+"");
        if(vat==1){
            cbVat.setChecked(true);
            tilVat.setVisibility(View.VISIBLE);
            etVat.setText(vatN);
        }else{
            cbVat.setChecked(false);
            tilVat.setVisibility(View.GONE);
        }

        if(isCredit==1){
            cbCredit.setChecked(true);
            tilCredit.setVisibility(View.VISIBLE);
            etCreditDays.setText(creditDays);
        }else{
            cbCredit.setChecked(false);
            tilCredit.setVisibility(View.GONE);
        }

        //Set Images to ImageViews
        setImages(merchantDetails.getMerchantId(),merchantDetails.getSequenceId());

        tvImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*ActivityCompat.requestPermissions(
                        MerchantRegistrer.this,
                        new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE },
                        REQUEST_IMAGE_CAPTURE
                );*/


                try {
                    startActivityForResult(cameraPhoto.takePhotoIntent(),CAMERA_REQUEST1);
                } catch (IOException e) {
                    System.out.println("===============================================================");
                    System.out.println("Error while taking photo1");
                    e.printStackTrace();
                }

            }
        });

        tvImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  ActivityCompat.requestPermissions(
                        MerchantRegistrer.this,
                        new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE },
                        CAMERA_REQUEST
                );*/

                try {
                    startActivityForResult(cameraPhoto.takePhotoIntent(),CAMERA_REQUEST);
                } catch (IOException e) {
                    System.out.println("===============================================================");
                    System.out.println("Error while taking photo");
                    e.printStackTrace();
                }

            }
        });

        cbVat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    vat = 1;
                    tilVat.setVisibility(View.VISIBLE);
                }else{
                    vat = 0;
                    tilVat.setVisibility(View.GONE);
                }
            }
        });


        cbCredit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    isCredit = 1;
                    tilCredit.setVisibility(View.VISIBLE);
                }else{
                    isCredit = 0;
                    tilCredit.setVisibility(View.GONE);
                }
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* lat=lati;
                lon=loni;*/
               //Place Picker

                lat= getLat();
                lon = getLon();


                merchantName = etMerchantName.getText().toString();
                merchantName = merchantName.replace(" ","%20");

                contact1 = etContact1.getText().toString();
                contact1 = contact1.replace(" ","%20");

                contact2 = etContact2.getText().toString();
                contact2 = contact2.replace(" ","%20");

                address1 = etAddress1.getText().toString();
                address1 = address1.replace(" ","%20");

                address2 = etAddress2.getText().toString();
                address2 = address2.replace(" ","%20");

                buildingNo = etBuildingNo.getText().toString();
                buildingNo  = buildingNo.replace(" ","%20");

                city = etCity.getText().toString();
                city = city.replace(" ","%20");

                contactPerson = etContactPerson.getText().toString();
                contactPerson = contactPerson.replace(" ","%20");

                //referenceID = etReferenceID.getText().toString();
                referenceID = ""+0;
                //referenceID = referenceID.replace(" ","%20");


                vatNo = etVat.getText().toString();
                vatNo = vatNo.replace(" ","%20");

                creditDays = etCreditDays.getText().toString();
                creditDays = creditDays.replace(" ","%20");

              //  String placePicker = tvPlacePicker.getText().toString();

                discountRate = Integer.parseInt(etDiscountRate.getText().toString());


                String dis = tvDistrict.getText().toString();
                String are = tvArea.getText().toString();
               // String pat = tvPath.getText().toString();

                LocationManager manager = (LocationManager) getSystemService(getApplicationContext().LOCATION_SERVICE );
                boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                if(statusOfGPS==false){
                    Toast.makeText(getApplicationContext(),"GPS is disabled.. Please Check your GPS", Toast.LENGTH_LONG).show();
                    return;
                }

                if(lat == 0.0 && lon == 0.0){
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.invalied_location), Toast.LENGTH_LONG).show();
                    return;
                }


                if( merchantName.equals("")){
                    Toast.makeText(getApplicationContext(), "Enter Merchant Name", Toast.LENGTH_LONG).show();
                    return;
                }

                /*if(placePicker.equals("Pick your location")){
                    Toast.makeText(getApplicationContext(),"Please Pick Your Location", Toast.LENGTH_LONG).show();
                    return;
                }*/

                if(dis.equals("Select your district")){
                    Toast.makeText(getApplicationContext(), "You haven't selected district.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(are.equals("Select your area")){
                    Toast.makeText(getApplicationContext(), "You haven't selected your area.", Toast.LENGTH_SHORT).show();
                    return;
                }

               /* if(pat.equals("Select your path")){
                    Toast.makeText(getApplicationContext(), "You haven't selected a path.", Toast.LENGTH_SHORT).show();
                    return;
                }*/

                if(vat==1 && vatNo.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Enter VAT number", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(isCredit==1 && creditDays.isEmpty()){
                    //Toast.makeText(getApplicationContext(), "Enter VAT number", Toast.LENGTH_SHORT).show();
                    displayStatusMessage("Enter credit days between 0 and 45",2);
                    return;
                }
                if (isCredit==1 && !creditDays.isEmpty()){
                    try{
                        int cDays = Integer.parseInt(creditDays);
                        if (cDays>45){
                            displayStatusMessage("Enter credit days between 0 and 45",2);
                            return;
                        }
                    }catch (Exception e){
                        System.out.println(" ==== Error parsing credit days ====");
                        e.printStackTrace();
                    }
                }



                //Toast.makeText(getApplicationContext(), "outside: " + lat + "lon: " + lon , Toast.LENGTH_SHORT).show();
                int isActive = 1;

                Gson gson = new Gson();
                String getJson = SessionManager.pref.getString("user", "");
                LoginUser prefUser = gson.fromJson(getJson, LoginUser.class);
                int updateUserd = prefUser.getUserTypeId();

                DateManager dateManager = new DateManager();
                String date = dateManager.getDateWithTime();

                String enteredDate = date;
                enteredDate = enteredDate.replace(" ","%20");

                int discountRate = 0;
                String merchantClass = "A", merchantType = "B";

                int isSync = 0;
                String syncDate = date;
                syncDate = syncDate.replace(" ","%20");
                String updatedDate = date;
                updatedDate = updatedDate.replace(" ","%20");

                UpdateDefMerachant(sequenceId,merchantId,merchantName,contact1,contact2,lon,lat,isActive,discountRate,isSync,syncDate,buildingNo,address1,address2,city,contactPerson,areaCode,merchantType,merchantClass,districCode,updateUserd,referenceID,vat,vatNo,getApplicationContext(),syncId,isCredit,creditDays);

            }
        });

    }

    private void setImages(int merchantId, int sequenceId) {


        String sql = "SELECT * FROM "+TABLE_IMAGE+" WHERE "+ COL_IMAGE_MERCHANT_ID+ " = "+merchantId +" OR "+COL_IMAGE_SEQUENCE_ID+ " = "+sequenceId;
        SQLiteDatabase database = db.getWritableDatabase();
        Cursor cursor = database.rawQuery(sql,null);
        if (cursor.getCount() == 0) {
            System.out.println(" ============= No data of " + TABLE_IMAGE);

        } else {
            isUpdate = true;
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_IMAGE_MERCHANT_ID));
                System.out.println("ID "+id);

                try {

                    byte[] image = cursor.getBlob(cursor.getColumnIndex(DbHelper.COL_IMAGE1));
                    Bitmap bitmap = BitmapFactory.decodeByteArray(image,0,image.length);
                    ivPhoto1.setImageBitmap(bitmap);

                    byte[] image2 = cursor.getBlob(cursor.getColumnIndex(DbHelper.COL_IMAGE2));
                    Bitmap bitmap2 = BitmapFactory.decodeByteArray(image2,0,image2.length);
                    ivPhoto2.setImageBitmap(bitmap2);

                } catch (Exception e) {
                    Log.e("Update Merchant", "<loadImageFromDB> Error : " + e.getLocalizedMessage());
                }

            }
        }
    }

    private void setDistrict(String districCode) {


        String districtName="";
        Cursor cursor = null;
        SQLiteDatabase database = db.getReadableDatabase();

        String query = "SELECT  * FROM " + TABLE_DISTRICT+ " WHERE " +
                TABLE_AREA_DISTRICT_CODE+ " = " + districCode ;

        cursor = database.rawQuery(query, null);
        if (cursor.getCount() == 0) {
            System.out.println(" ============= No data related to the " + districCode);

        } else {
            while (cursor.moveToNext()) {
                districtName = cursor.getString(cursor.getColumnIndex(DbHelper.TABLE_DISTRICT_DISTRICT_NAME));
            }
        }
        if (!districtName.equals("")){
            tvDistrict.setText(districtName);
        }
    }

    private void setAreaName(String areaCode) {

        String pathName="";
        Cursor cursor = null;
        SQLiteDatabase database = db.getReadableDatabase();

        String query = "SELECT  * FROM " + TABLE_PATH+ " WHERE " +
                TABLE_PATH_DELIVERYPATHID+ " = " + areaCode ;

        cursor = database.rawQuery(query, null);
        if (cursor.getCount() == 0) {
            System.out.println(" ============= No data related to the " + areaCode);

        } else {
            while (cursor.moveToNext()) {
                pathName = cursor.getString(cursor.getColumnIndex(DbHelper.TABLE_PATH_PATHNAME));
            }
        }
        if (!pathName.equals("")){
            tvArea.setText(pathName);
        }
    }

    private void initializeViews(){

        //TextInputLayout
        tilVat = (TextInputLayout)findViewById(R.id.tilVat);
        tilCredit = (TextInputLayout)findViewById(R.id.tilCreidt);

        //Edit texts
        etMerchantName = (EditText) findViewById(R.id.etMerchantName);
        etContact1 = (EditText) findViewById(R.id.etContactNo1);
        etContact2= (EditText) findViewById(R.id.etContactNo2);
        etAddress1= (EditText) findViewById(R.id.etAddress1);
        etAddress2= (EditText) findViewById(R.id.etAddress2);;
        etBuildingNo= (EditText) findViewById(R.id.etBuildingNo);
        etCity= (EditText) findViewById(R.id.etCity);
        etVat= (EditText) findViewById(R.id.etVat);
        etCreditDays= (EditText) findViewById(R.id.etCredit);
        etContactPerson = (EditText) findViewById(R.id.etContactPerson);
       // etReferenceID = (EditText) findViewById(R.id.etReference);
        etDiscountRate = (EditText) findViewById(R.id.etDiscountRate);

        //TextViews
        tvPath = (TextView) findViewById(R.id.tvPath);
        tvArea = (TextView) findViewById(R.id.tvArea);
        tvDistrict = (TextView) findViewById(R.id.tvDistrict);
        //tvPlacePicker = (TextView) findViewById(R.id.tvPlacePicker);


        tvImage1 = (TextView)findViewById(R.id.tvImage1);
        tvImage2 = (TextView)findViewById(R.id.tvImage2);

        //ImageViews
        ivPhoto1 = (ImageView) findViewById(R.id.ivImage1);
        ivPhoto2 = (ImageView) findViewById(R.id.ivImage2);


        //Buttons
        btnUpdate = (Button) findViewById(R.id.btnUpdate);

        //Como Box
        cbVat  = (CheckBox) findViewById(R.id.cbVat);
        cbCredit  = (CheckBox) findViewById(R.id.cbCredit);
    }


//Update Methods
    private void UpdateDefMerachant(String sequenceId, final int merchantId, String merchantName,
                                    String contact1, String contact2, double lon, double lat,
                                    int isActive, int discountRate, int isSync, String syncDate,
                                    String buildingNo, String address1, String address2, String city,
                                    String contactPerson, String areaCode, String merchantType, String merchantClass,
                                    String districCode, int updateUserd, String referenceID, int vat, String vatNo,
                                    Context context, String id, int isCredit, String syncId) {


        System.out.println("Credit Days: "+ creditDays);
        if (creditDays.equals("")){
            creditDays="0";
        }
        final ContentValues cv = new ContentValues();
        cv.put("MerchantName",merchantName.replace("%20"," "));
        cv.put("ContactNo1",contact1);
        cv.put("ContactNo2",contact2);
        cv.put("Longitude",lon);
        cv.put("Latitude",lat);
        cv.put("IsActive",isActive);
        cv.put("DiscountRate",discountRate);
        cv.put("IsSync",isSync);
        cv.put("SyncDate",syncDate);
        cv.put("BuildingNo",buildingNo.replace("%20"," "));
        cv.put("Address1",address1.replace("%20"," "));
        cv.put("Address2",address2.replace("%20"," "));
        cv.put("City",city.replace("%20"," "));
        cv.put("ContactPerson",contactPerson.replace("%20"," "));
        cv.put("AreaCode",areaCode);
        cv.put("MerchantType",merchantType);
        cv.put("MerchantClass",merchantClass);
        cv.put("DistrictCode",districCode);
        cv.put("UpdatedUser",updateUserd);
        cv.put("ReferenceID",referenceID);
        cv.put("IsVat",vat);
        cv.put("VatNo",vatNo);
        cv.put("pathCode",pathCode);
        cv.put("SyncId",seqId);
        cv.put(TBL_MERCHANT_IS_CREDIT,isCredit);
        cv.put(TBL_MERCHANT_CREDIT_DAYS,creditDays);

        if(NetworkConnection.checkNetworkConnection(context)==true){
            String url = HTTPPaths.seriveUrl+"UpdateDefMerachant?merchantId="+merchantId+"&merchantName="
                    +merchantName+"&contactNo1="+contact1+"&contactNo2="
                    +contact2+"&longitude="+lon+"&latitude="+lat+"&isActive="+isActive+
                    "&discountRate="+discountRate+"&isSync="+isSync+"&syncDate="+syncDate+
                    "&buildingNo="+buildingNo+"&address1="+address1+"&address2="+address2+
                    "&city="+city+"&contactPerson="+contactPerson+"&areaCode="+areaCode+"&merchantType="
                    +merchantType+"&merchantClass="+merchantClass+"&districtCode="+districCode+"&updatedUser="
                    +updateUserd+"&referenceID="+referenceID+"&isVat="+vat+"&vatNo="+vatNo+"&syncId="+seqId+"&isCredit="+isCredit+"&creditDays="+creditDays;


            System.out.println("Update URL "+url);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JsonObject object = Json.parse(response).asObject();
                            int id = object.get("ID").asInt();
                            System.out.println("Response ID "+ id);
                            if(id==200){
                                getSequenceID(""+merchantId);
                                // Toast.makeText(MRA_UpdateActivity.this, "Successfully Updated..", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(),MerchantUpdateActivity.class);
                                startActivity(intent);
                                db.updateTable(""+merchantId,cv,DbHelper.TBL_MERCHANT_MERCHANT_ID +" =?",DbHelper.TABLE_MERCHANT);
                            }else{
                                Toast.makeText(MRA_UpdateActivity.this, "Update fail.. try again", Toast.LENGTH_LONG).show();
                                System.out.println("PAth update fail");
                                db.updateTable(""+merchantId,cv,DbHelper.TBL_MERCHANT_MERCHANT_ID +" =?",DbHelper.TABLE_MERCHANT);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("Update Error"+error.getMessage());
                            db.updateTable(""+merchantId,cv,DbHelper.TBL_MERCHANT_MERCHANT_ID +" =?",DbHelper.TABLE_MERCHANT);
                            Toast.makeText(MRA_UpdateActivity.this, "Something went wrong. Please Try Again"+ error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
            );
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        }else{
            boolean success= db.updateTable(""+merchantId,cv,DbHelper.TBL_MERCHANT_MERCHANT_ID +" =?",DbHelper.TABLE_MERCHANT);
            if (success==true){
                System.out.println("Updated and save to local SQLite DB");
                Intent intent = new Intent(getApplicationContext(),MerchantUpdateActivity.class);
                startActivity(intent);
            }else{
                System.out.println("Update fail SQLite DB");
                Toast.makeText(MRA_UpdateActivity.this, "Update fail.. try again", Toast.LENGTH_LONG).show();
            }
        }

        System.out.println("merchant ID: "+merchantId+" Sync: "+syncId);

        saveImageToDB(merchantId,seqId);


    }

    private void getSequenceID(final String s) {
        String url = HTTPPaths.seriveUrl+"GetDeliveryLocation?merchantID="+s;
        System.out.println("Path URL" +url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonObject object = Json.parse(response).asObject();
                        int id = object.get("ID").asInt();
                        System.out.println("Path ID "+ id);
                        if(id==200){
                            String newUrl = response.replace("{\"Data\":\"", "{\"Data\": ");
                            String new2Url = newUrl.replace("\",\"ID\":200}", "}");
                            String new3Url = new2Url.replace("\\", "");
                            if (response != null) {
                                try {
                                    JSONObject jsonObj_main = new JSONObject(new3Url);
                                    JSONObject jobj = jsonObj_main.getJSONObject("Data");
                                        String SequenceID = jobj.getString("SequenceID");
                                        updatePath(s,SequenceID);
                                }catch (JSONException e){
                                    Toast.makeText(MRA_UpdateActivity.this, "Json Error: ", Toast.LENGTH_LONG).show();
                                    System.out.println("Json Error"+ e.getMessage());
                                }
                            }
                        }else{

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Path reg error"+error.getMessage());
                        Toast.makeText(MRA_UpdateActivity.this, "Something went wrong.."+ error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    private void updatePath(String merchantId,String sequenceID) {
        String pathID = Integer.toString(pathCode);

        String url = HTTPPaths.seriveUrl+"UpdateDeliveryLocation?merchantID="+merchantId+"&deliveryPathID="+pathID+"&sequenceID="+sequenceID;
        System.out.println("Path URL" +url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonObject object = Json.parse(response).asObject();
                        int id = object.get("ID").asInt();
                        System.out.println("Path ID "+ id);
                        if(id==200){
                            Toast.makeText(MRA_UpdateActivity.this, "Successfully Updated ", Toast.LENGTH_LONG).show();
                            System.out.println("Merchant path Update is successful");

                        }else{
                            Toast.makeText(MRA_UpdateActivity.this, "Merchant Update fail.. try again", Toast.LENGTH_LONG).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Path reg error"+error.getMessage());
                        Toast.makeText(MRA_UpdateActivity.this, "Something went wrong.."+ error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }



//Load All Spinners

    private void loadArea(final ArrayList<Area> areaArrayList) {
    areaItems.clear();
    for (int i = 0; i < areaArrayList.size(); i++) {
        areaItems.add(areaArrayList.get(i).getAreaName());
    }
    areaSpinnerDialog=new SpinnerDialog(MRA_UpdateActivity.this,areaItems,"Select or Search District",R.style.DialogAnimations_SmileWindow);

    areaSpinnerDialog.bindOnSpinerListener(new OnSpinerItemClick()
    {
        @Override
        public void onClick(String item, int position)
        {

            tvArea.setText(item);
            selectedArea =item;

            for (int  i = 0 ;i<areaArrayList.size();i++){

                if (areaArrayList.get(i).getAreaName().equals(selectedArea)){
                    areaCode = areaArrayList.get(i).getAreaCode();
                }
            }

        }
    });
}

    private void loadDistrict(final ArrayList<DistrictMerchant> districtMerchantArrayList) {
        districtItems.clear();
        for (int i = 0; i < districtMerchantArrayList.size(); i++) {
            districtItems.add(districtMerchantArrayList.get(i).getDistrictName());
        }
        districtSpinnerDialog=new SpinnerDialog(MRA_UpdateActivity.this,districtItems,"Select or Search District",R.style.DialogAnimations_SmileWindow);

        districtSpinnerDialog.bindOnSpinerListener(new OnSpinerItemClick()
        {
            @Override
            public void onClick(String item, int position)
            {
                tvDistrict.setText(item);
                selectedDistrict =item;

                for (int  i = 0 ;i<districtMerchantArrayList.size();i++){

                    if (districtMerchantArrayList.get(i).getDistrictName().equals(selectedDistrict)){
                        districCode = districtMerchantArrayList.get(i).getDistrictCode();
                    }
                }

            }
        });
    }

    private void loadPath(final ArrayList<DeliveryPath> pathArrayList) {
        pathItems.clear();
        for (int i = 0; i < pathArrayList.size(); i++) {
            pathItems.add(pathArrayList.get(i).getPathName());
        }
        pathSpinnerDialog=new SpinnerDialog(MRA_UpdateActivity.this,pathItems,"Select or Search Path",R.style.DialogAnimations_SmileWindow);

        pathSpinnerDialog.bindOnSpinerListener(new OnSpinerItemClick()
        {
            @Override
            public void onClick(String item, int position)
            {

                tvArea.setText(item);
                selectedPath =item;

                for (int  i = 0 ;i<pathArrayList.size();i++){

                    if (pathArrayList.get(i).getPathName().equals(selectedPath)){
                        pathCode = pathArrayList.get(i).getDeliveryPathId();
                    }
                }

            }
        });

    }



//Geo Location
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

            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            lat = mLastLocation.getLatitude();
            lon = mLastLocation.getLongitude();
            //Toast.makeText(this, "insidelat: " + lat + "lon: " + lon , Toast.LENGTH_SHORT).show();
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

    public double getLon(){
        return lon;
    }

    public double getLat(){
        return lat;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                StringBuilder stBuilder = new StringBuilder();
                String placename = String.format("%s", place.getName());
               // tvPlacePicker.setText(placename);
                String latitude = String.valueOf(place.getLatLng().latitude);
                lati = place.getLatLng().latitude;
                String longitude = String.valueOf(place.getLatLng().longitude);
                loni = place.getLatLng().longitude;
                String address = String.format("%s", place.getAddress());
                stBuilder.append("Name: ");
                stBuilder.append(placename);
                stBuilder.append("\n");
                stBuilder.append("Latitude: ");
                stBuilder.append(latitude);
                stBuilder.append("\n");
                stBuilder.append("Logitude: ");
                stBuilder.append(longitude);
                stBuilder.append("\n");
                stBuilder.append("Address: ");
                stBuilder.append(address);
                //Toast.makeText(this, stBuilder.toString(), Toast.LENGTH_LONG).show();
            }
        }*/

        if (resultCode == RESULT_OK){

            if (requestCode==CAMERA_REQUEST){
                String photoPath = cameraPhoto.getPhotoPath();
                //selectedPhoto = photoPath;
                System.out.println("Photo path "+photoPath);

                try {
                    Bitmap bitmap = ImageLoader.init().from(photoPath).requestSize(200,200).getBitmap();
                    ivPhoto2.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    System.out.println("===============================================================");
                    System.out.println("Error while setting photo2");
                    e.printStackTrace();

                }
            }

        }


        if (resultCode == RESULT_OK){

            if (requestCode==CAMERA_REQUEST1){

                String photoPath = cameraPhoto.getPhotoPath();
                //selectedPhoto = photoPath;
                System.out.println("Photo path1 "+photoPath);

                try {
                    Bitmap bitmap = ImageLoader.init().from(photoPath).requestSize(200,200).getBitmap();
                    ivPhoto1.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    System.out.println("===============================================================");
                    System.out.println("Error while setting photo1");
                    e.printStackTrace();
                }
            }
        }
    }

    //Merchant Image
    private void saveImageToDB(final int merchantId, final String seqId) {




        System.out.println("Entering Image Sync DB Method");
        System.out.println("merchant ID: "+merchantId+" SyncID: "+seqId);
        try {
            MerchantImage merchantImage = new MerchantImage(seqId,merchantId,
                    imageToBitmap(ivPhoto1),imageToBitmap(ivPhoto2),0);

            final DbHelper db = new DbHelper(getApplicationContext());
            ContentValues cv = merchantImage.getMerchantImageContentValues();
            int afectedRows=0;

            try{

                if (!isUpdate){
                    boolean s = db.insertDataAll(cv, TABLE_IMAGE);

                    if (s){
                        System.out.println("Image is save to database");
                    }else {
                        System.out.println("Image is not save to database");
                    }
                }else{
                    SQLiteDatabase database = db.getWritableDatabase();
                    if (merchantId!=0){
                        afectedRows = database.update(TABLE_IMAGE,cv,COL_IMAGE_MERCHANT_ID +" = ? " ,new String[]{""+merchantId});
                    }else{
                        afectedRows = database.update(TABLE_IMAGE,cv,COL_IMAGE_SEQUENCE_ID +" = ? " ,new String[]{""+seqId});
                    }

                    if(afectedRows>0){
                        System.out.println("Image  updated  of merchant "+merchantId);
                    }else{
                        System.out.println("Image  not  updated  of merchant "+merchantId);
                    }
                    database.close();
                }


            }catch (Exception e){
                System.out.println("Error at Image Sync: "+e.getMessage());
            }

            if (merchantId!=0){
                Gson gson = new Gson();
                String jsonString = gson.toJson(merchantImage,MerchantImage.class);
                System.out.println("\n\n\nImage Json: "+jsonString+" \n\n");

                String url = HTTPPaths.seriveUrl+"AddMerchantImage";

                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(jsonString);

                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("Json Error in (Sync Image): " + e.getMessage());
                }

                if (NetworkConnection.checkNetworkConnection(getApplicationContext())==true){
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObj,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    System.out.println("=========response - "+response.toString());
                                    JsonObject object = Json.parse(String.valueOf(response)).asObject();
                                    int id = object.get("ID").asInt();

                                    if (id==200) {
                                        System.out.println("Image Sync successful");
                                        ContentValues cv = new ContentValues();
                                        cv.put(COL_IMAGE_ISSYNC,1);
                                        SQLiteDatabase database = db.getWritableDatabase();
                                        try{
                                            int afectedRows = database.update(TABLE_IMAGE,cv,COL_IMAGE_SEQUENCE_ID +" = ? " ,new String[]{""+seqId});
                                            if(afectedRows>0){
                                                System.out.println("Is Sync is updated as 1 of merchant "+merchantId);
                                            }
                                        }catch (SQLException e){
                                            System.out.println("Error at data update at IsSync Image ");
                                        }finally {
                                            database.close();
                                        }
                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    System.out.println("@@@@@ (Image Sync)  error - "+error.toString());
                                }
                            });
                    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
                }
            }

        } finally {

        }
    }

    private byte[] imageToBitmap(ImageView ivPhoto) {
        Bitmap bitmap = ((BitmapDrawable)ivPhoto.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] byteArray = stream.toByteArray();

        return byteArray;
    }

    private void displayStatusMessage(String s,int colorValue) {

        AlertDialog.Builder builder = null;
        View view = null;
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

        builder = new AlertDialog.Builder(MRA_UpdateActivity.this);
        view = getLayoutInflater().inflate(R.layout.layout_for_custom_message, null);

        tvOk = (TextView) view.findViewById(R.id.tvOk);
        tvMessage = (TextView) view.findViewById(R.id.tvMessage);
        imageView = (ImageView)view.findViewById(R.id.iv_status_k);

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
}
