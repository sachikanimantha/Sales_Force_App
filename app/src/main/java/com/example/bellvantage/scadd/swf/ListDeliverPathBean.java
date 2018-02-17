package com.example.bellvantage.scadd.swf;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bellvantage on 11/07/2017.
 */

class ListDeliverPathBean {
    /**
     * DeliveryPathId : 1
     * PathName : JATHIKAPOLA
     * CreateDate : Date(1496169000000)
     * CreatedUser : ADMIN
     * _DeliveryLocations : null
     * ListDdfDefinePath : null
     */

    private int DeliveryPathId;
    private String PathName;
    private String CreateDate;
    private String CreatedUser;
    private Object _DeliveryLocations;
    private Object ListDdfDefinePath;

    public static ListDeliverPathBean objectFromData(String str) {

        return new Gson().fromJson(str, ListDeliverPathBean.class);
    }

    public static ListDeliverPathBean objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), ListDeliverPathBean.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<ListDeliverPathBean> arrayListDeliverPathBeanFromData(String str) {

        Type listType = new TypeToken<ArrayList<ListDeliverPathBean>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<ListDeliverPathBean> arrayListDeliverPathBeanFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<ListDeliverPathBean>>() {
            }.getType();

            return new Gson().fromJson(jsonObject.getString(str), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList();


    }

    public int getDeliveryPathId() {
        return DeliveryPathId;
    }

    public void setDeliveryPathId(int DeliveryPathId) {
        this.DeliveryPathId = DeliveryPathId;
    }

    public String getPathName() {
        return PathName;
    }

    public void setPathName(String PathName) {
        this.PathName = PathName;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String CreateDate) {
        this.CreateDate = CreateDate;
    }

    public String getCreatedUser() {
        return CreatedUser;
    }

    public void setCreatedUser(String CreatedUser) {
        this.CreatedUser = CreatedUser;
    }

    public Object get_DeliveryLocations() {
        return _DeliveryLocations;
    }

    public void set_DeliveryLocations(Object _DeliveryLocations) {
        this._DeliveryLocations = _DeliveryLocations;
    }

    public Object getListDdfDefinePath() {
        return ListDdfDefinePath;
    }

    public void setListDdfDefinePath(Object ListDdfDefinePath) {
        this.ListDdfDefinePath = ListDdfDefinePath;
    }
}
