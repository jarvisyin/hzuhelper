package com.hzuhelper.Utility;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

public class Common {
	public static <T> T requestToBean(HttpServletRequest request,
			Class<T> beanClass) throws InstantiationException,
			IllegalAccessException {

		T bean = beanClass.newInstance();
		Field[] fs = beanClass.getDeclaredFields();

		for (Field field : fs) {
			String value = request.getParameter(field.getName());
			if (value != null) {
				field.set(bean, value);
			}
		}
		return bean;

	}

	public static String getUUID() {
		return java.util.UUID.randomUUID().toString().replace("-", "");
	}

	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	public static boolean isNullorEmpty(String str) {
		if (str == null || str.equals("")) {
			return true;
		}
		return false;
	}
}
