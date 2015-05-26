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
import com.hzuhelper.model.receive.P6004;
import com.hzuhelper.wedget.CourseDatetimePicker;
import com.hzuhelper.wedget.CourseDatetimePicker.Init;

public class Add extends BaseActivity implements OnClickListener {
	private Button btnAdd;
	private Button btnBack;
	private EditText name;
	private EditText site;
	private EditText teacher;
	private TextView time1;
	private TextView time2;
	private P6004 model;

	private CourseDatetimePicker time2Picker;
	private CourseDatetimePicker time1Picker;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_course_add);

		site = (EditText) findViewById(R.id.Site);
		name = (EditText) findViewById(R.id.Name);
		teacher = (EditText) findViewById(R.id.Teacher);
		time1 = (TextView) findViewById(R.id.Time);
		time2 = (TextView) findViewById(R.id.Termtime);
		btnBack = (Button) findViewById(R.id.btn_left);

		btnBack.setOnClickListener(this);

		int row = getIntent().getIntExtra("row", -1);
		int col = getIntent().getIntExtra("col", -1);
		model = new P6004();
		model.setDaytime(col);
		model.setCoursetime1(row);
		model.setCoursetime2(1);
		model.setWeektime(3);
		model.setStarttime(1);
		model.setEndtime(20);

		initTime1();
		initTime2();

		btnAdd = (Button) findViewById(R.id.btn_right);
		btnAdd.setOnClickListener(this);
	}

	private void initTime1() {
		time1Picker = new CourseDatetimePicker(Add.this,
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
		time2Picker = new CourseDatetimePicker(Add.this,
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
		switch (v.getId()) {
		case R.id.btn_left:
			this.finish();
			break;
			
		case R.id.btn_right:
			if (name.getText().toString().trim().equals("")) {
				Toast.makeText(Add.this, "课程名不能为空！",
						Toast.LENGTH_SHORT).show();
				return;
			}
			model.setSite(site.getText().toString());
			model.setName(name.getText().toString());
			model.setTeacher(teacher.getText().toString());
			model.setWeektime(model.getWeektime() - 1);
			model.setStatu(5);
			CourseDB.save(model);
			Intent i = new Intent(Add.this,
					Table.class);
			startActivity(i);
			Add.this.finish();
			break;
		}		
	}
}
