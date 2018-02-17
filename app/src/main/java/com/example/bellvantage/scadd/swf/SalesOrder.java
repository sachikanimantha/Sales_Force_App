package com.example.bellvantage.scadd.swf;

import android.content.ContentValues;

import com.example.bellvantage.scadd.DB.DbHelper;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Bellvantage on 30/06/2017.
 */

public class SalesOrder implements Serializable {

    int MerchantId;
    int DitributorId;
    int SalesRepId;
    int SaleTypeId;
    String EnteredDate;
    String EnteredUser;
    int SaleStatus;
    double TotalAmount;
    double TotalDiscount;
    double TotalVAT;
    String EstimateDeliveryDate;
    int IsVat;
    int IsReturn;
    int IsSync;
    String SalesrepType;
    String SaleDate;
    int InvoiceNumber;
    String SyncDate;
    int SalesOrderId;
    int creditType;
    int creditDays;

    public String getSaleDate() {
        return SaleDate;
    }

    public void setSaleDate(String saleDate) {
        SaleDate = saleDate;
    }

    public int getInvoiceNumber() {
        return InvoiceNumber;
    }

    public void setInvoiceNumber(int invoiceNumber) {
        InvoiceNumber = invoiceNumber;
    }

    public String getSyncDate() {
        return SyncDate;
    }

    public void setSyncDate(String syncDate) {
        SyncDate = syncDate;
    }

    public int getSalesOrderId() {
        return SalesOrderId;
    }

    public void setSalesOrderId(int salesOrderId) {
        SalesOrderId = salesOrderId;
    }

    ArrayList<ProductListLast> _ListSalesOrderLineItem;


    public SalesOrder(int merchantId, int ditributorId, int salesRepId,
                      int saleTypeId, String enteredDate, String enteredUser,
                      int saleStatus, double totalAmount, double totalDiscount,
                      double totalVAT, String saleDate, int isVat, int isReturn,
                      int invoiceNumber, String estimateDeliveryDate,
                      String syncDate, int isSync, int salesOrderId,
                      ArrayList<ProductListLast> _ListSalesOrderLineItem,String salesrepType) {


        MerchantId = merchantId;
        DitributorId = ditributorId;
        SalesRepId = salesRepId;
        SaleTypeId = saleTypeId;
        EnteredDate = enteredDate;
        EnteredUser = enteredUser;
        SaleStatus = saleStatus;
        TotalAmount = totalAmount;
        TotalDiscount = totalDiscount;
        TotalVAT = totalVAT;
        SaleDate = saleDate;
        IsVat = isVat;
        IsReturn = isReturn;
        InvoiceNumber = invoiceNumber;
        EstimateDeliveryDate = estimateDeliveryDate;
        SyncDate = syncDate;
        IsSync = isSync;
        SalesOrderId = salesOrderId;
        this._ListSalesOrderLineItem = _ListSalesOrderLineItem;
        SalesrepType = salesrepType;
    }

    public SalesOrder(int merchantId, int ditributorId, int salesRepId,int saleTypeId, String enteredDate, String enteredUser,
                      int saleStatus, double totalAmount, double totalDiscount, double totalVAT,String estimateDeliveryDate,
                      int isVat, int isReturn, ArrayList<ProductListLast> _ListSalesOrderLineItem,String salesrepType) {

        MerchantId = merchantId;
        DitributorId = ditributorId;
        SalesRepId = salesRepId;
        SaleTypeId = saleTypeId;
        EnteredDate = enteredDate;
        EnteredUser = enteredUser;
        SaleStatus = saleStatus;
        TotalAmount = totalAmount;
        TotalDiscount = totalDiscount;
        TotalVAT = totalVAT;
        EstimateDeliveryDate = estimateDeliveryDate;
        IsVat = isVat;
        IsReturn = isReturn;
        //IsSync = isSync;
        this._ListSalesOrderLineItem = _ListSalesOrderLineItem;
        SalesrepType = salesrepType;
    }




    public SalesOrder(int merchantId, int ditributorId, int salesRepId,
                      int saleTypeId, String enteredDate, String enteredUser,
                      int saleStatus, double totalAmount, double totalDiscount,
                      double totalVAT, String saleDate, int isVat, int isReturn,
                      int invoiceNumber, String estimateDeliveryDate,
                      String syncDate, int isSync, int salesOrderId,
                      ArrayList<ProductListLast> _ListSalesOrderLineItem,String salesrepType,int credittype,int creditdays) {


        MerchantId = merchantId;
        DitributorId = ditributorId;
        SalesRepId = salesRepId;
        SaleTypeId = saleTypeId;
        EnteredDate = enteredDate;
        EnteredUser = enteredUser;
        SaleStatus = saleStatus;
        TotalAmount = totalAmount;
        TotalDiscount = totalDiscount;
        TotalVAT = totalVAT;
        SaleDate = saleDate;
        IsVat = isVat;
        IsReturn = isReturn;
        InvoiceNumber = invoiceNumber;
        EstimateDeliveryDate = estimateDeliveryDate;
        SyncDate = syncDate;
        IsSync = isSync;
        SalesOrderId = salesOrderId;
        this._ListSalesOrderLineItem = _ListSalesOrderLineItem;
        SalesrepType = salesrepType;

        creditType = credittype;
        creditDays = creditdays;
    }

