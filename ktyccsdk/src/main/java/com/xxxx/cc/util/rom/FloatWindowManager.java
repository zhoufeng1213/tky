package com.xxxx.cc.util.rom;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;

import com.xxxx.cc.util.LogUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class FloatWindowManager {
    private static final String TAG = "FloatWindowManager";
    private static volatile FloatWindowManager instance;
    private boolean isWindowDismiss;
    private WindowManager windowManager;
    private WindowManager.LayoutParams mParams;
    private Dialog dialog;

    public FloatWindowManager() {
        this.isWindowDismiss = true;
        this.windowManager = null;
        this.mParams = null;
    }

    public static FloatWindowManager getInstance() {
        if (FloatWindowManager.instance == null) {
            synchronized (FloatWindowManager.class) {
                if (FloatWindowManager.instance == null) {
                    FloatWindowManager.instance = new FloatWindowManager();
                }
            }
        }
        return FloatWindowManager.instance;
    }

    public boolean checkPermission(final Context context) {
        if (Build.VERSION.SDK_INT < 23) {
            if (RomUtils.checkIsMiuiRom()) {
                return this.miuiPermissionCheck(context);
            }
            if (RomUtils.checkIsMeizuRom()) {
                return this.meizuPermissionCheck(context);
            }
            if (RomUtils.checkIsHuaweiRom()) {
                return this.huaweiPermissionCheck(context);
            }
            if (RomUtils.checkIs360Rom()) {
                return this.qikuPermissionCheck(context);
            }
            if (RomUtils.checkIsOppoRom()) {
                return this.oppoROMPermissionCheck(context);
            }
        }
        return this.commonROMPermissionCheck(context);
    }

    private boolean huaweiPermissionCheck(final Context context) {
        return HuaweiUtils.checkFloatWindowPermission(context);
    }

    private boolean miuiPermissionCheck(final Context context) {
        return MiuiUtils.checkFloatWindowPermission(context);
    }

    private boolean meizuPermissionCheck(final Context context) {
        return MeizuUtils.checkFloatWindowPermission(context);
    }

    private boolean qikuPermissionCheck(final Context context) {
        return QikuUtils.checkFloatWindowPermission(context);
    }

    private boolean oppoROMPermissionCheck(final Context context) {
        return OppoUtils.checkFloatWindowPermission(context);
    }

    private boolean commonROMPermissionCheck(final Context context) {
        if (RomUtils.checkIsMeizuRom()) {
            return this.meizuPermissionCheck(context);
        }
        Boolean result = true;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                final Class clazz = Settings.class;
                final Method canDrawOverlays = clazz.getDeclaredMethod("canDrawOverlays", Context.class);
                result = (Boolean) canDrawOverlays.invoke(null, context);
            } catch (Exception e) {
                Log.e("FloatWindowManager", Log.getStackTraceString((Throwable) e));
            }
        }
        return result;
    }

    public void applyPermission(final Activity context) {
        if (Build.VERSION.SDK_INT < 23) {
            if (RomUtils.checkIsMiuiRom()) {
                this.miuiROMPermissionApply((Context) context);
            } else if (RomUtils.checkIsMeizuRom()) {
                this.meizuROMPermissionApply(context);
            } else if (RomUtils.checkIsHuaweiRom()) {
                this.huaweiROMPermissionApply(context);
            } else if (RomUtils.checkIs360Rom()) {
                this.ROM360PermissionApply((Context) context);
            } else if (RomUtils.checkIsOppoRom()) {
                this.oppoROMPermissionApply((Context) context);
            }
        } else {
            LogUtils.e("commonROMPermissionAppl");
            this.commonROMPermissionApply(context);
        }
    }

    private void ROM360PermissionApply(final Context context) {
        this.showConfirmDialog(context, new OnConfirmResult() {
            @Override
            public void confirmResult(final boolean confirm) {
                if (confirm) {
                    QikuUtils.applyPermission(context);
                } else {
                    Log.e("FloatWindowManager", "ROM:360, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    private void huaweiROMPermissionApply(final Activity context) {
        this.showConfirmDialog((Context) context, new OnConfirmResult() {
            @Override
            public void confirmResult(final boolean confirm) {
                if (confirm) {
                    HuaweiUtils.applyPermission(context);
                } else {
                    Log.e("FloatWindowManager", "ROM:huawei, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    private void meizuROMPermissionApply(final Activity context) {
        this.showConfirmDialog(context, new OnConfirmResult() {
            @Override
            public void confirmResult(final boolean confirm) {
                if (confirm) {
                    MeizuUtils.applyPermission(context);
                } else {
                    Log.e("FloatWindowManager", "ROM:meizu, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    private void miuiROMPermissionApply(final Context context) {
        this.showConfirmDialog(context, new OnConfirmResult() {
            @Override
            public void confirmResult(final boolean confirm) {
                if (confirm) {
                    MiuiUtils.applyMiuiPermission(context);
                } else {
                    Log.e("FloatWindowManager", "ROM:miui, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    private void oppoROMPermissionApply(final Context context) {
        this.showConfirmDialog(context, new OnConfirmResult() {
            @Override
            public void confirmResult(final boolean confirm) {
                if (confirm) {
                    OppoUtils.applyOppoPermission(context);
                } else {
                    Log.e("FloatWindowManager", "ROM:miui, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    private void commonROMPermissionApply(final Activity context) {
        if (RomUtils.checkIsMeizuRom()) {
            this.meizuROMPermissionApply(context);
        } else if (Build.VERSION.SDK_INT >= 23) {
            this.showConfirmDialog(context, new OnConfirmResult() {
                @Override
                public void confirmResult(final boolean confirm) {
                    if (confirm) {
                        try {
                            FloatWindowManager.commonROMPermissionApplyInternal(context);
                        } catch (Exception e) {
                            Log.e("FloatWindowManager", Log.getStackTraceString(e));
                        }
                    } else {
                        Log.d("FloatWindowManager", "user manually refuse OVERLAY_PERMISSION");
                    }
                }
            });
        }
    }

    public static void commonROMPermissionApplyInternal(final Context context) throws NoSuchFieldException, IllegalAccessException {
        final Class clazz = Settings.class;
        final Field field = clazz.getDeclaredField("ACTION_MANAGE_OVERLAY_PERMISSION");
        final Intent intent = new Intent(field.get(null).toString());
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }

    private void showConfirmDialog(final Context context, final OnConfirmResult result) {
        this.showConfirmDialog(context, "您的手机没有授予悬浮窗权限，请开启后再试", result);
    }

    private void showConfirmDialog(final Context context, final String message, final OnConfirmResult result) {
        if (this.dialog != null && this.dialog.isShowing()) {
            this.dialog.dismiss();
        }
        (this.dialog = new AlertDialog.Builder(context)
                .setCancelable(true)
                .setTitle("").setMessage(message)
                .setCancelable(false)
                .setPositiveButton("现在去开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        result.confirmResult(true);
                        dialog.dismiss();
                    }
                }).setNegativeButton("暂不开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, final int which) {
                        result.confirmResult(false);
                        dialog.dismiss();
//                        FloatWindowManager.this.showConfirmDialog(context, message, result);
                    }
                }).create()).show();
    }


    public void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private int dp2px(final Context context, final float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private interface OnConfirmResult {
        void confirmResult(final boolean p0);
    }
}
