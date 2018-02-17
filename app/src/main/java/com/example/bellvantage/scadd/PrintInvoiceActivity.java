package com.example.bellvantage.scadd;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.example.bellvantage.scadd.Adapters.ItemAdapter;
import com.example.bellvantage.scadd.Adapters.PrintviewMainAdapter;
import com.example.bellvantage.scadd.Adapters.PrintviewMainAdapterItems;
import com.example.bellvantage.scadd.DB.MySingleton;
import com.example.bellvantage.scadd.Utils.DateManager;
import com.example.bellvantage.scadd.Utils.SessionManager;
import com.example.bellvantage.scadd.Utils.SyncManager;
import com.example.bellvantage.scadd.swf.InvoiceLineItem;
import com.example.bellvantage.scadd.swf.InvoiceList2;
import com.example.bellvantage.scadd.swf.LoginUser;
import com.example.bellvantage.scadd.swf.ReturnInventory;
import com.example.bellvantage.scadd.swf.ReturnInventoryLineItem;
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

import org.json.JSONObject;

import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Set;
import java.util.UUID;

public class PrintInvoiceActivity extends AppCompatActivity implements Runnable {

    //Tool Bar
    Toolbar mToolbar;

    //Navigation Drawer
    private AccountHeader headerResult = null;
    private Drawer result = null;

    RecyclerView rvItem;
    RecyclerView.LayoutManager layoutManager;
    InvoiceList2 invoiceDetails;
    ArrayList<InvoiceLineItem> itemArrayList;
    ArrayList<ReturnInventory> returnInventories;
    ArrayList<String> creditnotes;

    ItemAdapter adapter;

    TextView tv_invoice_type, tv_merchant_name, tv_merchant_address, tv_dis_name,
            tv_dis_address, tv_invoice_no, tv_billdate, tv_deliverydate,
            tv_salesrep_name, tv_salesrep_cono;

    TextView totaltotal, totalvat, freeIssues, grosstotal, tvPreviousReturn;
    double grandTotal = 0, totalVat = 0, freeTotal = 0, grossTotal = 0, grossTotal_round = 0;

    String sit = "";
    int invoice_type;
    String merchant_name;
    String merchant_address;

    String dis_address;
    //String invoice_no;
    String billdate, delivaryDate;
    String salesrep_name;
    String salesrep_cono;

    double previousReturnAmount = 0, previousReturnAmount_round = 0;
    SyncManager syncManager;

    protected static final String TAG = "TAG";
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    BluetoothAdapter mBluetoothAdapter;
    private UUID applicationUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ProgressDialog mBluetoothConnectProgressDialog;
    private BluetoothSocket mBluetoothSocket;
    BluetoothDevice mBluetoothDevice;
    OutputStream os;
    BitSet dots = null;
    int mWidth, mHeight;
    String mStatus;
    String BILL = "";
    SimpleDateFormat sdf_date, sdf_time, sdf_date2;
    String today, justTime;
    Button btn_print;
    LinearLayout main_lay, vat_status_lay, return_Lay, return_layhead;
    String mDeviceName;
    Boolean aBoolean = false;
    int invoiceNo;
    int distributorId;
    int isVat = 0, userId;
    String userName;

    ListView listviewmain;
    PrintviewMainAdapter printviewMainAdapter;

    ArrayList<ReturnInventoryLineItem> returnInventoryLineItems = new ArrayList<>();
    PrintviewMainAdapterItems printviewMainAdapterItems;

    String dis_name;
    String dis_add1;

    String DistributorName;
    String DistributorAddres1;

    String iDate = "", ddate = "";
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
        setContentView(R.layout.activity_print_invoice);

        //Toolbar
        mToolbar = (Toolbar) findViewById(R.id.tb_main);
        mToolbar.setTitle("Invoice - scadd Group");

        syncManager = new SyncManager(getApplicationContext());

        Gson gson = new Gson();
        String getJson = SessionManager.pref.getString("user", "");
        LoginUser prefUser = gson.fromJson(getJson, LoginUser.class);
        userName = prefUser.getUserName();
        userId = prefUser.getUserTypeId();

        //get data from intent
        if (getIntent().getSerializableExtra("invoiceDetails") != null) {
            invoiceDetails = (InvoiceList2) getIntent().getSerializableExtra("invoiceDetails");
            System.out.println("invoiceDetails.getMerchantId - " + invoiceDetails.getMerchantId());
        }

        if (getIntent().getSerializableExtra("itemArrayList") != null) {
            itemArrayList = (ArrayList<InvoiceLineItem>) getIntent().getSerializableExtra("itemArrayList");
            System.out.println("itemArrayList.size - " + itemArrayList.size());
        }

