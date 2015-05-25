package com.hzuhelper.wedget;

import com.hzuhelper.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

public class ProgressDialog extends Dialog{

    public ProgressDialog(Context context){
        super(context, R.style.ProgressDialog);
        init();
    }

    private void init(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        setContentView(R.layout.progress_view);
    }

}
