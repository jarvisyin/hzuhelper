package com.hzuhelper.activity.course;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.hzuhelper.R;
import com.hzuhelper.activity.BaseActivity;
import com.hzuhelper.adapter.CourseAdapter;
import com.hzuhelper.database.table.CourseDB;
import com.hzuhelper.model.receive.P6004;

public class ConflictList extends BaseActivity implements OnClickListener{
	private ListView lv_listCourse;
	private List<P6004> cList;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course_list);
		findViewById(R.id.btn_left).setOnClickListener(this);
		int courseid = Integer.valueOf(getIntent().getIntExtra("courseid", -1));
		P6004 model = CourseDB.getModel(courseid);
		cList = CourseDB.getList("daytime=" + model.getDaytime()
				+ " and courseTime1>=" + model.getCoursetime1()
				+ " and courseTime1<="
				+ (model.getCoursetime1() + model.getCoursetime2() - 1));

		lv_listCourse = (ListView) findViewById(R.id.listView1);

		TextView tv = new TextView(this);
		tv.setPadding(16, 16, 16, 16);
		tv.setTextSize(15);
		tv.setBackgroundColor(getResources().getColor(R.color.colorF5FFF5));
		tv.setText("提示！\n    该时段出现重复课程，请在该列课程中选择其中一个，或者单击修改其他课程的上课时间");
		lv_listCourse.addHeaderView(tv);

		CourseAdapter adp = new CourseAdapter(this,cList);
		lv_listCourse.setAdapter(adp);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_left:
			Intent i = new Intent(ConflictList.this,Table.class);
			startActivity(i);
			break;

		default:
			break;
		}
	}
}
