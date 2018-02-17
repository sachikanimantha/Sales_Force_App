package com.example.bellvantage.scadd.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

/**
 * Created by Sachika on 06/11/2017.
 */

public class ImageBase64 {
    final String TAG = this.getClass().getSimpleName();

    public ImageBase64() {
    }

    public static String encode(Bitmap bitmap) {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        int quality = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bao);
        if(bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }

        byte[] ba = bao.toByteArray();
        String encodedImage = Base64.encodeToString(ba, 0);
        return encodedImage;
    }

    public static Bitmap decode(String encodedImage) {
        String pureBase64Encoded = encodedImage.substring(encodedImage.indexOf(",") + 1);
        byte[] decodedString = Base64.decode(pureBase64Encoded, 8);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return bitmap;
    }
}