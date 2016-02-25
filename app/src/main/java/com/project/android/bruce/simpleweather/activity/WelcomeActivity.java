package com.project.android.bruce.simpleweather.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.project.android.bruce.simpleweather.R;

/**
 * Created by shuai on 2016/2/25.
 */
public class WelcomeActivity extends AppCompatActivity {

    private boolean isFirstIn;

    private static final int TIME = 2000;
    private static final int GO_HOME = 1000;
    private static final int GO_GUIDE = 1001;

    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GO_HOME:
                    goHome();
                    break;
                case GO_GUIDE:
                    goGuide();
                    break;
            }
        }
    };

    private void goGuide() {
        startActivity(new Intent(this,GuideActivity.class));
        finish();
    }

    private void goHome() {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);

        init();
    }

    private void init() {
        SharedPreferences sharedPreferences = getSharedPreferences("welcome",MODE_PRIVATE);
        isFirstIn = sharedPreferences.getBoolean("isFirstIn",true);
        if (!isFirstIn){
            myHandler.sendEmptyMessageAtTime(GO_HOME,TIME);
        }else {
            myHandler.sendEmptyMessageAtTime(GO_GUIDE,TIME);
            SharedPreferences.Editor editor =  sharedPreferences.edit();
            editor.putBoolean("isFirstIn",false);
            editor.commit();
        }
    }

}
