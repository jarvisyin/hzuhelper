package com.hzuhelper.Control.Manager;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.hzuhelper.ServletBase;
import com.hzuhelper.BLL.TakeoutRestaurantBLL;
import com.hzuhelper.BLL.TakeoutRestaurantGetListBLL;
import com.hzuhelper.Domain.TakeoutRestaurant1;
import com.hzuhelper.Utility.Common;

public class ManaTakeoutRes extends ServletBase {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ArrayList<TakeoutRestaurant1> list;
		TakeoutRestaurantGetListBLL bll = new TakeoutRestaurantGetListBLL();
		if ((list = bll.getList()) == null) {
			response.getWriter().write("错误:" + bll.getErrorMsg());
			return;
		}
		request.setAttribute("list", list);
		request.getRequestDispatcher("/manager/TakeoutRestaurant.jsp").forward(
				request, response);
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
		String phone = request.getParameter("phone");
		map.put("statu", "false");
		try {
			if (name == null) {
				map.put("errorMsg", "id为空");
			} else if (intro == null) {
				map.put("errorMsg", "intro为空");
			} else if (phone == null) {
				map.put("errorMsg", "phone为空");
			} else {
				TakeoutRestaurant1 model = new TakeoutRestaurant1();
				model.setName(name);
				model.setIntro(intro);
				model.setPhone(phone);
				TakeoutRestaurantBLL bll = new TakeoutRestaurantBLL();
				bll.add(model);
				map.put("statu", "true");
			}
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
		String phone = request.getParameter("phone");
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
			} else if (phone == null) {
				map.put("errorMsg", "phone为空");
			} else {
				TakeoutRestaurant1 model = new TakeoutRestaurant1();
				model.setId(Integer.parseInt(id));
				model.setName(name);
				model.setIntro(intro);
				model.setPhone(phone);
				TakeoutRestaurantBLL bll = new TakeoutRestaurantBLL();
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
		String id = request.getParameter("id");
		try {
			if (id == null) {
				map.put("statu", "false");
				map.put("errorMsg", "id为空");
			} else {
				TakeoutRestaurantBLL trbll = new TakeoutRestaurantBLL();
				trbll.delete(id);
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
