package com.example.bellvantage.scadd.swf;

import android.content.ContentValues;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.example.bellvantage.scadd.DB.DbHelper.COL_MERCHANT_CLASS_ClassDescription;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_MERCHANT_CLASS_ClassID;

/**
 * Created by Sachika on 12/09/2017.
 */

public class MerchantClass implements Serializable {


    /**
     * ClassID : 1
     * ClassDescription : A
     */

    private int ClassID;
    private String ClassDescription;


    public MerchantClass(int classID, String classDescription) {
       this.setClassID(classID);
       this.setClassDescription(classDescription);

    }

    public static MerchantClass objectFromData(String str) {

        return new Gson().fromJson(str, MerchantClass.class);
    }

    public static MerchantClass objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), MerchantClass.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<MerchantClass> arrayMerchantClassFromData(String str) {

        Type listType = new TypeToken<ArrayList<MerchantClass>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<MerchantClass> arrayMerchantClassFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<MerchantClass>>() {
            }.getType();

            return new Gson().fromJson(jsonObject.getString(str), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList();


    }

    public int getClassID() {
        return ClassID;
    }

    public void setClassID(int ClassID) {
        this.ClassID = ClassID;
    }

    public String getClassDescription() {
        return ClassDescription;
    }

    public void setClassDescription(String ClassDescription) {
        this.ClassDescription = ClassDescription;
    }

    public ContentValues getMerchantClassCV(){
        ContentValues cv = new ContentValues();
        cv.put(COL_MERCHANT_CLASS_ClassID,getClassID());
        cv.put(COL_MERCHANT_CLASS_ClassDescription,getClassDescription());
        return cv;
    }

}
