package com.example.bellvantage.scadd.swf;

import android.content.ContentValues;

import com.example.bellvantage.scadd.DB.DbHelper;

import java.io.Serializable;

/**
 * Created by Bellvantage on 31/05/2017.
 */

public class InvoiceLineItem implements Serializable{

    private int batchID;
    private  int productID;
    private int invoiceID;
    private  int qty;
    private  int freeQu;
    private  double price;
    private  double lineTotal;
    private int issync;
    private String syncDate;
    private  String itemName;



    /*
    \"BatchID\":1,
\"ProductID\":8,
\"InvoiceID\":100177,
\"Quantity\":3,
\"FreeQuantity\":0,
\"UnitSellingPrice\":100.00,
\"UnitSellingDiscount\":0.00,
\"TotalAmount\":300.00,
\"TotalDiscount\":0.00,
\"IsSync\":0,
\"SyncDate\":\"\\/Date(1504860652287)\\/\",
\"_ProductBatch\":null,
\"_SalesInvoice\":null,
\"_Product\":{
	\"ProductId\":8,
	\"PackingTypeId\":3,
	\"SellingCategoryId\":1,
	\"CompanyId\":1,
	\"CategoryId\":5,
	\"ProductName\":\"PUPPY CHIKEN \\u0026 MILK (400G)\",
	\"IsActive\":1,
	\"EnteredDate\":\"\\/Date(1496169000000)\\/\",
	\"EnteredUser\":\"ADMIN\",
	\"UnitSellingPrice\":197.84,
	\"UnitDistributorPrice\":197.84,
	\"ReferenceID\":\"46664\",
	\"IsVat\":0,
	\"VatRate\":0,
	\"_DefProductCategory\":null,
	\"_DefCompany\":null,
	\"_DefProductSellingCategory\":null,
	\"_DefPackingType\":null,
	\"ListPrimarySalesInvoiceLineItem\":null,
	\"ListPrimarySalesLineItem\":null,
	\"ListSalesRepInventory\":null,
	\"_ProductBatchList\":null
     */

    public InvoiceLineItem(int batchID, int productID, int invoiceID,
                           int qty, int freeQu,
                           double price, double lineTotal,
                           int issync, String syncDate, String itemName) {

        this.setBatchID(batchID);
        this.setProductID(productID);
        this.setInvoiceID(invoiceID);
        this.setQty(qty);
        this.setFreeQu(freeQu);
        this.setPrice(price);
        this.setLineTotal(lineTotal);
        this.setIssync(issync);
        this.setSyncDate(syncDate);
        this.setItemName(itemName);
    }


    public int getFreeQu() {
        return freeQu;
    }

    public void setFreeQu(int freeQu) {
        this.freeQu = freeQu;
    }

    public int getBatchID() {
        return batchID;
    }

    public void setBatchID(int batchID) {
        this.batchID = batchID;
    }

    public int getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(int invoiceID) {
        this.invoiceID = invoiceID;
    }

    public int getIssync() {
        return issync;
    }

    public void setIssync(int issync) {
        this.issync = issync;
    }

    public String getSyncDate() {
        return syncDate;
    }

    public void setSyncDate(String syncDate) {
        this.syncDate = syncDate;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(double lineTotal) {
        this.lineTotal = lineTotal;
    }


    public ContentValues getCVvaluesFromInvoiceLineItem(){

        ContentValues cv = new ContentValues();
        cv.put(DbHelper.COL_INVOICE_LINE_ITEM_batchID,getBatchID());
        cv.put(DbHelper.COL_INVOICE_LINE_ITEM_productID,getProductID());
        cv.put(DbHelper.COL_INVOICE_LINE_ITEM_invoiceID,getInvoiceID());
        cv.put(DbHelper.COL_INVOICE_LINE_ITEM_qty,getQty());
        cv.put(DbHelper.COL_INVOICE_LINE_ITEM_freeQuantity,getFreeQu());
        cv.put(DbHelper.COL_INVOICE_LINE_ITEM_price,getPrice());
        cv.put(DbHelper.COL_INVOICE_LINE_ITEM_lineTotal,getLineTotal());
        cv.put(DbHelper.COL_INVOICE_LINE_ITEM_issync,getIssync());
        cv.put(DbHelper.COL_INVOICE_LINE_ITEM_syncDate,getSyncDate());
        cv.put(DbHelper.COL_INVOICE_LINE_ITEM_itemName,getItemName());
        return cv;
    }
}
