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


import com.kaopiz.kprogresshud.KProgressHUD;

/**
 * 等待框的接口
 * Created by 沈钦赐 on 16/1/17.
 */
public interface WaitDialogInterface {

    /**
     * 隐藏对话框
     */
    void hideWaitDialog();


    /**
     * 显示等待的对话框
     *
     * @param text
     * @return
     */
    KProgressHUD showWaitDialog(String text, boolean canBack);

}
