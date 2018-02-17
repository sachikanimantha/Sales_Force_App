package com.example.bellvantage.scadd.swf;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bellvantage on 11/07/2017.
 */

class DefMerchantBean {
    /**
     * MerchantId : 1
     * MerchantName : A COLOMBO  PHARMACY
     * ContactNo1 : 112436447
     * ContactNo2 : 765555555
     * Longitude : 79.8666
     * Latitude : 6.9205
     * IsActive : 1
     * EnteredDate : Date(1499759160950)
     * EnteredUser :
     * DiscountRate : 12
     * IsSync : 1
     * SyncDate : Date(1497933251000)
     * BuildingNo : 123 ug a3-97
     * Address1 : PEOPLES PARK
     * Address2 : petta VIP
     * City : Colombo 11
     * ContactPerson : YOGA
     * AreaCode : 1
     * MerchantType : PRQ
     * MerchantClass : A
     * DistrictCode : 1
     * UpdatedUser : ADMIN
     * UpdatedDate : Date(1497933230443)
     * ReferenceID : A000009
     * IsVat : 1
     * VatNo : 112233
     * SyncId :
     * _ListMerchantPayment : null
     * _ReturnInventory : null
     * _DeliveryLocations : null
     * _ListReturnInventory : null
     * _ListSalesInvoice : null
     * _ListSalesOrder : null
     */

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
    private String SyncId;
    private Object _ListMerchantPayment;
    private Object _ReturnInventory;
    private Object _DeliveryLocations;
    private Object _ListReturnInventory;
    private Object _ListSalesInvoice;
    private Object _ListSalesOrder;

    public static DefMerchantBean objectFromData(String str) {

        return new Gson().fromJson(str, DefMerchantBean.class);
    }

    public static DefMerchantBean objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), DefMerchantBean.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<DefMerchantBean> arrayDefMerchantBeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<DefMerchantBean>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<DefMerchantBean> arrayDefMerchantBeanFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<DefMerchantBean>>() {
            }.getType();

            return new Gson().fromJson(jsonObject.getString(str), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList();


    }

    public int getMerchantId() {
        return MerchantId;
    }

    public void setMerchantId(int MerchantId) {
        this.MerchantId = MerchantId;
    }

    public String getMerchantName() {
        return MerchantName;
    }

    public void setMerchantName(String MerchantName) {
        this.MerchantName = MerchantName;
    }

    public String getContactNo1() {
        return ContactNo1;
    }

    public void setContactNo1(String ContactNo1) {
        this.ContactNo1 = ContactNo1;
    }

    public String getContactNo2() {
        return ContactNo2;
    }

    public void setContactNo2(String ContactNo2) {
        this.ContactNo2 = ContactNo2;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String Longitude) {
        this.Longitude = Longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String Latitude) {
        this.Latitude = Latitude;
    }

    public int getIsActive() {
        return IsActive;
    }

    public void setIsActive(int IsActive) {
        this.IsActive = IsActive;
    }

    public String getEnteredDate() {
        return EnteredDate;
    }

    public void setEnteredDate(String EnteredDate) {
        this.EnteredDate = EnteredDate;
    }

    public String getEnteredUser() {
        return EnteredUser;
    }

    public void setEnteredUser(String EnteredUser) {
        this.EnteredUser = EnteredUser;
    }

    public int getDiscountRate() {
        return DiscountRate;
    }

    public void setDiscountRate(int DiscountRate) {
        this.DiscountRate = DiscountRate;
    }

    public int getIsSync() {
        return IsSync;
    }

    public void setIsSync(int IsSync) {
        this.IsSync = IsSync;
    }

    public String getSyncDate() {
        return SyncDate;
    }

    public void setSyncDate(String SyncDate) {
        this.SyncDate = SyncDate;
    }

    public String getBuildingNo() {
        return BuildingNo;
    }

    public void setBuildingNo(String BuildingNo) {
        this.BuildingNo = BuildingNo;
    }

    public String getAddress1() {
        return Address1;
    }

    public void setAddress1(String Address1) {
        this.Address1 = Address1;
    }

    public String getAddress2() {
        return Address2;
    }

    public void setAddress2(String Address2) {
        this.Address2 = Address2;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String City) {
        this.City = City;
    }

    public String getContactPerson() {
        return ContactPerson;
    }

    public void setContactPerson(String ContactPerson) {
        this.ContactPerson = ContactPerson;
    }

    public int getAreaCode() {
        return AreaCode;
    }

    public void setAreaCode(int AreaCode) {
        this.AreaCode = AreaCode;
    }

    public String getMerchantType() {
        return MerchantType;
    }

    public void setMerchantType(String MerchantType) {
        this.MerchantType = MerchantType;
    }

    public String getMerchantClass() {
        return MerchantClass;
    }

    public void setMerchantClass(String MerchantClass) {
        this.MerchantClass = MerchantClass;
    }

    public int getDistrictCode() {
        return DistrictCode;
    }

    public void setDistrictCode(int DistrictCode) {
        this.DistrictCode = DistrictCode;
    }

    public String getUpdatedUser() {
        return UpdatedUser;
    }

    public void setUpdatedUser(String UpdatedUser) {
        this.UpdatedUser = UpdatedUser;
    }

    public String getUpdatedDate() {
        return UpdatedDate;
    }

    public void setUpdatedDate(String UpdatedDate) {
        this.UpdatedDate = UpdatedDate;
    }

    public String getReferenceID() {
        return ReferenceID;
    }

    public void setReferenceID(String ReferenceID) {
        this.ReferenceID = ReferenceID;
    }

    public int getIsVat() {
        return IsVat;
    }

    public void setIsVat(int IsVat) {
        this.IsVat = IsVat;
    }

    public String getVatNo() {
        return VatNo;
    }

    public void setVatNo(String VatNo) {
        this.VatNo = VatNo;
    }

    public String getSyncId() {
        return SyncId;
    }

    public void setSyncId(String SyncId) {
        this.SyncId = SyncId;
    }

    public Object get_ListMerchantPayment() {
        return _ListMerchantPayment;
    }

    public void set_ListMerchantPayment(Object _ListMerchantPayment) {
        this._ListMerchantPayment = _ListMerchantPayment;
    }

    public Object get_ReturnInventory() {
        return _ReturnInventory;
    }

    public void set_ReturnInventory(Object _ReturnInventory) {
        this._ReturnInventory = _ReturnInventory;
    }

    public Object get_DeliveryLocations() {
        return _DeliveryLocations;
    }

    public void set_DeliveryLocations(Object _DeliveryLocations) {
        this._DeliveryLocations = _DeliveryLocations;
    }

    public Object get_ListReturnInventory() {
        return _ListReturnInventory;
    }

    public void set_ListReturnInventory(Object _ListReturnInventory) {
        this._ListReturnInventory = _ListReturnInventory;
    }

    public Object get_ListSalesInvoice() {
        return _ListSalesInvoice;
    }

    public void set_ListSalesInvoice(Object _ListSalesInvoice) {
        this._ListSalesInvoice = _ListSalesInvoice;
    }

    public Object get_ListSalesOrder() {
        return _ListSalesOrder;
    }

    public void set_ListSalesOrder(Object _ListSalesOrder) {
        this._ListSalesOrder = _ListSalesOrder;
    }
}
