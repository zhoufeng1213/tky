package com.xxxx.cc.ui.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.xxxx.cc.R;
import com.xxxx.cc.model.ContentBean;
import com.xxxx.cc.util.TimeUtils;

import java.util.List;

/**
 * @author zhoufeng
 * @date 2019/8/5
 * @moduleName
 */
public class HistoryAdapter extends BaseQuickAdapter<ContentBean, BaseViewHolder> {
    public HistoryAdapter(@Nullable List<ContentBean> data) {
        super(R.layout.item_communication_history_v1, data);

    }

    private CommuncationDetailAdapter.OnPlayAudioListener onPlayAudioListener;

    @Override
    protected void convert(BaseViewHolder helper, ContentBean item) {
        helper.setText(R.id.tv_call_phone, item.getDnis());
        String userName = "";
        if (!TextUtils.isEmpty(item.getContactName())) {
            userName = item.getContactName();
            helper.setVisible(R.id.tv_call_name, true);
        }
        helper.setText(R.id.tv_call_name, userName);
        String createTime = TimeUtils.stringToDate_HM_MD(item.getCreateTime());
        helper.setText(R.id.tv_start_time, createTime);
        int durationTime = item.getDurationInSec();
        helper.setText(R.id.tv_call_duration, "通话时长 " + TimeUtils.getWatchTime1(durationTime));
        String direction = "呼出";
        if (item.getDirection() != null && item.getDirection().equals("INBOUND")) {
            direction = "呼入";
        }
        helper.setText(R.id.tv_direction, direction);
        if (item.getDaSampleName() != null) {
            helper.setText(R.id.tv_state, item.getDaSampleName());
        } else {
            if (item.getBillingInSec() > 0) {
                helper.setText(R.id.tv_state, "接通");
            } else {
                helper.setText(R.id.tv_state, "未接通");
            }
        }
        if (item.getContext() != null)
            helper.setText(R.id.tv_call, item.getReserved2() + "(" + item.getContext() + ")");
        else
            helper.setText(R.id.tv_call, item.getReserved2());
        if (item.getBillingInSec() > 0) {
            helper.setImageResource(R.id.iv_call_photo, R.mipmap.icon_call_ok);
            helper.setTextColor(R.id.tv_call_duration, mContext.getResources().getColor(R.color.c_42A6FE));
        } else {
            helper.setImageResource(R.id.iv_call_photo, R.mipmap.icon_call_fail);
            helper.setTextColor(R.id.tv_call_duration, mContext.getResources().getColor(R.color.c_FDB201));
        }
        helper.setGone(R.id.iv_call_record,false);
        if (item.getRecordFile() != null && !item.getReserved5().equals("")) {
            String recordFile = item.getRecordFile();
            String reserved5 = item.getReserved5();
            helper.setGone(R.id.iv_call_record, (!TextUtils.isEmpty(recordFile) && !TextUtils.isEmpty(reserved5)));
            ImageButton recordBt = helper.itemView.findViewById(R.id.iv_call_record);
            ProgressBar progressBar = helper.getView(R.id.pb);
            RelativeLayout rv=helper.itemView.findViewById(R.id.iv_play_rl);
            rv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onPlayAudioListener != null) {
                        onPlayAudioListener.onPlayAudio(item,
                                TextUtils.isEmpty(reserved5) ? recordFile : reserved5,
                                recordBt, progressBar
                        );
                    }
                }
            });
            recordBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onPlayAudioListener != null) {
                        onPlayAudioListener.onPlayAudio(item,
                                TextUtils.isEmpty(reserved5) ? recordFile : reserved5,
                                recordBt, progressBar
                        );
                    }
                }
            });

            if (item.isPlay()) {
                recordBt.setImageResource(R.drawable.ic_svg_voice_pause);
            } else {
                recordBt.setImageResource(R.drawable.ic_svg_voice_play);
            }
        }

    }

    public void setOnPlayAudioListener(CommuncationDetailAdapter.OnPlayAudioListener onPlayAudioListener) {
        this.onPlayAudioListener = onPlayAudioListener;
    }
}
