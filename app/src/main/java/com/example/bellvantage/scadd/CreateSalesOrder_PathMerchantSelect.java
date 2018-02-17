package com.example.bellvantage.scadd;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bellvantage.scadd.Adapters.CreateSalesorderMerchantSelectAdapter;
import com.example.bellvantage.scadd.DB.DbHelper;
import com.example.bellvantage.scadd.Utils.DateManager;
import com.example.bellvantage.scadd.Utils.SessionManager;
import com.example.bellvantage.scadd.Utils.SyncManager;
import com.example.bellvantage.scadd.swf.DeliveryPath;
import com.example.bellvantage.scadd.swf.LoginUser;
import com.example.bellvantage.scadd.swf.MerchantDetails;
import com.example.bellvantage.scadd.swf.SalesRep;
import com.google.gson.Gson;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.Drawer;

import java.util.ArrayList;
import java.util.Calendar;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

import static com.example.bellvantage.scadd.DB.DbHelper.COL_SALES_REP_DistributorId;


public class CreateSalesOrder_PathMerchantSelect extends AppCompatActivity {

    //Toolbar
    private Toolbar mToolBar;
    String toolBarTitle;

    //Navigation Drawer
    private AccountHeader headerResult = null;
    private Drawer result = null;
    TextView tv_status, tv_pathname;
    LinearLayout cl_main;
    RecyclerView recyclerView_merchants;
    Calendar calendar;
    int year, month, day;
    int userTypeId;
    String userType;
    String today_;


    int distri_id, is_distri_vat;
    String distri_name, salesrepType, username;
    int MerchantId_ = 0, isMerchantVat, pathid = 0,salesRep_id = 0;
    String MerchantName_, pathName_;


    SpinnerDialog spinnerDialogPath;
    ArrayList<DeliveryPath> arrayPath = new ArrayList<>();
    ArrayList<String> arrayStringPath = new ArrayList<>();
    MerchantDetails merchantDetailsObject ;

    ArrayList<String> merchantid_visited;
    ArrayList<MerchantDetails> arrayMerchant = new ArrayList<>();

    CreateSalesorderMerchantSelectAdapter createSalesorderMerchantSelectAdapter;

    double[] location = {0,0};
    ArrayList<MerchantDetails> merchantDetails = new ArrayList<>();


    SyncManager syncManager;
    Toolbar mToolbar;
    int i;
    ArrayList<SalesRep> salesRepArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_sales_order_pathmerchant);
        //loadDrawer(savedInstanceState);

        mToolbar = (Toolbar) findViewById(R.id.tb_main);
        mToolbar.setTitle("Create Sales Order - Step 01");

        syncManager = new SyncManager(getApplicationContext());
        arrayPath.clear();
        arrayPath = syncManager.getAllPath();

        init();
        loadPath(arrayPath);
        tv_pathname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialogPath.showSpinerDialog();
            }
        });
    }


    private void init() {

        Gson gson = new Gson();
        String getJson = SessionManager.pref.getString("user","");
        LoginUser prefUser = gson.fromJson(getJson, LoginUser.class);
        userTypeId = prefUser.getUserTypeId();
        userType = prefUser.getUserType();
        username = prefUser.getUserName();

        long time = DateManager.todayMillsecWithDateFormat("dd/MM/yyyy hh:mm:ss");
        today_ = (new SyncManager(getApplicationContext())).getDate(time, "yyyy-MM-dd");
        System.out.println("today_ - " + today_);

        visitedMerchants(today_);

        System.out.println("username - " + username);

        String sql1 = "SELECT " + COL_SALES_REP_DistributorId + " FROM " + DbHelper.TABLE_SALES_REP + " WHERE " + DbHelper.COL_SALES_REP_SalesRepId + " = " + userTypeId;
        distri_id = Integer.parseInt((new SyncManager(getApplicationContext())).getStringValueFromSQLite(sql1, COL_SALES_REP_DistributorId));
        System.out.println("distri_id - " + distri_id);

        //cl_main = (LinearLayout) findViewById(R.id.cl_create_sales_order_main);
        tv_pathname = (TextView) findViewById(R.id.tv_path_);
        tv_status = (TextView) findViewById(R.id.tv_merchantlist_nodata);
        tv_status.setVisibility(View.VISIBLE);
        tv_status.animate().rotationX(360).setDuration(1000).alpha(1);

        recyclerView_merchants = (RecyclerView) findViewById(R.id.rv_merchantlist_cso);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);


        salesRepArray = (new SyncManager(getApplicationContext())).getSalesRepDetailsFromSQLite("SELECT * FROM " + DbHelper.TABLE_SALES_REP);
        salesRep_id = salesRepArray.get(0).getSalesRepId();
        salesrepType = salesRepArray.get(0).getSalesRepType();
    }


