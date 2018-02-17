package com.example.bellvantage.scadd;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.bellvantage.scadd.Adapters.AdapterForReviseSelected;
import com.example.bellvantage.scadd.DB.DbHelper;
import com.example.bellvantage.scadd.Utils.DateManager;
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

public class SOR__ListItemActivity_Selected extends AppCompatActivity {

    Toolbar toolbar;
    TextView tvTotalDiscountAmount, tvTotalVatAmount, tvTotal, tvReviseSalesOrder, tvDeliveryDate, tvBrand, tvTotalAmount;
    EditText etSearchProduct;
    LinearLayout llVat;
    ToggleButton tbProducts;
    Button btnShowAllProducts;
    RecyclerView rvProductList;
    OrderList orderList;
    int salesOrderId, isVatSalesOrder;
    double totalDiscountAmount, netTotalAmount, totalVat, totalFreeAmount;
    //ArrayList<ReviseSalesOrderList> allProductArrayList = new ArrayList<>();
    ArrayList<ReviseSalesOrderList> selectedArrayList = new ArrayList<>();
    ArrayList<ReviseSalesOrderList> oldArrayList = new ArrayList<>();
    SyncManager syncManager;
    ReviseSalesOrderList reviseSalesOrderList;
    AdapterForReviseSelected adapterForReviseSelected;


    String updatedUser;
    DbHelper db;
    String estimatedDiliveryDate;
    DateManager dateManager;

    //date
    DatePicker datePicker;
    Calendar calendar;
    int year, month, day;
    String tod;
    Long dilDate = Long.valueOf(0);

    // AlertDialog
    AlertDialog dialog;
    AlertDialog.Builder mBuilder;

    //Category DialogsBox Spinner
    ArrayList<CategoryDetails> categoryDetailsArrayList = new ArrayList<>();

    ArrayList<String> categoryArraay = new ArrayList<>();
    SpinnerDialog spdCategory;
    int categoryId;


