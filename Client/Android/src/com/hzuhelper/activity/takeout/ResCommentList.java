package com.hzuhelper.activity.takeout;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.hzuhelper.R;
import com.hzuhelper.activity.BaseActivity;
import com.hzuhelper.activity.chat.TweetCommit;
import com.hzuhelper.adapter.TweetAdapter;
import com.hzuhelper.config.StaticValues;
import com.hzuhelper.model.receive.ARRAY_CT0001;
import com.hzuhelper.model.receive.CT0001;
import com.hzuhelper.tools.ConstantStrUtil;
import com.hzuhelper.tools.ToastUtil;
import com.hzuhelper.web.JSONUtils;
import com.hzuhelper.web.ResultObj;
import com.hzuhelper.web.WebRequest;
import com.hzuhelper.wedget.RefreshAbleListView;
import com.hzuhelper.wedget.RefreshAbleListView.OnGetMoreDateListener;
import com.hzuhelper.wedget.RefreshAbleListView.OnRefreshListener;

public class ResCommentList extends BaseActivity implements OnClickListener {

    private RefreshAbleListView     listview;
    private ArrayList<ARRAY_CT0001> list1;
    private TweetAdapter            adapter;
    private int                     tagId;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takeout_res_comment_list);

        tagId = getIntent().getIntExtra("tagId",-1);
        initListView();
        listview.refresh();
        findViewById(R.id.btn_right).setOnClickListener(this);
    }

    /**
     * 初始化
     */
    private void initListView(){
        list1 = new ArrayList<ARRAY_CT0001>();
        adapter = new TweetAdapter(list1,ResCommentList.this);
        listview = (RefreshAbleListView)findViewById(R.id.listView);
        listview.setAdapter(adapter);
        listview.setonRefreshListener(new OnRefreshListener() {
            public void onRefresh(){
                refresh();
            }
        });
        listview.setonGetMoreDateListene(new OnGetMoreDateListener() {
            public void onGetMoreDate(){
                getMoreDate();
            }
        });
    }

    /**
     * 刷新
     */
    private void refresh(){
        WebRequest wq = new WebRequest(ConstantStrUtil.URL_PATH_CHAT_TWEET_GETLIST) {
            @Override
            protected void onFailure(ResultObj resultObj){
                ToastUtil.show(resultObj);
                listview.onRefreshComplete();
            }

            @Override
            protected void onSuccess(ResultObj resultObj){
                CT0001 ct0001 = JSONUtils.fromJson(resultObj,CT0001.class);
                list1.clear();
                list1.addAll(ct0001.getARRAY_CT0001());
                if (ct0001.getARRAY_CT0001().size()<20) {
                    listview.setIsGetMoreDataable(false);
                } else {
                    listview.setIsGetMoreDataable(true);
                }
                adapter.notifyDataSetChanged();
                listview.onRefreshComplete();
            }
        };
        wq.setParam(StaticValues.type,StaticValues.tag);
        wq.setParam(StaticValues.tagId,String.valueOf(tagId));
        wq.start();
    }

    /**
     * 获取更多数据
     */
    private void getMoreDate(){
        int listSize = 0;
        if ((listSize = list1.size())<1) return;
        ARRAY_CT0001 model = list1.get(listSize-1);
        WebRequest wq = new WebRequest(ConstantStrUtil.URL_PATH_CHAT_TWEET_GETLIST) {
            @Override
            protected void onFailure(ResultObj resultObj){
                ToastUtil.show(resultObj);
                listview.onRefreshComplete();
            }

            @Override
            protected void onSuccess(ResultObj resultObj){
                CT0001 ct0001 = JSONUtils.fromJson(resultObj,CT0001.class);
                list1.addAll(ct0001.getARRAY_CT0001());
                if (ct0001.getARRAY_CT0001().size()<20) {
                    listview.setIsGetMoreDataable(false);
                } else {
                    listview.setIsGetMoreDataable(true);
                }
                adapter.notifyDataSetChanged();
                listview.onGetMoreDateComplete();
            }
        };
        wq.setParam(StaticValues.type,StaticValues.tag);
        wq.setParam(StaticValues.tagId,String.valueOf(tagId));
        wq.setParam(StaticValues.id,String.valueOf(model.getId()));
        wq.start();
    }

    @Override
    public void onClick(View v){
        Intent intent = new Intent(ResCommentList.this,TweetCommit.class);
        startActivity(intent);
    }

}
