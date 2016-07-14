package com.huchiwei.zhihudailynews.common.support.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.huchiwei.zhihudailynews.R;
import com.huchiwei.zhihudailynews.modules.news.adapter.NewsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * RecyclerView Adapter的简单封装
 *
 * @author huchiwei
 * @version 1.0.0
 */
public abstract class SimpleRecyclerViewAdapter<T,VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    /** View类型: Header, 值为: {@value} **/
    protected static final int VT_HEADER = 0;
    /** View类型: Body, 值为: {@value} **/
    protected static final int VT_BODY = 1;
    /** View类型: Footer, 值为: {@value} **/
    protected static final int VT_FOOTER = 2;

    protected Context mContext;
    protected LayoutInflater mLayoutInflater;

    private List<T> mData = new ArrayList<>();         // 数据源

    // =====================================================================
    // Constructor =========================================================
    public SimpleRecyclerViewAdapter(Context context) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    // =========================================================================
    // Override Methods =========================================================
    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == VT_HEADER){
            return this.getHeaderView(parent);
        }else if(viewType == VT_FOOTER){
            return getFooterView(parent);
        }else{
            return this.getItemView(parent);
        }
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        this.bindViewData(holder, position);
    }

    @Override
    public int getItemCount() {
        return null != mData ? mData.size() : 0;
    }

    // =========================================================================
    // Custom Methods =========================================================
    /**
     * 添加Header View, 请返回对应layout id
     * @return 默认-1, 不添加
     */
    protected VH getHeaderView(ViewGroup parent){
        return null;
    }

    /**
     * 添加Footer View, 请返回对应layout id
     * @return 默认-1, 不添加
     */
    protected VH getFooterView(ViewGroup parent){
        return null;
    }

    /**
     * 获取指定数据项
     * @param position 数据位置
     * @return 数据项
     */
    public T getDataItem(int position){
        if(null == this.mData)
            return null;
        if(position > this.mData.size() -1)
            return null;
        return this.mData.get(position);
    }

    /**
     * 添加数据
     * @param newData 新数据
     */
    public void addData(List<T> newData){
        if(null == newData)
            return ;

        if(null == mData){
            mData = newData;
        }else{
            mData.addAll(newData);
        }
        notifyDataSetChanged();
    }

    /**
     * 更新数据
     * @param newData 新数据
     */
    public void updateData(List<T> newData){
        if(null == newData)
            return ;

        mData = newData;
        notifyDataSetChanged();
    }

    // =================================================
    // 待实现方法 ========================================
    protected abstract VH getItemView(ViewGroup parent);

    protected abstract void bindViewData(VH viewHolder, int position);
}
