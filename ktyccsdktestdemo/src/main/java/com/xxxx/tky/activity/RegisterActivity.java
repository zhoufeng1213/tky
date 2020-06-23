package com.xxxx.tky.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.alibaba.fastjson.JSONObject;
import com.fly.sweet.dialog.SweetAlertDialog;
import com.flyco.roundview.RoundTextView;
import com.google.gson.Gson;
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
import com.xxxx.cc.model.HistoryRequestBean;
import com.xxxx.cc.model.RegisterRequestBean;
import com.xxxx.cc.model.SmsBean;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import constacne.UiType;
import io.reactivex.functions.Consumer;
import listener.UpdateDownloadListener;
import model.UiConfig;
import model.UpdateConfig;
import update.UpdateAppUtils;

import static com.xxxx.cc.global.Constans.COMMON_PAGE_SIZE;

/**
 * @author zhoufeng
 * @date 2020/2/1
 * @moduleName
 */
public class RegisterActivity extends BaseHttpRequestActivity {


    @BindView(R.id.phone_num_edit)
    XEditText phoneNumEdit;
    @BindView(R.id.message_edit)
    XEditText messageEdit;
    @BindView(R.id.pwd_edit)
    XEditText pwdEdit;
    @BindView(R.id.pwd_sure_edit)
    XEditText pwdSureEdit;
    @BindView(R.id.nickName_edit)
    XEditText nickNameEdit;
    @BindView(R.id.register_button)
    TextView registerButton;

    @BindView(R.id.send_message)
    TextView sendMessageTx;

    private String code;
    private String pwd;
    private String phoneNum;
    private RxPermissions rxPermissions;
    private String nickName;
    @Override
    public int getLayoutViewId() {
        return R.layout.activity_register;
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
//        setTheme(R.style.AppTheme);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);

        rxPermissions = new RxPermissions(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AntiShakeUtils.isInvalidClick(view)) {
                    return;
                }
                register();
            }
        });

    }
    private String messagecode;
    private void register() {
        phoneNum=phoneNumEdit.getTextEx();
        pwd=pwdEdit.getTextEx();
        nickName=nickNameEdit.getTextEx();
        if ( TextUtils.isEmpty(phoneNum) ){
            ToastUtil.showToast(this,"请输入手机号");
            return;
        } else if (!isMobile(phoneNum)) {
            ToastUtil.showToast(this,"请输入正确的手机号");
            return;
        } else if (!hasSendMsg) {
            ToastUtil.showToast(this,"请获取验证码");
            return;
        }else if(TextUtils.isEmpty(messageEdit.getTextEx())){
            ToastUtil.showToast(this,"请输入验证码");
            return;
        }else if(TextUtils.isEmpty(messagecode)){
            ToastUtil.showToast(this,"未收到服务器验证码请重试");
            return;
        }else if(!messagecode.equals(messageEdit.getTextEx())){
            ToastUtil.showToast(this,"验证码不一致");
            return;
        }else if(TextUtils.isEmpty(pwd)||pwd.length()<6||pwd.length()>20){
            ToastUtil.showToast(this,"请输入6-20位密码");
            return;
        }else if(!pwd.equals(pwdSureEdit.getTextEx())){
            ToastUtil.showToast(this,"两次输入密码不一致");
            return;
        }else if(TextUtils.isEmpty(nickName)||nickName.length()<2){
            ToastUtil.showToast(this,"请输入两位以上的昵称");
            return;
        }else {
            basePostPresenter.presenterBusiness(
                    HttpRequest.Register.registerUrl, false);
        }

    }
    private boolean hasSendMsg;
    MyCountDownTimer myCountDownTimer;
    @OnClick(R.id.send_message)
    public void checkPhoneNum(View view) {
        if (AntiShakeUtils.isInvalidClick(view)) {
            return;
        }
        phoneNum=phoneNumEdit.getTextEx().trim();
        if ( TextUtils.isEmpty(phoneNum) ){
            ToastUtil.showToast(this,"请输入手机号");
            return;
        } else if (!isMobile(phoneNum)) {
            ToastUtil.showToast(this,"请输入正确的手机号");
            return;
        }else{
            if(myCountDownTimer==null){
                myCountDownTimer = new MyCountDownTimer(60000,1000);
            }else {
                myCountDownTimer.cancel();
            }

            myCountDownTimer.start();
            basePostPresenter.presenterBusiness(
                    HttpRequest.Register.smsUrl, false);
            hasSendMsg=true;
        }


    }
    @OnClick(R.id.close_image)
    public void close(View view) {
        if (AntiShakeUtils.isInvalidClick(view)) {
            return;
        }
     onBackPressed();

    }
    public boolean isMobile(String mobiles) {
        Pattern p = Pattern.compile("^(1)\\d{10}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public JSONObject getHttpRequestParams(String moduleName) {
        JSONObject jsonObject = new JSONObject();
        if (HttpRequest.Register.registerUrl.equals(moduleName)) {

            RegisterRequestBean requestBean = new RegisterRequestBean();
            requestBean.setName(nickName);
            requestBean.setPhone(phoneNum);
            requestBean.setPassword(pwd);
            requestBean.setCode(messagecode);
            jsonObject = JSONObject.parseObject(new Gson().toJson(requestBean));
        }else if(HttpRequest.Register.smsUrl.equals(moduleName)){
            SmsBean smsBean=new SmsBean();
            smsBean.setPhone(phoneNum);
            jsonObject = JSONObject.parseObject(new Gson().toJson(smsBean));
        }
        return jsonObject;
    }

    @Override
    public void dealHttpRequestFail(String moduleName, BaseBean result) {
        if(result!=null&&result.getMessage()!=null){
            ToastUtil.showToast(this,result.getMessage()+"");
        }

        result.getCode();
//        if (!TextUtils.isEmpty(phoneNumEdit.getText().toString().trim()) && !TextUtils.isEmpty(pwdEdit.getText().toString().trim())) {
//            login();
//        }
    }



    @Override
    public void dealHttpRequestResult(String moduleName, BaseBean result, String response) {
        if (HttpRequest.Register.registerUrl.equals(moduleName)) {

            if(result.getCode()==0){
                ToastUtil.showToast(this,"注册成功");
                Intent i=new Intent();
                i.putExtra("phone", phoneNum);
                setResult(RESULT_OK, i);
                finish();
            }else {
                ToastUtil.showToast(this,result.getMessage()+" ");
            }
        }else if(HttpRequest.Register.smsUrl.equals(moduleName)){
            if (result.getCode()==0&&result.getData() != null) {
                messagecode=result.getData().toString();
            }
        }

    }


    @Override
    protected void onResume() {
        super.onResume();

    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        if(myCountDownTimer!=null){
            myCountDownTimer.cancel();
        }
        super.onDestroy();
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
    private class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //计时过程
        @Override
        public void onTick(long l) {
            //防止计时过程中重复点击
            sendMessageTx.setClickable(false);
            sendMessageTx.setText(l/1000+"s");

        }

        //计时完毕的方法
        @Override
        public void onFinish() {
            //重新给Button设置文字
            sendMessageTx.setText("重新获取验证码");
            //设置可点击
            sendMessageTx.setClickable(true);
        }
    }

}
