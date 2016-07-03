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
package ren.qinc.markdowneditors.engine;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import ren.qinc.markdowneditors.utils.ColorUtils;

/**
 * ActionMode回调 状态栏又要保持透明，又要有颜色
 * 可以用v7包下的ActionMode也可以用普通的ActionMode
 * 如果是v7 则startSupportActionMode(pasteModeCallback);
 * 普通的startActionMode(pasteModeCallback);
 * Created by 沈钦赐 on 16/2/3.
 */
public abstract class ActionModeCallback implements ActionMode.Callback {
    private static float DEFAULT_ALPHA = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? 0.1f : 0.2f;
    protected int statusBarColor;
    private Activity mActivity;
    private int mActionModeStatusBarColor = 0;

    protected ActionModeCallback(@NonNull Activity activity, @ColorRes int actionModeStatusBarColorRes) {
        this.mActionModeStatusBarColor = ColorUtils.getAlphaColor(activity.getResources().getColor(actionModeStatusBarColorRes), (int) ((1 - DEFAULT_ALPHA) * 255));
        this.mActivity = activity;
    }

    @Override
    public final boolean onCreateActionMode(ActionMode mode, Menu menu) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            statusBarColor = mActivity.getWindow().getStatusBarColor();
            //set your gray color
            mActivity.getWindow().setStatusBarColor(mActionModeStatusBarColor);
//            mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        return onCreateActionModeCustom(mode, menu);
    }

    public abstract boolean onCreateActionModeCustom(ActionMode mode, Menu menu);

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        return false;
    }

    @Override
    public final void onDestroyActionMode(ActionMode mode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            mActivity.getWindow().setStatusBarColor(statusBarColor);

        }
        onDestroyActionModeCustom(mode);
    }

    public abstract void onDestroyActionModeCustom(ActionMode mode);
}
