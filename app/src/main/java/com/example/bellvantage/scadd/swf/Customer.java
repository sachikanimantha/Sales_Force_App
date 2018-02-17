package com.example.bellvantage.scadd.swf;

/**
 * Created by Sachika on 5/29/2017.
 */

public class Customer {

        private String CustomerCode;
        private String ShopName;
        private String OutletCode;
        private String OutletSubCode;
        private String CustomerStatus;
        private int CustomerBalance;
        private double CreditLimit;
        private int PericeylCreditMaxAge;
        private int DCSLCreditMaxAge;
        private int GroupCode;
        private String Address1;
        private String Address2;
        private String SalesPersonCode;
        private Device DeviceConfig ;
        private AttendanceLocation ListAttendanceLocation ;
        private Attendance ListAttendance ;

        public String getCustomerCode() {
                return CustomerCode;
        }

        public void setCustomerCode(String customerCode) {
                CustomerCode = customerCode;
        }

        public String getShopName() {
                return ShopName;
        }

        public void setShopName(String shopName) {
                ShopName = shopName;
        }

        public String getOutletCode() {
                return OutletCode;
        }

        public void setOutletCode(String outletCode) {
                OutletCode = outletCode;
        }

        public String getOutletSubCode() {
                return OutletSubCode;
        }

        public void setOutletSubCode(String outletSubCode) {
                OutletSubCode = outletSubCode;
        }

        public String getCustomerStatus() {
                return CustomerStatus;
        }

        public void setCustomerStatus(String customerStatus) {
                CustomerStatus = customerStatus;
        }

        public int getCustomerBalance() {
                return CustomerBalance;
        }

        public void setCustomerBalance(int customerBalance) {
                CustomerBalance = customerBalance;
        }

        public double getCreditLimit() {
                return CreditLimit;
        }

        public void setCreditLimit(double creditLimit) {
                CreditLimit = creditLimit;
        }

        public int getPericeylCreditMaxAge() {
                return PericeylCreditMaxAge;
        }

        public void setPericeylCreditMaxAge(int periceylCreditMaxAge) {
                PericeylCreditMaxAge = periceylCreditMaxAge;
        }

        public int getDCSLCreditMaxAge() {
                return DCSLCreditMaxAge;
        }

        public void setDCSLCreditMaxAge(int DCSLCreditMaxAge) {
                this.DCSLCreditMaxAge = DCSLCreditMaxAge;
        }

        public int getGroupCode() {
                return GroupCode;
        }

        public void setGroupCode(int groupCode) {
                GroupCode = groupCode;
        }

        public String getAddress1() {
                return Address1;
        }

        public void setAddress1(String address1) {
                Address1 = address1;
        }

        public String getAddress2() {
                return Address2;
        }

        public void setAddress2(String address2) {
                Address2 = address2;
        }

        public String getSalesPersonCode() {
                return SalesPersonCode;
        }

        public void setSalesPersonCode(String salesPersonCode) {
                SalesPersonCode = salesPersonCode;
        }

        public Device getDeviceConfig() {
                return DeviceConfig;
        }

        public void setDeviceConfig(Device deviceConfig) {
                DeviceConfig = deviceConfig;
        }

        public AttendanceLocation getListAttendanceLocation() {
                return ListAttendanceLocation;
        }

        public void setListAttendanceLocation(AttendanceLocation listAttendanceLocation) {
                ListAttendanceLocation = listAttendanceLocation;
        }

        public Attendance getListAttendance() {
                return ListAttendance;
        }

        public void setListAttendance(Attendance listAttendance) {
                ListAttendance = listAttendance;
        }
}
