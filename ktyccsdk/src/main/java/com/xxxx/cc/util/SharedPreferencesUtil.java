package com.xxxx.cc.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

/**
 * @author zhoufeng
 * @date 2018/5/30
 */
public class SharedPreferencesUtil {

    public static void saveObjectBean(Context mContext, Object object, String key) {
        SharedPreferences sp = mContext.getSharedPreferences(mContext.getPackageName(), Activity.MODE_PRIVATE);
        sp.edit().putString(key, new Gson().toJson(object)).apply();
    }

    public static Object getObjectBean(Context mContext, String key, Class cls) {
        SharedPreferences sp = mContext.getSharedPreferences(mContext.getPackageName(), Activity.MODE_PRIVATE);
        String json = sp.getString(key, "");
        return "".equals(json) ? null : new Gson().fromJson(json, cls);
    }

    public static String getObjectBeanListJson(Context mContext, String key) {
        SharedPreferences sp = mContext.getSharedPreferences(mContext.getPackageName(), Activity.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    public static void save(Context mContext, String key, String value) {
        SharedPreferences sp = mContext.getSharedPreferences(mContext.getPackageName(), Activity.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }

    public static String getValue(Context mContext, String key) {
        SharedPreferences sp = mContext.getSharedPreferences(mContext.getPackageName(), Activity.MODE_PRIVATE);
        return sp.getString(key, "");
    }
}