    //SharedPreferences
    SharedPreferences spref;
    SharedPreferences.Editor editor;
    String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sor___list_item__selected);

        db = new DbHelper(getApplicationContext());
        syncManager = new SyncManager(getApplicationContext());
        dateManager = new DateManager();

        initializeViews();

        tod = "Date(" + (new DateManager()).todayMillsec() + ")";

        Gson gson = new Gson();
        String getJson = SessionManager.pref.getString("user", "");
        LoginUser prefUser = gson.fromJson(getJson, LoginUser.class);
        updatedUser = prefUser.getUserName();

        //Get details from SharedPreferences
        spref = getSharedPreferences("invoice.conf", Context.MODE_PRIVATE);
        editor = spref.edit();
        userType = spref.getString("SalesRepType", "");
        System.out.println("User Type: " + userType);

        toolbar.setTitle("Revise Sales Order ");

        //get Data from INTENT
        if (getIntent().getSerializableExtra("orderList") != null) {
            orderList = (OrderList) getIntent().getSerializableExtra("orderList");
            totalVat = orderList.getTotalVAT();
            //totalDiscountAmount = orderList.getTotalDiscount();
            netTotalAmount = orderList.getTotalAmount();
        }

        //set Estimated Delivery Date & Total vat, Total Amount etc...
        salesOrderId = orderList.getSalesOrderId();
        isVatSalesOrder = orderList.getIsVat();
        estimatedDiliveryDate = orderList.getEstimateDeliveryDate();
        int start = estimatedDiliveryDate.indexOf("(");
        int end = estimatedDiliveryDate.indexOf(")");
        String iDate = estimatedDiliveryDate.substring(start + 1, end);

        if (orderList.getIsVat() == 1) {
            llVat.setVisibility(View.VISIBLE);
        } else {
            llVat.setVisibility(View.GONE);
        }

        if (getIntent().getSerializableExtra("selectedArrayList") != null) {
            double amount = 0;
            selectedArrayList = (ArrayList<ReviseSalesOrderList>) getIntent().getSerializableExtra("selectedArrayList");
            oldArrayList = (ArrayList<ReviseSalesOrderList>) getIntent().getSerializableExtra("oldArrayList");
        } else {
            selectedArrayList = syncManager.getReviseOrderLineItemsBySalesOrderId(salesOrderId);
            //set Old Array List
            for (int i = 0; i < selectedArrayList.size(); i++) {
                ReviseSalesOrderList rvOld = new ReviseSalesOrderList(selectedArrayList.get(i).getProductId(),
                        selectedArrayList.get(i).getBatchID(), selectedArrayList.get(i).getQuantity(),
                        selectedArrayList.get(i).getFreeQuantity());
                oldArrayList.add(rvOld);
            }
        }

        tvDeliveryDate.setText(dateManager.getDateAccordingToMilliSeconds(Long.parseLong(iDate), "yyyy-MM-dd"));
        year = Integer.parseInt(dateManager.getDateAccordingToMilliSeconds(Long.parseLong(iDate), "yyyy"));
        month = Integer.parseInt(dateManager.getDateAccordingToMilliSeconds(Long.parseLong(iDate), "MM"));
        day = Integer.parseInt(dateManager.getDateAccordingToMilliSeconds(Long.parseLong(iDate), "d"));

        System.out.println("Date: " + year + "." + month + "." + day);
        long tod_long = (new DateManager()).todayMillsec();
        final String tod = "/Date(" + tod_long + ")/";

        //Calculate Total Amounts and set Data to Text Views
        setTotalAmounts(selectedArrayList);

        loadRecyclerView(selectedArrayList);

        //Set Data sales order details to Text Views

        btnShowAllProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SOR__ListItemActivity_AllSelected.class);
                intent.putExtra("orderList", orderList);
                intent.putExtra("selectedArrayList", selectedArrayList);
                intent.putExtra("oldArrayList", oldArrayList);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                SOR__ListItemActivity_Selected.this.finish();
                startActivity(intent);
            }
        });

        //Category Details
        tvBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spdCategory.showSpinerDialog();
            }
        });
        categoryDetailsArrayList = syncManager.getCategoryDetailsFromSQLite("SELECT * FROM " + DbHelper.TABLE_CATEGORY);
        loadCategoryDetails(categoryDetailsArrayList);


        //Date Picker DialogBox
        tvDeliveryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(67);
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
                for (ReviseSalesOrderList reviseSalesOrderList : selectedArrayList) {
                    String produvtName = reviseSalesOrderList.getProductName().toLowerCase();
                    if (produvtName.contains(newText)) {
                        newArrayList.add(reviseSalesOrderList);
                    }
                }
                adapterForReviseSelected.setFilter(newArrayList);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //Sales Order
        tvReviseSalesOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                readPhoneNumber();

                DateManager dateManager = new DateManager();
                long todayMilSec = dateManager.todayMillsec();
                String updatedDate = "/Date(" + todayMilSec + ")/";
                double vatAmount = 0;

                if (isVatSalesOrder != 1) {
                    vatAmount = orderList.getTotalVAT();
                }

                //Delete old line item
                SQLiteDatabase database = db.getWritableDatabase();
                database.execSQL("DELETE FROM " + DbHelper.TABLE_LINE_ITEM + " WHERE " + DbHelper.COL_LINE_ITEM_SalesOrderID + " = " + salesOrderId + "");
                ArrayList<ProductListLast> productListLastArrayList = new ArrayList<ProductListLast>();

                for (int i = 0; i < selectedArrayList.size(); i++) {

                    int salesOrderID = salesOrderId;
                    int batchID = selectedArrayList.get(i).getBatchID();
                    int productID = selectedArrayList.get(i).getProductId();
                    int quantity = selectedArrayList.get(i).getQuantity();
                    int freeQuantity = selectedArrayList.get(i).getFreeQuantity();
                    double unitSellingPrice = selectedArrayList.get(i).getUnitSellingPrice();
                    double unitSellingDiscount = 0;
                    double totalAmount = selectedArrayList.get(i).getAmount();
                    double totalDiscount = 0;
                    int isSync = 0;
                    String name = selectedArrayList.get(i).getProductName();
                    String expiredate = selectedArrayList.get(i).getExpiryDate();
                    int stock = selectedArrayList.get(i).getStock();
                    int img = 0;
                    int isVat = selectedArrayList.get(i).getIsVat();
                    double vatRate = selectedArrayList.get(i).getVatRate();

                    ProductListLast productListLast = new ProductListLast(
                            salesOrderID, batchID, productID,
                            quantity, freeQuantity, unitSellingPrice,
                            unitSellingDiscount, totalAmount, totalDiscount,
                            isSync, name, expiredate, stock, img, isVat, vatRate, 0.0, tod
                    );

                    productListLastArrayList.add(productListLast);

                    //Insert Line Items
                    SalesOrderLineItem salesOrderLineItem = new SalesOrderLineItem(salesOrderId, batchID, productID,
                            quantity, freeQuantity, unitSellingPrice, unitSellingDiscount, totalAmount,
                            totalDiscount, isSync, updatedDate);

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
                double totalVAT = totalVat;
                String saleDate = orderList.getSaleDate();
                int isVat = orderList.getIsVat();
                int isReturn = orderList.getIsReturn();
                final int invoiceNumber = orderList.getInvoiceNumber();

                String estimateDeliveryDate;
                if (dilDate == 0) {
                    estimateDeliveryDate = orderList.getEstimateDeliveryDate();
                } else {
                    estimateDeliveryDate = "/Date(" + dilDate + ")/";
                }

                String syncDate = orderList.getSyncDate();
                int isSync = orderList.getIsSync();
                int salesOrderId = orderList.getSalesOrderId();
                ArrayList<ProductListLast> _ListSalesOrderLineItem = productListLastArrayList;
                String merchantName = orderList.getMerchntName();

                OrderList orderList1 = new OrderList(invoiceNumber, estimateDeliveryDate,
                        totalDiscount, totalAmount, syncDate, isSync, saleStatus,
                        enteredUser, enteredDate, updatedUser, updatedDate, saleDate,
                        saleTypeId, salesRepId, ditributorId, merchantId, salesOrderId,
                        totalVAT, isVat, isReturn, merchantName,orderList.getIsCredit(),orderList.getCreditDays());

                //insert Updated data to TABLE_SALES_ORDER

                database.execSQL("DELETE FROM " + DbHelper.TABLE_SALES_ORDER + " WHERE " + DbHelper.COL_SALES_ORDER_SalesOrderId + " = " + salesOrderId + "");
                ContentValues cv = orderList1.getOrderListContentValues();
                db.updateTable(salesOrderId + "", cv, DbHelper.COL_SALES_ORDER_SalesOrderId + " = ?", DbHelper.TABLE_SALES_ORDER);
                boolean success = db.insertDataAll(cv, DbHelper.TABLE_SALES_ORDER);
                System.out.println("SalesOrdtoerid sorder: " + salesOrderId);
                System.out.println("InvoiceNumber sorder: " + orderList1.getSalesOrderId());

                if (!success) {
                    System.out.println("Data is not inserted to " + DbHelper.TABLE_SALES_ORDER);
                } else {
                    System.out.println(DbHelper.TABLE_SALES_ORDER + " Table syncing...");
                }

                SalesOrder salesOrder = new SalesOrder(
                        merchantId, ditributorId, salesRepId, saleTypeId, enteredDate, enteredUser,
                        saleStatus, totalAmount, totalDiscount, totalVAT, saleDate, isVat, isReturn,
                        invoiceNumber, estimateDeliveryDate, syncDate, isSync, salesOrderId, _ListSalesOrderLineItem, tod
                );

                Gson gson = new Gson();
                String jsonString = gson.toJson(salesOrder, SalesOrder.class);
                System.out.println("Json String ===========\n " + jsonString);

                Cursor cursor = null;
                String jsonQuery = "SELECT * FROM " + DbHelper.TABLE_REVISE_ORDER_JSON + " WHERE " + DbHelper.COL_REVISE_SALES_ORDER_JSON_InvoiceNumber + " = " + invoiceNumber;
                cursor = database.rawQuery(jsonQuery, null);

                ReviseOrderJson reviseOrderJson = new ReviseOrderJson(invoiceNumber, jsonString, 0);
                ContentValues cvJson = reviseOrderJson.getReviseSalesOrderContentValues();
                if (cursor.getCount() > 0) {
                    System.out.println("Updated Revise Sales Order json table " + invoiceNumber);
                    db.updateTable(invoiceNumber + "", cvJson, DbHelper.COL_REVISE_SALES_ORDER_JSON_InvoiceNumber + " = ?", DbHelper.TABLE_REVISE_ORDER_JSON);
                } else {
                    System.out.println("No Data related to " + invoiceNumber);
                    boolean success1 = db.insertDataAll(cvJson, DbHelper.TABLE_REVISE_ORDER_JSON);
                    if (success1) {
                        System.out.println("Data is inserted to " + DbHelper.TABLE_REVISE_ORDER_JSON);
                    } else {
                        System.out.println("Data is not inserted to " + DbHelper.TABLE_REVISE_ORDER_JSON);
                    }
                }
                displayStatusMessage("Sales Order is Revised \nSales Order Number is " + salesOrderId, 1);

                //Stock Calculation
                stockCalculation(selectedArrayList, oldArrayList);

            }
        });


    }

    private void readPhoneNumber() {

        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        String number = tm.getLine1Number();
        String location = String.valueOf(tm.getCellLocation());
        String deviceID =tm.getDeviceId();
        String NetworkOperator =tm.getNetworkOperator();
        String SimSerialNumber =tm.getSimSerialNumber();

        System.out.println("========================================================");
        System.out.println("Mobile Number "+ number);
        System.out.println("Device ID "+ deviceID);
        System.out.println("Location "+ location);
        System.out.println("NetworkOperator "+ NetworkOperator);
        System.out.println("SimSerialNumber "+ SimSerialNumber);
        System.out.println("========================================================");
    }

    //Reduce Stock
    private void stockCalculation(ArrayList<ReviseSalesOrderList> selectedArrayList, ArrayList<ReviseSalesOrderList> oldArrayList) {

        System.out.println("Size of Selected array list: " +selectedArrayList.size());
        System.out.println("Size of Old Array list: " +oldArrayList.size());

        for (int i=0;i<selectedArrayList.size();i++){

            boolean avaialability = false;
            int productID = selectedArrayList.get(i).getProductId();
            int batchID= selectedArrayList.get(i).getBatchID();
            int quantity = selectedArrayList.get(i).getQuantity();
            int freeQuantity = selectedArrayList.get(i).getFreeQuantity();

            System.out.println("Product Id: "+productID);
            System.out.println("Batch Id: "+batchID);

            int oldQuantity = 0;
            int oldFreeQuantity = 0;
            for (int j =0 ; j<oldArrayList.size();j++){
                int oldProductId = oldArrayList.get(j).getProductId();
                int oldBatchID= oldArrayList.get(j).getBatchID();



                if (oldProductId==productID && oldBatchID==batchID){
                    avaialability=true;
                    oldQuantity = oldArrayList.get(j).getQuantity();
                    oldFreeQuantity = oldArrayList.get(j).getFreeQuantity();
                    System.out.println("Same array");
                }
            }
            System.out.println("Quantity Cal "+ quantity + " Old Quantity cal "+oldQuantity);
            if (avaialability){
                updateStock(productID,batchID,quantity,oldQuantity,freeQuantity,oldFreeQuantity);
            }else{
                updateStock(productID,batchID,quantity,oldQuantity,freeQuantity,oldFreeQuantity);
                System.out.println("new Prodct");
            }
        }


        //for reduse stock of removed products
        for (int x=0; x<oldArrayList.size();x++){

            boolean isUnavailable = true;
            int productId=oldArrayList.get(x).getProductId();
            int batchId=oldArrayList.get(x).getBatchID();
            int qty = oldArrayList.get(x).getQuantity();
            int freeQty = oldArrayList.get(x).getFreeQuantity();

            for (int y=0;y<selectedArrayList.size();y++){

                int newProductId=selectedArrayList.get(y).getProductId();
                int newBatchId=selectedArrayList.get(y).getBatchID();

                if (productId==newProductId && batchId==newBatchId){
                    isUnavailable = false;
                    break;
                }
            }

            if (isUnavailable){
                updateStock(productId,batchId,0,qty ,freeQty,0);
            }
        }
    }

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
        String query= null;
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


    //Set Total Amounts
    private void setTotalAmounts(ArrayList<ReviseSalesOrderList> selectedArrayList) {
        //set Total amounts to text views
        double amount = 0,totalVatAmount=0,freeAmount=0,itemVatAmount=0;
        totalVat=0;

        for (int i=0;i<selectedArrayList.size();i++){

            double subTotal = selectedArrayList.get(i).getUnitSellingPrice()*selectedArrayList.get(i).getQuantity();
            amount += subTotal;
            netTotalAmount = amount;

            double free = selectedArrayList.get(i).getFreeQuantity()*selectedArrayList.get(i).getUnitSellingPrice();

            freeAmount+=free;
            totalFreeAmount = freeAmount;

            //Calculate VAT
            double VATRATE = selectedArrayList.get(i).getVatRate();
            double unitprice = selectedArrayList.get(i).getUnitSellingPrice();
            int qty = selectedArrayList.get(i).getQuantity();
            double vat = 0;
            if (orderList.getIsVat()==1){
                if(VATRATE != 0 && unitprice != 0){
                    vat = (VATRATE*unitprice*qty)/(100+VATRATE);
                    itemVatAmount+=vat;
                }else{
                    itemVatAmount = 0.00;
                }
            }
            totalVat = itemVatAmount;
        }

        //Set Total Values
        tvTotalAmount.setText(String.format("Rs. %.2f", netTotalAmount+totalFreeAmount));
        tvTotal.setText(String.format("Rs. %.2f", netTotalAmount));
        tvTotalDiscountAmount.setText(String.format("Rs. %.2f", totalFreeAmount));
        tvTotalVatAmount.setText(String.format("Rs. %.2f", totalVat));
    }

    //Date Picker Functions
    @Override
    protected Dialog onCreateDialog(int id) {

        if(id == 67) {
            return new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            showDate(year, month+1, dayOfMonth);
                        }
                    }, year, month-1, day);
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

        try{
            dilDate = dateManager.getMilSecAccordingToDate( dy+"."+mnt+"."+Year);
            System.out.println("==== Date From Milliseconds =====" + dilDate);
        }catch (Exception e){
            System.out.println("Date Conversion Error " + e.getMessage());
        }


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
        tvReviseSalesOrder = (TextView)findViewById(R.id.tvReviseSalesOrder);
        tvTotalAmount = (TextView)findViewById(R.id.tvTotalAmount);


        //Buttons
        tbProducts   = (ToggleButton) findViewById(R.id.tbProducts);
        btnShowAllProducts   = (Button) findViewById(R.id.btnShowAllProducts);


        //EditText
        etSearchProduct = (EditText) findViewById(R.id.etSearchProduct);


        //Layouts
        llVat = (LinearLayout) findViewById(R.id.llVat);


        /*calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);*/

    }

    private void loadRecyclerView(ArrayList<ReviseSalesOrderList> reviseSalesOrderListArrayList) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL,
                true
        );

        rvProductList.setLayoutManager(layoutManager);
        rvProductList.setHasFixedSize(true);
        adapterForReviseSelected = new AdapterForReviseSelected(
                selectedArrayList,
                SOR__ListItemActivity_Selected.this
        );
        rvProductList.setAdapter(adapterForReviseSelected);
    }

    //get quantity and free quantity from dialog box
    public void getQty(final ReviseSalesOrderList reviseSalesOrderList){


        AlertDialog.Builder mBuilder = new AlertDialog.Builder(SOR__ListItemActivity_Selected.this);
        View mView = getLayoutInflater().inflate(R.layout.layout_for_popup,null);

        final EditText etQuantity,etFreeQuantity;
        TextView tvButton;

        etQuantity = (EditText) mView.findViewById(R.id.etQuantity);
        etFreeQuantity = (EditText) mView.findViewById(R.id.etFreeQuantity);
        tvButton = (TextView)mView.findViewById(R.id.tvButton);

        //Set quantities
        etQuantity.setText(""+reviseSalesOrderList.getQuantity());
        etFreeQuantity.setText(""+reviseSalesOrderList.getFreeQuantity());


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

                //set current product quantity , free quantity and amount
                reviseSalesOrderList.setQuantity(quantity);
                reviseSalesOrderList.setFreeQuantity(freeQuantity);
                reviseSalesOrderList.setISCheckedItem(true);
                reviseSalesOrderList.setAmount(reviseSalesOrderList.getUnitSellingPrice()*quantity);


                boolean add = false;
                for (int i = 0 ; i < selectedArrayList.size();i++){
                    if (selectedArrayList.get(i).getProductId()==reviseSalesOrderList.getProductId()){
                        selectedArrayList.set(i,reviseSalesOrderList);
                        add = true;
                    }
                }

                if (add==false){
                    selectedArrayList.add(reviseSalesOrderList);
                }

                setTotalAmounts(selectedArrayList);


                dialog.dismiss();
                loadRecyclerView(selectedArrayList);

            }
        });

        mBuilder.setView(mView);
        dialog = mBuilder.create();
        dialog.show();
    }

    //remove and add products
    public void prepare(View view, int position){
        double amount = 0,freeAmount=0,itemVatAmount=0;

        if(((CheckBox)view).isChecked()){
            reviseSalesOrderList = selectedArrayList.get(position);
            reviseSalesOrderList.setISCheckedItem(true);
            reviseSalesOrderList.setQuantity(1);
            selectedArrayList.add(reviseSalesOrderList);
            adapterForReviseSelected.notifyDataSetChanged();
            setTotalAmounts(selectedArrayList);

        }else {

            reviseSalesOrderList = selectedArrayList.get(position);
            reviseSalesOrderList.setISCheckedItem(false);
            selectedArrayList.remove(reviseSalesOrderList);
            netTotalAmount = netTotalAmount -reviseSalesOrderList.getAmount();
            totalFreeAmount = totalFreeAmount-(reviseSalesOrderList.getFreeQuantity()*reviseSalesOrderList.getUnitSellingPrice());

            tvTotal.setText(String.format("Rs. %.2f", netTotalAmount));
            tvTotalDiscountAmount.setText(String.format("Rs. %.2f", totalFreeAmount));
            tvTotalAmount.setText(String.format("Rs. %.2f", totalFreeAmount+netTotalAmount));


        }

    }

    private void loadCategoryDetails(final ArrayList<CategoryDetails> categoryDetailsArrayList) {
        categoryArraay.clear();
        String brandName;
        for (int i = 0; i < categoryDetailsArrayList.size(); i++) {
            if (categoryDetailsArrayList.get(i).getCategoryName().equalsIgnoreCase("other")){
                brandName = "ALL Products";
            }else{
                brandName = categoryDetailsArrayList.get(i).getCategoryName();
            }
            categoryArraay.add(brandName);
        }
        spdCategory=new SpinnerDialog(SOR__ListItemActivity_Selected.this,categoryArraay,"Select or Search Category",R.style.DialogAnimations_SmileWindow);

        spdCategory.bindOnSpinerListener(new OnSpinerItemClick()
        {
            @Override
            public void onClick(String item, int position)
            {

             tvBrand.setText(item);
                if (item.equalsIgnoreCase("ALL Products")){
                    item = "Other";
                    adapterForReviseSelected.setFilter(selectedArrayList);
                }

                for (int  i = 0 ;i<categoryDetailsArrayList.size();i++){

                    if (categoryDetailsArrayList.get(i).getCategoryName().equalsIgnoreCase(item)){

                        categoryId = categoryDetailsArrayList.get(i).getCategoryid();

                        ArrayList<ReviseSalesOrderList> newArrayList = new ArrayList<>();
                        for(int j = 0; j<selectedArrayList.size();j++){
                            int cid = selectedArrayList.get(j).getCategoryId();
                            if(cid==categoryId){
                                newArrayList.add(selectedArrayList.get(j));
                            }
                        }

                        if (newArrayList.size()>0){
                            adapterForReviseSelected.setFilter(newArrayList);
                        }else {
                            Toast.makeText(SOR__ListItemActivity_Selected.this, "No Products related to this brand", Toast.LENGTH_LONG).show();
                        }
                    }
                }



            }
        });
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

        builder = new AlertDialog.Builder(SOR__ListItemActivity_Selected.this);
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
                Intent intent = new Intent(getApplicationContext(), SalesOrderOfflineActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        displayWarningMessage("Do you want  to cancel Revise Order? ",3);
    }

    private void displayWarningMessage(String s,int colorValue) {

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

        builder = new AlertDialog.Builder(SOR__ListItemActivity_Selected.this);
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
                Intent intent = new Intent(getApplicationContext(),SalesOrderOfflineActivity.class);
                SOR__ListItemActivity_Selected.this.finish();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

    }


}
