package com.example.bellvantage.scadd.swf;

import android.content.ContentValues;

import java.util.ArrayList;

import static com.example.bellvantage.scadd.DB.DbHelper.COL_MSTOCK_distributor_id;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_MSTOCK_entered_date;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_MSTOCK_entered_user;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_MSTOCK_is_sync;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_MSTOCK_merchant_id;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_MSTOCK_merchant_stock_id;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_MSTOCK_sales_status;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_MSTOCK_salesrep_id;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_MSTOCK_sync_date;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_MSTOCK_updated_date;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_MSTOCK_updated_user;

/**
 * Created by Bellvantage on 18/10/2017.
 */

public class MerchantStock {

    /*
    {"SyncDate":"\/Date(1508906282281)\/",
"IsSync":0,
"Status":2,
"EnteredUser":"SUNETH",
"EnteredDate":"\/Date(1508178600000)\/",
"UpdatedUser":"",
"UpdatedDate":"\/Date(1508906282281)\/",
"SalesRepId":3,
"DitributorId":1,
"MerchantId":61793,
"MerchentStockId":1617931000,
"_MerchantStockLineItem":[
	{"MerchentStockId":1617931000,
	"ProductID":130,
	"Quantity":3,
	"IsSync":0,
	"SyncDate":"\/Date(1508906282281)\/",
	"_MerchantStock":null,
	"_DefProduct":null},

	{"MerchentStockId":1617931000,
	"ProductID":282,
	"Quantity":3,
	"IsSync":0,
	"SyncDate":"\/Date(1508906282281)\/",
	"_MerchantStock":null,
	"_DefProduct":null},

	{"MerchentStockId":1617931000,
	"ProductID":155,
	"Quantity":2,
	"IsSync":0,
	"SyncDate":"\/Date(1508906282281)\/",
	"_MerchantStock":null,
	"_DefProduct":null}
	]
,"_DefSalesRep":null,
"_DefMerchant":null},
     */

    int MerchentStockId;
    int MerchantId;
    int DitributorId;
    int SalesRepId;
    String EnteredDate;
    String EnteredUser;
    int Status;
    int IsSync;
    String SyncDate;
    String UpdatedUser;
    String UpdatedDate;
    ArrayList<MerchantStockLineitems> _MerchantStockLineItem ;

    public MerchantStock(int merchentStockId, int merchantId, int ditributorId, int salesRepId,
                         String enteredDate, String enteredUser, int status, int isSync, String syncDate,
                         String updatedUser, String updatedDate, ArrayList<MerchantStockLineitems> _MerchantStockLineItem) {
        MerchentStockId = merchentStockId;
        MerchantId = merchantId;
        DitributorId = ditributorId;
        SalesRepId = salesRepId;
        EnteredDate = enteredDate;
        EnteredUser = enteredUser;
        Status = status;
        IsSync = isSync;
        SyncDate = syncDate;
        UpdatedUser = updatedUser;
        UpdatedDate = updatedDate;
        this._MerchantStockLineItem = _MerchantStockLineItem;
    }

    public MerchantStock(int merchentStockId, int merchantId, int ditributorId, int salesRepId,
                         String enteredDate, String enteredUser, int status, int isSync, String syncDate,
                         String updatedUser, String updatedDate) {
        MerchentStockId = merchentStockId;
        MerchantId = merchantId;
        DitributorId = ditributorId;
        SalesRepId = salesRepId;
        EnteredDate = enteredDate;
        EnteredUser = enteredUser;
        Status = status;
        IsSync = isSync;
        SyncDate = syncDate;
        UpdatedUser = updatedUser;
        UpdatedDate = updatedDate;
    }

    public int getMerchentStockId() {
        return MerchentStockId;
    }

    public void setMerchentStockId(int merchentStockId) {
        MerchentStockId = merchentStockId;
    }

    public int getMerchantId() {
        return MerchantId;
    }

    public void setMerchantId(int merchantId) {
        MerchantId = merchantId;
    }

    public int getDitributorId() {
        return DitributorId;
    }

    public void setDitributorId(int ditributorId) {
        DitributorId = ditributorId;
    }

    public int getSalesRepId() {
        return SalesRepId;
    }

    public void setSalesRepId(int salesRepId) {
        SalesRepId = salesRepId;
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

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public int getIsSync() {
        return IsSync;
    }

    public void setIsSync(int isSync) {
        IsSync = isSync;
    }

    public String getSyncDate() {
        return SyncDate;
    }

    public void setSyncDate(String syncDate) {
        SyncDate = syncDate;
    }

    public String getUpdatedUser() {
        return UpdatedUser;
    }

    public void setUpdatedUser(String updatedUser) {
        UpdatedUser = updatedUser;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        UpdatedDate = updatedDate;
    }

    public ArrayList<MerchantStockLineitems> get_MerchantStockLineItem() {
        return _MerchantStockLineItem;
    }

    public void set_MerchantStockLineItem(ArrayList<MerchantStockLineitems> _MerchantStockLineItem) {
        this._MerchantStockLineItem = _MerchantStockLineItem;
    }

    public ContentValues getCVvalues_merchantStock(){

        ContentValues cv = new ContentValues();
        cv.put(COL_MSTOCK_merchant_stock_id,getMerchentStockId());
        cv.put(COL_MSTOCK_merchant_id,getMerchantId());
        cv.put(COL_MSTOCK_distributor_id,getDitributorId());
        cv.put(COL_MSTOCK_salesrep_id,getSalesRepId());
        cv.put(COL_MSTOCK_entered_date,getEnteredDate());
        cv.put(COL_MSTOCK_entered_user,getEnteredUser());
        cv.put(COL_MSTOCK_sales_status,getStatus());
        cv.put(COL_MSTOCK_is_sync,getIsSync());
        cv.put(COL_MSTOCK_sync_date,getSyncDate());
        cv.put(COL_MSTOCK_updated_user,getUpdatedUser());
        cv.put(COL_MSTOCK_updated_date,getUpdatedDate());
        return cv;
    }


}
