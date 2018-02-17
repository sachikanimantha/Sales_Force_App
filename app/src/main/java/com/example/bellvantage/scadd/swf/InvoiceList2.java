package com.example.bellvantage.scadd.swf;

import android.content.ContentValues;

import com.example.bellvantage.scadd.DB.DbHelper;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Bellvantage on 28/08/2017.
 */

public class InvoiceList2 implements Serializable {

    int VehicleId ;
    String VehicleNumber ;
    String DeliveryDate ;
    int IsDelivered ;
    double OusAmount ;
    double InvoiceAmount ;
    String SyncDate ;
    int IsSync ;
    int SaleStatus ;
    String EnteredUser ;
    String EnteredDate ;
    String InvoiceDate ;
    int SaleTypeId ;
    int SalesRepId ;
    int DistributorId ;
    int MerchantId ;
    int InvoiceId ;
    int isCredit;
    String creditDays;
    double TotalVAT;

    //salesrep
     String salesRepName;
     String salesRepContactNo;

    //_DefMerchant

     String merchantName;
     String buildingNo;
     String address1;
     String address2;
     String city;
     String merchantContactNo;



    public ArrayList<InvoiceLineItem> _ListInvoiceLineItem ;


/*
{\"VehicleId\":0,
\"VehicleNumber\":\"\",
\"DeliveryDate\":\"\\/Date(1504872732249)\\/\",
\"IsDelivered\":0,
\"OusAmount\":0.00,
\"InvoiceAmount\":515.67,
\"SyncDate\":\"\\/Date(1504872732249)\\/\",
\"IsSync\":0,
\"SaleStatus\":2,
\"EnteredUser\":\"\",
\"EnteredDate\":\"\\/Date(1497345354497)\\/\",
\"InvoiceDate\":\"\\/Date(1497292200000)\\/\",
\"SaleTypeId\":0,
\"SalesRepId\":3,
\"DistributorId\":1,
\"MerchantId\":1,
\"InvoiceId\":100000,
\"TotalVAT\":0,

\"_DefSalesRef\":{
	\"SalesRepId\":3,\"Distributord\":1,\"SalesRepName\":\"Sunesth Saminda Liyanagea\",
	\"EpfNumber\":123,\"IsActive\":1,\"EnteredUser\":\"\",\"EnteredDate\":\"\\/Date(1504872732295)\\/\",
	\"PresentAddress\":\"No 4, Kolpittiya \",\"PermanentAddress\":\"Gale Road,Colombo\",
	\"ContactNo\":\"773403674\",\"UpdatedUser\":\"SALESREP\",\"UpdatedDate\":\"\\/Date(1504872384853)\\/\",
	\"ReferenceID\":\"63\",\"NextSalesOrderNo\":10532,\"NextInvoiceNo\":10050,\"SalesRepType\":\"P\",
	\"NextCreditNoteNo\":10061,\"_DefDistributor\":null,\"_ListVehicleInventory\":null,
	\"_ListDeviceInfo\":null,\"_ListDefDefinePath\":null,\"_ListActualPath\":null,\"_ListAttendance\":null,
	\"_ListSalesRepInventory\":null,\"_ListReturnInventory\":null,\"_ListSalesInvoice\":null,
	\"_ListSalesOrder\":null,\"_ListDefMerchant\":null,\"_ListDeliveryLocations\":null,\"_DefLogin\":null},

\"_DefMerchant\":{
	\"MerchantId\":1,\"MerchantName\":\"A COLOMBO  PHARMACY\",\"ContactNo1\":\"112436447\",
	\"ContactNo2\":\"112436447\",\"Longitude\":\"79.86712109375\",\"Latitude\":\"6.92061250000001\",
	\"IsActive\":1,\"EnteredDate\":\"\\/Date(1504872732436)\\/\",\"EnteredUser\":\"\",\"DiscountRate\":0,
	\"IsSync\":1,\"SyncDate\":\"\\/Date(1500015161000)\\/\",\"BuildingNo\":\"123 ug a3-97\",
	\"Address1\":\"PEOPLES PARK\",\"Address2\":\"petta VIP\",\"City\":\"Colombo 11\",\"ContactPerson\":\"YOGA\",
	\"AreaCode\":4,\"MerchantType\":\"B\",\"MerchantClass\":\"A\",\"DistrictCode\":3,\"UpdatedUser\":\"3\",
	\"UpdatedDate\":\"\\/Date(1500047307747)\\/\",\"ReferenceID\":\"A000009\",\"IsVat\":1,\"VatNo\":\"112233\",
	\"SyncId\":\"null\",\"IsCredit\":0,\"CreditLimit\":0,\"CreditDays\":0,\"_ListMerchantPayment\":null,
	\"_ReturnInventory\":null,\"_DeliveryLocations\":null,\"_ListReturnInventory\":null,\"_ListSalesInvoice\":null,
	\"_ListSalesOrder\":null},\"_ListMerchantPayment\":null,\"_ListInvoiceLineItem\":null}
 */

