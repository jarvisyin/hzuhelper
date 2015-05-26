package com.hzuhelper.model.receive;

import com.hzuhelper.model.RECEIVE;

/**
 * 最新应用信息
 * @author jarvisyin
 *
 */
public class P6001 extends RECEIVE{
    private String appContent;
    private String osType;
    private String url;
    private String versionName;
    private int    versionCode;

    public String getAppContent(){
        return appContent;
    }

    public void setAppContent(String appContent){
        this.appContent = appContent;
    }

    public String getOsType(){
        return osType;
    }

    public void setOsType(String osType){
        this.osType = osType;
    }

    public String getUrl(){
        return url;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public String getVersionName(){
        return versionName;
    }

    public void setVersionName(String versionName){
        this.versionName = versionName;
    }

    public int getVersionCode(){
        return versionCode;
    }

    public void setVersionCode(int versionCode){
        this.versionCode = versionCode;
    }

}
