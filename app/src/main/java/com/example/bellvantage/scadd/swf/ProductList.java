package com.example.bellvantage.scadd.swf;

import java.io.Serializable;

/**
 * Created by Bellvantage on 29/06/2017.
 */

public class ProductList{

    String name;
    String expireDate;
    int stock;
    double unitPrice;
    double subTotal;
    int qty;
    int freeQty;
    int img;
    int batchid;
    int productid;
   // double UnitSellingDiscount;
   // double TotalDiscount;
    int isVatHas;
    double vatRate;
    int returnqty;

    int categoryid;

    double row_free;
    double row_net;
    double row_total;
    /*
    productArrayList, grandtotal, 0,0, img_done, position, itemVatAmount,returnQty,freeIssuedAmount,netvalue,grandtotal
     */

    public ProductList(String name, String expireDate, int stock, double unitPrice,
                       double subTotal, int qty, int freeQty, int img, int batchid, int productid,
                       int isVatHas, double vatRate, int returnqty,
                       double row_free, double row_net, double row_total,int catid) {

        this.name = name;
        this.expireDate = expireDate;
        this.stock = stock;
        this.unitPrice = unitPrice;
        this.subTotal = subTotal;
        this.qty = qty;
        this.freeQty = freeQty;
        this.img = img;
        this.batchid = batchid;
        this.productid = productid;
        this.isVatHas = isVatHas;
        this.vatRate = vatRate;
        this.returnqty = returnqty;
        this.row_free = row_free;
        this.row_net = row_net;
        this.row_total = row_total;
        this.categoryid = catid;
    }

    public ProductList(String name, int stock,
                       double subTotal, int qty, int freeQty, int img, int batchid, int productid,
                       int isVatHas, double vatRate, int returnqty,
                       double row_free, double row_net, double row_total) {

        /*
        ProductName, LoadQuantity,
                        0, 0, 0, R.drawable.ic_not_done, BatchID, ProductId, IsVat, vatrate, 0, 0, 0, 0, catid
         */

        this.name = name;
        //this.expireDate = expireDate;
        this.stock = stock;
        //this.unitPrice = unitPrice;
        this.subTotal = subTotal;
        this.qty = qty;
        this.freeQty = freeQty;
        this.img = img;
        this.batchid = batchid;
        this.productid = productid;
        this.isVatHas = isVatHas;
        this.vatRate = vatRate;
        this.returnqty = returnqty;
        this.row_free = row_free;
        this.row_net = row_net;
        this.row_total = row_total;
    }

    public ProductList(String name, String expireDate, int stock, double unitPrice, double vatRate, double subTotal,
                       int qty, int freeQty, int img, int batchid, int productid, int isItemhasVat, int returnQty) {
        this.name = name;
        this.expireDate = expireDate;
        //this.catergoryId = catergoryId;
        this.stock = stock;
        this.unitPrice = unitPrice;
        this.subTotal = subTotal;
        this.qty = qty;
        this.freeQty = freeQty;
        this.img = img;
        this.batchid = batchid;
        this.productid = productid;
        this.isVatHas = isItemhasVat;
        this.vatRate = vatRate;
        //this.returnqty = returnQty;
    }

    //construct for create sales order
    public ProductList(String name, String expireDate, int stock, double unitPrice, double subTotal,
                       int qty, int freeQty,int img,int batchid,int productid,int isItemhasVat,double vatrate,int returnQty) {
        this.name = name;
        this.expireDate = expireDate;
        //this.catergoryId = catergoryId;
        this.stock = stock;
        this.unitPrice = unitPrice;
        this.subTotal = subTotal;
        this.qty = qty;
        this.freeQty = freeQty;
        this.img = img;
        this.batchid = batchid;
        this.productid = productid;
        this.isVatHas = isItemhasVat;
        this.vatRate= vatrate;
        this.returnqty = returnQty;
    }
    //for get details from sqlite
    public ProductList(String name,int productid,int isItemhasVat, double unitPrice,double vatrate,
                       String expireDate,int batchid, int stock,int returnQty) {

        this.name = name;
        this.expireDate = expireDate;
        this.stock = stock;
        this.unitPrice = unitPrice;
        this.vatRate= vatrate;
        this.batchid = batchid;
        this.productid = productid;
        this.isVatHas = isItemhasVat;
        //this.returnqty = returnQty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getFreeQty() {
        return freeQty;
    }

    public void setFreeQty(int freeQty) {
        this.freeQty = freeQty;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public int getBatchid() {
        return batchid;
    }

    public void setBatchid(int batchid) {
        this.batchid = batchid;
    }

    public int getProductid() {
        return productid;
    }

    public void setProductid(int productid) {
        this.productid = productid;
    }

    public int getIsVatHas() {
        return isVatHas;
    }

    public void setIsVatHas(int isVatHas) {
        this.isVatHas = isVatHas;
    }

    public double getVatRate() {
        return vatRate;
    }

    public void setVatRate(double vatRate) {
        this.vatRate = vatRate;
    }

    public int getReturnqty() {
        return returnqty;
    }

    public void setReturnqty(int returnqty) {
        this.returnqty = returnqty;
    }

    public double getRow_free() {
        return row_free;
    }

    public void setRow_free(double row_free) {
        this.row_free = row_free;
    }

    public double getRow_net() {
        return row_net;
    }

    public void setRow_net(double row_net) {
        this.row_net = row_net;
    }

    public double getRow_total() {
        return row_total;
    }

    public void setRow_total(double row_total) {
        this.row_total = row_total;
    }

    public int getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }
}
