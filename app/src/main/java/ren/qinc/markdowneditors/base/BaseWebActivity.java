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

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextSwitcher;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import ren.qinc.markdowneditors.R;
import ren.qinc.markdowneditors.utils.Check;
import ren.qinc.markdowneditors.utils.Network;
import ren.qinc.markdowneditors.utils.SystemBarUtils;
import ren.qinc.markdowneditors.utils.SystemUtils;
import ren.qinc.markdowneditors.widget.ObservableWebView;


/**
 * 网页浏览,必须提供
 * 1. mProgressBar 2. mWebView 3. tv_title
 * 子类可以在initOther添加js接口等不同操作
 * Created by 沈钦赐
 */
public class BaseWebActivity extends BaseToolbarActivity {

    public static final String CONTENT_KEY = "extra_content";
    public static final String URL_KEY = "extra_url";
    public static final String TITLE_KEY = "extra_title";

    @Bind(R.id.progressbar)
    protected NumberProgressBar mProgressBar;
    @Bind(R.id.webView)
    protected ObservableWebView mWebView;
    @Bind(R.id.tv_title)
    protected TextSwitcher mTextSwitcher;

    private String url, title, content;


    @Override
    protected void onResume() {
        super.onResume();
        if (mWebView != null) mWebView.onResume();
    }

    @Override
    protected void onPause() {
        if (mWebView != null) mWebView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        if (mWebView != null) {
            mWebView.pauseTimers();
            mWebView.stopLoading();
            mWebView.setFocusable(true); //
            mWebView.removeAllViews();
            mWebView.clearHistory();
            mWebView.destroy();
        }
        super.onDestroy();
    }

    /**
     * New intent intent.
     *
     * @param context      the context
     * @param url          url优先加载
     * @param content      网页内容,如果url不为空,内存可为空
     * @param defaultTitle the default title
     * @return the intent
     */
    public static void load(Context context, String url, String content, String defaultTitle) {
        Intent intent = new Intent(context, BaseWebActivity.class);
        intent.putExtra(URL_KEY, url);
        intent.putExtra(TITLE_KEY, defaultTitle);
        intent.putExtra(CONTENT_KEY, content);
        context.startActivity(intent);
    }

    /**
     * Load url.
     *
     * @param context      the context
     * @param url          the url
     * @param defaultTitle the default title
     */
    public static void loadUrl(Context context, @NonNull String url, @Nullable String defaultTitle) {
        load(context, url, "", defaultTitle);
    }

    /**
     * Load content.
     *
     * @param context      the context
     * @param content      the content
     * @param defaultTitle the default title
     */
    public static void loadContent(Context context, @NonNull String content, String defaultTitle) {
        load(context, "", content, defaultTitle);
    }


    protected void initStatusBar() {
        SystemBarUtils.setHeightAndPadding(this, mToolbar);
    }

    /**
     * onCreate之后调用,可以用来初始化view
     *
     * @param savedInstanceState the saved instance state
     */
    @Override
    public final void onCreateAfter(Bundle savedInstanceState) {
        parseIntent();


        enableJavascript();
        enableCaching();
        enableClient();
        enableAdjust();
        zoomedOut();
        //网页标题
        mTextSwitcher.setFactory(() -> {
            TextView textView = new TextView(this);
            textView.setTextAppearance(this, R.style.WebTitle);
            textView.setSingleLine(true);
            textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            textView.postDelayed(() -> textView.setSelected(true), 1738);
            return textView;
        });
        mTextSwitcher.setInAnimation(this, android.R.anim.fade_in);
        mTextSwitcher.setOutAnimation(this, android.R.anim.fade_out);
        if (title != null) setTitle(title);

        if (mProgressBar != null)
            mProgressBar.setReachedBarColor(BaseApplication.color(R.color.colorPrimary));

        //设置滑动监听
        mWebView.setOnScrollChangedCallback((dx, dy, x, y) -> {//滑动监听

        });

        onCreateAfterLater();
    }

    private void zoomedOut() {
        mWebView.getSettings().setSupportZoom(false);// 不支持缩放
        mWebView.getSettings().setUseWideViewPort(true);// 将图片调整到适合webview的大小
        mWebView.getSettings().setBuiltInZoomControls(false);// 不显示缩放工具
    }

    private void enableClient() {
        mWebView.setWebChromeClient(new ChromeClient());
        mWebView.setWebViewClient(new WebClient());
    }

    private void enableAdjust() {
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);// 把所有内容放在webview等宽的一列中
        mWebView.getSettings().setLoadWithOverviewMode(true);// 加载一个页面是否与概述模式
    }

    /**
     * Enable caching.打开缓存
     */
    private void enableCaching() {
        mWebView.getSettings().setAppCachePath(getFilesDir() + getPackageName() + "/cache");
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebView.getSettings().setAllowFileAccess(true);// 访问文件数据
    }

    /**
     * 打开javaScript支持
     * Enable javascript.
     */
    private void enableJavascript() {
        mWebView.getSettings().setJavaScriptEnabled(true);// 能够执行Javascript脚本
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
    }

    private void parseIntent() {
        url = getIntent().getStringExtra(URL_KEY);
        title = getIntent().getStringExtra(TITLE_KEY);
        content = getIntent().getStringExtra(CONTENT_KEY);
    }

    /**
     * 界面初始化后,网页还没有开始加载,子类可以重写方法操作要操作的东西
     * On create after later.
     */
    protected void onCreateAfterLater() {

    }

    /**
     * 界面渲染完毕，可在这里进行初始化工作，建议在这里启动线程进行初始化工作
     * 数据获取等操作
     */
    @Override
    public void initData() {
        initOther();

        if (!Check.isEmpty(url)) {
            if (Network.getInstance().isConnected()) {
                mWebView.loadUrl(url);
            } else {
                //没有网络
            }
        } else if (!Check.isEmpty(content)) {
            mWebView.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null);
        }

    }

    /**
     * Init other.子类可以重写此方法,初始化其他东西,如添加javascript接口
     */
    protected void initOther() {

        //mWebview.addJavascriptInterface(new JavaScriptInterface(context, download), "android");
    }

    /**
     * Activitiy的布局,必须重写
     *
     * @return 布局资源
     */
    @Override
    public int getLayoutId() {
        return R.layout.activity_common_web;
    }

    @Override
    protected boolean hasBackButton() {
        return true;
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        if (mTextSwitcher != null)
            mTextSwitcher.setText(title);
    }


    protected void refresh() {
        mWebView.reload();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        this.switchScreenConfiguration(null);
                    } else {
                        if (mWebView.canGoBack()) {
                            mWebView.goBack();//回退
                        } else {
                            finish();
                        }
                    }
                    return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    public void switchScreenConfiguration(MenuItem item) {
        Configuration configuration = this.getResources().getConfiguration();
        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
            if (item != null) item.setTitle(this.getString(R.string.menu_web_vertical));
        } else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
            if (item != null) item.setTitle(this.getString(R.string.menu_web_horizontal));
        }
