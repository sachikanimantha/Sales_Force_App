package com.example.bellvantage.scadd.swf;

import android.content.ContentValues;

import java.io.Serializable;

import static com.example.bellvantage.scadd.DB.DbHelper.COL_RETURN_INVENTORY_JSON_IsSync;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_RETURN_INVENTORY_JSON_JsonString;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_RETURN_INVENTORY_JSON_creditNoteNo;

/**
 * Created by Sachika on 22/08/2017.
 */

public class ReturnInventoryJson implements Serializable {

    private  int creditNoteNo;
    private String JsonString;
    private int IsSYnc;

    public ReturnInventoryJson(int creditNoteNo, String jsonString, int isSYnc) {
        this.setCreditNoteNo(creditNoteNo);
        this.setJsonString(jsonString);
        this.setIsSYnc(isSYnc);
    }

    public int getCreditNoteNo() {
        return creditNoteNo;
    }

    public void setCreditNoteNo(int creditNoteNo) {
        this.creditNoteNo = creditNoteNo;
    }

    public String getJsonString() {
        return JsonString;
    }

    public void setJsonString(String jsonString) {
        JsonString = jsonString;
    }

    public int getIsSYnc() {
        return IsSYnc;
    }

    public void setIsSYnc(int isSYnc) {
        IsSYnc = isSYnc;
    }

    public ContentValues getReturnInvntoryJsonValues(){
        ContentValues cv = new ContentValues();
        cv.put(COL_RETURN_INVENTORY_JSON_creditNoteNo,getCreditNoteNo());
        cv.put(COL_RETURN_INVENTORY_JSON_JsonString,getJsonString());
        cv.put(COL_RETURN_INVENTORY_JSON_IsSync,getIsSYnc());
        return  cv;
    }
}
