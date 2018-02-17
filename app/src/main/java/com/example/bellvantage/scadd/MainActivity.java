package com.example.bellvantage.scadd;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.example.bellvantage.scadd.Adapters.MenuAdapter;
import com.example.bellvantage.scadd.DB.DbHelper;
import com.example.bellvantage.scadd.DB.MySingleton;
import com.example.bellvantage.scadd.MC.MyMenu;
import com.example.bellvantage.scadd.Services.GPS_Tracker;
import com.example.bellvantage.scadd.Utils.DateManager;
import com.example.bellvantage.scadd.Utils.SessionManager;
import com.example.bellvantage.scadd.Utils.SyncManager;
import com.example.bellvantage.scadd.domains.ActualPath;
import com.example.bellvantage.scadd.swf.LoginUser;
import com.example.bellvantage.scadd.web.HTTPPaths;
import com.google.gson.Gson;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    //Toolbar
     Toolbar mToolBar;

    //Navigation Drawer
     AccountHeader headerResult = null;
     Drawer result = null;
     static final int PROFILE_SETTING = 1;

    String userName;
    int salesRepId,gpsStatus;
    double lat, lon;
    float accuracy;

    //RecycleView
    RecyclerView rvMenuItem;
    MenuAdapter adapter;
    int userId;

    DbHelper db;

    BroadcastReceiver broadcastReceiver, networkMonitorBroadCastReciver;

    @Override
    protected void onResume() {
        super.onResume();
        if (broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {


                    DateManager dateManager  =  new DateManager();
                    String routeDate = dateManager.getTodayDateString();
                    String locationTime = dateManager.getDateWithTime();
                    locationTime = locationTime.replace(" ", "%20");
                    int distributerId = 0;

                    System.out.println("longitude "+ intent.getExtras().get("lon"));
                    System.out.println("lattitude "+ intent.getExtras().get("lat"));

                    lat = (double) intent.getExtras().get("lat");
                    lon = (double) intent.getExtras().get("lon");
                    accuracy = (float) intent.getExtras().get("accuracy");
                    gpsStatus = (int) intent.getExtras().get("gpsStatus");

                    if(gpsStatus==0){
                       //pop up
                        //Alert Dialog
                        //displayStatusMessage("Enable Location",3);
                    }


                    System.out.println("main lot " + lat);
                    System.out.println("main lon " + lon);
                    System.out.println("main accuracy " + accuracy);


                    ActualPath actualPath = new ActualPath();
                    actualPath.setROUTE_DATE(routeDate);
                    actualPath.setDISTRIBUTER_ID(distributerId);
                    actualPath.setSALES_REP_ID(salesRepId);
                    actualPath.setUserName(userName);
                    actualPath.setLOCATION_TIME(locationTime);
                    actualPath.setLATITUDE(lat);
                    actualPath.setLONGITUDE(lon);
                    actualPath.setIS_SYNC(DbHelper.SYNC_STATUS_FAIL);
                    actualPath.setSYNC_DATE(locationTime);
                    actualPath.setACCURACY(accuracy);

                    //syncData(lat,lon,accuracy,routeDate,locationTime);
                    insertActualPath(actualPath);
                }
            };
        }
        registerReceiver(broadcastReceiver,new IntentFilter("location_update"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver !=null){
            unregisterReceiver(broadcastReceiver);
            unregisterReceiver(networkMonitorBroadCastReciver);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //String date = DateFormat.format("MM/dd/yyyy", new Date((new Date()).getTime())).toString();
        db = new DbHelper(getApplicationContext());

        networkMonitorBroadCastReciver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                System.out.println("NetworkMonitor Service start...");
            }
        };


        mToolBar = (Toolbar) findViewById(R.id.tb_main);
        mToolBar.setLogo(R.drawable.logos);

        Date dt = new Date();
        long date = Date.UTC(dt.getYear(), dt.getMonth(), dt.getDay(), dt.getHours(),dt.getMinutes(), dt.getSeconds());
        String senddate = "/date("+date+")/";
        System.out.println("Date " + senddate);

        if(!runTimePermisions()){
            enableButton();
        }


        /*//time
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                LocationManager manager = (LocationManager) getSystemService(getApplicationContext().LOCATION_SERVICE );
                                boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                                if(statusOfGPS==false){
                                    displayStatusMessage("Enaable Location",3);
                                }

                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();*/




        Gson gson = new Gson();
        String getJson = SessionManager.pref.getString("user", "");
        LoginUser prefUser = gson.fromJson(getJson, LoginUser.class);
        userName =prefUser.getUserName();
        salesRepId = prefUser.getUserTypeId();
        userId = prefUser.getUserTypeId();

        //Toast.makeText(this, "User Name "+ userName, Toast.LENGTH_SHORT).show();

        //=========Navigation Drawer===========================================
        insertNavigationDraer(savedInstanceState);




        //RecyclerView
        rvMenuItem =(RecyclerView)findViewById(R.id.rvMenuItem);
        //layoutManager = new LinearLayoutManager(this);
        LinearLayoutManager layoutManager = new GridLayoutManager(MainActivity.this,3,LinearLayoutManager.VERTICAL,false);
        rvMenuItem.setLayoutManager(layoutManager);
        rvMenuItem.setHasFixedSize(true);
        //String[] menuLabels = { "Assign Path","Primary Sales Order","Sales Orders","Load Vehicle","Sales Order to Invoice","Invoice Reprint", "Merchant Registration","Inquiry","Returns" ,"Reports","Attendance", "View Stock" , "Cancel Invoice"};
        //String[] menuLabels = { "Admin","Load Vehicle","Primary Sales Order","Sales Orders Offline","Sales Order to Invoice","Invoice Print","Return-Note Print","Cancel Invoice","View Vehicle Stock","View Stock","Returns" ,"Customer Registration","Inquiry","Reports", "Search Invoice Payment" };
        String[] menuLabels = { "Admin","Primary Sales Order","Sales Orders Offline","Invoice Print","Return-Note Print","Cancel Invoice","Returns" ,"Customer Registration", "Search Invoice Payment", "Evaluation","camera","Sync Sales orders"};
        int[] menuImages = {
                R.drawable.mm_admin,
                R.drawable.mm_order_request,
                R.drawable.mm_invoice,
                R.drawable.mm_invoice_reprint,
                R.drawable.mm_invoice_reprint,
                R.drawable.mm_cancel_sales_order,
                R.drawable.mm_returns,
                R.drawable.mm_merchant_register,
                R.drawable.mm_search_invoice,
                R.drawable.mm_report_icon,
                R.drawable.ic_menu_camera,
                R.drawable.mm_sync_sales_orders

        };

        ArrayList<MyMenu> arrayList = new ArrayList<>();

        for(int i = 0; i < menuLabels.length; i++) {
            String menuName = menuLabels[i];
            int img = menuImages[i];
            arrayList.add(new MyMenu(menuName,img));
        }
        adapter = new MenuAdapter(getApplicationContext(),arrayList,salesRepId);
        rvMenuItem.setAdapter(adapter);

    }

    private void insertNavigationDraer(Bundle savedInstanceState) {
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withTranslucentStatusBar(true)

                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {

                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

        //Drawer
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolBar)
                .withDisplayBelowStatusBar(true)
                .withActionBarDrawerToggleAnimated(true)
                .withDrawerGravity(Gravity.LEFT)
                .withSavedInstance(savedInstanceState)
                .withSelectedItem(0)
                .withTranslucentStatusBar(false)
                .withAccountHeader(headerResult) //set the AccountHeader  created earlier for the header
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Home").withIcon(GoogleMaterial.Icon.gmd_home).withIdentifier(1).withSelectedColor(3),
                        new PrimaryDrawerItem().withName("Sync Sales Orders").withIcon(GoogleMaterial.Icon.gmd_sync).withIdentifier(2).withSelectedColor(2),
                        // new PrimaryDrawerItem().withName("Sales").withIcon(FontAwesome.Icon.faw_shopping_cart).withIdentifier(3).withBadge(getSalesOrders()).withBadgeStyle(new BadgeStyle(Color.RED, Color.RED)),
                        new PrimaryDrawerItem().withName("Sync Manual").withIcon(FontAwesome.Icon.faw_chain_broken).withIdentifier(3),
                        new PrimaryDrawerItem().withName("My Orders").withIcon(FontAwesome.Icon.faw_shopping_cart).withIdentifier(4).withBadge(new SyncManager(MainActivity.this).getSalesOrders()).withBadgeStyle(new BadgeStyle(Color.RED, Color.RED)),
                        new PrimaryDrawerItem().withName("Messages").withIcon(GoogleMaterial.Icon.gmd_message).withIdentifier(5).withBadgeStyle(new BadgeStyle(Color.RED, Color.RED)),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withDescription("Explore Yourself...").withName("scadd").withIdentifier(6),
                        //new SectionDrawerItem().withName("eMart App"),
                        new SecondaryDrawerItem().withName("Settings").withIcon(GoogleMaterial.Icon.gmd_settings),
                        new SecondaryDrawerItem().withName("About Us").withIcon(GoogleMaterial.Icon.gmd_apps).withTag("Bullhorn"),
                        new SecondaryDrawerItem().withName("Logout").withIcon(GoogleMaterial.Icon.gmd_exit_to_app)
                        // new SwitchDrawerItem().withName("Switch").withIcon(GoogleMaterial.Icon.gmd_ac_unit).withChecked(true).withOnCheckedChangeListener(onCheckedChangeListener),
                        // new ToggleDrawerItem().withName("Toggle").withIcon(GoogleMaterial.Icon.gmd_adjust).withChecked(true).withOnCheckedChangeListener(onCheckedChangeListener)
                ) // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {


                        if (((Nameable) drawerItem).getName().getText(MainActivity.this)=="Shopping Cart"){

                        }

                        if (((Nameable) drawerItem).getName().getText(MainActivity.this)=="Home"){
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }

                        if (((Nameable) drawerItem).getName().getText(MainActivity.this)=="Sync Sales Orders"){
                            Intent intent = new Intent(getApplicationContext(), SyncSalesOrdersActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }

                        if (((Nameable) drawerItem).getName().getText(MainActivity.this)=="Sync Manual"){

                            Intent intent = new Intent(getApplicationContext(),LoginSyncActivityDrawer.class);

                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("userId",userId);
                            intent.putExtra("userName",userName);
                            startActivity(intent);
                            finish();

                        }
                        if (((Nameable) drawerItem).getName().getText(MainActivity.this)=="My Orders"){
                            Intent intent = new Intent(getApplicationContext(),SalesOrderToInvoice.class);
                            startActivity(intent);
                        }
                        if (((Nameable) drawerItem).getName().getText(MainActivity.this)=="Settings"){

                        }

                        if (((Nameable) drawerItem).getName().getText(MainActivity.this)=="Logout"){
                           /* SessionManager.Logut();
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();*/
                            displayStatusMessage("Are you sure want to exit? ",3);
                        }

                        return false;
                    }
                })
                .build();
    }



    void insertActualPath(final ActualPath actualPath) {


        String ROUTE_DATE=actualPath.getROUTE_DATE();
        int DISTRIBUTER_ID= actualPath.getDISTRIBUTER_ID();
        int SALES_REP_ID = actualPath.getSALES_REP_ID();
        String userName = actualPath.getUserName();
        String LOCATION_TIME = actualPath.getLOCATION_TIME();
        //LOCATION_TIME = LOCATION_TIME.replace(" ", "%20");
        double LONGITUDE = actualPath.getLONGITUDE();
        double LATITUDE = actualPath.getLATITUDE();
        int IS_SYNC = actualPath.getIS_SYNC();
        String SYNC_DATE = actualPath.getROUTE_DATE();
        //SYNC_DATE = SYNC_DATE.replace(" ","%20");
        float ACCURACY =actualPath.getACCURACY();


        if(checkNetworkConnection()){
            String actualPathurl = HTTPPaths.seriveUrl+"SaveActualPath?routeDate="+ROUTE_DATE+"&locationTime="+LOCATION_TIME+"&longitude="+LONGITUDE+"&latitude="+LATITUDE+"&isSync=1&syncDate="+LOCATION_TIME+"&accuracy="+ACCURACY+"&userName="+userName;
            System.out.println(actualPathurl);

            StringRequest stringRequest = new StringRequest(Request.Method.GET, actualPathurl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            JsonObject object = Json.parse(response).asObject();
                            int id = object.get("ID").asInt();

                            System.out.println("Response id: " + id);

                            if(id==200){
                                insertActualPathToLacalDb(actualPath,DbHelper.SYNC_STATUS_OK);
                                System.out.println("tracking and sync");

                            }else{
                                //Toast.makeText(getApplicationContext(), "sync fail", Toast.LENGTH_SHORT).show();
                                System.out.println("sync fail");
                                insertActualPathToLacalDb(actualPath,DbHelper.SYNC_STATUS_FAIL);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            insertActualPathToLacalDb(actualPath,DbHelper.SYNC_STATUS_FAIL);
                            System.out.println("Volley error " + error.getMessage());
                        }
                    });
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        }else{
            insertActualPathToLacalDb(actualPath,DbHelper.SYNC_STATUS_FAIL);
        }
    }

    private void insertActualPathToLacalDb(ActualPath actualPath, int syncStatus) {
        actualPath.setIS_SYNC(syncStatus);
        ContentValues cv =actualPath.getActualPathContentvalues();

        boolean success = db.insertDataAll(cv,DbHelper.TBL25);
        if (success){
            System.out.println( "Your location is inserted " + syncStatus);
        }
        else {
            System.out.println( "Your location is not inserted " + syncStatus);
        }
    }

    public boolean checkNetworkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

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

        builder = new AlertDialog.Builder(MainActivity.this);
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
                SessionManager.Logut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

    }
    @Override
    public void onBackPressed() {

        displayStatusMessage("Are you sure want to exit? ",3);

       /* (new AlertDialog.Builder(this)).setTitle("scadd").setMessage("Are you sure want to exit").setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SessionManager.Logut();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create().show();*/
    }

    //Services
    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(networkMonitorBroadCastReciver,new IntentFilter(HTTPPaths.UI_UPDATE_BROADCAST));
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

     void enableButton() {
        Intent intent =  new Intent(getApplicationContext(),GPS_Tracker.class);
        startService(intent);
    }

     boolean runTimePermisions() {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.
                checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.
                checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},100);
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                enableButton();
            }else{
                runTimePermisions();
            }
        }
    }

    public void syncData(double lat, double lon, double accuracy,String routeDate,String locationTime){

        System.out.println("sync method lot " + lat);
        System.out.println("sync method lon " + lon);
        System.out.println("sync method accuracy " +accuracy);

        String actualPathurl = HTTPPaths.seriveUrl+"SaveActualPath?routeDate="+routeDate+"&locationTime="+locationTime+"&longitude="+lon+"&latitude="+lat+"&isSync=1&syncDate="+locationTime+"&accuracy="+accuracy+"&userName="+userName;
        //String actualPathurl = "http://113.59.211.246:9010/Service1.svc/SaveActualPath?routeDate=2017-06-22&locationTime="+locationTime+"&longitude="+lon+"&latitude="+lat+"&isSync=1&syncDate="+locationTime+"&accuracy="+accuracy+"&userName="+userName;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, actualPathurl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JsonObject object = Json.parse(response).asObject();
                        int id = object.get("ID").asInt();

                        if(id==200){
                            //Toast.makeText(getApplicationContext(), "tracking and sync", Toast.LENGTH_SHORT).show();
                            System.out.println("tracking and sync");

                        }else{
                            //Toast.makeText(getApplicationContext(), "sync fail", Toast.LENGTH_SHORT).show();
                            System.out.println("sync fail");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Volley Error", Toast.LENGTH_SHORT).show();
                        System.out.println("Volley error " + error.getMessage());
                    }
                });
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

}
