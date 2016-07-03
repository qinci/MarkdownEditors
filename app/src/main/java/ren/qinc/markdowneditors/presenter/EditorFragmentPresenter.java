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
import android.text.TextUtils;
import android.widget.EditText;

import java.io.File;
import java.util.Stack;

import ren.qinc.markdowneditors.base.mvp.BasePresenter;
import rx.functions.Action1;

/**
 * 编辑界面Presenter
 * Created by 沈钦赐 on 16/1/18.
 */
public class EditorFragmentPresenter extends BasePresenter<IEditorFragmentView> {
    private String filePath;
    private String fileName;
    private boolean isCreateFile;

    private Stack<String> fileStack = new Stack<>();

    public EditorFragmentPresenter(File file) {
        if (file.isDirectory()) {
            this.filePath = file.getAbsolutePath();
            this.fileName = "";
            isCreateFile = true;
        } else {
            this.fileName = file.getName();
            this.filePath = file.getParent();
        }
    }


    public void loadFile() {
        mCompositeSubscription.add(mDataManager.readFile(getMDFile())
                .subscribe(content -> {
                    if (getMvpView() == null) return;
                    getMvpView().onReadSuccess(fileName, content);
                }, throwable -> {
                    callFailure(-1, throwable.getMessage(), IEditorFragmentView.CALL_LOAOD_FILE);
                }));

    }

    @NonNull
    public File getMDFile() {
        return new File(filePath, fileName);
    }

    private boolean textChanged = false;

    public void refreshMenuIcon() {
        if (getMvpView() != null) return;
        if (textChanged)
            getMvpView().otherSuccess(IEditorFragmentView.CALL_NO_SAVE);
        else
            getMvpView().otherSuccess(IEditorFragmentView.CALL_SAVE);

    }

    public void textChange() {
        textChanged = true;
        if (getMvpView() != null) {
            getMvpView().otherSuccess(IEditorFragmentView.CALL_NO_SAVE);
        }

    }

    public void save(String name, String content) {
        saveForExit(name, content, false);
    }

    public void saveForExit(String name, String content, boolean exit) {
        if (TextUtils.isEmpty(name)) {
            callFailure(-1, "名字不能为空", IEditorFragmentView.CALL_SAVE);
            return;
        }
        if (content == null) return;

        //上一次文件名为空
        if (TextUtils.isEmpty(fileName)) {
            //新建文件
            if (isCreateFile) {
                //新创建文件，但是文件已经存在了
                File file = new File(filePath, name + ".md");
                if (!file.isDirectory() && file.exists()) {
                    callFailure(-1, "文件已经存在", IEditorFragmentView.CALL_SAVE);
                    return;
                }
            }
            fileName = name;
        }


        if (!fileName.endsWith(".md") &&
                !fileName.endsWith(".markdown") &&
                !fileName.endsWith(".mdown")) {
            fileName = fileName + ".md";
        }
        mDataManager.saveFile(getMDFile(), content).subscribe(success -> {
            if (success) {
                isCreateFile = false;
                textChanged = false;
                if (!rename(name)) {
                    callFailure(-1, "重命名失败", IEditorFragmentView.CALL_SAVE);
                    return;
                }
                if (getMvpView() != null) {
                    if (exit)
                        getMvpView().otherSuccess(IEditorFragmentView.CALL_EXIT);
                    else
                        getMvpView().otherSuccess(IEditorFragmentView.CALL_SAVE);
                }
            } else {
                callFailure(-1, "保存失败", IEditorFragmentView.CALL_SAVE);
            }
        }, throwable -> {
            callFailure(-1, "保存失败", IEditorFragmentView.CALL_SAVE);
        });
    }

    private boolean rename(String newName) {

        int end = fileName.lastIndexOf(".");
        String name = fileName.substring(0, end);
        if (newName.equals(name)) return true;

        String suffix = fileName.substring(end, fileName.length());
        if (suffix.endsWith(".md") ||
                suffix.endsWith(".markdown") ||
                suffix.endsWith(".mdown")) {
            //重命名
            File oldFile = getMDFile();
            File newPath = new File(filePath, newName + suffix);
            if (oldFile.getAbsolutePath().equals(newPath.getAbsolutePath())) return true;

            fileName = newPath.getName();

            if (newPath.exists())//文件已经存在了
                return false;
            boolean b = oldFile.renameTo(newPath);
            return b;
        }
        return false;
    }

    public boolean isSave() {
        return !textChanged;
    }
}