    public InvoiceList2(int vehicleId, String vehicleNumber, String deliveryDate, int isDelivered, double ousAmount,
                        double invoiceAmount, String syncDate, int isSync, int saleStatus, String enteredUser, String enteredDate,
                        String invoiceDate, int saleTypeId, int salesRepId, int distributorId,
                        int merchantId, int invoiceId, double totalVAT,
                        String salesRepName, String salesRepContactNo,
                        String merchantName, String buildingNo,
                        String address1, String address2,
                        String city, int isCredit,String creditDays,String merchantContactNo) {

        this.setVehicleId(vehicleId);
        this.setVehicleNumber(vehicleNumber);
        this.setDeliveryDate(deliveryDate);
        this.setIsDelivered(isDelivered);
        this.setOusAmount(ousAmount);
        this.setInvoiceAmount(invoiceAmount);
        this.setSyncDate(syncDate);
        this.setIsSync(isSync);
        this.setSaleStatus(saleStatus);
        this.setEnteredUser(enteredUser);
        this.setEnteredDate(enteredDate);
        this.setInvoiceDate(invoiceDate);
        this.setSaleTypeId(saleTypeId);
        this.setSalesRepId(salesRepId);
        this.setDistributorId(distributorId);
        this.setMerchantId(merchantId);
        this.setInvoiceId(invoiceId);
        this.setTotalVAT(totalVAT);
        this.setSalesRepName(salesRepName);
        this.setSalesRepContactNo(salesRepContactNo);
        this.setMerchantName(merchantName);
        this.setBuildingNo(buildingNo);
        this.setAddress1(address1);
        this.setAddress2(address2);
        this.setCity(city);
        this.setMerchantContactNo(merchantContactNo);
        this.setIsCredit(isCredit);
        this.setCreditDays(creditDays);

    }

    public InvoiceList2(int vehicleId, String vehicleNumber, String deliveryDate,
                        int isDelivered, double ousAmount, double invoiceAmount,
                        String syncDate, int isSync, int saleStatus,
                        String enteredUser, String enteredDate, String invoiceDate,
                        int saleTypeId, int salesRepId, int distributorId, int merchantId,
                        int invoiceId, double totalVAT, String salesRepName,
                        String salesRepContactNo, String merchantName, String buildingNo,
                        String address1, String address2, String city, String merchantContactNo,
                        ArrayList<InvoiceLineItem> _ListInvoiceLineItem) {
        this.setVehicleId(vehicleId);
        this.setVehicleNumber(vehicleNumber);
        this.setDeliveryDate(deliveryDate);
        this.setIsDelivered(isDelivered);
        this.setOusAmount(ousAmount);
        this.setInvoiceAmount(invoiceAmount);
        this.setSyncDate(syncDate);
        this.setIsSync(isSync);
        this.setSaleStatus(saleStatus);
        this.setEnteredUser(enteredUser);
        this.setEnteredDate(enteredDate);
        this.setInvoiceDate(invoiceDate);
        this.setSaleTypeId(saleTypeId);
        this.setSalesRepId(salesRepId);
        this.setDistributorId(distributorId);
        this.setMerchantId(merchantId);
        this.setInvoiceId(invoiceId);
        this.setTotalVAT(totalVAT);
        this.setSalesRepName(salesRepName);
        this.setSalesRepContactNo(salesRepContactNo);
        this.setMerchantName(merchantName);
        this.setBuildingNo(buildingNo);
        this.setAddress1(address1);
        this.setAddress2(address2);
        this.setCity(city);
        this.setMerchantContactNo(merchantContactNo);
        this.set_ListInvoiceLineItem(_ListInvoiceLineItem);
    }

    public int getIsCredit() {
        return isCredit;
    }

    public void setIsCredit(int isCredit) {
        this.isCredit = isCredit;
    }

    public String getCreditDays() {
        return creditDays;
    }

    public void setCreditDays(String creditDays) {
        this.creditDays = creditDays;
    }

    public ArrayList<InvoiceLineItem> get_ListInvoiceLineItem() {
        return _ListInvoiceLineItem;
    }

    public void set_ListInvoiceLineItem(ArrayList<InvoiceLineItem> _ListInvoiceLineItem) {
        this._ListInvoiceLineItem = _ListInvoiceLineItem;
    }

    public int getVehicleId() {
        return VehicleId;
    }

    public void setVehicleId(int vehicleId) {
        VehicleId = vehicleId;
    }

    public String getVehicleNumber() {
        return VehicleNumber;
    }

    public void setVehicleNumber(String vehicleNumber) {
        VehicleNumber = vehicleNumber;
    }

    public String getDeliveryDate() {
        return DeliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        DeliveryDate = deliveryDate;
    }

    public int getIsDelivered() {
        return IsDelivered;
    }

    public void setIsDelivered(int isDelivered) {
        IsDelivered = isDelivered;
    }

    public double getOusAmount() {
        return OusAmount;
    }

    public void setOusAmount(double ousAmount) {
        OusAmount = ousAmount;
    }

    public double getInvoiceAmount() {
        return InvoiceAmount;
    }

