package com.hzuhelper.activity.takeout;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hzuhelper.R;
import com.hzuhelper.activity.BaseActivity;
import com.hzuhelper.activity.chat.TweetCommit;
import com.hzuhelper.adapter.MenuAdapter;
import com.hzuhelper.config.StaticData;
import com.hzuhelper.model.TakeoutMenuItemInfo;
import com.hzuhelper.model.TakeoutRestaurantInfo;
import com.hzuhelper.tools.ConstantStrUtil;
import com.hzuhelper.web.WebRequest;

public class MenuList extends BaseActivity {

	private ProgressDialog progressDialog;
	private TakeoutRestaurantInfo TakeoutRestaurantInfo;
	protected ArrayList<TakeoutMenuItemInfo> list;
	private ListView listView;
	private Builder phoneCallADB;
	private Builder commentADB;
	private String[] phones;
	protected Button btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_takeout_menu_list);
		progressDialog = ProgressDialog.show(MenuList.this,
				null, "获取中...");
		topbarInit();
		listViewInit();
		phoneCallInit();
		commentInit();
		dingInit();
		caiInit();
	}

	private void caiInit() {
		Button btnCai = (Button) findViewById(R.id.cai);
		if (TakeoutRestaurantInfo.getCai() > 0)
			btnCai.setText(String.valueOf(TakeoutRestaurantInfo.getCai()));
		else
			btnCai.setText("踩");
		btnCai.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				btn = (Button) v;
				btn.setEnabled(false);
				TakeoutRestaurantInfo.setCai(TakeoutRestaurantInfo.getCai() + 1);
				btn.setText(String.valueOf(TakeoutRestaurantInfo.getCai()));
				new Thread(new Runnable() {
					public void run() {
						try {
							commentCommit("cai");
						} catch (Exception e) {
							errorMsg = e.getMessage();
							handler.sendEmptyMessage(CAI_FAIL);
						}
					}
				}).start();
			}
		});
	}

	private void dingInit() {
		Button btnDing = (Button) findViewById(R.id.ding);
		if (TakeoutRestaurantInfo.getDing() > 0)
			btnDing.setText(String.valueOf(TakeoutRestaurantInfo.getDing()));
		else
			btnDing.setText("赞");
		btnDing.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				btn = (Button) v;
				btn.setEnabled(false);
				TakeoutRestaurantInfo.setDing(TakeoutRestaurantInfo.getDing() + 1);
				btn.setText(String.valueOf(TakeoutRestaurantInfo.getDing()));
				new Thread(new Runnable() {
					public void run() {
						try {
							commentCommit("ding");
						} catch (Exception e) {
							errorMsg = e.getMessage();
							handler.sendEmptyMessage(DING_FAIL);
						}
					}
				}).start();
			}
		});
	}

	private void commentCommit(String type) throws Exception {
		String url = ConstantStrUtil.DOMAINNAME
				+ "/takeout/restaurant/comment/commit";
		WebRequest wq = new WebRequest(url);
		HashMap<String, String> params = WebRequest.getUserInfo(this);
		params.put("type", type);
		params.put("restaurantId",
				String.valueOf(TakeoutRestaurantInfo.getId()));
		wq.setParams(params);
		String result = wq.post();
		if (result == null)
			throw new Exception("网络连接失败");
		JSONObject json = new JSONObject(result);
		if (json.getString("statu").equals("false"))
			throw new Exception(json.getString("errorMsg"));
	}

	private void commentInit() {
		commentADB = new AlertDialog.Builder(this);
		String[] items = { "我要评论", "阅读评论" };
		commentADB.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				try {
					if (item == 0) {
						Intent i = new Intent(MenuList.this,
								TweetCommit.class);
						i.putExtra("command", StaticData.RESTAURANT_COMMENT);
						StaticData
								.setTakeout_restaurantInfo(TakeoutRestaurantInfo);
						startActivity(i);
					} else {
						Intent i = new Intent(MenuList.this,
								ResCommentList.class);
						i.putExtra("tagId", TakeoutRestaurantInfo.getTagId());
						startActivity(i);
					}
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(MenuList.this,
							e.toString() + TakeoutRestaurantInfo.getTagId(),
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		Button comment = (Button) findViewById(R.id.comment);
		if (TakeoutRestaurantInfo.getCommentNum() < 1) {
			comment.setText("评论");
		} else {
			comment.setText(String.valueOf(TakeoutRestaurantInfo
					.getCommentNum()));
		}
		comment.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				commentADB.show();
			}
		});
	}

	private void phoneCallInit() {
		phoneCallADB = new AlertDialog.Builder(this);
		phoneCallADB.setTitle("拨打:");
		phones = TakeoutRestaurantInfo.getPhone().split(",");
		phoneCallADB.setItems(phones, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				try {
					Intent phoneIntent = new Intent(
							"android.intent.action.CALL", Uri.parse("tel:"
									+ phones[item]));
					startActivity(phoneIntent);
				} catch (Exception e) {
					Toast.makeText(MenuList.this, "拨打失败",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		Button phone = (Button) findViewById(R.id.phone);
		phone.setText(TakeoutRestaurantInfo.getPhone());
		phone.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				phoneCallADB.show();
			}
		});
	}

	private void topbarInit() {
		TextView tv_center = (TextView) findViewById(R.id.tv_center);
		TakeoutRestaurantInfo = StaticData.getTakeout_restaurantInfo();
		tv_center.setText(TakeoutRestaurantInfo.getName());
	}

	private void listViewInit() {
		listView = (ListView) findViewById(R.id.listView);
		new Thread(new Runnable() {
			public void run() {
				list = loadData();
				if (list == null) {
					handler.sendEmptyMessage(LOAD_FAIL);
					return;
				}
				handler.sendEmptyMessage(LOAD_SUCCESS);
			}
		}).start();
	}

	private String errorMsg;
	private final byte LOAD_SUCCESS = 0;
	private final byte LOAD_FAIL = 1;
	private final byte DING_FAIL = 3;
	private final byte CAI_FAIL = 4;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LOAD_FAIL:
				Toast.makeText(MenuList.this, "获取数据失败",Toast.LENGTH_SHORT).show();
				progressDialog.dismiss();
				break;
			case LOAD_SUCCESS:
				MenuAdapter adapter = new MenuAdapter(MenuList.this,list);
				listView.setAdapter(adapter);
				progressDialog.dismiss();
				break;
			case DING_FAIL:
				Toast.makeText(MenuList.this, errorMsg,Toast.LENGTH_SHORT).show();
				TakeoutRestaurantInfo.setDing(TakeoutRestaurantInfo.getDing() - 1);
				btn.setText(String.valueOf(TakeoutRestaurantInfo.getDing()));
				break;
			case CAI_FAIL:
				Toast.makeText(MenuList.this, errorMsg,Toast.LENGTH_SHORT).show();
				TakeoutRestaurantInfo.setCai(TakeoutRestaurantInfo.getCai() - 1);
				btn.setText(String.valueOf(TakeoutRestaurantInfo.getCai()));
				break;
			}
		};
	};

	private ArrayList<TakeoutMenuItemInfo> loadData() {
		WebRequest wq = new WebRequest(ConstantStrUtil.DOMAINNAME+"/menu/itemgetlist?restaurantId="+TakeoutRestaurantInfo.getId());
		String result = wq.get();
		if (result == null)
			return null;
		try {
			JSONObject json = new JSONObject(result);
			if (json.getString("statu").equals("false"))
				return null;
			JSONArray array = json.getJSONArray("response");
			ArrayList<TakeoutMenuItemInfo> list = new ArrayList<TakeoutMenuItemInfo>();
			for (int i = 0, len = array.length(); i < len; i++) {
				JSONObject a = array.getJSONObject(i);
				TakeoutMenuItemInfo model = new TakeoutMenuItemInfo();
				model.setId(a.getInt("id"));
				model.setIntro(a.getString("intro"));
				model.setName(a.getString("name"));
				model.setPrice(Float.parseFloat(a.getString("price")));
				list.add(model);
			}
			return list;
		} catch (JSONException ex) {
			Log.i("jarvisyin", ex.toString());
			return null;
		}
	}
}
