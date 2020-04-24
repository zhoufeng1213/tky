package com.xxxx.cc.global;

import android.content.Context;
import android.content.Intent;

import com.kty.mars.baselibrary.http.LoggerInterceptor;
import com.sdk.keepbackground.work.DaemonEnv;
import com.xxxx.cc.callback.CallPhoneBack;
import com.xxxx.cc.model.CommunicationRecordResponseBean;
import com.xxxx.cc.model.UserBean;
import com.xxxx.cc.service.LinphoneService;
import com.xxxx.cc.ui.CallActivity;
import com.xxxx.cc.ui.HistoryActivity;
import com.xxxx.cc.util.LinServiceManager;
import com.xxxx.cc.util.LogUtils;
import com.xxxx.cc.util.NetUtil;
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
    private Context mContext;
    private String phoneNum;
    private String userName;
    private String headUrl;
    private String customUserId;
    UserBean cacheUserBean;

    public static CallPhoneBack callPhoneBack;

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
     *
     * @param context
     */
    public void initKtyCcSdk(Context context) {
        initNetConfig();
        DbUtil.init(context);
//        startLinPhoneService(context);
    }

    private void initNetConfig() {
        CookieJarImpl cookieJar = new CookieJarImpl(new MemoryCookieStore());
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggerInterceptor("zwmn", true))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
//                .cookieJar(cookieJar)
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
        SharedPreferencesUtil.save(context, Constans.KTY_CUSTOM_BEGIN, "");
        //删除数据库数据
        DbUtil.clearDb();
    }

    public void clearPhone(Context context){
        Object objectBean = SharedPreferencesUtil.getObjectBean(context, USERBEAN_SAVE_TAG, UserBean.class);
        if (objectBean != null) {
            cacheUserBean = (UserBean) objectBean;
            LinServiceManager.unRegisterOnlineLinPhone(cacheUserBean, false);
        }
    }

    /**
     * 拨打电话
     */
    public void callPhone(Context mContext, String phoneNum, String userName, String headUrl,
                          String customUserId,CallPhoneBack callBack) {
        this.mContext = mContext;
        this.phoneNum = phoneNum;
        this.userName = userName;
        this.headUrl = headUrl;
        this.customUserId = customUserId;
        callPhoneBack = callBack;
        //先判断用户是否保存在了本地
        LogUtils.e("callPhone");

        if (!NetUtil.isNetworkConnected(mContext)) {
            LogUtils.e("没有网络");
            ToastUtil.showToast(mContext, "当前没有网络，请检查网络连接");
        } else {
            try {
                LinServiceManager.hookCall();
                Object objectBean = SharedPreferencesUtil.getObjectBean(mContext, USERBEAN_SAVE_TAG, UserBean.class);
                if (objectBean != null) {
                    cacheUserBean = (UserBean) objectBean;
                    //判断service是否已经起来了
                    if (!LinphoneService.isReady()) {
                        startLinPhoneService(mContext);
                        LogUtils.e("重新启动service");
                    }
                    linphoneServiceCall();
                } else {
                    LogUtils.e("objectBean == null");
                    ToastUtil.showToast(mContext, "objectBean == null");
                }
            } catch (Exception e) {
                LogUtils.e("Exception：" + e.getMessage());
                ToastUtil.showToast(mContext, e.getMessage());
            }
        }
    }

    private void linphoneServiceCall() {
        if (LinphoneService.getCore() != null) {
            if (LinphoneService.isRegister()) {
                LogUtils.e("已经注册 LinphoneService");
                goToCallActivity(mContext, phoneNum, userName, headUrl,customUserId);
            } else {
                LogUtils.e("还没注册 LinphoneService，现在开始注册");
                LinphoneService.getCore().addListener(new CoreListenerStub() {
                    @Override
                    public void onRegistrationStateChanged(Core core, ProxyConfig cfg, RegistrationState
                            state, String message) {
                        LogUtils.e("linphoneServiceCall linphone_registration state:" + state.name() + ", message:" + message);
                        if (state == RegistrationState.Ok) {
                            LinServiceManager.removeListener(this);
                            LinphoneService.setRegister(true);
                            goToCallActivity(mContext, phoneNum, userName, headUrl,customUserId);
                        } else if (state == RegistrationState.None || state == RegistrationState.Failed) {
                            LogUtils.e("注册失败了-----》" + state.name());
                            ToastUtil.showToast(mContext, "注册服务失败");
                        }
                    }
                });
                LinServiceManager.setLinPhoneConfig(cacheUserBean);
            }
        } else {
            LogUtils.e("LinphoneService .getCore() == nul");
            ToastUtil.showToast(mContext, "LinphoneService .getCore() == nul");
        }
    }

    public interface CallPhoneInterface {
        void goToCall(CommunicationRecordResponseBean mCommunicationRecordResponseBean);
    }

    public CallPhoneInterface mDemoInterface;

    public void setmCallPhoneInterface(CallPhoneInterface callPhoneInterface) {
        mDemoInterface = callPhoneInterface;
    }

    private void goToCallActivity(Context mContext, String phoneNum, String userName,
                                  String headUrl,String customUserId) {
        //先挂断之前可能因为网络原因没有把之前的电话挂断的电话
        LinServiceManager.hookCall();
        LogUtils.e("goToCallActivity");
        //开始拨打电话
        Intent intent = new Intent(mContext, CallActivity.class);
        intent.putExtra("phoneNum", phoneNum);
        intent.putExtra("name", userName);
        intent.putExtra("headUrl", headUrl);
        intent.putExtra("customUserId", customUserId);
        intent.putExtra("linPhoneRegistStatus", true);
        mContext.startActivity(intent);
    }


    public static void goToHistoryActivity(Context context) {
        //先判断用户是否保存在了本地
        try {
            Object objectBean = SharedPreferencesUtil.getObjectBean(context, USERBEAN_SAVE_TAG, UserBean.class);
            if (objectBean != null) {
//                context.startActivity(new Intent(context, HistoryActivity.class));
                ToastUtil.showToast(context, "go historyActivity");
            } else {
                ToastUtil.showToast(context, "请登录");
            }
        } catch (Exception e) {
            ToastUtil.showToast(context, "请登录");
        }
    }


    public static void startLinPhoneService(Context context) {
        if (!LinphoneService.isReady()) {
            DaemonEnv.startServiceSafely(context, LinphoneService.class);
        }
//        Intent intent = new Intent(context, LinphoneService.class);
//        context.startService(intent);
    }
}