        if (getIntent().getSerializableExtra("itemArrayReturnNote") != null) {
            returnInventories = (ArrayList<ReturnInventory>) getIntent().getSerializableExtra("itemArrayReturnNote");
            System.out.println("itemArrayReturnNote.size - " + returnInventories.size());
        }

        creditnotes = getIntent().getStringArrayListExtra("creditNoteArray");
        System.out.println("creditnotes.size - " + creditnotes.size());

        System.out.println("invoice id  - " + invoiceDetails.getInvoiceId());
        insertNavigationDraer(savedInstanceState);


        previousReturnAmount = (new SyncManager(getApplicationContext())).getPreviousReturnAmount(invoiceDetails.getInvoiceId());
        previousReturnAmount_round = Math.round(previousReturnAmount * 100D) / 100D;

        System.out.println("previousReturnAmount_round - " + previousReturnAmount_round);

        System.out.println("invoiceDetails.getInvoiceId() - " + invoiceDetails.getInvoiceId());


        //==========RecyclerView ======================


        Calendar c = Calendar.getInstance();
        sdf_date = new SimpleDateFormat("yyyy-MM-dd"); //ch 10
        today = sdf_date.format(c.getTime());

        sdf_time = new SimpleDateFormat("HH:mm:ss"); //ch 8
        justTime = sdf_time.format(c.getTime());


        init();
        loadDistributorId();
        System.out.println("dis id - " + distributorId);
        getDistributorDetails(distributorId);
        loadData();

        rvItem = (RecyclerView) findViewById(R.id.rvItems);
        layoutManager = new LinearLayoutManager(this);
        try {
            rvItem.setLayoutManager(layoutManager);
            rvItem.setHasFixedSize(true);
            adapter = new ItemAdapter(PrintInvoiceActivity.this, itemArrayList);
            rvItem.setAdapter(adapter);
        } catch (Exception e) {
            System.out.print("Error RecyclerView" + e);
        }


        if (returnInventories.size() == 0) {
            return_Lay.setVisibility(View.GONE);
            return_layhead.setVisibility(View.GONE);
        } else {
            return_Lay.setVisibility(View.VISIBLE);
            return_layhead.setVisibility(View.VISIBLE);
        }

        listviewmain = (ListView) findViewById(R.id.listview_returnnote_item);
        printviewMainAdapter = new PrintviewMainAdapter(getApplicationContext(), R.layout.layout_for_returnnote___, returnInventories);
        listviewmain.setAdapter(printviewMainAdapter);


