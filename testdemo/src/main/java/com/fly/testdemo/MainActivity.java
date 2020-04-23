package com.fly.testdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xxxx.cc.callback.CallPhoneBack;
import com.xxxx.cc.callback.LoginCallBack;
import com.xxxx.cc.global.SdkTool;

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
    public void login() {
        if (TextUtils.isEmpty(homeEtAccount.getText().toString().trim())) {
            Toast.makeText(MainActivity.this, "请输入账号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(homeEtPassword.getText().toString().trim())) {
            Toast.makeText(MainActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        SdkTool.login(this, homeEtAccount.getText().toString().trim(),
                homeEtPassword.getText().toString().trim(),
                new LoginCallBack() {
                    @Override
                    public void onSuccess(int code, String message) {
                        Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(int code, String message) {
                        Toast.makeText(MainActivity.this, TextUtils.isEmpty(message) ? "登录失败，原因无" : message, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @OnClick(R.id.home_tv_login_out)
    public void exit() {
        SdkTool.unRegister(this);
        Toast.makeText(MainActivity.this, "登出成功", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.home_tv_call)
    public void call() {
        if (TextUtils.isEmpty(homeEtPhone.getText().toString().trim())) {
            Toast.makeText(MainActivity.this, "请输入号码", Toast.LENGTH_SHORT).show();
            return;
        }
//        if(TextUtils.isEmpty(homeEtContactName.getText().toString().trim())){
//            Toast.makeText(MainActivity.this,"请输入姓名",Toast.LENGTH_SHORT).show();
//            return;
//        }
        SdkTool.callPhone(this, homeEtPhone.getText().toString().trim(),
                homeEtContactName.getText().toString().trim(), "",
                new CallPhoneBack() {
                    @Override
                    public void onSuccess(String callId) {
//                        LogUtils.e("SdkTool","callid:" + callId);
                    }

                    @Override
                    public void onFailed(String message) {
//                        LogUtils.e("SdkTool","拨打电话失败:");
                    }

                    @Override
                    public void watchPhoneStatus(int status) {
//                        LogUtils.e("当前拨打电话的状态:"+status );
                    }
                }
        );
    }

    @OnClick(R.id.home_tv_history)
    public void goToHistory() {
        SdkTool.goToHistoryActivity(this);
    }

    @OnClick(R.id.home_tv_history_fragment)
    public void goToHistoryFragment() {
//        startActivity(new Intent(this, CallHistoryFragmentActivity.class));
    }

    @OnClick(R.id.home_tv_switch_url_host)
    public void changeUrl() {
        SdkTool.switchHostOption(this, homeEtHost.getText().toString().trim());
    }
}
