package com.hzuhelper.BLL;

import java.sql.SQLException;
import java.util.Date;

import com.hzuhelper.Control.DAL.ChatTweetDAL;
import com.hzuhelper.Domain.ChatTweet;

public class ChatTweetBLL {
	private String errorMsg = "";

	public String getErrorMsg() {
		return errorMsg;
	}

	public ChatTweet getTweet(Object obj_id) {
		if (obj_id == null) {
			errorMsg += "id is null";
			return null;
		}

		int id;
		try {
			id = Integer.parseInt(obj_id.toString());
		} catch (Exception ex) {
			errorMsg += "id is not number";
			return null;
		}
		ChatTweet chat_tweetInfo;
		try {
			chat_tweetInfo = new ChatTweetDAL().getModel(id);
		} catch (SQLException e) {
			e.printStackTrace();
			errorMsg += e.toString();
			return null;
		}
		return chat_tweetInfo;
	}

	public void commit(String email, String password, String content, int tagId)
			throws Exception {
		UserLoginBLL uibll = new UserLoginBLL();
		if (!uibll.Login(email, password)) {
			errorMsg = uibll.getErrorMsg();
			throw new Exception("用户登录失败");
		}
		ChatTweet chat_tweetInfo = new ChatTweet();
		chat_tweetInfo.setAuthorId(uibll.getUserInfo().getId());
		chat_tweetInfo.setContent(content);
		chat_tweetInfo.setPublishDatetime(new Date());
		chat_tweetInfo.setStatu(1);
		chat_tweetInfo.setTagId(tagId);
		new ChatTweetDAL().add(chat_tweetInfo);
	}
}
