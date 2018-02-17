package com.example.bellvantage.scadd.swf;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bellvantage on 08/12/2017.
 */

public class ReturnTest implements Serializable {

    private ArrayList<DataBean> Data;

    public static ReturnTest objectFromData(String str) {

        return new Gson().fromJson(str, ReturnTest.class);
    }

    public static ReturnTest objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), ReturnTest.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<ReturnTest> arrayReturnTestFromData(String str) {

        Type listType = new TypeToken<ArrayList<ReturnTest>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<ReturnTest> arrayReturnTestFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<ReturnTest>>() {
            }.getType();

            return new Gson().fromJson(jsonObject.getString(str), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList();


    }

    public ArrayList<DataBean> getData() {
        return Data;
    }

    public void setData(ArrayList<DataBean> Data) {
        this.Data = Data;
    }
}
