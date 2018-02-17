package com.example.bellvantage.scadd.swf;

import android.content.ContentValues;

/**
 * Created by Sachika on 15/06/2017.
 */

public class DistrictMerchant {
    private String districtName;
    private String districtCode;



    public DistrictMerchant(String districtName, String districtCode) {
        this.setDistrictCode(districtCode);
        this.setDistrictName(districtName);
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public ContentValues getDistrictCcontetnValue(){
        ContentValues cv = new ContentValues();
        cv.put("districtCode",districtCode);
        cv.put("districtName",districtName);
        return  cv;
    }
}
