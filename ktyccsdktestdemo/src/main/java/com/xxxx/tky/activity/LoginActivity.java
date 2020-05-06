package com.xxxx.tky.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.fly.sweet.dialog.SweetAlertDialog;
import com.flyco.roundview.RoundTextView;
import com.sdk.keepbackground.work.DaemonEnv;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xw.repo.XEditText;
import com.xxxx.cc.base.activity.BaseHttpRequestActivity;
import com.xxxx.cc.callback.LoginCallBack;
import com.xxxx.cc.global.Constans;
import com.xxxx.cc.global.HttpRequest;
import com.xxxx.cc.global.KtyCcNetUtil;
import com.xxxx.cc.global.KtyCcOptionsUtil;
import com.xxxx.cc.global.PackageUtils;
import com.xxxx.cc.model.BaseBean;
import com.xxxx.cc.util.LogUtils;
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
    @BindView(R.id.tv_app_version)
    TextView tvVersion;
@BindView(R.id.setting_image)
ImageView settingImage;

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
        //安装这个版本后的第一次需要清除一下请求的begin time
        boolean notClearBeginTime = SharedPreferencesUtil.getBoolean(mContext, Contant.CLEAR_REQUEST_BEGIN_TIME);
        if (!notClearBeginTime) {
            SharedPreferencesUtil.save(mContext, Constans.KTY_CC_BEGIN, "");
            SharedPreferencesUtil.save(mContext, Constans.KTY_CUSTOM_BEGIN, "");
            SharedPreferencesUtil.setBoolean(mContext, Contant.CLEAR_REQUEST_BEGIN_TIME, true);
        }
        rxPermissions = new RxPermissions(this);
        UpdateAppUtils.getInstance().deleteInstalledApk();
        String appVersion = PackageUtils.getVersionName(this);
        if (!TextUtils.isEmpty(appVersion)) {
            tvVersion.setText("v" + appVersion);
        } else {
            tvVersion.setText("");
        }
        String phoneNum = SharedPreferencesUtil.getValue(mContext, Contant.LOGIN_SAVE_TAG);
        String pwd = SharedPreferencesUtil.getValue(mContext, Contant.LOGIN_PWD_SAVE_TAG);
        if (!TextUtils.isEmpty(phoneNum)) {
            phoneNumEdit.setText(phoneNum);
        }
        if (!TextUtils.isEmpty(pwd)) {
            pwdEdit.setText(pwd);
        }
        if (!SharedPreferencesUtil.getBoolean(getApplicationContext(), Constans.AGREE_PROTOCOL_TAG)) {
            showAgreeDialog();
        } else {
            //判断是否有地址
            String address = SharedPreferencesUtil.getValue(mContext, Constans.TKY_CHANGE_URL);
            if(!TextUtils.isEmpty(address)){
                KtyCcOptionsUtil.switchHostOption(mContext, address);
            }
            //先检测更新
            baseGetPresenter.presenterBusiness(
                    HttpRequest.Version.checkVersion + BuildConfig.VERSION_NAME, false);
        }
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
                                        if (code == 45003)
                                            Toast.makeText(mContext, "密码错误", Toast.LENGTH_SHORT).show();
                                        else
                                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
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

    @OnClick(R.id.setting_image)
    public void settingImage(View view) {
        if (AntiShakeUtils.isInvalidClick(view)) {
            return;
        }
        //弹出输入网址
        showChangeUrlDialog();

    }

    private  SweetAlertDialog sweetAlertDialog;
    /**
     * 显示call自己
     */
    private void showChangeUrlDialog() {
        sweetAlertDialog = new SweetAlertDialog(mContext);
        View dialogView = LayoutInflater.from(mContext)
                .inflate(R.layout.dialog_login_change_url, null, false);
        sweetAlertDialog.setCustomView(dialogView);
        sweetAlertDialog.setCanceledOnTouchOutside(true);
        sweetAlertDialog.setCancelable(true);

        RoundTextView dialogConfirm = dialogView.findViewById(R.id.dialog_confirm);
        RoundTextView dialogCancel = dialogView.findViewById(R.id.dialog_cancel);

        EditText editText = dialogView.findViewById(R.id.phone_num_edit);
        String address = SharedPreferencesUtil.getValue(mContext, Constans.TKY_CHANGE_URL);
        if (!TextUtils.isEmpty(address)) {
            LogUtils.e("当前保存的请求地址：" + address);
            editText.setText(address);
        }else{
            editText.setText(Constans.BASE_URL);
        }

        dialogConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sweetAlertDialog != null) {
                    sweetAlertDialog.dismiss();
                }
                KtyCcOptionsUtil.switchHostOption(mContext, editText.getText().toString().trim());
                SharedPreferencesUtil.save(mContext,Constans.TKY_CHANGE_URL,editText.getText().toString().trim());
            }
        });

        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sweetAlertDialog != null) {
                    sweetAlertDialog.dismiss();
                }
            }
        });

        sweetAlertDialog.show();
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

    private void showAgreeDialog() {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(this);
        View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_agree_protocol, null, false);
        sweetAlertDialog.setCustomView(dialogView);
        sweetAlertDialog.setCancelable(false);
        TextView dialogContent = dialogView.findViewById(R.id.dialog_content);
        RoundTextView dialogConfirm = dialogView.findViewById(R.id.dialog_confirm);
        RoundTextView dialogCancel = dialogView.findViewById(R.id.dialog_cancel);


        String content = getString(R.string.agree_content);
        SpannableString spannableString = new SpannableString(content);
        int index1 = content.indexOf("《") + 1;
        int index2 = content.indexOf("》") + 1;
        int index3 = content.indexOf("《", index1) + 1;
        int index4 = content.indexOf("》", index2) + 1;

        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                if (AntiShakeUtils.isInvalidClick(view)) {
                    return;
                }
                Intent intent = new Intent(LoginActivity.this, CustomServicerActivity.class);
                intent.putExtra("type", 1);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setColor(getResources().getColor(R.color.c_2f9cf6));
                ds.setUnderlineText(false);
            }
        };
        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                if (AntiShakeUtils.isInvalidClick(view)) {
                    return;
                }
                Intent intent;
                intent = new Intent(LoginActivity.this, CustomServicerActivity.class);
                intent.putExtra("type", 2);
                startActivity(intent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setColor(getResources().getColor(R.color.c_2f9cf6));
                ds.setUnderlineText(false);
            }
        };

        spannableString.setSpan(clickableSpan1, index1 - 1, index2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(clickableSpan2, index3 - 1, index4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        dialogContent.setText(spannableString);
        dialogContent.setMovementMethod(LinkMovementMethod.getInstance());

        dialogConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesUtil.setBoolean(getApplicationContext(), Constans.AGREE_PROTOCOL_TAG, true);
                sweetAlertDialog.dismiss();
                baseGetPresenter.presenterBusiness(
                        HttpRequest.Version.checkVersion + BuildConfig.VERSION_NAME, false);
            }
        });
        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
        });
        sweetAlertDialog.show();
    }
}
