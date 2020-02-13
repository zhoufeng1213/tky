package com.xxxx.tky.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.roundview.RoundLinearLayout;
import com.flyco.roundview.RoundTextView;
import com.xxxx.cc.base.fragment.BaseFragment;
import com.xxxx.cc.global.KtyCcSdkTool;
import com.xxxx.cc.util.ToastUtil;
import com.xxxx.tky.R;
import com.xxxx.tky.activity.ContactDetailActivity;
import com.xxxx.tky.adapter.SearchPersonAdapter;
import com.xxxx.tky.contant.Contant;
import com.xxxx.tky.model.ContactUserName;
import com.xxxx.tky.util.AntiShakeUtils;
import com.xxxx.tky.util.MobilePhoneUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author zhoufeng
 * @date 2020/2/2
 * @moduleName
 */
public class DialFragment extends BaseFragment {

    @BindView(R.id.phone_num_text_view)
    EditText phoneNumTextView;
    @BindView(R.id.button1)
    RoundTextView button1;
    @BindView(R.id.button2)
    RoundTextView button2;
    @BindView(R.id.button3)
    RoundTextView button3;
    @BindView(R.id.button4)
    RoundTextView button4;
    @BindView(R.id.button5)
    RoundTextView button5;
    @BindView(R.id.button6)
    RoundTextView button6;
    @BindView(R.id.button7)
    RoundTextView button7;
    @BindView(R.id.button8)
    RoundTextView button8;
    @BindView(R.id.button9)
    RoundTextView button9;
    @BindView(R.id.button_xing)
    RoundTextView buttonXing;
    @BindView(R.id.button0)
    RoundTextView button0;
    @BindView(R.id.button_jin)
    RoundTextView buttonJin;
    @BindView(R.id.call_button)
    ImageView callButton;
    @BindView(R.id.delete)
    ImageView delete;
    @BindView(R.id.person_list_recycler)
    RecyclerView personListRecycler;
    @BindView(R.id.dial_layout)
    LinearLayout dialLayout;
    @BindView(R.id.hide_key_image)
    ImageView hideKeyImage;
    @BindView(R.id.open_key_image_layout)
    RoundLinearLayout openKeyImageLayout;

    private List<ContactUserName> contactUserNameList;
    private SearchPersonAdapter searchPersonAdapter;
    private List<ContactUserName> userNameList;


    @Override
    public int getContentViewId() {
        return R.layout.fragment_dial;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        delete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                phoneNumTextView.setText("");
                if (mHandler.hasMessages(RC_SEARCH)) {
                    mHandler.removeMessages(RC_SEARCH);
                }
                userNameList.clear();
                searchPersonAdapter.notifyDataSetChanged();
                return false;
            }
        });
        phoneNumTextView.setInputType(InputType.TYPE_NULL);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (Contant.contactUserNameList == null) {
            contactUserNameList = MobilePhoneUtil.getMobilePhoneList(mContext);
            //根据字母排序
            Collections.sort(contactUserNameList, new Comparator<ContactUserName>() {
                @Override
                public int compare(ContactUserName o1, ContactUserName o2) {
                    return o1.getDisplayNameSpelling().compareTo(o2.getDisplayNameSpelling());
                }
            });
            Contant.contactUserNameList = contactUserNameList;
        } else {
            contactUserNameList = Contant.contactUserNameList;
        }


        personListRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        userNameList = new ArrayList<>();
        searchPersonAdapter = new SearchPersonAdapter(userNameList);
        personListRecycler.setAdapter(searchPersonAdapter);
        searchPersonAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (AntiShakeUtils.isInvalidClick(view)) {
                    return;
                }
