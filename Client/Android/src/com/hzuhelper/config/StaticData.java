package com.hzuhelper.config;

import java.util.LinkedHashMap;

import com.hzuhelper.model.TakeoutRestaurantInfo;

public class StaticData {
	private static TakeoutRestaurantInfo Takeout_restaurantInfo;
	public final static int RESTAURANT_COMMENT = 1;

	public static LinkedHashMap<Integer, String> weekTime;
	public static LinkedHashMap<Integer, String> dayTime;
	public static LinkedHashMap<Integer, String> courseTime;
	public static LinkedHashMap<Integer, String> termTime;
	static {
		weekTime = new LinkedHashMap<Integer, String>();
		weekTime.put(1, "单周");
		weekTime.put(2, "双周");
		weekTime.put(3, "全周");

		dayTime = new LinkedHashMap<Integer, String>();
		dayTime.put(1, "周一");
		dayTime.put(2, "周二");
		dayTime.put(3, "周三");
		dayTime.put(4, "周四");
		dayTime.put(5, "周五");
		dayTime.put(6, "周六");
		dayTime.put(7, "周日");

		courseTime = new LinkedHashMap<Integer, String>();
		courseTime.put(1, "第1节");
		courseTime.put(2, "第2节");
		courseTime.put(3, "第3节");
		courseTime.put(4, "第4节");
		courseTime.put(5, "第5节");
		courseTime.put(6, "第6节");
		courseTime.put(7, "第7节");
		courseTime.put(8, "第8节");
		courseTime.put(9, "第9节");
		courseTime.put(10, "第10节");
		courseTime.put(11, "第11节");
		courseTime.put(12, "第12节");

		termTime = new LinkedHashMap<Integer, String>();
		termTime.put(1, "第1周");
		termTime.put(2, "第2周");
		termTime.put(3, "第3周");
		termTime.put(4, "第4周");
		termTime.put(5, "第5周");
		termTime.put(6, "第6周");
		termTime.put(7, "第7周");
		termTime.put(8, "第8周");
		termTime.put(9, "第9周");
		termTime.put(10, "第10周");
		termTime.put(11, "第11周");
		termTime.put(12, "第12周");
		termTime.put(13, "第13周");
		termTime.put(14, "第14周");
		termTime.put(15, "第15周");
		termTime.put(16, "第16周");
		termTime.put(17, "第17周");
		termTime.put(18, "第18周");
		termTime.put(19, "第19周");
		termTime.put(20, "第20周");
		termTime.put(21, "第21周");
		termTime.put(22, "第22周");
		termTime.put(23, "第23周");
		termTime.put(24, "第24周");
		termTime.put(25, "第25周");
	}

	public static TakeoutRestaurantInfo getTakeout_restaurantInfo() {
		TakeoutRestaurantInfo a = Takeout_restaurantInfo;
		Takeout_restaurantInfo = null;
		return a;
	}

	public static void setTakeout_restaurantInfo(
			TakeoutRestaurantInfo takeout_restaurantInfo) {
		Takeout_restaurantInfo = takeout_restaurantInfo;
	}
}