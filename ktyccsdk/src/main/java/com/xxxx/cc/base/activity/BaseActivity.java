package com.xxxx.cc.base.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.gyf.barlibrary.ImmersionBar;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.xxxx.cc.R;
import com.xxxx.cc.base.widget.LoadingDialog;
import com.xxxx.cc.util.ToastUtil;

import butterknife.ButterKnife;


/**
 * Created by zhoufeng on 15/4/2.
 */
public abstract class BaseActivity extends RxAppCompatActivity {

    public Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewBeforeSetContentView();
        setContentView(getLayoutViewId());
        ButterKnife.bind(this);
        mContext = this;
        initToolbar();
        if(isAddImmersionBar()){
            ImmersionBar.with(this).statusBarDarkFont(true).barColor(R.color.white).fitsSystemWindows(true)
                    .init();
        }
        initView(savedInstanceState);
    }

    public boolean isAddImmersionBar(){
        return true;
    }


    public void initViewBeforeSetContentView() {

    }

    public abstract int getLayoutViewId();

    public  Toolbar getToolbar(){
        return  null;
    }

    public  String getToolBarTitle(){
        return "";
    }

    /**
     * 初始化Toolbar
     */
    public void initToolbar() {
        Toolbar mToolbar = getToolbar();
        if (mToolbar != null) {
            mToolbar.setTitle(getToolBarTitle());
            mToolbar.setTitleTextColor(getResources().getColor(R.color.black));
            setSupportActionBar(mToolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setHomeButtonEnabled(true);
                actionBar.setDisplayHomeAsUpEnabled(true);
//                actionBar.setHomeAsUpIndicator(R.drawable.pub_back_hei);
//                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }

    }


    public void initView(Bundle savedInstanceState) {

    }


    /**
     * 选项菜单
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                colseActivity();
                return true;
            default:
                break;
        }
        return false;
    }


    public void colseActivity() {
        finish();
    }


    private LoadingDialog loadingDialog;

    public void showDialog() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
        }
        loadingDialog.setMessage(true);
        loadingDialog.show();
    }

    public void showDialog(String title) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
        }
        loadingDialog.setMessage(title);
        loadingDialog.show();
    }

    public void showDialog(String title, boolean isCancel) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this, isCancel);
        }
        loadingDialog.setMessage(title);
        loadingDialog.show();
    }

    public void dismissDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
    }

    public void showToast(String msg) {
        ToastUtil.showToast(getApplicationContext(), msg);
    }

    public void startActivity(Class className, Object... params) {
        Intent intent = new Intent(mContext, className);
        if (params != null) {
            if (params.length > 1 && params.length % 2 == 0) {
                for (int i = 0; i < params.length; i += 2) {
                    if (params[i + 1] instanceof Integer) {
                        intent.putExtra(String.valueOf(params[i]), Integer.valueOf(String.valueOf(params[i + 1])));
                    } else {
                        intent.putExtra(String.valueOf(params[i]), String.valueOf(params[i + 1]));
                    }
                }
            }
        }
        startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        dismissDialog();
        if(isAddImmersionBar()){
            ImmersionBar.with(this).destroy();
        }
        super.onDestroy();
    }

}
