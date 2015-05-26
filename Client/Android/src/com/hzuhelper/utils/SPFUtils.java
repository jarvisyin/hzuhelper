package com.hzuhelper.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.hzuhelper.AppContext;
import com.hzuhelper.config.StaticValues;

public class SPFUtils{
    public static void put(String SPFname,String key,String value){
        SharedPreferences sp = AppContext.getAppContext().getSharedPreferences(SPFname,Context.MODE_PRIVATE);
        Editor ed = sp.edit();
        ed.putString(key,value);
        ed.commit();
    }

    public static void put(String key,String value){
        put(StaticValues.SPF_FILE_COMMON,key,value);
    }

    public static String get(String SPFname,String key){
        SharedPreferences sp = AppContext.getAppContext().getSharedPreferences(SPFname,Context.MODE_PRIVATE);
        return sp.getString(key,null);
    }

    public static String get(String key){
        return get(StaticValues.SPF_FILE_COMMON,key);
    }
}
