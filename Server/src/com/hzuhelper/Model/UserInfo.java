package com.hzuhelper.Model;

import java.util.Date;

public class UserInfo {

	private String id;
	private String email;
	private String password;
	private String studentId;
	private Date registerDatetime;
	private Date recentlyLoginDatetime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public Date getRegisterDatetime() {
		return registerDatetime;
	}

	public void setRegister_date(Date registerDatetime) {
		this.registerDatetime = registerDatetime;
	}

	public Date getRecentlyLoginDatetime() {
		return recentlyLoginDatetime;
	}

	public void setRecentlyLoginDatetime(Date recentlyLoginDatetime) {
		this.recentlyLoginDatetime = recentlyLoginDatetime;
	}
}
