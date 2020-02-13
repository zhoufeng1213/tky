package com.xxxx.cc.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gyf.barlibrary.ImmersionBar;
import com.xxxx.cc.R;
import com.xxxx.cc.base.activity.BaseActivity;
import com.xxxx.cc.base.presenter.MyStringCallback;
import com.xxxx.cc.model.UserBean;
import com.xxxx.cc.service.FloatingImageDisplayService;
import com.xxxx.cc.util.LinServiceManager;
import com.xxxx.cc.util.LogUtils;
import com.xxxx.cc.util.NetUtil;
import com.xxxx.cc.util.SharedPreferencesUtil;
import com.xxxx.cc.util.TimeUtils;
import com.xxxx.cc.util.rom.FloatWindowManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;

import org.greenrobot.greendao.annotation.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.linphone.core.Call;
import org.linphone.core.Core;
import org.linphone.core.CoreListenerStub;

import java.util.ArrayList;
import java.util.List;

import static com.xxxx.cc.global.Constans.USERBEAN_SAVE_TAG;

public class CallActivity extends BaseActivity {


    ImageView shrink;
    ImageView headImg;
    TextView nameTextView;
    TextView phoneNumTextView;
    TextView phoneStatus;
    TextView communicationTime;
    LinearLayout llSilience;
    LinearLayout llKeepCommunication;
    LinearLayout llMianti;
    LinearLayout llContainer;
    LinearLayout onHook;
    TextView communicationStatus;
    LinearLayout layoutPhoneAddress;
    TextView tvPhoneAddress;

    private boolean hook;
    private String phoneNum;
    private boolean isNeedRequestPermission;
    private String userContactName;
    static final String check_phone_url = "http://mobsec-dianhua.baidu.com/dianhua_api/open/location";
    private static final String TAG = "CallActivity";

    @Override
    public int getLayoutViewId() {
        return R.layout.activity_communication;
    }

    @Override
    public Toolbar getToolbar() {
        return null;
    }

    @Override
    public String getToolBarTitle() {
        return null;
    }

    @Override
    public boolean isAddImmersionBar() {
        return false;
    }

    private void findView() {
        shrink = this.findViewById(R.id.shrink);
        headImg = this.findViewById(R.id.head_img);
        nameTextView = this.findViewById(R.id.name);
        phoneNumTextView = this.findViewById(R.id.phone_num);
        phoneStatus = this.findViewById(R.id.phone_status);
        communicationTime = this.findViewById(R.id.communication_time);
        llSilience = this.findViewById(R.id.ll_silience);
        llKeepCommunication = this.findViewById(R.id.ll_keep_communication);
        llMianti = this.findViewById(R.id.ll_mianti);
        llContainer = this.findViewById(R.id.ll_container);
        onHook = this.findViewById(R.id.on_hook);
        communicationStatus = this.findViewById(R.id.communication_status);
        layoutPhoneAddress = this.findViewById(R.id.layout_phone_address);
        tvPhoneAddress = findViewById(R.id.phone_address);
    }

