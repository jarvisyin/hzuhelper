package com.hzuhelper;

import java.util.Stack;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 * 
 * @author jarvis
 * @version 1.0
 * @created 2014-1-30
 */
public class AppManager {
	private static Stack<Activity> activityStack;
	private static AppManager instance;

	private AppManager() {
	}

	public static AppManager getAppManager() {
		if (instance == null) {
			return new AppManager();
		}
		return instance;
	}

	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	public Activity currentActivity() {
		return activityStack.lastElement();
	}

	public void finishActivity() {
		finishActivity(activityStack.lastElement());
	}

	public void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	public void finishActivity(Class<?> cls) {
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
			}
		}
	}

	public void finishAllActivity() {
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			if (null != activityStack.get(i)) {
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
	}

	public void appExit(Context context) {
		try {
			finishAllActivity();
			ActivityManager activityMng = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			activityMng.restartPackage(context.getPackageName());
			
			System.exit(0);
		} catch (Exception e) {
		}
	}
}
