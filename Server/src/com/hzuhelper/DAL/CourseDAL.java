package com.hzuhelper.DAL;

import java.sql.*;
import java.util.ArrayList;

import com.hzuhelper.Model.*;
import com.hzuhelper.Utility.*;

public class CourseDAL {

	public ArrayList<CourseInfo> getList(String username) throws SQLException {
		ArrayList<CourseInfo> clist = null;
		ResultSet rs = null;
		PreparedStatement pst = null;
		Connection connection = null;
		MySqlHelper msh = MySqlHelper.getInstance();
		try {
			String sqlText = "select a.weekTime,a.daytime,a.courseTime1,a.courseTime2,a.name,a.teacher,a.site,a.statu,a.id,a.endTime,a.startTime from Course as a,User as b where a.studentid=b.studentId and b.email=?";
			// String sqlText = "select * from course where studentid=?";
			connection = msh.getConnection();
			pst = connection.prepareStatement(sqlText);
			pst.setString(1, username);
			rs = pst.executeQuery();
			clist = new ArrayList<CourseInfo>();
			while (rs.next()) {
				CourseInfo model = new CourseInfo();
				model.setWeekTime(rs.getInt("weekTime"));
				model.setDaytime(rs.getInt("daytime"));
				model.setCourseTime1(rs.getInt("courseTime1"));
				model.setCourseTime2(rs.getInt("CourseTime2"));
				model.setName(rs.getString("name"));
				model.setTeacher(rs.getString("teacher"));
				model.setSite(rs.getString("site"));
				model.setStatu(rs.getInt("statu"));
				model.setId(rs.getInt("id"));
				model.setEndTime(rs.getInt("endTime"));
				model.setStartTime(rs.getInt("startTime"));
				clist.add(model);
			}
			return clist;
		} catch (SQLException e) {
			throw e;
		} finally {
			msh.free(rs, connection, pst);
		}
	}

}
