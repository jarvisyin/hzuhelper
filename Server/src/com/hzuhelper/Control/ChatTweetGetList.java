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
import com.hzuhelper.BLL.ChatTweetGetListBLL;
import com.hzuhelper.Domain.ChatTweet;
import com.hzuhelper.Utility.ConstantStrUtil;
import com.hzuhelper.Utility.JsonDateValueProcessor;

public class ChatTweetGetList extends ServletBase {
	private static final long serialVersionUID =1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		HashMap<String, Object> map = new HashMap<String, Object>();

		ChatTweetGetListBLL ctgbll = new ChatTweetGetListBLL();
		ArrayList<ChatTweet> ctlist = ctgbll.getList(req.getParameter(ConstantStrUtil.STR_TYPE),
				req.getParameter("id"),
				req.getParameter(ConstantStrUtil.USERNAME),
				req.getParameter(ConstantStrUtil.PASSWORD), req.getParameter("tagId"));
		if (ctlist == null) {
			map.put(ConstantStrUtil.STR_STATU, ConstantStrUtil.STR_FALSE);
			map.put(ConstantStrUtil.STR_ERRMSG, ctgbll.getErrorMsg());
			out.print(JSONObject.fromObject(map).toString());
		} else {
			map.put(ConstantStrUtil.STR_RESPONSE, ctlist);
			map.put(ConstantStrUtil.STR_STATU, ConstantStrUtil.STR_TRUE);
			JsonConfig jsonConfig = new JsonConfig();
			jsonConfig.registerJsonValueProcessor(Date.class,
					new JsonDateValueProcessor());
			out.print(JSONObject.fromObject(map, jsonConfig).toString());
		}

	}
}
