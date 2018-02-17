package com.example.bellvantage.scadd.swf;

import android.content.ContentValues;
import android.database.Cursor;

import java.io.Serializable;

import static com.example.bellvantage.scadd.DB.DbHelper.COL_IMAGE1;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_IMAGE2;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_IMAGE_ISSYNC;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_IMAGE_MERCHANT_ID;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_IMAGE_SEQUENCE_ID;

/**
 * Created by Sachika on 13/10/2017.
 */

public class MerchantImage implements Serializable {

    private String SequenceId;
    private int  MerchantId;
    private byte[] Image1;
    private byte[] Image2;
    private int IsSync;

    public MerchantImage() {
    }

    public MerchantImage(String sequenceId, int merchantId, byte[] image1, byte[] image2, int isSync) {
       this.setSequenceId(sequenceId);
       this.setMerchantId(merchantId);
       this.setImage1(image1);
       this.setImage2(image2);
       this.setIsSync(isSync);
    }

    public int getIsSync() {
        return IsSync;
    }

    public void setIsSync(int isSync) {
        IsSync = isSync;
    }

    public byte[] getImage1() {
        return Image1;
    }

    public void setImage1(byte[] image1) {
        Image1 = image1;
    }

    public byte[] getImage2() {
        return Image2;
    }

    public void setImage2(byte[] image2) {
        Image2 = image2;
    }

    public String getSequenceId() {
        return SequenceId;
    }

    public void setSequenceId(String sequenceId) {
        SequenceId = sequenceId;
    }

    public int getMerchantId() {
        return MerchantId;
    }

    public void setMerchantId(int merchantId) {
        MerchantId = merchantId;
    }

    public ContentValues getMerchantImageContentValues(){
        ContentValues cv =new ContentValues();
        cv.put(COL_IMAGE_SEQUENCE_ID,getSequenceId());
        cv.put(COL_IMAGE_MERCHANT_ID,getMerchantId());
        cv.put(COL_IMAGE1,getImage1());
        cv.put(COL_IMAGE2,getImage2());
        cv.put(COL_IMAGE_ISSYNC,getIsSync());
        return cv;

    }


    public static MerchantImage getDbInstance(Cursor cursor) {
        MerchantImage merchantImage = new MerchantImage();
        merchantImage.setSequenceId(cursor.getString(cursor.getColumnIndex(COL_IMAGE_SEQUENCE_ID)));
        merchantImage.setMerchantId(cursor.getInt(cursor.getColumnIndex(COL_IMAGE_MERCHANT_ID)));
        merchantImage.setImage1(cursor.getBlob(cursor.getColumnIndex(COL_IMAGE1)));
        merchantImage.setImage2(cursor.getBlob(cursor.getColumnIndex(COL_IMAGE2)));
        merchantImage.setIsSync(cursor.getInt(cursor.getColumnIndex(COL_IMAGE_ISSYNC)));

        return merchantImage;
    }


    /*
    try {
        cursor =  db.rawQuery(sql,null);
        if(cursor.moveToFirst()){
            do{
                Attendance attendance = Attendance.getDBInstance(cursor);
                attendances.add(attendance);
            }while(cursor.moveToNext());
        }
        return attendances;

    }catch (Exception e){
        throw  e;
    }finally {
        if(cursor != null){
            cursor.close();
        }
        db.close();
    }*/



}
