package com.huchiwei.zhihudailynews.modules.news.contract;

import android.util.Log;
import android.widget.Toast;

import com.huchiwei.zhihudailynews.core.helper.RetrofitHelper;
import com.huchiwei.zhihudailynews.core.utils.DateUtil;
import com.huchiwei.zhihudailynews.modules.news.activity.NewsActivity;
import com.huchiwei.zhihudailynews.modules.news.api.NewsService;
import com.huchiwei.zhihudailynews.modules.news.entity.News4List;

import java.util.Date;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * NewsPresenter
 *
 * @author huchiwei
 * @version 1.0.0
 */
public class NewsPresenter implements NewsContract.Presenter {
    private final String TAG = this.getClass().getName();

    private NewsContract.View mNewsView;
    private NewsService mNewsService;

    public NewsPresenter(NewsContract.View mNewsView) {
        this.mNewsView = mNewsView;
        mNewsService = RetrofitHelper.createApi(NewsService.class);
    }

    @Override
    public void fetchLatest() {
        mNewsService
                .fetchLatestNews()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext(), onFail());
    }

    @Override
    public void fetchHistory(Date date) {
        String newsDate = DateUtil.format(date, "yyyyMMdd");
        mNewsService
                .fetchHistoryNews(newsDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext(), onFail());
    }

    @Override
    public void init() {
        this.fetchLatest();
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
                mNewsView.onDataChanged(news4List);
            }
        };
    }

    private Action1<Throwable> onFail(){
        return new Action1<Throwable>() {
            @Override
            public void call(Throwable t) {
                Log.e(TAG, "onFailure: 获取最新的消息出错了", t);
                mNewsView.onFetchFail();
            }
        };
    }
}
