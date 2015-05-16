package com.hzuhelper.activity.user;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.hzuhelper.R;
import com.hzuhelper.activity.BaseActivity;
import com.hzuhelper.model.UserInfo;
import com.hzuhelper.tools.ConstantStrUtil;
import com.hzuhelper.tools.MD5Utils;
import com.hzuhelper.tools.StringUtils;
import com.hzuhelper.web.WebRequest;

public class Register extends BaseActivity implements OnClickListener {
	private EditText editEmail;
	private EditText editName;
	private EditText editStudentId;
	private EditText editPassword;
	private EditText editRepassword;
	private String email;
	private String truename;
	private String studentId;
	private String password;
	private String repassword;

	private String errorMsg;

	private UserInfo userinfo = new UserInfo();
	private ProgressDialog dialog;
	private WebRequest fetchURL;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_register);

		editEmail = (EditText) findViewById(R.id.LoginEmail);
		editName = (EditText) findViewById(R.id.LoginName);
		editStudentId = (EditText) findViewById(R.id.LoginStudentID);
		editPassword = (EditText) findViewById(R.id.LoginPassword);
		editRepassword = (EditText) findViewById(R.id.LoginRepassword);

		findViewById(R.id.LoginComfirm).setOnClickListener(this);
	}
	
	protected final int REGISTER_SUCCESS = 0;
	protected final int WEBREQUEST_FAIL = 1;
	protected final int REGISTER_FAIL = 2;
	protected final int UNKNOW_ERROR = 3;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (dialog != null) {
				dialog.dismiss();
			}
			switch (msg.what) {
			case REGISTER_SUCCESS:
				Toast.makeText(getApplicationContext(), "注册成功!",Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(Register.this, Login.class);
				startActivity(intent);
				Register.this.finish();
				break;
			case WEBREQUEST_FAIL:
				Toast.makeText(getApplicationContext(), "网络连接失败!",Toast.LENGTH_LONG).show();
				break;
			case UNKNOW_ERROR:
				Toast.makeText(getApplicationContext(), "错误!注册失败!",Toast.LENGTH_LONG).show();
				break;
			case REGISTER_FAIL:
				Toast.makeText(getApplicationContext(), errorMsg,Toast.LENGTH_LONG).show();
				break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		email = editEmail.getText().toString();
		truename = editName.getText().toString();
		studentId = editStudentId.getText().toString();
		password = editPassword.getText().toString();
		repassword = editRepassword.getText().toString();

		if (StringUtils.isEmpty(truename) || StringUtils.isEmpty(studentId)|| StringUtils.isEmpty(repassword) || StringUtils.isEmpty(password)) {
			Toast.makeText(getApplicationContext(), "请完善信息", Toast.LENGTH_LONG).show();
			return;
		}
		if (!password.equals(repassword)) {
			Toast.makeText(getApplicationContext(), "两次密码不一致",Toast.LENGTH_LONG).show();
			return;
		}
		if (!StringUtils.isEmail(email)) {
			Toast.makeText(getApplicationContext(), "请输入正确的邮箱格式",Toast.LENGTH_LONG).show();
			return;
		}
		// 和服务器的数据验证并保存数据
		userinfo.setEmail(email);
		userinfo.setTruename(truename);
		userinfo.setStudentId(studentId);
		userinfo.setPassword(MD5Utils.getMD5Str(password).toString());

		dialog = ProgressDialog.show(Register.this, null, "注册中...");
		new Thread(new Runnable() {
			public void run() {
				String url = ConstantStrUtil.DOMAINNAME + "/users/register";
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("email", userinfo.getEmail());
				params.put("truename", userinfo.getTruename());
				params.put("studentId", userinfo.getStudentId());
				params.put("password", userinfo.getPassword());
				fetchURL = new WebRequest(url);
				fetchURL.setParams(params);
				String strResult = fetchURL.post();
				if (strResult == null || strResult.equals("")) {
					mHandler.sendEmptyMessage(WEBREQUEST_FAIL);
					return;
				}
				try {
					JSONObject jsonobject = new JSONObject(strResult);
					if (jsonobject.getString(ConstantStrUtil.STR_STATU).equals(
							ConstantStrUtil.STR_FALSE)) {
						errorMsg = jsonobject.getString(ConstantStrUtil.STR_ERRMSG);
						mHandler.sendEmptyMessage(REGISTER_FAIL);
						return;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					errorMsg = e.toString();
					mHandler.sendEmptyMessage(REGISTER_FAIL);
					return;
				}
				mHandler.sendEmptyMessage(REGISTER_SUCCESS);
			}
		}).start();
	}
}
