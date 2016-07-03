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

package ren.qinc.markdowneditors.entity;

import java.util.Date;

/**
 * 文件实体
 * Created by 沈钦赐 on 16/1/26.
 */
public class FileBean {
    /**
     * 文件名字
     * The Name.
     */
    public String name;
    /**
     * 绝对路径
     * The Abs path.
     */
    public String absPath;
    /**
     * 是否文件夹
     * The Is folder.
     */
    public boolean isDirectory;
    /**
     * 最后修改时间
     * The Last time.
     */
    public Date lastTime;

    /**
     * 文件大小
     * The Size.
     */
    public long size;

    public boolean isSelect = false;

    public FileBean(String name, String absPath, boolean isDirectory, Date lastTime, long size) {
        this.name = name;
        this.absPath = absPath;
        this.isDirectory = isDirectory;
        this.lastTime = lastTime;
        this.size = size;
    }

    public FileBean() {
    }

    @Override
    public String toString() {
        return "FileBean{" +
                "name='" + name + '\'' +
                ", absPath='" + absPath + '\'' +
                ", isDirectory=" + isDirectory +
                ", lastTime=" + lastTime +
                ", size=" + size +
                '}';
    }

    @Override
    public int hashCode() {// 重写hashCode方法
        return this.name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {// 重写equals方法
        if (obj == null) return false;
        if (this == obj)
            return true;

        if (obj instanceof FileBean) {
            FileBean p = (FileBean) obj;
            return name.equals(p.name) && lastTime.equals(p.lastTime);
        }
        return false;
    }
}
