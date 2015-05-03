package com.hzuhelper.Control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.hzuhelper.ServletBase;
import com.hzuhelper.BLL.TakeoutMenuBLL;
import com.hzuhelper.Domain.TakeoutMenu;
import com.hzuhelper.Utility.Common;
import com.hzuhelper.Utility.ConstantStrUtil;

public class MenuItemGetList extends ServletBase {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("statu", "false");
		String restaurantId = request.getParameter("restaurantId");
		if (restaurantId == null || !Common.isNumeric(restaurantId)) {
			map.put("errorMsg", "外卖铺ID不合法");
			out.print(JSONObject.fromObject(map).toString());
			return;
		}

		try {
			TakeoutMenuBLL bll = new TakeoutMenuBLL();
			ArrayList<TakeoutMenu> list;
			list = bll.getList(Integer.parseInt(restaurantId));
			map.put("statu", "true");
			map.put(ConstantStrUtil.STR_RESPONSE, list);
		} catch (Exception e) {
			map.put("errorMsg", e.toString());
		}
		out.print(JSONObject.fromObject(map).toString());
	}
}
