package com.xxxx.cc.base.widget;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.xxxx.cc.R;


/**
 * Created by administrator on 2016/5/4.
 */
public class LoadingDialog {

    private TextView textView;
    private Context mContext;
    private View mDialogView;
    private Dialog dialog;
    private boolean isCancelDialog = true;

    public LoadingDialog(Context mContext) {
        this.mContext = mContext;
        initDialogView();
    }

    public LoadingDialog(Context mContext, boolean isCancelDialog) {
        this.mContext = mContext;
        this.isCancelDialog = isCancelDialog;
        initDialogView();
    }

    private void initDialogView() {
        mDialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_loading, null);
        textView = mDialogView.findViewById(R.id.txtMsg);
        initDialog();
    }

    private void initDialog() {
        dialog = new Dialog(mContext, R.style.dialog);
        dialog.setContentView(mDialogView);
        dialog.setCancelable(isCancelDialog);
        dialog.setCanceledOnTouchOutside(isCancelDialog);
    }

    public void setMessage(CharSequence msg) {
        textView.setText(msg);
    }

    public void setMessage(int msg) {
        textView.setText(msg);
    }

    public void setMessage(boolean isGone) {
        textView.setVisibility(isGone ? View.GONE : View.VISIBLE);
    }

    public void show() {
        if (dialog != null) {
            dialog.show();
        }
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public void hide() {
        if (dialog != null) {
            dialog.hide();
        }
    }

    public boolean isShowing() {
        if (dialog != null) {
            return dialog.isShowing();
        }
        return false;
    }

}
