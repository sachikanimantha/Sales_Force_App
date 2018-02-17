package com.example.bellvantage.scadd.swf;

import android.content.ContentValues;

import com.example.bellvantage.scadd.DB.DbHelper;

/**
 * Created by Bellvantage on 22/12/2017.
 */

public class PromotionList {

    /*
    "ProductId\":1,
\"BatchId\":1,
\"CatalogueType\":0,
\"StartDate\":\"\\\/Date(1513276200000)\\\/\",
\"EndDate\":\"\\\/Date(1513276200000)\\\/\",
\"Image\":\"System.Byte[]\",
\"EnteredUser\":\"0\",
\"EnteredDate\":\"\\\/Date(1513314680783)\\\/\",
\"_DefProduct\":null,
\"_ProductBatch\":null}
     */

    int ProductId;
    int BatchId;
    int CatalogueType;
    String StartDate;
    String EndDate;
    int EnteredUser;
    String EnteredDate;
    int ImageIndex;

    public PromotionList(int productId, int batchId, int catalogueType, String startDate, String endDate, int enteredUser, String enteredDate,int imageIndex) {
        ProductId = productId;
        BatchId = batchId;
        CatalogueType = catalogueType;
        StartDate = startDate;
        EndDate = endDate;
        EnteredUser = enteredUser;
        EnteredDate = enteredDate;
        ImageIndex = imageIndex;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public int getBatchId() {
        return BatchId;
    }

    public void setBatchId(int batchId) {
        BatchId = batchId;
    }

    public int getCatalogueType() {
        return CatalogueType;
    }

    public void setCatalogueType(int catalogueType) {
        CatalogueType = catalogueType;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public int getEnteredUser() {
        return EnteredUser;
    }

    public void setEnteredUser(int enteredUser) {
        EnteredUser = enteredUser;
    }

    public String getEnteredDate() {
        return EnteredDate;
    }

    public void setEnteredDate(String enteredDate) {
        EnteredDate = enteredDate;
    }

    public int getImageIndex() {
        return ImageIndex;
    }

    public void setImageIndex(int imageIndex) {
        ImageIndex = imageIndex;
    }

    public ContentValues getCVPromotionalData(){
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.COL_PROMOTION_ProductId,getProductId());
        cv.put(DbHelper.COL_PROMOTION_BatchId,getBatchId());
        cv.put(DbHelper.COL_PROMOTION_CatalogueType,getCatalogueType());
        cv.put(DbHelper.COL_PROMOTION_StartDate,getStartDate());
        cv.put(DbHelper.COL_PROMOTION_EndDate,getEndDate());
        cv.put(DbHelper.COL_PROMOTION_EnteredUser,getEnteredUser());
        cv.put(DbHelper.COL_PROMOTION_IMAGE_INDEX,getImageIndex());
        cv.put(DbHelper.COL_PROMOTION_EnteredDate,getEnteredDate());
        return cv;
    }

}
