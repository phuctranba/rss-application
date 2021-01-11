package com.e.rssapplication.DataBase;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MySharedPreferences {
    private static String PREF_FIRST_OPEN = "pref_first_open";
    private static String PREF_DEFAULT_WEBSITE = "pref_default_website";

    public static void setPrefFirstOpen(Context context, boolean isEnabled){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PREF_FIRST_OPEN,isEnabled);
        editor.apply();
    }

    public static boolean getPrefFirstOpen(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return  preferences.getBoolean(PREF_FIRST_OPEN,true);
    }

    public static void setPrefDefaultWebsite(Context context, String value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_DEFAULT_WEBSITE,value);
        editor.apply();
    }

    public static String getPrefDefaultWebsite(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return  preferences.getString(PREF_DEFAULT_WEBSITE,EnumWebSite.VNEXPRESS.name());
    }
}
