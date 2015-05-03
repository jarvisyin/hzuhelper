package com.hzuhelper.Control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.hzuhelper.ServletBase;
import com.hzuhelper.BLL.UserLoginBLL;
import com.hzuhelper.Utility.ConstantStrUtil;

public class UserLogin extends ServletBase {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter out = resp.getWriter();

		String Email = req.getParameter("email");
		String PassWord = req.getParameter("password");
		boolean islogin = false;
		UserLoginBLL bll = new UserLoginBLL();
		if (Email != null && PassWord != null) {
			// 验证用户名和密码
			boolean checkOk = bll.Login(Email, PassWord);
			if (checkOk) {
				islogin = true;
			}
		}
		// 返回信息
		req.getSession().setAttribute("user", bll.getUserInfo().getId());
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(ConstantStrUtil.STR_STATU, String.valueOf(islogin));
		out.print(JSONObject.fromObject(map).toString());
	}

}
