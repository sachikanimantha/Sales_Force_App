package com.example.bellvantage.scadd.swf;

/**
 * Created by Bellvantage on 31/05/2017.
 */

public class UserResponse {

        public LoginUser Data;
        public int ID ;

    public LoginUser getData() {
        return Data;
    }

    public void setData(LoginUser data) {
        Data = data;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

}
