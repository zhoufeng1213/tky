package com.xxxx.cc.global;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.kty.mars.baselibrary.http.LoggerInterceptor;
import com.sdk.keepbackground.work.DaemonEnv;
import com.xxxx.cc.R;
import com.xxxx.cc.base.presenter.MyStringCallback;
import com.xxxx.cc.callback.CallPhoneBack;
import com.xxxx.cc.model.BaseBean;
import com.xxxx.cc.model.CommunicationRecordResponseBean;
import com.xxxx.cc.model.MakecallBean;
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
import com.zhy.http.okhttp.builder.PostStringBuilder;
import com.zhy.http.okhttp.cookie.CookieJarImpl;
import com.zhy.http.okhttp.cookie.store.MemoryCookieStore;
import com.zhy.http.okhttp.https.HttpsUtils;

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.linphone.core.Core;
import org.linphone.core.CoreListenerStub;
import org.linphone.core.ProxyConfig;
import org.linphone.core.RegistrationState;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;

import static com.xxxx.cc.global.Constans.KTY_CC_BASE_URL;
import static com.xxxx.cc.global.Constans.KTY_CC_BEGIN;
import static com.xxxx.cc.global.Constans.USERBEAN_SAVE_TAG;
import static com.xxxx.cc.global.Constans.VOICE_RECORD_PREFIX;
import static com.xxxx.cc.global.HttpRequest.makecallInternal;
import static java.lang.String.valueOf;

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
                .hostnameVerifier(new AllowAllHostnameVerifier())
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

    public void clearPhone(Context context) {
        Object objectBean = SharedPreferencesUtil.getObjectBean(context, USERBEAN_SAVE_TAG, UserBean.class);
        if (objectBean != null) {
            cacheUserBean = (UserBean) objectBean;
            LinServiceManager.unRegisterOnlineLinPhone(cacheUserBean, false);
        }
    }
    private String[] needPermissions = {
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CALL_PHONE};
    private List<String> permissionList = new ArrayList<>();

    /**
     * 拨打电话
     */
    public void callPhone(@NonNull Context mContext, @NonNull String phoneNum, @NonNull String userName, @NonNull String headUrl,
                          @NonNull String customUserId, @NonNull CallPhoneBack callBack,String extendedData) {
        this.mContext = mContext;
        this.phoneNum = phoneNum;
        this.userName = userName;
        this.headUrl = headUrl;
        this.customUserId = customUserId;
        callPhoneBack = callBack;
        //先判断用户是否保存在了本地
        LogUtils.i("callPhone");
        for (String needPermission : needPermissions) {
            if (ContextCompat.checkSelfPermission(mContext, needPermission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(needPermission);
            }
        }
        if (permissionList.size() > 0) {
           callBack.onFailed("请先申请权限");
           return;
        }

        if (!NetUtil.isNetworkConnected(mContext)) {
            LogUtils.e("没有网络");
            callBack.onFailed("没有网络");
        } else {
            try {

                LinServiceManager.hookCall();
                LinServiceManager.addListener(mCoreListener);
                Object objectBean = SharedPreferencesUtil.getObjectBean(mContext, USERBEAN_SAVE_TAG, UserBean.class);
                if (objectBean != null) {
                    cacheUserBean = (UserBean) objectBean;
                    //判断service是否已经起来了
                    if (!LinphoneService.isReady()) {
                        startLinPhoneService(mContext);
                        LogUtils.e("重新启动service");
                    }
                    linphoneServiceCall(callBack,extendedData);
                } else {
                    LogUtils.e("objectBean == null");
                    callBack.onFailed("没有登录");
//                    ToastUtil.showToast(mContext, "objectBean == null");
                }
            } catch (Exception e) {
                LogUtils.e("Exception：" + e.getMessage());
                callBack.onFailed("Exception：" + e.getMessage());
            }
        }
    }

    private CoreListenerStub mCoreListener = new CoreListenerStub() {
        @Override
        public void onCallStateChanged(Core core, org.linphone.core.Call call, org.linphone.core.Call.State state, String message) {
            if (state == org.linphone.core.Call.State.End) {
                if(callPhoneBack != null){
                   callPhoneBack.watchPhoneStatus(2);
                }
            }
        }
    };

    private void linphoneServiceCall(@NonNull CallPhoneBack callBack,String extendedData) {
        if (LinphoneService.getCore() != null) {
            if (LinphoneService.isRegister()) {
                LogUtils.e("已经注册 LinphoneService");
                doPostByHeaders(mContext, makecallInternal,callBack,extendedData,
                        "token", cacheUserBean.getToken(),
                        "Content-Type", "application/json");
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
                            doPostByHeaders(mContext, makecallInternal,callBack,extendedData,
                                    "token", cacheUserBean.getToken(),
                                    "Content-Type", "application/json");
                        } else if (state == RegistrationState.None || state == RegistrationState.Failed) {
                            LogUtils.e("注册失败了-----》" + state.name());
                            callBack.onFailed("注册服务失败");
                        }
                    }
                });
                LinServiceManager.setLinPhoneConfig(cacheUserBean);
            }
        } else {
            LogUtils.e("LinphoneService .getCore() == nul");
            callBack.onFailed("Service 还没有启动");
        }
    }

    public void hookCall() {
        LinServiceManager.hookCall();
    }

    public void switchAudio(Context context,boolean isHandsfree) {
        LinServiceManager.switchAudio(context, isHandsfree);
    }

    public interface CallPhoneInterface {
        void goToCall(CommunicationRecordResponseBean mCommunicationRecordResponseBean);
    }

    public CallPhoneInterface mDemoInterface;

    public void setmCallPhoneInterface(CallPhoneInterface callPhoneInterface) {
        mDemoInterface = callPhoneInterface;
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
        if (!LinphoneService.isReady() && context != null) {
            DaemonEnv.startServiceSafely(context, LinphoneService.class);
        }
    }

    private void doPostByHeaders(Context context, final String moduleName, @NonNull CallPhoneBack callBack,String extendedData, String... headers) {
        //先挂断之前可能因为网络原因没有把之前的电话挂断的电话
        LinServiceManager.hookCall();

        //判断网络是否可用
        if (!NetUtil.isNetworkConnected(context)) {
            callBack.onFailed("网络连接失败，请检查网络");
            return;
        }
        PostStringBuilder okHttpUtils = OkHttpUtils.postString();
        okHttpUtils.url(Constans.BASE_URL + moduleName);
        //添加header
        if (headers != null) {
            if (headers.length > 1 && headers.length % 2 == 0) {
                for (int i = 0; i < headers.length; i += 2) {
                    okHttpUtils.addHeader(valueOf(headers[i]), valueOf(headers[i + 1]));
                }
            }
        }
        JSONObject jsonObject = new JSONObject();
        MakecallBean makecallBean = new MakecallBean();
        makecallBean.setCaller(cacheUserBean.getCcUserInfo().getExtensionNo());
        makecallBean.setCallee(phoneNum);
        makecallBean.setName(userName);
        makecallBean.setAppname("android");
        makecallBean.setExtendedData(extendedData);
        jsonObject = JSONObject.parseObject(new Gson().toJson(makecallBean));

        LogUtils.e("url:" + Constans.BASE_URL + moduleName + "，Params:" + jsonObject.toString());
        okHttpUtils
                .content(jsonObject.toString())
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new MyStringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e("e.getMessage():" + e.getMessage());
                        if (e != null) {
                            try {
                                BaseBean baseBean = JSON.parseObject(e.getMessage(), BaseBean.class);
                                if(baseBean!=null){
                                    if (baseBean.getCode() == 45009) {
                                        callBackFailed("您的登录身份已过期，请退出重新登录");
                                    } else {
                                        callBackFailed(baseBean.getCode()+baseBean.getMessage());
                                    }
                                }else {
                                    callBackFailed("onError");
                                }

                            } catch (JSONException ex) {
                                callBackFailed(e.getMessage());
                            }
                        }else {
                            callBackFailed("服务器出错4");
                        }
                    }


                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.e("tag", "onResponse:" + response);
                        try {
                            if (!TextUtils.isEmpty(response)) {
                                BaseBean baseBean = JSON.parseObject(response, BaseBean.class);
                                if(baseBean!=null){
                                    if (baseBean.isOk()) {
                                        JSONObject json = (JSONObject) baseBean.getData();
                                        if(json!=null){
                                            callBack.onSuccess(json.getString("uuid"));
                                        }else {
                                            callBack.onFailed("uuid is null");
                                        }

                                    } else {
                                        callBack.onFailed(baseBean.getMessage());
                                    }
                                }

                            } else {
                                callBackFailed("服务器出错5");
                            }
                        } catch (Exception e) {
                            callBackFailed(e.getMessage());
                        }
                    }
                });
    }
    private void callBackSuccess(String msg){
        if(callPhoneBack!=null){
            callPhoneBack.onSuccess(msg);
        }
    }
    private void callBackFailed(String msg){
        if(callPhoneBack!=null){
            callPhoneBack.onFailed(msg);
        }
    }
}
