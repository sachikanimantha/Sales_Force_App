package com.example.bellvantage.scadd.swf;

import android.content.ContentValues;
import android.database.Cursor;

import java.io.Serializable;

/**
 * Created by Bellvantage on 12/06/2017.
 */

public class Merchant2 implements Serializable {

    String userName;
    int userId ;
    int isVat;

    public Merchant2(String userName,int userId,int isVat) {
        //this.userName = userName;
        //this.userId = userId;
        //this.isVat = isVat;
        this.setUserName(userName);
        this.setUserId(userId);
        this.setIsVat(isVat);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getIsVat() {
        return isVat;
    }

    public void setIsVat(int isVat) {
        this.isVat = isVat;
    }

    public ContentValues getMerchantDetails(){
        ContentValues cv = new ContentValues();
        cv.put("username",userName);
        cv.put("userid",userId);
        cv.put("isvat",isVat);
        return cv;
    }

}
