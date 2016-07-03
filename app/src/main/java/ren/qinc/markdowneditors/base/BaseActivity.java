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

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.view.ActionMode;
import android.support.v7.view.StandaloneActionMode;
import android.support.v7.widget.ActionBarContextView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.kaopiz.kprogresshud.KProgressHUD;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.Field;

import butterknife.ButterKnife;
import ren.qinc.markdowneditors.AppManager;
import ren.qinc.markdowneditors.event.RxEvent;
import ren.qinc.markdowneditors.event.RxEventBus;
import ren.qinc.markdowneditors.utils.SystemBarUtils;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * 原始Activity封装
 * Created by 沈钦赐 on 16/21/25.
 */
public abstract class BaseActivity extends AppCompatActivity implements BaseViewInterface, WaitDialogInterface, EventInterface {

    protected BaseApplication application;
    protected LayoutInflater inflater;
    protected Context mContext;

    /**
     * On create.
     *
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if ( BuildConfig.DEBUG) {//严苛模式
//            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
//            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());
//        }
        if (isNeedLogin()) {//如果子类返回true,代表当前界面需要登录才能进去
            finish();
        }
        registerEvent();

        if (getLayoutId() != 0) {// 设置布局,如果子类有返回布局的话
            setContentView(getLayoutId());
            ButterKnife.bind(this);
        } else {
            //没有提供ViewId
            throw new IllegalStateException(this.getClass().getSimpleName() + "没有提供正确的LayoutId");
        }
        inflater = getLayoutInflater();
        init();
        initStatusBar();
        //留给子类重写
        onCreateAfter(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //友盟统计
        MobclickAgent.onPageStart(this.getClass().getSimpleName());
        MobclickAgent.onResume(getApplicationContext());
    }

    @Override
    protected void onPause() {
        super.onPause();
        //友盟统计
        MobclickAgent.onPageEnd(this.getClass().getSimpleName());
        MobclickAgent.onPause(getApplicationContext());

    }

    @Override
    protected void onDestroy() {
        //注销EventBus
        unregisterEvent();
        //移除任务栈
        AppManager.getAppManager().removeActivity(this);
        ButterKnife.unbind(this);//解绑定
        super.onDestroy();
    }

    private boolean isFirstFocused = true;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (isFirstFocused && hasFocus) {
            isFirstFocused = false;
            initData();//此时界面渲染完毕,可以用来初始化数据等
        }
    }


    //用于接收事件
    private Subscription mSubscribe;

    @Override
    public void registerEvent() {
        //订阅
        mSubscribe = RxEventBus.getInstance().toObserverable()
                .filter(o -> o instanceof RxEvent)//只接受RxEvent
                .map(o -> (RxEvent) o)
                .filter(r -> hasNeedEvent(r.type))//只接受type = 1和type = 2的东西
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onEventMainThread);
    }

    @Override
    public void unregisterEvent() {
        if (mSubscribe != null) {
            mSubscribe.unsubscribe();
        }
    }

    /**
     * 根据需要重写，如果返回True，这代表该type的Event你是要接受的 会回调
     * Has need event boolean.
     *
     * @param type the type
     * @return the boolean
     */
    @Override
    public boolean hasNeedEvent(int type) {
        return type == RxEvent.TYPE_FINISH;
    }

    @Override
    public void onEventMainThread(RxEvent e) {
        if (e.type == RxEvent.TYPE_FINISH && e.o.length > 0) {
            //xxxx执行响应的操作
        }
    }


