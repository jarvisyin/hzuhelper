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
import com.hzuhelper.tools.ConstantStrUtil;
import com.hzuhelper.web.WebRequest;
import com.hzuhelper.R;

public class CommentCommit extends BaseActivity implements OnClickListener,
		TextWatcher {

	private EditText tv_content=null;
	private TextView tv_count=null;
	private Button btn_right;
	private ProgressDialog myDialog;

	private int MAX_COUNT=255;
	private long count;
	private long inputCount;
	private int tweetId;
	private int commentId;

	private String commentBeginningString = "回复 @匿名用户 :";

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_comment_commit);

		tweetId = getIntent().getIntExtra("tweetId", -1);
		commentId = getIntent().getIntExtra(ConstantStrUtil.COMMENT_ID, 0);
		if (commentId != -1)
			MAX_COUNT -= commentBeginningString.length();

		tv_content = (EditText) findViewById(R.id.tv_content);
		tv_count = (TextView) findViewById(R.id.tv_count);
		btn_right = (Button) findViewById(R.id.btn_right);

		tv_count.setText(String.valueOf(MAX_COUNT));
		tv_content.addTextChangedListener(this);
		btn_right.setOnClickListener(this);		
	}

	public void onClick(View v) {
		if (count < 0) {
			Toast.makeText(CommentCommit.this, "输入数字超出限制", Toast.LENGTH_SHORT).show();
			return;
		}
		if (tv_content.getText().toString().trim().length() < 1) {
			Toast.makeText(CommentCommit.this, "内容不能为空", Toast.LENGTH_SHORT).show();
			return;
		}
		myDialog = ProgressDialog.show(CommentCommit.this, null, "发布中....");
		new Thread(new Runnable() {
			public void run() {
				String url = ConstantStrUtil.DOMAINNAME+ConstantStrUtil.URL_PATH_CHAT_COMMENT_COMMIT;
				WebRequest wq = new WebRequest(url);
				HashMap<String, String> params = WebRequest.getUserInfo(CommentCommit.this);

				params.put("tweetId", String.valueOf(tweetId));
				params.put("commentId", String.valueOf(commentId));
				if (commentId != 0) {
					params.put("content",commentBeginningString+tv_content.getText().toString());
				} else {
					params.put("content",tv_content.getText().toString());
				}
				wq.setParams(params);
				String result = wq.post();
				if (result == null) {
					handler.sendEmptyMessage(COMMIT_FAIL);
					return;
				}
				try {
					JSONObject jsonObject = new JSONObject(result);
					if (jsonObject.getString(ConstantStrUtil.STR_STATU).equals(
							ConstantStrUtil.STR_FALSE)) {
						handler.sendEmptyMessage(COMMIT_FAIL);
						return;
					}
				} catch (Exception ex) {
					handler.sendEmptyMessage(COMMIT_FAIL);
					ex.printStackTrace();
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

	public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
	public void onTextChanged(CharSequence s, int start, int before, int count) {}

	private final int COMMIT_SUCCESS = 1;
	private final int COMMIT_FAIL = 2;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case COMMIT_SUCCESS:
				Toast.makeText(CommentCommit.this, "发布成功", Toast.LENGTH_SHORT)	.show();
				myDialog.dismiss();
				CommentCommit.this.finish();
				break;
			case COMMIT_FAIL:
				Toast.makeText(CommentCommit.this, "发布失败", Toast.LENGTH_SHORT)	.show();
				myDialog.dismiss();
				break;
			}
		}
	};
}
