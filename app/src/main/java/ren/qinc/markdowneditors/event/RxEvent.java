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

package ren.qinc.markdowneditors.event;

import rx.android.schedulers.AndroidSchedulers;

/**
 * The type Rx event.
 * Created by 沈钦赐 on 16/1/21.
 */
public class RxEvent {
    /**
     * Activity关闭事件
     * The constant TYPE_FINISH.
     */
    public static final int TYPE_FINISH = 1;
    //刷新预览数据
    public static final int TYPE_REFRESH_DATA = 2;
    public static final int TYPE_REFRESH_NOTIFY = 3;
    //刷新文件夹
    public static final int TYPE_REFRESH_FOLDER = 4;

    public int type;
    public Object[] o = new Object[0];

    public RxEvent(int type, Object... obj) {
        this.type = type;
        if (obj != null) {
            this.o = obj;
        }
    }

    public boolean isType(int type) {
        return this.type == type;
    }

    public boolean isTypeAndData(int type) {
        return isType(type) && o.length > 0;
    }


    //使用教程：
    private void test() {
        //发送
        RxEventBus.getInstance().send(new RxEvent(TYPE_FINISH));
        //订阅
        RxEventBus.getInstance().toObserverable()
                .filter(o -> o instanceof RxEvent)//只接受RxEvent
                .map(o -> (RxEvent) o)
                .filter(r -> r.isType(1) || r.isType(2))//只接受type = 1和type = 2的东西
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(o -> {
                    System.out.println("收到了");
                });
    }
}
