/*
 * Copyright (c) 2016 -
 * All rights reserved
 * Created on 2016-06-04
 */
package com.huchiwei.zhihudailynews.core.helper;

import android.content.Context;

import com.huchiwei.zhihudailynews.core.config.AppConfig;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit辅助类
 *
 * @author hucw
 * @version 1.0.0
 */
public class RetrofitHelper {

    /**
     * Retrofit单例
     */
    private static Retrofit retrofitInstance;

    /**
     * 获取Retrofit实例
     * @return Retrofit
     */
    public static synchronized Retrofit getInstance(){
        if (null != retrofitInstance)
            return retrofitInstance;

        // baseUrl总是以/结束，@URL不要以/开头
        retrofitInstance = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(AppConfig.getBaseUrl())
                .build();
        return retrofitInstance;
    }

    /**
     * 创建Retrofit请求Api
     * @param context 上下文
     * @param clazz   Retrofit Api接口
     * @return api实例
     */
    public static <T> T createApi(Context context, Class<T> clazz){
        return getInstance().create(clazz);
    }
}
