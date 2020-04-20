package com.xxxx.tky.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fly.sweet.dialog.SweetAlertDialog;
import com.flyco.roundview.RoundTextView;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.xxxx.cc.base.fragment.BaseHttpRequestFragment;
import com.xxxx.cc.global.HttpRequest;
import com.xxxx.cc.model.BaseBean;
import com.xxxx.cc.model.CommunicationRecordBean;
import com.xxxx.cc.model.CommunicationRecordResponseBean;
import com.xxxx.cc.model.CommunicationRecordReturnResultBean;
import com.xxxx.cc.model.QueryCustomPersonBean;
import com.xxxx.cc.model.UserBean;
import com.xxxx.cc.util.LogUtils;
import com.xxxx.cc.util.SharedPreferencesUtil;
import com.xxxx.tky.R;
import com.xxxx.tky.adapter.CommunicationRecordAdapter;
import com.xxxx.tky.model.CommunicationRecordRequestBean;
import com.xxxx.tky.model.SaveSummaryBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.xxxx.cc.global.Constans.COMMON_PAGE_SIZE;
import static com.xxxx.cc.global.Constans.USERBEAN_SAVE_TAG;

/**
 * @author zhoufeng
 * @date 2020/2/7
 * @moduleName
 */
public class CommunicationRecordFragment extends BaseHttpRequestFragment {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.srl_refresh)
    SmartRefreshLayout srlRefresh;

    private CommunicationRecordAdapter communicationRecordAdapter;
    private List<CommunicationRecordResponseBean> contentBeanList = new ArrayList<>();
    private int page;

    private UserBean cacheUserBean;
    private QueryCustomPersonBean queryCustomPersonBean;
    public CommunicationRecordResponseBean selectCommunicationRecordResponseBean;

    public static CommunicationRecordFragment newInstance(String data) {
        CommunicationRecordFragment fragment = new CommunicationRecordFragment();
        Bundle bundle = new Bundle();
        bundle.putString("data", data);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public int getContentViewId() {
        return R.layout.fragment_communication_record;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        srlRefresh.setEnableLoadMore(true);
        srlRefresh.setEnableRefresh(false);
        srlRefresh.setEnableLoadMoreWhenContentNotFull(false);

        try {
            Object objectBean = SharedPreferencesUtil.getObjectBean(mContext, USERBEAN_SAVE_TAG, UserBean.class);
            if (objectBean != null) {
                cacheUserBean = (UserBean) objectBean;
            }

            if (cacheUserBean != null && recycler != null) {
                if (getArguments() != null) {
                    String data = getArguments().getString("data");
                    if (!TextUtils.isEmpty(data)) {
                        queryCustomPersonBean = JSON.parseObject(data, QueryCustomPersonBean.class);
                        if (queryCustomPersonBean != null) {
                            recycler.setLayoutManager(new LinearLayoutManager(mContext));
                            communicationRecordAdapter = new CommunicationRecordAdapter(contentBeanList);
                            recycler.setAdapter(communicationRecordAdapter);
                            communicationRecordAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                                @Override
                                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                                    if (R.id.edit_record == view.getId()) {
                                        //弹出修改框
                                        selectCommunicationRecordResponseBean = contentBeanList.get(position);
                                        showEditDialog();
                                    }
                                }
                            });

                            srlRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
                                @Override
                                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                                    page++;
                                    basePostPresenter.presenterBusinessByHeader(
                                            HttpRequest.CallHistory.calldetailWithCommRecords,
                                            false,
                                            "token", cacheUserBean.getToken(),
                                            "Content-Type", "application/json"
                                    );
                                }
                            });
                            basePostPresenter.presenterBusinessByHeader(
                                    HttpRequest.CallHistory.calldetailWithCommRecords,
                                    false,
                                    "token", cacheUserBean.getToken(),
                                    "Content-Type", "application/json"
                            );
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private SweetAlertDialog sweetAlertDialog;
    private EditText userMeno;

    public void showEditDialog() {
        if (sweetAlertDialog != null && sweetAlertDialog.isShowing()) return;
        sweetAlertDialog = new SweetAlertDialog(mContext);
        View dialogView = LayoutInflater.from(mContext)
                .inflate(R.layout.dialog_edit, null, false);
        sweetAlertDialog.setCustomView(dialogView);

        RoundTextView dialogConfirm = dialogView.findViewById(R.id.dialog_confirm);
        RoundTextView dialogCancel = dialogView.findViewById(R.id.dialog_cancel);
        ImageView ivClose = dialogView.findViewById(R.id.iv_close);
        userMeno = dialogView.findViewById(R.id.user_meno);

        if (selectCommunicationRecordResponseBean != null && !TextUtils.isEmpty(selectCommunicationRecordResponseBean.getCommunication())) {
            userMeno.setText(selectCommunicationRecordResponseBean.getCommunication());
            userMeno.setSelection(selectCommunicationRecordResponseBean.getCommunication().length());
        }
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sweetAlertDialog != null) {
                    sweetAlertDialog.dismiss();
                }
            }
        });

        dialogConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userMeno != null && userMeno.getText().toString().trim().equals("")) {
                    showToast("保存的沟通记录不能为空");
                    return;
                }
                if (sweetAlertDialog != null) {
                    sweetAlertDialog.dismiss();
                }

                if (cacheUserBean != null && selectCommunicationRecordResponseBean != null && selectCommunicationRecordResponseBean.getCalldetailId() != null) {
                    if (selectCommunicationRecordResponseBean.getId() != null) {
                        basePostPresenter.presenterBusinessByHeader(HttpRequest.Contant.updateSummary + selectCommunicationRecordResponseBean.getId(), true,
                                "token", cacheUserBean.getToken());

                    } else {
                        basePostPresenter.presenterBusinessByHeader(HttpRequest.Contant.saveSummary, true,
                                "token", cacheUserBean.getToken());
                    }


                } else {
                    showToast("未能查询到您的本次通话记录，保存沟通记录失败");
                }
            }
        });

        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sweetAlertDialog != null) {
                    sweetAlertDialog.dismiss();
                    selectCommunicationRecordResponseBean = null;
                }
            }
        });
        sweetAlertDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        basePostPresenter.presenterBusinessByHeader(
                HttpRequest.CallHistory.calldetailWithCommRecords,
                false,
                "token", cacheUserBean.getToken(),
                "Content-Type", "application/json"
        );
