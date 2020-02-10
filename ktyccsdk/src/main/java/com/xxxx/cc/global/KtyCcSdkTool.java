package com.xxxx.cc.global;

import android.content.Context;
import android.content.Intent;

import com.xxxx.cc.model.UserBean;
import com.xxxx.cc.service.LinphoneService;
import com.xxxx.cc.ui.CallActivity;
import com.xxxx.cc.ui.HistoryActivity;
import com.xxxx.cc.util.LinServiceManager;
import com.xxxx.cc.util.LogUtils;
import com.xxxx.cc.util.SharedPreferencesUtil;
import com.xxxx.cc.util.ToastUtil;
import com.xxxx.cc.util.db.DbUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.MemoryCookieStore;
import com.zhy.http.okhttp.https.HttpsUtils;

import org.linphone.core.Core;
import org.linphone.core.CoreListenerStub;
import org.linphone.core.ProxyConfig;
import org.linphone.core.RegistrationState;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

import static com.xxxx.cc.global.Constans.KTY_CC_BASE_URL;
import static com.xxxx.cc.global.Constans.KTY_CC_BEGIN;
import static com.xxxx.cc.global.Constans.USERBEAN_SAVE_TAG;
import static com.xxxx.cc.global.Constans.VOICE_RECORD_PREFIX;

/**
 * @author zhoufeng
 * @date 2019/8/7
 * @moduleName
 */
public class KtyCcSdkTool {

    private static volatile KtyCcSdkTool ktyCcSdkTool;

    private KtyCcSdkTool() {

    }

    public static KtyCcSdkTool getInstance() {
        if (ktyCcSdkTool == null) {
            synchronized (KtyCcSdkTool.class) {
                if (ktyCcSdkTool == null) {
                    ktyCcSdkTool = new KtyCcSdkTool();
                }
            }
        }
        return ktyCcSdkTool;
    }

    /**
     * 初始化db和net
     * @param context
     */
    public void initKtyCcSdk(Context context){
        initNetConfig();
        DbUtil.init(context);
    }

    private void initNetConfig() {
        CookieJarImpl cookieJar = new CookieJarImpl(new MemoryCookieStore());
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .cookieJar(cookieJar)
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }

    /**
     * 清除用户数据
     *
     * @param context
     */
    public void unRegister(Context context) {
        LinphoneService.setRegister(false);
        //清除用户数据
        SharedPreferencesUtil.save(context, USERBEAN_SAVE_TAG, "");
        SharedPreferencesUtil.save(context, VOICE_RECORD_PREFIX, "");
        SharedPreferencesUtil.save(context, KTY_CC_BASE_URL, "");
        SharedPreferencesUtil.save(context, KTY_CC_BEGIN, "");
        SharedPreferencesUtil.save(context, Constans.KTY_CUSTOM_BEGIN,"");
        //删除数据库数据
        DbUtil.clearDb();
    }


    /**
     * 拨打电话
     */
    public void callPhone(Context mContext, String phoneNum, String userName, String headUrl) {
//        if (LinphoneService.isReady() && LinphoneService.getCore() != null && LinphoneService.isRegister()) {
//            Intent intent = new Intent(mContext, CallActivity.class);
//            intent.putExtra("phoneNum", phoneNum);
//            intent.putExtra("name", userName);
//            intent.putExtra("headUrl", headUrl);
//            intent.putExtra("linPhoneRegistStatus", true);
//            mContext.startActivity(intent);
//        }else{
//            ToastUtil.showToast(mContext, "请登录");
//        }

        //先判断用户是否保存在了本地
        try {
            LinServiceManager.hookCall();
            Object objectBean = SharedPreferencesUtil.getObjectBean(mContext, USERBEAN_SAVE_TAG, UserBean.class);
            if (objectBean != null) {
                UserBean cacheUserBean = (UserBean) objectBean;
                //判断service是否已经起来了
                if (LinphoneService.isReady()) {
                    if (LinphoneService.getCore() != null) {
                        if(LinphoneService.isRegister()){
                            goToCallActivity(mContext,phoneNum,userName,headUrl);
                        }else{
                            LinphoneService.getCore().addListener(new CoreListenerStub() {
                                @Override
                                public void onRegistrationStateChanged(Core core, ProxyConfig cfg, RegistrationState
                                        state, String message) {

                                    if (state == RegistrationState.Ok) {
                                        LinServiceManager.removeListener(this);
                                        LinphoneService.setRegister(true);
                                        goToCallActivity(mContext,phoneNum,userName,headUrl);
                                    } else if (state == RegistrationState.Failed) {
                                        LogUtils.e("注册失败了-----》" + state.name());
                                        ToastUtil.showToast(mContext, "请登录");
                                    }else{
                                        LogUtils.e("注册失败了-----》" + state.name());
                                    }
                                }
                            });
                            LinServiceManager.setLinPhoneConfig(cacheUserBean);
                        }
                    } else {
                        ToastUtil.showToast(mContext, "请登录");
                    }
                }
            } else {
                ToastUtil.showToast(mContext, "请登录");
            }
        } catch (Exception e) {
            ToastUtil.showToast(mContext, "请登录");
        }

    }

    private static void goToCallActivity(Context mContext, String phoneNum, String userName, String headUrl){
        //开始拨打电话
        Intent intent = new Intent(mContext, CallActivity.class);
        intent.putExtra("phoneNum", phoneNum);
        intent.putExtra("name", userName);
        intent.putExtra("headUrl", headUrl);
        intent.putExtra("linPhoneRegistStatus", true);
        mContext.startActivity(intent);
    }


    public static void goToHistoryActivity(Context context) {
        //先判断用户是否保存在了本地
        try {
            Object objectBean = SharedPreferencesUtil.getObjectBean(context, USERBEAN_SAVE_TAG, UserBean.class);
            if (objectBean != null) {
                context.startActivity(new Intent(context, HistoryActivity.class));
            }else{
                ToastUtil.showToast(context, "请登录");
            }
        } catch (Exception e) {
            ToastUtil.showToast(context, "请登录");
        }
    }


}
