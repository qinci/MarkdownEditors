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

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ren.qinc.markdowneditors.R;


/**
 * TAB
 * Created by 沈钦赐 on 16/6/21.
 */
public class TabView extends HorizontalScrollView {

    private LinearLayout mLayout;
    private LayoutInflater mInflater;

    public TabView(Context context) {
        super(context);
        init();
    }

    public TabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.setOverScrollMode(OVER_SCROLL_NEVER);
        this.setHorizontalScrollBarEnabled(false);

        mInflater = LayoutInflater.from(getContext());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLayout = new LinearLayout(getContext());
        mLayout.setPadding(1, 0, 1, 0);
        mLayout.setOrientation(LinearLayout.HORIZONTAL);
        addView(mLayout, params);
    }


    public void addTab(String title, OnClickListener onClickListener) {
        View view = mInflater.inflate(R.layout.item_tab_text, mLayout, false);
        TextView textView = (TextView) view.findViewById(R.id.file_name);
        textView.setOnClickListener(onClickListener);
        textView.setText(title);
        textView.setTag(R.id.tag, mLayout.getChildCount());
        if (mLayout.getChildCount() <= 0) {
            //第一个就隐藏箭头
            view.findViewById(R.id.arrow).setVisibility(View.GONE);
        } else {
            //设置前一个的字体颜色
            TextView lastTitle = (TextView) mLayout.getChildAt(mLayout.getChildCount() - 1).findViewById(R.id.file_name);
            lastTitle.setTextColor(0x88ffffff);
        }
        mLayout.addView(view, mLayout.getChildCount());

        //滑到最右边
        this.postDelayed(() -> this.smoothScrollBy(1000, 0), 5);
    }


    public boolean removeTab() {
        int count = mLayout.getChildCount();
        if (count > 1) {
            //移除最后一个
            mLayout.removeViewAt(count - 1);
            View lastView = mLayout.getChildAt(mLayout.getChildCount() - 1);
            //设置最后一个的字体颜色为白色
            TextView lastTitle = (TextView) lastView.findViewById(R.id.file_name);
            lastTitle.setTextColor(0xffffffff);
            return true;
        }

        if (mLayout.getChildCount() == 1) {
            View lastView = mLayout.getChildAt(mLayout.getChildCount() - 1);
            lastView.findViewById(R.id.arrow).setVisibility(View.GONE);
        }
        return false;
    }

}
