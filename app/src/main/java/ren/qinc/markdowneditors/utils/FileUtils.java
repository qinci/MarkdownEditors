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
package ren.qinc.markdowneditors.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 文件工具类
 * Created by 沈钦赐 on 16/1/17.
 */
public class FileUtils {
    /**
     * 递归删除文件夹
     *
     * @param dir the dir
     * @return the boolean
     */
    public static boolean deleteDir(@NonNull File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        if (dir == null) {
            return false;
        }
        return dir.delete();
    }

    /**
     * 获取文件夹大小
     *
     * @param file the file
     * @return the folder size
     * @throws Exception the exception
     */
    public static long getFolderSize(@NonNull File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 获取缓存目录
     *
     * @param context  the mContext
     * @param fileName the file name
     * @return file
     */
    public static File getCacheFile(@NonNull Context context, @NonNull String fileName) {
        File savedir = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            savedir = new File(context.getExternalCacheDir(), fileName);
        }

        if (savedir == null) {
            savedir = new File(context.getCacheDir(), fileName);
        }

        if (!savedir.exists()) {
            savedir.mkdirs();
        }
        return savedir;
    }

    /**
     * 获取文件目录
     *
     * @param context the mContext
     * @return file
     */
    public static String getFile(@NonNull Context context) {
        File savedir = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            savedir = context.getExternalFilesDir(null);
        }

        if (savedir == null) {
            savedir = context.getFilesDir();
        }

