package com.xxxx.cc.util;

import android.content.Context;
import android.widget.Toast;

/**
 * 保证显示的toast唯一
 */
public class ToastUtil {

//    public static Toast mToast;

    public static void showToast(Context context, String msg) {
//        if (mToast == null) {
        Toast mToast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
//        } else {
        mToast.setText(msg);
//        }
        mToast.show();
    }


    public static void showToastShort(Context context, String msg) {
//        if (mToast == null) {
        Toast mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
//        } else {
        mToast.setText(msg);
//        }
        mToast.show();
    }
}
