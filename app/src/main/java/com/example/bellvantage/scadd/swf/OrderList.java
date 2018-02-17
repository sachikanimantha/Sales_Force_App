package com.example.bellvantage.scadd.swf;

import android.content.ContentValues;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.example.bellvantage.scadd.DB.DbHelper.COL_SALES_ORDER_CreditDate;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SALES_ORDER_CreditType;

/**
 * Created by Bellvantage on 25/07/2017.
 */

public class OrderList implements Serializable {


    /**
     * InvoiceNumber : 0
     * EstimateDeliveryDate : Date(1500957362263)
     * TotalDiscount : 0.0
     * TotalAmount : 0.0
     * SyncDate : Date(1500957362263)
     * IsSync : 0
     * SaleStatus : 1
     * EnteredUser : 3
     * EnteredDate : Date(1497524465660)
     * UpdatedUser :
     * UpdatedDate : Date(1500957362263)
     * SaleDate : Date(1497465000000)
     * SaleTypeId : 0
     * SalesRepId : 3
     * DitributorId : 1
     * MerchantId : 1
     * SalesOrderId : 100346
     * TotalVAT : 0
     * IsVat : 0
     * IsReturn : 0
     * ListSalesInvoice : null
     * _ListSalesOrderLineItem : [{"SalesOrderID":100346,"BatchID":1,"ProductID":1,"Quantity":2,"FreeQuantity":0,"UnitSellingPrice":240.25,"UnitSellingDiscount":0,"TotalAmount":480.5,"TotalDiscount":0,"IsSync":0,"SyncDate":"Date(1500957362360)","_SalesOrder":null,"_ProductBatch":null,"ProductName":null}]
     * _SalesOrderLineItem : null
     * _DefSalesRef : null
     * _DefMerchant : null
     */

    private int InvoiceNumber;
    private String EstimateDeliveryDate;
    private double TotalDiscount;
    private double TotalAmount;
    private String SyncDate;
    private int IsSync;
    private int SaleStatus;
    private String EnteredUser;
    private String EnteredDate;
    private String UpdatedUser;
    private String UpdatedDate;
    private String SaleDate;
    private int SaleTypeId;
    private int SalesRepId;
    private int DitributorId;
    private int MerchantId;
    private int SalesOrderId;
    private double TotalVAT;
    private int IsVat;
    private int IsCredit;
    private String CreditDays;
    private int IsReturn;
    private String MerchntName;
    private Object ListSalesInvoice;
    private Object _SalesOrderLineItem;
    private Object _DefSalesRef;
    private Object _DefMerchant;
    private List<SalesOrderLineItem> _ListSalesOrderLineItem;

    public OrderList(int invoiceNumber, String estimateDeliveryDate, double totalDiscount,
                     double totalAmount, String syncDate, int isSync,
                     int saleStatus, String enteredUser, String enteredDate,
                     String updatedUser, String updatedDate, String saleDate,
                     int saleTypeId, int salesRepId, int ditributorId,
                     int merchantId, int salesOrderId, double totalVAT,
                     int isVat, int isReturn,
                     String merchntName,int isCredit,String creditDays) {

        this.setInvoiceNumber(invoiceNumber);
        this.setEstimateDeliveryDate(estimateDeliveryDate);
        this.setTotalDiscount(totalDiscount);
        this.setTotalAmount(totalAmount);
        this.setSyncDate(syncDate);
        this.setIsSync(isSync);
        this.setSaleStatus(saleStatus);
        this.setEnteredUser(enteredUser);
        this.setEnteredDate(enteredDate);
        this.setUpdatedUser(updatedUser);
        this.setUpdatedDate(updatedDate);
        this.setSaleDate(saleDate);
        this.setSaleTypeId(saleTypeId);
        this.setSalesRepId(salesRepId);
        this.setDitributorId(ditributorId);
        this.setMerchantId(merchantId);
        this.setSalesOrderId(salesOrderId);
        this.setTotalVAT(totalVAT);
        this.setIsVat(isVat);
        this.setIsReturn(isReturn);
        this.setMerchntName(merchntName);
        this.setIsCredit(isCredit);
        this.setCreditDays(creditDays);
    }

    public int getIsCredit() {
        return IsCredit;
    }

    public void setIsCredit(int isCredit) {
        IsCredit = isCredit;
    }

    public String getCreditDays() {
        return CreditDays;
    }

    public void setCreditDays(String creditDays) {
        CreditDays = creditDays;
    }

    public static OrderList objectFromData(String str) {

        return new Gson().fromJson(str, OrderList.class);
    }

