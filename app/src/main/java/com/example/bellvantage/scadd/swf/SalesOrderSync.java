package com.example.bellvantage.scadd.swf;

import android.content.ContentValues;

import java.io.Serializable;

/**
 * Created by Sachika on 11/08/2017.
 */

public class SalesOrderSync implements Serializable {

    private  int SalesOrderID;
    private  int SaleStatus;
    private  int IsSync;

    public SalesOrderSync(int salesOrderID, int saleStatus, int isSync) {
       this.setSalesOrderID(salesOrderID);
        this.setSaleStatus(saleStatus);
        this.setIsSync(isSync);
    }

    public int getSalesOrderID() {
        return SalesOrderID;
    }

    public void setSalesOrderID(int salesOrderID) {
        SalesOrderID = salesOrderID;
    }

    public int getSaleStatus() {
        return SaleStatus;
    }

    public void setSaleStatus(int saleStatus) {
        SaleStatus = saleStatus;
    }

    public int getIsSync() {
        return IsSync;
    }

    public void setIsSync(int isSync) {
        IsSync = isSync;
    }


    public ContentValues getSalesOrderSyncContentValues(){
        ContentValues cv = new ContentValues();

        cv.put("SalesOrderID",getSalesOrderID());
        cv.put("SaleStatus",getSaleStatus());
        cv.put("IsSync",getIsSync());

        return  cv;
    }
}
