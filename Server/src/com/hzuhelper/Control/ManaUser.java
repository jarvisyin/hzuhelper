package com.hzuhelper.Control;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hzuhelper.ServletBase;
import com.hzuhelper.BLL.UserBLL;
import com.hzuhelper.Utility.Common;

public class ManaUser extends ServletBase {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String str_page = request.getParameter("page");
		int page = 1;
		if (str_page != null && Common.isNumeric(str_page)) {
			page = Integer.parseInt(str_page);
		}
		page = page < 1 ? 1 : page;
		request.setAttribute("page", page);
		try {
			request.setAttribute("userCount", new UserBLL().count());
			request.setAttribute("uilist", new UserBLL().getlist(page));
			request.getRequestDispatcher("/manager/User.jsp").forward(request,
					response);
		} catch (Exception e) {
			response.getWriter().print(e.toString());
		}
	}
}
