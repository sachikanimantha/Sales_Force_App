package com.example.bellvantage.scadd;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bellvantage.scadd.Adapters.ReturnInventoryAdapter;
import com.example.bellvantage.scadd.DB.DbHelper;
import com.example.bellvantage.scadd.Utils.DateManager;
import com.example.bellvantage.scadd.Utils.SessionManager;
import com.example.bellvantage.scadd.Utils.SyncManager;
import com.example.bellvantage.scadd.Utils.UtilityManager;
import com.example.bellvantage.scadd.swf.DefProduct;
import com.example.bellvantage.scadd.swf.LoginUser;
import com.example.bellvantage.scadd.swf.MerchantDetails;
import com.example.bellvantage.scadd.swf.OrderList;
import com.example.bellvantage.scadd.swf.ProductBatch;
import com.example.bellvantage.scadd.swf.ReturnInventory;
import com.example.bellvantage.scadd.swf.ReturnInventoryJson;
import com.example.bellvantage.scadd.swf.ReturnInventoryLineItem;
import com.example.bellvantage.scadd.swf.ReturnType;
import com.example.bellvantage.scadd.web.HTTPPaths;
import com.google.gson.Gson;

import java.util.ArrayList;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

import static com.example.bellvantage.scadd.DB.DbHelper.COL_INVOICE_ITEM_InvoiceId;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_INVOICE_LINE_ITEM_invoiceID;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_INVOICE_LINE_ITEM_itemName;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_INVOICE_LINE_ITEM_price;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_INVOICE_LINE_ITEM_productID;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SRI_BATCH_ID;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SRI_PRODUCT_ID;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SRI_QTY_INHAND;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SYNC_ID_NEXT_ID;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SYNC_ID_TABLE_ID;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_VEHICLE_INVENTORY_BatchID;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_VEHICLE_INVENTORY_LoadQuantity;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_VEHICLE_INVENTORY_ProductId;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_INVOICE_ITEM;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_INVOICE_LINE_ITEM;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_RETURN_INVENTORY;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_RETURN_INVENTORY_ID;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_RETURN_INVENTORY_JSON;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_RETURN_INVENTORY_LINEITEM;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_SALESREP_INVENTORY;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_SYNC_ID;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_VEHICALE_INVENTORY;

public class ReturnActivty extends AppCompatActivity {
    //Views
    Toolbar toolbar;
    TextView tvReason, tvProductName, tvExpireDate, tvTotalAmount, tvProductStatus, tvAddProduct, tvFinish, tvMerchantSelect,
            tvTotalDicountAmount, tvNetTotalAmount, tvTotalOutstandingAmount;
    EditText etQuantity;//etDiscount;
    RecyclerView rvProductList;
    LinearLayout llMerchantName, llInvoice;

    DbHelper db;
    SyncManager syncManager;
    DateManager dateManager;
    boolean setItemData = false;
    int pos, setDataQty;

    //Return Type DialogsBox Spinner
    ArrayList<ReturnType> returnTypeArrayList = new ArrayList<>();
    ArrayList<String> returnTypeArray = new ArrayList<>();
    SpinnerDialog spdReturnType;
    int returnTypeId, batchID, distributerId;
    double dis;

    //Product DialogsBox Spinner
    ArrayList<DefProduct> productArrayList = new ArrayList<>();

    ArrayList<String> productArray = new ArrayList<>();
    SpinnerDialog spdProduct;
    int productId, salesRepId, selectedProductId;

    double unitPrice = 0, totalReturnAmount = 0, totalAmount = 0, totalOutstanding = 0, totalDiscount = 0;
    String enterdUser, selectedName = null;

    //Expire Date DialogBox == Product Batch
    ArrayList<ProductBatch> productBatchArrayList = new ArrayList<>();
    ArrayList<String> productBatchArray = new ArrayList<>();
    SpinnerDialog spdProductBatch;
    int productProductBatchId;

    //Product Status DialogsBox Spinner
    ArrayList<String> productStatusArray = new ArrayList<>();
    SpinnerDialog spdProductStatus;
    int productStatusId;

    //Spinners
    ArrayList<String> merchantArray = new ArrayList<>();
    ArrayList<MerchantDetails> merchantArrayList = new ArrayList<>();
    SpinnerDialog spdMerchant;
    int merchantID;
    boolean isMerchantSelect = false;

    // Invoice Spinner
    ArrayList<String> invoiceArray = new ArrayList<>();
    SpinnerDialog spdInvoice;
    String invoiceId;

    ArrayList<ReturnInventoryLineItem> returnInventoryLineItemArrayList = new ArrayList<>();
    ArrayList<ReturnInventoryLineItem> returnInventorySelectedBatch = new ArrayList<>();
    ReturnInventoryAdapter returnInventoryAdapter;

    //SharedPreferences
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    int NextCreditNoteNo, creditNoteNo, previousCreditNoteNo, userId;
    String userName, SalesRepType;

    // AlertDialog
    AlertDialog dialog;
    AlertDialog.Builder mBuilder;