    private void initListener() {
        onHook.setOnClickListener(onClickListener);
        shrink.setOnClickListener(onClickListener);
        llSilience.setOnClickListener(onClickListener);
        llMianti.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.on_hook) {
                hookCall();
            } else if (i == R.id.shrink) {
                clickShrink();
            } else if (i == R.id.ll_silience) {
                clickLlSilience();
            } else if (i == R.id.ll_mianti) {
                clickLlMianti();
            }
        }
    };


    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ImmersionBar.with(this).init();
        findView();
        initListener();

        boolean linPhoneRegistStatus = getIntent().getBooleanExtra("linPhoneRegistStatus", false);
        phoneNum = getIntent().getStringExtra("phoneNum");
        if (linPhoneRegistStatus && !TextUtils.isEmpty(phoneNum)) {
            String headUrl = getIntent().getStringExtra("headUrl");
            userContactName = getIntent().getStringExtra("name");
            Glide.with(mContext).load(headUrl)
                    .apply(new RequestOptions().centerCrop().circleCrop().error(R.mipmap.default_head))
                    .into(headImg);
            nameTextView.setText(TextUtils.isEmpty(userContactName) ? "" : userContactName);
            phoneNumTextView.setText(phoneNum);
            //先请求权限
            requestPermission(phoneNum);
//            callPhone(phoneNum);
            getPhoneAddress();
        } else {
            LogUtils.e("呼叫失败1");
            showToast("呼叫失败");
            finish();
        }
    }

    private void getPhoneAddress() {
        //判断网络是否可用
        if (!NetUtil.isNetworkConnected(this)) {
            return;
        }
        GetBuilder okHttpUtils = OkHttpUtils.get();
        okHttpUtils.url(check_phone_url);
        okHttpUtils.addParams("tel", phoneNum);
        okHttpUtils.build()
                .execute(new MyStringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        layoutPhoneAddress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        layoutPhoneAddress.setVisibility(View.GONE);
                        LogUtils.i(TAG, response);
                        if (!TextUtils.isEmpty(response)) {
                            try {
                                JSONObject json = new JSONObject(response);
                                JSONObject phoneDetail = json.optJSONObject("response").optJSONObject(phoneNum).optJSONObject("detail");
                                String province = phoneDetail.optString("province");
                                JSONArray area = phoneDetail.optJSONArray("area");
                                String city = "";
                                String showText = "";
                                if (null != area && area.length() > 0) {
                                    city = area.getJSONObject(0).optString("city");
                                }
                                if (!TextUtils.isEmpty(province)) {
                                    showText = province + " " + city;
                                } else {
                                    showText = city;
                                }

                                if (!TextUtils.isEmpty(showText)) {
                                    layoutPhoneAddress.setVisibility(View.VISIBLE);
                                    tvPhoneAddress.setText(showText);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                    }
                });

    }

    private String[] needPermissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CALL_PHONE};
    private List<String> permissionList = new ArrayList<>();

    private void requestPermission(String phoneNum) {
        LogUtils.e("requestPermission");
        permissionList.clear();
        for (String needPermission : needPermissions) {
            if (ContextCompat.checkSelfPermission(mContext, needPermission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(needPermission);
            }
        }
        if (permissionList.size() > 0) {
            isNeedRequestPermission = true;
            ActivityCompat.requestPermissions(this, needPermissions, 996);
        } else {
            callPhone(phoneNum);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        LogUtils.e("requestPermission2");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasPermissionDismiss = false;
        if (996 == requestCode) {
            for (int grantResult : grantResults) {
                if (grantResult == -1) {
                    hasPermissionDismiss = true;
                    break;
                }
            }
            //如果有权限没有被允许
            if (hasPermissionDismiss) {
                isNeedRequestPermission = true;
                showToast("权限被禁止，请允许");
                finish();
            } else {
                isNeedRequestPermission = false;
                callPhone(phoneNum);
            }
        }
    }

    private void callPhone(String phoenNum) {
        LogUtils.e("callPhone");
        LinServiceManager.addListener(mCoreListener);
        //下面的代码一定都要设置，并且在configureAccountActivity中的设置也不能少
        try {
            Object objectBean = SharedPreferencesUtil.getObjectBean(mContext, USERBEAN_SAVE_TAG, UserBean.class);
            if (objectBean != null) {
                UserBean userBean = (UserBean) objectBean;
//                Call call = LinServiceManager.callPhone(phoenNum, userBean.getCcUserInfo().getExtensionNo());
                Call call = LinServiceManager.callPhone(phoenNum, TextUtils.isEmpty(userContactName) ? "" : userContactName);
                if (call != null) {
                    LinServiceManager.switchAudio(mContext, false);
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                } else {
                    LogUtils.e("呼叫失败1");
                    showToast("呼叫失败");
                    finish();
                }
            } else {
                LogUtils.e("呼叫失败2");
                showToast("呼叫失败");
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void hookCall() {
        hook = true;
        LinServiceManager.hookCall();
        LogUtils.e("进入了End");
        if (floatingImageDisplayService != null) {
            floatingImageDisplayService.releaseService();
        }
        finish();
    }

    public void clickShrink() {
        LogUtils.e("clickShrink   ------ > clickShrink");
        //获取悬浮框权限
        boolean permission = FloatWindowManager.getInstance().checkPermission(this);
        if (permission) {
            LogUtils.e("clickShrink   ------ > bindFloatService");
            bindFloatService();
            moveTaskToBack(true);
        } else {
            FloatWindowManager.getInstance().applyPermission(this);
        }
    }

    private void bindFloatService() {
        LogUtils.e("bindFloatService");
        Intent floatWindowIntent = new Intent(this, FloatingImageDisplayService.class);
        floatWindowIntent.putExtra("TIME", this.communicationSecond);
        bindService(floatWindowIntent, conn, BIND_AUTO_CREATE);
//        if(!isNeedRequestPermission){
//            moveTaskToBack(true);
//        }

    }

    private boolean mBound;

    public void setMBound(boolean bound) {
        this.mBound = bound;
    }


    private FloatingImageDisplayService floatingImageDisplayService;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(@NotNull ComponentName name, @NotNull IBinder service) {

            FloatingImageDisplayService.FloatingImageDisplayBinder floatingImageDisplayBinder = (FloatingImageDisplayService.FloatingImageDisplayBinder) service;
            floatingImageDisplayService = floatingImageDisplayBinder.getService();
            setMBound(true);
            isMiantiClicked = true;
            llMianti.setBackgroundResource(R.drawable.selected_container);
        }

        @Override
        public void onServiceDisconnected(@NotNull ComponentName name) {
            setMBound(false);
        }
    };

    private boolean isSilenceClicked;
    private boolean defaultEnableMic;

    public void clickLlSilience() {
        LinServiceManager.setEnableMic(!defaultEnableMic);
        defaultEnableMic = !defaultEnableMic;
        if (!isSilenceClicked) {
            llSilience.setBackgroundResource(R.drawable.selected_container);
        } else {
            llSilience.setBackgroundResource(R.drawable.is_mianti);
        }
        isSilenceClicked = !isSilenceClicked;
    }


    private boolean isMiantiClicked;

    public void clickLlMianti() {
        isMiantiClicked = !isMiantiClicked;
        LinServiceManager.switchAudio(mContext, isMiantiClicked);
        if (isMiantiClicked) {
            llMianti.setBackgroundResource(R.drawable.selected_container);
        } else {
            llMianti.setBackgroundResource(R.drawable.is_mianti);
        }
    }


    private CoreListenerStub mCoreListener = new CoreListenerStub() {
        @Override
        public void onCallStateChanged(Core core, Call call, Call.State state, String message) {
            LogUtils.e("tag:" + state.name());
            if (state == Call.State.End) {
                LogUtils.e("进入了End2");
                hook = true;
                if (floatingImageDisplayService != null) {
                    floatingImageDisplayService.releaseService();
                }
                finish();
            }
        }
    };

    private int communicationSecond;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                communicationStatus.setText("正在通话中");
                communicationSecond++;
                communicationTime.setVisibility(View.VISIBLE);
                communicationTime.setText(TimeUtils.getWatchTime(communicationSecond));
                Message message = new Message();
                message.what = 1;
                this.sendMessageDelayed(message, 1000);
            }
        }
    };


    @Override
    protected void onPause() {
        super.onPause();
        if (mBound) {
            unbindService(conn);
            mBound = false;
        }
        if (!hook) {
            LogUtils.e("onPause   ------ > bindFloatService");
            if (!isNeedRequestPermission) {
                bindFloatService();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
        LinServiceManager.closeSpeaker(this);
        LinServiceManager.removeListener(mCoreListener);
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        if (mBound) {
            unbindService(conn);
            mBound = false;
        }
    }


}
