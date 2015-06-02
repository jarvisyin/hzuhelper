package com.hzuhelper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.hzuhelper.activity.BaseActivity;
import com.hzuhelper.activity.chat.TweetList;

public class AppStart extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handle.sendEmptyMessageDelayed(0,500);
    }

    private Handler handle = new Handler(){
                               @Override
                               public void handleMessage(Message msg){
                                   startActivity(new Intent(AppStart.this,TweetList.class));
                                   AppStart.this.finish();
                               }
                           };

    /*private void initSimnulateServer(){
        *//**
          * 获取本地数据
          */
    /*

    // 获取成绩数目
    int scoreCount = CourseDB.count();
    // 获取课程数目
    courseCount = CourseDB.count();

    // 获取本地最近一次更新成绩的时间 ,ConstantStrUtil.APP_STARTTIME
    score_updateTime = SPFUtils.get("scoreUpdateTime");
    // 获取帐号密码
    username = sp.getString(ConstantStrUtil.USERNAME,"");
    password = sp.getString(ConstantStrUtil.PASSWORD,"");
    *//**
      * 从服务端获取数据
      */
    /*
    WebRequest wr = new WebRequest(staticURL.clientinit);
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

    *//**
      * 处理服务端获取的信息
      */
    /*
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
     //if (!json.getString("courseData").equals("null")) CourseService.resolveJsonString(json.getJSONArray("courseData"),getApplicationContext());

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

    private void getCourse(){}

    private void scoreInfoDealing(JSONArray jsonArray) throws JSONException{
    int len = jsonArray.length();
    if (len<1) { return; }
    ScoreDB.delete();
    for (int i = 0; i<len; i++) {
     JSONObject jb = jsonArray.getJSONObject(i);
     P6006 model = new P6006();
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
    }*/

}
