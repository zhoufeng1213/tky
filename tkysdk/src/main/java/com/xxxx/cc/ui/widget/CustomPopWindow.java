package com.xxxx.cc.ui.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.xxxx.cc.R;
import com.xxxx.cc.model.ContentBean;

/**
 * @author zhoufeng
 * @date 2019/8/5
 * @moduleName
 */

public class CustomPopWindow extends PopupWindow {
    private static final String TAG = "CustomPopWindow";
    private final View view;
    private Context context;
    private OnPopWindowItemClickListener listener;

    public CustomPopWindow(final Context context, final ContentBean contentBean) {
        super(context);
        this.view = LayoutInflater.from(context).inflate(R.layout.widget_popupwindow, null);
        this.context = context;
        this.initView(contentBean);
        this.initPopWindow();
    }

    @SuppressLint({"SetTextI18n"})
    private void initView(final ContentBean contentBean) {
        final TextView popPhoneNum = this.view.findViewById(R.id.popupwindow_phone_num);
        if (null != contentBean.getDnis()) {
            popPhoneNum.setText(contentBean.getDnis());
        }
        final TextView cancel = this.view.findViewById(R.id.popupwindow_cancel);
        final TextView viewDetails = this.view.findViewById(R.id.view_communication_details);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                CustomPopWindow.this.dismiss();
            }
        });
        viewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                CustomPopWindow.this.listener.onPopItemClick();
                CustomPopWindow.this.dismiss();
            }
        });
    }

    private void initPopWindow() {
        this.setContentView(this.view);
        this.setWidth(-1);
        this.setHeight(-2);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setAnimationStyle(R.style.mypopwindow_anim_style);
        final ColorDrawable dw = new ColorDrawable(16777215);
        this.setBackgroundDrawable(dw);
    }

    public void backgroundAlpha(final Activity context, final float bgAlpha) {
        final WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(2);
        context.getWindow().setAttributes(lp);
    }

    public void setOnPopWindowItemClickListener(final OnPopWindowItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnPopWindowItemClickListener {
        void onPopItemClick();
    }
}