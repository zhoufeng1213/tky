package com.xxxx.cc.callback;

import com.xxxx.cc.model.UserBean;

/**
 * @author zhoufeng
 * @date 2019/8/13
 * @moduleName
 */
public interface LoginCallBack {
    void onSuccess(int code, String message, UserBean userBean);

    void onFailed(int code, String message);
}
