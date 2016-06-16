package com.huchiwei.zhihudailynews.modules.news.entity;

import java.util.List;

/**
 * 列表新闻信息
 *
 * @author huchiwei
 * @version 1.0.0
 */
public class News4List{

    private String date;                              // 最新日期
    private List<News> stories;                       // 最新新闻
    private List<News> top_stories;                   // 推荐新闻(ViewPaper)

    // =========================================================================
    // setter/getter ===========================================================
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public List<News> getStories() {
        return stories;
    }
    public void setStories(List<News> stories) {
        this.stories = stories;
    }

    public List<News> getTop_stories() {
        return top_stories;
    }
    public void setTop_stories(List<News> top_stories) {
        this.top_stories = top_stories;
    }
}
