package com.huchiwei.zhihudailynews.modules.news.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.huchiwei.zhihudailynews.R;
import com.huchiwei.zhihudailynews.common.ui.ToolbarActivity;
import com.huchiwei.zhihudailynews.core.helper.RetrofitHelper;
import com.huchiwei.zhihudailynews.core.utils.ImageUtil;
import com.huchiwei.zhihudailynews.core.utils.ToastUtil;
import com.huchiwei.zhihudailynews.modules.news.api.NewsService;
import com.huchiwei.zhihudailynews.modules.news.entity.News;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 新闻明细Activity
 *
 * @author huchiwei
 * @version 1.0.0
 */
public class NewsDetailActivity extends ToolbarActivity {
    private static final String TAG = "NewsDetailActivity";

    @BindView(R.id.nd_coverImg)
    ImageView mCoverImg;

    @BindView(R.id.nd_title)
    TextView mTitle;

    @BindView(R.id.nd_image_source)
    TextView mImageSource;

    @BindView(R.id.nd_body)
    WebView mBody;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);

        if(null != getSupportActionBar()){
            getSupportActionBar().setTitle("");
        }

        // 禁止JS
        mBody.getSettings().setJavaScriptEnabled(false);
        // 禁止缩放
        mBody.getSettings().setSupportZoom(false);
        // 禁止缩放工具
        mBody.getSettings().setBuiltInZoomControls(false);
        // 单列显示内容
        mBody.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //缓存
        mBody.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        // 延缓图片加载
        mBody.getSettings().setBlockNetworkImage(true);

        Intent intent = this.getIntent();
        RetrofitHelper.createApi(NewsService.class)
                .loadNews(intent.getIntExtra("newsId", -1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<News>() {
                    @Override
                    public void onCompleted() {}

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.show("新闻消息详情获取失败");
                        Log.e(TAG, "onError: 获取新闻消息失败", e);
                    }

                    @Override
                    public void onNext(News news) {
                        if(null == news){
                            ToastUtil.show("无法获取新闻详情");
                            return;
                        }

                        Log.d(TAG, "onResponse: " + news.getTitle());
                        mTitle.setText(news.getTitle());
                        mImageSource.setText(news.getImage_source());
                        ImageUtil.displayImage(NewsDetailActivity.this, mCoverImg, news.getImage());

                        String html = news.getBody();
                        // 替换头部图片空白占位html
                        html = html.replace("<div class=\"img-place-holder\">", "").replace("<div class=\"headline\">", "");
                        if(null != news.getCss()){
                            html = "<link rel=\"stylesheet\" type=\"text/css\" href=\"" + news.getCss().get(0) +"\" />" + html;
                        }
                        mBody.loadData(html, "text/html; charset=utf-8" , "utf-8");
                    }
                });
    }

    @Override
    protected boolean showBackButton() {
        return true;
    }
}
