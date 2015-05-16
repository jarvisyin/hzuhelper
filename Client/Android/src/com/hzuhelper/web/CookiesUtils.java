package com.hzuhelper.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.ClientCookie;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.hzuhelper.AppContext;
import com.hzuhelper.config.StaticValues;

class CookiesUtils{

  private static CookiesUtils cookiesUtils;

  private Map<String,String>  cookiesMap;

  private CookiesUtils(){
    SharedPreferences sp = AppContext.getAppContext().getSharedPreferences(StaticValues.cookies,Context.MODE_PRIVATE);
    cookiesMap = new HashMap<String,String>(3);
    cookiesMap.put(StaticValues.JSESSIONID,sp.getString(StaticValues.JSESSIONID,null));
    cookiesMap.put(StaticValues.UserName,sp.getString(StaticValues.UserName,null));
    cookiesMap.put(StaticValues.UserToken,sp.getString(StaticValues.UserToken,null));
  }

  public static CookiesUtils getInstances(){
    if (cookiesUtils==null) {
      synchronized (CookiesUtils.class) {
        if (cookiesUtils==null) cookiesUtils = new CookiesUtils();
      }
    }
    return cookiesUtils;
  }

  public CookieStore getCookieStore(){
    BasicCookieStore cookieStore = new BasicCookieStore();
    cookieStore.addCookie(new BasicClientCookie(StaticValues.JSESSIONID,cookiesMap.get(StaticValues.JSESSIONID)));
    cookieStore.addCookie(new BasicClientCookie(StaticValues.UserName,cookiesMap.get(StaticValues.UserName)));
    cookieStore.addCookie(new BasicClientCookie(StaticValues.UserToken,cookiesMap.get(StaticValues.UserToken)));
    return cookieStore;
  }

  /**
   * 更新cookies
   * @param cookieStore
   */
  public void updateCookies(CookieStore cookieStore){
    List<Cookie> cookies = cookieStore.getCookies();
    for (Cookie cookie : cookies) {
      String value = cookiesMap.get(cookie.getName());
      if (!isEquel(value,cookie.getName())) {
        saveCookies(cookies);
        break;
      }
    }
  }

  /**
   * 保存cookies，                     <br/>
   * 1.数据持久化                         <br/>
   * 2.更新内存中的数据              <br/>
   * @param cookies     
   */
  private void saveCookies(List<Cookie> cookies){
    synchronized (cookiesUtils) {
      SharedPreferences sp = AppContext.getAppContext().getSharedPreferences(StaticValues.cookies,Context.MODE_PRIVATE);
      Editor edit = sp.edit();
      for (Cookie cookie : cookies) {
        String value = cookiesMap.get(cookie.getName());
        if (!isEquel(value,cookie.getName())) {
          edit.putString(cookie.getName(),value);
        }
      }
      edit.commit();
    }
  }

  private boolean isEquel(String str1,String str2){
    if (str1==null&&str2==null) return true;
    else if (str1!=null) return str1.equals(str2);
    else return str2.equals(str1);
  }
}
