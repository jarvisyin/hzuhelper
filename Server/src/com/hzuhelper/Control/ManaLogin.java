package com.hzuhelper.Control;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class Mana_login
 */
public class ManaLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		if (request.getAttribute("cmd") != null
				&& request.getAttribute("cmd").equals("exit"))
			removeSession(response, request);
		request.setAttribute("message", "Please login");
		request.getRequestDispatcher("/manager/Login.jsp").forward(request,
				response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		if (email.equals("ywj-1991@qq.com") && password.equals("ywj-1991")) {
			request.getSession().setAttribute("user", email);
			response.sendRedirect("/manager/user");
			return;
		}

		request.setAttribute("message", "用户名或密码错误");
		request.getRequestDispatcher("/manager/Login.jsp").forward(request,
				response);
	}

	private void removeSession(HttpServletResponse response,
			HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.invalidate();
	}
}
