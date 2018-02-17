package com.example.bellvantage.scadd.swf;

import android.content.ContentValues;

import java.io.Serializable;

import static com.example.bellvantage.scadd.DB.DbHelper.COL_TABLE_INVOICE_UTILIZATION_CreditNoteNo;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TABLE_INVOICE_UTILIZATION_EnteredDate;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TABLE_INVOICE_UTILIZATION_EnteredUser;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TABLE_INVOICE_UTILIZATION_InvoiceId;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TABLE_INVOICE_UTILIZATION_IsSync;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TABLE_INVOICE_UTILIZATION_UtilizedAmount;

/**
 * Created by Bellvantage on 15/09/2017.
 */

public class InvoiceUtilization implements Serializable {

    private int InvoiceId;
    private int CreditNoteNo;
    private double UtilizedAmount;
    String EnteredUser;
    String EnteredDate;
    int IsSync;

    public InvoiceUtilization(int invoiceId, int creditNoteNo, double utilizedAmount, String enteredUser, String enteredDate,int isSync) {
        this.setInvoiceId(invoiceId);
        this.setCreditNoteNo(creditNoteNo);
        this.setUtilizedAmount(utilizedAmount);
        this.setEnteredUser(enteredUser);
        this.setEnteredDate(enteredDate);
        this.setIsSync(isSync);
    }

    public int getIsSync() {
        return IsSync;
    }

    public void setIsSync(int isSync) {
        IsSync = isSync;
    }

    public int getInvoiceId() {
        return InvoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        InvoiceId = invoiceId;
    }

    public int getCreditNoteNo() {
        return CreditNoteNo;
    }

    public void setCreditNoteNo(int creditNoteNo) {
        CreditNoteNo = creditNoteNo;
    }

    public double getUtilizedAmount() {
        return UtilizedAmount;
    }

    public void setUtilizedAmount(double utilizedAmount) {
        UtilizedAmount = utilizedAmount;
    }

    public String getEnteredUser() {
        return EnteredUser;
    }

    public void setEnteredUser(String enteredUser) {
        EnteredUser = enteredUser;
    }

    public String getEnteredDate() {
        return EnteredDate;
    }

    public void setEnteredDate(String enteredDate) {
        EnteredDate = enteredDate;
    }


    public ContentValues getInvoiceUtilizationCV(){
        ContentValues cv = new ContentValues();
        cv.put(COL_TABLE_INVOICE_UTILIZATION_InvoiceId,getInvoiceId());
        cv.put(COL_TABLE_INVOICE_UTILIZATION_CreditNoteNo,getCreditNoteNo());
        cv.put(COL_TABLE_INVOICE_UTILIZATION_UtilizedAmount,getUtilizedAmount());
        cv.put(COL_TABLE_INVOICE_UTILIZATION_EnteredUser,getEnteredUser());
        cv.put(COL_TABLE_INVOICE_UTILIZATION_EnteredDate,getEnteredDate());
        cv.put(COL_TABLE_INVOICE_UTILIZATION_IsSync,getIsSync());

        return cv;
    }


}