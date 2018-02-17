package com.example.bellvantage.scadd.swf;

import android.content.ContentValues;

/**
 * Created by Bellvantage on 28/07/2017.
 */

public class DistributorDetails  {

    /*
    //using for get salesrep details
    {"SalesRepId":3,
"Distributord":1,
"SalesRepName":"Sunesth Saminda Liyanagea",
"EpfNumber":123,
"IsActive":1,
"EnteredUser":"",
"EnteredDate":"\/Date(1501567938843)\/",
"PresentAddress":"No 4, Kolpittiya ",
"PermanentAddress":"Gale Road,Colombo",
"ContactNo":"773403674",
"UpdatedUser":"Kasun Bandara",
"UpdatedDate":"\/Date(1501229033130)\/",
"ReferenceID":"63","NextSalesOrderNo":10192,
"NextInvoiceNo":10050,
"SalesRepType":"R",
"_DefDistributor":      //using for get dis details
	{"DistributorId":1,
	"RegionId":1,
	"AreaCode":1,
	"TeritoryId":1,
	"DistributorName":"SHADA AGENCIES",
	"ContactNumber":"",
	"Address1":"NO.41/A, RATHWATTHA MAWATHA, BADULLA.",
	"Address2":"salmal rd",
	"Address3":"Colombo 05",
	"Address4":"",
	"Email":"shada@gmail.com",
	"PartnerId":1,
	"IsSync":1,
	"SyncDate":"\/Date(1500529447000)\/",
	"ReferenceId":"DSTAA000",
	"IsVat":1,
	"VatNo":"1234-7000",
	"UpdatedUser":"ADMIN",
	"UpdatedDate":"\/Date(1500529447800)\/",
	"EnteredUser":"ADMIN",
	"EnteredDate":"\/Date(1497033000000)\/",
	"DistributorType":"DST",
	"_DefTeritory":null,
	"_DefAria":null,
	"_DefSalesRep":null,
	"_PrimarySalesOrder":null,
	"_DefLogin":null
	},
"_ListVehicleInventory":null,
"_ListDeviceInfo":null,
"_ListDefDefinePath":null,
"_ListActualPath":null,
"_ListAttendance":null,
"_ListSalesRepInventory":null,
"_ListReturnInventory":null,
"_ListSalesInvoice":null,
"_ListSalesOrder":null,
"_ListDefMerchant":null,
"_ListDeliveryLocations":null,
"_DefLogin":{"Address":"63 Norris Canal Road, Colombo 11",
	"ContactNumber":"0112334455",
	"Name":"Salesrep1",
	"UserType":"S",
	"SyncDate":"\/Date(1496169000000)\/",
	"IsSync":0,
	"ServiceUrl":"www.scadds.lk",
	"UserTypeId":3,
	"UserPassword":"123456",
	"UserName":"SALESREP",
	"EpfNumber":1
}

	}200
     */


    int distributorId;
    int regionId;
    int areaCode;
    int teritoryId;
    String distributorName;
    String contactNumber;
    String address1;
    String address2;
    String address3;
    String address4;
    String email;
    int partnerId;
    int isSync;
    String syncDate;
    String referenceId;
    int isVat;
    String vatNo;
    String updatedUser;
    String updatedDate;
    String enteredUser;
    String enteredDate;
    String distributorType;


    public DistributorDetails(int distributorId, int regionId, int areaCode,
                              int teritoryId, String distributorName, String contactNumber,
                              String address1, String address2, String address3,
                              String address4, String email, int partnerId, int isSync,
                              String syncDate, String referenceId, int isVat, String vatNo,
                              String updatedUser, String updatedDate, String enteredUser,
                              String enteredDate, String distributorType) {

        this.distributorId = distributorId;
        this.regionId = regionId;
        this.areaCode = areaCode;
        this.teritoryId = teritoryId;
        this.distributorName = distributorName;
        this.contactNumber = contactNumber;
        this.address1 = address1;
        this.address2 = address2;
        this.address3 = address3;
        this.address4 = address4;
        this.email = email;
        this.partnerId = partnerId;
        this.isSync = isSync;
        this.syncDate = syncDate;
        this.referenceId = referenceId;
        this.isVat = isVat;
        this.vatNo = vatNo;
        this.updatedUser = updatedUser;
        this.updatedDate = updatedDate;
        this.enteredUser = enteredUser;
        this.enteredDate = enteredDate;
        this.distributorType = distributorType;
    }

    public DistributorDetails(int distributorId, String distributorName, String contactNumber,
                              String address1, String address2, String address3,
                              String address4, String email, int isSync,
                              String syncDate, String referenceId, int isVat, String vatNo,
                              String updatedUser, String updatedDate, String enteredUser,
                              String enteredDate, String distributorType) {

        this.distributorId = distributorId;
        this.distributorName = distributorName;
        this.contactNumber = contactNumber;
        this.address1 = address1;
        this.address2 = address2;
        this.address3 = address3;
        this.address4 = address4;
        this.email = email;
        this.isSync = isSync;
        this.syncDate = syncDate;
        this.referenceId = referenceId;
        this.isVat = isVat;
        this.vatNo = vatNo;
        this.updatedUser = updatedUser;
        this.updatedDate = updatedDate;
        this.enteredUser = enteredUser;
        this.enteredDate = enteredDate;
        this.distributorType = distributorType;
    }


