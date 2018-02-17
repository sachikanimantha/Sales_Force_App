package com.example.bellvantage.scadd.Services;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

/**
 * Created by Sachika on 6/15/2017.
 */

public class GPS_Tracker extends Service {

    LocationListener listner;
    LocationManager manager;
    int gpsStatus = 1 ;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        listner = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Intent intent = new Intent("location_update");
                intent.putExtra("lat",location.getLatitude());
                intent.putExtra("lon",location.getLongitude());
                intent.putExtra("accuracy",location.getAccuracy());
                intent.putExtra("gpsStatus",gpsStatus);
                sendBroadcast(intent);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                gpsStatus = 0;

                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            }
        };
        manager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //do
            return;
        }
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 30000, 0, listner);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (manager != null){
            manager.removeUpdates(listner);
        }
    }

}
