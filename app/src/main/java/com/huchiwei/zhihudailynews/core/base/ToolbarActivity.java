package com.huchiwei.zhihudailynews.core.base;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.huchiwei.zhihudailynews.R;

/**
 * Activity抽象类
 *
 * @author huchiwei
 * @version 1.0.0
 */
public abstract class ToolbarActivity extends AbstractActivity{
    private LinearLayout rootLayout;

    private Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 这句很关键，注意是调用父类的方法
        super.setContentView(R.layout.activity_base);

        // 经测试在代码里直接声明透明状态栏更有效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);

            /*if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
                //将侧边栏顶部延伸至status bar
                mDrawerLayout.setFitsSystemWindows(true);
                //将主页面顶部延伸至status bar;虽默认为false,但经测试,DrawerLayout需显示设置
                mDrawerLayout.setClipToPadding(false);
            }*/
        }

        initToolbar();
    }

    @Override
    public void setContentView(int layoutId) {
        setContentView(View.inflate(this, layoutId, null));
    }

    @Override
    public void setContentView(View view) {
        rootLayout = (LinearLayout) findViewById(R.id.root_layout);
        if (rootLayout == null) return;
        rootLayout.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        initToolbar();
    }

    private void initToolbar() {
        mToolBar = (Toolbar) findViewById(R.id.widget_toolbar);
        if (mToolBar != null) {
            setSupportActionBar(mToolBar);

            if(this.showBackButton() && null != getSupportActionBar()){
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
            }
        }
    }

    /**
     * 是否需要显示返回按钮
     * @return true表示需要,否则不需要
     */
    protected abstract boolean showBackButton();
}
