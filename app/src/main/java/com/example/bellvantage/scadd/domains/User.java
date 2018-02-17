package com.example.bellvantage.scadd.domains;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Created by chathura on 11/16/16.
 */
public class User {
    private String EpfNumber;
    private String UserId;
    private String UserFullName;
    private String Password;
    private int OutletCode;
    private int OutletSubCode;
    private int RegionCode;
    private String UserLevel;

    public User(){

    }

    public User(String epfNumber, String userId, String userFullName, String password, int outletCode, int outletSubCode, int regionCode, String userLevel) {
        EpfNumber = epfNumber;
        UserId = userId;
        UserFullName = userFullName;
        Password = password;
        OutletCode = outletCode;
        OutletSubCode = outletSubCode;
        RegionCode = regionCode;
        UserLevel = userLevel;
    }

    public String getEpfNumber() {
        return EpfNumber;
    }

    public void setEpfNumber(String epfNumber) {
        EpfNumber = epfNumber;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserFullName() {
        return UserFullName.trim();
    }

    public void setUserFullName(String userFullName) {
        UserFullName = userFullName.trim();
    }

    public int getOutletCode() {
        return OutletCode;
    }

    public void setOutletCode(int outletCode) {
        OutletCode = outletCode;
    }

    public int getOutletSubCode() {
        return OutletSubCode;
    }

    public void setOutletSubCode(int outletSubCode) {
        OutletSubCode = outletSubCode;
    }

    public int getRegionCode() {
        return RegionCode;
    }

    public void setRegionCode(int regionCode) {
        RegionCode = regionCode;
    }

    public String getUserLevel() {
        return UserLevel;
    }

    public void setUserLevel(String userLevel) {
        UserLevel = userLevel;
    }


    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "EpfNumber='" + EpfNumber + '\'' +
                ", UserId='" + UserId + '\'' +
                ", UserFullName='" + UserFullName + '\'' +
                ", OutletCode=" + OutletCode +
                ", OutletSubCode=" + OutletSubCode +
                ", RegionCode=" + RegionCode +
                ", UserLevel='" + UserLevel + '\'' +
                '}';
    }

    public static User getUser(Cursor cursor) {
        User user = new User();
        int index = 1;
        user.setEpfNumber(cursor.getString(index++));
        user.setUserId(cursor.getString(index++));
        user.setUserFullName(cursor.getString(index++));
        user.setPassword(cursor.getString(index++));
        user.setOutletCode(cursor.getInt(index++));
        user.setOutletSubCode(cursor.getInt(index++));
        user.setUserLevel(cursor.getString(index++));
        return  user;
    }


    public ContentValues getContentValues(){
        ContentValues cv = new ContentValues();
        cv.put("EpfNumber",getEpfNumber().trim());
        cv.put("UserId",getUserId().trim());
        cv.put("UserFullName",getUserFullName().trim());
        cv.put("Password",getPassword().trim());
        cv.put("OutletCode",getOutletCode());
        cv.put("OutletSubCode",getOutletSubCode());
        cv.put("RegionCode",getRegionCode());
        cv.put("UserLevel",getUserLevel());
        return  cv;
    }
}
