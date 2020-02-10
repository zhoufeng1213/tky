package com.xxxx.cc.util.rom;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AppOpsManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Method;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class HuaweiUtils {
    private static final String TAG = "HuaweiUtils";

    public static boolean checkFloatWindowPermission(final Context context) {
        final int version = Build.VERSION.SDK_INT;
        return version < 19 || checkOp(context, 24);
    }

    public static void applyPermission(final Activity context) {
        try {
            final Intent intent = new Intent();
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.addviewmonitor.AddViewMonitorActivity");
            intent.setComponent(comp);
            if (RomUtils.getEmuiVersion() == 3.1) {
                context.startActivityForResult(intent, 0);
            } else {
                comp = new ComponentName("com.huawei.systemmanager", "com.huawei.notificationmanager.ui.NotificationManagmentActivity");
                intent.setComponent(comp);
                context.startActivityForResult(intent, 0);
            }
        } catch (SecurityException e) {
            final Intent intent2 = new Intent();
            intent2.setFlags(FLAG_ACTIVITY_NEW_TASK);
            final ComponentName comp2 = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");
            intent2.setComponent(comp2);
            context.startActivityForResult(intent2, 0);
            Log.e("HuaweiUtils", Log.getStackTraceString(e));
        } catch (ActivityNotFoundException e2) {
            final Intent intent2 = new Intent();
            intent2.setFlags(FLAG_ACTIVITY_NEW_TASK);
            final ComponentName comp2 = new ComponentName("com.Android.settings", "com.android.settings.permission.TabItem");
            intent2.setComponent(comp2);
            context.startActivityForResult(intent2, 0);
            e2.printStackTrace();
            Log.e("HuaweiUtils", Log.getStackTraceString(e2));
        } catch (Exception e3) {
            Toast.makeText(context, "进入设置页面失败，请手动设置", Toast.LENGTH_SHORT).show();
            Log.e("HuaweiUtils", Log.getStackTraceString(e3));
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
                Log.e("HuaweiUtils", Log.getStackTraceString((Throwable) e));
                return false;
            }
        }
        Log.e("HuaweiUtils", "Below API 19 cannot invoke!");
        return false;
    }
}
