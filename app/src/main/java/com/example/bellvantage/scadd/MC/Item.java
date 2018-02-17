package com.example.bellvantage.scadd.MC;

import java.io.Serializable;

/**
 * Created by Bellvantage on 25/05/2017.
 */

public class Item implements Serializable{

    private  int productID;
    private  String itemName;
    private  int qty;
    private  double price;
    private  double lineTotal;

    public Item(int productID,String itemName, int qty, double price, double lineTotal) {
        this.setItemName(itemName);
        this.setproductID(productID);
        this.setQty(qty);
        this.setPrice(price);
        this.setLineTotal(lineTotal);
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getproductID() {
        return productID;
    }

    public void setproductID(int productID) {
        this.productID = productID;
    }


    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getLineTotal() {
        return lineTotal;
    }

    public void setLineTotal(double lineTotal) {
        this.lineTotal = lineTotal;
    }
}
