package com.hzuhelper.activity.course;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hzuhelper.R;
import com.hzuhelper.activity.BaseActivity;
import com.hzuhelper.config.StaticData;
import com.hzuhelper.database.CourseDB;
import com.hzuhelper.model.CourseInfo;
import com.hzuhelper.wedget.CourseDatetimePicker;
import com.hzuhelper.wedget.CourseDatetimePicker.Init;

public class Edit extends BaseActivity implements OnClickListener{

	private Button btn_right;
	private Button btn_left;
	private EditText name;
	private EditText site;
	private EditText teacher;
	private TextView time1;
	private TextView time2;
	private CourseInfo model;
	private CourseDatetimePicker time2Picker;
	private CourseDatetimePicker time1Picker;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course_edit);

		// findview
		site = (EditText) findViewById(R.id.Site);
		name = (EditText) findViewById(R.id.Name);
		teacher = (EditText) findViewById(R.id.Teacher);
		time1 = (TextView) findViewById(R.id.Time);
		time2 = (TextView) findViewById(R.id.Termtime);
		btn_right = (Button) findViewById(R.id.btn_right);
		btn_left = (Button) findViewById(R.id.btn_left);
		btn_left.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

		// get intent
		int id = Integer.valueOf(getIntent().getIntExtra("courseid", -1));

		// get courseModel By id in the database
		model = CourseDB.getModel(id);
		model.setWeektime(model.getWeektime() + 1);

		// set EditText and ViewText
		site.setText(model.getSite());
		name.setText(model.getName());
		teacher.setText(model.getTeacher());

		initTime1();
		initTime2();
		// 保存修改后的数据
		btn_right.setOnClickListener(this);
	}

	private void initTime1() {
		time1Picker = new CourseDatetimePicker(Edit.this,
				"选择上课时间", StaticData.dayTime, model.getDaytime(),
				StaticData.courseTime, model.getCoursetime1(),
				StaticData.courseTime, model.getCoursetime2());
		time1Picker.setIntit(new Init() {
			public void setText() {
				time1.setText(StaticData.dayTime.get(time1Picker
						.getData1PositionKey())
						+ " "
						+ time1Picker.getData2PositionKey()
						+ "-"
						+ time1Picker.getData3PositionKey() + "节课");
			}

			public void onSubmit() {
				model.setDaytime(time1Picker.getData1PositionKey());
				model.setCoursetime1(time1Picker.getData2PositionKey());
				model.setCoursetime2(time1Picker.getData3PositionKey());
			}
		});
		time1Picker.setText();
		time1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				time1Picker.show();
			}
		});
	}

	private void initTime2() {
		time2Picker = new CourseDatetimePicker(Edit.this,
				"选择上课时间", StaticData.weekTime, model.getWeektime(),
				StaticData.termTime, model.getStarttime(), StaticData.termTime,
				model.getEndtime());

		time2Picker.setIntit(new Init() {
			public void setText() {
				time2.setText(StaticData.weekTime.get(time2Picker
						.getData1PositionKey())
						+ " "
						+ time2Picker.getData2PositionKey()
						+ "-"
						+ time2Picker.getData3PositionKey() + "周");
			}

			public void onSubmit() {
				model.setWeektime(time2Picker.getData1PositionKey());
				model.setStarttime(time2Picker.getData2PositionKey());
				model.setEndtime(time2Picker.getData3PositionKey());
			}
		});
		time2Picker.setText();

		time2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				time2Picker.show();
			}
		});
	}

	
	@Override
	public void onClick(View v) {
		if (name.getText().toString().trim().equals("")) {
			Toast.makeText(Edit.this, "课程名不能为空！",Toast.LENGTH_SHORT).show();
			return;
		}
		model.setSite(site.getText().toString());
		model.setName(name.getText().toString());
		model.setTeacher(teacher.getText().toString());
		model.setWeektime(model.getWeektime() - 1);
		model.setStatu(5);
		CourseDB.update(model);
		Toast.makeText(Edit.this, "保存成功",
				Toast.LENGTH_SHORT).show();
		Intent i = new Intent(Edit.this,
				Table.class);
		startActivity(i);
		finish();
	}
}
