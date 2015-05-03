package com.hzuhelper.Control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import com.hzuhelper.ServletBase;
import com.hzuhelper.BLL.ChatCommentGetList2BLL;
import com.hzuhelper.Domain.ChatComment2;
import com.hzuhelper.Utility.ConstantStrUtil;
import com.hzuhelper.Utility.JsonDateValueProcessor;

/**
 * Servlet implementation class Chat_commentGetList2
 */
public class ChatCommentGetList2 extends ServletBase {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();

		HashMap<String, Object> map = new HashMap<String, Object>();

		// 更改时间格式
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonDateValueProcessor());

		// 获取评论
		ChatCommentGetList2BLL ccgbll = new ChatCommentGetList2BLL();
		ArrayList<ChatComment2> list = ccgbll.getList(
				request.getParameter(ConstantStrUtil.USERNAME),
				request.getParameter(ConstantStrUtil.PASSWORD),
				request.getParameter("id"), request.getParameter("type"));
		if (list == null) {
			map.put(ConstantStrUtil.STR_STATU, ConstantStrUtil.STR_FALSE);
			map.put(ConstantStrUtil.STR_ERRMSG, ccgbll.getErrorMsg());
			out.print(JSONObject.fromObject(map, jsonConfig).toString());
			return;
		}

		map.put(ConstantStrUtil.STR_RESPONSE, list);
		map.put(ConstantStrUtil.STR_STATU, ConstantStrUtil.STR_TRUE);
		out.print(JSONObject.fromObject(map, jsonConfig).toString());
	}
}
