package com.huchiwei.zhihudailynews.modules.navigation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 导航页面Fragment适配器
 *
 * @author huchiwei
 * @version 1.0.0
 */
public class NavFragmentAdapter extends FragmentPagerAdapter{

    private List<Fragment> mFragments = new ArrayList<>();
    private NavFragment mCurrentFragment;

    public NavFragmentAdapter(FragmentManager fm) {
        super(fm);

        mFragments.add(HomeFragment.newInstance());
        mFragments.add(DestFragment.newInstance());
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);

        if(getCurrentFragment() != object){
            mCurrentFragment = (NavFragment) object;
        }
    }

    public Fragment getCurrentFragment() {
        return mCurrentFragment;
    }
}
