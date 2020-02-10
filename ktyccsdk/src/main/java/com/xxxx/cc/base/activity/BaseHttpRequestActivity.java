package com.xxxx.cc.base.activity;

import android.os.Bundle;

import com.alibaba.fastjson.JSONObject;
import com.xxxx.cc.base.presenter.BaseGetPresenter;
import com.xxxx.cc.base.presenter.BasePostPresenter;
import com.xxxx.cc.model.BaseBean;

/**
 * @author zhoufeng
 * @date 2018/5/31
 */
public abstract class BaseHttpRequestActivity extends BaseActivity {

    public BasePostPresenter basePostPresenter;
    public BaseGetPresenter baseGetPresenter;

    @Override
    public void initView(Bundle savedInstanceState) {
        basePostPresenter = new BasePostPresenter(this);
        baseGetPresenter = new BaseGetPresenter(this);
        super.initView(savedInstanceState);
    }

    public JSONObject getHttpRequestParams(String moduleName) {
        return new JSONObject();
    }


//    public void dealHttpRequestResult(String moduleName, BaseBean result) {
//
//    }

    public void dealHttpRequestResult(String moduleName, BaseBean result, String response) {

    }

    public void dealHttpRequestFail(String moduleName, BaseBean result) {
        if (result != null) {
            showToast(result.getMessage() != null ? result.getMessage() : "服务器发生错误");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        basePostPresenter = null;
        baseGetPresenter = null;
    }
}
