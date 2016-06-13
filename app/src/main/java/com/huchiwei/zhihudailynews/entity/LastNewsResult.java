package com.huchiwei.zhihudailynews.entity;

import java.util.List;

/**
 * 最新新闻列表数据
 *
 * @author huchiwei
 * @version 1.0.0
 */
public class LastNewsResult {

    private String date;                              // 最新日期
    private List<News> stories;                       // 最新新闻
    private List<News> topStories;                    // 推荐新闻(ViewPaper)

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

    public List<News> getTopStories() {
        return topStories;
    }
    public void setTopStories(List<News> topStories) {
        this.topStories = topStories;
    }
}
