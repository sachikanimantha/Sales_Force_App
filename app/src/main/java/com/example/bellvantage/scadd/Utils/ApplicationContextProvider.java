package com.example.bellvantage.scadd.Utils;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.v7.app.AlertDialog;

/**
 * Created by Sachika on 12/06/2017.
 */

public class ApplicationContextProvider extends Application {

    private static Context sContext;

    // AlertDialog
    AlertDialog custuDialog;
    AlertDialog.Builder mBuilder;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        MultiDex.install(this);

    }

    public static Context getCurrentContext() {
        return sContext;
    }



}
