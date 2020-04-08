package com.xxxx.tky;


import com.kty.mars.baselibrary.LibContext;
import com.xxxx.cc.global.GlobalApplication;
import com.xxxx.cc.global.KtyCcOptionsUtil;
import com.xxxx.cc.global.KtyCcSdkTool;
import com.xxxx.cc.model.UserBean;
import com.xxxx.cc.save.FileSaveManager;
import com.xxxx.cc.service.FloatingImageDisplayService;
import com.xxxx.cc.util.LinServiceManager;
import com.xxxx.cc.util.LogCatHelper;
import com.xxxx.cc.util.LogUtils;
import com.xxxx.cc.util.SharedPreferencesUtil;
import com.xxxx.tky.util.HomeKeyListener;

import static com.xxxx.cc.global.Constans.USERBEAN_SAVE_TAG;


/**
 * @author zhoufeng
 * @date 2019/8/7
 * @moduleName
 */
public class MyApplicaiton extends GlobalApplication {
    private HomeKeyListener mHomeKeyListener;
    private UserBean cacheUserBean;
    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
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
        initHomeKeyListener();
        Object objectBean = SharedPreferencesUtil.getObjectBean(this, USERBEAN_SAVE_TAG, UserBean.class);
        if (objectBean != null) {
            cacheUserBean = (UserBean) objectBean;
        }
    }

    private void initHomeKeyListener() {
        mHomeKeyListener = new HomeKeyListener(this);
        mHomeKeyListener.setOnHomePressedListener(new HomeKeyListener.OnHomePressedListener() {
            @Override
            public void onHomePressed() {
//                if (!FloatingImageDisplayService.isStarted) {
//                    LinServiceManager.unRegisterOnlineLinPhone(cacheUserBean, false);
//                    LogUtils.e("KEYCODE_HOME");
//                    if (!FloatingImageDisplayService.isStarted) {
//                        LinServiceManager.unRegisterOnlineLinPhone(cacheUserBean, false);
//                    }
//                }
            }

            @Override
            public void onHomeLongPressed() {
//                if (!FloatingImageDisplayService.isStarted) {
//                    LinServiceManager.unRegisterOnlineLinPhone(cacheUserBean, false);
//                    LogUtils.e("KEYCODE_HOME");
//                    if (!FloatingImageDisplayService.isStarted) {
//                        LinServiceManager.unRegisterOnlineLinPhone(cacheUserBean, false);
//
//                    }
//                }
            }
        });
        mHomeKeyListener.startWatch();
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        mHomeKeyListener.stopWatch();

    }



}
