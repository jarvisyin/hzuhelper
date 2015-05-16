package com.hzuhelper.activity.takeout;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hzuhelper.R;
import com.hzuhelper.activity.BaseActivity;
import com.hzuhelper.config.StaticData;
import com.hzuhelper.model.TakeoutRestaurantInfo;
import com.hzuhelper.tools.ConstantStrUtil;
import com.hzuhelper.web.WebRequest;

public class ResList extends BaseActivity{
  private ArrayList<TakeoutRestaurantInfo> list;
  private ListView                         listview;
  private ProgressDialog                   progressDialog;

  @Override
  protected void onCreate(Bundle savedInstanceState){
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_takeout_res_list);
    slidingMenuInit();
    init();
  }

  private void init(){
    progressDialog = ProgressDialog.show(ResList.this,null,"获取中...");
    listview = (ListView)findViewById(R.id.listView);
    listview.setOnItemClickListener(new OnItemClickListener(){
      public void onItemClick(AdapterView<?> arg0,View view,int position,long arg3){
        Intent i = new Intent(ResList.this,MenuList.class);
        StaticData.setTakeout_restaurantInfo(list.get(position));
        startActivity(i);
      }
    });
    new Thread(new Runnable(){
      public void run(){
        list = loadData();
        if (list==null) {
          handler.sendEmptyMessage(LOAD_FAIL);
          return;
        }
        handler.sendEmptyMessage(LOAD_SUCCESS);
      }
    }).start();
  }

  private final int LOAD_FAIL    = 0;
  private final int LOAD_SUCCESS = 1;
  private Handler   handler      = new Handler(){
                                   public void handleMessage(Message msg){
                                     switch (msg.what) {
                                     case LOAD_FAIL:
                                       Toast.makeText(ResList.this,"获取数据失败",Toast.LENGTH_SHORT).show();
                                       break;
                                     case LOAD_SUCCESS:
                                       Adapter adapter = new Adapter(list);
                                       listview.setAdapter(adapter);
                                       break;
                                     }
                                     progressDialog.dismiss();
                                   }

                                 };

  private ArrayList<TakeoutRestaurantInfo> loadData(){
    WebRequest wq = new WebRequest(ConstantStrUtil.DOMAINNAME+"/menu/restaurantgetlist");
    String result = wq.get();
    if (result==null) return null;
    try {
      JSONObject json = new JSONObject(result);
      if (json.getString("statu").equals("false")) return null;

      JSONArray array = json.getJSONArray("response");
      ArrayList<TakeoutRestaurantInfo> list = new ArrayList<TakeoutRestaurantInfo>();
      for (int i = 0, len = array.length(); i<len; i++) {
        JSONObject a = array.getJSONObject(i);
        TakeoutRestaurantInfo model = new TakeoutRestaurantInfo();
        model.setId(a.getInt("id"));
        model.setTagId(a.getInt("tagId"));
        model.setDing(a.getInt("ding"));
        model.setCai(a.getInt("cai"));
        model.setIntro(a.getString("intro"));
        model.setName(a.getString("name"));
        model.setPhone(a.getString("phone"));
        list.add(model);
      }
      return list;
    } catch (JSONException ex) {
      Log.i("jarvisyin",ex.toString());
      return null;
    }
  }

  class Adapter extends BaseAdapter{
    class ListItemView{
      TextView name;
      TextView phone;
      TextView intro;
    }

    ArrayList<TakeoutRestaurantInfo> list = new ArrayList<TakeoutRestaurantInfo>();

    public Adapter(ArrayList<TakeoutRestaurantInfo> list){
      this.list = list;
    }

    @Override
    public int getCount(){
      return list.size();
    }

    @Override
    public Object getItem(int position){
      return list.get(position);
    }

    @Override
    public long getItemId(int position){
      return list.get(position).getId();
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent){
      ListItemView listItemView;
      if (convertView==null) {
        convertView = LayoutInflater.from(ResList.this).inflate(R.layout.list_item_takeout_res,null);
        listItemView = new ListItemView();
        listItemView.name = (TextView)convertView.findViewById(R.id.name);
        listItemView.phone = (TextView)convertView.findViewById(R.id.phone);
        listItemView.intro = (TextView)convertView.findViewById(R.id.intro);
        convertView.setTag(listItemView);
      } else {
        listItemView = (ListItemView)convertView.getTag();
      }

      TakeoutRestaurantInfo model = list.get(position);
      listItemView.name.setText(model.getName());
      listItemView.phone.setText("电话: "+model.getPhone().replace(",",", "));
      listItemView.intro.setText(model.getIntro());
      return convertView;
    }
  }
}
