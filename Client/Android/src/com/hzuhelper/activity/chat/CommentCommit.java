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
import android.widget.Toast;

import com.hzuhelper.R;
import com.hzuhelper.activity.BaseActivity;
import com.hzuhelper.config.StaticValues;
import com.hzuhelper.config.staticURL;
import com.hzuhelper.tools.ConstantStrUtil;
import com.hzuhelper.tools.ToastUtil;
import com.hzuhelper.web.ResultObj;
import com.hzuhelper.web.WebRequest;

public class CommentCommit extends BaseActivity implements OnClickListener,TextWatcher {

    private EditText       tvContent              = null;
    private TextView       tvCount                = null;
    private Button         btnRight;
    private ProgressDialog myDialog;

    private int            MAX_COUNT              = 255;
    private long           count;
    private long           inputCount;
    private int            tweetId;
    private int            commentId;

    private String         commentBeginningString = "回复 @匿名用户 :";

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_comment_commit);

        tweetId = getIntent().getIntExtra(StaticValues.tweetId,-1);
        commentId = getIntent().getIntExtra(ConstantStrUtil.COMMENT_ID,0);
        if (commentId!=-1) MAX_COUNT -= commentBeginningString.length();

        tvContent = (EditText)findViewById(R.id.tv_content);
        tvCount = (TextView)findViewById(R.id.tv_count);
        btnRight = (Button)findViewById(R.id.btn_right);

        tvCount.setText(String.valueOf(MAX_COUNT));
        tvContent.addTextChangedListener(this);
        btnRight.setOnClickListener(this);
    }

    public void onClick(View v){
        if (count<0) {
            Toast.makeText(CommentCommit.this,"输入数字超出限制",Toast.LENGTH_SHORT).show();
            return;
        }
        if (tvContent.getText().toString().trim().length()<1) {
            Toast.makeText(CommentCommit.this,"内容不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        myDialog = ProgressDialog.show(CommentCommit.this,null,"发布中....");
        WebRequest wq = new WebRequest(staticURL.URL_PATH_CHAT_COMMENT_COMMIT,WebRequest.METHOD_POST) {
            @Override
            protected void onFinished(ResultObj resultObj){
                myDialog.dismiss();
                if (resultObj.getState()==ResultObj.STATE_SUCCESS) {
                    ToastUtil.show("发布成功");
                    CommentCommit.this.finish();
                } else {
                    ToastUtil.show(resultObj.getErrorMsg());
                }
            }
        };
        wq.setParam(StaticValues.tweetId,String.valueOf(tweetId));
        wq.setParam(StaticValues.commentId,String.valueOf(commentId));
        if (commentId!=0) {
            wq.setParam(StaticValues.content,commentBeginningString+tvContent.getText().toString());
        } else {
            wq.setParam(StaticValues.content,tvContent.getText().toString());
        }
        wq.start();
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
