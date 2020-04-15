package com.xxxx.tky.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.xxxx.cc.base.activity.BaseActivity;
import com.xxxx.tky.R;
import com.xxxx.tky.util.AntiShakeUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class CustomServicerActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.wv)
    WebView wv;

    @Override
    public int getLayoutViewId() {
        return R.layout.activity_guide;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        int type = getIntent().getIntExtra("type", -1);
        if (type == 1) {
            tvTitle.setText("服务条款");
            wv.getSettings().setJavaScriptEnabled(true);
            wv.setWebViewClient(new WebViewClient());
            wv.loadUrl("https://tkycdn.ketianyun.com/document/TermsOfService.html");
        } else if (type == 2) {
            tvTitle.setText("隐私协议");
            wv.getSettings().setJavaScriptEnabled(true);
            wv.setWebViewClient(new WebViewClient());
            wv.loadUrl("https://tkycdn.ketianyun.com/document/PrivacyAgreement.html");
        }
    }

    @OnClick(R.id.iv_close)
    public void back(View view) {
        if (AntiShakeUtils.isInvalidClick(view)) {
            return;
        }
        finish();
    }
}
