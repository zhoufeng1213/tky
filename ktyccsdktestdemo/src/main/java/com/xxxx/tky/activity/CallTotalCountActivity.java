package com.xxxx.tky.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.xxxx.cc.base.activity.BaseHttpRequestActivity;
import com.xxxx.cc.global.HttpRequest;
import com.xxxx.cc.model.BaseBean;
import com.xxxx.cc.model.CallTotalCountBean;
import com.xxxx.cc.model.UserBean;
import com.xxxx.cc.util.SharedPreferencesUtil;
import com.xxxx.tky.R;
import com.xxxx.tky.util.AntiShakeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xxxx.cc.global.Constans.USERBEAN_SAVE_TAG;

/**
 * @author zhoufeng
 * @date 2020/4/14
 * @moduleName
 */
public class CallTotalCountActivity extends BaseHttpRequestActivity {

    @BindView(R.id.huchu_number_textview)
    TextView huchuNumberTextview;
    @BindView(R.id.jietong_number_textview)
    TextView jietongNumberTextview;
    @BindView(R.id.tonghuashichang_textview)
    TextView tonghuashichangTextview;
    @BindView(R.id.huru_number_textview)
    TextView huruNumberTextview;
    @BindView(R.id.jietonghuru_number_textview)
    TextView jietonghuruNumberTextview;
    @BindView(R.id.weijie_textview)
    TextView weijieTextview;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private UserBean cacheUserBean;

    @Override
    public int getLayoutViewId() {
        return R.layout.activity_call_to_total;
    }


    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        Object objectBean = SharedPreferencesUtil.getObjectBean(mContext, USERBEAN_SAVE_TAG, UserBean.class);
        if (objectBean != null) {
            cacheUserBean = (UserBean) objectBean;
            baseGetPresenter.presenterBusinessByHeader(HttpRequest.callTotalCount,
                    "token", cacheUserBean.getToken());
        } else {
            finish();
        }
    }

    @OnClick(R.id.iv_close)
    public void clickClose(View view) {
        if (AntiShakeUtils.isInvalidClick(view)) {
            return;
        }
        finish();
    }


    @Override
    public void dealHttpRequestResult(String moduleName, BaseBean result, String response) {
        super.dealHttpRequestResult(moduleName, result, response);
        //直接赋值
        CallTotalCountBean callTotalCountBean = JSON.parseObject(result.getData().toString(), CallTotalCountBean.class);
        if (callTotalCountBean != null) {
            huchuNumberTextview.setText(String.valueOf(callTotalCountBean.getCalloutCount()));
            jietongNumberTextview.setText(String.valueOf(callTotalCountBean.getSuccessCallOutCount()));
            tonghuashichangTextview.setText(String.valueOf(callTotalCountBean.getTotalCallOutTime()));
            huruNumberTextview.setText(String.valueOf(callTotalCountBean.getCallinCount()));
            jietonghuruNumberTextview.setText(String.valueOf(callTotalCountBean.getSuccessCallInCount()));
            weijieTextview.setText(String.valueOf(callTotalCountBean.getMisscallInCount()));
        }
    }

    @Override
    public void dealHttpRequestFail(String moduleName, BaseBean result) {
        super.dealHttpRequestFail(moduleName, result);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
