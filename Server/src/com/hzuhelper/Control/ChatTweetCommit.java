package com.hzuhelper.Control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.hzuhelper.ServletBase;
import com.hzuhelper.BLL.ChatTweetBLL;
import com.hzuhelper.Utility.Common;
import com.hzuhelper.Utility.ConstantStrUtil;

public class ChatTweetCommit extends ServletBase {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		HashMap<String, Object> map = new HashMap<String, Object>();

		String username = req.getParameter(ConstantStrUtil.USERNAME);
		String password = req.getParameter(ConstantStrUtil.PASSWORD);
		String content = req.getParameter("content");
		String tagId = req.getParameter("tagId");
		map.put("statu", "false");
		try {
			if (Common.isNullorEmpty(username)) {
				map.put("errorMsg", "用户名为空");
			} else if (Common.isNullorEmpty(password)) {
				map.put("errorMsg", "密码为空");
			} else if (Common.isNullorEmpty(content)) {
				map.put("errorMsg", "content为空");
			} else if (Common.isNullorEmpty(tagId)) {
				map.put("errorMsg", "tagId为空");
			} else if (!Common.isNumeric(tagId)) {
				map.put("errorMsg", "tagId不是数字");
			} else {
				ChatTweetBLL ctcBll = new ChatTweetBLL();
				ctcBll.commit(username, password, content,
						Integer.parseInt(tagId));
				map.put(ConstantStrUtil.STR_STATU, ConstantStrUtil.STR_TRUE);
			}
		} catch (Exception e) {
			map.put("errorMsg", e.toString());
		} finally {
			out.print(JSONObject.fromObject(map).toString());
		}
	}
}
