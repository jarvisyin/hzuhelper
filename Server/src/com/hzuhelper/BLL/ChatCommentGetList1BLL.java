package com.hzuhelper.BLL;

import java.sql.SQLException;
import java.util.ArrayList;

import com.hzuhelper.Control.DAL.ChatCommentDAL;
import com.hzuhelper.Control.DAL.ChatTweetDAL;
import com.hzuhelper.Domain.ChatComment1;
import com.hzuhelper.Domain.ChatTweet;

public class ChatCommentGetList1BLL {
	private String errorMsg;
	private ChatTweet Chat_tweet;
	private ArrayList<ChatComment1> list;

	public String getErrorMsg() {
		return errorMsg;
	}

	public ChatTweet getChat_tweet() {
		return Chat_tweet;
	}

	public ArrayList<ChatComment1> commentList() {
		return list;
	}

	public boolean getList(String str_tweet_id, String str_comment_id) {

		if (str_tweet_id == null) {
			errorMsg = "tweet_id is null";
			return false;
		}
		if (str_comment_id == null) {
			errorMsg = "comment_id is null";
			return false;
		}

		int tweet_id;

		try {
			tweet_id = Integer.parseInt(str_tweet_id.toString());
		} catch (Exception ex) {
			errorMsg = "tweet_id is not number";
			return false;
		}

		int comment_id;
		try {
			comment_id = Integer.parseInt(str_comment_id.toString());
		} catch (Exception ex) {
			errorMsg = "comment_id is not number";
			return false;
		}

		try {
			if (comment_id < 1) {
				Chat_tweet = new ChatTweetDAL().getModel(tweet_id);
				if (Chat_tweet == null) {
					errorMsg = "Chat_tweet is null";
					return false;
				}
			}
			list = new ChatCommentDAL().getList(tweet_id, comment_id);
		} catch (SQLException ex) {
			errorMsg = ex.toString();
			return false;
		} catch (Exception ex) {
			errorMsg = ex.toString();
			return false;
		}
		return true;
	}
}
