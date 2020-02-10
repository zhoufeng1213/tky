package com.xxxx.tky.fragment;

import android.view.View;

import com.xxxx.cc.base.fragment.BaseFragment;
import com.xxxx.tky.R;
import com.xxxx.tky.activity.CallHistoryFragmentActivity;
import com.xxxx.tky.activity.CustomPersonActivity;
import com.xxxx.tky.activity.MineCustomPersonActivity;
import com.xxxx.tky.util.AntiShakeUtils;

import butterknife.OnClick;

//import com.xxxx.tky.activity.CustomPersonActivity;

/**
 * @author zhoufeng
 * @date 2020/2/1
 * @moduleName
 */
public class WorkFragment extends BaseFragment {


    @Override
    public int getContentViewId() {
        return R.layout.fragment_work;
    }

    public static WorkFragment newInstance() {
        return new WorkFragment();
    }

    @OnClick(R.id.lishitonghua_layout)
    public void clickHistory(View view) {
        if (AntiShakeUtils.isInvalidClick(view)) {
            return;
        }
        startActivity(CallHistoryFragmentActivity.class);
    }

    @OnClick(R.id.kehuziliao_layout)
    public void clickCustom(View view) {
        if (AntiShakeUtils.isInvalidClick(view)) {
            return;
        }
        startActivity(MineCustomPersonActivity.class);
    }



}
