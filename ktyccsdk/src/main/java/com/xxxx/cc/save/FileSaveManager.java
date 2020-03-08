package com.xxxx.cc.save;

import com.kty.mars.baselibrary.LibContext;
import com.xxxx.cc.util.FileUtil;
import com.xxxx.cc.util.LogUtils;
import com.xxxx.cc.util.StringUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author wei.zhang
 */
public class FileSaveManager {
    public static final String defaultDirPath = FileUtil.getCachePath(LibContext.getInstance().getContext()) + "log/";
    private static final String pre_file_name = "tky_log_";

    /**
     * 静态内部类
     */

    private static class SingletonHolder {
        private static final FileSaveManager sIntance = new FileSaveManager();

    }

    private FileSaveManager() {
        FileUtil.createDirs(defaultDirPath);
    }

    public static FileSaveManager getInstance() {
        return SingletonHolder.sIntance;
    }

    /**
     * 向文件里写内容
     *
     * @param dirPath  文件保存的目录
     * @param message  需要写入的内容
     * @param isAppend 内容追加
     * @return 成功或失败
     */
    public boolean writeMessageToFile(String dirPath, String message, boolean isAppend) {
        if (StringUtils.isEmpty(dirPath)) {
            dirPath = defaultDirPath;
        } else {
            File fileDir = new File(dirPath);
            if (!fileDir.exists()) {
                FileUtil.createDirs(dirPath);
            }
        }

        try {
            String name = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            String fileName = pre_file_name + name + ".log";
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            message = date + "\n" + message;
            return FileUtil.writeFile(message, dirPath + fileName, isAppend);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * 获取最近几天文件
     *
     * @param dirPath 文件保存的目录
     * @param days    天数
     * @return days天的文件
     */
    public List<File> getRecentFiles(String dirPath, int days) {
        if (days < 0) {
            days = 0;
        }
        List<File> files = new ArrayList<>();
        if (StringUtils.isEmpty(dirPath)) {
            dirPath = defaultDirPath;
        } else {
            File fileDir = new File(dirPath);
            if (!fileDir.exists()) {
                return files;
            }
        }

        try {
            long currentTime = System.currentTimeMillis();
            long dealTime = currentTime - days * 24 * 60 * 60 * 1000;
            String dealName = pre_file_name + new SimpleDateFormat("yyyy-MM-dd").format(new Date(dealTime)) + ".log";
            File dirList = new File(dirPath);
            if (dirList.exists()) {
                File[] listFiles = dirList.listFiles();
                if (null != listFiles && listFiles.length > 0) {
                    for (File file : listFiles) {
                        String fileName = file.getName();
                        if (fileName.startsWith(pre_file_name)) {
                            int code = fileName.compareTo(dealName);
                            if (code > 0) {
                                files.add(file);
                            }
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return files;

    }

    /**
     * 获取最近几天文件路径
     *
     * @param dirPath 文件保存的目录
     * @param days    天数
     * @return days天的文件路径
     */
    public List<String> getRecentFilePaths(String dirPath, int days) {
        if (days < 0) {
            days = 0;
        }
        List<String> paths = new ArrayList<>();
        if (StringUtils.isEmpty(dirPath)) {
            dirPath = defaultDirPath;
        } else {
            File fileDir = new File(dirPath);
            if (!fileDir.exists()) {
                return paths;
            }
        }

        try {
            long currentTime = System.currentTimeMillis();
            long dealTime = currentTime - days * 24 * 60 * 60 * 1000;
            String dealName =  new SimpleDateFormat("yyyyMMdd").format(new Date(dealTime));
            LogUtils.e("dealName:"+dealName);
            File dirList = new File(dirPath);
            if (dirList.exists()) {
                File[] listFiles = dirList.listFiles();
                if (null != listFiles && listFiles.length > 0) {
                    for (File file : listFiles) {
                        String fileName = file.getName();
                        LogUtils.e("fileName:"+fileName);
                        if (fileName.startsWith(dealName)) {
                            LogUtils.e("startsWith:");
                                paths.add(file.getAbsolutePath());

                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtils.e("paths:"+paths.size());
        return paths;

    }

    /**
     * 删除days天前的文件
     *
     * @param dirPath 文件所在目录
     * @param days    天数
     */
    public void deleteOutlineFiles(String dirPath, int days) {
        if (days < 0) {
            days = 0;
        }
        if (StringUtils.isEmpty(dirPath)) {
            dirPath = defaultDirPath;
        } else {
            File fileDir = new File(dirPath);
            if (!fileDir.exists()) {
                return;
            }
        }
        try {
            long currentTime = System.currentTimeMillis();
            long dealTime = currentTime - days * 24 * 60 * 60 * 1000;
            String dealName = new SimpleDateFormat("yyyyMMdd").format(new Date(dealTime));
            File dirList = new File(dirPath);
            if (dirList.exists()) {
                File[] listFiles = dirList.listFiles();
                if (null != listFiles && listFiles.length > 0) {
                    for (File file : listFiles) {
                        String fileName = file.getName();
                        if (fileName.startsWith(dealName)) {
                            file.delete();
                        }
                        }


                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
