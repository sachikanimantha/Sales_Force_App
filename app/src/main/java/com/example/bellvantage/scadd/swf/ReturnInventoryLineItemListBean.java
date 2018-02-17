package com.example.bellvantage.scadd.swf;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bellvantage on 08/12/2017.
 */

public class ReturnInventoryLineItemListBean {
    /**
     * CreditNoteNo : 114710001
     * ProductId : 1
     * BatchId : 1
     * Quantity : 2
     * TotalAmount : 1400
     * ReturnType : 3
     * IsSellable : 1
     * DiscountedPrice : 0
     * productName : null
     */

    private int CreditNoteNo;
    private int ProductId;
    private int BatchId;
    private int Quantity;
    private int TotalAmount;
    private int ReturnType;
    private int IsSellable;
    private int DiscountedPrice;
    private String productName;

    public static ReturnInventoryLineItemListBean objectFromData(String str) {

        return new Gson().fromJson(str, ReturnInventoryLineItemListBean.class);
    }

    public static ReturnInventoryLineItemListBean objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), ReturnInventoryLineItemListBean.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<ReturnInventoryLineItemListBean> arrayReturnInventoryLineItemListBeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<ReturnInventoryLineItemListBean>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<ReturnInventoryLineItemListBean> arrayReturnInventoryLineItemListBeanFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<ReturnInventoryLineItemListBean>>() {
            }.getType();

            return new Gson().fromJson(jsonObject.getString(str), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList();


    }

    public int getCreditNoteNo() {
        return CreditNoteNo;
    }

    public void setCreditNoteNo(int CreditNoteNo) {
        this.CreditNoteNo = CreditNoteNo;
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

    public int getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(int TotalAmount) {
        this.TotalAmount = TotalAmount;
    }

    public int getReturnType() {
        return ReturnType;
    }

    public void setReturnType(int ReturnType) {
        this.ReturnType = ReturnType;
    }

    public int getIsSellable() {
        return IsSellable;
    }

    public void setIsSellable(int IsSellable) {
        this.IsSellable = IsSellable;
    }

    public int getDiscountedPrice() {
        return DiscountedPrice;
    }

    public void setDiscountedPrice(int DiscountedPrice) {
        this.DiscountedPrice = DiscountedPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
