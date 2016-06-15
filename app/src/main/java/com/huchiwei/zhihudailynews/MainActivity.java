package com.huchiwei.zhihudailynews;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.huchiwei.zhihudailynews.api.NewsService;
import com.huchiwei.zhihudailynews.core.retrofit.RetrofitHelper;
import com.huchiwei.zhihudailynews.entity.ListNewsResult;
import com.huchiwei.zhihudailynews.entity.News;
import com.huchiwei.zhihudailynews.ui.NewsAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private List<News> mNews = new ArrayList<>();
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView)findViewById(R.id.news_list_view);

        fetchLastNews();
    }


    /**
     * 拉取今日热闻
     */
    private void fetchLastNews(){
        NewsService newsService = RetrofitHelper.createApi(this, NewsService.class);
        newsService.findLastNews().enqueue(new Callback<ListNewsResult>() {
            @Override
            public void onResponse(Call<ListNewsResult> call, Response<ListNewsResult> response) {
                if(response.isSuccessful()){
                    ListNewsResult result = response.body();
                    Log.d(TAG, "最新日期: " + result.getDate());
                    Log.d(TAG, "当天新闻" + response.body().getStories().size() + "条");
                    Log.d(TAG, "推荐新闻" + result.getTop_stories().size() + "条");

                    mNews = result.getStories();
                    NewsAdapter newsAdapter = new NewsAdapter(MainActivity.this, R.layout.list_news, mNews);
                    mListView.setAdapter(newsAdapter);
                }

            }

            @Override
            public void onFailure(Call<ListNewsResult> call, Throwable t) {
                Log.e(TAG, "onFailure: 获取最新的消息出错了", t);
            }
        });
    }
}
