package com.xxxx.tky.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.xxxx.cc.base.activity.BaseActivity;
import com.xxxx.cc.base.presenter.MyStringCallback;
import com.xxxx.cc.util.LogUtils;
import com.xxxx.cc.util.NetUtil;
import com.xxxx.tky.R;
import com.xxxx.tky.fragment.HistoryFragment;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author zhoufeng
 * @date 2019/8/12
 * @moduleName
 */
public class CallHistoryFragmentActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_fragment);

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction beginTransaction = manager.beginTransaction();
        FragmentTransaction transaction = beginTransaction.replace(R.id.fragment_container,
                new HistoryFragment());
        transaction.commit();
    }

    @Override
    public int getLayoutViewId() {
        return R.layout.activity_history_fragment;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
