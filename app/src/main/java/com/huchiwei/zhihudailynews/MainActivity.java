package com.huchiwei.zhihudailynews;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.huchiwei.zhihudailynews.core.utils.DateUtil;
import com.huchiwei.zhihudailynews.modules.news.api.NewsService;
import com.huchiwei.zhihudailynews.core.retrofit.RetrofitHelper;

import com.huchiwei.zhihudailynews.modules.news.entity.News4List;
import com.huchiwei.zhihudailynews.modules.news.ui.NewsAdapter;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @BindView(R.id.news_recycler_view) RecyclerView mNewsRecyclerView;

    @BindView(R.id.activity_main) SwipeRefreshLayout mSwipeRefreshLayout;

    private NewsAdapter mNewsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 绑定各种View
        ButterKnife.setDebug(true);
        ButterKnife.bind(MainActivity.this);

        // RecyclerView处理
        mNewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mNewsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mNewsRecyclerView.setHasFixedSize(true);

        // 下拉刷新
        mSwipeRefreshLayout.setColorSchemeResources(R.color.swipe_refresh_red, R.color.swipe_refresh_blue, R.color.swipe_refresh_green);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchLastNews();
            }
        });

        // 拉取新闻
        this.fetchLastNews();
    }

    /**
     * 拉取最新的新闻
     */
    public void fetchLastNews(){
        NewsService newsService = RetrofitHelper.createApi(this, NewsService.class);
        newsService.findLastNews().enqueue(new Callback<News4List>() {
            @Override
            public void onResponse(Call<News4List> call, Response<News4List> response) {
                if(response.isSuccessful()){
                    News4List news4List = response.body();
                    Log.d(TAG, "最新日期: " + news4List.getDate());
                    Log.d(TAG, "当天新闻" + response.body().getStories().size() + "条");

                    if(null != news4List.getTop_stories())
                        Log.d(TAG, "推荐新闻" + news4List.getTop_stories().size() + "条");

                    String mDate;
                    Date groupDate = DateUtil.parseDate(news4List.getDate());
                    if(DateUtil.isToday(groupDate)){
                        mDate = "今日热闻";
                    }else if(DateUtil.isToday(groupDate)){
                        mDate = "昨日热闻";
                    }else if(DateUtil.year(groupDate) != DateUtil.year(new Date())){
                        mDate = DateUtil.format(groupDate, "yyyy年MM月dd日");
                    }else{
                        mDate = DateUtil.format(groupDate, "MM月dd日");
                    }
                    Log.d(TAG, "onBindViewHolder: " + mDate);
                    mNewsAdapter = new NewsAdapter(MainActivity.this, mDate, news4List.getStories());
                    mNewsRecyclerView.setAdapter(mNewsAdapter);

                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<News4List> call, Throwable t) {
                Log.e(TAG, "onFailure: 获取最新的消息出错了", t);
            }
        });
    }
}
