package com.xxxx.cc.util;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;

import com.xxxx.cc.model.UserBean;
import com.xxxx.cc.service.LinphoneService;

import org.linphone.core.AccountCreator;
import org.linphone.core.Address;
import org.linphone.core.AuthInfo;
import org.linphone.core.Call;
import org.linphone.core.CallParams;
import org.linphone.core.Core;
import org.linphone.core.CoreListenerStub;
import org.linphone.core.Factory;
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
        Core core = LinphoneService.getCore();
        if (core != null && core.getCallsNb() > 0) {
            Call call = core.getCurrentCall();
            if (core.getCalls().length > 0) {
                call = core.getCalls()[0];
            }
            if (call != null) {
                call.terminate();
            }
        }
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
        LogUtils.e("LinServiceManager, callPhone:" + phoenNum);
        Core core = LinphoneService.getCore();
        if (core != null) {
            LogUtils.e("core != null");
            Address addressToCall = core.interpretUrl(phoenNum);
            CallParams params = core.createCallParams(null);
            params.setAudioBandwidthLimit(0);
            params.enableVideo(false);
            params.addCustomHeader("X-ContactName", contactName);
            if (addressToCall != null) {
                LogUtils.e("addressToCall != null");
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

    public static void switchAudio(Context mContext, boolean isMianti) {
        if (audioManager == null) {
            audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        }

        if (audioManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            } else {
                audioManager.setMode(AudioManager.MODE_IN_CALL);
            }
            if (isMianti && !audioManager.isSpeakerphoneOn()) {
                audioManager.setSpeakerphoneOn(true);
                audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
                        audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL),
                        AudioManager.STREAM_VOICE_CALL);
            } else {
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
            if (audioManager != null) {
                audioManager.setMode(AudioManager.MODE_NORMAL);
                audioManager.setSpeakerphoneOn(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void unRegisterLinPhone() {
        LogUtils.e("unRegisterLinPhone");
        //https://github.com/BelledonneCommunications/linphone-android/issues/85
        //https://github.com/BelledonneCommunications/linphone-android/issues/650
        Core linphoneCore = LinphoneService.getCore();
        if (linphoneCore != null) {
            ProxyConfig[] proxyConfigList = linphoneCore.getProxyConfigList();

            if (proxyConfigList != null && proxyConfigList.length > 0) {

                LogUtils.e("linphone_unregistration", "proxyConfig to remove, size:" + proxyConfigList.length);
                for (ProxyConfig proxyConfig : proxyConfigList) {
                    //Set proxyConfig Expires
                    proxyConfig.edit();
                    proxyConfig.enableRegister(false);
                    proxyConfig.setExpires(0);
                    proxyConfig.done();

                    //Remove proxy config
                    linphoneCore.removeProxyConfig(proxyConfig);
                }
                linphoneCore.refreshRegisters();
            }
        }
        AuthInfo[] authInfos = LinphoneService.getCore().getAuthInfoList();
        if (authInfos != null && authInfos.length > 0) {
            for (AuthInfo authInfo : authInfos) {
                linphoneCore.removeAuthInfo(authInfo);
            }
        }
        LinphoneService.setRegister(false);
    }


    public static void setLinPhoneConfig(UserBean userBean) {
        if (LinphoneService.getCore() != null) {

            AccountCreator mAccountCreator = LinphoneService.getCore().createAccountCreator(null);

            mAccountCreator.setUsername(userBean.getCcUserInfo().getExtensionNo());
            mAccountCreator.setDomain(userBean.getCcUserInfo().getDomain());
            mAccountCreator.setPassword(userBean.getCcUserInfo().getExtensionPassword());
            mAccountCreator.setTransport(TransportType.Tcp);

            ProxyConfig cfg = mAccountCreator.createProxyConfig();
            String username = userBean.getCcUserInfo().getExtensionNo();
            String domain = userBean.getCcUserInfo().getDomain();
            String sipAddressStr = "sip:" + username + '@' + domain;
            String password = userBean.getCcUserInfo().getExtensionPassword();

            String[] dnsServers = {"223.5.5.5", "114.114.114.114"};
            int expire = 128;

            String proxyAddressStr = "sip:" + userBean.getCcServerProxy();
            LogUtils.e("proxyAddressStr:"+proxyAddressStr);
            Address proxyAddress = Factory.instance().createAddress(proxyAddressStr);
            proxyAddress.setTransport(TransportType.Tcp);

            cfg.setIdentityAddress(LinphoneService.getCore().createAddress(sipAddressStr));
//            cfg.setServerAddr(proxyAddressStr);
            cfg.setServerAddr(proxyAddress.asStringUriOnly());
//            cfg.setRoute(proxyAddressStr);
//            cfg.setRoute(proxyAddress.asStringUriOnly());
            cfg.enableRegister(true);
            cfg.setExpires(expire);

            Factory lcFactory = Factory.instance();
//            lcFactory.setDebugMode(true, "Linphone_tky");

            LinphoneService.getCore().addAuthInfo(lcFactory.createAuthInfo(username, username, password, null, domain, domain));

//            PayloadType[] setCodecs = new PayloadType[]{payloadType[0], payloadType[3], payloadType[4]};
//            LinphoneService.getCore().setAudioPayloadTypes(setCodecs);
            LinphoneService.getCore().setStunServer("stun1.ketianyun.com:3478");
            LinphoneService.getCore().setDnsServers(dnsServers);
            LinphoneService.getCore().setMaxCalls(1);
            LinphoneService.getCore().setUserAgent("SIP Agent", "1.0");
            LinphoneService.getCore().addProxyConfig(cfg);
            LinphoneService.getCore().setDefaultProxyConfig(cfg);
        }
    }

    public static void unRegisterOnlineLinPhone(UserBean cacheUserBean, boolean isExit) {
        if (cacheUserBean != null && cacheUserBean.getCcUserInfo() != null) {

            if (LinphoneService.getCore() != null) {
                if (LinphoneService.isRegister()) {
                    unRegisterLinPhone();
                    if (isExit) {
                        LogUtils.e("完全退出");
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(0);
                    }
                } else {
//                    LogUtils.e("注册 LinphoneService");
//                    LinphoneService.getCore().addListener(new CoreListenerStub() {
//                        @Override
//                        public void onRegistrationStateChanged(Core core, ProxyConfig cfg, RegistrationState
//                                state, String message) {
//                            LogUtils.e("linphone_registration", "state:" + state.name() + ", message:" + message);
//                            if (state == RegistrationState.Ok) {
//                                LinServiceManager.removeListener(this);
//                                LinphoneService.setRegister(true);
//                                unRegisterLinPhone();
//                                if(isExit)
//                                {
//                                    LogUtils.e("完全退出");
//                                    android.os.Process.killProcess(android.os.Process.myPid());
//                                    System.exit(0);
//                                }
//                            } else if (state == RegistrationState.Failed) {
//                                LogUtils.e("注册服务失败" + state.name());
//                                unRegisterLinPhone();
//                                if(isExit) {
//                                    LogUtils.e("完全退出");
//                                    android.os.Process.killProcess(android.os.Process.myPid());
//                                    System.exit(0);
//                                }
//                            }
//
//                        }
//                    });
//                    LinServiceManager.setLinPhoneConfig(cacheUserBean);
                }
            }
        }

    }
}
