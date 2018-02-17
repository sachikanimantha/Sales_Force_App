package com.example.bellvantage.scadd.swf;

import java.io.Serializable;

/**
 * Created by Sachika on 30/11/2017.
 */

public class Cash implements Serializable{
    int cashId;
    String cashtype;

    public Cash(int cashId, String cashtype) {
        this.cashId = cashId;
        this.cashtype = cashtype;
    }

    public int getCashId() {
        return cashId;
    }

    public void setCashId(int cashId) {
        this.cashId = cashId;
    }

    public String getCashtype() {
        return cashtype;
    }

    public void setCashtype(String cashtype) {
        this.cashtype = cashtype;
    }
}
