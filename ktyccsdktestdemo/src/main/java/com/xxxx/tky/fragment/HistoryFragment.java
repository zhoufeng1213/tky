package com.xxxx.tky.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.xxxx.cc.base.fragment.BaseHttpRequestFragment;
import com.xxxx.cc.global.HttpRequest;
import com.xxxx.cc.global.KtyCcSdkTool;
import com.xxxx.cc.model.BaseBean;
import com.xxxx.cc.model.ContentBean;
import com.xxxx.cc.model.HistoryRequestBean;
import com.xxxx.cc.model.HistoryResponseBean;
import com.xxxx.cc.model.UserBean;
import com.xxxx.cc.ui.CommunicationDetailActivity;
import com.xxxx.cc.ui.adapter.HistoryAdapter;
import com.xxxx.cc.util.LogUtils;
import com.xxxx.cc.util.SharedPreferencesUtil;
import com.xxxx.cc.util.ThreadTask;
import com.xxxx.cc.util.db.DbUtil;
import com.xxxx.tky.R;

import java.util.ArrayList;
import java.util.List;

import static com.xxxx.cc.global.Constans.COMMON_PAGE_SIZE;
import static com.xxxx.cc.global.Constans.KTY_CC_BEGIN;
import static com.xxxx.cc.global.Constans.USERBEAN_SAVE_TAG;
import static com.xxxx.cc.global.Constans.VOICE_RECORD_PREFIX;

/**
 * @author zhoufeng
 * @date 2019/8/12
 * @moduleName
 */
public class HistoryFragment extends BaseHttpRequestFragment {

    RecyclerView recycler;
    SmartRefreshLayout srlRefresh;
    LinearLayout lMainContainer;

    private HistoryAdapter historyAdapter;
    private List<ContentBean> historyResponseBeanList = new ArrayList<>();
    private int page;

    private UserBean cacheUserBean;
    private boolean isFirstRefrush = true;
    private long endTime = System.currentTimeMillis();
    private long beginTime = 0;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_history;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recycler = view.findViewById(R.id.recycler);
        srlRefresh = view.findViewById(R.id.srl_refresh);
        lMainContainer = view.findViewById(R.id.ll_main_container);

        srlRefresh.setEnableLoadMore(true);
        srlRefresh.setEnableRefresh(true);
        srlRefresh.setEnableLoadMoreWhenContentNotFull(false);

