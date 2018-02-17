package com.example.bellvantage.scadd.swf;

import android.content.ContentValues;

import java.io.Serializable;

/**
 * Created by Sachika on 15/06/2017.
 */

public class Area implements Serializable {
    private String areaCode;
    private String areaName;

    public Area(String areaCode, String areaName) {
        this.setAreaCode(areaCode);
        this.setAreaName(areaName);
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public ContentValues getAreaContentvalues(){
        ContentValues cv = new ContentValues();
        cv.put("areaCode",getAreaCode());
        cv.put("areaName",getAreaName());
        return cv;
    }
}
