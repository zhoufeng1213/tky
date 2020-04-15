package com.xxxx.tky.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxxx.cc.base.activity.BaseActivity;
import com.xxxx.cc.global.PackageUtils;
import com.xxxx.tky.R;
import com.xxxx.tky.util.AntiShakeUtils;

public class ActivityAboutActivity extends BaseActivity implements View.OnClickListener {
    private TextView tvTitle;
    private TextView userImage;
    private TextView tvVersion;
    private TextView btnFuwuxieyi;
    private TextView btnYinsi;
    private TextView companyName;
    private ImageView iv_close;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        iv_close = findViewById(R.id.iv_close);
        iv_close.setOnClickListener(this);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("关于");
        userImage = (TextView) findViewById(R.id.user_image);
        tvVersion = (TextView) findViewById(R.id.tv_version);
        btnFuwuxieyi = (TextView) findViewById(R.id.btn_fuwuxieyi);
        btnYinsi = (TextView) findViewById(R.id.btn_yinsi);
        companyName = (TextView) findViewById(R.id.company_name);
        btnFuwuxieyi.setOnClickListener(this);
        btnYinsi.setOnClickListener(this);
        String appVersion = PackageUtils.getVersionName(getApplicationContext());
        if (!TextUtils.isEmpty(appVersion)) {
            tvVersion.setText("v" + appVersion);
        } else {
            tvVersion.setText("");
        }
    }

    @Override
    public int getLayoutViewId() {
        return R.layout.activity_about;
    }

    private View getHeadEmptyView() {
        return (View) findViewById(R.id.head_empty_view);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btn_fuwuxieyi:
                intent = new Intent(this, CustomServicerActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
                break;
            case R.id.btn_yinsi:
                intent = new Intent(this, CustomServicerActivity.class);
                intent.putExtra("type", 2);
                startActivity(intent);
                break;
            case R.id.iv_close:
                if (AntiShakeUtils.isInvalidClick(view)) {
                    return;
                }
                finish();
                break;
        }
    }
}
