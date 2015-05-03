package com.hzuhelper.Control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.hzuhelper.ServletBase;
import com.hzuhelper.BLL.CourseBLL;
import com.hzuhelper.Domain.Course;
import com.hzuhelper.Utility.ConstantStrUtil;

public class CourseGetList extends ServletBase {
	private static final long serialVersionUID = 2625234828883804336L;

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		HashMap<String, Object> map = new HashMap<String, Object>();

		CourseBLL cbll = new CourseBLL();
		ArrayList<Course> clist = cbll.getList(
				req.getParameter(ConstantStrUtil.USERNAME),
				req.getParameter(ConstantStrUtil.PASSWORD));

		if (clist == null) {
			map.put(ConstantStrUtil.STR_STATU, ConstantStrUtil.STR_FALSE);
			map.put(ConstantStrUtil.STR_ERRMSG, cbll.getErrorMsg());
		} else {
			map.put(ConstantStrUtil.STR_STATU, ConstantStrUtil.STR_TRUE);
			map.put(ConstantStrUtil.STR_RESPONSE, clist);
		}
		out.print(JSONObject.fromObject(map).toString());
	}
}
