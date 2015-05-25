package com.hzuhelper.activity.chat;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hzuhelper.R;
import com.hzuhelper.activity.BaseActivity;
import com.hzuhelper.config.StaticData;
import com.hzuhelper.config.StaticValues;
import com.hzuhelper.config.staticURL;
import com.hzuhelper.model.receive.ARRAY_P6003;
import com.hzuhelper.tools.ToastUtil;
import com.hzuhelper.web.ResultObj;
import com.hzuhelper.web.WebRequest;

public class TweetCommit extends BaseActivity implements OnClickListener,TextWatcher {

    private EditText              tvContent = null;
    private TextView              tvCount   = null;
    private Button                btnRight;
    private ProgressDialog        myDialog;
    private String                content;
    private static int            MAX_COUNT = 255;
    private long                  count;
    private long                  inputCount;
    private int                   command;
    private ARRAY_P6003 takeoutRestaurantInfo;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_tweet_commit);
        tvContent = (EditText)findViewById(R.id.tv_content);
        tvCount = (TextView)findViewById(R.id.tv_count);
        btnRight = (Button)findViewById(R.id.btn_right);

        command = getIntent().getIntExtra(StaticValues.command,-1);
        if (command==1) {
            takeoutRestaurantInfo = StaticData.getTakeout_restaurantInfo();
            MAX_COUNT = MAX_COUNT-takeoutRestaurantInfo.getName().length()-2;
        }

        tvContent.addTextChangedListener(this);
        btnRight.setOnClickListener(this);
    }

    public void onClick(View v){
        content = tvContent.getText().toString();
        if (content.trim().length()<1) {
            ToastUtil.show("内容不能为空");
            return;
        }
        if (count<0) {
            ToastUtil.show("输入数字超出限制");
            return;
        }
        myDialog = ProgressDialog.show(TweetCommit.this,null,"发布中...");
        WebRequest wr = new WebRequest(staticURL.URL_PATH_CHAT_TWEET_COMMIT) {
            @Override
            protected void onFinished(ResultObj resultObj){
                myDialog.dismiss();
                if (resultObj.getState()==ResultObj.STATE_SUCCESS) {
                    ToastUtil.show("发布成功");
                    TweetCommit.this.finish();
                } else {
                    ToastUtil.show(resultObj.getErrorMsg());
                }
            }
        };

        wr.setParam(StaticValues.tagId,"0");
        if (command==1) {
            content = "#"+takeoutRestaurantInfo.getName()+"#"+content;
            wr.setParam(StaticValues.tagId,String.valueOf(takeoutRestaurantInfo.getTagId()));
        }
        wr.setParam(StaticValues.content,content);
        wr.start();
    }

    public void afterTextChanged(Editable s){
        inputCount = tvContent.getText().toString().length();
        count = MAX_COUNT-inputCount;
        tvCount.setText(String.valueOf(count));
        if (count<0) tvCount.setTextColor(getResources().getColor(R.color.colorFF5050));
        else tvCount.setTextColor(getResources().getColor(R.color.black));
    }

    public void beforeTextChanged(CharSequence s,int start,int count,int after){}

    public void onTextChanged(CharSequence s,int start,int before,int count){}

}
