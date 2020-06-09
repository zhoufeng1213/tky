package com.xxxx.cc.service;

import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Messenger;
import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.sdk.keepbackground.work.AbsWorkService;
import com.sdk.keepbackground.work.DaemonEnv;
import com.xxxx.cc.R;
import com.xxxx.cc.base.activity.BaseActivity;
import com.xxxx.cc.global.KtyCcOptionsUtil;
import com.xxxx.cc.global.KtyCcSdkTool;
import com.xxxx.cc.model.SocketLoginResponseBean;
import com.xxxx.cc.model.UserBean;
import com.xxxx.cc.ui.CallActivity;
import com.xxxx.cc.util.LinServiceManager;
import com.xxxx.cc.util.LogUtils;
import com.xxxx.cc.util.SharedPreferencesUtil;
import com.xxxx.cc.util.ToastUtil;
import com.xxxx.cc.util.net.ConnectivityStatus;
import com.xxxx.cc.util.net.ReactiveNetwork;

import org.json.JSONException;
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
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static com.xxxx.cc.global.Constans.USERBEAN_SAVE_TAG;

public class LinphoneService extends AbsWorkService {

    private static LinphoneService sInstance;

    private Handler mHandler;
    private Timer mTimer;

    private Core mCore;
    private CoreListenerStub mCoreListener;

    private boolean isRegister;
    private CompositeDisposable disposables;

    public static boolean isReady() {
        return sInstance != null;
    }
    private Socket mSocket;

    private  Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            if(CallActivity.callActivityIsShowing){
                CallActivity.needUnRegisterLinphone=true;//正在打电话的时候延时注销
            }else {
                unRegisterLinPhone();
            }

        }
    };
    private void unRegisterLinPhone(){
        Object objectBean = SharedPreferencesUtil.getObjectBean(LinphoneService.this, USERBEAN_SAVE_TAG, UserBean.class);
        if (objectBean != null) {
            UserBean   cacheUserBean = (UserBean) objectBean;
            LinServiceManager.unRegisterOnlineLinPhone(cacheUserBean, false);
            LogUtils.e("socket msg unRegisterOnlineLinPhone");
            Intent broadcast = new Intent(BaseActivity.SOCKET_CLICK_OUT);
            LinphoneService.this.sendBroadcast(broadcast);
        }
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
        registScoket();


        ReactiveNetwork reactiveNetwork = new ReactiveNetwork();
        if (null == disposables) {
            disposables = new CompositeDisposable();
        }

        //监听网络连接类型的 （数据流量 、wifi 、断线）
        Disposable nc = reactiveNetwork.observeNetworkConnectivity(getApplicationContext())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ConnectivityStatus>() {
                    @Override
                    public void accept(final ConnectivityStatus status) throws Exception {
//                        判断当前网络要是mobile的，那么就监听信号
                        if (status == ConnectivityStatus.MOBILE_CONNECTED||status == ConnectivityStatus.WIFI_CONNECTED) {
                        }else {
                            if(mSocket!=null){
                                mSocket.close();
                                mSocket.disconnect();
                            }
                            unRegisterLinPhone();
                        }
                    }
                });
        disposables.add(nc);

    }

    public void socketEmit() {
        if (mSocket == null) {
            mSocket = KtyCcOptionsUtil.getmSocket();
        }
        if (!mSocket.connected()) {
            mSocket.connect();
        }
        Object objectBean = SharedPreferencesUtil.getObjectBean(this, USERBEAN_SAVE_TAG, UserBean.class);
        if (objectBean != null) {
            UserBean cacheUserBean = (UserBean) objectBean;
            org.json.JSONObject obj = new org.json.JSONObject();
            try {
                obj.put("username", "");
                obj.put("password", "");
                obj.put("token", cacheUserBean.getToken());
                obj.put("agentNo", cacheUserBean.getCcUserInfo().getExtensionNo());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mSocket.emit("LOGIN", obj);

        }


    }
    private void registScoket() {
        if(mSocket==null){
            mSocket = KtyCcOptionsUtil.getmSocket();
        }
        if(mSocket!=null){
            mSocket.off("CB_KICKOUT");
            mSocket.on("CB_KICKOUT", onNewMessage);
            mSocket.on("CB_LOGIN", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
//                try{
//                    if(args.length>0){
//                        SocketLoginResponseBean bean = JSON.parseObject(args[0].toString(), SocketLoginResponseBean.class);
//                        if(bean!=null){
//                            if(bean.getCode()==200){
//                                callPhone(phoneNum);
//                            }else {
//                                ToastUtil.showToast(CallActivity.this,"socket连接出错"+bean.getMessage());
//                                finish();
//                            }
//
//                        }else {
//                            ToastUtil.showToast(CallActivity.this,"socket 连接出错");
//                            finish();
//                        }
//                    }
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//

                }
            });
            if(!mSocket.connected()){
                mSocket.connect();
            }
        }


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
        // A stopped Core can be started again
        // To ensure resources are freed, we must ensure it will be garbage collected
        mCore = null;
        // Don't forget to free the singleton as well
        sInstance = null;
        if (mSocket != null) {
            mSocket.off("CB_KICKOUT");
            mSocket.disconnect();
            mSocket.close();
        }
        if (null != disposables) {
            disposables.dispose();
        }
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

}
