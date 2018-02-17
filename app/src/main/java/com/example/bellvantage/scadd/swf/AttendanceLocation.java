package com.example.bellvantage.scadd.swf;

import java.util.Date;

/**
 * Created by Sachika on 5/29/2017.
 */

public class AttendanceLocation {

    private String UserId;
    private String DateAlias;
    private int SequenceId;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getDateAlias() {
        return DateAlias;
    }

    public void setDateAlias(String dateAlias) {
        DateAlias = dateAlias;
    }

    public int getSequenceId() {
        return SequenceId;
    }

    public void setSequenceId(int sequenceId) {
        SequenceId = sequenceId;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getCreatedUser() {
        return CreatedUser;
    }

    public void setCreatedUser(String createdUser) {
        CreatedUser = createdUser;
    }

    public Date getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(Date createdDate) {
        CreatedDate = createdDate;
    }

    private String Longitude;
    private String Latitude;
    private String CreatedUser;
    private Date CreatedDate;
    
}
