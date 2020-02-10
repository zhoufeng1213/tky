package com.xxxx.tky.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;


import java.io.File;

/**
 * @author zhoufeng
 * @date 2019/11/4
 * @moduleName
 */
public class UpdateUtils {
    /**
     * 自动安装apk文件
     *
     * @param context
     * @param file
     */
    public static void openApk(Context context, String file, String packageName) {
        File apk = new File(file);
        if (apk.exists()) {
            if (Build.VERSION.SDK_INT >= 26) {
                boolean b = context.getPackageManager().canRequestPackageInstalls();
                if (b) {
                    Uri apkUri = FileProvider.getUriForFile(context, packageName + ".fileProvider", apk);
                    Intent install = new Intent(Intent.ACTION_VIEW);
                    install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    install.setDataAndType(apkUri, "application/vnd.android.package-archive");
                    context.startActivity(install);
                } else {
                    //请求安装未知应用来源的权限
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, 80);
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //在AndroidManifest中的android:authorities值
                Uri apkUri = FileProvider.getUriForFile(context, packageName + ".fileprovider", apk);
                Intent install = new Intent(Intent.ACTION_VIEW);
                install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                install.setDataAndType(apkUri, "application/vnd.android.package-archive");
                context.startActivity(install);
            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(Uri.fromFile(apk), "application/vnd.android.package-archive");
                context.startActivity(intent);
            }
        }

    }
}
