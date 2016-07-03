package ren.qinc.markdowneditors.presenter;


import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import ren.qinc.markdowneditors.base.mvp.BasePresenter;
import ren.qinc.markdowneditors.entity.FileBean;
import ren.qinc.markdowneditors.utils.Check;
import ren.qinc.markdowneditors.utils.FileUtils;
import rx.Subscriber;

/**
 * 主界面的Presenter
 * Created by 沈钦赐 on 16/1/18.
 */
public class FolderManagerPresenter extends BasePresenter<IFolderManagerView> {
    private Stack<String> fileStack = new Stack<>();
    private List<FileBean> files = new ArrayList<>();
    /**
     * 用来保存FileBean缓存，可以用来复制粘贴等操作
     */
    private List<FileBean> temp;

    public FolderManagerPresenter(List<FileBean> files) {
        this.files = files;
    }

    private void getFileList(File currentFolder) {
        getFileList(currentFolder, null);
    }

    /**
     * 获取文件列表集合
     * Get file list.
     *
     * @param currentFolder the current folder
     * @param key           the key 搜索用的key
     */
    private void getFileList(File currentFolder, String key) {
        if (currentFolder == null) {
            return;
        }
        if (!currentFolder.exists()) {
            callFailure(-1, "文件夹不存在", IFolderManagerView.CALL_GET_FILES);
            return;
        }

        if (!currentFolder.isDirectory()) {//不是文件夹
            callFailure(-1, "不是文件夹", IFolderManagerView.CALL_GET_FILES);
            return;
        }

        //显示进度条
        callShowProgress(null, false, IFolderManagerView.CALL_GET_FILES);
        mCompositeSubscription.add(
                mDataManager.getFileListData(currentFolder, key)
                        .subscribe(new Subscriber<List<FileBean>>() {
                                       @Override
                                       public void onCompleted() {
                                           mCompositeSubscription.remove(this);//任务完成
                                           callHideProgress(IFolderManagerView.CALL_GET_FILES);
                                       }

                                       @Override
                                       public void onError(Throwable e) {
                                           mCompositeSubscription.remove(this);//任务完成
                                           callFailure(-1, "异常", IFolderManagerView.CALL_GET_FILES);
                                       }

                                       @Override
                                       public void onNext(List<FileBean> fileBeans) {
                                           files.clear();
                                           files.addAll(fileBeans);
                                           if (getMvpView() != null)
                                               getMvpView().getFileListSuccess(fileBeans);
                                       }
                                   }
                        ));
    }


    /**
     * 返回上一级
     * Back folder string.
     *
     * @return 是否返回了
     */
    public boolean backFolder() {
        if (fileStack.size() > 1) {
            fileStack.pop();//最后一个不要了
            //设置tab
            callOtherSuccess(IFolderManagerView.CALL_REMOVE_TAB);
            getFileList(new File(currentPath()));
            return true;
        }
        return false;
    }

    /**
     * 返回某一级，index表示第几个元素
     * Back folder boolean.
     *
     * @param index the index
     * @return the boolean
     */
    public boolean backFolder(int index) {
        //把fileStack pop到剩下index个
        //最后一个peek，然后进入
        boolean isRemoved = false;
        while (fileStack.size() > index + 1) {
            fileStack.pop();
            callOtherSuccess(IFolderManagerView.CALL_REMOVE_TAB);
            isRemoved = true;
        }
        if (isRemoved) {
            refreshCurrentPath();//刷新当前文件夹
        }
        return isRemoved;
    }

    /**
     * 进入文件夹
     * Enter folder string.
     *
     * @param path the path
     * @return the string
     */
    public void enterFolder(String path) {
        if (Check.isEmpty(path)) return;
        File file = new File(path);
        if (!file.isDirectory()) return;
        fileStack.push(path);
        //这里设置tab
        if (getMvpView() != null) getMvpView().addTab(file.getName());
        getFileList(file);
    }

    /**
     * 初始化根目录
     * Init root string.
     *
     * @return the string
     */
    public void initRoot(Context context) {
        fileStack.clear();
        String rootFolder = FileUtils.getFile(context);
        if (rootFolder != null) {
            fileStack.push(rootFolder);
            File file = new File(currentPath());
            getFileList(file);
            //这里设置tab
            if (getMvpView() != null) getMvpView().addTab("本地");//1
        } else {
            callFailure(-1, "路径找不到", IFolderManagerView.CALL_GET_FILES);
        }
    }

