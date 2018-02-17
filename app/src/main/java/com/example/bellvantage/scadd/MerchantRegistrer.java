package com.example.bellvantage.scadd;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.example.bellvantage.scadd.swf.MerchantClass;
import com.example.bellvantage.scadd.swf.MerchantDetails;
import com.example.bellvantage.scadd.swf.MerchantImage;
import com.example.bellvantage.scadd.swf.MerchantType;
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

import static com.example.bellvantage.scadd.DB.DbHelper.COL_IMAGE1;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_IMAGE2;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_IMAGE_ISSYNC;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_IMAGE_SEQUENCE_ID;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_DELIVERY_LOCATIONS;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_DELIVERY_LOCATION_DELIVERY_PATH_ID;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_DELIVERY_LOCATION_MERCHANT_ID;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_IMAGE;

public class MerchantRegistrer extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    Button btnRegister;
    EditText etMerchantName,etContact1,etContact2,etAddress1,etAddress2,etBuildingNo,etCity,etVat,etCredit,etContactPerson,etReferenceID;
    TextInputLayout tilVat,tilCredit;
    String merchantName,contact1,contact2,buildingNo,address1,address2,city,vatNo,creditDays;
    int enteredUser,vat=0,pathCode;
    ArrayList<DistrictMerchant> districtMerchantArrayList = new ArrayList<>();
    ArrayList<Area> areaArrayList = new ArrayList<>();
    ArrayList<DeliveryPath> pathArrayList = new ArrayList<>();
    ArrayList<MerchantClass> merchantClassArrayList = new ArrayList<>();
    ArrayList<MerchantType> merchantTypeArrayList = new ArrayList<>();
    CheckBox cbVat,cbCredit;
    String selectedDistrict,selectedArea,selectedPath,contactPerson,referenceID,merchantClass,merchantType;
    String districCode, areaCode,syncId,merchantClassId;
    TextView tvPath,tvArea,tvDistrict,tvMerchantType,tvImage1,tvImage2;//tvMerchantClass
    //Spinners
    Spinner spArea,spDistrict,spPath;

    ImageView ivPhoto1,ivPhoto2;
    static  final int REQUEST_IMAGE_CAPTURE = 999,REQUEST_GALARY_CAPTURE1 = 888;
    Cam cameraPhoto;
    String selectedPhoto1=null,selectedPhoto2=null;

    final  int CAMERA_REQUEST= 13323;
    final  int CAMERA_REQUEST1= 13324;


    int isCredit = 0;

    //Geo Location
    private GoogleApiClient mGoogleApiClient;
    private int PLACE_PICKER_REQUEST = 1;
    private Location mLastLocation;
    private  double lat;
    private  double lon;
    private DbHelper db;
    private SyncManager syncManager;

    //Spinner Dailo
    ArrayList<String> pathItems=new ArrayList<>();
    ArrayList<String> districtItems=new ArrayList<>();
    ArrayList<String> merchantClassItem=new ArrayList<>();
    ArrayList<String> merchantTypeItem=new ArrayList<>();
    ArrayList<String> areaItems=new ArrayList<>();
    SpinnerDialog pathSpinnerDialog,areaSpinnerDialog,districtSpinnerDialog,merchantClassSpinnerDialog,merchantTypeSpinnerDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant_registrer);

        this.db = new DbHelper(getApplicationContext());
       /* //Geo Location
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .enableAutoManage(this, this)
                    .build();
        }*/ // Place picker

        cameraPhoto =new Cam(MerchantRegistrer.this);
        //Geo Location
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        Gson gson = new Gson();
        String getJson = SessionManager.pref.getString("user", "");
        LoginUser prefUser = gson.fromJson(getJson, LoginUser.class);
        enteredUser =prefUser.getUserTypeId();

        //initialize views
        initializeViews();

        //set data to spinners
        syncManager = new SyncManager(getApplicationContext());
        areaArrayList = syncManager.getAllArea();
        districtMerchantArrayList = syncManager.getAllDistrict();
        pathArrayList=syncManager.getAllPath();
        merchantClassArrayList=syncManager.getAllMerchantClass();
        merchantTypeArrayList=syncManager.getAllMerchantTypes();
        System.out.println("<OnCrete> Customer Class array size: "+ merchantClassArrayList.size());
        loadDistrict(districtMerchantArrayList);
        //loadArea(areaArrayList);
        loadPath(pathArrayList);
        loadType(merchantTypeArrayList);

        /*if (merchantClassArrayList.size()>0){
            loadMerchantClass(merchantClassArrayList);
        }*/

        tvArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pathSpinnerDialog.showSpinerDialog();
            }
        });
       /* tvPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pathSpinnerDialog.showSpinerDialog();
            }
        });*/

       //Merchan Class
        /*tvMerchantClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                merchantClassSpinnerDialog.showSpinerDialog();
            }
        });*/

        tvMerchantType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                merchantTypeSpinnerDialog.showSpinerDialog();
            }
        });

        tvDistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                districtSpinnerDialog.showSpinerDialog();
            }
        });

        /*tvPlacePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                *//*lat= 7.178789199999999;
                lon =79.89050979999999;*//*
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(MerchantRegistrer.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });*/


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

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                LocationManager manager = (LocationManager) getSystemService(getApplicationContext().LOCATION_SERVICE );
                boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                if(statusOfGPS==false){
                    //Toast.makeText(getApplicationContext(),"GPS is disabled.. Please Check your GPS", Toast.LENGTH_LONG).show();
                    displayStatusMessage("GPS is disabled..\n Please Check your GPS",3);
                    return;
                }

                lat=getLat();
                lon=getLon();


                merchantName = etMerchantName.getText().toString();
                merchantName = merchantName.replace(" ","%20");

                if( merchantName.equals("")){
                   // Toast.makeText(MerchantRegistrer.this, "Enter Merchant Name", Toast.LENGTH_LONG).show();
                    displayStatusMessage("Enter Customer Name",3);
                    return;
                }


                contact1 = etContact1.getText().toString();
                //validateTelephoneNumber(contact1, "Contact Number 1");
                if (contact1.length()>10 || contact1.length()<10 || contact1.contains(" ")){
                    displayStatusMessage("Invalid Telephone Number\n Use this Format 0712345678 for "+ "Contact Number 1",2);
                    return;
                }
                //contact1 = contact1.replace(" ","%20");

                contact2 = etContact2.getText().toString();
                if (contact2.length()>10 || contact2.length()<10 || contact2.contains(" ")){
                    displayStatusMessage("Invalid Telephone Number\n Use this Format 0712345678 for "+ "Contact Number 2",2);
                    return;
                }


                address1 = etAddress1.getText().toString();
                address1 = address1.replace(" ","%20");

                address2 = etAddress2.getText().toString();
                address2 = address2.replace(" ","%20");

                buildingNo = etBuildingNo.getText().toString();
                buildingNo = buildingNo.replace(" ","%20");

                city = etCity.getText().toString();
                city = city.replace(" ","%20");

                contactPerson = etContactPerson.getText().toString();
                contactPerson = contactPerson.replace(" ","%20");

                //referenceID = etReferenceID.getText().toString();
