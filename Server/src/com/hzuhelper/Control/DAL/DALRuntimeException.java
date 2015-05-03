package com.hzuhelper.Control.DAL;

import java.sql.SQLException;

public class DALRuntimeException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DALRuntimeException(SQLException e) {
		super(e.getMessage(), e.getCause());
		e.printStackTrace();
	}
}
