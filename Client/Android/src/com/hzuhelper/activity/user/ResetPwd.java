package com.hzuhelper.activity.user;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.hzuhelper.R;

public class ResetPwd extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reset_pwd);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.reset_pwd,menu);
        return true;
    }

}
