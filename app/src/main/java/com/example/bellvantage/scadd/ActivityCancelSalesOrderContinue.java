package com.example.bellvantage.scadd;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.bellvantage.scadd.Adapters.CancelSalesOrderProductListAdapter;
import com.example.bellvantage.scadd.DB.DbHelper;
import com.example.bellvantage.scadd.Utils.DateManager;
import com.example.bellvantage.scadd.Utils.SessionManager;
import com.example.bellvantage.scadd.Utils.SyncManager;
import com.example.bellvantage.scadd.domains.ReviseSalesOrderList;
import com.example.bellvantage.scadd.swf.CategoryDetails;
import com.example.bellvantage.scadd.swf.LoginUser;
import com.example.bellvantage.scadd.swf.OrderList;
import com.example.bellvantage.scadd.swf.SalesOrderSync;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

import static com.example.bellvantage.scadd.DB.DbHelper.COL_SRI_BATCH_ID;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SRI_PRODUCT_ID;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SRI_QTY_INHAND;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_VEHICLE_INVENTORY_BatchID;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_VEHICLE_INVENTORY_LoadQuantity;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_VEHICLE_INVENTORY_ProductId;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_SALESREP_INVENTORY;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_VEHICALE_INVENTORY;

public class ActivityCancelSalesOrderContinue extends AppCompatActivity {

    Toolbar toolbar;
    TextView tvTotalDiscountAmount,tvTotalVatAmount,tvTotal,tvReviseSalesOrder,tvDeliveryDate,tvBrand,tvConfirm,tvTotalAmount;
    LinearLayout llVat;
    EditText etSearchProduct;
    Button btnTest;
    ToggleButton tbProducts;
    RecyclerView rvProductList;
    OrderList orderList;
    int salesOrderId,responseSalesOrderId=0;
    double totalDiscountAmount, netTotalAmount,totalVat,totalFreeAmount;
    ArrayList<ReviseSalesOrderList> reviseSalesOrderListArrayList = new ArrayList<>();
    ArrayList<ReviseSalesOrderList> reviseSalesOrderListArrayList1 = new ArrayList<>();
    SyncManager syncManager;
    ReviseSalesOrderList reviseSalesOrderList;
    CancelSalesOrderProductListAdapter cancelSalesOrderProductListAdapter;



    String updatedUser;
    DbHelper db;
    String estimatedDiliveryDate;
    DateManager dateManager;

    //date
    DatePicker datePicker;
    Calendar calendar;
    int year, month, day;

    // AlertDialog
    AlertDialog dialog;
    AlertDialog.Builder mBuilder;

    //Category DialogsBox Spinner
    ArrayList<CategoryDetails> categoryDetailsArrayList = new ArrayList<>();

    ArrayList<String> categoryArraay = new ArrayList<>();
    SpinnerDialog spdCategory;
    int categoryId,id ;

