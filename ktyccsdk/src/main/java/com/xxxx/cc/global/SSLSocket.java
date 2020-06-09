package com.xxxx.cc.global;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.internal.tls.OkHostnameVerifier;

public class SSLSocket {
    public static OkHttpClient genSSLSocketFactory() {
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            X509TrustManager x509TrustManager=new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };

            OkHttpClient ohc=new OkHttpClient.Builder().hostnameVerifier(OkHostnameVerifier.INSTANCE)
                    .sslSocketFactory(sc.getSocketFactory(),x509TrustManager)
                    .connectTimeout(10, TimeUnit.SECONDS).build();
            return ohc;
        } catch (Exception localException) {
//            LogHelper.e("SSLSocketFactory -> " + localException.getMessage());
        }
        return null;
    }
}
