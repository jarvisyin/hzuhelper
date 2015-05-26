package com.hzuhelper.activity.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.hzuhelper.R;
import com.hzuhelper.activity.BaseActivity;
import com.hzuhelper.activity.chat.TweetList;
import com.hzuhelper.config.StaticValues;
import com.hzuhelper.config.staticURL;
import com.hzuhelper.utils.ToastUtil;
import com.hzuhelper.utils.web.ResultObj;
import com.hzuhelper.utils.web.WebRequest;

public class Login extends BaseActivity{

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
        startActivityForResult(new Intent(Login.this,PhoneTypeChoice.class),PHONE_TYPE_CHOICE);
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

            WebRequest webRequest = new WebRequest(staticURL.users_login,WebRequest.METHOD_POST){
                @Override
                protected void onFinished(ResultObj resultObj){
                    if (resultObj.getState()==ResultObj.STATE_SUCCESS) {
                        startActivity(new Intent(Login.this,TweetList.class));
                        Login.this.finish();
                    } else {
                        ToastUtil.show(resultObj.getErrorMsg());
                    }
                    dialog.dismiss();

                }
            };
            webRequest.setParam(StaticValues.phone,phone.getText().toString());
            webRequest.setParam(StaticValues.password,password.getText().toString());
            webRequest.start();
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
