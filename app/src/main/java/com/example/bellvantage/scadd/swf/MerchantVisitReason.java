package com.example.bellvantage.scadd.swf;

import android.content.ContentValues;

import com.example.bellvantage.scadd.DB.DbHelper;

/**
 * Created by Bellvantage on 05/11/2017.
 */

public class MerchantVisitReason {

    int reason_id;
    String description;
    int allowstock;

    public MerchantVisitReason(int reason_id, String description, int allowstock) {
        this.reason_id = reason_id;
        this.description = description;
        this.allowstock = allowstock;
    }

    public int getReason_id() {
        return reason_id;
    }

    public void setReason_id(int reason_id) {
        this.reason_id = reason_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAllowstock() {
        return allowstock;
    }

    public void setAllowstock(int allowstock) {
        this.allowstock = allowstock;
    }


    public ContentValues getCVvaluesMerchantVisitReason(){

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.COL_MVISIT_REASON_REASON_ID,getReason_id());
        contentValues.put(DbHelper.COL_MVISIT_REASON_DESCRIPTION,getDescription());
        contentValues.put(DbHelper.COL_MVISIT_REASON_ALLOWSTOCK,getAllowstock());

        return contentValues;
    }

}
