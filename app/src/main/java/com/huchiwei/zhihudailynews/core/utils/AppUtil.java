package com.huchiwei.zhihudailynews.core.utils;

import com.huchiwei.zhihudailynews.core.config.AppConfig;

/**
 * App工具类
 *
 * @author huchiwei
 * @version 1.0.0
 */
public class AppUtil {

    /**
     * 判断是否开发模式
     * @return 返回true则表示处于开发模式，否则非开发模式
     */
    public static boolean isDev(){
        return AppConfig.APP_MODEL.equalsIgnoreCase("dev");
    }
}
