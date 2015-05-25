package com.hzuhelper.model.receive;

public class P6001{
    /**
     * 1---不需要升级
     * 2---需要升级，但非强制的
     * 3---需要强制升级
     */
    private String needToUpdate;
    private String URL;

    /**
     * 1---不需要升级                                    <br/>
     * 2---需要升级，但非强制的                 <br/>
     * 3---需要强制升级                                <br/>
     */
    public String getNeedToUpdate(){
        return needToUpdate;
    }

    public void setNeedToUpdate(String needToUpdate){
        this.needToUpdate = needToUpdate;
    }

    public String getURL(){
        return URL;
    }

    public void setURL(String uRL){
        URL = uRL;
    }
}
