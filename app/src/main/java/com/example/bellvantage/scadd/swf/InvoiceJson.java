package com.example.bellvantage.scadd.swf;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Bellvantage on 14/09/2017.
 */

public class InvoiceJson implements Serializable {

    /**
     * InvoiceNumber : 0
     * EstimateDeliveryDate : /Date(1503167400000)/
     * TotalDiscount : 0.0
     * TotalAmount : 2000.0
     * SyncDate : /Date(1505368476165)/
     * IsSync : 0
     * SaleStatus : 2
     * EnteredUser : HARSHA
     * EnteredDate : /Date(1503291696157)/
     * UpdatedUser :
     * UpdatedDate : /Date(1505368476165)/
     * SaleDate : /Date(1503253800000)/
     * SaleTypeId : 0
     * SalesRepId : 42
     * DitributorId : 1
     * MerchantId : 56056
     * SalesOrderId : 104210125
     * TotalVAT : 0.0
     * IsVat : 0
     * IsReturn : 0
     * OutstandingAmountInvoice : 1900.0
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
    int PaymentType;
    private int IsReturn;
    private double OutstandingAmountInvoice;
    ArrayList<InvoiceLineItemJson> _ListSalesOrderLineItem;

    public InvoiceJson(int invoiceNumber, String estimateDeliveryDate, double totalDiscount,
                       double totalAmount, String syncDate, int isSync, int saleStatus,
                       String enteredUser, String enteredDate, String updatedUser,
                       String updatedDate, String saleDate, int saleTypeId,
                       int salesRepId, int ditributorId, int merchantId, int salesOrderId,
                       double totalVAT, int isVat, int isCredit,String creditDays,int paymentType,int isReturn, double outstandingAmountInvoice,
                       ArrayList<InvoiceLineItemJson> _ListSalesOrderLineItem) {
        InvoiceNumber = invoiceNumber;
        EstimateDeliveryDate = estimateDeliveryDate;
        TotalDiscount = totalDiscount;
        TotalAmount = totalAmount;
        SyncDate = syncDate;
        IsSync = isSync;
        SaleStatus = saleStatus;
        EnteredUser = enteredUser;
        EnteredDate = enteredDate;
        UpdatedUser = updatedUser;
        UpdatedDate = updatedDate;
        SaleDate = saleDate;
        SaleTypeId = saleTypeId;
        SalesRepId = salesRepId;
        DitributorId = ditributorId;
        MerchantId = merchantId;
        SalesOrderId = salesOrderId;
        TotalVAT = totalVAT;
        IsVat = isVat;
        this.IsCredit = isCredit;
        this.CreditDays = creditDays;
        IsVat = isVat;
        this.PaymentType=paymentType;
        IsReturn = isReturn;
        OutstandingAmountInvoice = outstandingAmountInvoice;
        this._ListSalesOrderLineItem = _ListSalesOrderLineItem;
    }

    public ArrayList<InvoiceLineItemJson> get_ListSalesOrderLineItem() {
        return _ListSalesOrderLineItem;
    }

    public void set_ListSalesOrderLineItem(ArrayList<InvoiceLineItemJson> _ListSalesOrderLineItem) {
        this._ListSalesOrderLineItem = _ListSalesOrderLineItem;
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

    public double getOutstandingAmountInvoice() {
        return OutstandingAmountInvoice;
    }

    public void setOutstandingAmountInvoice(double OutstandingAmountInvoice) {
        this.OutstandingAmountInvoice = OutstandingAmountInvoice;
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

    public int getPaymentType() {
        return PaymentType;
    }

    public void setPaymentType(int paymentType) {
        PaymentType = paymentType;
    }
}
