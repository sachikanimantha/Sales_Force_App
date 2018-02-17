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

import static com.example.bellvantage.scadd.DB.DbHelper.COL_MERCHANT_TYPE_TypeCode;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_MERCHANT_TYPE_TypeDescription;

/**
 * Created by Bellvantage on 12/09/2017.
 */

public class MerchantType implements Serializable {

    /**
     * TypeCode : GRO
     * TypeDescription : Grocery
     */

    private String TypeCode;
    private String TypeDescription;

    public MerchantType(String typeCode, String typeDescription) {
        this.setTypeCode(typeCode);
        this.setTypeDescription(typeDescription);
    }

    public static MerchantType objectFromData(String str) {

        return new Gson().fromJson(str, MerchantType.class);
    }

    public static MerchantType objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), MerchantType.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<MerchantType> arrayMerchantTypeFromData(String str) {

        Type listType = new TypeToken<ArrayList<MerchantType>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<MerchantType> arrayMerchantTypeFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<MerchantType>>() {
            }.getType();

            return new Gson().fromJson(jsonObject.getString(str), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList();


    }

    public String getTypeCode() {
        return TypeCode;
    }

    public void setTypeCode(String TypeCode) {
        this.TypeCode = TypeCode;
    }

    public String getTypeDescription() {
        return TypeDescription;
    }

    public void setTypeDescription(String TypeDescription) {
        this.TypeDescription = TypeDescription;
    }

    public ContentValues getMerchantTypeCV(){
        ContentValues cv = new ContentValues();
        cv.put(COL_MERCHANT_TYPE_TypeCode,getTypeCode());
        cv.put(COL_MERCHANT_TYPE_TypeDescription,getTypeDescription());
        return  cv;
    }
}
