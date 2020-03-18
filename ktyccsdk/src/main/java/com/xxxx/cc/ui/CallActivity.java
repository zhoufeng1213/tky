package com.xxxx.cc.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.xxxx.cc.R;
import com.xxxx.cc.base.activity.BaseHttpRequestActivity;
import com.xxxx.cc.base.presenter.MyStringCallback;
import com.xxxx.cc.global.KtyCcSdkTool;
import com.xxxx.cc.model.BaseBean;
import com.xxxx.cc.model.CommunicationRecordResponseBean;
import com.xxxx.cc.model.MakecallBean;
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
import com.zhy.http.okhttp.builder.PostStringBuilder;

import org.greenrobot.greendao.annotation.NotNull;
import org.linphone.core.Call;
import org.linphone.core.Core;
import org.linphone.core.CoreListenerStub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;

import static com.xxxx.cc.global.Constans.USERBEAN_SAVE_TAG;
import static com.xxxx.cc.global.HttpRequest.makecallInternal;

import com.alibaba.fastjson.JSONObject;

public class CallActivity extends BaseHttpRequestActivity {


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
    private String userContactName;
    private boolean isNeedRequestPermission;
    static final String check_phone_url = "http://mobsec-dianhua.baidu.com/dianhua_api/open/location";
    private static final String TAG = "CallActivity";
    private UserBean cacheUserBean;
    private CommunicationRecordResponseBean mCommunicationRecordResponseBean;
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
            LogUtils.e("没有网络");
            return;
        }
        GetBuilder okHttpUtils = OkHttpUtils.get();
        okHttpUtils.url(check_phone_url);
        okHttpUtils.addParams("tel", phoneNum);
        okHttpUtils.build()
                .execute(new MyStringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        LogUtils.e("Exception：" + e.getMessage());
                        layoutPhoneAddress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.e("response：" + response);
                        layoutPhoneAddress.setVisibility(View.GONE);
                        if (!response.equals("")) {
                            try {
                                JSONObject json = JSON.parseObject(response);
                                if (!json.getJSONObject("response").toJSONString().equals("{}")) {
                                    JSONObject phoneDetail = json.getJSONObject("response").getJSONObject(phoneNum).getJSONObject("detail");
                                    String province = phoneDetail.getString("province");
                                    JSONArray area = phoneDetail.getJSONArray("area");
                                    String city = "";
                                    String showText = "";
                                    if (null != area && area.size() > 0) {
                                        city = area.getJSONObject(0).getString("city");
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
                cacheUserBean = (UserBean) objectBean;
//                Call call = LinServiceManager.callPhone(phoenNum, userBean.getCcUserInfo().getExtensionNo());
//                Call call = LinServiceManager.callPhone(phoenNum, TextUtils.isEmpty(userContactName) ? "" : userContactName);
                basePostPresenter.presenterBusinessByHeader(
                        makecallInternal,
                        "token", cacheUserBean.getToken(),
                        "Content-Type", "application/json"
                );
            } else {
                LogUtils.e("呼叫失败2");
                showToast("呼叫失败");
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public JSONObject getHttpRequestParams(String moduleName) {
        JSONObject jsonObject = new JSONObject();
        MakecallBean makecallBean = new MakecallBean();
        makecallBean.setCaller(cacheUserBean.getCcUserInfo().getExtensionNo());
        makecallBean.setCallee(phoneNum);
        makecallBean.setName(userContactName);
        makecallBean.setAppname("android");
        jsonObject = JSONObject.parseObject(new Gson().toJson(makecallBean));
        return jsonObject;
    }

    @Override
    public void dealHttpRequestResult(String moduleName, BaseBean result, String response) {
        if (!TextUtils.isEmpty(response)) {
            LinServiceManager.switchAudio(mContext, false);
            Message msg = new Message();
            msg.what = 1;
            handler.sendMessage(msg);
            mCommunicationRecordResponseBean = new CommunicationRecordResponseBean();
            JSONObject json = (JSONObject) result.getData();
            mCommunicationRecordResponseBean.setCalldetailId(json.getString("uuid"));
        }
    }

    @Override
    public void dealHttpRequestFail(String moduleName, BaseBean result) {
        super.dealHttpRequestFail(moduleName, result);
        LogUtils.e("呼叫失败1：" + result.getMessage());
        if(result.getMessage()!=null)
        showToast(result.getMessage());
        else showToast("呼叫失败");
        hookCall();
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
boolean isApplyPermission=false;
    public void clickShrink() {
        LogUtils.e("clickShrink   ------ > clickShrink");
        //获取悬浮框权限
        boolean permission = FloatWindowManager.getInstance().checkPermission(this);
        if (permission) {
            LogUtils.e("clickShrink   ------ > bindFloatService");
            bindFloatService();
            moveTaskToBack(true);
        } else {
            isApplyPermission=true;
            FloatWindowManager.getInstance(). applyPermission(this);
            LogUtils.e("clickShrink   ------ > applyPermission");
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
            LogUtils.e("onServiceConnected");
            FloatingImageDisplayService.FloatingImageDisplayBinder floatingImageDisplayBinder = (FloatingImageDisplayService.FloatingImageDisplayBinder) service;
            floatingImageDisplayService = floatingImageDisplayBinder.getService();
            setMBound(true);
            isMiantiClicked = true;
            llMianti.setBackgroundResource(R.drawable.selected_container);
        }

        @Override
        public void onServiceDisconnected(@NotNull ComponentName name) {
            LogUtils.e("onServiceDisconnected");
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
            LogUtils.i("linphone_callStateChange", "state:" + state.name() + ", message:" + message);
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
    protected void onResume() {
        super.onResume();
        if (mBound) {
            unbindService(conn);
            mBound = false;
        }
        isApplyPermission=false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.e("onPause ");
        if (KtyCcSdkTool.getInstance().mDemoInterface != null && mCommunicationRecordResponseBean != null) {
            KtyCcSdkTool.getInstance().mDemoInterface.goToCall(mCommunicationRecordResponseBean);
        }

        if (!hook) {
            LogUtils.e("onPause   ------ > clickShrink");
            if (!isNeedRequestPermission&&!isApplyPermission) {
                clickShrink();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.e("onDestroy ");
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

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK|| keyCode == KeyEvent.KEYCODE_HOME) {
                  clickShrink();
                    return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtils.e("onNewIntent ");
    }

}
