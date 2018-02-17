package com.example.bellvantage.scadd.swf;

import android.content.ContentValues;
import android.database.Cursor;

import java.io.Serializable;

import static com.example.bellvantage.scadd.DB.DbHelper.COL_SALESREP_TARGET_month;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SALESREP_TARGET_salesRepId;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SALESREP_TARGET_targetCategoryId;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SALESREP_TARGET_targetQty;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SALESREP_TARGET_year;

/**
 * Created by Sachika on 09/11/2017.
 */

public class SalesRepTarget implements Serializable {
    private int salesRepId = 0;
    private String targetCategoryId;
    private int targetQty = 0;
    private int year = 0;
    private int month = 0;


    public SalesRepTarget() {
    }

    public SalesRepTarget(int salesRepId, String targetCategoryId,
                          int targetQty, int year, int month){
        this.setSalesRepId(salesRepId);
        this.setTargetCategoryId(targetCategoryId);
        this.setTargetQty(targetQty);
        this.setYear(year);
        this.setMonth(month);
    }

    public int getSalesRepId() {
        return salesRepId;
    }

    public void setSalesRepId(int salesRepId) {
        this.salesRepId = salesRepId;
    }

    public String getTargetCategoryId() {
        return targetCategoryId;
    }

    public void setTargetCategoryId(String targetCategoryId) {
        this.targetCategoryId = targetCategoryId;
    }

    public int getTargetQty() {
        return targetQty;
    }

    public void setTargetQty(int targetQty) {
        this.targetQty = targetQty;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }


    public ContentValues getContentValues(){
        ContentValues cv = new ContentValues();

        cv.put(COL_SALESREP_TARGET_salesRepId,getTargetCategoryId());
        cv.put(COL_SALESREP_TARGET_targetCategoryId, getTargetCategoryId());
        cv.put(COL_SALESREP_TARGET_targetQty,getTargetQty());
        cv.put(COL_SALESREP_TARGET_year,getYear());
        cv.put(COL_SALESREP_TARGET_month,getMonth());

        return cv;
    }


    public SalesRepTarget getDbInstance(Cursor cursor){
        SalesRepTarget salesRepTarget = new SalesRepTarget();
        salesRepTarget.setSalesRepId(cursor.getInt(cursor.getColumnIndex(COL_SALESREP_TARGET_salesRepId)));
        salesRepTarget.setTargetCategoryId(cursor.getString(cursor.getColumnIndex(COL_SALESREP_TARGET_targetCategoryId)));
        salesRepTarget.setTargetQty(cursor.getInt(cursor.getColumnIndex(COL_SALESREP_TARGET_targetQty)));
        salesRepTarget.setYear(cursor.getInt(cursor.getColumnIndex(COL_SALESREP_TARGET_year)));
        salesRepTarget.setMonth(cursor.getInt(cursor.getColumnIndex(COL_SALESREP_TARGET_month)));
        return salesRepTarget;
    }

}
