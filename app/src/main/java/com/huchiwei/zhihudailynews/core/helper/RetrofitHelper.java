/*
 * Copyright (c) 2016 -
 * All rights reserved
 * Created on 2016-06-04
 */
package com.huchiwei.zhihudailynews.core.helper;

import android.content.Context;

import com.huchiwei.zhihudailynews.BuildConfig;
import com.huchiwei.zhihudailynews.core.Constants;
import com.huchiwei.zhihudailynews.core.utils.AppUtil;
import com.huchiwei.zhihudailynews.core.utils.ImageUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit辅助类
 *
 * @author hucw
 * @version 1.0.0
 */
public class RetrofitHelper {

    private static Retrofit retrofitInstance = null;

    /**
     * 获取Retrofit实例
     * @return Retrofit实例
     */
    private static Retrofit getInstance(){
        if(null == retrofitInstance){
            synchronized (ImageUtil.class){
                if(null == retrofitInstance){ // 双重检验锁,仅第一次调用时实例化
                    retrofitInstance = new Retrofit.Builder()
                            .baseUrl(BuildConfig.API_SERVER_URL)     // baseUrl总是以/结束，@URL不要以/开头
                            .client(buildOKHttpClient())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return retrofitInstance;
    }

    /**
     * 获取缓存对象
     * @return Cache
     */
    private static Cache getCache(){
        // 获取缓存目标
        File cacheFile = new File(AppUtil.getContext().getExternalCacheDir(), Constants.CACHE);
        // 创建缓存对象,最大缓存50m
        return new Cache(cacheFile, 1024*1024*50);
    }

    /**
     * 构建缓存拦截器
     * @return Interceptor
     */
    private static Interceptor buildCacheInterceptor(){
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                // 无网络连接时请求从缓存中读取
                if (!AppUtil.isNetworkConnected()) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }

                // 响应内容处理
                // 在线时缓存一分钟
                // 离线是缓存4周
                Response response = chain.proceed(request);
                if (AppUtil.isNetworkConnected()) {
                    int maxAge = 60;
                    response.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                            .build();
                }else {
                    // 无网络时，设置超时为4周
                    int maxStale = 60 * 60 * 24 * 28;
                    response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader("Pragma")
                            .build();
                }

                return response;
            }
        };
    }

    /**
     * 构建OkHttpClient
     * @return OkHttpClient
     */
    private static OkHttpClient buildOKHttpClient(){
        // http请求日志拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);

        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(buildCacheInterceptor())                  // 设置缓存
                .cache(getCache())
                .retryOnConnectionFailure(true)                           // 自动重连
                .connectTimeout(15, TimeUnit.SECONDS)                     // 15秒连接超时
                //.addNetworkInterceptor(mTokenInterceptor)               // 添加拦截器,可用于处理header信息,比如传token
                .build();
    }

    /**
     * 创建Retrofit请求Api
     * @param clazz   Retrofit Api接口
     * @return api实例
     */
    public static <T> T createApi(Class<T> clazz){
        return getInstance().create(clazz);
    }
}
