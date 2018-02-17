package com.example.bellvantage.scadd;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.bellvantage.scadd.Adapters.ReviseSalesOrderProductListAdapter;
import com.example.bellvantage.scadd.DB.DbHelper;
import com.example.bellvantage.scadd.DB.MySingleton;
import com.example.bellvantage.scadd.Utils.DateManager;
import com.example.bellvantage.scadd.Utils.NetworkConnection;
import com.example.bellvantage.scadd.Utils.SessionManager;
import com.example.bellvantage.scadd.Utils.SyncManager;
import com.example.bellvantage.scadd.domains.ReviseSalesOrderList;
import com.example.bellvantage.scadd.swf.CategoryDetails;
import com.example.bellvantage.scadd.swf.LoginUser;
import com.example.bellvantage.scadd.swf.OrderList;
import com.example.bellvantage.scadd.swf.ProductListLast;
import com.example.bellvantage.scadd.swf.ReviseOrderJson;
import com.example.bellvantage.scadd.swf.SalesOrder;
import com.example.bellvantage.scadd.swf.SalesOrderLineItem;
import com.example.bellvantage.scadd.web.HTTPPaths;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

public class SOR_ListItemActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView tvTotalDiscountAmount,tvTotalVatAmount,tvTotal,tvReviseSalesOrder,tvDeliveryDate,tvBrand;
    EditText etSearchProduct;
    Button btnTest;
    ToggleButton tbProducts;
    RecyclerView rvProductList;
    OrderList orderList;
    int salesOrderId;
    double totalDiscountAmount, netTotalAmount,totalVat;
    ArrayList<ReviseSalesOrderList> reviseSalesOrderListArrayList = new ArrayList<>();
    ArrayList<ReviseSalesOrderList> reviseSalesOrderListArrayList1 = new ArrayList<>();
    SyncManager syncManager;
    ReviseSalesOrderList reviseSalesOrderList;
    ReviseSalesOrderProductListAdapter reviseSalesOrderProductListAdapter;

    AlertDialog dialog;
    String updatedUser;
    DbHelper db;
    String estimatedDiliveryDate;
    DateManager dateManager;

    //date
    DatePicker datePicker;
    Calendar calendar;
    int year, month, day;

    //Category DialogsBox Spinner
    ArrayList<CategoryDetails> categoryDetailsArrayList = new ArrayList<>();

    ArrayList<String> categoryArraay = new ArrayList<>();
    SpinnerDialog spdCategory;
    int categoryId ,id=0;
    String salesrepType;
    String tod;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sor__list_item);

        db = new DbHelper(getApplicationContext());
        syncManager = new SyncManager(getApplicationContext());
        dateManager = new DateManager();
        initializeViews();


        long todayMilSec = dateManager.todayMillsec();
        tod = "/Date(" + todayMilSec + ")/";

        Gson gson = new Gson();
        String getJson = SessionManager.pref.getString("user", "");
        LoginUser prefUser = gson.fromJson(getJson, LoginUser.class);
        updatedUser =prefUser.getUserName();


        toolbar.setTitle("Revise Sales Order ");

        if (getIntent().getSerializableExtra("orderList")!=null){
            orderList = (OrderList) getIntent().getSerializableExtra("orderList");
        }

        salesrepType = getIntent().getExtras().getString("salesrep_type");


        if (getIntent().getSerializableExtra("id")!=null){
            id = (int) getIntent().getSerializableExtra("id");
        }


        salesOrderId = orderList.getSalesOrderId();
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
        netTotalAmount = orderList.getTotalAmount();

        if(id == 1){
            if (getIntent().getSerializableExtra("selectedArrayList")!=null){
                reviseSalesOrderListArrayList1 = (ArrayList<ReviseSalesOrderList>) getIntent().getSerializableExtra("selectedArrayList");
            }

        }else {
            reviseSalesOrderListArrayList1 = syncManager.getReviseOrderLineItemsBySalesOrderId(salesOrderId);
        }


        reviseSalesOrderListArrayList = syncManager.getReviseOrderLineItems(orderList.getIsVat());
        loadRecyclerView(reviseSalesOrderListArrayList,reviseSalesOrderListArrayList1,0);


        tvBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spdCategory.showSpinerDialog();
            }
        });
        categoryDetailsArrayList=syncManager.getCategoryDetailsFromSQLite("SELECT * FROM "+ DbHelper.TABLE_CATEGORY);
        loadCategoryDetails(categoryDetailsArrayList);

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SOR__ListItemActivity_AllSelected.class);
                intent.putExtra("reviseSalesOrderListArrayList1",reviseSalesOrderListArrayList1);
                intent.putExtra("orderList",orderList);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


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
                reviseSalesOrderProductListAdapter.setFilter(newArrayList);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        tvReviseSalesOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DateManager dateManager = new DateManager();
                long todayMilSec = dateManager.todayMillsec();
                String updatedDate = "/Date(" + todayMilSec + ")/";


                SQLiteDatabase database = db.getWritableDatabase();
                database.execSQL("DELETE FROM "+ DbHelper.TABLE_LINE_ITEM+ " WHERE " + DbHelper.COL_LINE_ITEM_SalesOrderID + " = "+salesOrderId+"");
                ArrayList<ProductListLast> productListLastArrayList = new ArrayList<ProductListLast>();

                for (int i=0;i<reviseSalesOrderListArrayList1.size();i++){
                    int salesOrderID = salesOrderId;
                    int batchID = reviseSalesOrderListArrayList1.get(i).getBatchID();
                    int productID =reviseSalesOrderListArrayList1.get(i).getProductId();
                    int quantity =reviseSalesOrderListArrayList1.get(i).getQuantity();
                    int freeQuantity = reviseSalesOrderListArrayList1.get(i).getFreeQuantity();
                    double unitSellingPrice = reviseSalesOrderListArrayList1.get(i).getUnitSellingPrice();
                    double unitSellingDiscount = 0;
                    double totalAmount = reviseSalesOrderListArrayList1.get(i).getAmount();
                    double totalDiscount = 0;
                    int isSync = 0;
                    String name = reviseSalesOrderListArrayList1.get(i).getProductName();
                    String expiredate =reviseSalesOrderListArrayList1.get(i).getExpiryDate();
                    int stock = reviseSalesOrderListArrayList1.get(i).getStock();
                    int img = 0;
                    int isVat = reviseSalesOrderListArrayList1.get(i).getIsVat();
                    double vatRate = reviseSalesOrderListArrayList1.get(i).getVatRate();

                    ProductListLast productListLast = new ProductListLast(salesOrderID,batchID,productID,
                                                                quantity,freeQuantity,unitSellingPrice,
                                                                unitSellingDiscount,totalAmount,totalDiscount,
                                                                isSync,name,expiredate,stock,img,isVat,vatRate,0.00,tod
                            );

                    productListLastArrayList.add(productListLast);


                    //Insert Line Items
                    SalesOrderLineItem salesOrderLineItem = new SalesOrderLineItem(salesOrderId,batchID,productID,
                            quantity,freeQuantity,unitSellingPrice,unitSellingDiscount,totalAmount,
                            totalDiscount,isSync,updatedDate);

                    ContentValues cvLineItem = salesOrderLineItem.getLineItemContentValues();
                    boolean successL = db.insertDataAll(cvLineItem, DbHelper.TABLE_LINE_ITEM);
                    if (!successL) {
                        System.out.println("Data is not inserted to " + DbHelper.TABLE_LINE_ITEM);
                    } else {
                        System.out.println(DbHelper.TABLE_LINE_ITEM + " Table syncing...");
                    }

                }


                //salesOrder
                int merchantId = orderList.getMerchantId();
                int ditributorId = orderList.getDitributorId();
                int salesRepId = orderList.getSalesRepId();
                int saleTypeId = orderList.getSaleTypeId();
                String enteredDate = orderList.getEnteredDate();
                String enteredUser = orderList.getEnteredUser();
                int saleStatus = orderList.getSaleStatus();
                double totalAmount = netTotalAmount;
                double totalDiscount = orderList.getTotalDiscount();
                double totalVAT = orderList.getTotalVAT();
                String saleDate = orderList.getSaleDate();
                int isVat = orderList.getIsVat();
                int isReturn = orderList.getIsReturn();
                final int invoiceNumber = orderList.getInvoiceNumber();
                String estimateDeliveryDate = orderList.getEstimateDeliveryDate();
                String syncDate = orderList.getSyncDate();
                int isSync = orderList.getIsSync();
                int salesOrderId = orderList.getSalesOrderId();
                ArrayList<ProductListLast> _ListSalesOrderLineItem = productListLastArrayList;
                String merchantName = orderList.getMerchntName();



                OrderList orderList1 = new OrderList(invoiceNumber,estimateDeliveryDate,
                        totalDiscount,totalAmount,syncDate,isSync,saleStatus,
                        enteredUser,enteredDate,updatedUser,updatedDate,saleDate,
                        saleTypeId,salesRepId,ditributorId,merchantId,salesOrderId,
                        totalVAT,isVat,isReturn,merchantName,orderList.getIsCredit(),orderList.getCreditDays());

                //insert Updated data to TABLE_SALES_ORDER

                database.execSQL("DELETE FROM "+ DbHelper.TABLE_SALES_ORDER + " WHERE " + DbHelper.COL_SALES_ORDER_InvoiceNumber + " = "+invoiceNumber+"");
                ContentValues cv = orderList1.getOrderListContentValues();
                db.updateTable(invoiceNumber+"",cv,DbHelper.COL_SALES_ORDER_InvoiceNumber + " = ?",DbHelper.TABLE_SALES_ORDER);
                boolean success = db.insertDataAll(cv, DbHelper.TABLE_SALES_ORDER);
                System.out.println("SalesOrderid sorder: " + salesOrderId);
                System.out.println("InvoiceNumber sorder: " + orderList1.getInvoiceNumber());
                System.out.println("InvoiceNumber sorder: " + invoiceNumber);
                if (!success) {
                    System.out.println("Data is not inserted to " + DbHelper.TABLE_SALES_ORDER);
                } else {
                    System.out.println(DbHelper.TABLE_SALES_ORDER + " Table syncing...");
                }



                SalesOrder salesOrder = new SalesOrder(
                        merchantId,ditributorId,salesRepId,saleTypeId,enteredDate,enteredUser,
                        saleStatus,totalAmount,totalDiscount,totalVAT,saleDate,isVat,isReturn,
                        _ListSalesOrderLineItem,salesrepType);

                Gson gson = new Gson();
                String jsonString = gson.toJson(salesOrder,SalesOrder.class);
                System.out.println("Json String ===========\n "+ jsonString);

                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(jsonString);

                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("Json Error Revise Sales Order: " + e.getMessage());
                }


                Cursor cursor = null;
                String jsonQuery = "SELECT * FROM " + DbHelper.TABLE_REVISE_ORDER_JSON + " WHERE " + DbHelper.COL_REVISE_SALES_ORDER_JSON_InvoiceNumber + " = " + invoiceNumber;
                cursor = database.rawQuery(jsonQuery, null);

                if (cursor.getCount() == 0) {
                    System.out.println("No Data related to " + invoiceNumber);
                    ReviseOrderJson reviseOrderJson = new ReviseOrderJson(invoiceNumber,jsonString,0);
                    ContentValues cvJson = reviseOrderJson.getReviseSalesOrderContentValues();
                    db.updateTable(invoiceNumber+"",cvJson,DbHelper.COL_REVISE_SALES_ORDER_JSON_InvoiceNumber+" = ?",DbHelper.TABLE_REVISE_ORDER_JSON);
                }else {
                    boolean success1 = db.insertDataAll(cv,DbHelper.TABLE_REVISE_ORDER_JSON);
                    if (success1){
                        System.out.println("Data is inserted to " + DbHelper.TABLE_REVISE_ORDER_JSON);
                    }else{
                        System.out.println("Data is not inserted to " + DbHelper.TABLE_REVISE_ORDER_JSON);
                    }
                }

                String url = HTTPPaths.seriveUrl+"UpdateSalesOrderMobile";

                if (NetworkConnection.checkNetworkConnection(getApplicationContext())==true){
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObj,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    System.out.println("=========response - "+response.toString());
                                    Toast.makeText(SOR_ListItemActivity.this, "Revise SalesOrder is Successful", Toast.LENGTH_SHORT).show();
                                    Intent reportsActivity = new Intent(getApplicationContext(), SO_ReviceSalesOrderavAtivity.class);
                                    reportsActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(reportsActivity);
                                    ContentValues cv = new ContentValues();
                                    cv.put("IsSync",1);
                                    db.updateTable(invoiceNumber+"",cv,DbHelper.COL_REVISE_SALES_ORDER_JSON_InvoiceNumber+" = ?",DbHelper.TABLE_REVISE_ORDER_JSON);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    System.out.println("error - "+error.toString());
                                    Toast.makeText(getApplicationContext(), "Error Occurred\n\n- "+error.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
                }

            }
        });


        //set Total amounts to text views
        tvTotalDiscountAmount.setText(String.format("Rs. %.2f",totalDiscountAmount));
        tvTotalVatAmount.setText(totalVat+"");
        tvTotal.setText(String.format("Rs. %.2f", netTotalAmount));

        tvDeliveryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(67);
            }
        });



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
        reviseSalesOrderProductListAdapter = new ReviseSalesOrderProductListAdapter(
                SOR_ListItemActivity.this,
                reviseSalesOrderListArrayList,
                reviseSalesOrderListArrayList1,
                method
        );
        rvProductList.setAdapter(reviseSalesOrderProductListAdapter);
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


        //Buttons
        tbProducts   = (ToggleButton) findViewById(R.id.tbProducts);
        btnTest   = (Button) findViewById(R.id.btnTest);


        //EditText
        etSearchProduct = (EditText) findViewById(R.id.etSearchProduct);


        /*calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);*/

    }

    public void prepare(View view, int position,int method){
        double amount = 0;
        if(((CheckBox)view).isChecked()){
            reviseSalesOrderList = reviseSalesOrderListArrayList.get(position);
            reviseSalesOrderList.setISCheckedItem(true);
            reviseSalesOrderList.setQuantity(1);
            reviseSalesOrderListArrayList1.add(reviseSalesOrderList);
            reviseSalesOrderProductListAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Product Name: "+reviseSalesOrderList.getProductName(), Toast.LENGTH_SHORT).show();

            for (int i=0;i<reviseSalesOrderListArrayList1.size();i++){
                double subTotal = reviseSalesOrderListArrayList1.get(i).getAmount();
                amount += subTotal;
                netTotalAmount = amount;
            }
            tvTotal.setText(String.format("Rs. %.2f", netTotalAmount));

        }else {
            reviseSalesOrderList = reviseSalesOrderListArrayList.get(position);
            reviseSalesOrderList.setISCheckedItem(false);
            reviseSalesOrderListArrayList1.remove(reviseSalesOrderList);
            netTotalAmount = netTotalAmount -reviseSalesOrderList.getAmount();
            reviseSalesOrderProductListAdapter.notifyDataSetChanged();
            tvTotal.setText(String.format("Rs. %.2f", netTotalAmount));

        }

    }

    public void getQty(final int position, final int method){

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(SOR_ListItemActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.layout_for_popup,null);

        final EditText etQuantity,etFreeQuantity;
        TextView tvButton;
        etQuantity = (EditText) mView.findViewById(R.id.etQuantity);
        etFreeQuantity = (EditText) mView.findViewById(R.id.etFreeQuantity);
        tvButton = (TextView)mView.findViewById(R.id.tvButton);

        tvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double amount = 0;
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
                }
                tvTotal.setText(String.format("Rs. %.2f", netTotalAmount));
                reviseSalesOrderProductListAdapter.notifyDataSetChanged();
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
        spdCategory=new SpinnerDialog(SOR_ListItemActivity.this,categoryArraay,"Select or Search Category",R.style.DialogAnimations_SmileWindow);

        spdCategory.bindOnSpinerListener(new OnSpinerItemClick()
        {
            @Override
            public void onClick(String item, int position)
            {

                tvBrand.setText(item);
                etSearchProduct.setText("");

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
                        reviseSalesOrderProductListAdapter.setFilter(newArrayList);
                    }
                }



            }
        });
    }

}
