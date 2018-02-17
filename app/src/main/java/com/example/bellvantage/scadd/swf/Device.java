package com.example.bellvantage.scadd.swf;

/**
 * Created by Sachika on 5/29/2017.
 */

public class Device {
    private String UserId;
    private int DeviceId;
    private String ServiceURL;
    private String CreatedUser;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public int getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(int deviceId) {
        DeviceId = deviceId;
    }

    public String getServiceURL() {
        return ServiceURL;
    }

    public void setServiceURL(String serviceURL) {
        ServiceURL = serviceURL;
    }

    public String getCreatedUser() {
        return CreatedUser;
    }

    public void setCreatedUser(String createdUser) {
        CreatedUser = createdUser;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public boolean isSync() {
        return IsSync;
    }

    public void setSync(boolean sync) {
        IsSync = sync;
    }

    public String getSyncDate() {
        return SyncDate;
    }

    public void setSyncDate(String syncDate) {
        SyncDate = syncDate;
    }

    private String CreatedDate;
    private boolean IsSync;
    private String SyncDate;
}
