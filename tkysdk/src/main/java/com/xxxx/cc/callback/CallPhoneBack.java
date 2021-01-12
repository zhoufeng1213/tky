package com.xxxx.cc.callback;

/**
 * @author zhoufeng
 * @date 2019/8/13
 * @moduleName
 */
public interface CallPhoneBack {
    void onSuccess(String callId);

    void onFailed(String message);
    void watchPhoneStatus(int status);
}
