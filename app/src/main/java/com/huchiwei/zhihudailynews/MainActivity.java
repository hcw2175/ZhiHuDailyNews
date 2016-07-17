package com.huchiwei.zhihudailynews;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;
import com.huchiwei.zhihudailynews.common.ui.ToolbarActivity;
import com.huchiwei.zhihudailynews.modules.navigation.NavFragment;
import com.huchiwei.zhihudailynews.modules.navigation.NavFragmentAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * MainActivity
 *
 * @author huchiwei
 * @version 1.0.0
 */
public class MainActivity extends ToolbarActivity{
    private final String TAG = this.getClass().getName();

    @BindView(R.id.nav_fragment_paper)
    AHBottomNavigationViewPager mNavViewPager;

    private NavFragment mCurrentFragment;
    private NavFragmentAdapter mNavFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // 初始化AHBottomNavigation
        AHBottomNavigation mBottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        mBottomNavigation.setForceTitlesDisplay(true);

        AHBottomNavigationAdapter navigationAdapter = new AHBottomNavigationAdapter(this, R.menu.menu_bottom_navigation);
        navigationAdapter.setupWithBottomNavigation(mBottomNavigation, null);

        // 导航Fragment内容管理
        mNavFragmentAdapter = new NavFragmentAdapter(getSupportFragmentManager());
        mNavViewPager.setAdapter(mNavFragmentAdapter);
        mCurrentFragment = (NavFragment) mNavFragmentAdapter.getCurrentFragment();

        // Set listeners
        mBottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                Log.d(TAG, "onTabSelected: " + position);

                // 获取当前Fragment
                if (mCurrentFragment == null) {
                    mCurrentFragment = (NavFragment) mNavFragmentAdapter.getCurrentFragment();
                }

                // 已选中忽略
                if(wasSelected)
                    return true;

                // 先隐藏已选中Fragment
                if (mCurrentFragment != null) {
                    mCurrentFragment.fadeOut();
                }

                // 重新设置对应position的Fragment并重新显示
                mNavViewPager.setCurrentItem(position, false);
                mCurrentFragment = (NavFragment) mNavFragmentAdapter.getCurrentFragment();
                mCurrentFragment.fadeIn();

                return true;
            }
        });
    }

    @Override
    protected boolean showBackButton() {
        return false;
    }

    /**
     * 退到桌面重新打开，不再显示Launcher Screen
     */
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}
