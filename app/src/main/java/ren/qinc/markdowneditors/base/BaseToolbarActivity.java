/*
 * Copyright 2016. SHENQINCI(沈钦赐)<946736079@qq.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ren.qinc.markdowneditors.base;

import android.os.Build;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.DecelerateInterpolator;

import java.lang.reflect.Method;

import butterknife.Bind;
import ren.qinc.markdowneditors.R;
import ren.qinc.markdowneditors.utils.Check;
import ren.qinc.markdowneditors.utils.ViewUtils;


/**
 * 带有Toolbar的Activity封装
 * Created by 沈钦赐 on 16/1/25.
 */
public abstract class BaseToolbarActivity extends BaseActivity {
    @Bind(R.id.id_toolbar)
    protected Toolbar mToolbar;
    @Bind(R.id.id_appbar)
    protected AppBarLayout mAppBar;

    @Override
    protected void init() {
        super.init();
        if (mToolbar == null) // 如果布局文件没有找到toolbar,则不设置actionbar
        {
            throw new IllegalStateException(this.getClass().getSimpleName() + ":要使用BaseToolbarActivity，必须在布局里面增加id为‘id_toolbar’的Toolbar");
        }
        initActionBar(mToolbar);
        initAppBarLayout(mAppBar);
    }

    protected void initAppBarLayout(AppBarLayout appBar) {
        if (appBar == null) return;
        if (Build.VERSION.SDK_INT >= 21) {
            this.mAppBar.setElevation(getElevation());
        }
    }

    /**
     * 返回阴影的高度
     * Get elevation float.
     *
     * @return the float
     */
    @FloatRange(from = 0.0)
    protected float getElevation() {
        return 0f;
    }

    /**
     * 初始化actionbar
     *
     * @param toolbar the mToolbar
     */
    private void initActionBar(Toolbar toolbar) {
        if (!Check.isEmpty(getSubtitleString())) {
            toolbar.setSubtitle(getSubtitleString());
        }
        if (getTitleString() != null) {
            toolbar.setTitle(getTitleString());
        }

        setSupportActionBar(toolbar);
        if (hasBackButton()) {//如果需要返回按钮
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        }
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }


    public AppBarLayout getAppBar() {
        return mAppBar;
    }

    /**
     * 是否有左上角返回按钮
     *
     * @return 返回true则表示需要左上角返回按钮
     */
    protected boolean hasBackButton() {
        return false;
    }

    /**
     * 子类可以重写,若不重写默认为程序名字
     *
     * @return 返回主标题的资源id
     */
    @NonNull
    protected String getTitleString() {
        return BaseApplication.string(R.string.app_name);
    }

    /**
     * 子类可以重写,若不重写默认为程序名字 返回String资源
     *
     * @return 副标题的资源id
     */
    @NonNull
    protected String getSubtitleString() {
        return "";
    }

    private boolean isHiddenAppBar = false;

    /**
     * 切换appBarLayout的显隐
     */
    protected void hideOrShowToolbar() {
        if (mAppBar == null) return;
        mAppBar.animate()
                .translationY(isHiddenAppBar ? 0 : -mAppBar.getHeight())
                .setInterpolator(new DecelerateInterpolator(2))
                .start();
        isHiddenAppBar = !isHiddenAppBar;
    }

    /**
     * 设置appBar的透明度
     * Sets app bar alpha.
     *
     * @param alpha the alpha 0-1.0
     */
    protected void setAppBarAlpha(@FloatRange(from = 0.0, to = 1.0) float alpha) {
        if (mAppBar != null) {
            ViewUtils.setAlpha(mAppBar, alpha);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // finish();
                onBackPressed();// 返回
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        setOverflowIconVisible(menu);
        return super.onCreateOptionsMenu(menu);
    }


    /**
     * 显示菜单图标
     *
     * @param menu menu
     */
    private void setOverflowIconVisible(Menu menu) {
        try {
            Class clazz = Class.forName("android.support.v7.view.menu.MenuBuilder");
            Method m = clazz.getDeclaredMethod("setOptionalIconsVisible", boolean.class);
            m.setAccessible(true);
            m.invoke(menu, true);
        } catch (Exception e) {
            Log.d("OverflowIconVisible", e.getMessage());
        }
    }
}