//                if (userNameList.get(position) != null && !TextUtils.isEmpty(userNameList.get(position).getPhoneNumber())) {
//                    KtyCcSdkTool.getInstance().callPhone(mContext, userNameList.get(position).getPhoneNumber(),
//                            userNameList.get(position).getName(),
//                            ""
//                    );
//                }
                Intent intent = new Intent(mContext, ContactDetailActivity.class);
                intent.putExtra("data", userNameList.get(position));
                startActivity(intent);
            }
        });


    }

    @OnClick({
            R.id.button0,
            R.id.button1,
            R.id.button2,
            R.id.button3,
            R.id.button4,
            R.id.button5,
            R.id.button6,
            R.id.button7,
            R.id.button8,
            R.id.button9,
            R.id.button_jin,
            R.id.button_xing,
    })
    public void clickView(View view) {
        try {
            RoundTextView roundTextView = (RoundTextView) view;
            phoneNumTextView.setText(phoneNumTextView.getText().toString() + roundTextView.getText().toString());

            if (mHandler.hasMessages(RC_SEARCH)) {
                mHandler.removeMessages(RC_SEARCH);
            }
            if (TextUtils.isEmpty(phoneNumTextView.getText().toString())) {
                userNameList.clear();
                searchPersonAdapter.notifyDataSetChanged();
            } else {
                mHandler.sendEmptyMessageDelayed(RC_SEARCH, INTERVAL);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.call_button)
    public void callPhone(View view) {
        if (AntiShakeUtils.isInvalidClick(view)) {
            return;
        }
        if (!TextUtils.isEmpty(phoneNumTextView.getText().toString().trim())) {
            KtyCcSdkTool.getInstance().callPhone(mContext, phoneNumTextView.getText().toString().trim(),
                    "",
                    ""
            );
        } else {
            ToastUtil.showToast(mContext, "请输入手机号");
        }
    }


    @OnClick(R.id.delete)
    public void deletePhone() {
        try {
            String text = phoneNumTextView.getText().toString().trim();
            if (!TextUtils.isEmpty(text)) {
                text = text.substring(0, text.length() - 1);
                phoneNumTextView.setText(text);
            }
            if (mHandler.hasMessages(RC_SEARCH)) {
                mHandler.removeMessages(RC_SEARCH);
            }
            if (TextUtils.isEmpty(phoneNumTextView.getText().toString())) {
                userNameList.clear();
                searchPersonAdapter.notifyDataSetChanged();
            } else {
                mHandler.sendEmptyMessageDelayed(RC_SEARCH, INTERVAL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.hide_key_image)
    public void hideKeyImage(View view) {
        if (AntiShakeUtils.isInvalidClick(view)) {
            return;
        }
        dialLayout.setVisibility(View.GONE);
        openKeyImageLayout.setVisibility(View.VISIBLE);

    }


    @OnClick(R.id.open_key_image_layout)
    public void openKeyImage(View view) {
        if (AntiShakeUtils.isInvalidClick(view)) {
            return;
        }
        dialLayout.setVisibility(View.VISIBLE);
        openKeyImageLayout.setVisibility(View.GONE);

    }

    private final int RC_SEARCH = 1;
    private final int INTERVAL = 500;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == RC_SEARCH) {
                handlerSearch();
            }
        }
    };


    @Override
    public void onDestroyView() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        super.onDestroyView();
    }


    private void handlerSearch() {
        String searchText = phoneNumTextView.getText().toString().trim();

        if (TextUtils.isEmpty(searchText)) {
            userNameList.clear();
            searchPersonAdapter.notifyDataSetChanged();
        } else {
            userNameList.clear();
            searchPersonAdapter.notifyDataSetChanged();
            //获取数据
            for (ContactUserName contactUserName : contactUserNameList) {
                if (contactUserName != null) {
                    if ((!TextUtils.isEmpty(contactUserName.getName()) && contactUserName.getName().contains(searchText))
                            || (!TextUtils.isEmpty(contactUserName.getPhoneNumber()) && contactUserName.getPhoneNumber().contains(searchText))
                            || (!TextUtils.isEmpty(contactUserName.getDisplayNameSpelling()) && (contactUserName.getDisplayNameSpelling().toUpperCase().contains(searchText) || contactUserName.getDisplayNameSpelling().toLowerCase().contains(searchText)))
                    ) {
                        userNameList.add(contactUserName);
                    }
                }
            }
            searchPersonAdapter.notifyDataSetChanged();

        }


    }

}
