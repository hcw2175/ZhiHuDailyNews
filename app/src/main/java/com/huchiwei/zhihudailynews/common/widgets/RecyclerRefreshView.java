package com.huchiwei.zhihudailynews.common.widgets;

import android.content.Context;
import android.util.AttributeSet;

import com.lhh.ptrrv.library.PullToRefreshRecyclerView;

/**
 * SwipeRefreshLayout通用组件
 *
 * @author huchiwei
 * @version 1.0.0
 */
public class RecyclerRefreshView extends PullToRefreshRecyclerView {

    public RecyclerRefreshView(Context context) {
        super(context);
    }

    public RecyclerRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
}
