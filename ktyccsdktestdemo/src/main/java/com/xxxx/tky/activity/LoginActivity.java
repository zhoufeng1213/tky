package com.xxxx.tky.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.flyco.roundview.RoundTextView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xw.repo.XEditText;
import com.xxxx.cc.base.activity.BaseHttpRequestActivity;
import com.xxxx.cc.callback.LoginCallBack;
import com.xxxx.cc.global.Constans;
import com.xxxx.cc.global.HttpRequest;
import com.xxxx.cc.global.KtyCcNetUtil;
import com.xxxx.cc.global.KtyCcOptionsUtil;
import com.xxxx.cc.model.BaseBean;
import com.xxxx.cc.util.SharedPreferencesUtil;
import com.xxxx.cc.util.ToastUtil;
import com.xxxx.tky.BuildConfig;
import com.xxxx.tky.R;
import com.xxxx.tky.contant.Contant;
import com.xxxx.tky.model.ContactUserName;
import com.xxxx.tky.model.VersionBean;
import com.xxxx.tky.util.AntiShakeUtils;
import com.xxxx.tky.util.FileUtil;
import com.xxxx.tky.util.KeyBoardUtil;
import com.xxxx.tky.util.MobilePhoneUtil;
import com.xxxx.tky.util.UpdateUtils;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import constacne.UiType;
import io.reactivex.functions.Consumer;
import listener.UpdateDownloadListener;
import model.UiConfig;
import model.UpdateConfig;
import update.UpdateAppUtils;

/**
 * @author zhoufeng
 * @date 2020/2/1
 * @moduleName
 */
public class LoginActivity extends BaseHttpRequestActivity {


    @BindView(R.id.phone_num_edit)
    XEditText phoneNumEdit;
    @BindView(R.id.pwd_edit)
    XEditText pwdEdit;
    @BindView(R.id.login_button)
    TextView loginButton;
    @BindView(R.id.home_et_host)
    EditText homeEtHost;
    @BindView(R.id.home_tv_switch_url_host)
    TextView homeTvSwitchUrlHost;

    private RxPermissions rxPermissions;

    @Override
    public int getLayoutViewId() {
        return R.layout.activity_login;
    }

    @Override
    public Toolbar getToolbar() {
        return null;
    }

    @Override
    public String getToolBarTitle() {
        return null;
    }

