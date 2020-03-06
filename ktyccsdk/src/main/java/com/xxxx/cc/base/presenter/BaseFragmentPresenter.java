package com.xxxx.cc.base.presenter;


import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.xxxx.cc.base.fragment.BaseHttpRequestFragment;
import com.xxxx.cc.global.Constans;
import com.xxxx.cc.model.BaseBean;
import com.xxxx.cc.util.NetUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.builder.PostStringBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;


/**
 * 模块名称:
 * Created by fly(zhoufeng) on 2017/3/13.
 */

public class BaseFragmentPresenter {

    private BaseHttpRequestFragment mActivity;
    private boolean isShowDialog = true;

    public BaseFragmentPresenter(BaseHttpRequestFragment mActivity) {
        this.mActivity = mActivity;
    }

    public void presenterBusiness(String moduleName, boolean isShowDialog) {
        this.isShowDialog = isShowDialog;
        if (isShowDialog) {
            mActivity.showDialog();
        }
        doPost(moduleName);
    }

    public void presenterBusiness(String moduleName) {
        mActivity.showDialog();
        doPost(moduleName);
    }

    public void presenterBusiness(String moduleName, String dialogMsg) {
        mActivity.showDialog(dialogMsg);
        doPost(moduleName);
    }


    public void presenterBusinessByHeader(String moduleName, boolean isShowDialog, String... headers) {
        this.isShowDialog = isShowDialog;
        if (isShowDialog) {
            mActivity.showDialog();
        }
        doPostByHeaders(moduleName, headers);
    }


    private void doPostByHeaders(final String moduleName, String... headers) {
        //判断网络是否可用
        if (!NetUtil.isNetworkConnected(mActivity.mContext)) {
            mActivity.dismissDialog();
            BaseBean baseBean = new BaseBean();
            baseBean.setMessage("无网络");
            mActivity.dealHttpRequestFail(moduleName, baseBean);
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
        okHttpUtils
                .content(mActivity.getHttpRequestParams(moduleName).toString())
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build()
                .execute(new MyStringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mActivity.dismissDialog();
                        BaseBean baseBean = new BaseBean();
                        if (e != null) {
                            baseBean.setMessage(e.getMessage());
                        } else {
                            baseBean = null;
                        }
                        Log.e("lxl","e.getMessage():"+e.getMessage());
                        mActivity.dealHttpRequestFail(moduleName, baseBean);
                    }


                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("lxl","onResponse:"+response);
                        try {
                            if (isShowDialog) {
                                mActivity.dismissDialog();
                            }
                            if (!TextUtils.isEmpty(response)) {
                                BaseBean baseBean = JSON.parseObject(response, BaseBean.class);
                                if (baseBean.isOk()) {
                                    mActivity.dealHttpRequestResult(moduleName, baseBean, response);
                                } else {
                                    mActivity.dismissDialog();
                                    mActivity.dealHttpRequestFail(moduleName, baseBean);
                                }
                            } else {
                                mActivity.dismissDialog();
                                mActivity.showToast("服务器无响应");
                                mActivity.dealHttpRequestFail(moduleName, null);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mActivity.dismissDialog();
                            mActivity.showToast("请检查网络连接");
                            mActivity.dealHttpRequestFail(moduleName, null);
                        }
                    }
                });
    }

    private void doPost(final String moduleName) {
        PostFormBuilder okHttpUtils = OkHttpUtils.post();
        okHttpUtils.url(Constans.BASE_URL + moduleName);
        Map<String, String> params = mActivity.getParams(moduleName);
        for (String key : params.keySet()) {
            okHttpUtils.addParams(key, params.get(key));
        }
        okHttpUtils.build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mActivity.dismissDialog();
                        mActivity.showToast("请检查网络连接");
//                        LogUtils.e("出错接口:"+moduleName);
//                        LogUtils.e("参数:"+params.toString());
                        mActivity.dealHttpRequestFail(moduleName,null);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            if(isShowDialog){
                                mActivity.dismissDialog();
                            }
                            if(!TextUtils.isEmpty(response)){
                                BaseBean baseBean = JSON.parseObject(response, BaseBean.class);
                                if (baseBean.isOk()) {
                                    mActivity.dealHttpRequestResult(moduleName, baseBean);
                                } else {
                                    mActivity.dismissDialog();
                                    mActivity.showToast(baseBean.getMessage() != null ? baseBean.getMessage() : "请检查网络连接");
                                    mActivity.dealHttpRequestFail(moduleName,baseBean);
                                }
                            }else{
                                mActivity.dismissDialog();
                                mActivity.showToast("请检查网络连接");
                                mActivity.dealHttpRequestFail(moduleName,null);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mActivity.dismissDialog();
                            mActivity.showToast("请检查网络连接");
                            mActivity.dealHttpRequestFail(moduleName,null);
                        }

                    }
                });

    }


}
