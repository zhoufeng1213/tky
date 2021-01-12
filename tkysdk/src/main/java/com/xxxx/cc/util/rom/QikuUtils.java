package com.xxxx.cc.util.rom;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Method;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class QikuUtils {
    private static final String TAG = "QikuUtils";

    public static boolean checkFloatWindowPermission(final Context context) {
        final int version = Build.VERSION.SDK_INT;
        return version < 19 || checkOp(context, 24);
    }

    @TargetApi(19)
    private static boolean checkOp(final Context context, final int op) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 19) {
            final AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            try {
                final Class clazz = AppOpsManager.class;
                final Method method = clazz.getDeclaredMethod("checkOp", Integer.TYPE, Integer.TYPE, String.class);
                return 0 == (int) method.invoke(manager, op, Binder.getCallingUid(), context.getPackageName());
            } catch (Exception e) {
                Log.e("QikuUtils", Log.getStackTraceString(e));
                return false;
            }
        }
        Log.e("", "Below API 19 cannot invoke!");
        return false;
    }

    public static void applyPermission(final Context context) {
        final Intent intent = new Intent();
        intent.setClassName("com.android.settings", "com.android.settings.Settings$OverlaySettingsActivity");
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        if (isIntentAvailable(intent, context)) {
            context.startActivity(intent);
        } else {
            intent.setClassName("com.qihoo360.mobilesafe", "com.qihoo360.mobilesafe.ui.index.AppEnterActivity");
            if (isIntentAvailable(intent, context)) {
                context.startActivity(intent);
            } else {
                Log.e("QikuUtils", "can't open permission page with particular name, please use \"adb shell dumpsys activity\" command and tell me the name of the float window permission page");
            }
        }
    }

    private static boolean isIntentAvailable(final Intent intent, final Context context) {
        return intent != null && context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }
}
