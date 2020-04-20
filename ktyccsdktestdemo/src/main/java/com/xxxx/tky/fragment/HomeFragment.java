package com.xxxx.tky.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.flyco.roundview.RoundTextView;
import com.xxxx.cc.base.fragment.BaseFragment;
import com.xxxx.tky.R;
import com.xxxx.tky.util.CallPhoneTool;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author zhoufeng
 * @date 2020/2/1
 * @moduleName
 */
public class HomeFragment extends BaseFragment {


    @BindView(R.id.home_et_contact_name)
    EditText homeEtContactName;
    @BindView(R.id.home_et_phone)
    EditText homeEtPhone;
    @BindView(R.id.home_tv_call)
    RoundTextView homeTvCall;
    Unbinder unbinder;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_home;
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }


    @OnClick(R.id.home_tv_call)
    public void call() {
        if (TextUtils.isEmpty(homeEtPhone.getText().toString().trim())) {
            Toast.makeText(mContext, "请输入号码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(homeEtContactName.getText().toString().trim())) {
            Toast.makeText(mContext, "请输入姓名", Toast.LENGTH_SHORT).show();
            return;
        }
//          CallPhoneTool.getInstance().callPhone(mContext, homeEtPhone.getText().toString().trim(),
//                homeEtContactName.getText().toString().trim(),
//                "http://img.mp.itc.cn/upload/20161020/e81dd51cb7ab4695bde800d1b001ba14_th.jpeg"
//        );

        CallPhoneTool.getInstance().callPhone(mContext, homeEtPhone.getText().toString().trim(),
                homeEtContactName.getText().toString().trim(),""
        );
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
