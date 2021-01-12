package com.xxxx.cc.util.rom;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Method;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MeizuUtils {
    private static final String TAG = "MeizuUtils";

    public static boolean checkFloatWindowPermission(final Context context) {
        final int version = Build.VERSION.SDK_INT;
        return version < 19 || checkOp(context, 24);
    }

    public static void applyPermission(final Activity context) {
        try {
            final Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
            intent.putExtra("packageName", context.getPackageName());
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            try {
                Log.e("MeizuUtils", "获取悬浮窗权限, 打开AppSecActivity失败, " + Log.getStackTraceString(e));
                FloatWindowManager.commonROMPermissionApplyInternal(context);
            } catch (Exception eFinal) {
                Log.e("MeizuUtils", "获取悬浮窗权限失败, 通用获取方法失败, " + Log.getStackTraceString(eFinal));
            }
        }
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
                Log.e("MeizuUtils", Log.getStackTraceString(e));
                return false;
            }
        }
        Log.e("MeizuUtils", "Below API 19 cannot invoke!");
        return false;
    }
}
