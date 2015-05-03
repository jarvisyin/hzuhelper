package com.hzuhelper.Control;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.hzuhelper.ServletBase;
import com.hzuhelper.BLL.UserLoginBLL;
import com.hzuhelper.Control.DAL.CourseDAL;
import com.hzuhelper.Control.DAL.ScoreDAL;
import com.hzuhelper.Utility.ConstantStrUtil;

public class ClientInit extends ServletBase {
	private static final long serialVersionUID = 1L;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter out = resp.getWriter();

		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(ConstantStrUtil.STR_STATU, ConstantStrUtil.STR_TRUE);
		// 检查更新
		Integer client_versionCode = Integer.valueOf(req
				.getParameter(ConstantStrUtil.STR_VERSION_CODE));
		if (client_versionCode < getVersionCode()) {
			map.put(ConstantStrUtil.STR_APPDOWNLOADURL, getAppDownLoadURL());
			out.print(JSONObject.fromObject(map).toString());
			return;
		}
		map.put(ConstantStrUtil.STR_APPDOWNLOADURL, "null");

		// 验证登录
		UserLoginBLL ulBll = new UserLoginBLL();
		if (ulBll.Login(req.getParameter(ConstantStrUtil.USERNAME),
				req.getParameter(ConstantStrUtil.PASSWORD)) == false) {
			map.put("login", ulBll.getErrorMsg());
			out.print(JSONObject.fromObject(map).toString());
			return;
		}
		map.put("login", "true");

		// 回传课表信息
		int courseCount = Integer.parseInt(req.getParameter("courseCount"));
		if (courseCount < 1) {
			try {
				map.put("courseData",
						new CourseDAL().getList(ulBll.getUserInfo().getEmail()));
			} catch (Exception ex) {
				map.put(ConstantStrUtil.STR_STATU, ConstantStrUtil.STR_FALSE);
				map.put(ConstantStrUtil.STR_ERRMSG, ex.toString());
				out.print(JSONObject.fromObject(map).toString());
				return;
			}
		} else
			map.put("courseData", "null");

		// 回传成绩信息
		try {
			Date server_score_change_Time = getScoreChangeTime();
			Date client_score_change_Time = sdf.parse(req
					.getParameter("scoreUpdateTime"));
			if (Integer.parseInt(req.getParameter("scoreCount")) < 1
					|| client_score_change_Time
							.before(server_score_change_Time)) {
				map.put("scoreData", new ScoreDAL().getList(new Long(ulBll
						.getUserInfo().getStudentId())));
				map.put("scoreMsg", getScore_Msg());
			} else {
				map.put("scoreData", "null");
				map.put("scoreMsg", "null");
			}
		} catch (Exception e) {
			map.put(ConstantStrUtil.STR_STATU, ConstantStrUtil.STR_FALSE);
			map.put(ConstantStrUtil.STR_ERRMSG, e.toString());
			out.print(JSONObject.fromObject(map).toString());
			return;
		}

		// 回传开学时间
		map.put(ConstantStrUtil.TERM_START_DATE, getTermStartTime());

		// 完成收工
		map.put(ConstantStrUtil.STR_STATU, ConstantStrUtil.STR_TRUE);
		out.print(JSONObject.fromObject(map).toString());
	}

	private String getScore_Msg() {
		return "1.此处所查看成绩并非绝对准确，一切以教务系统所查成绩为准 \n2.现在已经过了成绩录入截止时间，成绩已经无法修改，请各位不要打扰老师，谢谢！";
	}

	private String getAppDownLoadURL() {
		return "http://hzuhelper-huzhelper.stor.sinaapp.com/hzuhelper.apk";
	}

	private int getVersionCode() {
		return 4;
	}

	private String getTermStartTime() {
		return "2012-09-02 00:00:00";
	}

	private Date getScoreChangeTime() {
		try {
			return sdf.parse("2014-1-20 21:52:00");
		} catch (ParseException e) {
			return new Date();
		}
	}
}
