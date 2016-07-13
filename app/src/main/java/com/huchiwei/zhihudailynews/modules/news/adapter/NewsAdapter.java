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
public class NewsAdapter extends RecyclerView.Adapter {
    private static final String TAG = "NewsAdapter";

    private static final int VT_HEADER = 0;
    private static final int VT_BODY = 1;
    private static final int VT_FOOTER = 2;

    private Context mContext;
    private List<News> mNewses;
    private List<News> mTopNewses;

    private boolean mCanLoadMore;               // 是否允许加载更多

    public NewsAdapter(Context context){
        this.mContext = context;
    }

    /**
     * 渲染具体的ViewHolder
     *
     * @param parent   所属容器
     * @param viewType view类型,根据该类型渲染不同的viewHolder
     * @return
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType == VT_HEADER){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_news_list_banner, parent, false);
            return new ViewBannerHolder(view, this.mTopNewses);
        }else if(viewType == VT_FOOTER){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_load_more, parent, false);
            return new ViewFooterHolder(view);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_news_list, parent, false);
            return new ViewNormalHolder(view);
        }
    }

    /**
     * 绑定数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        News news = this.mNewses.get(position);
        if(null != news && (holder instanceof ViewNormalHolder)){
            ViewNormalHolder viewNormalHolder = (ViewNormalHolder)holder;
            updateViewHolder(viewNormalHolder, news, position);
        }
    }

    /**
     * 获取数据数量
     * @return 新闻数量
     */
    @Override
    public int getItemCount() {
        return mNewses == null ? 0 : mNewses.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0)
            return VT_HEADER;
        if(position + 1 == this.getItemCount() && mCanLoadMore)
            return VT_FOOTER;
        return VT_BODY;
    }

    /**
     * News Item View
     */
    public class ViewNormalHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.news_group_date) TextView mNewsDate;
        @BindView(R.id.news_title) TextView mNewsTitle;
        @BindView(R.id.news_cover_img) ImageView mNewsCoverImage;

        private int newsId;

        public ViewNormalHolder(View v) {
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

    /**
     * Top News Banner View
     */
    public class ViewBannerHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.news_banner)
        RollPagerView mNewsBanner;

        public ViewBannerHolder(View v, List<News> topNewses) {
            super(v);
            ButterKnife.bind(this, v);

            this.mNewsBanner.setHintView(new ColorPointHintView(v.getContext(), Color.YELLOW, Color.WHITE));
            this.mNewsBanner.setAdapter(new TopNewsLoopAdapter(mNewsBanner, topNewses));
        }
    }

    /**
     * Load More Footer View
     */
    public class ViewFooterHolder extends RecyclerView.ViewHolder {
        public ViewFooterHolder(View v) {
            super(v);
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
            this.mNewses =  newsList ;
            this.mTopNewses = news4List.getTop_stories();
        }else{
            this.mNewses.addAll(newsList);
        }

        // 通知数据变化
        this.notifyDataSetChanged();
    }

    /**
     * 获取屏幕上第一个可见的元素
     * @param position
     * @return
     */
    public News getFirstVisibleItem(int position){
        if(position == 0 || position+1 >= this.getItemCount())
            return null;

        return this.mNewses.get(position);
    }

    // ==================================================================
    // private methods ==================================================
    public void updateViewHolder(ViewNormalHolder viewNormalHolder, News news, int position){
        viewNormalHolder.mNewsTitle.setText(news.getTitle());
        viewNormalHolder.mNewsDate.setText(news.getPublishDate());
        if(!TextUtils.isEmpty(news.getListCoverImage())){
            ImageUtil.displayImage(mContext, viewNormalHolder.mNewsCoverImage, news.getListCoverImage());
        }

        if(position == 1){
            viewNormalHolder.mNewsDate.setVisibility(View.VISIBLE);
        } else {
            News beforeNews = this.mNewses.get(position-1);
            if(!beforeNews.getPublishDate().equalsIgnoreCase(news.getPublishDate())){
                viewNormalHolder.mNewsDate.setVisibility(View.VISIBLE);
            } else {
                viewNormalHolder.mNewsDate.setVisibility(View.GONE);
            }
        }
        viewNormalHolder.setNewsId(news.getId());
    }

    /**
     * 重新解析新闻，设置时间
     * @param newses 新闻列表
     * @param date   新闻事件
     * @return
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
}
