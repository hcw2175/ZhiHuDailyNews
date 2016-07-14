package com.huchiwei.zhihudailynews.core.cache;

import com.huchiwei.zhihudailynews.core.utils.AppUtil;

/**
 * 缓存处理辅助工具类
 *
 * @author huchiwei
 * @version 1.0.0
 */
public class CacheUtil {
    public static final String CACHE_FILE_NAME = "ZhihuDailyNews";

    // 声明为volatile,保证访问顺序
    private volatile static ACache mACache = null;

    /**
     * 获取ACache实例
     * @return ImageLoader实例
     */
    private static ACache getInstance(){
        if(null == mACache){
            synchronized (CacheUtil.class){
                if(null == mACache){ // 双重检验锁,仅第一次调用时实例化
                    mACache = ACache.get(AppUtil.getContext());
                }
            }
        }
        return mACache;
    }

    /**
     * 缓存String类型数据
     * @param key  缓存key
     */
    public static void put(String key, String value){
        getInstance().put(key, value);
    }

    /**
     * 获取String类型缓存数据
     * @param key  缓存key
     * @return 若不存在返回null
     */
    public static String getString(String key){
        return getInstance().getAsString(key);
    }
}
