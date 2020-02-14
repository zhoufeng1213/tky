package com.xxxx.cc.base.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.xxxx.cc.base.presenter.BaseFragmentPresenter;
import com.xxxx.cc.base.presenter.BaseGetPresenter;
import com.xxxx.cc.model.BaseBean;

import java.util.HashMap;
import java.util.Map;

/**
 * 模块名称:
 * Created by fly(zhoufeng) on 2017-04-08.
 */
public abstract class BaseHttpRequestFragment extends BaseFragment {

    public BaseFragmentPresenter basePostPresenter;
    public BaseGetPresenter baseGetPresenter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        basePostPresenter = new BaseFragmentPresenter(this);
    }

    public Map<String, String> getParams(String moduleName){
        return new HashMap<>();
    }

    public JSONObject getHttpRequestParams(String moduleName) {
        return new JSONObject();
    }

    public void dealHttpRequestResult(String moduleName, BaseBean result){

    }
    public void dealHttpRequestResult(String moduleName, BaseBean result, String response) {

    }
    public void dealHttpRequestFail(String moduleName,BaseBean result){

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        basePostPresenter = null;
    }


}
