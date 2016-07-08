package com.huchiwei.zhihudailynews.core.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.huchiwei.zhihudailynews.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * 图片处理工具
 *
 * @author huchiwei
 * @version 1.0.0
 */
public class ImageUtil {

    // 声明为volatile,保证访问顺序
    private volatile static ImageLoader mImageLoader = null;

    /**
     * 获取ImageLoader实例
     * @param context 上下文
     * @return ImageLoader实例
     */
    private static ImageLoader getInstance(Context context){
        if(null == mImageLoader){
            synchronized (ImageUtil.class){
                if(null == mImageLoader){ // 双重检验锁,仅第一次调用时实例化
                    mImageLoader = ImageLoader.getInstance();
                    mImageLoader.init(buildConfig(context));
                }
            }
        }
        return mImageLoader;
    }

    /**
     * 构建图片缓存配置
     * @param context 上下文
     * @return ImageLoaderConfiguration
     */
    private static ImageLoaderConfiguration buildConfig(Context context){
        DisplayImageOptions displayImageOptions = buildDisplayOptions(context);
        return new ImageLoaderConfiguration.Builder(context)
                .denyCacheImageMultipleSizesInMemory()                   // 禁止图片缓存不同大小图片
                .memoryCache(new LruMemoryCache(20 * 1024 * 1024))       // 内存缓存大小: 20M
                .memoryCacheSize(20 * 1024 * 1024)                       // 内存缓存大小: 20M
                .diskCacheSize(100 * 1024 * 1024)                        // SD卡缓存大小: 100M
                .diskCacheFileCount(500)                                 // SD卡缓存文件数量: 500
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())  //将保存的时候的URI名称用MD5加密
                .defaultDisplayImageOptions(displayImageOptions)
                .build();
    }

    /**
     * 构建显示图片配置
     * @param context 上下文
     * @return DisplayImageOptions
     */
    private static DisplayImageOptions buildDisplayOptions(Context context){
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.launcher_img)       // 加载中占位图片
                .showImageForEmptyUri(R.drawable.launcher_img)     // 图片为空或错误时的占位图片
                .showImageOnFail(R.drawable.launcher_img)          // 图片显示失败时的占位图片
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                //.imageScaleType(ImageScaleType.EXACTLY)
                .build();
    }

    /**
     * 根据uri显示图片
     * @param context   上下文
     * @param uri       图片URL
     * @param imageView 图片view
     */
    public static void displayImage(Context context, String uri, ImageView imageView){
        getInstance(context).displayImage(uri, imageView);
    }
}
