package com.example.bellvantage.scadd.swf;

/**
 * Created by Sachika on 06/06/2017.
 */

public class Merchant {
    private String MerchantId;
    private String MerchantName;
    private String MerchantAddress;
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

    public Merchant(String merchantId, String merchantName, String merchantAddress, String contactNo1, String contactNo2, String longitude, String latitude, int isActive, String enteredDate, String enteredUser, int discountRate, int isSync, String syncDate) {
        this.setMerchantId( merchantId);
        this.setMerchantName(merchantName);
        this.setMerchantAddress(merchantAddress);
        this.setContactNo1(contactNo1);
        this.setContactNo2(contactNo2);
        this.setLongitude(longitude);
        this.setLatitude(latitude);
        this.setIsActive(isActive);
        this.setEnteredDate( enteredDate);
        this.setEnteredUser(enteredUser);
        this.setDiscountRate( discountRate);
        this.setIsSync(isSync);
        this.setSyncDate(syncDate);
    }

    public Merchant(String merchantId, String merchantName) {
        MerchantId = merchantId;
        MerchantName = merchantName;
    }

    public String getMerchantId() {
        return MerchantId;
    }

    public void setMerchantId(String merchantId) {
        MerchantId = merchantId;
    }

    public String getMerchantName() {
        return MerchantName;
    }

    public void setMerchantName(String merchantName) {
        MerchantName = merchantName;
    }

    public String getMerchantAddress() {
        return MerchantAddress;
    }

    public void setMerchantAddress(String merchantAddress) {
        MerchantAddress = merchantAddress;
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
}
