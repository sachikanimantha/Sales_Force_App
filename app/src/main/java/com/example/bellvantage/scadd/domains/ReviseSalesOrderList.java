package com.example.bellvantage.scadd.domains;

import java.io.Serializable;

/**
 * Created by Bellvantage on 31/07/2017.
 */

public class ReviseSalesOrderList implements Serializable {

    int ProductId;
    int BatchID;
    double UnitSellingPrice;
    String ExpiryDate;
    String ProductName;
    int CategoryId;
    int IsVat;
    int Stock;
    int Quantity;
    int FreeQuantity;
    boolean IsCheckedItem;
    double amount;
    double VatRate;
    double vatAmount;


    public ReviseSalesOrderList(int productId, int batchID, int quantity,int freeQuantity) {
        this.ProductId = productId;
        this.BatchID = batchID;
        this.Quantity = quantity;
        this.FreeQuantity = freeQuantity;
    }

    public ReviseSalesOrderList(int productId, int batchID, double unitSellingPrice, String expiryDate,
                                String productName, int categoryId, int isVat, double vatRate, int stock, int quantity,
                                int freeQuantity, boolean isChecked, double amount, double vatAmount) {
        this.setProductId(productId);
        this.setBatchID(batchID);
        this.setUnitSellingPrice(unitSellingPrice);
        this.setExpiryDate(expiryDate);
        this.setProductName(productName);
        this.setCategoryId(categoryId);
        this.setIsVat(isVat);
        this.setStock(stock);
        this.setQuantity(quantity);
        this.setFreeQuantity(freeQuantity);
        this.setISCheckedItem(isChecked);
        this.setAmount(amount);
        this.setVatRate(vatRate);
        this.setVatAmount(vatAmount);
    }

    public double getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(double vatAmount) {
        this.vatAmount = vatAmount;
    }

    public double getVatRate() {
        return VatRate;
    }

    public void setVatRate(double vatRate) {
        VatRate = vatRate;
    }

    public boolean isCheckedItem() {
        return IsCheckedItem;
    }

    public void setCheckedItem(boolean checkedItem) {
        IsCheckedItem = checkedItem;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean getIsCheckedItem() {
        return IsCheckedItem;
    }

    public void setISCheckedItem(boolean checkedItem) {
        IsCheckedItem = checkedItem;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public int getBatchID() {
        return BatchID;
    }

    public void setBatchID(int batchID) {
        BatchID = batchID;
    }

    public double getUnitSellingPrice() {
        return UnitSellingPrice;
    }

    public void setUnitSellingPrice(double unitSellingPrice) {
        UnitSellingPrice = unitSellingPrice;
    }

    public String getExpiryDate() {
        return ExpiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        ExpiryDate = expiryDate;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }

    public int getIsVat() {
        return IsVat;
    }

    public void setIsVat(int isVat) {
        IsVat = isVat;
    }

    public int getStock() {
        return Stock;
    }

    public void setStock(int stock) {
        Stock = stock;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public int getFreeQuantity() {
        return FreeQuantity;
    }

    public void setFreeQuantity(int freeQuantity) {
        FreeQuantity = freeQuantity;
    }
}
