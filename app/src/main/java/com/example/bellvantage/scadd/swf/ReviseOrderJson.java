package com.example.bellvantage.scadd.swf;

import android.content.ContentValues;

import java.io.Serializable;

import static com.example.bellvantage.scadd.DB.DbHelper.COL_REVISE_SALES_JSON_IsSync;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_REVISE_SALES_ORDER_JSON_InvoiceNumber;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_REVISE_SALES_ORDER_JSON_JsonString;

/**
 * Created by Bellvantage on 03/08/2017.
 */

public class ReviseOrderJson implements Serializable {
    private int InvoiceNumber;
    String JsonString;
    int IsSync;

    public ReviseOrderJson(int invoiceNumber, String jsonString, int isSync) {
        this.setInvoiceNumber(invoiceNumber);
        this.setJsonString(jsonString);
        this.setIsSync(isSync);
    }

    public int getInvoiceNumber() {
        return InvoiceNumber;
    }

    public void setInvoiceNumber(int invoiceNumber) {
        InvoiceNumber = invoiceNumber;
    }

    public String getJsonString() {
        return JsonString;
    }

    public void setJsonString(String jsonString) {
        JsonString = jsonString;
    }

    public int getIsSync() {
        return IsSync;
    }

    public void setIsSync(int isSync) {
        IsSync = isSync;
    }

    public ContentValues getReviseSalesOrderContentValues(){
        ContentValues cv = new ContentValues();
        cv.put(COL_REVISE_SALES_ORDER_JSON_InvoiceNumber,getInvoiceNumber());
        cv.put(COL_REVISE_SALES_ORDER_JSON_JsonString,getJsonString());
        cv.put(COL_REVISE_SALES_JSON_IsSync,getIsSync());
        return  cv;
    }

}
