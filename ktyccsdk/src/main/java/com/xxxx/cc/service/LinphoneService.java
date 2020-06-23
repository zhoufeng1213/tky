package com.xxxx.cc.service;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Handler;
import android.os.IBinder;
import android.os.Messenger;
import android.provider.CallLog;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.sdk.keepbackground.work.AbsWorkService;
import com.xxxx.cc.R;
import com.xxxx.cc.base.presenter.MyStringCallback;
import com.xxxx.cc.global.Constans;
import com.xxxx.cc.global.HttpRequest;
import com.xxxx.cc.global.KtyCcSdkTool;
import com.xxxx.cc.model.CallHistoryBean;
import com.xxxx.cc.model.UserBean;
import com.xxxx.cc.util.LogUtils;
import com.xxxx.cc.util.SharedPreferencesUtil;
import com.xxxx.cc.util.SystemUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostStringBuilder;

import org.linphone.core.Call;
import org.linphone.core.CallParams;
import org.linphone.core.Core;
import org.linphone.core.CoreListenerStub;
import org.linphone.core.Factory;
import org.linphone.core.ProxyConfig;
import org.linphone.core.RegistrationState;
import org.linphone.core.tools.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import okhttp3.MediaType;

import static com.xxxx.cc.global.Constans.USERBEAN_SAVE_TAG;

public class LinphoneService extends AbsWorkService {

    private static LinphoneService sInstance;
    private Handler mHandler;
    private Timer mTimer;

    private Core mCore;
    private CoreListenerStub mCoreListener;

    private boolean isRegister;
    private TelephonyManager telephonyManager;

    public static boolean isReady() {
        return sInstance != null;
    }

    public static boolean isRegister() {
        if (sInstance == null) {
            return false;
        }
        return sInstance.isRegister;
    }

    public static void setRegister(boolean register) {
        if (sInstance != null) {
            sInstance.isRegister = register;
        }
    }

    public static LinphoneService getInstance() {
        return sInstance;
    }

    public static Core getCore() {
        if (sInstance == null) {
            return null;
        }
        return sInstance.mCore;
    }

    private MyPhoneListener myPhoneListener;
    public static boolean startTelFromCall;//SIM卡打电话
    public static String telUserName;//从CustomPesonDetailActivit启动打电话，如果用sim卡拨号，上传通话记录用此名字
    public static String telNum;//sim卡拨打的电话，用来对比第一条通话记录
    public static String callBySystemUUID;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // The first call to liblinphone SDK MUST BE to a Factory method
        // So let's enable the library debug logs & log collection
        String basePath = getFilesDir().getAbsolutePath();
//        Factory.instance().setLogCollectionPath(basePath);
//        Factory.instance().enableLogCollection(LogCollectionState.Enabled);
//        Factory.instance().setDebugMode(true, getString(R.string.app_name));


        mHandler = new Handler();
        // This will be our main Core listener, it will change activities depending on events
        mCoreListener = new CoreListenerStub() {
            @Override
            public void onCallStateChanged(Core core, Call call, Call.State state, String message) {
                LogUtils.e("LinphoneService CallState:" + state.name() + ",message:" + message);
                if (state == Call.State.IncomingReceived) {
                    CallParams params = getCore().createCallParams(call);
                    params.enableVideo(false);
                    call.acceptWithParams(params);

                    if (KtyCcSdkTool.callPhoneBack != null) {
                        KtyCcSdkTool.callPhoneBack.watchPhoneStatus(1);
                    }
                } else if (state == Call.State.Connected) {

                }
            }

            @Override
            public void onRegistrationStateChanged(Core lc, ProxyConfig cfg, RegistrationState cstate, String message) {
                LogUtils.e("LinphoneService onRegistrationStateChanged:" + cstate.name() + ",message:" + message);
                if (cstate == RegistrationState.Ok) {
                    LogUtils.e("LinphoneService onRegistrationStateChanged ok");
                    LinphoneService.setRegister(true);
                } else if (cstate == RegistrationState.None || cstate == RegistrationState.Cleared || cstate == RegistrationState.Failed) {
                    LinphoneService.setRegister(false);
                }
            }
        };

