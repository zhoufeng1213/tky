package com.xxxx.tky;

import android.app.Application;

import com.kty.mars.baselibrary.LibContext;
import com.xxxx.cc.global.KtyCcOptionsUtil;
import com.xxxx.cc.global.KtyCcSdkTool;
import com.xxxx.cc.save.FileSaveManager;
import com.xxxx.cc.util.LogCatHelper;

/**
 * @author zhoufeng
 * @date 2019/8/7
 * @moduleName
 */
public class MyApplicaiton extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        KtyCcOptionsUtil.init(this, "https://tky.ketianyun.com");
        KtyCcSdkTool.getInstance().initKtyCcSdk(this);
        LibContext.getInstance().init(this, false);
        String logPath=getApplicationContext().getExternalCacheDir()+"/log/";
        FileSaveManager.getInstance().deleteOutlineFiles(logPath,3);
        new Thread() {
            public void run() {
                LogCatHelper.getInstance(getApplicationContext(), logPath).start();
            }
        }
                .start();
    }
}
