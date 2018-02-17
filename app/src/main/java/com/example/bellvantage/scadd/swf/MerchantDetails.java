package com.example.bellvantage.scadd.swf;

import android.content.ContentValues;

import java.io.Serializable;

import static com.example.bellvantage.scadd.DB.DbHelper.TBL_MERCHANT_CREDIT_DAYS;
import static com.example.bellvantage.scadd.DB.DbHelper.TBL_MERCHANT_IS_CREDIT;
import static com.example.bellvantage.scadd.DB.DbHelper.TBL_MERCHANT_SYNC_ID;

/**
 * Created by Sachika on 20/06/2017.
 */

public class MerchantDetails implements Serializable {

    private int SequenceId;
    private int MerchantId;
    private String MerchantName;
    private String ContactNo1;
    private String ContactNo2;
    private String Longitude;
    private String Latitude;
    private int IsActive;
    private String EnteredDate;
    private String EnteredUser;
    private int DiscountRate;
    private int IsSync;
    private String SyncDate;
    private String BuildingNo;
    private String Address1;
    private String Address2;
    private String City;
    private String ContactPerson;
    private int AreaCode;
    private String MerchantType;
    private String MerchantClass;
    private int DistrictCode;
    private String UpdatedUser;
    private String UpdatedDate;
    private String ReferenceID;
    private int IsVat;
    private String VatNo;
    private int pathCode;
    private String SyncId;
    private String Image1;
    private String Image2;
    private int isCredit;
    private String creditDays;

    public MerchantDetails(int merchantId, String image1, String image2) {
        MerchantId = merchantId;
        Image1 = image1;
        Image2 = image2;
    }

    public MerchantDetails(int merchantId, String name, int isvat) {
        MerchantId = merchantId;
        MerchantName = name;
        IsVat = isvat;
    }

    public MerchantDetails(int sequenceId, int merchantId, String merchantName, String contactNo1,
                           String contactNo2, String longitude, String latitude,
                           int isActive, String enteredDate, String enteredUser,
                           int discountRate, int isSync, String syncDate, String buildingNo,
                           String address1, String address2, String city, String contactPerson,
                           int areaCode, String merchantType, String merchantClass, int districtCode,
                           String updatedUser, String updatedDate, String referenceID, int isVat,
                           String vatNo, int pathCode, String syncId, int isCredit, String creditDays) {
        this.setSequenceId(sequenceId);
        this.setMerchantId(merchantId);
        this.setMerchantName(merchantName);
        this.setContactNo1(contactNo1);
        this.setContactNo2(contactNo2);
        this.setLongitude(longitude);
        this.setLatitude(latitude);
        this.setIsActive(isActive);
        this.setEnteredDate(enteredDate);
        this.setEnteredUser(enteredUser);
        this.setDiscountRate(discountRate);
        this.setIsSync(isSync);
        this.setSyncDate(syncDate);
        this.setBuildingNo(buildingNo);
        this.setAddress1(address1);
        this.setAddress2(address2);
        this.setCity(city);
        this.setContactPerson(contactPerson);
        this.setAreaCode(areaCode);
        this.setMerchantType(merchantType);
        this.setMerchantClass(merchantClass);
        this.setDistrictCode(districtCode);
        this.setUpdatedUser(updatedUser);
        this.setUpdatedDate(updatedDate);
        this.setReferenceID(referenceID);
        this.setIsVat(isVat);
        this.setVatNo(vatNo);
        this.setPathCode(pathCode);
        this.setSyncId(syncId);
        this.isCredit = isCredit;
        this.creditDays = creditDays;
    }

