package com.huchiwei.zhihudailynews.core.utils;

import android.content.Context;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * 图片处理工具
 *
 * @author huchiwei
 * @version 1.0.0
 */
public class ImageUtil {

    private static ImageLoader mImageLoader = null;

    /**
     * 获取ImageLoader实例
     * @param context 上下文
     * @return ImageLoader实例
     */
    public static synchronized ImageLoader getInstance(Context context){
        if(null != mImageLoader)
            return mImageLoader;

        mImageLoader = ImageLoader.getInstance();
        mImageLoader.init(ImageLoaderConfiguration.createDefault(context));
        return mImageLoader;
    }

    /**
     * 显示图片
     * @param context 上下文
     * @param url      图片URL
     */
    public static void displayImage(Context context, String url, ImageView imageView){
        getInstance(context).displayImage(url, imageView);
    }
}
