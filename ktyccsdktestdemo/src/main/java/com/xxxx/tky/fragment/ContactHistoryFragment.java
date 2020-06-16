package com.xxxx.tky.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.lqr.recyclerview.LQRRecyclerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.xxxx.cc.base.fragment.BaseFragment;
import com.xxxx.cc.base.fragment.BaseHttpRequestFragment;
import com.xxxx.cc.global.HttpRequest;
import com.xxxx.cc.model.BaseBean;
import com.xxxx.cc.model.ContentBean;
import com.xxxx.cc.model.HistoryRequestBean;
import com.xxxx.cc.model.HistoryResponseBean;
import com.xxxx.cc.model.QueryCustomPersonBean;
import com.xxxx.cc.model.UserBean;
import com.xxxx.cc.util.LogUtils;
import com.xxxx.cc.util.SharedPreferencesUtil;
import com.xxxx.cc.util.ThreadTask;
import com.xxxx.cc.util.db.DbUtil;
import com.xxxx.tky.R;
import com.xxxx.tky.adapter.CallHistoryAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.xxxx.cc.global.Constans.COMMON_PAGE_SIZE;
import static com.xxxx.cc.global.Constans.KTY_CC_BEGIN;
import static com.xxxx.cc.global.Constans.USERBEAN_SAVE_TAG;
import static com.xxxx.cc.global.Constans.VOICE_RECORD_PREFIX;

/**
 * @author zhoufeng
 * @date 2020/2/7
 * @moduleName
 */
public class ContactHistoryFragment extends BaseHttpRequestFragment {
    @BindView(R.id.recycler_view_new)
    RecyclerView recyclerViewNew;
    @BindView(R.id.empty_text_view)
    TextView emptyTextView;
    @BindView(R.id.srl_refresh)
    SmartRefreshLayout srlRefresh;
    private QueryCustomPersonBean queryCustomPersonBean;
    private UserBean cacheUserBean;
    private CallHistoryAdapter historyAdapter;
    private List<ContentBean> historyResponseBeanList = new ArrayList<>();
    private int page;
    private Long beginTime;
    private boolean isFirstRefrush;
    private long endTime;

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Object objectBean = SharedPreferencesUtil.getObjectBean(mContext, USERBEAN_SAVE_TAG, UserBean.class);
        if (objectBean != null) {
            cacheUserBean = (UserBean) objectBean;
        }

