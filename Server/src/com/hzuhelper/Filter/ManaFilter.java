package com.hzuhelper.Filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ManaFilter implements Filter {

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
		resp.setContentType("text/html;charset=UTF-8");
		String url = req.getRequestURI();
		int len = url.length();
		if (!(url.substring(len - 3, len).equals(".js") || url.substring(
				len - 4, len).equals(".css"))
				&& (!url.equals("/manager/login") && req.getSession()
						.getAttribute("Manager") == null)) {
			resp.sendRedirect("/manager/login");
			return;
		}
		chain.doFilter(req, resp);// 放行。让其走到下个链或目标资源中
	}

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