    public MerchantDetails(int merchntID, String merchantName, String contactNo1, String contactNo2,
                           String longitude, String latitude, int isActive, String enteredDate, String enteredUser,
                           int discountRate, int isSync, String syncDate, String buildingNo, String address1, String address2,
                           String city, String contactPerson, int areaCode, String merchantType, String merchantClass,
                           int districtCode, String updatedUser, String updatedDate, String referenceID, int isVat, String vatNo) {
        this.setMerchantId(merchntID);
        this.setMerchantName(merchantName);
        this.setContactNo1(contactNo1);
        this.setContactNo2(contactNo2);
        this.setLongitude(longitude);
        this.setLatitude(latitude);
        this.setIsActive(isActive);
        this.setEnteredDate(enteredDate);
        this.setEnteredUser(enteredUser);
        this.setDiscountRate(discountRate);
        this.setIsSync(isSync);
        this.setSyncDate(syncDate);
        this.setBuildingNo(buildingNo);
        this.setAddress1(address1);
        this.setAddress2(address2);
        this.setCity(city);
        this.setContactPerson(contactPerson);
        this.setAreaCode(areaCode);
        this.setMerchantType(merchantType);
        this.setMerchantClass(merchantClass);
        this.setDistrictCode(districtCode);
        this.setUpdatedUser(updatedUser);
        this.setUpdatedDate(updatedDate);
        this.setReferenceID(referenceID);
        this.setIsVat(isVat);
        this.setVatNo(vatNo);
    }


