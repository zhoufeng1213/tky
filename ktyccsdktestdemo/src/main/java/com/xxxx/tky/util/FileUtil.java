package com.xxxx.tky.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

/**
 * @author zhoufeng
 * @date 2019/11/6
 * @moduleName
 */
public class FileUtil {

    public static String getSavePath(Context mContext) {
        String path = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) || !Environment.isExternalStorageRemovable()) {
            try {
                path = mContext.getExternalCacheDir().getAbsolutePath();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (TextUtils.isEmpty(path)) {
                path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
            }
        } else {
            path = mContext.getCacheDir().getAbsolutePath();
        }
        return path;
    }
}
