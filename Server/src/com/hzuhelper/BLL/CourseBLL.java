package com.hzuhelper.BLL;

import java.sql.SQLException;
import java.util.ArrayList;

import com.hzuhelper.Control.DAL.CourseDAL;
import com.hzuhelper.Domain.Course;

public class CourseBLL {
	private String errorMsg;

	public String getErrorMsg() {
		return errorMsg;
	}

	public ArrayList<Course> getList(String email, String password) {
		if (email == null || "".equals(email)) {
			errorMsg = "username is null";
			return null;
		}
		if (password == null || "".equals(password)) {
			errorMsg = "password is null";
			return null;
		}

		UserLoginBLL ulBll = new UserLoginBLL();
		if (!ulBll.Login(email, password)) {
			errorMsg = ulBll.getErrorMsg();
			return null;
		}

		try {
			ArrayList<Course> clist = new CourseDAL().getList(email);
			return clist;
		} catch (SQLException e) {
			errorMsg = e.toString();
			return null;
		}
	}
}
