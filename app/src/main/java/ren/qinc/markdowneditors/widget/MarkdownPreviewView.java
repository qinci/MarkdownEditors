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

package ren.qinc.markdowneditors.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build.VERSION;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import ren.qinc.markdowneditors.base.BaseWebActivity;

/**
 * Markdown View
 * The type Markdown preview view.
 */
public class MarkdownPreviewView extends NestedScrollView {
    public WebView mWebView;
    private Context mContext;
    private OnLoadingFinishListener mLoadingFinishListener;
    private ContentListener mContentListener;

    public MarkdownPreviewView(Context context) {
        super(context);
        init(context);
    }

    public MarkdownPreviewView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public MarkdownPreviewView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    @SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled"})
    private void init(Context context) {
        if (!isInEditMode()) {
            this.mContext = context;
//            setOrientation(VERTICAL);
            if (VERSION.SDK_INT >= 21) {
                WebView.enableSlowWholeDocumentDraw();
            }
            this.mWebView = new WebView(this.mContext);
            this.mWebView.getSettings().setJavaScriptEnabled(true);
            this.mWebView.setVerticalScrollBarEnabled(false);
            this.mWebView.setHorizontalScrollBarEnabled(false);
            this.mWebView.addJavascriptInterface(new JavaScriptInterface(this), "handler");
            this.mWebView.setWebViewClient(new MdWebViewClient(this));
            this.mWebView.loadUrl("file:///android_asset/markdown.html");
            addView(this.mWebView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }
    }

    public final void parseMarkdown(String str, boolean z) {
        this.mWebView.loadUrl("javascript:parseMarkdown(\"" + str.replace("\n", "\\n").replace("\"", "\\\"").replace("'", "\\'") + "\", " + z + ")");
    }

    public void setContentListener(ContentListener contentListener) {
        this.mContentListener = contentListener;
    }

    public void setOnLoadingFinishListener(OnLoadingFinishListener loadingFinishListener) {
        this.mLoadingFinishListener = loadingFinishListener;
    }

    public interface ContentListener {
    }

    public interface OnLoadingFinishListener {
        void onLoadingFinish();
    }

    final class JavaScriptInterface {
        final MarkdownPreviewView a;

        private JavaScriptInterface(MarkdownPreviewView markdownPreviewView) {
            this.a = markdownPreviewView;
        }

        @JavascriptInterface
        public void none() {

        }
    }


    final class MdWebViewClient extends WebViewClient {
        final MarkdownPreviewView mMarkdownPreviewView;

        private MdWebViewClient(MarkdownPreviewView markdownPreviewView) {
            this.mMarkdownPreviewView = markdownPreviewView;
        }

        public final void onPageFinished(WebView webView, String str) {
            if (this.mMarkdownPreviewView.mLoadingFinishListener != null) {
                this.mMarkdownPreviewView.mLoadingFinishListener.onLoadingFinish();
            }
        }

        public final void onReceivedError(WebView webView, int i, String str, String str2) {
            new StringBuilder("onReceivedError :errorCode:").append(i).append("description:").append(str).append("failingUrl").append(str2);
        }

        public final boolean shouldOverrideUrlLoading(WebView webView, String url) {
            if (!TextUtils.isEmpty(url))
                BaseWebActivity.loadUrl(webView.getContext(), url, null);
            return true;
        }
    }

    /**
     * 截屏
     *
     * @return
     */
    public Bitmap getScreen() {
        Bitmap bmp = Bitmap.createBitmap(mWebView.getWidth(), mWebView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        mWebView.draw(canvas);
        return bmp;
    }

}
