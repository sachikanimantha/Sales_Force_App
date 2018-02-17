package com.example.bellvantage.scadd;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.example.bellvantage.scadd.Adapters.OrderRequestsAdapter;
import com.example.bellvantage.scadd.DB.DbHelper;
import com.example.bellvantage.scadd.Utils.SessionManager;
import com.example.bellvantage.scadd.Utils.SyncManager;
import com.example.bellvantage.scadd.swf.InvoiceList2;
import com.example.bellvantage.scadd.swf.LoginUser;
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

public class OrderRequestActivity extends AppCompatActivity {

    //Tool Bar
    Toolbar mToolbar;

    //Navigation Drawer
    private AccountHeader headerResult = null;
    private Drawer result = null;

    private RecyclerView rvOrderRequests;
    private OrderRequestsAdapter adapter;

    private String TAG = OrderRequestActivity.class.getSimpleName();
    private ArrayList<InvoiceList2> arrayList_invoice =new ArrayList<>();
    private ArrayList<String> arryMerchant = new ArrayList<>();

    private RecyclerView.LayoutManager layoutManager;
    private int userTypeId;
    int distributorId = 0;
    String DistributorName;
    String DistributorAddres1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_request);
        mToolbar = (Toolbar) findViewById(R.id.tb_main);
        mToolbar.setTitle("Invoice Print");


        System.out.println("invoice list items - "+(new DbHelper(getApplicationContext())).totalROWS(DbHelper.TABLE_INVOICE_LINE_ITEM));

        Gson gson = new Gson();
        String getJson = SessionManager.pref.getString("user", "");
        LoginUser prefUser = gson.fromJson(getJson, LoginUser.class);
        userTypeId = prefUser.getUserTypeId();

        initDrawer(savedInstanceState);

        arrayList_invoice = (new SyncManager(getApplicationContext())).getInvoiceDetailsFromSQLite();

        //==========RecyclerView ======================
        rvOrderRequests = (RecyclerView) findViewById(R.id.rvOrderRequest);
        // layoutManager = new LinearLayoutManager(getApplicationContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL,
                true
        );
        rvOrderRequests.setLayoutManager(layoutManager);
        rvOrderRequests.setHasFixedSize(true);
        adapter = new OrderRequestsAdapter(OrderRequestActivity.this, arrayList_invoice);
        rvOrderRequests.setAdapter(adapter);
    }




    private void initDrawer(Bundle savedInstanceState) {


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
                .withToolbar(mToolbar)
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
                            Toast.makeText(OrderRequestActivity.this, ((Nameable) drawerItem).getName().getText(OrderRequestActivity.this), Toast.LENGTH_SHORT).show();
                        }

                        if (((Nameable) drawerItem).getName().getText(OrderRequestActivity.this)=="Shopping Cart"){

                        }

                        if (((Nameable) drawerItem).getName().getText(OrderRequestActivity.this)=="Home"){
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }

                        if (((Nameable) drawerItem).getName().getText(OrderRequestActivity.this)=="My Account"){

                        }

                        if (((Nameable) drawerItem).getName().getText(OrderRequestActivity.this)=="Messages"){

                        }
                        if (((Nameable) drawerItem).getName().getText(OrderRequestActivity.this)=="My Orders"){

                        }
                        if (((Nameable) drawerItem).getName().getText(OrderRequestActivity.this)=="Settings"){

                        }

                        if (((Nameable) drawerItem).getName().getText(OrderRequestActivity.this)=="Logout"){
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),SalesOrderOfflineActivity.class);
        OrderRequestActivity.this.finish();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
