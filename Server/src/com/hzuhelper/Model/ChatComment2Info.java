package com.hzuhelper.Model;

import java.util.Date;

public class ChatComment2Info {
	private int id;
	private String content;
	private int tweetId;
	private Date publishDatetime;
	private int statu;
	private int receiverId;
	private String yourContent;

	public Date getPublishDatetime() {
		return publishDatetime;
	}

	public void setPublishDatetime(Date publishDatetime) {
		this.publishDatetime = publishDatetime;
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

	public int getStatu() {
		return statu;
	}

	public void setStatu(int statu) {
		this.statu = statu;
	}

	public int getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(int receiverId) {
		this.receiverId = receiverId;
	}

	public String getYourContent() {
		return yourContent;
	}

	public void setYourContent(String yourContent) {
		this.yourContent = yourContent;
	}
}
