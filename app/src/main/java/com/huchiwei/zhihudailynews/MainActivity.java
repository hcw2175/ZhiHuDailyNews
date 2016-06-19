package com.huchiwei.zhihudailynews;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.huchiwei.zhihudailynews.common.widgets.LoadMoreView;
import com.huchiwei.zhihudailynews.core.retrofit.RetrofitHelper;
import com.huchiwei.zhihudailynews.core.utils.DateUtil;
import com.huchiwei.zhihudailynews.modules.news.api.NewsService;
import com.huchiwei.zhihudailynews.modules.news.entity.News4List;
import com.huchiwei.zhihudailynews.modules.news.ui.NewsAdapter;
import com.lhh.ptrrv.library.PullToRefreshRecyclerView;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @BindView(R.id.news_recycler_view)
    PullToRefreshRecyclerView mRefreshRecyclerView;

    private NewsAdapter mNewsAdapter;
    private Date mNewsDate = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 绑定View
        ButterKnife.bind(MainActivity.this);

        // 下拉刷新
        mRefreshRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRefreshRecyclerView.setColorSchemeResources(R.color.swipe_refresh_red, R.color.swipe_refresh_blue, R.color.swipe_refresh_green);
        mRefreshRecyclerView.setSwipeEnable(true);
        mRefreshRecyclerView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchNews(false);
            }
        });

        // 上拉加载更多
        LoadMoreView loadMoreView = new LoadMoreView(this, mRefreshRecyclerView.getRecyclerView());
        mRefreshRecyclerView.setLoadMoreFooter(loadMoreView);
        mRefreshRecyclerView.setPagingableListener(new PullToRefreshRecyclerView.PagingableListener() {
            @Override
            public void onLoadMoreItems() {
                Log.d(TAG, "onLoadMoreItems: 加载更多" );
                fetchNews(true);
            }
        });

        if(null != getSupportActionBar())
            getSupportActionBar().setTitle("今日热文");

        // 拉取新闻
        this.fetchNews(false);
    }

    private void fetchNews(final boolean isHistory){
        NewsService newsService = RetrofitHelper.createApi(this, NewsService.class);

        if(!isHistory){
            newsService.fetchLatestNews().enqueue(new Callback<News4List>() {
                @Override
                public void onResponse(Call<News4List> call, Response<News4List> response) {
                    mNewsAdapter = null;
                    parseData(response);
                }

                @Override
                public void onFailure(Call<News4List> call, Throwable t) {
                    Log.e(TAG, "onFailure: 获取最新的消息出错了", t);
                }
            });
        }else{
            String newsDate = DateUtil.format(this.mNewsDate, "yyyyMMdd");
            newsService.fetchHistoryNews(newsDate).enqueue(new Callback<News4List>() {
                @Override
                public void onResponse(Call<News4List> call, Response<News4List> response) {
                    parseData(response);
                }

                @Override
                public void onFailure(Call<News4List> call, Throwable t) {
                    Log.e(TAG, "onFailure: 获取消息出错了", t);
                }
            });
        }
    }

    private void parseData(Response<News4List> response){
        if(response.isSuccessful()){
            News4List news4List = response.body();
            Log.d(TAG, "新闻日期: " + news4List.getDate());
            Log.d(TAG, "当天新闻" + response.body().getStories().size() + "条");
            if(null != news4List.getTop_stories())
                Log.d(TAG, "推荐新闻" + news4List.getTop_stories().size() + "条");

            mNewsDate = DateUtil.parseDate(news4List.getDate());
            if(null == mNewsAdapter){
                mNewsAdapter = new NewsAdapter(MainActivity.this, news4List, news4List.getDate());
                mRefreshRecyclerView.setAdapter(mNewsAdapter);
            }else{
                mNewsAdapter.addNewses(news4List.getStories(), news4List.getDate());
            }

            mRefreshRecyclerView.setRefreshing(false);

            // 设置是否可以加载更多
            mRefreshRecyclerView.onFinishLoading(true, false);
        }else{
            Log.e(TAG, "新闻消息获取失败: " + response.message(), null);
        }
    }
}
