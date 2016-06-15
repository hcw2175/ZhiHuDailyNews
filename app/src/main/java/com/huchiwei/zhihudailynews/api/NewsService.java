package com.huchiwei.zhihudailynews.api;

import com.huchiwei.zhihudailynews.entity.ListNewsResult;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * 新闻类API
 *
 * @author hucw
 * @version 1.0.0
 */
public interface NewsService {

    @GET("news/latest")
    Call<ListNewsResult> findLastNews();
}
