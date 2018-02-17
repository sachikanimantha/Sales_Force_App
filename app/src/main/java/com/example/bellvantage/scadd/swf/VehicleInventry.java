package com.example.bellvantage.scadd.swf;

import android.content.ContentValues;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bellvantage on 27/07/2017.
 */

public class VehicleInventry implements Serializable {


    /**
     * ProductId : 4
     * BatchID : 2
     * DistributorID : 1
     * SalesRepID : 42
     * LoadQuantity : 20
     * OutstandingQuantity : 21
     * DiscountRule : 0
     * FreeIssueRule : 0
     * UnitSellingPrice : 0.0
     * InventoryDate : Date(1501093800000)
     */

    private int ProductId;
    private int BatchID;
    private int DistributorID;
    private int SalesRepID;
    private int LoadQuantity;
    private int OutstandingQuantity;
    private String DiscountRule;
    private String FreeIssueRule;
    private double UnitSellingPrice;
    private String InventoryDate;


    public VehicleInventry(int productId, int batchID, int distributorID, int salesRepID,
                           int loadQuantity, int outstandingQuantity, String discountRule,
                           String freeIssueRule, double unitSellingPrice, String inventoryDate) {

        this.setProductId(productId);
        this.setBatchID(batchID);
        this.setDistributorID(distributorID);
        this.setSalesRepID(salesRepID);
        this.setLoadQuantity(loadQuantity);
        this.setOutstandingQuantity(outstandingQuantity);
        this.setDiscountRule(discountRule);
        this.setFreeIssueRule(freeIssueRule);
        this.setUnitSellingPrice(unitSellingPrice);
        this.setInventoryDate(inventoryDate);
    }

    public static VehicleInventry objectFromData(String str) {

        return new Gson().fromJson(str, VehicleInventry.class);
    }

    public static VehicleInventry objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), VehicleInventry.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<VehicleInventry> arrayVehicleInventryFromData(String str) {

        Type listType = new TypeToken<ArrayList<VehicleInventry>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<VehicleInventry> arrayVehicleInventryFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<VehicleInventry>>() {
            }.getType();

            return new Gson().fromJson(jsonObject.getString(str), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList();


    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int ProductId) {
        this.ProductId = ProductId;
    }

    public int getBatchID() {
        return BatchID;
    }

    public void setBatchID(int BatchID) {
        this.BatchID = BatchID;
    }

    public int getDistributorID() {
        return DistributorID;
    }

    public void setDistributorID(int DistributorID) {
        this.DistributorID = DistributorID;
    }

    public int getSalesRepID() {
        return SalesRepID;
    }

    public void setSalesRepID(int SalesRepID) {
        this.SalesRepID = SalesRepID;
    }

    public int getLoadQuantity() {
        return LoadQuantity;
    }

    public void setLoadQuantity(int LoadQuantity) {
        this.LoadQuantity = LoadQuantity;
    }

    public int getOutstandingQuantity() {
        return OutstandingQuantity;
    }

    public void setOutstandingQuantity(int OutstandingQuantity) {
        this.OutstandingQuantity = OutstandingQuantity;
    }

    public String getDiscountRule() {
        return DiscountRule;
    }

    public void setDiscountRule(String DiscountRule) {
        this.DiscountRule = DiscountRule;
    }

    public String getFreeIssueRule() {
        return FreeIssueRule;
    }

    public void setFreeIssueRule(String FreeIssueRule) {
        this.FreeIssueRule = FreeIssueRule;
    }

    public double getUnitSellingPrice() {
        return UnitSellingPrice;
    }

    public void setUnitSellingPrice(double UnitSellingPrice) {
        this.UnitSellingPrice = UnitSellingPrice;
    }

    public String getInventoryDate() {
        return InventoryDate;
    }

    public void setInventoryDate(String InventoryDate) {
        this.InventoryDate = InventoryDate;
    }

    public ContentValues getVehicleInventoryContentvalues(){

        ContentValues cv  = new ContentValues();

        cv.put("ProductId",getProductId());
        cv.put("BatchId",getBatchID());
        cv.put("DistributorID",getDistributorID());
        cv.put("SalesRepID",getSalesRepID());
        cv.put("LoadQuantity",getLoadQuantity());
        cv.put("OutstandingQuantity",getOutstandingQuantity());
        cv.put("DiscountRule",getDiscountRule());
        cv.put("FreeIssueRule",getFreeIssueRule());
        cv.put("UnitSellingPrice",getUnitSellingPrice());
        cv.put("InventoryDate",getInventoryDate());
        System.out.println("LoadQuantity - "+LoadQuantity);

        return  cv;
    }

}
