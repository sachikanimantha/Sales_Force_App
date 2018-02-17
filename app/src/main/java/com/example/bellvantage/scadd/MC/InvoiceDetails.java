package com.example.bellvantage.scadd.MC;

import java.io.Serializable;

/**
 * Created by Bellvantage on 25/05/2017.
 */

public class InvoiceDetails implements Serializable{
    private String customer;
    private int customerNo;
    private String invoiceNo;
    private  double grandTotal;
    private  String dateInv;

    public InvoiceDetails(String customer, int customerNo, String invoiceNo, double grandTotal, String dateInv) {
        this.setCustomer(customer);
        this.setCustomerNo(customerNo);
        this.setInvoiceNo(invoiceNo);
        this.setGrandTotal(grandTotal);
        this.setDateInv(dateInv);
    }


    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public int getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(int customerNo) {
        this.customerNo = customerNo;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public double getGrandTotal() {
        return grandTotal;
    }

    public void setDateInv(String dateInv) {
        this.dateInv = dateInv;
    }

    public String getDateInv() {
        return dateInv;
    }

    public void setGrandTotal(double grandTotal) {
        this.grandTotal = grandTotal;
    }



}