//    private void loadDrawer(Bundle savedInstanceState) {
//
//        headerResult = new AccountHeaderBuilder()
//                .withActivity(this)
//                .withHeaderBackground(R.drawable.header)
//                .withTranslucentStatusBar(true)
//                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
//                    @Override
//                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
//
//                        return false;
//                    }
//                })
//                .withSavedInstance(savedInstanceState)
//                .build();
//
//        //Drawer
//        result = new DrawerBuilder()
//                .withActivity(this)
//                .withToolbar(mToolBar)
//                .withDisplayBelowStatusBar(false)
//                .withActionBarDrawerToggleAnimated(true)
//                .withDrawerGravity(Gravity.LEFT)
//                .withSavedInstance(savedInstanceState)
//                .withSelectedItem(0)
//                .withTranslucentStatusBar(true)
//                .withAccountHeader(headerResult) //set the AccountHeader  created earlier for the header
//                .addDrawerItems(
//                        new PrimaryDrawerItem().withName("Home").withIcon(GoogleMaterial.Icon.gmd_home).withIdentifier(1).withSelectedColor(3),
//                        new PrimaryDrawerItem().withName("Account").withIcon(GoogleMaterial.Icon.gmd_account_box).withIdentifier(2).withSelectedColor(2),
//                        new PrimaryDrawerItem().withName("Sales").withIcon(FontAwesome.Icon.faw_shopping_cart).withIdentifier(3).withBadge("5").withBadgeStyle(new BadgeStyle(Color.RED, Color.RED)),
//                        new PrimaryDrawerItem().withName("My Orders").withIcon(GoogleMaterial.Icon.gmd_list).withIdentifier(4),
//                        new PrimaryDrawerItem().withName("Messages").withIcon(GoogleMaterial.Icon.gmd_message).withIdentifier(5).withBadge("2").withBadgeStyle(new BadgeStyle(Color.RED, Color.RED)),
//                        new DividerDrawerItem(),
//                        new PrimaryDrawerItem().withDescription("Explore Yourself...").withName("scadd").withIdentifier(6),
//                        //new SectionDrawerItem().withName("eMart App"),
//                        new SecondaryDrawerItem().withName("Settings").withIcon(GoogleMaterial.Icon.gmd_settings),
//                        new SecondaryDrawerItem().withName("About Us").withIcon(GoogleMaterial.Icon.gmd_apps).withTag("Bullhorn"),
//                        new SecondaryDrawerItem().withName("Logout").withIcon(GoogleMaterial.Icon.gmd_exit_to_app)
//
//                ) // add the items we want to use with our Drawer
//                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
//                    @Override
//                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
//
//                        if (((Nameable) drawerItem).getName().getText(CreateSalesOrder_PathMerchantSelect.this) == "Home") {
//                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);
//                            finish();
//                        }
//
//                        if (((Nameable) drawerItem).getName().getText(CreateSalesOrder_PathMerchantSelect.this) == "Account") {
//                            Toast.makeText(CreateSalesOrder_PathMerchantSelect.this, "my account", Toast.LENGTH_SHORT).show();
//                        }
//
//                        if (((Nameable) drawerItem).getName().getText(CreateSalesOrder_PathMerchantSelect.this) == "Messages") {
//                            Toast.makeText(CreateSalesOrder_PathMerchantSelect.this, "this works messages", Toast.LENGTH_SHORT).show();
//                        }
//
//                        if (((Nameable) drawerItem).getName().getText(CreateSalesOrder_PathMerchantSelect.this) == "Logout") {
//                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);
//                            finish();
//                        }
//                        return false;
//                    }
//                })
//                .build();
//    }


    private void loadPath(final ArrayList<DeliveryPath> arrayP1) {
        System.out.println("arrayP1 size - " + arrayP1.size());
        for (int i = 0; i < arrayP1.size(); i++) {
            arrayStringPath.add(arrayP1.get(i).getPathName() + " - " + arrayP1.get(i).getDeliveryPathId());
        }

        spinnerDialogPath = new SpinnerDialog(CreateSalesOrder_PathMerchantSelect.this, arrayStringPath,
                "Select A Path", R.style.DialogAnimations_SmileWindow);

        spinnerDialogPath.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {

                tv_pathname.setText(item);
                //btn_submit.setEnabled(false);

                for (i = 0; i < arrayP1.size(); i++) {
                    String p = arrayP1.get(i).getPathName() + " - " + arrayP1.get(i).getDeliveryPathId();
                    if (p.equals(item)) {
                        pathid = arrayP1.get(i).getDeliveryPathId();
                        System.out.println("path Id in creaate Sales order " + pathid);
                        pathName_ = arrayP1.get(i).getPathName();
                    }
                }
                //arrayMerchant.clear();
                arrayMerchant = syncManager.getMerchantListVsPath(pathid);
                System.out.println("arrayMerchant size - " + arrayMerchant.size());
//                stringArrayVisitMerchant = syncManager.getMerchantListVsVisittoday(today_);
//                System.out.println("stringArrayVisitMerchant size - "+stringArrayVisitMerchant.size());
                loadMerchant(arrayMerchant);

            }
        });
    }

    private void loadMerchant(ArrayList<MerchantDetails> arrayMerchantfull) {

        arrayMerchantfull.add(0, new MerchantDetails(0, 0, "Direct Sale",
                "", "", "", "", 0, "", "", 0, 0,
                "", "", "", "", "", "", 0, "", "",
                0, "", "", "",
                0, "", 0, "", 0, ""));

        createSalesorderMerchantSelectAdapter = new CreateSalesorderMerchantSelectAdapter
                (getApplicationContext(), arrayMerchantfull ,merchantid_visited , new CreateSalesorderMerchantSelectAdapter.OnItemClickListener() {
                    @Override
                    public void OnItemClick(MerchantDetails merchant) {

                        MerchantId_ = merchant.getMerchantId();
                        System.out.println("MerchantId_ - " + MerchantId_);

                        ArrayList<MerchantDetails> md ;

                        if(MerchantId_ != 0){

                            md = (new SyncManager(getApplicationContext())).getMerchantDetailsAccordingtoMerchantId(MerchantId_);
                            merchantDetailsObject = new MerchantDetails(
                                    md.get(0).getSequenceId(),
                                    md.get(0).getMerchantId(),
                                    md.get(0).getMerchantName(),
                                    md.get(0).getContactNo1(),
                                    md.get(0).getContactNo2(),
                                    md.get(0).getLongitude(),
                                    md.get(0).getLatitude(),
                                    md.get(0).getIsActive(),
                                    md.get(0).getEnteredDate(),
                                    md.get(0).getEnteredUser(),
                                    md.get(0).getDiscountRate(),
                                    md.get(0).getIsSync(),
                                    md.get(0).getSyncDate(),
                                    md.get(0).getBuildingNo(),
                                    md.get(0).getAddress1(),
                                    md.get(0).getAddress2(),
                                    md.get(0).getCity(),
                                    md.get(0).getContactPerson(),
                                    md.get(0).getAreaCode(),
                                    md.get(0).getMerchantType(),
                                    md.get(0).getMerchantClass(),
                                    md.get(0).getDistrictCode(),
                                    md.get(0).getUpdatedUser(),
                                    md.get(0).getUpdatedDate(),
                                    md.get(0).getReferenceID(),
                                    md.get(0).getIsVat(),
                                    md.get(0).getVatNo(),
                                    md.get(0).getPathCode(),
                                    md.get(0).getSyncId(),
                                    md.get(0).getIsCredit(),
                                    md.get(0).getCreditDays()
                            );
                        }
                        Intent intent = null;
                        if(MerchantId_ != 0){

                            location = checkMerchantHasLocation(MerchantId_);
                            merchantDetails = (new SyncManager(getApplicationContext())).getMerchantDetailsAccordingtoMerchantId(MerchantId_);
                            System.out.println("location lati - " + location[0] + ",longi - " + location[1]);
                            MerchantName_ = merchantDetails.get(0).getMerchantName();
                            isMerchantVat = merchantDetails.get(0).getIsVat();
                        }

                        if (location[0] == 0 && location[1] == 0 && MerchantId_ == 0) {

                            System.out.println("inside direct sales ");
                            intent = new Intent(CreateSalesOrder_PathMerchantSelect.this, CreateSalesOrderContinue.class);
                            intent.putExtra("merchant_id_", 0);
                            intent.putExtra("merchant_name_", "Direct Sale");
                            intent.putExtra("merchant_isvat_", 0);
                            intent.putExtra("distributor_id", 0);
                            intent.putExtra("distributor_name", "");
                            intent.putExtra("distributor_isvat", 0);
                            intent.putExtra("salesrep_type", salesrepType);
                            intent.putExtra("vatType", 2);//vat value doesnt count
                            startActivity(intent);

                        }



                        if (location[0] != 0 && location[1] != 0 && MerchantId_ != 0) {

                            intent = new Intent(CreateSalesOrder_PathMerchantSelect.this, CreateSalesOrder_VisitReasonSelect.class);
                            intent.putExtra("vatType", 2);
                            intent.putExtra("merchant_id", MerchantId_);
                            intent.putExtra("merchant_name", MerchantName_);
                            intent.putExtra("merchant_isvat", isMerchantVat);
                            intent.putExtra("distributor_id", distri_id);
                            intent.putExtra("salesrep_id",salesRep_id);
                            intent.putExtra("path_id",pathid);

                            startActivity(intent);
                        } else {
                            if(MerchantId_ != 0){
                                displayStatusMessage2("You have to update merchant", 3);
                            }
                        }
                    }
                });

        recyclerView_merchants.setItemAnimator(new DefaultItemAnimator());
        recyclerView_merchants.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView_merchants.setAdapter(createSalesorderMerchantSelectAdapter);
        tv_status.setVisibility(View.GONE);
    }


    public double[] checkMerchantHasLocation(int merchantid) {

        String sql = "SELECT * FROM " + DbHelper.TABLE_MERCHANT + " WHERE " + DbHelper.TBL_MERCHANT_MERCHANT_ID + " = " + merchantid;
        SQLiteDatabase sqLiteDatabase = (new DbHelper(getApplicationContext())).getReadableDatabase();
        Cursor cursor = null;
        double lati = 0, longi = 0;
        double[] loc = {0, 0};
        try {
            cursor = sqLiteDatabase.rawQuery(sql, null);
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    while (cursor.moveToNext()) {
                        lati = cursor.getDouble(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_LATITUDE));
                        longi = cursor.getDouble(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_LONGITUDE));
                    }
                    loc[0] = lati;
                    loc[1] = longi;
                }
            }
        } catch (Exception e) {
            System.out.println("error - " + e.toString());
        }
        return loc;
    }

    public void visitedMerchants(String date){//yyyy-MM-dd
        String sql = "SELECT "+DbHelper.COL_MVISIT_MERCHANT_ID+" FROM "+
                DbHelper.TABLE_MVISIT +" WHERE "+DbHelper.COL_MVISIT_ENTERED_DATE+" = '"+date+"'";

        System.out.println("sql - "+sql);
        SQLiteDatabase sqLiteDatabase = (new DbHelper(getApplicationContext())).getReadableDatabase();
        Cursor cursor= null;
        merchantid_visited = new ArrayList<String>();

        try{
            cursor = sqLiteDatabase.rawQuery(sql,null);
            System.out.println("cursor.size - "+cursor.getCount());
            if(cursor != null && cursor.getCount() >0){
                while(cursor.moveToNext()){
                    merchantid_visited.add(cursor.getString(cursor.getColumnIndex(DbHelper.COL_MVISIT_MERCHANT_ID)));
                }
                System.out.println("merchantid_visited.size - "+merchantid_visited.size());
            }
        }catch(Exception e){

        }


    }


    private void displayStatusMessage2(String s, int colorValue) {

        AlertDialog.Builder builder;
        View view;
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

        builder = new AlertDialog.Builder(CreateSalesOrder_PathMerchantSelect.this);
        view = getLayoutInflater().inflate(R.layout.layout_for_custom_message, null);

        tvOk = (TextView) view.findViewById(R.id.tvOk);
        tvMessage = (TextView) view.findViewById(R.id.tvMessage);
        imageView = (ImageView) view.findViewById(R.id.iv_status_k);

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
                Intent intent = new Intent(CreateSalesOrder_PathMerchantSelect.this, MRA_UpdateActivity.class);
                intent.putExtra("merchant_details", merchantDetailsObject);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

}
