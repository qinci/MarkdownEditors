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

package ren.qinc.markdowneditors.view;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;

import butterknife.Bind;
import ren.qinc.markdowneditors.R;
import ren.qinc.markdowneditors.base.BaseFragment;
import ren.qinc.markdowneditors.event.RxEvent;
import ren.qinc.markdowneditors.widget.MarkdownPreviewView;

/**
 * 编辑预览界面
 * Created by 沈钦赐 on 16/1/21.
 */
public class EditorMarkdownFragment extends BaseFragment {
    @Bind(R.id.markdownView)
    protected MarkdownPreviewView mMarkdownPreviewView;
    @Bind(R.id.title)
    protected TextView mName;
    private String mContent;


    public EditorMarkdownFragment() {
    }

    public static EditorMarkdownFragment getInstance() {
        EditorMarkdownFragment editorFragment = new EditorMarkdownFragment();
        return editorFragment;
    }


    @Override
    public boolean hasNeedEvent(int type) {
        //接受刷新数据
        return type == RxEvent.TYPE_REFRESH_DATA;
    }

    boolean isPageFinish = false;

    @Override
    public void onEventMainThread(RxEvent event) {
        if (event.isTypeAndData(RxEvent.TYPE_REFRESH_DATA)) {
            //页面还没有加载完成
            mContent = event.o[1].toString();
            mName.setText(event.o[0].toString());
            if (isPageFinish)
                mMarkdownPreviewView.parseMarkdown(mContent, true);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_markdown;
    }

    @Override
    public void onCreateAfter(Bundle savedInstanceState) {
        mMarkdownPreviewView.setOnLoadingFinishListener(() -> {
            if (!isPageFinish && mContent != null)//
                mMarkdownPreviewView.parseMarkdown(mContent, true);
            isPageFinish = true;
        });
    }

    @Override
    public boolean hasMenu() {
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_editor_preview_frag, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void initData() {
    }
}
