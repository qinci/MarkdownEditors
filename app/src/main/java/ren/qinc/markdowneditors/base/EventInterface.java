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


import ren.qinc.markdowneditors.event.RxEvent;

/**
 * Eventbus
 * Created by 沈钦赐 on 16/1/21.
 */
public interface EventInterface {
    /**
     * 绑定
     */
    void registerEvent();


    /**
     * 解绑
     */
    void unregisterEvent();

    /**
     * 根据需要重写，如果返回True，这代表该type的Event你是要接受的 会回调
     * Has need event boolean.
     *
     * @param type the type
     * @return the boolean
     */
    boolean hasNeedEvent(int type);

    /**
     * 回调
     *
     * @param e
     */
    void onEventMainThread(RxEvent e);
}
