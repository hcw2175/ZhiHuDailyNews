package com.huchiwei.zhihudailynews.modules.news.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huchiwei.zhihudailynews.R;
import com.huchiwei.zhihudailynews.core.utils.ImageUtil;
import com.huchiwei.zhihudailynews.modules.news.activity.NewsDetailActivity;
import com.huchiwei.zhihudailynews.modules.news.entity.News;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;

import java.util.List;

/**
 * 新闻推荐消息轮播Adapter
 *
 * @author huchiwei
 * @version 1.0.0
 */
public class TopNewsLoopAdapter extends LoopPagerAdapter {

    private List<News> mTopNews;

    public TopNewsLoopAdapter(RollPagerView viewPager) {
        super(viewPager);
    }

    public TopNewsLoopAdapter(RollPagerView viewPager, List<News> topNews){
        super(viewPager);
        this.mTopNews = topNews;
    }

    @Override
    public View getView(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.adapter_news_list_banner_view, container, false);

        final News news = this.mTopNews.get(position);
        ImageView imageView = (ImageView)view.findViewById(R.id.news_banner_image);
        ImageUtil.displayImage(container.getContext(), imageView, news.getImage());

        TextView textView = (TextView)view.findViewById(R.id.news_banner_title);
        textView.setText(news.getTitle());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NewsDetailActivity.class);
                intent.putExtra("newsId", news.getId());
                v.getContext().startActivities(new Intent[]{intent});
            }
        });
        return view;
    }

    @Override
    protected int getRealCount() {
        return null==this.mTopNews ? 0 : this.mTopNews.size();
    }
}
