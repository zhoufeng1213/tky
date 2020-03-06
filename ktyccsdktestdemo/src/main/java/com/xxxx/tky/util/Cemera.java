package com.xxxx.tky.util;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import java.io.File;

public class Cemera {
private static String HEADER_IMAGE_TMP_PATH= Environment.getExternalStorageDirectory().getPath() + "/" + System.currentTimeMillis() + ".jpg";
        public static void startCamera(Activity activity, String tmpPath, int REQUEST_CODE_CAMERA_WITH_DATA) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            //适配7.0拍照取uri的处理、
            File file = new File(tmpPath);
            File fileDir = new File(HEADER_IMAGE_TMP_PATH);
//文件目录不存在就创建
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            if (Build.VERSION.SDK_INT < 24) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            } else {
                Uri uri = FileProvider.getUriForFile(activity, activity.getPackageName()+".fileprovider", file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
            }
            activity.startActivityForResult(intent, REQUEST_CODE_CAMERA_WITH_DATA);
        }
        public static void startAlbum(Activity activity,int REQUEST_CODE_FLAG_CHOOSE_IMG) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_PICK);
            intent.setType("image/*");
            activity.startActivityForResult(intent, REQUEST_CODE_FLAG_CHOOSE_IMG);
        }
    }



