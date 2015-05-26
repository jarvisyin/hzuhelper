package com.hzuhelper.utils;

import com.hzuhelper.AppContext;
import com.hzuhelper.utils.web.ResultObj;

import android.widget.Toast;

public class ToastUtil {

    public static void show(String msg){
        Toast.makeText(AppContext.getAppContext(),msg,Toast.LENGTH_LONG).show();
    }

    public static void show(ResultObj resultObj){
        Toast.makeText(AppContext.getAppContext(),resultObj.getResult(),Toast.LENGTH_LONG).show();
    }

}
