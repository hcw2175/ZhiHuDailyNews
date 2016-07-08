package com.huchiwei.zhihudailynews;

/**
 * APP常量配置
 *
 * @author hucw
 * @version 1.0.0
 */
public class AppConfig {
    /**
     * APP开发模式，值：{@value}
     * 三种情况：dev|stage|product
     */
    public static final String APP_MODEL = "dev";

    /** 生产环境基本URL, 值为： {@value} **/
    public static final String BASE_PRODUCT_URL = "http://news-at.zhihu.com/api/4/";
    /** 测试环境基本URL, 值为： {@value} **/
    public static final String BASE_STAGE_URL = "http://news-at.zhihu.com/api/4/";
    /** 开发环境基本URL, 值为： {@value} **/
    public static final String BASE_DEV_URL = "http://news-at.zhihu.com/api/4/";

    /** 图片URL **/
    public static final String BASE_IMAGE_URL = "http://news-at.zhihu.com/api/4/";

    /**
     * 获取api基本URL, baseUrl总是以/结束，@URL不要以/开头
     * @return BaseUrl
     */
    public static String getBaseUrl(){
        if(APP_MODEL.equalsIgnoreCase("dev"))
            return BASE_DEV_URL;
        if(APP_MODEL.equalsIgnoreCase("stage"))
            return BASE_STAGE_URL;
        return BASE_PRODUCT_URL;
    }
}