//                referenceID = referenceID.replace(" ","%20");

                vatNo="VAT";
                vatNo = etVat.getText().toString();
                vatNo = vatNo.replace(" ","%20");


                creditDays = etCredit.getText().toString();
                creditDays = creditDays.replace(" ","%20");

                String dis = tvDistrict.getText().toString();
                String are = tvArea.getText().toString();
//              String pat = tvPath.getText().toString();
                //String mClass = tvMerchantClass.getText().toString();
                String mType= tvMerchantType.getText().toString();

               // String placePicker = tvPlacePicker.getText().toString();



                //Toast.makeText(MerchantRegistrer.this, "la "+ lat +"\nlon "+lon , Toast.LENGTH_SHORT).show();
                System.out.println("la "+ lat +"\nlon "+lon );

                if(buildingNo.equals("")){
                    //Toast.makeText(getApplicationContext(),"Please Pick Your Location", Toast.LENGTH_LONG).show();
                    displayStatusMessage("Enter Building Number",2);
                    return;
                }

                if(address1.equals("")){
                    displayStatusMessage("Enter Address 1",2);
                    return;
                }

                if(address2.equals("")){
                    displayStatusMessage("Enter Address 2",2);
                    return;
                }

                /*if(placePicker.equals("Pick your location *")){
                    //Toast.makeText(getApplicationContext(),"Please Pick Your Location", Toast.LENGTH_LONG).show();
                    displayStatusMessage("Please Pick Your Location",2);
                    return;
                }*/

                if(dis.equals("Select your district *")){
                    //Toast.makeText(getApplicationContext(), "You haven't selected district.", Toast.LENGTH_SHORT).show();
                    displayStatusMessage("You haven't selected district.",2);
                    return;
                }

                if(are.equals("Select your area *")){
                    //Toast.makeText(getApplicationContext(), "You haven't selected your area.", Toast.LENGTH_SHORT).show();
                    displayStatusMessage("You haven't selected your area.",2);
                    return;
                }
               /* if(mClass.equals("Select a class *")){
                    //Toast.makeText(getApplicationContext(), "You haven't selected merchant class.", Toast.LENGTH_SHORT).show();
                    displayStatusMessage("You haven't selected Customer class.",2);
                    return;
                }*/
                if(mType.equals("Select a type *")){
                   // Toast.makeText(getApplicationContext(), "You haven't selected your area.", Toast.LENGTH_SHORT).show();
                    displayStatusMessage("You haven't selected Customer type.",2);
                    return;
                }

                /*if(pat.equals("Select your path")){
                    Toast.makeText(getApplicationContext(), "You haven't selected a path.", Toast.LENGTH_SHORT).show();
                    return;
                }*/

                if(vat==1 && vatNo.isEmpty()){
                    //Toast.makeText(getApplicationContext(), "Enter VAT number", Toast.LENGTH_SHORT).show();
                    displayStatusMessage("Enter VAT number",2);
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

                if(lat == 0.0 && lon == 0.0){
                    //Toast.makeText(getApplicationContext(),getResources().getString(R.string.invalied_location), Toast.LENGTH_LONG).show();
                    displayStatusMessage(getResources().getString(R.string.invalied_location),2);
                    return;
                }



               String image1 = tvImage1.getText().toString();
               String image2 = tvImage2.getText().toString();


                if (image1.equalsIgnoreCase("Capture Image 1 *")||image2.equalsIgnoreCase("Capture Image 2 *")){
                    displayStatusMessage("Please Capture 2 Images",2);
                    return;
                }

//                    saveImageToDB();


                int isActive = 1;

                DateManager dateManager = new DateManager();
                String date = dateManager.getDateWithTime();
                date = date.replace(" ","%20");
                String enteredDate = date;
                int discountRate = 0;


                int isSync = 0;
                String syncDate = date;

                //generate syncId
                long syncIdDate = dateManager.todayMillsec();
                SyncManager syncManager = new SyncManager(getApplicationContext());
                String lastSequenceId = syncManager.getLastMerchantID();
                syncId = lastSequenceId+enteredUser+syncIdDate;

                MerchantDetails merchantDetails = new MerchantDetails(0,0,merchantName,contact1,contact2,Double.toString(lon),
                        Double.toString(lon),isActive,enteredDate,
                        Integer.toString(enteredUser),discountRate,isSync,syncDate,buildingNo,address1,address2,city,contactPerson,
                        Integer.parseInt(areaCode),merchantType,merchantClass,Integer.parseInt(districCode),Integer.toString(enteredUser),
                        enteredDate,referenceID,vat,vatNo,pathCode,syncId,isCredit,creditDays);

                if (NetworkConnection.checkNetworkConnection(getApplicationContext())){
                    registerMerchant(merchantName,contact1,contact2,lon,lat,isActive,enteredDate,discountRate,buildingNo,address1,address2,city,contactPerson,areaCode,merchantType,merchantClass,districCode,referenceID,vat,vatNo,merchantDetails,syncId,isCredit,creditDays);
                }else{
                    insertMerchantToLocalDb(merchantDetails);
                }

                Intent intent = new Intent(getApplicationContext(),MerchantActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    private void insertMerchantToLocalDb(MerchantDetails merchant) {

        merchant.setMerchantName(merchant.getMerchantName().replace("%20"," "));
        merchant.setBuildingNo(merchant.getBuildingNo().replace("%20"," "));
        merchant.setAddress1(merchant.getAddress1().replace("%20"," "));
        merchant.setAddress2(merchant.getAddress2().replace("%20"," "));
        merchant.setContactPerson(merchant.getContactPerson().replace("%20"," "));

        ContentValues cv = merchant.getMerchntDetailsContentValues();
        Boolean success = db.insertDataAll(cv, DbHelper.TABLE_MERCHANT);

        if (!success) {
            System.out.println("Data is not inserted to " + DbHelper.TABLE_MERCHANT);
            Toast.makeText(this, "Customer Registration fail.. try again", Toast.LENGTH_SHORT).show();
            //saveImageToDB(merchant);

        } else {
            saveImageToDB(merchant);
            System.out.println("Customer insert successful...");
            Toast.makeText(this, "Customer Registration is successful", Toast.LENGTH_SHORT).show();

            fieldsClear();
        }
    }

    //Register Merchant
    private void registerMerchant(String merchantName, String contact1, String contact2, double lon, double lat, int isActive, final String enteredDate, int discountRate, String buildingNo, String address1, String address2, String city, String contactPerson, String areaCode, String merchantType, String merchantClass, String districCode, String referenceID, int vat, String vatNo, final MerchantDetails merchantDetails, String id, int isCredit, String syncId) {

        if (creditDays.equals("")){
            creditDays="0";
        }

        String url = HTTPPaths.seriveUrl+"SaveDefMerachant?merchantName="+merchantName+
                "&contactNo1="+contact1+"&contactNo2="+contact2+"&longitude="+
                lon+"&latitude="+lat+"&isActive="+isActive+"&enteredUser="+enteredUser+
                "&discountRate="+discountRate+"&buildingNo="+buildingNo+"&address1="+address1+
                "&address2="+address2+"&city="+city+"&contactPerson="+contactPerson+
                "&areaCode="+pathCode+"&merchantType="+merchantType+"&merchantClass="+merchantClass+
                "&districtCode="+districCode+"&referenceID="+referenceID+"&isVat="+vat+"&vatNo="+vatNo+"&syncId="+syncId+"&isCredit="+isCredit+"&creditDays="+creditDays;

        System.out.println("Registration URL " + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonObject object = Json.parse(response).asObject();
                        int id = object.get("ID").asInt();
                        String MerchantID = object.get("Data").asString();
                        System.out.println("ID of path "+id);
                        if(id==200){
                            merchantDetails.setIsSync(1);
                            merchantDetails.setMerchantId(Integer.parseInt(MerchantID));
                            fieldsClear();
                            System.out.println("Merchant Registration is successful");
                            insertMerchantToLocalDb(merchantDetails);
                            registerPath(MerchantID);


                        }else{
                            Toast.makeText(MerchantRegistrer.this, "Merchant Registration fail.. try again", Toast.LENGTH_LONG).show();
                            insertMerchantToLocalDb(merchantDetails);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Customer Registrer error "+error.getMessage());
                        Toast.makeText(MerchantRegistrer.this, "Something went wrong..", Toast.LENGTH_SHORT).show();
                        insertMerchantToLocalDb(merchantDetails);
                    }
                }
        );
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }

    private void registerPath(String merchantId) {

        String pathID = Integer.toString(pathCode);

        ContentValues cvDeliveryLocations = new ContentValues();
        cvDeliveryLocations.put(TABLE_DELIVERY_LOCATION_MERCHANT_ID,merchantId);
        cvDeliveryLocations.put(TABLE_DELIVERY_LOCATION_DELIVERY_PATH_ID,pathID);
        boolean s = db.insertDataAll(cvDeliveryLocations,TABLE_DELIVERY_LOCATIONS);
        if (s){
            System.out.println(merchantId+ " Merchant is added to the path");
        }else{
            System.out.println(merchantId+ " Merchant is not added to the path");
        }


        String url = HTTPPaths.seriveUrl+"SaveDeliveryLocation?merchantID="+merchantId+"&deliveryPathID="+pathID;
        System.out.println("Path URL" +url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonObject object = Json.parse(response).asObject();
                        int id = object.get("ID").asInt();
                        System.out.println("Path ID "+ id);
                        if(id==200){
                            Toast.makeText(MerchantRegistrer.this, "Successfully registered ", Toast.LENGTH_LONG).show();
                            System.out.println("Customer path Registration is successful");
                            fieldsClear();
                        }else{
                            Toast.makeText(MerchantRegistrer.this, "Merchant Registration fail.. try again", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("Path reg error"+error.getMessage());
                        Toast.makeText(MerchantRegistrer.this, "Something went wrong.."+ error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }


//Load Spinners

    private void loadMerchantClass(final ArrayList<MerchantClass> merchantClassArrayList) {
        merchantClassItem.clear();
        System.out.println("<Load Merchant Class> Merchant Class array size: "+ merchantClassArrayList.size());
        for (int i = 0; i < merchantClassArrayList.size(); i++) {
            merchantClassItem.add(merchantClassArrayList.get(i).getClassDescription());
        }
        merchantClassSpinnerDialog=new SpinnerDialog(MerchantRegistrer.this,merchantClassItem,
                "Select or Search Customer Class",R.style.DialogAnimations_SmileWindow);

        merchantClassSpinnerDialog.bindOnSpinerListener(new OnSpinerItemClick()
        {
            @Override
            public void onClick(String item, int position)
            {

                //tvMerchantClass.setText(item);
                merchantClass =item;

                for (int  i = 0 ;i<merchantClassArrayList.size();i++){

                    if (merchantClassArrayList.get(i).getClassDescription().equals(merchantClass)){
                        merchantClassId = merchantClassArrayList.get(i).getClassDescription();
                    }
                }
            }
        });
    }

    private void loadType(final ArrayList<MerchantType> merchantTypeArrayList) {
        merchantTypeItem.clear();
        System.out.println("<Load Merchant Type> Merchant Type array size: "+ merchantTypeArrayList.size());
        for (int i = 0; i < merchantTypeArrayList.size(); i++) {
            merchantTypeItem.add(merchantTypeArrayList.get(i).getTypeDescription());
        }
        merchantTypeSpinnerDialog=new SpinnerDialog(MerchantRegistrer.this,merchantTypeItem,
                "Select or Search Customer Type",R.style.DialogAnimations_SmileWindow);

        merchantTypeSpinnerDialog.bindOnSpinerListener(new OnSpinerItemClick()
        {
            @Override
            public void onClick(String item, int position)
            {

                tvMerchantType.setText(item);

                for (int  i = 0 ;i<merchantTypeArrayList.size();i++){

                    if (merchantTypeArrayList.get(i).getTypeDescription().equals(item)){
                        merchantType = merchantTypeArrayList.get(i).getTypeCode();
                    }
                }
            }
        });
    }

    private void loadArea(final ArrayList<Area> areaArrayList) {
    areaItems.clear();
    for (int i = 0; i < areaArrayList.size(); i++) {
        areaItems.add(areaArrayList.get(i).getAreaName());
    }
    areaSpinnerDialog=new SpinnerDialog(MerchantRegistrer.this,areaItems,
            "Select or Search District",R.style.DialogAnimations_SmileWindow);

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
        districtSpinnerDialog=new SpinnerDialog(MerchantRegistrer.this,districtItems,"Select or Search District",R.style.DialogAnimations_SmileWindow);

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

        pathSpinnerDialog=new SpinnerDialog(MerchantRegistrer.this,pathItems,"Select or Search Area",R.style.DialogAnimations_SmileWindow);

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
                        if (pathCode!=0){
                            areaCode = String.valueOf(pathCode);
                        }
                        areaCode = 0+"";
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
            System.out.println("Lattitude on conected : "+lat + " Lon: "+ lon);
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

    private void initializeViews(){

        //TextInputLayout
        tilVat = (TextInputLayout)findViewById(R.id.tilVat);
        tilCredit = (TextInputLayout)findViewById(R.id.tilCreidt);


        //Edit texts
        etMerchantName = (EditText) findViewById(R.id.etMerchantName);
        etContact1 = (EditText) findViewById(R.id.etContactNo1);
        etContact2= (EditText) findViewById(R.id.etContactNo2);
        etAddress1= (EditText) findViewById(R.id.etAddress1);
        etAddress2= (EditText) findViewById(R.id.etAddress2);
        etBuildingNo= (EditText) findViewById(R.id.etBuildingNo);
        etCity= (EditText) findViewById(R.id.etCity);
        etVat= (EditText) findViewById(R.id.etVat);
        etCredit= (EditText) findViewById(R.id.etCredit);
        etContactPerson = (EditText) findViewById(R.id.etContactPerson);
        //etReferenceID = (EditText) findViewById(R.id.etReference);


        //Buttons
        btnRegister = (Button) findViewById(R.id.btnRegister);

        //Como Box
        cbVat  = (CheckBox) findViewById(R.id.cbVat);
        cbCredit  = (CheckBox) findViewById(R.id.cbCredit);

        //TextViews
        tvPath = (TextView) findViewById(R.id.tvPath);
        tvArea = (TextView) findViewById(R.id.tvArea);
        tvDistrict = (TextView) findViewById(R.id.tvDistrict);
       // tvPlacePicker = (TextView) findViewById(R.id.tvPlacePicker);
       // tvMerchantClass = (TextView) findViewById(R.id.tvMerchantClass);
        tvMerchantType = (TextView) findViewById(R.id.tvMerchantType);
        tvImage1 = (TextView)findViewById(R.id.tvImage1);
        tvImage2 = (TextView)findViewById(R.id.tvImage2);

        //ImageViews
        ivPhoto1 = (ImageView) findViewById(R.id.ivImage1);
        ivPhoto2 = (ImageView) findViewById(R.id.ivImage2);


    }

    public  void fieldsClear(){
        etMerchantName.setText("");
        etContact1.setText("");
        etContact2.setText("");
        etContactPerson.setText("");
        etBuildingNo.setText("");
        etAddress1.setText("");
        etAddress2.setText("");
        etCity.setText("");
        //etReferenceID.setText("");
        etVat.setText("");
        etCredit.setText("");
        tvArea.setText("Select your area *");
        tvDistrict.setText("Select your district *");
       // tvPlacePicker.setText("Pick your location *");
       // tvMerchantClass.setText("Select a class *");
        tvMerchantType.setText("Select a type *");
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
                //tvPlacePicker.setText(placename);
                String latitude = String.valueOf(place.getLatLng().latitude);
                lat = place.getLatLng().latitude;
                String longitude = String.valueOf(place.getLatLng().longitude);
                lon = place.getLatLng().longitude;
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
               // Toast.makeText(this, stBuilder.toString(), Toast.LENGTH_LONG).show();
            }
        }*/

        if (resultCode == RESULT_OK){

            if (requestCode==CAMERA_REQUEST){
                String photoPath = cameraPhoto.getPhotoPath();
                System.out.println("Photo path2 "+photoPath);

                try {
                    Bitmap bitmap = ImageLoader.init().from(photoPath).requestSize(200,150).getBitmap();
                    ivPhoto2.setImageBitmap(bitmap);
                    tvImage2.setText("Update Image 2");
                    // selectedPhoto1 = ImageBase64.encode(bitmap);
                    //imageToBitmap(ivPhoto2);


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
                System.out.println("Photo path1 "+photoPath);

                try {
                    Bitmap bitmap = ImageLoader.init().from(photoPath).requestSize(200,200).getBitmap();
                    ivPhoto1.setImageBitmap(bitmap);
                    tvImage1.setText("Update Image 1");
                } catch (FileNotFoundException e) {
                    System.out.println("===============================================================");
                    System.out.println("Error while setting photo1");
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }

    //Check if the user has the camera
    private boolean hasCamera() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
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

        builder = new AlertDialog.Builder(MerchantRegistrer.this);
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

    //Merchant Image
    private void saveImageToDB(final MerchantDetails merchant) {

        System.out.println("Entering Image Sync DB Method");
        try {
            MerchantImage merchantImage = new MerchantImage(merchant.getSyncId(),merchant.getMerchantId(),
                                                            imageToBitmap(ivPhoto1),imageToBitmap(ivPhoto2),0);

            final DbHelper db = new DbHelper(getApplicationContext());
            ContentValues cv = merchantImage.getMerchantImageContentValues();

            try{
                boolean s = db.insertDataAll(cv, TABLE_IMAGE);
                System.out.println("CV Image 1:" +cv.get(COL_IMAGE1));
                System.out.println("CV Image 2:" +cv.get(COL_IMAGE2));

                if (s){
                    System.out.println("Image is save to database");
                    /*ContentValues cvImage2 = new ContentValues();
                    cvImage2.put(COL_IMAGE1,imageToBitmap(ivPhoto2));
                    SQLiteDatabase database = db.getWritableDatabase();
                    int afectedRows = database.update(TABLE_IMAGE,cvImage2,COL_IMAGE_SEQUENCE_ID +" = ? " ,new String[]{""+merchant.getSequenceId()});
                    if(afectedRows>0){
                        System.out.println("Image 1 updated  of merchant "+merchant.getMerchantId());
                    }*/

                }else {
                    System.out.println("Image is not save to database");
                }

            }catch (Exception e){
                System.out.println("Error at Image Sync: "+e.getMessage());
            }

            if (merchant.getMerchantId()!=0){
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
                                            int afectedRows = database.update(TABLE_IMAGE,cv,COL_IMAGE_SEQUENCE_ID +" = ? " ,new String[]{""+merchant.getSequenceId()});
                                            if(afectedRows>0){
                                                System.out.println("Is Sync is updated as 1 of merchant "+merchant.getMerchantId());
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

}
