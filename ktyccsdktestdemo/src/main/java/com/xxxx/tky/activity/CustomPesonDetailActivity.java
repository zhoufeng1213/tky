package com.xxxx.tky.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.flyco.roundview.RoundLinearLayout;
import com.flyco.roundview.RoundTextView;
import com.flyco.tablayout.SlidingTabLayout;
import com.gyf.barlibrary.ImmersionBar;
import com.xxxx.cc.base.activity.BaseActivity;
import com.xxxx.cc.base.activity.BaseHttpRequestActivity;
import com.xxxx.cc.global.HttpRequest;
import com.xxxx.cc.global.KtyCcSdkTool;
import com.xxxx.cc.model.QueryCustomPersonBean;
import com.xxxx.cc.model.UserBean;
import com.xxxx.cc.util.SharedPreferencesUtil;
import com.xxxx.cc.util.db.DbUtil;
import com.xxxx.tky.R;
import com.xxxx.tky.fragment.CommunicationRecordFragment;
import com.xxxx.tky.fragment.ContactHistoryFragment;
import com.xxxx.tky.util.AntiShakeUtils;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.xxxx.cc.global.Constans.USERBEAN_SAVE_TAG;

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
    @BindView(R.id.wly_text_view)
    TextView wlyTextView;
    @BindView(R.id.address_text_view)
    TextView addressTextView;
    @BindView(R.id.zidingyi_layout)
    RelativeLayout zidingyiLayout;

    private QueryCustomPersonBean queryCustomPersonBean;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String personBeanData;
    private UserBean cacheUserBean;

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


    public void setViewData(){
        if (!TextUtils.isEmpty(queryCustomPersonBean.getName())) {
            userName.setText(queryCustomPersonBean.getName());
        }
        phoneNum.setText(queryCustomPersonBean.getRealMobileNumber());
        addressTextView.setText(queryCustomPersonBean.getAddress());

    }

    boolean isFirstLoad = true;
    @Override
    protected void onResume() {
        super.onResume();
        //查找本地的数据库
        if(isFirstLoad){
            isFirstLoad = false;
        }else{
            if(queryCustomPersonBean != null){
                QueryCustomPersonBean tempBean = DbUtil.queryCustomPersonBeanById(queryCustomPersonBean.getId());
                if(tempBean != null){
                    queryCustomPersonBean = tempBean;
                    setViewData();
                }else{
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
        startActivity(CustomPersonDetailToDetailActivity.class, "data", personBeanData);

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
            KtyCcSdkTool.getInstance().callPhone(mContext, queryCustomPersonBean.getRealMobileNumber(),
                    queryCustomPersonBean.getName(),
                    ""
            );
        }

    }

    @Override
    public void goToCall() {

        new Handler().postDelayed(new Runnable() {
            public void run() {
                ((CommunicationRecordFragment) mFragments.get(0)).showEditDialog();
            }
        }, 1000);

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
//            KtyCcSdkTool.getInstance().callPhone(mContext, queryCustomPersonBean.getRealMobileNumber(),
//                    queryCustomPersonBean.getName(),
//                    ""
//            );
//        }
//    }

}
