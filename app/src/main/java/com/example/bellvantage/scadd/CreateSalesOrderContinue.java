package com.example.bellvantage.scadd;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.example.bellvantage.scadd.Adapters.ProductListRV;
import com.example.bellvantage.scadd.DB.DbHelper;
import com.example.bellvantage.scadd.DB.MySingleton;
import com.example.bellvantage.scadd.Utils.DateManager;
import com.example.bellvantage.scadd.Utils.SessionManager;
import com.example.bellvantage.scadd.Utils.SyncManager;
import com.example.bellvantage.scadd.swf.CategoryDetails;
import com.example.bellvantage.scadd.swf.LoginUser;
import com.example.bellvantage.scadd.swf.MerchantOutstandSalesOrderList;
import com.example.bellvantage.scadd.swf.ProductList;
import com.example.bellvantage.scadd.swf.ProductListItemPosition;
import com.example.bellvantage.scadd.swf.ProductListLast;
import com.example.bellvantage.scadd.swf.ReturnInventoryLineItem;
import com.example.bellvantage.scadd.web.HTTPPaths;
import com.google.gson.Gson;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

//import com.example.bellvantage.scadd.Adapters.ProductListAdapterRV;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_PROMOTION_BatchId;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_PROMOTION_CatalogueType;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_PROMOTION_ProductId;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_PROMOTION;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_PROMOTION_IMAGE;


public class CreateSalesOrderContinue extends AppCompatActivity {


    //Toolbar
    private Toolbar mToolBar;
    String toolBarTitle;

    //Navigation Drawer
    private AccountHeader headerResult = null;
    private Drawer result = null;

    //public Navigation
    NavigationView navigationView;
    TextView sd_brandnames;
    RecyclerView rv_salesitems;
    SearchView sv_items;

    TextView tv_total_grand, tv_vat_amount, tv_vat_header, tv_free_issues, tv_net_amount;
    double grandTotal = 0.00, TotalVatAmount = 0.00, TOTAL_free_issues = 0.00, TOTAL_net_amount = 0.00;

    LinearLayout ll_sales_order;
    Button btn_request_send;
    int userTypeID;
    String enteredUserName;
    int qty, freeqty, categoryId = 0, returnQty = 0;
    int img_not_done = R.drawable.ic_not_done;
    int img_done = R.drawable.ic_done;
    int selectedProductItem;
    SpinnerDialog sd_brandname;
    Toolbar mToolbar;

    int merchant_isvat_, distributor_isvat, salesOrder_isvat;//vat products = 0 mean there isnt chose anything
    int merchant_id_, distributor_id;
    String merchant_name_, distributor_name;
    String tod;

    // AlertDialog
    AlertDialog dialog,alertDialog;
    AlertDialog.Builder mBuilder;

    //data for putExtrasToLast()
    int salestypeid = 1;
    int salesstatus = 1;
    int is_return = 1;
    String syncDate;
    final int[] x = new int[2];
    final int[] y = new int[2];
    String salesrep_type;

    double unitprice;
    String expiredate;
    int stock;
    String name;
    int QTY,FREEQTY,ISVAT;
    double VATRATE;
    int BATCHID,PRODUCTID, RETURNQTY, CATEGORYID,newStock ;

    int credit_type = 0,credit_days = 0;

    int position_ = 0;
    int isItemFromSearchArray = 0;//use for brandname select
    int isItemFromBrandArray = 0;//use for brandname select
    ProductList productListItem_clicked = null;
    ArrayList<Integer> items = new ArrayList<>();

    ArrayList<MerchantOutstandSalesOrderList> merchantOutstandSalesOrderLists_arraylist = new ArrayList<>();

    ArrayList<CategoryDetails> arrayCategory = new ArrayList<>();
    ArrayList<String> arrayStringBrandlist = new ArrayList<>();

    ArrayList<ProductList> productArrayList = new ArrayList<>();    //first loading products list
    ProductListRV productListRV;
    static ArrayList<ProductListItemPosition> productListItemPositions = new ArrayList<>();

    ArrayList<ProductListLast> productArrayList_last = new ArrayList<>();
    ArrayList<ReturnInventoryLineItem> productsReturns = new ArrayList<>();

    DbHelper db ;

    public void init() {
        SessionManager.pref = getSharedPreferences("login.conf", Context.MODE_PRIVATE);
        SessionManager.editor = SessionManager.pref.edit();
        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.tb_main);
        mToolbar.setTitle("Create Sales Order - Step 04");

        syncDate = "/Date(" + (new DateManager()).todayMillsec() + ")/";
        tod = syncDate;

        db = new DbHelper(this);

        //getUser details
        Gson gson = new Gson();
        String getJson = SessionManager.pref.getString("user", "");
        LoginUser prefUser = gson.fromJson(getJson, LoginUser.class);
        userTypeID = prefUser.getUserTypeId();

//      enteredUserName = "Kasun Bandara";//enteruser.getName();

        merchant_name_ = getIntent().getExtras().getString("merchant_name_");
        merchant_id_ = getIntent().getExtras().getInt("merchant_id_");
        merchant_isvat_ = getIntent().getExtras().getInt("merchant_isvat_");
        distributor_id = getIntent().getExtras().getInt("distributor_id");
        distributor_name = getIntent().getExtras().getString("distributor_name");
        distributor_isvat = getIntent().getExtras().getInt("distributor_isvat");
        salesOrder_isvat = getIntent().getExtras().getInt("vatType");
        salesrep_type = getIntent().getExtras().getString("salesrep_type");

        credit_type = getIntent().getExtras().getInt("merchant_payment_method");
        credit_days = getIntent().getExtras().getInt("merchant_daycount");

