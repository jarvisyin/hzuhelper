package com.hzuhelper;

import java.util.HashMap;
import java.util.Map;

import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Display;
import android.view.WindowManager;

import cn.smssdk.SMSSDK;

import com.hzuhelper.tools.StringUtils;

/**
 * 全局应用程序类：用于保存和调用全局应用配置及访问网络数据
 * @author ywj
 * @version 1.0
 * @created 2014-6-29
 */
public class AppContext extends Application {
	
	public static final int NETTYPE_WIFI = 0x01;
	public static final int NETTYPE_CMWAP = 0x02;
	public static final int NETTYPE_CMNET = 0x03;
	
	public static final int PAGE_SIZE = 20;			//默认分页大小
			
	public static int screenWidth;
	public static int screenHeight;
	
	private static AppContext appContext;
	
	public static AppContext getAppContext(){
		return appContext;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
        //注册App异常崩溃处理器
		SMSSDK.initSDK(this, "<您的appkey>", "<您的appsecret>");
        Thread.setDefaultUncaughtExceptionHandler(AppException.getAppExceptionHandler());        
        initScreenDisplay();
        appContext=this;
	}

	/**
	 * 初始化
	 */
	private void initScreenDisplay(){		
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
	}	
	
	/**
	 * 检测网络是否可用
	 * @return
	 */
	public boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}

	/**
	 * 获取当前网络类型
	 * @return 0：没有网络   1：WIFI网络   2：WAP网络    3：NET网络
	 */
	public int getNetworkType() {
		int netType = 0;
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}		
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			String extraInfo = networkInfo.getExtraInfo();
			if(!StringUtils.isEmpty(extraInfo)){
				if (extraInfo.toLowerCase().equals("cmnet")) {
					netType = NETTYPE_CMNET;
				} else {
					netType = NETTYPE_CMWAP;
				}
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = NETTYPE_WIFI;
		}
		return netType;
	}
	
	/**
	 * 判断当前版本是否兼容目标版本的方法
	 * @param VersionCode
	 * @return
	 */
	public static boolean isMethodsCompat(int VersionCode) {
		int currentVersion = android.os.Build.VERSION.SDK_INT;
		return currentVersion >= VersionCode;
	}

	
	private Map<String,String> cookies=new HashMap<String, String>();
	public Map<String, String> getCookie() {
		return cookies;
	}
}
