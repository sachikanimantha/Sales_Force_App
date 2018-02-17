package com.example.bellvantage.scadd.swf;

/**
 * Created by Bellvantage on 10/10/2017.
 */

public class ProductListItemPosition {

    int position;
    int product_id;
    int batch_id;

    public ProductListItemPosition(int position, int product_id, int batch_id) {
        this.position = position;
        this.product_id = product_id;
        this.batch_id = batch_id;
    }

    public ProductListItemPosition(int position, int product_id) {
        this.position = position;
        this.product_id = product_id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getBatch_id() {
        return batch_id;
    }

    public void setBatch_id(int batch_id) {
        this.batch_id = batch_id;
    }



}
