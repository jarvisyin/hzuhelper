package com.hzuhelper.Control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.hzuhelper.BLL.TakeoutMenuBLL;
import com.hzuhelper.BLL.TakeoutRestaurantBLL;
import com.hzuhelper.Model.TakeoutMenuInfo;
import com.hzuhelper.Model.TakeoutRestaurantInfo1;
import com.hzuhelper.Utility.Common;

public class ManaTakeoutMenu extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		TakeoutRestaurantBLL trbll = new TakeoutRestaurantBLL();
		String str = request.getParameter("restaurantId");
		if (!(str != null && Common.isNumeric(str))) {
			try {
				request.setAttribute("list", trbll.getList());
				request.getRequestDispatcher(
						"/manager/TakeoutRestaurantList.jsp").forward(request,
						response);
				return;
			} catch (Exception e) {
				PrintWriter out = response.getWriter();
				out.write(e.toString());
			}
		}
		int restaurantId = Integer.parseInt(str);

		try {
			TakeoutMenuBLL tmigbll = new TakeoutMenuBLL();
			ArrayList<TakeoutMenuInfo> list = tmigbll.getList(restaurantId);
			TakeoutRestaurantInfo1 model = trbll.getModel(restaurantId);
			request.setAttribute("restaurantInfo", model);
			request.setAttribute("count", tmigbll.count(restaurantId));
			request.setAttribute("list", list);
			request.getRequestDispatcher("/manager/TakeoutMenu.jsp").forward(
					request, response);
		} catch (Exception e) {
			PrintWriter out = response.getWriter();
			out.write(e.toString());
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String cmd = request.getParameter("cmd");
		if (cmd.equals("delete")) {
			delete(request, out);
		} else if (cmd.equals("update")) {
			update(request, out);
		} else if (cmd.equals("add")) {
			add(request, out);
		}
	}

	private void add(HttpServletRequest request, PrintWriter out) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String name = request.getParameter("name");
		String intro = request.getParameter("intro");
		String price = request.getParameter("price");
		String restaurantId = request.getParameter("restaurantId");
		map.put("statu", "false");
		try {
			if (name == null) {
				map.put("errorMsg", "id为空");
			} else if (intro == null) {
				map.put("errorMsg", "intro为空");
			} else if (price == null) {
				map.put("errorMsg", "phone为空");
			} else if (restaurantId == null || !Common.isNumeric(restaurantId)) {
				map.put("errorMsg", "店铺ID不合法" + restaurantId);
			} else {
				TakeoutMenuInfo model = new TakeoutMenuInfo();
				model.setRestaurantId(Integer.parseInt(restaurantId));
				model.setName(name);
				model.setIntro(intro);
				model.setPrice(Float.parseFloat(price));
				TakeoutMenuBLL bll = new TakeoutMenuBLL();
				bll.add(model);
				map.put("statu", "true");
			}
		} catch (NumberFormatException e) {
			map.put("errorMsg", "价格必须是数字");
		} catch (Exception e) {
			map.put("errorMsg", e.toString());
		} finally {
			out.write(JSONObject.fromObject(map).toString());
		}
	}

	private void update(HttpServletRequest request, PrintWriter out) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String intro = request.getParameter("intro");
		String price = request.getParameter("price");
		map.put("statu", "false");
		try {
			if (id == null) {
				map.put("errorMsg", "id 为空");
			} else if (!Common.isNumeric(id)) {
				map.put("errorMsg", "id 不合法");
			} else if (name == null) {
				map.put("errorMsg", "name为空");
			} else if (intro == null) {
				map.put("errorMsg", "intro为空");
			} else if (price == null) {
				map.put("errorMsg", "price为空");
			} else {
				TakeoutMenuInfo model = new TakeoutMenuInfo();
				model.setId(Integer.parseInt(id));
				model.setName(name);
				model.setIntro(intro);
				model.setPrice(Float.parseFloat(price));
				TakeoutMenuBLL bll = new TakeoutMenuBLL();
				bll.update(model);
				map.put("statu", "true");
			}
		} catch (Exception e) {
			map.put("errorMsg", e.getMessage());
		} finally {
			out.write(JSONObject.fromObject(map).toString());
		}
	}

	private void delete(HttpServletRequest request, PrintWriter out) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String str = request.getParameter("id");
		try {
			if (str == null || !Common.isNumeric(str)) {
				map.put("statu", "false");
				map.put("errorMsg", "id不合法");
			} else {
				TakeoutMenuBLL tmbll = new TakeoutMenuBLL();
				tmbll.delete(Integer.parseInt(str));
				map.put("statu", "true");
			}
		} catch (Exception e) {
			map.put("statu", "false");
			map.put("errorMsg", e.toString());
		} finally {
			out.write(JSONObject.fromObject(map).toString());
		}
	}
}
