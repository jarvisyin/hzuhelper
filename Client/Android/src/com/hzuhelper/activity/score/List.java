package com.hzuhelper.activity.score;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.hzuhelper.R;
import com.hzuhelper.activity.BaseActivity;
import com.hzuhelper.activity.chat.TweetList;
import com.hzuhelper.adapter.ScoreAdapter;
import com.hzuhelper.config.StaticValues;
import com.hzuhelper.database.ScoreDB;
import com.hzuhelper.model.receive.P6006;

public class List extends BaseActivity{

    private ArrayList<P6006> sList;
    private ListView             lv_listScore;
    private AlertDialog.Builder  builder;
    private SharedPreferences    sp;
    private String               score_msg;
    private ScoreAdapter         adp;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_list);

        slidingMenuInit();

        sp = getSharedPreferences(StaticValues.SPF_FILE_COMMON,Context.MODE_PRIVATE);
        score_msg = sp.getString("scoreMsg","");
        if (!score_msg.equals("")) {
            builder = new Builder(this);
            builder.setTitle("注意！");
            builder.setMessage(score_msg);
            builder.setPositiveButton("ok",null);
            builder.create().show();
        }

        lv_listScore = (ListView)findViewById(R.id.listView1);
        sList = (ArrayList<P6006>)ScoreDB.getList();
        adp = new ScoreAdapter(this,sList);
        lv_listScore.setAdapter(adp);
        initTvMsg();
    }

    private void initTvMsg(){
        Button tvMsg = (Button)findViewById(R.id.tv_msg);
        if (adp.isFail) tvMsg.setText("挂科了?快来吐槽一下吧!");
        else tvMsg.setText("Yeah.告诉大家我没挂!");
        tvMsg.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(List.this,TweetList.class));
            }
        });
    }
}
