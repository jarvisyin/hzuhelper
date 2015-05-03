package com.hzuhelper.BLL;

import java.sql.SQLException;
import java.util.ArrayList;

import com.hzuhelper.Control.DAL.ChatCommentDAL;
import com.hzuhelper.Domain.ChatComment2;

public class ChatCommentGetList2BLL {
	private String errorMsg;

	public String getErrorMsg() {
		return errorMsg;
	}

	public ArrayList<ChatComment2> getList(String email, String password,
			String str_id, String type) {
		if (type == null) {
			errorMsg = "type is null";
			return null;
		}
		if (email == null) {
			errorMsg = "email is null";
			return null;
		}

		if (password == null) {
			errorMsg = "password is null";
			return null;
		}

		if (str_id == null) {
			errorMsg = "id is null";
			return null;
		}

		int id = -1;
		try {
			id = Integer.parseInt(str_id);
		} catch (Exception e) {
			errorMsg = "id is not number";
			return null;
		}

		UserLoginBLL ulbll = new UserLoginBLL();
		if (!ulbll.Login(email, password)) {
			errorMsg = ulbll.getErrorMsg();
			return null;
		}

		try {
			if (type.equals("receive")) {
				ChatCommentDAL ccdal = new ChatCommentDAL();
				return ccdal.getList(ulbll.getUserInfo().getId(), id, 1);
			}
			if (type.equals("send")) {
				ChatCommentDAL ccdal = new ChatCommentDAL();
				return ccdal.getList(ulbll.getUserInfo().getId(), id, -1);
			}
			errorMsg = "unknow type";
			return null;
		} catch (SQLException ex) {
			errorMsg = ex.toString();
			return null;
		}
	}
}
