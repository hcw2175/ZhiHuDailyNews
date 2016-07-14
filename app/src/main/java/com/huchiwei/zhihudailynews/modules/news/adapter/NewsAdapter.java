package com.huchiwei.zhihudailynews.modules.news.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huchiwei.zhihudailynews.R;
import com.huchiwei.zhihudailynews.common.support.recyclerview.SimpleRecyclerViewAdapter;
import com.huchiwei.zhihudailynews.core.utils.DateUtil;
import com.huchiwei.zhihudailynews.core.utils.ImageUtil;
import com.huchiwei.zhihudailynews.modules.news.entity.News;
import com.huchiwei.zhihudailynews.modules.news.entity.News4List;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.hintview.ColorPointHintView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 新闻列表适配器
 *
 * @author huchiwei
 * @version 1.0.0
 */
public class NewsAdapter extends SimpleRecyclerViewAdapter<News, RecyclerView.ViewHolder> {

    private List<News> mTopNewses;
    private boolean mCanLoadMore;               // 是否允许加载更多

    public NewsAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0)
            return VT_HEADER;
        if(position + 1 == this.getItemCount() && mCanLoadMore)
            return VT_FOOTER;
        return VT_BODY;
    }

    @Override
    protected ViewNormalHolder getItemView(ViewGroup parent) {
        View view = this.mLayoutInflater.inflate(R.layout.adapter_news_list, parent, false);;
        return new ViewNormalHolder(view);
    }

    @Override
    protected ViewBannerHolder getHeaderView(ViewGroup parent) {
        View view = this.mLayoutInflater.inflate(R.layout.adapter_news_list_banner, parent, false);;
        return new ViewBannerHolder(view, mTopNewses);
    }

    @Override
    protected ViewFooterHolder getFooterView(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_load_more, parent, false);
        return new ViewFooterHolder(view);
    }

    @Override
    protected void bindViewData(RecyclerView.ViewHolder viewHolder, int position) {
        News news = this.getDataItem(position);
        if(null != news && (viewHolder instanceof ViewNormalHolder)){
            ViewNormalHolder viewNormalHolder = (ViewNormalHolder)viewHolder;

            viewNormalHolder.mNewsTitle.setText(news.getTitle());
            viewNormalHolder.mNewsDate.setText(news.getPublishDate());
            if(!TextUtils.isEmpty(news.getListCoverImage())){
                ImageUtil.displayImage(mContext, viewNormalHolder.mNewsCoverImage, news.getListCoverImage());
            }

            if(position == 1){
                viewNormalHolder.mNewsDate.setVisibility(View.VISIBLE);
            } else {
                News beforeNews = this.getDataItem(position-1);
                if(!beforeNews.getPublishDate().equalsIgnoreCase(news.getPublishDate())){
                    viewNormalHolder.mNewsDate.setVisibility(View.VISIBLE);
                } else {
                    viewNormalHolder.mNewsDate.setVisibility(View.GONE);
                }
            }
            viewNormalHolder.setNewsId(news.getId());
        }
    }

    /**
     * 添加新闻列表数据
     * @param news4List    新闻数据
     * @param canLoadMore  是否可以加载更多,用于处理footer view
     */
    public void addNewses(News4List news4List, boolean canLoadMore){
        this.mCanLoadMore = canLoadMore;

        List<News> newses = news4List.getStories();
        if(null == newses || newses.size() == 0)
            return;

        String newsDate = news4List.getDate();
        List<News> newsList = parseNews(newses, newsDate);
        if(DateUtil.isToday(DateUtil.parseDate(newsDate))){
            this.updateData(newsList) ;
            this.mTopNewses = news4List.getTop_stories();
        }else{
            this.addData(newsList);
        }
    }

    /**
     * 重新解析新闻，设置时间
     * @param newses 新闻列表
     * @param date   新闻事件
     * @return List
     */
    private List<News> parseNews(List<News> newses, String date){
        if(null == newses || newses.size()==0)
            return null;

        Date groupDate = DateUtil.parseDate(date);
        if(DateUtil.isToday(groupDate)){
            date = "今日热闻";
        }else if(DateUtil.isYesterday(groupDate)){
            date = "昨日热闻";
        }else if(DateUtil.year(groupDate) != DateUtil.year(new Date())){
            date = DateUtil.format(groupDate, "yyyy年MM月dd日") +"  "+ DateUtil.format2Week(groupDate);
        }else{
            date = DateUtil.format(groupDate, "MM月dd日") +"  "+ DateUtil.format2Week(groupDate);
        }

        List<News> newsList = new ArrayList<>();
        for (News news : newses){
            news.setPublishDate(date);
            newsList.add(news);
        }
        return newsList;
    }

    // =====================================================================
    // View Holders =========================================================
    public class ViewNormalHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.news_group_date) TextView mNewsDate;
        @BindView(R.id.news_title) TextView mNewsTitle;
        @BindView(R.id.news_cover_img) ImageView mNewsCoverImage;

        private int newsId;

        ViewNormalHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        public int getNewsId() {
            return newsId;
        }
        public void setNewsId(int newsId) {
            this.newsId = newsId;
        }
    }

    public class ViewBannerHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.news_banner)
        RollPagerView mNewsBanner;

        ViewBannerHolder(View v, List<News> topNewses) {
            super(v);
            ButterKnife.bind(this, v);

            this.mNewsBanner.setHintView(new ColorPointHintView(v.getContext(), Color.YELLOW, Color.WHITE));
            this.mNewsBanner.setAdapter(new TopNewsLoopAdapter(mNewsBanner, topNewses));
        }
    }

    public class ViewFooterHolder extends RecyclerView.ViewHolder {
        ViewFooterHolder(View v) {
            super(v);
        }
    }
}