    /**
     * 刷新当前文件夹
     * Refresh current path.
     */
    public void refreshCurrentPath() {
        //如果是编辑模式，则关闭
        closeEditMode();
        //获取当前路径
        String path = currentPath();
        if (Check.isEmpty(path)) return;
        getFileList(new File(path));
    }

    /**
     * 搜索当前文件夹
     * Search current path.
     *
     * @param key the key
     */
    public void searchCurrentPath(String key) {
        String path = currentPath();
        if (Check.isEmpty(path)) return;
        getFileList(new File(path), key);
    }

    /**
     * 创建文件夹
     * Create folder.
     *
     * @param name the name 文件夹名字
     */
    public boolean createFolder(String name) {
        if (Check.isEmpty(name)) return false;
        //1.判断是否存在，如果存在，则提示，不关闭对话框
        //2.创建文件，提示成功，提示失败

        String currentPath = currentPath();
        if (Check.isEmpty(currentPath)) return false;
        File path = new File(currentPath, name);

        if (path.exists() && path.isDirectory()) {
            callFailure(-1, "文件夹已经存在！", IFolderManagerView.CALL_CREATE_FOLDER);
            return false;
        }

        path.mkdir();//创建文件夹
        if (path.exists() && path.isDirectory()) {
            //刷新当前文件夹
            refreshCurrentPath();
            return true;
        } else {
            callFailure(-1, "创建文件夹失败！", IFolderManagerView.CALL_CREATE_FOLDER);
        }
        return false;
    }

