package com.example.bellvantage.scadd.swf;

import android.content.ContentValues;

import com.example.bellvantage.scadd.DB.DbHelper;

/**
 * Created by Bellvantage on 04/08/2017.
 */

public class SalesRepInventoryDetails {

//        "ProductId":1,
//        "BatchID":1,
//        "DistributorId":1,
//        "SalesRepId":3,
//        "QuantityInHand":0,
//        "ReorderLevel":10,
//        "LastInventoryUpdate":"\/Date(1495650600000)\/"

    int ProductId ;
    int BatchID ;
    int DistributorId ;
    int SalesRepId ;
    int QuantityInHand;
    int ReorderLevel;
    String LastInventoryUpdate ;

    public SalesRepInventoryDetails(int productId, int batchID, int distributorId, int salesRepId,
                                    int quantityInHand, int reorderLevel, String lastInventoryUpdate) {

        this.setProductId(productId);
        this.setBatchID(batchID);
        this.setDistributorId(distributorId);
        this.setSalesRepId(salesRepId);
        this.setQuantityInHand(quantityInHand);
        this.setReorderLevel(reorderLevel);
        this.setLastInventoryUpdate(lastInventoryUpdate);

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

    public int getDistributorId() {
        return DistributorId;
    }

    public void setDistributorId(int distributorId) {
        DistributorId = distributorId;
    }

    public int getSalesRepId() {
        return SalesRepId;
    }

    public void setSalesRepId(int salesRepId) {
        SalesRepId = salesRepId;
    }

    public int getQuantityInHand() {
        return QuantityInHand;
    }

    public void setQuantityInHand(int quantityInHand) {
        QuantityInHand = quantityInHand;
    }

    public int getReorderLevel() {
        return ReorderLevel;
    }

    public void setReorderLevel(int reorderLevel) {
        ReorderLevel = reorderLevel;
    }

    public String getLastInventoryUpdate() {
        return LastInventoryUpdate;
    }

    public void setLastInventoryUpdate(String lastInventoryUpdate) {
        LastInventoryUpdate = lastInventoryUpdate;
    }

    public ContentValues getSRInventoryContents(){

        ContentValues cv = new ContentValues();
        cv.put(DbHelper.COL_SRI_PRODUCT_ID,getProductId());
        cv.put(DbHelper.COL_SRI_BATCH_ID,getBatchID());
        cv.put(DbHelper.COL_SRI_DISTRIBUTOR_ID,getDistributorId());
        cv.put(DbHelper.COL_SRI_SALESREP_ID,getSalesRepId());
        cv.put(DbHelper.COL_SRI_QTY_INHAND,getQuantityInHand());
        cv.put(DbHelper.COL_SRI_REORDER_LEVEL,getReorderLevel());
        cv.put(DbHelper.COL_SRI_LAST_INVENTORYUPDATE,getLastInventoryUpdate());


        return cv;
    }

}
