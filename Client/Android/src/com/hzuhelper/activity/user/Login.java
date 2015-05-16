package com.hzuhelper.activity.user;

import java.util.HashMap;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.hzuhelper.AppStart;
import com.hzuhelper.R;
import com.hzuhelper.activity.BaseActivity;
import com.hzuhelper.tools.ConstantStrUtil;
import com.hzuhelper.tools.MD5Utils;
import com.hzuhelper.tools.StringUtils;

public class Login extends BaseActivity implements OnClickListener,Runnable{
  private ProgressDialog dialog;

  private EditText       email;
  private EditText       password;

  @Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user_login);

    password = (EditText)findViewById(R.id.password);
    email = (EditText)findViewById(R.id.phone);
  }

  @Override
  public void onClick(View v){
    switch (v.getId()) {
    case R.id.login:
      email = email.getText().toString();
      password = password.getText().toString();
      if (!StringUtils.isEmail(email)||StringUtils.isEmpty(email)||StringUtils.isEmpty(password)) {
        Toast.makeText(getApplicationContext(),"账号或密码不能为空",Toast.LENGTH_LONG).show();
      } else {
        // 服务器验证并登录
        dialog = ProgressDialog.show(this,null,"登录中...");
        new Thread(this).start();
      }
      break;
    case R.id.login_text:
      Intent intent = new Intent(Login.this,Register.class);
      startActivity(intent);
      break;
    }
  }

  @Override
  public void run(){
    verifyLogin(email,password);
  }

  private void verifyLogin(String email,String password){
    String IsLogin = null;
    password = MD5Utils.getMD5Str(password);
    String url = ConstantStrUtil.DOMAINNAME+"/users/login";
    try {
      HashMap<String,String> params = new HashMap<String,String>();
      params.put(ConstantStrUtil.USERNAME,email);
      params.put(ConstantStrUtil.PASSWORD,password);
      WebRequest fetchURL = new WebRequest(url);
      fetchURL.setParams(params);
      String strResult = fetchURL.post();
      if (strResult!=null&&!StringUtils.isEmpty(strResult)) {
        JSONObject jsonobject = new JSONObject(strResult);
        String result = jsonobject.get("islogin").toString();
        IsLogin = result;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    if ("false".equals(IsLogin)) {
      Looper.prepare();
      Toast.makeText(getApplicationContext(),"用户名或密码错误",Toast.LENGTH_LONG).show();
      dialog.dismiss();
      Looper.loop();
    } else if ("true".equals(IsLogin)) {
      Looper.prepare();
      Toast.makeText(getApplicationContext(),"登录成功",Toast.LENGTH_LONG).show();
      // 保存用户名和密码到本地
      SharedPreferences sharepreferences = getSharedPreferences(ConstantStrUtil.COMMON_XML_NAME,Context.MODE_PRIVATE);
      Editor editor = sharepreferences.edit();
      editor.putString(ConstantStrUtil.USERNAME,email);
      editor.putString(ConstantStrUtil.PASSWORD,password);
      editor.commit();
      Intent intent = new Intent(Login.this,AppStart.class);
      startActivity(intent);
      dialog.dismiss();
      Looper.loop();
    } else {
      Looper.prepare();
      Toast.makeText(getApplicationContext(),"系统登录失败",Toast.LENGTH_LONG).show();
      dialog.dismiss();
      Looper.loop();
    }
  }
}
