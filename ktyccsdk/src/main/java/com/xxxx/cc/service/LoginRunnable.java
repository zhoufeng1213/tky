package com.xxxx.cc.service;

import com.xxxx.cc.callback.LoginCallBack;
import com.xxxx.cc.global.ErrorCode;
import com.xxxx.cc.model.UserBean;
import com.xxxx.cc.util.LinServiceManager;
import com.xxxx.cc.util.LogUtils;

import org.linphone.core.Core;
import org.linphone.core.CoreListenerStub;
import org.linphone.core.ProxyConfig;
import org.linphone.core.RegistrationState;

/**
 * @author zhoufeng
 * @date 2019/8/8
 * @moduleName
 */
public class LoginRunnable implements Runnable {

    private UserBean userBean;
    private LoginCallBack loginCallBack;


    public LoginRunnable(UserBean userBean, LoginCallBack loginCallBack) {
        this.userBean = userBean;
        this.loginCallBack = loginCallBack;
    }

    @Override
    public void run() {
        LogUtils.e("service还没起2");
        if(LinphoneService.isReady()){
            LogUtils.e("service起来了");
            LinServiceManager.addListener(new CoreListenerStub(){
                @Override
                public void onRegistrationStateChanged(Core lc, ProxyConfig cfg, RegistrationState cstate, String message) {
                    super.onRegistrationStateChanged(lc, cfg, cstate, message);
                    if (cstate == RegistrationState.Ok) {
                        LinphoneService.setRegister(true);
                        loginCallBack.onSuccess(ErrorCode.SUCCESS, "登录成功");
                    }else{
                        LogUtils.e("注册失败了-----》" + cstate.name());
                        loginCallBack.onFailed(ErrorCode.REGISTER_ERROR, "注册失败");
                    }
                }
            });
            LinServiceManager.setLinPhoneConfig(userBean);
        }
    }
}
