package com.xxxx.tky.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyco.roundview.RoundTextView;
import com.xxxx.tky.R;
import com.xxxx.tky.model.ContactUserName;

import java.util.List;

/**
 * @author zhoufeng
 * @date 2020/2/4
 * @moduleName
 */
public class ContactAdapter extends BaseQuickAdapter<ContactUserName, BaseViewHolder> {
    private static final String[] CONTACT_COLORS = {"#1a96fe", "#fd8645", "#FEA342", "#ACA9F5", "#F23B3B"};

    public ContactAdapter(@Nullable List<ContactUserName> data) {
        super(R.layout.item_contact, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ContactUserName item) {
        try {
            helper.setText(R.id.name, item.getName());
            if(!TextUtils.isEmpty(item.getName())){
                helper.setText(R.id.name_letter, item.getName().substring(0,1));
            }


            RoundTextView textView = helper.getView(R.id.name_letter);
            textView.getDelegate().setBackgroundColor(Color.parseColor(CONTACT_COLORS[helper.getAdapterPosition() % CONTACT_COLORS.length]));

            LinearLayout linearLayout = helper.getView(R.id.letter_layout);
            TextView letter = helper.getView(R.id.search_letter);
            linearLayout.setVisibility(View.VISIBLE);
            letter.setText(item.getLetters());
            if (helper.getAdapterPosition() > 0) {
                int lastPosition = helper.getAdapterPosition() - 1;
                if (lastPosition >= 0 && lastPosition < helper.getAdapterPosition() && getData() != null
                        && getData().size() > 0) {
                    ContactUserName lastBean = getData().get(lastPosition);
                    if (lastBean != null) {
                        String lastTime = lastBean.getLetters();
                        if (!TextUtils.isEmpty(lastTime) && !TextUtils.isEmpty(item.getLetters()) &&
                                lastTime.equals(item.getLetters())
                        ) {
                            linearLayout.setVisibility(View.GONE);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

