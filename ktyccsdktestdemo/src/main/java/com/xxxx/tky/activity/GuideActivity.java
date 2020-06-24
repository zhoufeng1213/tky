package com.xxxx.tky.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
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
    public static final String TITLE_NAME="titleName";
    public static final String WEB_URL="webUrl";
    @Override
    public int getLayoutViewId() {
        return R.layout.activity_guide;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        String titleName=getIntent().getStringExtra(TITLE_NAME);
        if(!TextUtils.isEmpty(titleName)){
            tvTitle.setText(titleName);
        }
        WebSettings webSettings = wv.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        wv.setWebViewClient(new WebViewClient());
        String webUrl=getIntent().getStringExtra(WEB_URL);
        if(!TextUtils.isEmpty(webUrl)){
            wv.loadUrl(webUrl);
        }
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("mailto:")) {
                    //Handle mail Urls
                    startActivity(new Intent(Intent.ACTION_SENDTO, Uri.parse(url)));
                } else if (url.startsWith("tel:")) {
                    //Handle telephony Urls
                    startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse(url)));
                } else {
                    view.loadUrl(url);
                }
                return true;
            }

            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                final Uri uri = request.getUrl();
                if (uri.toString().startsWith("mailto:")) {
                    //Handle mail Urls
                    startActivity(new Intent(Intent.ACTION_SENDTO, uri));
                } else if (uri.toString().startsWith("tel:")) {
                    //Handle telephony Urls
                    startActivity(new Intent(Intent.ACTION_DIAL, uri));
                } else {
                    //Handle Web Urls
                    view.loadUrl(uri.toString());
                }
                return true;
            }
        });

        wv.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                setTitle(title);
            }
        });

        //"https://www.ketianyun.com/callcenter/tky/app/help/help.html"
    }

    @OnClick(R.id.iv_close)
    public void back(View view) {
        if (AntiShakeUtils.isInvalidClick(view)) {
            return;
        }
        finish();
    }
}
