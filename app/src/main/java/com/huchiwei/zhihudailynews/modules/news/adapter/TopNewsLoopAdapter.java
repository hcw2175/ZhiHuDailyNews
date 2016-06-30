package com.huchiwei.zhihudailynews.modules.news.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.huchiwei.zhihudailynews.core.utils.ImageUtil;
import com.huchiwei.zhihudailynews.modules.news.entity.News;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.LoopPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 置顶新闻适配器
 *
 * @author huchiwei
 * @version 1.0.0
 */
public class TopNewsLoopAdapter extends LoopPagerAdapter{

    private List<String> topImages;

    public TopNewsLoopAdapter(RollPagerView viewPager) {
        super(viewPager);
    }

    public TopNewsLoopAdapter(RollPagerView viewPager, List<News> topNews){
        super(viewPager);

        topImages = new ArrayList<>();
        if(topNews.size() > 0){
            for(News news : topNews){
                topImages.add(news.getImage());
            }
        }
    }

    @Override
    public View getView(ViewGroup container, int position) {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // 图片
        ImageView imageView = new ImageView(container.getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(layoutParams);
        ImageUtil.displayImage(container.getContext(), this.topImages.get(position), imageView);

        return imageView;
    }

    @Override
    protected int getRealCount() {
        return null==this.topImages ? 0 : this.topImages.size();
    }
}
