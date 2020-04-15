package com.xxxx.tky.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shizhefei.view.indicator.IndicatorViewPager;
import com.xxxx.tky.R;
import com.xxxx.tky.fragment.DialFragment;
import com.xxxx.tky.fragment.WorkFragment;

/**
 * @author zhoufeng
 * @date 2020/2/6
 * @moduleName
 */
public class CustomPersonDetailAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {
    private String[] tabNames = {"沟通记录", "联系历史"};
    private LayoutInflater inflater;

    public CustomPersonDetailAdapter(FragmentManager fragmentManager, Context context) {
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
            convertView = inflater.inflate(R.layout.tab_custom_person_detail, container, false);
        }
        TextView textView = (TextView) convertView;
        textView.setText(tabNames[position]);
        return textView;
    }


    @Override
    public Fragment getFragmentForPage(int position) {
        switch (position) {
            case 0:
                return new DialFragment();
            case 1:
                return WorkFragment.newInstance();
            default:
                break;
        }
        return null;
    }
}

