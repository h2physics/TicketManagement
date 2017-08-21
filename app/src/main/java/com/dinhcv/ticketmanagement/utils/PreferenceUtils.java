package com.dinhcv.ticketmanagement.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by YukiNoHara on 5/31/2017.
 */

public class PreferenceUtils {
    public static SharedPreferences preferences;
    public static SharedPreferences.Editor editor;
    public static void setTokenReferences(Context context, String token){
        preferences = context.getSharedPreferences(PreferenceKey.PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString(PreferenceKey.PREF_ACCESS_TOKEN, token);
        editor.apply();
    }

    public static String getTokenReferences(Context context){
        preferences = context.getSharedPreferences(PreferenceKey.PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(PreferenceKey.PREF_ACCESS_TOKEN, null);
    }

    public static void setIsLogin(Context context, boolean isLogin){
        preferences = context.getSharedPreferences(PreferenceKey.PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putBoolean(PreferenceKey.PREF_IS_LOGIN, isLogin);
        editor.apply();
    }

    public static boolean getIsLogin(Context context){
        preferences = context.getSharedPreferences(PreferenceKey.PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(PreferenceKey.PREF_IS_LOGIN, false);
    }

}
