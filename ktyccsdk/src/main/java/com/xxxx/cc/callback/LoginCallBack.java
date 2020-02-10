package com.xxxx.cc.callback;

/**
 * @author zhoufeng
 * @date 2019/8/13
 * @moduleName
 */
public interface LoginCallBack {
    public void onSuccess(int code, String message);

    public void onFailed(int code, String message);
}
