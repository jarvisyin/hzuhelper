package com.hzuhelper.activity.chat;

import java.util.HashMap;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hzuhelper.activity.BaseActivity;
import com.hzuhelper.api.WebRequest;
import com.hzuhelper.config.StaticData;
import com.hzuhelper.model.TakeoutRestaurantInfo;
import com.hzuhelper.tools.ConstantStrUtil;
import com.hzuhelper.R;

public class TweetCommit extends BaseActivity implements OnClickListener,
		TextWatcher {

	private EditText tv_content = null;
	private TextView tv_count = null;
	private Button btn_right;
	private ProgressDialog myDialog;
	private String content;
	private static int MAX_COUNT = 255;
	private long count;
	private long inputCount;
	private int command;
	private TakeoutRestaurantInfo takeout_restaurantInfo;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_tweet_commit);
		tv_content = (EditText) findViewById(R.id.tv_content);
		tv_count = (TextView) findViewById(R.id.tv_count);
		btn_right = (Button) findViewById(R.id.btn_right);

		command = getIntent().getIntExtra("command", -1);
		if (command == 1) {
			takeout_restaurantInfo = StaticData.getTakeout_restaurantInfo();
			MAX_COUNT = MAX_COUNT - takeout_restaurantInfo.getName().length()-2;
		}

		tv_content.addTextChangedListener(this);
		btn_right.setOnClickListener(this);
	}

	public void onClick(View v) {
		content = tv_content.getText().toString();
		if (content.trim().length() < 1) {
			Toast.makeText(TweetCommit.this, "内容不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		if (count < 0) {
			Toast.makeText(TweetCommit.this, "输入数字超出限制", Toast.LENGTH_SHORT)	.show();
			return;
		}
		myDialog = ProgressDialog.show(TweetCommit.this, null, "发布中...");
		new Thread(new Runnable() {
			public void run() {
				WebRequest wq = new WebRequest(ConstantStrUtil.DOMAINNAME+ConstantStrUtil.URL_PATH_CHAT_TWEET_COMMIT);
				HashMap<String, String> params = WebRequest.getUserInfo(TweetCommit.this);
				params.put("tagId", "0");
				if (command == 1) {
					content ="#"+takeout_restaurantInfo.getName()+"#"+content;
					params.put("tagId",String.valueOf(takeout_restaurantInfo.getTagId()));
				}
				params.put("content", content);

				wq.setParams(params);
				String result = wq.post();
				try {
					JSONObject jsonObject = new JSONObject(result);
					if (jsonObject.getString(ConstantStrUtil.STR_STATU).equals(
							ConstantStrUtil.STR_FALSE)) {
						handler.sendEmptyMessage(COMMIT_FAIL);
						return;
					}
				} catch (Exception ex) {
					handler.sendEmptyMessage(COMMIT_FAIL);
					return;
				}
				handler.sendEmptyMessage(COMMIT_SUCCESS);
			}
		}).start();
	}

	public void afterTextChanged(Editable s) {
		inputCount = tv_content.getText().toString().length();
		count = MAX_COUNT - inputCount;
		tv_count.setText(String.valueOf(count));
		if (count < 0)
			tv_count.setTextColor(getResources().getColor(R.color.colorFF5050));
		else
			tv_count.setTextColor(getResources().getColor(R.color.black));
	}

	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	private final int COMMIT_SUCCESS = 1;
	private final int COMMIT_FAIL = 2;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case COMMIT_SUCCESS:
				Toast.makeText(TweetCommit.this, "发布成功", Toast.LENGTH_SHORT).show();
				myDialog.dismiss();
				TweetCommit.this.finish();
				break;
			case COMMIT_FAIL:
				Toast.makeText(TweetCommit.this, "发布失败", Toast.LENGTH_SHORT).show();
				myDialog.dismiss();
				break;
			}
		}
	};
}