        System.out.println("Loding data ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("merchant id - " + merchant_id_);
        System.out.println("merchant name - " + merchant_name_);
        System.out.println("merchant isvat - " + merchant_isvat_);
        System.out.println("distributor id - " + distributor_id);
        System.out.println("distributor_name - " + distributor_name);
        System.out.println("distributor_isvat -  " + distributor_isvat);
        System.out.println("salesOrder_isvat - " + salesOrder_isvat);
        System.out.println("salesrep_type - " + salesrep_type);
        System.out.println("credit_type - " + credit_type);
        System.out.println("credit_days - " + credit_days);

        rv_salesitems = (RecyclerView) findViewById(R.id.rv_salesitems);
        sd_brandnames = (TextView) findViewById(R.id.tv_brandname_spindialog);
        sv_items = (SearchView) findViewById(R.id.sv_items);

        //if panic moment- do disable searchview and brand name choose
        //sd_brandnames.setEnabled(false);

        ll_sales_order = (LinearLayout) findViewById(R.id.ll_listitems);
        btn_request_send = (Button) findViewById(R.id.btn_request_send);

        tv_vat_header = (TextView) findViewById(R.id.tv_total_vat_value_header);
        tv_vat_amount = (TextView) findViewById(R.id.tv_total_vat_value);
        tv_total_grand = (TextView) findViewById(R.id.tv_total_grand_value);
        tv_free_issues = (TextView) findViewById(R.id.tv_free_issued_value);
        tv_net_amount = (TextView) findViewById(R.id.tv_netamount_value);

        if(distributor_isvat != 1 || merchant_isvat_ != 1){
            salesOrder_isvat = 2;
        }else if(distributor_isvat == 1 && merchant_isvat_ == 1){
            salesOrder_isvat = 1;
        }else{
            salesOrder_isvat = 0;
        }


        if (salesOrder_isvat == 1) {
            tv_vat_header.setVisibility(View.VISIBLE);
            tv_vat_amount.setVisibility(View.VISIBLE);
        } else if (salesOrder_isvat != 1) {
            tv_vat_header.setVisibility(View.GONE);
            tv_vat_amount.setVisibility(View.GONE);
        }

        //brandname loading
        arrayCategory.clear();
        SyncManager syncManager = new SyncManager(getApplicationContext());
        String sql3 = "SELECT * FROM " + DbHelper.TABLE_CATEGORY;
        arrayCategory = syncManager.getCategoryDetailsFromSQLite(sql3);
        System.out.println("arrayCategory.size - "+arrayCategory.size());
        loadBrandsNames(arrayCategory);
        

        //initial loading product list
        productArrayList = syncManager.getProductListFromSQLite(salesOrder_isvat, 0, salesrep_type);

        for(int h=0;h< productArrayList.size() ; h++){
            //adding to a static position
            productListItemPositions.add
                    (h,new ProductListItemPosition(h,productArrayList.get(h).getProductid(),productArrayList.get(h).getBatchid()));
            System.out.println("productListItemPositions "
                    +" - pos id : "+productListItemPositions.get(h).getPosition()
                    +" - pro id : "+productListItemPositions.get(h).getProduct_id()
                    +" - bat id : "+productListItemPositions.get(h).getBatch_id());
        }

        System.out.println("isItemFromSearchArray - "+isItemFromSearchArray);

        productListRV = new ProductListRV(productArrayList, productArrayList, productsReturns, getApplicationContext(), new ProductListRV.OnItemClickListener() {
            @Override
            public void onItemClick(ProductList productList, int i) {
                showBox(productList);
                position_ = i;
                productListItem_clicked = productList;
            }
        });
        rv_salesitems.setItemAnimator(new DefaultItemAnimator());
        rv_salesitems.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_salesitems.setAdapter(productListRV);


        ArrayList<Integer> categoryIdList = new ArrayList<>();
        for (int k = 0; k < productArrayList.size(); k++) {
            int h = productArrayList.get(k).getCategoryid();
            categoryIdList.add(h);
        }

        //search view
        search(sv_items);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_create_sales_order_continue);


        System.out.println("create s o ..");
        init();

        sd_brandnames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sd_brandname.showSpinerDialog();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.csoc_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        loadOutstandingList();

