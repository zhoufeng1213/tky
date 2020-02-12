package com.xxxx.tky.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.xxxx.cc.base.activity.BaseActivity;
import com.xxxx.tky.R;
import com.xxxx.tky.util.AntiShakeUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class GuideActivity extends BaseActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.wv)
    WebView wv;

    @Override
    public int getLayoutViewId() {
        return  R.layout.activity_guide;
    }
    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        tvTitle.setText("使用指南");
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebViewClient(new WebViewClient());
        wv.loadUrl("https://www.ketianyun.com/callcenter/tky/app/help/help.html");
    }
    @OnClick(R.id.iv_close)
    public void back(View view) {
        if (AntiShakeUtils.isInvalidClick(view)) {
            return;
        }
        finish();
    }
}
