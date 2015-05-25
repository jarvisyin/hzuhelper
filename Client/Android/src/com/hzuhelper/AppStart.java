package com.hzuhelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
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
import com.hzuhelper.config.StaticValues;
import com.hzuhelper.config.staticURL;
import com.hzuhelper.database.CourseDB;
import com.hzuhelper.database.ScoreDB;
import com.hzuhelper.model.ScoreInfo;
import com.hzuhelper.model.receive.P6000;
import com.hzuhelper.model.receive.P6001;
import com.hzuhelper.server.CourseService;
import com.hzuhelper.tools.ConstantStrUtil;
import com.hzuhelper.tools.SPFUtils;
import com.hzuhelper.tools.ToastUtil;
import com.hzuhelper.web.JSONUtils;
import com.hzuhelper.web.ResultObj;
import com.hzuhelper.web.WebRequest;

public class AppStart extends BaseActivity{

    private String   score_updateTime;
    private String   result;
    private int      courseCount;
    private Date     date = new Date();
    private int      local_versionCode;
    private TextView tvInfo;
    private String   appDownLoadURL;
    private String   errorMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvInfo = (TextView)findViewById(R.id.tv_info);
        handle.sendEmptyMessageDelayed(0,300);
        getURL();
        initSimnulateServer();
    }

    @Override
    protected void onPause(){
        tag = -1;
    }

    private int     tag;
    private Handler handle = new Handler(){

                               @Override
                               public void handleMessage(Message msg){
                                   if (tag<0) return;
                                   switch (tag%4) {
                                   case 0:
                                       tvInfo.setText("    Loading    ");
                                       break;
                                   case 1:
                                       tvInfo.setText("    Loading.   ");
                                       break;
                                   case 2:
                                       tvInfo.setText("    Loading..  ");
                                       break;
                                   case 3:
                                       tvInfo.setText("    Loading... ");
                                       break;
                                   }
                                   tag++;
                                   handle.sendEmptyMessageDelayed(0,500);
                               }
                           };

    private void getURL(){
        WebRequest wq = new WebRequest(staticURL.URL,staticURL.get_url){
            @Override
            protected void onSuccess(ResultObj resultObj){
                P6000 p6000 = JSONUtils.fromJson(resultObj,P6000.class);
                staticURL.DOMAINNAME = p6000.getURL();
            }

            @Override
            protected void onFailure(ResultObj resultObj){
                ToastUtil.show(resultObj);
                getCourse();
            }
        };
        wq.start();
    }

    private void initSimnulateServer(){

        /**
         * 获取本地数据
         */
        // 获取当前app版本号
        int localVersionCode = 0;
        try {
            localVersionCode = getPackageManager().getPackageInfo(getPackageName(),PackageManager.GET_CONFIGURATIONS).versionCode;
        } catch (NameNotFoundException e1) {
            ToastUtil.show("无法获取app版本!");
            getCourse();
            return;
        }
        WebRequest wq = new WebRequest(staticURL.app_update){
            @Override
            protected void onSuccess(ResultObj resultObj){
                P6001 p6001 = JSONUtils.fromJson(resultObj,P6001.class);
                if ("2".equals(p6001.getNeedToUpdate())) {
                    getCourse();
                } else if ("3".equals(p6001.getNeedToUpdate())) {
                    getCourse();
                } else {
                    getCourse();
                }
            }

            @Override
            protected void onFailure(ResultObj resultObj){
                ToastUtil.show(resultObj);
                getCourse();
            }
        };

        // 获取成绩数目
        int scoreCount = CourseDB.count();
        // 获取课程数目
        courseCount = CourseDB.count();

        // 获取本地最近一次更新成绩的时间 ,ConstantStrUtil.APP_STARTTIME
        score_updateTime = SPFUtils.get("scoreUpdateTime");
        // 获取帐号密码
        /*username = sp.getString(ConstantStrUtil.USERNAME,"");
        password = sp.getString(ConstantStrUtil.PASSWORD,"");*/
        /**
         * 从服务端获取数据
         */
        WebRequest wr = new WebRequest(staticURL.URL_PATH_INIT);
        wr.setParam(StaticValues.versionCode,String.valueOf(local_versionCode));
        wr.setParam("courseCount",String.valueOf(courseCount));
        wr.setParam("scoreUpdateTime",score_updateTime);
        wr.setParam("scoreCount",String.valueOf(scoreCount));
        wr.start();
        //result = fetchURL.post();
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
                SPFUtils.put("scoreMsg",json.getString("scoreMsg"));
            }

            handler.sendEmptyMessage(GET_DATA_SUCCESS);
        } catch (JSONException e) {
            e.printStackTrace();
            handler.sendEmptyMessage(GET_DATA_FAIL);
            return;
        }
    }

    private void getCourse(){

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

        SPFUtils.put("scoreUpdateTime",sdf.format(date));
    }

    private void termStartTimeDealing(String termStartDate) throws JSONException{
        try {
            // 更新开学年月日 
            SPFUtils.put(ConstantStrUtil.TERM_START_DATE,termStartDate);
            // 更新开学周数
            DateFormat sf = DateFormat.getDateInstance();
            Calendar cd = Calendar.getInstance();
            cd.setFirstDayOfWeek(Calendar.MONDAY);
            cd.setTime(sf.parse(termStartDate));
            int a = cd.get(Calendar.WEEK_OF_YEAR);
            cd.setTime(new Date());
            int b = cd.get(Calendar.WEEK_OF_YEAR);
            SPFUtils.put(ConstantStrUtil.TERM_START_WEEK,String.valueOf(b-a+1));
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
