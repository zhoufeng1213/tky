package com.xxxx.tky.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyco.roundview.RoundTextView;
import com.xxxx.cc.model.QueryCustomPersonBean;
import com.xxxx.tky.R;

import java.util.List;

/**
 * @author zhoufeng
 * @date 2019/8/5
 * @moduleName
 */
public class CustomPersonAdapter extends BaseQuickAdapter<QueryCustomPersonBean, BaseViewHolder> {
    private static final String[] CONTACT_COLORS = {"#1a96fe", "#fd8645", "#FEA342", "#ACA9F5", "#F23B3B"};

    public CustomPersonAdapter(@Nullable List<QueryCustomPersonBean> data) {
        super(R.layout.item_custom_person, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, QueryCustomPersonBean item) {
        helper.setText(R.id.name, item.getName());
        helper.setText(R.id.phone, item.getRealMobileNumber());
        if (!TextUtils.isEmpty(item.getName())) {
            helper.setText(R.id.user_name, item.getName().substring(0, 1));
        }
        RoundTextView textView = helper.getView(R.id.user_name);
        textView.getDelegate().setBackgroundColor(Color.parseColor(CONTACT_COLORS[helper.getAdapterPosition() % CONTACT_COLORS.length]));
        if (!TextUtils.isEmpty(item.getUpdatetime())) {
            helper.setText(R.id.update_time, item.getUpdatetime());
            helper.setGone(R.id.update_time, true);
        } else {
            helper.setGone(R.id.update_time, false);
        }

    }
}
