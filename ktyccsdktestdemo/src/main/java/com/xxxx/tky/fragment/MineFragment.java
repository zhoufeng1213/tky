package com.xxxx.tky.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.roundview.RoundTextView;
import com.xxxx.cc.base.fragment.BaseFragment;
import com.xxxx.cc.global.KtyCcSdkTool;
import com.xxxx.cc.global.PackageUtils;
import com.xxxx.cc.model.UserBean;
import com.xxxx.cc.util.LinServiceManager;
import com.xxxx.cc.util.SharedPreferencesUtil;
import com.xxxx.tky.R;
import com.xxxx.tky.activity.FeedBackActivity;
import com.xxxx.tky.activity.LoginActivity;
import com.xxxx.tky.contant.Contant;
import com.xxxx.tky.util.AntiShakeUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.xxxx.cc.global.Constans.USERBEAN_SAVE_TAG;

/**
 * @author zhoufeng
 * @date 2020/2/1
 * @moduleName
 */
public class MineFragment extends BaseFragment {


    @BindView(R.id.user_image)
    RoundTextView userImage;
    @BindView(R.id.user_name)
    TextView userName;
    @BindView(R.id.user_company)
    TextView userCompany;
    @BindView(R.id.user_layout)
    LinearLayout userLayout;
    @BindView(R.id.tv_app_version)
    TextView tvVersion;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_mine;
    }

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Object objectBean = SharedPreferencesUtil.getObjectBean(mContext, USERBEAN_SAVE_TAG, UserBean.class);
        if (objectBean != null) {
            UserBean cacheUserBean = (UserBean) objectBean;
            if (!TextUtils.isEmpty(cacheUserBean.getUsername())) {
                userImage.setText(cacheUserBean.getUsername().substring(0, 1));
                userName.setText(cacheUserBean.getUsername());
            }
        }
        String appVersion = PackageUtils.getVersionName(mActivity);
        if (!TextUtils.isEmpty(appVersion)) {
            tvVersion.setText("v" + appVersion);
        } else {
            tvVersion.setText("");
        }
    }

    @OnClick({R.id.log_out_button, R.id.feedback_layout})
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.log_out_button) {
            if (AntiShakeUtils.isInvalidClick(view)) {
                return;
            }
            SharedPreferencesUtil.save(mContext, Contant.LOGIN_PWD_SAVE_TAG, "");
            LinServiceManager.unRegisterLinPhone();
            KtyCcSdkTool.getInstance().unRegister(mActivity);
            startActivity(LoginActivity.class);
            if (getActivity() != null) {
                getActivity().finish();

            }
        }
        else if (i == R.id.feedback_layout) {

            Intent intent = new Intent(mContext, FeedBackActivity.class);
            startActivity(intent);
        }

    }
}
