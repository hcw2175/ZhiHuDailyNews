package com.huchiwei.zhihudailynews;

import android.os.Bundle;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private RecyclerView mNewsRecyclerView;

    private NewsAdapter mNewsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNewsRecyclerView = (RecyclerView) findViewById(R.id.news_recycler_view);

        // 添加内置布局管理器
        mNewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 添加动画
        mNewsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        // 添加间隔
        /*mNewsRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                if (parent.)
                outRect.bottom = 10;
            }
        });*/
        // 固定大小
        mNewsRecyclerView.setHasFixedSize(true);

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

                    String mDate = "";
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
                }
            }

            @Override
            public void onFailure(Call<News4List> call, Throwable t) {
                Log.e(TAG, "onFailure: 获取最新的消息出错了", t);
            }
        });
    }
}