    public MerchantDetails(int merchantId, String merchantName, String contactNo1,
                           String contactNo2, String longitude, String latitude,
                           int isActive, String enteredDate, String enteredUser,
                           int discountRate, int isSync, String syncDate, String buildingNo,
                           String address1, String address2, String city, String contactPerson,
                           int areaCode, String merchantType, String merchantClass,
                           int districtCode, String updatedUser, String updatedDate,
                           String referenceID, int isVat, String vatNo,
                           String image1) {

        MerchantId = merchantId;
        MerchantName = merchantName;
        ContactNo1 = contactNo1;
        ContactNo2 = contactNo2;
        Longitude = longitude;
        Latitude = latitude;
        IsActive = isActive;
        EnteredDate = enteredDate;
        EnteredUser = enteredUser;
        DiscountRate = discountRate;
        IsSync = isSync;
        SyncDate = syncDate;
        BuildingNo = buildingNo;
        Address1 = address1;
        Address2 = address2;
        City = city;
        ContactPerson = contactPerson;
        AreaCode = areaCode;
        MerchantType = merchantType;
        MerchantClass = merchantClass;
        DistrictCode = districtCode;
        UpdatedUser = updatedUser;
        UpdatedDate = updatedDate;
        ReferenceID = referenceID;
        IsVat = isVat;
        VatNo = vatNo;
        Image1 = image1;
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

    public int getMerchantId() {
        return MerchantId;
    }

    public void setMerchantId(int merchantId) {
        MerchantId = merchantId;
    }

    public String getMerchantName() {
        return MerchantName;
    }

    public void setMerchantName(String merchantName) {
        MerchantName = merchantName;
    }

    public String getContactNo1() {
        return ContactNo1;
    }

    public void setContactNo1(String contactNo1) {
        ContactNo1 = contactNo1;
    }

    public String getContactNo2() {
        return ContactNo2;
    }

    public void setContactNo2(String contactNo2) {
        ContactNo2 = contactNo2;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public int getIsActive() {
        return IsActive;
    }

    public void setIsActive(int isActive) {
        IsActive = isActive;
    }

    public String getEnteredDate() {
        return EnteredDate;
    }

    public void setEnteredDate(String enteredDate) {
        EnteredDate = enteredDate;
    }

    public String getEnteredUser() {
        return EnteredUser;
    }

    public void setEnteredUser(String enteredUser) {
        EnteredUser = enteredUser;
    }

    public int getDiscountRate() {
        return DiscountRate;
    }

    public void setDiscountRate(int discountRate) {
        DiscountRate = discountRate;
    }

    public int getIsSync() {
        return IsSync;
    }

    public void setIsSync(int isSync) {
        IsSync = isSync;
    }

    public String getSyncDate() {
        return SyncDate;
    }

    public void setSyncDate(String syncDate) {
        SyncDate = syncDate;
    }

    public String getBuildingNo() {
        return BuildingNo;
    }

    public void setBuildingNo(String buildingNo) {
        BuildingNo = buildingNo;
    }

    public String getAddress1() {
        return Address1;
    }

    public void setAddress1(String address1) {
        Address1 = address1;
    }

    public String getAddress2() {
        return Address2;
    }

    public void setAddress2(String address2) {
        Address2 = address2;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getContactPerson() {
        return ContactPerson;
    }

    public void setContactPerson(String contactPerson) {
        ContactPerson = contactPerson;
    }

    public int getAreaCode() {
        return AreaCode;
    }

    public void setAreaCode(int areaCode) {
        AreaCode = areaCode;
    }

    public String getMerchantType() {
        return MerchantType;
    }

    public void setMerchantType(String merchantType) {
        MerchantType = merchantType;
    }

    public String getMerchantClass() {
        return MerchantClass;
    }

    public void setMerchantClass(String merchantClass) {
        MerchantClass = merchantClass;
    }

    public int getDistrictCode() {
        return DistrictCode;
    }

    public void setDistrictCode(int districtCode) {
        DistrictCode = districtCode;
    }

    public String getUpdatedUser() {
        return UpdatedUser;
    }

    public void setUpdatedUser(String updatedUser) {
        UpdatedUser = updatedUser;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        UpdatedDate = updatedDate;
    }

    public String getReferenceID() {
        return ReferenceID;
    }

    public void setReferenceID(String referenceID) {
        ReferenceID = referenceID;
    }

    public int getIsVat() {
        return IsVat;
    }

    public void setIsVat(int isVat) {
        IsVat = isVat;
    }

    public String getVatNo() {
        return VatNo;
    }

    public void setVatNo(String vatNo) {
        VatNo = vatNo;
    }

    public int getPathCode() {
        return pathCode;
    }

    public void setPathCode(int pathCode) {
        this.pathCode = pathCode;
    }

    public int getSequenceId() {
        return SequenceId;
    }

    public void setSequenceId(int sequenceId) {
        SequenceId = sequenceId;
    }

    public String getSyncId() {
        return SyncId;
    }

    public void setSyncId(String syncId) {
        SyncId = syncId;
    }

    public ContentValues getMerchntDetailsContentValues() {
        ContentValues cv = new ContentValues();
        cv.put("MerchantId", getMerchantId());
        cv.put("MerchantName", getMerchantName());
        cv.put("ContactNo1", getContactNo1());
        cv.put("ContactNo2", getContactNo1());
        cv.put("Longitude", getLongitude());
        cv.put("Latitude", getLatitude());
        cv.put("IsActive", getIsActive());
        cv.put("EnteredDate", getEnteredDate());
        cv.put("EnteredUser", getEnteredUser());
        cv.put("DiscountRate", getDiscountRate());
        cv.put("IsSync", getIsSync());
        cv.put("SyncDate", getSyncDate());
        cv.put("BuildingNo", getBuildingNo());
        cv.put("Address1", getAddress1());
        cv.put("Address2", getAddress2());
        cv.put("City", getCity());
        cv.put("ContactPerson", getContactPerson());
        cv.put("AreaCode", getAreaCode());
        cv.put("MerchantType", getMerchantType());
        cv.put("MerchantClass", getMerchantClass());
        cv.put("DistrictCode", getDistrictCode());
        cv.put("UpdatedUser", getUpdatedUser());
        cv.put("UpdatedDate", getUpdatedDate());
        cv.put("ReferenceID", getReferenceID());
        cv.put("IsVat", getIsVat());
        cv.put("VatNo", getVatNo());
        cv.put("pathCode", getPathCode());
        cv.put(TBL_MERCHANT_SYNC_ID, getSyncId());
        cv.put(TBL_MERCHANT_IS_CREDIT, getIsCredit());
        cv.put(TBL_MERCHANT_CREDIT_DAYS, getCreditDays());
        return cv;
    }
}
