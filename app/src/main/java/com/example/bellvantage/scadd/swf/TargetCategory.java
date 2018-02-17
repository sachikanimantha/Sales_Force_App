package com.example.bellvantage.scadd.swf;

import android.content.ContentValues;
import android.database.Cursor;

import java.io.Serializable;

import static com.example.bellvantage.scadd.DB.DbHelper.COL_TARGET_CATEGORY_enterdUser;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TARGET_CATEGORY_enteredDate;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TARGET_CATEGORY_targetCategoryId;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TARGET_CATEGORY_targetCategoryName;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TARGET_CATEGORY_unitOfMeasurement;

/**
 * Created by Sachika on 09/11/2017.
 */

public class TargetCategory implements Serializable {

    private String targetCategoryId;
    private String targetCategoryName;
    private String unitOfMeasurement;
    private String enteredDate;
    private String enterdUser;

    public TargetCategory() {
    }

    public TargetCategory(String targetCategoryId, String targetCategoryName, String unitOfMeasurement, String enteredDate, String enterdUser) {
       this.setTargetCategoryId(targetCategoryId);
       this.setTargetCategoryName(targetCategoryName);
       this.setUnitOfMeasurement(unitOfMeasurement);
       this.setEnteredDate(enteredDate);
       this.setEnterdUser(enterdUser);
    }

    public String getTargetCategoryId() {
        return targetCategoryId;
    }

    public void setTargetCategoryId(String targetCategoryId) {
        this.targetCategoryId = targetCategoryId;
    }

    public String getTargetCategoryName() {
        return targetCategoryName;
    }

    public void setTargetCategoryName(String targetCategoryName) {
        this.targetCategoryName = targetCategoryName;
    }

    public String getUnitOfMeasurement() {
        return unitOfMeasurement;
    }

    public void setUnitOfMeasurement(String unitOfMeasurement) {
        this.unitOfMeasurement = unitOfMeasurement;
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
        cv.put(COL_TARGET_CATEGORY_targetCategoryId,getTargetCategoryId());
        cv.put(COL_TARGET_CATEGORY_targetCategoryName,getTargetCategoryName());
        cv.put(COL_TARGET_CATEGORY_unitOfMeasurement,getUnitOfMeasurement());
        cv.put(COL_TARGET_CATEGORY_enteredDate,getEnteredDate());
        cv.put(COL_TARGET_CATEGORY_enterdUser,getEnterdUser());
        return cv;
    }



    public TargetCategory getDbInstance(Cursor cursor){
        TargetCategory targetCategory = new TargetCategory();

        targetCategory.setTargetCategoryId(cursor.getString(cursor.getColumnIndex(COL_TARGET_CATEGORY_targetCategoryId)));
        targetCategory.setTargetCategoryName(cursor.getString(cursor.getColumnIndex(COL_TARGET_CATEGORY_targetCategoryName)));
        targetCategory.setUnitOfMeasurement(cursor.getString(cursor.getColumnIndex(COL_TARGET_CATEGORY_unitOfMeasurement)));
        targetCategory.setEnteredDate(cursor.getString(cursor.getColumnIndex(COL_TARGET_CATEGORY_enteredDate)));
        targetCategory.setEnterdUser(cursor.getString(cursor.getColumnIndex(COL_TARGET_CATEGORY_enterdUser)));

        return targetCategory;
    }



}
