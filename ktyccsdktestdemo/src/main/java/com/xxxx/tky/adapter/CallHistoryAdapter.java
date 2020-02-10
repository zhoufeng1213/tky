package com.xxxx.tky.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import com.xxxx.cc.model.ContentBean;
import com.xxxx.cc.util.TimeUtils;
import com.xxxx.tky.R;

import java.util.List;

/**
 * @author zhoufeng
 * @date 2020/2/7
 * @moduleName
 */
public class CallHistoryAdapter  extends BaseQuickAdapter<ContentBean, BaseViewHolder> {


    public CallHistoryAdapter(@Nullable List<ContentBean> data) {
        super(R.layout.item_call_history, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ContentBean bean) {
        String createTime = bean.getCreateTime();
        if (TextUtils.isEmpty(createTime)) {
            createTime = "";
        }else{
            createTime =TimeUtils.stringToDate_MD_HM(createTime);
        }
        helper.setText(R.id.time_text, createTime);
        helper.setText(R.id.phone_area, bean.getArea());
        helper.setText(R.id.phone_num, bean.getDnis());
        helper.setText(R.id.time_duration, TimeUtils.getDurationTime(bean.getBillingInSec()));
    }

}

