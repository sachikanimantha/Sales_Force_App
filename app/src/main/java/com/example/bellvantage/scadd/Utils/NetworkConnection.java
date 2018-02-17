package com.example.bellvantage.scadd.Utils;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Sachika on 12/06/2017.
 */

public class NetworkConnection {


    public static boolean checkNetworkConnection(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public static boolean checkGPSConnection(Context context){
        LocationManager manager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE );
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

       return  statusOfGPS;
    }



}
