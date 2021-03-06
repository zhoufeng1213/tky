package com.xxxx.cc.global;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.xxxx.cc.base.presenter.MyStringCallback;
import com.xxxx.cc.callback.LoginCallBack;
import com.xxxx.cc.model.BaseBean;
import com.xxxx.cc.model.UserBean;
import com.xxxx.cc.service.LinphoneService;
import com.xxxx.cc.service.LoginRunnable;
import com.xxxx.cc.ui.util.CustomPersonDataUtil;
import com.xxxx.cc.ui.util.HttpCacheDataUtil;
import com.xxxx.cc.util.LinServiceManager;
import com.xxxx.cc.util.LogUtils;
import com.xxxx.cc.util.NetUtil;
import com.xxxx.cc.util.SharedPreferencesUtil;
import com.xxxx.cc.util.ThreadTask;
import com.xxxx.cc.util.ToastUtil;
import com.xxxx.cc.util.db.DbUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;

import org.linphone.core.Core;
import org.linphone.core.CoreListenerStub;
import org.linphone.core.ProxyConfig;
import org.linphone.core.RegistrationState;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;

import static com.xxxx.cc.global.Constans.KTY_CC_BEGIN;
import static com.xxxx.cc.global.Constans.KTY_CUSTOM_BEGIN;
import static com.xxxx.cc.global.Constans.USERBEAN_SAVE_TAG;

/**
 * @author zhoufeng
 * @date 2019/8/7
 * @moduleName
 */
public class KtyCcNetUtil {



    public static void login(Context context, String userName, String pwd, LoginCallBack loginCallBack) {
        if (loginCallBack == null) {
            ToastUtil.showToast(context, "loginCallBack 不能为null");
            return;
        }
        //判断网络是否可用
        if (!NetUtil.isNetworkConnected(context)) {
            loginCallBack.onFailed(ErrorCode.NOT_NET_ERROR, "无网络");
            return;
        }
        GetBuilder okHttpUtils = OkHttpUtils.get();
        okHttpUtils.url(Constans.BASE_URL + HttpRequest.Login.loginUser);
        //添加请求参数
        okHttpUtils.addParams("username", userName);
        okHttpUtils.addParams("password", pwd);
        okHttpUtils.build()
                .execute(new MyStringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        loginCallBack.onFailed(id, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        if (!TextUtils.isEmpty(response)) {
                            //把用户数据保存起来
                            try {
                                BaseBean baseBean = JSON.parseObject(response, BaseBean.class);
                                if (baseBean != null) {
                                    if (baseBean.isOk()) {
                                        dealLoginSuccess(context, baseBean);
                                        loginCallBack.onSuccess(ErrorCode.SUCCESS, "登录成功");
                                        //判断是否已经启动service了
//                                        dealLinkLinPhone(context,baseBean,loginCallBack);

                                    } else {
                                        loginCallBack.onFailed(baseBean.getCode(), baseBean.getMessage());
                                    }
                                } else {
                                    loginCallBack.onFailed(ErrorCode.INTERNAL_SERVER_ERROR, "服务器错误");
                                }
                            } catch (Exception e) {
                                loginCallBack.onFailed(ErrorCode.INTERNAL_SERVER_ERROR, "服务器错误");
                            }
                        } else {
                            loginCallBack.onFailed(ErrorCode.INTERNAL_SERVER_ERROR, "服务器错误");
                        }
                    }
                });
    }


    private static void dealLoginSuccess(Context context, BaseBean baseBean) {
        UserBean userBean = JSON.parseObject(baseBean.getData().toString(), UserBean.class);
        LogUtils.e(JSON.toJSONString(userBean));
        SharedPreferencesUtil.saveObjectBean(context, userBean, USERBEAN_SAVE_TAG);
        DbUtil.init(context);
        dealCache(context,userBean);
        dealCustomCache(context,userBean);
        //去配置linphone的参数
        if (!LinphoneService.isReady()) {
            context.startService(new Intent(context, LinphoneService.class));
        }
    }


    private static void dealCache(Context context,UserBean userBean){
        String beginStr = SharedPreferencesUtil.getValue(context,KTY_CC_BEGIN);
        long beginTime = 0;
        if(!TextUtils.isEmpty(beginStr)){
            try {
                beginTime = Long.valueOf(beginStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            //找到当前时间的前40天的时间当做开始时间
            Calendar cl = Calendar.getInstance();
            cl.add(Calendar.DATE,-40);
            Date date = cl.getTime();
            beginTime = date.getTime();
        }
        HttpCacheDataUtil httpCacheDataUtil = HttpCacheDataUtil.getInstance(context.getApplicationContext());
        httpCacheDataUtil.setQueryData(beginTime,System.currentTimeMillis(),userBean.getUserId(),userBean.getToken());
        httpCacheDataUtil.loadAllNetData();
    }



    private static void dealCustomCache(Context context,UserBean userBean){
        String beginStr = SharedPreferencesUtil.getValue(context,KTY_CUSTOM_BEGIN);
        LogUtils.e("beginStr====> "+(beginStr == null?"null":beginStr));
        long beginTime = 0;
        if(!TextUtils.isEmpty(beginStr)){
            try {
                beginTime = Long.valueOf(beginStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            //找到当前时间的前40天的时间当做开始时间
            Calendar cl = Calendar.getInstance();
            cl.add(Calendar.DATE,-40);
            Date date = cl.getTime();
            beginTime = date.getTime();
        }
        CustomPersonDataUtil httpCacheDataUtil = CustomPersonDataUtil.getInstance(context.getApplicationContext());
        httpCacheDataUtil.setQueryData(beginTime,System.currentTimeMillis(),userBean.getToken());
        httpCacheDataUtil.loadAllNetData();
    }

    private static void dealLinkLinPhone(Context context, BaseBean baseBean, LoginCallBack loginCallBack) {
        UserBean userBean = JSON.parseObject(baseBean.getData().toString(), UserBean.class);
        //判断service
        if(LinphoneService.isReady()){
            LinServiceManager.addListener(new CoreListenerStub(){
                @Override
                public void onRegistrationStateChanged(Core lc, ProxyConfig cfg, RegistrationState cstate, String message) {
                    super.onRegistrationStateChanged(lc, cfg, cstate, message);
                    if (cstate == RegistrationState.Ok) {
                        LinphoneService.setRegister(true);
                        loginCallBack.onSuccess(ErrorCode.SUCCESS, "登录成功");
                    }else{
                        loginCallBack.onFailed(ErrorCode.REGISTER_ERROR, "注册失败");
                    }
                }
            });
            LinServiceManager.setLinPhoneConfig(userBean);
        }else{
            LogUtils.e("service还没起");
            LoginRunnable loginRunnable = new LoginRunnable(userBean,loginCallBack);
            ThreadTask.getInstance().executorOtherThread(loginRunnable, 10);
        }
    }




}
