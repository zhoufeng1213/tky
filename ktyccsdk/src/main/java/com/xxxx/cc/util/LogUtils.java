package com.xxxx.cc.util;

import android.util.Log;


/**
 * @author zhoufeng
 * @date 2018/6/1
 */
public class LogUtils {

    public static void i(String tag, String msg) {
        Log.i(tag, msg);

    }

    public static void e(String tag, String msg) {
        Log.e(tag, msg);
    }

    public static void e(String msg) {
        Log.e("tag", msg);

    }

}
