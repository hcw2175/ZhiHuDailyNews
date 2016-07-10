package com.huchiwei.zhihudailynews;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.huchiwei.zhihudailynews.core.base.BaseActivity;
import com.huchiwei.zhihudailynews.core.helper.RetrofitHelper;
import com.huchiwei.zhihudailynews.core.support.RecyclerItemClickListener;
import com.huchiwei.zhihudailynews.core.utils.DateUtil;
import com.huchiwei.zhihudailynews.modules.news.activity.NewsDetailActivity;
import com.huchiwei.zhihudailynews.modules.news.adapter.NewsAdapter;
import com.huchiwei.zhihudailynews.modules.news.api.NewsService;
import com.huchiwei.zhihudailynews.modules.news.entity.News;
import com.huchiwei.zhihudailynews.modules.news.entity.News4List;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    @BindView(R.id.news_swipe_refresh)
    SwipeRefreshLayout mNewsSwipeRefresh;

    @BindView(R.id.news_recycler_view)
    RecyclerView mNewsListView;

    private LinearLayoutManager mLinearLayoutManager;

    private NewsAdapter mNewsAdapter = null;
    private Date mNewsDate = new Date();

    private int mLastVisibleItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 绑定View
        ButterKnife.bind(MainActivity.this);

        // 初始化RecyclerView
        mLinearLayoutManager = new LinearLayoutManager(this);
        mNewsListView.setLayoutManager(mLinearLayoutManager);

        mNewsAdapter = new NewsAdapter(this);
        mNewsListView.setAdapter(mNewsAdapter);

        // 点击事件
        RecyclerItemClickListener itemClickListener = new RecyclerItemClickListener(mNewsListView) {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder) {
                if(holder instanceof  NewsAdapter.ViewNormalHolder){
                    NewsAdapter.ViewNormalHolder viewNormalHolder = (NewsAdapter.ViewNormalHolder) holder;
                    Intent detailIntent = new Intent(MainActivity.this, NewsDetailActivity.class);
                    detailIntent.putExtra("newsId", viewNormalHolder.getNewsId());
                    startActivity(detailIntent);
                }
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

        // 上拉加载更多
        mNewsListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mLastVisibleItem + 1 == mNewsAdapter.getItemCount()) {
                    //handler.sendEmptyMessageDelayed(1, 3000);
                    Log.d(TAG, "onScrollStateChanged: Load More");
                    fetchNews(true);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mLastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();

                News news = mNewsAdapter.getFirstVisibleItem(mLinearLayoutManager.findFirstCompletelyVisibleItemPosition());
                if(null != news && null != getSupportActionBar()){
                    getSupportActionBar().setTitle(news.getPublishDate());
                }
            }
        });

        // 拉取新闻
        this.fetchNews(false);
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
    // fetch news methods ===================================================================
    private void fetchNews(final boolean isHistory){
        NewsService newsService = RetrofitHelper.createApi(NewsService.class);

        if(!isHistory){
            newsService
                    .fetchLatestNews()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(onNext(), onFail());
        }else{
            String newsDate = DateUtil.format(this.mNewsDate, "yyyyMMdd");
            newsService
                    .fetchHistoryNews(newsDate)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(onNext(), onFail());
        }
    }

    private Action1<News4List> onNext(){
        return new Action1<News4List>() {
            @Override
            public void call(News4List news4List) {
                Log.d(TAG, "新闻日期: " + news4List.getDate());
                Log.d(TAG, "当天新闻" + news4List.getStories().size() + "条");
                if(null != news4List.getTop_stories()){
                    Log.d(TAG, "当天推荐" + news4List.getTop_stories().size() + "条");
                }

                mNewsDate = DateUtil.parseDate(news4List.getDate());
                mNewsAdapter.addNewses(news4List, true);
                mNewsSwipeRefresh.setRefreshing(false);
            }
        };
    }

    private Action1<Throwable> onFail(){
        return new Action1<Throwable>() {
            @Override
            public void call(Throwable t) {
                mNewsSwipeRefresh.setRefreshing(false);
                Toast.makeText(MainActivity.this, "消息获取失败", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFailure: 获取最新的消息出错了", t);
            }
        };
    }
}
