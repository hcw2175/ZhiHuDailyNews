package com.huchiwei.zhihudailynews.modules.news.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huchiwei.zhihudailynews.R;
import com.huchiwei.zhihudailynews.core.utils.DateUtil;
import com.huchiwei.zhihudailynews.core.utils.ImageUtil;
import com.huchiwei.zhihudailynews.modules.news.entity.News;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.Date;
import java.util.List;

/**
 * 新闻列表适配器
 *
 * @author huchiwei
 * @version 1.0.0
 */
public class NewsAdapter extends RecyclerView.Adapter {
    private static final String TAG = "NewsAdapter";
    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_GROUP = 1;

    private Context mContext;
    private String mDate;
    private List<News> mNewses;
    private News firstNews;                 // 每次请求第一条新闻,用于判断显示不同布局

    private int size = 0;

    public NewsAdapter(Context context, String date, List<News> newses) {
        this.mContext = context;
        this.mNewses = newses;
        firstNews = newses.get(0);

        Date groupDate = DateUtil.parseDate(date);
        if(DateUtil.isToday(groupDate)){
            this.mDate = "今日热闻";
        }else if(DateUtil.isYesterday(groupDate)){
            this.mDate = "昨日热闻";
        }else if(DateUtil.year(groupDate) != DateUtil.year(new Date())){
            this.mDate = DateUtil.format(groupDate, "yyyy年MM月dd日");
        }else{
            this.mDate = DateUtil.format(groupDate, "MM月dd日");
        }
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
        if(viewType == TYPE_GROUP){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_group_card_view, parent, false);
            return new ViewGroupHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_card_view, parent, false);
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
        if(null != news){
            if(holder instanceof ViewGroupHolder){
                ViewGroupHolder viewGroupHolder = (ViewGroupHolder)holder;
                viewGroupHolder.mNewsDate.setText(this.mDate);
                this.bindViewNormalHolderData(news, viewGroupHolder.mNewsTitle, viewGroupHolder.mNewsCoverImage);
            }else{
                // 绑定viewNormalHolder数据
                ViewNormalHolder viewNormalHolder = (ViewNormalHolder)holder;
                this.bindViewNormalHolderData(news, viewNormalHolder.mNewsTitle, viewNormalHolder.mNewsCoverImage);
            }
        }
    }

    /**
     * 获取数据数量
     * @return
     */
    @Override
    public int getItemCount() {
        return mNewses == null ? 0 : mNewses.size();
    }

    /**
     * 获取元素布局类型
     * @param position 布局类型
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_GROUP : TYPE_NORMAL;
    }

    /**
     * 自定义ViewHolder
     */
    public static class ViewNormalHolder extends RecyclerView.ViewHolder {
        public TextView mNewsTitle;
        public ImageView mNewsCoverImage;

        public ViewNormalHolder(View v) {
            super(v);
            mNewsTitle = (TextView) v.findViewById(R.id.news_title);
            mNewsCoverImage = (ImageView) v.findViewById(R.id.news_cover_img);
        }
    }

    /**
     * 自定义ViewHolder, 用于显示带日期布局
     */
    public static class ViewGroupHolder extends ViewNormalHolder {
        public TextView mNewsDate;

        public ViewGroupHolder(View v) {
            super(v);
            mNewsDate = (TextView) v.findViewById(R.id.news_group_date);
        }
    }

    /**
     * 添加新闻列表
     * @param newses
     */
    public void addNewses(String newsDate, List<News> newses){
        if(null == newses || newses.size() == 0)
            return;

        this.mDate = newsDate;
        this.mNewses.addAll(newses);
        firstNews = newses.get(0);
        this.notifyDataSetChanged();
    }

    // ==================================================================
    // private methods ==================================================
    private void bindViewNormalHolderData(News news, TextView mNewsTitle, ImageView mNewsCoverImage){
        mNewsTitle.setText(news.getTitle());
        if(!TextUtils.isEmpty(news.getListCoverImage())){
            ImageUtil.displayImage(mContext, news.getListCoverImage(), mNewsCoverImage);
        }
    }
}
