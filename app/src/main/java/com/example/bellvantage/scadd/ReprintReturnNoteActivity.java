package com.example.bellvantage.scadd;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bellvantage.scadd.Adapters.PrintviewMainAdapterItems;
import com.example.bellvantage.scadd.Utils.DateManager;
import com.example.bellvantage.scadd.Utils.SyncManager;
import com.example.bellvantage.scadd.swf.ReturnInventory;
import com.example.bellvantage.scadd.swf.ReturnInventoryLineItem;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Set;
import java.util.UUID;

import static com.example.bellvantage.scadd.DB.DbHelper.COL_DISTRIBUTOR_ADDRESS_1;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_DISTRIBUTOR_DISTRIBUTOR_ID;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_DISTRIBUTOR_DISTRIBUTOR_NAME;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_DISTRIBUTOR_IS_VAT;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SALES_REP_ContactNo;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SALES_REP_SalesRepId;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SALES_REP_SalesRepName;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_DISTRIBUTOR;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_MERCHANT;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_SALES_REP;
import static com.example.bellvantage.scadd.DB.DbHelper.TBL_MERCHANT_ADDRESS_1;
import static com.example.bellvantage.scadd.DB.DbHelper.TBL_MERCHANT_ISVAT;
import static com.example.bellvantage.scadd.DB.DbHelper.TBL_MERCHANT_MERCHANT_ID;
import static com.example.bellvantage.scadd.DB.DbHelper.TBL_MERCHANT_MERCHANT_NAME;

public class ReprintReturnNoteActivity extends AppCompatActivity implements Runnable {
    Toolbar toolbar;
    ReturnInventory returnInventory;
    TextView tv_return_invoice_type, tv_return_invoice_no, tv_return_merchant_name,
            tv_return_merchant_address, tv_return_dis_name, tv_return_dis_address, tv_return_billdate,
            tv_return_deliverydate, tv_return_salesrep_name, tv_return_salesrep_cono,
            return_totaltotal, return_totalvat, return_grosstotal, return_outstand;

    LinearLayout return_vat_status_lay;

    ArrayList<ReturnInventoryLineItem> returnInventoryLineItem = new ArrayList<>();
    PrintviewMainAdapterItems printviewMainAdapterItems;

    ListView return_listview;
    Button return_btn_print;

    String merchant_name = "", merchant_address = "", distributor_name = "",
            distributor_address = "", salesrep_name = "",
            bill_Date = "", delivery_date = "", return_note_type = "";
    double total, total_vat, outstanding, grosstotal;

    int return_note_id, isVat, merchant_isvat, distributor_isvat, salesrep_co_no;
    int salesrepid, merchantid, distributorid;
    String description;
    int qty;
    double unitPrice, lineTotal;

    SimpleDateFormat sdf_date, sdf_time;
    String today, justTime;
    protected static final String TAG = "TAG";
    private static final int REQUEST_CONNECT_DEVICE = 10;
    private static final int REQUEST_ENABLE_BT = 20;
    BluetoothAdapter mBluetoothAdapter;
    private UUID applicationUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressDialog mBluetoothConnectProgressDialog;
    private BluetoothSocket mBluetoothSocket;
    BluetoothDevice mBluetoothDevice;
    OutputStream os;
    BitSet dots = null;


    String BILL = "";
    String[] sp = {"", " ", "  ", "   ", "    ", "     ", "      ", "       ", "        ", "         ", "          ", "           ", "            ", "             ",
            "              ", "               ", "                ", "                 ", "                  ", "                   ", "                    ",
            "                     ", "                      ", "                       ", "                        ", "                         ", "                          ",
            "                           ", "                            ", "                             ", "                              ", "                               ",
            "                                ", "                                 ", "                                  ", "                                   ",
            "                                    ", "                                     ", "                                      ", "                                       ",
            "                                        ", "                                         ", "                                          ",
            "                                           ", "                                            ", "                                             ",
            "                                              "};//46 MAX


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reprint_return_note);

        initializeViews();
        toolbar.setTitle("Reprint Return-Note");

        //Get Return Note Header Details from Intent
        if (getIntent().getSerializableExtra("returnInventory") != null) {
            returnInventory = (ReturnInventory) getIntent().getSerializableExtra("returnInventory");
        }