//        basePostPresenter.presenterBusinessByHeader(
//                HttpRequest.CallHistory.currentCalls + queryCustomPersonBean.getRealMobileNumber(),
//                false,
//                "token", cacheUserBean.getToken(),
//                "Accept", "application/json",
//                "Content-Type", "application/json"
//        );

    }

    @Override
    public JSONObject getHttpRequestParams(String moduleName) {
        JSONObject jsonObject = new JSONObject();
        if (HttpRequest.CallHistory.calldetailWithCommRecords.equals(moduleName)) {
            CommunicationRecordRequestBean requestBean = new CommunicationRecordRequestBean();
            requestBean.setPage(page);
            requestBean.setSize(COMMON_PAGE_SIZE);
            requestBean.setPhone(queryCustomPersonBean.getRealMobileNumber());
            jsonObject = JSONObject.parseObject(new Gson().toJson(requestBean));
        } else if (HttpRequest.Contant.saveSummary.equals(moduleName)) {
            SaveSummaryBean saveSummaryBean = new SaveSummaryBean();
            if (selectCommunicationRecordResponseBean != null && queryCustomPersonBean != null && userMeno != null) {
                saveSummaryBean.setCalldetailId(selectCommunicationRecordResponseBean.getCalldetailId());
                saveSummaryBean.setContactid(queryCustomPersonBean.getId());
                saveSummaryBean.setPhonenumber(queryCustomPersonBean.getRealMobileNumber());
                saveSummaryBean.setSummary(userMeno.getText().toString().trim());
            }
            jsonObject = JSONObject.parseObject(new Gson().toJson(saveSummaryBean));
        } else if ((HttpRequest.Contant.updateSummary + selectCommunicationRecordResponseBean.getId()).equals(moduleName)) {
            SaveSummaryBean saveSummaryBean = new SaveSummaryBean();
            if (userMeno != null) {
                saveSummaryBean.setSummary(userMeno.getText().toString().trim());
            }
            jsonObject = JSONObject.parseObject(new Gson().toJson(saveSummaryBean));
        }

        return jsonObject;
    }

    @Override
    public void dealHttpRequestFail(String moduleName, BaseBean result) {
        super.dealHttpRequestFail(moduleName, result);
        if ((HttpRequest.CallHistory.currentCalls + queryCustomPersonBean.getRealMobileNumber()).equals(moduleName)) {
            LogUtils.e("Code:" + result.getCode());
            if (contentBeanList.contains(currentCallsCommunicationRecordResponseBean)) {
                contentBeanList.remove(currentCallsCommunicationRecordResponseBean);
                communicationRecordAdapter.notifyDataSetChanged();
            }
            isCall = false;
            currentCallsCommunicationRecordResponseBean = null;
        } else {
            srlRefresh.finishLoadMore();
            srlRefresh.finishRefresh();
        }
    }

    CommunicationRecordResponseBean currentCallsCommunicationRecordResponseBean = null;
    boolean isCall = false;

    @Override
    public void dealHttpRequestResult(String moduleName, BaseBean result, String response) {
        if (HttpRequest.CallHistory.calldetailWithCommRecords.equals(moduleName)) {
            LogUtils.e("calldetailWithCommRecords:" + response);
            srlRefresh.finishLoadMore();
            srlRefresh.finishRefresh();
            if (!TextUtils.isEmpty(response)) {
                CommunicationRecordReturnResultBean historyResponseBean = (new Gson()).fromJson(response, CommunicationRecordReturnResultBean.class);
                LogUtils.e("Message:" + historyResponseBean.getMessage());
                LogUtils.e("Code" + historyResponseBean.getCode() + "historyResponseBean:" + historyResponseBean.getData().getContent().size());
                if (historyResponseBean.getCode() == 0) {
                    if (historyResponseBean.getData() != null &&
                            historyResponseBean.getData().getContent() != null
                            && historyResponseBean.getData().getContent().size() > 0) {
                        contentBeanList.clear();
                        communicationRecordAdapter.notifyDataSetChanged();
                        contentBeanList.addAll(historyResponseBean.getData().getContent());
                        LogUtils.e("isCall:" + isCall);
                        if (isCall && currentCallsCommunicationRecordResponseBean != null) {
                            contentBeanList.add(0, currentCallsCommunicationRecordResponseBean);
                        }
                        communicationRecordAdapter.notifyDataSetChanged();
                    }
                }
            }

        } else if ((HttpRequest.CallHistory.currentCalls + queryCustomPersonBean.getRealMobileNumber()).equals(moduleName)) {
            LogUtils.e("currentCalls:" + response);
//            if (!TextUtils.isEmpty(response)) {
//                CurrentCallsReturnResultBean currentCallsReturnResultBean = (new Gson()).fromJson(response, CurrentCallsReturnResultBean.class);
//                if (currentCallsReturnResultBean.getCode() == 0) {
//                    if (!isCall) {
//                        currentCallsCommunicationRecordResponseBean = new CommunicationRecordResponseBean();
//                        currentCallsCommunicationRecordResponseBean.setCallId(currentCallsReturnResultBean.getDateBean().getUuid());
//                        currentCallsCommunicationRecordResponseBean.setCreateTime(currentCallsReturnResultBean.getDateBean().getCallTime());
//                        currentCallsCommunicationRecordResponseBean.setBillingInSec(-1);
//                        contentBeanList.add(0, currentCallsCommunicationRecordResponseBean);
//                        communicationRecordAdapter.notifyDataSetChanged();
//                        LogUtils.e("contentBeanList:" + contentBeanList.size());
//                        selectCommunicationRecordResponseBean = currentCallsCommunicationRecordResponseBean;
//                        isCall = true;
//                    }
//                } else if (currentCallsReturnResultBean.getCode() == 1) {
//                    if (contentBeanList.contains(currentCallsCommunicationRecordResponseBean)) {
//                        contentBeanList.remove(currentCallsCommunicationRecordResponseBean);
//                        communicationRecordAdapter.notifyDataSetChanged();
//                    }
//                    isCall = false;
//                    currentCallsCommunicationRecordResponseBean = null;
//                }

//              }
        } else if (HttpRequest.Contant.saveSummary.equals(moduleName) || (HttpRequest.Contant.updateSummary + selectCommunicationRecordResponseBean.getId()).equals(moduleName)) {
            if (result != null && result.isOk()) {
                //刷新数据
                showToast("保存成功");
                CommunicationRecordBean responseBean = (new Gson()).fromJson(response, CommunicationRecordBean.class);

                selectCommunicationRecordResponseBean.setId(responseBean.getData().getId());
                selectCommunicationRecordResponseBean.setCommunication(responseBean.getData().getCommunication());
                basePostPresenter.presenterBusinessByHeader(
                        HttpRequest.CallHistory.calldetailWithCommRecords,
                        false,
                        "token", cacheUserBean.getToken(),
                        "Content-Type", "application/json"
                );

            }
        }
    }

    @Override
    public void onDestroy() {
        if (sweetAlertDialog != null) {
            sweetAlertDialog.dismiss();
        }
        super.onDestroy();
    }
}
