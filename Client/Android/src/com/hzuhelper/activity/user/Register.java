package com.hzuhelper.activity.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.hzuhelper.R;
import com.hzuhelper.activity.BaseActivity;
import com.hzuhelper.config.StaticValues;
import com.hzuhelper.config.staticURL;
import com.hzuhelper.utils.ToastUtil;
import com.hzuhelper.utils.web.ResultObj;
import com.hzuhelper.utils.web.WebRequest;

public class Register extends BaseActivity{
  //private EditText       editType;
  private EditText       editPhone;
  private EditText       editCode;
  private EditText       editPassword1;
  private EditText       editPassword2;

  private ProgressDialog dialog;

  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_user_register);
    //editType = (EditText)findViewById(R.id.type);
    editPhone = (EditText)findViewById(R.id.phone);
    editCode = (EditText)findViewById(R.id.code);
    editPassword1 = (EditText)findViewById(R.id.password1);
    editPassword2 = (EditText)findViewById(R.id.password2);
  }

  public void typeClick(View v){

  }

  public void getCode(View v){
    if (TextUtils.isEmpty(editPhone.getText())) {
      ToastUtil.show("请完善信息");
      return;
    }
    String phone = editPhone.getText().toString();

    dialog = ProgressDialog.show(Register.this,null,"注册中...");
    WebRequest wq = new WebRequest(staticURL.getcode,WebRequest.METHOD_POST){
      @Override
      protected void onFinished(ResultObj resultObj){
        if (resultObj.getState()==ResultObj.STATE_SUCCESS) {
          startActivity(new Intent(Register.this,Login.class));
          Register.this.finish();
        } else {
          ToastUtil.show(resultObj.getErrorMsg());
        }
        dialog.dismiss();
      }
    };
    wq.setParam(StaticValues.phone,phone);
    wq.start();
  }

  public void commit(View v){
    if (TextUtils.isEmpty(editPhone.getText())||TextUtils.isEmpty(editCode.getText())||TextUtils.isEmpty(editPassword1.getText())||TextUtils.isEmpty(editPassword2.getText())) {
      ToastUtil.show("请完善信息");
      return;
    }
    String phone = editPhone.getText().toString();
    String code = editCode.getText().toString();
    String password1 = editPassword1.getText().toString();
    String password2 = editPassword2.getText().toString();
    if (!password1.equals(password2)) {
      ToastUtil.show("两次密码不一致");
      return;
    }

    dialog = ProgressDialog.show(Register.this,null,"注册中...");
    WebRequest wq = new WebRequest(staticURL.users_register,WebRequest.METHOD_POST){
      @Override
      protected void onFinished(ResultObj resultObj){
        if (resultObj.getState()==ResultObj.STATE_SUCCESS) {
          startActivity(new Intent(Register.this,Login.class));
          Register.this.finish();
        } else {
          ToastUtil.show(resultObj.getErrorMsg());
        }
        dialog.dismiss();
      }
    };
    wq.setParam(StaticValues.phone,phone);
    wq.setParam(StaticValues.code,code);
    wq.setParam(StaticValues.password,password1);
    wq.start();
  }
}
