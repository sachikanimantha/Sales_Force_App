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

/**
 * Created by Bellvantage on 11/07/2017.
 */

public class DeliveryLocations implements Serializable {


    /**
     * MerchantID : 1
     * DeliveryPathID : 1
     * SequenceID : 1
     * _ListDeliverPath : {"DeliveryPathId":1,"PathName":"JATHIKAPOLA","CreateDate":"Date(1496169000000)","CreatedUser":"ADMIN","_DeliveryLocations":null,"ListDdfDefinePath":null}
     * _DefMerchant : {"MerchantId":1,"MerchantName":"A COLOMBO  PHARMACY","ContactNo1":"112436447","ContactNo2":"765555555","Longitude":"79.8666","Latitude":"6.9205","IsActive":1,"EnteredDate":"Date(1499759160950)","EnteredUser":"","DiscountRate":12,"IsSync":1,"SyncDate":"Date(1497933251000)","BuildingNo":"123 ug a3-97","Address1":"PEOPLES PARK","Address2":"petta VIP","City":"Colombo 11","ContactPerson":"YOGA","AreaCode":1,"MerchantType":"PRQ","MerchantClass":"A","DistrictCode":1,"UpdatedUser":"ADMIN","UpdatedDate":"Date(1497933230443)","ReferenceID":"A000009","IsVat":1,"VatNo":"112233","SyncId":"","_ListMerchantPayment":null,"_ReturnInventory":null,"_DeliveryLocations":null,"_ListReturnInventory":null,"_ListSalesInvoice":null,"_ListSalesOrder":null}
     */

    private int MerchantID;
    private int DeliveryPathID;
    private int SequenceID;
    private ListDeliverPathBean _ListDeliverPath;
    private DefMerchantBean _DefMerchant;

    public DeliveryLocations(int merchantID, int deliveryPathID) {
        this.setMerchantID(merchantID);
        this.setDeliveryPathID(deliveryPathID);
    }

    public static DeliveryLocations objectFromData(String str) {

        return new Gson().fromJson(str, DeliveryLocations.class);
    }

    public static DeliveryLocations objectFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);

            return new Gson().fromJson(jsonObject.getString(str), DeliveryLocations.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static List<DeliveryLocations> arrayDeliveryLocationsFromData(String str) {

        Type listType = new TypeToken<ArrayList<DeliveryLocations>>() {
        }.getType();

        return new Gson().fromJson(str, listType);
    }

    public static List<DeliveryLocations> arrayDeliveryLocationsFromData(String str, String key) {

        try {
            JSONObject jsonObject = new JSONObject(str);
            Type listType = new TypeToken<ArrayList<DeliveryLocations>>() {
            }.getType();

            return new Gson().fromJson(jsonObject.getString(str), listType);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new ArrayList();


    }

    public int getMerchantID() {
        return MerchantID;
    }

    public void setMerchantID(int MerchantID) {
        this.MerchantID = MerchantID;
    }

    public int getDeliveryPathID() {
        return DeliveryPathID;
    }

    public void setDeliveryPathID(int DeliveryPathID) {
        this.DeliveryPathID = DeliveryPathID;
    }

    public int getSequenceID() {
        return SequenceID;
    }

    public void setSequenceID(int SequenceID) {
        this.SequenceID = SequenceID;
    }

    public ListDeliverPathBean get_ListDeliverPath() {
        return _ListDeliverPath;
    }

    public void set_ListDeliverPath(ListDeliverPathBean _ListDeliverPath) {
        this._ListDeliverPath = _ListDeliverPath;
    }

    public DefMerchantBean get_DefMerchant() {
        return _DefMerchant;
    }

    public void set_DefMerchant(DefMerchantBean _DefMerchant) {
        this._DefMerchant = _DefMerchant;
    }

    public ContentValues getDeliveryLocationsContentValues(){
        ContentValues cv = new ContentValues();
        cv.put("MerchantID",getMerchantID());
        cv.put("DeliveryPathID",getDeliveryPathID());
        return  cv;
    }
}
