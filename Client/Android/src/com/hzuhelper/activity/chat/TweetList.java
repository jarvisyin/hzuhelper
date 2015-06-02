package com.hzuhelper.activity.chat;

import android.os.Bundle;
import android.view.View;

import com.hzuhelper.R;
import com.hzuhelper.activity.BaseActivity;
import com.hzuhelper.wedget.dialog.AlertDialog;

public class TweetList extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_tweet_list);
        checkOutApp();
    }

    private void checkOutApp(){
        final AlertDialog dialog = new AlertDialog(this);
        dialog.setMessage("发现新版本,建议立即更新使用。");
        dialog.setBtnRightText("立即更新");
        dialog.setBtnRightOnClickListener(new android.view.View.OnClickListener(){

            @Override
            public void onClick(View v){
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
