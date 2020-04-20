package com.xxxx.tky.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.roundview.RoundTextView;
import com.xxxx.cc.base.activity.BaseActivity;
import com.xxxx.tky.R;
import com.xxxx.tky.model.ContactUserName;
import com.xxxx.tky.util.AntiShakeUtils;
import com.xxxx.tky.util.CallPhoneTool;
import com.xxxx.tky.util.TextUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author zhoufeng
 * @date 2020/2/2
 * @moduleName
 */
public class ContactDetailActivity extends BaseActivity {

    @BindView(R.id.name_letter)
    RoundTextView nameLetter;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.tag)
    TextView tag;
    @BindView(R.id.phone_num)
    TextView phoneNum;
    @BindView(R.id.call_button)
    ImageView callButton;

    private ContactUserName contactUserName;

    @Override
    public int getLayoutViewId() {
        return R.layout.activity_contact_detail;
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

        contactUserName = (ContactUserName) getIntent().getSerializableExtra("data");
        if (contactUserName != null) {
            name.setText(contactUserName.getName());
            nameLetter.setText(TextUtil.getNameFirstName(contactUserName.getName()));
            phoneNum.setText(contactUserName.getPhoneNumber());
        }
    }

    @OnClick({R.id.bohao_layout})
    public void call(View view) {
        if (AntiShakeUtils.isInvalidClick(view)) {
            return;
        }
        if (
                !TextUtils.isEmpty(contactUserName.getName()) &&
                        !TextUtils.isEmpty(contactUserName.getPhoneNumber())
        ) {
            CallPhoneTool.getInstance().callPhone(mContext, contactUserName.getPhoneNumber(),
                    contactUserName.getName(),""
            );
        }
    }


}