    protected void init() {
        AppManager.getAppManager().addActivity(this);
        mContext = getApplicationContext();
        application = (BaseApplication) getApplication();
        inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    protected void initStatusBar() {
        SystemBarUtils.tintStatusBar(this, 0);
    }

    /**
     * 当前界面是否需要登录才能进去,默认不需要登录
     *
     * @return 返回true代表当前界面需要登录才能进入
     */
    protected boolean isNeedLogin() {
        return false;
    }

    /**
     * On login.登陆逻辑留给子类,
     */
    protected void onLogin() {
    }


    @Override
    public void hideWaitDialog() {
        if (mWait != null && mWait.isShowing())
            mWait.dismiss();
    }

    private KProgressHUD mWait;

    @Override
    public KProgressHUD showWaitDialog(String message, boolean canBack) {
        if (mWait == null)
            mWait = KProgressHUD.create(mContext)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("请稍等")
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f);
        else if (mWait.isShowing()) mWait.dismiss();
        mWait.setCancellable(canBack)
                .setDetailsLabel(message)
                .show();
        return mWait;
    }


    private InputMethodManager inputManager;
    //标志当前Activity是否有效
    private boolean isVisible;

    /**
     * 显示输入法
     *
     * @param editTextTemp edittext
     */
    public void showInput(final EditText editTextTemp) {
        if (editTextTemp != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    editTextTemp.setFocusable(true);
                    editTextTemp.requestFocus();
                    inputManager.showSoftInput(editTextTemp, 2);
                }
            }, 200);
        }
    }

    /**
     * 隐藏输入法
     *
     * @param editTextTemp editText
     */
    public void dismissInput(EditText editTextTemp) {
        if (editTextTemp != null && editTextTemp.isFocused()) {
            inputManager.hideSoftInputFromWindow(editTextTemp.getWindowToken(), 0);
        }
    }

    @Override
    public void onSupportActionModeStarted(ActionMode mode) {
        super.onSupportActionModeStarted(mode);
        fixActionModeCallback(this, mode);
    }

    /**
     * 修复长按文本启动系统的复制粘贴ActionMode的状态栏颜色
     *
     * @param activity
     * @param mode
     */
    private void fixActionModeCallback(AppCompatActivity activity, ActionMode mode) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            return;

        if (!(mode instanceof StandaloneActionMode))
            return;

        try {
            final Field mCallbackField = mode.getClass().getDeclaredField("mCallback");
            mCallbackField.setAccessible(true);
            final Object mCallback = mCallbackField.get(mode);

            final Field mWrappedField = mCallback.getClass().getDeclaredField("mWrapped");
            mWrappedField.setAccessible(true);
            final ActionMode.Callback mWrapped = (ActionMode.Callback) mWrappedField.get(mCallback);

            final Field mDelegateField = AppCompatActivity.class.getDeclaredField("mDelegate");
            mDelegateField.setAccessible(true);
            final Object mDelegate = mDelegateField.get(activity);

            mCallbackField.set(mode, new ActionMode.Callback() {

                @Override
                public boolean onCreateActionMode(android.support.v7.view.ActionMode mode, Menu menu) {
                    return mWrapped.onCreateActionMode(mode, menu);
                }

                @Override
                public boolean onPrepareActionMode(android.support.v7.view.ActionMode mode, Menu menu) {
                    return mWrapped.onPrepareActionMode(mode, menu);
                }

                @Override
                public boolean onActionItemClicked(android.support.v7.view.ActionMode mode, MenuItem item) {
                    return mWrapped.onActionItemClicked(mode, item);
                }

                @Override
                public void onDestroyActionMode(final android.support.v7.view.ActionMode mode) {
                    Class mDelegateClass = mDelegate.getClass().getSuperclass();
                    Window mWindow = null;
                    PopupWindow mActionModePopup = null;
                    Runnable mShowActionModePopup = null;
                    ActionBarContextView mActionModeView = null;
                    AppCompatCallback mAppCompatCallback = null;
                    ViewPropertyAnimatorCompat mFadeAnim = null;
                    android.support.v7.view.ActionMode mActionMode = null;

                    Field mFadeAnimField = null;
                    Field mActionModeField = null;

                    while (mDelegateClass != null) {
                        try {
                            if (TextUtils.equals("AppCompatDelegateImplV7", mDelegateClass.getSimpleName())) {
                                Field mActionModePopupField = mDelegateClass.getDeclaredField("mActionModePopup");
                                mActionModePopupField.setAccessible(true);
                                mActionModePopup = (PopupWindow) mActionModePopupField.get(mDelegate);

                                Field mShowActionModePopupField = mDelegateClass.getDeclaredField("mShowActionModePopup");
                                mShowActionModePopupField.setAccessible(true);
                                mShowActionModePopup = (Runnable) mShowActionModePopupField.get(mDelegate);

                                Field mActionModeViewField = mDelegateClass.getDeclaredField("mActionModeView");
                                mActionModeViewField.setAccessible(true);
                                mActionModeView = (ActionBarContextView) mActionModeViewField.get(mDelegate);

                                mFadeAnimField = mDelegateClass.getDeclaredField("mFadeAnim");
                                mFadeAnimField.setAccessible(true);
                                mFadeAnim = (ViewPropertyAnimatorCompat) mFadeAnimField.get(mDelegate);

                                mActionModeField = mDelegateClass.getDeclaredField("mActionMode");
                                mActionModeField.setAccessible(true);
                                mActionMode = (android.support.v7.view.ActionMode) mActionModeField.get(mDelegate);

                            } else if (TextUtils.equals("AppCompatDelegateImplBase", mDelegateClass.getSimpleName())) {
                                Field mAppCompatCallbackField = mDelegateClass.getDeclaredField("mAppCompatCallback");
                                mAppCompatCallbackField.setAccessible(true);
                                mAppCompatCallback = (AppCompatCallback) mAppCompatCallbackField.get(mDelegate);

                                Field mWindowField = mDelegateClass.getDeclaredField("mWindow");
                                mWindowField.setAccessible(true);
                                mWindow = (Window) mWindowField.get(mDelegate);
                            }

                            mDelegateClass = mDelegateClass.getSuperclass();
                        } catch (NoSuchFieldException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }

                    if (mActionModePopup != null) {
                        mWindow.getDecorView().removeCallbacks(mShowActionModePopup);
                    }

                    if (mActionModeView != null) {
                        if (mFadeAnim != null) {
                            mFadeAnim.cancel();
                        }

                        mFadeAnim = ViewCompat.animate(mActionModeView).alpha(0.0F);

                        final PopupWindow mActionModePopupFinal = mActionModePopup;
                        final ActionBarContextView mActionModeViewFinal = mActionModeView;
                        final ViewPropertyAnimatorCompat mFadeAnimFinal = mFadeAnim;
                        final AppCompatCallback mAppCompatCallbackFinal = mAppCompatCallback;
                        final android.support.v7.view.ActionMode mActionModeFinal = mActionMode;
                        final Field mFadeAnimFieldFinal = mFadeAnimField;
                        final Field mActionModeFieldFinal = mActionModeField;

                        mFadeAnim.setListener(new ViewPropertyAnimatorListenerAdapter() {
                            public void onAnimationEnd(View view) {
                                mActionModeViewFinal.setVisibility(View.GONE);
                                if (mActionModePopupFinal != null) {
                                    mActionModePopupFinal.dismiss();
                                } else if (mActionModeViewFinal.getParent() instanceof View) {
                                    ViewCompat.requestApplyInsets((View) mActionModeViewFinal.getParent());
                                }

                                mActionModeViewFinal.removeAllViews();
                                mFadeAnimFinal.setListener((ViewPropertyAnimatorListener) null);

                                try {
                                    if (mFadeAnimFieldFinal != null) {
                                        mFadeAnimFieldFinal.set(mDelegate, null);
                                    }
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }

                                mWrapped.onDestroyActionMode(mode);

                                if (mAppCompatCallbackFinal != null) {
                                    mAppCompatCallbackFinal.onSupportActionModeFinished(mActionModeFinal);
                                }

                                try {
                                    if (mActionModeFieldFinal != null) {
                                        mActionModeFieldFinal.set(mDelegate, null);
                                    }
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            });

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
