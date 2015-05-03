package com.hzuhelper.model;

import java.util.Date;

public class ChatComment1Info {
	private int id;
	private String content;
	private int tweet_id;
	private String author_id;
	private int statu;
	private String response_user_id;
	private Date publish_date;

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

	public int getTweet_id() {
		return tweet_id;
	}

	public void setTweet_id(int tweet_id) {
		this.tweet_id = tweet_id;
	}

	public String getAuthor_id() {
		return author_id;
	}

	public void setAuthor_id(String author_id) {
		this.author_id = author_id;
	}

	public int getStatu() {
		return statu;
	}

	public void setStatu(int statu) {
		this.statu = statu;
	}

	public String getResponse_user_id() {
		return response_user_id;
	}

	public void setResponse_user_id(String response_user_id) {
		this.response_user_id = response_user_id;
	}

	public Date getPublish_date() {
		return publish_date;
	}

	public void setPublish_date(Date publish_date) {
		this.publish_date = publish_date;
	}

}
