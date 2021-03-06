package com.xxxx.tky.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.roundview.RoundTextView;
import com.xxxx.cc.base.fragment.BaseFragment;
import com.xxxx.cc.global.Constans;
import com.xxxx.cc.global.KtyCcSdkTool;
import com.xxxx.cc.model.UserBean;
import com.xxxx.cc.service.LinphoneService;
import com.xxxx.cc.util.SharedPreferencesUtil;
import com.xxxx.cc.util.db.DbUtil;
import com.xxxx.tky.R;
import com.xxxx.tky.activity.LoginActivity;
import com.xxxx.tky.contant.Contant;
import com.xxxx.tky.util.AntiShakeUtils;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;

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
                userImage.setText(cacheUserBean.getUsername().substring(0,1));
                userName.setText(cacheUserBean.getUsername());
            }
        }
    }

    @Optional
    @OnClick(R.id.log_out_button)
    public void logOut(View view) {
        if (AntiShakeUtils.isInvalidClick(view)) {
            return;
        }
        SharedPreferencesUtil.save(mContext, Contant.LOGIN_PWD_SAVE_TAG,"");
//        KtyCcSdkTool.getInstance().unRegister(mContext);
        LinphoneService.setRegister(false);
        startActivity(LoginActivity.class);
        if(getActivity() != null){
            getActivity().finish();
        }
    }

}
