package com.hzuhelper.activity.user;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hzuhelper.AppStart;
import com.hzuhelper.R;
import com.hzuhelper.activity.BaseActivity;
import com.hzuhelper.config.StaticValues;
import com.hzuhelper.tools.ConstantStrUtil;
import com.hzuhelper.tools.StringUtils;
import com.hzuhelper.tools.ToastUtil;
import com.hzuhelper.web.ResultObj;
import com.hzuhelper.web.WebRequest;
import com.hzuhelper.web.WebRequest.RequestFinishedListener;

public class Login extends BaseActivity {

    public final static int PHONE_TYPE_CHOICE = 1;
    private ProgressDialog  dialog;
    private EditText        phone;
    private EditText        password;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        password = (EditText)findViewById(R.id.password);
        phone = (EditText)findViewById(R.id.phone);
    }

    /**
     * 手机号码类型选择
     * @param view
     */
    public void typeClick(View view){
        startActivityForResult(new Intent(Login.this,Register.class),PHONE_TYPE_CHOICE);
    }

    /**
     * 登录点击事件
     * @param view
     */
    public void loginClick(View view){
        if (TextUtils.isEmpty(phone.getText())) {
            ToastUtil.show("请输入手机号码");
        } else if (TextUtils.isEmpty(password.getText())) {
            ToastUtil.show("请输入密码");
        } else {
            // 服务器验证并登录
            dialog = ProgressDialog.show(this,null,"登录中...");
            String url = ConstantStrUtil.DOMAINNAME+"/users/login";

            WebRequest webRequest = new WebRequest(url);
            webRequest.setParam(StaticValues.phone,phone.getText().toString());
            webRequest.setParam(StaticValues.password,password.getText().toString());
            webRequest.setMethod(WebRequest.METHOD_POST);
            webRequest.setRequestFinishedListener(new RequestFinishedListener() {

                @Override
                public void onFinished(ResultObj resultObj){
                    // TODO Auto-generated method stub

                }
            });
            webRequest.start();
            if (strResult!=null&&!StringUtils.isEmpty(strResult)) {
                JSONObject jsonobject = new JSONObject(strResult);
                String result = jsonobject.get("islogin").toString();
                IsLogin = result;
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

    /**
     * 注册点击事件
     * @param view
     */
    public void registerClick(View view){
        startActivity(new Intent(Login.this,Register.class));
    }

    /**
     * 忘记密码点击事件
     * @param view
     */
    public void forgetpwdClick(View view){
        startActivity(new Intent(Login.this,ResetPwd.class));
    }

}
