package com.xxxx.tky;

import android.app.Application;

import com.xxxx.cc.global.KtyCcOptionsUtil;
import com.xxxx.cc.global.KtyCcSdkTool;

/**
 * @author zhoufeng
 * @date 2019/8/7
 * @moduleName
 */
public class MyApplicaiton extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        KtyCcOptionsUtil.init(this,"https://tky.ketianyun.com");
        KtyCcSdkTool.getInstance().initKtyCcSdk(this);
    }
}
