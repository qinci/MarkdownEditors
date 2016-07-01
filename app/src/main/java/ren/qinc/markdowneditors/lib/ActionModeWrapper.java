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

package ren.qinc.markdowneditors.lib;

import android.os.Build;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

/**
 * Created by Antonis Kalipetis on 01.08.2013.
 */
public class ActionModeWrapper extends ActionMode {

    private android.view.ActionMode mode;
    private ActionMode modeCompat;

    public ActionModeWrapper(android.view.ActionMode mode) {
        this.mode = mode;
    }

    public ActionModeWrapper(ActionMode modeCompat) {
        this.modeCompat = modeCompat;
    }

    @Override
    public void setTitle(CharSequence charSequence) {
        if (Build.VERSION.SDK_INT < 11) modeCompat.setTitle(charSequence);
        else mode.setTitle(charSequence);
    }

    @Override
    public void setSubtitle(CharSequence charSequence) {
        if (Build.VERSION.SDK_INT < 11) modeCompat.setSubtitle(charSequence);
        else mode.setSubtitle(charSequence);
    }

    @Override
    public void invalidate() {
        if (Build.VERSION.SDK_INT < 11) modeCompat.invalidate();
        else mode.invalidate();
    }

    @Override
    public void finish() {
        if (Build.VERSION.SDK_INT < 11) modeCompat.finish();
        else mode.finish();
    }

    @Override
    public Menu getMenu() {
        if (Build.VERSION.SDK_INT < 11) return modeCompat.getMenu();
        else return mode.getMenu();
    }

    @Override
    public CharSequence getTitle() {
        if (Build.VERSION.SDK_INT < 11) return modeCompat.getTitle();
        else return mode.getTitle();
    }

    @Override
    public void setTitle(int i) {
        if (Build.VERSION.SDK_INT < 11) modeCompat.setTitle(i);
        else mode.setTitle(i);
    }

    @Override
    public CharSequence getSubtitle() {
        if (Build.VERSION.SDK_INT < 11) return modeCompat.getSubtitle();
        else return mode.getSubtitle();
    }

    @Override
    public void setSubtitle(int i) {
        if (Build.VERSION.SDK_INT < 11) modeCompat.setSubtitle(i);
        else mode.setSubtitle(i);
    }

    @Override
    public View getCustomView() {
        if (Build.VERSION.SDK_INT < 11) return modeCompat.getCustomView();
        else return mode.getCustomView();
    }

    @Override
    public void setCustomView(View view) {
        if (Build.VERSION.SDK_INT < 11) modeCompat.setCustomView(view);
        else mode.setCustomView(view);
    }

    @Override
    public MenuInflater getMenuInflater() {
        if (Build.VERSION.SDK_INT < 11) return modeCompat.getMenuInflater();
        else return mode.getMenuInflater();
    }
}