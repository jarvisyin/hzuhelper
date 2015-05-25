package com.hzuhelper.wedget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

public class ProgressDialog extends Dialog {

    public ProgressDialog(Context context){
        super(context, R.style.progress_dialog);
        init();
    }

    private void init(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        setContentView(R.layout.progress_view);
    }

}
