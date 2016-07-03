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

import android.content.Context;
import android.os.SystemClock;
import android.view.Gravity;

public class Toast {
    /**
     * @Description Toast 全局控制
     */
    public static boolean isShow = true;
    public static android.widget.Toast toast;
    public static final int LENGTH_SHORT = android.widget.Toast.LENGTH_SHORT;
    public static final int LENGTH_LONG = android.widget.Toast.LENGTH_LONG;
    public static String oldMsg = "";
    private static long oldTime = 0;
    private static long newTime = 0;

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, CharSequence message) {
        showToast(context, message.toString(), LENGTH_SHORT);
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, int message) {
        showToast(context, context.getResources().getString(message), LENGTH_SHORT);
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, CharSequence message) {
        showToast(context, message.toString(), LENGTH_LONG);
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, int message) {
        showToast(context, context.getResources().getString(message), LENGTH_LONG);
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, CharSequence message, int duration) {
        showToast(context, message.toString(), duration);
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, int message, int duration) {
        showToast(context, context.getResources().getString(message), duration);
    }

    private static void showToast(Context context, String message, int duration) {
//		if(context==null){
//			context = NewsApplication.getInstance();
//		}


        if (!isShow || message == null || context == null) {
            return;
        }
        if (toast == null) {
            toast = android.widget.Toast.makeText(context, message, duration);
            toast.setGravity(Gravity.BOTTOM, 0, 300);
            toast.show();
        } else {
            if (oldMsg.equals(message)) {
                newTime = SystemClock.uptimeMillis();
                if (newTime - oldTime >= 50) {
                    toast.show();
                }
            } else {
                oldTime = SystemClock.uptimeMillis();
                toast.setText(message);
                toast.setDuration(duration);
                toast.show();
            }
        }
    }
}