    public DistributorDetails(int distributorId, String distributorName, int isSync, String syncDate,
                              String referenceId, int isVat, String vatNo, String updatedUser, String updatedDate,
                              String enteredUser, String enteredDate, String distributorType) {



        this.distributorId = distributorId;
        this.distributorName = distributorName;
        this.isSync = isSync;
        this.syncDate = syncDate;
        this.referenceId = referenceId;
        this.isVat = isVat;
        this.vatNo = vatNo;
        this.updatedUser = updatedUser;
        this.updatedDate = updatedDate;
        this.enteredUser = enteredUser;
        this.enteredDate = enteredDate;
        this.distributorType = distributorType;




    }

    public int getDistributorId() {
        return distributorId;
    }

    public void setDistributorId(int distributorId) {
        this.distributorId = distributorId;
    }

    public int getRegionId() {
        return regionId;
    }

    public void setRegionId(int regionId) {
        this.regionId = regionId;
    }

    public int getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(int areaCode) {
        this.areaCode = areaCode;
    }

    public int getTeritoryId() {
        return teritoryId;
    }

    public void setTeritoryId(int teritoryId) {
        this.teritoryId = teritoryId;
    }

    public String getDistributorName() {
        return distributorName;
    }

    public void setDistributorName(String distributorName) {
        this.distributorName = distributorName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
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

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getAddress4() {
        return address4;
    }

    public void setAddress4(String address4) {
        this.address4 = address4;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(int partnerId) {
        this.partnerId = partnerId;
    }

    public int getIsSync() {
        return isSync;
    }

    public void setIsSync(int isSync) {
        this.isSync = isSync;
    }

    public String getSyncDate() {
        return syncDate;
    }

    public void setSyncDate(String syncDate) {
        this.syncDate = syncDate;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public int getIsVat() {
        return isVat;
    }

    public void setIsVat(int isVat) {
        this.isVat = isVat;
    }

    public String getVatNo() {
        return vatNo;
    }

    public void setVatNo(String vatNo) {
        this.vatNo = vatNo;
    }

    public String getUpdatedUser() {
        return updatedUser;
    }

    public void setUpdatedUser(String updatedUser) {
        this.updatedUser = updatedUser;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getEnteredUser() {
        return enteredUser;
    }

    public void setEnteredUser(String enteredUser) {
        this.enteredUser = enteredUser;
    }

    public String getEnteredDate() {
        return enteredDate;
    }

    public void setEnteredDate(String enteredDate) {
        this.enteredDate = enteredDate;
    }

    public String getDistributorType() {
        return distributorType;
    }

    public void setDistributorType(String distributorType) {
        this.distributorType = distributorType;
    }

    public ContentValues getDistributorContentValues(){

        ContentValues cv = new ContentValues();
        cv.put("DISTRIBUTOR_ID",getDistributorId());
//        cv.put("REGION_ID",getRegionId());
//        cv.put("AREA_CODE",getAreaCode());
//        cv.put("TERITORY_ID",getTeritoryId());
        cv.put("DISTRIBUTOR_NAME",getDistributorName());
//        cv.put("CONTACT_NUMBER",getContactNumber());
//        cv.put("ADDRESS_1",getAddress1());
//        cv.put("ADDRESS_2",getAddress2());
//        cv.put("ADDRESS_3",getAddress3());
//        cv.put("ADDRESS_4",getAddress4());
//        cv.put("EMAIL",getEmail());
//        cv.put("PARTNER_ID",getPartnerId());
        cv.put("IS_SYNC",getIsSync());
        cv.put("SYNC_DATE",getSyncDate());
        cv.put("REFERENCE_ID",getReferenceId());
        cv.put("IS_VAT",getIsVat());
        cv.put("VAT_NO",getVatNo());
        cv.put("UPDATED_USER",getUpdatedUser());
        cv.put("UPDATED_DATE",getUpdatedDate());
        cv.put("ENTERED_USER",getEnteredUser());
        cv.put("ENTERED_DATE",getEnteredDate());
        cv.put("DISTRIBUTOR_TYPE",getDistributorType());

        return cv;
    }

    public ContentValues getDistributorContentValues2(){

        ContentValues cv = new ContentValues();
        cv.put("DISTRIBUTOR_ID",getDistributorId());
//        cv.put("REGION_ID",getRegionId());
//        cv.put("AREA_CODE",getAreaCode());
//        cv.put("TERITORY_ID",getTeritoryId());
        cv.put("DISTRIBUTOR_NAME",getDistributorName());
        cv.put("CONTACT_NUMBER",getContactNumber());
        cv.put("ADDRESS_1",getAddress1());
        cv.put("ADDRESS_2",getAddress2());
        cv.put("ADDRESS_3",getAddress3());
        cv.put("ADDRESS_4",getAddress4());
        cv.put("EMAIL",getEmail());
//        cv.put("PARTNER_ID",getPartnerId());
        cv.put("IS_SYNC",getIsSync());
        cv.put("SYNC_DATE",getSyncDate());
        cv.put("REFERENCE_ID",getReferenceId());
        cv.put("IS_VAT",getIsVat());
        cv.put("VAT_NO",getVatNo());
        cv.put("UPDATED_USER",getUpdatedUser());
        cv.put("UPDATED_DATE",getUpdatedDate());
        cv.put("ENTERED_USER",getEnteredUser());
        cv.put("ENTERED_DATE",getEnteredDate());
        cv.put("DISTRIBUTOR_TYPE",getDistributorType());

        return cv;
    }

}
