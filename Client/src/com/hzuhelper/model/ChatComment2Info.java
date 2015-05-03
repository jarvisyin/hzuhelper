package com.hzuhelper.model;


public class ChatComment2Info {
	private int id;
	private String content;
	private int tweet_id;
	private String publish_date;
	private int statu;
	private int response_comment_id;
	private String yourContent;

	public String getPublish_date() {
		return publish_date;
	}

	public void setPublish_date(String publish_date) {
		this.publish_date = publish_date;
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

	public int getTweet_id() {
		return tweet_id;
	}

	public void setTweet_id(int tweet_id) {
		this.tweet_id = tweet_id;
	}

	public int getStatu() {
		return statu;
	}

	public void setStatu(int statu) {
		this.statu = statu;
	}

	public int getResponse_comment_id() {
		return response_comment_id;
	}

	public void setResponse_comment_id(int response_comment_id) {
		this.response_comment_id = response_comment_id;
	}

	public String getYourContent() {
		return yourContent;
	}

	public void setYourContent(String yourContent) {
		this.yourContent = yourContent;
	}
}
