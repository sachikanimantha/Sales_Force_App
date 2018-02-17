package com.example.bellvantage.scadd.swf;

/**
 * Created by Bellvantage on 13/06/2017.
 */

public class BrandList {
    String productName;
    int productId;

    public BrandList(String productName,int productId) {
        this.productName = productName;
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }
}
