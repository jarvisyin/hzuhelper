package com.hzuhelper.Domain;

import java.util.Date;

public class ChatComment1 {
	private int id;
	private String content;
	private int tweetId;
	private String authorId;
	private int statu;
	private int responseCommentId;
	private String receiverId;
	private Date publishDatetime;

	public int getResponseCommentId() {
		return responseCommentId;
	}

	public void setResponseCommentId(int responseCommentId) {
		this.responseCommentId = responseCommentId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getTweetId() {
		return tweetId;
	}

	public void setTweetId(int tweetId) {
		this.tweetId = tweetId;
	}

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	public int getStatu() {
		return statu;
	}

	public void setStatu(int statu) {
		this.statu = statu;
	}

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}

	public Date getPublishDatetime() {
		return publishDatetime;
	}

	public void setPublishDatetime(Date publishDatetime) {
		this.publishDatetime = publishDatetime;
	}

}
