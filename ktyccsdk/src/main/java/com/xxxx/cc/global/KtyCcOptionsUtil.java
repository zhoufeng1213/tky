package com.xxxx.cc.global;

import android.content.Context;
import android.text.TextUtils;

import com.xxxx.cc.util.SharedPreferencesUtil;
import com.xxxx.cc.util.ToastUtil;

import java.net.URI;
import java.net.URISyntaxException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;

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
            initSocket(host);//"https://tky.ketianyun.com"
            return true;
        }
        ToastUtil.showToast(context, "host无效, host必须是'http://' or 'https://'开头");
        return false;
    }

    private static Socket mSocket;

    private static void initSocket(String path) {

        {
            try {
//                1.初始化socket.io，设置链接
                IO.setDefaultOkHttpCallFactory(SSLSocket.genSSLSocketFactory());
                IO.setDefaultOkHttpWebSocketFactory(SSLSocket.genSSLSocketFactory());
                IO.Options options = new IO.Options();
                options.path = "/agentbar";
                options.transports=new String[]{"websocket"};
                mSocket = IO.socket(path, options);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Socket getmSocket() {
        return mSocket;
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