    //SharedPreferences
    SharedPreferences spref ;
    SharedPreferences.Editor editor;
    String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_sales_order_continue);
        initializeViews();

        //Get details from SharedPreferences
        spref = getSharedPreferences("invoice.conf", Context.MODE_PRIVATE);
        editor =spref.edit();
        userType = spref.getString("SalesRepType","");

        System.out.println("User Type: " + userType);

        if (getIntent().getSerializableExtra("id")!=null){
            id = (int) getIntent().getSerializableExtra("id");
        }

        if(id==1){
            tvConfirm.setVisibility(View.GONE);
            toolbar.setTitle("Cancel Sales Order");
        }
        if(id==2){
            tvReviseSalesOrder.setVisibility(View.GONE);
            toolbar.setTitle("Confirm Sales Order");
            if (getIntent().getSerializableExtra("responseSalesOrderId")!=null){
                responseSalesOrderId = (int) getIntent().getSerializableExtra("responseSalesOrderId");
            }
        }

        db = new DbHelper(getApplicationContext());
        syncManager = new SyncManager(getApplicationContext());
        dateManager = new DateManager();

        Gson gson = new Gson();
        String getJson = SessionManager.pref.getString("user", "");
        LoginUser prefUser = gson.fromJson(getJson, LoginUser.class);
        updatedUser =prefUser.getUserName();

        if (getIntent().getSerializableExtra("orderList")!=null){
            orderList = (OrderList) getIntent().getSerializableExtra("orderList");
        }

        System.out.println("ISVAT: "+orderList.getIsVat());
        if (orderList.getIsVat()==1){
            llVat.setVisibility(View.VISIBLE);
        }else{
            llVat.setVisibility(View.GONE);
        }

        salesOrderId = orderList.getSalesOrderId();
        System.out.println("======== Sales Order ID (ACTIVITY) " + salesOrderId);
        estimatedDiliveryDate = orderList.getEstimateDeliveryDate();
        int start = estimatedDiliveryDate.indexOf("(");
        int end = estimatedDiliveryDate.indexOf(")");
        String iDate = estimatedDiliveryDate.substring(start+1,end);

        tvDeliveryDate.setText(dateManager.getDateAccordingToMilliSeconds(Long.parseLong(iDate), "yyyy-MM-dd"));
        year= Integer.parseInt(dateManager.getDateAccordingToMilliSeconds(Long.parseLong(iDate), "yyyy"));
        month= Integer.parseInt(dateManager.getDateAccordingToMilliSeconds(Long.parseLong(iDate), "MM"));
        day= Integer.parseInt(dateManager.getDateAccordingToMilliSeconds(Long.parseLong(iDate), "dd"));
        totalVat = orderList.getTotalVAT();
        totalDiscountAmount = orderList.getTotalDiscount();
        //netTotalAmount = orderList.getTotalAmount();
        totalFreeAmount = orderList.getTotalDiscount();

        reviseSalesOrderListArrayList1 = syncManager.getReviseOrderLineItemsBySalesOrderId(salesOrderId);
        reviseSalesOrderListArrayList = syncManager.getReviseOrderLineItems(orderList.getIsVat());
        loadRecyclerView(reviseSalesOrderListArrayList,reviseSalesOrderListArrayList1,0);

        if (reviseSalesOrderListArrayList1.size()>0){
            double amount = 0, freeAmount=0;
            for (int i=0;i<reviseSalesOrderListArrayList1.size();i++){
                double subTotal = reviseSalesOrderListArrayList1.get(i).getUnitSellingPrice()*reviseSalesOrderListArrayList1.get(i).getQuantity();
                amount += subTotal;
                netTotalAmount = amount;

                double free = reviseSalesOrderListArrayList1.get(i).getFreeQuantity()*reviseSalesOrderListArrayList1.get(i).getUnitSellingPrice();

                freeAmount+=free;
                totalFreeAmount = freeAmount;
            }
           System.out.println("===========================================================================================================================");
           // tvTotal.setText(String.format("Rs. %.2f", netTotalAmount));
           // tvTotalDiscountAmount.setText(String.format("Rs. %.2f", totalFreeAmount));
            System.out.println("Total Net Amount " + netTotalAmount);

        }

        tvBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spdCategory.showSpinerDialog();
            }
        });
        categoryDetailsArrayList=syncManager.getCategoryDetailsFromSQLite("SELECT * FROM "+ DbHelper.TABLE_CATEGORY);
        loadCategoryDetails(categoryDetailsArrayList);

        /*btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SOR__ListItemActivity_AllSelected.class);
                intent.putExtra("reviseSalesOrderListArrayList1",reviseSalesOrderListArrayList1);
                intent.putExtra("orderList",orderList);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });*/


        tbProducts.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    for (int i=0;i<reviseSalesOrderListArrayList1.size();i++){
                        int productId= reviseSalesOrderListArrayList1.get(i).getProductId();
                        int batchId= reviseSalesOrderListArrayList1.get(i).getBatchID();
                        ReviseSalesOrderList reviseSalesOrderList = reviseSalesOrderListArrayList1.get(i);
                        for (int j = 0;j<reviseSalesOrderListArrayList.size();j++){
                            if (productId==reviseSalesOrderListArrayList.get(j).getProductId() &&
                                    batchId==reviseSalesOrderListArrayList.get(j).getBatchID()){
                                reviseSalesOrderListArrayList.set(j,reviseSalesOrderList);
                                break;
                            }
                        }
                    }
                    loadRecyclerView(reviseSalesOrderListArrayList,reviseSalesOrderListArrayList1,1);
                }else{
                    loadRecyclerView(reviseSalesOrderListArrayList,reviseSalesOrderListArrayList1,0);
                }
            }
        });


        //Search Products
        etSearchProduct.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String newText = charSequence.toString().toLowerCase();
                ArrayList<ReviseSalesOrderList> newArrayList = new ArrayList<>();
                for(ReviseSalesOrderList reviseSalesOrderList:reviseSalesOrderListArrayList1){
                    String produvtName = reviseSalesOrderList.getProductName().toLowerCase();
                    if(produvtName.contains(newText)){
                        newArrayList.add(reviseSalesOrderList);
                    }
                }
                cancelSalesOrderProductListAdapter.setFilter(newArrayList);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        tvReviseSalesOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                for (int i=0;i<reviseSalesOrderListArrayList1.size();i++){
                    int productId = reviseSalesOrderListArrayList1.get(i).getProductId();
                    int batchId = reviseSalesOrderListArrayList1.get(i).getBatchID();
                    int quantity = reviseSalesOrderListArrayList1.get(i).getQuantity();
                    int freeQuantity = reviseSalesOrderListArrayList1.get(i).getFreeQuantity();
                    updateStock(productId,batchId,0,quantity ,freeQuantity,0);
                }

               /*if (NetworkConnection.checkNetworkConnection(getApplicationContext())){
                   String url = HTTPPaths.seriveUrl+"UpdateStatus?salesOrderID="+salesOrderId+"&status=3";
                   StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                           new Response.Listener<String>() {
                               @Override
                               public void onResponse(String response) {
                                   JsonObject object = Json.parse(response).asObject();
                                   int id = object.get("ID").asInt();
                                   System.out.println("cancel ID "+ id);
                                   if(id==200){
                                       Intent intent = new Intent(getApplicationContext(),ActivityCancelSalesOrders.class);
                                       intent.putExtra("id",1);
                                       intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                       ActivityCancelSalesOrderContinue.this.finish();
                                       startActivity(intent);
                                       Toast.makeText(ActivityCancelSalesOrderContinue.this, "Cancellation is successful ", Toast.LENGTH_LONG).show();
                                       System.out.println("Cancellation is successful");
                                       ContentValues cv = new ContentValues();
                                       cv.put("SaleStatus",3);
                                       cv.put("IsSync", 0);
                                       db.updateTable(salesOrderId+"",cv,DbHelper.COL_SALES_ORDER_SalesOrderId+" = ?",DbHelper.TABLE_SALES_ORDER);

                                   }else{
                                       //Toast.makeText(ActivityCancelSalesOrderContinue.this, "Merchant Update fail.. try again", Toast.LENGTH_LONG).show();
                                   }
                               }
                           },
                           new Response.ErrorListener() {
                               @Override
                               public void onErrorResponse(VolleyError error) {

                                   Toast.makeText(ActivityCancelSalesOrderContinue.this, "Error", Toast.LENGTH_SHORT).show();
                               }
                           }
                   );
                   MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
               }else{*/

                   // displayMessage(salesOrderId + " Sales order is canceled",1);
                    displayStatusMessage("Sales order is canceled \n Sales Order ID: " + salesOrderId,1,id);
                    System.out.println("Cancellation is successful");
                    ContentValues cv = new ContentValues();
                    cv.put("SaleStatus",3);
                    cv.put("IsSync", 0);
                    db.updateTable(salesOrderId+"",cv,DbHelper.COL_SALES_ORDER_SalesOrderId+" = ?",DbHelper.TABLE_SALES_ORDER);

                    syncToSqlite(salesOrderId,3);


               //}
            }
        });

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if (NetworkConnection.checkNetworkConnection(getApplicationContext())){
                   String urlC;

                    if (responseSalesOrderId!=0){
                        urlC = HTTPPaths.seriveUrl+"UpdateStatus?salesOrderID="+responseSalesOrderId+"&status=2";
                    }else{
                        urlC = HTTPPaths.seriveUrl+"UpdateStatus?salesOrderID="+salesOrderId+"&status=2";
                    }


                    StringRequest stringRequest = new StringRequest(Request.Method.GET, urlC,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    JsonObject object = Json.parse(response).asObject();
                                    int id = object.get("ID").asInt();
                                    System.out.println("cancel ID"+ id);

                                    if(id==200){
                                        Intent intent = new Intent(getApplicationContext(),ActivityCancelSalesOrders.class);
                                        intent.putExtra("id",2);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        ActivityCancelSalesOrderContinue.this.finish();
                                        startActivity(intent);
                                        Toast.makeText(ActivityCancelSalesOrderContinue.this, "Confirmation is successful ", Toast.LENGTH_LONG).show();
                                        System.out.println("Confirmation is successful");
                                        ContentValues cv = new ContentValues();
                                        cv.put("SaleStatus",2);
                                        cv.put("IsSync", 1);
                                        db.updateTable(salesOrderId+"",cv,DbHelper.COL_SALES_ORDER_SalesOrderId+" = ?",DbHelper.TABLE_SALES_ORDER);

                                    }else{
                                        Toast.makeText(ActivityCancelSalesOrderContinue.this, "Confirmation is successful ", Toast.LENGTH_LONG).show();
                                        System.out.println("Confirm is successful");
                                        ContentValues cv = new ContentValues();
                                        cv.put("SaleStatus",2);
                                        cv.put("IsSync", 0);
                                        db.updateTable(salesOrderId+"",cv,DbHelper.COL_SALES_ORDER_SalesOrderId+" = ?",DbHelper.TABLE_SALES_ORDER);
                                        ActivityCancelSalesOrderContinue.this.finish();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    Intent intent = new Intent(getApplicationContext(),ActivityCancelSalesOrders.class);
                                    intent.putExtra("id",2);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    ActivityCancelSalesOrderContinue.this.finish();
                                    startActivity(intent);

                                    Toast.makeText(ActivityCancelSalesOrderContinue.this, "Confirmation is successful ", Toast.LENGTH_LONG).show();
                                    System.out.println("Confirm is successful");
                                    ContentValues cv = new ContentValues();
                                    cv.put("SaleStatus",2);
                                    cv.put("IsSync", 0);
                                    db.updateTable(salesOrderId+"",cv,DbHelper.COL_SALES_ORDER_SalesOrderId+" = ?",DbHelper.TABLE_SALES_ORDER);


                                }
                            }
                    );
                    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
                }else{*/

                   // displayMessage(salesOrderId + " Sales order is confirmed",2);
                    displayStatusMessage("Sales order is confirmed \n Sales Order ID: " + salesOrderId,1, id);
                    System.out.println("Confirmation is successful");
                    ContentValues cv = new ContentValues();
                    cv.put("SaleStatus",2);
                    cv.put("IsSync", 0);
                    db.updateTable(salesOrderId+"",cv,DbHelper.COL_SALES_ORDER_SalesOrderId+" = ?",DbHelper.TABLE_SALES_ORDER);
                    syncToSqlite(salesOrderId,2);
                //}
            }
        });


        //set Total amounts to text views
        tvTotalAmount.setText(String.format("Rs. %.2f", netTotalAmount+totalFreeAmount));
        //tvTotalDiscountAmount.setText(String.format("Rs. %.2f",totalFreeAmount));
        tvTotalVatAmount.setText(String.format("Rs. %.2f", totalVat));
        tvTotal.setText(String.format("Rs. %.2f", netTotalAmount));
        tvTotalDiscountAmount.setText(String.format("Rs. %.2f", totalFreeAmount));
        tvDeliveryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // showDialog(67);
            }
        });



    }

    private void syncToSqlite(int salesOrderId,int status) {

        SQLiteDatabase database  = db.getWritableDatabase();
        Cursor cursor = null;
        String jsonQuery = "SELECT * FROM " + DbHelper.TABLE_SALESORDERS_SYNC + " WHERE " + DbHelper.COL_SALESORDER_SYNC_SalesOrderID + " = " + salesOrderId;
        cursor = database.rawQuery(jsonQuery, null);
        SalesOrderSync salesOrderSync = new SalesOrderSync(salesOrderId,status,0);
        ContentValues cv = salesOrderSync.getSalesOrderSyncContentValues();
        if (cursor.getCount() > 0) {
            boolean s= db.updateTable(salesOrderId+"",cv,DbHelper.COL_SALESORDER_SYNC_SalesOrderID+" = ?",DbHelper.TABLE_SALESORDERS_SYNC);
            if (s){
                System.out.println("Data is updated relate to the "+ salesOrderId);
            }
        }else {
            boolean success = db.insertDataAll(cv,DbHelper.TABLE_SALESORDERS_SYNC);
            if (success){
                System.out.println("============ Data is inserted to " + DbHelper.TABLE_SALESORDERS_SYNC);
            }else{
                System.out.println("============ Data is not inserted to " + DbHelper.TABLE_SALESORDERS_SYNC);
            }
        }

    }

    @Override
    protected Dialog onCreateDialog(int id) {

        if(id == 67) {
            return new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            showDate(year, month+1, dayOfMonth);
                        }
                    }, year, month, day);
        }
        return null;
    }

    private void showDate(int Year, int month, int day) {
        String mnt= null;
        String dy = null;
        if(month < 10){
            mnt = "0"+month;
        }else if(month >= 10){
            mnt = ""+month;
        }
        if(day < 10){
            dy = "0"+day;
        }else if(day >= 10){
            dy = ""+day;
        }

        estimatedDiliveryDate = Year +"-"+mnt+"-"+dy;
        tvDeliveryDate.setText(estimatedDiliveryDate);

    }


    private void loadRecyclerView(ArrayList<ReviseSalesOrderList> reviseSalesOrderListArrayList,ArrayList<ReviseSalesOrderList> reviseSalesOrderListArrayList1,int method) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL,
                false
        );

        rvProductList.setLayoutManager(layoutManager);
        rvProductList.setHasFixedSize(true);

        System.out.println("=============== reviseSalesOrderListArrayList size: " + reviseSalesOrderListArrayList.size() );
        cancelSalesOrderProductListAdapter = new CancelSalesOrderProductListAdapter(
                ActivityCancelSalesOrderContinue.this,
                reviseSalesOrderListArrayList,
                reviseSalesOrderListArrayList1,
                method
        );
        rvProductList.setAdapter(cancelSalesOrderProductListAdapter);
    }

    private void initializeViews() {
        //ToolBar
        toolbar = (Toolbar) findViewById(R.id.tb_main);

        //RecyclerView
        rvProductList = (RecyclerView) findViewById(R.id.rvProductList);

        //TextViews
        tvTotalDiscountAmount = (TextView) findViewById(R.id.tvTotalDiscountAmount);
        tvTotalVatAmount = (TextView) findViewById(R.id.tvTotalVatAmount);
        tvTotal = (TextView) findViewById(R.id.tvTotal);
        tvDeliveryDate = (TextView) findViewById(R.id.tvDeliveryDate);
        tvBrand = (TextView) findViewById(R.id.tvBrand);
        tvBrand = (TextView) findViewById(R.id.tvBrand);
        tvReviseSalesOrder = (TextView)findViewById(R.id.tvReviseSalesOrder);
        tvConfirm = (TextView)findViewById(R.id.tvConfirm);
        tvTotalAmount = (TextView)findViewById(R.id.tvTotalAmount);


        //Buttons
        tbProducts   = (ToggleButton) findViewById(R.id.tbProducts);
        //btnTest   = (Button) findViewById(R.id.btnTest);


        //EditText
        etSearchProduct = (EditText) findViewById(R.id.etSearchProduct);

        //Layouts
        llVat = (LinearLayout) findViewById(R.id.llVat);

        /*calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);*/

    }

    public void prepare(View view, int position,int method){
        double amount = 0,freeAmount=0;
        if(((CheckBox)view).isChecked()){
            reviseSalesOrderList = reviseSalesOrderListArrayList.get(position);
            reviseSalesOrderList.setISCheckedItem(true);
            reviseSalesOrderList.setQuantity(1);
            reviseSalesOrderListArrayList1.add(reviseSalesOrderList);
            cancelSalesOrderProductListAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Product Name: "+reviseSalesOrderList.getProductName(), Toast.LENGTH_SHORT).show();

            for (int i=0;i<reviseSalesOrderListArrayList1.size();i++){
                double subTotal = reviseSalesOrderListArrayList1.get(i).getAmount();
                double free = reviseSalesOrderListArrayList1.get(i).getFreeQuantity()*reviseSalesOrderListArrayList1.get(i).getUnitSellingPrice();
                amount += subTotal;
                freeAmount+=free;
                netTotalAmount = amount;
                totalFreeAmount = freeAmount;
            }
            tvTotal.setText(String.format("Rs. %.2f", netTotalAmount));
            tvTotalDiscountAmount.setText(String.format("Rs. %.2f", totalFreeAmount));

        }else {
            reviseSalesOrderList = reviseSalesOrderListArrayList.get(position);
            reviseSalesOrderList.setISCheckedItem(false);
            reviseSalesOrderListArrayList1.remove(reviseSalesOrderList);
            netTotalAmount = netTotalAmount -reviseSalesOrderList.getAmount();
            totalFreeAmount = totalFreeAmount-(reviseSalesOrderList.getFreeQuantity()*reviseSalesOrderList.getUnitSellingPrice());
            cancelSalesOrderProductListAdapter.notifyDataSetChanged();
            tvTotal.setText(String.format("Rs. %.2f", netTotalAmount));
            tvTotalDiscountAmount.setText(String.format("Rs. %.2f", netTotalAmount));


        }

    }

    public void getQty(final int position, final int method){

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(ActivityCancelSalesOrderContinue.this);
        View mView = getLayoutInflater().inflate(R.layout.layout_for_popup,null);

        final EditText etQuantity,etFreeQuantity;
        TextView tvButton;
        etQuantity = (EditText) mView.findViewById(R.id.etQuantity);
        etFreeQuantity = (EditText) mView.findViewById(R.id.etFreeQuantity);
        tvButton = (TextView)mView.findViewById(R.id.tvButton);

        tvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double amount = 0,freeAmount=0;
                String qty = etQuantity.getText().toString();
                String freeQty = etFreeQuantity.getText().toString();

                if (qty.isEmpty()){
                    qty=0+"";
                }

                if (freeQty.isEmpty()){
                    freeQty = 0+"";
                }
                int quantity = Integer.parseInt(qty);
                int freeQuantity = Integer.parseInt(freeQty);
//===============================================================================================================
                if (method==1){
                    reviseSalesOrderList = reviseSalesOrderListArrayList.get(position);
                    reviseSalesOrderList.setQuantity(quantity);
                    reviseSalesOrderList.setFreeQuantity(freeQuantity);
                    reviseSalesOrderList.setISCheckedItem(true);
                    reviseSalesOrderList.setAmount(reviseSalesOrderList.getUnitSellingPrice()*quantity);

                    reviseSalesOrderListArrayList.set(position,reviseSalesOrderList);
                    reviseSalesOrderListArrayList1.add(reviseSalesOrderList);

                }else{
                    reviseSalesOrderList = reviseSalesOrderListArrayList1.get(position);
                    reviseSalesOrderList.setQuantity(quantity);
                    reviseSalesOrderList.setFreeQuantity(freeQuantity);
                    reviseSalesOrderList.setISCheckedItem(true);
                    reviseSalesOrderList.setAmount(reviseSalesOrderList.getUnitSellingPrice()*quantity);

                    reviseSalesOrderListArrayList1.set(position,reviseSalesOrderList);
                }
                for (int i=0;i<reviseSalesOrderListArrayList1.size();i++){
                    double subTotal = reviseSalesOrderListArrayList1.get(i).getAmount();
                    amount += subTotal;
                    netTotalAmount = amount;

                    double free = reviseSalesOrderListArrayList1.get(i).getFreeQuantity()*reviseSalesOrderListArrayList1.get(i).getUnitSellingPrice();

                    freeAmount+=free;
                    totalFreeAmount = freeAmount;
                }
                tvTotal.setText(String.format("Rs. %.2f", netTotalAmount));
                tvTotalDiscountAmount.setText(String.format("Rs. %.2f", totalFreeAmount));
                cancelSalesOrderProductListAdapter.notifyDataSetChanged();
                dialog.dismiss();

            }
        });

        mBuilder.setView(mView);
        dialog = mBuilder.create();
        dialog.show();
    }
    private void loadCategoryDetails(final ArrayList<CategoryDetails> categoryDetailsArrayList) {
        categoryArraay.clear();
        for (int i = 0; i < categoryDetailsArrayList.size(); i++) {
            categoryArraay.add(categoryDetailsArrayList.get(i).getCategoryName());
        }
        spdCategory=new SpinnerDialog(ActivityCancelSalesOrderContinue.this,categoryArraay,"Select or Search Category",R.style.DialogAnimations_SmileWindow);

        spdCategory.bindOnSpinerListener(new OnSpinerItemClick()
        {
            @Override
            public void onClick(String item, int position)
            {

                tvBrand.setText(item);

                for (int  i = 0 ;i<categoryDetailsArrayList.size();i++){

                    if (categoryDetailsArrayList.get(i).getCategoryName().equals(item)){

                        categoryId = categoryDetailsArrayList.get(i).getCategoryid();

                        ArrayList<ReviseSalesOrderList> newArrayList = new ArrayList<>();
                        for(int j = 0; j<reviseSalesOrderListArrayList1.size();j++){
                            int cid = reviseSalesOrderListArrayList1.get(j).getCategoryId();
                            if(cid==categoryId){
                                newArrayList.add(reviseSalesOrderListArrayList1.get(j));
                            }
                        }
                        cancelSalesOrderProductListAdapter.setFilter(newArrayList);
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),SalesOrderOfflineActivity.class);
        ActivityCancelSalesOrderContinue.this.finish();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void displayStatusMessage(String s, int colorValue, final int id) {

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

        builder = new AlertDialog.Builder(ActivityCancelSalesOrderContinue.this);
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
        alertDialog.setCanceledOnTouchOutside(false);

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

                if (id==2){
                    /*Intent intent = new Intent(getApplicationContext(),SalesOrderToInvoice.class);
                    ActivityCancelSalesOrderContinue.this.finish();
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();*/
                    Intent intent = new Intent(getApplicationContext(),CreateInvoiceActivity.class);
                    intent.putExtra("orderList",orderList);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getApplicationContext(),SalesOrderOfflineActivity.class);
                    ActivityCancelSalesOrderContinue.this.finish();
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }

            }
        });

    }

    //Reduce Stock
    private void updateStock(int productID, int batchID, int quantity, int oldQuantity,int freeQuantity,int oldFreeQuantity) {

        SQLiteDatabase database = db.getWritableDatabase();

        int  dbQty = getProductQuantitFromDatabase(productID,batchID);
        int newQty = 0;
        int newFreeQty = 0;
        System.out.println("Quantity "+ quantity + " Old Quantity "+oldQuantity);

        if (quantity>oldQuantity){
            newQty = quantity-oldQuantity;
            dbQty = dbQty - newQty;
            System.out.println("Database Quantity q>"+dbQty);
        }
        else if(oldQuantity>quantity){
            newQty = oldQuantity - quantity;
            System.out.println("newQty old>"+newQty);
            dbQty = dbQty+newQty;
            System.out.println("Database Quantity old>"+dbQty);
        }

        if (freeQuantity>oldFreeQuantity){
            newFreeQty = freeQuantity-oldFreeQuantity;
            System.out.println("New Quantity 1st: "+ newFreeQty);
            dbQty = dbQty-newFreeQty;
            System.out.println("Database Free Quantity(FreeQuantity) fq>"+dbQty);
        }else if(oldFreeQuantity>freeQuantity){
            newFreeQty = oldFreeQuantity-freeQuantity;
            System.out.println("New Quantity 2nd: "+ newFreeQty);
            dbQty=dbQty+newFreeQty;
            System.out.println("Database Quantity ofq>"+dbQty);
        }

        ContentValues cv = new ContentValues();

        int afectedRows;
        System.out.println("User Type: " + userType);

        if (userType.equalsIgnoreCase("R")){

            cv.put(COL_VEHICLE_INVENTORY_LoadQuantity,dbQty);
            System.out.println("Database Quantity update"+dbQty);
            afectedRows = database.update(TABLE_VEHICALE_INVENTORY,cv,COL_VEHICLE_INVENTORY_ProductId+" = ?  AND " + COL_VEHICLE_INVENTORY_BatchID+ " = ? " ,new String[]{""+productID,""+batchID});

        }else{

            cv.put(COL_SRI_QTY_INHAND,dbQty);
            afectedRows = database.update(TABLE_SALESREP_INVENTORY,cv,COL_SRI_PRODUCT_ID +" = ?  AND " + COL_SRI_BATCH_ID+ " = ? " ,new String[]{""+productID,""+batchID});

        }

        if(afectedRows>0){
            System.out.println(afectedRows+ " Stock is updated ProductID: "+ productID+" BatchID: "+batchID );
        }
    }

    private int getProductQuantitFromDatabase(int productID, int batchID) {
        String query = null;
        int  dbQty = 0;
        if (userType.equalsIgnoreCase("R")){
            query =  "SELECT  "+ COL_VEHICLE_INVENTORY_LoadQuantity +" FROM " + TABLE_VEHICALE_INVENTORY+ " WHERE " +
                    COL_VEHICLE_INVENTORY_ProductId+ " = " + productID +
                    " AND " + COL_VEHICLE_INVENTORY_BatchID + "= "+batchID;
        }else{
            query =  "SELECT  "+ COL_SRI_QTY_INHAND +" FROM " + TABLE_SALESREP_INVENTORY+ " WHERE " +
                    COL_SRI_PRODUCT_ID+ " = " + productID +
                    " AND " + COL_SRI_BATCH_ID + "= "+batchID;
        }

        Cursor cursor = null;
        SQLiteDatabase database = db.getReadableDatabase();

        cursor = database.rawQuery(query, null);
        if (cursor.getCount() == 0) {
            System.out.println(" ============= No data related to this product ");

        } else {
            while (cursor.moveToNext()) {
                dbQty = cursor.getInt(0);
                System.out.println("Database Quantity: " + dbQty);
            }
        }

        return dbQty;
    }
}
