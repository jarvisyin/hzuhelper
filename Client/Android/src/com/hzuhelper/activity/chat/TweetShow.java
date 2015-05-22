package com.hzuhelper.activity.chat;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hzuhelper.R;
import com.hzuhelper.activity.BaseActivity;
import com.hzuhelper.adapter.CommentAdapter;
import com.hzuhelper.config.StaticValues;
import com.hzuhelper.config.staticURL;
import com.hzuhelper.model.receive.ARRAY_CT0001;
import com.hzuhelper.model.receive.ARRAY_CT0002;
import com.hzuhelper.model.receive.CT0002;
import com.hzuhelper.tools.StringUtils;
import com.hzuhelper.tools.ToastUtil;
import com.hzuhelper.web.JSONUtils;
import com.hzuhelper.web.ResultObj;
import com.hzuhelper.web.WebRequest;

public class TweetShow extends BaseActivity implements OnScrollListener,OnClickListener {
    private ListView                listView;
    //private SimpleDateFormat            sdf            = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
    private boolean                 updateInFooter = false;
    private ProgressDialog          progressDialog;

    private ARRAY_CT0001            chatTweetInfo;
    private ArrayList<ARRAY_CT0002> commentlist;
    private LinearLayout            footView;
    private CommentAdapter          adapter;
    private int                     tweetId;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_tweet_show);
        listView = (ListView)findViewById(R.id.listView1);
        findViewById(R.id.btn_left).setOnClickListener(this);
        progressDialog = ProgressDialog.show(TweetShow.this,null,"获取中...");
        tweetId = Integer.valueOf(getIntent().getIntExtra(StaticValues.tweetId,-1));
        getTweet();
    }

    private void getTweet(){
        WebRequest wq = new WebRequest(staticURL.chat_tweet) {
            @Override
            protected void onSuccess(ResultObj resultObj){
                chatTweetInfo = JSONUtils.fromJson(resultObj,ARRAY_CT0001.class);
                addHeadView();
                getComment();
            }

            @Override
            protected void onFailure(ResultObj resultObj){
                progressDialog.dismiss();
            }
        };
        wq.setParam(StaticValues.tweetId,String.valueOf(tweetId));
        wq.start();
    }

    private void getComment(){
        WebRequest wq = new WebRequest(staticURL.chat_comment_getlist1) {
            @Override
            protected void onSuccess(ResultObj resultObj){
                CT0002 ct0002 = JSONUtils.fromJson(resultObj,CT0002.class);
                addFootView();
                commentlist = new ArrayList<ARRAY_CT0002>(20);
                commentlist.addAll(ct0002.getARRAY_CT0002());
                adapter = new CommentAdapter(TweetShow.this,commentlist,chatTweetInfo);
                listView.setAdapter(adapter);
                if (ct0002.getARRAY_CT0002().size()<20) {
                    cancelUpdate();
                } else {
                    listView.setOnScrollListener(TweetShow.this);
                    updateInFooter = true;
                }
            }

            @Override
            protected void onFailure(ResultObj resultObj){
                progressDialog.dismiss();
            }
        };
        wq.setParam(StaticValues.tweetId,String.valueOf(tweetId));
        wq.start();
    }

    private void addFootView(){
        footView = (LinearLayout)LayoutInflater.from(this).inflate(R.layout.chat_tweet_listview_foot,null);
        listView.addFooterView(footView,null,false);
    }

    private void addHeadView(){
        LinearLayout headView = (LinearLayout)LayoutInflater.from(TweetShow.this).inflate(R.layout.chat_comment_listview_head,null);

        TextView tvDate = (TextView)headView.findViewById(R.id.tv_date);
        TextView tvContent = (TextView)headView.findViewById(R.id.tv_content);
        Button btnComment = (Button)headView.findViewById(R.id.btn_comment);

        tvDate.setText(chatTweetInfo.getPublishDate());
        tvContent.setText(Html.fromHtml(StringUtils.tweetContentTran(chatTweetInfo.getContent())));
        if (chatTweetInfo.getCommentCount()>0) {
            btnComment.setText(String.valueOf(chatTweetInfo.getCommentCount()));
        } else {
            btnComment.setText("评论");
        }
        btnComment.setOnClickListener(this);

        listView.addHeaderView(headView);
    }

    private void cancelUpdate(){
        footView.setVisibility(View.GONE);
    }

    public void onScrollStateChanged(AbsListView view,int scrollState){
        if (scrollState==OnScrollListener.SCROLL_STATE_IDLE&&listView.getLastVisiblePosition()>view.getCount()-2&&updateInFooter) {
            updateInFooter = false;
            WebRequest wq = new WebRequest(staticURL.chat_comment_getlist1) {
                @Override
                protected void onSuccess(ResultObj resultObj){
                    CT0002 ct0002 = JSONUtils.fromJson(resultObj,CT0002.class);
                    commentlist.addAll(ct0002.getARRAY_CT0002());
                    adapter.notifyDataSetChanged();
                    if (ct0002.getARRAY_CT0002().size()<20) {
                        cancelUpdate();
                    } else {
                        updateInFooter = true;
                    }
                }

                @Override
                protected void onFailure(ResultObj resultObj){
                    ToastUtil.show(resultObj);
                    progressDialog.dismiss();
                }
            };
            wq.setParam(StaticValues.tweetId,String.valueOf(tweetId));
            wq.setParam(StaticValues.commentId,String.valueOf(commentlist.get(commentlist.size()-1).getId()));
            wq.start();
        }
    }

    public void onScroll(AbsListView view,int firstVisibleItem,int visibleItemCount,int totalItemCount){}

    @Override
    public void onClick(View v){
        switch (v.getId()) {
        case R.id.btn_left:
            this.finish();
            break;
        case R.id.btn_comment:
            Intent intent = new Intent(TweetShow.this,CommentCommit.class);
            intent.putExtra(StaticValues.tweetId,chatTweetInfo.getId());
            startActivity(intent);
            break;
        }
    }

}
