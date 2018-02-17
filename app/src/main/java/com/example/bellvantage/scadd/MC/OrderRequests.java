package com.example.bellvantage.scadd.MC;

import java.util.Date;

/**
 * Created by Bellvantage on 22/05/2017.
 */

public class OrderRequests {
    private int requestNo;
    private String customerName;
    private String createdDate;


    public OrderRequests(int requestNo, String customerName, String createdDate) {
        this.setRequestNo(requestNo);
        this.setCustomerName(customerName);
        this.setCreatedDate(createdDate);

    }

    public int getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(int requestNo) {
        this.requestNo = requestNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }


}
