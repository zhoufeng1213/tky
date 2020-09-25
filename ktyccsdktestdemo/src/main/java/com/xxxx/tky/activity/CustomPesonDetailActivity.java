package com.xxxx.tky.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.Gson;
import com.xxxx.cc.base.activity.BaseHttpRequestActivity;
import com.xxxx.cc.global.HttpRequest;
import com.xxxx.cc.global.KtyCcSdkTool;
import com.xxxx.cc.model.BaseBean;
import com.xxxx.cc.model.CommunicationRecordResponseBean;
import com.xxxx.cc.model.CustomDefinedBean;
import com.xxxx.cc.model.QueryCustomPersonBean;
import com.xxxx.cc.model.UserBean;
import com.xxxx.cc.service.LinphoneService;
import com.xxxx.cc.util.LogUtils;
import com.xxxx.cc.util.SharedPreferencesUtil;
import com.xxxx.cc.util.TimeUtils;
import com.xxxx.cc.util.ToastUtil;
import com.xxxx.cc.util.db.DbUtil;
import com.xxxx.tky.R;
import com.xxxx.tky.fragment.CommunicationRecordFragment;
import com.xxxx.tky.fragment.ContactHistoryFragment;
import com.xxxx.tky.util.AntiShakeUtils;
import com.xxxx.tky.util.CallPhoneTool;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;

import static com.xxxx.cc.global.Constans.USERBEAN_SAVE_TAG;
import static com.xxxx.cc.global.HttpRequest.Contant.updateLastCallTime;

/**
 * @author zhoufeng
 * @date 2020/2/6
 * @moduleName
 */
