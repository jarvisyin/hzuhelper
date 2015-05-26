package com.hzuhelper.wedget;

import com.hzuhelper.R;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

public class ProgressDialog extends Dialog{

    public ProgressDialog(Context context){
        super(context, R.style.Dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        setContentView(R.layout.dialog_progress);
    }

}
