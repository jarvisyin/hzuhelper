package com.hzuhelper;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.IBinder;
import android.text.TextUtils;

import com.hzuhelper.config.StaticValues;
import com.hzuhelper.config.staticURL;
import com.hzuhelper.model.receive.P6000;
import com.hzuhelper.model.receive.P6001;
import com.hzuhelper.utils.SPFUtils;
import com.hzuhelper.utils.web.JSONUtils;
import com.hzuhelper.utils.web.ResultObj;
import com.hzuhelper.utils.web.WebRequest;

public class AppServices extends Service{

    @Override
    public void onCreate(){
        try {
            getURL();
            checkOutApp();
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent){
        return null;
    }

    @Override
    public void onDestroy(){}

    private void getURL(){
        refreshURL();
        WebRequest wq = new WebRequest(staticURL.host_url,staticURL.get_url){
            @Override
            protected void onSuccess(ResultObj resultObj){
                P6000 p6000 = JSONUtils.fromJson(resultObj,P6000.class);
                SPFUtils.put(StaticValues.request_url,p6000.getURL());
                refreshURL();
            }
        };
        wq.start();
    }

    private void refreshURL(){
        String url = SPFUtils.get(StaticValues.request_url);
        if (!TextUtils.isEmpty(url)) {
            staticURL.request_url = url;
        }
    }

    private void checkOutApp() throws NameNotFoundException{
        final int localVersionCode = getPackageManager().getPackageInfo(getPackageName(),PackageManager.GET_CONFIGURATIONS).versionCode;
        WebRequest wq = new WebRequest(staticURL.app_update){
            @Override
            protected void onSuccess(ResultObj resultObj){
                P6001 p6001 = JSONUtils.fromJson(resultObj,P6001.class);
                if (localVersionCode<p6001.getVersionCode()) {
                    SPFUtils.put(StaticValues.needToUpdate,StaticValues.True);
                } else {
                    SPFUtils.put(StaticValues.needToUpdate,StaticValues.False);
                }
            }
        };
        wq.setParam(StaticValues.type,StaticValues.android);
        wq.start();
    }

}
