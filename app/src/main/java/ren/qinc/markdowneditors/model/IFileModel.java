package ren.qinc.markdowneditors.model;

import java.io.File;

import ren.qinc.markdowneditors.entity.FileBean;
import rx.Observable;

/**
 * FileMode抽象
 * Created by 沈钦赐 on 16/1/26.
 */
public interface IFileModel {
    /**
     * 将文件类型转换
     * Gets file bean.
     *
     * @param file the file
     * @return the file bean
     */
    Observable<FileBean> getFileBeanObservable(File file);

    /**
     * 将文件类型转换
     * Gets file observable.
     *
     * @param fileBean the file bean
     * @return the file observable
     */
    Observable<File> getFileObservable(FileBean fileBean);


    /**
     * 将文件类型转换为FileBean
     * Gets file bean.
     *
     * @param file the file
     * @param name the name
     * @return the file bean
     */
    FileBean getFile(File file, String name);

    /**
     * 将FileBean类型转换为File
     * Gets file bean.
     *
     * @param fileBean the file bean
     * @return the file
     */
    File getFile(FileBean fileBean);
}
