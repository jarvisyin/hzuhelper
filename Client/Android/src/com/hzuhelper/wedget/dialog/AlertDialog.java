package com.hzuhelper.wedget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.hzuhelper.R;

public class AlertDialog extends Dialog implements android.view.View.OnClickListener{

    private TextView message;
    private Button   btnLeft;
    private Button   btnRight;

    public AlertDialog(Context context,int theme){
        super(context, theme);
        init();
    }

    public AlertDialog(Context context){
        super(context, R.style.Dialog);
        init();
    }

    private void init(){
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_alert,null);
        message = (TextView)view.findViewById(R.id.message);
        btnLeft = (Button)view.findViewById(R.id.btn_left);
        btnRight = (Button)view.findViewById(R.id.btn_right);
        btnLeft.setOnClickListener(this);
        btnRight.setOnClickListener(this);
        setContentView(view,new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
    }

    public void setMessage(CharSequence text){
        this.message.setText(text);
    }

    public void setBtnLeftText(String text){
        this.btnLeft.setText(text);
    }

    public void setBtnRightText(String text){
        this.btnRight.setText(text);
    }

    public void setBtnLeftOnClickListener(android.view.View.OnClickListener onClickListener){
        this.btnLeft.setOnClickListener(onClickListener);
    }

    public void setBtnRightOnClickListener(android.view.View.OnClickListener onClickListener){
        this.btnRight.setOnClickListener(onClickListener);
    }

    @Override
    public void onClick(View v){
        this.dismiss();
    }
}
