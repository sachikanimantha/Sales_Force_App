package com.example.bellvantage.scadd.swf;

import android.content.ContentValues;

import com.example.bellvantage.scadd.DB.DbHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sachika on 21/07/2017.
 */

public class SalesOrderLineItem implements Serializable {


    /**
     * SalesOrderID : 0
     * BatchID : 2
     * ProductID : 4
     * Quantity : 1
     * FreeQuantity : 0
     * UnitSellingPrice : 100.00
     * UnitSellingDiscount : 0.00
     * TotalAmount : 100.00
     * TotalDiscount : 0.00
     * IsSync : 1
     * SyncDate : Date(1500610227366)
     */

    private int SalesOrderID;
    private int BatchID;
    private int ProductID;
    private int currentStock;
    private int Quantity;
    private int FreeQuantity;
    private double UnitSellingPrice;
    private double UnitSellingDiscount;
    private double TotalAmount;
    private double TotalDiscount;
    private int IsSync;
    private String SyncDate;


    public SalesOrderLineItem(int salesOrderID, int batchID, int productID, int quantity, int freeQuantity,
                              double unitSellingPrice, double unitSellingDiscount, double totalAmount,
                              double totalDiscount, int isSync, String syncDate) {
       this.setSalesOrderID(salesOrderID);
        this.setBatchID(batchID);
        this.setProductID(productID);
        this.setQuantity(quantity);
        this.setFreeQuantity(freeQuantity);
        this.setUnitSellingPrice(unitSellingPrice);
        this.setUnitSellingDiscount(unitSellingDiscount);
        this.setTotalAmount(totalAmount);
        this.setTotalDiscount(totalDiscount);
        this.setIsSync(isSync);
        this.setSyncDate(syncDate);
    }

    public SalesOrderLineItem(int salesOrderID, int batchID, int productID, int quantity, int freeQuantity,
                              double unitSellingPrice, double unitSellingDiscount, double totalAmount,
                              double totalDiscount, int isSync) {
        this.setSalesOrderID(salesOrderID);
        this.setBatchID(batchID);
        this.setProductID(productID);
        this.setQuantity(quantity);
        this.setFreeQuantity(freeQuantity);
        this.setUnitSellingPrice(unitSellingPrice);
        this.setUnitSellingDiscount(unitSellingDiscount);
        this.setTotalAmount(totalAmount);
        this.setTotalDiscount(totalDiscount);
        this.setIsSync(isSync);
    }

    public SalesOrderLineItem(int salesOrderID, int batchID, int productID, int quantity, int freeQuantity,
                              double unitSellingPrice, double unitSellingDiscount, double totalAmount,
                              double totalDiscount, int isSync,int Stock) {
        this.setSalesOrderID(salesOrderID);
        this.setBatchID(batchID);
        this.setProductID(productID);
        this.setCurrentStock(Stock);
        this.setQuantity(quantity);
        this.setFreeQuantity(freeQuantity);
        this.setUnitSellingPrice(unitSellingPrice);
        this.setUnitSellingDiscount(unitSellingDiscount);
        this.setTotalAmount(totalAmount);
        this.setTotalDiscount(totalDiscount);
        this.setIsSync(isSync);
    }

    public static SalesOrderLineItem objectFromData(String str) {

        return new Gson().fromJson(str, SalesOrderLineItem.class);
    }

    public static SalesOrderLineItem objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), SalesOrderLineItem.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<SalesOrderLineItem> arraySalesOrderLineItemFromData(String str) {

        Type listType = new TypeToken<ArrayList<SalesOrderLineItem>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<SalesOrderLineItem> arraySalesOrderLineItemFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<SalesOrderLineItem>>() {
            }.getType();

            return new Gson().fromJson(jsonObject.getString(str), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList();


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

    public int getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
    }

    public ContentValues getLineItemContentValues(){

        ContentValues cv = new ContentValues();

        cv.put("SalesOrderID",getSalesOrderID());
        cv.put("BatchID",getBatchID());
        cv.put("ProductID",getProductID());
        cv.put("Quantity",getQuantity());
        cv.put("FreeQuantity",getFreeQuantity());
        cv.put("UnitSellingPrice",getUnitSellingPrice());
        cv.put("UnitSellingDiscount",getUnitSellingDiscount());
        cv.put("TotalAmount",getTotalAmount());
        cv.put("TotalDiscount",getTotalDiscount());
        cv.put("IsSync",getIsSync());
        cv.put("SyncDate",getSyncDate());

        return  cv;
    }

    public ContentValues getLineItemContentValues2(){

        ContentValues cv = new ContentValues();

        cv.put("SalesOrderID",getSalesOrderID());
        cv.put("BatchID",getBatchID());
        cv.put("ProductID",getProductID());
        cv.put("Quantity",getQuantity());
        cv.put("FreeQuantity",getFreeQuantity());
        cv.put("UnitSellingPrice",getUnitSellingPrice());
        cv.put("UnitSellingDiscount",getUnitSellingDiscount());
        cv.put("TotalAmount",getTotalAmount());
        cv.put("TotalDiscount",getTotalDiscount());
        cv.put("IsSync",getIsSync());
        //cv.put("SyncDate",getSyncDate());

        return  cv;
    }

    public ContentValues getLineItemContentValues3(){

        ContentValues cv = new ContentValues();

        cv.put("SalesOrderID",getSalesOrderID());
        cv.put("BatchID",getBatchID());
        cv.put("ProductID",getProductID());
        cv.put(DbHelper.COL_LINE_ITEM_CurrentStock,getCurrentStock());
        cv.put("Quantity",getQuantity());
        cv.put("FreeQuantity",getFreeQuantity());
        cv.put("UnitSellingPrice",getUnitSellingPrice());
        cv.put("UnitSellingDiscount",getUnitSellingDiscount());
        cv.put("TotalAmount",getTotalAmount());
        cv.put("TotalDiscount",getTotalDiscount());
        cv.put("IsSync",getIsSync());
        //cv.put("SyncDate",getSyncDate());

        return  cv;
    }


}
