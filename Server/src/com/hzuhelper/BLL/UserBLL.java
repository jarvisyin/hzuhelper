package com.hzuhelper.BLL;

import java.security.MessageDigest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import com.hzuhelper.Control.DAL.StudentDAL;
import com.hzuhelper.Control.DAL.UserDAL;
import com.hzuhelper.Domain.User;
import com.hzuhelper.Utility.SHA256;

public class UserBLL {

	public ArrayList<User> getlist(int page) {
		try {
			return new UserDAL().getList(page - 1);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public static void regster(String email, String password, String studentId,
			String truename) throws Exception {
		if (email == null || "".equals(email)) {
			throw new Exception("邮箱不能为空");
		}
		if (studentId == null || "".equals(studentId)) {
			throw new Exception("学号不能为空");
		}
		if (password == null || "".equals(password)) {
			throw new Exception("密码不能为空");
		}
		if (truename == null || "".equals(truename)) {
			throw new Exception("姓名不能为空");
		}
		if (!new StudentDAL().exist(studentId, truename)) {
			throw new Exception("学号与姓名无法匹配");
		}

		UserDAL udal = new UserDAL();

		User userinfo = new User();
		userinfo.setId(UUID.randomUUID().toString());
		userinfo.setEmail(email);
		userinfo.setStudentId(studentId);
		userinfo.setPassword(SHA256.SHA256Encrypt(userinfo.getId() + password));
		userinfo.setRegisterDatetime(new Date());
		udal.add(userinfo);
	}

	public int count() throws SQLException {
		return new UserDAL().count();
	}
}
