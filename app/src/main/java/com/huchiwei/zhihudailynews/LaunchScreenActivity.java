package com.huchiwei.zhihudailynews;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huchiwei.zhihudailynews.core.cache.CacheUtil;
import com.huchiwei.zhihudailynews.core.helper.RetrofitHelper;
import com.huchiwei.zhihudailynews.core.utils.ImageUtil;
import com.huchiwei.zhihudailynews.modules.news.api.NewsService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * App启动画面Activity
 *
 * @author huchiwei
 * @version 1.0.0
 */
public class LaunchScreenActivity extends AppCompatActivity {
    private static final String TAG = "LaunchScreenActivity";

    @BindView(R.id.launch_background)
    ImageView mLauncherBackground;

    @BindView(R.id.launch_background_source)
    TextView mLauncherBackgroundSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_screen);
        ButterKnife.bind(LaunchScreenActivity.this);

        Log.d(TAG, "onCreate: LaunchScreenActivity");
        if(!TextUtils.isEmpty(CacheUtil.getString("start_image"))){
            String startImageSource = CacheUtil.getString("start_image_source");
            if(!TextUtils.isEmpty(startImageSource)){
                startImageSource = "@" + startImageSource;
                mLauncherBackgroundSource.setText(startImageSource);
                mLauncherBackgroundSource.setVisibility(View.VISIBLE);
            }

            ImageUtil.displayImage(LaunchScreenActivity.this, mLauncherBackground, CacheUtil.getString("start_image"));
        }else{
            // 显示默认图片
            ImageUtil.displayImage(LaunchScreenActivity.this, mLauncherBackground, "drawable://" + R.drawable.launcher_img);
        }

        // 缓存启动图片数据
        loadStartImage();

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
            Intent intent = new Intent(LaunchScreenActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            }
        }, 3000);
    }

    /**
     * 启动线程去下载启动图片
     */
    private void loadStartImage(){
        RetrofitHelper.createApi(NewsService.class)
                .loadStartImage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseBody>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: 启动图片加载失败", e);
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            String result = responseBody.string();
                            Log.i(TAG, "subscribe call result: " + result, null);

                            // 设置到缓存中
                            JSONObject json = new JSONObject(result);
                            CacheUtil.put("start_image_source", json.getString("text"));
                            CacheUtil.put("start_image", json.getString("img"));
                        } catch (JSONException | IOException e) {
                            Log.e(TAG, "onNext: 启动图片解析失败", null);
                            e.printStackTrace();
                        }
                    }
                });
    }
}
