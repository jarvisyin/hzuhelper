package com.hzuhelper.model;


public class ChatTweetInfo {
	private int id;
	private String content;
	private int ding;
	private int cai;
	private String publish_date;
	private int statu;
	private int tag_id;
	private int comment_count;
	private String author_id;

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

	public int getDing() {
		return ding;
	}

	public void setDing(int ding) {
		this.ding = ding;
	}

	public int getCai() {
		return cai;
	}

	public void setCai(int cai) {
		this.cai = cai;
	}

	public String getPublish_date() {
		return publish_date;
	}

	public void setPublish_date(String publish_date) {
		this.publish_date = publish_date;
	}

	public int getStatu() {
		return statu;
	}

	public void setStatu(int statu) {
		this.statu = statu;
	}

	public int getTag_id() {
		return tag_id;
	}

	public void setTag_id(int tag_id) {
		this.tag_id = tag_id;
	}

	public int getComment_count() {
		return comment_count;
	}

	public void setComment_count(int comment_count) {
		this.comment_count = comment_count;
	}

	public String getAuthor_id() {
		return author_id;
	}

	public void setAuthor_id(String author_id) {
		this.author_id = author_id;
	}
}