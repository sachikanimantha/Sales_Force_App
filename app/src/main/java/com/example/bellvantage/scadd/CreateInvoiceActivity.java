package com.example.bellvantage.scadd;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bellvantage.scadd.Adapters.AdapterForInvoiceLineItem;
import com.example.bellvantage.scadd.Adapters.AdapterForReturnLineItem;
import com.example.bellvantage.scadd.DB.DbHelper;
import com.example.bellvantage.scadd.Utils.DateManager;
import com.example.bellvantage.scadd.Utils.SessionManager;
import com.example.bellvantage.scadd.Utils.SyncManager;
import com.example.bellvantage.scadd.Utils.UtilityManager;
import com.example.bellvantage.scadd.domains.ReviseSalesOrderList;
import com.example.bellvantage.scadd.swf.Cash;
import com.example.bellvantage.scadd.swf.InvoiceJson;
import com.example.bellvantage.scadd.swf.InvoiceLineItem;
import com.example.bellvantage.scadd.swf.InvoiceLineItemJson;
import com.example.bellvantage.scadd.swf.InvoiceList2;
import com.example.bellvantage.scadd.swf.InvoiceUtilization;
import com.example.bellvantage.scadd.swf.LoginUser;
import com.example.bellvantage.scadd.swf.OrderList;
import com.example.bellvantage.scadd.swf.Payment;
import com.example.bellvantage.scadd.swf.ReturnInventory;
import com.example.bellvantage.scadd.swf.ReturnInventoryLineItem;
import com.example.bellvantage.scadd.swf.SalesOrderSync;
import com.example.bellvantage.scadd.web.HTTPPaths;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

import static com.example.bellvantage.scadd.DB.DbHelper.COL_DISTRIBUTOR_DISTRIBUTOR_ID;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_INVOICE_JSON_InvoiceNumber;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_INVOICE_JSON_IsSync;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_INVOICE_JSON_JsonString;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_RETURN_INVENTORY_JSON_JsonString;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_RETURN_INVENTORY_JSON_creditNoteNo;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_RETURN_INVENTORY_merchantId;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_RETURN_INVENTORY_totalOutstanding;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SALES_ORDER_InvoiceNumber;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SALES_ORDER_IsSync;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SALES_ORDER_SaleStatus;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SALES_ORDER_SalesOrderId;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SALES_REP_SalesRepId;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SYNC_ID_NEXT_ID;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SYNC_ID_TABLE_ID;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_DISTRIBUTOR;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_INVOICE_ITEM;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_INVOICE_JSON;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_INVOICE_LINE_ITEM;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_INVOICE_UTILIZATION;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_MERCHANT;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_PATH;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_PATH_DELIVERYPATHID;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_RETURN_INVENTORY;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_RETURN_INVENTORY_JSON;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_SALES_INVOICE_ID;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_SALES_ORDER;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_SALES_REP;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_SYNC_ID;
import static com.example.bellvantage.scadd.DB.DbHelper.TBL_MERCHANT_CREDIT_DAYS;
import static com.example.bellvantage.scadd.DB.DbHelper.TBL_MERCHANT_IS_CREDIT;
import static com.example.bellvantage.scadd.DB.DbHelper.TBL_MERCHANT_MERCHANT_ID;

public class CreateInvoiceActivity extends AppCompatActivity {

    //Views
    Toolbar toolbar;
    TextView    tvMerchantName,tvMerchantAddress,tvDeliveryDate,tvArea,tvDistributerName,
                tvDistributerAddress,tvInvoiceGrandTotal,tvSubTotal,tvReturnGrandTotal,
                tvCreditNotes,tvFreeIssued,tvSalesRepName,tvSalesRepAddress,tvInvoiceTitle,
                tvPreviousReturns,tvTotalInvoiceValue,tvCreateInvoice,tvInvoiceNumber,tvTotalVat,
                tvPaymentType,tvCashtype;
    RecyclerView rvInvoiceLineItem,rvReturnLineItem;
    LinearLayout llReturnTitles,llReturnLineItem,llVat,llCash;
    TextInputLayout tilCreditDays;
    EditText etCreditDays;

    double invoiceGrandTotal =0,returnGrandTotal=0,freeIssuedDiscount=0,previousReturns=0,totalInvoiceValue=0,InvoiceAmount=0,totalVat=0;
    OrderList orderList;
    DbHelper db;
    DateManager dateManager;
    SyncManager syncManager ;

    int buildingNo ;
    String address1;
    String address2;
    String city,contactNumber,salesrepContactNumber;
    String salesRepName = null,salesrepType=null;

    String syncDate,creditDays;
    int isVat, isCredit,paymentId;
    int credtDays;

    boolean isClick=false;

    //SharedPreferences
    SharedPreferences spref ;
    SharedPreferences.Editor editor;
    int NextInvoiceNo=0,invoiceNumber=0,previousInvoiceNo=0;

    AdapterForInvoiceLineItem adapterForInvoiceLineItem;
    AdapterForReturnLineItem adapterForReturnLineItem;


    ArrayList<ReviseSalesOrderList> lineItemArrayList = new ArrayList<>();
    ArrayList<ReturnInventoryLineItem> returnInventoryLineItemsArrayList = new ArrayList<>();

    ArrayList<ReturnInventory> outstandingArrayList = new ArrayList<>();
    ArrayList<InvoiceLineItemJson> jsonInvoiceLineItem = new ArrayList<>();
    ArrayList<InvoiceUtilization> invoiceUtilizationArrayList = new ArrayList<>();

    String enterdUser;

    //Spiners