        init();
    }

    private void init(){
        try {
            Object objectBean = SharedPreferencesUtil.getObjectBean(mContext, USERBEAN_SAVE_TAG, UserBean.class);
            if (objectBean != null) {
                cacheUserBean = (UserBean) objectBean;
            }
            if (cacheUserBean != null) {
                recycler.setLayoutManager(new LinearLayoutManager(mContext));
                historyAdapter = new HistoryAdapter(historyResponseBeanList);
                historyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        //拨打电话
                        ContentBean contentBean = historyResponseBeanList.get(position);
                        KtyCcSdkTool.getInstance().callPhone(mContext,contentBean.getDnis(),
                                contentBean.getContactName(),
                                ""
                        );
                    }
                });
                historyAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                    @Override
                    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                        if (view.getId() == R.id.more) {
                            showPopupWidow(historyResponseBeanList.get(position));
                        }
                    }
                });
                recycler.setAdapter(historyAdapter);


                srlRefresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
                    @Override
                    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                        page++;
                        List<ContentBean> list = DbUtil.queryPhoneRecordList(cacheUserBean.getUserId(),page);
                        if(list != null && list.size() > 0){
                            srlRefresh.finishLoadMore();
                            srlRefresh.finishRefresh();
                            historyResponseBeanList.addAll(list);
                            historyAdapter.notifyDataSetChanged();
                        }else{
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
                        if(!isFirstRefrush){
                            endTime = System.currentTimeMillis();
                            historyResponseBeanList.clear();
                            historyAdapter.notifyDataSetChanged();
                            loadData();
                        }

                        isFirstRefrush = false;

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
            } else {
                showToast("请先登录");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadData(){
        //判断是否存在begin
        String beginStr = SharedPreferencesUtil.getValue(mContext,KTY_CC_BEGIN);
        if(!TextUtils.isEmpty(beginStr)){
            try {
                beginTime = Long.valueOf(beginStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(beginTime >0){
            //先把本地数据查询出来
            List<ContentBean> list = DbUtil.queryPhoneRecordList(cacheUserBean.getUserId(),page);
            if(list != null && list.size() > 0){
                historyResponseBeanList.addAll(list);
                historyAdapter.notifyDataSetChanged();
            }
        }
    }

    private BottomSheetDialog bottomSheetDialog;

    private void showPopupWidow(ContentBean contentBean) {
        bottomSheetDialog = new BottomSheetDialog(mContext);
        bottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        View view = View.inflate(mContext, R.layout.widget_popupwindow, null);
        bottomSheetDialog.setContentView(view);

        TextView viewCommunicationDetails = view.findViewById(R.id.view_communication_details);
        viewCommunicationDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                Intent detailsIntent = new Intent(mContext, CommunicationDetailActivity.class);
                detailsIntent.putExtra("content", JSON.toJSONString(contentBean));
                startActivity(detailsIntent);
            }
        });
        TextView popPhoneNum = view.findViewById(R.id.popupwindow_phone_num);
        if (null != contentBean.getDnis()) {
            popPhoneNum.setText(contentBean.getDnis());
        }
        TextView cancel = view.findViewById(R.id.popupwindow_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();


//        CustomPopWindow customPopWindow = new CustomPopWindow(mContext, contentBean);
//        customPopWindow.showAtLocation(lMainContainer, 80, 0, 0);
//        customPopWindow.backgroundAlpha(mActivity, 0.7F);
//        customPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public final void onDismiss() {
//                customPopWindow.backgroundAlpha(mActivity, 1.0F);
//            }
//        });
//        customPopWindow.setOnPopWindowItemClickListener(new CustomPopWindow.OnPopWindowItemClickListener() {
//            @Override
//            public final void onPopItemClick() {
//                Intent detailsIntent = new Intent(mContext, CommunicationDetailActivity.class);
//                detailsIntent.putExtra("content", JSON.toJSONString(contentBean));
//                startActivity(detailsIntent);
//            }
//        });
    }

    @Override
    public JSONObject getHttpRequestParams(String moduleName) {
        JSONObject jsonObject = new JSONObject();
        if (HttpRequest.CallHistory.callHistory.equals(moduleName)) {
            ArrayList<String> userIdArrays = new ArrayList<>();
            userIdArrays.add(cacheUserBean.getUserId());
            HistoryRequestBean requestBean = new HistoryRequestBean();
            requestBean.setPage(page);
            requestBean.setSize(COMMON_PAGE_SIZE);
            requestBean.setUserIds(userIdArrays);
            requestBean.setBegin(beginTime);
            requestBean.setEnd(endTime);
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
                        SharedPreferencesUtil.save(mContext,KTY_CC_BEGIN,String.valueOf(endTime));
                        SharedPreferencesUtil.save(mContext, VOICE_RECORD_PREFIX, historyResponseBean.getRecordPrefix());
                        //把查询到的直接添加到数据库
                        saveData(historyResponseBean.getPage().getContent());
                        LogUtils.e(historyResponseBean.getPage().getContent().toString());
                        if(page == 0){
                            historyResponseBeanList.addAll(0,historyResponseBean.getPage().getContent());
                        }else{
                            historyResponseBeanList.addAll(historyResponseBean.getPage().getContent());
                        }

                        historyAdapter.notifyDataSetChanged();
                    }
                }
            }

        }
    }


    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
        return fragment;
    }

    private void saveData(List<ContentBean> list){
        ThreadTask.getInstance().executorDBThread(new Runnable() {
            @Override
            public void run() {
                DbUtil.savePhoneRecordList(list);
            }
        }, 10);
    }
}
