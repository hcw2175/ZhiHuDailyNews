package com.huchiwei.zhihudailynews.core.base;

import java.io.Serializable;

/**
 * 实体基础类
 *
 * @author huchiwei
 * @version 1.0.0
 */
public abstract class IdEntity implements Serializable {
    private int id;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
