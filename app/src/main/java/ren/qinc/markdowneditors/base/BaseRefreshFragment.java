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

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import butterknife.Bind;
import ren.qinc.markdowneditors.R;

/**
 * 带下拉刷新的Fragment
 * Created by 沈钦赐 on 16/1/27.
 */
public abstract class BaseRefreshFragment extends BaseFragment {

    @Bind(R.id.id_refresh)
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }


    @Override
    public void onCreateAfter(Bundle savedInstanceState) {
        initRefresh();
    }


    private void initRefresh() {
        if (mSwipeRefreshLayout == null) {
            throw new IllegalStateException(this.getClass().getSimpleName() + ":要使用BaseRefreshFragment，必须在布局里面增加id为‘id_refresh’的MaterialRefreshLayout");
        }
        mSwipeRefreshLayout.setColorSchemeColors(getColors());
        mSwipeRefreshLayout.setOnRefreshListener(() -> BaseRefreshFragment.this.onRefresh(mSwipeRefreshLayout));
    }

    protected int[] getColors() {
        int[] colors = {BaseApplication.color(R.color.colorPrimary)};
        return colors;
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


    protected final boolean isRefresh() {
        return mSwipeRefreshLayout.isRefreshing();
    }

    /**
     * On refresh.刷新回调
     *
     * @param swipeRefreshLayout the swipe refresh layout
     */
    protected abstract void onRefresh(SwipeRefreshLayout swipeRefreshLayout);

}
