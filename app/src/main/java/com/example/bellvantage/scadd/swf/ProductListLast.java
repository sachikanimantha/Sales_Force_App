package com.example.bellvantage.scadd.swf;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Bellvantage on 21/06/2017.
 */

public class ProductListLast implements Serializable{

    int SalesOrderID;
    int BatchID;
    int ProductID;
    int Quantity;
    int FreeQuantity;
    double UnitSellingPrice;
    double UnitSellingDiscount;
    double TotalAmount;
    double TotalDiscount;
    double ItemVatAmount;
    int IsSync;
    String SyncDate;

    //unwanted to upload
    String name;
    String expiredate;
    int stock;
    int img;
    int isVat;
    double vatRate;

    double lineTotal;
    double freeTotal;
    double grossTotal;
    //int positionOnList;


    public ProductListLast(int salesOrderID, int batchID, int productID, int quantity, int freeQuantity,
                           double unitSellingPrice, double unitSellingDiscount, double totalAmount,
                           double totalDiscount, double itemVatAmount, int isSync,
                           String name, String expiredate, int stock, int img, int isVat, double vatRate,
                           double lineTotal, double freeTotal, double grossTotal) {

        SalesOrderID = salesOrderID;
        BatchID = batchID;
        ProductID = productID;
        Quantity = quantity;
        FreeQuantity = freeQuantity;
        UnitSellingPrice = unitSellingPrice;
        UnitSellingDiscount = unitSellingDiscount;
        TotalAmount = totalAmount;
        TotalDiscount = totalDiscount;
        ItemVatAmount = itemVatAmount;
        IsSync = isSync;
        this.name = name;
        this.expiredate = expiredate;
        this.stock = stock;
        this.img = img;
        this.isVat = isVat;
        this.vatRate = vatRate;
        this.lineTotal = lineTotal;
        this.freeTotal = freeTotal;
        this.grossTotal = grossTotal;
    }

    //all
    public ProductListLast(int salesOrderID, int batchID, int productID,
                           int quantity, int freeQuantity, double unitSellingPrice,
                           double unitSellingDiscount, double totalAmount,
                           double totalDiscount, int isSync, String name,
                           String expiredate, int stock, int img, int isVat,
                           double vatRate,double itemVatA,String syncDate) {
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
        this.name = name;
        this.expiredate = expiredate;
        this.stock = stock;
        this.img = img;
        this.isVat = isVat;
        this.vatRate = vatRate;
        this.ItemVatAmount = itemVatA;
        this.SyncDate = syncDate;
       // this.positionOnList = positionOnList;
    }

    //only link ask
    public ProductListLast(int salesOrderID, int batchID, int productID, int quantity,
                           int freeQuantity, double unitSellingPrice, double unitSellingDiscount,
                           double totalAmount, double totalDiscount, int isSync,double itemVatA) {
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
        this.ItemVatAmount = itemVatA;
    }

    public String getSyncDate() {
        return SyncDate;
    }

    public void setSyncDate(String syncDate) {
        SyncDate = syncDate;
    }

    public int getSalesOrderID() {
        return SalesOrderID;
    }

    public void setSalesOrderID(int salesOrderID) {
        SalesOrderID = salesOrderID;
    }

    public int getBatchID() {
        return BatchID;
    }

    public void setBatchID(int batchID) {
        BatchID = batchID;
    }

    public int getProductID() {
        return ProductID;
    }

    public void setProductID(int productID) {
        ProductID = productID;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public int getFreeQuantity() {
        return FreeQuantity;
    }

    public void setFreeQuantity(int freeQuantity) {
        FreeQuantity = freeQuantity;
    }

    public double getUnitSellingPrice() {
        return UnitSellingPrice;
    }

    public void setUnitSellingPrice(double unitSellingPrice) {
        UnitSellingPrice = unitSellingPrice;
    }

    public double getUnitSellingDiscount() {
        return UnitSellingDiscount;
    }

    public void setUnitSellingDiscount(double unitSellingDiscount) {
        UnitSellingDiscount = unitSellingDiscount;
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

    public int getIsSync() {
        return IsSync;
    }

    public void setIsSync(int isSync) {
        IsSync = isSync;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpiredate() {
        return expiredate;
    }

    public void setExpiredate(String expiredate) {
        this.expiredate = expiredate;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public int getIsVat() {
        return isVat;
    }

    public void setIsVat(int isVat) {
        this.isVat = isVat;
    }

    public double getVatRate() {
        return vatRate;
    }

    public void setVatRate(double vatRate) {
        this.vatRate = vatRate;
    }

    public double getItemVatAmount() {
        return ItemVatAmount;
    }

    public void setItemVatAmount(double itemVatAmount) {
        ItemVatAmount = itemVatAmount;
    }

    public double getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(double lineTotal) {
        this.lineTotal = lineTotal;
    }

    public double getFreeTotal() {
        return freeTotal;
    }

    public void setFreeTotal(double freeTotal) {
        this.freeTotal = freeTotal;
    }

    public double getGrossTotal() {
        return grossTotal;
    }

    public void setGrossTotal(double grossTotal) {
        this.grossTotal = grossTotal;
    }


}
