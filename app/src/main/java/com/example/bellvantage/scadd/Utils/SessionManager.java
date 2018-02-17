package com.example.bellvantage.scadd.Utils;

import android.content.SharedPreferences;

/**
 * Created by Sachika on 31/05/2017.
 */

public class SessionManager {
    public static SharedPreferences pref;
    public static SharedPreferences.Editor editor;


    public static void Logut(){
        editor = pref.edit();
        editor.clear();
        editor.commit();
    }
}
