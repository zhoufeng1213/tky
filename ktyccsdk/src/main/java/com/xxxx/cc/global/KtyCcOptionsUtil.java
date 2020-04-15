package com.xxxx.cc.global;

import android.content.Context;
import android.text.TextUtils;

import com.xxxx.cc.util.SharedPreferencesUtil;
import com.xxxx.cc.util.ToastUtil;

import static com.xxxx.cc.global.Constans.KTY_CC_BASE_URL;

/**
 * @author zhoufeng
 * @date 2019/8/7
 * @moduleName
 */
public class KtyCcOptionsUtil {

    public static boolean init(Context context, String host) {
        if (TextUtils.isEmpty(host)) {
            ToastUtil.showToast(context, "host不能为空");
            return false;
        }
        if (host.startsWith("http://") || host.startsWith("https://")) {
            Constans.BASE_URL = host;
            SharedPreferencesUtil.save(context, KTY_CC_BASE_URL, host);
            return true;
        }
        ToastUtil.showToast(context, "host无效, host必须是'http://' or 'https://'开头");
        return false;
    }

    public static boolean switchHostOption(Context context, String host) {
        Constans.BASE_URL = host;
        if (TextUtils.isEmpty(host)) {
            ToastUtil.showToast(context, "host不能为空");
            return false;
        }
        if (host.startsWith("http://") || host.startsWith("https://")) {
            SharedPreferencesUtil.save(context, KTY_CC_BASE_URL, host);
            return true;
        }
        ToastUtil.showToast(context, "host无效, host必须是'http://' or 'https://'开头");
        return false;
    }
}
