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

public class ProductBatch implements Serializable {


    /**
     * ProductId : 1
     * BatchId : 1
     * Quantity : 100
     * BlockQuantity : 150
     * UnitDealrePrice : 450
     * UnitSellingPrice : 700
     * IsAllSold : 0
     * ExpiryDate : Date(1497983400000)
     * EnteredUser : 3
     * EnteredDate : Date(1498039899510)
     */

    private int ProductId;
    private int BatchId;
    private int Quantity;
    private int BlockQuantity;
    private double UnitDealrePrice;
    private double UnitSellingPrice;
    private int IsAllSold;
    private String ExpiryDate;
    private String EnteredUser;
    private String EnteredDate;


    public ProductBatch(int productId, int batchId, int quantity, int blockQuantity, double unitDealrePrice,
                        double unitSellingPrice, int isAllSold, String expiryDate, String enteredUser,
                        String enteredDate) {
        this.setProductId(productId);
        this.setBatchId(batchId);
        this.setQuantity(quantity);
        this.setBlockQuantity(blockQuantity);
        this.setUnitDealrePrice(unitDealrePrice);
        this.setUnitSellingPrice(unitSellingPrice);
        this.setIsAllSold(isAllSold);
        this.setExpiryDate(expiryDate);
        this.setEnteredUser(enteredUser);
        this.setEnteredDate(enteredDate);
    }

    public static ProductBatch objectFromData(String str) {

        return new Gson().fromJson(str, ProductBatch.class);
    }

    public static ProductBatch objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), ProductBatch.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<ProductBatch> arrayProductBatchFromData(String str) {

        Type listType = new TypeToken<ArrayList<ProductBatch>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<ProductBatch> arrayProductBatchFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<ProductBatch>>() {
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

    public int getBatchId() {
        return BatchId;
    }

    public void setBatchId(int BatchId) {
        this.BatchId = BatchId;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int Quantity) {
        this.Quantity = Quantity;
    }

    public int getBlockQuantity() {
        return BlockQuantity;
    }

    public void setBlockQuantity(int BlockQuantity) {
        this.BlockQuantity = BlockQuantity;
    }

    public double getUnitDealrePrice() {
        return UnitDealrePrice;
    }

    public void setUnitDealrePrice(double UnitDealrePrice) {
        this.UnitDealrePrice = UnitDealrePrice;
    }

    public double getUnitSellingPrice() {
        return UnitSellingPrice;
    }

    public void setUnitSellingPrice(double UnitSellingPrice) {
        this.UnitSellingPrice = UnitSellingPrice;
    }

    public int getIsAllSold() {
        return IsAllSold;
    }

    public void setIsAllSold(int IsAllSold) {
        this.IsAllSold = IsAllSold;
    }

    public String getExpiryDate() {
        return ExpiryDate;
    }

    public void setExpiryDate(String ExpiryDate) {
        this.ExpiryDate = ExpiryDate;
    }

    public String getEnteredUser() {
        return EnteredUser;
    }

    public void setEnteredUser(String EnteredUser) {
        this.EnteredUser = EnteredUser;
    }

    public String getEnteredDate() {
        return EnteredDate;
    }

    public void setEnteredDate(String EnteredDate) {
        this.EnteredDate = EnteredDate;
    }


    public ContentValues getProductBatchContentvalues(){

        ContentValues cv = new ContentValues();

        cv.put("ProductId",getProductId());
        cv.put("BatchId",getBatchId());
        cv.put("Quantity",getQuantity());
        cv.put("BlockQuantity",getBlockQuantity());
        cv.put("UnitDealrePrice",getUnitDealrePrice());
        cv.put("UnitSellingPrice",getUnitSellingPrice());
        cv.put("IsAllSold",getIsAllSold());
        cv.put("ExpiryDate",getExpiryDate());
        cv.put("EnteredUser",getEnteredUser());
        cv.put("EnteredDate",getEnteredDate());

        return cv;
    }


}
