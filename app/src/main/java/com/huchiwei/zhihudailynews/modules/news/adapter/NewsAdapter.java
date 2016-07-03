package com.huchiwei.zhihudailynews.modules.news.adapter;

import android.content.Context;
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

    private static final int VT_BANNER = 0;
    private static final int VT_NEWS = 1;

    private Context mContext;
    private List<News> mNewses;
    private List<News> mTopNewses;

    public NewsAdapter(Context context, News4List news4List, String newsDate) {
        this.mContext = context;
        this.mNewses = this.parseNews(news4List.getStories(), newsDate);

        if(null != news4List.getTop_stories()){
            this.mTopNewses = news4List.getTop_stories();
        }

        this.notifyDataSetChanged();
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
        /*if(viewType == VT_BANNER){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_banner, parent, false);
            return new ViewBannerHolder(view);
        }else{
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_card, parent, false);
            return new ViewNormalHolder(view);
        }*/

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_card, parent, false);
        return new ViewNormalHolder(view);
    }

    /**
     * 绑定数据
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        News news = this.mNewses.get(position);
        if(null != news){
            /*if(holder instanceof ViewBannerHolder){
                ViewBannerHolder viewBannerHolder = (ViewBannerHolder)holder;
                viewBannerHolder.setAdapter(this.mTopNewses);

                updateViewHolder(viewBannerHolder, news, position);
            }else{
                ViewNormalHolder viewNormalHolder = (ViewNormalHolder)holder;
                updateViewHolder(viewNormalHolder, news, position);
            }
            */
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
        return position==0 ? VT_BANNER : VT_NEWS;
    }

    /**
     * 自定义ViewHolder
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
     * 带Banner的Holder
     */
    /*public class ViewBannerHolder extends ViewNormalHolder {
        @BindView(R.id.news_top_banner)
        RollPagerView mRollPagerView;

        public ViewBannerHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

            this.mRollPagerView.setHintView(new ColorPointHintView(v.getContext(), Color.YELLOW, Color.WHITE));
        }

        public void setAdapter(List<News> topNewses){
            this.mRollPagerView.setAdapter(new TopNewsLoopAdapter(mRollPagerView, topNewses));
        }
    }*/


    /**
     * 添加新闻列表
     * @param newses
     */
    public void addNewses(List<News> newses, String newsDate){
        if(null == newses || newses.size() == 0)
            return;

        int orgSize = this.mNewses.size();

        List<News> newsList = parseNews(newses, newsDate);
        this.mNewses.addAll(newsList);

        // 通知数据变化
        this.notifyItemRangeInserted(orgSize, newses.size());
    }

    // ==================================================================
    // private methods ==================================================
    public void updateViewHolder(ViewNormalHolder viewNormalHolder, News news, int position){
        viewNormalHolder.mNewsTitle.setText(news.getTitle());
        viewNormalHolder.mNewsDate.setText(news.getPublishDate());
        if(!TextUtils.isEmpty(news.getListCoverImage())){
            ImageUtil.displayImage(mContext, news.getListCoverImage(), viewNormalHolder.mNewsCoverImage);
        }

        if(position == 0){
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
            date = DateUtil.format(groupDate, "yyyy年MM月dd日");
        }else{
            date = DateUtil.format(groupDate, "MM月dd日");
        }

        List<News> newsList = new ArrayList<>();
        for (News news : newses){
            news.setPublishDate(date);
            newsList.add(news);
        }
        return newsList;
    }
}
