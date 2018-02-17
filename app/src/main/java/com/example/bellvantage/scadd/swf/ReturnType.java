package com.example.bellvantage.scadd.swf;

import android.content.ContentValues;

import com.example.bellvantage.scadd.DB.DbHelper;

import java.io.Serializable;

/**
 * Created by Sachika on 16/08/2017.
 */

public class ReturnType implements Serializable {


    /**
     * ReturnType : 1
     * ReturnTypeName : Expired
     */

    private int ReturnType;
    private String ReturnTypeName;

    public ReturnType(int returnType, String returnTypeName) {
       this.setReturnType(returnType);
       this.setReturnTypeName(returnTypeName);
    }

    public int getReturnType() {
        return ReturnType;
    }

    public void setReturnType(int ReturnType) {
        this.ReturnType = ReturnType;
    }

    public String getReturnTypeName() {
        return ReturnTypeName;
    }

    public void setReturnTypeName(String ReturnTypeName) {
        this.ReturnTypeName = ReturnTypeName;
    }

    public ContentValues getReturnTypeCV(){

        ContentValues cv = new ContentValues();

        cv.put(DbHelper.COL_RETURN_TYPE_ReturnType,getReturnType());
        cv.put(DbHelper.COL_RETURN_TYPE_ReturnTypeName,getReturnTypeName());

        return cv;
    }

}
