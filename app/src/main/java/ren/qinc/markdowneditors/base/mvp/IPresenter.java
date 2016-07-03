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

package ren.qinc.markdowneditors.base.mvp;

/**
 * Presenter顶层抽象
 * The type Base presenter.
 */
public interface IPresenter<V extends IMvpView> {

    /**
     * 建立关系
     *
     * @param mvpView 界面
     */
    void attachView(V mvpView);

    /**
     * 分离界面和Presenter
     */
    void detachView();

}