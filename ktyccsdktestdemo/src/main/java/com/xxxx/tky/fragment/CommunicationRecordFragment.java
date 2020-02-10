package com.xxxx.tky.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
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
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.xxxx.cc.base.fragment.BaseHttpRequestFragment;
import com.xxxx.cc.global.HttpRequest;
import com.xxxx.cc.model.BaseBean;
import com.xxxx.cc.model.BaseContactBean;
import com.xxxx.cc.model.CommunicationRecordReturnResultBean;
import com.xxxx.cc.model.QueryCustomPersonBean;
import com.xxxx.cc.model.UserBean;
import com.xxxx.cc.util.LogUtils;
import com.xxxx.cc.util.SharedPreferencesUtil;
import com.xxxx.tky.R;
import com.xxxx.tky.adapter.CommunicationRecordAdapter;
import com.xxxx.tky.model.CommunicationRecordRequestBean;
import com.xxxx.cc.model.CommunicationRecordResponseBean;
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
    private List<CommunicationRecordResponseBean>  contentBeanList = new ArrayList<>();
    private int page;

    private UserBean cacheUserBean;
    private QueryCustomPersonBean queryCustomPersonBean;
    private CommunicationRecordResponseBean selectCommunicationRecordResponseBean;

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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        srlRefresh.setEnableLoadMore(true);
        srlRefresh.setEnableRefresh(false);
        srlRefresh.setEnableLoadMoreWhenContentNotFull(false);

        try {
            Object objectBean = SharedPreferencesUtil.getObjectBean(mContext, USERBEAN_SAVE_TAG, UserBean.class);
            if (objectBean != null) {
                cacheUserBean = (UserBean) objectBean;
            }

            if (cacheUserBean != null) {
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
                                    if(R.id.edit_record == view.getId()){
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
                                            HttpRequest.Contant.communicationRecord,
                                            false,
                                            "token", cacheUserBean.getToken(),
                                            "Content-Type", "application/json"
                                    );
                                }
                            });
                            basePostPresenter.presenterBusinessByHeader(
                                    HttpRequest.Contant.communicationRecord,
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
    private void showEditDialog(){
        sweetAlertDialog = new SweetAlertDialog(mContext);
        View dialogView = LayoutInflater.from(mContext)
                .inflate(R.layout.dialog_edit, null, false);
        sweetAlertDialog.setCustomView(dialogView);

        RoundTextView dialogConfirm = dialogView.findViewById(R.id.dialog_confirm);
        RoundTextView dialogCancel = dialogView.findViewById(R.id.dialog_cancel);
        ImageView ivClose = dialogView.findViewById(R.id.iv_close);
        userMeno = dialogView.findViewById(R.id.user_meno);
        if(selectCommunicationRecordResponseBean != null && !TextUtils.isEmpty(selectCommunicationRecordResponseBean.getCommRecoeds())){
            userMeno.setText(selectCommunicationRecordResponseBean.getCommRecoeds());
            userMeno.setSelection(selectCommunicationRecordResponseBean.getCommRecoeds().length());
        }
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sweetAlertDialog != null){
                    sweetAlertDialog.dismiss();
                }
            }
        });

        dialogConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sweetAlertDialog != null){
                    sweetAlertDialog.dismiss();
                }
                if(cacheUserBean != null){
                    basePostPresenter.presenterBusinessByHeader(HttpRequest.Contant.saveSummary,true,
                            "token",cacheUserBean.getToken());
                }
            }
        });

        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sweetAlertDialog != null){
                    sweetAlertDialog.dismiss();
                }
            }
        });
        sweetAlertDialog.show();
    }


    @Override
    public JSONObject getHttpRequestParams(String moduleName) {
        JSONObject jsonObject = new JSONObject();
        if (HttpRequest.Contant.communicationRecord.equals(moduleName)) {
            CommunicationRecordRequestBean requestBean = new CommunicationRecordRequestBean();
            requestBean.setPage(page);
            requestBean.setSize(COMMON_PAGE_SIZE);
            requestBean.setPhone(queryCustomPersonBean.getRealMobileNumber());
            jsonObject = JSONObject.parseObject(new Gson().toJson(requestBean));
        }else if(HttpRequest.Contant.saveSummary.equals(moduleName)){
            SaveSummaryBean saveSummaryBean = new SaveSummaryBean();
            if(selectCommunicationRecordResponseBean != null && queryCustomPersonBean != null && userMeno != null){
                saveSummaryBean.setCalldetailId(selectCommunicationRecordResponseBean.getCallId());
                saveSummaryBean.setContactid(queryCustomPersonBean.getId());
                saveSummaryBean.setPhonenumber(queryCustomPersonBean.getRealMobileNumber());
                saveSummaryBean.setSummary(userMeno.getText().toString().trim());
            }
            jsonObject = JSONObject.parseObject(new Gson().toJson(saveSummaryBean));
        }
        LogUtils.e(jsonObject.toJSONString());
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
        if (HttpRequest.Contant.communicationRecord.equals(moduleName)) {
            srlRefresh.finishLoadMore();
            srlRefresh.finishRefresh();
            if (!TextUtils.isEmpty(response)) {
                LogUtils.e(response);
                CommunicationRecordReturnResultBean historyResponseBean = (new Gson()).fromJson(response, CommunicationRecordReturnResultBean.class);
                if (historyResponseBean.getCode() == 0) {
                    if (historyResponseBean.getPage() != null &&
                            historyResponseBean.getPage().getContent() != null
                            && historyResponseBean.getPage().getContent().size() > 0) {
                        contentBeanList.addAll(historyResponseBean.getPage().getContent());
                        communicationRecordAdapter.notifyDataSetChanged();
                    }
                }
            }

        }else  if (HttpRequest.Contant.saveSummary.equals(moduleName)) {
            if(result != null && result.isOk()){
                //刷新数据
                showToast("保存成功");
                contentBeanList.clear();
                communicationRecordAdapter.notifyDataSetChanged();

                basePostPresenter.presenterBusinessByHeader(
                        HttpRequest.Contant.communicationRecord,
                        false,
                        "token", cacheUserBean.getToken(),
                        "Content-Type", "application/json"
                );

            }
        }
    }

    @Override
    public void onDestroy() {
        if(sweetAlertDialog != null){
            sweetAlertDialog.dismiss();
        }
        super.onDestroy();
    }
}
