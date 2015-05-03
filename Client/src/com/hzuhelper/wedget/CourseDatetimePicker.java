package com.hzuhelper.wedget;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hzuhelper.AppContext;
import com.hzuhelper.R;

public class CourseDatetimePicker {
	private Context context;
	private int height;
	private String title = null;
	private AlertDialog.Builder dialog;
	private Init init;
	private LinkedHashMap<Integer, String> data1 = null;
	private LinkedHashMap<Integer, String> data2 = null;
	private LinkedHashMap<Integer, String> data3 = null;

	private int data1PositionKey;
	private int data2PositionKey;
	private int data3PositionKey;

	private int data1Position;
	private int data2Position;
	private int data3Position;

	private ScrollView sol_Lln2;
	private ScrollView sol_Lln3;
	private ScrollView sol_Lln1;

	private LinearLayout lly_Lln1;
	private LinearLayout lly_Lln2;
	private LinearLayout lly_Lln3;

	public CourseDatetimePicker(Context context, String title,
			LinkedHashMap<Integer, String> data1, int data1PositionKey,
			LinkedHashMap<Integer, String> data2, int data2PositionKey,
			LinkedHashMap<Integer, String> data3, int data3PositionKey) {

		this.context = context;
		this.data1 = data1;
		this.data2 = data2;
		this.data3 = data3;

		this.data1PositionKey = data1PositionKey;
		this.data2PositionKey = data2PositionKey;
		this.data3PositionKey = data3PositionKey;

		height = AppContext.screenHeight/ 10;
		this.title = title;
	}

	public void show() {
		this.data1Position = getDataPositionByKey(data1PositionKey, data1);
		this.data2Position = getDataPositionByKey(data2PositionKey, data2);
		this.data3Position = getDataPositionByKey(data3PositionKey, data3);

		dialog = new AlertDialog.Builder(context);
		if (title != null)
			dialog.setTitle(title);

		View dialog_view = LayoutInflater.from(context).inflate(R.layout.course_chose_time_dialog, null);

		// 设置第一列的数据
		lly_Lln1 = (LinearLayout) dialog_view.findViewById(R.id.slv_daytime);
		setColumnView(lly_Lln1, data1);

		// 设置第二列的数据
		lly_Lln2 = (LinearLayout) dialog_view.findViewById(R.id.slv_courseTime1);
		setColumnView(lly_Lln2, data2);

		// 设置第三列的数据
		lly_Lln3 = (LinearLayout) dialog_view.findViewById(R.id.slv_courseTime2);
		setColumnView(lly_Lln3, data3);

		sol_Lln1 = (ScrollView) dialog_view.findViewById(R.id.sol_daytime);
		sol_Lln2 = (ScrollView) dialog_view.findViewById(R.id.sol_coursetime1);
		sol_Lln3 = (ScrollView) dialog_view.findViewById(R.id.sol_coursetime2);
		LayoutParams aa = sol_Lln1.getLayoutParams();
		aa.height = height * 4;
		sol_Lln1.setLayoutParams(aa);
		sol_Lln2.setLayoutParams(aa);
		sol_Lln3.setLayoutParams(aa);

		TextView tv_background = (TextView) dialog_view.findViewById(R.id.tv_background);
		LayoutParams bb = tv_background.getLayoutParams();
		bb.height = height;
		tv_background.setLayoutParams(bb);

		// 最后要对dialog添加中间内容
		dialog.setView(dialog_view);
		// 设置我们自己定义的布局文件作为弹出框的Content
		dialog.setPositiveButton("确定", dif_ok);
		dialog.setNegativeButton("取消", null);

		sol_Lln1.setOnTouchListener(sol_OnTouchListener);
		sol_Lln2.setOnTouchListener(sol_OnTouchListener);
		sol_Lln3.setOnTouchListener(sol_OnTouchListener);

		dialog.show();

		Handler handler = new Handler();
		handler.postDelayed(runnable, 200);
	}

	private Runnable runnable = new Runnable() {
		public void run() {
			sol_Lln1.smoothScrollTo(0, (int) (data1Position + 0.5) * height);
			sol_Lln2.smoothScrollTo(0, (int) (data2Position + 0.5) * height);
			sol_Lln3.smoothScrollTo(0, (int) (data3Position + 0.5) * height);
		}
	};

