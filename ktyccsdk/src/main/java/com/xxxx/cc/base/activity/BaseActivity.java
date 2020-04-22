package com.xxxx.cc.base.activity;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.telephony.SignalStrength;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.fly.sweet.dialog.SweetAlertDialog;
import com.flyco.roundview.RoundTextView;
import com.gyf.barlibrary.ImmersionBar;
import com.kty.mars.baselibrary.util.StatusBarUtil;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.xxxx.cc.R;
import com.xxxx.cc.base.widget.LoadingDialog;
import com.xxxx.cc.service.LinphoneService;
import com.xxxx.cc.util.ActivityUtil;
import com.xxxx.cc.util.CallUtil;
import com.xxxx.cc.util.LogUtils;
import com.xxxx.cc.util.ToastUtil;
import com.xxxx.cc.util.net.ConnectivityStatus;
import com.xxxx.cc.util.net.ReactiveNetwork;
import com.zhanshow.mylibrary.network.NetWorkUtils;
import com.zhanshow.mylibrary.phonestate.MyPhoneStateListener;
import com.zhanshow.mylibrary.phonestate.PhoneStateUtils;

import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by zhoufeng on 15/4/2.
 */
public abstract class BaseActivity extends RxAppCompatActivity {

    public Context mContext;
    public boolean network = true;


    private ReactiveNetwork reactiveNetwork;
    private CompositeDisposable disposables;

    private boolean isNetSignPoor;

    private ConnectivityStatus currentConnectivityStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        initViewBeforeSetContentView();
        setContentView(getLayoutViewId());
        ActivityUtil.addActivity(this.getPackageName() + "." + this.getLocalClassName());
        ButterKnife.bind(this);
        mContext = this;

