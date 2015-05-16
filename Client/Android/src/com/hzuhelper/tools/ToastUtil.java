package com.hzuhelper.tools;

import com.hzuhelper.AppContext;

import android.widget.Toast;

public class ToastUtil {

    public static void show(String msg){
        Toast.makeText(AppContext.getAppContext(),msg,Toast.LENGTH_LONG).show();
    }

}