	private OnTouchListener sol_OnTouchListener = new OnTouchListener() {

		// 这里写真正的事件
		private void antionAfterStop(ScrollView myview) {
			int y = (int) myview.getScrollY();
			int z = y / height;
			if (y % height > height / 2)
				z += 1;
			if (myview == sol_Lln2) {
				if (z > data3Position) {
					data3Position = z;
					sol_Lln3.smoothScrollTo(0, z * height);
				}
				data2Position = z;
				sol_Lln2.smoothScrollTo(0, z * height);
			} else if (myview == sol_Lln3) {
				if (z < data2Position) {
					data2Position = z;
					sol_Lln2.smoothScrollTo(0, z * height);
				}
				data3Position = z;
				sol_Lln3.smoothScrollTo(0, z * height);
			} else if (myview == sol_Lln1) {
				data1Position = z;
				sol_Lln1.smoothScrollTo(0, z * height);
			}
		}

		private int lastY = 0;
		private int touchEventId = -9983761;

		public boolean onTouch(View v, MotionEvent event) {
			int eventAction = event.getAction();
			int y = (int) event.getRawY();
			switch (eventAction) {
			case MotionEvent.ACTION_UP:
				int touchY = 0;
				if (!(Math.abs(touchY - y) < 20)) {
					handler1.sendMessageDelayed(
							handler1.obtainMessage(touchEventId, v), 50);
				}
				break;
			}
			return false;
		}

		private Handler handler1 = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				View scroller = (View) msg.obj;
				if (msg.what == touchEventId) {
					if (lastY == scroller.getScrollY()) {
						antionAfterStop((ScrollView) scroller);
					} else {
						handler1.sendMessageDelayed(
								handler1.obtainMessage(touchEventId, scroller),
								200);
						lastY = scroller.getScrollY();
					}
				}
			}
		};
	};

	private void setColumnView(LinearLayout linearLayout,
			LinkedHashMap<Integer, String> map) {
		TextView tv1 = new TextView(context);
		tv1.setHeight((int) (height * 1.5));
		linearLayout.addView(tv1);
		Set<Integer> keys = map.keySet();
		Iterator<Integer> iterator = keys.iterator();
		while (iterator.hasNext()) {
			int key = iterator.next();
			TextView tv2 = new TextView(context);
			tv2.setText(map.get(key));
			tv2.setHeight(height);
			tv2.setTextSize(20);
			tv2.setGravity(Gravity.CENTER);
			tv2.setTextColor(Color.rgb(0, 0, 0));
			linearLayout.addView(tv2);
		}
		tv1 = new TextView(context);
		tv1.setHeight((int) (height * 1.5));
		linearLayout.addView(tv1);
	}

	private int getDataKeyByPosition(int position,LinkedHashMap<Integer, String> map) {
		Set<Integer> keys = map.keySet();
		Iterator<Integer> it = keys.iterator();
		for (int i = 0; i < position; i++) {
			it.next();
		}
		return it.next();
	}

	private int getDataPositionByKey(int key, LinkedHashMap<Integer, String> map) {
		int i = 0;
		Set<Integer> keys = map.keySet();
		Iterator<Integer> it = keys.iterator();
		while (it.hasNext()) {
			if (key == it.next())
				break;
			i++;
		}
		return i;
	}

	public void setText() {
		if (init != null) {
			init.setText();
		}
	}

	public void setIntit(Init intit) {
		this.init = intit;
	}

	private DialogInterface.OnClickListener dif_ok = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			data1PositionKey = getDataKeyByPosition(data1Position, data1);
			data2PositionKey = getDataKeyByPosition(data2Position, data2);
			data3PositionKey = getDataKeyByPosition(data3Position, data3);
			Log.i("jarvisyin", "data1PositionKey=" + data1PositionKey
					+ ";data2PositionKey=" + data2PositionKey
					+ ";data3PositionKey=" + data3PositionKey);
			if (init != null) {
				init.setText();
				init.onSubmit();
			}
		}
	};

	// 接口初始化
	public interface Init {
		public void setText();
		public void onSubmit();
	}

	// getting
	public int getData1PositionKey() {
		return data1PositionKey;
	}

	public int getData2PositionKey() {
		return data2PositionKey;
	}

	public int getData3PositionKey() {
		return data3PositionKey;
	}
}
