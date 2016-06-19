package com.huchiwei.zhihudailynews.modules.news.ui;

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

    public TopNewsLoopAdapter(RollPagerView viewPager){
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
        ImageView view = new ImageView(container.getContext());
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // 显示图片
        ImageUtil.displayImage(container.getContext(), this.topImages.get(position), view);
        return view;
    }

    @Override
    protected int getRealCount() {
        return null==this.topImages ? 0 : this.topImages.size();
    }
}
