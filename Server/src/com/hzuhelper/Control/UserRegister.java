package com.hzuhelper.Control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.hzuhelper.ServletBase;
import com.hzuhelper.BLL.UserBLL;
import com.hzuhelper.Utility.ConstantStrUtil;

public class UserRegister extends ServletBase {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter out = resp.getWriter();

		String email = req.getParameter("email");
		String truename = req.getParameter("truename");
		String studentId = req.getParameter("studentId");
		String password = req.getParameter("password");

		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			UserBLL.regster(email, password, studentId, truename);
			map.put(ConstantStrUtil.STR_STATU, ConstantStrUtil.STR_TRUE);
		} catch (Exception e) {
			map.put(ConstantStrUtil.STR_STATU, ConstantStrUtil.STR_FALSE);
			map.put(ConstantStrUtil.STR_ERRMSG, e.getMessage());
		}
		out.print(JSONObject.fromObject(map).toString());
	}
}
