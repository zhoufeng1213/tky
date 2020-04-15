package com.xxxx.tky.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lqr.recyclerview.LQRRecyclerView;
import com.xxxx.cc.base.fragment.BaseFragment;
import com.xxxx.cc.model.ContentBean;
import com.xxxx.cc.model.QueryCustomPersonBean;
import com.xxxx.cc.model.UserBean;
import com.xxxx.cc.util.SharedPreferencesUtil;
import com.xxxx.cc.util.db.DbUtil;
import com.xxxx.tky.R;
import com.xxxx.tky.adapter.CallHistoryAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.xxxx.cc.global.Constans.USERBEAN_SAVE_TAG;

/**
 * @author zhoufeng
 * @date 2020/2/7
 * @moduleName
 */
public class ContactHistoryFragment extends BaseFragment {
    @BindView(R.id.recycler_view)
    LQRRecyclerView recyclerView;
    @BindView(R.id.empty_text_view)
    TextView emptyTextView;

    private QueryCustomPersonBean queryCustomPersonBean;
    private UserBean cacheUserBean;
    private CallHistoryAdapter historyAdapter;
    private List<ContentBean> historyResponseBeanList = new ArrayList<>();

    public static ContactHistoryFragment newInstance(String data) {
        ContactHistoryFragment fragment = new ContactHistoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("data", data);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_contact_history;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Object objectBean = SharedPreferencesUtil.getObjectBean(mContext, USERBEAN_SAVE_TAG, UserBean.class);
        if (objectBean != null) {
            cacheUserBean = (UserBean) objectBean;
        }
        try {
            if (getArguments() != null && cacheUserBean != null) {
                String data = getArguments().getString("data");
                if (!TextUtils.isEmpty(data)) {
                    queryCustomPersonBean = JSON.parseObject(data, QueryCustomPersonBean.class);
                    if (queryCustomPersonBean != null) {
                        List<ContentBean> list = DbUtil.queryPhoneRecordListByHistory(cacheUserBean.getUserId(),
                                queryCustomPersonBean.getRealMobileNumber(), "OUTBOUND");
                        if (list != null && list.size() > 0) {
                            recyclerView.setVisibility(View.VISIBLE);
                            emptyTextView.setVisibility(View.GONE);
                            historyAdapter = new CallHistoryAdapter(list);
                            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
                            recyclerView.setAdapter(historyAdapter);

                        } else {
                            recyclerView.setVisibility(View.GONE);
                            emptyTextView.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
