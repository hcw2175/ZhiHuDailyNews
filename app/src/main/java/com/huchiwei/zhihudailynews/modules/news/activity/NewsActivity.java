package com.huchiwei.zhihudailynews.modules.news.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.huchiwei.zhihudailynews.R;
import com.huchiwei.zhihudailynews.common.support.recyclerview.SimpleRecyclerView;
import com.huchiwei.zhihudailynews.common.ui.ToolbarActivity;
import com.huchiwei.zhihudailynews.common.support.recyclerview.RecyclerItemClickListener;
import com.huchiwei.zhihudailynews.core.utils.DateUtil;
import com.huchiwei.zhihudailynews.modules.news.adapter.NewsAdapter;
import com.huchiwei.zhihudailynews.modules.news.contract.NewsContract;
import com.huchiwei.zhihudailynews.modules.news.contract.NewsPresenter;
import com.huchiwei.zhihudailynews.modules.news.entity.News;
import com.huchiwei.zhihudailynews.modules.news.entity.News4List;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 新闻列表Activity
 */
public class NewsActivity extends ToolbarActivity implements NewsContract.View {
    @BindView(R.id.news_recycler_view)
    SimpleRecyclerView mNewsListView;

    private NewsAdapter mNewsAdapter = null;
    private Date mNewsDate = new Date();
    private NewsPresenter mNewsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 绑定View
        ButterKnife.bind(NewsActivity.this);

        // List Adapter
        mNewsAdapter = new NewsAdapter(this);
        mNewsListView.setAdapter(mNewsAdapter);

        // 点击事件
        mNewsListView.addOnItemTouchListener(new RecyclerItemClickListener(mNewsListView.getRecyclerView()) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder) {
                if(holder instanceof  NewsAdapter.ViewNormalHolder){
                    NewsAdapter.ViewNormalHolder viewNormalHolder = (NewsAdapter.ViewNormalHolder) holder;
                    Intent detailIntent = new Intent(NewsActivity.this, NewsDetailActivity.class);
                    detailIntent.putExtra("newsId", viewNormalHolder.getNewsId());
                    startActivity(detailIntent);
                }
            }
        });

        // 下拉刷新
        mNewsListView.addOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mNewsPresenter.fetchLatest();
            }
        });

        // 上拉加载更多
        mNewsListView.addScrollListener(
                new SimpleRecyclerView.OnLoadMoreListener() {
                    @Override
                    public void onLoadMore() {
                        mNewsPresenter.fetchHistory(mNewsDate);
                    }
                },
                new SimpleRecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled() {
                        RecyclerView.LayoutManager layoutManager = mNewsListView.getLayoutManager();
                        if(layoutManager instanceof LinearLayoutManager){
                            LinearLayoutManager linearLayoutManager = (LinearLayoutManager)layoutManager;
                            News news = mNewsAdapter.getDataItem(linearLayoutManager.findFirstCompletelyVisibleItemPosition());
                            if(null != news && null != getSupportActionBar()){
                                getSupportActionBar().setTitle(news.getPublishDate());
                            }
                        }

                    }
                });

        // 实例化Presenter
        mNewsPresenter = new NewsPresenter(this);
        mNewsPresenter.init();
    }

    @Override
    protected boolean showBackButton() {
        return false;
    }

    /**
     * 退到桌面重新打开，不再显示Launcher Screen
     */
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    // ======================================================================================
    // view methods ===================================================================
    @Override
    public void onDataChanged(News4List news4List) {
        mNewsDate = DateUtil.parseDate(news4List.getDate());
        mNewsAdapter.addNewses(news4List, true);
        this.setRefreshing(false);
    }

    @Override
    public void onFetchFail() {
        this.setRefreshing(false);
        Toast.makeText(NewsActivity.this, "消息获取失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        mNewsListView.setRefreshing(refreshing);
    }
}
