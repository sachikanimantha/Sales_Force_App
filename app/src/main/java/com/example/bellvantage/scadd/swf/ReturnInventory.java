package com.example.bellvantage.scadd.swf;

import android.content.ContentValues;

import java.io.Serializable;
import java.util.ArrayList;

import static com.example.bellvantage.scadd.DB.DbHelper.COL_RETURN_INVENTORY_SalesRepId;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_RETURN_INVENTORY_creditNoteNo;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_RETURN_INVENTORY_distributorId;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_RETURN_INVENTORY_isSync;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_RETURN_INVENTORY_merchantId;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_RETURN_INVENTORY_returnDate;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_RETURN_INVENTORY_syncDate;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_RETURN_INVENTORY_totalAmount;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_RETURN_INVENTORY_totalOutstanding;

/**
 * Created by Bellvantage on 17/08/2017.
 */

public class ReturnInventory implements Serializable {

    private int CreditNoteNo ;
    private String ReturnDate ;
    private double TotalAmount;
    private double TotalOutstanding;
    private int SalesRepId;
    private int MerchantId;
    private int DistributorId;
    private String SyncDate;
    private int IsSync;
    private String EnteredDate ;
    private String EnteredUser;
    private DistributorDetails _DefDistributor;
    private MerchantDetails _DefMerchant;
    private SalesRep _DistDefSaleRep;
    private ArrayList<ReturnInventoryLineItem> _ReturnInventoryLineItemList;


    //Set outstanding


    public ReturnInventory(int creditNoteNo, double totalOutstanding) {
        CreditNoteNo = creditNoteNo;
        TotalOutstanding = totalOutstanding;
    }

    public ReturnInventory(int creditNoteNo, String returnDate, double totalAmount,
                           double totalOutstanding, int merchantId, int salesRepId,
                           int distributorId, String syncDate,
                           int isSync, String enteredDate,
                           String enteredUser, DistributorDetails _DefDistributor,
                           MerchantDetails _DefMerchant, SalesRep _DistDefSaleRep,
                           ArrayList<ReturnInventoryLineItem> _ReturnInventoryLineItemList) {
        CreditNoteNo = creditNoteNo;
        ReturnDate = returnDate;
        TotalAmount = totalAmount;
        TotalOutstanding = totalOutstanding;
        MerchantId = merchantId;
        DistributorId = distributorId;
        SalesRepId = salesRepId;
        SyncDate = syncDate;
        IsSync = isSync;
        EnteredDate = enteredDate;
        EnteredUser = enteredUser;
        this._DefDistributor = _DefDistributor;
        this._DefMerchant = _DefMerchant;
        this._DistDefSaleRep = _DistDefSaleRep;
        this._ReturnInventoryLineItemList = _ReturnInventoryLineItemList;
    }

    public ReturnInventory(int creditNoteNo, String returnDate,
                           double totalAmount, double totalOutstanding,
                           int merchantId,int salesrepId ,int distributorId,
                           int isSync, String syncDate) {

        this.setCreditNoteNo(creditNoteNo);
        this.setReturnDate(returnDate) ;
        this.setTotalAmount (totalAmount);
        this.setTotalOutstanding(totalOutstanding);
        this.setMerchantId(merchantId);
        this.setSalesRepId(salesrepId);
        this.setDistributorId (distributorId);
        this.setIsSync (isSync);
        this.setSyncDate (syncDate);
    }

    public int getSalesRepId() {
        return SalesRepId;
    }

    public void setSalesRepId(int salesRepId) {
        SalesRepId = salesRepId;
    }

    public int getCreditNoteNo() {
        return CreditNoteNo;
    }

    public void setCreditNoteNo(int creditNoteNo) {
        CreditNoteNo = creditNoteNo;
    }

    public String getReturnDate() {
        return ReturnDate;
    }

    public void setReturnDate(String returnDate) {
        ReturnDate = returnDate;
    }

    public double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        TotalAmount = totalAmount;
    }

    public double getTotalOutstanding() {
        return TotalOutstanding;
    }

    public void setTotalOutstanding(double totalOutstanding) {
        TotalOutstanding = totalOutstanding;
    }

    public int getMerchantId() {
        return MerchantId;
    }

    public void setMerchantId(int merchantId) {
        MerchantId = merchantId;
    }

    public int getDistributorId() {
        return DistributorId;
    }

    public void setDistributorId(int distributorId) {
        DistributorId = distributorId;
    }

    public String getSyncDate() {
        return SyncDate;
    }

    public void setSyncDate(String syncDate) {
        SyncDate = syncDate;
    }

    public int getIsSync() {
        return IsSync;
    }

    public void setIsSync(int isSync) {
        IsSync = isSync;
    }

    public String getEnteredDate() {
        return EnteredDate;
    }

    public void setEnteredDate(String enteredDate) {
        EnteredDate = enteredDate;
    }

    public String getEnteredUser() {
        return EnteredUser;
    }

    public void setEnteredUser(String enteredUser) {
        EnteredUser = enteredUser;
    }

    public DistributorDetails get_DefDistributor() {
        return _DefDistributor;
    }

    public void set_DefDistributor(DistributorDetails _DefDistributor) {
        this._DefDistributor = _DefDistributor;
    }

    public MerchantDetails get_DefMerchant() {
        return _DefMerchant;
    }

    public void set_DefMerchant(MerchantDetails _DefMerchant) {
        this._DefMerchant = _DefMerchant;
    }

    public SalesRep get_DistDefSaleRep() {
        return _DistDefSaleRep;
    }

    public void set_DistDefSaleRep(SalesRep _DistDefSaleRep) {
        this._DistDefSaleRep = _DistDefSaleRep;
    }

    public ArrayList<ReturnInventoryLineItem> get_ReturnInventoryLineItemList() {
        return _ReturnInventoryLineItemList;
    }

    public void set_ReturnInventoryLineItemList(ArrayList<ReturnInventoryLineItem> _ReturnInventoryLineItemList) {
        this._ReturnInventoryLineItemList = _ReturnInventoryLineItemList;
    }

    public ContentValues getReturnInventoryContentValues(){
        ContentValues cv= new ContentValues();
        cv.put(COL_RETURN_INVENTORY_creditNoteNo,getCreditNoteNo());
        cv.put(COL_RETURN_INVENTORY_returnDate,getReturnDate());
        cv.put(COL_RETURN_INVENTORY_totalAmount,getTotalAmount());
        cv.put(COL_RETURN_INVENTORY_totalOutstanding,getTotalOutstanding());
        cv.put(COL_RETURN_INVENTORY_merchantId,getMerchantId());
        cv.put(COL_RETURN_INVENTORY_SalesRepId,getSalesRepId());
        cv.put(COL_RETURN_INVENTORY_distributorId,getDistributorId());
        cv.put(COL_RETURN_INVENTORY_isSync,getIsSync());
        cv.put(COL_RETURN_INVENTORY_syncDate,getSyncDate());
        return  cv;
    }

}
