package com.hzuhelper.Control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.hzuhelper.ServletBase;
import com.hzuhelper.BLL.TakeoutRestaurantBLL;
import com.hzuhelper.BLL.UserLoginBLL;
import com.hzuhelper.Utility.Common;
import com.hzuhelper.Utility.ConstantStrUtil;

public class TakeoutResCommentCommit extends ServletBase {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		HashMap<String, String> map = new HashMap<String, String>();
		/*
		 * String email = request.getParameter(ConstantStrUtil.USERNAME); String
		 * password = request.getParameter(ConstantStrUtil.PASSWORD);
		 * UserLoginBLL ulbll = new UserLoginBLL(); map.put("statu", "false");
		 * if (!ulbll.Login(email, password)) { map.put("errorMsg",
		 * ulbll.getErrorMsg());
		 * out.print(JSONObject.fromObject(map).toString()); return; }
		 */

		String id = request.getParameter("restaurantId");
		String type = request.getParameter("type");
		if (id == null || !Common.isNumeric(id)) {
			map.put("errorMsg", "id不合法");
			out.print(JSONObject.fromObject(map).toString());
			return;
		}
		String userid = request.getSession().getAttribute("user").toString();
		if ("ding".equals(type)) {
			dingCommit(map, Integer.parseInt(id), userid);
		} else if ("cai".equals(type)) {
			caiCommit(map, Integer.parseInt(id), userid);
		} else {
			map.put("errorMsg", "unkonw type");
		}
		out.print(JSONObject.fromObject(map).toString());
	}

	private void caiCommit(HashMap<String, String> map, int restaurantId,
			String userId) {
		TakeoutRestaurantBLL trbll = new TakeoutRestaurantBLL();
		try {
			trbll.cai(restaurantId, userId);
			map.put("statu", "true");
		} catch (Exception e) {
			map.put("errorMsg", e.getMessage().toString());
		}
	}

	private void dingCommit(HashMap<String, String> map, int restaurantId,
			String userId) {
		TakeoutRestaurantBLL trbll = new TakeoutRestaurantBLL();
		try {
			trbll.ding(restaurantId, userId);
			map.put("statu", "true");
		} catch (Exception e) {
			map.put("errorMsg", e.getMessage().toString());
		}
	}
}
