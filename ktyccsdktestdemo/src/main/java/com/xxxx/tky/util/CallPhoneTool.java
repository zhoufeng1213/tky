package com.xxxx.tky.util;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.xxxx.cc.base.presenter.MyStringCallback;
import com.xxxx.cc.callback.CallPhoneBack;
import com.xxxx.cc.global.Constans;
import com.xxxx.cc.global.HttpRequest;
import com.xxxx.cc.global.KtyCcSdkTool;
import com.xxxx.cc.model.BaseBean;
import com.xxxx.cc.model.UserBean;
import com.xxxx.cc.service.LinphoneService;
import com.xxxx.cc.util.LogUtils;
import com.xxxx.cc.util.NetUtil;
import com.xxxx.cc.util.SharedPreferencesUtil;
import com.xxxx.cc.util.SystemUtils;
import com.xxxx.cc.util.ToastUtil;
import com.xxxx.tky.model.MakecallBean;
import com.xxxx.tky.model.XiangyuncallBean;
import com.xxxx.tky.view.ButtomCallDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostStringBuilder;

import okhttp3.Call;
import okhttp3.MediaType;

import static com.xxxx.cc.global.Constans.USERBEAN_SAVE_TAG;
import static com.xxxx.cc.global.HttpRequest.Contant.updateLastCallTime;


public class CallPhoneTool {

    private static volatile CallPhoneTool callPhoneTool;
    private Context mContext;
    private UserBean cacheUserBean;
    private String phoneNum;
    private String userName;
    private String customeUserId;
    public CallPhoneBySim callPhoneBySim;
    public CallPhoneFromDetailActivity callPhoneFromDetailActivity;

    private CallPhoneTool() {

    }

    public static CallPhoneTool getInstance() {
        if (callPhoneTool == null) {
            synchronized (CallPhoneTool.class) {
                if (callPhoneTool == null) {
                    callPhoneTool = new CallPhoneTool();
                }
            }
        }
        return callPhoneTool;
    }


    ButtomCallDialog dialog;

