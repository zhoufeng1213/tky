package com.xxxx.tky.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyco.roundview.RoundTextView;
import com.xxxx.tky.R;
import com.xxxx.tky.model.ContactUserName;

import java.util.List;

/**
 * @author zhoufeng
 * @date 2019/8/5
 * @moduleName
 */
public class SearchPersonAdapter extends BaseQuickAdapter<ContactUserName, BaseViewHolder> {
    private static final String[] CONTACT_COLORS = {"#1a96fe", "#fd8645", "#FEA342", "#ACA9F5", "#F23B3B"};

    public SearchPersonAdapter(@Nullable List<ContactUserName> data) {
        super(R.layout.item_contact, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ContactUserName item) {
        helper.setText(R.id.name, item.getName());
        if (!TextUtils.isEmpty(item.getName())) {
            helper.setText(R.id.name_letter, item.getName().substring(0, 1));
        }
        RoundTextView textView = helper.getView(R.id.name_letter);
        textView.getDelegate().setBackgroundColor(Color.parseColor(CONTACT_COLORS[helper.getAdapterPosition() % CONTACT_COLORS.length]));
    }
}
