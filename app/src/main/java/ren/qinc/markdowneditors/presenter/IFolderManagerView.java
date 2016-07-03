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

import java.util.List;

import ren.qinc.markdowneditors.base.mvp.IMvpView;
import ren.qinc.markdowneditors.entity.FileBean;

/**
 * 回调方法抽象，每个界面的回调都不同，所以提取出来
 */
public interface IFolderManagerView extends IMvpView {
    //没有参数的回调一般用成功回调即可
    int CALL_GET_FILES = 1;
    int CALL_CREATE_FOLDER = 2;
    int CALL_COPY_PASTE = 3;
    int CALL_CUT_PASTE = 4;
    int CALL_PASTE_MODE = 5;
    int CALL_CLOSE_PASTE_MODE = 6;
    int CALL_EDIT_MODE = 7;
    int CALL_CLOSE_EDIT_MODE = 8;
    int CALL_REMOVE_TAB = 9;
    int CALL_OTHER = 10;


    /**
     * 获取文件列表成功
     *
     * @param files the files
     */
    void getFileListSuccess(List<FileBean> files);


    /**
     * 增加tab
     * Add tab.
     *
     * @param title the title
     */
    void addTab(String title);

    /**
     * Update position.
     *
     * @param position the position
     * @param bean     the bean
     */
    void updatePosition(int position, FileBean bean);

    void addFilePosition(int position, FileBean bean);


}