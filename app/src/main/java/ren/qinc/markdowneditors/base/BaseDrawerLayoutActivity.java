package ren.qinc.markdowneditors.base;

import android.content.res.ColorStateList;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;

import butterknife.Bind;
import ren.qinc.markdowneditors.R;
import ren.qinc.markdowneditors.utils.SystemBarUtils;


/**
 * 带侧滑菜单的Activity
 * Created by 沈钦赐 on 16/1/15.
 */
public abstract class BaseDrawerLayoutActivity extends BaseToolbarActivity implements NavigationView.OnNavigationItemSelectedListener {
    @Bind(R.id.id_drawer_layout)
    protected DrawerLayout mDrawerLayout;
    @Bind(R.id.id_navigation_view)
    protected NavigationView mNavigationView;

    protected DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    protected NavigationView getNavigationView() {
        return mNavigationView;
    }

    @Override
    protected void init() {
        super.init();
        if (mDrawerLayout == null || mNavigationView == null) // 如果布局文件没有找到toolbar,则不设置actionbar
        {
            throw new IllegalStateException(this.getClass().getSimpleName() + ":要使用BaseDrawerLayoutActivity，必须在布局里面增加id为‘id_drawer’的DrawerLayout");
        }
        initDrawer();
    }

    protected void initStatusBar() {
        SystemBarUtils.tintStatusBarForDrawer(this,mDrawerLayout, getResources().getColor(R.color.colorPrimary));
    }
    private void initDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, getToolbar(), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();


        mNavigationView.setNavigationItemSelectedListener(this);

        ColorStateList colorStateList = new ColorStateList(
                new int[][]{{android.R.attr.state_checked, android.R.attr.state_enabled},
                        {android.R.attr.state_enabled},
                        {}},
                new int[]{BaseApplication.color(R.color.colorPrimary), BaseApplication.color(R.color.colorSecondaryText), 0xffDCDDDD});
        mNavigationView.setItemIconTintList(colorStateList);//设置图标的颜色变化
        mNavigationView.setItemTextColor(colorStateList);//设置item的颜色变化
        if (getDefaultMenuItemId() > 0)
            mNavigationView.setCheckedItem(getDefaultMenuItemId());//设置默认选择
    }

    /**
     * 默认选择的菜单id
     * Gets default menu item id.
     *
     * @return the default menu item id
     */
    @IdRes
    protected int getDefaultMenuItemId() {
        return -1;
    }

    @Override
    public void onBackPressed() {//返回按钮
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}
