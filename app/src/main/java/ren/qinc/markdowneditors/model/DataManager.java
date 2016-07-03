package ren.qinc.markdowneditors.model;

import android.support.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ren.qinc.markdowneditors.entity.FileBean;
import ren.qinc.markdowneditors.utils.Check;
import ren.qinc.markdowneditors.utils.FileUtils;
import ren.qinc.markdowneditors.utils.RxUtils;
import rx.Observable;
import rx.Subscriber;

/**
 * Model统一数据管理
 * Created by 沈钦赐 on 16/1/26.
 */
public class DataManager {
    private IFileModel mFileModel;

    public static DataManager getInstance() {
        return DataManagerInstance.getManager();
    }

    private DataManager() {
        mFileModel = FileModel.getInstance();
    }

    /**
     * 读取文件
     *
     * @param file the file path
     * @return the observable
     */
    public Observable<String> readFile(@NonNull File file) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                if (file == null) {
                    subscriber.onError(new IllegalStateException("文件获取失败：路径错误"));
                    return;
                }
                if (file.isDirectory()) {
                    subscriber.onError(new IllegalStateException("文件获取失败：不是文件"));
                    return;
                }
                if (!file.exists()) {
                    subscriber.onError(new IllegalStateException("文件获取失败：文件不存在"));
                    return;
                }

                subscriber.onNext(FileUtils.readFile(file));
            }
        })
                .compose(RxUtils.applySchedulersIoAndMainThread());
    }

    /**
     * 保存文件
     *
     * @param file    the file path
     * @param content the content
     * @return the observable
     */
    public Observable<Boolean> saveFile(@NonNull File file, @NonNull String content) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (file == null) {
                    subscriber.onError(new IllegalStateException("文件保存失败：路径错误"));
                    return;
                }

                boolean b = FileUtils.writeByte(file, content);
                subscriber.onNext(b);
            }
        }).compose(RxUtils.applySchedulersIoAndMainThread());

    }

    /**
     * 获取文件列表，（md文件和文件夹）并转化为FileBean
     * Gets file list data.
     *
     * @param currentFolder the current folder
     * @return the file list data
     */
    public Observable<List<FileBean>> getFileListData(File currentFolder, String key) {
        File[] files = null;
        if (Check.isEmpty(key))//默认，文件夹和文件
            files = currentFolder
                    .listFiles(file -> file.isDirectory() ||
                            file.getName().endsWith(".md") ||
                            file.getName().endsWith(".markdown") ||
                            file.getName().endsWith(".mdown"));
        else //搜索
            files = currentFolder
                    .listFiles(file -> file.getName().contains(key) &&
                            (
                                    file.getName().endsWith(".md") ||
                                            file.getName().endsWith(".markdown") ||
                                            file.getName().endsWith(".mdown")));//只显示md和文件夹


        if (files == null)
            return getCommonObservable();

        return Observable
                .from(files)
                .filter(file -> file != null)
//                .filter(file -> file.isDirectory() || file.getName().endsWith(".md"))
                .flatMap(file -> mFileModel.getFileBeanObservable(file)
                        .filter(bean -> bean != null))
//                .toList()
                .toSortedList(this::fileSort)
                .compose(RxUtils.applySchedulersIoAndMainThread());
    }

    /**
     * 文件复制
     * Copy file observable.
     *
     * @param beans      the beans 待复制的文件或者文件夹集合
     * @param targetPath the targetPath 目标目录
     * @return the observable
     */
    public Observable<FileBean> copyFile(List<FileBean> beans, String targetPath) {
        return Observable
                .from(beans)
//                .flatMap(bean -> mFileModel.getFileObservable(bean))
                .map(bean ->
                        FileUtils.copyFolder(bean.absPath, targetPath) ? bean : null
                )
                .map(bean -> {
                    if (bean == null) throw new IllegalStateException("复制失败了");
                    else return bean;
                })
                .map(bean -> {//新路径改变
                    if (targetPath.endsWith(File.separator)) {
                        bean.absPath = targetPath + bean.name;
                    } else {
                        bean.absPath = targetPath + File.separator + bean.name;
                    }
                    return bean;
                })
                .compose(RxUtils.applySchedulersIoAndMainThread());
    }

    public Observable<FileBean> cutFile(List<FileBean> beans, String target) {
        return Observable
                .from(beans)
//                .flatMap(bean -> mFileModel.getFileObservable(bean))
                .map(bean ->
                        FileUtils.moveFolder(bean.absPath, target) ? bean : null
                )
                .map(bean -> {
                    if (bean == null) throw new IllegalStateException("剪切失败了");
                    else return bean;
                })
                .map(bean -> {//新路径改变
                    if (target.endsWith(File.separator)) {
                        bean.absPath = target + bean.name;
                    } else {
                        bean.absPath = target + File.separator + bean.name;
                    }
                    return bean;
                })
                .compose(RxUtils.applySchedulersIoAndMainThread());
    }

    /**
     * 过去一个数据空回调
     * Gets common observable.
     *
     * @param <T> the type parameter
     * @return the common observable
     */
    @SuppressWarnings("unchecked")
    private <T> Observable<T> getCommonObservable() {
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                subscriber.onNext((T) getNullList());
                subscriber.onCompleted();
            }
        });
    }

    /**
     * 获取一个空的List
     * Gets null list.
     *
     * @param <T> the type parameter
     * @return the null list
     */
    @SuppressWarnings("unchecked")
    private <T> T getNullList() {
        return (T) new ArrayList<>();
    }

    /**
     * 文件排序
     * File sort int.
     *
     * @param file1 the file 1
     * @param file2 the file 2
     * @return the int
     */
    private int fileSort(FileBean file1, FileBean file2) {
        //大体按照时间排序
        if ((file1.isDirectory && file2.isDirectory) || (!file1.isDirectory && !file2.isDirectory)) {
            return file1.name.compareTo(file2.name);
//            return -1 * file1.lastTime.compareTo(file2.lastTime);
        }
        //如果是文件和文件夹，则文件拍在前面
        if (file1.isDirectory && !file2.isDirectory) {
            return 1;
        } else {
            return -1;
        }
    }

    private static class DataManagerInstance {
        public static DataManager manager = new DataManager();

        public static DataManager getManager() {
            return manager;
        }
    }
}
