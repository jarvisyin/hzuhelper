package com.hzuhelper.Control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.hzuhelper.ServletBase;
import com.hzuhelper.BLL.ChatCommentCommitBLL;
import com.hzuhelper.Utility.Common;
import com.hzuhelper.Utility.ConstantStrUtil;

public class ChatCommentCommit extends ServletBase {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		HashMap<String, Object> map = new HashMap<String, Object>();

		String username = req.getParameter(ConstantStrUtil.USERNAME);
		String password = req.getParameter(ConstantStrUtil.PASSWORD);
		String content = req.getParameter("content");
		String tweetId = req.getParameter("tweetId");
		String commentId = req.getParameter("commentId");
		map.put("statu", "false");
		try {
			if (Common.isNullorEmpty(username)) {
				map.put("errorMsg", "用户名为空");
			} else if (Common.isNullorEmpty(password)) {
				map.put("errorMsg", "密码为空");
			} else if (Common.isNullorEmpty(content)) {
				map.put("errorMsg", "content为空");
			} else if (Common.isNullorEmpty(tweetId)) {
				map.put("errorMsg", "tweetId为空");
			} else if (Common.isNullorEmpty(commentId)) {
				map.put("errorMsg", "commentId为空");
			} else if (!Common.isNumeric(tweetId)) {
				map.put("errorMsg", "tweetId不是数字");
			} else if (!Common.isNumeric(commentId)) {
				map.put("errorMsg", "commentId不是数字");
			} else {
				ChatCommentCommitBLL ctcBll = new ChatCommentCommitBLL();
				ctcBll.commit(username, password, content,
						Integer.parseInt(tweetId), Integer.parseInt(commentId));
				map.put("statu", "true");
			}
		} catch (Exception e) {
			map.put("errorMsg", e.toString());
		} finally {
			out.print(JSONObject.fromObject(map).toString());
		}
	}
}
