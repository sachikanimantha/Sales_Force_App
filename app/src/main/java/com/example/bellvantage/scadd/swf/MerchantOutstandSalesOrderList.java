package com.example.bellvantage.scadd.swf;

/**
 * Created by Bellvantage on 20/06/2017.
 */

public class MerchantOutstandSalesOrderList {

    int PaymentID;
    int InvoiceID;
    double TotalAmount;
    double PaidAmount;
    String PaymentType;
    String PaymentDate;

    public MerchantOutstandSalesOrderList(int paymentID, int invoiceID, double totalAmount, double paidAmount, String paymentType, String paymentDate) {
        PaymentID = paymentID;
        InvoiceID = invoiceID;
        TotalAmount = totalAmount;
        PaidAmount = paidAmount;
        PaymentType = paymentType;
        PaymentDate = paymentDate;
    }

    public int getPaymentID() {
        return PaymentID;
    }

    public void setPaymentID(int paymentID) {
        PaymentID = paymentID;
    }

    public int getInvoiceID() {
        return InvoiceID;
    }

    public void setInvoiceID(int invoiceID) {
        InvoiceID = invoiceID;
    }

    public double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        TotalAmount = totalAmount;
    }

    public double getPaidAmount() {
        return PaidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        PaidAmount = paidAmount;
    }

    public String getPaymentType() {
        return PaymentType;
    }

    public void setPaymentType(String paymentType) {
        PaymentType = paymentType;
    }

    public String getPaymentDate() {
        return PaymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        PaymentDate = paymentDate;
    }
}
