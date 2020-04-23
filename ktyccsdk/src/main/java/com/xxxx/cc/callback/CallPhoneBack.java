package com.xxxx.cc.callback;

/**
 * @author zhoufeng
 * @date 2019/8/13
 * @moduleName
 */
public interface CallPhoneBack {
    public void onSuccess(String callId);

    public void onFailed(String message);
    public void watchPhoneStatus(int status);
}
