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

package ren.qinc.markdowneditors.presenter;

import android.support.annotation.NonNull;

import ren.qinc.markdowneditors.base.mvp.IMvpView;

/**
 * 回调方法抽象，每个界面的回调都不同，所以提取出来
 */
public interface IEditorFragmentView extends IMvpView {
    //没有参数的回调一般用成功回调即可
    int CALL_LOAOD_FILE = 1;
    int CALL_NO_SAVE = 2;
    int CALL_SAVE = 3;
    int CALL_EXIT = 4;

    //文件读取成功
    void onReadSuccess(@NonNull String name, @NonNull String content);


}