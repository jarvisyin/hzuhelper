package com.hzuhelper.web;

import android.text.TextUtils;

import com.google.gson.Gson;

public class JSONUtils {

    private static Gson gson = new Gson();

    public static <T>T fromJson(ResultObj resultObj,Class<T> cls){
        if (resultObj==null||TextUtils.isEmpty(resultObj.getResult())||cls==null) return null;
        return gson.fromJson(resultObj.getResult(),cls);
    }

}
