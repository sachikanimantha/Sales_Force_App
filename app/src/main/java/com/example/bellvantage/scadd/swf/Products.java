package com.example.bellvantage.scadd.swf;

/**
 * Created by Sachika on 5/29/2017.
 */

public class Products {

    private String ProductCode;
    private String ProductName;
    private String ProductShortCode;
    private String ProductType;
    private String ProductPrice;
    private String ProductPriceBulk;
    private String PackingType;
    private int NoofItem;
    private int Size;
    private int StockQuantity;

    public String getProductCode() {
        return ProductCode;
    }

    public void setProductCode(String productCode) {
        ProductCode = productCode;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductShortCode() {
        return ProductShortCode;
    }

    public void setProductShortCode(String productShortCode) {
        ProductShortCode = productShortCode;
    }

    public String getProductType() {
        return ProductType;
    }

    public void setProductType(String productType) {
        ProductType = productType;
    }

    public String getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(String productPrice) {
        ProductPrice = productPrice;
    }

    public String getProductPriceBulk() {
        return ProductPriceBulk;
    }

    public void setProductPriceBulk(String productPriceBulk) {
        ProductPriceBulk = productPriceBulk;
    }

    public String getPackingType() {
        return PackingType;
    }

    public void setPackingType(String packingType) {
        PackingType = packingType;
    }

    public int getNoofItem() {
        return NoofItem;
    }

    public void setNoofItem(int noofItem) {
        NoofItem = noofItem;
    }

    public int getSize() {
        return Size;
    }

    public void setSize(int size) {
        Size = size;
    }

    public int getStockQuantity() {
        return StockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        StockQuantity = stockQuantity;
    }
}
