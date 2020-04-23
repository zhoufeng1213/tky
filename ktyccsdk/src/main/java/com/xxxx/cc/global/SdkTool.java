package com.xxxx.cc.global;

import android.content.Context;

import com.xxxx.cc.callback.CallPhoneBack;
import com.xxxx.cc.callback.LoginCallBack;

/**
 * @author zhoufeng
 * @date 2020/4/21
 * @moduleName
 */
public class SdkTool {

    public static void initOptions(Context context,String host){
        KtyCcOptionsUtil.init(context,host);
    }

    public static void initKtyCcSdk(Context context){
        KtyCcSdkTool.getInstance().initKtyCcSdk(context);
    }

    public static void login(Context context, String userName, String pwd, LoginCallBack loginCallBack){
        KtyCcNetUtil.loginBySdk(context, userName, pwd, loginCallBack);
    }

    public static void callPhone(Context mContext, String phoneNum, String userName, String headUrl, CallPhoneBack callPhoneBack){
        KtyCcSdkTool.getInstance().callPhone(mContext, phoneNum, userName, headUrl,"",callPhoneBack);
    }

    public static void goToHistoryActivity(Context context){
        KtyCcSdkTool.goToHistoryActivity(context);
    }

    public static void switchHostOption(Context context, String host){
        KtyCcOptionsUtil.switchHostOption(context, host);
    }

    public static void unRegister(Context context){
        KtyCcSdkTool.getInstance().unRegister(context);
        KtyCcSdkTool.getInstance().clearPhone(context);
    }

}
