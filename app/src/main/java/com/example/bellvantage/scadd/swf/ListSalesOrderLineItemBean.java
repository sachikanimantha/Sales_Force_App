package com.example.bellvantage.scadd.swf;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sachika on 25/07/2017.
 */

class ListSalesOrderLineItemBean {
    /**
     * SalesOrderID : 100346
     * BatchID : 1
     * ProductID : 1
     * Quantity : 2
     * FreeQuantity : 0
     * UnitSellingPrice : 240.25
     * UnitSellingDiscount : 0.0
     * TotalAmount : 480.5
     * TotalDiscount : 0.0
     * IsSync : 0
     * SyncDate : Date(1500957362360)
     * _SalesOrder : null
     * _ProductBatch : null
     * ProductName : null
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
    private Object _SalesOrder;
    private Object _ProductBatch;
    private Object ProductName;

    public static ListSalesOrderLineItemBean objectFromData(String str) {

        return new Gson().fromJson(str, ListSalesOrderLineItemBean.class);
    }

    public static ListSalesOrderLineItemBean objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), ListSalesOrderLineItemBean.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<ListSalesOrderLineItemBean> arrayListSalesOrderLineItemBeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<ListSalesOrderLineItemBean>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<ListSalesOrderLineItemBean> arrayListSalesOrderLineItemBeanFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<ListSalesOrderLineItemBean>>() {
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

    public Object get_SalesOrder() {
        return _SalesOrder;
    }

    public void set_SalesOrder(Object _SalesOrder) {
        this._SalesOrder = _SalesOrder;
    }

    public Object get_ProductBatch() {
        return _ProductBatch;
    }

    public void set_ProductBatch(Object _ProductBatch) {
        this._ProductBatch = _ProductBatch;
    }

    public Object getProductName() {
        return ProductName;
    }

    public void setProductName(Object ProductName) {
        this.ProductName = ProductName;
    }
}
