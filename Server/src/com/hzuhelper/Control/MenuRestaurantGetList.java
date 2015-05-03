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
import com.hzuhelper.BLL.TakeoutRestaurantGetListBLL;
import com.hzuhelper.Domain.TakeoutRestaurant1;
import com.hzuhelper.Utility.ConstantStrUtil;

/**
 * Servlet implementation class Menu_restaurantGetList
 */
public class MenuRestaurantGetList extends ServletBase {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		HashMap<String, Object> map = new HashMap<String, Object>();

		TakeoutRestaurantGetListBLL bll = new TakeoutRestaurantGetListBLL();
		ArrayList<TakeoutRestaurant1> list;
		if ((list = bll.getList()) == null) {
			map.put("statu", "false");
			map.put("errorMsg", bll.getErrorMsg());
			out.print(JSONObject.fromObject(map).toString());
			return;
		}

		map.put("statu", "true");
		map.put(ConstantStrUtil.STR_RESPONSE, list);
		out.print(JSONObject.fromObject(map).toString());
	}
}
