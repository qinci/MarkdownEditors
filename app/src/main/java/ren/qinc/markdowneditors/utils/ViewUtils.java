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
package ren.qinc.markdowneditors.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

public class ViewUtils {

    /**
     * Set view alpha
     * 设置透明度
     *
     * @param view  view
     * @param alpha alpha value
     */
    public static void setAlpha(View view, float alpha) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            view.setAlpha(alpha);
        } else {
            Drawable drawable = view.getBackground();
            if (drawable != null) {
                drawable.setAlpha((int) (alpha * 255));
            }
        }
    }

    /**
     * 根据Android系统版本，调用版本API中的设置View背景的方法
     * According to the Android version, calls the Settings in the View background method of version API
     *
     * @param view     view
     * @param drawable drawable
     */
    public static void setViewBackgroundDrawable(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }

    /**
     * 获取View在当前窗口内的绝对坐标
     * obtain the absolute coordinates to obtain the View within the current window
     *
     * @param view view
     * @return int[] x y
     */
    public static int[] getLocationInWindow(View view) {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        return location;
    }

    /**
     * 获取View在整个屏幕内的绝对坐标
     * obtain access to View the absolute coordinates of within the entire screen
     *
     * @param view view
     * @return int[] x y
     */
    public static int[] getLocationOnScreen(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location;
    }

    public static void startActivity(Intent intent, Activity activity,
                                     View sharedElement, String sharedElementName) {
        ActivityOptionsCompat optionsCompat
                = ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity, sharedElement, sharedElementName);
        try {
            ActivityCompat.startActivity(activity, intent,
                    optionsCompat.toBundle());
            //界面共享该图片元素
        } catch (IllegalArgumentException e) {
            activity.startActivity(intent);//如果异常 直接启动
        }
    }

}