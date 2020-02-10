package com.xxxx.cc.util.rom;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.Method;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MiuiUtils {
    private static final String TAG = "MiuiUtils";

    public static int getMiuiVersion() {
        final String version = RomUtils.getSystemProperty("ro.miui.ui.version.name");
        if (version != null) {
            try {
                return Integer.parseInt(version.substring(1));
            } catch (Exception e) {
                Log.e("MiuiUtils", "get miui version code error, version : " + version);
                Log.e("MiuiUtils", Log.getStackTraceString((Throwable) e));
            }
        }
        return -1;
    }

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
                Log.e("MiuiUtils", Log.getStackTraceString((Throwable) e));
                return false;
            }
        }
        Log.e("MiuiUtils", "Below API 19 cannot invoke!");
        return false;
    }

    public static void applyMiuiPermission(final Context context) {
        final int versionCode = getMiuiVersion();
        if (versionCode == 5) {
            goToMiuiPermissionActivity_V5(context);
        } else if (versionCode == 6) {
            goToMiuiPermissionActivity_V6(context);
        } else if (versionCode == 7) {
            goToMiuiPermissionActivity_V7(context);
        } else if (versionCode == 8) {
            goToMiuiPermissionActivity_V8(context);
        } else {
            Log.e("MiuiUtils", "this is a special MIUI rom version, its version code " + versionCode);
        }
    }

    private static boolean isIntentAvailable(final Intent intent, final Context context) {
        return intent != null && context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }

    public static void goToMiuiPermissionActivity_V5(final Context context) {
        Intent intent = null;
        final String packageName = context.getPackageName();
        intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        final Uri uri = Uri.fromParts("package", packageName, null);
        intent.setData(uri);
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        if (isIntentAvailable(intent, context)) {
            context.startActivity(intent);
        } else {
            Log.e("MiuiUtils", "intent is not available!");
        }
    }

    public static void goToMiuiPermissionActivity_V6(final Context context) {
        final Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
        intent.putExtra("extra_pkgname", context.getPackageName());
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        if (isIntentAvailable(intent, context)) {
            context.startActivity(intent);
        } else {
            Log.e("MiuiUtils", "Intent is not available!");
        }
    }

    public static void goToMiuiPermissionActivity_V7(final Context context) {
        final Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
        intent.putExtra("extra_pkgname", context.getPackageName());
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        if (isIntentAvailable(intent, context)) {
            context.startActivity(intent);
        } else {
            Log.e("MiuiUtils", "Intent is not available!");
        }
    }

    public static void goToMiuiPermissionActivity_V8(final Context context) {
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
        intent.putExtra("extra_pkgname", context.getPackageName());
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        if (isIntentAvailable(intent, context)) {
            context.startActivity(intent);
        } else {
            intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
            intent.setPackage("com.miui.securitycenter");
            intent.putExtra("extra_pkgname", context.getPackageName());
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            if (isIntentAvailable(intent, context)) {
                context.startActivity(intent);
            } else {
                Log.e("MiuiUtils", "Intent is not available!");
            }
        }
    }
}
