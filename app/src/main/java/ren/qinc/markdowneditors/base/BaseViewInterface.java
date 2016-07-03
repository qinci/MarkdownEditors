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

import android.os.Bundle;
import android.support.annotation.LayoutRes;

/**
 * Created by 沈钦赐 on 16/21/25.
 */
public interface BaseViewInterface {


    /**
     * Activitiy的布局,必须重写
     *
     * @return 布局资源
     */
    @LayoutRes
    int getLayoutId();

    /**
     * onCreate之后调用,可以用来初始化view
     */
    void onCreateAfter(Bundle savedInstanceState);

    /**
     * 界面渲染完毕，可在这里进行初始化工作，建议在这里启动线程进行初始化工作
     * 数据获取等操作
     */
    void initData();

}