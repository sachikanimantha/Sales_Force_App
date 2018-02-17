package com.example.bellvantage.scadd.Utils;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Bellvantage on 12/09/2017.
 */

public class SharedPreferenceStore extends Activity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public void savingStringSP(String SP_KEY,String VALUE_KEY,String value1){

        sharedPreferences = getSharedPreferences(SP_KEY,MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(VALUE_KEY,value1);
        editor.commit();
    }

    public void savingIntegerSP(String SP_KEY,String VALUE_KEY,int value2){

        sharedPreferences = getSharedPreferences(SP_KEY,MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putInt(VALUE_KEY,value2);
        editor.commit();
    }

    public void savingFloatSP(String SP_KEY,String VALUE_KEY,float value3){

        sharedPreferences = getSharedPreferences(SP_KEY,MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putFloat(VALUE_KEY,value3);
        editor.commit();
    }

    public String gettingStringSP(String SP_KEY,String VALUE_KEY){

        //sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences = getSharedPreferences(SP_KEY,MODE_PRIVATE);
        String string1 = sharedPreferences.getString(VALUE_KEY,"");
        return string1;
    }

    public int gettingIntegerSP(String SP_KEY,String VALUE_KEY){

        //sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences = getSharedPreferences(SP_KEY,MODE_PRIVATE);
        int int1 = sharedPreferences.getInt(VALUE_KEY,0);
        return int1;
    }

}
