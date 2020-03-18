package com.xxxx.cc.base.presenter;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.xxxx.cc.global.GlobalApplication;
import com.xxxx.cc.model.BaseBean;
import com.xxxx.cc.util.HttpExceptionUtil;
import com.xxxx.cc.util.LogUtils;
import com.xxxx.cc.util.ToastUtil;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Response;


/**
 * @author zhoufeng
 * @date 2019/2/26
 * @moduleName
 */
public abstract class MyStringCallback extends Callback<String> {
    @Override
    public boolean validateReponse(Response response, int id) {
        return true;
    }

    @Override
    public String parseNetworkResponse(Response response, int id) throws Exception {
        LogUtils.e("response:"+response);
        if (response.code() >= 200 && response.code() < 300) {
            if (response.body() != null) {
                return response.body().string();
            }
            return null;
        } else {
            String mes=response.body().string();
            if(mes!=null)
            {
                LogUtils.e("error:"+mes);
                throw new Exception(mes);
            }
            else {
                throw new Exception(HttpExceptionUtil.getHttpExceptionMsg(response.code() + ""));
            }
        }

    }
}