        if (!savedir.exists()) {
            savedir.mkdirs();
        }
        return savedir.getAbsolutePath();
    }

    /**
     * Gets root path.
     *
     * @param context the context
     * @return the root path
     * @description 获取存储路径(如果有内存卡，这是内存卡根目录，如果没有内存卡，则是软件的包file目录)
     */
    public static String getRootFolder(@NonNull Context context) {
        String rootPath = null;

        if (android.os.Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            rootPath = context.getFilesDir().getAbsolutePath();
        }
        return rootPath;
    }

    /**
     * 写字节
     * Write byte.
     *
     * @param path    the path
     * @param content the content
     * @throws IOException the io exception
     */
    public static boolean writeByte(@NonNull String path, @NonNull String content) {
        return writeByte(new File(path), content);
    }

    /**
     * 写字节
     * Write byte.
     *
     * @param file    the file
     * @param content the content
     * @throws IOException the io exception
     */
    public static boolean writeByte(@NonNull File file, @NonNull String content) {
        if (file.isDirectory()) {
            return false;
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
            }
        }
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            byte[] b = content.getBytes();
            out.write(b);
            out.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            return false;
        } finally {
            CloseableClose(out);
        }
    }

    /**
     * 追加
     * Add byte.
     *
     * @param fileName the file
     * @param content  the content
     */
    public static boolean addByte(@NonNull File fileName, @NonNull String content) {
        if (!fileName.isFile()) {
            return false;
        }
        OutputStream out = null;
        try {
            out = new FileOutputStream(fileName, true);
            byte[] b = content.getBytes();
            for (int i = 0; i < b.length; i++) {
                out.write(b[i]);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            return false;
        } finally {
            CloseableClose(out);
        }
    }

    /**
     * 读取文件，一次性读取
     * Read file string.
     *
     * @param file the file
     * @return the string
     */
    public static String readFile(@NonNull File file) {
        if (!file.isFile()) {
            return "";
        }
        Long filelength = file.length();     //获取文件长度
        if (filelength > Integer.MAX_VALUE) {
            return readFileByLines(file);
        }
        byte[] filecontent = new byte[filelength.intValue()];
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            in.read(filecontent);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
        } finally {
            CloseableClose(in);
        }
        return new String(filecontent);
    }

    /**
     * 按行读取
     * Read file by lines string.
     *
     * @param file the file
     * @return the string
     */
    public static String readFileByLines(@NonNull File file) {
        if (!file.isFile()) {
            return "";
        }
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            while ((tempString = reader.readLine()) != null) {
                builder.append(tempString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
        } finally {
            CloseableClose(reader);
        }

        return builder.toString();
    }

    /**
     * 复制文件
     * Copy file boolean.
     *
     * @param sourceFilePath the source file path
     * @param targetFilePath the target file path
     * @return the boolean
     */
    public static boolean copyFile(@NonNull String sourceFilePath, @NonNull String targetFilePath) {
        return copyFile(new File(sourceFilePath), new File(targetFilePath));
    }

    /**
     * 复制文件
     * Copy file boolean.
     *
     * @param sourceFile the source file
     * @param targetFile the target file
     * @return the boolean
     */
    private static boolean copyFile(@NonNull File sourceFile, @NonNull File targetFile) {
        if (!sourceFile.exists() || targetFile.exists()) {
            //原始文件不存在，目标文件已经存在
            return false;
        }
        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(sourceFile);
            output = new FileOutputStream(targetFile);
            int temp;
            while ((temp = input.read()) != (-1)) {
                output.write(temp);
            }
            input.close();
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
        } finally {
            CloseableClose(input);
            CloseableClose(output);
        }
        return true;
    }

    public static boolean copyFolder(@NonNull String oldPath, @NonNull String newPath) {
        return copyFolder(new File(oldPath), new File(newPath));
    }

    /**
     * 复制整个文件夹
     * Copy folder.
     *
     * @param oldFile the old path
     * @param newPath the new path
     */
    public static boolean copyFolder(@NonNull File oldFile, @NonNull File newPath) {
        if (oldFile.isFile())//如果是文件，直接复制
            return copyFile(oldFile, new File(newPath, oldFile.getName()));
        try {//文件夹
            newPath.mkdirs(); //如果文件夹不存在 则建立新文件夹
            File[] temps = oldFile.listFiles();
            File temp;
            boolean flag = true;
            for (int i = 0; i < temps.length; i++) {
                temp = temps[i];
                //文件夹里面
                if (temp.isFile()) {
                    File path = new File(newPath, oldFile.getName());
                    path.mkdirs();
                    File file = new File(path, temp.getName());
                    flag = copyFile(temp, file);
                } else if (temp.isDirectory()) {//如果是子文件夹
                    flag = copyFolder(temp, new File(newPath + File.separator + oldFile.getName()));
                }

                if (!flag) {
                    break;
                }
            }
            return flag;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 移动文件到指定目录
     *
     * @param oldPath String  如：/test/abc.md
     * @param newPath String  如：/abc.md
     */
    public static boolean moveFile(@NonNull String oldPath, @NonNull String newPath) {
        return moveFile(new File(oldPath), new File(newPath));
    }

    public static boolean moveFile(@NonNull File oldPath, @NonNull File newPath) {
        if (!oldPath.isFile()) {
            return false;
        }
        //如果是文件夹，这创建文件
        if (newPath.isDirectory()) newPath = new File(newPath, oldPath.getName());
        try {
            return oldPath.renameTo(newPath);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移动文件到指定目录
     *
     * @param oldPath String
     * @param newPath String
     */
    public static boolean moveFolder(@NonNull String oldPath, @NonNull String newPath) {
        return moveFolder(new File(oldPath), new File(newPath));
    }

    /**
     * 移动文件夹
     * Move folder.
     *
     * @param oldFile the old path
     * @param newPath the new path
     */
    public static boolean moveFolder(@NonNull File oldFile, File newPath) {
        return copyFolder(oldFile, newPath) && deleteFile(oldFile);
    }

    /**
     * 删除文件
     * Delete file boolean.
     *
     * @param file the file
     * @return the boolean
     */
    public static boolean deleteFile(File file) {
        return deleteDir(file);
    }

    public static void CloseableClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }


    /**
     * Try to return the absolute file path from the given Uri
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String uri2FilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

}
