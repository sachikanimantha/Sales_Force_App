package com.example.bellvantage.scadd.swf;

import android.content.ContentValues;

import java.io.Serializable;

import static com.example.bellvantage.scadd.DB.DbHelper.COL_RETURN_INVENTORY_LINEITEM_batchId;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_RETURN_INVENTORY_LINEITEM_creditNoteNo;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_RETURN_INVENTORY_LINEITEM_discountedPrice;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_RETURN_INVENTORY_LINEITEM_isSellable;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_RETURN_INVENTORY_LINEITEM_isSync;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_RETURN_INVENTORY_LINEITEM_productId;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_RETURN_INVENTORY_LINEITEM_productName;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_RETURN_INVENTORY_LINEITEM_quantity;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_RETURN_INVENTORY_LINEITEM_returnType;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_RETURN_INVENTORY_LINEITEM_totalAmount;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_RETURN_INVENTORY_LINEITEM_unitPrice;

/**
 * Created by Sachika on 17/08/2017.
 */

public class ReturnInventoryLineItem implements Serializable {

    private int CreditNoteNo;
    private int ProductId;
    private int BatchId;
    private int Quantity;
    private double TotalAmount;
    private int ReturnType;
    private int IsSellable;
    private double DiscountedPrice;
    private  String productName;
    private  double unitPrice;
    private  int isSync;
    private String ExpireDate;

    public ReturnInventoryLineItem(int productId, int batchId, double unitPrice,String expireDate) {
        this.setProductId(productId);
        this.setBatchId(batchId);
        this.setUnitPrice(unitPrice);
        this.setExpireDate(expireDate);
    }

    public ReturnInventoryLineItem(int creditNoteNo, int productId, int batchId,
                                   int quantity, double totalAmount, int returnType,
                                   int isSellable, double discountedPrice,
                                   String productName, double unitPrice, int isSync) {
       this.setCreditNoteNo(creditNoteNo);
        this.setProductId(productId);
        this.setBatchId(batchId);
        this.setQuantity(quantity);
        this.setTotalAmount(totalAmount);
        this.setReturnType(returnType);
        this.setIsSellable(isSellable);
        this.setDiscountedPrice(discountedPrice);
        this.setProductName(productName);
        this.setUnitPrice(unitPrice);
        this.setIsSync(isSync);
    }


    public ReturnInventoryLineItem(int productId, double unitPrice,
                                   int quantity, double totalAmount,
                                   String productName) {

        this.setProductId(productId);
        this.setQuantity(quantity);
        this.setTotalAmount(totalAmount);
        this.setProductName(productName);
        this.setUnitPrice(unitPrice);
    }

    public ReturnInventoryLineItem(int productId, double unitPrice,
                                   int quantity, double totalAmount,int returntype,
                                   String productName) {

        this.setProductId(productId);
        this.setQuantity(quantity);
        this.setTotalAmount(totalAmount);
        this.setProductName(productName);
        this.setReturnType(returntype);
        this.setUnitPrice(unitPrice);
    }


    public String getExpireDate() {
        return ExpireDate;
    }

    public void setExpireDate(String expireDate) {
        ExpireDate = expireDate;
    }

    public int getIsSync() {
        return isSync;
    }

    public void setIsSync(int isSync) {
        this.isSync = isSync;
    }

    public int getCreditNoteNo() {
        return CreditNoteNo;
    }

    public void setCreditNoteNo(int creditNoteNo) {
        CreditNoteNo = creditNoteNo;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public int getBatchId() {
        return BatchId;
    }

    public void setBatchId(int batchId) {
        BatchId = batchId;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        TotalAmount = totalAmount;
    }

    public int getReturnType() {
        return ReturnType;
    }

    public void setReturnType(int returnType) {
        ReturnType = returnType;
    }

    public int getIsSellable() {
        return IsSellable;
    }

    public void setIsSellable(int isSellable) {
        IsSellable = isSellable;
    }

    public double getDiscountedPrice() {
        return DiscountedPrice;
    }

    public void setDiscountedPrice(double discountedPrice) {
        DiscountedPrice = discountedPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public ContentValues getReturnInventoryLineItemCv(){
        ContentValues cv= new ContentValues();
        cv.put(COL_RETURN_INVENTORY_LINEITEM_creditNoteNo,getCreditNoteNo());
        cv.put(COL_RETURN_INVENTORY_LINEITEM_productId,getProductId());
        cv.put(COL_RETURN_INVENTORY_LINEITEM_batchId,getBatchId());
        cv.put(COL_RETURN_INVENTORY_LINEITEM_quantity,getQuantity());
        cv.put(COL_RETURN_INVENTORY_LINEITEM_totalAmount,getTotalAmount());
        cv.put(COL_RETURN_INVENTORY_LINEITEM_returnType,getReturnType());
        cv.put(COL_RETURN_INVENTORY_LINEITEM_isSellable,getIsSellable());
        cv.put(COL_RETURN_INVENTORY_LINEITEM_discountedPrice,getDiscountedPrice());
        cv.put(COL_RETURN_INVENTORY_LINEITEM_productName,getProductName());
        cv.put(COL_RETURN_INVENTORY_LINEITEM_isSync,getIsSync());
        cv.put(COL_RETURN_INVENTORY_LINEITEM_unitPrice,getUnitPrice());

        return  cv;
    }
}
