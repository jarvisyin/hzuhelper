package com.hzuhelper.Control.DAL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.hzuhelper.Utility.MySqlHelper;

public class StudentDAL {
	static String TABLE_NAME = "Student";
	static String id = "id";
	static String name = "name";

	public boolean exist(String _id, String _name) throws Exception {
		ResultSet rs = null;
		PreparedStatement pst = null;
		Connection connection = null;
		MySqlHelper msh = MySqlHelper.getInstance();
		try {
			String sql = "select 1 as num from " + TABLE_NAME + " where " + id
					+ "=? and " + name + "=?";
			connection = msh.getConnection();
			pst = connection.prepareStatement(sql);
			pst.setString(1, _id);
			pst.setString(2, _name);
			rs = pst.executeQuery();
			if (rs.next() && rs.getInt(1) == 1) {
				return true;
			}
			return false;
		} catch (Exception e) {
			throw e;
		} finally {
			msh.free(rs, connection, pst);
		}
	}

}