        navigationView = (NavigationView) findViewById(R.id.csoc_nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case 0:
                        //Toast.makeText(CreateSalesOrderContinue.this, "id 0000", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        //Toast.makeText(CreateSalesOrderContinue.this, "id 1111", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });

        btn_request_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long tod_long = (new DateManager()).todayMillsec();
                String tod = "/Date(" + tod_long + ")/";

                if (productArrayList_last.size() > 0) {
                    if (salesOrder_isvat == 0 || salesOrder_isvat == 2) {
                        putExtrasToLast(productArrayList_last, productsReturns, merchant_id_, distributor_id,
                                userTypeID, salestypeid, tod, enteredUserName, salesstatus, TotalVatAmount,
                                0, is_return, tod, grandTotal, TOTAL_free_issues,newStock,credit_type,credit_days);
                    } else if (salesOrder_isvat == 1) {
                        putExtrasToLast(productArrayList_last, productsReturns, merchant_id_, distributor_id,
                                userTypeID, salestypeid, tod, enteredUserName, salesstatus, TotalVatAmount,
                                1, is_return, tod, grandTotal, TOTAL_free_issues,newStock,credit_type,credit_days);
                    }
                } else {
                    displayMessage("Select items before go any further");
                }
            }
        });
    }

    public void loadBrandsNames(final ArrayList<CategoryDetails> categoryDetailses) {
        arrayStringBrandlist.add(0, "All");
        for (int i = 1; i < categoryDetailses.size() + 1; i++) {
            arrayStringBrandlist.add(categoryDetailses.get(i - 1).getCategoryName());
        }
        sd_brandname = new SpinnerDialog(CreateSalesOrderContinue.this, arrayStringBrandlist, "Select A Brand", R.style.DialogAnimations_SmileWindow);

        sd_brandname.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String s, int i) {
                sd_brandnames.setText(s);
                //for (int i2 = 0; i2 < categoryDetailses.size() + 1; i2++) {
                    if (i == 0) {

                        getProductListFromSQLiteLast(0);
                        sd_brandnames.setText("All");
                        isItemFromSearchArray = 0;
                        isItemFromBrandArray = 0;
                        //break;

                    }
                    if (categoryDetailses.get(i - 1).getCategoryName().equals(s) && i != 0) {

                        categoryId = categoryDetailses.get(i - 1).getCategoryid();
                        getProductListFromSQLiteLast(categoryId);
                        isItemFromSearchArray = 1;
                        isItemFromBrandArray = 1;
                        sd_brandnames.setText(categoryDetailses.get(i - 1).getCategoryName());
                        //break;
                    }
                //}
            }
        });
    }


    private void getProductListFromSQLiteLast(int cat_id) {

        ArrayList<ProductList> pl = new ArrayList<>();

        for (int i = 0; i < productArrayList.size(); i++) {
            if (cat_id == productArrayList.get(i).getCategoryid() && cat_id != 0) {
                pl.add(productArrayList.get(i));
            }
        }
        if (cat_id == 0) {
            for (int i2 = 0; i2 < productArrayList.size(); i2++) {
                pl.add(i2, productArrayList.get(i2));
            }
        }

        if (pl.size() == 0) {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(CreateSalesOrderContinue.this);
            View mView = getLayoutInflater().inflate(R.layout.layout_for_custom_message, null);

            TextView tvOk, tvMessage;
            tvOk = (TextView) mView.findViewById(R.id.tvOk);
            tvMessage = (TextView) mView.findViewById(R.id.tvMessage);
            tvMessage.setText("No products, related to this category");

            //if no products,load the default array
            tvOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    productListRV = new ProductListRV(productArrayList, productArrayList, productsReturns, getApplicationContext(), new ProductListRV.OnItemClickListener() {
                        @Override
                        public void onItemClick(ProductList productList, int i) {
                            //Toast.makeText(CreateSalesOrderContinue.this, "Item i - " + i, Toast.LENGTH_SHORT).show();
                            showBox(productList);
                            position_ = i;
                        }
                    });

                    rv_salesitems.setItemAnimator(new DefaultItemAnimator());
                    rv_salesitems.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    rv_salesitems.setAdapter(productListRV);

                    productListRV.notifyDataSetChanged();

                }
            });



            mBuilder.setView(mView);
            dialog = mBuilder.create();
            dialog.show();
        }

        System.out.println("pl.size() - "+pl.size());

        productListRV = new ProductListRV(pl, pl, productsReturns, getApplicationContext(), new ProductListRV.OnItemClickListener() {
            @Override
            public void onItemClick(ProductList productList, int i) {
                //Toast.makeText(CreateSalesOrderContinue.this, "Item i - " + i, Toast.LENGTH_SHORT).show();
                showBox(productList);
                position_ = i;
            }
        });

        rv_salesitems.setItemAnimator(new DefaultItemAnimator());
        rv_salesitems.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_salesitems.setAdapter(productListRV);

        productListRV.notifyDataSetChanged();

    }


    public void showBox(final ProductList productList) {

        unitprice = productList.getUnitPrice();
        expiredate = productList.getExpireDate();
        stock = productList.getStock();
        name = productList.getName();
        QTY = productList.getQty();
        FREEQTY = productList.getFreeQty();
        ISVAT = productList.getIsVatHas();
        VATRATE = productList.getVatRate();
        BATCHID = productList.getBatchid();
        PRODUCTID = productList.getProductid(); //uniq
        RETURNQTY = productList.getReturnqty();
        CATEGORYID = productList.getCategoryid();

        final int salesOrderID = 0;
        final double unitSellingDiscount = 0.0;
        final double totalDiscount = 0.0;
        final int isSync = 1;
        x[0] = productList.getBatchid();
        x[1] = productList.getProductid();

        System.out.println("Clicked a Row ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("unitprice -" + unitprice);
        System.out.println("stock -" + stock);
        System.out.println("name -" + name);
        System.out.println("QTY -" + QTY);
        System.out.println("FREEQTY -" + FREEQTY);
        System.out.println("ISVAT -" + ISVAT);
        System.out.println("VATRATE -" + VATRATE);
        System.out.println("BATCHID -" + BATCHID);
        System.out.println("PRODUCTID -" + PRODUCTID);
        System.out.println("RETURNQTY -" + RETURNQTY);
        System.out.println("CATEGORYID -" + CATEGORYID);
        System.out.println("inside of showBox,isItemFromSearchArray - "+isItemFromSearchArray);

        selectedProductItem = PRODUCTID;

        AlertDialog.Builder builder = new AlertDialog.Builder(CreateSalesOrderContinue.this);
        View view1 = getLayoutInflater().inflate(R.layout.layout_for_create_sales_order_final_window, null);
        builder.setView(view1);
        dialog = builder.create();
        dialog.show();

        final EditText et_qty, et_free_qty, et_return_qty;
        final Button btn_add, btn_update, btn_remove,btn_viewpromotion,btnPromotion;
        TextView tv_itemanme_stock;

        tv_itemanme_stock = (TextView) view1.findViewById(R.id.tv_itemname_and_stock);
        tv_itemanme_stock.setText(name + " (Stock - " + stock + " )");

        et_qty = (EditText) view1.findViewById(R.id.et_cso_quantity);
        et_free_qty = (EditText) view1.findViewById(R.id.et_cso_free_quantity);
        et_return_qty = (EditText) view1.findViewById(R.id.et_cso_return_quantity);
        btn_add = (Button) view1.findViewById(R.id.btn_add_sales_itemto_cart);
        btn_update = (Button) view1.findViewById(R.id.btn_update_sales_itemto_cart);
        btn_remove = (Button) view1.findViewById(R.id.btn_remove_sales_itemto_cart);
        btn_viewpromotion = (Button) view1.findViewById(R.id.btn_view_promotion);
        btnPromotion = (Button) view1.findViewById(R.id.btn_promotion);


        if (QTY != 0 || FREEQTY != 0 || RETURNQTY != 0) {
            System.out.println("qty !=0 , freeqty != 0 , returnqty !=0 ~~~~~~~can update,remove~~~");
            et_qty.setText(String.valueOf(QTY));
            et_free_qty.setText(String.valueOf(FREEQTY));
            et_return_qty.setText(String.valueOf(RETURNQTY));
            btn_add.setEnabled(false);
            btn_update.setEnabled(true);
            btn_remove.setEnabled(true);
        }

        if (QTY == 0 && FREEQTY == 0 && RETURNQTY == 0) {
            System.out.println("qty ==0 , freeqty == 0 , returnqty ==0 ~~~~~~~~~can add~~~~~~~~~~~");
            btn_add.setEnabled(true);
            btn_update.setEnabled(false);
            btn_remove.setEnabled(false);
        }

        btn_add.setOnClickListener(new View.OnClickListener() {//~~~~~~~~~~~~~~~~~~~~~~~~~~~
            @Override
            public void onClick(View v) {
                if (!et_qty.getText().toString().equals("")) {
                    qty = Integer.parseInt(et_qty.getText().toString());
                } else {
                    qty = 0;
                }
                if (!et_free_qty.getText().toString().equals("")) {
                    freeqty = Integer.parseInt(et_free_qty.getText().toString());
                } else {
                    freeqty = 0;
                }
                if (!et_return_qty.getText().toString().equals("")) {
                    returnQty = Integer.parseInt(et_return_qty.getText().toString());
                } else {
                    returnQty = 0;
                }

                System.out.println("Adding ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                System.out.println("qty - " + qty);
                System.out.println("freeqty - " + freeqty);
                System.out.println("returnQty - " + returnQty);

                double grandtotal = 0;
                double itemVatAmount = 0;
                double freeIssuedAmount = 0;
                double netvalue = 0;

                        /* IMPORTANT ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                        P.W.V + (VatRate*P.W.V)/100 = Price

                        (V.R*P.W.V)/100 = (V.R * PRICE)/(100+V.R) <------ this is what we need to find out in every item,
                        and multiply by qty,finnally get the sum of every selected products TotalVat

                        (V.R * PRICE) * QTY /(100+V.R)
                         */

                if (VATRATE != 0 && unitprice != 0) {
                    itemVatAmount = (VATRATE * unitprice * qty) / (100 + VATRATE);
                } else {
                    itemVatAmount = 0.00;
                }
                grandtotal = unitprice * (qty + freeqty);
                freeIssuedAmount = unitprice * (freeqty);
                netvalue = unitprice * qty;
//                        if(salesOrder_isvat == 1){
//                            netvalue = (unitprice * qty) - itemVatAmount;
//                        }else if(salesOrder_isvat != 1){
//                            netvalue = unitprice * qty;
//                        }

                System.out.println("Adding row values ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
                System.out.println("freeIssuedAmount - " + freeIssuedAmount);
                System.out.println("netvalue - " + netvalue);
                System.out.println("grandtotal - " + grandtotal);
                System.out.println("itemVatAmount - " + itemVatAmount);

                newStock = stock - (qty + freeqty);
                System.out.println("!!!!!!!!!!!!!!\nstock - "+stock);
                System.out.println("qty - "+qty);
                System.out.println("freeqty - "+freeqty);
                System.out.println("newStock - "+newStock);

                if (qty + freeqty <= stock) {
                    if (qty == 0 && freeqty == 0 && returnQty > 0) {
                        productsReturns.add(new ReturnInventoryLineItem(0, PRODUCTID, BATCHID, returnQty, 0.0, 0, 0, 0.0, name, 0, 0));

                        ProductList productList1 = new ProductList(name, expiredate, newStock, unitprice, grandtotal, qty, freeqty,
                                img_done, BATCHID, PRODUCTID, ISVAT, VATRATE, returnQty, freeIssuedAmount, netvalue, grandtotal, CATEGORYID);

                        if(isItemFromSearchArray == 0 ){

                            System.out.println("position_ "+position_);
                            productArrayList.remove(position_);
                            productArrayList.add(position_, productList1);
                            productListRV.update(productArrayList, position_, itemVatAmount);
                            sv_items.setQuery("",false);
                            sv_items.clearFocus();
                            isItemFromSearchArray = 0;

                        }else if(isItemFromSearchArray == 1){

                            for(int i=0;i< productArrayList.size() ;i++){
                                if(productListItemPositions.get(i).getBatch_id() == BATCHID &&
                                        productListItemPositions.get(i).getProduct_id() == PRODUCTID ){
                                    position_ = productListItemPositions.get(i).getPosition();
                                }
                            }

                            System.out.println("position_ "+position_);
                            productArrayList.remove(position_);
                            productArrayList.add(position_, productList1);
                            productListRV.update(productArrayList, position_, itemVatAmount);
                            sv_items.setQuery("",false);
                            sv_items.clearFocus();
                            isItemFromSearchArray = 0;

                        }
                        if(isItemFromBrandArray == 1){
                            productListRV = new ProductListRV(productArrayList, productArrayList, productsReturns, getApplicationContext(),
                                    new ProductListRV.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(ProductList productList, int position) {
                                            showBox(productList);
                                            position_ = position;
                                        }
                                    });
                            rv_salesitems.setAdapter(productListRV);
                        }


                        dialog.dismiss();
//                        productArrayList_last.add(new ProductListLast(salesOrderID, BATCHID, PRODUCTID, qty, freeqty,
//                                unitprice, unitSellingDiscount, netvalue, totalDiscount, itemVatAmount, isSync, name,
//                                expiredate, newStock, img_done, ISVAT, VATRATE, grandtotal, freeIssuedAmount, netvalue));

                    } else if (qty > 0 || freeqty > 0) {
                        //return qty >= 0

                        productArrayList_last.add(new ProductListLast(salesOrderID, BATCHID, PRODUCTID, qty, freeqty,
                                unitprice, unitSellingDiscount, netvalue, totalDiscount, itemVatAmount, isSync, name,
                                expiredate, newStock, img_done, ISVAT, VATRATE, grandtotal, freeIssuedAmount, netvalue));
                        //add returns if any available
                        if (returnQty > 0) {
                            productsReturns.add(new ReturnInventoryLineItem(0, PRODUCTID, BATCHID, returnQty, 0.0, 0, 0, 0.0, name, 0, 0));
                        }

                        ProductList productList1 = new ProductList(name, expiredate, newStock, unitprice, grandtotal, qty, freeqty,
                                img_done, BATCHID, PRODUCTID, ISVAT, VATRATE, returnQty, freeIssuedAmount, netvalue, grandtotal, CATEGORYID);

                        if(isItemFromSearchArray == 0){

                            //if item clicked first position
                            System.out.println("position_ "+position_);
                            productArrayList.remove(position_);
                            productArrayList.add(position_, productList1);
                            productListRV.update(productArrayList, position_, itemVatAmount);
                            sv_items.setQuery("",false);
                            sv_items.clearFocus();
                            isItemFromSearchArray = 0;

                        }else if(isItemFromSearchArray == 1){
                            //item clicked after search
                            for(int i=0;i< productArrayList.size() ;i++){
                                //find a place it has been very first
                                if(productListItemPositions.get(i).getBatch_id() == BATCHID &&
                                        productListItemPositions.get(i).getProduct_id() == PRODUCTID ){
                                    position_ = productListItemPositions.get(i).getPosition();
                                }
                            }

                            System.out.println("position_ "+position_);
                            productArrayList.remove(position_);
                            productArrayList.add(position_, productList1);
                            productListRV.update(productArrayList, position_, itemVatAmount);
                            sv_items.setQuery("",false);
                            sv_items.clearFocus();
                            isItemFromSearchArray = 0;
                        }
                        //if we choose brandname,then we dnt have to filter any charactor,so
                        if(isItemFromBrandArray == 1){
                            productListRV = new ProductListRV(productArrayList, productArrayList, productsReturns, getApplicationContext(),
                                    new ProductListRV.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(ProductList productList, int position) {
                                            showBox(productList);
                                            position_ = position;
                                        }
                                    });
                            rv_salesitems.setAdapter(productListRV);
                        }

                        grandTotal = grandTotal + (grandtotal);
                        TotalVatAmount = TotalVatAmount + itemVatAmount;
                        TOTAL_free_issues = TOTAL_free_issues + freeIssuedAmount;
                        TOTAL_net_amount = TOTAL_net_amount + netvalue;

                        System.out.println("Counting whole values ~~~~Adding~~~~~~~~~~~~~~~~~~~~~~~~\n");
                        System.out.println("TOTAL_free_issues - " + TOTAL_free_issues);
                        System.out.println("TOTAL_net_amount - " + TOTAL_net_amount);
                        System.out.println("grandTotal - " + grandTotal);
                        System.out.println("TotalVatAmount - " + TotalVatAmount);
                        //System.out.println("new Stock - "+newStock);

                        tv_vat_amount.setText(String.format("Rs. %.2f", TotalVatAmount));
                        tv_total_grand.setText(String.format("Rs. %.2f", grandTotal));
                        tv_free_issues.setText(String.format("Rs. %.2f", TOTAL_free_issues));
                        tv_net_amount.setText(String.format("Rs. %.2f", TOTAL_net_amount));

                        dialog.dismiss();
                    }
                } else if (qty + freeqty > stock) {
                    Toast.makeText(CreateSalesOrderContinue.this, "Insufficient Stock", Toast.LENGTH_SHORT).show();

                } else {
                    et_qty.setText("");
                    et_free_qty.setText("");
                }
            }
        });


        btn_update.setOnClickListener(new View.OnClickListener() {//~~~~~~~~~~~~~~~~~~~~~~~~
            @Override
            public void onClick(View v) {
                btn_add.setEnabled(true);
                btn_update.setEnabled(true);
                btn_remove.setEnabled(true);

                double grandtotal = 0, oldgrandtotal = 0;
                double itemVatAmount = 0, olditemVatAmount = 0;
                double freeIssuedAmount = 0, oldfreeIssuesAmount = 0;
                double netvalue = 0, oldNetValue = 0;

                int newQty;
                int newFreeQty;
                int newReturnQty;

                if (!et_qty.getText().toString().equals("")) {
                    newQty = Integer.parseInt(et_qty.getText().toString());
                } else {
                    newQty = 0;
                }

                if (!et_free_qty.getText().toString().equals("")) {
                    newFreeQty = Integer.parseInt(et_free_qty.getText().toString());
                } else {
                    newFreeQty = 0;
                }

                if (!et_return_qty.getText().toString().equals("")) {
                    newReturnQty = Integer.parseInt(et_return_qty.getText().toString());
                } else {
                    newReturnQty = 0;
                }


                int allQty = QTY + FREEQTY ;
                int newAllQty = newQty+ newFreeQty ;

                newStock = stock + allQty - newAllQty;

                if (newQty + newFreeQty <= stock) {

                    grandtotal = unitprice * (newQty + newFreeQty);
                    freeIssuedAmount = unitprice * newFreeQty;
                    oldgrandtotal = unitprice * (QTY + FREEQTY);
                    oldfreeIssuesAmount = unitprice * FREEQTY;

                    if (VATRATE != 0 && unitprice != 0) {
                        itemVatAmount = (VATRATE * unitprice * newQty) / (100 + VATRATE);
                        olditemVatAmount = (VATRATE * unitprice * QTY) / (100 + VATRATE);
                    } else {
                        itemVatAmount = 0.00;
                        olditemVatAmount = 0.00;
                    }

                    netvalue = unitprice * newQty;
                    oldNetValue = unitprice * QTY;


                    System.out.println("Updating row values ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");

                    System.out.println("oldfreeIssuesAmount - " + oldfreeIssuesAmount);
                    System.out.println("oldNetValue - " + oldNetValue);
                    System.out.println("oldgrandtotal - " + oldgrandtotal);
                    System.out.println("olditemVatAmount - " + olditemVatAmount + "\n");

                    System.out.println("freeIssuedAmount - " + freeIssuedAmount);
                    System.out.println("netvalue - " + netvalue);
                    System.out.println("grandtotal - " + grandtotal);
                    System.out.println("itemVatAmount - " + itemVatAmount);

//                            if(salesOrder_isvat == 1){
//                                netvalue = (unitprice * newQty) - itemVatAmount;
//                                oldNetValue = (unitprice * qty) - olditemVatAmount;
//                            }else if(salesOrder_isvat != 1){
//                                netvalue = unitprice * newQty;
//                                oldNetValue = unitprice * qty;
//                            }


                    for (int i = 0; i < productArrayList_last.size(); i++) {
                        if (x[0] == productArrayList_last.get(i).getBatchID() && x[1] == productArrayList_last.get(i).getProductID()) {

                            double freeDif = freeIssuedAmount - oldfreeIssuesAmount;
                            double netDiff = netvalue - oldNetValue;
                            double grandDiff = grandtotal - oldgrandtotal;
                            double vatDiff = itemVatAmount - olditemVatAmount;

                            System.out.println("freeDif - "+freeDif);
                            System.out.println("netDiff - "+netDiff);
                            System.out.println("grandDiff - "+grandDiff);
                            System.out.println("vatDiff - "+vatDiff);

                            grandTotal = grandTotal + grandDiff;
                            TOTAL_free_issues = TOTAL_free_issues + freeDif;
                            TotalVatAmount = TotalVatAmount + vatDiff;
                            TOTAL_net_amount = TOTAL_net_amount + netDiff;

                            System.out.println("Counting whole values ~~~~Updating~~~~~~~~~~~~~~~~~~~~~~\n");
                            System.out.println("TOTAL_free_issues - " + TOTAL_free_issues);
                            System.out.println("TOTAL_net_amount - " + TOTAL_net_amount);
                            System.out.println("grandTotal - " + grandTotal);
                            System.out.println("TotalVatAmount - " + TotalVatAmount);
                            System.out.println("new Stock - "+newStock);

                            productArrayList_last.remove(i);
                            productArrayList_last.add(
                                    new ProductListLast(salesOrderID, BATCHID, PRODUCTID, newQty, newFreeQty,
                                            unitprice, unitSellingDiscount, netvalue, totalDiscount, vatDiff, isSync, name,
                                            expiredate, newStock, img_done, ISVAT, VATRATE, grandtotal, freeIssuedAmount, netvalue));
                            System.out.println("pass last array");
                            break;
                        }
                    }

                    if (newReturnQty > 0){
                        for (int i = 0; i < productsReturns.size(); i++) {
                            //add returns if any available
                            if (x[0] == productsReturns.get(i).getBatchId() && x[1] == productsReturns.get(i).getProductId()) {
                                productsReturns.remove(i);
                            }
                        }
                        productsReturns.add(new ReturnInventoryLineItem(0, PRODUCTID, BATCHID, newReturnQty, 0.0, 0, 0, 0.0, name, 0, 0));
                    }
                    if(newReturnQty == 0){
                        for (int i = 0; i < productsReturns.size(); i++) {
                            //add returns if any available
                            if (x[0] == productsReturns.get(i).getBatchId() && x[1] == productsReturns.get(i).getProductId()) {
                                productsReturns.remove(i);
                            }
                        }
                    }


                    ProductList productList1 = new ProductList(name, expiredate, newStock, unitprice, grandtotal, newQty, newFreeQty,
                            img_done, BATCHID, PRODUCTID, ISVAT, VATRATE, newReturnQty, freeIssuedAmount, netvalue, grandtotal, CATEGORYID);


                    if(isItemFromSearchArray == 0){

                        System.out.println("position_ "+position_);
                        productArrayList.remove(position_);
                        productArrayList.add(position_, productList1);
                        productListRV.update(productArrayList, position_, itemVatAmount);
                        sv_items.setQuery("",false);
                        sv_items.clearFocus();
                        isItemFromSearchArray = 0;

                    }else if(isItemFromSearchArray == 1){
                        for(int i=0;i< productArrayList.size() ;i++){
                            if(productListItemPositions.get(i).getBatch_id() == BATCHID &&
                                    productListItemPositions.get(i).getProduct_id() == PRODUCTID ){
                                position_ = productListItemPositions.get(i).getPosition();
                            }
                        }

                        System.out.println("position_ "+position_);
                        productArrayList.remove(position_);
                        productArrayList.add(position_, productList1);
                        productListRV.update(productArrayList, position_, itemVatAmount);
                        sv_items.setQuery("",false);
                        sv_items.clearFocus();
                        isItemFromSearchArray = 0;
                    }

                    tv_vat_amount.setText(String.format("Rs. %.2f", TotalVatAmount));
                    tv_total_grand.setText(String.format("Rs. %.2f", grandTotal));
                    tv_free_issues.setText(String.format("Rs. %.2f", TOTAL_free_issues));
                    tv_net_amount.setText(String.format("Rs. %.2f", TOTAL_net_amount));

                    dialog.dismiss();

                } else {
                    Toast.makeText(CreateSalesOrderContinue.this, "Insufficient Stock.", Toast.LENGTH_SHORT).show();
                    et_qty.setText("");
                    et_free_qty.setText("");
                }
                dialog.dismiss();
            }
        });


        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double grandtotal = 0;
                double itemVatAmount = 0;
                double freeIssuedAmount = 0;
                double netvalue = 0;

                if (!et_qty.getText().toString().equals("")) {
                    qty = Integer.parseInt(et_qty.getText().toString());
                } else {
                    qty = 0;
                }

                if (!et_free_qty.getText().toString().equals("")) {
                    freeqty = Integer.parseInt(et_free_qty.getText().toString());
                } else {
                    freeqty = 0;
                }

                if (!et_return_qty.getText().toString().equals("")) {
                    returnQty = Integer.parseInt(et_return_qty.getText().toString());
                } else {
                    returnQty = 0;
                }


                if (VATRATE != 0 && unitprice != 0) {
                    itemVatAmount = (VATRATE * unitprice * qty) / (100 + VATRATE);
                } else {
                    itemVatAmount = 0.00;
                }
                grandtotal = unitprice * (qty + freeqty);
                freeIssuedAmount = unitprice * freeqty;
//                        if(salesOrder_isvat == 1){
//                            netvalue = (unitprice * qty) - itemVatAmount;
//                        }else if(salesOrder_isvat != 1){
//                            netvalue = unitprice * qty;
//                        }
                netvalue = unitprice * qty;

                //int allQty = qty +freeqty;
                newStock = stock + qty +freeqty;

                System.out.println("Removing row values ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
                System.out.println("freeIssuedAmount - " + freeIssuedAmount);
                System.out.println("netvalue - " + netvalue);
                System.out.println("grandtotal - " + grandtotal);
                System.out.println("itemVatAmount - " + itemVatAmount);
                System.out.println("new Stock - "+newStock);

                et_qty.setText("0");
                et_free_qty.setText("0");
                et_return_qty.setText("0");


                for (int i = 0; i < productArrayList_last.size(); i++) {
                    if (x[0] == productArrayList_last.get(i).getBatchID() && x[1] == productArrayList_last.get(i).getProductID()) {
                        productArrayList_last.remove(i);
                    }
                }

                if (returnQty > 0) {
                    for (int i = 0; i < productsReturns.size(); i++) {
                        //add returns if any available
                        if (x[0] == productsReturns.get(i).getBatchId() && x[1] == productsReturns.get(i).getProductId()) {
                            productsReturns.remove(i);
                        }
                    }
                }
                ProductList productList1 = new ProductList(name, expiredate, newStock, unitprice, 0, 0, 0,
                        img_not_done, BATCHID, PRODUCTID, ISVAT, VATRATE, 0, 0, 0, 0, CATEGORYID);

                if(isItemFromSearchArray == 0){

                    System.out.println("position_ "+position_);
                    productArrayList.remove(position_);
                    productArrayList.add(position_, productList1);
                    productListRV.update(productArrayList, position_, itemVatAmount);
                    sv_items.setQuery("",false);
                    sv_items.clearFocus();
                    isItemFromSearchArray = 0;

                }else if(isItemFromSearchArray == 1){

                    for(int i=0;i< productArrayList.size() ;i++){

                        if(productListItemPositions.get(i).getBatch_id() == BATCHID &&
                                productListItemPositions.get(i).getProduct_id() == PRODUCTID ){
                            position_ = productListItemPositions.get(i).getPosition();
                        }
                    }

                    System.out.println("position_ "+position_);
                    productArrayList.remove(position_);
                    productArrayList.add(position_, productList1);
                    productListRV.update(productArrayList, position_, itemVatAmount);
                    sv_items.setQuery("",false);
                    sv_items.clearFocus();
                    isItemFromSearchArray = 0;
                }

                grandTotal = grandTotal - grandtotal;
                TotalVatAmount = TotalVatAmount - itemVatAmount;
                TOTAL_free_issues = TOTAL_free_issues - freeIssuedAmount;
                TOTAL_net_amount = TOTAL_net_amount - netvalue;

                System.out.println("Counting whole values ~~~~removing~~~~~~~~~~~~~~~~~~~~~~\n");
                System.out.println("TOTAL_free_issues - " + TOTAL_free_issues);
                System.out.println("TOTAL_net_amount - " + TOTAL_net_amount);
                System.out.println("grandTotal - " + grandTotal);
                System.out.println("TotalVatAmount - " + TotalVatAmount);

                dialog.dismiss();

                tv_vat_amount.setText(String.format("Rs. %.2f", TotalVatAmount));
                tv_total_grand.setText(String.format("Rs. %.2f", grandTotal));
                tv_free_issues.setText(String.format("Rs. %.2f", TOTAL_free_issues));
                tv_net_amount.setText(String.format("Rs. %.2f", TOTAL_net_amount));
            }
        });


        //int catImgindex = getCatImgIndex(productList.getProductid(),productList.getBatchid());
        boolean isPromotion = getCatelogType(productList.getProductid(),productList.getBatchid(),1);
        boolean isCatelog = getCatelogType(productList.getProductid(),productList.getBatchid(),0);
        if (isPromotion){
            btnPromotion.setVisibility(View.VISIBLE);
        }else{
            btnPromotion.setVisibility(View.GONE);
        }

        if (isCatelog){
            btn_viewpromotion.setVisibility(View.VISIBLE);
        }else{
            btn_viewpromotion.setVisibility(View.GONE);
        }


        btn_viewpromotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sql = "SELECT "+COL_PROMOTION_CatalogueType+" FROM "+
                        TABLE_PROMOTION+" WHERE " +
                        COL_PROMOTION_ProductId+" = "+PRODUCTID+" AND "+
                        COL_PROMOTION_BatchId+" = "+BATCHID;

                Cursor cursor = null;
                int cat_type = 0;
                /*
                try{
                    cursor = (new DbHelper(getApplicationContext())).getReadableDatabase().rawQuery(sql,null);
                    if(cursor.getCount() > 0 && cursor != null){

                        while(cursor.moveToFirst()){
                            cat_type = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_PROMOTION_CatalogueType));
                        }
                    }
                }catch(Exception e){
                    System.out.println("========= Error at View Promotion ============");
                    e.printStackTrace();
                }*/
                Intent intent = new Intent(CreateSalesOrderContinue.this,ViewPromotion.class);
                intent.putExtra("nameItem",name);
                intent.putExtra("BATCHIDItem",BATCHID);
                intent.putExtra("PRODUCTIDItem",PRODUCTID);
                intent.putExtra("CAT_TYPEItem",1);

                System.out.println("name - "+name);
                System.out.println("BATCHID - "+BATCHID);
                System.out.println("PRODUCTID - "+PRODUCTID);
                System.out.println("cat_type - "+0);

                startActivity(intent);
            }
        });

        btnPromotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CreateSalesOrderContinue.this,ViewPromotion.class);
                intent.putExtra("nameItem",name);
                intent.putExtra("BATCHIDItem",BATCHID);
                intent.putExtra("PRODUCTIDItem",PRODUCTID);
                intent.putExtra("CAT_TYPEItem",1);

                System.out.println("name - "+name);
                System.out.println("BATCHID - "+BATCHID);
                System.out.println("PRODUCTID - "+PRODUCTID);
                System.out.println("cat_type - "+0);

                startActivity(intent);
            }
        });
    }

    private boolean getCatelogType(int productid, int batchid,int catType) {
        SQLiteDatabase database = db.getWritableDatabase();
        int catelogType = 0;
        boolean isAvailable = false;
        String sql = "SELECT * FROM "+ TABLE_PROMOTION+" WHERE "
                + DbHelper.COL_PROMOTION_ProductId+" = '"+productid+"' AND "
                +DbHelper.COL_PROMOTION_BatchId+" = '"+batchid+"' AND "+COL_PROMOTION_CatalogueType + " = '"+ catType+"'" ;
        System.out.println("Image query: "+ sql);

        Cursor cursor;

        try{
            System.out.println("Inside try - catalog");
            cursor = database.rawQuery(sql,null);
            System.out.println("cursor size in image select: "+ cursor.getCount());//not execute
            if(cursor.getCount() > 0){
                System.out.println("catalog select");

                isAvailable = true;

                   /* while (cursor.moveToNext()){
                        System.out.println("inside catalog while");
                        catelogType = cursor.getInt(cursor.getColumnIndex(COL_PROMOTION_CatalogueType));
                    }
                    System.out.println("catalog Type SQL: "+catelogType);*/
            }else{
                System.out.println("no data in "+ TABLE_PROMOTION_IMAGE);
            }
        }catch(Exception e){
            System.out.println("Error at ProductListRv");
            e.printStackTrace();
        }
        return isAvailable;
    }


    public void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                productListRV.getFilter().filter(newText);
                isItemFromSearchArray = 1;
                return true;
            }
        });

        System.out.println("inside search Method,isItemFromSearchArray - "+isItemFromSearchArray);
    }
    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    //load outstand list to drawer
    private void loadOutstandingList() {

        System.out.println("merchant id - " + merchant_id_);
        String url_outstanding_list = HTTPPaths.seriveUrl + "GetOutstandingPaymentListByMerchant?merchantID=" + merchant_id_;
        System.out.println(url_outstanding_list);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_outstanding_list,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JsonObject jsonObject = Json.parse(response).asObject();
                        int id = jsonObject.get("ID").asInt();
                        //PaymentID,InvoiceID,TotalAmount,PaidAmount,PaymentType,PaymentDate

                        //Toast.makeText(CreateSalesOrderContinue.this, "outstandList id - "+id, Toast.LENGTH_SHORT).show();
                        if (id == 200) {
                            String url2 = response.replace("{\"Data\":\"", "{\"Data\": ");
                            String url3 = url2.replace("\",\"ID\":200}", "}");
                            String url4 = url3.replace("\\", "");
                            Toast.makeText(CreateSalesOrderContinue.this, "outstandList - " + id, Toast.LENGTH_SHORT).show();
                            if (response != null) {
                                try {
                                    JSONObject jsonObject_main = new JSONObject(url4);

                                    JSONArray jsonArray_main = jsonObject_main.getJSONArray("Data");

                                    for (int i = 0; i < jsonArray_main.length(); i++) {

                                        JSONObject jsonObject1 = jsonArray_main.getJSONObject(i);

                                        int paymentID = jsonObject1.getInt("PaymentID");
                                        int invoiceID = jsonObject1.getInt("InvoiceID");
                                        double totalAmount = jsonObject1.getDouble("TotalAmount");
                                        double paidAmount = jsonObject1.getDouble("PaidAmount");
                                        String paymentType = jsonObject1.getString("PaymentType");
                                        String paymentDate = jsonObject1.getString("PaymentDate");

                                        merchantOutstandSalesOrderLists_arraylist.add(new MerchantOutstandSalesOrderList(paymentID, invoiceID, totalAmount, paidAmount, paymentType, paymentDate));
                                        System.out.println("size -%%%% " + merchantOutstandSalesOrderLists_arraylist.size());

                                    }
                                    addMenuItem();

                                    //##############################################################
                                    //setAdapterForOutstandlist();

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
                        Toast.makeText(CreateSalesOrderContinue.this, "Volly error - " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }

    //create a new menu item in navigation drawer
    private void addMenuItem() {
        //System.out.println("inside of menu");
        Menu menu = navigationView.getMenu();
        SubMenu subMenu1, subMenu2;
        subMenu1 = menu.addSubMenu("Sales ( " + merchant_name_ + " )");
        // System.out.println("sizeeeeeeeeeee - "+merchantOutstandSalesOrderLists_arraylist.size());
        if (merchantOutstandSalesOrderLists_arraylist.size() > 0) {
            for (int i = 0; i < merchantOutstandSalesOrderLists_arraylist.size(); i++) {
                double outstanding = merchantOutstandSalesOrderLists_arraylist.get(i).getTotalAmount() - merchantOutstandSalesOrderLists_arraylist.get(i).getPaidAmount();
                subMenu1.add("P.ID - " + merchantOutstandSalesOrderLists_arraylist.get(i).getPaymentID() + "  Outstanding - Rs: " + outstanding);
            }
        }
        menu.addSubMenu("logout");

    }

    private void putExtrasToLast(ArrayList<ProductListLast> productListLasts, ArrayList<ReturnInventoryLineItem> returnProducts,
                                 int merchantid, int distId, int salesrepid, int salestypeid,
                                 String enterdate, String enteruser, int salesstatus, double totalvatamount,
                                 int isvat_all, int is_return, String estimate_date, double grandtotal,
                                 double freeTotal,int newstock,int credittype,int creditdays) {

        Intent intent = new Intent(CreateSalesOrderContinue.this, CreateSalesOrderLast.class);
        intent.putExtra("merchant_id", merchantid);
        intent.putExtra("distributor_id", distId);
        intent.putExtra("salesrep_id", salesrepid);
        intent.putExtra("salestype_id", salestypeid);
        intent.putExtra("enter_date", enterdate);
        intent.putExtra("enter_user", enteruser);
        intent.putExtra("sales_status", salesstatus);
        intent.putExtra("totalVatAmount", totalvatamount);
        intent.putExtra("grandtotal", grandtotal); //total amount
        intent.putExtra("estimate_date", estimate_date);
        intent.putExtra("is_vat_all", isvat_all);
        intent.putExtra("is_return", is_return);
        intent.putExtra("newStock",newstock);

        intent.putExtra("obj", productListLasts);
        intent.putExtra("objReturn", returnProducts);
        intent.putExtra("salesrep_type", salesrep_type);

        intent.putExtra("totalFreeIssue", freeTotal);
        intent.putExtra("creditType",credittype);
        intent.putExtra("creditDays",creditdays);


        System.out.println("grand totAL - " + grandtotal);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //CreateSalesOrderContinue.this.finish();
        startActivity(intent);
    }

    private void loadDrawer(Bundle savedInstanceState) {

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

        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolBar)
                .withDisplayBelowStatusBar(false)
                .withActionBarDrawerToggleAnimated(true)
                .withDrawerGravity(Gravity.LEFT)
                .withSavedInstance(savedInstanceState)
                .withSelectedItem(0)
                .withTranslucentStatusBar(true)
                .withCloseOnClick(false)
                .withAccountHeader(headerResult).build(); //set the AccountHeader  created earlier for the header


        loadOutstandingList();

       /* merchantOutstandingSalesOrderLists_arraylist.add(new MerchantOutstandingSalesOrderList("Sale 1","2017/03/23",24000.00,13440.00));
        merchantOutstandingSalesOrderLists_arraylist.add(new MerchantOutstandingSalesOrderList("Sale 2","2017/01/09",12000.00,10000.00));
        merchantOutstandingSalesOrderLists_arraylist.add(new MerchantOutstandingSalesOrderList("Sale 3","2017/04/23",19600.00,17000.00));
        merchantOutstandingSalesOrderLists_arraylist.add(new MerchantOutstandingSalesOrderList("Sale 4","2016/12/12",20000.00,15000.00));
        merchantOutstandingSalesOrderLists_arraylist.add(new MerchantOutstandingSalesOrderList("Sale 5","2017/01/20",25990.00,32000.00));

        result.addItem(new PrimaryDrawerItem().withName("Home").withIcon(GoogleMaterial.Icon.gmd_home));
        result.addItem(new PrimaryDrawerItem().withName("Sales List").withDescription("Merchant - "+merchant_name).withIcon(GoogleMaterial.Icon.gmd_list));
        for(int i=0;i< 5; i++){

            result.addItem(
                    new SecondaryDrawerItem().
                            withName(merchantOutstandingSalesOrderLists_arraylist.get(i).getSale_type()).
                            withDescription(
                                    "Total - "+merchantOutstandingSalesOrderLists_arraylist.get(i).getTotal_amount()+
                                            "       paid - "+merchantOutstandingSalesOrderLists_arraylist.get(i).getPaid_amount()).
                            withIdentifier(i+1).
                            withSelectedColor(3)
            );

        } */
        result.addItem(new PrimaryDrawerItem().withName("Logout").withIcon(GoogleMaterial.Icon.gmd_exit_to_app));

        result.setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                if (((Nameable) drawerItem).getName().getText(CreateSalesOrderContinue.this) == "Home") {

                }

                if (((Nameable) drawerItem).getName().getText(CreateSalesOrderContinue.this) == "Sales List") {

                    LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                    View view1 = layoutInflater.inflate(R.layout.layout_for_merchant_outstanding_list, ll_sales_order, false);
                    PopupWindow popupWindow = new PopupWindow(view1, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    popupWindow.setOutsideTouchable(true);
                    //popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    popupWindow.showAtLocation(ll_sales_order, Gravity.CENTER, 0, 0);
                    TextView salename, sale_date, sale_amount, paid_amount, balance;

                    salename = (TextView) view1.findViewById(R.id.tv_saleoutstand_saleorder);
                    sale_date = (TextView) view1.findViewById(R.id.tv_saleoutstand_saleorder);
                    sale_amount = (TextView) view1.findViewById(R.id.tv_saleoutstand_saleorder);
                    paid_amount = (TextView) view1.findViewById(R.id.tv_saleoutstand_saleorder);
                    balance = (TextView) view1.findViewById(R.id.tv_saleoutstand_saleorder);

                    //salename.setText(merchantOutstandingSalesOrderList.getSale_type());
                }

                if (((Nameable) drawerItem).getName().getText(CreateSalesOrderContinue.this) == "Logout") {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });
    }


    public void displayMessage(String s) {
        mBuilder = new AlertDialog.Builder(CreateSalesOrderContinue.this);
        View mView = getLayoutInflater().inflate(R.layout.layout_for_custom_message, null);

        TextView tvOk, tvMessage;
        tvOk = (TextView) mView.findViewById(R.id.tvOk);
        tvMessage = (TextView) mView.findViewById(R.id.tvMessage);
        tvMessage.setText(s);

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        mBuilder.setView(mView);
        dialog = mBuilder.create();
        dialog.show();
    }

}