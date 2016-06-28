package com.huchiwei.zhihudailynews.modules.news.api;

import com.huchiwei.zhihudailynews.modules.news.entity.News;
import com.huchiwei.zhihudailynews.modules.news.entity.News4List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * 新闻类API
 *
 * @author hucw
 * @version 1.0.0
 */
public interface NewsService {

    @GET("news/latest")
    Call<News4List> fetchLatestNews();

    @GET("news/before/{date}")
    Call<News4List> fetchHistoryNews(@Path("date") String date);

    @GET("news/{id}")
    Call<News> loadNews(@Path("id") int id);
}
