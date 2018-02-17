package com.example.bellvantage.scadd.swf;

import android.content.ContentValues;

/**
 * Created by Bellvantage on 02/08/2017.
 */

public class CategoryDetails {

    /*
    "CategoryId":1,
    "CategoryName":"Red Bull",
    "CategoryShortName":"RBL",
    "IsActive":1,
    "EnteredDate":"\/Date(1496082600000)\/",
    "EnteredUser":"ADMIN",
    "_defProduct":null
     */

    int Categoryid;
    String CategoryName;
    String CategoryShortName;
    int isactive;
    String enterddate;
    String entereduser;

    public CategoryDetails(int categoryid, String categoryName, String categoryShortName,
                           int isactive, String enterddate, String entereduser) {

        Categoryid = categoryid;
        CategoryName = categoryName;
        CategoryShortName = categoryShortName;
        this.isactive = isactive;
        this.enterddate = enterddate;
        this.entereduser = entereduser;
    }

    public int getCategoryid() {
        return Categoryid;
    }

    public void setCategoryid(int categoryid) {
        Categoryid = categoryid;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getCategoryShortName() {
        return CategoryShortName;
    }

    public void setCategoryShortName(String categoryShortName) {
        CategoryShortName = categoryShortName;
    }

    public int getIsactive() {
        return isactive;
    }

    public void setIsactive(int isactive) {
        this.isactive = isactive;
    }

    public String getEnterddate() {
        return enterddate;
    }

    public void setEnterddate(String enterddate) {
        this.enterddate = enterddate;
    }

    public String getEntereduser() {
        return entereduser;
    }

    public void setEntereduser(String entereduser) {
        this.entereduser = entereduser;
    }

    public ContentValues getCategoryContents(){

        ContentValues cv = new ContentValues();
        cv.put("CATEGORY_ID",Categoryid);
        cv.put("CATEGORY_NAME",CategoryName);
        cv.put("CATEGORY_SNAME",CategoryShortName);
        cv.put("CATEGORY_ISACTIVE",isactive);
        cv.put("CATEGORY_ENTERED_USER",enterddate);
        cv.put("CATEGORY_ENTERED_DATE",entereduser);

        return cv;
    }

}
