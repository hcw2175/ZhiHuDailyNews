package com.huchiwei.zhihudailynews.modules.news.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.huchiwei.zhihudailynews.R;
import com.huchiwei.zhihudailynews.core.helper.RetrofitHelper;
import com.huchiwei.zhihudailynews.core.utils.ImageUtil;
import com.huchiwei.zhihudailynews.modules.news.api.NewsService;
import com.huchiwei.zhihudailynews.modules.news.entity.News;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 新闻明细Activity
 *
 * @author huchiwei
 * @version 1.0.0
 */
public class NewsDetailActivity extends AppCompatActivity {
    private static final String TAG = "NewsDetailActivity";

    @BindView(R.id.nd_title)
    TextView mNewsTitle;

    @BindView(R.id.nd_coverImg)
    ImageView mCoverImg;

    @BindView(R.id.nd_image_source)
    TextView mImageSource;

    @BindView(R.id.nd_body)
    TextView mBody;

    private News mNews = new News();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail);

        ButterKnife.bind(this);

        Intent intent = this.getIntent();
        Log.d(TAG, "onCreate: " + intent.getIntExtra("newsId", -1));

        NewsService newsService = RetrofitHelper.createApi(this, NewsService.class);
        newsService.loadNews(intent.getIntExtra("newsId", -1)).enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if(response.isSuccessful()){
                    mNews = response.body();
                    Log.d(TAG, "onResponse: " + mNews.getTitle());

                    ImageUtil.displayImage(NewsDetailActivity.this, mNews.getImage(), mCoverImg);

                    mNewsTitle.setText(mNews.getTitle());
                    mImageSource.setText(mNews.getImage_source());

                    mBody.setText(Html.fromHtml(mNews.getBody()));
                    //mBody.setText(mNews.getBody()));
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Log.e(TAG, "onFailure: 获取新闻消息失败", t);
            }
        });
    }
}
