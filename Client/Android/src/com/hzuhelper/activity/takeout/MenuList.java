package com.hzuhelper.activity.takeout;

import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hzuhelper.R;
import com.hzuhelper.activity.BaseActivity;
import com.hzuhelper.activity.chat.TweetCommit;
import com.hzuhelper.adapter.MenuAdapter;
import com.hzuhelper.config.StaticData;
import com.hzuhelper.config.StaticValues;
import com.hzuhelper.config.staticURL;
import com.hzuhelper.model.receive.ARRAY_P6002;
import com.hzuhelper.model.receive.ARRAY_P6003;
import com.hzuhelper.model.receive.P6002;
import com.hzuhelper.utils.ToastUtil;
import com.hzuhelper.utils.web.JSONUtils;
import com.hzuhelper.utils.web.ResultObj;
import com.hzuhelper.utils.web.WebRequest;

public class MenuList extends BaseActivity{

    private ProgressDialog progressDialog;
    private ARRAY_P6003    TakeoutRestaurantInfo;
    private ListView       listView;
    private Builder        phoneCallADB;
    private Builder        commentADB;
    private String[]       phones;
    protected Button       btn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_takeout_menu_list);
        progressDialog = ProgressDialog.show(MenuList.this,null,"获取中...");
        topbarInit();
        listViewInit();
        phoneCallInit();
        commentInit();
        dingInit();
        caiInit();
    }

    private void caiInit(){
        Button btnCai = (Button)findViewById(R.id.cai);
        if (TakeoutRestaurantInfo.getCai()>0) btnCai.setText(String.valueOf(TakeoutRestaurantInfo.getCai()));
        else btnCai.setText("踩");
        btnCai.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                btn = (Button)v;
                btn.setEnabled(false);
                TakeoutRestaurantInfo.setCai(TakeoutRestaurantInfo.getCai()+1);
                btn.setText(String.valueOf(TakeoutRestaurantInfo.getCai()));
                WebRequest wq = new WebRequest(staticURL.takeout_restaurant_comment_commit){
                    @Override
                    protected void onFailure(ResultObj resultObj){
                        ToastUtil.show(resultObj.getErrorMsg());
                        TakeoutRestaurantInfo.setCai(TakeoutRestaurantInfo.getCai()-1);
                        btn.setText(String.valueOf(TakeoutRestaurantInfo.getCai()));
                    }
                };
                wq.setParam(StaticValues.type,StaticValues.cai);
                wq.setParam(StaticValues.restaurantId,String.valueOf(TakeoutRestaurantInfo.getId()));
                wq.start();
            }
        });
    }

    private void dingInit(){
        Button btnDing = (Button)findViewById(R.id.ding);
        if (TakeoutRestaurantInfo.getDing()>0) btnDing.setText(String.valueOf(TakeoutRestaurantInfo.getDing()));
        else btnDing.setText("赞");
        btnDing.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                btn = (Button)v;
                btn.setEnabled(false);
                TakeoutRestaurantInfo.setDing(TakeoutRestaurantInfo.getDing()+1);
                btn.setText(String.valueOf(TakeoutRestaurantInfo.getDing()));
                WebRequest wq = new WebRequest(staticURL.takeout_restaurant_comment_commit){
                    @Override
                    protected void onFailure(ResultObj resultObj){
                        ToastUtil.show(resultObj.getErrorMsg());
                        TakeoutRestaurantInfo.setDing(TakeoutRestaurantInfo.getDing()-1);
                        btn.setText(String.valueOf(TakeoutRestaurantInfo.getDing()));
                    }
                };
                wq.setParam(StaticValues.type,StaticValues.ding);
                wq.setParam(StaticValues.restaurantId,String.valueOf(TakeoutRestaurantInfo.getId()));
                wq.start();
            }
        });
    }

    private void commentInit(){
        commentADB = new AlertDialog.Builder(this);
        String[] items = {"我要评论","阅读评论"};
        commentADB.setItems(items,new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int item){
                try {
                    if (item==0) {
                        Intent i = new Intent(MenuList.this,TweetCommit.class);
                        i.putExtra("command",StaticData.RESTAURANT_COMMENT);
                        StaticData.setTakeout_restaurantInfo(TakeoutRestaurantInfo);
                        startActivity(i);
                    } else {
                        Intent i = new Intent(MenuList.this,ResCommentList.class);
                        i.putExtra("tagId",TakeoutRestaurantInfo.getTagId());
                        startActivity(i);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MenuList.this,e.toString()+TakeoutRestaurantInfo.getTagId(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button comment = (Button)findViewById(R.id.comment);
        if (TakeoutRestaurantInfo.getCommentNum()<1) {
            comment.setText("评论");
        } else {
            comment.setText(String.valueOf(TakeoutRestaurantInfo.getCommentNum()));
        }
        comment.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                commentADB.show();
            }
        });
    }

    private void phoneCallInit(){
        phoneCallADB = new AlertDialog.Builder(this);
        phoneCallADB.setTitle("拨打:");
        phones = TakeoutRestaurantInfo.getPhone().split(",");
        phoneCallADB.setItems(phones,new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int item){
                try {
                    Intent phoneIntent = new Intent("android.intent.action.CALL",Uri.parse("tel:"+phones[item]));
                    startActivity(phoneIntent);
                } catch (Exception e) {
                    Toast.makeText(MenuList.this,"拨打失败",Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button phone = (Button)findViewById(R.id.phone);
        phone.setText(TakeoutRestaurantInfo.getPhone());
        phone.setOnClickListener(new OnClickListener(){
            public void onClick(View v){
                phoneCallADB.show();
            }
        });
    }

    private void topbarInit(){
        TextView tv_center = (TextView)findViewById(R.id.tv_center);
        TakeoutRestaurantInfo = StaticData.getTakeout_restaurantInfo();
        tv_center.setText(TakeoutRestaurantInfo.getName());
    }

    private void listViewInit(){
        listView = (ListView)findViewById(R.id.listView);
        WebRequest wq = new WebRequest(staticURL.menu_itemgetlist){
            @Override
            protected void onFailure(ResultObj resultObj){
                ToastUtil.show(resultObj.getErrorMsg());
                progressDialog.dismiss();
            }

            @Override
            protected void onSuccess(ResultObj resultObj){
                P6002 p6002 = JSONUtils.fromJson(resultObj,P6002.class);
                List<ARRAY_P6002> arrayP6002 = p6002.getARRAY_P6002();
                MenuAdapter adapter = new MenuAdapter(MenuList.this,arrayP6002);
                listView.setAdapter(adapter);
                progressDialog.dismiss();
            }
        };
        wq.setParam(StaticValues.restaurantId,String.valueOf(TakeoutRestaurantInfo.getId()));
        wq.start();
    }

}
