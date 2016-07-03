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
 * 通用回调方法抽象父类
 * The type Base presenter.
 * Created by 沈钦赐 on 16/1/17.
 */
public interface IMvpView {
    /**
     * 其他通用的成功回调（没有带参数，回调）
     * Other success.
     *
     * @param flag the flag
     */
    void otherSuccess(int flag);

    /**
     * 失败时调用
     *
     * @param errorCode 失败码
     * @param message   消息
     * @param flag      标志 如标志登陆，标志列表数据请求。。。。
     */
    void onFailure(int errorCode, String message, int flag);

    /**
     * 显示Progress
     *
     * @param message wait消息框的消息
     * @param canBack 是否可以返回标志
     * @param flag    操作标志
     */
    void showWait(String message, boolean canBack, int flag);

    /**
     * 隐藏Progress
     *
     * @param flag 标志
     */
    void hideWait(int flag);
}
