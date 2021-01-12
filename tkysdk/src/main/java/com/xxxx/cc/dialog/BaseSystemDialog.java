package com.xxxx.cc.dialog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.xxxx.cc.R;

/**
 * @author wei.zhang
 */
public class BaseSystemDialog {
    protected static AlertDialog mDialog;


    protected static void dismissDialog() {
        if (null != mDialog && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }


    protected static void showDialog(int delayTime) {
        if (null != mDialog && !mDialog.isShowing()) {
            mDialog.show();
        }


    }

    protected static void setDialog(int width, int height, int delayTime) {
        if (mDialog.getWindow() != null) {
            Window window = mDialog.getWindow();
            window.setDimAmount(0.2f);
            window.setBackgroundDrawableResource(R.color.transparent);
            showDialog(delayTime);
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.width = width;

            if (height > 0) {
                layoutParams.height = height;
            } else {
                layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            }
            layoutParams.gravity = Gravity.CENTER;
            window.setAttributes(layoutParams);
            window.setWindowAnimations(R.style.ScaleDialog);
        }


        mDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dismissDialog();
                }
                return false;
            }
        });


    }


}
