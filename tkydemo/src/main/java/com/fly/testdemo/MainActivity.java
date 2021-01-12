package com.fly.testdemo;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.xxxx.cc.callback.CallPhoneBack;
import com.xxxx.cc.callback.LoginCallBack;
import com.xxxx.cc.global.SdkTool;
import com.xxxx.cc.model.UserBean;
import com.xxxx.cc.util.LogUtils;
import com.xxxx.cc.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

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
    @BindView(R.id.home_extendedData)
    EditText homeExtendedData;
    @BindView(R.id.home_tv_login_in)
    TextView homeTvLoginIn;
    @BindView(R.id.home_tv_login_out)
    TextView homeTvLoginOut;
    @BindView(R.id.home_tv_call)
    TextView homeTvCall;
    @BindView(R.id.home_hands_free)
    TextView homeHandsFree;
    private String[] needPermissions = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CALL_PHONE};
    private List<String> permissionList = new ArrayList<>();
    private  boolean isHandsFree;
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
                    public void onSuccess(int code, String message, UserBean userBean) {
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

    @OnClick(R.id.home_hook)
    public void hook() {
        SdkTool.hookCall();
        Toast.makeText(MainActivity.this, "挂断", Toast.LENGTH_SHORT).show();
    }
    @OnClick(R.id.home_hands_free)
    public void handsFree() {
        isHandsFree=!isHandsFree;
        SdkTool.switchAudio(this,isHandsFree);
        if(isHandsFree){
            homeHandsFree.setText("关免提");
        }else {
            homeHandsFree.setText("开免提");
        }

    }
    @OnClick(R.id.home_tv_call)
    public void call() {
        if (TextUtils.isEmpty(homeEtPhone.getText().toString().trim())) {
            Toast.makeText(MainActivity.this, "请输入号码", Toast.LENGTH_SHORT).show();
            return;
        }
        permissionList.clear();
        for (String needPermission : needPermissions) {
            if (ContextCompat.checkSelfPermission(this, needPermission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(needPermission);
            }
        }
        if (permissionList.size() > 0) {
            ActivityCompat.requestPermissions(this, needPermissions, 996);
        } else {
            callPhone();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        LogUtils.e("requestPermission2");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasPermissionDismiss = false;
        if (996 == requestCode) {
            for (int grantResult : grantResults) {
                if (grantResult == -1) {
                    hasPermissionDismiss = true;
                    break;
                }
            }
            //如果有权限没有被允许
            if (hasPermissionDismiss) {
                ToastUtil.showToast(this, "权限被禁止，请允许");
                finish();
            } else {
                callPhone();
            }
        }

    }

    private void callPhone() {
        SdkTool.callPhone(this, homeEtPhone.getText().toString().trim(),
                homeEtContactName.getText().toString().trim(), "测试测试",
                new CallPhoneBack() {
                    @Override
                    public void onSuccess(String callId) {
                        LogUtils.e("SdkToolCallback", "callid:" + callId);
                    }

                    @Override
                    public void onFailed(String message) {
                        LogUtils.e("SdkToolCallback", "拨打电话失败:" + message);
                    }

                    @Override
                    public void watchPhoneStatus(int status) {
                        LogUtils.e("SdkToolCallback", "当前拨打电话的状态:" + status);
                    }
                }
        );
    }
}
