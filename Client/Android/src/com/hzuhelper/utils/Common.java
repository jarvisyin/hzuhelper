package com.hzuhelper.utils;

import java.util.Calendar;
import java.util.Date;

public class Common {

	public static byte getDayOfWeek(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		if (dayOfWeek < 0)
			dayOfWeek = 0;
		return (byte) dayOfWeek;
	}

	public static int getWeekOfTerm(Date date) {
		Calendar cl = Calendar.getInstance();
		cl.setTime(date);
		int WeekOfTerm = cl.get(Calendar.WEEK_OF_YEAR);
		return (int) (WeekOfTerm - 35);
	}

	public static int getMonoOfWeek(Date date) {
		return getWeekOfTerm(date) % 2;
	}


}