        try {
            if (getArguments() != null && cacheUserBean != null && recyclerViewNew != null) {
                String data = getArguments().getString("data");
                if (!TextUtils.isEmpty(data)) {
                    queryCustomPersonBean = JSON.parseObject(data, QueryCustomPersonBean.class);
                    if (queryCustomPersonBean != null) {
                        historyResponseBeanList = DbUtil.queryPhoneRecordListByHistory(cacheUserBean.getUserId(),
                                queryCustomPersonBean.getRealMobileNumber(), "OUTBOUND");
                        if (historyResponseBeanList != null && historyResponseBeanList.size() > 0) {

                            emptyTextView.setVisibility(View.GONE);
                            historyAdapter = new CallHistoryAdapter(historyResponseBeanList);
                            recyclerViewNew.setLayoutManager(new LinearLayoutManager(mContext));
                            recyclerViewNew.setAdapter(historyAdapter);
                            recyclerViewNew.setVisibility(View.VISIBLE);
                        } else {
                            recyclerViewNew.setVisibility(View.GONE);
                            emptyTextView.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            srlRefresh.setEnableLoadMore(true);
            srlRefresh.setEnableRefresh(true);
            srlRefresh.setEnableLoadMoreWhenContentNotFull(false);
            srlRefresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    page++;
                    List<ContentBean> list = DbUtil.queryPhoneRecordList(cacheUserBean.getUserId(), page);
                    if (list != null && list.size() > 0) {
                        srlRefresh.finishLoadMore();
                        srlRefresh.finishRefresh();
                        historyResponseBeanList.addAll(list);
                        historyAdapter.notifyDataSetChanged();
                    } else {
                        basePostPresenter.presenterBusinessByHeader(
                                HttpRequest.CallHistory.callHistory,
                                false,
                                "token", cacheUserBean.getToken(),
                                "Content-Type", "application/json"
                        );
                    }
                }

                @Override
                public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                    page = 0;
//                    if (!isFirstRefrush) {
//                        endTime = System.currentTimeMillis();
//                        historyResponseBeanList.clear();
//                        historyAdapter.notifyDataSetChanged();
//                        loadData();
//                    }
//
//                    isFirstRefrush = false;
                    endTime=System.currentTimeMillis();
                    basePostPresenter.presenterBusinessByHeader(
                            HttpRequest.CallHistory.callHistory,
                            false,
                            "token", cacheUserBean.getToken(),
                            "Content-Type", "application/json"
                    );
                }
            });
            loadData();
            srlRefresh.autoRefresh();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void loadData() {
        //判断是否存在begin
        String beginStr = SharedPreferencesUtil.getValue(mContext, KTY_CC_BEGIN);
        if (!TextUtils.isEmpty(beginStr)) {
            try {
                beginTime = Long.valueOf(beginStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (beginTime > 0) {
            //先把本地数据查询出来
            List<ContentBean> list = DbUtil.queryPhoneRecordList(cacheUserBean.getUserId(), page);
            if (list != null && list.size() > 0) {
                historyResponseBeanList.addAll(list);
                historyAdapter.notifyDataSetChanged();
            }
        }
        basePostPresenter.presenterBusinessByHeader(
                HttpRequest.CallHistory.callHistory,
                false,
                "token", cacheUserBean.getToken(),
                "Content-Type", "application/json"
        );
    }
    @Override
    public JSONObject getHttpRequestParams(String moduleName) {
        JSONObject jsonObject = new JSONObject();
        if (HttpRequest.CallHistory.callHistory.equals(moduleName)) {
            ArrayList<String> userIdArrays = new ArrayList<>();
            userIdArrays.add(cacheUserBean.getUserId());
            HistoryRequestBean requestBean = new HistoryRequestBean();
            if(queryCustomPersonBean!=null){
                requestBean.setDnis(queryCustomPersonBean.getMobile());
            }
            requestBean.setPage(page);
            requestBean.setSize(COMMON_PAGE_SIZE);
            requestBean.setUserIds(userIdArrays);
            requestBean.setBegin(0);
            requestBean.setEnd(endTime);
            jsonObject = JSONObject.parseObject(new Gson().toJson(requestBean));
        }
        return jsonObject;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void dealHttpRequestFail(String moduleName, BaseBean result) {
        super.dealHttpRequestFail(moduleName, result);
        srlRefresh.finishLoadMore();
        srlRefresh.finishRefresh();
    }

    @Override
    public void dealHttpRequestResult(String moduleName, BaseBean result, String response) {
        if (HttpRequest.CallHistory.callHistory.equals(moduleName)) {
            srlRefresh.finishLoadMore();
            srlRefresh.finishRefresh();
            if (!TextUtils.isEmpty(response)) {
                HistoryResponseBean historyResponseBean = (new Gson()).fromJson(response, HistoryResponseBean.class);
                if (historyResponseBean.getCode() == 0) {
                    if (historyResponseBeanList != null &&
                            historyResponseBean.getPage() != null &&
                            historyResponseBean.getPage().getContent() != null
                            && historyResponseBean.getPage().getContent().size() > 0) {
                        SharedPreferencesUtil.save(mContext, KTY_CC_BEGIN, String.valueOf(endTime));
                        SharedPreferencesUtil.save(mContext, VOICE_RECORD_PREFIX, historyResponseBean.getRecordPrefix());
                        //把查询到的直接添加到数据库
                        saveData(historyResponseBean.getPage().getContent());
                        LogUtils.e(historyResponseBean.getPage().getContent().toString());
                        if (page == 0) {
                            historyResponseBeanList.clear();
                            historyAdapter.notifyDataSetChanged();
                            historyResponseBeanList.addAll(historyResponseBean.getPage().getContent());
//                            historyResponseBeanList.addAll(0, historyResponseBean.getPage().getContent());
                        } else {
                            historyResponseBeanList.addAll(historyResponseBean.getPage().getContent());
                        }

                        historyAdapter.notifyDataSetChanged();
                    }
                }
            }

        }
    }
    private void saveData(List<ContentBean> list) {
        ThreadTask.getInstance().executorDBThread(new Runnable() {
            @Override
            public void run() {
                DbUtil.savePhoneRecordList(list);
            }
        }, 10);
    }


}
