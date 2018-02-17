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
 * Created by Bellvantage on 24/07/2017.
 */

public class SalesRep implements Serializable {


    /**
     * SalesRepId : 3
     * Distributord : 1
     * SalesRepName : Sunesth Saminda Liyanagea
     * EpfNumber : 123
     * IsActive : 1
     * EnteredUser :
     * EnteredDate : Date(1500887652199)
     * PresentAddress : No 4, Kolpittiya
     * PermanentAddress : Gale Road,Colombo
     * ContactNo : 773403674
     * UpdatedUser : 3
     * UpdatedDate : Date(1500024598487)
     * ReferenceID : 63
     * NextCreditNoteNo:0
     * NextSalesOrderNo : 10388
     * NextInvoiceNo : 10050
     * SalesRepType : R
     * _DefDistributor : null
     * _ListVehicleInventory : null
     * _ListDeviceInfo : null
     * _ListDefDefinePath : null
     * _ListActualPath : null
     * _ListAttendance : null
     * _ListSalesRepInventory : null
     * _ListReturnInventory : null
     * _ListSalesInvoice : null
     * _ListSalesOrder : null
     * _ListDefMerchant : null
     * _ListDeliveryLocations : null
     * _DefLogin : null
     */

    private int SalesRepId;
    private int DistributorId;
    private String SalesRepName;
    private int EpfNumber;
    private int IsActive;
    private String EnteredUser;
    private String EnteredDate;
    private String UpdatedUser;
    private String UpdatedDate;
    private String ReferenceID;
    private int NextCreditNoteNo;
    private int NextSalesOrderNo;
    private int NextInvoiceNo;
    private String SalesRepType;
    private String ContactNo;

   /* private Object _DefDistributor;
    private Object _ListVehicleInventory;
    private Object _ListDeviceInfo;
    private Object _ListDefDefinePath;
    private Object _ListActualPath;
    private Object _ListAttendance;
    private Object _ListSalesRepInventory;
    private Object _ListReturnInventory;
    private Object _ListSalesInvoice;
    private Object _ListSalesOrder;
    private Object _ListDefMerchant;
    private Object _ListDeliveryLocations;
    private Object _DefLogin;*/


    //1st
    public SalesRep(int salesRepId, int distributorId, String salesRepName, int epfNumber,
                    int isActive, String enteredUser, String enteredDate, String updatedUser,
                    String updatedDate, String referenceID,int nextCreditNoteNo, int nextSalesOrderNo, int nextInvoiceNo, String salesRepType,String contactNo) {

        this.setSalesRepId(salesRepId);
        this.setDistributord(distributorId);
        this.setSalesRepName(salesRepName);
        this.setEpfNumber(epfNumber);
        this.setIsActive(isActive);
        this.setEnteredUser(enteredUser);
        this.setEnteredDate(enteredDate);
        this.setUpdatedUser(updatedUser);
        this.setUpdatedDate(updatedDate);
        this.setReferenceID(referenceID);
        this.setNextSalesOrderNo(nextSalesOrderNo);
        this.setNextInvoiceNo(nextInvoiceNo);
        this.setSalesRepType(salesRepType);
        this.setNextCreditNoteNo(nextCreditNoteNo);
        this.setContactNo(contactNo);
    }

    public String getContactNo() {
        return ContactNo;
    }

    public void setContactNo(String contactNo) {
        ContactNo = contactNo;
    }

    public int getNextCreditNoteNo() {
        return NextCreditNoteNo;
    }

    public void setNextCreditNoteNo(int nextCreditNoteNo) {
        NextCreditNoteNo = nextCreditNoteNo;
    }

    public static SalesRep objectFromData(String str) {

        return new Gson().fromJson(str, SalesRep.class);
    }

    public static SalesRep objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), SalesRep.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<SalesRep> arraySalesRepFromData(String str) {

        Type listType = new TypeToken<ArrayList<SalesRep>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<SalesRep> arraySalesRepFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<SalesRep>>() {
            }.getType();

            return new Gson().fromJson(jsonObject.getString(str), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList();


    }

    public int getSalesRepId() {
        return SalesRepId;
    }

    public void setSalesRepId(int SalesRepId) {
        this.SalesRepId = SalesRepId;
    }

    public int getDistributord() {
        return DistributorId;
    }

    public void setDistributord(int Distributord) {
        this.DistributorId = Distributord;
    }

    public String getSalesRepName() {
        return SalesRepName;
    }

    public void setSalesRepName(String SalesRepName) {
        this.SalesRepName = SalesRepName;
    }

    public int getEpfNumber() {
        return EpfNumber;
    }

    public void setEpfNumber(int EpfNumber) {
        this.EpfNumber = EpfNumber;
    }

    public int getIsActive() {
        return IsActive;
    }

    public void setIsActive(int IsActive) {
        this.IsActive = IsActive;
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

    public String getUpdatedUser() {
        return UpdatedUser;
    }

    public void setUpdatedUser(String UpdatedUser) {
        this.UpdatedUser = UpdatedUser;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String UpdatedDate) {
        this.UpdatedDate = UpdatedDate;
    }

    public String getReferenceID() {
        return ReferenceID;
    }

    public void setReferenceID(String ReferenceID) {
        this.ReferenceID = ReferenceID;
    }

    public int getNextSalesOrderNo() {
        return NextSalesOrderNo;
    }

    public void setNextSalesOrderNo(int NextSalesOrderNo) {
        this.NextSalesOrderNo = NextSalesOrderNo;
    }

    public int getNextInvoiceNo() {
        return NextInvoiceNo;
    }

    public void setNextInvoiceNo(int NextInvoiceNo) {
        this.NextInvoiceNo = NextInvoiceNo;
    }

    public String getSalesRepType() {
        return SalesRepType;
    }

    public void setSalesRepType(String SalesRepType) {
        this.SalesRepType = SalesRepType;
    }


    public ContentValues getSalesRepContentValues(){

        ContentValues cv = new ContentValues();

        cv.put("SalesRepId",getSalesRepId());
        cv.put("DistributorId",getDistributord());
        cv.put("SalesRepName",getSalesRepName());
        cv.put("EpfNumber",getEpfNumber());
        cv.put("IsActive",getIsActive());
        cv.put("EnteredUser",getEnteredUser());
        cv.put("EnteredDate",getEnteredDate());
        cv.put("UpdatedUser",getUpdatedUser());
        cv.put("UpdatedDate",getUpdatedDate());
        cv.put("ReferenceID",getReferenceID());
        cv.put("NextCreditNoteNo",getNextCreditNoteNo());
        cv.put("NextSalesOrderNo",getNextSalesOrderNo());
        cv.put("NextInvoiceNo",getNextInvoiceNo());
        cv.put("SalesRepType",getSalesRepType());
        cv.put("ContactNo",getContactNo());
        return  cv;
    }
}