    public void setInvoiceAmount(double invoiceAmount) {
        InvoiceAmount = invoiceAmount;
    }

    public String getSyncDate() {
        return SyncDate;
    }

    public void setSyncDate(String syncDate) {
        SyncDate = syncDate;
    }

    public int getIsSync() {
        return IsSync;
    }

    public void setIsSync(int isSync) {
        IsSync = isSync;
    }

    public int getSaleStatus() {
        return SaleStatus;
    }

    public void setSaleStatus(int saleStatus) {
        SaleStatus = saleStatus;
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

    public String getInvoiceDate() {
        return InvoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        InvoiceDate = invoiceDate;
    }

    public int getSaleTypeId() {
        return SaleTypeId;
    }

    public void setSaleTypeId(int saleTypeId) {
        SaleTypeId = saleTypeId;
    }

    public int getSalesRepId() {
        return SalesRepId;
    }

    public void setSalesRepId(int salesRepId) {
        SalesRepId = salesRepId;
    }

    public int getDistributorId() {
        return DistributorId;
    }

    public void setDistributorId(int distributorId) {
        DistributorId = distributorId;
    }

    public int getMerchantId() {
        return MerchantId;
    }

    public void setMerchantId(int merchantId) {
        MerchantId = merchantId;
    }

    public int getInvoiceId() {
        return InvoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        InvoiceId = invoiceId;
    }

    public double getTotalVAT() {
        return TotalVAT;
    }

    public void setTotalVAT(double totalVAT) {
        TotalVAT = totalVAT;
    }



    public String getSalesRepName() {
        return salesRepName;
    }

    public void setSalesRepName(String salesRepName) {
        this.salesRepName = salesRepName;
    }

    public String getSalesRepContactNo() {
        return salesRepContactNo;
    }

    public void setSalesRepContactNo(String salesRepContactNo) {
        this.salesRepContactNo = salesRepContactNo;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
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
        this.merchantContactNo = merchantContactNo;
    }


    public ContentValues getCVfromInvoicelist(){
        ContentValues cv = new ContentValues();

        cv.put(DbHelper.COL_INVOICE_ITEM_VehicleID,getVehicleId());
        cv.put(DbHelper.COL_INVOICE_ITEM_VehicleNumber,getVehicleNumber());
        cv.put(DbHelper.COL_INVOICE_ITEM_DeliveryDate,getDeliveryDate());
        cv.put(DbHelper.COL_INVOICE_ITEM_isDelivered,getIsDelivered());
        cv.put(DbHelper.COL_INVOICE_ITEM_OusAmount,getOusAmount());
        cv.put(DbHelper.COL_INVOICE_ITEM_InvoiceAmount,getInvoiceAmount());
        cv.put(DbHelper.COL_INVOICE_ITEM_SyncDate,getSyncDate());
        cv.put(DbHelper.COL_INVOICE_ITEM_IsSync,getIsSync());
        cv.put(DbHelper.COL_INVOICE_ITEM_EnteredUser,getEnteredUser());
        cv.put(DbHelper.COL_INVOICE_ITEM_EnteredDate,getEnteredDate());
        cv.put(DbHelper.COL_INVOICE_ITEM_InvoiceDate,getInvoiceDate());
        cv.put(DbHelper.COL_INVOICE_ITEM_SaleTypeId,getSaleTypeId());
        cv.put(DbHelper.COL_INVOICE_ITEM_SalesRepId,getSalesRepId());
        cv.put(DbHelper.COL_INVOICE_ITEM_DistributorId,getDistributorId());
        cv.put(DbHelper.COL_INVOICE_ITEM_MerchantId,getMerchantId());
        cv.put(DbHelper.COL_INVOICE_ITEM_InvoiceId,getInvoiceId());
        cv.put(DbHelper.COL_INVOICE_ITEM_TotalVAT,getTotalVAT());

        cv.put(DbHelper.COL_INVOICE_ITEM_salesRepName,getSalesRepName());
        cv.put(DbHelper.COL_INVOICE_ITEM_salesRepContactNo,getSalesRepContactNo());

        cv.put(DbHelper.COL_INVOICE_ITEM_merchantName,getMerchantName());
        cv.put(DbHelper.COL_INVOICE_ITEM_buildingNo,getBuildingNo());
        cv.put(DbHelper.COL_INVOICE_ITEM_address1,getAddress1());
        cv.put(DbHelper.COL_INVOICE_ITEM_address2,getAddress2());
        cv.put(DbHelper.COL_INVOICE_ITEM_city,getCity());
        cv.put(DbHelper.COL_INVOICE_ITEM_merchantContactNo,getMerchantContactNo());
        cv.put(DbHelper.COL_INVOICE_ITEM_merchantContactNo,getMerchantContactNo());
        cv.put(DbHelper.COL_INVOICE_ITEM_IsCredit,getIsCredit());
        cv.put(DbHelper.COL_INVOICE_ITEM_CreditDays,getCreditDays());
        cv.put(DbHelper.COL_INVOICE_ITEM_merchantContactNo,getMerchantContactNo());



        return cv;
    }
}
