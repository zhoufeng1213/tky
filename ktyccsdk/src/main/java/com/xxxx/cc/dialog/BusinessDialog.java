package com.xxxx.cc.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kty.mars.baselibrary.LibContext;
import com.kty.mars.baselibrary.util.EffectiveClick;
import com.xxxx.cc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wei.zhang
 */
public class BusinessDialog extends BaseSystemDialog {
    private static List<View> mItemList = new ArrayList<>();
    private static EffectiveClick effectiveClick = EffectiveClick.create();

    private static void setItemClick(int position) {
        if (null != mItemList) {
            for (int i = 0; i < mItemList.size(); i++) {
                View view = mItemList.get(i);
                ImageView imageView = view.findViewById(R.id.iv_item_site_select);
                if (i == position) {
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    imageView.setVisibility(View.INVISIBLE);
                }
            }

        }

    }

    public static void showSelectDialog(Activity activity, JSONArray items, final DialogClickListener dialogClickListener) {
        if (null == items || items.length() == 0) {
            return;
        }
        dismissDialog();
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        mDialog = builder.create();
        mDialog.setCancelable(false);
        View rootView = LayoutInflater.from(activity).inflate(R.layout.dialog_site_view, null);
        LinearLayout linearLayout = rootView.findViewById(R.id.layout_site_dialog);
        linearLayout.removeAllViews();
        mItemList.clear();
        for (int i = 0; i < items.length(); i++) {
            final int position = i;
            JSONObject json = null;
            try {
                json = items.getJSONObject(i);

                View view = LayoutInflater.from(activity).inflate(R.layout.dialog_item_site_view, null, false);
                TextView textView = view.findViewById(R.id.tv_item_site_name);
                textView.setText(json.optString("name"));
                mItemList.add(view);
                //默认设置第一个
//                if (!TextUtils.isEmpty(Tools.getSiteName()) && Tools.getSiteName().equals(bean.getSiteName())) {
//                    setItemClick(position);
//                }
                final JSONObject finalJson = json;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (effectiveClick.isRedundantClick()) {
                            return;
                        }
                        setItemClick(position);
                        if (dialogClickListener != null) {
                            dialogClickListener.onSiteClick(finalJson);
                        }
                        dismissDialog();
                    }
                });
                linearLayout.addView(view);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mDialog.setView(rootView);
        setDialog((int) LibContext.getInstance().getContext().getResources().getDimension(R.dimen.dpi_640px), 0);

    }


}
