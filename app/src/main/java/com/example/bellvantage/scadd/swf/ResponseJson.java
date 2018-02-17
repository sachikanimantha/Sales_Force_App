package com.example.bellvantage.scadd.swf;

/**
 * Created by chathura on 11/16/16.
 */
public class ResponseJson {
    String Data;
    int ID;

    public ResponseJson(String data, int ID) {
        Data = data;
        this.ID = ID;
    }


    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
