package com.huchiwei.zhihudailynews.entity;

/**
 * 栏目信息
 *
 * @author huchiwei
 * @version 1.0.0
 */
public class Section {
    private String thumbnail;
    private int id;
    private String name;

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
