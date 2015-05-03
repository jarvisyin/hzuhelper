package com.hzuhelper.DAL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.hzuhelper.Utility.MySqlHelper;

public class TagRecordDAL {
	static String TABLE_NAME = "TagRecord";
	static String name = "name";
	static String tagId = "tagId";
	static String userId = "userId";

	public boolean exist(int _restaurantId, String _userId) {
		ResultSet rs = null;
		PreparedStatement pst = null;
		Connection connection = null;
		MySqlHelper msh = MySqlHelper.getInstance();
		try {
			String sql = "select 1 as num from " + TABLE_NAME + " where "
					+ tagId + "=? and " + userId + "=?";
			connection = msh.getConnection();
			pst = connection.prepareStatement(sql);
			pst.setInt(1, _restaurantId);
			pst.setString(2, _userId);
			rs = pst.executeQuery();
			if (rs.next() && rs.getInt(1) == 1) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			msh.free(rs, connection, pst);
		}
		return false;
	}

	public boolean add(int _restaurantId, String _userId) throws Exception {
		PreparedStatement pst = null;
		Connection connection = null;
		MySqlHelper msh = MySqlHelper.getInstance();
		try {
			String sql = "insert into " + TABLE_NAME + "(" + tagId + ","
					+ userId + ") values(?,?)";
			connection = msh.getConnection();
			pst = connection.prepareStatement(sql);
			pst.setInt(1, _restaurantId);
			pst.setString(2, _userId);
			pst.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			msh.free(null, connection, pst);
		}
		return false;
	}
}
