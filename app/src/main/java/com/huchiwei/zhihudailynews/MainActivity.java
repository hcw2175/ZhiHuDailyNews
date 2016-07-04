package com.huchiwei.zhihudailynews;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.huchiwei.zhihudailynews.core.helper.RetrofitHelper;
import com.huchiwei.zhihudailynews.core.support.RecyclerItemClickListener;
import com.huchiwei.zhihudailynews.core.utils.DateUtil;
import com.huchiwei.zhihudailynews.modules.news.activity.NewsDetailActivity;
import com.huchiwei.zhihudailynews.modules.news.adapter.NewsAdapter;
import com.huchiwei.zhihudailynews.modules.news.api.NewsService;
import com.huchiwei.zhihudailynews.modules.news.entity.News4List;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @BindView(R.id.news_recycler_view)
    RecyclerView mNewsListView;

    @BindView(R.id.news_swipe_refresh)
    SwipeRefreshLayout mNewsSwipeRefresh;

    private NewsAdapter mNewsAdapter;
    private Date mNewsDate = new Date();
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 绑定View
        ButterKnife.bind(MainActivity.this);

        // 初始化RecyclerView
        mLinearLayoutManager = new LinearLayoutManager(this);
        mNewsListView.setLayoutManager(mLinearLayoutManager);

        // 点击事件
        RecyclerItemClickListener itemClickListener = new RecyclerItemClickListener(mNewsListView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder) {
                NewsAdapter.ViewNormalHolder viewNormalHolder = (NewsAdapter.ViewNormalHolder) holder;
                Intent detailIntent = new Intent(MainActivity.this, NewsDetailActivity.class);
                detailIntent.putExtra("newsId", viewNormalHolder.getNewsId());
                startActivity(detailIntent);
            }
        };
        mNewsListView.addOnItemTouchListener(itemClickListener);

        // 下拉刷新
        mNewsSwipeRefresh.setColorSchemeResources(R.color.swipe_refresh_red, R.color.swipe_refresh_blue, R.color.swipe_refresh_green);
        mNewsSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchNews(false);
            }
        });

        mNewsListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //super.onScrolled(recyclerView, dx, dy);
                /*int totalCount = mLinearLayoutManager.getItemCount();
                int lastVisible = mLinearLayoutManager.findLastVisibleItemPosition();
                // 列表可见项只剩下4项并且向下滚动时加载更多
                Log.d(TAG, "totalCount: " + totalCount + ", lastVisible: "+ lastVisible);
                if(totalCount-lastVisible >= 4 && dy > 0){
                    fetchNews(true);
                }*/
            }
        });

        //if(null != getSupportActionBar())
        //    getSupportActionBar().setTitle("今日热文");

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
                    parseData(response, false);
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
                    parseData(response, true);
                }

                @Override
                public void onFailure(Call<News4List> call, Throwable t) {
                    Log.e(TAG, "onFailure: 获取消息出错了", t);
                }
            });
        }
    }

    private void parseData(Response<News4List> response, boolean isHistory){
        if(response.isSuccessful()){
            News4List news4List = response.body();
            Log.d(TAG, "新闻日期: " + news4List.getDate());
            Log.d(TAG, "当天新闻" + news4List.getStories().size() + "条");

            if(!isHistory && null != news4List.getTop_stories()){
                Log.d(TAG, "当天推荐" + news4List.getTop_stories().size() + "条");
            }

            mNewsDate = DateUtil.parseDate(news4List.getDate());
            if(null == mNewsAdapter){
                mNewsAdapter = new NewsAdapter(MainActivity.this, news4List, news4List.getDate());
                mNewsListView.setAdapter(mNewsAdapter);
            }else{
                mNewsAdapter.addNewses(news4List.getStories(), news4List.getDate());
            }

            mNewsSwipeRefresh.setRefreshing(false);
        }else{
            Log.e(TAG, "新闻消息获取失败: " + response.message(), null);
        }
    }
}
