package com.huchiwei.zhihudailynews.core.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.widget.ImageView;

import com.huchiwei.zhihudailynews.R;
import com.huchiwei.zhihudailynews.core.Constants;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.io.File;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
        // 获取缓存目标
        File cacheFile = new File(AppUtil.getContext().getExternalCacheDir(), Constants.CACHE_IMAGES);

        DisplayImageOptions displayImageOptions = buildDisplayOptions();
        return new ImageLoaderConfiguration.Builder(context)
                .denyCacheImageMultipleSizesInMemory()                           // 禁止图片缓存不同大小图片
                .memoryCache(new WeakMemoryCache())                              // 内存缓存
                //.memoryCacheSize(2 * 1024 * 1024)
                .diskCache(new UnlimitedDiskCache(cacheFile))                    // SD卡缓存设置
                //.diskCacheSize(50 * 1024 * 1024)                               // SD卡缓存大小: 100M
                //.diskCacheFileCount(100)                                       // SD卡缓存文件数量: 500
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())          //将保存的时候的URI名称用MD5加密
                .defaultDisplayImageOptions(displayImageOptions)
                //.writeDebugLogs()
                .build();
    }

    /**
     * 构建显示图片配置
     * @return DisplayImageOptions
     */
    private static DisplayImageOptions buildDisplayOptions(){
        return new DisplayImageOptions.Builder()
                //.showImageOnLoading(R.drawable.launcher_img)     // 加载中占位图片
                //.showImageForEmptyUri(R.drawable.launcher_img)   // 图片为空或错误时的占位图片
                .showImageOnFail(R.drawable.launcher_img)          // 图片显示失败时的占位图片
                .cacheInMemory(true)
                .cacheOnDisk(true)
                //.considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                //.handler(new Handler())
                //.imageScaleType(ImageScaleType.EXACTLY)
                .build();
    }

    /**
     * 根据uri显示图片
     * @param context   上下文
     * @param uri       图片URL
     * @param imageView 图片view
     */
    public static void displayImage(final Context context, final String uri, final ImageView imageView){
        /*Observable.just(uri)
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        getInstance(context).displayImage(s, imageView);
                    }
                });*/
        getInstance(context).displayImage(uri, imageView);
    }
}
