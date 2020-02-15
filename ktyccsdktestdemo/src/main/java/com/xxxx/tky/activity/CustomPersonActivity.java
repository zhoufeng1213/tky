package com.xxxx.tky.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.lqr.recyclerview.LQRRecyclerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.xxxx.cc.base.activity.BaseHttpRequestActivity;
import com.xxxx.cc.global.HttpRequest;
import com.xxxx.cc.global.KtyCcSdkTool;
import com.xxxx.cc.model.BaseBean;
import com.xxxx.cc.model.CustomPersonRequestBean;
import com.xxxx.cc.model.CustomPersonReturnResultBean;
import com.xxxx.cc.model.QueryCustomPersonBean;
import com.xxxx.cc.model.UserBean;
import com.xxxx.cc.util.LogUtils;
import com.xxxx.cc.util.SharedPreferencesUtil;
import com.xxxx.cc.util.ThreadTask;
import com.xxxx.cc.util.db.DbUtil;
import com.xxxx.tky.R;
import com.xxxx.tky.adapter.CustomPersonAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.xxxx.cc.global.Constans.COMMON_LOAD_PAGE_SIZE;
import static com.xxxx.cc.global.Constans.COMMON_PAGE_SIZE;
import static com.xxxx.cc.global.Constans.KTY_CC_BEGIN;
import static com.xxxx.cc.global.Constans.KTY_CUSTOM_BEGIN;
import static com.xxxx.cc.global.Constans.USERBEAN_SAVE_TAG;

/**
 * @author zhoufeng
 * @date 2020/2/3
 * @moduleName
 */
public class CustomPersonActivity extends BaseHttpRequestActivity {
    @BindView(R.id.recycler)
    LQRRecyclerView recycler;
    @BindView(R.id.srl_refresh)
    SmartRefreshLayout srlRefresh;

    @Override
    public int getLayoutViewId() {
        return R.layout.activity_custom_person;
    }

    @Override
    public Toolbar getToolbar() {
        return null;
    }

    @Override
    public String getToolBarTitle() {
        return null;
    }

    private CustomPersonAdapter historyAdapter;
    private List<QueryCustomPersonBean> historyResponseBeanList = new ArrayList<>();
    private int page;

    private UserBean cacheUserBean;
    private boolean isFirstRefrush = true;
    private long endTime = System.currentTimeMillis();
    private long beginTime = 0;


    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        srlRefresh.setEnableLoadMore(true);
        srlRefresh.setEnableRefresh(true);
        srlRefresh.setEnableLoadMoreWhenContentNotFull(false);

        init();
    }


    private void init() {
        try {
            Object objectBean = SharedPreferencesUtil.getObjectBean(mContext, USERBEAN_SAVE_TAG, UserBean.class);
            if (objectBean != null) {
                cacheUserBean = (UserBean) objectBean;
            }
            if (cacheUserBean != null) {
                recycler.setLayoutManager(new LinearLayoutManager(mContext));
                historyAdapter = new CustomPersonAdapter(historyResponseBeanList);
                historyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        //拨打电话
//                        if(!TextUtils.isEmpty(historyResponseBeanList.get(position).getName()) &&
//                                        !TextUtils.isEmpty(historyResponseBeanList.get(position).getRealMobileNumber())){
//                              CallPhoneTool.getInstance().callPhone(mContext, historyResponseBeanList.get(position).getRealMobileNumber(),
//                                    historyResponseBeanList.get(position).getName(),
//                                    ""
//                            );
//                        }
                        startActivity(CustomPesonDetailActivity.class, "data", JSON.toJSON(historyResponseBeanList.get(position)));
                    }
                });
                recycler.setAdapter(historyAdapter);


                srlRefresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
                    @Override
                    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                        page++;
                        List<QueryCustomPersonBean> list = DbUtil.queryCustomPersonBeanList(page);
                        if (list != null && list.size() > 0) {
                            srlRefresh.finishLoadMore();
                            srlRefresh.finishRefresh();
                            historyResponseBeanList.addAll(list);
                            historyAdapter.notifyDataSetChanged();
                        } else {
                            baseGetPresenter.presenterBusinessByHeader(
                                    HttpRequest.Contant.mineContacts,
                                    false,
                                    "token", cacheUserBean.getToken()
                            );
                        }
                    }

                    @Override
                    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                        page = 0;
