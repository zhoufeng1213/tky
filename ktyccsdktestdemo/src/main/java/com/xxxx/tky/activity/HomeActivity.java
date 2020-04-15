package com.xxxx.tky.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.shizhefei.view.indicator.FixedIndicatorView;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;
import com.shizhefei.view.viewpager.SViewPager;
import com.xxxx.cc.global.HttpRequest;
import com.xxxx.cc.global.KtyCcSdkTool;
import com.xxxx.cc.model.BaseBean;
import com.xxxx.cc.model.UserBean;
import com.xxxx.cc.service.FloatingImageDisplayService;
import com.xxxx.cc.util.LinServiceManager;
import com.xxxx.cc.util.LogUtils;
import com.xxxx.cc.util.SharedPreferencesUtil;
import com.xxxx.cc.util.ToastUtil;
import com.xxxx.tky.R;
import com.xxxx.tky.fragment.AddressBookFragment;
import com.xxxx.tky.fragment.DialFragment;
import com.xxxx.tky.fragment.MineFragment;
import com.xxxx.tky.fragment.WorkFragment;

import butterknife.BindView;

import static com.xxxx.cc.global.Constans.USERBEAN_SAVE_TAG;

/**
 * @author zhoufeng
 * @date 2020/2/1
 * @moduleName
 */
public class HomeActivity extends BaseTransitionActivity {

    @BindView(R.id.tab_main_viewPager)
    SViewPager tabMainViewPager;
    @BindView(R.id.tab_main_indicator)
    FixedIndicatorView tabMainIndicator;
    private UserBean cacheUserBean;
    private IndicatorViewPager indicatorViewPager;

    @Override
    public int getLayoutViewId() {
        return R.layout.activity_home;
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
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        tabMainIndicator.setOnTransitionListener(new OnTransitionTextListener().setColor(getResources().getColor(R.color.f_f2a79fc), Color.GRAY));

        indicatorViewPager = new IndicatorViewPager(tabMainIndicator, tabMainViewPager);
        indicatorViewPager.setAdapter(new MyAdapter(getSupportFragmentManager(), mContext));
        // 禁止viewpager的滑动事件
        tabMainViewPager.setCanScroll(false);
        // 设置viewpager保留界面不重新加载的页面数量
        tabMainViewPager.setOffscreenPageLimit(4);
        Object objectBean = SharedPreferencesUtil.getObjectBean(mContext, USERBEAN_SAVE_TAG, UserBean.class);
        if (objectBean != null) {
            cacheUserBean = (UserBean) objectBean;
        }


        //先启动service
        KtyCcSdkTool.startLinPhoneService(this);
//        if (cacheUserBean != null && cacheUserBean.getCcUserInfo() != null && LinphoneService.getCore() == null || LinphoneService.getCore().equals("") || !LinphoneService.isRegister()) {
//            LogUtils.e("is register");
//            baseGetPresenter.presenterBusinessByHeader(HttpRequest.registe, false, "token", cacheUserBean.getToken());
//        }
    }

    @Override
    public com.alibaba.fastjson.JSONObject getHttpRequestParams(String moduleName) {
        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
        if ((HttpRequest.registe).equals(moduleName)) {
            jsonObject.put("extensionNo", cacheUserBean.getCcUserInfo().getExtensionNo());
        }
        return jsonObject;
    }

    @Override
    public void dealHttpRequestResult(String moduleName, BaseBean result, String response) {
        super.dealHttpRequestResult(moduleName, result, response);
        if (HttpRequest.registe.equals(moduleName)) {
            if (result.isOk() && !TextUtils.isEmpty(response)) {
                com.alibaba.fastjson.JSONObject jsonObject = JSONObject.parseObject(response);
                boolean register = jsonObject.getBoolean("register");
                LogUtils.e("register:" + register);
                if (register) {
                    new AlertDialog.Builder(HomeActivity.this).setTitle("该帐号已在其他端登录，请联系其他端退出登录")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dismissDialog();
                                        }
                                    }
                            ).show();
                }
            }
        }
    }

    @Override
    public void dealHttpRequestFail(String moduleName, BaseBean result) {
        super.dealHttpRequestFail(moduleName, result);
        ToastUtil.showToast(this, result.getMessage());
    }

    private class MyAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {
        private String[] tabNames = {"拨号盘", "工作台", "通讯录", "我的"};
        private int[] tabIcons = {R.drawable.maintab_3_selector, R.drawable.maintab_1_selector,
                R.drawable.maintab_2_selector, R.drawable.maintab_4_selector};

        private LayoutInflater inflater;

        public MyAdapter(FragmentManager fragmentManager, Context context) {
            super(fragmentManager);
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return tabNames.length;
        }

        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.tab_main, container, false);
            }
            TextView textView = (TextView) convertView;
            textView.setText(tabNames[position]);
            textView.setCompoundDrawablesWithIntrinsicBounds(0, tabIcons[position], 0, 0);
            return textView;
        }


        @Override
        public Fragment getFragmentForPage(int position) {
            switch (position) {
                case 0:
                    return new DialFragment();
                case 1:
                    return WorkFragment.newInstance();
                case 2:
                    return new AddressBookFragment();
                case 3:
                    return MineFragment.newInstance();
                default:
                    break;
            }
            return null;
        }
    }


    private long firstTime = 0;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (FloatingImageDisplayService.isStarted) {
                moveTaskToBack(true);
                LogUtils.e("moveTaskToBack");
                return true;
            }
            long secondTime = System.currentTimeMillis();
            // 如果两次按键时间间隔大于2秒，则不退出
            if (secondTime - firstTime > 2000) {
                ToastUtil.showToast(HomeActivity.this, getString(R.string.login_exit_msg));
                firstTime = secondTime;
                return true;
            } else {
                finish();
                LinServiceManager.unRegisterOnlineLinPhone(cacheUserBean, true);
                return false;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean isNeedShowNetPoorManyTimes() {
        return true;
    }
}

