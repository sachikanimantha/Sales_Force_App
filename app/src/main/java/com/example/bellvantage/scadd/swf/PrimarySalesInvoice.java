package com.example.bellvantage.scadd.swf;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.example.bellvantage.scadd.DB.DbHelper.COL_PRIMARY_SALES_INVOICE_DeliveredDate;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_PRIMARY_SALES_INVOICE_EstimatedDeliveryDate;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_PRIMARY_SALES_INVOICE_ExpectedDeliveryDate;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_PRIMARY_SALES_INVOICE_InvoiceNumber;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_PRIMARY_SALES_INVOICE_OrderDate;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_PRIMARY_SALES_INVOICE_SalesInvoiceNumber;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_PRIMARY_SALES_INVOICE_SalesRepID;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_PRIMARY_SALES_INVOICE_StatusId;

/**
 * Created by Sachika on 15/11/2017.
 */

public class PrimarySalesInvoice implements Serializable {


    /**
     * InvoiceNumber : C065747
     * OrderDate : /Date(1510597800000)/
     * StatusId : 0
     * EstimatedDeliveryDate : /Date(1510597800000)/
     * IsApproved : 0
     * ApprovedUser :
     * ApprovedDate : /Date(1510597800000)/
     * ExpectedDeliveryDate : /Date(1510597800000)/
     * IsDelivered : 0
     * DeliveredDate : /Date(1510715274610)/
     * ClearingAgentName :
     * ClearingAgentContactNo :
     * VehicleNumber :
     * SalesInvoiceNumber : 0
     * SalesRepID : 42
     * ListPrimarySalesInvoiceLineItem : [{"ProductId":1,"InvoiceNumber":"C065747","OrderQty":4,"SizeOfUnit":0.25,"_PrimarySalesInvoice":null,"_DefProduct":null},{"ProductId":12,"InvoiceNumber":"C065747","OrderQty":5,"SizeOfUnit":0.5,"_PrimarySalesInvoice":null,"_DefProduct":null},{"ProductId":20,"InvoiceNumber":"C065747","OrderQty":6,"SizeOfUnit":0.95,"_PrimarySalesInvoice":null,"_DefProduct":null}]
     */

    private String InvoiceNumber;
    private String OrderDate;
    private int StatusId;
    private String EstimatedDeliveryDate;
    private String ExpectedDeliveryDate;
    private String DeliveredDate;
    private int SalesInvoiceNumber;
    private int SalesRepID;
    private ArrayList<PrimarySalesInvoiceLineItemBean> ListPrimarySalesInvoiceLineItemArrayList;


    public PrimarySalesInvoice() {
    }

    public PrimarySalesInvoice(String invoiceNumber, String orderDate,
                               int statusId, String estimatedDeliveryDate,
                               String expectedDeliveryDate, String deliveredDate,
                               int salesInvoiceNumber, int salesRepID) {
        InvoiceNumber = invoiceNumber;
        OrderDate = orderDate;
        StatusId = statusId;
        EstimatedDeliveryDate = estimatedDeliveryDate;
        ExpectedDeliveryDate = expectedDeliveryDate;
        DeliveredDate = deliveredDate;
        SalesInvoiceNumber = salesInvoiceNumber;
        SalesRepID = salesRepID;
    }

    public static PrimarySalesInvoice objectFromData(String str) {

        return new Gson().fromJson(str, PrimarySalesInvoice.class);
    }

