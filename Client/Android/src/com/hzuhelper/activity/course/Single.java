package com.hzuhelper.activity.course;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.hzuhelper.R;
import com.hzuhelper.activity.BaseActivity;
import com.hzuhelper.config.StaticData;
import com.hzuhelper.database.table.CourseDB;
import com.hzuhelper.model.receive.P6004;

public class Single extends BaseActivity implements OnClickListener{
	private TextView name;
	private TextView site;
	private TextView teacher;
	private TextView time;
	private TextView termtime;
	private P6004 model;
	private Builder adb;
	private ProgressDialog mydialog;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coures_show);
		site = (TextView) findViewById(R.id.Site);
		name = (TextView) findViewById(R.id.name);
		teacher = (TextView) findViewById(R.id.Teacher);
		time = (TextView) findViewById(R.id.Time);
		termtime = (TextView) findViewById(R.id.Termtime);

		int courseid = Integer.valueOf(getIntent().getIntExtra("courseid", -1));

		model = CourseDB.getModel(courseid);

		site.setText(model.getSite());
		name.setText(model.getName());
		teacher.setText(model.getTeacher());
		time.setText(StaticData.dayTime.get(model.getDaytime() + "  "
				+ model.getCoursetime1() + "-"
				+ (model.getCoursetime1() + model.getCoursetime2() - 1) + "节课"));
		termtime.setText(StaticData.weekTime.get(model.getWeektime() + 1)
				+ "  " + model.getStarttime() + "-" + model.getEndtime() + "周");

		findViewById(R.id.btn_right).setOnClickListener(this);
		findViewById(R.id.btn_delete).setOnClickListener(this);

		mydialog = new ProgressDialog(this);
		mydialog.setMessage("删除中...");

		adb = new AlertDialog.Builder(Single.this);
		adb.setTitle("提示！");
		adb.setMessage("确定删除吗？");
		adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				CourseDB.delete(model.getId());
				Intent i = new Intent(Single.this,
						Table.class);
				startActivity(i);
				finish();
			}
		});
		adb.setNegativeButton("取消", null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_right:
			Intent i = new Intent(Single.this,Edit.class);
			i.putExtra("courseid", model.getId());
			startActivity(i);
			break;
		case R.id.btn_delete:			
			adb.show();
			break;
		}		
	}
}