    //Payment type spinner
    ArrayList<Payment> paymnetTypeArrayList = new ArrayList<>();
    ArrayList<String> paymentTypes = new ArrayList<>();
    SpinnerDialog spdPaymentTypes;
    int paymnetTypeId;

    //Cash type spinner
    ArrayList<Cash> cashTypeArrayList = new ArrayList<>();
    ArrayList<String> cashTypes = new ArrayList<>();
    SpinnerDialog spdCashTypes;
    int cashTypeId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_invoice);

        initializeViews();

        //Spinners
        //Payment Type DialogsBox
        genaratePaymentTypes();
        loadPaymentTypes(paymnetTypeArrayList);

        tvPaymentType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spdPaymentTypes.showSpinerDialog();
            }
        });

        //Cash Type DialogsBox
        genarateCashTypes();
        loadCashTypes(cashTypeArrayList);

        tvCashtype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spdCashTypes.showSpinerDialog();
            }
        });


        db = new DbHelper(CreateInvoiceActivity.this);
        dateManager =new DateManager();

        syncManager = new SyncManager(CreateInvoiceActivity.this);
        toolbar.setTitle("Create Invoice");

        //SharedPreferences
        Gson gson = new Gson();
        String getJson = SessionManager.pref.getString("user", "");
        LoginUser prefUser = gson.fromJson(getJson, LoginUser.class);
        enterdUser =prefUser.getUserName();

       //Get details from SharedPreferences
        spref = getSharedPreferences("invoice.conf", Context.MODE_PRIVATE);
        editor =spref.edit();
        NextInvoiceNo = spref.getInt("NextInvoiceNo",-1);
        salesrepType = spref.getString("SalesRepType","");


        System.out.println("SalesRepType "+salesrepType);

        if (salesrepType.equalsIgnoreCase("P")){
            tvCreateInvoice.setVisibility(View.GONE);
        }else{
            tvCreateInvoice.setVisibility(View.VISIBLE);
        }

        try{
            previousInvoiceNo = getPreviousInvoiceNo();
            System.out.println("Previous Invoice Number try : "+ previousInvoiceNo);
        }catch (Exception e){
            System.out.println("Error at Invoice Create: " + e.getMessage());
        }

        //Date
        long tod_long = (new DateManager()).todayMillsec();
        syncDate = "/Date(" + tod_long + ")/";


        if (getIntent().getSerializableExtra("orderList")!=null){
            orderList = (OrderList) getIntent().getSerializableExtra("orderList");
        }

        //Get Total Vat
        totalVat = orderList.getTotalVAT();
        tvTotalVat.setText(String.format("%.2f",totalVat));

        if (orderList.getIsVat()==1){
            tvInvoiceTitle.setText("TAX INVOICE");
            llVat.setVisibility(View.VISIBLE);
        }else{
            tvInvoiceTitle.setText("SALES INVOICE");
        }

        //Payments
        getPaymentType(orderList.getMerchantId());

        if (paymnetTypeId==1){//credit
            tilCreditDays.setVisibility(View.VISIBLE);
            tilCreditDays.getEditText().setText(""+credtDays);
            tvPaymentType.setText("Credit");
            //tvCashtype.setText("Credit");
            tvCashtype.setVisibility(View.VISIBLE);
            isCredit=1;
        }else if (paymnetTypeId==0){//cash
            tilCreditDays.setVisibility(View.GONE);
            tvPaymentType.setText("Cash");
            isCredit=0;
            paymentId=0;

        }else{
            etCreditDays.setVisibility(View.GONE);
            tvCashtype.setVisibility(View.GONE);
            isCredit=0;
            paymentId=0;
        }

        llReturnTitles.setVisibility(View.GONE);
        llReturnLineItem.setVisibility(View.GONE);
        tvCreditNotes.setVisibility(View.GONE);

        try{
            //Set Merchant Details
            if (orderList.getMerchntName().isEmpty()){
                tvMerchantName.setText("Direct Sale");
            }else{
                tvMerchantName.setText(orderList.getMerchntName());

            }
            String merchantAdrress = getMerchantAddress(orderList.getMerchantId());

            if (!merchantAdrress.equals("")){
                tvMerchantAddress.setText(merchantAdrress);
            }

        }catch (Exception e){
            System.out.println("Error at Print Address: "+e.getMessage());
        }

        //set Delivery Date
        String deliveryDate = orderList.getEstimateDeliveryDate();
        int start = deliveryDate.indexOf("(");
        int end = deliveryDate.indexOf(")");
        String iDate = deliveryDate.substring(start+1,end);
        tvDeliveryDate.setText(dateManager.getDateAccordingToMilliSeconds(Long.parseLong(iDate), "yyyy-MM-dd"));
        
        //set Area
