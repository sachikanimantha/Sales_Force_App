package com.example.bellvantage.scadd.swf;

/**
 * Created by Sachika on 5/29/2017.
 */

public class ListSalesOrderLineItem {

    public int SalesOrderNumber;
    public String ItemCode;
    public int DozenQty;
    public int BottleQty;
    public int Discount;
    public int NewPrice;
    public Products ProductDetail;

    public int getSalesOrderNumber() {
        return SalesOrderNumber;
    }

    public void setSalesOrderNumber(int salesOrderNumber) {
        SalesOrderNumber = salesOrderNumber;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public int getDozenQty() {
        return DozenQty;
    }

    public void setDozenQty(int dozenQty) {
        DozenQty = dozenQty;
    }

    public int getBottleQty() {
        return BottleQty;
    }

    public void setBottleQty(int bottleQty) {
        BottleQty = bottleQty;
    }

    public int getDiscount() {
        return Discount;
    }

    public void setDiscount(int discount) {
        Discount = discount;
    }

    public int getNewPrice() {
        return NewPrice;
    }

    public void setNewPrice(int newPrice) {
        NewPrice = newPrice;
    }

    public Products getProductDetail() {
        return ProductDetail;
    }

    public void setProductDetail(Products productDetail) {
        ProductDetail = productDetail;
    }
}
