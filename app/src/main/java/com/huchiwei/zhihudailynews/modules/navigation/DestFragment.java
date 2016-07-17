package com.huchiwei.zhihudailynews.modules.navigation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.huchiwei.zhihudailynews.R;

/**
 * 导航目的地Fragment管理
 *
 * @author huchiwei
 * @version 1.0.0
 */
public class DestFragment extends NavFragment {

    public static DestFragment newInstance() {
        return new DestFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.setNavFragmentContent((FrameLayout) container.findViewById(R.id.nav_dest_fragment));
        return inflater.inflate(R.layout.nav_dest_fragment, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