//        String area = getArea(orderList.getMerchantId());
//        if (!area.equals("")){
//            tvArea.setText(area);
//        }else{
            tvArea.setText("Not Available");
        //}

        //set Distributor
        getDistributerDetails(orderList.getDitributorId());

        //set SalesRep
        getSalesrepDetails(orderList.getSalesRepId());

        //set Invoice Line Item
        lineItemArrayList = syncManager.getReviseOrderLineItemsBySalesOrderId(orderList.getSalesOrderId());
        loadRecyclerView(lineItemArrayList);

        //setTotal Invoice Values
        for (int i=0;i<lineItemArrayList.size();i++){
            invoiceGrandTotal +=lineItemArrayList.get(i).getUnitSellingPrice()*lineItemArrayList.get(i).getQuantity();
            freeIssuedDiscount += lineItemArrayList.get(i).getFreeQuantity()*lineItemArrayList.get(i).getUnitSellingPrice();
            InvoiceAmount = invoiceGrandTotal+freeIssuedDiscount;
        }

        tvInvoiceGrandTotal.setText(String.format("%.2f", InvoiceAmount));
        tvSubTotal.setText(String.format("%.2f", invoiceGrandTotal));
        tvFreeIssued.setText(String.format("%.2f", freeIssuedDiscount));

        //Set ReturnNotes
        returnInventoryLineItemsArrayList =syncManager.getCreditNote(orderList.getMerchantId());
        if (returnInventoryLineItemsArrayList.size()>0){
            llReturnTitles.setVisibility(View.VISIBLE);
            llReturnLineItem.setVisibility(View.VISIBLE);
            tvCreditNotes.setVisibility(View.VISIBLE);
            loadReturnInventoryRecyclerView(returnInventoryLineItemsArrayList);
        }

        //setTotal Values For ReturnInventory
        for (int i=0;i<returnInventoryLineItemsArrayList.size();i++){
            returnGrandTotal +=returnInventoryLineItemsArrayList.get(i).getUnitPrice()*returnInventoryLineItemsArrayList.get(i).getQuantity();
        }
        tvReturnGrandTotal.setText(String.format("%.2f", returnGrandTotal));

        //Genarate Invoice Number
       // invoiceNumber =  Integer.parseInt((orderList.getSalesRepId()+1000)+""+NextInvoiceNo);
       // invoiceNumber =  NextInvoiceNo;
        System.out.println("Previous Invoice Number: "+ previousInvoiceNo);
        System.out.println("Next Invoice Number: "+ NextInvoiceNo);
        if (NextInvoiceNo>previousInvoiceNo){
            NextInvoiceNo+=1;
            invoiceNumber = NextInvoiceNo;
            System.out.println("Next invoice number is grater than previous invoice number");
        }else{
            previousInvoiceNo+=1;
            invoiceNumber= previousInvoiceNo;
            System.out.println("previous invoice number is grater than Next invoice number");
        }

        //Disable Create Invoice button after create invoice
        if (isClick){
            tvCreateInvoice.setVisibility(View.GONE);
        }


        tvInvoiceNumber.setText(""+new UtilityManager().genarateInvoiceId(invoiceNumber,orderList.getSalesRepId()));

        getPreviousCreditAmount(orderList.getMerchantId(),invoiceGrandTotal);

        //Create Invoice
        tvCreateInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("Payment Type id "+ paymnetTypeId);

                credtDays = 0;
                String paymnet = tvPaymentType.getText().toString();
                String cash = tvCashtype.getText().toString();
                creditDays = etCreditDays.getText().toString();

                if (paymnet.equalsIgnoreCase("Select a Payment Type")){
                    displayStatusMessage("Please Select a Payment Type",3,1);
                    return;
                }
                if (paymnetTypeId==1 && creditDays.isEmpty()){
                    displayStatusMessage("Please Enter Credit Days",3,1);
                    return;
                }
                try{
                    credtDays = Integer.parseInt(creditDays);
                    System.out.println("Credit Days: "+ credtDays);
                }catch (Exception e){
                    System.out.println("Error while parsing credit days at Create Invoice");
                }
                if (paymnetTypeId==1 && credtDays>45){
                    displayStatusMessage("Please Enter Credit Days between 0 to 45",3,1);
                    return;
                }
                if (paymnetTypeId==1 && cash.equalsIgnoreCase("Select a Cash Type")){
                    displayStatusMessage("Please Select a Cash Type",3,1);
                    return;
                }

                displayCreateInvoiceMessege("Do you want to create invoice? ",3);
            }
        });

    }

    private void getPaymentType(int merchantId) {
        SQLiteDatabase database = db.getWritableDatabase();
        Cursor cursor = null;
        String sql = "SELECT * FROM "+TABLE_MERCHANT + " WHERE "+TBL_MERCHANT_MERCHANT_ID +" = "+merchantId;
        cursor = database.rawQuery(sql,null);
        if (cursor.getCount()>0){
            try{
                while (cursor.moveToNext()){
                    paymnetTypeId = cursor.getInt(cursor.getColumnIndex(TBL_MERCHANT_IS_CREDIT));
                    credtDays = cursor.getInt(cursor.getColumnIndex(TBL_MERCHANT_CREDIT_DAYS));
                }
            }catch (Exception e){
                System.out.println("Error when get payment type related to the customer ");
                e.printStackTrace();
            }
        }
    }

    private void loadCashTypes(final ArrayList<Cash> cashTypeArrayList) {
        cashTypes.clear();
        for (int i = 0; i < cashTypeArrayList.size(); i++) {
            cashTypes.add(cashTypeArrayList.get(i).getCashtype());
        }
        spdCashTypes = new SpinnerDialog(CreateInvoiceActivity.this, cashTypes, "Select a Cash Type", R.style.DialogAnimations_SmileWindow);

        spdCashTypes.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {

                tvCashtype.setText(item);
                for (int i = 0; i<cashTypeArrayList.size();i++){
                    if (item.equalsIgnoreCase(cashTypeArrayList.get(i).getCashtype())){
                        cashTypeId = cashTypeArrayList.get(i).getCashId();
                        paymentId=cashTypeId;
                    }
                }
            }
        });
    }

    private void genarateCashTypes() {
        Cash cheque = new Cash(1,"Cheque");
        Cash cash = new Cash(0,"Cash");
        cashTypeArrayList.add(cheque);
        cashTypeArrayList.add(cash);
    }

    private void genaratePaymentTypes() {
        Payment credit = new Payment(1,"Credit");
        Payment cash = new Payment(0,"Cash");
        paymnetTypeArrayList.add(credit);
        paymnetTypeArrayList.add(cash);

    }

    private void loadPaymentTypes(final ArrayList<Payment> paymnetTypeArrayList) {
        paymentTypes.clear();
        for (int i = 0; i < paymnetTypeArrayList.size(); i++) {
            paymentTypes.add(paymnetTypeArrayList.get(i).getPaymnetType());
        }
        spdPaymentTypes = new SpinnerDialog(CreateInvoiceActivity.this, paymentTypes, "Select a Payment Type", R.style.DialogAnimations_SmileWindow);

        spdPaymentTypes.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {

                tvPaymentType.setText(item);
                for (int i = 0; i<paymnetTypeArrayList.size();i++){
                        if (item.equalsIgnoreCase(paymnetTypeArrayList.get(i).getPaymnetType())){
                        paymnetTypeId = paymnetTypeArrayList.get(i).getPaymnetId();
                            System.out.println("@@@ Payment Type ID: "+paymnetTypeId);
                    }
                }

                System.out.println("@@@ Payment Type ID1: "+paymnetTypeId);
                if (paymnetTypeId==1){//credit
                    tilCreditDays.setVisibility(View.VISIBLE);
                    tvCashtype.setVisibility(View.VISIBLE);
                    isCredit=1;
                }else if (paymnetTypeId==0){//cash
                    tilCreditDays.setVisibility(View.GONE);
                    tvCashtype.setVisibility(View.GONE);
                    isCredit=0;
                    paymentId=0;

                }else{
                    etCreditDays.setVisibility(View.GONE);
                    tvCashtype.setVisibility(View.GONE);
                    isCredit=0;
                    paymentId=0;
                }

            }
        });
        
    }

    private int getPreviousInvoiceNo() {

            int id=0;
            SQLiteDatabase database = db.getWritableDatabase();
            String query = "SELECT * FROM "+TABLE_SYNC_ID+ " WHERE "+ COL_SYNC_ID_TABLE_ID + " = " + TABLE_SALES_INVOICE_ID;
            Cursor cursor = database.rawQuery(query,null);

            while (cursor.moveToNext()){
                id=cursor.getInt(cursor.getColumnIndex(COL_SYNC_ID_NEXT_ID));
            }

            System.out.println("GetPreviousInvoiceNo  Previous Invoice Number: " + id);
            return id;
    }

    private void updateSalesOrderStatus(int orderId, int salesOrderId, double invoiceId) {
        SQLiteDatabase database = db.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_SALES_ORDER_SaleStatus,4);
        cv.put(COL_SALES_ORDER_IsSync, 0);
        cv.put(COL_SALES_ORDER_InvoiceNumber, invoiceId);
        int afectedRows = database.update(TABLE_SALES_ORDER, cv,COL_SALES_ORDER_SalesOrderId+ " = ? " ,new String[]{""+orderId});
        if (afectedRows>0){
            System.out.println("============================ Sales Order status updated as 4 =================================");
        }
        //Update Status to server
        syncToSqlite(orderId,4);
    }

    private void getPreviousCreditAmount(int merchantId,double invoiceAmount) {

        Cursor cursor = null;
        SQLiteDatabase database = db.getReadableDatabase();
        double balance = invoiceAmount;

        String query = "SELECT  * FROM " + TABLE_RETURN_INVENTORY+ " WHERE " +
                COL_RETURN_INVENTORY_merchantId+ " = " + merchantId +" AND " + COL_RETURN_INVENTORY_totalOutstanding + " > 0 ";

        cursor = database.rawQuery(query, null);
        if (cursor.getCount() == 0) {
            System.out.println(" ============= No data related to the " + merchantId);

        } else {
            while (cursor.moveToNext()) {
                int  CreditNoteNo = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_RETURN_INVENTORY_creditNoteNo));
                double  TotalOutstanding = cursor.getDouble(cursor.getColumnIndex(DbHelper.COL_RETURN_INVENTORY_totalOutstanding));
                int invoiceId = new UtilityManager().genarateInvoiceId(invoiceNumber,orderList.getSalesRepId());
                System.out.println("Invoice Id "+ invoiceId);
                System.out.println("@@@@@@@@@@@@@@@@@@@@@@\norderlist.size - "+orderList.getMerchantId());
                if(balance!=0){
                    if (balance>TotalOutstanding){
                        //updateOutstnding(CreditNoteNo,0);
                        ReturnInventory returnInventory =new ReturnInventory(CreditNoteNo,0);
                        outstandingArrayList.add(returnInventory);
                        balance -= TotalOutstanding;
                        previousReturns+=TotalOutstanding;

                        //invoiceUtilizationArrayList.add(new InvoiceUtilization(invoiceNumber,CreditNoteNo,TotalOutstanding,enterdUser,new DateManager().getTodayDateString(),0));
                        invoiceUtilizationArrayList.add(new InvoiceUtilization(invoiceId,CreditNoteNo,TotalOutstanding,enterdUser,new DateManager().getTodayDateString(),0));
                    }else{
                        double outstandingBalance = TotalOutstanding-balance;
                        previousReturns+=balance;
                        //updateOutstnding(CreditNoteNo,outstandingBalance);
                        ReturnInventory returnInventory =new ReturnInventory(CreditNoteNo,outstandingBalance);
                        invoiceUtilizationArrayList.add(new InvoiceUtilization(invoiceId,CreditNoteNo,balance,enterdUser,new DateManager().getTodayDateString(),0));
                        outstandingArrayList.add(returnInventory);
                        balance=0;
                        break;
                    }
                }
            }
        }

        tvPreviousReturns.setText(String.format("%.2f",previousReturns));
        totalInvoiceValue=invoiceGrandTotal-previousReturns;
        tvTotalInvoiceValue.setText(String.format("%.2f",totalInvoiceValue));

    }

    private void updateOutstnding() {

        for (int i=0;i<outstandingArrayList.size();i++){

            int creditNoteNo = outstandingArrayList.get(i).getCreditNoteNo();
            double outstandingAmount = outstandingArrayList.get(i).getTotalOutstanding();

            System.out.println("Return Note number: "+ creditNoteNo+ " OutstandingAmount: "+ outstandingAmount);

            ContentValues cv = new ContentValues();
            cv.put(COL_RETURN_INVENTORY_totalOutstanding,outstandingAmount);
            SQLiteDatabase database = db.getWritableDatabase();
            int afectedRows = database.update(TABLE_RETURN_INVENTORY,cv,COL_RETURN_INVENTORY_totalOutstanding +" + ? " ,new String[]{""+creditNoteNo});
            if(afectedRows>0){
                System.out.println("Outstnding Amount is updated at row "+creditNoteNo +"=== Amount "+ outstandingAmount );
                updateOutstandingOfJson(creditNoteNo,outstandingAmount);
            }
        }


    }

    private void updateOutstandingOfJson(int creditNoteNo, double outstandingAmount) {
        System.out.println("Credit Note Number at updateOutstandingOfJson: "+ creditNoteNo);
        String newCreditNoteNUmber = UtilityManager.truncateInvoiceId(creditNoteNo);
        SQLiteDatabase database = db.getWritableDatabase();
        String sql =    "SELECT * FROM "+TABLE_RETURN_INVENTORY_JSON+
                        " WHERE "+COL_RETURN_INVENTORY_JSON_creditNoteNo + " = "+newCreditNoteNUmber;

        Cursor cursor = database.rawQuery(sql,null);
        if (cursor.getCount()>0){
            while (cursor.moveToNext()){
                String retunJson = cursor.getString(cursor.getColumnIndex(COL_RETURN_INVENTORY_JSON_JsonString));
                System.out.println("Json" + retunJson);
                try {
                    JSONObject json = new JSONObject(retunJson);
                    json.put("TotalOutstanding", outstandingAmount);
                    System.out.println("Updated Json" + json);
                    ContentValues cv = new ContentValues();
                    cv.put(COL_RETURN_INVENTORY_JSON_JsonString,json.toString());
                    int afectedRows = database.update(TABLE_RETURN_INVENTORY_JSON,cv,COL_RETURN_INVENTORY_JSON_creditNoteNo +" + ? " ,new String[]{""+newCreditNoteNUmber});
                    if(afectedRows>0){
                        System.out.println("Return json updated relatedt to "+newCreditNoteNUmber +"=== Amount "+ outstandingAmount );
                    }

                } catch (JSONException e) {
                    System.out.println("==== Error at converting Return Note Json String into Json Object =====");
                    e.printStackTrace();
                }
            }

        }else {
            System.out.println("No data related to the "+ creditNoteNo);
        }
    }

    private void getDistributerDetails(int ditributorId) {
        String address1,address2,address= null;
        String ditstributerName = null;;
        Cursor cursor = null;
        SQLiteDatabase database = db.getReadableDatabase();

        String query = "SELECT  * FROM " + TABLE_DISTRIBUTOR+ " WHERE " +
                COL_DISTRIBUTOR_DISTRIBUTOR_ID+ " = " + ditributorId ;

        cursor = database.rawQuery(query, null);
        if (cursor.getCount() == 0) {
            System.out.println(" ============= No data related to the " + ditributorId);

        } else {
            while (cursor.moveToNext()) {
                ditstributerName = cursor.getString(cursor.getColumnIndex(DbHelper.COL_DISTRIBUTOR_DISTRIBUTOR_NAME));
                address1 = cursor.getString(cursor.getColumnIndex(DbHelper.COL_DISTRIBUTOR_ADDRESS_1));
                //address2 = cursor.getString(cursor.getColumnIndex(DbHelper.COL_DISTRIBUTOR_ADDRESS_2));
                address = address1 ;
            }
        }
        tvDistributerName.setText(ditstributerName);
        tvDistributerAddress.setText(address);
    }

    private void getSalesrepDetails(int saleRepId) {
        String address1,address2,address= null;

        Cursor cursor = null;
        SQLiteDatabase database = db.getReadableDatabase();

        String query = "SELECT  * FROM " + TABLE_SALES_REP+ " WHERE " +
                COL_SALES_REP_SalesRepId+ " = " + saleRepId ;

        cursor = database.rawQuery(query, null);
        if (cursor.getCount() == 0) {
            System.out.println(" ============= No data related to the " + saleRepId);

        } else {
            while (cursor.moveToNext()) {
                salesRepName = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_REP_SalesRepName));
                salesrepContactNumber = cursor.getString(cursor.getColumnIndex(DbHelper.COL_SALES_REP_ContactNo));
                //address2 = cursor.getString(cursor.getColumnIndex(DbHelper.COL_DISTRIBUTOR_ADDRESS_2));
                address = salesrepContactNumber ;
            }
        }
        tvSalesRepName.setText(salesRepName);
        tvSalesRepAddress.setText(address);
    }

    private String getArea(int merchantId) {
        String area = null;
        int areaCode = 0;
        Cursor cursor = null;
        SQLiteDatabase database = db.getReadableDatabase();

        String query = "SELECT  * FROM " + TABLE_MERCHANT+ " WHERE " +
                TBL_MERCHANT_MERCHANT_ID+ " = " + merchantId ;

        cursor = database.rawQuery(query, null);
        if (cursor.getCount() == 0) {
            System.out.println(" ============= No data related to the " + merchantId);

        } else {
            while (cursor.moveToNext()) {
                areaCode = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_AREA_CODE));
            }
        }

        String AreaQuery = "SELECT  * FROM " + TABLE_PATH+ " WHERE " +
                TABLE_PATH_DELIVERYPATHID+ " = " + areaCode ;

        cursor = database.rawQuery(AreaQuery, null);
        if (cursor.getCount() == 0) {
            System.out.println(" ============= No data related to the " + areaCode);

        } else {
            while (cursor.moveToNext()) {
                area = cursor.getString(cursor.getColumnIndex(DbHelper.TABLE_AREA_AREA_NAME));
            }
        }
        
        return area;
    }

    private String getMerchantAddress(int merchantId) {
        String address= null;
        Cursor cursor = null;
        SQLiteDatabase database = db.getReadableDatabase();

        String query = "SELECT  * FROM " + TABLE_MERCHANT+ " WHERE " +
                TBL_MERCHANT_MERCHANT_ID+ " = " + merchantId ;

        cursor = database.rawQuery(query, null);
        if (cursor.getCount() == 0) {
            System.out.println(" ============= No data related to the " + merchantId);

        } else {
            while (cursor.moveToNext()) {
                buildingNo = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_BUILDING_NO));
                address1 = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_ADDRESS_1));
                address2 = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_ADDRESS_2));
                city = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_CITY));
                contactNumber = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_CONTACT_NO1));

                address = buildingNo + ", "+ address1 +  ",\n" + address2 + ", " + city + ".";
            }
        }

        return address;
    }

    private void initializeViews() {
        toolbar = (Toolbar) findViewById(R.id.tb_main);

        //TextViews
        tvMerchantName = (TextView) findViewById(R.id.tvMerchantName);
        tvMerchantAddress = (TextView) findViewById(R.id.tvMerchantAddress);
        tvDeliveryDate = (TextView) findViewById(R.id.tvDeliveryDate);
        tvArea = (TextView) findViewById(R.id.tvArea);
        tvDistributerName = (TextView) findViewById(R.id.tvDistributerName);
        tvDistributerAddress = (TextView) findViewById(R.id.tvDistributerAddress);
        tvInvoiceGrandTotal = (TextView) findViewById(R.id.tvInvoiceGrandTotal);
        tvSubTotal = (TextView) findViewById(R.id.tvSubTotal);
        tvReturnGrandTotal = (TextView) findViewById(R.id.tvReturnGrandTotal);
        tvCreditNotes = (TextView) findViewById(R.id.tvCreditNotes);
        tvFreeIssued = (TextView) findViewById(R.id.tvFreeIssued);
        tvSalesRepName = (TextView) findViewById(R.id.tvSalesRepName);
        tvSalesRepAddress = (TextView) findViewById(R.id.tvSalesRepAddress);
        tvPreviousReturns = (TextView) findViewById(R.id.tvPreviousReturns);
        tvTotalInvoiceValue = (TextView) findViewById(R.id.tvTotalInvoiceValue);
        tvCreateInvoice = (TextView) findViewById(R.id.tvCreateInvoice);
        tvInvoiceTitle = (TextView) findViewById(R.id.tvInvoiceTitle);
        tvInvoiceNumber = (TextView) findViewById(R.id.tvInvoiceNumber);
        tvTotalVat = (TextView) findViewById(R.id.tvTotalVat);
        tvPaymentType = (TextView) findViewById(R.id.tvPaymentType);
        tvCashtype = (TextView) findViewById(R.id.tvCashType);

        //RecyclerViews
        rvInvoiceLineItem = (RecyclerView) findViewById(R.id.rvInvoiceLineItem);
        rvReturnLineItem = (RecyclerView) findViewById(R.id.rvReturnLineItem);

        //Layouts
        llReturnLineItem = (LinearLayout) findViewById(R.id.llReturnLineItem);
        llReturnTitles = (LinearLayout) findViewById(R.id.llReturnTitles);
        llVat = (LinearLayout) findViewById(R.id.llVat);

        //EditText
        etCreditDays = (EditText) findViewById(R.id.etCreditDays);

        //TextInputLayouts
        tilCreditDays = (TextInputLayout) findViewById(R.id.tilCreditDays);


    }

    private void loadRecyclerView(ArrayList<ReviseSalesOrderList> lineItemArrayList) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL,
                false
        );

        rvInvoiceLineItem.setLayoutManager(layoutManager);
        rvInvoiceLineItem.setHasFixedSize(true);

        adapterForInvoiceLineItem = new AdapterForInvoiceLineItem(
                lineItemArrayList,
                CreateInvoiceActivity.this
        );
        rvInvoiceLineItem.setAdapter(adapterForInvoiceLineItem);
    }

    private void loadReturnInventoryRecyclerView(ArrayList<ReturnInventoryLineItem> lineItemArrayList) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL,
                false
        );

        rvReturnLineItem.setLayoutManager(layoutManager);
        rvReturnLineItem.setHasFixedSize(true);

        adapterForReturnLineItem = new AdapterForReturnLineItem(
                lineItemArrayList,
                CreateInvoiceActivity.this
        );
        rvReturnLineItem.setAdapter(adapterForReturnLineItem);
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

    private void updateSyncNextID(int invoiceId) {
        SQLiteDatabase database = db.getWritableDatabase();
        ContentValues cv =new ContentValues();
        cv.put(COL_SYNC_ID_NEXT_ID,invoiceId);
        int afectedRows = database.update(TABLE_SYNC_ID,cv,COL_SYNC_ID_TABLE_ID +" = ? " ,new String[]{""+TABLE_SALES_INVOICE_ID});
        if(afectedRows>0){
            System.out.println(TABLE_SYNC_ID + " is Updated "+ " Invoice Number " + invoiceId );
        }
    }

    private void displayStatusMessage(String s, int colorValue, final int messageType) {

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

        builder = new AlertDialog.Builder(CreateInvoiceActivity.this);
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
                if (messageType==0){
                    alertDialog.dismiss();
                    Intent intent = new Intent(getApplicationContext(),OrderRequestActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    CreateInvoiceActivity.this.finish();
                    startActivity(intent);
                }else{
                    alertDialog.dismiss();
                }

             }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //Disable Create Invoice button after create invoice
        if (isClick){
            tvCreateInvoice.setVisibility(View.GONE);
        }

        Intent intent = new Intent(getApplicationContext(),SalesOrderOfflineActivity.class);
        CreateInvoiceActivity.this.finish();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void displayCreateInvoiceMessege(String s, int colorValue) {

        AlertDialog.Builder builder = null;
        View view = null;
        TextView tvOk, tvMessage,tvNo;
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

        builder = new AlertDialog.Builder(CreateInvoiceActivity.this);
        view = getLayoutInflater().inflate(R.layout.layout_for_custom_message_with_ok_cancel, null);

        tvOk = (TextView) view.findViewById(R.id.tvOk);
        tvNo = (TextView) view.findViewById(R.id.tvCancel);
        tvMessage = (TextView) view.findViewById(R.id.tvMessage);
        imageView = (ImageView)view.findViewById(R.id.iv_status_okay);

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
                createSalesOrder();
            }
        });
        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                ContentValues cv = new ContentValues();
                cv.put("SaleStatus",1);
                cv.put("IsSync", 0);
                db.updateTable(orderList.getSalesOrderId()+"",cv,DbHelper.COL_SALES_ORDER_SalesOrderId+" = ?",DbHelper.TABLE_SALES_ORDER);
                syncToSqlite(orderList.getSalesOrderId(),1);
                changeOdrerStatus();
                /*Intent intent = new Intent(getApplicationContext(),SalesOrderOfflineActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                CreateInvoiceActivity.this.finish();
                startActivity(intent);*/




                Intent intent = new Intent(getApplicationContext(),SOR__ListItemActivity_Selected.class);
                intent.putExtra("orderList",orderList);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


                //navigate to print invoce activity
                /*ArrayList<InvoiceLineItem> finalInvoiceLineItemArrayList = new ArrayList<>();
                ArrayList<ReturnInventory> finalReturnInventories = new ArrayList<>();

                finalInvoiceLineItemArrayList = (new SyncManager(getApplicationContext())).getInvoiceLineItem_ACC_invoiceID(new UtilityManager().genarateInvoiceId(invoiceNumber,orderList.getSalesRepId()));
                finalReturnInventories = (new SyncManager(getApplicationContext())).getReturnNoteHeaderData(orderList.getMerchantId(),getDate(Long.parseLong(iDate), "dd/MM/yyyy HH:mm"));

                ArrayList<String> creditnote_array = (new SyncManager(getApplicationContext())).getReturnNoteCreditId_vs_merchant(merchantid,today_mill);
                System.out.println("creditnote_array.size - "+creditnote_array.size());

                launchAtivity(finalInvoiceLineItemArrayList,invoiceLis,finalReturnInventories,creditnote_array);*/

            }
        });

    }

    private void changeOdrerStatus() {
    }

    private void createSalesOrder() {

        editor.putInt("NextInvoiceNo",invoiceNumber);
        editor.apply();
        editor.commit();

        if ( creditDays.isEmpty()||paymnetTypeId ==0){
            creditDays = "0";
        }




        //Update Invoice Number
        updateSyncNextID(invoiceNumber);

        updateSalesOrderStatus(orderList.getSalesOrderId(),new UtilityManager().genarateInvoiceId(invoiceNumber,orderList.getSalesRepId()),orderList.getTotalVAT());
        updateOutstnding();

        int soid = 0;
        try{
            soid= Integer.parseInt(new UtilityManager().truncateInvoiceId(orderList.getSalesOrderId()));
        }catch (Exception e){
            System.out.println("Error at parsing int "+ e.getMessage());
        }

        //Save Invoice details to sqlite Invoice Table
        InvoiceList2 invoiceList2 = new InvoiceList2(
                0,0+"",orderList.getEstimateDeliveryDate(),0,totalInvoiceValue,
                totalInvoiceValue,syncDate,0,1,enterdUser,syncDate,
                syncDate,orderList.getSaleTypeId(),orderList.getSalesRepId(),
                orderList.getDitributorId(),orderList.getMerchantId(),new UtilityManager().genarateInvoiceId(invoiceNumber,orderList.getSalesRepId()),orderList.getTotalVAT(),
                salesRepName,salesrepContactNumber,orderList.getMerchntName(),buildingNo+"",address1,address2,
                city,orderList.getIsCredit(),orderList.getCreditDays(),contactNumber
        );

        /*
        InvoiceList2(int vehicleId, String vehicleNumber, String deliveryDate, int isDelivered, double ousAmount,
                        double invoiceAmount, String syncDate, int isSync, int saleStatus, String enteredUser, String enteredDate,
                        String invoiceDate, int saleTypeId, int salesRepId, int distributorId,
                        int merchantId, int invoiceId, double totalVAT,
                        String salesRepName, String salesRepContactNo,
                        String merchantName, String buildingNo,
                        String address1, String address2,
                        String city, int isCredit,String creditDays,String merchantContactNo)
         */

        ContentValues cv = invoiceList2.getCVfromInvoicelist();
        boolean success = (new DbHelper(getApplicationContext())).insertDataAll(cv, TABLE_INVOICE_ITEM);
        if(success){
            System.out.println(TABLE_INVOICE_ITEM+" data insert to sqlite db");
        }else{
            System.out.println(TABLE_INVOICE_ITEM+" data not insert to sqlite db");
        }

        //Save Invoice Line Items for Invoice Line Item Table

        System.out.println("Invoice Line item size: " +lineItemArrayList.size());

        for (int i=0;i<lineItemArrayList.size();i++){
            InvoiceLineItem invoiceLineItem = new InvoiceLineItem(
                    lineItemArrayList.get(i).getBatchID(),
                    lineItemArrayList.get(i).getProductId(),
                    new UtilityManager().genarateInvoiceId(invoiceNumber,orderList.getSalesRepId()),
                    lineItemArrayList.get(i).getQuantity(),
                    lineItemArrayList.get(i).getFreeQuantity(),
                    lineItemArrayList.get(i).getUnitSellingPrice(),
                    lineItemArrayList.get(i).getAmount(),
                    0,
                    syncDate,
                    lineItemArrayList.get(i).getProductName()
            );

            ContentValues cv1 = invoiceLineItem.getCVvaluesFromInvoiceLineItem();
            boolean success1 = (new DbHelper(getApplicationContext())).insertDataAll(cv1, TABLE_INVOICE_LINE_ITEM);
            if(success1){
                System.out.println(TABLE_INVOICE_LINE_ITEM+" data insert to sqlite db ["+ new UtilityManager().genarateInvoiceId(invoiceNumber,orderList.getSalesRepId())+"]");
            }else{
                System.out.println(TABLE_INVOICE_LINE_ITEM+" data not insert to sqlite db");
            }

            //line item for json
            InvoiceLineItemJson invoiceLineItemJson = new InvoiceLineItemJson(
                    soid,
                    lineItemArrayList.get(i).getBatchID(),
                    lineItemArrayList.get(i).getProductId(),
                    lineItemArrayList.get(i).getQuantity(),
                    lineItemArrayList.get(i).getFreeQuantity(),
                    lineItemArrayList.get(i).getUnitSellingPrice(),
                    0,
                    lineItemArrayList.get(i).getAmount(),
                    0,
                    0,
                    syncDate,
                    lineItemArrayList.get(i).getProductName()
            );
            jsonInvoiceLineItem.add(invoiceLineItemJson);

        }

        //save data to Invoice Utilization Table
        for (int j=0;j<invoiceUtilizationArrayList.size();j++){
            InvoiceUtilization invoiceUtilization = invoiceUtilizationArrayList.get(j);
            ContentValues iuCv = invoiceUtilization.getInvoiceUtilizationCV();
            System.out.println("InvoiceId "+iuCv.get("InvoiceId"));
            System.out.println("UtilizedAmount "+iuCv.get("UtilizedAmount"));

            boolean ivSuccess = db.insertDataAll(iuCv,TABLE_INVOICE_UTILIZATION);
            if (ivSuccess){
                System.out.println("Data is inserted to "+TABLE_INVOICE_UTILIZATION);
            }else{
                System.out.println("Data is not inserted to "+ TABLE_INVOICE_UTILIZATION);
            }
        }


        //Create Json
        //Save Invoice details
        InvoiceJson invoiceJson = new InvoiceJson(
                invoiceNumber,
                orderList.getEstimateDeliveryDate(),
                0,//TotalDiscount
                invoiceGrandTotal,
                syncDate,
                0,//IsSync
                0,//SalesStatus
                enterdUser,
                syncDate,//EnteredDate
                enterdUser,//Update User
                syncDate,//updateDate
                orderList.getSaleDate(),
                orderList.getSaleTypeId(),
                orderList.getSalesRepId(),
                orderList.getDitributorId(),
                orderList.getMerchantId(),
                orderList.getSalesOrderId(),
                orderList.getTotalVAT(),
                orderList.getIsVat(),
                isCredit,
                creditDays,
                paymentId,
                orderList.getIsReturn(),
                totalInvoiceValue,//Invoice Outstanding Amount
                jsonInvoiceLineItem
        );

        Gson gson1 = new Gson();
        String json = gson1.toJson(invoiceJson,InvoiceJson.class);
        System.out.println("========= Invoice Json: "+ json);

        ContentValues cvJson = new ContentValues();
        cvJson.put(COL_INVOICE_JSON_InvoiceNumber,new UtilityManager().genarateInvoiceId(invoiceNumber,orderList.getSalesRepId()));
        cvJson.put(COL_INVOICE_JSON_JsonString,json);
        cvJson.put(COL_INVOICE_JSON_IsSync,0);

        boolean successJson = db.insertDataAll(cvJson,TABLE_INVOICE_JSON);
        if (successJson){
            System.out.println("Data inserted to "+ TABLE_INVOICE_JSON);
        }else {
            System.out.println("Data is not inserted to "+ TABLE_INVOICE_JSON);
        }

        String url = HTTPPaths.seriveUrl+"CreateSalesInvoiceMobile";

        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("Json Error Invoice Json: : " + e.getMessage());
        }

        displayStatusMessage("Invoice is Created. \n Invoice Number is "+ new UtilityManager().genarateInvoiceId(invoiceNumber,orderList.getSalesRepId()),1,0);
        isClick=true;
    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}

