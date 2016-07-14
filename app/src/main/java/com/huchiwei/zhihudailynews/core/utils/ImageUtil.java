package com.huchiwei.zhihudailynews.core.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.huchiwei.zhihudailynews.R;

/**
 * 图片处理工具
 *
 * @author huchiwei
 * @version 1.0.0
 */
public class ImageUtil {

    /**
     * 根据uri显示图片
     * @param context   上下文
     * @param imageView 图片view
     * @param uri       图片URL
     */
    public static void displayImage(Context context, ImageView imageView, String uri){
        Glide
                .with(context)
                .load(uri)
                .centerCrop()
                .error(R.drawable.launcher_img)
                .into(imageView);
    }
}
