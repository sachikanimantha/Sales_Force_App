package com.example.bellvantage.scadd.domains;

import android.content.ContentValues;

import java.util.Date;

/**
 * Created by Sachika on 16/06/2017.
 */

public class ActualPath {

    private String ROUTE_DATE;
    private int DISTRIBUTER_ID;
    private int SALES_REP_ID;
    private  String userName;
    private String LOCATION_TIME;
    private double LONGITUDE;
    private double LATITUDE;
    private int IS_SYNC;
    private String SYNC_DATE;
    private float ACCURACY;




    public String getROUTE_DATE() {
        return ROUTE_DATE;
    }

    public void setROUTE_DATE(String ROUTE_DATE) {
        this.ROUTE_DATE = ROUTE_DATE;
    }

    public int getDISTRIBUTER_ID() {
        return DISTRIBUTER_ID;
    }

    public void setDISTRIBUTER_ID(int DISTRIBUTER_ID) {
        this.DISTRIBUTER_ID = DISTRIBUTER_ID;
    }

    public int getSALES_REP_ID() {
        return SALES_REP_ID;
    }

    public void setSALES_REP_ID(int SALES_REP_ID) {
        this.SALES_REP_ID = SALES_REP_ID;
    }

    public String getLOCATION_TIME() {
        return LOCATION_TIME;
    }

    public void setLOCATION_TIME(String LOCATION_TIME) {
        this.LOCATION_TIME = LOCATION_TIME;
    }

    public double getLONGITUDE() {
        return LONGITUDE;
    }

    public void setLONGITUDE(double LONGITUDE) {
        this.LONGITUDE = LONGITUDE;
    }

    public double getLATITUDE() {
        return LATITUDE;
    }

    public void setLATITUDE(double LATITUDE) {
        this.LATITUDE = LATITUDE;
    }

    public int getIS_SYNC() {
        return IS_SYNC;
    }

    public void setIS_SYNC(int IS_SYNC) {
        this.IS_SYNC = IS_SYNC;
    }

    public String getSYNC_DATE() {
        return SYNC_DATE;
    }

    public void setSYNC_DATE(String SYNC_DATE) {
        this.SYNC_DATE = SYNC_DATE;
    }

    public float getACCURACY() {
        return ACCURACY;
    }

    public void setACCURACY(float ACCURACY) {
        this.ACCURACY = ACCURACY;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public ContentValues getActualPathContentvalues(){

        ContentValues cv = new ContentValues();

        cv.put("ROUTE_DATE",getROUTE_DATE());
        cv.put("DISTRIBUTER_ID",getROUTE_DATE());
        cv.put("SALES_REP_ID",getSALES_REP_ID());
        cv.put("USER_NAME",getUserName());
        cv.put("LOCATION_TIME",getLOCATION_TIME());
        cv.put("LONGITUDE",getLONGITUDE());
        cv.put("LATITUDE",getLATITUDE());
        cv.put("IS_SYNC",getIS_SYNC());
        cv.put("SYNC_DATE",getSYNC_DATE());
        cv.put("ACCURACY",getACCURACY());

        return cv;
    }
}
