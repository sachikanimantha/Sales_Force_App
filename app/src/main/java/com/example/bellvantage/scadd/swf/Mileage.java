package com.example.bellvantage.scadd.swf;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Date;

import static com.example.bellvantage.scadd.DB.DbHelper.COL_TABLE_MILEAGE_id;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TABLE_MILEAGE_markedTime;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TABLE_MILEAGE_salesRepId;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TABLE_MILEAGE_type;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TABLE_SYNC_MILEAGE_enteredUser;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TABLE_SYNC_MILEAGE_isSyncIn;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TABLE_SYNC_MILEAGE_isSyncOut;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TABLE_SYNC_MILEAGE_mileageDate;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TABLE_SYNC_MILEAGE_mileageIn;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TABLE_SYNC_MILEAGE_mileageInTime;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TABLE_SYNC_MILEAGE_mileageOut;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TABLE_SYNC_MILEAGE_mileageOutTime;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TABLE_SYNC_MILEAGE_salesRepId;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TABLE_SYNC_MILEAGE_syncDate;

/**
 * Created by Sachika on 07/12/2017.
 */

public class Mileage {

    private int id;
    private Date markedTime;
    private int type;

    private String salesRepId;

    String mileageDate;
    String mileageInTime;
    String mileageOutTime;
    String mileageIn;
    String mileageOut;
    String syncDate;
    String enteredUser;
    int isSyncIn;
    int isSyncOut;

    public Mileage() {
    }

    public Mileage( String mileageDate,String salesRepId, String mileageInTime,
                    String mileageIn, String mileageOutTime,  String mileageOut,
                    int isSyncIn, int isSyncOut,String syncDate, String enteredUser) {
        this.salesRepId = salesRepId;
        this.mileageDate = mileageDate;
        this.mileageInTime = mileageInTime;
        this.mileageOutTime = mileageOutTime;
        this.mileageIn = mileageIn;
        this.mileageOut = mileageOut;
        this.syncDate = syncDate;
        this.enteredUser = enteredUser;
        this.isSyncIn = isSyncIn;
        this.isSyncOut= isSyncOut;
    }

    public int getIsSyncIn() {
        return isSyncIn;
    }

    public int getIsSyncOut() {
        return isSyncOut;
    }

    public void setIsSyncOut(int isSyncOut) {
        this.isSyncOut = isSyncOut;
    }

    public void setIsSyncIn(int isSync) {
        this.isSyncIn = isSync;
    }

    public String getMileageDate() {
        return mileageDate;
    }

    public void setMileageDate(String mileageDate) {
        this.mileageDate = mileageDate;
    }

    public String getMileageInTime() {
        return mileageInTime;
    }

    public void setMileageInTime(String mileageInTime) {
        this.mileageInTime = mileageInTime;
    }

    public String getMileageOutTime() {
        return mileageOutTime;
    }

    public void setMileageOutTime(String mileageOutTime) {
        this.mileageOutTime = mileageOutTime;
    }

    public String getMileageIn() {
        return mileageIn;
    }

    public void setMileageIn(String mileageIn) {
        this.mileageIn = mileageIn;
    }

    public String getMileageOut() {
        return mileageOut;
    }

    public void setMileageOut(String mileageOut) {
        this.mileageOut = mileageOut;
    }

    public String getSyncDate() {
        return syncDate;
    }

    public void setSyncDate(String syncDate) {
        this.syncDate = syncDate;
    }

    public String getEnteredUser() {
        return enteredUser;
    }

    public void setEnteredUser(String enteredUser) {
        this.enteredUser = enteredUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getMarkedTime() {
        return markedTime;
    }

    public void setMarkedTime(Date markedTime) {
        this.markedTime = markedTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSalesRepId() {
        return salesRepId;
    }

    public void setSalesRepId(String salesRepId) {
        this.salesRepId = salesRepId;
    }

    public ContentValues getContenValues(){
        ContentValues cv = new ContentValues();
        cv.put(COL_TABLE_MILEAGE_id,getId());
        cv.put(COL_TABLE_MILEAGE_markedTime,getMarkedTime().getTime());
        cv.put(COL_TABLE_MILEAGE_type,getType());
        cv.put(COL_TABLE_MILEAGE_salesRepId,getSalesRepId());
        return cv;
    }

    public ContentValues getSyncContenValues(){
        ContentValues cv = new ContentValues();
        cv.put(COL_TABLE_SYNC_MILEAGE_mileageDate,getMileageDate());
        cv.put(COL_TABLE_SYNC_MILEAGE_salesRepId,getSalesRepId());
        cv.put(COL_TABLE_SYNC_MILEAGE_mileageInTime,getMileageInTime());
        cv.put(COL_TABLE_SYNC_MILEAGE_mileageIn,getMileageIn());
        cv.put(COL_TABLE_SYNC_MILEAGE_mileageOutTime,getMileageOutTime());
        cv.put(COL_TABLE_SYNC_MILEAGE_mileageOut,getMileageDate());
        cv.put(COL_TABLE_SYNC_MILEAGE_isSyncIn,getIsSyncIn());
        cv.put(COL_TABLE_SYNC_MILEAGE_isSyncOut,getIsSyncOut());
        cv.put(COL_TABLE_SYNC_MILEAGE_syncDate,getSyncDate());
        cv.put(COL_TABLE_SYNC_MILEAGE_enteredUser,getEnteredUser());
        return cv;
    }


    public static Mileage getDBInstance(Cursor cursor){
        Mileage mileage = new Mileage();
        mileage.setId(cursor.getInt(cursor.getColumnIndex(COL_TABLE_MILEAGE_id)));
        mileage.setMarkedTime(new Date(cursor.getInt(cursor.getColumnIndex(COL_TABLE_MILEAGE_markedTime))));
        mileage.setType(cursor.getInt(cursor.getColumnIndex(COL_TABLE_MILEAGE_type)));
        mileage.setSalesRepId(cursor.getString(cursor.getColumnIndex(COL_TABLE_MILEAGE_salesRepId)));
        return mileage;
    }


}
