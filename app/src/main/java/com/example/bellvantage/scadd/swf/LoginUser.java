package com.example.bellvantage.scadd.swf;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Sachika on 31/05/2017.
 */

public class LoginUser {

    public String Address;
    public String ContactNumber;
    public String Name;
    public String UserType;
    public String SyncDate;
    public int IsSync;
    public String ServiceUrl;
    public int UserTypeId;
    public String UserPassword;
    public String UserName;
    public String EpfNumber;


    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }


    public String getUserType() {
        return UserType;
    }

    public void setUserType (String userType) {
        UserType  = userType;
    }

    public String getSyncDate() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

    public void setSyncDate(String syncDate) {

        SyncDate = syncDate;
    }

    public int getIsSync() {
        return IsSync;
    }

    public void setIsSync(int isSync) {
        IsSync = isSync;
    }

    public String getServiceUrl() {
        return ServiceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        ServiceUrl = serviceUrl;
    }

    public int getUserTypeId() {
        return UserTypeId;
    }

    public void setUserTypeId(int userTypeId) {
        UserTypeId = userTypeId;
    }

    public String getUserPassword() {
        return UserPassword;
    }

    public void setUserPassword(String userPassword) {
        UserPassword = userPassword;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getEpfNumber() {
        return EpfNumber;
    }

    public void setEpfNumber(String epfNumber) {
        EpfNumber = epfNumber;
    }
}