    public int getMerchantId() {
        return MerchantId;
    }

    public void setMerchantId(int merchantId) {
        MerchantId = merchantId;
    }

    public int getDitributorId() {
        return DitributorId;
    }

    public void setDitributorId(int ditributorId) {
        DitributorId = ditributorId;
    }

    public int getSalesRepId() {
        return SalesRepId;
    }

    public void setSalesRepId(int salesRepId) {
        SalesRepId = salesRepId;
    }

    public int getSaleTypeId() {
        return SaleTypeId;
    }

    public void setSaleTypeId(int saleTypeId) {
        SaleTypeId = saleTypeId;
    }

    public String getEnteredDate() {
        return EnteredDate;
    }

    public void setEnteredDate(String enteredDate) {
        EnteredDate = enteredDate;
    }

    public String getEnteredUser() {
        return EnteredUser;
    }

    public void setEnteredUser(String enteredUser) {
        EnteredUser = enteredUser;
    }

    public int getSaleStatus() {
        return SaleStatus;
    }

    public void setSaleStatus(int saleStatus) {
        SaleStatus = saleStatus;
    }

    public double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        TotalAmount = totalAmount;
    }

    public double getTotalDiscount() {
        return TotalDiscount;
    }

    public void setTotalDiscount(double totalDiscount) {
        TotalDiscount = totalDiscount;
    }

    public double getTotalVAT() {
        return TotalVAT;
    }

    public void setTotalVAT(double totalVAT) {
        TotalVAT = totalVAT;
    }

    public String getEstimateDeliveryDate() {
        return EstimateDeliveryDate;
    }

    public void setEstimateDeliveryDate(String estimateDeliveryDate) {
        EstimateDeliveryDate = estimateDeliveryDate;
    }

    public int getIsVat() {
        return IsVat;
    }

    public void setIsVat(int isVat) {
        IsVat = isVat;
    }

    public int getIsReturn() {
        return IsReturn;
    }

    public void setIsReturn(int isReturn) {
        IsReturn = isReturn;
    }

    public int getIsSync() {
        return IsSync;
    }

    public void setIsSync(int isSync) {
        IsSync = isSync;
    }

    public String getSalesrepType() {
        return SalesrepType;
    }

    public void setSalesrepType(String salesrepType) {
        SalesrepType = salesrepType;
    }

    public ArrayList<ProductListLast> get_ListSalesOrderLineItem() {
        return _ListSalesOrderLineItem;
    }

    public void set_ListSalesOrderLineItem(ArrayList<ProductListLast> _ListSalesOrderLineItem) {
        this._ListSalesOrderLineItem = _ListSalesOrderLineItem;
    }

    public int getCreditType() {
        return creditType;
    }

    public void setCreditType(int creditType) {
        this.creditType = creditType;
    }

    public int getCreditDays() {
        return creditDays;
    }

    public void setCreditDays(int creditDays) {
        this.creditDays = creditDays;
    }

    public ContentValues getSalesOrderContentValues(){

        ContentValues cv = new ContentValues();

        cv.put(DbHelper.COL_SALES_ORDER_MerchantId,getMerchantId());
        cv.put(DbHelper.COL_SALES_ORDER_DitributorId,getDitributorId());
        cv.put(DbHelper.COL_SALES_ORDER_SalesRepId,getSalesRepId());
        cv.put(DbHelper.COL_SALES_ORDER_SaleTypeId,getSaleTypeId());
        cv.put(DbHelper.COL_SALES_ORDER_EnteredDate,getEnteredDate());
        cv.put(DbHelper.COL_SALES_ORDER_EnteredUser,getEnteredUser());
        cv.put(DbHelper.COL_SALES_ORDER_SaleStatus,getSaleStatus());
        cv.put(DbHelper.COL_SALES_ORDER_TotalAmount,getTotalAmount());
        cv.put(DbHelper.COL_SALES_ORDER_TotalDiscount,getTotalDiscount());
        cv.put(DbHelper.COL_SALES_ORDER_TotalVAT,getTotalVAT());
        cv.put(DbHelper.COL_SALES_ORDER_SaleDate,getSaleDate());
        cv.put(DbHelper.COL_SALES_ORDER_IsVat,getIsVat());
        cv.put(DbHelper.COL_SALES_ORDER_IsReturn,getIsReturn());
        cv.put(DbHelper.COL_SALES_ORDER_InvoiceNumber,getInvoiceNumber());
        cv.put(DbHelper.COL_SALES_ORDER_EstimateDeliveryDate,getEstimateDeliveryDate());
        cv.put(DbHelper.COL_SALES_ORDER_SyncDate,getSyncDate());
        cv.put(DbHelper.COL_SALES_ORDER_IsSync,getIsSync());
        cv.put(DbHelper.COL_SALES_ORDER_SalesOrderId,getSalesOrderId());
        cv.put(DbHelper.COL_SALES_ORDER_CreditType,getCreditType());
        cv.put(DbHelper.COL_SALES_ORDER_CreditDate,getCreditDays());

        return  cv;
    }


}
