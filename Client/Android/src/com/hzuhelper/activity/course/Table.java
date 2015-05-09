package com.hzuhelper.activity.course;

import java.util.HashMap;
import java.util.LinkedList;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.hzuhelper.AppContext;
import com.hzuhelper.R;
import com.hzuhelper.activity.BaseActivity;
import com.hzuhelper.api.WebRequest;
import com.hzuhelper.database.CourseDB;
import com.hzuhelper.model.CourseInfo;
import com.hzuhelper.server.CourseService;
import com.hzuhelper.tools.ConstantStrUtil;

public class Table extends BaseActivity{

  private final int              REUQEST_DATA_FAIL = 0;
  private final int              WEBREUQEST_FAIL   = 1;
  private final int              REQUEST_SUCCESS   = 2;
  private final int              UNKNOWN_ERROR     = 3;

  private final int              WC                = ViewGroup.LayoutParams.WRAP_CONTENT;
  private final int              MP                = ViewGroup.LayoutParams.MATCH_PARENT;
  private int                    tv_height_1;
  private int                    tv_height_2;
  private int                    tv_width_1;
  private int                    tv_width_2;

  private LinearLayout           llo_courseTable;
  private LinearLayout           llo_head;
  private LayoutParams           lp_llo;
  private LayoutParams           lp_tv;
  private CourseView             tv;
  private boolean[][]            courseTime        = new boolean[8][13];
  private CourseInfo             model;

  private int                    textViewid;

  private Button                 btn_getCourse;
  private ProgressDialog         myDialog;
  private String                 username;
  private int                    colorrgbTag       = 0;
  private String[]               WeekDay           = {"一","二","三","四","五","六","日"};
  private int                    colorrgb[]        = {Color.rgb(113,187,172),Color.rgb(168,157,218),Color.rgb(158,201,108),Color.rgb(87,183,229),Color.rgb(213,142,180),
      Color.rgb(255,215,0),Color.rgb(255,160,122)  };
  private LinkedList<CourseInfo> clist;

