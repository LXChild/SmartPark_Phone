package com.lxchild.www.splash;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

import com.lxchild.www.guide.GuideActivity;
import com.lxchild.www.smartpark.MainActivity;
import com.lxchild.www.smartpark.R;

/**
 * Created by LXChild on 2015/4/26.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SplashActivity extends Activity{

    private String TAG = SplashActivity.class.getSimpleName();
    private final int GO_GUIDE = 0X234;
    private final int GO_INDEX = 0X235;
    private final int SPLASH_DELAY_MILLIS = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_GUIDE:
                    goGuide();
                    break;
                case GO_INDEX:
                    goIndex();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initData() {
        if (isFirstEnter(this)) {
            handler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
        } else {
            handler.sendEmptyMessageDelayed(GO_INDEX, SPLASH_DELAY_MILLIS);
        }
    }


    private void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
    }

    private static final String PREFS_NAME = "Guide";
    private static final String KEY_GUIDE = "isFirstEnter";
    /**判断是否第一次运行程序*/
    private boolean isFirstEnter(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getBoolean(KEY_GUIDE, true);
    }
    /**跳转至引导界面*/
    private void goGuide() {
        Intent intent = new Intent();
        intent.setClass(SplashActivity.this, GuideActivity.class);
        startActivity(intent);
        finish();
    }
    /**跳转至主界面*/
    private void goIndex() {
        Intent intent = new Intent();
        intent.setClass(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
