package com.example.bellvantage.scadd.Adapters;

/**
 * Created by Bellvantage on 16/10/2017.
 */

public class Img {
    private  byte[] image;

    public Img(byte[] image) {
        this.setImage(image);
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
