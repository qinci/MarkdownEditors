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

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import me.drakeet.library.CrashWoodpecker;
import ren.qinc.markdowneditors.AppManager;
import ren.qinc.markdowneditors.utils.Check;


/**
 * 业务无关的Application基类
 * Created by 沈钦赐 on 16/21/25.
 */
public abstract class BaseApplication extends Application {
    static Context context;
    static Resources resource;

    private RefWatcher refWatcher;


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        resource = context.getResources();

        if (hasMemoryLeak()) {
            refWatcher = LeakCanary.install(this);//预定义的 RefWatcher，同时也会启用一个 ActivityRefWatcher
        }
        if (hasCrashLog()) {
            CrashWoodpecker.fly().to(this);//崩溃异常捕获
        }

    }


    public static synchronized Context context() {
        return context;
    }

    public static RefWatcher getRefWatcher(Context context) {
        if (context == null) {
            return null;
        }
        BaseApplication application = (BaseApplication) context.getApplicationContext();
        if (application.hasMemoryLeak()) {
            return application.refWatcher;
        }
        return null;
    }

    protected abstract boolean hasMemoryLeak();

    protected abstract boolean hasCrashLog();


    @Override
    public void onTerminate() {
        super.onTerminate();
        AppManager.getAppManager().AppExit(this);
    }

    /**
     * 根据资源返回String值
     *
     * @param id 资源id
     * @return String
     */
    public static String string(int id) {
        return resource.getString(id);
    }

    /**
     * 根据资源返回color值
     *
     * @param id 资源id
     * @return int类型的color
     */
    public static int color(int id) {
        return resource.getColor(id);
    }

    /**
     * 根据资源翻译Drawable值
     *
     * @param id 资源id
     * @return Drawable
     */
    public static Drawable drawable(int id) {
        return resource.getDrawable(id);
    }

    //======Snackbar
    public static Snackbar showSnackbar(@NonNull View view, @NonNull String message, @Snackbar.Duration int duration, @Nullable View.OnClickListener listener, @Nullable String actionStr) {
        Snackbar snackbar = Snackbar.make(view, message, duration);
        if (listener != null && Check.isEmpty(actionStr)) {
            snackbar.setAction(actionStr, listener);
        }
        snackbar.show();
        return snackbar;
    }

    public static Snackbar showSnackbar(@NonNull View view, @NonNull int messageRes, @Snackbar.Duration int duration, @Nullable View.OnClickListener listener, @Nullable String actionStr) {
        Snackbar snackbar = Snackbar.make(view, messageRes, duration);
        if (listener != null && Check.isEmpty(actionStr)) {
            snackbar.setAction(actionStr, listener);
        }
        snackbar.show();
        return snackbar;
    }

    public static Snackbar showSnackbar(@NonNull View view, @NonNull String message) {
        return showSnackbar(view, message, Snackbar.LENGTH_SHORT, null, null);
    }

    public static Snackbar showSnackbar(@NonNull View view, @StringRes int messageRes) {
        return showSnackbar(view, messageRes, Snackbar.LENGTH_SHORT, null, null);
    }

    public static Snackbar showSnackbarLong(@NonNull View view, @NonNull String message) {
        return showSnackbar(view, message, Snackbar.LENGTH_LONG, null, null);
    }

    public static Snackbar showSnackbarIndefinite(@NonNull View view, @NonNull String message) {
        return showSnackbar(view, message, Snackbar.LENGTH_INDEFINITE, null, null);
    }

    public static Snackbar showSnackbar(@NonNull View view, @NonNull String message, @Nullable View.OnClickListener listener, @Nullable String actionStr) {
        return showSnackbar(view, message, Snackbar.LENGTH_SHORT, listener, actionStr);
    }

    public static Snackbar showSnackbarLong(@NonNull View view, @NonNull String message, @Nullable View.OnClickListener listener, @Nullable String actionStr) {
        return showSnackbar(view, message, Snackbar.LENGTH_LONG, listener, actionStr);
    }

    public static Snackbar showSnackbarIndefinite(@NonNull View view, @NonNull String message, @Nullable View.OnClickListener listener, @Nullable String actionStr) {
        return showSnackbar(view, message, Snackbar.LENGTH_INDEFINITE, listener, actionStr);
    }

    public static Snackbar showSnackbar(@NonNull View view, @NonNull String message, @Nullable View.OnClickListener listener) {
        return showSnackbar(view, message, Snackbar.LENGTH_SHORT, listener, "确定");
    }

    public static Snackbar showSnackbarLong(@NonNull View view, @NonNull String message, @Nullable View.OnClickListener listener) {
        return showSnackbar(view, message, Snackbar.LENGTH_LONG, listener, "确定");
    }

    public static Snackbar showSnackbarIndefinite(@NonNull View view, @NonNull String message, @Nullable View.OnClickListener listener) {
        return showSnackbar(view, message, Snackbar.LENGTH_INDEFINITE, listener, "确定");
    }

}
