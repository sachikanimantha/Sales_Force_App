package com.example.bellvantage.scadd.swf;

import android.content.ContentValues;

import com.example.bellvantage.scadd.DB.DbHelper;

import java.io.Serializable;

/**
 * Created by Bellvantage on 27/07/2017.
 */

public class DefProduct implements Serializable {


    /**
     * ProductId : 1
     * PackingTypeId : 1
     * SellingCategoryId : 1
     * CompanyId : 1
     * CategoryId : 1
     * ProductName : ENERGY DRINK (250ML)
     * IsActive : 1
     * EnteredDate : Date(1498644081407)
     * EnteredUser : ADMIN
     * UnitSellingPrice : 240.25
     * UnitDistributorPrice : 240.25
     * ReferenceID : 15800
     * IsVat : 0
     * VatRate : 0
     * SizeOfUnit:0
     * AddToDCard:1
     * _DefProductCategory : {"CategoryId":1,"CategoryName":"Red Bull","CategoryShortName":"RBL","IsActive":1,"EnteredDate":"Date(1496082600000)","EnteredUser":"ADMIN","_defProduct":null}
     * _DefCompany : null
     * _DefProductSellingCategory : null
     * _DefPackingType : null
     * ListPrimarySalesInvoiceLineItem : null
     * ListPrimarySalesLineItem : null
     * ListSalesRepInventory : null
     * _ProductBatchList : null
     */

    private int ProductId;
    private int PackingTypeId;
    private int SellingCategoryId;
    private int CompanyId;
    private int CategoryId;
    private String ProductName;
    private int IsActive;
    private String EnteredDate;
    private String EnteredUser;
    private double UnitSellingPrice;
    private double UnitDistributorPrice;
    private String ReferenceID;
    private int IsVat;
    private int VatRate;
    private int AddToCart;
    private int SizeOfUnit;


    public DefProduct(int productId, int packingTypeId, int sellingCategoryId, int companyId, int categoryId,
                      String productName, int isActive, String enteredDate, String enteredUser,
                      double unitSellingPrice, double unitDistributorPrice, String referenceID,
                      int isVat, int vatRate) {

        this.setProductId(productId);
        this.setPackingTypeId(packingTypeId);
        this.setSellingCategoryId(sellingCategoryId);
        this.setCompanyId(companyId);
        this.setCategoryId(categoryId);
        this.setProductName(productName);
        this.setIsActive(isActive);
        this.setEnteredDate(enteredDate);
        this.setEnteredUser(enteredUser);
        this.setUnitSellingPrice(unitSellingPrice);
        this.setUnitDistributorPrice(unitDistributorPrice);
        this.setReferenceID(referenceID);
        this.setIsVat(isVat);
        this.setVatRate(vatRate);
    }


    public DefProduct(int productId, int packingTypeId, int sellingCategoryId, int companyId, int categoryId,
                      String productName, int isActive, String enteredDate, String enteredUser,
                      double unitSellingPrice, double unitDistributorPrice, String referenceID,
                      int isVat, int vatRate,int sizeOfUnit,int addtoCart) {

        this.setProductId(productId);
        this.setPackingTypeId(packingTypeId);
        this.setSellingCategoryId(sellingCategoryId);
        this.setCompanyId(companyId);
        this.setCategoryId(categoryId);
        this.setProductName(productName);
        this.setIsActive(isActive);
        this.setEnteredDate(enteredDate);
        this.setEnteredUser(enteredUser);
        this.setUnitSellingPrice(unitSellingPrice);
        this.setUnitDistributorPrice(unitDistributorPrice);
        this.setReferenceID(referenceID);
        this.setIsVat(isVat);
        this.setVatRate(vatRate);
        this.setSizeOfUnit(sizeOfUnit);
        this.setAddToCart(addtoCart);
    }


    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public int getPackingTypeId() {
        return PackingTypeId;
    }

    public void setPackingTypeId(int packingTypeId) {
        PackingTypeId = packingTypeId;
    }

    public int getSellingCategoryId() {
        return SellingCategoryId;
    }

    public void setSellingCategoryId(int sellingCategoryId) {
        SellingCategoryId = sellingCategoryId;
    }

    public int getCompanyId() {
        return CompanyId;
    }

    public void setCompanyId(int companyId) {
        CompanyId = companyId;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
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

    public double getUnitSellingPrice() {
        return UnitSellingPrice;
    }

    public void setUnitSellingPrice(double unitSellingPrice) {
        UnitSellingPrice = unitSellingPrice;
    }

    public double getUnitDistributorPrice() {
        return UnitDistributorPrice;
    }

    public void setUnitDistributorPrice(double unitDistributorPrice) {
        UnitDistributorPrice = unitDistributorPrice;
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

    public int getVatRate() {
        return VatRate;
    }

    public void setVatRate(int vatRate) {
        VatRate = vatRate;
    }

    public int getAddToCart() {
        return AddToCart;
    }

    public void setAddToCart(int addToCart) {
        AddToCart = addToCart;
    }

    public int getSizeOfUnit() {
        return SizeOfUnit;
    }

    public void setSizeOfUnit(int sizeOfUnit) {
        SizeOfUnit = sizeOfUnit;
    }

    public ContentValues getProducListContentValues(){

        ContentValues cv = new ContentValues();

        cv.put("ProductId",getProductId());
        cv.put("PackingTypeId",getPackingTypeId());
        cv.put("SellingCategoryId",getSellingCategoryId());
        cv.put("CompanyId",getCompanyId());
        cv.put("CategoryId",getCategoryId());
        cv.put("ProductName",getProductName());
        cv.put("IsActive",getIsActive());
        cv.put("EnteredUser",getEnteredUser());
        cv.put("EnteredDate",getEnteredDate());
        cv.put("UnitSellingPrice",getUnitSellingPrice());
        cv.put("UnitDistributorPrice",getUnitDistributorPrice());
        cv.put("IsVat",getIsVat());
        cv.put("VatRate",getVatRate());
        cv.put(DbHelper.COL_PRODUCT_SizeOfUnit,getSizeOfUnit());
        cv.put(DbHelper.COL_PRODUCT_AddToCard,getAddToCart());

        return  cv;

    }
}
