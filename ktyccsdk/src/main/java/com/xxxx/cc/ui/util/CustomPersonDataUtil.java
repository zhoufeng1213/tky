package com.xxxx.cc.ui.util;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.xxxx.cc.base.presenter.MyStringCallback;
import com.xxxx.cc.global.Constans;
import com.xxxx.cc.global.HttpRequest;
import com.xxxx.cc.model.CustomPersonRequestBean;
import com.xxxx.cc.model.CustomPersonReturnResultBean;
import com.xxxx.cc.model.QueryCustomPersonBean;
import com.xxxx.cc.model.UserBean;
import com.xxxx.cc.util.LogUtils;
import com.xxxx.cc.util.SharedPreferencesUtil;
import com.xxxx.cc.util.TextUtil;
import com.xxxx.cc.util.ThreadTask;
import com.xxxx.cc.util.db.DbUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import static com.xxxx.cc.global.Constans.COMMON_PAGE_SIZE;
import static com.xxxx.cc.global.Constans.KTY_CUSTOM_BEGIN;

/**
 * @author zhoufeng
 * @date 2019/8/16
 * @moduleName
 */
public class CustomPersonDataUtil {


    private static volatile CustomPersonDataUtil httpCacheDataUtil;
    private Context mContext;

    private CustomPersonDataUtil(Context context) {
        this.mContext = context;
    }

    public static CustomPersonDataUtil getInstance(Context context) {
        if (httpCacheDataUtil == null) {
            synchronized (CustomPersonDataUtil.class) {
                if (httpCacheDataUtil == null) {
                    httpCacheDataUtil = new CustomPersonDataUtil(context);
                }
            }
        }
        return httpCacheDataUtil;
    }

    private int loadPage = 0;
    private long beginTime = 0;
    private long endTime = 0;
    private String token;
    private UserBean userBean;

    public void setQueryData(UserBean bean, long beginTime, long endTime, int loadPage) {
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.token = bean.getToken();
        this.userBean = bean;
        this.loadPage = loadPage;
    }

    public void loadAllNetData() {
        if (!TextUtils.isEmpty(token)) {
            ThreadTask.getInstance().executorNetThread(new Runnable() {
                @Override
                public void run() {
                    LogUtils.e("客户资料--》正在加载数据" + loadPage);
                    requestPost();
                }
            }, 10);
        }
    }


    private void requestPost() {
        GetBuilder okHttpUtils = OkHttpUtils.get();
        okHttpUtils.url(Constans.BASE_URL + HttpRequest.Contant.mineContacts);
        //添加header
        okHttpUtils.addHeader("token", token);
        //添加请求参数
        Map mapParam = JSON.parseObject(getHttpRequestParams().toJSONString(),
                Map.class);
        for (Object key : mapParam.keySet()) {
            okHttpUtils.addParams((String) key, mapParam.get(key) == null ? "" : String.valueOf(mapParam.get(key)));
        }
        LogUtils.e("url:"+Constans.BASE_URL+HttpRequest.Contant.mineContacts+"，Params:"+mapParam.toString());
        okHttpUtils
                .build()
                .execute(new MyStringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        LogUtils.e("加载失败");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        dealResult(response);
                    }
                });
    }

    private void dealResult(String response) {
        if (!TextUtils.isEmpty(response)) {
            LogUtils.e(response);
            CustomPersonReturnResultBean historyResponseBean = (new Gson()).fromJson(response, CustomPersonReturnResultBean.class);
            if (historyResponseBean.getCode() == 0) {

                if (historyResponseBean.getPage() != null) {
                    int totalPage = historyResponseBean.getPage().getTotalPages();
                    if (historyResponseBean.getPage().getContent() != null && historyResponseBean.getPage().getContent().size() > 0) {
                        //把查询到的直接添加到数据库
                        //循环吧拼音放进去
                        List<QueryCustomPersonBean> tempList = new ArrayList<>();
                        for (QueryCustomPersonBean bean : historyResponseBean.getPage().getContent()) {
                            if (bean != null) {
                                bean.setLetters(TextUtil.getNameFirstChar(bean.getName()));
                                bean.setDisplayNameSpelling(TextUtil.getNameToPinyin(bean.getName()));
                                tempList.add(bean);
                            }
                        }
                        saveCacheData(tempList);
                    }
                    if (loadPage >= totalPage) {
                        LogUtils.e("客户资料--》缓存数据成功");
                        SharedPreferencesUtil.save(mContext, KTY_CUSTOM_BEGIN, String.valueOf(endTime));
                    } else {
                        loadPage++;
                        loadAllNetData();
                    }

                }
            }
        }
    }


    private void saveCacheData(List<QueryCustomPersonBean> list) {
        ThreadTask.getInstance().executorDBThread(new Runnable() {
            @Override
            public void run() {
                DbUtil.saveCustomPersonListOrUpdate(list);
            }
        }, 10);
    }


    public JSONObject getHttpRequestParams() {
        JSONObject jsonObject = new JSONObject();
        CustomPersonRequestBean requestBean = new CustomPersonRequestBean();
        requestBean.setPage(loadPage);
        requestBean.setSize(COMMON_PAGE_SIZE);
        requestBean.setStarttime(beginTime);
        requestBean.setEndtime(endTime);
        jsonObject = JSONObject.parseObject(new Gson().toJson(requestBean));
        return jsonObject;
    }

}
