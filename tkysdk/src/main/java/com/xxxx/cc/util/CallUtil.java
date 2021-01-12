package com.xxxx.cc.util;

import android.app.Activity;
import android.content.Intent;

import com.xxxx.cc.service.FloatingImageDisplayService;
import com.xxxx.cc.ui.CallActivity;
import com.xxxx.cc.util.rom.FloatWindowManager;

/**
 * @author zhoufeng
 * @date 2020/4/8
 * @moduleName
 */
public class CallUtil {

    /**
     * 因为CallActivity设置了singleInstance，需要在onstart方法中执行这个方法
     *
     * @param activity
     */
    public static void restartConferenceActivityToOtherActivity(Activity activity) {
        String meetingActivityName = "com.xxxx.cc.ui.CallActivity";
        //判断现在的activity中是否包含会议activity
        if (activity != null && ActivityUtil.isHasActivityByName(meetingActivityName) && !(activity.getPackageName() + "." + activity.getLocalClassName()).contains(meetingActivityName)) {
            LogUtils.e("还在打电话，重新进入电话界面1");
            if (!FloatWindowManager.getInstance().checkPermission(activity) && !FloatingImageDisplayService.isStarted
                    && CallActivity.isReturnCall
            ) {
                LogUtils.e("还在打电话，重新进入电话界面2");
                activity.startActivity(new Intent(activity, CallActivity.class));
            }
        }
    }

}