    //OrderList
    OrderList orderList;
    int IntentId=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_return);

        initializeViews();
        //Initialize Utilities
        db = new DbHelper(getApplicationContext());
        syncManager = new SyncManager(getApplicationContext());
        dateManager = new DateManager();


        pref = getSharedPreferences("invoice.conf", Context.MODE_PRIVATE);
        editor = pref.edit();


        //Get details from SharedPreferences
        NextCreditNoteNo = pref.getInt("NextCreditNoteNo", -1);
        pref = getSharedPreferences("invoice.conf", Context.MODE_PRIVATE);
        editor = pref.edit();
        SalesRepType = pref.getString("SalesRepType", "");
        System.out.println("SalesRep Type: " + SalesRepType);

        System.out.println("==== Next Credit Note Number PREF" + NextCreditNoteNo);


        //Generate CreditNote Number

        try {
            previousCreditNoteNo = getPreviousCreditNoteNo();
            System.out.println("Previous Credit Note Number: " + previousCreditNoteNo);
        } catch (Exception e) {
            System.out.println("Error at create Credit Note Number: " + e.getMessage());
        }

        System.out.println("Next Credit Note Number: " + NextCreditNoteNo);


        //get Data from INTENT
        if (getIntent().getSerializableExtra("merchantId") != null) {
            merchantID = (int) getIntent().getSerializableExtra("merchantId");
            System.out.println("Merchant ID get Intent" + merchantID);
            llMerchantName.setVisibility(View.GONE);
            isMerchantSelect = true;
            returnInventoryLineItemArrayList = (ArrayList<ReturnInventoryLineItem>) getIntent().getSerializableExtra("productsReturnLast");
        }

        if (getIntent().getSerializableExtra("id")!=null){
            IntentId = (int) getIntent().getSerializableExtra("id");
            System.out.println("id ===== "+IntentId);
        }

        if(IntentId==1 || IntentId==2){
            if (getIntent().getSerializableExtra("orderList")!=null){
                orderList = (OrderList) getIntent().getSerializableExtra("orderList");
            }
            System.out.println("========== id "+IntentId);
            System.out.println("========== Order Id "+orderList.getSalesOrderId());
        }

        loadRecyclerView(returnInventoryLineItemArrayList);


        Gson gson = new Gson();
        String getJson = SessionManager.pref.getString("user", "");
        LoginUser prefUser = gson.fromJson(getJson, LoginUser.class);
        salesRepId = prefUser.getUserTypeId();
        enterdUser = prefUser.getUserName();
        distributerId = syncManager.getDitstributerId();
        userId = prefUser.getUserTypeId();
        userName = prefUser.getUserName();
        System.out.println("======= Distributor Id " + distributerId);
        creditNoteNo = NextCreditNoteNo;
        //creditNoteNo = Integer.parseInt((salesRepId+1000)+""+NextCreditNoteNo);



        /*etDiscount.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });*/

        etQuantity.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });


        //set Title to the toolbar
        toolbar.setTitle("Return Items");

        //Return Type DialogsBox Spinner
        tvReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spdReturnType.showSpinerDialog();
            }
        });
        returnTypeArrayList = syncManager.getReturnTypeArrayList();
        loadReturnTypes(returnTypeArray);

        //Prodduct DialogsBox Spinner
        tvProductName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spdProduct.showSpinerDialog();
            }
        });
        productArrayList = syncManager.getProductArrayList();
        loadProducts(productArrayList);

        //ProductStatus DialogsBox Spinner
        tvProductStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spdProductStatus.showSpinerDialog();
            }
        });

        //Invoice DialogsBox Spinner
       /* tvInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spdInvoice.showSpinerDialog();
            }
        });*/
        invoiceArray = getInvoiceDetails();
        //loadInvoice(invoiceArray);

        loadProductStatus();
        System.out.println("================ Product id zcsxc " + productId);

        //merchant DialogBox Spinner
        merchantArrayList = syncManager.getMerchantList();
        System.out.println("===Merchant array list size " + merchantArrayList.size());
        loadMerchant(merchantArrayList);
        tvMerchantSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spdMerchant.showSpinerDialog();
            }
        });


        //ProductBatch DialogsBox Spinner
        tvExpireDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pname = tvProductName.getText().toString();
                if (pname.equalsIgnoreCase("Select a Product")) {
                    Toast.makeText(ReturnActivty.this, "Select a Product", Toast.LENGTH_SHORT).show();
                    return;
                }
                spdProductBatch.showSpinerDialog();

            }
        });
        productBatchArrayList = syncManager.getProductBatchArrayList();

        //Quantity TextChange Listener
        etQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String eDate = tvExpireDate.getText().toString();
                String product = tvProductName.getText().toString();
                if (eDate.equalsIgnoreCase("Select a Expire Date")) {
                    if (!product.equalsIgnoreCase("Select a Product")) {
                        Toast.makeText(ReturnActivty.this, "Select a Expire Date et on text chnged", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }
                String quantity = charSequence.toString();

                if (!quantity.equals("")) {
                    System.out.println("======== Unit Selling Price TOTAL" + unitPrice);
                    int qty = Integer.parseInt(quantity);
                    totalAmount = qty * unitPrice;
                    tvTotalAmount.setText(String.format("RS %.2f", totalAmount));
                    System.out.println("Total Amount: " + String.format("RS %.2f", totalAmount));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        //Quantity TextChange Listener
        /*etDiscount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String eDate = tvExpireDate.getText().toString();
                if (eDate.equalsIgnoreCase("Select a Expire Date")){
                    Toast.makeText(ReturnActivty.this, "Select a Expire Date et Dis text changed", Toast.LENGTH_SHORT).show();
                    return;
                }
                String discount = charSequence.toString();

                if (!discount.equals("")){
                    System.out.println("======== Unit Selling Price TOTAL" + unitPrice);
                    dis = Double.parseDouble(discount);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/

        tvAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String merchantName = tvMerchantSelect.getText().toString();
                String reason = tvReason.getText().toString();
                String proStatus = tvProductStatus.getText().toString();
                String expDate = tvExpireDate.getText().toString();
                String productName = tvProductName.getText().toString();
                String qtys = etQuantity.getText().toString();


                if (productName.equalsIgnoreCase("Select a Product")) {
                    Toast.makeText(ReturnActivty.this, "Select a Product", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (expDate.equalsIgnoreCase("Select a Expire Date")) {
                    Toast.makeText(ReturnActivty.this, "Select a Expire Date add prduct select ed text", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isMerchantSelect) {
                    if (merchantName.equalsIgnoreCase("Select a merchant")) {
                        Toast.makeText(ReturnActivty.this, "Select a merchant", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }


                if (reason.equalsIgnoreCase("Select a Reason")) {
                    Toast.makeText(ReturnActivty.this, "Select a reason", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (proStatus.equalsIgnoreCase("Select Product Status")) {
                    Toast.makeText(ReturnActivty.this, "Select a product status", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (qtys.equals("")) {
                    Toast.makeText(ReturnActivty.this, "Enter Quantity", Toast.LENGTH_SHORT).show();
                    return;
                }
                int qty = Integer.parseInt(qtys);


                boolean add = false;
                tvProductName.setEnabled(true);


                System.out.println("Return Type ID" + returnTypeId);
                if (setItemData) {
                    ReturnInventoryLineItem returnInventoryLineItem = new ReturnInventoryLineItem(
                            creditNoteNo, productId, batchID, qty, totalAmount, returnTypeId, productStatusId, 0, productName, unitPrice, 0
                    );
                    returnInventoryLineItemArrayList.set(pos, returnInventoryLineItem);

                    setItemData = false;

                } else {

                    for (int i = 0; i < returnInventoryLineItemArrayList.size(); i++) {
                        if (productId == returnInventoryLineItemArrayList.get(i).getProductId() && batchID == returnInventoryLineItemArrayList.get(i).getBatchId()) {
                            totalAmount = totalAmount + returnInventoryLineItemArrayList.get(i).getTotalAmount();
                            qty = qty + returnInventoryLineItemArrayList.get(i).getQuantity();
                            // discount = discount  + returnInventoryLineItemArrayList.get(i).getDiscountedPrice();

                            ReturnInventoryLineItem returnInventoryLineItem = new ReturnInventoryLineItem(
                                    creditNoteNo, productId, batchID, qty, totalAmount, returnTypeId, productStatusId, 0, productName, unitPrice, 0
                            );

                            returnInventoryLineItemArrayList.set(i, returnInventoryLineItem);
                            add = true;
                        }
                    }

                    if (!add) {
                        //set added product to RecyclerView
                        ReturnInventoryLineItem returnInventoryLineItem = new ReturnInventoryLineItem(
                                creditNoteNo, productId, batchID, qty, totalAmount, returnTypeId, productStatusId, 0, productName, unitPrice, 0
                        );
                        returnInventoryLineItemArrayList.add(returnInventoryLineItem);
                    }
                }

                loadRecyclerView(returnInventoryLineItemArrayList);

                //set Default Values
                tvProductName.setText("Select a Product");
                tvExpireDate.setText("Select a Expire Date");
                tvReason.setText("Select a Reason");
                tvProductStatus.setText("Select Product Status");
                //tvInvoice.setText("Select an Invoice");
                etQuantity.setText("");
                tvTotalAmount.setText("");
                //etDiscount.setText("");

                totalDiscount = 0;
                totalReturnAmount = 0;
                totalOutstanding = 0;

                //Set Total Return Amount and Total Outstanding
                for (int j = 0; j < returnInventoryLineItemArrayList.size(); j++) {
                    totalDiscount += returnInventoryLineItemArrayList.get(j).getDiscountedPrice();
                    totalReturnAmount += returnInventoryLineItemArrayList.get(j).getTotalAmount();
                    totalOutstanding = totalReturnAmount;
                }

                tvTotalDicountAmount.setText(String.format("Rs. %.2f", totalDiscount));
                tvNetTotalAmount.setText(String.format("Rs. %.2f", totalReturnAmount));
                // tvTotalOutstandingAmount.setText(String.format("Rs. %.2f",totalOutstanding));
                llInvoice.setVisibility(View.GONE);
                //etQuantity.setFocusable(false);


            }
        });

        //Confirm Return note and add credit note to SQLite DB
        tvFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("Return Note Array Size: " + returnInventoryLineItemArrayList.size());

                //Generate Credit Note Number
                if (NextCreditNoteNo > previousCreditNoteNo) {
                    NextCreditNoteNo++;
                    creditNoteNo = NextCreditNoteNo;
                } else {
                    previousCreditNoteNo++;
                    creditNoteNo = previousCreditNoteNo;
                }

                System.out.println("====Next Credit Note Number PREF SAVE " + creditNoteNo);
                editor.putInt("NextCreditNoteNo", creditNoteNo);
                editor.apply();

                //Update Credit Note Number
                updateCreditNoteNo(creditNoteNo);

                if (returnInventoryLineItemArrayList.size() == 0) {
                    Toast.makeText(ReturnActivty.this, "Please add  products ", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (int i = 0; i < returnInventoryLineItemArrayList.size(); i++) {

                    if (returnInventoryLineItemArrayList.get(i).getUnitPrice() == 0 || returnInventoryLineItemArrayList.get(i).getBatchId() == 0 || returnInventoryLineItemArrayList.get(i).getTotalAmount() == 0) {
                        Toast.makeText(ReturnActivty.this, "Please fill all the data of  " + returnInventoryLineItemArrayList.get(i).getProductName(), Toast.LENGTH_LONG).show();
                        return;
                    }
                }


                String syncDate = "/Date(" + (new DateManager()).todayMillsec() + ")/";
                System.out.println("==== Next Credit Card Number 1: " + NextCreditNoteNo);
                String returnDate = syncDate;

                System.out.println("==== Next Credit Card Number 2: " + NextCreditNoteNo);
                ReturnInventory returnInventory = new ReturnInventory(new UtilityManager().genarateInvoiceId(creditNoteNo, salesRepId),
                        returnDate, totalReturnAmount, totalOutstanding,
                        merchantID, salesRepId, distributerId, 0, syncDate);

                System.out.println("Merchant ID before cv" + merchantID);

                displayStatusMessage("Return note is Created \n Return note Number: " + new UtilityManager().genarateInvoiceId(creditNoteNo, salesRepId), 1, 0);


                ContentValues cv = returnInventory.getReturnInventoryContentValues();

                System.out.println("Merchant ID CV " + cv.get("merchantId"));
                System.out.println("Credit Note Number " + cv.get("creditNoteNo"));
                System.out.println("Credit SalesRepId " + cv.get("SalesRepId"));

                boolean success = db.insertDataAll(cv, TABLE_RETURN_INVENTORY);
                if (!success) {
                    System.out.println("Not inserted to " + TABLE_RETURN_INVENTORY);
                } else {
                    System.out.println("Inserted to " + TABLE_RETURN_INVENTORY);
                }

                for (int i = 0; i < returnInventoryLineItemArrayList.size(); i++) {
                    if (returnInventoryLineItemArrayList.get(i).getUnitPrice() > 0) {
                        ReturnInventoryLineItem returnInventoryLineItem = returnInventoryLineItemArrayList.get(i);
                        returnInventoryLineItem.setCreditNoteNo(new UtilityManager().genarateInvoiceId(creditNoteNo, salesRepId));
                        ContentValues cv1 = returnInventoryLineItem.getReturnInventoryLineItemCv();
                        boolean s = db.insertDataAll(cv1, TABLE_RETURN_INVENTORY_LINEITEM);

                        System.out.println("Return Note Number line item " + cv1.get("creditNoteNo"));

                        if (!s) {
                            System.out.println("Not inserted to " + TABLE_RETURN_INVENTORY_LINEITEM);
                        } else {
                            System.out.println("Inserted to " + TABLE_RETURN_INVENTORY_LINEITEM);
                        }
                    } else {
                        returnInventoryLineItemArrayList.remove(i);
                        System.out.println("Removed " + returnInventoryLineItemArrayList.get(i).getProductName() + returnInventoryLineItemArrayList.get(i).getUnitPrice());
                    }
                }

                System.out.println("Total Return Amount: "+totalReturnAmount);
                System.out.println("Total Return Outstanding: "+totalOutstanding);
                //Generate Json
                ReturnInventory returnInventoryJsonObj = new ReturnInventory(creditNoteNo,
                        returnDate, totalReturnAmount, totalOutstanding,
                        merchantID, salesRepId, distributerId, syncDate, 0, syncDate, enterdUser, null, null, null, returnInventoryLineItemArrayList);

                Gson gson1 = new Gson();
                String returnInventoryJson = gson1.toJson(returnInventoryJsonObj, ReturnInventory.class);
                System.out.println("========= Return Inventory Json: " + returnInventoryJson);
                String url = HTTPPaths.seriveUrl + "CreateCreditNoteMobile";

                ReturnInventoryJson returnInventoryJson1 = new ReturnInventoryJson(creditNoteNo, returnInventoryJson, 0);
                ContentValues cve = returnInventoryJson1.getReturnInvntoryJsonValues();
                boolean suc = db.insertDataAll(cve, TABLE_RETURN_INVENTORY_JSON);
                if (suc) {
                    System.out.println("Data is inserted to " + TABLE_RETURN_INVENTORY_JSON + "\n Data === " + cve.toString());
                } else {
                    System.out.println("Data is inserted to " + TABLE_RETURN_INVENTORY_JSON + "\n Data === " + cve.toString());
                }

                //Add products to inventory
                addProductsToInventory(returnInventoryLineItemArrayList);
                etQuantity.clearFocus();
            }
        });
    }

    private void addProductsToInventory(ArrayList<ReturnInventoryLineItem> returnInventoryLineItemArrayList) {
        SQLiteDatabase database = db.getWritableDatabase();


        for (int i = 0; i < returnInventoryLineItemArrayList.size(); i++) {
            int pid = returnInventoryLineItemArrayList.get(i).getProductId();
            int bid = returnInventoryLineItemArrayList.get(i).getBatchId();
            int returnType = returnInventoryLineItemArrayList.get(i).getReturnType();
            int isSalable = returnInventoryLineItemArrayList.get(i).getIsSellable();
            int qty = returnInventoryLineItemArrayList.get(i).getQuantity();





            if (returnInventoryLineItemArrayList.get(i).getReturnType() == 3 || returnInventoryLineItemArrayList.get(i).getReturnType() == 5) {

                if (returnType ==3 && isSalable!=1){
                    System.out.println("Inventory is not updated...");
                }else{
                    int dbQty = getProductQuantitFromDatabase(pid, bid);

                    ContentValues cv = new ContentValues();

                    int afectedRows,affectedRowsSR = 0;
                    System.out.println("Sales Rep Type: " + SalesRepType);

                    if (SalesRepType.equalsIgnoreCase("R")) {

                        cv.put(COL_VEHICLE_INVENTORY_LoadQuantity, dbQty + returnInventoryLineItemArrayList.get(i).getQuantity());
                        System.out.println("Database Quantity update" + dbQty+qty);
                        afectedRows = database.update(TABLE_VEHICALE_INVENTORY, cv, COL_VEHICLE_INVENTORY_ProductId + " = ?  AND " + COL_VEHICLE_INVENTORY_BatchID + " = ? ", new String[]{"" + pid, "" + bid});

                       //SalesRepInventory
                        int dbSRQty = getSalesRepProductQuantitFromDatabase(pid, bid);
                        ContentValues cvSR = new ContentValues();
                        cvSR.put(COL_SRI_QTY_INHAND, dbSRQty+qty);
                        affectedRowsSR = database.update(TABLE_SALESREP_INVENTORY, cvSR, COL_SRI_PRODUCT_ID + " = ?  AND " + COL_SRI_BATCH_ID + " = ? ", new String[]{"" + pid, "" + bid});

                    } else {
                        cv.put(COL_SRI_QTY_INHAND, dbQty+qty);
                        afectedRows = database.update(TABLE_SALESREP_INVENTORY, cv, COL_SRI_PRODUCT_ID + " = ?  AND " + COL_SRI_BATCH_ID + " = ? ", new String[]{"" + pid, "" + bid});
                    }

                    if (afectedRows > 0) {
                        System.out.println(afectedRows + " Stock is updated ProductID: " + pid + " BatchID: " + bid);
                    }
                    System.out.println("Updated Saelsrep Inventory count: "+affectedRowsSR);
                }



            }
        }
    }

   /* private void loadInvoice(ArrayList<String> invoiceArray) {

        spdInvoice=new SpinnerDialog(ReturnActivty.this,invoiceArray,"Select an Invoice",R.style.DialogAnimations_SmileWindow);

        spdInvoice.bindOnSpinerListener(new OnSpinerItemClick()
        {
            @Override
            public void onClick(String item, int position)
            {
                ArrayList<DefProduct> productArray= getInvoiceLineItem(item);
                boolean isInvoiceItem=false;
                tvInvoice.setText(item);
                //productArrayList=getInvoiceLineItem(item);
                loadProducts(productArray);

               *//* if(setItemData){
                    for (int i=0;i<productArray.size();i++){
                        if (selectedName==productArray.get(i).getProductName()){
                            isInvoiceItem=true;
                            break;
                        }
                    }

                   if (isInvoiceItem==false){
                       displayStatusMessage("Your selected product is not in this Invoice",3,1);
                       tvInvoice.setText("Select an Invoice");
                   }

                }*//*


                    // Toast.makeText(ReturnActivty.this, ""+item, Toast.LENGTH_SHORT).show();
            }
        });
    }*/

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), SalesOrderOfflineActivity.class);
        ReturnActivty.this.finish();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private ArrayList<DefProduct> getInvoiceLineItem(String item) {

        System.out.println("==========================================================Invoice ID " + item);

        ArrayList<DefProduct> products = new ArrayList<>();
        SQLiteDatabase database = db.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_INVOICE_LINE_ITEM +
                " WHERE " + COL_INVOICE_LINE_ITEM_invoiceID + " = " + item;
        Cursor cursor = database.rawQuery(query, null);

        System.out.println("Cursor Size " + cursor.getCount());

        while (cursor.moveToNext()) {
            System.out.println("While Loop of====================================================");

            int ProductId = cursor.getInt(cursor.getColumnIndex(COL_INVOICE_LINE_ITEM_productID));
            String ProductName = cursor.getString(cursor.getColumnIndex(COL_INVOICE_LINE_ITEM_itemName));
            double UnitSellingPrice = cursor.getDouble(cursor.getColumnIndex(COL_INVOICE_LINE_ITEM_price));
            //invoiceItems.add(id);

            DefProduct defProduct = new DefProduct(ProductId, 0, 0, 0, 0,
                    ProductName, 0, "", "", UnitSellingPrice, 0, "", 0, 0);
            products.add(defProduct);

        }

        System.out.println("Product Array List Size= " + products.size());
        return products;

    }

    private void updateCreditNoteNo(int creditNoteNo) {
        SQLiteDatabase database = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_SYNC_ID_NEXT_ID, creditNoteNo);
        int afectedRows = database.update(TABLE_SYNC_ID, cv, COL_SYNC_ID_TABLE_ID + " = ? ", new String[]{"" + TABLE_RETURN_INVENTORY_ID});
        if (afectedRows > 0) {
            System.out.println(TABLE_SYNC_ID + " is Updated " + " Credit Note Number " + creditNoteNo);
        }
    }

    private int getPreviousCreditNoteNo() {
        int id = 0;
        SQLiteDatabase database = db.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_SYNC_ID + " WHERE " + COL_SYNC_ID_TABLE_ID + " = " + TABLE_RETURN_INVENTORY_ID;
        Cursor cursor = database.rawQuery(query, null);

        while (cursor.moveToNext()) {
            id = cursor.getInt(cursor.getColumnIndex(COL_SYNC_ID_NEXT_ID));
        }

        System.out.println("Previous CreditNote Number of SQLite Table : " + id);
        return id;
    }

    private void loadRecyclerView(ArrayList<ReturnInventoryLineItem> returnInventoryLineItemArrayList) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL,
                false
        );

        rvProductList.setLayoutManager(layoutManager);
        rvProductList.setHasFixedSize(true);
        returnInventoryAdapter = new ReturnInventoryAdapter(
                returnInventoryLineItemArrayList,
                ReturnActivty.this
        );
        rvProductList.setAdapter(returnInventoryAdapter);
    }

    private void loadProductStatus() {
        productStatusArray.clear();
        productStatusArray.add("Salable");
        productStatusArray.add("Non-Salable");
        spdProductStatus = new SpinnerDialog(ReturnActivty.this, productStatusArray, "Select Product Status", R.style.DialogAnimations_SmileWindow);

        spdProductStatus.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                tvProductStatus.setText(item);
                if (item.equalsIgnoreCase("Salable")) {
                    productStatusId = 1;
                }
                if (item.equalsIgnoreCase("Non-Salable")) {
                    productStatusId = 2;
                }
            }
        });

    }

    private void loadProductBatch(final ArrayList<ProductBatch> productBatchArrayList) {

        if (productBatchArrayList.size() == 0) {
//            Toast.makeText(this, "No Product Batch", Toast.LENGTH_LONG).show();
            displayStatusMessage("No Product Batch.. Go to Sync Manual and Sync Product Batches", 3, 1);
        }
        returnInventorySelectedBatch.clear();
        productBatchArray.clear();
        for (int i = 0; i < productBatchArrayList.size(); i++) {

            if (productId == productBatchArrayList.get(i).getProductId()) {
                System.out.println("====== Selected Product id inside spdproductBatch " + productId);//10
                System.out.println("====== Product id inside array " + productBatchArrayList.get(i).getProductId());//10
                String expireDate = productBatchArrayList.get(i).getExpiryDate();
                int batchId = productBatchArrayList.get(i).getBatchId();
                double unitPrice = productBatchArrayList.get(i).getUnitSellingPrice();
                System.out.println("Batch Id " + batchId);//1
                int start = expireDate.indexOf("(");
                int end = expireDate.indexOf(")");
                String iDate = expireDate.substring(start + 1, end);
                expireDate = dateManager.getDateAccordingToMilliSeconds(Long.parseLong(iDate), "yyyy-MM-dd");
                productBatchArray.add(expireDate+  String.format(" - Rs %.2f", unitPrice));
                returnInventorySelectedBatch.add(new ReturnInventoryLineItem(productId, batchId, unitPrice, expireDate));

            }
        }
        spdProductBatch = new SpinnerDialog(ReturnActivty.this, productBatchArray, "Select a Expire Date", R.style.DialogAnimations_SmileWindow);

        spdProductBatch.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {


                tvExpireDate.setText(item);
                etQuantity.setText("");

                unitPrice = returnInventorySelectedBatch.get(position).getUnitPrice();
                batchID = returnInventorySelectedBatch.get(position).getBatchId();
                int pid = returnInventorySelectedBatch.get(position).getProductId();
                System.out.println("====== Clicking List Position: " + position);
                System.out.println("====== Clicking Unit Price: " + unitPrice);
                System.out.println("====== Clicking batchID Price: " + batchID);
                System.out.println("====== Clicking Product Id: " + pid);

                if (setItemData) {
                    etQuantity.setText("" + setDataQty);
                    totalAmount = unitPrice * setDataQty;
                    tvTotalAmount.setText(String.format("RS %.2f", totalAmount));
                }
            }
        });
    }

    private void loadProducts(final ArrayList<DefProduct> productArrayList) {

        productArray.clear();
        for (int i = 0; i < productArrayList.size(); i++) {
            productArray.add(productArrayList.get(i).getProductName());
        }
        spdProduct = new SpinnerDialog(ReturnActivty.this, productArray, "Select or Search a Product", R.style.DialogAnimations_SmileWindow);

        spdProduct.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {

                tvProductName.setText(item);
                etQuantity.setText("");
                tvTotalAmount.setText("");
                tvExpireDate.setText("Select a Expire Date");
                for (int i = 0; i < productArrayList.size(); i++) {

                    if (productArrayList.get(i).getProductName().equalsIgnoreCase(item)) {
                        productId = productArrayList.get(i).getProductId();
                        System.out.println("================Selected Product id " + productId);
                        loadProductBatch(productBatchArrayList);
                    }

                }
            }
        });
    }

    private void loadReturnTypes(ArrayList<String> returnTypeArray) {

        returnTypeArray.clear();
        for (int i = 0; i < returnTypeArrayList.size(); i++) {
            returnTypeArray.add(returnTypeArrayList.get(i).getReturnTypeName());
        }
        spdReturnType = new SpinnerDialog(ReturnActivty.this, returnTypeArray, "Select a Reason", R.style.DialogAnimations_SmileWindow);

        spdReturnType.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {

                tvReason.setText(item);

                //SRN Invoice

              /*  if (item.equalsIgnoreCase("Returns from SRN")){

                    llInvoice.setVisibility(View.VISIBLE);
                    tvProductName.setEnabled(true);

                }else {
                    llInvoice.setVisibility(View.GONE);
                    if (setItemData){
                        tvProductName.setText(selectedName);
                    }else{
                        tvProductName.setText("Select a Product");
                    }
                    loadProducts(productArrayList);
                }
*/


                //Set Product Status
                if (item.equalsIgnoreCase("Expired")) {
                    tvProductStatus.setText("Non-Salable");
                    productStatusId = 2;
                } else if (item.equalsIgnoreCase("Returns from SRN") || item.equalsIgnoreCase("50% Off")) {
                    tvProductStatus.setText("Salable");
                    productStatusId = 1;
                }else if (item.equalsIgnoreCase("Damage") || item.equalsIgnoreCase("50% Off")) {
                    tvProductStatus.setText("Non-Salable");
                    productStatusId = 2;
                } else {
                    tvProductStatus.setText("Select Product Status");
                }

                for (int i = 0; i < returnTypeArrayList.size(); i++) {

                    if (returnTypeArrayList.get(i).getReturnTypeName().equalsIgnoreCase(item)) {

                        returnTypeId = returnTypeArrayList.get(i).getReturnType();
                        System.out.println("Return Type " + returnTypeId);
                    }
                }
            }
        });

    }

    private void loadMerchant(final ArrayList<MerchantDetails> merchantArrayList) {
        merchantArray.clear();
        for (int i = 0; i < merchantArrayList.size(); i++) {
            merchantArray.add(merchantArrayList.get(i).getMerchantName() + " - " + merchantArrayList.get(i).getMerchantId());
        }
        spdMerchant = new SpinnerDialog(ReturnActivty.this, merchantArray, "Select or Search Merchant", R.style.DialogAnimations_SmileWindow);

        spdMerchant.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {

                tvMerchantSelect.setText(item);

                for (int i = 0; i < merchantArrayList.size(); i++) {
                    String merchantName = merchantArrayList.get(i).getMerchantName() + " - " + merchantArrayList.get(i).getMerchantId();
                    if (merchantName.equals(item)) {
                        merchantID = merchantArrayList.get(i).getMerchantId();
                    }
                }

            }
        });
    }

    private void initializeViews() {
        toolbar = (Toolbar) findViewById(R.id.tb_main);

        //TextViews
        tvReason = (TextView) findViewById(R.id.tvReason);
        tvProductName = (TextView) findViewById(R.id.tvProductName);
        tvExpireDate = (TextView) findViewById(R.id.tvExpireDate);
        tvTotalAmount = (TextView) findViewById(R.id.tvTotalAmount);
        tvProductStatus = (TextView) findViewById(R.id.tvProductStatus);
        tvAddProduct = (TextView) findViewById(R.id.tvCreateItemReturnNote);
        tvFinish = (TextView) findViewById(R.id.tvFinish);
        tvMerchantSelect = (TextView) findViewById(R.id.tvMerchantSelect);
        tvTotalDicountAmount = (TextView) findViewById(R.id.tvTotalDicountAmount);
        tvNetTotalAmount = (TextView) findViewById(R.id.tvNetTotalAmount);
        //tvTotalOutstandingAmount = (TextView) findViewById(R.id.tvTotalOutstandingAmount);
        //tvInvoice = (TextView) findViewById(R.id.tvInvoice);

        //EditText
        etQuantity = (EditText) findViewById(R.id.etQuantity);
        //etDiscount = (EditText) findViewById(R.id.etDiscount);

        //RecyclerView
        rvProductList = (RecyclerView) findViewById(R.id.rvProductList);

        //Layouts
        llMerchantName = (LinearLayout) findViewById(R.id.llMerchantName);
        llInvoice = (LinearLayout) findViewById(R.id.llInvoice);
    }

    public void deleteItem(ReturnInventoryLineItem returnInventoryLineItem, int position) {
        returnInventoryLineItemArrayList.remove(returnInventoryLineItem);
        returnInventoryAdapter.notifyDataSetChanged();

        totalReturnAmount = totalReturnAmount - returnInventoryLineItem.getTotalAmount();
        totalOutstanding = totalReturnAmount;
        totalDiscount = totalDiscount - returnInventoryLineItem.getDiscountedPrice();

        tvTotalDicountAmount.setText(String.format("Rs. %.2f", totalDiscount));
        tvNetTotalAmount.setText(String.format("Rs. %.2f", totalReturnAmount));
        // tvTotalOutstandingAmount.setText(String.format("Rs. %.2f",totalOutstanding));

    }

    public void setItemData(ReturnInventoryLineItem returnInventoryLineItem, int position) {
        productId = returnInventoryLineItem.getProductId();
        selectedName = returnInventoryLineItem.getProductName();
        selectedProductId = returnInventoryLineItem.getProductId();
        tvProductName.setText(returnInventoryLineItem.getProductName());
        loadProductBatch(productBatchArrayList);
        setItemData = true;
        pos = position;
        tvProductName.setEnabled(false);
        tvProductName.setTextColor(Color.BLACK);
        setDataQty = returnInventoryLineItem.getQuantity();
    }

    private ArrayList<String> getInvoiceDetails() {
        ArrayList<String> invoiceItems = new ArrayList<>();
        SQLiteDatabase database = db.getWritableDatabase();
        String query = null;
        if (setItemData) {
            query = "SELECT * FROM " + TABLE_INVOICE_ITEM + " AS i " +
                    "INNER JOIN " + TABLE_INVOICE_LINE_ITEM + " AS il ON i." + COL_INVOICE_ITEM_InvoiceId + " = il." + COL_INVOICE_LINE_ITEM_invoiceID +
                    "WHERE il." + COL_INVOICE_LINE_ITEM_productID + " =" + selectedProductId;
            System.out.println("CUST Query");
        } else {
            query = "SELECT * FROM " + TABLE_INVOICE_ITEM;
            System.out.println("Default Query");
        }

        Cursor cursor = database.rawQuery(query, null);

        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex(COL_INVOICE_ITEM_InvoiceId));
            invoiceItems.add(id);
        }
        return invoiceItems;
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

        builder = new AlertDialog.Builder(ReturnActivty.this);
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
        alertDialog.setCanceledOnTouchOutside(false);
       /* tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });*/

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

                /*if(id==1){
                    Intent intent = new Intent(getApplicationContext(), LoginSyncActivityDrawer.class);
                    intent.putExtra("userName",userName);
                    intent.putExtra("userId",userId);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }*/

                System.out.println(" intent id "+IntentId);
                if (IntentId!=0){
                    Intent intent = new Intent(getApplicationContext(), ActivityCancelSalesOrderContinue.class);
                    intent.putExtra("orderList", orderList);
                    intent.putExtra("id", 2);
                    intent.putExtra("responseSalesOrderId", 0);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                    ReturnActivty.this.finish();
                }else{
                    Intent intent = new Intent(getApplicationContext(), SalesOrderOfflineActivity.class);
                    ReturnActivty.this.finish();
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }




            }
        });

    }

    private int getProductQuantitFromDatabase(int productID, int batchID) {
        String query = null,querySalesRepInventory=null;
        int dbVehicelQty = 0,dbSaleRepQty=0;
        if (SalesRepType.equalsIgnoreCase("R")) {
            query = "SELECT  " + COL_VEHICLE_INVENTORY_LoadQuantity + " FROM " + TABLE_VEHICALE_INVENTORY + " WHERE " +
                    COL_VEHICLE_INVENTORY_ProductId + " = " + productID +
                    " AND " + COL_VEHICLE_INVENTORY_BatchID + "= " + batchID;

            querySalesRepInventory = "SELECT  " + COL_SRI_QTY_INHAND + " FROM " + TABLE_SALESREP_INVENTORY + " WHERE " +
                    COL_SRI_PRODUCT_ID + " = " + productID +
                    " AND " + COL_SRI_BATCH_ID + "= " + batchID;
        } else {
            query = "SELECT  " + COL_SRI_QTY_INHAND + " FROM " + TABLE_SALESREP_INVENTORY + " WHERE " +
                    COL_SRI_PRODUCT_ID + " = " + productID +
                    " AND " + COL_SRI_BATCH_ID + "= " + batchID;
        }

        Cursor cursor = null;
        SQLiteDatabase database = db.getReadableDatabase();

        cursor = database.rawQuery(query, null);
        if (cursor.getCount() == 0) {
            System.out.println(" ============= No data related to this product ");

        } else {
            while (cursor.moveToNext()) {
                dbVehicelQty = cursor.getInt(0);
                System.out.println("Database Quantity Vehicle Inventory: " + dbVehicelQty);
            }
        }

        return dbVehicelQty;
    }

    private int getSalesRepProductQuantitFromDatabase(int productID, int batchID) {
        String query = null;
        int dbVehicelQty = 0;
        if (SalesRepType.equalsIgnoreCase("R")) {

            query = "SELECT  " + COL_SRI_QTY_INHAND + " FROM " + TABLE_SALESREP_INVENTORY + " WHERE " +
                    COL_SRI_PRODUCT_ID + " = " + productID +
                    " AND " + COL_SRI_BATCH_ID + "= " + batchID;
        }

        Cursor cursor = null;
        SQLiteDatabase database = db.getReadableDatabase();

        cursor = database.rawQuery(query, null);
        if (cursor.getCount() == 0) {
            System.out.println(" ============= No data related to this product in salesrep inventory");

        } else {
            while (cursor.moveToNext()) {
                dbVehicelQty = cursor.getInt(0);
                System.out.println("Database Quantity Salesrep Inventory: " + dbVehicelQty);
            }
        }

        return dbVehicelQty;
    }

}