    public static PrimarySalesInvoice objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), PrimarySalesInvoice.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<PrimarySalesInvoice> arrayPrimarySalesInvoiceFromData(String str) {

        Type listType = new TypeToken<ArrayList<PrimarySalesInvoice>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<PrimarySalesInvoice> arrayPrimarySalesInvoiceFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<PrimarySalesInvoice>>() {
            }.getType();

            return new Gson().fromJson(jsonObject.getString(str), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList();


    }

    public String getInvoiceNumber() {
        return InvoiceNumber;
    }

    public void setInvoiceNumber(String InvoiceNumber) {
        this.InvoiceNumber = InvoiceNumber;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String OrderDate) {
        this.OrderDate = OrderDate;
    }

    public int getStatusId() {
        return StatusId;
    }

    public void setStatusId(int StatusId) {
        this.StatusId = StatusId;
    }

    public String getEstimatedDeliveryDate() {
        return EstimatedDeliveryDate;
    }

    public void setEstimatedDeliveryDate(String EstimatedDeliveryDate) {
        this.EstimatedDeliveryDate = EstimatedDeliveryDate;
    }

    public String getExpectedDeliveryDate() {
        return ExpectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(String ExpectedDeliveryDate) {
        this.ExpectedDeliveryDate = ExpectedDeliveryDate;
    }


    public String getDeliveredDate() {
        return DeliveredDate;
    }

    public void setDeliveredDate(String DeliveredDate) {
        this.DeliveredDate = DeliveredDate;
    }


    public int getSalesInvoiceNumber() {
        return SalesInvoiceNumber;
    }

    public void setSalesInvoiceNumber(int SalesInvoiceNumber) {
        this.SalesInvoiceNumber = SalesInvoiceNumber;
    }

    public int getSalesRepID() {
        return SalesRepID;
    }

    public void setSalesRepID(int SalesRepID) {
        this.SalesRepID = SalesRepID;
    }

    public ArrayList<PrimarySalesInvoiceLineItemBean> getListPrimarySalesInvoiceLineItemArrayList() {
        return ListPrimarySalesInvoiceLineItemArrayList;
    }

    public void setListPrimarySalesInvoiceLineItemArrayList(ArrayList<PrimarySalesInvoiceLineItemBean> ListPrimarySalesInvoiceLineItem) {
        this.ListPrimarySalesInvoiceLineItemArrayList = ListPrimarySalesInvoiceLineItem;
    }


    public ContentValues getContentValues(){

        ContentValues cv = new ContentValues();

        cv.put(COL_PRIMARY_SALES_INVOICE_InvoiceNumber,getInvoiceNumber());
        cv.put(COL_PRIMARY_SALES_INVOICE_OrderDate,getOrderDate());
        cv.put(COL_PRIMARY_SALES_INVOICE_StatusId,getStatusId());
        cv.put(COL_PRIMARY_SALES_INVOICE_EstimatedDeliveryDate,getEstimatedDeliveryDate());
        cv.put(COL_PRIMARY_SALES_INVOICE_ExpectedDeliveryDate,getExpectedDeliveryDate());
        cv.put(COL_PRIMARY_SALES_INVOICE_DeliveredDate,getDeliveredDate());
        cv.put(COL_PRIMARY_SALES_INVOICE_SalesInvoiceNumber,getSalesInvoiceNumber());
        cv.put(COL_PRIMARY_SALES_INVOICE_SalesRepID,getSalesRepID());

        return cv;

    }


    public PrimarySalesInvoice getDbInvoce(Cursor cursor){
        PrimarySalesInvoice primarySalesInvoice = new PrimarySalesInvoice();

        primarySalesInvoice.setInvoiceNumber(cursor.getString(cursor.getColumnIndex(COL_PRIMARY_SALES_INVOICE_InvoiceNumber)));
        primarySalesInvoice.setOrderDate(cursor.getString(cursor.getColumnIndex(COL_PRIMARY_SALES_INVOICE_OrderDate)));
        primarySalesInvoice.setStatusId(cursor.getInt(cursor.getColumnIndex(COL_PRIMARY_SALES_INVOICE_StatusId)));
        primarySalesInvoice.setEstimatedDeliveryDate(cursor.getString(cursor.getColumnIndex(COL_PRIMARY_SALES_INVOICE_EstimatedDeliveryDate)));
        primarySalesInvoice.setExpectedDeliveryDate(cursor.getString(cursor.getColumnIndex(COL_PRIMARY_SALES_INVOICE_ExpectedDeliveryDate)));
        primarySalesInvoice.setDeliveredDate(cursor.getString(cursor.getColumnIndex(COL_PRIMARY_SALES_INVOICE_DeliveredDate)));
        primarySalesInvoice.setSalesInvoiceNumber(cursor.getInt(cursor.getColumnIndex(COL_PRIMARY_SALES_INVOICE_SalesInvoiceNumber)));
        primarySalesInvoice.setSalesRepID(cursor.getInt(cursor.getColumnIndex(COL_PRIMARY_SALES_INVOICE_SalesRepID)));

        return primarySalesInvoice;
    }




}