public class CustomPesonDetailActivity extends BaseHttpRequestActivity implements KtyCcSdkTool.CallPhoneInterface {

    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.tab_layout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.phone_image)
    ImageView phoneImage;
    @BindView(R.id.phone_num)
    TextView phoneNum;
    @BindView(R.id.phone_layout)
    LinearLayout phoneLayout;
    @BindView(R.id.line1)
    View line1;
    @BindView(R.id.jpkh_text_view)
    TextView jpkhTextView;
    @BindView(R.id.jpkh_text_view_value)
    TextView jpkhTvValue;
    @BindView(R.id.wly_text_view)
    TextView wlyTextView;
    @BindView(R.id.wly_text_view_value)
    TextView wlyTvValue;
    @BindView(R.id.address_text_view)
    TextView addressTextView;
    @BindView(R.id.address_text_view_value)
    TextView addressTvValue;
    @BindView(R.id.rtv_zy_text_view)
    TextView zyTextView;
    @BindView(R.id.rtv_zy_text_view_value)
    TextView zyTvValue;
    @BindView(R.id.zidingyi_layout)
    LinearLayout zidingyiLayout;

    private QueryCustomPersonBean queryCustomPersonBean;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    public String personBeanData;
    private UserBean cacheUserBean;
    private List<CustomDefinedBean> customDefinedBeans = new ArrayList<>();
    private CommunicationRecordResponseBean mCommunicationRecordResponseBean;//仅给sim卡使用

    @Override
    public int getLayoutViewId() {
        return R.layout.activity_custom_person_detail_new;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        tvTitle.setText("客户信息");
        try {

            Object objectBean = SharedPreferencesUtil.getObjectBean(mContext, USERBEAN_SAVE_TAG, UserBean.class);
            if (objectBean != null) {
                cacheUserBean = (UserBean) objectBean;
            }
            personBeanData = getIntent().getStringExtra("data");
            if (!TextUtils.isEmpty(personBeanData)) {
                mFragments.add(CommunicationRecordFragment.newInstance(personBeanData));
                mFragments.add(ContactHistoryFragment.newInstance(personBeanData));
                queryCustomPersonBean = JSON.parseObject(personBeanData, QueryCustomPersonBean.class);
                if (queryCustomPersonBean != null) {
                    setViewData();
                }

                String[] titles = {"沟通记录", "通话记录"};
                viewPager.setOffscreenPageLimit(2);
                tabLayout.setViewPager(viewPager, titles, this, mFragments);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        KtyCcSdkTool.getInstance().setmCallPhoneInterface(this);

    }


    public void setViewData() {
        if (!TextUtils.isEmpty(queryCustomPersonBean.getName())) {
            userName.setText(queryCustomPersonBean.getName());
        }
        phoneNum.setText(queryCustomPersonBean.getRealMobileNumber());
        addressTextView.setText(queryCustomPersonBean.getAddress());

        getDefinedMessage();


    }

    private void getDefinedMessage() {
        baseGetPresenter.presenterBusinessByHeader(HttpRequest.Contant.selfdefinedContacts, false, "token", cacheUserBean.getToken());

    }

    boolean isFirstLoad = true;

    @Override
    protected void onResume() {
        super.onResume();
        if (LinphoneService.startTelFromCall && mCommunicationRecordResponseBean != null) {
            goToCall(mCommunicationRecordResponseBean);
        }
        //查找本地的数据库
        if (isFirstLoad) {
            isFirstLoad = false;
        } else {
            if (queryCustomPersonBean != null) {
                QueryCustomPersonBean tempBean = DbUtil.queryCustomPersonBeanById(queryCustomPersonBean.getId());
                if (tempBean != null) {
                    queryCustomPersonBean = tempBean;
                    try {
                        personBeanData = com.alibaba.fastjson.JSONObject.parseObject((new Gson()).toJson(queryCustomPersonBean)).toJSONString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    setViewData();
                } else {
                    finish();
                }
            }
        }
    }

    @OnClick(R.id.go_to_detail)
    public void showDetail(View view) {
        if (AntiShakeUtils.isInvalidClick(view)) {
            return;
        }
        Intent intent = new Intent(this, CustomPersonDetailToDetailActivity.class);
        intent.putExtra("data", personBeanData);
        intent.putExtra("definedData", (Serializable) customDefinedBeans);
        startActivity(intent);
//        startActivity(CustomPersonDetailToDetailActivity.class, "data", personBeanData,"definedData",customDefinedBeans);

    }

    @OnClick(R.id.iv_close)
    public void back(View view) {
        if (AntiShakeUtils.isInvalidClick(view)) {
            return;
        }
        finish();
    }

    @OnClick(R.id.call_phone_btn)
    public void callPhone(View view) {

        if (AntiShakeUtils.isInvalidClick(view)) {
            return;
        }
        if (queryCustomPersonBean != null && !TextUtils.isEmpty(queryCustomPersonBean.getRealMobileNumber())) {
            LinphoneService.getInstance().setUpLoadContentCall(new LinphoneService.UpLoadContentCall() {
                @Override
                public void onResponse() {
                    ((ContactHistoryFragment) mFragments.get(1)).loadData();
                }

                @Override
                public void onError() {
                    ToastUtil.showToast(CustomPesonDetailActivity.this, "通话记录上传失败");
                }
            });
            CallPhoneTool.getInstance().setCallPhoneFromDetailActivity(new CallPhoneTool.CallPhoneFromDetailActivity() {
                @Override
                public void onCall(String sessionId) {
                    if(!TextUtils.isEmpty(sessionId)){
                        mCommunicationRecordResponseBean = new CommunicationRecordResponseBean();
                        mCommunicationRecordResponseBean.setCalldetailId(sessionId);
                        if (KtyCcSdkTool.getInstance().mDemoInterface != null && mCommunicationRecordResponseBean != null) {
                            KtyCcSdkTool.getInstance().mDemoInterface.goToCall(mCommunicationRecordResponseBean);
                        }
                    }

                }
            });

            CallPhoneTool.getInstance().setCallPhoneBySim(new CallPhoneTool.CallPhoneBySim() {
                @Override
                public void onCall() {
                    LinphoneService.callBySystemUUID = UUID.randomUUID().toString();
                    mCommunicationRecordResponseBean = new CommunicationRecordResponseBean();
                    mCommunicationRecordResponseBean.setCalldetailId(LinphoneService.callBySystemUUID);
                    if (KtyCcSdkTool.getInstance().mDemoInterface != null && mCommunicationRecordResponseBean != null) {
                        KtyCcSdkTool.getInstance().mDemoInterface.goToCall(mCommunicationRecordResponseBean);
                    }
                    //直接调用更新电话时间
                    if (!TextUtils.isEmpty(queryCustomPersonBean.getId())) {
                        LogUtils.e("当前请求的token：" + cacheUserBean.getToken());
                        basePostPresenter.presenterBusinessByHeader(
                                (updateLastCallTime + "/" + queryCustomPersonBean.getId()),
                                "token", cacheUserBean.getToken(),
                                "Content-Type", "application/json"
                        );
                    }
                }
            });

            CallPhoneTool.getInstance().callPhone(mContext, queryCustomPersonBean.getRealMobileNumber(),
                    queryCustomPersonBean.getName(),
                    queryCustomPersonBean.getId()
            );
        }

    }

    @Override
    public void goToCall(CommunicationRecordResponseBean selectCommunicationRecordResponseBean) {
        ((CommunicationRecordFragment) mFragments.get(0)).selectCommunicationRecordResponseBean = selectCommunicationRecordResponseBean;
        ((CommunicationRecordFragment) mFragments.get(0)).showEditDialog();

    }

//
//    @OnClick(R.id.show_edit_btn)
//    public void showEdit(View view) {
//        if (AntiShakeUtils.isInvalidClick(view)) {
//            return;
//        }
//        startActivity(CustomPersonDetailToDetailActivity.class, "data", personBeanData);
//    }

//
//    @OnClick(R.id.call_phone_btn)
//    public void callPhone(View view) {
//        if (AntiShakeUtils.isInvalidClick(view)) {
//            return;
//        }
//        if (queryCustomPersonBean != null && !TextUtils.isEmpty(queryCustomPersonBean.getRealMobileNumber())) {
//              CallPhoneTool.getInstance().callPhone(mContext, queryCustomPersonBean.getRealMobileNumber(),
//                    queryCustomPersonBean.getName(),
//                    ""
//            );
//        }
//    }


    @Override
    public void dealHttpRequestFail(String moduleName, BaseBean result) {
        super.dealHttpRequestFail(moduleName, result);
        if ((updateLastCallTime + "/" + queryCustomPersonBean.getId()).equals(moduleName)) {
            LogUtils.e("更新客户电话时间失败");

        }

    }

    @Override
    public void dealHttpRequestResult(String moduleName, BaseBean result, String response) {
        super.dealHttpRequestResult(moduleName, result, response);
        if (HttpRequest.Contant.selfdefinedContacts.equals(moduleName)) {
//            LogUtils.i("zwmn", moduleName + " >> " + result + " >> " + response);
            if (!TextUtils.isEmpty(response)) {
                try {
                    JSONObject json = new JSONObject(response);
                    JSONArray dataList = json.optJSONArray("data");
                    if (null != dataList && dataList.length() > 0) {
                        customDefinedBeans.clear();
                        for (int i = 0; i < dataList.length(); i++) {
                            JSONObject item = dataList.getJSONObject(i);
                            if (item.optBoolean("showInPage")) {
                                CustomDefinedBean bean = (new Gson()).fromJson(item.toString(), CustomDefinedBean.class);
                                if (!TextUtils.isEmpty(personBeanData)) {
                                    JSONObject personJson = new JSONObject(personBeanData);
                                    String value = personJson.optString(bean.getField());
                                    String type = item.optString("type");
                                    if ("time".equals(type)) {
                                        value = TimeUtils.getWatchTime1(Integer.parseInt(value));
                                    }

                                    bean.setValue(value);
                                }
                                customDefinedBeans.add(bean);
                            }
                        }

                        setCustomDefinedView();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } else if ((updateLastCallTime + "/" + queryCustomPersonBean.getId()).equals(moduleName)) {
            LogUtils.e("更新客户电话时间成功");

        }


    }

    private void setCustomDefinedView() {
        jpkhTextView.setVisibility(View.GONE);
        wlyTextView.setVisibility(View.GONE);
        addressTextView.setVisibility(View.GONE);
        if (null != customDefinedBeans && customDefinedBeans.size() > 0) {
            for (int i = 0; i < customDefinedBeans.size() && i < 4; i++) {
                CustomDefinedBean bean = customDefinedBeans.get(i);
                if (i == 0) {
                    jpkhTextView.setVisibility(View.VISIBLE);
                    jpkhTextView.setText(bean.getName() + " : ");
                    jpkhTvValue.setText(bean.getValue());

                }
                if (i == 1) {
                    wlyTextView.setVisibility(View.VISIBLE);
                    wlyTextView.setText(bean.getName() + " : ");
                    wlyTvValue.setText(bean.getValue());

                }
                if (i == 2) {
                    addressTextView.setVisibility(View.VISIBLE);
                    addressTextView.setText(bean.getName() + " : ");
                    addressTvValue.setText(bean.getValue());
                }

                if (i == 3) {
                    zyTextView.setText(bean.getName() + " : ");
                    zyTvValue.setText(bean.getValue());
                }
            }


        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KtyCcSdkTool.getInstance().setmCallPhoneInterface(null);
        CallPhoneTool.getInstance().setCallPhoneBySim(null);
        CallPhoneTool.getInstance().setCallPhoneFromDetailActivity(null);
        LinphoneService.getInstance().setUpLoadContentCall(null);
    }
}
