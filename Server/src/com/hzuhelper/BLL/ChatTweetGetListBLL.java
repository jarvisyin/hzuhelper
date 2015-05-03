package com.hzuhelper.BLL;

import java.util.ArrayList;

import com.hzuhelper.Control.DAL.ChatTweetDAL;
import com.hzuhelper.Domain.ChatTweet;
import com.hzuhelper.Utility.ConstantStrUtil;

public class ChatTweetGetListBLL {

	private String errorMsg;

	public String getErrorMsg() {
		return errorMsg;
	}

	public ArrayList<ChatTweet> getList(String type, String str_id,
			String email, String password, String tagId) {
		if (type == null || type == "") {
			errorMsg = "type is null";
			return null;
		}
		try {
			if (type.equals(ConstantStrUtil.STR_MINE)) {
				UserLoginBLL ubll = new UserLoginBLL();
				if (!ubll.Login(email, password)) {
					errorMsg = ubll.getErrorMsg();
					return null;
				}
				if (str_id == null) {
					return new ChatTweetDAL().getList(ubll.getUserInfo()
							.getId());
				}
				int id = Integer.parseInt(str_id.toString());
				return new ChatTweetDAL().getList(id, ubll.getUserInfo()
						.getId());
			} else if (type.equals(ConstantStrUtil.STR_ALL)) {
				if (str_id == null) {
					return new ChatTweetDAL().getList();
				}
				int id = Integer.parseInt(str_id.toString());
				return new ChatTweetDAL().getList(id);
			} else if (type.equals("tag")) {
				if (str_id == null) {
					return new ChatTweetDAL().getList(-1,
							Integer.parseInt(tagId));
				}
				int id = Integer.parseInt(str_id.toString());
				return new ChatTweetDAL().getList(id, Integer.parseInt(tagId));
			} else {
				errorMsg = "unknow type : " + type;
				return null;
			}
		} catch (Exception ex) {
			errorMsg = ex.toString();
			return null;
		}
		// ArrayList<Chat_tweetInfo> ctlist = new Chat_tweetDAL().getList(id);
	}
}
