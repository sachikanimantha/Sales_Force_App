package com.example.bellvantage.scadd.swf;

import java.io.Serializable;

/**
 * Created by Sachika on 30/11/2017.
 */

public class Payment implements Serializable {
    int paymnetId;
    String paymnetType;

    public Payment(int paymnetId, String paymnetType) {
        this.paymnetId = paymnetId;
        this.paymnetType = paymnetType;
    }

    public int getPaymnetId() {
        return paymnetId;
    }

    public void setPaymnetId(int paymnetId) {
        this.paymnetId = paymnetId;
    }

    public String getPaymnetType() {
        return paymnetType;
    }

    public void setPaymnetType(String paymnetType) {
        this.paymnetType = paymnetType;
    }
}