        listviewmain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(PrintInvoiceActivity.this, "id - "+id+",position - "+position, Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(PrintInvoiceActivity.this);
                //LayoutInflater layoutInflater = getLayoutInflater();
                view = getLayoutInflater().inflate(R.layout.layout_for_returnnote, null);
                builder.setView(view);

                TextView creditno = (TextView) view.findViewById(R.id.tv_returnnote_id);
                TextView returnDate = (TextView) view.findViewById(R.id.tv_returnnote_delivery_date);
                TextView totalamount = (TextView) view.findViewById(R.id.tv_returnnote_amount);
                TextView syncDate = (TextView) view.findViewById(R.id.tv_returnnote_credit_date);
                ListView return_items = (ListView) view.findViewById(R.id.lv__returnnote);

                String DELIVERYdATA = returnInventories.get(position).getReturnDate();
                String CREDITdATE = returnInventories.get(position).getSyncDate();

//                int start = DELIVERYdATA.indexOf("(");
//                int end = DELIVERYdATA.indexOf(")");
//                String date1 = DELIVERYdATA.substring(start+1,end);

                int start2 = CREDITdATE.indexOf("(");
                int end2 = CREDITdATE.indexOf(")");
                String date2 = CREDITdATE.substring(start2 + 1, end2);

                creditno.setText("Return note - " + returnInventories.get(position).getCreditNoteNo());
                returnDate.setText(": " + DELIVERYdATA /*(new DateManager()).getDateAccordingToMilliSeconds(Long.parseLong(date1), "yyyy-MM-dd")*/);
                syncDate.setText(": " + (new DateManager()).getDateAccordingToMilliSeconds(Long.parseLong(date2), "yyyy-MM-dd"));
                totalamount.setText(String.format("Rs : %.2f", returnInventories.get(position).getTotalAmount()));

                System.out.println("creditnotes.get() - " + creditnotes.get(position));
                returnInventoryLineItems = (new SyncManager(getApplicationContext())).getReturnNoteItemData(Integer.parseInt(creditnotes.get(position)));
                System.out.println("returnInventoryLineItems.size - " + returnInventoryLineItems.size());
                printviewMainAdapterItems = new PrintviewMainAdapterItems(getApplicationContext(), R.layout.layout_for_returnnote_items, returnInventoryLineItems);
                return_items.setAdapter(printviewMainAdapterItems);

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });


        btn_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (mBluetoothAdapter == null) {
                    Toast.makeText(PrintInvoiceActivity.this, "Bluetooth Adapter null", Toast.LENGTH_SHORT).show();
                } else {
                    if (!mBluetoothAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    } else {
                        ListPairedDevices();
                        Intent connectIntent = new Intent(PrintInvoiceActivity.this, IRA_DeviceList.class);
                        startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                    }
                }
            }
        });

    }//end onCreate


    private void init() {

        tv_invoice_type = (TextView) findViewById(R.id.tv_invoice_type);
        tv_invoice_no = (TextView) findViewById(R.id.tv_return_invoice_no);
        tv_merchant_name = (TextView) findViewById(R.id.tv_merchant_name);
        tv_merchant_address = (TextView) findViewById(R.id.tv_merchant_address);
        tv_dis_name = (TextView) findViewById(R.id.tv_dis_name);
        tv_dis_address = (TextView) findViewById(R.id.tv_dis_address);
        tv_billdate = (TextView) findViewById(R.id.tv_billdate);
        tv_deliverydate = (TextView) findViewById(R.id.tv_deliverydate);
        tv_salesrep_name = (TextView) findViewById(R.id.tv_salesrep_name);
        tv_salesrep_cono = (TextView) findViewById(R.id.tv_salesrep_co_no);

        totaltotal = (TextView) findViewById(R.id.tv_return_totaltotal);
        totalvat = (TextView) findViewById(R.id.tv_totalvat);
        freeIssues = (TextView) findViewById(R.id.tv_free_issue);
        grosstotal = (TextView) findViewById(R.id.tv_grosstotal);
        tvPreviousReturn = (TextView) findViewById(R.id.tvPreviousReturn);

        totaltotal.setText("0.00");
        totalvat.setText("0.00");
        freeIssues.setText("0.00");
        grosstotal.setText("0.00");

        vat_status_lay = (LinearLayout) findViewById(R.id.ll_vat_status);
        main_lay = (LinearLayout) findViewById(R.id.ll_main);
        return_layhead = (LinearLayout) findViewById(R.id.ll_returnnote);
        return_Lay = (LinearLayout) findViewById(R.id.ll_returnnote_item);
        btn_print = (Button) findViewById(R.id.btn_print_main);

    }

    private void loadDistributorId() {
        distributorId = invoiceDetails.getDistributorId();
        System.out.println("dis id - " + distributorId);
    }

    private void loadData() {
        //getdata
        merchant_name = invoiceDetails.getMerchantName();
        invoiceNo = invoiceDetails.getInvoiceId();
        //invoice_type = invoiceDetails.getSaleTypeId();

        totalVat = invoiceDetails.getTotalVAT();

        if (totalVat > 0) {
            isVat = 1;
        } else {
            isVat = 0;
        }

        merchant_address = invoiceDetails.getAddress1();
        //billdate = invoiceDetails.getInvoiceDate();                         //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        billdate = today;
        delivaryDate = today;
        salesrep_name = invoiceDetails.getSalesRepName();
        salesrep_cono = invoiceDetails.getSalesRepContactNo();


        //sales  item data
        int size = itemArrayList.size();

        for (int i = 0; i < size; i++) {

            int ItemCode = itemArrayList.get(i).getProductID();
            String itemName = itemArrayList.get(i).getItemName();
            int qty = itemArrayList.get(i).getQty();
            double price = itemArrayList.get(i).getPrice();
            double total = itemArrayList.get(i).getLineTotal();
            int freQTY = itemArrayList.get(i).getFreeQu();
//            grandTotal += total;
//            freeTotal += freQTY * price;

            double subtotal = (qty + freQTY) * price;
            grandTotal = grandTotal + subtotal;

            //grandTotal = grandTotal + (total + (freQTY * price));
            freeTotal = freeTotal + (freQTY * price);
            //grossTotal = grossTotal + total - previousReturnAmount;
        }

        grossTotal = grandTotal - freeTotal - previousReturnAmount;
        //grossTotal_round = Math.round(grossTotal * 100D) / 100D;
        System.out.println("grossTotal_round - " + grossTotal_round);


        if (isVat == 0) {
            sit = "SALES INVOICE";
            totalVat = 0.00;
            vat_status_lay.setVisibility(View.GONE);
        } else if (isVat == 1) {
            sit = "TAX INVOICE";
            totalVat = invoiceDetails.getTotalVAT();
            vat_status_lay.setVisibility(View.VISIBLE);
        } else {
            sit = "INVOICE";
        }

        //~~~~~~~~~~~~~~`


//        //set tOTAL Values
//
//        grossTotal = grandTotal-totalVat+ freeTotal; // calculate freeAmount + grandTotal
//
//        netTotal = grandTotal-previousReturnAmount;
//        //netTotal-+freeAmount;


        //setdata
        tv_invoice_type.setText(sit);
        tv_invoice_no.setText("Invoice No: " + invoiceNo);
        tv_merchant_name.setText(merchant_name);
        tv_merchant_address.setText(merchant_address);
        tv_billdate.setText("Bill Date :  " + billdate);
        tv_deliverydate.setText("Delivery Date :" + delivaryDate);
        tv_salesrep_name.setText(salesrep_name);
        tv_salesrep_cono.setText(salesrep_cono);
        totaltotal.setText(String.format("Rs. %.2f", grandTotal));//
        totalvat.setText(String.format("Rs. %.2f", totalVat));
        freeIssues.setText(String.format("Rs. %.2f", freeTotal));//correct
        grosstotal.setText(String.format("Rs. %.2f", grossTotal));
        tvPreviousReturn.setText(String.format("Rs. %.2f", previousReturnAmount));//correct

        System.out.println("grossTotal - " + grossTotal);
        System.out.println("previousReturnAmount - " + previousReturnAmount);


    }


    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    private void print_bill_woosim_header(int invoice_type, int invoice_no, String merchant_name,
                                          String merchant_address, String dis_name, String dis_address, String billdate) {
        //64CPL
        //57cpl if we add 1 bit space for each charactor

        String invoice_no_ = "", merchantName = "", merchantAddress = "", distriName = "", destriAddress = "";

        String invoiceType = "";

        if (isVat == 0) {
            invoiceType = "SALES";
        } else if (isVat == 1) {
            invoiceType = "  TAX";
        }


        if (String.valueOf(invoice_no).length() == 14) {
            invoice_no_ = invoice_no + sp[0];
        } else if (String.valueOf(invoice_no).length() == 13) {
            invoice_no_ = sp[1] + invoice_no;
        } else if (String.valueOf(invoice_no).length() == 12) {
            invoice_no_ = sp[1] + invoice_no + sp[1];
        } else if (String.valueOf(invoice_no).length() == 11) {
            invoice_no_ = sp[1] + invoice_no + sp[2];
        } else if (String.valueOf(invoice_no).length() == 10) {
            invoice_no_ = sp[2] + invoice_no + sp[2];
        } else if (String.valueOf(invoice_no).length() == 9) {
            invoice_no_ = sp[2] + invoice_no + sp[3];
        } else if (String.valueOf(invoice_no).length() == 8) {
            invoice_no_ = sp[2] + invoice_no + sp[4];
        } else if (String.valueOf(invoice_no).length() == 7) {
            invoice_no_ = sp[2] + invoice_no + sp[5];
        } else if (String.valueOf(invoice_no).length() == 6) {
            invoice_no_ = sp[2] + invoice_no + sp[6];
        } else if (String.valueOf(invoice_no).length() == 5) {
            invoice_no_ = sp[2] + invoice_no + sp[7];
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

        BILL = "\n\n                      " + invoiceType + " INVOICE                      \n\n\n";
        BILL = BILL + "Customer" + sp[26] + "InvoiceNo" + invoice_no_;          //8,22,13,14
        BILL = BILL + sp[5] + merchantName + "InvoiceDate " + billdate + "\n";                       //5,29,12,10
        //BILL = BILL + sp[5] + dis_address_ + "\n\n";                //5,29 13,10
        for (String x : merAdd) {
            System.out.println("x - " + x);
            BILL = BILL + sp[5] + x + "\n";
        }
        BILL = BILL + "Distributor" + sp[46] + "\n";
        BILL = BILL + sp[5] + distriName + "\n";                                             //5,52
        BILL = BILL + sp[5] + destriAddress + "\n";                                        //5,52
        BILL = BILL + "---------------------------------------------------------\n\n";
        BILL = BILL + "   Description    " + "   QTY   " + "    UnitPrice   " + "        Amount\n";    //18,9,16,16
        BILL = BILL + "---------------------------------------------------------\n";
        System.out.println(BILL);
    }

    private void print_bill_woosim_dataList(String desc, int itemcode, int qty, int free_qty, double unitprice) {


        //64CPL
        //57cpl if we add 1 bit space for each charactor
        String desc_ = "", qty_ = "", free_qty_ = "", unitprice_ = "", rowprice_ = "";


        System.out.println("unitprice perc - " + String.format("%.2f", unitprice));
        double rowprice = qty * unitprice;
        System.out.println("rowprice perc - " + String.format("%.2f", rowprice));

        int sp1 = 46 - desc.length();   //reserved 23ch
        if (desc.length() >= 46) {     //description
            desc_ = desc.substring(0, 46);
        } else if (desc.length() < 46) {
            desc_ = desc + sp[sp1];
        }

        int sp2 = 6 - String.valueOf(qty).length();       //reserved 6ch
        qty_ = sp[sp2] + qty;

        int sp3 = 13 - String.valueOf(String.format("%.2f", unitprice)).length();       //reserved 13ch
        unitprice_ = sp[sp3] + String.format("%.2f", unitprice);

        int sp4 = 14 - String.valueOf(String.format("%.2f", rowprice)).length();       //reserved 14ch
        rowprice_ = sp[sp4] + String.format("%.2f", rowprice);

        int sp20 = 4 - String.valueOf(qty).length();       //reserved 4ch
        free_qty_ = sp[sp20] + free_qty;


        BILL = BILL + desc_ + "( Free" + free_qty_ + ")" + "\n";                                                                                      //57
        BILL = BILL + sp[4] + sp[14] + qty_ + sp[3] + unitprice_ + sp[3] + rowprice_ + sp[2] + "\n\n";//4,14,6,3,13,3,14,2      //10,14,6,13,14
        System.out.println(BILL);
    }

    private void print_bill_woosim_footer(double grandtotal,
                                          double freeissus, double previous, double netvalue, double totalvat) {
        //64CPL
        //57cpl if we add 1 bit space for each charactor

        double ttl = grandtotal + totalvat;

        int spaces = 15 - String.valueOf(String.format("%.2f", ttl)).length();
        String bal = sp[spaces] + String.format("%.2f", ttl);

        int spaces2 = 15 - String.valueOf(String.format("%.2f", freeissus)).length();
        String free = sp[spaces2] + String.format("%.2f", freeissus);

        int spaces0 = 8 - String.valueOf(String.format("%.2f", totalvat)).length();
        String vat = sp[spaces0] + String.format("%.2f", totalvat);

        int spaces3 = 15 - String.valueOf(String.format("%.2f", previous)).length();
        String prev = sp[spaces3] + String.format("%.2f", previous);

        int spaces4 = 15 - String.valueOf(String.format("%.2f", netvalue)).length();
        String net = sp[spaces4] + String.format("%.2f", netvalue);

        BILL = BILL + "---------------------------------------------------------\n";
        BILL = BILL + "Grand Total" + sp[31] + bal + "\n";       //11,33,15
        BILL = BILL + "Free Issues" + sp[31] + free + "\n";       //11,31,15
        if (isVat == 1) {
            BILL = BILL + "Total Vat" + sp[40] + vat + "\n";       //9,40,8
        }
        BILL = BILL + "Previous Return" + sp[27] + prev + "\n";       //15,27,15
        BILL = BILL + "Gross Total" + sp[31] + net + "\n\n";       //11,33,15
        System.out.println(BILL);
    }

    private void print_bill_woosim_finalfooter(String date, String time, String salesrep_no, String salesrep_name) {

        String sales_rep = "";
        if (salesrep_name.length() >= 28) {
            sales_rep = salesrep_name.substring(0, 28);
        } else {
            sales_rep = salesrep_name + sp[28 - salesrep_name.length()];
        }

        /*

        BILL = BILL + "Sales Rep: " + sales_rep + "__________________"+"\n";             //11,28,18
        BILL = BILL + sp[11] + salesrep_no + sp[18] +"    Signature    "+ "\n";                //11,10,18,18
         */

        BILL = BILL + "\nPrinted Date:" + date + "  Time:" + time + sp[19] + "\n\n";                                        //13,10,7,8,19
//        BILL = BILL + "Signature _____________________"+sp[26]+"\n";
//        BILL = BILL + "Sales Rep: " + sales_rep + "\n";         //11,46
//        BILL = BILL + sp[11] + salesrep_no + sp[36] + "\n";                //11,10,36

        BILL = BILL + "Sales Rep: " + sales_rep + "__________________" + "\n";             //11,28,18
        BILL = BILL + sp[11] + salesrep_no + sp[18] + "    Signature    " + "\n";                //11,10,18,18

        BILL = BILL + "---------------------------------------------------------\n\n";
        BILL = BILL + "                Imported & distributed by                \n";
        BILL = BILL + "                scadd EXPORTS (PVT) LTD                \n";
        BILL = BILL + "           833,SIRIMAVO BANDARANAYAKE MAWATHA,           \n";
        BILL = BILL + "                       COLOMBO 14,                       \n";
        BILL = BILL + "                 TELEPHONE : 011 5224200                 \n\n\n\n";
    }

    private void print_returnnote_woosim_header(int returnnote_id, String credit_date, String delivery_date) {

        BILL = BILL + "\n\n                      RETURN NOTE                      \n";
        BILL = BILL + sp[22] + "Id - " + returnnote_id + sp[21] + "\n\n"; //24,9,24
        BILL = BILL + "Credit Date : " + credit_date + sp[33] + "\n";          //14,10,33
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

        if (String.valueOf(unit_price).length() > 12) {
            unit_price1 = String.valueOf(unit_price).substring(0, 12);
        } else if (String.valueOf(unit_price).length() <= 12) {
            unit_price1 = sp[12 - String.valueOf(unit_price).length()] + unit_price;
        }

        if (String.valueOf(linetotal).length() > 13) {
            linetotal1 = String.valueOf(linetotal).substring(0, 13);
        } else if (String.valueOf(linetotal).length() <= 13) {
            linetotal1 = sp[13 - String.valueOf(linetotal).length()] + linetotal;
        }

        BILL = BILL + "\n" + item_name1 + qty1 + unit_price1 + linetotal1;                          //27,5,12,13
        System.out.println(BILL);
    }

    private void print_returnnote_woosim_footer(double totalRetunnote) {

        String totalRetunnote_ = "";

        if (String.valueOf(totalRetunnote).length() == 10) {
            totalRetunnote_ = sp[0] + totalRetunnote;
        } else if (String.valueOf(totalRetunnote).length() == 9) {
            totalRetunnote_ = sp[1] + totalRetunnote;
        } else if (String.valueOf(totalRetunnote).length() == 8) {
            totalRetunnote_ = sp[2] + totalRetunnote;
        } else if (String.valueOf(totalRetunnote).length() == 7) {
            totalRetunnote_ = sp[3] + totalRetunnote;
        } else if (String.valueOf(totalRetunnote).length() == 6) {
            totalRetunnote_ = sp[4] + totalRetunnote;
        } else if (String.valueOf(totalRetunnote).length() == 5) {
            totalRetunnote_ = sp[5] + totalRetunnote;
        } else if (String.valueOf(totalRetunnote).length() == 4) {
            totalRetunnote_ = sp[6] + totalRetunnote;
        } else if (String.valueOf(totalRetunnote).length() == 3) {
            totalRetunnote_ = sp[7] + totalRetunnote;
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

        Intent intent = new Intent(getApplicationContext(), SalesOrderOfflineActivity.class);
        PrintInvoiceActivity.this.finish();
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

                        mBluetoothConnectProgressDialog = new ProgressDialog(PrintInvoiceActivity.this);
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
                    Intent connectIntent = new Intent(PrintInvoiceActivity.this, IRA_DeviceList.class);
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

                                for(int o = 0; o < 2 ; o++){

                                    print_bill_woosim_header(invoice_type, invoiceNo, merchant_name, merchant_address, dis_name, dis_add1, billdate);
                                    int size = itemArrayList.size();
                                    for (int i = 0; i < size; i++) {
                                        int ItemCode = itemArrayList.get(i).getProductID();
                                        String itemName = itemArrayList.get(i).getItemName();
                                        int qty = itemArrayList.get(i).getQty();
                                        double price = itemArrayList.get(i).getPrice();
                                        double total = itemArrayList.get(i).getLineTotal();
                                        int freeQTY = itemArrayList.get(i).getFreeQu();
                                        print_bill_woosim_dataList(itemName, ItemCode, qty, freeQTY, price);
                                    }

                                    print_bill_woosim_footer(grandTotal, freeTotal, previousReturnAmount_round, grossTotal, totalVat);

                                    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~for return inventory print

                                    for (int k = 0; k < returnInventories.size(); k++) {
                                        int creditnote = returnInventories.get(k).getCreditNoteNo();
                                        String deliverydate = returnInventories.get(k).getReturnDate();
                                        String creditdate = returnInventories.get(k).getSyncDate();
                                        double totalGrand = returnInventories.get(k).getTotalAmount();

//                                    int start0 = deliverydate.indexOf("(");
//                                    int end0 = deliverydate.indexOf(")");
//                                    String date0 = deliverydate.substring(start0+1,end0);

                                        int start00 = creditdate.indexOf("(");
                                        int end00 = creditdate.indexOf(")");
                                        String date00 = creditdate.substring(start00 + 1, end00);

                                        ArrayList<ReturnInventoryLineItem> lineItems = (new SyncManager(getApplicationContext())).getReturnNoteItemData(creditnote);
                                        print_returnnote_woosim_header(creditnote,
                                                (new DateManager()).getDateAccordingToMilliSeconds(Long.parseLong(date00), "yyyy-MM-dd"),
                                                deliverydate);

                                        for (int g = 0; g < lineItems.size(); g++) {
                                            String name_ = lineItems.get(g).getProductName();
                                            int qty_ = lineItems.get(g).getQuantity();
                                            double unitprice = lineItems.get(g).getUnitPrice();
                                            double linevalue = lineItems.get(g).getTotalAmount();
                                            print_returnnote_woosim_body(name_, qty_, unitprice, linevalue);
                                        }
                                        print_returnnote_woosim_footer(totalGrand);
                                    }
                                    print_bill_woosim_finalfooter(today, justTime, salesrep_cono, salesrep_name);


                                    os.write(IRA_PrintCommand.SET_RIGHTSIDE_CHARCTOR_SIZE);
                                    os.write(BILL.getBytes());
                                }

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
            Toast.makeText(PrintInvoiceActivity.this, "DeviceConnected", Toast.LENGTH_SHORT).show();
            //closeConnectionWindow();
        }
    };

    public static byte intToByteArray(int value) {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();

        for (int k = 0; k < b.length; k++) {
            //System.out.println("Selva   [" + k + "] = " + "0x" + IRA_UnicodeFormatter.byteToHex(b[k]));
        }
        return b[3];
    }

    public byte[] sel(int val) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putInt(val);
        buffer.flip();
        return buffer.array();
    }

    private void print_image() {
        //Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.scadd);
        //Bitmap bmp = BitmapFactory.decodeFile("/storage/sdcard1/image/scadd.jpg"); //m2
        Bitmap bmp = BitmapFactory.decodeFile("/storage/emulated/0/images/scadd.jpg"); //samsung tab,huwawey tab

        convertBitmap(bmp);

        try {
            os.write(IRA_PrintCommand.SET_LINE_SPACING_24);

            int offset = 0;
            while (offset < bmp.getHeight()) {
                os.write("         ".getBytes()); //give 9 charactors space from start
                os.write(IRA_PrintCommand.SELECT_BIT_IMAGE_MODE);

                for (int x = 0; x < bmp.getWidth(); ++x) {
                    for (int k = 0; k < 3; ++k) {
                        byte slice = 0;
                        for (int b = 0; b < 8; ++b) {
                            int y = (((offset / 8) + k) * 8) + b;
                            int i = (y * bmp.getWidth()) + x;
                            boolean v = false;
                            if (i < dots.length()) {
                                v = dots.get(i);
                            }
                            slice |= (byte) ((v ? 1 : 0) << (7 - b));
                        }
                        os.write(slice);
                    }
                }
                offset += 24;
                os.write(IRA_PrintCommand.FEED_LINE);//there was all 7 lines
            }
            os.write(IRA_PrintCommand.SET_LINE_SPACING_30);
        } catch (Exception e) {
            System.out.println("e4 - " + e.toString());
        }
    }

    public String convertBitmap(Bitmap inputBitmap) {

        mWidth = inputBitmap.getWidth();
        mHeight = inputBitmap.getHeight();
        convertArgbToGrayscale(inputBitmap, mWidth, mHeight);
        mStatus = "ok";
        return mStatus;
    }

    private void convertArgbToGrayscale(Bitmap bmpOriginal, int width, int height) {
        int pixel;
        int k = 0;
        int B = 0, G = 0, R = 0;
        dots = new BitSet();
        try {
            for (int x = 0; x < height; x++) {
                for (int y = 0; y < width; y++) {
                    // get one pixel color
                    pixel = bmpOriginal.getPixel(y, x);

                    // retrieve color of all channels
                    R = Color.red(pixel);
                    G = Color.green(pixel);
                    B = Color.blue(pixel);
                    // take conversion up to one single value by calculating
                    // pixel intensity.
                    R = G = B = (int) (0.299 * R + 0.587 * G + 0.114 * B);
                    // set bit into bitset, by calculating the pixel's luma
                    if (R < 55) {
                        dots.set(k);//this is the bitset that i'm printing
                    }
                    k++;
                }
            }
        } catch (Exception e) {
            System.out.println("error 3 - " + e.toString());
        }
    }

    private void getDistributorDetails(int distributorId) {

        String url_distri = HTTPPaths.seriveUrl + "GetDEFDistributorByDistributorId?distributorId=" + distributorId;

        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url_distri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JsonObject object = Json.parse(response).asObject();
                        int id = object.get("ID").asInt();

                        if (id == 200) {
                            String newUrl = response.replace("{\"Data\":\"", "{\"Data\": ");
                            String new2Url = newUrl.replace("\",\"ID\":200}", "}");
                            String new3Url = new2Url.replace("\\", "");
                            if (response != null) {
                                try {
                                    JSONObject jsonObj_main = new JSONObject(new3Url);
                                    System.out.println("url3 " + new3Url);

                                    JSONObject jsonObject = jsonObj_main.getJSONObject("Data");
                                    //System.out.println("Size ------ "+jsonObject.length());
                                    dis_name = jsonObject.getString("DistributorName");
                                    dis_add1 = jsonObject.getString("Address1");

                                    //get and set from another service
                                    tv_dis_name.setText(dis_name);
                                    tv_dis_address.setText(dis_add1);
                                    setDistributorDetails(dis_name, dis_add1);

                                    System.out.println("dis name - " + dis_name);

                                } catch (Exception e) {
                                }

                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest2);
    }

    private void setDistributorDetails(String dis_name, String dis_add1) {
        DistributorName = dis_name;
        DistributorAddres1 = dis_add1;
    }

    private void insertNavigationDraer(Bundle savedInstanceState) {
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
                .withTranslucentStatusBar(false)
                .withAccountHeader(headerResult) //set the AccountHeader  created earlier for the header
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Home").withIcon(GoogleMaterial.Icon.gmd_home).withIdentifier(1).withSelectedColor(3),
                        new PrimaryDrawerItem().withName("Sync Sales Orders").withIcon(GoogleMaterial.Icon.gmd_sync).withIdentifier(2).withSelectedColor(2),
                        // new PrimaryDrawerItem().withName("Sales").withIcon(FontAwesome.Icon.faw_shopping_cart).withIdentifier(3).withBadge(getSalesOrders()).withBadgeStyle(new BadgeStyle(Color.RED, Color.RED)),
                        new PrimaryDrawerItem().withName("Sync Manual").withIcon(FontAwesome.Icon.faw_chain_broken).withIdentifier(3),
                        new PrimaryDrawerItem().withName("My Orders").withIcon(FontAwesome.Icon.faw_shopping_cart).withIdentifier(4).withBadge(new SyncManager(PrintInvoiceActivity.this).getSalesOrders()).withBadgeStyle(new BadgeStyle(Color.RED, Color.RED)),
                        new PrimaryDrawerItem().withName("Messages").withIcon(GoogleMaterial.Icon.gmd_message).withIdentifier(5).withBadgeStyle(new BadgeStyle(Color.RED, Color.RED)),
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


                        if (((Nameable) drawerItem).getName().getText(PrintInvoiceActivity.this) == "Shopping Cart") {

                        }

                        if (((Nameable) drawerItem).getName().getText(PrintInvoiceActivity.this) == "Home") {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }

                        if (((Nameable) drawerItem).getName().getText(PrintInvoiceActivity.this) == "Sync Sales Orders") {
                            Intent intent = new Intent(getApplicationContext(), SyncSalesOrdersActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }

                        if (((Nameable) drawerItem).getName().getText(PrintInvoiceActivity.this) == "Sync Manual") {

                            Intent intent = new Intent(getApplicationContext(), LoginSyncActivityDrawer.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("userId", userId);
                            intent.putExtra("userName", userName);
                            startActivity(intent);
                            finish();

                        }
                        if (((Nameable) drawerItem).getName().getText(PrintInvoiceActivity.this) == "My Orders") {
                            Intent intent = new Intent(getApplicationContext(), SalesOrderToInvoice.class);
                            startActivity(intent);
                        }
                        if (((Nameable) drawerItem).getName().getText(PrintInvoiceActivity.this) == "Settings") {

                        }

                        if (((Nameable) drawerItem).getName().getText(PrintInvoiceActivity.this) == "Logout") {
                            SessionManager.Logut();
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
}
