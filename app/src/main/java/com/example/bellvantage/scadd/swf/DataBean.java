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

public class DataBean {
    /**
     * CreditNoteNo : 114710001
     * ReturnDate : /Date(1512498600000)/
     * TotalAmount : 1400.0
     * TotalOutstanding : 700
     * MerchantId : 5552
     * SalesRepId : 147
     * DistributorId : 96
     * SyncDate : /Date(1512538304453)/
     * IsSync : 0
     * EnteredDate : /Date(1512518456000)/
     * EnteredUser : 00444
     * _DefDistributor : null
     * _DefMerchant : null
     * _DistDefSaleRep : null
     * _ReturnInventoryLineItemList : [{"CreditNoteNo":114710001,"ProductId":1,"BatchId":1,"Quantity":2,"TotalAmount":1400,"ReturnType":3,"IsSellable":1,"DiscountedPrice":0,"productName":null},{"CreditNoteNo":114710001,"ProductId":1,"BatchId":1,"Quantity":2,"TotalAmount":1400,"ReturnType":3,"IsSellable":1,"DiscountedPrice":0,"productName":null}]
     */

    private int CreditNoteNo;
    private String ReturnDate;
    private double TotalAmount;
    private int TotalOutstanding;
    private int MerchantId;
    private int SalesRepId;
    private int DistributorId;
    private String SyncDate;
    private int IsSync;
    private String EnteredDate;
    private String EnteredUser;
    private Object _DefDistributor;
    private Object _DefMerchant;
    private Object _DistDefSaleRep;
    private ArrayList<ReturnInventoryLineItemListBean> _ReturnInventoryLineItemList;

    public static DataBean objectFromData(String str) {

        return new Gson().fromJson(str, DataBean.class);
    }

    public static DataBean objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), DataBean.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<DataBean> arrayDataBeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<DataBean>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<DataBean> arrayDataBeanFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<DataBean>>() {
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

    public String getReturnDate() {
        return ReturnDate;
    }

    public void setReturnDate(String ReturnDate) {
        this.ReturnDate = ReturnDate;
    }

    public double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(double TotalAmount) {
        this.TotalAmount = TotalAmount;
    }

    public int getTotalOutstanding() {
        return TotalOutstanding;
    }

    public void setTotalOutstanding(int TotalOutstanding) {
        this.TotalOutstanding = TotalOutstanding;
    }

    public int getMerchantId() {
        return MerchantId;
    }

    public void setMerchantId(int MerchantId) {
        this.MerchantId = MerchantId;
    }

    public int getSalesRepId() {
        return SalesRepId;
    }

    public void setSalesRepId(int SalesRepId) {
        this.SalesRepId = SalesRepId;
    }

    public int getDistributorId() {
        return DistributorId;
    }

    public void setDistributorId(int DistributorId) {
        this.DistributorId = DistributorId;
    }

    public String getSyncDate() {
        return SyncDate;
    }

    public void setSyncDate(String SyncDate) {
        this.SyncDate = SyncDate;
    }

    public int getIsSync() {
        return IsSync;
    }

    public void setIsSync(int IsSync) {
        this.IsSync = IsSync;
    }

    public String getEnteredDate() {
        return EnteredDate;
    }

    public void setEnteredDate(String EnteredDate) {
        this.EnteredDate = EnteredDate;
    }

    public String getEnteredUser() {
        return EnteredUser;
    }

    public void setEnteredUser(String EnteredUser) {
        this.EnteredUser = EnteredUser;
    }

    public Object get_DefDistributor() {
        return _DefDistributor;
    }

    public void set_DefDistributor(Object _DefDistributor) {
        this._DefDistributor = _DefDistributor;
    }

    public Object get_DefMerchant() {
        return _DefMerchant;
    }

    public void set_DefMerchant(Object _DefMerchant) {
        this._DefMerchant = _DefMerchant;
    }

    public Object get_DistDefSaleRep() {
        return _DistDefSaleRep;
    }

    public void set_DistDefSaleRep(Object _DistDefSaleRep) {
        this._DistDefSaleRep = _DistDefSaleRep;
    }

    public ArrayList<ReturnInventoryLineItemListBean> get_ReturnInventoryLineItemList() {
        return _ReturnInventoryLineItemList;
    }

    public void set_ReturnInventoryLineItemList(ArrayList<ReturnInventoryLineItemListBean> _ReturnInventoryLineItemList) {
        this._ReturnInventoryLineItemList = _ReturnInventoryLineItemList;
    }
}
