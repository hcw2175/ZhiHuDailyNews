package com.huchiwei.zhihudailynews.modules.news.entity;

import com.huchiwei.zhihudailynews.core.base.IdEntity;

import java.util.List;

/**
 * 新闻明细实体类
 *
 * @author huchiwei
 * @version 1.0.0
 */

public class News extends IdEntity {

    private String title;                                      // 标题
    private String body;                                       // 正文
    private String image;                                      // 封面大图，新闻明细或置顶新闻都有该属性
    private String image_source;                               // 图片版权
    private String share_url;                                  // 分享的URL
    private int type;                                          // 新闻类型, 0:普通新闻，1：推荐新闻
    private String publishDate;                                // 发布时间
    private List<String> css;                                  // 详情页显示样式

    private List<String> images;                               // 封面图, 列表使用

    // ==========================================================
    // methods ==================================================
    public String getListCoverImage(){
        return images != null ? images.get(0) : "";
    }

    // ==========================================================
    // setter/getter ============================================
    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }

    public String getImage_source() {
        return image_source;
    }
    public void setImage_source(String image_source) {
        this.image_source = image_source;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public String getShare_url() {
        return share_url;
    }
    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }

    public List<String> getImages() {
        return images;
    }
    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getPublishDate() {
        return publishDate;
    }
    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public List<String> getCss() {
        return css;
    }
    public void setCss(List<String> css) {
        this.css = css;
    }
}