    @Override
    public void initViewBeforeSetContentView() {
        super.initViewBeforeSetContentView();
        setTheme(R.style.AppTheme);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        rxPermissions = new RxPermissions(this);
        UpdateAppUtils.getInstance().deleteInstalledApk();
        String phoneNum = SharedPreferencesUtil.getValue(mContext, Contant.LOGIN_SAVE_TAG);
        String pwd = SharedPreferencesUtil.getValue(mContext, Contant.LOGIN_PWD_SAVE_TAG);
        if (!TextUtils.isEmpty(phoneNum)) {
            phoneNumEdit.setText(phoneNum);
        }
        if (!TextUtils.isEmpty(pwd)) {
            pwdEdit.setText(pwd);
        }

        //先检测更新
        baseGetPresenter.presenterBusiness(
                HttpRequest.Version.checkVersion + BuildConfig.VERSION_NAME, false);

//        if (!TextUtils.isEmpty(phoneNum) && !TextUtils.isEmpty(pwd)) {
//            login();
//        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AntiShakeUtils.isInvalidClick(view)) {
                    return;
                }
                login();
            }
        });
    }

    private void login() {
        if (!TextUtils.isEmpty(phoneNumEdit.getText().toString().trim()) &&
                !TextUtils.isEmpty(pwdEdit.getText().toString().trim())) {

            rxPermissions.request(
                    Manifest.permission.CALL_PHONE,
                    Manifest.permission.READ_CONTACTS
            ).subscribe(new Consumer<Boolean>() {
                @Override
                public void accept(Boolean aBoolean) throws Exception {
                    if (aBoolean) {
                        showDialog();
                        KtyCcNetUtil.login(mContext, phoneNumEdit.getText().toString().trim(),
                                pwdEdit.getText().toString().trim(),
                                new LoginCallBack() {
                                    @Override
                                    public void onSuccess(int code, String message) {

                                        SharedPreferencesUtil.save(mContext, Contant.LOGIN_SAVE_TAG, phoneNumEdit.getText().toString().trim());
                                        SharedPreferencesUtil.save(mContext, Contant.LOGIN_PWD_SAVE_TAG,
                                                pwdEdit.getText().toString().trim());
//                                                dismissDialog();
                                        if (Contant.contactUserNameList == null) {
                                            List<ContactUserName> contactUserNameList = MobilePhoneUtil.getMobilePhoneList(mContext);
                                            //根据字母排序
                                            Collections.sort(contactUserNameList, new Comparator<ContactUserName>() {
                                                @Override
                                                public int compare(ContactUserName o1, ContactUserName o2) {
                                                    return o1.getDisplayNameSpelling().compareTo(o2.getDisplayNameSpelling());
                                                }
                                            });
                                            Contant.contactUserNameList = contactUserNameList;
                                        }
                                        KeyBoardUtil.hideKeyboard(mContext, pwdEdit);
                                        Toast.makeText(mContext, "登录成功", Toast.LENGTH_SHORT).show();
                                        startActivity(HomeActivity.class);
                                        finish();
                                    }

                                    @Override
                                    public void onFailed(int code, String message) {
                                        dismissDialog();
//                                                Toast.makeText(mContext, TextUtils.isEmpty(message) ? "登录失败，原因无" : message, Toast.LENGTH_SHORT).show();
                                        Toast.makeText(mContext, "密码错误", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        ToastUtil.showToast(mContext, "请允许该权限，否则无法使用");
                    }
                }
            });

        } else {
            ToastUtil.showToast(mContext, "请输入用户名和密码");
        }
    }


    @OnClick(R.id.home_tv_switch_url_host)
    public void changeUrl(View view) {
        if (AntiShakeUtils.isInvalidClick(view)) {
            return;
        }

        KtyCcOptionsUtil.switchHostOption(mContext, homeEtHost.getText().toString().trim());
    }

    @Override
    public void dealHttpRequestFail(String moduleName, BaseBean result) {
        if (!TextUtils.isEmpty(phoneNumEdit.getText().toString().trim()) && !TextUtils.isEmpty(pwdEdit.getText().toString().trim())) {
            login();
        }
    }

    private boolean isStartInstallPermissionSettingActivity = false;
    private boolean isDownFinish;
    private boolean isUpdateing;


    @Override
    public void dealHttpRequestResult(String moduleName, BaseBean result, String response) {
        if (result.getData() != null) {
            VersionBean versionBean = JSON.parseObject(result.getData().toString(), VersionBean.class);
            if (versionBean != null && versionBean.isNeedUpdate()) {
                //显示diaolog
                UpdateConfig updateConfig = new UpdateConfig();
                updateConfig.setCheckWifi(false);
                updateConfig.setNeedCheckMd5(false);
                updateConfig.setForce(versionBean.isNeedForceUpdate());
//                updateConfig.setForce(true);
                updateConfig.setNotifyImgRes(R.mipmap.ic_launcher);
                updateConfig.setApkSaveName(BuildConfig.VERSION_NAME);

                UiConfig uiConfig = new UiConfig();
                uiConfig.setUiType(UiType.PLENTIFUL);
                uiConfig.setDownloadingToastText(getString(R.string.apk_updating));
                UpdateAppUtils
                        .getInstance()
                        .apkUrl(versionBean.getDownloadUrl())
                        .updateTitle(getString(R.string.find_new_version) + "(" + versionBean.getNewVersion() + ")")
                        .updateContent(versionBean.getComment())
                        .uiConfig(uiConfig)
                        .updateConfig(updateConfig)
                        .setUpdateDownloadListener(new UpdateDownloadListener() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onDownload(int progress) {
                            }

                            @Override
                            public void onFinish() {
                                isDownFinish = true;
                                isUpdateing = false;
                                installProcess();
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        })
                        .update();
            } else {
                if (!TextUtils.isEmpty(phoneNumEdit.getText().toString().trim()) && !TextUtils.isEmpty(pwdEdit.getText().toString().trim())) {
                    login();
                }
            }
        } else {
            if (!TextUtils.isEmpty(phoneNumEdit.getText().toString().trim()) && !TextUtils.isEmpty(pwdEdit.getText().toString().trim())) {
                login();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (isStartInstallPermissionSettingActivity && isDownFinish) {
            installProcess();
            isStartInstallPermissionSettingActivity = false;
            isDownFinish = false;
        }
    }


    //安装应用的流程
    private void installProcess() {
        boolean haveInstallPermission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //先获取是否有安装未知来源应用的权限
            haveInstallPermission = getPackageManager().canRequestPackageInstalls();
            if (!haveInstallPermission) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    isStartInstallPermissionSettingActivity = true;
                    startInstallPermissionSettingActivity();
                }
                return;
            }
        }
        isStartInstallPermissionSettingActivity = false;
        //有权限，开始安装应用程序
        String savePath = FileUtil.getSavePath(mContext);
        if (!TextUtils.isEmpty(savePath)) {
            UpdateUtils.
                    openApk(mContext, savePath + File.separator + Constans.APK_NAME, BuildConfig.APPLICATION_ID);

        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity() {
        Uri packageURI = Uri.parse("package:" + getPackageName());
        //注意这个是8.0新API
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
        startActivityForResult(intent, 10086);
    }


    @Override
    protected void onPause() {
        super.onPause();
        dismissDialog();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    assert v != null;
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    /** here */
                    v.clearFocus();
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return getWindow().superDispatchTouchEvent(ev) || onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }


}
