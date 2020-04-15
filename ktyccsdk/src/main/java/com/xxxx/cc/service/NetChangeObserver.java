package com.xxxx.cc.service;

/**
 * @author zhoufeng
 * @date 2020/1/8
 * @moduleName
 */
public interface NetChangeObserver {
    /**
     * 网络连接回调 type为网络类型
     */
    void onNetConnected();

    /**
     * 没有网络
     */
    void onNetDisConnect();
}
