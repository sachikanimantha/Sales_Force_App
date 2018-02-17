package com.example.bellvantage.scadd;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bellvantage.scadd.Adapters.ViewStockAdapter;
import com.example.bellvantage.scadd.DB.DbHelper;
import com.example.bellvantage.scadd.Utils.DateManager;
import com.example.bellvantage.scadd.Utils.SessionManager;
import com.example.bellvantage.scadd.Utils.SyncManager;
import com.example.bellvantage.scadd.swf.DistributorDetails;
import com.example.bellvantage.scadd.swf.LoginUser;
import com.example.bellvantage.scadd.swf.ProductList;
import com.example.bellvantage.scadd.swf.SalesRep;
import com.google.gson.Gson;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;
import java.util.UUID;

public class ViewRedyStock extends AppCompatActivity implements Runnable{


    TextView tv_slsrep_name,tv_slsrep_cono,tv_slsrep_id,tv_dis_name,tv_dis_address,tv_tv_nodata;
    RecyclerView rv_viewStock;
    Button btn_viewstock_print;
    ArrayList<ProductList> productListArrayList = new ArrayList<>();
    ViewStockAdapter viewStockAdapter;
    int userTypeId;


    protected static final String TAG = "TAG";
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    BluetoothAdapter mBluetoothAdapter;
    private UUID applicationUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressDialog mBluetoothConnectProgressDialog;
    private BluetoothSocket mBluetoothSocket;
    BluetoothDevice mBluetoothDevice;
    OutputStream os;

    ArrayList<SalesRep> salesRepArray = new ArrayList<>();
    ArrayList<DistributorDetails> distributorDetailsArray = new ArrayList<>();

    SimpleDateFormat sdf_date,sdf_time;
    String today,justTime;

