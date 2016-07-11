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

package ren.qinc.markdowneditors.model;

import android.support.annotation.NonNull;

import java.io.File;
import java.util.Date;

import ren.qinc.markdowneditors.entity.FileBean;
import rx.Observable;

/**
 * File 文件相关数据处理
 * Created by 沈钦赐 on 16/1/26.
 */
public class FileModel implements IFileModel {
    private static FileModel instance = new FileModel();

    private FileModel() {

    }

    public static FileModel getInstance() {
        return instance;
    }

    @Override
    public Observable<FileBean> getFileBeanObservable(File file) {
        if (file == null)
            return null;
        return Observable.just(getFile(file, file.getName()));
    }

    @Override
    public Observable<File> getFileObservable(FileBean fileBean) {
        if (fileBean == null)
            return null;
        return Observable.just(getFile(fileBean));
    }

    public FileBean getFile(File file, String name) {
        FileBean bean = new FileBean();
        bean.absPath = file.getAbsolutePath();
        bean.isDirectory = file.isDirectory();
        bean.lastTime = new Date(file.lastModified());
        bean.name = name;

        if (file.isDirectory()) {
            bean.size = 0;
        } else {
            bean.size = file.length();
        }

        return bean;
    }

    @Override
    public File getFile(@NonNull FileBean fileBean) {
        return new File(fileBean.absPath);
    }
}
