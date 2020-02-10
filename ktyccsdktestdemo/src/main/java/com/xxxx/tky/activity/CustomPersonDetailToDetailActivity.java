package com.xxxx.tky.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fly.sweet.dialog.SweetAlertDialog;
import com.google.gson.Gson;
import com.xxxx.cc.base.activity.BaseActivity;
import com.xxxx.cc.base.activity.BaseHttpRequestActivity;
import com.xxxx.cc.global.HttpRequest;
import com.xxxx.cc.global.KtyCcSdkTool;
import com.xxxx.cc.model.BaseBean;
import com.xxxx.cc.model.HistoryRequestBean;
import com.xxxx.cc.model.QueryCustomPersonBean;
import com.xxxx.cc.model.UserBean;
import com.xxxx.cc.util.SharedPreferencesUtil;
import com.xxxx.cc.util.db.DbUtil;
import com.xxxx.tky.R;
import com.xxxx.tky.model.UpdateUserBean;
import com.xxxx.tky.util.AntiShakeUtils;
import com.xxxx.tky.util.KeyBoardUtil;
import com.xxxx.tky.util.TextUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xxxx.cc.global.Constans.COMMON_PAGE_SIZE;
import static com.xxxx.cc.global.Constans.USERBEAN_SAVE_TAG;
import static org.linphone.mediastream.MediastreamerAndroidContext.getContext;

/**
 * @author zhoufeng
 * @date 2020/2/7
 * @moduleName
 */
public class CustomPersonDetailToDetailActivity extends BaseHttpRequestActivity {


    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.delete_btn)
    ImageView deleteBtn;
    @BindView(R.id.user_name)
    EditText userName;
    @BindView(R.id.name_detail_image)
    ImageView nameDetailImage;
    @BindView(R.id.phone_num)
    TextView phoneNum;
    @BindView(R.id.phone_detail_image)
    ImageView phoneDetailImage;
    @BindView(R.id.user_beizhu)
    EditText userBeizhu;
    private QueryCustomPersonBean queryCustomPersonBean;
    private UserBean cacheUserBean;

    @Override
    public int getLayoutViewId() {
        return R.layout.activity_custom_person_detail_to_detail_new;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        tvTitle.setText("客户详情");
        try {
            Object objectBean = SharedPreferencesUtil.getObjectBean(mContext, USERBEAN_SAVE_TAG, UserBean.class);
            if (objectBean != null) {
                cacheUserBean = (UserBean) objectBean;
            }

            String data = getIntent().getStringExtra("data");
            if (!TextUtils.isEmpty(data)) {

                queryCustomPersonBean = JSON.parseObject(data, QueryCustomPersonBean.class);
                if (queryCustomPersonBean != null) {
                    if (!TextUtils.isEmpty(queryCustomPersonBean.getName())) {
                        userName.setText(queryCustomPersonBean.getName());
                        userName.setSelection(queryCustomPersonBean.getName().length());
                    }
                    phoneNum.setText(queryCustomPersonBean.getRealMobileNumber());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }



    @OnClick(R.id.iv_close)
    public void back(View view) {
        if (AntiShakeUtils.isInvalidClick(view)) {
            return;
        }
        finish();
    }

    @OnClick(R.id.name_layout)
    public void fourceName(View view) {
        if (AntiShakeUtils.isInvalidClick(view)) {
            return;
        }
        userName.setFocusable(true);
        userName.setFocusableInTouchMode(true);
        userName.postDelayed(new Runnable() {
            @Override
            public void run() {
                userName.requestFocus();
                InputMethodManager manager = ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE));
                if (manager != null) {
                    manager.showSoftInput(userName, 0);
                }
            }
        },100);
    }


    @OnClick(R.id.save_btn)
    public void save(View view) {
        if (AntiShakeUtils.isInvalidClick(view)) {
            return;
        }
        if(cacheUserBean != null && queryCustomPersonBean != null){
            basePostPresenter.presenterBusinessByHeader(
                    HttpRequest.Contant.update+queryCustomPersonBean.getId(),
                    "token",cacheUserBean.getToken());

        }

    }

    @Override
    public void dealHttpRequestResult(String moduleName, BaseBean result, String response) {
        if((HttpRequest.Contant.update+queryCustomPersonBean.getId()).equals(moduleName)){
            if(result.isOk()){
                showToast("修改成功");
                //把本地的数据库更新一下
                queryCustomPersonBean.setName(userName.getText().toString().trim());
                queryCustomPersonBean.setLetters(TextUtil.getNameFirstChar(userName.getText().toString().trim()));
                queryCustomPersonBean.setDisplayNameSpelling(TextUtil.getNameToPinyin(
                        userName.getText().toString().trim()));
                DbUtil.updateQueryCustomPersonBeanById(queryCustomPersonBean);
                finish();
            }else{
                showToast("修改失败");
            }
        }else if((HttpRequest.Contant.back+queryCustomPersonBean.getId()).equals(moduleName)){
            if(result.isOk()){
                showToast("放弃成功");
                //把本地的数据库更新一下
                queryCustomPersonBean.setDatastatus(false);
                DbUtil.updateQueryCustomPersonBeanById(queryCustomPersonBean);
                finish();
            }else{
                showToast("放弃失败");
            }
        }
    }

    @Override
    public JSONObject getHttpRequestParams(String moduleName) {
        JSONObject jsonObject = new JSONObject();
        if ((HttpRequest.Contant.update+queryCustomPersonBean.getId()).equals(moduleName)) {
            UpdateUserBean requestBean = new UpdateUserBean();
            requestBean.setName(userName.getText().toString());
            jsonObject = JSONObject.parseObject(new Gson().toJson(requestBean));
        }
        return jsonObject;
    }



    @OnClick(R.id.delete_btn)
    public void fangQi(View view) {
        if (AntiShakeUtils.isInvalidClick(view)) {
            return;
        }
        //掉http，然后修改本地数据库
        new SweetAlertDialog(this,SweetAlertDialog.WARNING_TYPE)
                .setTitleText("是否放弃该客户?")
                .setConfirmText("是")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                        if(cacheUserBean != null && queryCustomPersonBean != null){
                            basePostPresenter.presenterBusinessByHeader(
                                    HttpRequest.Contant.back+queryCustomPersonBean.getId(),
                                    "token",cacheUserBean.getToken());

                        }
                    }
                })
                .setCancelText("否")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                    }
                })
                .show();
    }
}
