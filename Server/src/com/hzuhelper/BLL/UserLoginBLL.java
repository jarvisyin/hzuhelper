package com.hzuhelper.BLL;

import com.hzuhelper.Control.DAL.UserDAL;
import com.hzuhelper.Domain.User;
import com.hzuhelper.Utility.SHA256;

public class UserLoginBLL {
	private User userInfo;
	private String errorMsg;

	public String getErrorMsg() {
		return errorMsg;
	}

	public User getUserInfo() {
		return userInfo;
	}

	public boolean Login(String email, String password) {
		if (email == null || email.equals("")) {
			errorMsg = "邮箱地址为空";
			return false;
		}
		if (password == null || password.equals("")) {
			errorMsg = "密码为空";
			return false;
		}

		UserDAL dao = new UserDAL();
		if ((userInfo = dao.getModel(email)) == null) {
			errorMsg = "该用户不存在";
			return false;
		}
		
		SHA256.SHA256Encrypt(userInfo)
		
		if (!password.equals(userInfo.getPassword())) {
			errorMsg = "password is wrong";
			return false;
		}
		return true;
	}
}
