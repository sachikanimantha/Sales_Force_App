package com.example.bellvantage.scadd;

import java.io.Serializable;

/**
 * Created by Sachika on 15/11/2017.
 */

public class Target implements Serializable {

    String TargetCategoryId;
    int TargetQuantity;
    String TargetCategoryName;
    int ProductId;

    public Target(String targetCategoryId, int targetQuantity, String targetCategoryName, int productId) {
        TargetCategoryId = targetCategoryId;
        TargetQuantity = targetQuantity;
        TargetCategoryName = targetCategoryName;
        ProductId = productId;
    }


    public String getTargetCategoryId() {
        return TargetCategoryId;
    }

    public void setTargetCategoryId(String targetCategoryId) {
        TargetCategoryId = targetCategoryId;
    }

    public int getTargetQuantity() {
        return TargetQuantity;
    }

    public void setTargetQuantity(int targetQuantity) {
        TargetQuantity = targetQuantity;
    }

    public String getTargetCategoryName() {
        return TargetCategoryName;
    }

    public void setTargetCategoryName(String targetCategoryName) {
        TargetCategoryName = targetCategoryName;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }
}