    /**
     * 拨打电话
     */
    public void callPhone(Context mContext, String phoneNum, String userName, String customeUserId) {
        this.mContext = mContext;
        this.phoneNum = phoneNum;
        this.userName = userName;
        this.customeUserId = customeUserId;
        Object objectBean = SharedPreferencesUtil.getObjectBean(mContext, USERBEAN_SAVE_TAG, UserBean.class);
        if (objectBean != null) {
            cacheUserBean = (UserBean) objectBean;
        }
        ButtomCallDialog.Builder builder = new ButtomCallDialog.Builder(mContext);
        if (cacheUserBean == null) {
            return;
        }
        boolean[] siteEnables = cacheUserBean.getSiteEnables();
        if (siteEnables != null && !siteEnables[0] && !siteEnables[1] && !siteEnables[2]) {
            builder.addMenu("无拨号权限", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LogUtils.e("---》无拨号权限");
                    dialog.cancel();
                }
            });
        }
        if (siteEnables != null && siteEnables[0]) {
            builder.addMenu("网络拨号", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                    if (cacheUserBean.getCcUserInfo() == null || cacheUserBean.getCcUserInfo().getExtensionNo() == null || cacheUserBean.getCcUserInfo().getExtensionNo().equals("")) {
                        showToast("请联系管理员配置分机号,然后退出重新登录");
                        return;
                    }
                    KtyCcSdkTool.getInstance().callPhone(mContext, phoneNum,
                            userName,
                            "", customeUserId, new CallPhoneBack() {
                                @Override
                                public void onSuccess(String callId) {

                                }

                                @Override
                                public void onFailed(String message) {

                                }

                                @Override
                                public void watchPhoneStatus(int status) {

                                }
                            }
                    );

                }
            });
        }

        if (siteEnables != null && siteEnables[1]) {
            builder.addMenu("双呼拨号", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                    LogUtils.e("Mobile:" + cacheUserBean.getMobile());
                    if (cacheUserBean.getMobile() == null || cacheUserBean.getMobile().equals("")) {
                        showToast("请前往我的个人信息页面配置手机号");
                        return;
                    }
                    doPostByHeaders(Constans.BASE_URL,
                            HttpRequest.makecallExternal,
                            "token", cacheUserBean.getToken(),
                            "Content-Type", "application/json"
                    );
                }
            });
        }
        if (siteEnables != null && siteEnables[3]) {
            builder.addMenu("API拨号", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                    LogUtils.e("Mobile:" + cacheUserBean.getMobile());
                    doPostByHeaders(Constans.BASE_URL,
                            HttpRequest.xiangyunCallApi,
                            "token", cacheUserBean.getToken(),
                            "Content-Type", "application/json"
                    );
                }
            });
        }

        if (siteEnables != null && siteEnables[2]) {
            builder.addMenu("手机卡拨号", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                    checkSimCallPermission(mContext, "tel:" + phoneNum);
                }
            });
        }

        builder.setTitle("呼叫号码" + phoneNum);
        dialog = builder.create();
        dialog.show();

    }

    private void checkSimCallPermission(Context context, String telPhone) {
        PermissionUtil.requestPermission(context, new PermissionUtil.IPermissionListener() {
            @Override
            public void permissionGranted() {
                if (SystemUtils.hasSim(context)) {
                    call(context, telPhone);
                } else {
                    ToastUtil.showToast(context, "无SIM卡");
                }

            }

            @Override
            public void permissionDenied() {
                showToast("权限被拒绝");
            }
        }, "权限被拒绝,请设置应用权限", Manifest.permission.CALL_PHONE, Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_PHONE_STATE);
    }

    private void call(Context context, String telPhone) {
        try {
            if (callPhoneBySim != null) {
                callPhoneBySim.onCall();
            }
            LinphoneService.telUserName = userName;
            LinphoneService.telNum = telPhone;
            LinphoneService.startTelFromCall = true;
            Intent intent = new Intent(Intent.ACTION_CALL);
            Uri uri = Uri.parse(telPhone);
            intent.setData(uri);
            context.startActivity(intent);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void setCallPhoneBySim(CallPhoneBySim callPhoneBySim) {
        this.callPhoneBySim = callPhoneBySim;
    }
    public void setCallPhoneFromDetailActivity(CallPhoneFromDetailActivity callPhoneFromDetailActivity) {
        this.callPhoneFromDetailActivity = callPhoneFromDetailActivity;
    }
    private void doPostByHeaders(String baseUrl, String moduleName, String... headers) {
        //判断网络是否可用
        if (!NetUtil.isNetworkConnected(mContext)) {
            BaseBean baseBean = new BaseBean();
            baseBean.setMessage("网络连接失败，请检查网络");
            showToast("请检查网络连接");
            dealHttpRequestFail(moduleName, baseBean);
            return;
        }
        PostStringBuilder okHttpUtils = OkHttpUtils.postString();
        okHttpUtils.url(baseUrl + moduleName);
        //添加header
        if (headers != null) {
            if (headers.length > 1 && headers.length % 2 == 0) {
                for (int i = 0; i < headers.length; i += 2) {
                    okHttpUtils.addHeader(String.valueOf(headers[i]), String.valueOf(headers[i + 1]));
                }
            }
        }
        LogUtils.e("url:" + baseUrl + moduleName + "，Params:" + getHttpRequestParams(moduleName).toString());
        okHttpUtils
                .content(getHttpRequestParams(moduleName).toString())
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new MyStringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e("onError:" + e.getMessage());
                        if (e != null) {
                            BaseBean baseBean = JSON.parseObject(e.getMessage(), BaseBean.class);
                            dealHttpRequestFail(moduleName, baseBean);
                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        LogUtils.e("response:" + response);
                        try {
                            if (!TextUtils.isEmpty(response)) {
                                BaseBean baseBean = JSON.parseObject(response, BaseBean.class);
                                if (baseBean.isOk()) {
                                    dealHttpRequestSuccess(moduleName, baseBean);
                                } else if (baseBean.getCode() == 200 && moduleName.equals(HttpRequest.xiangyunCallApi)) {
                                    dealHttpRequestSuccess(moduleName, baseBean);
                                } else if (baseBean.getCode() == 400 && moduleName.equals(HttpRequest.xiangyunCallApi)) {
                                    dealHttpRequestFail(moduleName, baseBean);
                                }else {
                                    LogUtils.e("呼叫失败 http 4");
                                    if (baseBean != null && TextUtils.isEmpty(baseBean.getMessage())) {
                                        baseBean.setMessage("呼叫失败");
                                    }
                                    dealHttpRequestFail(moduleName, baseBean);
                                }
                            } else {
                                showToast("服务器无响应");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            showToast("请检查网络连接");
                        }
                    }
                });

    }

    public void showToast(String msg) {
        ToastUtil.showToast(mContext, msg);
    }

    public JSONObject getHttpRequestParams(String moduleName) {
        JSONObject jsonObject = new JSONObject();
        if (HttpRequest.makecallExternal.equals(moduleName)) {
            MakecallBean makecallBean = new MakecallBean();
            makecallBean.setCaller(cacheUserBean.getMobile());
            makecallBean.setCallee(phoneNum);
            makecallBean.setName(userName);
            makecallBean.setAppname("android");
            jsonObject = JSONObject.parseObject(new Gson().toJson(makecallBean));
        } else if (HttpRequest.xiangyunCallApi.equals(moduleName)) {
            XiangyuncallBean xiangyuncallBean = new XiangyuncallBean(phoneNum);
            jsonObject = JSONObject.parseObject(new Gson().toJson(xiangyuncallBean));
        }
        return jsonObject;
    }

    public void dealHttpRequestFail(String moduleName, BaseBean result) {
        if (HttpRequest.makecallExternal.equals(moduleName)) {
            showToast(result.getMessage());
        } else if ((updateLastCallTime + "/" + customeUserId).equals(moduleName)) {
            LogUtils.e("更新客户电话时间失败");
        } else if (HttpRequest.xiangyunCallApi.equals(moduleName)) {
            try {
                JSONObject jsonObject= (JSONObject) result.getData();
                showToast(jsonObject.getString("msg"));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void dealHttpRequestSuccess(String moduleName, BaseBean result) {
        if (HttpRequest.makecallExternal.equals(moduleName)) {
            showToast("呼叫成功，请注意接听电话");
            if (!TextUtils.isEmpty(customeUserId)) {
                doPostByHeaders(Constans.BASE_URL,
                        (updateLastCallTime + "/" + customeUserId),
                        "token", cacheUserBean.getToken(),
                        "Content-Type", "application/json"
                );
            }
            try {
                JSONObject jsonObject= (JSONObject) result.getData();
                if(callPhoneFromDetailActivity!=null){
                    callPhoneFromDetailActivity.onCall(jsonObject.getString("uuid"));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        } else if ((updateLastCallTime + "/" + customeUserId).equals(moduleName)) {

            LogUtils.e("更新客户电话时间成功");
        }else if(HttpRequest.xiangyunCallApi.equals(moduleName)){
            try {
                JSONObject jsonObject= (JSONObject) result.getData();
                if(jsonObject.getBoolean("success")){
                    showToast("呼叫成功，请注意接听电话");
                    if(callPhoneFromDetailActivity!=null){
                        callPhoneFromDetailActivity.onCall(jsonObject.getString("sessionId"));
                    }
                    if (!TextUtils.isEmpty(customeUserId)) {
                        doPostByHeaders(Constans.BASE_URL,
                                (updateLastCallTime + "/" + customeUserId),
                                "token", cacheUserBean.getToken(),
                                "Content-Type", "application/json"
                        );
                    }
                }else {
                    showToast(jsonObject.getString("msg"));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public interface CallPhoneBySim {
        void onCall();
    }
    public interface CallPhoneFromDetailActivity {
        void onCall(String sessionId);
    }
}
