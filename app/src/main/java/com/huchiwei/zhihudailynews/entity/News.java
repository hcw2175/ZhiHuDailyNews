package com.huchiwei.zhihudailynews.entity;

import java.util.List;

/**
 * 知乎新闻内容
 *
 * @author huchiwei
 * @version 1.0.0
 */
public class News {

    private Section section;                                   // 所属栏目

    private int id;                                            // 新闻id
    private String title;                                      // 标题
    private String body;                                       // 正文
    private String image;                                      // 封面图片
    private String image_source;                               // 图片版权
    private String share_url;                                  // 分享的URL
    private String ga_prefix;                                  // 供 Google Analytics 使用
    private int type;                                          // 新闻类型, 0:普通新闻，1：推荐新闻

    private List<RecommendersBean> recommenders;               // 推荐者
    private List<String> css;                                  // 样式，供手机端的 WebView(UIWebView) 使用

    private List<String> images;                               // 列表封面图片


    /**
     * 推荐用户
     */
    public static class RecommendersBean {
        private String avatar;

        public String getAvatar() {
            return avatar;
        }
        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
    }

    // =========================================================================
    // setter/getter ===========================================================
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

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<RecommendersBean> getRecommenders() {
        return recommenders;
    }
    public void setRecommenders(List<RecommendersBean> recommenders) {
        this.recommenders = recommenders;
    }

    public List<String> getCss() {
        return css;
    }
    public void setCss(List<String> css) {
        this.css = css;
    }

    public List<String> getImages() {
        return images;
    }
    public void setImages(List<String> images) {
        this.images = images;
    }
}
