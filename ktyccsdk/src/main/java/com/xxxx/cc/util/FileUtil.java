package com.xxxx.cc.util;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * @author zhoufeng
 * @date 2019/8/5
 * @moduleName
 */
public class FileUtil {

    public static String getFilePath(Context mContext) {
        File file = new File(mContext.getExternalCacheDir(), "/voiceCache");
        if (!file.exists()) {
            boolean flag = file.mkdir();
            Log.e("tag", "flag:" + flag);
        }
        return file.getAbsolutePath();
    }

    public static void createFilePath(Context mContext, String fileName) {
        String fileStr = getFilePath(mContext) + "/" + fileName;
        File file = new File(fileStr);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
