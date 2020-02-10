package com.xxxx.cc.util;

import android.util.Log;

import com.xxxx.cc.BuildConfig;


/**
 * @author zhoufeng
 * @date 2018/6/1
 */
public class LogUtils {

    public static void i(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.e("tag", msg);
        }
    }

    public static void e(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.e("tag", msg);
        }
    }

    public static void e(String msg) {
        if (BuildConfig.DEBUG) {
            Log.e("tag", msg);
        }
    }

}