    /**
     * 在当前目录下，是否存在要创建的文件夹名
     * Create foloder is exists boolean.
     *
     * @param name the name
     * @return the boolean
     */
    public boolean createFoloderIsExists(String name) {
        String currentPath = currentPath();
        if (Check.isEmpty(currentPath)) return false;
        File path = new File(currentPath, name);
        if (path.exists() && path.isDirectory()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean fileIsExists(String name) {
        String currentPath = currentPath();
        if (Check.isEmpty(currentPath)) return false;
        File path = new File(currentPath, name);
        if (path.exists()) {
            return true;
        } else {
            return false;
        }
    }


    public String currentPath() {
        return fileStack.peek();
    }


    //===========编辑模式相关==============

    public void closeEditMode() {
        if (files == null || mEditMode != EDIT_MODE_OPEN) return;
        for (FileBean file : files) {
            file.isSelect = false;
        }
        mEditMode = EDIT_MODE_CLOSE;
        callOtherSuccess(IFolderManagerView.CALL_CLOSE_EDIT_MODE);
    }

    public void openEditMode() {
        if (files == null || mEditMode == EDIT_MODE_OPEN) return;
        mEditMode = EDIT_MODE_OPEN;
        callOtherSuccess(IFolderManagerView.CALL_EDIT_MODE);
    }

    public boolean isEditMode() {
        return mEditMode == EDIT_MODE_OPEN;
    }


    /**
     * 选择模式(操作文件,多选，复制粘贴等)
     * 0：无
     * 1：编辑模式
     * 2：复制粘贴模式
     * 3：剪切粘贴模式
     * 4：删除
     * The Is selectmode.
     */
    private int mEditMode = EDIT_MODE_CLOSE;
    /*
     * editMode
     * 0：无
     * 1：编辑模式
     * 2：复制粘贴模式
     * 3：剪切粘贴模式
     * 4：删除
     */
    private static final int EDIT_MODE_CLOSE = 0;
    private static final int EDIT_MODE_OPEN = 1;
    private static final int EDIT_MODE_COPY_PARSE = 2;
    private static final int EDIT_MODE_CUT_PARSE = 3;
    private static final int EDIT_MODE_DELETE = 4;

    public int getSelectCount() {
        if (files == null) return 0;
        int i = 0;
        for (FileBean file : files) {
            if (file.isSelect) i++;
        }
        return i;
    }

    /**
     * 获取当前选择的bean
     * Get select bean file bean.
     *
     * @return the file bean
     */
    public FileBean getSelectBean() {
        FileBean bean = null;
        for (FileBean file : files) {
            if (file.isSelect) bean = file;
        }
        return bean;
    }

    /**
     * 重命名
     * Rename.
     *
     * @param bean the bean
     */
    public boolean rename(FileBean bean, String targetName) {
        if (bean == null || Check.isEmpty(targetName)) return false;

        int end = bean.absPath.lastIndexOf(bean.name);
        String targetPath = bean.absPath.substring(0, end) + targetName;

        //重命名
        File tempFile = new File(bean.absPath);
        if (!tempFile.renameTo(new File(targetPath)))
            return false;

        bean.name = targetName;
        bean.absPath = targetPath;

        //更新列表
        if (getMvpView() != null) getMvpView().updatePosition(files.indexOf(bean), bean);

        return true;
    }

    /**
     * 将已经选择的fileBean缓存起来
     * Select temp boolean.
     *
     * @return the boolean
     */
    private boolean selectTemp() {
        if (temp == null) {
            temp = new ArrayList<>();
        } else {
            temp.clear();
        }
        for (FileBean file : files) {
            if (file.isSelect) temp.add(file);
        }

        if (temp.isEmpty()) {
            temp = null;
            return false;
        }

        return true;


    }

    /**
     * 复制
     * Copy.
     */
    public void copy() {
        if (!selectTemp()) {
            return;
        }
        //先关闭编辑模式
        closeEditMode();
        //复制模式
        mEditMode = EDIT_MODE_COPY_PARSE;
        //进入复制粘贴模式
        callOtherSuccess(IFolderManagerView.CALL_PASTE_MODE);
    }

    /**
     * 剪切
     * Cut.
     */
    public void cut() {
        if (!selectTemp()) {
            return;
        }
        //先关闭编辑模式
        closeEditMode();
        //剪切模式
        mEditMode = EDIT_MODE_CUT_PARSE;
        //进入剪切粘贴模式
        callOtherSuccess(IFolderManagerView.CALL_PASTE_MODE);
    }

    public boolean delete() {
        if (!selectTemp()) {
            return false;
        }
        if (temp == null) {
            return false;
        }

        for (FileBean bean : temp) {
            if (!FileUtils.deleteFile(new File(bean.absPath))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 粘贴
     * Paste.
     */
    public void paste() {
        if (getMvpView() == null) return;
        if (temp == null) return;
        if (mEditMode == EDIT_MODE_CUT_PARSE) {//剪切粘贴
            //判断是否要剪切的文件夹是否包含在路径
            String path = currentPath();
            for (FileBean bean : temp) {
                if (path.contains(bean.absPath)) {
                    //当前文件夹包含在剪切文件夹里面
                    callFailure(-1, "当前路径不能粘贴", IFolderManagerView.CALL_OTHER);
                    return;
                }
            }

            mCompositeSubscription.add(
                    mDataManager.cutFile(temp, path)
                            .subscribe(new Subscriber<FileBean>() {
                                @Override
                                public void onCompleted() {
                                    mCompositeSubscription.remove(this);//任务完成
                                    callHideProgress(IFolderManagerView.CALL_COPY_PASTE);
                                    mEditMode = EDIT_MODE_CLOSE;
                                    callOtherSuccess(IFolderManagerView.CALL_CLOSE_PASTE_MODE);
                                }

                                @Override
                                public void onError(Throwable e) {
                                    mCompositeSubscription.remove(this);//任务完成
                                    callFailure(-1, "粘贴错误:" + e.getMessage(), IFolderManagerView.CALL_COPY_PASTE);
                                }

                                @Override
                                public void onNext(FileBean file) {//粘贴成功
                                    getMvpView().addFilePosition(0, file);
                                }
                            })
            );
        } else if (mEditMode == EDIT_MODE_COPY_PARSE) {//复制粘贴
            String path = currentPath();
            for (FileBean bean : temp) {
                if (path.equals(bean.absPath)) {
                    //当前文件夹包含在剪切文件夹里面
                    callFailure(-1, "文件已经存在", IFolderManagerView.CALL_OTHER);
                    return;
                }
            }
            mCompositeSubscription.add(
                    mDataManager.copyFile(temp, path)
                            .subscribe(new Subscriber<FileBean>() {
                                @Override
                                public void onCompleted() {
                                    mCompositeSubscription.remove(this);//任务完成
                                    callHideProgress(IFolderManagerView.CALL_COPY_PASTE);
                                    mEditMode = EDIT_MODE_CLOSE;
                                    callOtherSuccess(IFolderManagerView.CALL_CLOSE_PASTE_MODE);
                                }

                                @Override
                                public void onError(Throwable e) {
                                    mCompositeSubscription.remove(this);//任务完成
                                    callFailure(-1, "粘贴错误:" + e.getMessage(), IFolderManagerView.CALL_COPY_PASTE);
                                }

                                @Override
                                public void onNext(FileBean file) {//粘贴成功
                                    getMvpView().addFilePosition(0, file);
                                }
                            })
            );
        }
    }
}
