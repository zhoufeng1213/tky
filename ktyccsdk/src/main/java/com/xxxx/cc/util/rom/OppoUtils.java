package com.xxxx.cc.util.rom;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Method;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class OppoUtils {
    private static final String TAG = "OppoUtils";

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
                Log.e("OppoUtils", Log.getStackTraceString(e));
                return false;
            }
        }
        Log.e("OppoUtils", "Below API 19 cannot invoke!");
        return false;
    }

    public static void applyOppoPermission(final Context context) {
        try {
            final Intent intent = new Intent();
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            final ComponentName comp = new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.sysfloatwindow.FloatWindowListActivity");
            intent.setComponent(comp);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
