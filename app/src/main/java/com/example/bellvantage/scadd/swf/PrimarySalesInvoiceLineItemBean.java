package com.example.bellvantage.scadd.swf;

import android.content.ContentValues;
import android.database.Cursor;

import static com.example.bellvantage.scadd.DB.DbHelper.COL_PRIMARY_SALES_INVOICE_LINE_ITEM_InvoiceNumber;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_PRIMARY_SALES_INVOICE_LINE_ITEM_OrderQty;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_PRIMARY_SALES_INVOICE_LINE_ITEM_ProductId;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_PRIMARY_SALES_INVOICE_LINE_ITEM_SizeOfUnit;

/**
 * Created by Sachika on 15/11/2017.
 */

public class PrimarySalesInvoiceLineItemBean {
    /**
     * ProductId : 1
     * InvoiceNumber : C065747
     * OrderQty : 4
     * SizeOfUnit : 0.25
     */

    private int ProductId;
    private String InvoiceNumber;
    private int OrderQty;
    private double SizeOfUnit;

    public PrimarySalesInvoiceLineItemBean() {
    }

    public PrimarySalesInvoiceLineItemBean(int productId, String invoiceNumber, int orderQty, double sizeOfUnit) {
        ProductId = productId;
        InvoiceNumber = invoiceNumber;
        OrderQty = orderQty;
        SizeOfUnit = sizeOfUnit;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int ProductId) {
        this.ProductId = ProductId;
    }

    public String getInvoiceNumber() {
        return InvoiceNumber;
    }

    public void setInvoiceNumber(String InvoiceNumber) {
        this.InvoiceNumber = InvoiceNumber;
    }

    public int getOrderQty() {
        return OrderQty;
    }

    public void setOrderQty(int OrderQty) {
        this.OrderQty = OrderQty;
    }

    public double getSizeOfUnit() {
        return SizeOfUnit;
    }

    public void setSizeOfUnit(double SizeOfUnit) {
        this.SizeOfUnit = SizeOfUnit;
    }


    public ContentValues getContentValues(){
        ContentValues cv = new ContentValues();
        cv.put(COL_PRIMARY_SALES_INVOICE_LINE_ITEM_ProductId,getProductId());
        cv.put(COL_PRIMARY_SALES_INVOICE_LINE_ITEM_InvoiceNumber,getInvoiceNumber());
        cv.put(COL_PRIMARY_SALES_INVOICE_LINE_ITEM_OrderQty,getOrderQty());
        cv.put(COL_PRIMARY_SALES_INVOICE_LINE_ITEM_SizeOfUnit,getSizeOfUnit());
        return  cv;
    }


    public PrimarySalesInvoiceLineItemBean getDbInstance(Cursor cursor){
        PrimarySalesInvoiceLineItemBean primarySalesInvoiceLineItemBean = new PrimarySalesInvoiceLineItemBean();
        primarySalesInvoiceLineItemBean.setProductId(cursor.getInt(cursor.getColumnIndex(COL_PRIMARY_SALES_INVOICE_LINE_ITEM_ProductId)));
        primarySalesInvoiceLineItemBean.setInvoiceNumber(cursor.getString(cursor.getColumnIndex(COL_PRIMARY_SALES_INVOICE_LINE_ITEM_InvoiceNumber)));
        primarySalesInvoiceLineItemBean.setOrderQty(cursor.getInt(cursor.getColumnIndex(COL_PRIMARY_SALES_INVOICE_LINE_ITEM_OrderQty)));
        primarySalesInvoiceLineItemBean.setSizeOfUnit(cursor.getInt(cursor.getColumnIndex(COL_PRIMARY_SALES_INVOICE_LINE_ITEM_SizeOfUnit)));
        return primarySalesInvoiceLineItemBean;
    }

}
