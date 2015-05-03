package com.hzuhelper.Control.DAL;

import java.sql.SQLException;

public class DALException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String detailMessage;

	public DALException(SQLException e) {
		super(e.getMessage(), e.getCause());
		if ("23000".equals(e.getSQLState())) {
			detailMessage = "用户名已存在";
		}
	}

	@Override
	public String getMessage() {
		if (detailMessage != null)
			return detailMessage;
		return super.getMessage();
	}
}
