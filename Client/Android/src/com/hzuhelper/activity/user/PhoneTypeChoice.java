package com.hzuhelper.activity.user;

import com.hzuhelper.R;
import com.hzuhelper.R.layout;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class PhoneTypeChoice extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_phone_type_choice);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.phone_type_choice,menu);
        return true;
    }

}
