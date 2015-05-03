package com.hzuhelper.BLL;

import java.util.Date;

import com.hzuhelper.Control.DAL.ChatCommentDAL;
import com.hzuhelper.Control.DAL.ChatTweetDAL;
import com.hzuhelper.Domain.ChatComment1;

public class ChatCommentCommitBLL {

	public void commit(String email, String password, String content,
			int tweetId, int commentId) throws Exception {

		UserLoginBLL uibll = new UserLoginBLL();
		if (!uibll.Login(email, password)) {
			throw new Exception(uibll.getErrorMsg());
		}

		ChatComment1 chat_commentInfo = new ChatComment1();
		String receiverId;
		if (commentId < 1) {
			receiverId = new ChatTweetDAL().getModel(tweetId).getAuthorId();
		} else {
			chat_commentInfo.setResponseCommentId(commentId);
			receiverId = new ChatCommentDAL().getModel(commentId)
					.getAuthorId();
		}
		chat_commentInfo.setAuthorId(uibll.getUserInfo().getId());
		chat_commentInfo.setContent(content);
		chat_commentInfo.setPublishDatetime(new Date());
		chat_commentInfo.setStatu(1);
		chat_commentInfo.setTweetId(tweetId);
		chat_commentInfo.setReceiverId(receiverId);

		new ChatCommentDAL().add(chat_commentInfo);
		new ChatTweetDAL().addCommentCount(tweetId);
	}
}
