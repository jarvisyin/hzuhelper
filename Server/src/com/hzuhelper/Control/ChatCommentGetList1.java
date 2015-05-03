package com.hzuhelper.Control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import com.hzuhelper.ServletBase;
import com.hzuhelper.BLL.ChatCommentGetList1BLL;
import com.hzuhelper.Utility.ConstantStrUtil;
import com.hzuhelper.Utility.JsonDateValueProcessor;

public class ChatCommentGetList1 extends ServletBase {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		HashMap<String, Object> map = new HashMap<String, Object>();

		// 更改时间格式
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class,
				new JsonDateValueProcessor());

		// 获取评论
		ChatCommentGetList1BLL ccBll = new ChatCommentGetList1BLL();
		boolean b = ccBll.getList(req.getParameter("tweetId"),
				req.getParameter("commentId"));
		if (!b) {
			map.put(ConstantStrUtil.STR_STATU, ConstantStrUtil.STR_FALSE);
			map.put(ConstantStrUtil.STR_ERRMSG, ccBll.getErrorMsg());
			out.print(JSONObject.fromObject(map, jsonConfig).toString());
			return;
		}

		map.put("tweet", ccBll.getChat_tweet());
		map.put("commentlist", ccBll.commentList());
		map.put(ConstantStrUtil.STR_STATU, ConstantStrUtil.STR_TRUE);
		out.print(JSONObject.fromObject(map, jsonConfig).toString());
	}
}
