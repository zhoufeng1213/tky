package com.xxxx.cc.util;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;

import com.xxxx.cc.model.UserBean;
import com.xxxx.cc.service.LinphoneService;

import org.linphone.core.AccountCreator;
import org.linphone.core.Address;
import org.linphone.core.Call;
import org.linphone.core.CallParams;
import org.linphone.core.Core;
import org.linphone.core.CoreListenerStub;
import org.linphone.core.Factory;
import org.linphone.core.PayloadType;
import org.linphone.core.ProxyConfig;
import org.linphone.core.TransportType;

/**
 * @author zhoufeng
 * @date 2019/8/6
 * @moduleName
 */
public class LinServiceManager {


    public static void setEnableMic(boolean enableMic) {
        if (LinphoneService.getCore() != null) {
            LinphoneService.getCore().enableMic(enableMic);
        }
    }


    public static void hookCall() {
//        Core core = LinphoneService.getCore();
//        if (core != null && core.getCallsNb() > 0) {
//            Call call = core.getCurrentCall();
//            if (call == null) {
//                call = core.getCalls()[0];
//            }
//            call.terminate();
//        }
        if (LinphoneService.getCore() != null) {
            LinphoneService.getCore().terminateAllCalls();
        }
    }


    public static void addListener(CoreListenerStub mCoreListener) {
        if (LinphoneService.getCore() != null) {
            LinphoneService.getCore().addListener(mCoreListener);
        }
    }

    public static void removeListener(CoreListenerStub mCoreListener) {
        if (LinphoneService.getCore() != null) {
            LinphoneService.getCore().removeListener(mCoreListener);
        }
    }


    public static Call callPhone(String phoenNum, String contactName) {
        Core core = LinphoneService.getCore();
        if(core != null){
            Address addressToCall = core.interpretUrl(phoenNum);
            CallParams params = core.createCallParams(null);
            params.setAudioBandwidthLimit(0);
            params.enableVideo(false);
            params.addCustomHeader("X-ContactName", contactName);
            if (addressToCall != null) {
                setSpeaker(false);
                return core.inviteAddressWithParams(addressToCall, params);
            }
        }
        return null;
    }

    /**
     * 免提
     */
    private static AudioManager audioManager;

    private static boolean isSpeakerEnabled() {
        AudioManager var10000 = audioManager;
        Boolean b = var10000 != null ? var10000.isSpeakerphoneOn() : null;
        return b != null ? b : false;
    }

    private static void setSpeaker(boolean state) {
        AudioManager var10000 = audioManager;
        if (var10000 != null) {
            var10000.setSpeakerphoneOn(state);
        }
    }

    public static void switchAudio(Context mContext,boolean isMianti) {
        if (audioManager == null) {
            audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        }

        if(audioManager != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            } else {
                audioManager.setMode(AudioManager.MODE_IN_CALL);
            }
            if(isMianti && !audioManager.isSpeakerphoneOn()){
                audioManager.setSpeakerphoneOn(true);
                audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
                        audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL),
                        AudioManager.STREAM_VOICE_CALL);
            }else{
                audioManager.setSpeakerphoneOn(false);
            }
        }

    }


    //关闭扬声器
    public static void closeSpeaker(Context mContext) {
        try {
            if (audioManager == null) {
                audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
            }
            if(audioManager != null){
                audioManager.setMode(AudioManager.MODE_NORMAL);
                audioManager.setSpeakerphoneOn(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void setLinPhoneConfig(UserBean userBean) {
        if (LinphoneService.getCore() != null) {
            AccountCreator mAccountCreator = LinphoneService.getCore().createAccountCreator(null);

            mAccountCreator.setUsername(userBean.getCcUserInfo().getExtensionNo());
            mAccountCreator.setDomain(userBean.getCcUserInfo().getDomain());
            mAccountCreator.setPassword(userBean.getCcUserInfo().getExtensionPassword());
            mAccountCreator.setTransport(TransportType.Udp);

            ProxyConfig cfg = mAccountCreator.createProxyConfig();
            String username = userBean.getCcUserInfo().getExtensionNo();
            String domain = userBean.getCcUserInfo().getDomain();
            String sipAddressStr = "sip:" + username + '@' + domain;
            String password = userBean.getCcUserInfo().getExtensionPassword();
            String proxyAddressStr = "sip:" + userBean.getCcServerProxy();
            String[] dnsServers = {"223.5.5.5"};
            int expire = 30;

            cfg.setIdentityAddress(LinphoneService.getCore().createAddress(sipAddressStr));
            cfg.setServerAddr(proxyAddressStr);
            cfg.setRoute(proxyAddressStr);
            cfg.enableRegister(true);
            cfg.setExpires(expire);


            Factory lcFactory = Factory.instance();
            LinphoneService.getCore().addAuthInfo(lcFactory.createAuthInfo(username, username, password, null, domain, domain));

            PayloadType[] payloadType = LinphoneService.getCore().getAudioPayloadTypes();
            PayloadType[] setCodecs = new PayloadType[]{payloadType[0], payloadType[3], payloadType[4]};
            LinphoneService.getCore().setAudioPayloadTypes(setCodecs);
            LinphoneService.getCore().setDnsServers(dnsServers);
            LinphoneService.getCore().setMaxCalls(1);
            LinphoneService.getCore().setUserAgent("SIP Agent", "1.0");
            LinphoneService.getCore().addProxyConfig(cfg);
            LinphoneService.getCore().setDefaultProxyConfig(cfg);
        }
    }
}
