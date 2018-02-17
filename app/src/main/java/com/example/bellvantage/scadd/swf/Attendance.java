package com.example.bellvantage.scadd.swf;

/**
 * Created by Sachika on 5/29/2017.
 */

public class Attendance {
    private String UserId;
    private String AttendanceDate;
    private String AttendaceTime;
    private String Longitude;
    private String Latitude;
    private String AttendanceType;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getAttendanceDate() {
        return AttendanceDate;
    }

    public void setAttendanceDate(String attendanceDate) {
        AttendanceDate = attendanceDate;
    }

    public String getAttendaceTime() {
        return AttendaceTime;
    }

    public void setAttendaceTime(String attendaceTime) {
        AttendaceTime = attendaceTime;
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

    public String getAttendanceType() {
        return AttendanceType;
    }

    public void setAttendanceType(String attendanceType) {
        AttendanceType = attendanceType;
    }
}
