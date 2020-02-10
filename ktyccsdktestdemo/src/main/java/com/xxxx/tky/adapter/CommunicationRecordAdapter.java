package com.xxxx.tky.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxxx.cc.util.TimeUtils;
import com.xxxx.tky.R;
import com.xxxx.cc.model.CommunicationRecordResponseBean;

import java.util.List;

/**
 * @author zhoufeng
 * @date 2020/2/7
 * @moduleName
 */
public class CommunicationRecordAdapter extends BaseQuickAdapter<CommunicationRecordResponseBean, BaseViewHolder> {


    public CommunicationRecordAdapter(@Nullable List<CommunicationRecordResponseBean> data) {
        super(R.layout.item_communication_record, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CommunicationRecordResponseBean bean) {
        String createTime = bean.getCreateTime();
        if (TextUtils.isEmpty(createTime)) {
            createTime = "";
        }else{
            createTime = TimeUtils.stringToDate_MD_HMS(createTime);
        }
        helper.setText(R.id.time_text, createTime);
        helper.setText(R.id.content_text, bean.getCommRecoeds());
        helper.setText(R.id.time_duration, TimeUtils.getWatchTime(bean.getBillingInSec()));
//        helper.setGone(R.id.edit_record,!TextUtils.isEmpty(bean.getBlegUuid()));
        ImageView imageView = helper.getView(R.id.image_tag);
        imageView.setImageResource(bean.getBillingInSec() == 0?R.drawable.notification:R.drawable.success);
        helper.addOnClickListener(R.id.edit_record);
    }

}

