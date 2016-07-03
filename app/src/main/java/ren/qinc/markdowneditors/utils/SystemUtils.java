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
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;

import ren.qinc.markdowneditors.base.BaseApplication;

/**
 * 系统相关工具集合
 * 来至网上，作者未知（表示感谢）
 */
public class SystemUtils {

    /**
     * 显示键盘
     *
     * @param dialog
     */
    public static void showSoftKeyboard(Dialog dialog) {
        dialog.getWindow().setSoftInputMode(4);
    }

    /**
     * 显示键盘
     *
     * @param view
     */
    public static void showSoftKeyboard(View view) {
        ((InputMethodManager) BaseApplication.context().getSystemService(Context.INPUT_METHOD_SERVICE))
                .showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 隐藏键盘
     *
     * @param view
     */
    public static void hideSoftKeyboard(View view) {
        if (view == null)
            return;
        ((InputMethodManager) BaseApplication.context().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /***
     * 获取activity的宽度
     *
     * @param activity the activity
     * @return width pixels
     */
    public static int getWidthPixels(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /***
     * 获取activity的高度
     *
     * @param activity the activity
     * @return height pixels
     */
    public static int getHeightPixels(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取当前应用版本号
     *
     * @param context
     * @return version
     * @throws Exception
     */
    public static String getAppVersion(Context context) {
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            return "未知";
        }
        return packInfo.versionName;
    }

    public static int getAppVersionCode(Context context) {
        int versionCode;
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = packInfo.versionCode;
        } catch (NameNotFoundException e) {
            versionCode = 1;
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取当前系统SDK版本号
     */
    public static int getSystemVersion() {
        /* 获取当前系统的android版本号 */
        return Build.VERSION.SDK_INT;
    }

    /**
     * @description 2.2
     */
    public static boolean hasFroyo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    /**
     * @description 2.3
     */
    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    /**
     * @description 3.x
     */
    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    /**
     * @description 4.0
     */
    public static boolean hasIcecreamSandwich() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    /**
     * @description 4.1
     */
    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= 17;
    }

    /**
     * Has kit kat boolean.
     *
     * @return the boolean
     * @description 4.4
     */
    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= 19;
    }

    /**
     * @description 5.0
     */
    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= 21;
    }

    /**
     * 判断软件是前台还是后台
     *
     * @param context the mContext
     * @return boolean
     */
    public static boolean isBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets phone msg.
     *
     * @param context the mContext
     * @return the phone msg
     * @throws Exception the exception
     */
    public static String getPhoneMsg(Context context) throws Exception {
        return "手机型号: " + Build.MODEL + ",\nSDK版本:"
                + Build.VERSION.SDK + ",\n系统版本:"
                + Build.VERSION.RELEASE +
                ",软件版本:" + getAppVersion(context) +
                ",软件版本号:" + getAppVersionCode(context);

    }


    /**
     * 复制到剪切板
     *
     * @param context the mContext
     * @param text    the text
     */
    public static void copyToClipBoard(Context context, String text) {
        ClipData clipData = ClipData.newPlainText("text_copy", text);
        ClipboardManager manager = (ClipboardManager) context.getSystemService(
                Context.CLIPBOARD_SERVICE);
        manager.setPrimaryClip(clipData);
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public static int barHeight = 0;

    public static int getStatusBarHeight() {
        if (barHeight > 0) {
            return barHeight;
        }
        if (BaseApplication.context() == null) {
            return 0;
        }
        Class<?> c;
        Object obj;
        Field field;
        int x;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            barHeight = BaseApplication.context().getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            Log.i("test", "状态栏高度获取失败了");
        }
        return barHeight;
    }

    //从assets 文件夹中获取文件并读取数据
    public static String getAssertString(Context context, String filename) {
        AssetManager am = context.getAssets();
        InputStream is = null;

        try {
            is = am.open(filename);
            return new String(readInputStream(is)).trim();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static byte[] readInputStream(InputStream in) {
        byte[] buffer = null;
        try {
            int length = in.available();
            buffer = new byte[length];
            in.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }
}
