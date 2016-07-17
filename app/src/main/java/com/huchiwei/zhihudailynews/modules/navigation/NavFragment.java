package com.huchiwei.zhihudailynews.modules.navigation;

import android.support.v4.app.Fragment;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.huchiwei.zhihudailynews.R;

/**
 * 导航Fragment基类
 *
 * @author huchiwei
 * @version 1.0.0
 */
public abstract class NavFragment extends Fragment {

    private FrameLayout navFragmentContent;

    public void fadeOut(){
        if (navFragmentContent != null) {
            Animation fadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
            navFragmentContent.startAnimation(fadeOut);
        }
    }

    public void fadeIn(){
        if (navFragmentContent != null) {
            Animation fadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
            navFragmentContent.startAnimation(fadeOut);
        }
    }

    public FrameLayout getNavFragmentContent() {
        return navFragmentContent;
    }
    public void setNavFragmentContent(FrameLayout navFragmentContent) {
        this.navFragmentContent = navFragmentContent;
    }
}
