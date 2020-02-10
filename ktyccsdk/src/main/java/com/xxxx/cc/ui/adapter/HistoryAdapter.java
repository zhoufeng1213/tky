package com.xxxx.cc.ui.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxxx.cc.R;
import com.xxxx.cc.model.ContentBean;

import java.util.List;

/**
 * @author zhoufeng
 * @date 2019/8/5
 * @moduleName
 */
public class HistoryAdapter extends BaseQuickAdapter<ContentBean, BaseViewHolder> {

    public HistoryAdapter(@Nullable List<ContentBean> data) {
        super(R.layout.item_communication_history, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ContentBean item) {
        helper.setText(R.id.phone_num, item.getDnis());
        String userName = "";
        if(!TextUtils.isEmpty(item.getContactName())){
            userName = "(" + item.getContactName() + ")";
        }
        helper.setText(R.id.name, userName);
        if(!TextUtils.isEmpty(item.getArea())){
            helper.setText(R.id.phone_area, item.getArea());
            helper.setGone(R.id.phone_area,true);
        }else{
            helper.setGone(R.id.phone_area,false);
        }
//        helper.setText(R.id.phone_area, item.getArea());
        helper.setText(R.id.time, item.getCreateTime());

        helper.addOnClickListener(R.id.more);
    }
}