//        System.out.println("Return Note Id: "+returnInventory.getCreditNoteNo());
//        System.out.println("Return Note SalesRep Id: "+returnInventory.getSalesRepId());


        Calendar c = Calendar.getInstance();
        sdf_date = new SimpleDateFormat("yyyy-MM-dd"); //ch 10
        today = sdf_date.format(c.getTime());

        sdf_time = new SimpleDateFormat("HH:mm:ss"); //ch 8
        justTime = sdf_time.format(c.getTime());


        init();
        LoadData();


        return_btn_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter == null) {
                    Toast.makeText(ReprintReturnNoteActivity.this, "Bluetooth Adapter null", Toast.LENGTH_SHORT).show();
                } else {
                    if (!mBluetoothAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    } else {
                        ListPairedDevices();
                        Intent connectIntent = new Intent(ReprintReturnNoteActivity.this, IRA_DeviceList.class);
                        startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                    }
                }
            }
        });
    }

    private void initializeViews() {

        toolbar = (Toolbar) findViewById(R.id.tb_main);
    }

    private void init() {

        tv_return_invoice_type = (TextView) findViewById(R.id.tv_return_return_invoice_type);
        tv_return_invoice_no = (TextView) findViewById(R.id.tv_return_return_invoice_no);
        tv_return_merchant_name = (TextView) findViewById(R.id.tv_return_merchant_name);
        tv_return_merchant_address = (TextView) findViewById(R.id.tv_return_merchant_address);
        tv_return_dis_name = (TextView) findViewById(R.id.tv_return_dis_name);
        tv_return_dis_address = (TextView) findViewById(R.id.tv_return_dis_address);
        tv_return_billdate = (TextView) findViewById(R.id.tv_return_billdate);
        tv_return_deliverydate = (TextView) findViewById(R.id.tv_return_deliverydate);
        tv_return_salesrep_name = (TextView) findViewById(R.id.tv_return_salesrep_name);
        tv_return_salesrep_cono = (TextView) findViewById(R.id.tv_return_salesrep_co_no);

        return_totaltotal = (TextView) findViewById(R.id.tv_return_return_totaltotal);
        return_totalvat = (TextView) findViewById(R.id.tv_return_totalvat);
        return_grosstotal = (TextView) findViewById(R.id.tv_return_grosstotal);
        return_outstand = (TextView) findViewById(R.id.tv_return_outstanding);
        return_outstand.setVisibility(View.GONE);

        return_vat_status_lay = (LinearLayout) findViewById(R.id.ll_return_vat_status);
        return_btn_print = (Button) findViewById(R.id.btn_return_print_main);
        return_listview = (ListView) findViewById(R.id.lv_return_Items);
    }

    private void LoadData() {

        SyncManager syncManager = new SyncManager(getApplicationContext());

        salesrepid = returnInventory.getSalesRepId();
        merchantid = returnInventory.getMerchantId();
        distributorid = returnInventory.getDistributorId();

        String sql_merchant_name = "SELECT " + TBL_MERCHANT_MERCHANT_NAME + " FROM " + TABLE_MERCHANT + " WHERE " + TBL_MERCHANT_MERCHANT_ID + " = " + merchantid;
        String sql_merchant_address = "SELECT " + TBL_MERCHANT_ADDRESS_1 + " FROM " + TABLE_MERCHANT + " WHERE " + TBL_MERCHANT_MERCHANT_ID + " = " + merchantid;
        String sql_merchant_isvat = "SELECT " + TBL_MERCHANT_ISVAT + " FROM " + TABLE_MERCHANT + " WHERE " + TBL_MERCHANT_MERCHANT_ID + " = " + merchantid;

        merchant_name = syncManager.getStringValueFromSQLite(sql_merchant_name, TBL_MERCHANT_MERCHANT_NAME);
        merchant_address = syncManager.getStringValueFromSQLite(sql_merchant_address, TBL_MERCHANT_ADDRESS_1);
        merchant_isvat = Integer.parseInt(syncManager.getStringValueFromSQLite(sql_merchant_isvat, TBL_MERCHANT_ISVAT));

        String sql_disti_name = "SELECT " + COL_DISTRIBUTOR_DISTRIBUTOR_NAME + " FROM " + TABLE_DISTRIBUTOR + " WHERE " + COL_DISTRIBUTOR_DISTRIBUTOR_ID + " = " + distributorid;
        String sql_dist_address = "SELECT " + COL_DISTRIBUTOR_ADDRESS_1 + " FROM " + TABLE_DISTRIBUTOR + " WHERE " + COL_DISTRIBUTOR_DISTRIBUTOR_ID + " = " + distributorid;
        String sql_dist_isvat = "SELECT " + COL_DISTRIBUTOR_IS_VAT + " FROM " + TABLE_DISTRIBUTOR + " WHERE " + COL_DISTRIBUTOR_DISTRIBUTOR_ID + " = " + distributorid;

        distributor_name = syncManager.getStringValueFromSQLite(sql_disti_name, COL_DISTRIBUTOR_DISTRIBUTOR_NAME);
        distributor_address = syncManager.getStringValueFromSQLite(sql_dist_address, COL_DISTRIBUTOR_ADDRESS_1);
        distributor_isvat = Integer.parseInt(syncManager.getStringValueFromSQLite(sql_dist_isvat, COL_DISTRIBUTOR_IS_VAT));

        String sql_salesrep_name = "SELECT " + COL_SALES_REP_SalesRepName + " FROM " + TABLE_SALES_REP + " WHERE " + COL_SALES_REP_SalesRepId + " = " + salesrepid;
        String sql_salesrep_co_no = "SELECT " + COL_SALES_REP_ContactNo + " FROM " + TABLE_SALES_REP + " WHERE " + COL_SALES_REP_SalesRepId + " = " + salesrepid;


        salesrep_name = syncManager.getStringValueFromSQLite(sql_salesrep_name, COL_SALES_REP_SalesRepName);

        try {
            salesrep_co_no = Integer.parseInt(syncManager.getStringValueFromSQLite(sql_salesrep_co_no, COL_SALES_REP_ContactNo));
        } catch (Exception e) {
            System.out.println("Error Print Credit Note Activity: " + e.getMessage());
        }

        //bill_Date = returnInventory.getEnteredDate();
        delivery_date = returnInventory.getReturnDate();

        System.out.println("merchant_name - " + merchant_name);
        System.out.println("merchant_address - " + merchant_address);
        System.out.println("distributor_name - " + distributor_name);
        System.out.println("distributor_address - " + distributor_address);
        System.out.println("salesrep_name - " + salesrep_name);
        System.out.println("salesrep_co_no - " + salesrep_co_no);
        //System.out.println("bill_Date - "+bill_Date);
        System.out.println("delivery_date - " + delivery_date);


        int start0 = delivery_date.indexOf("(");
        int end0 = delivery_date.indexOf(")");
        String date_delivery = delivery_date.substring(start0 + 1, end0);


        if (merchant_isvat == 1 && distributorid == 1) {
            isVat = 1;
            return_note_type = "Tax Return Note";
            return_totalvat.setText("0.00");
        } else {
            isVat = 0;
            return_note_type = "Return Note";
        }

        return_note_id = returnInventory.getCreditNoteNo();

        System.out.println("return_note_type - " + return_note_type);
        System.out.println("return_note_id - " + return_note_id);

        returnInventoryLineItem = syncManager.getCreditNoteLineItem(return_note_id, 1);
        System.out.println("returnInventoryLineItem.size - " + returnInventoryLineItem.size());

        tv_return_invoice_type.setText("" + return_note_type);
        tv_return_invoice_no.setText("Return Note Id - " + return_note_id);
        tv_return_merchant_name.setText(merchant_name);
        tv_return_merchant_address.setText(merchant_address);
        tv_return_dis_name.setText(distributor_name);
        tv_return_dis_address.setText(distributor_address);
        tv_return_billdate.setText("Bill Date - " + (new DateManager()).getDateAccordingToMilliSeconds(Long.parseLong(date_delivery), "yyyy-MM-dd"));
        tv_return_deliverydate.setText("Delivery Date - " + (new DateManager()).getDateAccordingToMilliSeconds(Long.parseLong(date_delivery), "yyyy-MM-dd"));
        tv_return_salesrep_name.setText(salesrep_name);
        tv_return_salesrep_cono.setText("" + salesrep_co_no);


        for (int i = 0; i < returnInventoryLineItem.size(); i++) {

            String desc = returnInventoryLineItem.get(i).getProductName();
//            int productid = returnInventoryLineItem.get(i).getProductId();
//            int qty = returnInventoryLineItem.get(i).getQuantity();
//            double unitprice = returnInventoryLineItem.get(i).getUnitPrice();
//            double lineTotal = unitprice * qty;
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("desc - " + desc);
//           returnInventoryLineItem.add(new ReturnInventoryLineItem(productid,unitprice,qty,lineTotal,desc));
        }

        printviewMainAdapterItems = new PrintviewMainAdapterItems(getApplicationContext(), R.layout.layout_for_returnnote_items, returnInventoryLineItem);
        return_listview.setAdapter(printviewMainAdapterItems);

        outstanding = returnInventory.getTotalOutstanding();
        total = returnInventory.getTotalAmount();

        return_totaltotal.setText("" + total);
        return_grosstotal.setText("" + total);
        //return_outstand.setText(""+outstanding);

    }

    private void print_bill_woosim_header(int returnnote_id, String merchant_name,
                                          String merchant_address, String dis_name, String dis_address, String billdate) {
        //64CPL
        //57cpl if we add 1 bit space for each charactor

        String merchantName = "", merchantAddress = "", distriName = "", destriAddress = "";

        String invoiceType = "";

        if (isVat == 0) {
            invoiceType = "SALES";
        } else if (isVat == 1) {
            invoiceType = "  TAX";
        }

        if (merchant_name.length() > 29) {
            merchantName = merchant_name.substring(0, 29);
        } else if (merchant_name.length() <= 29) {
            merchantName = merchant_name + sp[29 - merchant_name.length()];
        }

        String[] merAdd = merchant_address.split(",");
        for (String x : merAdd) {
            System.out.println("x - " + x);
        }

        if (dis_name.length() > 52) {
            distriName = dis_name.substring(0, 52);
        } else if (dis_name.length() <= 52) {
            distriName = dis_name + sp[52 - dis_name.length()];
        }

        if (dis_address.length() > 52) {
            destriAddress = dis_address.substring(0, 52);
        } else if (dis_address.length() <= 52) {
            destriAddress = dis_address + sp[52 - dis_address.length()];
        }

        BILL = BILL + "\n\n                      RETURN NOTE                      \n";
        BILL = BILL + sp[22] + "Id - " + returnnote_id + sp[21] + "\n\n"; //24,9,24
        BILL = BILL + "Customer" + sp[26] + sp[23];          //8,26,9,14
        BILL = BILL + sp[5] + merchantName + "BillDate    " + billdate + "\n";                       //5,29,12,10
        //BILL = BILL + sp[5] + dis_address_ + "\n\n";                //5,29 13,10
        for (String x : merAdd) {
            System.out.println("x - " + x);
            BILL = BILL + sp[5] + x + "\n";
        }
        BILL = BILL + "Distributor" + sp[46] + "\n";
        BILL = BILL + sp[5] + distriName + "\n";                                             //5,52
        BILL = BILL + sp[5] + destriAddress + "\n";                                        //5,52
        System.out.println(BILL);
    }

    private void print_bill_woosim_finalfooter(String date, String time, String salesrep_no, String salesrep_name) {

        String sales_rep = "";
        if (salesrep_name.length() >= 28) {
            sales_rep = salesrep_name.substring(0, 28);
        } else {
            sales_rep = salesrep_name + sp[28 - salesrep_name.length()];
        }

        BILL = BILL + "\nPrinted Date:" + date + "  Time:" + time + sp[19] + "\n\n";     //13,10,7,8,19
        BILL = BILL + "Sales Rep: " + sales_rep + "__________________" + "\n";             //11,28,18
        BILL = BILL + sp[11] + salesrep_no + sp[18] + "    Signature    " + "\n";                //11,10,18,18
        BILL = BILL + "---------------------------------------------------------\n\n";
        BILL = BILL + "                Imported & distributed by                \n";
        BILL = BILL + "                scadd EXPORTS (PVT) LTD                \n";
        BILL = BILL + "           833,SIRIMAVO BANDARANAYAKE MAWATHA,           \n";
        BILL = BILL + "                       COLOMBO 14,                       \n";
        BILL = BILL + "                 TELEPHONE : 011 5224200                 \n\n\n\n";
    }

    private void print_returnnote_woosim_header(String credit_date, String delivery_date) {

//        BILL = BILL +"\n\n                      RETURN NOTE                      \n";
//        BILL = BILL + sp[22]+"Id - " + returnnote_id + sp[21]+"\n\n"; //24,9,24
        BILL = BILL + "\nCredit Date : " + credit_date + sp[33] + "\n";          //14,10,33
        BILL = BILL + "Delivery Date : " + delivery_date + sp[31] + "\n";   //16,10,31
        BILL = BILL + "---------------------------------------------------------\n\n";
        BILL = BILL + " Item                      " + " QTY " + "     Price  " + "  LineTotal  ";    //27,5,12,13
        BILL = BILL + "---------------------------------------------------------\n";
        System.out.println(BILL);
    }

    private void print_returnnote_woosim_body(String item_name, int qty, double unit_price, double linetotal) {

        String item_name1 = "", qty1 = "", unit_price1 = "", linetotal1 = "";
        if (item_name.length() > 27) {
            item_name1 = item_name.substring(0, 27);
        } else if (item_name.length() <= 27) {
            item_name1 = item_name + sp[27 - item_name.length()];
        }

        int s = 5 - String.valueOf(qty).length();       //reserved 6ch
        qty1 = sp[s] + qty;

        if (String.valueOf(String.format("%.2f",unit_price)).length() > 12) {
            unit_price1 = String.valueOf(String.format("%.2f",unit_price)).substring(0, 12);
        } else if (String.valueOf(String.format("%.2f",unit_price)).length() <= 12) {
            unit_price1 = sp[12 - String.valueOf(String.format("%.2f",unit_price)).length()] + String.format("%.2f",unit_price);
        }

        if (String.valueOf(String.format("%.2f",linetotal)).length() > 13) {
            linetotal1 = String.valueOf(String.format("%.2f",linetotal)).substring(0, 13);
        } else if (String.valueOf(String.format("%.2f",linetotal)).length() <= 13) {
            linetotal1 = sp[13 - String.valueOf(String.format("%.2f",linetotal)).length()] + String.format("%.2f",unit_price);
        }

        BILL = BILL + "\n" + item_name1 + qty1 + unit_price1 + linetotal1;                          //27,5,12,13
        System.out.println(BILL);
    }

    private void print_returnnote_woosim_footer(double totalRetunnote) {

        String totalRetunnote_ = "";
        if (String.valueOf(String.format("%.2f",totalRetunnote)).length() == 10) {
            totalRetunnote_ = sp[0] + String.format("%.2f",totalRetunnote);
        } else if (String.valueOf(String.format("%.2f",totalRetunnote)).length() == 9) {
            totalRetunnote_ = sp[1] + String.format("%.2f",totalRetunnote);
        } else if (String.valueOf(String.format("%.2f",totalRetunnote)).length() == 8) {
            totalRetunnote_ = sp[2] + String.format("%.2f",totalRetunnote);
        } else if (String.valueOf(String.format("%.2f",totalRetunnote)).length() == 7) {
            totalRetunnote_ = sp[3] + String.format("%.2f",totalRetunnote);
        } else if (String.valueOf(String.format("%.2f",totalRetunnote)).length() == 6) {
            totalRetunnote_ = sp[4] + String.format("%.2f",totalRetunnote);
        } else if (String.valueOf(String.format("%.2f",totalRetunnote)).length() == 5) {
            totalRetunnote_ = sp[5] + String.format("%.2f",totalRetunnote);
        } else if (String.valueOf(String.format("%.2f",totalRetunnote)).length() == 4) {
            totalRetunnote_ = sp[6] + String.format("%.2f",totalRetunnote);
        } else if (String.valueOf(String.format("%.2f",totalRetunnote)).length() == 3) {
            totalRetunnote_ = sp[7] + String.format("%.2f",totalRetunnote);
        }
        BILL = BILL + "\n" + "---------------------------------------------------------";//27,5,12,13
        BILL = BILL + "\n" + sp[23] + "Total" + sp[19] + totalRetunnote_ + "\n";          //23,5,19,10
        BILL = BILL + "---------------------------------------------------------\n\n";
        System.out.println(BILL);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (mBluetoothSocket != null) ;
            System.out.println("onDestroy - socket closed");
            mBluetoothSocket.close();
        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
    }

    @Override
    public void onBackPressed() {
        try {
            mBluetoothSocket.close();
            //mBluetoothConnectProgressDialog.dismiss();

        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
        setResult(RESULT_CANCELED);
        finish();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        ReprintReturnNoteActivity.this.finish();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    public void onActivityResult(int mRequestCode, int mResultCode, Intent mDataIntent) {
        super.onActivityResult(mRequestCode, mResultCode, mDataIntent);

        switch (mRequestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (mResultCode == Activity.RESULT_OK) {
                    //mBluetoothAdapter = null;
                    mBluetoothSocket = null;

                    Bundle mExtra = mDataIntent.getExtras();
                    String mDeviceAddress = mExtra.getString("DeviceAddress");
                    //String mDeviceName = mExtra.getString("DeviceName");
                    Log.v(TAG, "Coming incoming address " + mDeviceAddress);
                    mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(mDeviceAddress);
                    if (mBluetoothDevice != null) {
                        //mBluetoothConnectProgressDialog = ProgressDialog.show(this, "Connecting...", mBluetoothDevice.getName() + " : " + mBluetoothDevice.getAddress(), true, false);

                        mBluetoothConnectProgressDialog = new ProgressDialog(ReprintReturnNoteActivity.this);
                        mBluetoothConnectProgressDialog.setMessage("Loading... " + mBluetoothDevice.getName() + " :" + mBluetoothDevice.getAddress());

                        mBluetoothConnectProgressDialog.setCancelable(false);
                        mBluetoothConnectProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        mBluetoothConnectProgressDialog.show();

                        Thread mBlutoothConnectThread = new Thread(this);
                        mBlutoothConnectThread.start();
                    } else {
                        System.out.println("Bluetooth device is null");
                    }
                }
                break;

            case REQUEST_ENABLE_BT:
                if (mResultCode == Activity.RESULT_OK) {
                    ListPairedDevices();
                    Intent connectIntent = new Intent(ReprintReturnNoteActivity.this, IRA_DeviceList.class);
                    startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                } else {
                    // Toast.makeText(getApplicationContext(), "Message", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void ListPairedDevices() {
        Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter.getBondedDevices();
        if (mPairedDevices.size() > 0) {
            for (BluetoothDevice mDevice : mPairedDevices) {
                Log.v(TAG, "PairedDevices: " + mDevice.getName() + "  " + mDevice.getAddress());
            }
        }
    }

    public void run() {
        try {
            mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(applicationUUID);
            mBluetoothAdapter.cancelDiscovery();
            mBluetoothSocket.connect();


            int start0 = delivery_date.indexOf("(");
            int end0 = delivery_date.indexOf(")");
            final String date_delivery = delivery_date.substring(start0 + 1, end0);
            final String da = (new DateManager()).getDateAccordingToMilliSeconds(Long.parseLong(date_delivery), "yyyy-MM-dd");

            if (mBluetoothSocket.isConnected()) {

                if (mBluetoothDevice.getName().equalsIgnoreCase("WOOSIM")) {
                    Thread t = new Thread() {
                        public void run() {
                            try {
                                os = mBluetoothSocket.getOutputStream();
                                //print_image();    //they dont want image

                                print_bill_woosim_header(return_note_id, merchant_name, merchant_address,
                                        distributor_name, distributor_address, da);
                                //int returnnote_id, String merchant_name,
                                //String merchant_address, String dis_name, String dis_address, String billdate

                                /*

        System.out.println("merchant_name - "+merchant_name);
        System.out.println("merchant_address - "+merchant_address);
        System.out.println("distributor_name - "+distributor_name);
        System.out.println("distributor_address - "+distributor_address);
        System.out.println("salesrep_name - "+salesrep_name);
        System.out.println("salesrep_co_no - "+salesrep_co_no);
        System.out.println("bill_Date - "+bill_Date);
        System.out.println("delivery_date - "+delivery_date);
                                 */

                                print_returnnote_woosim_header(da, da);
                                ArrayList<ReturnInventoryLineItem> arr = (new SyncManager(getApplicationContext())).getReturnNoteItemData(return_note_id);
                                for (int g = 0; g < arr.size(); g++) {
                                    String name_ = arr.get(g).getProductName();
                                    int qty_ = arr.get(g).getQuantity();
                                    double unitprice = arr.get(g).getUnitPrice();
                                    double linevalue = arr.get(g).getTotalAmount();
                                    print_returnnote_woosim_body(name_, qty_, unitprice, linevalue);
                                }
                                print_returnnote_woosim_footer(total);
                                print_bill_woosim_finalfooter(today, justTime, "" + salesrep_co_no, salesrep_name);


                                os.write(IRA_PrintCommand.SET_RIGHTSIDE_CHARCTOR_SIZE);
                                os.write(BILL.getBytes());

                            } catch (Exception e) {
                                Log.e("Main", "Exe ", e);
                            }
                        }
                    };
                    t.start();
                }
                //closeSocket(mBluetoothSocket);    //cannot put here
                //finish();                         //
                //btn_print.setActivated(false);    //cannot
            }
            mHandler.sendEmptyMessage(0);
        } catch (Exception eConnectException) {
            //Log.d(TAG, "CouldNotConnectToSocket", eConnectException);
            System.out.println("error1 - " + eConnectException.toString());
            closeSocket(mBluetoothSocket);    //SHOULDNT CLOSE THIS SOCKET
            System.out.println("error2 - " + eConnectException.toString());

            return;
        }
    }

    private void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            nOpenSocket.close();
            Log.d(TAG, "SocketClosed");
            System.out.println("sout SocketClosed");
        } catch (Exception ex) {
            Log.d(TAG, "CouldNotCloseSocket");
            System.out.println("sout " + ex.toString());
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mBluetoothConnectProgressDialog.dismiss();
            Toast.makeText(ReprintReturnNoteActivity.this, "DeviceConnected", Toast.LENGTH_SHORT).show();
            //closeConnectionWindow();
        }
    };


}
