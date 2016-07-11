/*
 * Copyright 2016. SHENQINCI(沈钦赐)<dev@qinc.me>
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
public interface IEditorActivityView extends IMvpView {
    int CALL_GET_FILES = 1;


    void onNameChange(@NonNull String name);

}