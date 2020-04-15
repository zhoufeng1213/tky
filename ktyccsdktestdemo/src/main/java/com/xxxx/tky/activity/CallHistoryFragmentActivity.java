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
        getPhoneAddress();

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

    private void getPhoneAddress() {
        //判断网络是否可用
        if (!NetUtil.isNetworkConnected(this)) {
            return;
        }
        GetBuilder okHttpUtils = OkHttpUtils.get();
        okHttpUtils.url("http://mobsec-dianhua.baidu.com/dianhua_api/open/location");
        okHttpUtils.addParams("tel", "17620370606");
        okHttpUtils.build()
                .execute(new MyStringCallback() {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        LogUtils.i("TAG", "onError");
//                        layoutPhoneAddress.setVisibility(View.GONE);
                    }

                    @Override
                    public void onResponse(String response, int id) {
//                        layoutPhoneAddress.setVisibility(View.VISIBLE);
                        LogUtils.i("TAG", response);
                        if (response != null && !TextUtils.isEmpty(response.trim())) {
                            try {
                                JSONObject json = new JSONObject(response);
                                JSONObject phoneDetail = json.optJSONObject("response").optJSONObject("17620370606").optJSONObject("detail");
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

                                LogUtils.i("TAG", showText);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }


                    }
                });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
