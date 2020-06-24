package com.xxxx.tky.fragment;

import android.content.Intent;
import android.view.View;

import com.xxxx.cc.base.fragment.BaseFragment;
import com.xxxx.tky.R;
import com.xxxx.tky.activity.CallHistoryFragmentActivity;
import com.xxxx.tky.activity.CallTotalCountActivity;
import com.xxxx.tky.activity.GuideActivity;
import com.xxxx.tky.activity.MineCustomPersonActivity;
import com.xxxx.tky.util.AntiShakeUtils;
import com.xxxx.tky.view.ImageTextButton;

import butterknife.BindView;
import butterknife.OnClick;

//import com.xxxx.tky.activity.CustomPersonActivity;

/**
 * @author zhoufeng
 * @date 2020/2/1
 * @moduleName
 */
public class WorkFragment extends BaseFragment {

    @BindView(R.id.recent_call_bt)
    ImageTextButton recent_call_BT;
    @BindView(R.id.customer_bt)
    ImageTextButton customer_BT;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_work;
    }

    public static WorkFragment newInstance() {
        return new WorkFragment();
    }

    @OnClick(R.id.recent_call_bt)
    public void clickHistory(View view) {
        if (AntiShakeUtils.isInvalidClick(view)) {
            return;
        }
        startActivity(CallHistoryFragmentActivity.class);
    }

    @OnClick(R.id.customer_bt)
    public void clickCustom(View view) {
        if (AntiShakeUtils.isInvalidClick(view)) {
            return;
        }
        startActivity(MineCustomPersonActivity.class);
    }

    @OnClick(R.id.guide_banner_id)
    public void clickGuide(View view) {
        if (AntiShakeUtils.isInvalidClick(view)) {
            return;
        }
        Intent intent = new Intent(getContext(), GuideActivity.class);
        intent.putExtra(GuideActivity.TITLE_NAME,"使用指南");
        intent.putExtra(GuideActivity.WEB_URL,"https://www.ketianyun.com/callcenter/tky/app/help/help.html");
//            intent.setAction("android.intent.action.VIEW");
//            Uri content_url = Uri.parse("https://www.ketianyun.com/callcenter/tky/app/help/help.html");//此处填链接
//            intent.setData(content_url);
        startActivity(intent);
    }

    @OnClick(R.id.hwtj_bt)
    public void hwtjClick(View view) {
        if (AntiShakeUtils.isInvalidClick(view)) {
            return;
        }
        startActivity(CallTotalCountActivity.class);
    }

}
