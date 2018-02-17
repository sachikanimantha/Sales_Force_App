package com.example.bellvantage.scadd.swf;

import android.content.ContentValues;
import android.database.Cursor;

import java.io.Serializable;

import static com.example.bellvantage.scadd.DB.DbHelper.COL_TARGET_CATEGORY_LINE_ITEM_enterdUser;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TARGET_CATEGORY_LINE_ITEM_enteredDate;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TARGET_CATEGORY_LINE_ITEM_productId;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TARGET_CATEGORY_LINE_ITEM_targetCategoryId;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TARGET_CATEGORY_LINE_ITEM_unitPerQuantity;

/**
 * Created by Bellvantage on 09/11/2017.
 */

public class TargetCategoryLineItem implements Serializable {

    private String targetCategoryId;
    private int productId = 0;
    private int unitPerQuantity = 0;
    private String enteredDate;
    private String enterdUser;


    public TargetCategoryLineItem() {
    }

    public TargetCategoryLineItem(String targetCategoryId, int productId, int unitPerQuantity, String enteredDate, String enterdUser) {
       this.setTargetCategoryId(targetCategoryId);
        this.setProductId(productId);
        this.setUnitPerQuantity(unitPerQuantity);
        this.setEnterdUser(enterdUser);
        this.setEnteredDate(enteredDate);
    }

    public String getTargetCategoryId() {
        return targetCategoryId;
    }

    public void setTargetCategoryId(String targetCategoryId) {
        this.targetCategoryId = targetCategoryId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getUnitPerQuantity() {
        return unitPerQuantity;
    }

    public void setUnitPerQuantity(int unitPerQuantity) {
        this.unitPerQuantity = unitPerQuantity;
    }

    public String getEnteredDate() {
        return enteredDate;
    }

    public void setEnteredDate(String enteredDate) {
        this.enteredDate = enteredDate;
    }

    public String getEnterdUser() {
        return enterdUser;
    }

    public void setEnterdUser(String enterdUser) {
        this.enterdUser = enterdUser;
    }

    public ContentValues getContentValues(){
        ContentValues cv = new ContentValues();
        cv.put(COL_TARGET_CATEGORY_LINE_ITEM_targetCategoryId,getTargetCategoryId());
        cv.put(COL_TARGET_CATEGORY_LINE_ITEM_productId,getProductId());
        cv.put(COL_TARGET_CATEGORY_LINE_ITEM_unitPerQuantity,getUnitPerQuantity());
        cv.put(COL_TARGET_CATEGORY_LINE_ITEM_enteredDate,getEnteredDate());
        cv.put(COL_TARGET_CATEGORY_LINE_ITEM_enterdUser,getEnterdUser());
        return cv;
    }


    public TargetCategoryLineItem getDbInstance(Cursor cursor){
        TargetCategoryLineItem targetCategoryLineItem = new TargetCategoryLineItem();
        targetCategoryLineItem.setTargetCategoryId(cursor.getString(cursor.getColumnIndex(COL_TARGET_CATEGORY_LINE_ITEM_targetCategoryId)));
        targetCategoryLineItem.setProductId(cursor.getInt(cursor.getColumnIndex(COL_TARGET_CATEGORY_LINE_ITEM_productId)));
        targetCategoryLineItem.setUnitPerQuantity(cursor.getInt(cursor.getColumnIndex(COL_TARGET_CATEGORY_LINE_ITEM_unitPerQuantity)));
        targetCategoryLineItem.setEnteredDate(cursor.getString(cursor.getColumnIndex(COL_TARGET_CATEGORY_LINE_ITEM_enteredDate)));
        targetCategoryLineItem.setEnterdUser(cursor.getString(cursor.getColumnIndex(COL_TARGET_CATEGORY_LINE_ITEM_enterdUser)));
        return targetCategoryLineItem;
    }


}
