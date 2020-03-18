package com.xxxx.cc.base.presenter;


import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.xxxx.cc.base.activity.BaseHttpRequestActivity;
import com.xxxx.cc.global.Constans;
import com.xxxx.cc.model.BaseBean;
import com.xxxx.cc.util.LogUtils;
import com.xxxx.cc.util.NetUtil;
import com.xxxx.cc.util.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;

import java.util.Map;

import okhttp3.Call;


/**
 * 模块名称:
 * Created by fly(zhoufeng) on 2017/3/13.
 */

public class BaseGetPresenter {

    private BaseHttpRequestActivity mActivity;

    public BaseGetPresenter(BaseHttpRequestActivity mActivity) {
        this.mActivity = mActivity;
    }

    public void presenterBusiness(String moduleName, boolean isShowDialog) {
        if (isShowDialog) {
            mActivity.showDialog();
        }
        doGet(moduleName);
    }

    public void presenterBusiness(String moduleName) {
        mActivity.showDialog();
        doGet(moduleName);
    }

    public void presenterBusiness(String moduleName, String dialogMsg) {
        mActivity.showDialog(dialogMsg);
        doGet(moduleName);
    }

    public void presenterBusinessByHeader(String moduleName, String... headers) {
        mActivity.showDialog();
        doGetByHeaders(moduleName, headers);
    }

    public void presenterBusinessByHeader(String moduleName, boolean isShowDialog, String... headers) {
        if (isShowDialog) {
            mActivity.showDialog();
        }
        doGetByHeaders(moduleName, headers);
    }

    private void doGetByHeaders(final String moduleName, String... headers) {
        try {
            //判断网络是否可用
            if (!NetUtil.isNetworkConnected(mActivity)) {
                mActivity.dismissDialog();
                BaseBean baseBean = new BaseBean();
                baseBean.setMessage("无网络");
                mActivity.dealHttpRequestFail(moduleName, baseBean);
                return;
            }
            GetBuilder okHttpUtils = OkHttpUtils.get();
            okHttpUtils.url(Constans.BASE_URL + moduleName);
            //添加header
            if (headers != null) {
                if (headers.length > 1 && headers.length % 2 == 0) {
                    for (int i = 0; i < headers.length; i += 2) {
                        okHttpUtils.addHeader(String.valueOf(headers[i]), String.valueOf(headers[i + 1]));
                    }
                }
            }
            LogUtils.e("url:"+Constans.BASE_URL + moduleName+"，Params:"+mActivity.getHttpRequestParams(moduleName).toString());
            //添加请求参数
            Map mapParam = JSON.parseObject(mActivity.getHttpRequestParams(moduleName).toJSONString(),
                    Map.class);
            for (Object key : mapParam.keySet()) {
                okHttpUtils.addParams( (String) key, mapParam.get(key)==null?"":String.valueOf(mapParam.get(key)));
            }
            okHttpUtils.build()
                    .execute(new MyStringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            LogUtils.e("e.getMessage():"+e.getMessage());
                            mActivity.dismissDialog();
                            if (e != null) {
                                BaseBean baseBean = JSON.parseObject(e.getMessage(), BaseBean.class);
                                if(baseBean.getCode()==45009)
                                {
                                    ToastUtil.showToast(mActivity,"您的登录身份已过期，请退出重新登录");
                                }
                            else {
                                    mActivity.dealHttpRequestFail(moduleName, baseBean);
                                }
                            }
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.e("lxl","onResponse:"+response);
                            try {
                                mActivity.dismissDialog();
                                BaseBean baseBean = JSON.parseObject(response, BaseBean.class);
                                if (baseBean.isOk()) {
                                    mActivity.dealHttpRequestResult(moduleName, baseBean,response);
                                } else {
    //                                mActivity.showToast(baseBean.getResMsg() != null ? baseBean.getResMsg() : "请检查网络连接");
                                    mActivity.dealHttpRequestFail(moduleName, baseBean);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            BaseBean baseBean = new BaseBean();
            baseBean.setMessage("数据转化错误");
            mActivity.dealHttpRequestFail(moduleName, baseBean);
        }
    }

    private void doGet(final String moduleName) {
        //判断网络是否可用
        if (!NetUtil.isNetworkConnected(mActivity)) {
            mActivity.dismissDialog();
            BaseBean baseBean = new BaseBean();
            baseBean.setMessage("无网络");
            mActivity.dealHttpRequestFail(moduleName, baseBean);
            return;
        }
        GetBuilder okHttpUtils = OkHttpUtils.get();
        okHttpUtils.url(Constans.BASE_URL + moduleName);
       LogUtils.e("url:"+Constans.BASE_URL + moduleName+"，Params:"+mActivity.getHttpRequestParams(moduleName).toString());
        //添加请求参数
        Map mapParam = JSON.parseObject(mActivity.getHttpRequestParams(moduleName).toJSONString(),
                Map.class);
        for (Object key : mapParam.keySet()) {
            okHttpUtils.addParams((String) key, (String) mapParam.get(key));
        }
       LogUtils.e("url:"+Constans.BASE_URL + moduleName+"，Params:"+mapParam.toString());
        okHttpUtils.build()
                .execute(new MyStringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        mActivity.dismissDialog();
                        LogUtils.e("Message:"+e.getMessage());
                        if (e != null) {
                            BaseBean baseBean;
                            try {
                                 baseBean = JSON.parseObject(e.getMessage(), BaseBean.class);
                            }catch (JSONException exception)
                            {
                                 baseBean =new BaseBean();
                                baseBean.setMessage(e.getMessage());
                            }


                            mActivity.dealHttpRequestFail(moduleName, baseBean);

                        }
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        mActivity.dismissDialog();
                        BaseBean baseBean = JSON.parseObject(response, BaseBean.class);
                        if (baseBean.isOk()) {
                            mActivity.dealHttpRequestResult(moduleName, baseBean,response);
                        } else {
//                            mActivity.showToast(baseBean.getResMsg() != null ? baseBean.getResMsg() : "请检查网络连接");
                            mActivity.dealHttpRequestFail(moduleName, baseBean);
                        }
                    }
                });
    }


}
