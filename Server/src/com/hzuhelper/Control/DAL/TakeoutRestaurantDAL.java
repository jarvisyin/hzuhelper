package com.hzuhelper.Control.DAL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.hzuhelper.Domain.TakeoutRestaurant1;
import com.hzuhelper.Domain.TakeoutRestaurant2;
import com.hzuhelper.Utility.MySqlHelper;

public class TakeoutRestaurantDAL {
	static String TABLE_NAME = "TakeoutRestaurant";
	static String id = "id";
	static String name = "name";
	static String intro = "intro";
	static String ding = "ding";
	static String cai = "cai";
	static String tagId = "tagId";
	static String commentCount = "commentCount";
	static String phone = "phone";

	public void delete(int _id) throws SQLException {
		MySqlHelper msh = MySqlHelper.getInstance();
		Connection connection = null;
		PreparedStatement pst = null;
		try {
			connection = msh.getConnection();
			String sql = "delete from " + TABLE_NAME + " where " + id
					+ "=? limit 1";
			pst = connection.prepareStatement(sql);
			pst.setInt(1, _id);
			if (pst.executeUpdate() != 1) {
				throw new SQLException("不存在该行！");
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			msh.free(null, connection, pst);
		}
	}

	public ArrayList<TakeoutRestaurant1> getList1() throws SQLException {
		ArrayList<TakeoutRestaurant1> clist = null;
		ResultSet rs = null;
		PreparedStatement pst = null;
		Connection connection = null;
		MySqlHelper msh = MySqlHelper.getInstance();
		try {
			String sqlText = "select " + id + "," + name + "," + intro + ","
					+ phone + "," + ding + "," + cai + "," + tagId + ","
					+ commentCount + " from " + TABLE_NAME + " ";
			connection = msh.getConnection();
			pst = connection.prepareStatement(sqlText);
			rs = pst.executeQuery();
			clist = new ArrayList<TakeoutRestaurant1>(20);
			while (rs.next()) {
				clist.add(RsToModel(rs));
			}
			return clist;
		} catch (SQLException e) {
			throw e;
		} finally {
			msh.free(rs, connection, pst);
		}
	}

	public ArrayList<TakeoutRestaurant2> getList2() throws SQLException {
		ArrayList<TakeoutRestaurant2> clist = null;
		ResultSet rs = null;
		PreparedStatement pst = null;
		Connection connection = null;
		MySqlHelper msh = MySqlHelper.getInstance();
		try {
			String sqlText = "select " + id + "," + name + " from "
					+ TABLE_NAME;
			connection = msh.getConnection();
			pst = connection.prepareStatement(sqlText);
			rs = pst.executeQuery();
			clist = new ArrayList<TakeoutRestaurant2>();
			while (rs.next()) {
				TakeoutRestaurant2 model = new TakeoutRestaurant2();
				model.setId(rs.getInt(id));
				model.setName(rs.getString(name));
				clist.add(model);
			}
			return clist;
		} catch (SQLException e) {
			throw e;
		} finally {
			msh.free(rs, connection, pst);
		}
	}

	public void add(TakeoutRestaurant1 model) throws SQLException {
		PreparedStatement pst = null;
		Connection connection = null;
		MySqlHelper msh = MySqlHelper.getInstance();
		try {
			connection = msh.getConnection();
			String sqlText = "insert into " + TABLE_NAME + "(" + name + ","
					+ intro + "," + ding + "," + cai + "," + tagId + ","
					+ commentCount + "," + phone + ") values(?,?,?,?,?,?,?) ";
			pst = connection.prepareStatement(sqlText);
			pst.setString(1, model.getName());
			pst.setString(2, model.getIntro());
			pst.setInt(3, model.getDing());
			pst.setInt(4, model.getCai());
			pst.setInt(5, model.getTagId());
			pst.setInt(6, model.getCommentNum());
			pst.setString(7, model.getPhone());
			pst.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			msh.free(null, connection, pst);
		}
	}

	public void update(TakeoutRestaurant1 model) throws SQLException {
		PreparedStatement pst = null;
		Connection connection = null;
		MySqlHelper msh = MySqlHelper.getInstance();
		try {
			connection = msh.getConnection();
			String sqlText = "update " + TABLE_NAME + " set " + name + "=?,"
					+ intro + "=?," + phone + "=? where " + id + "=? ";
			pst = connection.prepareStatement(sqlText);
			pst.setString(1, model.getName());
			pst.setString(2, model.getIntro());
			pst.setString(3, model.getPhone());
			pst.setInt(4, model.getId());
			if (pst.executeUpdate() != 1) {
				throw new SQLException("更新失败不存在该ID");
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			msh.free(null, connection, pst);
		}
	}

	public TakeoutRestaurant1 getModel(int _id) throws SQLException {
		ResultSet rs = null;
		PreparedStatement pst = null;
		Connection connection = null;
		MySqlHelper msh = MySqlHelper.getInstance();
		try {
			String sqlText = "select " + id + "," + name + "," + intro + ","
					+ phone + "," + ding + "," + cai + "," + tagId + ","
					+ commentCount + " from " + TABLE_NAME + " where " + id
					+ "=?";
			connection = msh.getConnection();
			pst = connection.prepareStatement(sqlText);
			pst.setInt(1, _id);
			rs = pst.executeQuery();
			if (!rs.next()) {
				throw new SQLException("不存在该id");
			}
			return RsToModel(rs);
		} catch (SQLException e) {
			throw e;
		} finally {
			msh.free(rs, connection, pst);
		}
	}

	public void ding(int _id) throws SQLException {
		PreparedStatement pst = null;
		Connection connection = null;
		MySqlHelper msh = MySqlHelper.getInstance();
		try {
			String sqlText = "update " + TABLE_NAME + " set " + ding + "="
					+ ding + "+1 where " + id + "=? limit 1";
			connection = msh.getConnection();
			pst = connection.prepareStatement(sqlText);
			pst.setInt(1, _id);
			if (pst.executeUpdate() != 1)
				throw new SQLException("不存在该id，修改失败");
		} catch (SQLException e) {
			throw e;
		} finally {
			msh.free(null, connection, pst);
		}
	}

	public void cai(int _id) throws SQLException {
		PreparedStatement pst = null;
		Connection connection = null;
		MySqlHelper msh = MySqlHelper.getInstance();
		try {
			String sqlText = "update " + TABLE_NAME + " set " + cai + "=" + cai
					+ "+1 where " + id + "=? limit 1";
			connection = msh.getConnection();
			pst = connection.prepareStatement(sqlText);
			pst.setInt(1, _id);
			if (pst.executeUpdate() != 1)
				throw new SQLException("不存在该id，修改失败");
		} catch (SQLException e) {
			throw e;
		} finally {
			msh.free(null, connection, pst);
		}
	}

	private TakeoutRestaurant1 RsToModel(ResultSet rs) throws SQLException {
		TakeoutRestaurant1 model = new TakeoutRestaurant1();
		model.setId(rs.getInt(id));
		model.setName(rs.getString(name));
		model.setIntro(rs.getString(intro));
		model.setPhone(rs.getString(phone));
		model.setDing(rs.getInt(ding));
		model.setCai(rs.getInt(cai));
		model.setCommentNum(rs.getInt(commentCount));
		model.setTagId(rs.getInt(tagId));
		return model;
	}
}
