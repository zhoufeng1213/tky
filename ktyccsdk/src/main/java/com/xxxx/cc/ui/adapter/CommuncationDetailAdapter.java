package com.xxxx.cc.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

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
public class CommuncationDetailAdapter extends BaseQuickAdapter<ContentBean, BaseViewHolder> {

    private OnPlayAudioListener onPlayAudioListener;

    public CommuncationDetailAdapter(@Nullable List<ContentBean> data) {
        super(R.layout.item_communication_detail, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ContentBean bean) {
        String createTime = bean.getCreateTime();
        if (TextUtils.isEmpty(createTime)) {
            createTime = "";
        }
//        else {
//            createTime = TimeUtils.stampToDate(createTime);
//        }
        helper.setText(R.id.tv_time, createTime);

        String callState = "通话";
        String watchTime = TimeUtils.getWatchTime(bean.getBillingInSec());
        helper.setText(R.id.tv_call_duration, String.format("%s %s", callState, watchTime));

        helper.setTextColor(R.id.tv_time, ContextCompat.getColor(mContext,
                bean.getBillingInSec() > 0 ? R.color.c_A0A0A0 : R.color.f_f03535));
        helper.setTextColor(R.id.tv_call_duration, ContextCompat.getColor(mContext,
                bean.getBillingInSec() > 0 ? R.color.c_A0A0A0 : R.color.f_f03535));

        String recordFile = bean.getRecordFile();
        String reserved5 = bean.getReserved5();
        helper.setGone(R.id.iv_play_stop, (!TextUtils.isEmpty(recordFile) && !TextUtils.isEmpty(reserved5)));

        ImageView playImage = helper.getView(R.id.iv_play_stop);
        ProgressBar progressBar = helper.getView(R.id.pb);
        playImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPlayAudioListener != null) {
                    onPlayAudioListener.onPlayAudio(bean,
                            TextUtils.isEmpty(reserved5) ? recordFile : reserved5,
                            playImage, progressBar
                    );
                }
            }
        });
    }

    public void setOnPlayAudioListener(OnPlayAudioListener onPlayAudioListener) {
        this.onPlayAudioListener = onPlayAudioListener;
    }

    public interface OnPlayAudioListener {
        void onPlayAudio(ContentBean contentBean, String filePath, ImageView imageView, ProgressBar progressBar);
    }
}
