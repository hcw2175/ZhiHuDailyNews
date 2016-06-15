package com.huchiwei.zhihudailynews.modules.news.api;

import com.huchiwei.zhihudailynews.modules.news.entity.News4List;
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
    Call<News4List> findLastNews();
}
