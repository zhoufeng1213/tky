package com.xxxx.cc.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kty.mars.baselibrary.util.EffectiveClick;
import com.xxxx.cc.R;
import com.xxxx.cc.global.ComUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wei.zhang
 */
public class BusinessDialog extends BaseSystemDialog {
    private static List<View> mSelectItemList = new ArrayList<>();
    private static List<View> mRadioItemList = new ArrayList<>();
    private static EffectiveClick effectiveClick = EffectiveClick.create();

    private static void setSelectItemClick(int position) {
        if (null != mSelectItemList) {
            for (int i = 0; i < mSelectItemList.size(); i++) {
                View view = mSelectItemList.get(i);
                ImageView imageView = view.findViewById(R.id.iv_item_site_select);
                if (i == position) {
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    imageView.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    private static void setRadioItemClick(int position) {
        if (null != mRadioItemList) {
            for (int i = 0; i < mRadioItemList.size(); i++) {
                View view = mRadioItemList.get(i);
                ImageView imageView = view.findViewById(R.id.iv_item_site_select);
                if (i == position) {
                    imageView.setBackgroundResource(R.mipmap.icon_radio_black_select);
                } else {
                    imageView.setBackgroundResource(R.mipmap.icon_radio_black);
                }
            }
        }

    }

    public static void showItemDialog(Activity activity, String type, String selectValue, JSONArray items, final DialogClickListener dialogClickListener) {
        if (null == items || items.length() == 0) {
            return;
        }
        dismissDialog();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        mDialog = builder.create();
        mDialog.setCancelable(false);
        View rootView = null;
        if ("select".equals(type)) {
            rootView = setSelectRootView(activity, selectValue, items, dialogClickListener);
        } else if ("radio".equals(type)) {
            rootView = setRadioRootView(activity, selectValue, items, dialogClickListener);
        }


        mDialog.setView(rootView);
        int width = (int) ComUtils.dp2px(activity, 300);
        int height = (int) ComUtils.dp2px(activity, 400);

        setRootViewMaxHeight(rootView, width, height);
        setDialog(width, 0, 0);


    }

    private static View setRadioRootView(Activity activity, String selectValue, JSONArray items, DialogClickListener dialogClickListener) {
        View rootView = LayoutInflater.from(activity).inflate(R.layout.dialog_site_view, null);
        LinearLayout linearLayout = rootView.findViewById(R.id.layout_site_dialog);
        linearLayout.removeAllViews();
        mRadioItemList.clear();
        for (int i = 0; i < items.length(); i++) {
            final int position = i;
            JSONObject json = null;
            try {
                json = items.getJSONObject(i);
                View view = LayoutInflater.from(activity).inflate(R.layout.dialog_item_radio_view, null, false);
                TextView textView = view.findViewById(R.id.tv_item_site_name);
                textView.setText(json.optString("name"));
                mRadioItemList.add(view);
                //默认设置第一个
                if (!TextUtils.isEmpty(selectValue) && selectValue.equals(json.optString("name"))) {
                    setRadioItemClick(position);
                }
                final JSONObject finalJson = json;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (effectiveClick.isRedundantClick()) {
                            return;
                        }
                        setSelectItemClick(position);
                        if (dialogClickListener != null) {
                            dialogClickListener.onSingleItemSelected(finalJson);
                        }
                        dismissDialog();
                    }
                });
                linearLayout.addView(view);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return rootView;


    }

    private static View setSelectRootView(Activity activity, String selectValue, JSONArray items, final DialogClickListener dialogClickListener) {
        View rootView = LayoutInflater.from(activity).inflate(R.layout.dialog_site_view, null);
        LinearLayout linearLayout = rootView.findViewById(R.id.layout_site_dialog);
        linearLayout.removeAllViews();
        mSelectItemList.clear();
        for (int i = 0; i < items.length(); i++) {
            final int position = i;
            JSONObject json = null;
            try {
                json = items.getJSONObject(i);
                View view = LayoutInflater.from(activity).inflate(R.layout.dialog_item_select_view, null, false);
                TextView textView = view.findViewById(R.id.tv_item_site_name);
                textView.setText(json.optString("name"));
                mSelectItemList.add(view);
                //默认设置第一个
                if (!TextUtils.isEmpty(selectValue) && selectValue.equals(json.optString("name"))) {
                    setSelectItemClick(position);
                }
                final JSONObject finalJson = json;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (effectiveClick.isRedundantClick()) {
                            return;
                        }
                        setSelectItemClick(position);
                        if (dialogClickListener != null) {
                            dialogClickListener.onSingleItemSelected(finalJson);
                        }
                        dismissDialog();
                    }
                });
                linearLayout.addView(view);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return rootView;

    }

    private static void setRootViewMaxHeight(View rootView, int width, int needHeight) {
        //这种设置宽高的方式也是好使的！！！-- show 前调用，show 后调用都可以！！！
        //设置rootView的最大高度
        rootView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop,
                                       int oldRight, int oldBottom) {
                int contentHeight = rootView.getHeight();
                if (contentHeight > needHeight) {
                    //注意：这里的 LayoutParams 必须是 FrameLayout的！！
                    rootView.setLayoutParams(new FrameLayout.LayoutParams(width,
                            needHeight));
                    setDialog(width, needHeight, 0);
                }
            }
        });

    }


}
