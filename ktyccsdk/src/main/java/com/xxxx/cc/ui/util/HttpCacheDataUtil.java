package com.xxxx.cc.ui.util;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.xxxx.cc.base.presenter.MyStringCallback;
import com.xxxx.cc.global.Constans;
import com.xxxx.cc.global.HttpRequest;
import com.xxxx.cc.model.ContentBean;
import com.xxxx.cc.model.HistoryRequestBean;
import com.xxxx.cc.model.HistoryResponseBean;
import com.xxxx.cc.util.LogUtils;
import com.xxxx.cc.util.SharedPreferencesUtil;
import com.xxxx.cc.util.ThreadTask;
import com.xxxx.cc.util.db.DbUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostStringBuilder;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;

import static com.xxxx.cc.global.Constans.COMMON_LOAD_PAGE_SIZE;
import static com.xxxx.cc.global.Constans.KTY_CC_BEGIN;
import static com.xxxx.cc.global.Constans.VOICE_RECORD_PREFIX;

/**
 * @author zhoufeng
 * @date 2019/8/16
 * @moduleName
 */
public class HttpCacheDataUtil {


    private static volatile HttpCacheDataUtil httpCacheDataUtil;
    private Context mContext;

    private HttpCacheDataUtil(Context context){
        this.mContext = context;
    }

    public static HttpCacheDataUtil getInstance(Context context){
        if(httpCacheDataUtil == null){
            synchronized (HttpCacheDataUtil.class){
                if(httpCacheDataUtil == null){
                    httpCacheDataUtil = new HttpCacheDataUtil(context);
                }
            }
        }
        return httpCacheDataUtil;
    }

    private int loadPage = 0;
    private long beginTime = 0;
    private long endTime = 0;
    private String userId;
    private String token;

    public void setQueryData(long beginTime, long endTime, String userId, String token) {
        this.beginTime = beginTime;
        this.endTime = endTime;
        this.userId = userId;
        this.token = token;
    }

    public void loadAllNetData(){
        if(!TextUtils.isEmpty(token) && !TextUtils.isEmpty(userId)){
            ThreadTask.getInstance().executorNetThread(new Runnable() {
                @Override
                public void run() {
                    LogUtils.e("正在加载数据"+loadPage);
                    requestPost();
                }
            }, 10);
        }
    }


    private void requestPost(){
        PostStringBuilder okHttpUtils = OkHttpUtils.postString();
        okHttpUtils.url(Constans.BASE_URL + HttpRequest.CallHistory.callHistory);
        //添加header
        okHttpUtils.addHeader("token", token);
        okHttpUtils.addHeader("Content-Type", "application/json");
        LogUtils.e("url:"+ Constans.BASE_URL +HttpRequest.CallHistory.callHistory+"，Params:"+getHttpPostParams());
        okHttpUtils
                .content(getHttpPostParams())
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
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

    private void dealResult(String response){
        if (!TextUtils.isEmpty(response)) {
            HistoryResponseBean historyResponseBean = (new Gson()).fromJson(response, HistoryResponseBean.class);
            if (historyResponseBean.getCode() == 0) {

                if (historyResponseBean.getPage() != null) {
                    int totalPage = historyResponseBean.getPage().getTotalPages();

                    SharedPreferencesUtil.save(mContext, VOICE_RECORD_PREFIX, historyResponseBean.getRecordPrefix());
                    if( historyResponseBean.getPage().getContent() != null && historyResponseBean.getPage().getContent().size()>0){
                        //把查询到的直接添加到数据库
                        saveCacheData(historyResponseBean.getPage().getContent());
                    }
                    if(loadPage >= totalPage){
                        LogUtils.e("缓存数据成功");
                        SharedPreferencesUtil.save(mContext,KTY_CC_BEGIN,String.valueOf(endTime));
                    }else{
                        loadPage++;
                        loadAllNetData();
                    }

                }
            }
        }
    }


    private void saveCacheData(List<ContentBean> list){
        ThreadTask.getInstance().executorDBThread(new Runnable() {
            @Override
            public void run() {
                DbUtil.savePhoneRecordListOrUpdate(list);
            }
        }, 10);
    }


    private String getHttpPostParams() {
        ArrayList<String> userIdArrays = new ArrayList<>();
        userIdArrays.add(userId);
        HistoryRequestBean requestBean = new HistoryRequestBean();
        requestBean.setPage(loadPage);
        requestBean.setSize(COMMON_LOAD_PAGE_SIZE);
        requestBean.setUserIds(userIdArrays);
        requestBean.setBegin(beginTime);
        requestBean.setEnd(endTime);
        return JSONObject.parseObject(new Gson().toJson(requestBean)).toJSONString();
    }

}
