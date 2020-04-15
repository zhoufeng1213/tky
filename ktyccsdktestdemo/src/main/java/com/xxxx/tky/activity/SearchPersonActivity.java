package com.xxxx.tky.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.roundview.RoundLinearLayout;
import com.xw.repo.XEditText;
import com.xxxx.cc.base.activity.BaseActivity;
import com.xxxx.tky.R;
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

import static org.linphone.mediastream.MediastreamerAndroidContext.getContext;

/**
 * @author zhoufeng
 * @date 2020/2/3
 * @moduleName
 */
public class SearchPersonActivity extends BaseActivity {


    @BindView(R.id.search_edit)
    XEditText searchEdit;
    @BindView(R.id.search_layout)
    RoundLinearLayout searchLayout;
    @BindView(R.id.search_recycler)
    RecyclerView searchRecycler;

    private List<ContactUserName> contactUserNameList;
    private SearchPersonAdapter searchPersonAdapter;
    private List<ContactUserName> userNameList;

    @Override
    public int getLayoutViewId() {
        return R.layout.activity_search_person;
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


        searchEdit.postDelayed(new Runnable() {
            @Override
            public void run() {
                searchEdit.requestFocus();
                InputMethodManager manager = ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE));
                if (manager != null) {
                    manager.showSoftInput(searchEdit, 0);
                }
            }
        }, 100);


        searchEdit.setOnXTextChangeListener(new XEditText.OnXTextChangeListener() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (mHandler.hasMessages(RC_SEARCH)) {
                    mHandler.removeMessages(RC_SEARCH);
                }
                if (TextUtils.isEmpty(s.toString())) {
                    userNameList.clear();
                    searchPersonAdapter.notifyDataSetChanged();
                } else {
                    mHandler.sendEmptyMessageDelayed(RC_SEARCH, INTERVAL);
                }
            }
        });

        searchRecycler.setLayoutManager(new LinearLayoutManager(this));
        userNameList = new ArrayList<>();
        searchPersonAdapter = new SearchPersonAdapter(userNameList);
        searchRecycler.setAdapter(searchPersonAdapter);
        searchPersonAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (AntiShakeUtils.isInvalidClick(view)) {
                    return;
                }
                Intent intent = new Intent(mContext, ContactDetailActivity.class);
                intent.putExtra("data", userNameList.get(position));
                startActivity(intent);
            }
        });
    }


    private final int RC_SEARCH = 1;
    private final int INTERVAL = 300; //输入时间间隔为300毫秒

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == RC_SEARCH) {
                handlerSearch();
            }
        }
    };

    @Override
    protected void onDestroy() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();

    }

    private void handlerSearch() {
        String searchText = searchEdit.getText().toString().trim();

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