        reactiveNetwork = new ReactiveNetwork();
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
                        currentConnectivityStatus = status;
                        //判断当前网络要是mobile的，那么就监听信号
                        if (status == ConnectivityStatus.MOBILE_CONNECTED) {
                            if (!network) {
                                LogUtils.e(ConnectivityStatus.MOBILE_CONNECTED.description + "连接上了");
                                onNetworkConnected();
                                network = true;
                            }
                        } else if (status == ConnectivityStatus.WIFI_CONNECTED) {
                            if (!network) {
                                LogUtils.e(ConnectivityStatus.WIFI_CONNECTED.description + "连接上了");
                                onNetworkConnected();
                                network = true;
                            }
                        } else {
                            network = false;
                            onNetworkDisConnected(status);
                        }
                    }
                });
        disposables.add(nc);

        //监听wifi强度
        Disposable wi = reactiveNetwork.observeWifiInfo(getApplicationContext())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WifiInfo>() {
                    @Override
                    public void accept(WifiInfo info) {
                        if (network && currentConnectivityStatus != null && currentConnectivityStatus.description.equals(
                                ConnectivityStatus.WIFI_CONNECTED
                        )) {
                            int strength = 100 + info.getRssi();
                            //一般低于30都是网络比较差的
                            LogUtils.e("wifi信号：" + strength);
                            if (strength <= 30) {
                                onNetworkConnectedPoor(ConnectivityStatus.WIFI_CONNECTED);
                            }
                        }
                    }
                });
        disposables.add(wi);

        listenMobileSign();

        initView(savedInstanceState);
    }


    private void listenMobileSign() {
        PhoneStateUtils.registerPhoneStateListener(this, new MyPhoneStateListener.MyPhoneStateListenerListener() {
            @Override
            public void onSignalStrengthsChanged(SignalStrength signalStrength) {
                if (network) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        //判断当前网络是mobile还是wifi
                        String networkTypeName = NetWorkUtils.getNetworkTypeName(mContext);
                        if (!NetWorkUtils.NETWORK_TYPE_WIFI.equals(networkTypeName) &&
                                !NetWorkUtils.NETWORK_TYPE_UNKNOWN.equals(networkTypeName) &&
                                !NetWorkUtils.NETWORK_TYPE_DISCONNECT.equals(networkTypeName)
                        ) {
                            if (currentConnectivityStatus != null && currentConnectivityStatus.description.equals(
                                    ConnectivityStatus.MOBILE_CONNECTED)) {
                                LogUtils.e("2g,3g,4g的信号强度：" + signalStrength.getLevel());
                                if (signalStrength.getLevel() == 0 || signalStrength.getLevel() == 1) {
                                    onNetworkConnectedPoor(ConnectivityStatus.MOBILE_CONNECTED);
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    private int showNetPoorToastLength = 0;

    /**
     * 网络连接状态
     */
    protected void onNetworkConnectedPoor(ConnectivityStatus status) {
        LogUtils.e("当前：" + status.description + "，网络不佳");
//        if(isShowNetErrorDialog()){
//            if(netErrorSweetAlertDialog == null){
//                netErrorDialog();
//            }
//        }
        showNetPoorToastLength++;
        if (isNeedShowNetPoorManyTimes()) {
            if (showNetPoorToastLength < 151 && showNetPoorToastLength % 15 == 0 && mContext != null) {
                ToastUtil.showToastShort(mContext, "当前网络信号较差，请更换网络或者到网络好的地方");
            }
        } else {
            if (!isNetSignPoor) {
                isNetSignPoor = true;
                showToast("当前网络信号较差，请更换网络或者到网络好的地方");
            }
        }
    }

    /**
     * 网络连接状态
     */
    protected void onNetworkConnected() {
        LogUtils.e("网络连接了");
    }

    /**
     * 网络断开的时候调用
     */
    protected void onNetworkDisConnected(ConnectivityStatus status) {
        LogUtils.e("网络断了:" + status.description);
        showToast("网络断了，请检查网络连接");
    }


    @Override
    protected void onResume() {
        super.onResume();
//        if (!LinphoneService.isReady()) {
//            Intent intent = new Intent(mContext, LinphoneService.class);
//            mContext.startService(intent);
//        }
    }

    protected void setStatusBar() {

        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            StatusBarUtil.setStatusBarColor(this, 1426063360);
        }


    }


    public boolean isAddImmersionBar() {
        return true;
    }


    public void initViewBeforeSetContentView() {

    }

    public abstract int getLayoutViewId();

    public Toolbar getToolbar() {
        return null;
    }

    public String getToolBarTitle() {
        return "";
    }

    /**
     * 初始化Toolbar
     */
    public void initToolbar() {
        Toolbar mToolbar = getToolbar();
        if (mToolbar != null) {
            mToolbar.setTitle(getToolBarTitle());
            mToolbar.setTitleTextColor(getResources().getColor(R.color.black));
            setSupportActionBar(mToolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setHomeButtonEnabled(true);
                actionBar.setDisplayHomeAsUpEnabled(true);
//                actionBar.setHomeAsUpIndicator(R.drawable.pub_back_hei);
//                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }

    }


    public void initView(Bundle savedInstanceState) {

    }


    /**
     * 选项菜单
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                colseActivity();
                return true;
            default:
                break;
        }
        return false;
    }


    public void colseActivity() {
        finish();
    }


    private LoadingDialog loadingDialog;

    public void showDialog() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
        }
        loadingDialog.setMessage(true);
        loadingDialog.show();
    }

    public void showDialog(String title) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
        }
        loadingDialog.setMessage(title);
        loadingDialog.show();
    }

    public void showDialog(String title, boolean isCancel) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this, isCancel);
        }
        loadingDialog.setMessage(title);
        loadingDialog.show();
    }

    public void dismissDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    public void showToast(String msg) {
        ToastUtil.showToast(getApplicationContext(), msg);
    }

    public void startActivity(Class className, Object... params) {
        Intent intent = new Intent(mContext, className);
        if (params != null) {
            if (params.length > 1 && params.length % 2 == 0) {
                for (int i = 0; i < params.length; i += 2) {
                    if (params[i + 1] instanceof Integer) {
                        intent.putExtra(String.valueOf(params[i]), Integer.valueOf(String.valueOf(params[i + 1])));
                    } else {
                        intent.putExtra(String.valueOf(params[i]), String.valueOf(params[i + 1]));
                    }
                }
            }
        }
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        CallUtil.restartConferenceActivityToOtherActivity(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        NetWorkUtils.unRegisterNetWork(this);
        PhoneStateUtils.unRegisterPhoneStateListener(this);
    }

    @Override
    protected void onDestroy() {
        ActivityUtil.removeActivity(this.getPackageName() + "." + this.getLocalClassName());
        dismissDialog();
        if (isAddImmersionBar()) {
            ImmersionBar.with(this).destroy();
        }
        if (null != disposables) {
            disposables.dispose();
        }
        if (netErrorSweetAlertDialog != null) {
            netErrorSweetAlertDialog.dismiss();
        }
        super.onDestroy();
    }

    private SweetAlertDialog netErrorSweetAlertDialog;

    public void netErrorDialog() {
        netErrorSweetAlertDialog = new SweetAlertDialog(mContext);
        View dialogView = LayoutInflater.from(mContext)
                .inflate(R.layout.dialog_net_error, null, false);
        netErrorSweetAlertDialog.setCustomView(dialogView);
        netErrorSweetAlertDialog.setCanceledOnTouchOutside(false);
        netErrorSweetAlertDialog.setCancelable(false);

        RoundTextView dialogConfirm = dialogView.findViewById(R.id.dialog_confirm);
        dialogConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dealNetErrorConfirm();
            }
        });

        netErrorSweetAlertDialog.show();
    }


    public void dealNetErrorConfirm() {
        if (netErrorSweetAlertDialog != null) {
            netErrorSweetAlertDialog.dismiss();
        }
    }

    public boolean isShowNetErrorDialog() {
        return true;
    }

    public boolean isNeedShowNetPoorManyTimes() {
        return false;
    }

}
