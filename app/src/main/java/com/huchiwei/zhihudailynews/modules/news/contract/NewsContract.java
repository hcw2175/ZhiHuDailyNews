package com.huchiwei.zhihudailynews.modules.news.contract;

import com.huchiwei.zhihudailynews.common.support.mvp.BasePresenter;
import com.huchiwei.zhihudailynews.common.support.mvp.BaseView;
import com.huchiwei.zhihudailynews.modules.news.entity.News4List;

import java.util.Date;

/**
 * 新闻列表契约类
 *
 * @author huchiwei
 * @version 1.0.0
 */
public interface NewsContract {

    interface Presenter extends BasePresenter{

        /**
         * 拉取最新新闻
         */
        void fetchLatest();

        /**
         * 拉取历史消息
         * @param date 指定日期
         */
        void fetchHistory(Date date);
    }

    interface View extends BaseView<Presenter>{

        void onDataChanged(News4List news4List);

        void onFetchFail();
    }
}
