package com.xxxx.cc.global;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.sdk.keepbackground.work.DaemonEnv;

public class GlobalApplication extends MultiDexApplication {
    public static GlobalApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        DaemonEnv.init(this);
//        NetStateReceiver.registerNetworkStateReceiver(this);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }


    public static GlobalApplication getInstance() {
        return instance;
    }
}
