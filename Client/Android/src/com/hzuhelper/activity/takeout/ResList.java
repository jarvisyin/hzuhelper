package com.hzuhelper.activity.takeout;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hzuhelper.R;
import com.hzuhelper.activity.BaseActivity;
import com.hzuhelper.config.StaticData;
import com.hzuhelper.config.staticURL;
import com.hzuhelper.model.receive.ARRAY_P6003;
import com.hzuhelper.model.receive.P6003;
import com.hzuhelper.tools.ToastUtil;
import com.hzuhelper.web.JSONUtils;
import com.hzuhelper.web.ResultObj;
import com.hzuhelper.web.WebRequest;

public class ResList extends BaseActivity {
    private ArrayList<ARRAY_P6003> list;
    private ListView               listview;
    private ProgressDialog         progressDialog;

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
        listview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0,View view,int position,long arg3){
                Intent i = new Intent(ResList.this,MenuList.class);
                StaticData.setTakeout_restaurantInfo(list.get(position));
                startActivity(i);
            }
        });
        new WebRequest(staticURL.menu_restaurantgetlist) {
            @Override
            protected void onFailure(ResultObj resultObj){
                ToastUtil.show(resultObj.getErrorMsg());
                progressDialog.dismiss();
            }

            @Override
            protected void onSuccess(ResultObj resultObj){
                P6003 p6003 = JSONUtils.fromJson(resultObj,P6003.class);
                resultObj.getResult();
                Adapter adapter = new Adapter(p6003.getARRAY_P6003());
                listview.setAdapter(adapter);
                progressDialog.dismiss();
            }
        }.start();
    }

    private final class Adapter extends BaseAdapter {
        class ListItemView {
            TextView name;
            TextView phone;
            TextView intro;
        }

        private List<ARRAY_P6003> list;

        public Adapter(List<ARRAY_P6003> list){
            if (list==null) list = new ArrayList<ARRAY_P6003>();
            this.list = list;
        }

        @Override
        public int getCount(){
            return list.size();
        }

        @Override
        public ARRAY_P6003 getItem(int position){
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

            ARRAY_P6003 model = list.get(position);
            listItemView.name.setText(model.getName());
            listItemView.phone.setText("电话: "+model.getPhone().replace(",",", "));
            listItemView.intro.setText(model.getIntro());
            return convertView;
        }
    }
}
