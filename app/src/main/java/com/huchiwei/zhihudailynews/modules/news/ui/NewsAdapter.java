package com.huchiwei.zhihudailynews.modules.news.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huchiwei.zhihudailynews.R;
import com.huchiwei.zhihudailynews.modules.news.entity.News;

import java.util.List;

/**
 * 新闻列表适配器
 *
 * @author huchiwei
 * @version 1.0.0
 */
public class NewsAdapter extends ArrayAdapter<News> {

    private int resourceId;

    /**
     * 构建函数
     * @param context              上下文
     * @param resource
     * @param objects
     */
    public NewsAdapter(Context context, int resource, List<News> objects) {
        super(context, resource, objects);
        this.resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 获取当前实例
        News news = getItem(position);

        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        ImageView imageView = (ImageView)view.findViewById(R.id.news_image);
        TextView textView = (TextView)view.findViewById(R.id.news_title);

        List<String> images = news.getImages();
        if(images.size() > 0)
            imageView.setImageResource(getContext().getResources().getIdentifier(images.get(0), "drawable", "com.huchiwei.zhihudailynews.modules.news.ui"));
        textView.setText(news.getTitle());
        return view;
    }
}
