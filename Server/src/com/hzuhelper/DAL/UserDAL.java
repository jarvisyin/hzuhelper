package com.hzuhelper.DAL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.hzuhelper.Model.UserInfo;
import com.hzuhelper.Utility.MySqlHelper;

public class UserDAL {
	static String TABLE_NAME = "User";
	static String id = "id";
	static String email = "email";
	static String password = "password";
	static String studentId = "studentId";
	static String registerDatetime = "registerDatetime";
	static String recentlyLoginDatetime = "recentlyLoginDatetime";

	public UserInfo getModel(String _email) throws SQLException {
		ResultSet rs = null;
		PreparedStatement pst = null;
		Connection connection = null;
		MySqlHelper msh = MySqlHelper.getInstance();
		try {
			connection = msh.getConnection();
			String sql = "select * from " + TABLE_NAME + " where " + email
					+ "=?";
			pst = connection.prepareStatement(sql);
			pst.setString(1, _email);
			rs = pst.executeQuery();
			if (rs.next()) {
				return RsToModel(rs);
			}
		} catch (SQLException ex) {
			throw ex;
		} finally {
			msh.free(rs, connection, pst);
		}
		return null;
	}

	public void add(UserInfo userinfo) throws SQLException {
		ResultSet rs = null;
		PreparedStatement pst = null;
		Connection connection = null;
		MySqlHelper msh = MySqlHelper.getInstance();
		try {
			String sql = "insert into " + TABLE_NAME + "(" + id + "," + email
					+ "," + password + "," + studentId + ") values(?,?,?,?)";
			connection = msh.getConnection();
			pst = connection.prepareStatement(sql);
			pst.setString(1, userinfo.getId());
			pst.setString(2, userinfo.getEmail());
			pst.setString(3, userinfo.getPassword());
			pst.setString(4, userinfo.getStudentId());
			pst.execute();
		} catch (SQLException e) {
			throw e;
		} finally {
			msh.free(rs, connection, pst);
		}
	}

	public boolean exist(String _email) throws SQLException {
		ResultSet rs = null;
		PreparedStatement pst = null;
		Connection connection = null;
		MySqlHelper msh = MySqlHelper.getInstance();
		try {
			String sql = "select 1  from " + TABLE_NAME + " where " + email
					+ "=? limit 1";
			connection = msh.getConnection();
			pst = connection.prepareStatement(sql);
			pst.setString(1, _email);
			rs = pst.executeQuery();
			if (rs.next() && rs.getInt(1) == 1) {
				return true;
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			msh.free(rs, connection, pst);
		}
		return false;
	}

	public int count() throws SQLException {
		ResultSet rs = null;
		PreparedStatement pst = null;
		Connection connection = null;
		MySqlHelper msh = MySqlHelper.getInstance();
		try {
			String sql = "select count(*) from " + TABLE_NAME;
			connection = msh.getConnection();
			pst = connection.prepareStatement(sql);
			rs = pst.executeQuery();
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			throw e;
		} finally {
			msh.free(rs, connection, pst);
		}
	}

	public ArrayList<UserInfo> getList(int page) throws SQLException {
		ResultSet rs = null;
		PreparedStatement pst = null;
		Connection connection = null;
		MySqlHelper msh = MySqlHelper.getInstance();
		try {
			String sql = "select * from " + TABLE_NAME + " order by "
					+ registerDatetime + " desc limit ?,20";
			connection = msh.getConnection();
			pst = connection.prepareStatement(sql);
			pst.setInt(1, page * 20);
			rs = pst.executeQuery();
			ArrayList<UserInfo> uilist = new ArrayList<UserInfo>();
			while (rs.next()) {
				uilist.add(RsToModel(rs));
			}
			return uilist;
		} catch (SQLException e) {
			throw e;
		} finally {
			msh.free(rs, connection, pst);
		}
	}

	public UserInfo RsToModel(ResultSet rs) throws SQLException {
		UserInfo model = new UserInfo();
		model.setId(rs.getString(id));
		model.setEmail(rs.getString(email));
		model.setPassword(rs.getString(password));
		model.setStudentId(rs.getString(studentId));
		model.setRegister_date(rs.getTimestamp(registerDatetime));
		return model;
	}
}
