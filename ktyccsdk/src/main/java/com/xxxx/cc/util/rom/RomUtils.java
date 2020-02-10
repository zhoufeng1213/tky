package com.xxxx.cc.util.rom;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RomUtils {
    private static final String TAG = "RomUtils";

    public static double getEmuiVersion() {
        try {
            final String emuiVersion = getSystemProperty("ro.build.version.emui");
            final String version = emuiVersion.substring(emuiVersion.indexOf("_") + 1);
            return Double.parseDouble(version);
        } catch (Exception e) {
            e.printStackTrace();
            return 4.0;
        }
    }

    public static int getMiuiVersion() {
        final String version = getSystemProperty("ro.miui.ui.version.name");
        if (version != null) {
            try {
                return Integer.parseInt(version.substring(1));
            } catch (Exception e) {
                Log.e("RomUtils", "get miui version code error, version : " + version);
            }
        }
        return -1;
    }

    public static String getSystemProperty(final String propName) {
        BufferedReader input = null;
        String line;
        try {
            final Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            Log.e("RomUtils", "Unable to read sysprop " + propName, (Throwable) ex);
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    Log.e("RomUtils", "Exception while closing InputStream", (Throwable) e);
                }
            }
        }
        return line;
    }

    public static boolean checkIsHuaweiRom() {
        return Build.MANUFACTURER.contains("HUAWEI");
    }

    public static boolean checkIsMiuiRom() {
        return !TextUtils.isEmpty((CharSequence) getSystemProperty("ro.miui.ui.version.name"));
    }

    public static boolean checkIsMeizuRom() {
        final String meizuFlymeOSFlag = getSystemProperty("ro.build.display.id");
        return !TextUtils.isEmpty((CharSequence) meizuFlymeOSFlag) && (meizuFlymeOSFlag.contains("flyme") || meizuFlymeOSFlag.toLowerCase().contains("flyme"));
    }

    public static boolean checkIs360Rom() {
        return Build.MANUFACTURER.contains("QiKU") || Build.MANUFACTURER.contains("360");
    }

    public static boolean checkIsOppoRom() {
        return Build.MANUFACTURER.contains("OPPO") || Build.MANUFACTURER.contains("oppo");
    }
}
