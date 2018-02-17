package com.example.bellvantage.scadd.swf;

import java.util.ArrayList;

/**
 * Created by Sachika on 5/29/2017.
 */


public class DataService {

        private int SalesOrderNumber ;
        private int CustomerCode ;
        private int OutletCode ;
        private int SubCode ;
        private int PriceListId ;
        private String InvoiceStatus ;
        private int CanceledDate ;
        private String CanceledTime ;
        private String CanceledUser ;
        private String CreatedDate ;
        private String CreatedTime ;
        private String CreatedUser ;
        private String InvoiceNumber ;
        private String PericeylInvoiceNumber ;
        private String OtherInvoiceNumber ;
        private String DeliveryDate ;
        private ArrayList<com.example.bellvantage.scadd.swf.ListSalesOrderLineItem> ListSalesOrderLineItem ;
        private Customer _Customer  ;


    public DataService(int salesOrderNumber, int customerCode, int outletCode, int subCode, int priceListId, String invoiceStatus, int canceledDate, String canceledTime, String canceledUser, String createdDate, String createdTime, String createdUser, String invoiceNumber, String periceylInvoiceNumber, String otherInvoiceNumber, String deliveryDate, ArrayList<com.example.bellvantage.scadd.swf.ListSalesOrderLineItem> listSalesOrderLineItem, Customer _Customer) {
        this.setSalesOrderNumber (salesOrderNumber);
        this.setCustomerCode (customerCode) ;
        this.setOutletCode (outletCode) ;
        this.setSubCode (subCode) ;
        this.setPriceListId (priceListId) ;
        this.setInvoiceStatus (invoiceStatus) ;
        this.setCanceledDate (canceledDate) ;
        this.setCanceledTime (canceledTime) ;
        this.setCanceledUser (canceledUser) ;
        this.setCreatedDate (createdDate) ;
        this.setCreatedTime (createdTime) ;
        this.setCreatedUser (createdUser) ;
        this.setInvoiceNumber (invoiceNumber) ;
        this.setPericeylInvoiceNumber (periceylInvoiceNumber) ;
        this.setOtherInvoiceNumber (otherInvoiceNumber) ;
        this.setDeliveryDate (deliveryDate) ;
        this.setListSalesOrderLineItem (listSalesOrderLineItem) ;
        this.set_Customer (_Customer) ;
    }

    public int getSalesOrderNumber() {
            return SalesOrderNumber;
        }

        public void setSalesOrderNumber(int salesOrderNumber) {
            SalesOrderNumber = salesOrderNumber;
        }

        public int getCustomerCode() {
            return CustomerCode;
        }

        public void setCustomerCode(int customerCode) {
            CustomerCode = customerCode;
        }

        public int getOutletCode() {
            return OutletCode;
        }

        public void setOutletCode(int outletCode) {
            OutletCode = outletCode;
        }

        public int getSubCode() {
            return SubCode;
        }

        public void setSubCode(int subCode) {
            SubCode =subCode;
        }

        public int getPriceListId() {
            return PriceListId;
        }

        public void setPriceListId(int priceListId) {
            PriceListId =priceListId;
        }

        public String getInvoiceStatus() {
            return InvoiceStatus;
        }

        public void setInvoiceStatus(String invoiceStatus) {
            InvoiceStatus = invoiceStatus;
        }

        public int getCanceledDate() {
            return CanceledDate;
        }

        public void setCanceledDate(int canceledDate) {
            CanceledDate = canceledDate;
        }

        public String getCanceledTime() {
            return CanceledTime;
        }

        public void setCanceledTime(String canceledTime) {
            CanceledTime = canceledTime;
        }

        public String getCanceledUser() {
            return CanceledUser;
        }

        public void setCanceledUser(String canceledUser) {
            CanceledUser = canceledUser;
        }

        public String getCreatedDate() {
            return CreatedDate;
        }

        public void setCreatedDate(String createdDate) {
            CreatedDate = createdDate;
        }

        public String getCreatedTime() {
            return CreatedTime;
        }

        public void setCreatedTime(String createdTime) {
            CreatedTime = createdTime;
        }

        public String getCreatedUser() {
            return CreatedUser;
        }

        public void setCreatedUser(String createdUser) {
            CreatedUser = createdUser;
        }

        public String getInvoiceNumber() {
            return InvoiceNumber;
        }

        public void setInvoiceNumber(String invoiceNumber) {
            InvoiceNumber = invoiceNumber;
        }

        public String getPericeylInvoiceNumber() {
            return PericeylInvoiceNumber;
        }

        public void setPericeylInvoiceNumber(String periceylInvoiceNumber) {
            PericeylInvoiceNumber = periceylInvoiceNumber;
        }

        public String getOtherInvoiceNumber() {
            return OtherInvoiceNumber;
        }

        public void setOtherInvoiceNumber(String otherInvoiceNumber) {
            OtherInvoiceNumber = otherInvoiceNumber;
        }

        public String getDeliveryDate() {
            return DeliveryDate;
        }

        public void setDeliveryDate(String deliveryDate) {
            DeliveryDate = deliveryDate;
        }

        public ArrayList<com.example.bellvantage.scadd.swf.ListSalesOrderLineItem> getListSalesOrderLineItem() {
            return ListSalesOrderLineItem;
        }

        public void setListSalesOrderLineItem(ArrayList<com.example.bellvantage.scadd.swf.ListSalesOrderLineItem> listSalesOrderLineItem) {
            ListSalesOrderLineItem = listSalesOrderLineItem;
        }

        public Customer get_Customer() {
            return _Customer;
        }

        public void set_Customer(Customer _Customer) {
            this._Customer = _Customer;
        }






}
