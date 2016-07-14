package com.huchiwei.zhihudailynews.common.support.recyclerview;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.huchiwei.zhihudailynews.R;

/**
 * RecyclerView二次封装,统一增加下拉刷新、加载更多事件
 *
 * @author huchiwei
 * @version 1.0.0
 */
public class SimpleRecyclerView extends FrameLayout {

    protected SwipeRefreshLayout mSwipeRefresh;
    protected RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;

    private int mLastVisibleItem;

    public SimpleRecyclerView(Context context) {
        super(context, null);
    }

    public SimpleRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.widget_recyclerview, this);

        mSwipeRefresh = (SwipeRefreshLayout) rootView.findViewById(R.id.widget_base_swipe_refresh);
        mSwipeRefresh.setColorSchemeResources(R.color.swipe_refresh_red, R.color.swipe_refresh_blue, R.color.swipe_refresh_green);

        mLayoutManager = new LinearLayoutManager(context);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.widget_base_recycler_view);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
    }

    /**
     * 设计布局管理器
     * @param layoutManager
     */
    public void setLayoutManager(RecyclerView.LayoutManager layoutManager){
        this.mLayoutManager = layoutManager;
        this.mRecyclerView.setLayoutManager(layoutManager);
    }

    /**
     * 设置Adapter
     * @param adapter
     */
    public void setAdapter(RecyclerView.Adapter adapter){
        this.mRecyclerView.setAdapter(adapter);
    }

    /**
     * 设置点击事件
     * @param itemClickListener
     */
    public void addOnItemTouchListener(RecyclerItemClickListener itemClickListener){
        this.mRecyclerView.addOnItemTouchListener(itemClickListener);
    }

    /**
     * 设置下拉刷新
     * @param onRefreshListener
     */
    public void addOnRefreshListener(SwipeRefreshLayout.OnRefreshListener onRefreshListener){
        this.mSwipeRefresh.setOnRefreshListener(onRefreshListener);
    }

    public void setRefreshing(boolean refreshing){
        this.mSwipeRefresh.setRefreshing(refreshing);
    }

    /**
     * 添加滚动到底加载更多事件监听
     *
     * @param onLoadMoreListener
     */
    public void addOnLoadMoreListener(final OnLoadMoreListener onLoadMoreListener) {
        this.addScrollListener(onLoadMoreListener, null);
    }

    /**
     * 添加滚动事件以及到底加载更多事件监听
     * @param onLoadMoreListener
     * @param onScrollListener
     */
    public void addScrollListener(final OnLoadMoreListener onLoadMoreListener, final OnScrollListener onScrollListener){
        this.mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mLastVisibleItem + 1 == mRecyclerView.getAdapter().getItemCount()) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    if (mSwipeRefresh.isRefreshing())
                        mSwipeRefresh.setRefreshing(false);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(mLayoutManager instanceof LinearLayoutManager)
                    mLastVisibleItem = ((LinearLayoutManager)mLayoutManager).findLastVisibleItemPosition();

                if(null != onScrollListener)
                    onScrollListener.onScrolled();
            }
        });
    }

    public RecyclerView getRecyclerView(){
        return this.mRecyclerView;
    }

    public SwipeRefreshLayout getSwipeRefresh() {
        return mSwipeRefresh;
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    // =====================================================================
    // OnLoadMoreListener ==================================================
    public interface OnLoadMoreListener{
        void onLoadMore();
    }

    public interface OnScrollListener{
        void onScrolled();
    }
}
