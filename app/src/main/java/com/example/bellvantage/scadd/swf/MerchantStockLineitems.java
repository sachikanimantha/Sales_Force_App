package com.example.bellvantage.scadd.swf;

import android.content.ContentValues;

import static com.example.bellvantage.scadd.DB.DbHelper.COL_MS_LINEITEM_is_sync;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_MS_LINEITEM_merchant_stock_id;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_MS_LINEITEM_product_id;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_MS_LINEITEM_product_name;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_MS_LINEITEM_quantity;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_MS_LINEITEM_sync_date;

/**
 * Created by Bellvantage on 18/10/2017.
 */

public class MerchantStockLineitems  {


    int MerchentStockId;
    int ProductID;
    String product_name ;
    int Quantity;
    int IsSync;
    String SyncDate;

    public MerchantStockLineitems(int merchentStockId, int productID, String productname, int quantity, int isSync, String syncDate) {
        MerchentStockId = merchentStockId;
        ProductID = productID;
        product_name = productname;
        Quantity = quantity;
        IsSync = isSync;
        SyncDate = syncDate;
    }

//    public MerchantStockLineitems(int merchentStockId, int productID, int quantity, int isSync, String syncDate) {
//        MerchentStockId = merchentStockId;
//        ProductID = productID;
//        Quantity = quantity;
//        IsSync = isSync;
//        SyncDate = syncDate;
//    }

    public int getMerchentStockId() {
        return MerchentStockId;
    }

    public void setMerchentStockId(int merchentStockId) {
        MerchentStockId = merchentStockId;
    }

    public int getProductID() {
        return ProductID;
    }

    public void setProductID(int productID) {
        ProductID = productID;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
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

    public ContentValues setCVvalues_merchantStocklineitems(){

        ContentValues cv = new ContentValues();
        cv.put(COL_MS_LINEITEM_merchant_stock_id,getMerchentStockId());
        cv.put(COL_MS_LINEITEM_product_id,getProductID());
        cv.put(COL_MS_LINEITEM_product_name,getProduct_name());
        cv.put(COL_MS_LINEITEM_quantity,getQuantity());
        cv.put(COL_MS_LINEITEM_is_sync,getIsSync());
        cv.put(COL_MS_LINEITEM_sync_date,getSyncDate());

        return cv;
    }

}
