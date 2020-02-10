package com.xxxx.tky.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xxxx.cc.callback.LoginCallBack;
import com.xxxx.cc.global.KtyCcNetUtil;
import com.xxxx.cc.global.KtyCcOptionsUtil;
import com.xxxx.cc.global.KtyCcSdkTool;
import com.xxxx.tky.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.home_et_account)
    EditText homeEtAccount;
    @BindView(R.id.home_et_password)
    EditText homeEtPassword;
    @BindView(R.id.home_tv_login_status)
    TextView homeTvLoginStatus;
    @BindView(R.id.home_et_contact_name)
    EditText homeEtContactName;
    @BindView(R.id.home_et_phone)
    EditText homeEtPhone;
    @BindView(R.id.home_tv_login_in)
    TextView homeTvLoginIn;
    @BindView(R.id.home_tv_login_out)
    TextView homeTvLoginOut;
    @BindView(R.id.home_tv_call)
    TextView homeTvCall;
    @BindView(R.id.home_tv_history)
    TextView homeTvHistory;
    @BindView(R.id.home_tv_history_fragment)
    TextView homeTvHistoryFragment;
    @BindView(R.id.home_et_host)
    EditText homeEtHost;
    @BindView(R.id.home_tv_switch_url_host)
    TextView homeTvSwitchUrlHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.home_tv_login_in)
    public void login(){
        if(TextUtils.isEmpty(homeEtAccount.getText().toString().trim())){
            Toast.makeText(MainActivity.this,"请输入账号",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(homeEtPassword.getText().toString().trim())){
            Toast.makeText(MainActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
            return;
        }
        KtyCcNetUtil.login(this, homeEtAccount.getText().toString().trim(),
                homeEtPassword.getText().toString().trim(),
                new LoginCallBack() {
                    @Override
                    public void onSuccess(int code, String message) {
                        Toast.makeText(MainActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(int code, String message) {
                        Toast.makeText(MainActivity.this,TextUtils.isEmpty(message)?"登录失败，原因无":message,Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @OnClick(R.id.home_tv_login_out)
    public void exit(){
        KtyCcSdkTool.getInstance().unRegister(this);
        Toast.makeText(MainActivity.this,"登出成功",Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.home_tv_call)
    public void call(){
        if(TextUtils.isEmpty(homeEtPhone.getText().toString().trim())){
            Toast.makeText(MainActivity.this,"请输入号码",Toast.LENGTH_SHORT).show();
            return;
        }
//        if(TextUtils.isEmpty(homeEtContactName.getText().toString().trim())){
//            Toast.makeText(MainActivity.this,"请输入姓名",Toast.LENGTH_SHORT).show();
//            return;
//        }
        KtyCcSdkTool.getInstance().callPhone(this,homeEtPhone.getText().toString().trim(),
                homeEtContactName.getText().toString().trim(),
                "http://img.mp.itc.cn/upload/20161020/e81dd51cb7ab4695bde800d1b001ba14_th.jpeg"
                );
    }

    @OnClick(R.id.home_tv_history)
    public void goToHistory(){
        KtyCcSdkTool.goToHistoryActivity(this);
    }

    @OnClick(R.id.home_tv_history_fragment)
    public void goToHistoryFragment(){
        startActivity(new Intent(this,CallHistoryFragmentActivity.class));
    }

    @OnClick(R.id.home_tv_switch_url_host)
    public void changeUrl(){
        KtyCcOptionsUtil.switchHostOption(this,homeEtHost.getText().toString().trim());
    }
}
