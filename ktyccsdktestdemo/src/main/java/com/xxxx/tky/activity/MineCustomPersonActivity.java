package com.xxxx.tky.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.lqr.adapter.LQRAdapterForRecyclerView;
import com.lqr.adapter.LQRHeaderAndFooterAdapter;
import com.lqr.adapter.LQRViewHolderForRecyclerView;
import com.lqr.recyclerview.LQRRecyclerView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.xxxx.cc.base.activity.BaseHttpRequestActivity;
import com.xxxx.cc.global.Constans;
import com.xxxx.cc.global.HttpRequest;
import com.xxxx.cc.model.BaseBean;
import com.xxxx.cc.model.CustomPersonRequestBean;
import com.xxxx.cc.model.CustomPersonReturnResultBean;
import com.xxxx.cc.model.QueryCustomPersonBean;
import com.xxxx.cc.model.UserBean;
import com.xxxx.cc.util.LogUtils;
import com.xxxx.cc.util.SharedPreferencesUtil;
import com.xxxx.cc.util.TextUtil;
import com.xxxx.cc.util.TimeUtils;
import com.xxxx.cc.util.db.DbUtil;
import com.xxxx.tky.R;
import com.xxxx.tky.util.AntiShakeUtils;
import com.xxxx.tky.widget.QuickIndexBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.xxxx.cc.global.Constans.COMMON_LOAD_PAGE_SIZE;
import static com.xxxx.cc.global.Constans.KTY_CC_BEGIN;
import static com.xxxx.cc.global.Constans.KTY_CUSTOM_BEGIN;
import static com.xxxx.cc.global.Constans.USERBEAN_SAVE_TAG;

/**
 * @author zhoufeng
 * @date 2020/2/3
 * @moduleName
 */
