package com.xxxx.tky.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.xw.repo.XEditText;
import com.xxxx.cc.base.activity.BaseHttpRequestActivity;
import com.xxxx.cc.global.HttpRequest;
import com.xxxx.cc.model.BaseBean;
import com.xxxx.cc.model.RegisterRequestBean;
import com.xxxx.cc.model.SmsBean;
import com.xxxx.cc.model.UpdateRequestBean;
import com.xxxx.cc.model.UserBean;
import com.xxxx.cc.util.SharedPreferencesUtil;
import com.xxxx.cc.util.ToastUtil;
import com.xxxx.tky.R;
import com.xxxx.tky.util.AntiShakeUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

import static com.xxxx.cc.global.Constans.USERBEAN_SAVE_TAG;
import static com.xxxx.cc.global.HttpRequest.Contant.updateLastCallTime;

public class MineDetailActivity extends BaseHttpRequestActivity {
    @BindView(R.id.detail_nick_name)
    TextView nickNameTx;
    @BindView(R.id.detail_user_name)
    TextView userNameTx;
    @BindView(R.id.detail_email_name)
    TextView emailTx;
    @BindView(R.id.detail_telphone)
    XEditText telPhoneEdit;
    private UserBean cacheUserBean;
    private String tempMobile="";
    String newTelPhone="";
    @Override
    public int getLayoutViewId() {
        return R.layout.activity_details;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        Object objectBean = SharedPreferencesUtil.getObjectBean(mContext, USERBEAN_SAVE_TAG, UserBean.class);
        if (objectBean != null) {
            cacheUserBean = (UserBean) objectBean;
            if (!TextUtils.isEmpty(cacheUserBean.getUname())) {
                nickNameTx.setText(cacheUserBean.getUname());
            }
            if(!TextUtils.isEmpty(cacheUserBean.getEmail())){
                emailTx.setText(cacheUserBean.getEmail());
            }
            if(!TextUtils.isEmpty(cacheUserBean.getMobile())){
                tempMobile=cacheUserBean.getMobile();
                telPhoneEdit.setText(cacheUserBean.getMobile());
            }
            if(!TextUtils.isEmpty(cacheUserBean.getUsername())){
                userNameTx.setText(cacheUserBean.getUsername());
            }
        }
    }
    @OnClick(R.id.save)
    public void save(View view) {
        if (AntiShakeUtils.isInvalidClick(view)) {
            return;
        }
        if(tempMobile.equals(telPhoneEdit.getTextEx())){
            onBackPressed();
            return;
        }
        if(!TextUtils.isEmpty(telPhoneEdit.getTextEx().trim())&&!isMobile(telPhoneEdit.getTextEx())){
            showToast("请输入正确的手机号");
            return;
        }
        basePostPresenter.presenterBusinessByHeader(
                HttpRequest.Update.updateUrl,
                "token", cacheUserBean.getToken(),
                "Content-Type", "application/json"
        );

    }
    public boolean isMobile(String mobiles) {

        Pattern p = Pattern.compile("^(1)\\d{10}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
    @OnClick(R.id.iv_close)
    public void close(View view) {
        if (AntiShakeUtils.isInvalidClick(view)) {
            return;
        }
        onBackPressed();
    }
    public JSONObject getHttpRequestParams(String moduleName) {
        JSONObject jsonObject = new JSONObject();
        if ( HttpRequest.Update.updateUrl.equals(moduleName)) {

            UpdateRequestBean requestBean = new UpdateRequestBean();
            requestBean.setMobile(telPhoneEdit.getTextEx());
            newTelPhone=telPhoneEdit.getTextEx();
            requestBean.setUname(nickNameTx.getText().toString());

            jsonObject = JSONObject.parseObject(new Gson().toJson(requestBean));
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
        if ( HttpRequest.Update.updateUrl.equals(moduleName)) {
            if(result.getCode()==0){
                ToastUtil.showToast(this,"修改成功");
                cacheUserBean.setMobile(newTelPhone);
                SharedPreferencesUtil.saveObjectBean(MineDetailActivity.this, cacheUserBean, USERBEAN_SAVE_TAG);
                finish();
            }else {
                ToastUtil.showToast(this,result.getMessage()+" ");
            }
        }
    }
}