//                        if(!isFirstRefrush){
                        endTime = System.currentTimeMillis();
                        historyResponseBeanList.clear();
                        historyAdapter.notifyDataSetChanged();
                        loadData();
//                        }

                        isFirstRefrush = false;

                        baseGetPresenter.presenterBusinessByHeader(
                                HttpRequest.Contant.mineContacts,
                                false,
                                "token", cacheUserBean.getToken()
                        );
                    }
                });
                loadData();
                srlRefresh.autoRefresh();
            } else {
                showToast("请先登录");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        //判断是否存在begin
        String beginStr = SharedPreferencesUtil.getValue(mContext, KTY_CUSTOM_BEGIN);
        if (!TextUtils.isEmpty(beginStr)) {
            try {
                beginTime = Long.valueOf(beginStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (beginTime > 0) {
            //先把本地数据查询出来
            List<QueryCustomPersonBean> list = DbUtil.queryCustomPersonBeanList(page);
            if (list != null && list.size() > 0) {
                historyResponseBeanList.addAll(list);
                historyAdapter.notifyDataSetChanged();
            }
        }
    }


    @Override
    public JSONObject getHttpRequestParams(String moduleName) {
        JSONObject jsonObject = new JSONObject();
        if (HttpRequest.Contant.mineContacts.equals(moduleName)) {
            CustomPersonRequestBean requestBean = new CustomPersonRequestBean();
            requestBean.setPage(page);
            requestBean.setSize(COMMON_LOAD_PAGE_SIZE);
            requestBean.setStarttime(beginTime);
            requestBean.setEndtime(endTime);
            jsonObject = JSONObject.parseObject(new Gson().toJson(requestBean));
        }
        return jsonObject;
    }

    @Override
    public void dealHttpRequestFail(String moduleName, BaseBean result) {
        super.dealHttpRequestFail(moduleName, result);
        srlRefresh.finishLoadMore();
        srlRefresh.finishRefresh();
    }

    @Override
    public void dealHttpRequestResult(String moduleName, BaseBean result, String response) {
        LogUtils.i("zwmn", "CustomPersonActivity 请求数据 " + response);
        if (HttpRequest.Contant.mineContacts.equals(moduleName)) {
            srlRefresh.finishLoadMore();
            srlRefresh.finishRefresh();
            if (!TextUtils.isEmpty(response)) {
                CustomPersonReturnResultBean historyResponseBean = (new Gson()).fromJson(response, CustomPersonReturnResultBean.class);
                if (historyResponseBean.getCode() == 0) {
                    if (historyResponseBeanList != null &&
                            historyResponseBean.getPage() != null &&
                            historyResponseBean.getPage().getContent() != null
                            && historyResponseBean.getPage().getContent().size() > 0) {
                        SharedPreferencesUtil.save(mContext, KTY_CC_BEGIN, String.valueOf(endTime));
                        //把查询到的直接添加到数据库
//                        saveData(historyResponseBean.getPage().getContent());
                        DbUtil.saveCustomPersonListOrUpdate(historyResponseBean.getPage().getContent());
//                        LogUtils.e(historyResponseBean.getPage().getContent().get(0).getName().toString());
                        if (page == 0) {
                            //直接去数据库重新查找数据
                            List<QueryCustomPersonBean> list = DbUtil.queryCustomPersonBeanList(page);
                            historyResponseBeanList.clear();
                            historyAdapter.notifyDataSetChanged();
                            if (list != null && list.size() > 0) {
                                historyResponseBeanList.addAll(list);
                            }
                        } else {
                            historyResponseBeanList.addAll(historyResponseBean.getPage().getContent());
                        }

                        historyAdapter.notifyDataSetChanged();
                    }
                }
            }

        }
    }


    private void saveData(List<QueryCustomPersonBean> list) {
        ThreadTask.getInstance().executorDBThread(new Runnable() {
            @Override
            public void run() {
                DbUtil.saveCustomPersonListOrUpdate(list);
            }
        }, 10);
    }


}