public class MineCustomPersonActivity extends BaseHttpRequestActivity {


    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.search_edit)
    EditText searchEdit;
    @BindView(R.id.search_btn)
    ImageView searchBtn;
    @BindView(R.id.recycler)
    LQRRecyclerView recycler;
    @BindView(R.id.srl_refresh)
    SmartRefreshLayout srlRefresh;
    @BindView(R.id.qib)
    QuickIndexBar qib;
    @BindView(R.id.tvLetter)
    TextView tvLetter;

    @Override
    public int getLayoutViewId() {
        return R.layout.activity_mine_custom_person;
    }

    private List<QueryCustomPersonBean> historyResponseBeanList = new ArrayList<>();
    //    private List<ShowMineCustomPersonBean> showMineCustomPersonBeanList = new ArrayList<>();
    private int page;

    private UserBean cacheUserBean;
    private long endTime = System.currentTimeMillis();
    private long beginTime = 0;

    @Override
    public boolean isAddImmersionBar() {
        return false;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ImmersionBar.with(this).init();
        srlRefresh.setEnableLoadMore(false);
        srlRefresh.setEnableRefresh(true);
        srlRefresh.setEnableLoadMoreWhenContentNotFull(false);

        init();
        //获取客户可自定义项(所有客户相同)
        getAllItems();
    }

    private void getAllItems() {
        baseGetPresenter.presenterBusinessByHeader(
                HttpRequest.Contant.getSubItem,
                "token", cacheUserBean.getToken());
    }


    private void init() {
        try {
            Object objectBean = SharedPreferencesUtil.getObjectBean(mContext, USERBEAN_SAVE_TAG, UserBean.class);
            if (objectBean != null) {
                cacheUserBean = (UserBean) objectBean;
            }
            if (cacheUserBean != null) {
                initAdapter();
                srlRefresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
                    @Override
                    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                        page++;
//                        List<QueryCustomPersonBean> list = DbUtil.queryCustomPersonBeanList(page);
                        List<QueryCustomPersonBean> list = DbUtil.queryCustomPersonBeanAllList();
                        if (list != null && list.size() > 0) {
                            srlRefresh.finishLoadMore();
                            srlRefresh.finishRefresh();
                            historyResponseBeanList.addAll(list);
                        }
                    }

                    @Override
                    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                        page = 0;
                        endTime = System.currentTimeMillis();
                        baseGetPresenter.presenterBusinessByHeader(
                                HttpRequest.Contant.mineContacts,
                                false,
                                "token", cacheUserBean.getToken()
                        );
                    }
                });
                loadData();
                srlRefresh.autoRefresh();


                qib.setOnLetterUpdateListener(new QuickIndexBar.OnLetterUpdateListener() {
                    @Override
                    public void onLetterUpdate(String letter) {
                        try {
                            //显示对话框
                            showLetter(letter);
                            if ("#".equalsIgnoreCase(letter)) {
                                recycler.moveToPosition(0);
                            } else {
                                for (int i = 0; i < historyResponseBeanList.size(); i++) {
                                    QueryCustomPersonBean friend = historyResponseBeanList.get(i);
                                    if (!TextUtils.isEmpty(friend.getDisplayNameSpelling())) {
                                        String c = friend.getDisplayNameSpelling().charAt(0) + "";
                                        if (c.equalsIgnoreCase(letter)) {
                                            LogUtils.e("查找了----" + c);
                                            recycler.moveToPosition(i);
                                            break;
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            LogUtils.e("查找了----bug");
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onLetterCancel() {
                        LogUtils.e("查找了----onLetterCancel");
                        //隐藏对话框
                        hideLetter();
                    }
                });
            } else {
                showToast("请先登录");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private LQRHeaderAndFooterAdapter mAdapter;

    private void initAdapter() {
        LQRAdapterForRecyclerView adapter = new LQRAdapterForRecyclerView<QueryCustomPersonBean>(mContext,
                historyResponseBeanList,
                R.layout.item_mine_custom_person) {
            @Override
            public void convert(LQRViewHolderForRecyclerView helper, QueryCustomPersonBean item, int position) {
                try {
                    helper.setText(R.id.user_name, item.getName());
                    helper.setText(R.id.update_time, TimeUtils.stringToDate_MD_HMS(item.getUpdatetime()));
                    helper.setText(R.id.phone_num, item.getRealMobileNumber());

                    TextView letter = helper.getView(R.id.tvLetter);
                    letter.setText(item.getLetters());
                    letter.setVisibility(View.VISIBLE);
                    if (helper.getAdapterPosition() > 0) {
                        int lastPosition = helper.getAdapterPosition() - 1;
                        if (lastPosition >= 0 && lastPosition < helper.getAdapterPosition() && getData() != null
                                && getData().size() > 0) {
                            QueryCustomPersonBean lastBean = getData().get(lastPosition);
                            if (lastBean != null) {
                                String lastTime = lastBean.getLetters();
                                if (!TextUtils.isEmpty(lastTime) && !TextUtils.isEmpty(item.getLetters()) &&
                                        lastTime.equals(item.getLetters())
                                ) {
                                    letter.setVisibility(View.INVISIBLE);
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
        recycler.setAdapter(mAdapter);

        ((LQRAdapterForRecyclerView) mAdapter.getInnerAdapter()).setOnItemClickListener((lqrViewHolder, viewGroup, view, i) -> {
            if (AntiShakeUtils.isInvalidClick(view)) {
                return;
            }
            startActivity(CustomPesonDetailActivity.class, "data", JSON.toJSON(historyResponseBeanList.get(i)));

        });

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
//            List<QueryCustomPersonBean> list = DbUtil.queryCustomPersonBeanList(page);
            List<QueryCustomPersonBean> list = DbUtil.queryCustomPersonBeanAllList();
            if (list != null && list.size() > 0) {
                historyResponseBeanList.addAll(list);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

//
//    private List<ShowMineCustomPersonBean> getShowMineCustomPersonBeanList(List<QueryCustomPersonBean> list){
//        List<ShowMineCustomPersonBean> tempList = new ArrayList<>();
//        if(list != null && list.size() > 0){
//            for(QueryCustomPersonBean bean : list){
//                if(bean != null){
//                    tempList.add(new ShowMineCustomPersonBean(
//                            bean.getId(),
//                            bean.getName(),
//                            bean.getRealMobileNumber(),
//                            "",
//                            bean.getUpdatetime(),
//                            TextUtil.getNameFirstChar(bean.getName()),
//                            TextUtil.getNameToPinyin(bean.getName())
//                    ));
//                }
//            }
//        }
//        return tempList;
//    }


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
        if (HttpRequest.Contant.getSubItem.equals(moduleName)) {
//            SharedPreferencesUtil.save(getApplicationContext(), Constans.SP_DEFINED_ITEM_KEY, "");
        } else {
            srlRefresh.finishLoadMore();
            srlRefresh.finishRefresh();
        }

    }

    @Override
    public void dealHttpRequestResult(String moduleName, BaseBean result, String response) {
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

                        List<QueryCustomPersonBean> tempList = new ArrayList<>();
                        for (QueryCustomPersonBean bean : historyResponseBean.getPage().getContent()) {
                            if (bean != null) {
                                bean.setLetters(TextUtil.getNameFirstChar(bean.getName()));
                                bean.setDisplayNameSpelling(TextUtil.getNameToPinyin(bean.getName()));
                                tempList.add(bean);
                            }
                        }

                        DbUtil.saveCustomPersonListOrUpdate(tempList);
                        if (page == 0) {
                            //直接去数据库重新查找数据
//                            List<QueryCustomPersonBean> list = DbUtil.queryCustomPersonBeanList(page);
                            List<QueryCustomPersonBean> list = DbUtil.queryCustomPersonBeanAllList();
                            historyResponseBeanList.clear();
                            mAdapter.notifyDataSetChanged();
                            if (list != null && list.size() > 0) {
                                historyResponseBeanList.addAll(list);
//                                showMineCustomPersonBeanList.addAll(getShowMineCustomPersonBeanList(list));
                            }
                        } else {
                            historyResponseBeanList.addAll(historyResponseBean.getPage().getContent());
//                            showMineCustomPersonBeanList.addAll(getShowMineCustomPersonBeanList(historyResponseBean.getPage().getContent()));
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

        } else if (HttpRequest.Contant.getSubItem.equals(moduleName)) {
            if (result.isOk()) {
                LogUtils.i("zwmn", "获取items:" + response);
                SharedPreferencesUtil.save(getApplicationContext(), Constans.SP_DEFINED_ITEM_KEY, response);

            } else {
                LogUtils.i("zwmn", "获取item失败");
                SharedPreferencesUtil.save(getApplicationContext(), Constans.SP_DEFINED_ITEM_KEY, "");
            }
        }
    }

    private void showLetter(String letter) {
        tvLetter.setVisibility(View.VISIBLE);
        tvLetter.setText(letter);
    }

    private void hideLetter() {
        tvLetter.setVisibility(View.GONE);
    }


    @OnClick(R.id.iv_close)
    public void back(View view) {
        if (AntiShakeUtils.isInvalidClick(view)) {
            return;
        }
        finish();
    }

    @OnClick(R.id.search_btn)
    public void search(View view) {
        if (AntiShakeUtils.isInvalidClick(view)) {
            return;
        }
        if (!TextUtils.isEmpty(searchEdit.getText().toString().trim())) {
            List<QueryCustomPersonBean> list = DbUtil.queryCustomPersonBeanAllListByNameOrPhone(searchEdit.getText().toString().trim());
            if (list != null && list.size() > 0) {
                historyResponseBeanList.clear();
                mAdapter.notifyDataSetChanged();
                historyResponseBeanList.addAll(list);
                mAdapter.notifyDataSetChanged();
            } else {
                historyResponseBeanList.clear();
                mAdapter.notifyDataSetChanged();
            }
        } else {
            List<QueryCustomPersonBean> list = DbUtil.queryCustomPersonBeanAllList();
            if (list != null && list.size() > 0) {
                historyResponseBeanList.clear();
                mAdapter.notifyDataSetChanged();
                historyResponseBeanList.addAll(list);
                mAdapter.notifyDataSetChanged();
            }
        }
    }


    boolean isFirstLoad = true;

    @Override
    protected void onResume() {
        super.onResume();
        //查找本地的数据库
        if (isFirstLoad) {
            isFirstLoad = false;
        } else {
            if (!TextUtils.isEmpty(searchEdit.getText().toString().trim())) {
                List<QueryCustomPersonBean> list = DbUtil.queryCustomPersonBeanAllListByNameOrPhone(searchEdit.getText().toString().trim());
                if (list != null && list.size() > 0) {
                    historyResponseBeanList.clear();
                    mAdapter.notifyDataSetChanged();
                    historyResponseBeanList.addAll(list);
                    mAdapter.notifyDataSetChanged();
                } else {
                    historyResponseBeanList.clear();
                    mAdapter.notifyDataSetChanged();
                }
            } else {
                List<QueryCustomPersonBean> list = DbUtil.queryCustomPersonBeanAllList();
                if (list != null && list.size() > 0) {
                    historyResponseBeanList.clear();
                    mAdapter.notifyDataSetChanged();
                    historyResponseBeanList.addAll(list);
                    mAdapter.notifyDataSetChanged();
                }
            }
        }
    }


    @Override
    protected void onDestroy() {
        ImmersionBar.with(this).destroy();
        super.onDestroy();
    }
}
