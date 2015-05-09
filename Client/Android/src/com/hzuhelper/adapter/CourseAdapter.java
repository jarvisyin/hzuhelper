package com.hzuhelper.adapter;

import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hzuhelper.R;
import com.hzuhelper.activity.course.Single;
import com.hzuhelper.activity.course.Table;
import com.hzuhelper.config.StaticData;
import com.hzuhelper.database.CourseDB;
import com.hzuhelper.model.CourseInfo;

public class CourseAdapter extends BaseAdapter implements OnClickListener,android.content.DialogInterface.OnClickListener {

	private List<CourseInfo> cList;
	private Context context;
	private int courseid;

	public CourseAdapter(Context context, List<CourseInfo> cList) {
		this.cList = cList;
		this.context = context;
	}

	public int getCount() {
		return cList.size();
	}

	@Override
	public CourseInfo getItem(int position) {
		return cList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return cList.get(position).getId();
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		ViewHolder viewHolder;
		if (view == null) {
			view = LayoutInflater.from(context).inflate(R.layout.list_item_course, null);
			viewHolder = new ViewHolder();
			viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_name);
			viewHolder.tv_teacher = (TextView) view.findViewById(R.id.tv_teacher);
			viewHolder.tv_time = (TextView) view.findViewById(R.id.tv_time);
			viewHolder.btn_choice = (Button) view.findViewById(R.id.btn_choice);
			viewHolder.lly_info = (LinearLayout) view.findViewById(R.id.lly_info);			
			view.setTag(viewHolder);
		} else
			viewHolder = (ViewHolder) view.getTag();

		CourseInfo cm = cList.get(position);
		viewHolder.tv_name.setText(cm.getName());
		viewHolder.tv_teacher.setText(cm.getTeacher());
		viewHolder.tv_time.setText(cm.getStarttime() + "-" + cm.getEndtime()
				+ "周  " + StaticData.dayTime.get(cm.getDaytime()) + " "
				+ cm.getCoursetime1() + "-"
				+ (cm.getCoursetime1() + cm.getCoursetime2() - 1) + "节 "
				+ cm.getSite());
		viewHolder.btn_choice.setTag(cm.getId());
		viewHolder.btn_choice.setOnClickListener(this);
		viewHolder.lly_info.setTag(cm.getId());
		viewHolder.lly_info.setOnClickListener(this);
		return view;
	}

	private final class ViewHolder {
		TextView tv_name;
		TextView tv_teacher;
		TextView tv_time;
		Button btn_choice;
		LinearLayout lly_info;
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lly_info:
			Intent i = new Intent(context, Single.class);
			i.putExtra("courseid", (Integer) (v.getTag()));// v.getTag()
			context.startActivity(i);
			break;
		case R.id.btn_choice:
			courseid = (Integer) (v.getTag());
			AlertDialog.Builder adb = new Builder(context);
			adb.setTitle("提示！");
			adb.setMessage("你选择该课程后，同一时段的其他课程将被删除，是否进行该操作");
			adb.setPositiveButton("确定", this);
			adb.setNegativeButton("取消", null);
			adb.create().show();
			break;
		}	
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		for (CourseInfo cm : cList) {
			if (cm.getId() != courseid)
				CourseDB.delete(cm.getId());
		}
		Toast.makeText(context, "操作成功！", Toast.LENGTH_LONG).show();
		Intent i = new Intent(context, Table.class);
		context.startActivity(i);
	}
}