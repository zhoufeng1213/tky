package com.xxxx.tky.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.kty.mars.baselibrary.log.LogUtil;
import com.xxxx.cc.base.presenter.BasePostPresenter;
import com.xxxx.cc.base.presenter.MyStringCallback;
import com.xxxx.cc.global.Constans;
import com.xxxx.cc.global.HttpRequest;
import com.xxxx.cc.global.KtyCcSdkTool;
import com.xxxx.cc.model.BaseBean;
import com.xxxx.cc.model.UserBean;
import com.xxxx.cc.util.LogUtils;
import com.xxxx.cc.util.NetUtil;
import com.xxxx.cc.util.SharedPreferencesUtil;
import com.xxxx.cc.util.ToastUtil;
import com.xxxx.tky.model.MakecallBean;
import com.xxxx.tky.view.ButtomCallDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostStringBuilder;

import okhttp3.Call;
import okhttp3.MediaType;

import static com.xxxx.cc.global.Constans.USERBEAN_SAVE_TAG;


public class CallPhoneTool {

    private static volatile CallPhoneTool callPhoneTool;
    private Context mContext;
    private UserBean cacheUserBean;
    private String phoneNum;
    private String userName;

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
    public void callPhone(Context mContext, String phoneNum, String userName) {
        this.mContext = mContext;
        this.phoneNum = phoneNum;
        this.userName = userName;
        Object objectBean = SharedPreferencesUtil.getObjectBean(mContext, USERBEAN_SAVE_TAG, UserBean.class);
        if (objectBean != null) {
            cacheUserBean = (UserBean) objectBean;
        }
        ButtomCallDialog.Builder builder = new ButtomCallDialog.Builder(mContext);
        builder.addMenu("网络拨号", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                if (cacheUserBean.getCcUserInfo()==null||cacheUserBean.getCcUserInfo().getExtensionNo() == null || cacheUserBean.getCcUserInfo().getExtensionNo() .equals("")) {
                    showToast("请联系管理员配置分机号,然后退出重新登录");
                    return;
                }
                KtyCcSdkTool.getInstance().callPhone(mContext, phoneNum,
                        userName,
                        ""
                );

            }
        }).addMenu("双呼拨号", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                LogUtils.e("Mobile:" + cacheUserBean.getMobile());
                if (cacheUserBean.getMobile() == null || cacheUserBean.getMobile().equals("")) {
                    showToast("请联系管理员配置手机号,然后退出重新登录");
                    return;
                }
                doPostByHeaders(
                        HttpRequest.makecallExternal,
                        "token", cacheUserBean.getToken(),
                        "Content-Type", "application/json"
                );
            }
        });
        builder.setTitle("呼叫号码" + phoneNum);
        dialog = builder.create();
        dialog.show();

    }

    private void doPostByHeaders(String moduleName, String... headers) {
        //判断网络是否可用
        if (!NetUtil.isNetworkConnected(mContext)) {
            BaseBean baseBean = new BaseBean();
            baseBean.setMessage("网络连接失败，请检查网络");
            showToast("请检查网络连接");
            dealHttpRequestFail(moduleName, baseBean);
            return;
        }
        PostStringBuilder okHttpUtils = OkHttpUtils.postString();
        okHttpUtils.url(Constans.BASE_URL + moduleName);
        //添加header
        if (headers != null) {
            if (headers.length > 1 && headers.length % 2 == 0) {
                for (int i = 0; i < headers.length; i += 2) {
                    okHttpUtils.addHeader(String.valueOf(headers[i]), String.valueOf(headers[i + 1]));
                }
            }
        }
       LogUtils.e("url:"+Constans.BASE_URL + moduleName+"，Params:"+getHttpRequestParams(moduleName).toString());
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
                                    showToast("呼叫成功，请注意接听电话");

                                } else {
                                    showToast("呼叫失败");
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
        MakecallBean makecallBean = new MakecallBean();
        makecallBean.setCaller(cacheUserBean.getMobile());
        makecallBean.setCallee(phoneNum);
        makecallBean.setName(userName);
        makecallBean.setAppname("android");
        jsonObject = JSONObject.parseObject(new Gson().toJson(makecallBean));
        return jsonObject;
    }

    public void dealHttpRequestFail(String moduleName, BaseBean result) {

    }
}