        try {
            copyIfNotExist(R.raw.linphonerc_default, basePath + "/.linphonerc");
            copyFromPackage(R.raw.linphonerc_factory, "linphonerc");
        } catch (IOException ioe) {
            Log.e(ioe);
        }

        // Create the Core and add our listener
        mCore = Factory.instance()
                .createCore(basePath + "/.linphonerc", basePath + "/linphonerc", this);
        mCore.addListener(mCoreListener);
        // Core is ready to be configured
        configureCore();
        //监听电话
        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        myPhoneListener = new MyPhoneListener();
        if (telephonyManager != null) {
            LogUtils.e("ccservice telephony listen");
            telephonyManager.listen(myPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);
        }
//        CallstatusReciver callstatusReciver=new CallstatusReciver();
//        registerReceiver(callstatusReciver)
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        LogUtils.e("service onStartCommand");
        if (sInstance != null) {
            return START_STICKY;
        }

        sInstance = this;
        mCore.start();

        TimerTask lTask =
                new TimerTask() {
                    @Override
                    public void run() {
                        mHandler.post(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        if (mCore != null) {
//                                            LogUtils.e(" Linphone scheduler   TimerTask ");
                                            mCore.iterate();
                                        }
                                    }
                                });
                    }
                };
        mTimer = new Timer("Linphone scheduler");
        mTimer.schedule(lTask, 0, 20);
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        LogUtils.e("service onDestroy");
        if (mTimer != null) {
            mTimer.cancel();
        }
        if (mCore != null) {
            mCore.removeListener(mCoreListener);
            mCore.stop();
        }
        if (telephonyManager != null && myPhoneListener != null) {
            telephonyManager.listen(myPhoneListener, PhoneStateListener.LISTEN_NONE);
        }

        // A stopped Core can be started again
        // To ensure resources are freed, we must ensure it will be garbage collected
        mCore = null;
        // Don't forget to free the singleton as well
        sInstance = null;

        super.onDestroy();
    }


    private boolean mIsRunning;

    @Override
    public Boolean needStartWorkService() {
        return true;
    }

    @Override
    public void startWork() {
        mIsRunning = true;


    }

    @Override
    public void stopWork() {
        mIsRunning = false;
    }

    @Override
    public Boolean isWorkRunning() {
        return mIsRunning;
    }

    @Override
    public IBinder onBindService(Intent intent, Void aVoid) {
        return new Messenger(new Handler()).getBinder();
    }

    @Override
    public void onServiceKilled() {
        mIsRunning = false;
//        System.exit(0);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        // For this sample we will kill the Service at the same time we kill the app
        LogUtils.e("kill the Service");
        stopSelf();

        super.onTaskRemoved(rootIntent);
    }

    private void configureCore() {
        // We will create a directory for user signed certificates if needed
        String basePath = getFilesDir().getAbsolutePath();
        String userCerts = basePath + "/user-certs";
        File f = new File(userCerts);
        if (!f.exists()) {
            if (!f.mkdir()) {
                Log.e(userCerts + " can't be created.");
            }
        }
        mCore.setUserCertificatesPath(userCerts);
    }

    private void copyIfNotExist(int ressourceId, String target) throws IOException {
        File lFileToCopy = new File(target);
        if (!lFileToCopy.exists()) {
            copyFromPackage(ressourceId, lFileToCopy.getName());
        }
    }

    private void copyFromPackage(int ressourceId, String target) throws IOException {
        FileOutputStream lOutputStream = openFileOutput(target, 0);
        InputStream lInputStream = getResources().openRawResource(ressourceId);
        int readByte;
        byte[] buff = new byte[8048];
        while ((readByte = lInputStream.read(buff)) != -1) {
            lOutputStream.write(buff, 0, readByte);
        }
        lOutputStream.flush();
        lOutputStream.close();
        lInputStream.close();
    }

    private void getContentCallLogDelay() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                getContentCallLog();
            }
        }, 1000);
    }

    private boolean hasPostDealy;

    //获取通话记录
    private void getContentCallLog() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, // 查询通话记录的URI
                new String[]{"_id", CallLog.Calls.CACHED_NAME, CallLog.Calls.NUMBER, CallLog.Calls.DATE, CallLog.Calls.DURATION, CallLog.Calls.TYPE}
                , null, null, CallLog.Calls.DEFAULT_SORT_ORDER// 按照时间逆序排列，最近打的最先显示
        );
        CallHistoryBean callHistoryBean = null;
        while (cursor.moveToNext()) {
            callHistoryBean = new CallHistoryBean();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (LinphoneService.telUserName != null) {
                callHistoryBean.setContactName(LinphoneService.telUserName);  //姓名
                LinphoneService.telUserName = null;
            } else {
                callHistoryBean.setContactName(cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)));  //姓名
            }

            callHistoryBean.setDnis(cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER)));  //号码

            int billingInSec = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.DURATION));//获取通话时长，值为多少秒
            callHistoryBean.setBillingInSec(billingInSec);
            callHistoryBean.setDuration((int) (idle-offHook) / 1000);
            callHistoryBean.setCreateTime(dateFormat.format(idle - billingInSec * 1000));
            callHistoryBean.setHangupTime(dateFormat.format(idle));
            callHistoryBean.setBridgeTime(dateFormat.format(idle - billingInSec * 1000));
