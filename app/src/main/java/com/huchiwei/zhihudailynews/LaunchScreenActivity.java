package com.huchiwei.zhihudailynews;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * App启动画面Activity
 *
 * @author huchiwei
 * @version 1.0.0
 */
public class LaunchScreenActivity extends AppCompatActivity {
    private static final String TAG = "LaunchScreenActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_screen);
        Log.d(TAG, "onCreate: LaunchScreenActivity");

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LaunchScreenActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 5000);
    }
}
