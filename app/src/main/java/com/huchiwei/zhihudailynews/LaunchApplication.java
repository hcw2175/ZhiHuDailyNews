package com.huchiwei.zhihudailynews;

import android.app.Application;

/**
 * LaunchApplication
 *
 * @author huchiwei
 * @version 1.0.0
 */
public class LaunchApplication extends Application {
    private static LaunchApplication mApplication;

    public static LaunchApplication getInstance(){
        return mApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
    }
}
