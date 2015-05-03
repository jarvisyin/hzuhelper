package com.hzuhelper.activity.takeout;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.hzuhelper.R;
import com.hzuhelper.activity.BaseActivity;
import com.hzuhelper.activity.chat.TweetCommit;
import com.hzuhelper.adapter.TweetAdapter;
import com.hzuhelper.api.WebRequest;
import com.hzuhelper.model.ChatTweetInfo;
import com.hzuhelper.tools.ConstantStrUtil;
import com.hzuhelper.wedget.RefreshAbleListView;
import com.hzuhelper.wedget.RefreshAbleListView.OnGetMoreDateListener;
import com.hzuhelper.wedget.RefreshAbleListView.OnRefreshListener;

public class ResCommentList extends BaseActivity implements OnClickListener{

	private int tagId;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_takeout_res_comment_list);

		getTagId();
		initListView();
		listview.refresh();
		findViewById(R.id.btn_right).setOnClickListener(this);
	}

	private void getTagId() {
		tagId = getIntent().getIntExtra("tagId", -1);
		if (tagId == -1) {
			throw new RuntimeException("tagId is null");
		}
	}

	// 加载全部tweet
	private final int TWEET_ALL_REFRESH_FAIL = 10;
	private final int TWEET_ALL_GET_MORE_FAIL = 13;
	private final int TWEET_ALL_REFRESH = 11;
	private final int TWEET_ALL_GET_MORE_DATA = 12;
	private RefreshAbleListView listview;
	private ArrayList<ChatTweetInfo> list1;
	private ArrayList<ChatTweetInfo> list2;
	private TweetAdapter adapter;

	/**
	 * 初始化
	 */
	private void initListView() {
		list1 = new ArrayList<ChatTweetInfo>();
		list2 = null;
		adapter = new TweetAdapter(list1, ResCommentList.this);
		listview = (RefreshAbleListView) findViewById(R.id.listView);
		listview.setAdapter(adapter);
		listview.setonRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				refresh();
			}
		});
		listview.setonGetMoreDateListene(new OnGetMoreDateListener() {
			public void onGetMoreDate() {
				getMoreDate();
			}
		});
	}

	/**
	 * 刷新
	 */
	private void refresh() {
		new Thread(new Runnable() {
			public void run() {
				list2 = loadTweetAllData(ConstantStrUtil.DOMAINNAME
						+ ConstantStrUtil.URL_PATH_CHAT_TWEET_GETLIST);
				if (list2 == null) {
					handler.sendEmptyMessage(TWEET_ALL_REFRESH_FAIL);
					return;
				}
				handler.sendEmptyMessage(TWEET_ALL_REFRESH);
			}
		}).start();
	}

	/**
	 * 获取更多数据
	 */
	private void getMoreDate() {
		new Thread(new Runnable() {
			public void run() {
				int listSize = 0;
				if ((listSize = list1.size()) < 1)
					return;
				ChatTweetInfo model = list1.get(listSize - 1);
				list2 = loadTweetAllData(ConstantStrUtil.DOMAINNAME
						+ ConstantStrUtil.URL_PATH_CHAT_TWEET_GETLIST + "?id="
						+ model.getId());
				if (list2 == null) {
					handler.sendEmptyMessage(TWEET_ALL_GET_MORE_FAIL);
					return;
				}
				handler.sendEmptyMessage(TWEET_ALL_GET_MORE_DATA);
			}
		}).start();
	}

	/**
	 * 网络获取数据
	 */
	private ArrayList<ChatTweetInfo> loadTweetAllData(String url) {
		ArrayList<ChatTweetInfo> list = new ArrayList<ChatTweetInfo>(20);
		WebRequest wq = new WebRequest(url);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(ConstantStrUtil.STR_TYPE, "tag");
		params.put("tagId", String.valueOf(tagId));
		wq.setParams(params);
		String json = wq.post();
		if (json == null) {
			return null;
		}
		JSONObject jsonResult;
		try {
			jsonResult = new JSONObject(json);
			if (!jsonResult.getString(ConstantStrUtil.STR_STATU).equals(
					ConstantStrUtil.STR_TRUE)) {
				return null;
			}
			JSONArray jsonResponse = jsonResult
					.getJSONArray(ConstantStrUtil.STR_RESPONSE);
			for (int i = 0, length = jsonResponse.length(); i < length; i++) {
				JSONObject jo = jsonResponse.getJSONObject(i);
				ChatTweetInfo m = new ChatTweetInfo();
				m.setAuthor_id(jo.getString("authorId"));
				m.setCai(jo.getInt("cai"));
				m.setComment_count(jo.getInt("commentCount"));
				m.setContent(jo.getString("content"));
				m.setDing(jo.getInt("ding"));
				m.setId(jo.getInt("id"));
				m.setPublish_date(jo.getString("publishDatetime"));
				m.setStatu(jo.getInt("statu"));
				m.setTag_id(jo.getInt("tagId"));
				list.add(m);
			}
			return list;
		} catch (JSONException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case TWEET_ALL_REFRESH_FAIL:
				Toast.makeText(ResCommentList.this, "获取数据失败",
						Toast.LENGTH_SHORT).show();
				listview.onRefreshComplete();
				break;
			case TWEET_ALL_GET_MORE_FAIL:
				Toast.makeText(ResCommentList.this, "获取数据失败",
						Toast.LENGTH_SHORT).show();
				listview.onGetMoreDateComplete();
				break;
			case TWEET_ALL_REFRESH:
				list1.clear();
				list1.addAll(list2);
				if (list2.size() < 20) {
					listview.setIsGetMoreDataable(false);
				} else {
					listview.setIsGetMoreDataable(true);
				}
				list2 = null;
				adapter.notifyDataSetChanged();
				listview.onRefreshComplete();
				break;
			case TWEET_ALL_GET_MORE_DATA:
				list1.addAll(list2);
				if (list2.size() < 20) {
					listview.setIsGetMoreDataable(false);
				} else {
					listview.setIsGetMoreDataable(true);
				}
				list2 = null;
				adapter.notifyDataSetChanged();
				listview.onGetMoreDateComplete();
				break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		Intent intent = new Intent(ResCommentList.this,
				TweetCommit.class);
		startActivity(intent);
	}

}