//        getResources().updateConfiguration(configuration,null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_web, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (menuClick(id)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean menuClick(int id) {
        switch (id) {
            case R.id.action_refresh:
                refresh();
                return true;
            case R.id.action_copy_url:
                //                String copyDone = getString(R.string.tip_copy_done);
                SystemUtils.copyToClipBoard(this, mWebView.getUrl());
                Snackbar.make(mWebView, "复制完成", Snackbar.LENGTH_SHORT).show();
                return true;
            case R.id.action_open_url:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                Uri uri = Uri.parse(url);
                intent.setData(uri);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Snackbar.make(mWebView, "打开失败", Snackbar.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_clear_cache:
                mWebView.clearCache(true);
                mWebView.clearHistory();
                Snackbar.make(mWebView, "清理缓存成功", Snackbar.LENGTH_SHORT).show();
                return true;
            case R.id.action_clear_cookie:
                CookieSyncManager.createInstance(this);
                CookieSyncManager.getInstance().startSync();
                CookieManager.getInstance().removeSessionCookie();
                Snackbar.make(mWebView, "清理Cookie成功", Snackbar.LENGTH_SHORT).show();
                return true;

        }

        return false;
    }


    private class ChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (mProgressBar == null) return;
            mProgressBar.setProgress(newProgress);
            if (newProgress == 100) {
                mProgressBar.setVisibility(View.GONE);
            } else {
                mProgressBar.setVisibility(View.VISIBLE);
            }
        }


        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            setTitle(title);
        }

        //扩展支持alert事件
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("注意").setMessage(message).setPositiveButton("确定", null);
            builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.show();
            result.confirm();
            return true;
        }

        //扩展浏览器上传文件
        //3.0++版本
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            openFileChooserImpl(uploadMsg, acceptType);
        }

        //3.0--版本
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            openFileChooserImpl(uploadMsg, "");
        }

        // Android 4.1++
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            openFileChooserImpl(uploadMsg, acceptType);
        }

        // For Android > 5.0
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> uploadMsg, FileChooserParams fileChooserParams) {
            String acceptType = null;
            if (Build.VERSION.SDK_INT >= 21) {
                String[] acceptTypes = fileChooserParams.getAcceptTypes();
                if (acceptTypes != null && acceptTypes.length > 0) {
                    acceptType = acceptTypes[0];
                }
            }
            openFileChooserImplForAndroid5(uploadMsg, acceptType);
            return true;
        }
    }

    private final static int FILECHOOSER_RESULTCODE = 101;
    private final static int FILECHOOSER_RESULTCODE_FOR_ANDROID_5 = 102;
    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> mUploadMessageForAndroid5;

    /**
     * 5.--文件选择
     * Open file chooser.
     *
     * @param uploadMsg  the upload msg
     * @param acceptType the accept type
     */
    private void openFileChooserImpl(ValueCallback<Uri> uploadMsg, String acceptType) {
        if (TextUtils.isEmpty(acceptType.trim()))
            acceptType = "image/*";
        mUploadMessage = uploadMsg;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType(acceptType);
        startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
    }

    /**
     * 5.++文件选择
     * Open file chooser impl for android 5.
     *
     * @param uploadMsg  the upload msg
     * @param acceptType the accept type
     */
    private void openFileChooserImplForAndroid5(ValueCallback<Uri[]> uploadMsg, String acceptType) {
        if (TextUtils.isEmpty(acceptType.trim()))
            acceptType = "image/*";
        mUploadMessageForAndroid5 = uploadMsg;
        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType(acceptType);

        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");

        startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE_FOR_ANDROID_5);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;

        } else if (requestCode == FILECHOOSER_RESULTCODE_FOR_ANDROID_5) {
            if (null == mUploadMessageForAndroid5)
                return;
            Uri result = (intent == null || resultCode != RESULT_OK) ? null : intent.getData();
            if (result != null) {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{result});
            } else {
                mUploadMessageForAndroid5.onReceiveValue(new Uri[]{});
            }
            mUploadMessageForAndroid5 = null;
        }
    }

    private class WebClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url != null) view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            Snackbar.make(view, "加载错误", Snackbar.LENGTH_LONG).show();
        }

        public void onPageFinished(WebView view, String url) {
//            Snackbar.make(view, "加载完成", Snackbar.LENGTH_SHORT).show();
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Snackbar.make(view, "加载错误errorCode:" + errorCode, Snackbar.LENGTH_LONG).show();
        }
    }


}