package com.example.bellvantage.scadd.swf;

import java.io.Serializable;

/**
 * Created by Bellvantage on 14/09/2017.
 */

public class InvoiceLineItemJson implements Serializable {


    /**
     * SalesOrderID : 104210125
     * BatchID : 1
     * ProductID : 6
     * Quantity : 10
     * FreeQuantity : 0
     * UnitSellingPrice : 100.0
     * UnitSellingDiscount : 0.0
     * TotalAmount : 1000.0
     * TotalDiscount : 0.0
     * IsSync : 0
     * SyncDate : /Date(1505368478490)/
     * ProductName :
     */

    private int SalesOrderID;
    private int BatchID;
    private int ProductID;
    private int Quantity;
    private int FreeQuantity;
    private double UnitSellingPrice;
    private double UnitSellingDiscount;
    private double TotalAmount;
    private double TotalDiscount;
    private int IsSync;
    private String SyncDate;
    private String ProductName;

    public InvoiceLineItemJson(int salesOrderID, int batchID, int productID,
                               int quantity, int freeQuantity, double unitSellingPrice,
                               double unitSellingDiscount, double totalAmount,
                               double totalDiscount, int isSync, String syncDate, String productName) {
        SalesOrderID = salesOrderID;
        BatchID = batchID;
        ProductID = productID;
        Quantity = quantity;
        FreeQuantity = freeQuantity;
        UnitSellingPrice = unitSellingPrice;
        UnitSellingDiscount = unitSellingDiscount;
        TotalAmount = totalAmount;
        TotalDiscount = totalDiscount;
        IsSync = isSync;
        SyncDate = syncDate;
        ProductName = productName;
    }

    public int getSalesOrderID() {
        return SalesOrderID;
    }

    public void setSalesOrderID(int SalesOrderID) {
        this.SalesOrderID = SalesOrderID;
    }

    public int getBatchID() {
        return BatchID;
    }

    public void setBatchID(int BatchID) {
        this.BatchID = BatchID;
    }

    public int getProductID() {
        return ProductID;
    }

    public void setProductID(int ProductID) {
        this.ProductID = ProductID;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int Quantity) {
        this.Quantity = Quantity;
    }

    public int getFreeQuantity() {
        return FreeQuantity;
    }

    public void setFreeQuantity(int FreeQuantity) {
        this.FreeQuantity = FreeQuantity;
    }

    public double getUnitSellingPrice() {
        return UnitSellingPrice;
    }

    public void setUnitSellingPrice(double UnitSellingPrice) {
        this.UnitSellingPrice = UnitSellingPrice;
    }

    public double getUnitSellingDiscount() {
        return UnitSellingDiscount;
    }

    public void setUnitSellingDiscount(double UnitSellingDiscount) {
        this.UnitSellingDiscount = UnitSellingDiscount;
    }

    public double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(double TotalAmount) {
        this.TotalAmount = TotalAmount;
    }

    public double getTotalDiscount() {
        return TotalDiscount;
    }

    public void setTotalDiscount(double TotalDiscount) {
        this.TotalDiscount = TotalDiscount;
    }

    public int getIsSync() {
        return IsSync;
    }

    public void setIsSync(int IsSync) {
        this.IsSync = IsSync;
    }

    public String getSyncDate() {
        return SyncDate;
    }

    public void setSyncDate(String SyncDate) {
        this.SyncDate = SyncDate;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String ProductName) {
        this.ProductName = ProductName;
    }
}
