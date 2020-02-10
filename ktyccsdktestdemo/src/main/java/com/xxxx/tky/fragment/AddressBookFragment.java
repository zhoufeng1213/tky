package com.xxxx.tky.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.roundview.RoundTextView;
import com.lqr.adapter.LQRAdapterForRecyclerView;
import com.lqr.adapter.LQRHeaderAndFooterAdapter;
import com.lqr.adapter.LQRViewHolderForRecyclerView;
import com.lqr.recyclerview.LQRRecyclerView;
import com.xxxx.cc.base.fragment.BaseFragment;
import com.xxxx.tky.R;
import com.xxxx.tky.activity.BaseTransitionActivity;
import com.xxxx.tky.activity.ContactDetailActivity;
import com.xxxx.tky.activity.SearchPersonActivity;
import com.xxxx.tky.contant.Contant;
import com.xxxx.tky.model.ContactUserName;
import com.xxxx.tky.util.AntiShakeUtils;
import com.xxxx.tky.util.MobilePhoneUtil;
import com.xxxx.tky.widget.QuickIndexBar;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author zhoufeng
 * @date 2020/2/1
 * @moduleName
 */
public class AddressBookFragment extends BaseFragment {

    @BindView(R.id.rvContacts)
    LQRRecyclerView mRvContacts;
    @BindView(R.id.qib)
    QuickIndexBar mQib;
    @BindView(R.id.tvLetter)
    TextView mTvLetter;

    private List<ContactUserName> contactUserNameList;
    private LQRHeaderAndFooterAdapter mAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_address_book;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(Contant.contactUserNameList == null){
            contactUserNameList = MobilePhoneUtil.getMobilePhoneList(mContext);
            //根据字母排序
            Collections.sort(contactUserNameList, new Comparator<ContactUserName>() {
                @Override
                public int compare(ContactUserName o1, ContactUserName o2) {
                    return o1.getDisplayNameSpelling().compareTo(o2.getDisplayNameSpelling());
                }
            });
            Contant.contactUserNameList = contactUserNameList;
        }else{
            contactUserNameList = Contant.contactUserNameList;
        }

        initAdapter();

        mQib.setOnLetterUpdateListener(new QuickIndexBar.OnLetterUpdateListener() {
            @Override
            public void onLetterUpdate(String letter) {
                try {
                    //显示对话框
                    showLetter(letter);
                    for (int i = 0; i < contactUserNameList.size(); i++) {
                        ContactUserName friend = contactUserNameList.get(i);
                        if(!TextUtils.isEmpty(friend.getDisplayNameSpelling())){
                            String c = friend.getDisplayNameSpelling().charAt(0) + "";
                            if (c.equalsIgnoreCase(letter)) {
                                mRvContacts.moveToPosition(i);
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onLetterCancel() {
                //隐藏对话框
                hideLetter();
            }
        });

    }

    public static final String[] CONTACT_COLORS = {"#1a96fe", "#fd8645", "#FEA342", "#ACA9F5", "#F23B3B"};

    private void initAdapter(){
        LQRAdapterForRecyclerView adapter = new LQRAdapterForRecyclerView<ContactUserName>(mContext, contactUserNameList,
                R.layout.item_contact) {
            @Override
            public void convert(LQRViewHolderForRecyclerView helper, ContactUserName item, int position) {
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
        };
        mAdapter = adapter.getHeaderAndFooterAdapter();
        mRvContacts.setAdapter(mAdapter);

        ((LQRAdapterForRecyclerView) mAdapter.getInnerAdapter()).setOnItemClickListener((lqrViewHolder, viewGroup, view, i) -> {
            if (AntiShakeUtils.isInvalidClick(view)) {
                return;
            }
            Intent intent = new Intent(mContext, ContactDetailActivity.class);
            intent.putExtra("data",contactUserNameList.get(i));
            startActivity(intent);
        });

    }

    private void showLetter(String letter) {
        mTvLetter.setVisibility(View.VISIBLE);
        mTvLetter.setText(letter);
    }

    private void hideLetter() {
        mTvLetter.setVisibility(View.GONE);
    }

    @OnClick({R.id.search_layout,R.id.search_edit})
    public void clickSearch(View view){
        if (AntiShakeUtils.isInvalidClick(view)) {
            return;
        }

        startActivity(SearchPersonActivity.class);
    }


}
