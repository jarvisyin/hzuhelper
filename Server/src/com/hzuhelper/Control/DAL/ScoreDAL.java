package com.hzuhelper.Control.DAL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.hzuhelper.Domain.Score;
import com.hzuhelper.Utility.MySqlHelper;

public class ScoreDAL {

	public static final String TABLE_NAME = "Score";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_XN = "xn";
	public static final String COLUMN_XQ = "xq";
	public static final String COLUMN_KCMC = "kcmc";
	public static final String COLUMN_KCLX = "kclx";
	public static final String COLUMN_KHFS = "khfs";
	public static final String COLUMN_PSCJ = "pscj";
	public static final String COLUMN_QZCJ = "qzcj";
	public static final String COLUMN_SYCJ = "sycj";
	public static final String COLUMN_QMCJ = "qmcj";
	public static final String COLUMN_ZPCJ = "zpcj";
	public static final String COLUMN_JD = "jd";
	public static final String COLUMN_XF = "xf";
	public static final String COLUMN_CXCJ = "cxcj";

	public ArrayList<Score> getList(long xh) throws SQLException {
		ArrayList<Score> slist = null;
		ResultSet rs = null;
		PreparedStatement pst = null;
		Connection connection = null;
		MySqlHelper msh = MySqlHelper.getInstance();
		try {
			String sqlText = "select * from Score where xh=?";
			connection = msh.getConnection();
			pst = connection.prepareStatement(sqlText);
			pst.setLong(1, xh);
			rs = pst.executeQuery();
			slist = new ArrayList<Score>();
			while (rs.next()) {
				Score model = new Score();
				model.setXn(rs.getString(COLUMN_XN));
				model.setXq(rs.getString(COLUMN_XQ));
				model.setKcmc(rs.getString(COLUMN_KCMC));
				model.setKclx(rs.getString(COLUMN_KCLX));
				model.setKhfs(rs.getString(COLUMN_KHFS));
				model.setPscj(rs.getString(COLUMN_PSCJ));
				model.setQzcj(rs.getString(COLUMN_QZCJ));
				model.setSycj(rs.getString(COLUMN_SYCJ));
				model.setQmcj(rs.getString(COLUMN_QMCJ));
				model.setZpcj(rs.getString(COLUMN_ZPCJ));
				model.setJd(rs.getString(COLUMN_JD));
				model.setXf(rs.getString(COLUMN_XF));
				model.setCxcj(rs.getString(COLUMN_CXCJ));
				slist.add(model);
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			msh.free(rs, connection, pst);
		}
		return slist;
	}

}
