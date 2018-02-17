package com.example.bellvantage.scadd.MC;

import java.io.Serializable;

/**
 * Created by Bellvantage on 31/05/2017.
 */

public class InvoiceLineItem implements Serializable{
    private  int productID;
    private  String itemName;
    private  int qty;
    private  int freeQuantity;
    private  double price;
    private  double lineTotal;
    private  int freeQu;

    public InvoiceLineItem(int productID, String itemName, int qty,int freeQu, double price, double lineTotal) {

        this.setItemName(itemName);
        this.setProductID(productID);
        this.setQty(qty);
        this.setPrice(price);
        this.setLineTotal(lineTotal);
        this.setFreeQuantity(freeQu);
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
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

    public int getFreeQuantity() {
        return freeQu;
    }

    public void setFreeQuantity(int freeQu) {
        this.freeQu = freeQu;
    }

}