    public static OrderList objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), OrderList.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<OrderList> arrayOrderListFromData(String str) {

        Type listType = new TypeToken<ArrayList<OrderList>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<OrderList> arrayOrderListFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<OrderList>>() {
            }.getType();

            return new Gson().fromJson(jsonObject.getString(str), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList();


    }

    public String getMerchntName() {
        return MerchntName;
    }

    public void setMerchntName(String merchntName) {
        MerchntName = merchntName;
    }



    public int getInvoiceNumber() {
        return InvoiceNumber;
    }

    public void setInvoiceNumber(int InvoiceNumber) {
        this.InvoiceNumber = InvoiceNumber;
    }

    public String getEstimateDeliveryDate() {
        return EstimateDeliveryDate;
    }

    public void setEstimateDeliveryDate(String EstimateDeliveryDate) {
        this.EstimateDeliveryDate = EstimateDeliveryDate;
    }

    public double getTotalDiscount() {
        return TotalDiscount;
    }

    public void setTotalDiscount(double TotalDiscount) {
        this.TotalDiscount = TotalDiscount;
    }

    public double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(double TotalAmount) {
        this.TotalAmount = TotalAmount;
    }

    public String getSyncDate() {
        return SyncDate;
    }

    public void setSyncDate(String SyncDate) {
        this.SyncDate = SyncDate;
    }

    public int getIsSync() {
        return IsSync;
    }

    public void setIsSync(int IsSync) {
        this.IsSync = IsSync;
    }

    public int getSaleStatus() {
        return SaleStatus;
    }

    public void setSaleStatus(int SaleStatus) {
        this.SaleStatus = SaleStatus;
    }

    public String getEnteredUser() {
        return EnteredUser;
    }

    public void setEnteredUser(String EnteredUser) {
        this.EnteredUser = EnteredUser;
    }

    public String getEnteredDate() {
        return EnteredDate;
    }

    public void setEnteredDate(String EnteredDate) {
        this.EnteredDate = EnteredDate;
    }

    public String getUpdatedUser() {
        return UpdatedUser;
    }

    public void setUpdatedUser(String UpdatedUser) {
        this.UpdatedUser = UpdatedUser;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String UpdatedDate) {
        this.UpdatedDate = UpdatedDate;
    }

    public String getSaleDate() {
        return SaleDate;
    }

    public void setSaleDate(String SaleDate) {
        this.SaleDate = SaleDate;
    }

    public int getSaleTypeId() {
        return SaleTypeId;
    }

    public void setSaleTypeId(int SaleTypeId) {
        this.SaleTypeId = SaleTypeId;
    }

    public int getSalesRepId() {
        return SalesRepId;
    }

    public void setSalesRepId(int SalesRepId) {
        this.SalesRepId = SalesRepId;
    }

    public int getDitributorId() {
        return DitributorId;
    }

    public void setDitributorId(int DitributorId) {
        this.DitributorId = DitributorId;
    }

    public int getMerchantId() {
        return MerchantId;
    }

    public void setMerchantId(int MerchantId) {
        this.MerchantId = MerchantId;
    }

    public int getSalesOrderId() {
        return SalesOrderId;
    }

    public void setSalesOrderId(int SalesOrderId) {
        this.SalesOrderId = SalesOrderId;
    }

    public double getTotalVAT() {
        return TotalVAT;
    }

    public void setTotalVAT(double TotalVAT) {
        this.TotalVAT = TotalVAT;
    }

    public int getIsVat() {
        return IsVat;
    }

    public void setIsVat(int IsVat) {
        this.IsVat = IsVat;
    }

    public int getIsReturn() {
        return IsReturn;
    }

    public void setIsReturn(int IsReturn) {
        this.IsReturn = IsReturn;
    }

    public Object getListSalesInvoice() {
        return ListSalesInvoice;
    }

    public void setListSalesInvoice(Object ListSalesInvoice) {
        this.ListSalesInvoice = ListSalesInvoice;
    }

    public Object get_SalesOrderLineItem() {
        return _SalesOrderLineItem;
    }

    public void set_SalesOrderLineItem(Object _SalesOrderLineItem) {
        this._SalesOrderLineItem = _SalesOrderLineItem;
    }

    public Object get_DefSalesRef() {
        return _DefSalesRef;
    }

    public void set_DefSalesRef(Object _DefSalesRef) {
        this._DefSalesRef = _DefSalesRef;
    }

    public Object get_DefMerchant() {
        return _DefMerchant;
    }

    public void set_DefMerchant(Object _DefMerchant) {
        this._DefMerchant = _DefMerchant;
    }

    public List<SalesOrderLineItem> get_ListSalesOrderLineItem() {
        return _ListSalesOrderLineItem;
    }

    public void set_ListSalesOrderLineItem(List<SalesOrderLineItem> _ListSalesOrderLineItem) {
        this._ListSalesOrderLineItem = _ListSalesOrderLineItem;
    }

    public ContentValues getOrderListContentValues(){

        ContentValues cv = new ContentValues();

        cv.put("InvoiceNumber",getInvoiceNumber());
        cv.put("EstimateDeliveryDate",getEstimateDeliveryDate());
        cv.put("TotalDiscount",getTotalDiscount());
        cv.put("TotalAmount",getTotalAmount());
        cv.put("SyncDate",getSyncDate());
        cv.put("IsSync",getIsSync());
        cv.put("SaleStatus",getSaleStatus());
        cv.put("EnteredUser",getEnteredUser());
        cv.put("EnteredDate",getEnteredDate());
        cv.put("UpdatedUser",getUpdatedUser());
        cv.put("UpdatedDate",getUpdatedDate());
        cv.put("SaleDate",getSaleDate());
        cv.put("SaleTypeId",getSaleTypeId());
        cv.put("SalesRepId",getSalesRepId());
        cv.put("DitributorId",getDitributorId());
        cv.put("MerchantId",getMerchantId());
        cv.put("SalesOrderId",getSalesOrderId());
        cv.put("TotalVAT",getTotalVAT());
        cv.put("IsVat",getIsVat());
        cv.put("IsReturn",getIsReturn());
        cv.put("MerchntName",getMerchntName());
        cv.put(COL_SALES_ORDER_CreditType,getIsCredit());
        cv.put(COL_SALES_ORDER_CreditDate,getCreditDays());

        return  cv;

    }


}
