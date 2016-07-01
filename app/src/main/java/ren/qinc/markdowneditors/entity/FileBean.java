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
        if(obj == null)return false;
        if (this == obj)
            return true;

        if (obj instanceof FileBean) {
            FileBean p = (FileBean) obj;
            return name.equals(p.name)&&lastTime.equals(p.lastTime);
        }
        return false;
    }
}
