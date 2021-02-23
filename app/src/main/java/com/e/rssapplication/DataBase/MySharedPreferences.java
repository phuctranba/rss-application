package com.e.rssapplication.DataBase;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


//Lớp này dùng để thao tác với SharedPreferences
public class MySharedPreferences {

//    Giá trị ghi nhớ lần đầu đăng nhâp
    private static String PREF_FIRST_OPEN = "pref_first_open";

//    Giá trị ghi nhớ web nguồn tin lần cuối được mở
    private static String PREF_DEFAULT_WEBSITE = "pref_default_website";


//    Đặt giá trị cho biến ghi nhớ lần đầu mở app
    public static void setPrefFirstOpen(Context context, boolean isEnabled){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PREF_FIRST_OPEN,isEnabled);
        editor.apply();
    }

//    Lấy giá trị biến ghi nhớ lần đầu mở app
    public static boolean getPrefFirstOpen(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return  preferences.getBoolean(PREF_FIRST_OPEN,true);
    }

    //    Đặt giá trị cho biến ghi nhớ web nguồn lần cuối mở
    public static void setPrefDefaultWebsite(Context context, String value){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_DEFAULT_WEBSITE,value);
        editor.apply();
    }
//    Lấy giá trị biến ghi nhớ web nguồn lần cuối mở
    public static String getPrefDefaultWebsite(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return  preferences.getString(PREF_DEFAULT_WEBSITE,EnumWebSite.CAND.name());
    }
}
