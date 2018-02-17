package com.example.bellvantage.scadd.swf;

import android.content.ContentValues;

import com.example.bellvantage.scadd.DB.DbHelper;

/**
 * Created by Bellvantage on 25/08/2017.
 */

public class SyncTables {

    int id ;
    String name;
    String time;
    int rowcount;
    int rowcount_new;
    int salesrepid;
    int status;

    public SyncTables(int id, String name, String time, int rowcount,int rowcount_new, int salesrep_id, int status) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.rowcount = rowcount;
        this.salesrepid = salesrep_id;
        this.status = status;
        this.rowcount_new = rowcount_new;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getRowcount() {
        return rowcount;
    }

    public void setRowcount(int rowcount) {
        this.rowcount = rowcount;
    }

    public int getSalesrepid() {
        return salesrepid;
    }

    public void setSalesrepid(int salesrepid) {
        this.salesrepid = salesrepid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getRowcount_new() {
        return rowcount_new;
    }

    public void setRowcount_new(int rowcount_new) {
        this.rowcount_new = rowcount_new;
    }

    public ContentValues getSyncCVvalues(){

        ContentValues contentValues = new ContentValues();
        contentValues.put(DbHelper.COL_SYNC_ID,getId());
        contentValues.put(DbHelper.COL_SYNC_TABLE_NAME,getName());
        contentValues.put(DbHelper.COL_SYNC_TABLE_TIME,getTime());
        contentValues.put(DbHelper.COL_SYNC_ROWCOUNT,getRowcount());
        contentValues.put(DbHelper.COL_SYNC_ROWCOUNT_NEW,getRowcount_new());//
        contentValues.put(DbHelper.COL_SYNC_SALESREPID,getSalesrepid());
        contentValues.put(DbHelper.COL_SYNC_STATUS,getStatus());
        return contentValues;
    }
}