//            long dateLong = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE)); //获取通话日期

            break;
        }

        if (callHistoryBean != null) {
            if (!telNum.contains(callHistoryBean.getDnis()) && !hasPostDealy) {
                getContentCallLogDelay();
                hasPostDealy = true;
                return;
            }
            if(callBySystemUUID==null){
                LinphoneService.callBySystemUUID = UUID.randomUUID().toString();
            }
            callHistoryBean.setCallId(callBySystemUUID);
            callHistoryBean.setAni(SystemUtils.getNativePhoneNumber(this));
            callHistoryBean.setArea("");

            Object objectBean = SharedPreferencesUtil.getObjectBean(this, USERBEAN_SAVE_TAG, UserBean.class);
            UserBean cacheUserBean = null;
            if (objectBean != null) {
                cacheUserBean = (UserBean) objectBean;
            }
            if (cacheUserBean == null) {
                return;
            }
            callHistoryBean.setUserId(cacheUserBean.getUserId());
            callHistoryBean.setAgentUserName(cacheUserBean.getUname());
            callHistoryBean.setOrgId(cacheUserBean.getOrgId());


            requestPost(callHistoryBean, cacheUserBean.getToken());
        }
    }


    private void requestPost(CallHistoryBean callHistoryBean, String token) {
        PostStringBuilder okHttpUtils = OkHttpUtils.postString();
        okHttpUtils.url(Constans.BASE_URL + HttpRequest.CallHistory.pushCallHistory);
        //添加header
        okHttpUtils.addHeader("token", token);
        okHttpUtils.addHeader("Content-Type", "application/json");

        okHttpUtils
                .content(JSONObject.parseObject(new Gson().toJson(callHistoryBean)).toJSONString())
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new MyStringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        if(upLoadContentCall!=null){
                            upLoadContentCall.onError();
                        }

                        LogUtils.e("加载失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if(upLoadContentCall!=null){
                            upLoadContentCall.onResponse();
                        }
                        LogUtils.e("上传通话记录成功");
                    }
                });
    }

    private UpLoadContentCall upLoadContentCall;


    public void setUpLoadContentCall(UpLoadContentCall upLoadContentCall) {
        this.upLoadContentCall = upLoadContentCall;
    }

    public interface UpLoadContentCall {
        void onResponse();

        void onError();
    }

    private long offHook;
    private long idle;

    class MyPhoneListener extends PhoneStateListener {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                //空闲
                case TelephonyManager.CALL_STATE_IDLE:
                    if (startTelFromCall) {
                        startTelFromCall = false;
                        idle = System.currentTimeMillis();
                        hasPostDealy = false;
                        getContentCallLogDelay();

                    }
                    LogUtils.e("ccservice", "CALL_STATE_IDLE");
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    LogUtils.e("ccservice", "CALL_STATE_RINGING");
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if (startTelFromCall) {
                        offHook = System.currentTimeMillis();
                    }
                    LogUtils.e("ccservice", "CALL_STATE_OFFHOOK");
                    break;
                default:
                    break;
            }
        }
    }

}
