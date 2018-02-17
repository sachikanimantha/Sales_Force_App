package com.example.bellvantage.scadd.MC;

/**
 * Created by Bellvantage on 16/05/2017.
 */

public class MyMenu {
    private  String menuName;
    private int menuImage;

    public MyMenu(String menuName, int menuImage) {
        this.setMenuName(menuName);
        this.setMunuImage(menuImage);
    }

    public String getMenuName() {

        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getMenuImage() {
        return menuImage;
    }

    public void setMunuImage(int menuImage) {
        this.menuImage = menuImage;
    }
}
