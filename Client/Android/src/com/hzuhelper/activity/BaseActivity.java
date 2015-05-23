package com.hzuhelper.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.hzuhelper.AppManager;
import com.hzuhelper.R;
import com.hzuhelper.activity.chat.TweetList;
import com.hzuhelper.activity.course.Table;
import com.hzuhelper.activity.score.List;
import com.hzuhelper.activity.takeout.ResList;
import com.slidingmenu.lib.SlidingMenu;

public class BaseActivity extends FragmentActivity{

  @Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    AppManager.getAppManager().addActivity(this);
  }

  protected void slidingMenuInit(){
    final SlidingMenu menu = new SlidingMenu(this);
    menu.setMode(SlidingMenu.LEFT);
    menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
    menu.setShadowWidthRes(R.dimen.shadow_width);
    menu.setShadowDrawable(R.drawable.menu_shadow);
    menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
    menu.setBehindWidth(400);
    menu.setFadeDegree(0.35f);
    menu.attachToActivity(this,SlidingMenu.SLIDING_CONTENT);
    menu.setMenu(R.layout.menu_frame);

    /** 初始化组件 初始化定义，包括一些点击按钮和时间的初始;参数为点击滑动菜单的按钮 */
    MyClickListener myClickListener = new MyClickListener();
    // 跳转到tweet
    findViewById(R.id.btn_menu_chat_tweet).setOnClickListener(myClickListener);
    // 跳转到成绩表
    findViewById(R.id.btn_menu_score).setOnClickListener(myClickListener);
    // 跳转到课程程表
    findViewById(R.id.btn_menu_course).setOnClickListener(myClickListener);
    // 跳转到外卖列表
    findViewById(R.id.btn_menu_takeout).setOnClickListener(myClickListener);
  }

  @Override
  protected void onDestroy(){
    super.onDestroy();
    AppManager.getAppManager().finishActivity(this);
  }

  private class MyClickListener implements OnClickListener{

    @Override
    public void onClick(View view){
      switch (view.getId()) {
      case R.id.btn_menu_takeout:
        BaseActivity.this.startActivity(new Intent(BaseActivity.this,ResList.class));
        BaseActivity.this.finish();
        break;
      case R.id.btn_menu_course:
        BaseActivity.this.startActivity(new Intent(BaseActivity.this,Table.class));
        BaseActivity.this.finish();
        break;
      case R.id.btn_menu_score:
        BaseActivity.this.startActivity(new Intent(BaseActivity.this,List.class));
        BaseActivity.this.finish();
        break;
      case R.id.btn_menu_chat_tweet:
        BaseActivity.this.startActivity(new Intent(BaseActivity.this,TweetList.class));
        BaseActivity.this.finish();
        break;
      }
    }
  };

  private boolean isExit = false;

  /**
   * 双击返回键退出
   */
  public boolean onKeyDown(int keyCode,KeyEvent event){
    if (keyCode==KeyEvent.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_DOWN) {

      Timer tExit = null;
      if (isExit==false) {
        isExit = true; // 准备退出

        Toast.makeText(this,"再按一次退出程序",Toast.LENGTH_SHORT).show();
        tExit = new Timer();
        tExit.schedule(new TimerTask(){

          @Override
          public void run(){
            isExit = false; //取消退出 
          }
        },2000); //如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
      }
    } else {
      AppManager.getAppManager().appExit(this);
    }
    return super.onKeyDown(keyCode,event);
  }
}
