package com.hzuhelper.DAL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.hzuhelper.Model.TakeoutMenuInfo;
import com.hzuhelper.Utility.MySqlHelper;

public class TakeoutMenuDAL {
	static String TABLE_NAME = "TakeoutMenu";
	static String id = "id";
	static String name = "name";
	static String price = "price";
	static String restaurantId = "restaurantId";
	static String intro = "intro";

	public void deleteByResId(int resId) throws SQLException {
		PreparedStatement pst = null;
		Connection connection = null;
		MySqlHelper msh = MySqlHelper.getInstance();
		try {
			connection = msh.getConnection();
			String sqlText = "delete from " + TABLE_NAME + " where "
					+ restaurantId + "=?";
			pst = connection.prepareStatement(sqlText);
			pst.setInt(1, resId);
			pst.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			msh.free(null, connection, pst);
		}
	}

	public ArrayList<TakeoutMenuInfo> getList1(int _restaurantId)
			throws SQLException {
		ArrayList<TakeoutMenuInfo> clist = null;
		ResultSet rs = null;
		PreparedStatement pst = null;
		Connection connection = null;
		MySqlHelper msh = MySqlHelper.getInstance();
		try {
			connection = msh.getConnection();
			String sqlText = "select " + id + "," + name + "," + price + ","
					+ restaurantId + "," + intro + " from " + TABLE_NAME + " ";
			if (_restaurantId > 0)
				sqlText += "where " + restaurantId + "=?";
			pst = connection.prepareStatement(sqlText);
			if (_restaurantId > 0)
				pst.setInt(1, _restaurantId);
			rs = pst.executeQuery();
			clist = new ArrayList<TakeoutMenuInfo>(20);
			while (rs.next()) {
				TakeoutMenuInfo model = new TakeoutMenuInfo();
				model.setId(rs.getInt(id));
				model.setName(rs.getString(name));
				model.setIntro(rs.getString(intro));
				model.setPrice(rs.getFloat(price));
				model.setRestaurantId(rs.getInt(restaurantId));
				clist.add(model);
			}
			return clist;
		} catch (SQLException e) {
			throw e;
		} finally {
			msh.free(rs, connection, pst);
		}
	}

	public int count(int _restaurantId) throws SQLException {
		ResultSet rs = null;
		PreparedStatement pst = null;
		Connection connection = null;
		MySqlHelper msh = MySqlHelper.getInstance();
		try {
			connection = msh.getConnection();
			String sqlText = "select count(*) from " + TABLE_NAME + " where "
					+ restaurantId + "=?";
			pst = connection.prepareStatement(sqlText);
			pst.setInt(1, _restaurantId);
			rs = pst.executeQuery();
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			throw e;
		} finally {
			msh.free(rs, connection, pst);
		}
	}

	public int count() throws SQLException {
		ResultSet rs = null;
		PreparedStatement pst = null;
		Connection connection = null;
		MySqlHelper msh = MySqlHelper.getInstance();
		try {
			connection = msh.getConnection();
			String sqlText = "select count(*) from " + TABLE_NAME;
			pst = connection.prepareStatement(sqlText);
			rs = pst.executeQuery();
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			throw e;
		} finally {
			msh.free(rs, connection, pst);
		}
	}

	public void delete(int _id) throws SQLException {
		ResultSet rs = null;
		PreparedStatement pst = null;
		Connection connection = null;
		MySqlHelper msh = MySqlHelper.getInstance();
		try {
			connection = msh.getConnection();
			String sqlText = "delete from " + TABLE_NAME + " where " + id
					+ "=? limit 1";
			pst = connection.prepareStatement(sqlText);
			pst.setInt(1, _id);
			if (pst.executeUpdate() != 1)
				throw new SQLException("删除失败");
		} catch (SQLException e) {
			throw e;
		} finally {
			msh.free(rs, connection, pst);
		}
	}

	public void add(TakeoutMenuInfo model) throws SQLException {
		PreparedStatement pst = null;
		Connection connection = null;
		MySqlHelper msh = MySqlHelper.getInstance();
		try {
			connection = msh.getConnection();
			String sqlText = "insert into " + TABLE_NAME + "  (" + name + ","
					+ price + "," + restaurantId + "," + intro
					+ ") value(?,?,?,?)";
			pst = connection.prepareStatement(sqlText);
			pst.setString(1, model.getName());
			pst.setFloat(2, model.getPrice());
			pst.setInt(3, model.getRestaurantId());
			pst.setString(4, model.getIntro());
			pst.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			msh.free(null, connection, pst);
		}
	}

	public void update(TakeoutMenuInfo model) throws SQLException {
		PreparedStatement pst = null;
		Connection connection = null;
		MySqlHelper msh = MySqlHelper.getInstance();
		try {
			connection = msh.getConnection();
			String sqlText = "update " + TABLE_NAME + " set " + name + "=?,"
					+ price + "=?," + intro + "=? where " + id + "=? limit 1";
			pst = connection.prepareStatement(sqlText);
			pst.setString(1, model.getName());
			pst.setFloat(2, model.getPrice());
			pst.setString(3, model.getIntro());
			pst.setInt(4, model.getId());
			if (pst.executeUpdate() != 1) {
				throw new SQLException("修改失败!不存在该菜单");
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			msh.free(null, connection, pst);
		}
	}
}
