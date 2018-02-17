package com.example.bellvantage.scadd.swf;

import android.content.ContentValues;

import com.example.bellvantage.scadd.DB.DbHelper;

/**
 * Created by Bellvantage on 05/11/2017.
 */

public class MerchantVisit {

    int merchant_id ;
    int reason_id;
    int salesrep_id;
    int distributor_id;
    int deliverypath_id;
    int is_sync;
    String entered_date;
    String entered_user;

    public MerchantVisit(int merchant_id, int reason_id, int salesrep_id, int distributor_id,
                         int deliverypath_id,int issync, String entered_date, String entered_user) {
        this.merchant_id = merchant_id;
        this.reason_id = reason_id;
        this.salesrep_id = salesrep_id;
        this.distributor_id = distributor_id;
        this.deliverypath_id = deliverypath_id;
        this.is_sync = issync;
        this.entered_date = entered_date;
        this.entered_user = entered_user;
    }

    public int getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(int merchant_id) {
        this.merchant_id = merchant_id;
    }

    public int getReason_id() {
        return reason_id;
    }

    public void setReason_id(int reason_id) {
        this.reason_id = reason_id;
    }

    public int getSalesrep_id() {
        return salesrep_id;
    }

    public void setSalesrep_id(int salesrep_id) {
        this.salesrep_id = salesrep_id;
    }

    public int getDistributor_id() {
        return distributor_id;
    }

    public void setDistributor_id(int distributor_id) {
        this.distributor_id = distributor_id;
    }

    public int getDeliverypath_id() {
        return deliverypath_id;
    }

    public void setDeliverypath_id(int deliverypath_id) {
        this.deliverypath_id = deliverypath_id;
    }

    public String getEntered_date() {
        return entered_date;
    }

    public void setEntered_date(String entered_date) {
        this.entered_date = entered_date;
    }

    public String getEntered_user() {
        return entered_user;
    }

    public void setEntered_user(String entered_user) {
        this.entered_user = entered_user;
    }

    public int getIs_sync() {
        return is_sync;
    }

    public void setIs_sync(int is_sync) {
        this.is_sync = is_sync;
    }

    public ContentValues getCVvaluesfromMerchantVisit(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.COL_MVISIT_MERCHANT_ID,getMerchant_id());
        contentValues.put(DbHelper.COL_MVISIT_REASON_ID,getReason_id());
        contentValues.put(DbHelper.COL_MVISIT_SALESREP_ID,getReason_id());
        contentValues.put(DbHelper.COL_MVISIT_DISTRIBUTOR_ID,getDistributor_id());
        contentValues.put(DbHelper.COL_MVISIT_DELIVERYPATH_ID,getDeliverypath_id());
        contentValues.put(DbHelper.COL_MVISIT_ISSYNC,getIs_sync());
        contentValues.put(DbHelper.COL_MVISIT_ENTERED_DATE,getEntered_date());
        contentValues.put(DbHelper.COL_MVISIT_ENTERED_USER,getEntered_user());

        return contentValues;
    }


}
