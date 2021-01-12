package com.xxxx.cc.global;

import android.content.Context;
import android.support.annotation.NonNull;

import com.xxxx.cc.callback.CallPhoneBack;
import com.xxxx.cc.callback.LoginCallBack;
import com.xxxx.cc.util.LinServiceManager;

/**
 * @author zhoufeng
 * @date 2020/4/21
 * @moduleName
 */
public class SdkTool {

    public static void login(Context context, String userName, String pwd, LoginCallBack loginCallBack){
        KtyCcNetUtil.loginBySdk(context, userName, pwd, loginCallBack);
    }

    public static void callPhone(@NonNull Context mContext, @NonNull String phoneNum, @NonNull String userName,String extendedData, @NonNull CallPhoneBack callBack){
        KtyCcSdkTool.getInstance().callPhone(mContext, phoneNum, userName, "","",callBack,extendedData);
    }
    public static void hookCall(){
        KtyCcSdkTool.getInstance().hookCall();
    }
    public static void switchAudio(Context context,boolean isHandsfree){
        KtyCcSdkTool.getInstance().switchAudio(context,isHandsfree);
    }

    public static void unRegister(Context context){
        KtyCcSdkTool.getInstance().clearPhone(context);
        KtyCcSdkTool.getInstance().unRegister(context);
    }

}
