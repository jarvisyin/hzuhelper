package com.hzuhelper.activity.chat;

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
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.hzuhelper.R;
import com.hzuhelper.activity.BaseActivity;
import com.hzuhelper.adapter.TweetAdapter;
import com.hzuhelper.adapter.TweetMsgAdapter;
import com.hzuhelper.model.ChatComment2Info;
import com.hzuhelper.model.ChatTweetInfo;
import com.hzuhelper.tools.ConstantStrUtil;
import com.hzuhelper.web.WebRequest;
import com.hzuhelper.wedget.RefreshAbleListView;
import com.hzuhelper.wedget.RefreshAbleListView.OnGetMoreDateListener;
import com.hzuhelper.wedget.RefreshAbleListView.OnRefreshListener;

public class TweetList extends BaseActivity{
  private RefreshAbleListView visiableListView;

  @Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chat_tweet_list);

    slidingMenuInit();
    initTweetAllListView();
    initTweetMineListView();
    initMsgSendListView();
    initMsgReceiveListView();

    tweetAllListview.refresh();
    visiableListView = tweetAllListview;

    initFootBar();
    initBtnRight();
  }

  private void initBtnRight(){
    Button btn_right = (Button)findViewById(R.id.btn_right);
    btn_right.setOnClickListener(new OnClickListener(){
      public void onClick(View v){
        Intent intent = new Intent(TweetList.this,TweetCommit.class);
        startActivity(intent);
      }
    });
  }

  private void initFootBar(){
    RadioGroup chat_footbar = (RadioGroup)findViewById(R.id.chat_footbar);
    chat_footbar.setOnCheckedChangeListener(new OnCheckedChangeListener(){
      public void onCheckedChanged(RadioGroup group,int checkedId){
        visiableListView.setVisibility(View.GONE);
        switch (checkedId) {
        case R.id.chat_footbar_tweet_all:
          tweetAllListview.setVisibility(View.VISIBLE);
          if (tweetAllList1.size()<1) tweetAllListview.refresh();
          visiableListView = tweetAllListview;
          break;
        case R.id.chat_footbar_tweet_mine:
          tweetMineListview.setVisibility(View.VISIBLE);
          if (tweetMineList1.size()<1) tweetMineListview.refresh();
          visiableListView = tweetMineListview;
          break;
        case R.id.chat_footbar_message_send:
          msgSendListview.setVisibility(View.VISIBLE);
          if (msgSendList1.size()<1) msgSendListview.refresh();
          visiableListView = msgSendListview;
          break;
        case R.id.chat_footbar_message_receive:
          msgReceiveListview.setVisibility(View.VISIBLE);
          if (msgReceiveList1.size()<1) msgReceiveListview.refresh();
          visiableListView = msgReceiveListview;
          break;
        default:
          break;
        }
      }
    });
  }

  // 加载全部tweet
  private final int                TWEET_ALL_REFRESH_FAIL  = 10;
  private final int                TWEET_ALL_GET_MORE_FAIL = 13;
  private final int                TWEET_ALL_REFRESH       = 11;
  private final int                TWEET_ALL_GET_MORE_DATA = 12;
  private RefreshAbleListView      tweetAllListview;
  private ArrayList<ChatTweetInfo> tweetAllList1;
  private ArrayList<ChatTweetInfo> tweetAllList2;
  private TweetAdapter             tweetAllAdapter;

  /**
   * 初始化
   */
  private void initTweetAllListView(){
    tweetAllList1 = new ArrayList<ChatTweetInfo>();
    tweetAllList2 = null;
    tweetAllAdapter = new TweetAdapter(tweetAllList1,TweetList.this);
    tweetAllListview = (RefreshAbleListView)findViewById(R.id.lv_tweet_all);
    tweetAllListview.setAdapter(tweetAllAdapter);
    tweetAllListview.setonRefreshListener(new OnRefreshListener(){
      public void onRefresh(){
        tweetAllRefresh();
      }
    });
    tweetAllListview.setonGetMoreDateListene(new OnGetMoreDateListener(){
      public void onGetMoreDate(){
        TweetAllGetMoreDate();
      }
    });
  }

  /**
   * 刷新
   */
  private void tweetAllRefresh(){
    new Thread(new Runnable(){
      public void run(){
        tweetAllList2 = loadTweetAllData(ConstantStrUtil.DOMAINNAME+ConstantStrUtil.URL_PATH_CHAT_TWEET_GETLIST);
        if (tweetAllList2==null) {
          handler.sendEmptyMessage(TWEET_ALL_REFRESH_FAIL);
          return;
        }
        handler.sendEmptyMessage(TWEET_ALL_REFRESH);
      }
    }).start();
  }

  /**
   * 获取更多数据
   */
  private void TweetAllGetMoreDate(){
    new Thread(new Runnable(){
      public void run(){
        int listSize = 0;
        if ((listSize = tweetAllList1.size())<1) return;
        ChatTweetInfo model = tweetAllList1.get(listSize-1);
        tweetAllList2 = loadTweetAllData(ConstantStrUtil.DOMAINNAME+ConstantStrUtil.URL_PATH_CHAT_TWEET_GETLIST+"?id="+model.getId());
        if (tweetAllList2==null) {
          handler.sendEmptyMessage(TWEET_ALL_GET_MORE_FAIL);
          return;
        }
        handler.sendEmptyMessage(TWEET_ALL_GET_MORE_DATA);
      }
    }).start();
  }

  /**
   * 网络获取数据
   */
  private ArrayList<ChatTweetInfo> loadTweetAllData(String url){
    ArrayList<ChatTweetInfo> list = new ArrayList<ChatTweetInfo>(20);
    WebRequest wq = new WebRequest(url);
    HashMap<String,String> params = new HashMap<String,String>();
    params.put(ConstantStrUtil.STR_TYPE,ConstantStrUtil.STR_ALL);
    wq.setParams(params);
    String json = wq.post();
    if (json==null) { return null; }
    JSONObject jsonResult;
    try {
      jsonResult = new JSONObject(json);
      if (!jsonResult.getString(ConstantStrUtil.STR_STATU).equals(ConstantStrUtil.STR_TRUE)) { return null; }
      JSONArray jsonResponse = jsonResult.getJSONArray(ConstantStrUtil.STR_RESPONSE);
      for (int i = 0, length = jsonResponse.length(); i<length; i++) {
        JSONObject jo = jsonResponse.getJSONObject(i);
        ChatTweetInfo m = new ChatTweetInfo();
        m.setAuthor_id(jo.getString("authorId"));
        m.setCai(jo.getInt("cai"));
        m.setComment_count(jo.getInt("commentCount"));
        m.setContent(jo.getString("content"));
        m.setDing(jo.getInt("ding"));
        m.setId(jo.getInt("id"));
        m.setPublish_date(jo.getString("publishDatetime"));
        m.setStatu(jo.getInt("statu"));
        m.setTag_id(jo.getInt("tagId"));
        list.add(m);
      }
      return list;
    } catch (JSONException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  // 加载我的tweet
  private final int                TWEET_MINE_REFRESH_FAIL  = 20;
  private final int                TWEET_MINE_GET_MORE_FAIL = 23;
  private final int                TWEET_MINE_REFRESH       = 21;
  private final int                TWEET_MINE_GET_MORE_DATA = 22;
  private RefreshAbleListView      tweetMineListview;
  private ArrayList<ChatTweetInfo> tweetMineList1;
  private ArrayList<ChatTweetInfo> tweetMineList2;
  private TweetAdapter             tweetMineAdapter;

  /**
   * 初始化
   */
  private void initTweetMineListView(){
    tweetMineList1 = new ArrayList<ChatTweetInfo>();
    tweetMineList2 = null;
    tweetMineAdapter = new TweetAdapter(tweetMineList1,TweetList.this);
    tweetMineListview = (RefreshAbleListView)findViewById(R.id.lv_tweet_mine);
    tweetMineListview.setAdapter(tweetMineAdapter);
    tweetMineListview.setonRefreshListener(new OnRefreshListener(){
      public void onRefresh(){
        tweetMineRefresh();
      }
    });
    tweetMineListview.setonGetMoreDateListene(new OnGetMoreDateListener(){
      public void onGetMoreDate(){
        tweetMineGetMoreDate();
      }
    });
  }

  /**
   * 刷新
   */
  private void tweetMineRefresh(){
    new Thread(new Runnable(){
      public void run(){
        tweetMineList2 = loadTweetMineData(ConstantStrUtil.DOMAINNAME+ConstantStrUtil.URL_PATH_CHAT_TWEET_GETLIST);
        if (tweetMineList2==null) {
          handler.sendEmptyMessage(TWEET_MINE_REFRESH_FAIL);
          return;
        }
        handler.sendEmptyMessage(TWEET_MINE_REFRESH);
      }
    }).start();
  }

  /**
   * 获取更多数据
   */
  private void tweetMineGetMoreDate(){
    new Thread(new Runnable(){
      public void run(){
        int listSize = 0;
        if ((listSize = tweetMineList1.size())<1) return;
        ChatTweetInfo model = tweetMineList1.get(listSize-1);
        tweetMineList2 = loadTweetAllData(ConstantStrUtil.DOMAINNAME+ConstantStrUtil.URL_PATH_CHAT_TWEET_GETLIST+"?id="+model.getId());
        if (tweetMineList2==null) {
          handler.sendEmptyMessage(TWEET_MINE_GET_MORE_FAIL);
          return;
        }
        handler.sendEmptyMessage(TWEET_MINE_GET_MORE_DATA);
      }
    }).start();
  }

  /**
   * 网络获取数据
   */
  private ArrayList<ChatTweetInfo> loadTweetMineData(String url){
    ArrayList<ChatTweetInfo> list = new ArrayList<ChatTweetInfo>(20);
    WebRequest wq = new WebRequest(url);
    HashMap<String,String> params = WebRequest.getUserInfo(this);
    params.put(ConstantStrUtil.STR_TYPE,ConstantStrUtil.STR_MINE);
    wq.setParams(params);
    String json = wq.post();
    if (json==null) { return null; }
    JSONObject jsonResult;
    try {
      jsonResult = new JSONObject(json);
      if (!jsonResult.getString(ConstantStrUtil.STR_STATU).equals(ConstantStrUtil.STR_TRUE)) { return null; }
      JSONArray jsonResponse = jsonResult.getJSONArray(ConstantStrUtil.STR_RESPONSE);
      for (int i = 0, length = jsonResponse.length(); i<length; i++) {
        JSONObject jo = jsonResponse.getJSONObject(i);
        ChatTweetInfo m = new ChatTweetInfo();
        m.setAuthor_id(jo.getString("authorId"));
        m.setCai(jo.getInt("cai"));
        m.setComment_count(jo.getInt("commentCount"));
        m.setContent(jo.getString("content"));
        m.setDing(jo.getInt("ding"));
        m.setId(jo.getInt("id"));
        m.setPublish_date(jo.getString("publishDatetime"));
        m.setStatu(jo.getInt("statu"));
        m.setTag_id(jo.getInt("tagId"));
        list.add(m);
      }
      return list;
    } catch (JSONException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  // 加载发出消息
  private final int                   MSG_SEND_REFRESH_FAIL  = 50;
  private final int                   MSG_SEND_GET_MORE_FAIL = 53;
  private final int                   MSG_SEND_REFRESH       = 51;
  private final int                   MSG_SEND_GET_MORE_DATA = 52;
  private RefreshAbleListView         msgSendListview;
  private ArrayList<ChatComment2Info> msgSendList1;
  private ArrayList<ChatComment2Info> msgSendList2;
  private TweetMsgAdapter             msgSendAdapter;

  /**
   * 初始化
   */
  private void initMsgSendListView(){
    msgSendList1 = new ArrayList<ChatComment2Info>();
    msgSendList2 = null;
    msgSendAdapter = new TweetMsgAdapter(msgSendList1,TweetList.this);
    msgSendListview = (RefreshAbleListView)findViewById(R.id.lv_msg_send);
    msgSendListview.setAdapter(msgSendAdapter);
    msgSendListview.setonRefreshListener(new OnRefreshListener(){
      public void onRefresh(){
        msgSendRefresh();
      }
    });
    msgSendListview.setonGetMoreDateListene(new OnGetMoreDateListener(){
      public void onGetMoreDate(){
        msgSendGetMoreDate();
      }
    });
  }

  /**
   * 刷新
   */
  private void msgSendRefresh(){
    new Thread(new Runnable(){
      public void run(){
        msgSendList2 = loadMsgSendData(ConstantStrUtil.DOMAINNAME+"/chat/comment/getlist2"+"?id=-1");
        if (msgSendList2==null) {
          handler.sendEmptyMessage(MSG_SEND_REFRESH_FAIL);
          return;
        }
        handler.sendEmptyMessage(MSG_SEND_REFRESH);
      }
    }).start();
  }

  /**
   * 获取更多数据
   */
  private void msgSendGetMoreDate(){
    new Thread(new Runnable(){
      public void run(){
        int listSize = 0;
        if ((listSize = msgSendList1.size())<1) return;
        ChatComment2Info model = msgSendList1.get(listSize-1);
        msgSendList2 = loadMsgSendData(ConstantStrUtil.DOMAINNAME+"/chat/comment/getlist2"+"?id="+model.getId());
        if (msgSendList2==null) {
          handler.sendEmptyMessage(MSG_SEND_GET_MORE_FAIL);
          return;
        }
        handler.sendEmptyMessage(MSG_SEND_GET_MORE_DATA);
      }
    }).start();
  }

  /**
   * 网络获取数据
   */
  private ArrayList<ChatComment2Info> loadMsgSendData(String url){
    ArrayList<ChatComment2Info> list = new ArrayList<ChatComment2Info>(20);
    WebRequest wq = new WebRequest(url);
    HashMap<String,String> params = WebRequest.getUserInfo(this);
    params.put(ConstantStrUtil.STR_TYPE,"send");
    wq.setParams(params);
    String json = wq.post();
    if (json==null) { return null; }
    JSONObject jsonResult;
    try {
      jsonResult = new JSONObject(json);
      if (!jsonResult.getString(ConstantStrUtil.STR_STATU).equals(ConstantStrUtil.STR_TRUE)) { return null; }
      JSONArray jsonResponse = jsonResult.getJSONArray(ConstantStrUtil.STR_RESPONSE);
      for (int i = 0, length = jsonResponse.length(); i<length; i++) {
        JSONObject jo = jsonResponse.getJSONObject(i);
        ChatComment2Info m = new ChatComment2Info();
        m.setTweet_id(jo.getInt("tweetId"));
        m.setResponse_comment_id(jo.getInt("receiverId"));
        m.setContent(jo.getString("content"));
        m.setId(jo.getInt("id"));
        m.setPublish_date(jo.getString("publishDatetime"));
        m.setStatu(jo.getInt("statu"));
        m.setYourContent(jo.getString("yourContent"));
        list.add(m);
      }
      return list;
    } catch (JSONException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  // 加载收到消息
  private final int                   MSG_RECEIVE_REFRESH_FAIL  = 40;
  private final int                   MSG_RECEIVE_GET_MORE_FAIL = 43;
  private final int                   MSG_RECEIVE_REFRESH       = 41;
  private final int                   MSG_RECEIVE_GET_MORE_DATA = 42;
  private RefreshAbleListView         msgReceiveListview;
  private ArrayList<ChatComment2Info> msgReceiveList1;
  private ArrayList<ChatComment2Info> msgReceiveList2;
  private TweetMsgAdapter             msgReceiveAdapter;

  /**
   * 初始化
   */
  private void initMsgReceiveListView(){
    msgReceiveList1 = new ArrayList<ChatComment2Info>();
    msgReceiveList2 = null;
    msgReceiveAdapter = new TweetMsgAdapter(msgReceiveList1,TweetList.this);
    msgReceiveListview = (RefreshAbleListView)findViewById(R.id.lv_msg_receive);
    msgReceiveListview.setAdapter(msgReceiveAdapter);
    msgReceiveListview.setonRefreshListener(new OnRefreshListener(){
      public void onRefresh(){
        msgReceiveRefresh();
      }
    });
    msgReceiveListview.setonGetMoreDateListene(new OnGetMoreDateListener(){
      public void onGetMoreDate(){
        msgReceiveGetMoreDate();
      }
    });
  }

  /**
   * 刷新
   */
  private void msgReceiveRefresh(){
    new Thread(new Runnable(){
      public void run(){
        msgReceiveList2 = loadMsgReceiveData(ConstantStrUtil.DOMAINNAME+"/chat/comment/getlist2"+"?id=-1");
        if (msgReceiveList2==null) {
          handler.sendEmptyMessage(MSG_RECEIVE_REFRESH_FAIL);
          return;
        }
        handler.sendEmptyMessage(MSG_RECEIVE_REFRESH);
      }
    }).start();
  }

  /**
   * 获取更多数据
   */
  private void msgReceiveGetMoreDate(){
    new Thread(new Runnable(){
      public void run(){
        int listSize = 0;
        if ((listSize = msgReceiveList1.size())<1) return;
        ChatComment2Info model = msgReceiveList1.get(listSize-1);
        msgReceiveList2 = loadMsgReceiveData(ConstantStrUtil.DOMAINNAME+"/chat/comment/getlist2"+"?id="+model.getId());
        if (msgReceiveList2==null) {
          handler.sendEmptyMessage(MSG_RECEIVE_GET_MORE_FAIL);
          return;
        }
        handler.sendEmptyMessage(MSG_RECEIVE_GET_MORE_DATA);
      }
    }).start();
  }

  /**
   * 网络获取数据
   */
  private ArrayList<ChatComment2Info> loadMsgReceiveData(String url){
    ArrayList<ChatComment2Info> list = new ArrayList<ChatComment2Info>(20);
    WebRequest wq = new WebRequest(url);
    HashMap<String,String> params = WebRequest.getUserInfo(this);
    params.put(ConstantStrUtil.STR_TYPE,"receive");
    wq.setParams(params);
    String json = wq.post();
    if (json==null) { return null; }
    JSONObject jsonResult;
    try {
      jsonResult = new JSONObject(json);
      if (!jsonResult.getString(ConstantStrUtil.STR_STATU).equals(ConstantStrUtil.STR_TRUE)) { return null; }
      JSONArray jsonResponse = jsonResult.getJSONArray(ConstantStrUtil.STR_RESPONSE);
      for (int i = 0, length = jsonResponse.length(); i<length; i++) {
        JSONObject jo = jsonResponse.getJSONObject(i);
        ChatComment2Info m = new ChatComment2Info();
        m.setTweet_id(jo.getInt("tweetId"));
        m.setResponse_comment_id(jo.getInt("receiverId"));
        m.setContent(jo.getString("content"));
        m.setId(jo.getInt("id"));
        m.setPublish_date(jo.getString("publishDatetime"));
        m.setStatu(jo.getInt("statu"));
        m.setYourContent(jo.getString("yourContent"));
        list.add(m);
      }
      return list;
    } catch (JSONException ex) {
      ex.printStackTrace();
      return null;
    }
  }

  private Handler handler = new Handler(){
                            public void handleMessage(Message msg){
                              switch (msg.what) {
                              case TWEET_ALL_REFRESH_FAIL:
                                Toast.makeText(TweetList.this,"获取数据失败",Toast.LENGTH_SHORT).show();
                                tweetAllListview.onRefreshComplete();
                                break;
                              case TWEET_ALL_GET_MORE_FAIL:
                                Toast.makeText(TweetList.this,"获取数据失败",Toast.LENGTH_SHORT).show();
                                tweetAllListview.onGetMoreDateComplete();
                                break;
                              case TWEET_ALL_REFRESH:
                                tweetAllList1.clear();
                                tweetAllList1.addAll(tweetAllList2);
                                if (tweetAllList2.size()<20) {
                                  tweetAllListview.setIsGetMoreDataable(false);
                                } else {
                                  tweetAllListview.setIsGetMoreDataable(true);
                                }
                                tweetAllList2 = null;
                                tweetAllAdapter.notifyDataSetChanged();
                                tweetAllListview.onRefreshComplete();
                                break;
                              case TWEET_ALL_GET_MORE_DATA:
                                tweetAllList1.addAll(tweetAllList2);
                                if (tweetAllList2.size()<20) {
                                  tweetAllListview.setIsGetMoreDataable(false);
                                } else {
                                  tweetAllListview.setIsGetMoreDataable(true);
                                }
                                tweetAllList2 = null;
                                tweetAllAdapter.notifyDataSetChanged();
                                tweetAllListview.onGetMoreDateComplete();
                                break;
                              case TWEET_MINE_REFRESH_FAIL:
                                Toast.makeText(TweetList.this,"获取数据失败",Toast.LENGTH_SHORT).show();
                                tweetMineListview.onRefreshComplete();
                                break;
                              case TWEET_MINE_GET_MORE_FAIL:
                                Toast.makeText(TweetList.this,"获取数据失败",Toast.LENGTH_SHORT).show();
                                tweetMineListview.onGetMoreDateComplete();
                                break;
                              case TWEET_MINE_REFRESH:
                                tweetMineList1.clear();
                                tweetMineList1.addAll(tweetMineList2);
                                if (tweetMineList2.size()<20) {
                                  tweetMineListview.setIsGetMoreDataable(false);
                                } else {
                                  tweetMineListview.setIsGetMoreDataable(true);
                                }
                                tweetMineList2 = null;
                                tweetMineAdapter.notifyDataSetChanged();
                                tweetMineListview.onRefreshComplete();
                                break;
                              case TWEET_MINE_GET_MORE_DATA:
                                tweetMineList1.addAll(tweetMineList2);
                                if (tweetMineList2.size()<20) {
                                  tweetMineListview.setIsGetMoreDataable(false);
                                } else {
                                  tweetMineListview.setIsGetMoreDataable(true);
                                }
                                tweetMineList2 = null;
                                tweetMineAdapter.notifyDataSetChanged();
                                tweetMineListview.onGetMoreDateComplete();
                                break;
                              case MSG_SEND_REFRESH_FAIL:
                                Toast.makeText(TweetList.this,"获取数据失败",Toast.LENGTH_SHORT).show();
                                msgSendListview.onRefreshComplete();
                                break;
                              case MSG_SEND_GET_MORE_FAIL:
                                Toast.makeText(TweetList.this,"获取数据失败",Toast.LENGTH_SHORT).show();
                                msgSendListview.onGetMoreDateComplete();
                                break;
                              case MSG_SEND_REFRESH:
                                msgSendList1.clear();
                                msgSendList1.addAll(msgSendList2);
                                if (msgSendList2.size()<20) {
                                  msgSendListview.setIsGetMoreDataable(false);
                                } else {
                                  msgSendListview.setIsGetMoreDataable(true);
                                }
                                msgSendList2 = null;
                                msgSendAdapter.notifyDataSetChanged();
                                msgSendListview.onRefreshComplete();
                                break;
                              case MSG_SEND_GET_MORE_DATA:
                                msgSendList1.addAll(msgSendList2);
                                if (msgSendList2.size()<20) {
                                  msgSendListview.setIsGetMoreDataable(false);
                                } else {
                                  msgSendListview.setIsGetMoreDataable(true);
                                }
                                msgSendList2 = null;
                                msgSendAdapter.notifyDataSetChanged();
                                msgSendListview.onGetMoreDateComplete();
                                break;

                              case MSG_RECEIVE_REFRESH_FAIL:
                                Toast.makeText(TweetList.this,"获取数据失败",Toast.LENGTH_SHORT).show();
                                msgReceiveListview.onRefreshComplete();
                                break;
                              case MSG_RECEIVE_GET_MORE_FAIL:
                                Toast.makeText(TweetList.this,"获取数据失败",Toast.LENGTH_SHORT).show();
                                msgReceiveListview.onGetMoreDateComplete();
                                break;
                              case MSG_RECEIVE_REFRESH:
                                msgReceiveList1.clear();
                                msgReceiveList1.addAll(msgReceiveList2);
                                if (msgReceiveList2.size()<20) {
                                  msgReceiveListview.setIsGetMoreDataable(false);
                                } else {
                                  msgReceiveListview.setIsGetMoreDataable(true);
                                }
                                msgReceiveList2 = null;
                                msgReceiveAdapter.notifyDataSetChanged();
                                msgReceiveListview.onRefreshComplete();
                                break;
                              case MSG_RECEIVE_GET_MORE_DATA:
                                msgReceiveList1.addAll(msgReceiveList2);
                                if (msgReceiveList2.size()<20) {
                                  msgReceiveListview.setIsGetMoreDataable(false);
                                } else {
                                  msgReceiveListview.setIsGetMoreDataable(true);
                                }
                                msgReceiveList2 = null;
                                msgReceiveAdapter.notifyDataSetChanged();
                                msgReceiveListview.onGetMoreDateComplete();
                                break;
                              }
                            }
                          };
}
