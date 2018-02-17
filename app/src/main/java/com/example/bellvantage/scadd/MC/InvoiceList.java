package com.example.bellvantage.scadd.MC;


import java.io.Serializable;

/**
 * Created by Sachika on 31/05/2017.
 */

public class InvoiceList implements Serializable{

    // get invoice details
    private int invoiceId;
    private int saleTypeId;
    private int IsVat;
    private double totalVat;
    private String invoiceDate;
    private String deliveryDate;

    //get sales rep details

    private String salesRepName;
    private String salesRepContactNo;

    //_DefMerchant

    private String merchantName;
    private String buildingNo;
    private String address1;
    private String address2;
    private String city;
    private String merchantContactNo;

    private int destributorId;

    public InvoiceList(int invoiceId, int saleTypeId, String invoiceDate,
                       String deliveryDate, String salesRepName, String salesRepContactNo,
                       String merchantName, String buildingNo, String address1, String address2,
                       String city, String merchantContactNo,int destributorId,int isVat,double totalVat) {
            this.setInvoiceId(invoiceId);
            this.setSaleTypeId(saleTypeId);
            this.setInvoiceDate(invoiceDate);
            this.setDeliveryDate ( deliveryDate);
            this.setSalesRepName ( salesRepName);
            this.setSalesRepContactNo ( salesRepContactNo);
            this.setMerchantName ( merchantName);
            this.setBuildingNo(buildingNo);
            this.setAddress1(address1);
            this.setAddress2(address2);
            this.setCity(city);
            this.setMerchantContactNo ( merchantContactNo);
            this.setDestributorId(destributorId);
            this.setIsVat(isVat);
            this.setTotalVat(totalVat);

    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getSaleTypeId() {
        return saleTypeId;
    }

    public void setSaleTypeId(int saleTypeId) {
        this.saleTypeId = saleTypeId;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        if(deliveryDate==null){
            this.deliveryDate = "Date(1496169000000)";
        }else{
            this.deliveryDate = deliveryDate;
        }

    }

    public String getSalesRepName() {
        return salesRepName;
    }

    public void setSalesRepName(String salesRepName) {
        if (salesRepName==null){
            this.salesRepName = "Sales Rep";
        }else{
            this.salesRepName = salesRepName;
        }

    }

    public String getSalesRepContactNo() {
        return salesRepContactNo;
    }

    public void setSalesRepContactNo(String salesRepContactNo) {
        if (salesRepContactNo==null){
            this.salesRepContactNo = "No Number";
        }else{
            this.salesRepContactNo = salesRepContactNo;
        }
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
       if (merchantName==null){
           this.merchantName ="Not avilable";
       }else{
           this.merchantName =merchantName;
       }
    }

    public String getBuildingNo() {
        return buildingNo;
    }

    public void setBuildingNo(String buildingNo) {
        this.buildingNo = buildingNo;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMerchantContactNo() {
        return merchantContactNo;
    }

    public void setMerchantContactNo(String merchantContactNo) {
       if(merchantContactNo==null){
           this.merchantContactNo = "No number";
       }else{
           this.merchantContactNo = merchantContactNo;
       }
    }

    public int getDestributorId() {
        return destributorId;
    }

    public void setDestributorId(int destributorId) {
        this.destributorId = destributorId;
    }

    public int getIsVat() {
        return IsVat;
    }

    public void setIsVat(int isVat) {
        IsVat = isVat;
    }

    public double getTotalVat() {
        return totalVat;
    }

    public void setTotalVat(double totalVat) {
        this.totalVat = totalVat;
    }
}
