package com.hzuhelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

import com.hzuhelper.activity.AppUpdateService;
import com.hzuhelper.activity.BaseActivity;
import com.hzuhelper.activity.chat.TweetList;
import com.hzuhelper.activity.user.Login;
import com.hzuhelper.api.WebRequest;
import com.hzuhelper.database.CourseDB;
import com.hzuhelper.database.DAOHelper;
import com.hzuhelper.database.ScoreDB;
import com.hzuhelper.model.ScoreInfo;
import com.hzuhelper.server.CourseService;
import com.hzuhelper.tools.ConstantStrUtil;

public class AppStart extends BaseActivity{

  private String            username;
  private String            password;
  private String            score_updateTime;
  private String            result;
  private SharedPreferences sp;
  private int               courseCount;
  private Editor            ed;
  private Date              date               = new Date();
  // private int server_versionCode;
  private int               local_versionCode;
  // private Intent i;
  private TextView          tv_info;
  private String            tv_infoText;
  private String            appDownLoadURL;

  private final int         CHANGE_TV_INFOTEXT = 5;
  private String            errorMsg;

  @Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    sp = getSharedPreferences(ConstantStrUtil.COMMON_XML_NAME,Context.MODE_PRIVATE);
    ed = sp.edit();

    loadingStyle();
    initDB();
    initSimnulateServer();
  }

  private void loadingStyle(){
    tv_info = (TextView)findViewById(R.id.tv_info);
    new Thread(new Runnable(){
      public void run(){
        for (int i = 0; true; i++) {
          switch (i%5) {
          case 0:
            tv_infoText = "    Loading    ";
            break;
          case 1:
            tv_infoText = "    Loading.   ";
            break;
          case 2:
            tv_infoText = "    Loading..  ";
            break;
          case 3:
            tv_infoText = "    Loading... ";
            break;
          default:
            tv_infoText = "    Loading....";
            break;
          }
          handler.sendEmptyMessage(CHANGE_TV_INFOTEXT);
          try {
            Thread.sleep(500);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    }).start();
  }

  private void initDB(){
    List<String> createTableCommanders = new ArrayList<String>();
    createTableCommanders.add(CourseDB.createTableCommander());
    createTableCommanders.add(ScoreDB.createTableCommander());
    DAOHelper.createTableCommanders = createTableCommanders;
  }

  private void initSimnulateServer(){
    new Thread(new Runnable(){
      public void run(){
        getDataFromServer();
      }
    }).start();
  }

  private void getDataFromServer(){
    /**
     * 获取本地数据
     */
    // 获取本地最近一次更新成绩的时间
    score_updateTime = sp.getString("scoreUpdateTime",ConstantStrUtil.APP_STARTTIME);
    // 获取当前app版本号
    try {
      local_versionCode = getPackageManager().getPackageInfo(getPackageName(),PackageManager.GET_CONFIGURATIONS).versionCode;
    } catch (NameNotFoundException e1) {
      e1.printStackTrace();
      handler.sendEmptyMessage(GET_VERSIONCODE_FAIL);
      return;
    }
    // 获取成绩数目
    int scoreCount = CourseDB.count();
    // 获取课程数目
    courseCount = CourseDB.count();

    // 获取帐号密码
    username = sp.getString(ConstantStrUtil.USERNAME,"");
    password = sp.getString(ConstantStrUtil.PASSWORD,"");
    /**
     * 从服务端获取数据
     */
    WebRequest fetchURL = new WebRequest(ConstantStrUtil.DOMAINNAME+ConstantStrUtil.URL_PATH_INIT);
    HashMap<String,String> params = new HashMap<String,String>();
    // 版本信息
    params.put(ConstantStrUtil.STR_VERSION_CODE,String.valueOf(local_versionCode));
    // 用户信息
    params.put(ConstantStrUtil.USERNAME,username);
    params.put(ConstantStrUtil.PASSWORD,password);
    // 课程信息
    params.put("courseCount",String.valueOf(courseCount));
    // 成绩信息
    params.put("scoreUpdateTime",score_updateTime);
    params.put("scoreCount",String.valueOf(scoreCount));
    // get it
    fetchURL.setParams(params);
    result = fetchURL.post();
    if (result==null) {
      handler.sendEmptyMessage(GET_DATA_FAIL);
      return;
    }

    /**
     * 处理服务端获取的信息
     */
    try {
      JSONObject json = new JSONObject(result);

      // 判断从server端获取数据的状态
      String statu = json.getString(ConstantStrUtil.STR_STATU);
      if (!statu.equals(ConstantStrUtil.STR_TRUE)) {
        errorMsg = json.getString(ConstantStrUtil.STR_ERRMSG);
        handler.sendEmptyMessage(GET_DATA_FAIL);
        return;
      }

      // app更新处理
      if (!json.getString(ConstantStrUtil.STR_APPDOWNLOADURL).equals("null")) {
        appDownLoadURL = json.getString(ConstantStrUtil.STR_APPDOWNLOADURL);
        handler.sendEmptyMessage(APP_UPDATE);
        return;
      }

      // 验证用户信息
      if (!json.getString("login").equals("true")) {
        errorMsg = json.getString("login");
        handler.sendEmptyMessage(LOGIN_FAIL);
        return;
      }

      // 处理从server获取的课表信息
      if (!json.getString("courseData").equals("null")) CourseService.resolveJsonString(json.getJSONArray("courseData"),getApplicationContext());

      // 处理开学时间
      termStartTimeDealing(json.getString(ConstantStrUtil.TERM_START_DATE));

      // 处理从server获取的成绩信息
      if (!json.getString("scoreData").equals("null")) scoreInfoDealing(json.getJSONArray("scoreData"));

      // 更新需要公布的消息(成绩模块)
      if (!json.getString("scoreMsg").equals("null")) {
        ed.putString("scoreMsg",json.getString("scoreMsg"));
        ed.commit();
      }

      handler.sendEmptyMessage(GET_DATA_SUCCESS);
    } catch (JSONException e) {
      e.printStackTrace();
      handler.sendEmptyMessage(GET_DATA_FAIL);
      return;
    }
  }

  protected void appUpdate(){
    AlertDialog.Builder alert = new AlertDialog.Builder(this);
    // alert.setTitle("软件升级");
    alert.setMessage("发现新版本,建议立即更新使用.");
    alert.setPositiveButton("暂时退出\n以后再说",new OnClickListener(){
      public void onClick(DialogInterface dialog,int which){
        finish();
      }
    });
    alert.setNegativeButton("立即更新",new DialogInterface.OnClickListener(){
      public void onClick(DialogInterface dialog,int which){
        // 开启更新服务UpdateService
        // 这里为了把update更好模块化，可以传一些updateService依赖的值
        // 如布局ID，资源ID，动态获取的标题,这里以app_name为例
        Intent i = new Intent(AppStart.this,AppUpdateService.class);
        i.putExtra(ConstantStrUtil.STR_APPDOWNLOADURL,appDownLoadURL);
        startService(i);
        finish();
      }
    });
    alert.create().show();
  }

  private void scoreInfoDealing(JSONArray jsonArray) throws JSONException{
    int len = jsonArray.length();
    if (len<1) { return; }
    ScoreDB.delete();
    for (int i = 0; i<len; i++) {
      JSONObject jb = jsonArray.getJSONObject(i);
      ScoreInfo model = new ScoreInfo();
      model.setXn(jb.getString(ScoreDB.COLUMN_XN));
      model.setXq(jb.getString(ScoreDB.COLUMN_XQ));
      model.setKcmc(jb.getString(ScoreDB.COLUMN_KCMC));
      model.setKclx(jb.getString(ScoreDB.COLUMN_KCLX));
      model.setKhfs(jb.getString(ScoreDB.COLUMN_KHFS));
      model.setPscj(jb.getString(ScoreDB.COLUMN_PSCJ));
      model.setQzcj(jb.getString(ScoreDB.COLUMN_QZCJ));
      model.setSycj(jb.getString(ScoreDB.COLUMN_SYCJ));
      model.setQmcj(jb.getString(ScoreDB.COLUMN_QMCJ));
      model.setZpcj(jb.getString(ScoreDB.COLUMN_ZPCJ));
      model.setJd(jb.getString(ScoreDB.COLUMN_JD));
      model.setXf(jb.getString(ScoreDB.COLUMN_XF));
      model.setCxcj(jb.getString(ScoreDB.COLUMN_CXCJ));

      ScoreDB.save(model);
    }
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
    ed.putString("scoreUpdateTime",sdf.format(date));
    ed.commit();
  }

  private void termStartTimeDealing(String termStartDate) throws JSONException{
    try {
      // 更新开学年月日
      ed.putString(ConstantStrUtil.TERM_START_DATE,termStartDate);
      ed.commit();
      // 更新开学周数
      DateFormat sf = DateFormat.getDateInstance();
      Calendar cd = Calendar.getInstance();
      cd.setFirstDayOfWeek(Calendar.MONDAY);
      cd.setTime(sf.parse(termStartDate));
      int a = cd.get(Calendar.WEEK_OF_YEAR);
      cd.setTime(new Date());
      int b = cd.get(Calendar.WEEK_OF_YEAR);
      ed.putInt(ConstantStrUtil.TERM_START_WEEK,b-a+1);
      ed.commit();
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  private final int APP_UPDATE           = 0;
  private final int GET_VERSIONCODE_FAIL = 1;
  private final int GET_DATA_FAIL        = 2;
  private final int LOGIN_FAIL           = 3;
  private final int GET_DATA_SUCCESS     = 4;
  private Handler   handler              = new Handler(){
                                           public void handleMessage(Message msg){
                                             switch (msg.what) {
                                             case APP_UPDATE:
                                               appUpdate();
                                               break;
                                             case GET_VERSIONCODE_FAIL:
                                               Toast.makeText(AppStart.this,"获取app版本失败!",Toast.LENGTH_SHORT).show();
                                               startActivity(new Intent(AppStart.this,TweetList.class));
                                               AppStart.this.finish();
                                               break;
                                             case GET_DATA_FAIL:
                                               Toast.makeText(AppStart.this,"获取数据失败!",Toast.LENGTH_LONG).show();
                                               startActivity(new Intent(AppStart.this,TweetList.class));
                                               AppStart.this.finish();
                                               break;
                                             case LOGIN_FAIL:
                                               Toast.makeText(AppStart.this,errorMsg,Toast.LENGTH_SHORT).show();
                                               startActivity(new Intent(AppStart.this,Login.class));
                                               AppStart.this.finish();
                                               break;
                                             case CHANGE_TV_INFOTEXT:
                                               tv_info.setText(tv_infoText);
                                               break;
                                             case GET_DATA_SUCCESS:
                                               startActivity(new Intent(AppStart.this,TweetList.class));
                                               AppStart.this.finish();
                                               break;
                                             default:
                                               break;
                                             }
                                           }
                                         };
}
