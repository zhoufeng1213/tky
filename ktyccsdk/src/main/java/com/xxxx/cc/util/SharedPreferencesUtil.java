package com.xxxx.cc.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import static com.xxxx.cc.global.Constans.USERBEAN_SAVE_TAG;

/**
 * @author zhoufeng
 * @date 2018/5/30
 */
public class SharedPreferencesUtil {

    public static void saveObjectBean(Context mContext, Object object, String key) {
        if (!USERBEAN_SAVE_TAG.equals(key)) {
            key = getUserId(mContext) + key;
        }
        SharedPreferences sp = mContext.getSharedPreferences(mContext.getPackageName(), Activity.MODE_PRIVATE);
        sp.edit().putString(key, new Gson().toJson(object)).apply();
    }

    public static Object getObjectBean(Context mContext, String key, Class cls) {
        if (!USERBEAN_SAVE_TAG.equals(key)) {
            key = getUserId(mContext) + key;
        }
        SharedPreferences sp = mContext.getSharedPreferences(mContext.getPackageName(), Activity.MODE_PRIVATE);
        String json = sp.getString(key, "");
        return "".equals(json) ? null : new Gson().fromJson(json, cls);
    }

    public static String getObjectBeanListJson(Context mContext, String key) {
        if (!USERBEAN_SAVE_TAG.equals(key)) {
            key = getUserId(mContext) + key;
        }
        SharedPreferences sp = mContext.getSharedPreferences(mContext.getPackageName(), Activity.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    public static void save(Context mContext, String key, String value) {
        if (!USERBEAN_SAVE_TAG.equals(key)) {
            key = getUserId(mContext) + key;
        }
        SharedPreferences sp = mContext.getSharedPreferences(mContext.getPackageName(), Activity.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }

    public static String getValue(Context mContext, String key) {
        if (!USERBEAN_SAVE_TAG.equals(key)) {
            key = getUserId(mContext) + key;
        }
        SharedPreferences sp = mContext.getSharedPreferences(mContext.getPackageName(), Activity.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    public static boolean getBoolean(Context mContext, String key) {
        if (!USERBEAN_SAVE_TAG.equals(key)) {
            key = getUserId(mContext) + key;
        }
        SharedPreferences sp = mContext.getSharedPreferences(mContext.getPackageName(), Activity.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    public static void setBoolean(Context mContext, String key, boolean value) {
        if (!USERBEAN_SAVE_TAG.equals(key)) {
            key = getUserId(mContext) + key;
        }
        SharedPreferences sp = mContext.getSharedPreferences(mContext.getPackageName(), Activity.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).apply();
    }

    public static String getUserId(Context mContext) {
//        Object objectBean = SharedPreferencesUtil.getObjectBean(mContext, USERBEAN_SAVE_TAG, UserBean.class);
//        if (objectBean != null) {
//            UserBean cacheUserBean = (UserBean) objectBean;
//            return cacheUserBean.getUserId();
//        }
        return "";
    }


}
