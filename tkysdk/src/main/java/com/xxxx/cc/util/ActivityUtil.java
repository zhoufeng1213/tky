package com.xxxx.cc.util;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author zhoufeng
 * @date 2019/10/14
 * @moduleName
 */
public class ActivityUtil {

    public static List<String> activityList = new ArrayList<>();

    public static void addActivity(String name) {
        if (activityList != null) {
            activityList.add(name);
        }
    }

    public static void removeActivity(String name) {
        if (activityList != null && !TextUtils.isEmpty(name)) {
            Iterator<String> iterator = activityList.iterator();
            while (iterator.hasNext()) {
                String temp = iterator.next();
                if (name.equals(temp) || temp.contains(name)) {
                    iterator.remove();
                }
            }
        }
    }


    public static boolean isHasActivityByName(String name) {
        boolean result = false;
        if (activityList != null && !TextUtils.isEmpty(name)) {
            for (String temp : activityList) {
                if (name.contains(temp) || temp.contains(name)) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
}