  @Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_course_table);
    slidingMenuInit();
    
    llo_courseTable = (LinearLayout)findViewById(R.id.llo_courseTable);
    llo_head = (LinearLayout)findViewById(R.id.llo_head);
    btn_getCourse = (Button)findViewById(R.id.btn_right);

    lp_llo = new LinearLayout.LayoutParams(WC,WC);
    lp_tv = new LinearLayout.LayoutParams(WC,WC);

    btn_getCourse.setOnClickListener(btn_getCourseClickListener);// 一键导入按钮监听事件
    getSizeOfScreen(); // 获取屏幕尺寸大小，是程序能在不同大小的手机上有更好的兼容性
    clist = CourseDB.getLinkedList(); // 获取本地课程表
    drawTableHead(); // 画课程表的头，就是星期一、二、三 那里
    drawTableRest(); // 画剩下的课程表
  }

  /**
   * 画课程表的头，就是星期一、二、三 那里
   */
  private void drawTableHead(){
    lp_tv.setMargins(1,0,0,1);
    TextView tv = new TextView(this);
    tv.setWidth(tv_width_1);
    tv.setHeight(tv_height_1);
    tv.setGravity(Gravity.CENTER);
    tv.setBackgroundResource(R.drawable.border3);
    tv.setLayoutParams(lp_tv);
    llo_head.addView(tv);
    for (int col = 0; col<7; col++) {
      tv = new TextView(this);
      tv.setWidth(tv_width_2);
      tv.setHeight(tv_height_1);
      tv.setGravity(Gravity.CENTER);
      tv.setText(WeekDay[col]);
      tv.setBackgroundResource(R.drawable.border3);
      tv.setLayoutParams(lp_tv);
      llo_head.addView(tv);
    }
  }

  /**
   * 画剩下的课程表
   */
  private void drawTableRest(){
    lp_tv = new LinearLayout.LayoutParams(MP,WC);
    lp_tv.setMargins(1,1,0,0);

    LinearLayout llo = new LinearLayout(this);
    llo.setOrientation(LinearLayout.VERTICAL);
    llo.setLayoutParams(lp_llo);
    for (int row = 1; row<13; row++) {
      tv = new CourseView(this);
      tv.setWidth(tv_width_1);
      tv.setGravity(Gravity.CENTER);
      tv.setLayoutParams(lp_tv);
      tv.setText(String.valueOf(row));
      tv.setTextColor(Color.BLACK);
      tv.setHeight(tv_height_2);
      tv.setBackgroundResource(R.drawable.border3);
      llo.addView(tv);
    }
    llo_courseTable.addView(llo);

    if (!clist.isEmpty()) model = clist.removeFirst();
    for (int col = 1; col<8; col++) {
      llo = new LinearLayout(this);
      llo.setOrientation(LinearLayout.VERTICAL);
      llo.setLayoutParams(lp_llo);
      for (int row = 1; row<13; row++) {
        if (courseTime[col][row]==false) {
          tv = new CourseView(this);
          tv.setId(row*10+col);
          tv.setWidth(tv_width_2);
          tv.setRow(row);
          tv.setCol(col);
          tv.setGravity(Gravity.CENTER);
          tv.setLayoutParams(lp_tv);
          if (model!=null&&model.getDaytime()==col&&model.getCoursetime1()==row) {
            tv.setOnClickListener(ocShow1);
            tv.setCourseid(model.getId());
            tv.setHeight((tv_height_2+1)*model.getCoursetime2()-1);
            model.setSite(model.getSite().replace("(金)",""));
            tv.setText(model.getName()+" "+model.getSite());

            tv.setBackgroundColor(colorrgb[colorrgbTag%7]);
            colorrgbTag++;
            tv.setTextColor(Color.WHITE);
            int len = model.getCoursetime2();
            for (int i = 0; i<len; i++) {
              courseTime[col][model.getCoursetime1()+i] = true;
            }
            if (!clist.isEmpty()) model = clist.removeFirst();
            else model = null;
            while (model!=null&&courseTime[model.getDaytime()][model.getCoursetime1()]) {
              if (!tv.getText().toString().contains("(错误) ")) {
                tv.setText("(错误) "+tv.getText());
                tv.setOnClickListener(ocShow2);
              }
              if (!clist.isEmpty()) model = clist.removeFirst();
              else model = null;
            }
          } else {
            if (col!=0) {
              tv.setOnClickListener(ocAdd);
              tv.setBackgroundResource(R.drawable.border2);
              tv.setTextSize(17);
            } else {
              tv.setText(String.valueOf(row));
              tv.setTextColor(Color.BLACK);
              tv.setBackgroundResource(R.drawable.border3);

            }
            tv.setHeight(tv_height_2);
            courseTime[col][row] = true;
          }
          llo.addView(tv);
          tv.setClickable(true);
        }
      }
      llo_courseTable.addView(llo);
      tv_width_1 = tv_width_2;
    }
  }

  /***
   * 获取屏幕尺寸大小，是程序能在不同大小的手机上有更好的兼容性
   */
  private void getSizeOfScreen(){
    tv_width_1 = AppContext.screenWidth/15;
    tv_width_2 = (AppContext.screenWidth/15)*2;
    tv_height_1 = AppContext.screenHeight/26;
    tv_height_2 = (AppContext.screenHeight*6)/52;
  }

  /**
   * 从服务端获取数据并进行处理
   */
  private Runnable        getCourseFromServerRunable = new Runnable(){
                                                       public void run(){
                                                         WebRequest webreq = new WebRequest(ConstantStrUtil.DOMAINNAME+ConstantStrUtil.URL_PATH_COURSE_GET);
                                                         HashMap<String,String> params = WebRequest.getUserInfo(getApplicationContext());
                                                         webreq.setParams(params);
                                                         String json = webreq.post();
                                                         if (json==null||json.equals("")) {
                                                           handler.sendEmptyMessage(WEBREUQEST_FAIL);
                                                           return;
                                                         }
                                                         try {
                                                           JSONObject jb = new JSONObject(json);
                                                           if (jb.getString(ConstantStrUtil.STR_STATU).equals("false")) {
                                                             handler.sendEmptyMessage(REUQEST_DATA_FAIL);
                                                             return;
                                                             // "无法获取数据！"
                                                           }
                                                           CourseService.resolveJsonString(jb.getJSONArray(ConstantStrUtil.STR_RESPONSE),getApplication());
                                                           handler.sendEmptyMessage(REQUEST_SUCCESS);
                                                         } catch (Exception e) {
                                                           Message msg = new Message();
                                                           Bundle data = new Bundle();
                                                           msg.what = UNKNOWN_ERROR;
                                                           data.putString(ConstantStrUtil.STR_ERRMSG,e.toString());
                                                           msg.setData(data);
                                                           handler.sendMessage(msg);
                                                         }
                                                       }
                                                     };

  /**
   * 一键导入按钮监听事件
   */
  private OnClickListener btn_getCourseClickListener = new OnClickListener(){
                                                       public void onClick(View v){
                                                         AlertDialog.Builder adb = new AlertDialog.Builder(Table.this);
                                                         adb.setTitle("提示！");
                                                         adb.setMessage("重新导入课表将覆盖现有课表，您确定导入吗？");
                                                         adb.setPositiveButton("确定",new DialogInterface.OnClickListener(){
                                                           @Override
                                                           public void onClick(DialogInterface dialog,int which){
                                                             myDialog = ProgressDialog.show(Table.this,null,"获取中...");
                                                             SharedPreferences sp = getSharedPreferences(ConstantStrUtil.COMMON_XML_NAME,Context.MODE_PRIVATE);
                                                             username = sp.getString(ConstantStrUtil.USERNAME,null);
                                                             if (username!=null) {
                                                               new Thread(getCourseFromServerRunable).start();
                                                             } else myDialog.dismiss();
                                                           }
                                                         });
                                                         adb.setNegativeButton("取消",null);
                                                         adb.show();
                                                       }
                                                     };

  /**
   * 有课程的格子被点击后的监听事件
   */
  private OnClickListener ocShow1                    = new OnClickListener(){
                                                       public void onClick(View v){
                                                         CourseView view = (CourseView)v;
                                                         Intent i = new Intent(Table.this,Single.class);
                                                         i.putExtra("courseid",view.getCourseid());
                                                         startActivity(i);
                                                       }
                                                     };

  /**
   * 有课程并且有错误(同一时段有多门课)的格子被点击后的监听事件
   */
  private OnClickListener ocShow2                    = new OnClickListener(){
                                                       public void onClick(View v){
                                                         CourseView view = (CourseView)v;
                                                         Intent i = new Intent(Table.this,ConflictList.class);
                                                         i.putExtra("courseid",view.getCourseid());
                                                         startActivity(i);
                                                       }
                                                     };

  /**
   * 没有课程的格子被点击后的监听事件
   */
  private OnClickListener ocAdd                      = new OnClickListener(){
                                                       public void onClick(View v){
                                                         CourseView view = (CourseView)v;
                                                         if (textViewid==0) {
                                                           textViewid = view.getId();
                                                           view.setBackgroundResource(R.drawable.border1);
                                                           view.setText("+");
                                                         } else if (textViewid==view.getId()) {
                                                           Intent i = new Intent(Table.this,Add.class);
                                                           i.putExtra("row",view.getRow());
                                                           i.putExtra("col",view.getCol());
                                                           startActivity(i);
                                                         } else {
                                                           CourseView cv = (CourseView)findViewById(textViewid);
                                                           cv.setBackgroundResource(R.drawable.border2);
                                                           cv.setText("");
                                                           view.setBackgroundResource(R.drawable.border1);
                                                           view.setText("+");
                                                           textViewid = view.getId();
                                                         }
                                                       }
                                                     };

  private Handler         handler                    = new Handler(){
                                                       public void handleMessage(Message msg){
                                                         switch (msg.what) {
                                                         case REQUEST_SUCCESS:
                                                           Intent mIntent = new Intent();
                                                           mIntent.setClass(Table.this,Table.class);
                                                           startActivity(mIntent);
                                                           Table.this.finish();
                                                           break;
                                                         case UNKNOWN_ERROR:
                                                           Bundle data = msg.getData();
                                                           data.getString(ConstantStrUtil.STR_ERRMSG);
                                                           Toast.makeText(Table.this,data.getString(ConstantStrUtil.STR_ERRMSG),Toast.LENGTH_LONG).show();
                                                           break;
                                                         case WEBREUQEST_FAIL:
                                                           Toast.makeText(Table.this,"网络连接失败!",Toast.LENGTH_LONG).show();
                                                           break;
                                                         case REUQEST_DATA_FAIL:
                                                           Toast.makeText(Table.this,"获取数据失败!",Toast.LENGTH_LONG).show();
                                                           break;
                                                         default:
                                                           break;
                                                         }
                                                         myDialog.dismiss();
                                                       }
                                                     };

  private class CourseView extends TextView{

    private int row;
    private int col;
    private int courseid;

    public int getRow(){
      return row;
    }

    public void setRow(int row){
      this.row = row;
    }

    public int getCol(){
      return col;
    }

    public void setCol(int col){
      this.col = col;
    }

    public int getCourseid(){
      return courseid;
    }

    public void setCourseid(int courseid){
      this.courseid = courseid;
    }

    public CourseView(Context context){
      super(context);
    }
  }
}