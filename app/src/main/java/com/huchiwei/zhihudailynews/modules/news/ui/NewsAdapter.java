package com.huchiwei.zhihudailynews.modules.news.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huchiwei.zhihudailynews.R;
import com.huchiwei.zhihudailynews.modules.news.entity.News;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.List;

/**
 * 新闻列表适配器
 *
 * @author huchiwei
 * @version 1.0.0
 */
public class NewsAdapter extends RecyclerView.Adapter {
    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_GROUP = 1;

    private String mDate;
    private List<News> mNewses;

    private ImageLoader imageLoader;

    public NewsAdapter(Context mContext, String mDate, List<News> mNewses) {
        this.mDate = mDate;
        this.mNewses = mNewses;

        // 初始化ImageLoader实例
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
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
     * @param position 元素位置
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

    // ==================================================================
    // private methods ==================================================
    private void bindViewNormalHolderData(News news, TextView mNewsTitle, ImageView mNewsCoverImage){
        mNewsTitle.setText(news.getTitle());
        if(!TextUtils.isEmpty(news.getListCoverImage())){
            imageLoader.displayImage(news.getListCoverImage(), mNewsCoverImage);
        }
    }
}
