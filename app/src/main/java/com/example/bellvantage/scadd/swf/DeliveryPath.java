package com.example.bellvantage.scadd.swf;

import android.content.ContentValues;

import java.io.Serializable;

/**
 * Created by Bellvantage on 29/06/2017.
 */

public class DeliveryPath implements Serializable {

    private int DeliveryPathId;
    private String PathName;


    /*
http://113.59.211.246:9021/Service1.svc/GetDefSalesPathListBySalesId?salesRepID=3

    {"SalesRepID":42,
"PathID":1,
"AssignDate":"\/Date(1497983400000)\/",
"IsExpired":0,
"EnteredUser":"1",
"EnteredDate":"\/Date(1498021938913)\/",
"UpdatedUser":"3",
"UpdatedDate":"\/Date(1498029165423)\/",
"_DefSalesRep":
	{"SalesRepId":42,"Distributord":1,"SalesRepName":"HARSHA CHAMARA ","EpfNumber":990789,
	"IsActive":1,"EnteredUser":"","EnteredDate":"\/Date(1507446550986)\/",
	"PresentAddress":"No:43,Mirisswaththa, Piliyandala ","PermanentAddress":"No 2, salmal rd, gampaha",
	"ContactNo":"0912234434","UpdatedUser":"","UpdatedDate":"\/Date(1507202500823)\/","ReferenceID":"241",
	"NextSalesOrderNo":21132,"NextInvoiceNo":21130,"SalesRepType":"R","NextCreditNoteNo":20038,"_DefDistributor":null,
	"_ListVehicleInventory":null,"_ListDeviceInfo":null,"_ListDefDefinePath":null,"_ListActualPath":null,"_ListAttendance":null,
	"_ListSalesRepInventory":null,"_ListReturnInventory":null,"_ListSalesInvoice":null,"_ListSalesOrder":null,"_ListDefMerchant":null,
	"_ListDeliveryLocations":null,"_DefLogin":null},
"_DeliveryPath":{
	"DeliveryPathId":1,
	"PathName":"JATHIKAPOLA",
	"CreateDate":"\/Date(1496169000000)\/",
	"CreatedUser":"ADMIN",
	"_DeliveryLocations":null,
	"ListDdfDefinePath":null}
},
     */


    public DeliveryPath(int deliveryPathId, String pathName) {
        this.setDeliveryPathId(deliveryPathId);
        this.setPathName(pathName);
    }

    public int getDeliveryPathId() {
        return DeliveryPathId;
    }

    public void setDeliveryPathId(int deliveryPathId) {
        DeliveryPathId = deliveryPathId;
    }

    public String getPathName() {
        return PathName;
    }

    public void setPathName(String pathName) {
        PathName = pathName;
    }


    public ContentValues getDeliveryPathContentValues(){
        ContentValues cv = new ContentValues();
        cv.put("DeliveryPathId",DeliveryPathId);
        cv.put("PathName",PathName);
        return  cv;
    }
}
