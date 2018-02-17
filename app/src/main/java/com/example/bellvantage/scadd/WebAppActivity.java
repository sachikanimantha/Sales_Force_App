package com.example.bellvantage.scadd;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bellvantage.scadd.Utils.SessionManager;
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

public class WebAppActivity extends AppCompatActivity {

    //Toolbar
    private Toolbar mToolBar;
    String toolBarTitle;

    //Navigation Drawer
    private AccountHeader headerResult = null;
    private Drawer result = null;

    //Web View
    WebView vwSite;
    String url;
    ProgressBar pBar;

    String epfNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_app);

        Gson gson = new Gson();
        String getJson = SessionManager.pref.getString("user", "");
        LoginUser prefUser = gson.fromJson(getJson, LoginUser.class);
        epfNumber =prefUser.getEpfNumber();
        System.out.println("epfNumber - "+epfNumber);

        if(getIntent().getSerializableExtra("title")!=null){
            toolBarTitle  = (String) getIntent().getSerializableExtra("title");
        }

        switch (toolBarTitle){


            case "Assign Path":
                url = HTTPPaths.baseUrl+"AssignPath.aspx?EpfNumber="+epfNumber;
                break;

            case "Create Sales Orders":
                url = HTTPPaths.baseUrl+"CustomerSerch.aspx?EpfNumber="+epfNumber;
                break;

            case "Revise Sale Orders":
                url = HTTPPaths.baseUrl+"ConfirmSalesOrder.aspx?isEdit=1&EpfNumber="+epfNumber;
                break;

            case "Confirm Sale Orders":
                url = HTTPPaths.baseUrl+"ConfirmSalesOrder.aspx?isConfirm=1&EpfNumber="+epfNumber;
                break;

            case "Cancel Orders":
                url = HTTPPaths.baseUrl+"ConfirmSalesOrder.aspx?isCancel=1&EpfNumber="+epfNumber;
                break;

            case "Returns":
                url = HTTPPaths.baseUrl+"CustomerSerch.aspx?EpfNumber="+epfNumber+"&Return=1";
                break;

            case "Sales Order to Invoice":
                url = HTTPPaths.baseUrl+"InvoicingSalesOrders.aspx?EpfNumber="+epfNumber;
                break;

            case "Inquiry":
                url = HTTPPaths.baseUrl+"InquiryMenu.aspx?UserId=scadd";
                break;

            case "MIS":
                url = HTTPPaths.baseUrl+"CustomerSerch.aspx?CustomerMenu=1&UserId=scadd";
                break;

            case "Copy Previous Order":
                url = HTTPPaths.baseUrl+"ConfirmSalesOrder.aspx?isClone=1&EpfNumber="+epfNumber;
                break;

            //Primary Sales
            //"Create Primary Sales Orders", "Revise Primary Sale Orders",  "Cancel Primary Sale Orders","Primary Sale Orders Approved Report"

            case "Create Primary Sales Orders":
                url = HTTPPaths.baseUrl+"PrimarySalesOrderManage.aspx?EpfNumber="+epfNumber;
                break;

            case "Revise Primary Sale Orders":
                url = HTTPPaths.baseUrl+"ManagePrimarySalesOrderList.aspx?isEdit=1&EpfNumber="+epfNumber;
                break;

            case "Confirm Primary Sale Orders":
                url = HTTPPaths.baseUrl+"ManagePrimarySalesOrderList.aspx?isConfirm=1&EpfNumber="+epfNumber;
                break;

            case "Primary Sale Orders Approved Report":
                url = HTTPPaths.baseUrl+"ManagePrimarySalesOrderList.aspx?isReport=1&EpfNumber="+epfNumber;
                break;
            /*
            Primary Sales Order
PSO Create - /PrimarySalesOrderManage.aspx
PSO Revise - /ManagePrimarySalesOrderList.aspx?isEdit=1
PSO Confirm - /ManagePrimarySalesOrderList.aspx?isConfirm=1
(New Link)
PSO Approved Report - /ManagePrimarySalesOrderList.aspx?isReport=1
             */


            case "Cancel Primary Orders":
                url = HTTPPaths.baseUrl+"ManagePrimarySalesOrderList.aspx?isCancel=1";
                break;

            case  "Load Vehicle":
                url = HTTPPaths.baseUrl+"AssignStock.aspx?EpfNumber="+epfNumber;
                break;

            case  "Cancel Invoice":
                url = HTTPPaths.baseUrl+"InvoiceCancelMobile.aspx?EpfNumber="+epfNumber;
                break;

            //reports
            case  "SalesRep Inventory Report":
                url = HTTPPaths.baseUrl+"SalesRepInventoryReportMobile.aspx?EpfNumber="+epfNumber;
                break;


            case "Vehicle Inventory Report":
                url = HTTPPaths.baseUrl+"VehicleInventoryReportMobile.aspx?EpfNumber="+epfNumber;
                break;

            case  "Search Invoice Payment":
                url = HTTPPaths.baseUrl+"InvoicePaymentSearchMobile.aspx?EpfNumber="+epfNumber;
                break;
        }

        // Makes Progress bar Visible
        getWindow().setFeatureInt( Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);

        //WebView
        try{

            vwSite = (WebView)findViewById(R.id.vwSite);
            pBar = (ProgressBar) findViewById(R.id.pBar);
            pBar.setMax(100);
            vwSite.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int progress)
                {

                    pBar.setVisibility(View.VISIBLE);
                    pBar.setProgress(progress);
                    //Make the bar disappear after URL is loaded, and changes string to Loading...
                    setTitle("Loading...");
                    setProgress(progress * 100); //Make the bar disappear after URL is loaded

                    // Return the app name after finish loading
                    if(progress == 100){
                        setTitle(toolBarTitle);
                        pBar.setVisibility(View.GONE);
                    }
                    super.onProgressChanged(view,progress);
                }
            });
            vwSite.getSettings().setLoadsImagesAutomatically(true);
            vwSite.getSettings().setJavaScriptEnabled(true);
            vwSite.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            vwSite.setWebViewClient(new WebViewClient());
            vwSite.loadUrl(url);



        }catch(Exception e){
            System.out.println("Error - "+e.toString());
        }




        //=========Navigation Drawer===========================================

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
                .withDisplayBelowStatusBar(false)
                .withActionBarDrawerToggleAnimated(true)
                .withDrawerGravity(Gravity.LEFT)
                .withSavedInstance(savedInstanceState)
                .withSelectedItem(0)
                .withTranslucentStatusBar(true)
                .withAccountHeader(headerResult) //set the AccountHeader  created earlier for the header
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Home").withIcon(GoogleMaterial.Icon.gmd_home).withIdentifier(1).withSelectedColor(3),
                        new PrimaryDrawerItem().withName("Account").withIcon(GoogleMaterial.Icon.gmd_account_box).withIdentifier(2).withSelectedColor(2),
                        new PrimaryDrawerItem().withName("Sales").withIcon(FontAwesome.Icon.faw_shopping_cart).withIdentifier(3).withBadge("5").withBadgeStyle(new BadgeStyle(Color.RED, Color.RED)),
                        new PrimaryDrawerItem().withName("My Orders").withIcon(GoogleMaterial.Icon.gmd_list).withIdentifier(4),
                        new PrimaryDrawerItem().withName("Messages").withIcon(GoogleMaterial.Icon.gmd_message).withIdentifier(5).withBadge("2").withBadgeStyle(new BadgeStyle(Color.RED, Color.RED)),
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
                        if (drawerItem instanceof Nameable) {
                            Toast.makeText(WebAppActivity.this, ((Nameable) drawerItem).getName().getText(WebAppActivity.this), Toast.LENGTH_SHORT).show();
                        }

                        if (((Nameable) drawerItem).getName().getText(WebAppActivity.this)=="Shopping Cart"){

                        }

                        if (((Nameable) drawerItem).getName().getText(WebAppActivity.this)=="Home"){
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }

                        if (((Nameable) drawerItem).getName().getText(WebAppActivity.this)=="My Account"){

                        }

                        if (((Nameable) drawerItem).getName().getText(WebAppActivity.this)=="Messages"){

                        }
                        if (((Nameable) drawerItem).getName().getText(WebAppActivity.this)=="My Orders"){

                        }
                        if (((Nameable) drawerItem).getName().getText(WebAppActivity.this)=="Settings"){

                        }

                        if (((Nameable) drawerItem).getName().getText(WebAppActivity.this)=="Logout"){
                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }

                        return false;
                    }
                })
                .build();

    }

   /* private void shouldShowProgress(boolean show) {
        if (show) {
            pBar.setVisibility(View.VISIBLE);
        } else {
            pBar.setVisibility(View.GONE);
        }
    }*/



    public boolean checkNetworkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}
