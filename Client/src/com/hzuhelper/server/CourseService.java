package com.hzuhelper.server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.hzuhelper.database.CourseDB;
import com.hzuhelper.model.CourseInfo;

public class CourseService {

	public static void resolveJsonString(JSONArray rpja, Context context)
			throws JSONException {
		if (rpja.length() < 1)
			return;
		CourseDB.delete();
		for (int i = 0, len = rpja.length(); i < len; i++) {
			JSONObject rpjo = rpja.getJSONObject(i);
			CourseInfo course = new CourseInfo();
			course.setServerid(rpjo.getInt("id"));
			course.setWeektime((byte) rpjo.getInt("weekTime"));
			course.setDaytime((byte) rpjo.getInt("daytime"));
			course.setCoursetime1(rpjo.getInt("courseTime1"));
			course.setCoursetime2(rpjo.getInt("courseTime2"));
			course.setStarttime(rpjo.getInt("startTime"));
			course.setEndtime(rpjo.getInt("endTime"));
			course.setName(rpjo.getString("name"));
			course.setTeacher(rpjo.getString("teacher"));
			course.setSite(rpjo.getString("site"));
			course.setStatu(rpjo.getInt("statu"));
			CourseDB.save(course);
		}
	}
}
