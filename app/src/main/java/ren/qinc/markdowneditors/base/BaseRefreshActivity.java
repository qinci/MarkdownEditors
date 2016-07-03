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

import android.support.v4.widget.SwipeRefreshLayout;

import butterknife.Bind;
import ren.qinc.markdowneditors.R;


/**
 * 带有下拉刷新的activity
 * Created by 沈钦赐 on 16/1/25.
 */
public abstract class BaseRefreshActivity extends BaseToolbarActivity {

    @Bind(R.id.id_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void init() {
        super.init();
        initRefresh();
    }

    private void initRefresh() {
        if (mSwipeRefreshLayout == null) {
            throw new IllegalStateException(this.getClass().getSimpleName() + ":要使用BaseRefreshActivity，必须在布局里面增加id为‘id_refresh’的MaterialRefreshLayout");
        }
        mSwipeRefreshLayout.setColorSchemeColors(getColors());
        mSwipeRefreshLayout.setOnRefreshListener(() -> BaseRefreshActivity.this.onRefresh(mSwipeRefreshLayout));
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }

    protected int[] getColors() {
        int[] colors = {BaseApplication.color(R.color.colorPrimary)};
        return colors;
    }


    protected final boolean isRefresh() {
        return mSwipeRefreshLayout.isRefreshing();
    }

    protected final boolean refresh() {
        if (isRefresh()) {
            return false;
        }
        mSwipeRefreshLayout.setRefreshing(true);
        onRefresh(mSwipeRefreshLayout);
        return true;
    }

    protected final boolean finishRefresh() {
        if (!isRefresh()) {
            return false;
        }
        mSwipeRefreshLayout.setRefreshing(false);
        return true;
    }

    protected abstract void onRefresh(SwipeRefreshLayout swipeRefreshLayout);

}