    String BILL = "";
    int distri_id;
    int is_distri_vat ;
    String salesrepType,salesrep_name,salesrep_cono,distri_name,distri_address;
    String[] sp = {"", " ", "  ", "   ", "    ", "     ", "      ", "       ", "        ", "         ", "          ", "           ", "            ", "             ",
            "              ", "               ", "                ", "                 ", "                  ", "                   ", "                    ",
            "                     ", "                      ", "                       ", "                        ", "                         ", "                          ",
            "                           ", "                            ", "                             ", "                              ", "                               ",
            "                                ", "                                 ", "                                  ", "                                   ",
            "                                    ", "                                     ", "                                      ", "                                       ",
            "                                        ", "                                         ", "                                          ",
            "                                           ", "                                            ", "                                             ",
            "                                              ", "                                               ", "                                                ",
            "                                                 ", "                                                  ", "                                                   ",
            "                                                    ", "                                                     ", "                                                      "};//ch 54



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_redy_stock);


        Calendar c = Calendar.getInstance();
        sdf_date = new SimpleDateFormat("yyyy-MM-dd"); //ch 10
        today = sdf_date.format(c.getTime());

        sdf_time = new SimpleDateFormat("HH:mm:ss"); //ch 8
        justTime = sdf_time.format(c.getTime());

        findDistributorANDSalesrepDetails();
        init();

        btn_viewstock_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter == null) {
                    Toast.makeText(ViewRedyStock.this, "Bluetooth Adapter null", Toast.LENGTH_SHORT).show();
                } else {
                    if (!mBluetoothAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    } else {
                        ListPairedDevices();
                        Intent connectIntent = new Intent(ViewRedyStock.this, IRA_DeviceList.class);
                        startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                    }
                }
            }
        });



    }


    private void findDistributorANDSalesrepDetails() {

        SessionManager.pref = getSharedPreferences("login.conf", Context.MODE_PRIVATE);
        SessionManager.editor = SessionManager.pref.edit();

        Gson gson = new Gson();
        String getJson = SessionManager.pref.getString("user", "");
        LoginUser prefUser = gson.fromJson(getJson, LoginUser.class);
        userTypeId = prefUser.getUserTypeId();
        System.out.println("user id - "+userTypeId);


        //find a distri id according to salesrep is
        String sql0 = "SELECT " + DbHelper.COL_SALES_REP_DistributorId + " FROM " + DbHelper.TABLE_SALES_REP +
                " where " + DbHelper.COL_SALES_REP_SalesRepId + " = " + userTypeId;
        System.out.println("sql0 - " + sql0);

        SyncManager syncManager = new SyncManager(this);

        String sql_d = "SELECT * FROM " + DbHelper.TABLE_DISTRIBUTOR;
        String sql_s = "SELECT * FROM " + DbHelper.TABLE_SALES_REP;
        try{

            salesRepArray = syncManager.getSalesRepDetailsFromSQLite(sql_s);
            distributorDetailsArray = syncManager.getDistributorDetailsFromSQLite(sql_d, userTypeId);

            //sizes should b 1 ,1 bcz only one sales rep and dev
            System.out.println("salesRepArray.size() - " + salesRepArray.size());
            System.out.println("distributorDetailsArray.size() - " + distributorDetailsArray.size());

            distri_id = distributorDetailsArray.get(0).getDistributorId();
            distri_name = distributorDetailsArray.get(0).getDistributorName();
            distri_address = distributorDetailsArray.get(0).getAddress1();
            is_distri_vat = distributorDetailsArray.get(0).getIsVat();
            salesrepType = salesRepArray.get(0).getSalesRepType();
            salesrep_name = salesRepArray.get(0).getSalesRepName();
            salesrep_cono = salesRepArray.get(0).getContactNo();

        }catch(Exception e){

        }

        System.out.println("distri_id - " + distri_id);
        System.out.println("distri_name - " + distri_name);
        System.out.println("distri_address - " + distri_address);
        System.out.println("is_distri_vat - " + is_distri_vat);
        System.out.println("salesrepType - " + salesrepType);
        System.out.println("salesrep_name - " + salesrep_name);
        System.out.println("salesrep_cono - " + salesrep_cono);

    }

    private void init() {

        tv_slsrep_name = (TextView)findViewById(R.id.tv_salesrep_name_rsstock);
        tv_slsrep_cono = (TextView)findViewById(R.id.tv_salesrep_cono_rsstock);
        tv_slsrep_id = (TextView)findViewById(R.id.tv_salesrep_id_rsstock);
        tv_dis_name = (TextView)findViewById(R.id.tv_dis_name_rsstock);
        tv_dis_address = (TextView)findViewById(R.id.tv_dis_address_rsstock);

        tv_tv_nodata = (TextView)findViewById(R.id.tv_nodata_rs);
        tv_tv_nodata.setVisibility(View.GONE);

        rv_viewStock = (RecyclerView)findViewById(R.id.rv_viewstock_rsitems);
        rv_viewStock.setVisibility(View.VISIBLE);

        btn_viewstock_print = (Button)findViewById(R.id.btn_print_rsviewstock);

        tv_slsrep_name.setText(salesrep_name);
        tv_slsrep_cono.setText(salesrep_cono);
        tv_slsrep_id.setText("ID - "+userTypeId);
        tv_dis_name.setText(distri_name);
        tv_dis_address.setText(distri_address);

        //initial loading product list
        productListArrayList = (new SyncManager(getApplicationContext())).getProductListFromSQLite(2, 0, salesrepType);

        if(productListArrayList.size() != 0){

            tv_tv_nodata.setVisibility(View.GONE);
            rv_viewStock.setVisibility(View.VISIBLE);
            viewStockAdapter = new ViewStockAdapter(productListArrayList,getApplicationContext());
            rv_viewStock.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            rv_viewStock.setItemAnimator(new DefaultItemAnimator());
            rv_viewStock.setAdapter(viewStockAdapter);

        }else{

            tv_tv_nodata.setVisibility(View.VISIBLE);
            rv_viewStock.setVisibility(View.GONE);
        }


    }


    private void print_salesrepInventory_woosim_header( String dis_name, String dis_address) {
        //64CPL
        //57cpl if we add 1 bit space for each charactor

        String  distriName = "", destriAddress = "";

        if (dis_name.length() > 52) {
            distriName = dis_name.substring(0, 52);
        } else if (dis_name.length() <= 52) {
            distriName = dis_name + sp[52 - dis_name.length()];
        }

//        if (dis_address.length() > 52) {
//            destriAddress = dis_address.substring(0, 52);
//        } else if (dis_address.length() <= 52) {
//            destriAddress = dis_address + sp[52 - dis_address.length()];
//        }

        BILL = "\n\n"+sp[15]+"Salesrep Vehicle Inventory"+sp[16]+"\n\n\n";

        BILL = BILL + "Distributor" + sp[46] + "\n";
        BILL = BILL + sp[5] + distriName + "\n";                                             //5,52
        //BILL = BILL + sp[5] + destriAddress + "\n";                                        //5,52
        BILL = BILL + "---------------------------------------------------------\n\n";
        BILL = BILL + "   Description    " + "  Stock  " + "    UnitPrice  " + "   Expire Date \n";    //18,9,15,15
        BILL = BILL + "---------------------------------------------------------\n";
        System.out.println(BILL);
    }

    private void print_salesrepInventory_woosim_dataList(String desc, int stock,double unitprice, String expireDate) {

        //64CPL
        //57cpl if we add 1 bit space for each charactor
        String desc_ = "", stk_ = "",exp_date="", unitprice_ = "";

        int sp1 = 57 - desc.length();   //reserved 23ch
        if (desc.length() >= 57) {     //description
            desc_ = desc.substring(0, 57);
        } else if (desc.length() < 57) {
            desc_ = desc + sp[sp1];
        }

        int sp2 = 7 - String.valueOf(stock).length();       //reserved 7ch
        stk_ = sp[sp2] + stock;

        int sp3 = 13 - String.valueOf(String.format("%.2f",unitprice)).length();       //reserved 13ch
        unitprice_ = sp[sp3] + String.format("%.2f",unitprice);

        int sp4 = 13 - String.valueOf(expireDate).length();       //reserved 13ch
        exp_date = sp[sp4] + expireDate;

        BILL = BILL + desc_ + "\n";                                                                                      //57
        BILL = BILL + sp[18] + stk_ +"  "+ unitprice_+"  "+ exp_date+"  "+"\n\n";              //18,7+2,13+2,13+2
        System.out.println(BILL);
    }

    private void print_salesrepInventory_woosim_finalfooter(String date,String time,String salesrep_no,String salesrep_name){

        String sales_rep = "";
        if (salesrep_name.length() >= 28) {
            sales_rep = salesrep_name.substring(0, 28);
        } else {
            sales_rep = salesrep_name + sp[28 - salesrep_name.length()];
        }
        BILL = BILL + "\nPrinted Date:" + date + "  Time:" + time + sp[19] + "\n\n";                                        //13,10,7,8,19
        BILL = BILL + "Sales Rep: " + sales_rep + "__________________"+"\n";             //11,28,18
        BILL = BILL + sp[11] + salesrep_no + sp[18] +"    Signature    "+ "\n";                //11,10,18,18

        BILL = BILL + "---------------------------------------------------------\n\n";
        BILL = BILL + "                Imported & distributed by                \n";
        BILL = BILL + "                scadd EXPORTS (PVT) LTD                \n";
        BILL = BILL + "           833,SIRIMAVO BANDARANAYAKE MAWATHA,           \n";
        BILL = BILL + "                       COLOMBO 14,                       \n";
        BILL = BILL + "                 TELEPHONE : 011 5224200                 \n\n\n\n";
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

        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        ViewRedyStock.this.finish();
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

                        mBluetoothConnectProgressDialog = new ProgressDialog(ViewRedyStock.this);
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
                    Intent connectIntent = new Intent(ViewRedyStock.this, IRA_DeviceList.class);
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
            if (mBluetoothSocket.isConnected()) {

                if (mBluetoothDevice.getName().equalsIgnoreCase("WOOSIM")) {
                    Thread t = new Thread() {
                        public void run() {
                            try {
                                os = mBluetoothSocket.getOutputStream();
                                //print_image();    //they dont want image

                                print_salesrepInventory_woosim_header(distri_name,distri_address);

                                int size = productListArrayList.size();
                                for (int i = 0; i < size; i++) {

                                    String desc = productListArrayList.get(i).getName();
                                    int stock = productListArrayList.get(i).getStock();
                                    double unitprice = productListArrayList.get(i).getUnitPrice();
                                    String expiredate = productListArrayList.get(i).getExpireDate();



                                    int start2 = expiredate.indexOf("(");
                                    int end2 = expiredate.indexOf(")");
                                    String date2 = expiredate.substring(start2+1,end2);

                                    String d = (new DateManager()).getDateAccordingToMilliSeconds(Long.parseLong(date2), "yyyy-MM-dd");


                                    print_salesrepInventory_woosim_dataList(desc,stock,unitprice,d);
                                }

                                print_salesrepInventory_woosim_finalfooter(today,justTime,salesrep_cono,salesrep_name);

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
            Toast.makeText(ViewRedyStock.this, "DeviceConnected", Toast.LENGTH_SHORT).show();
            //closeConnectionWindow();
        }
    };

}
