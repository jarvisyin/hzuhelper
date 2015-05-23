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
import com.hzuhelper.tools.ToastUtil;
import com.hzuhelper.web.ResultObj;
import com.hzuhelper.web.WebRequest;

<<<<<<< HEAD
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
=======
public class Register extends BaseActivity implements OnClickListener {
    private EditText       editEmail;
    private EditText       editName;
    private EditText       editStudentId;
    private EditText       editPassword;
    private EditText       editRepassword;
    private String         email;
    private String         truename;
    private String         studentId;
    private String         password;
    private String         repassword;

    private String         errorMsg;

    private UserInfo       userinfo = new UserInfo();
    private ProgressDialog dialog;
    private WebRequest     fetchURL;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        editEmail = (EditText)findViewById(R.id.LoginEmail);
        editName = (EditText)findViewById(R.id.LoginName);
        editStudentId = (EditText)findViewById(R.id.LoginStudentID);
        editPassword = (EditText)findViewById(R.id.LoginPassword);
        editRepassword = (EditText)findViewById(R.id.LoginRepassword);

        findViewById(R.id.LoginComfirm).setOnClickListener(this);
    }

    protected final int REGISTER_SUCCESS = 0;
    protected final int WEBREQUEST_FAIL  = 1;
    protected final int REGISTER_FAIL    = 2;
    protected final int UNKNOW_ERROR     = 3;
    private Handler     mHandler         = new Handler() {
                                             public void handleMessage(Message msg){
                                                 if (dialog!=null) {
                                                     dialog.dismiss();
                                                 }
                                                 switch (msg.what) {
                                                 case REGISTER_SUCCESS:
                                                     Toast.makeText(getApplicationContext(),"注册成功!",Toast.LENGTH_SHORT).show();
                                                     Intent intent = new Intent(Register.this,Login.class);
                                                     startActivity(intent);
                                                     Register.this.finish();
                                                     break;
                                                 case WEBREQUEST_FAIL:
                                                     Toast.makeText(getApplicationContext(),"网络连接失败!",Toast.LENGTH_LONG).show();
                                                     break;
                                                 case UNKNOW_ERROR:
                                                     Toast.makeText(getApplicationContext(),"错误!注册失败!",Toast.LENGTH_LONG).show();
                                                     break;
                                                 case REGISTER_FAIL:
                                                     Toast.makeText(getApplicationContext(),errorMsg,Toast.LENGTH_LONG).show();
                                                     break;
                                                 }
                                             }
                                         };

    private void fuck(){
        //打开注册页面
        RegisterPage registerPage = new RegisterPage();
        registerPage.setRegisterCallback(new EventHandler() {
            public void afterEvent(int event,int result,Object data){
                // 解析注册结果
                if (result==SMSSDK.RESULT_COMPLETE) {
                    @SuppressWarnings("unchecked")
                    HashMap<String,Object> phoneMap = (HashMap<String,Object>)data;
                    String country = (String)phoneMap.get("country");
                    String phone = (String)phoneMap.get("phone");

                    // 提交用户信息
                    registerUser(country,phone);
                }
            }
        });
        registerPage.show(context);
    }

    @Override
    public void onClick(View v){
        email = editEmail.getText().toString();
        truename = editName.getText().toString();
        studentId = editStudentId.getText().toString();
        password = editPassword.getText().toString();
        repassword = editRepassword.getText().toString();

        if (StringUtils.isEmpty(truename)||StringUtils.isEmpty(studentId)||StringUtils.isEmpty(repassword)||StringUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),"请完善信息",Toast.LENGTH_LONG).show();
            return;
        }
        if (!password.equals(repassword)) {
            Toast.makeText(getApplicationContext(),"两次密码不一致",Toast.LENGTH_LONG).show();
            return;
        }
        if (!StringUtils.isEmail(email)) {
            Toast.makeText(getApplicationContext(),"请输入正确的邮箱格式",Toast.LENGTH_LONG).show();
            return;
        }
        // 和服务器的数据验证并保存数据
        userinfo.setEmail(email);
        userinfo.setTruename(truename);
        userinfo.setStudentId(studentId);
        userinfo.setPassword(MD5Utils.getMD5Str(password).toString());

        dialog = ProgressDialog.show(Register.this,null,"注册中...");
        new Thread(new Runnable() {
            public void run(){
                String url = ConstantStrUtil.DOMAINNAME+"/users/register";
                HashMap<String,String> params = new HashMap<String,String>();
                params.put("email",userinfo.getEmail());
                params.put("truename",userinfo.getTruename());
                params.put("studentId",userinfo.getStudentId());
                params.put("password",userinfo.getPassword());
                fetchURL = new WebRequest(url);
                fetchURL.setParams(params);
                String strResult = fetchURL.post();
                if (strResult==null||strResult.equals("")) {
                    mHandler.sendEmptyMessage(WEBREQUEST_FAIL);
                    return;
                }
                try {
                    JSONObject jsonobject = new JSONObject(strResult);
                    if (jsonobject.getString(ConstantStrUtil.STR_STATU).equals(ConstantStrUtil.STR_FALSE)) {
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
>>>>>>> b3a3867b86ded446fea022ae63b1ce587c34157d
}
