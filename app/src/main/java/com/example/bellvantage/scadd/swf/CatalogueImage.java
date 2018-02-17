package com.example.bellvantage.scadd.swf;

import android.content.ContentValues;

import com.example.bellvantage.scadd.DB.DbHelper;

/**
 * Created by Bellvantage on 28/12/2017.
 */

public class CatalogueImage {

    int productid;
    int batchid;
    byte[] img;
    int catalogue_type;

    public int getProductid() {
        return productid;
    }

    public void setProductid(int productid) {
        this.productid = productid;
    }

    public int getBatchid() {
        return batchid;
    }

    public void setBatchid(int batchid) {
        this.batchid = batchid;
    }

    public byte[] getImg() {
        return img;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public int getCatalogue_type() {
        return catalogue_type;
    }

    public void setCatalogue_type(int catalogue_type) {
        this.catalogue_type = catalogue_type;
    }

    public CatalogueImage(int productid, int batchid, byte[] img, int catalogue_type) {
        this.productid = productid;
        this.batchid = batchid;
        this.img = img;
        this.catalogue_type = catalogue_type;
    }

    public ContentValues getCVImagevalues(){
        ContentValues cv = new ContentValues();
        cv.put(DbHelper.COL_PROMOTION_IMAGE_productid,getProductid());
        cv.put(DbHelper.COL_PROMOTION_IMAGE_batchid,getBatchid());
        cv.put(DbHelper.COL_PROMOTION_IMAGE_img,getImg());
        cv.put(DbHelper.COL_PROMOTION_IMAGE_catalogue_type,getCatalogue_type());
        return cv;
    }
}
