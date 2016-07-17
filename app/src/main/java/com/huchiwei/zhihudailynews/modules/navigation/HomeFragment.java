package com.huchiwei.zhihudailynews.modules.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.huchiwei.zhihudailynews.R;
import com.huchiwei.zhihudailynews.common.support.recyclerview.RecyclerItemClickListener;
import com.huchiwei.zhihudailynews.common.support.recyclerview.SimpleRecyclerView;
import com.huchiwei.zhihudailynews.core.utils.DateUtil;
import com.huchiwei.zhihudailynews.modules.news.activity.NewsDetailActivity;
import com.huchiwei.zhihudailynews.modules.news.adapter.NewsAdapter;
import com.huchiwei.zhihudailynews.modules.news.contract.NewsContract;
import com.huchiwei.zhihudailynews.modules.news.contract.NewsPresenter;
import com.huchiwei.zhihudailynews.modules.news.entity.News;
import com.huchiwei.zhihudailynews.modules.news.entity.News4List;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeFragment extends NavFragment implements NewsContract.View{

    @BindView(R.id.news_recycler_view)
    SimpleRecyclerView mNewsListView;

    private NewsAdapter mNewsAdapter = null;
    private Date mNewsDate = new Date();

    private NewsPresenter mNewsPresenter;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.setNavFragmentContent((FrameLayout) container.findViewById(R.id.nav_dest_fragment));
        return inflater.inflate(R.layout.nav_home_fragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 绑定View
        ButterKnife.bind(this, view);

        // List Adapter
        mNewsAdapter = new NewsAdapter(getContext());
        mNewsListView.setAdapter(mNewsAdapter);
        mNewsListView.getRecyclerView().setNestedScrollingEnabled(false);

        // 点击事件
        mNewsListView.addOnItemTouchListener(new RecyclerItemClickListener(mNewsListView.getRecyclerView()) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder) {
                if(holder instanceof  NewsAdapter.ViewNormalHolder){
                    NewsAdapter.ViewNormalHolder viewNormalHolder = (NewsAdapter.ViewNormalHolder) holder;
                    Intent detailIntent = new Intent(getActivity(), NewsDetailActivity.class);
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

                            AppCompatActivity parentActivity = ((AppCompatActivity)getActivity());
                            if(null != news && null != parentActivity.getSupportActionBar()){
                                parentActivity.setTitle(news.getPublishDate());
                            }
                        }

                    }
                });

        // 实例化Presenter
        mNewsPresenter = new NewsPresenter(this);
        mNewsPresenter.init();
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
        Toast.makeText(getActivity(), "消息获取失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        mNewsListView.setRefreshing(refreshing);
    }
}
