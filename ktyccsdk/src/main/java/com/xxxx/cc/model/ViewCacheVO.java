package com.xxxx.cc.model;

import android.widget.ImageView;
import android.widget.ProgressBar;

public class ViewCacheVO {
    private ImageView ivPlay;
    private ProgressBar pb;

    public ViewCacheVO(final ImageView ivPlay, final ProgressBar pb) {
        this.ivPlay = ivPlay;
        this.pb = pb;
    }

    public ImageView getIvPlay() {
        return this.ivPlay;
    }

    public void setIvPlay(final ImageView ivPlay) {
        this.ivPlay = ivPlay;
    }

    public ProgressBar getPb() {
        return this.pb;
    }

    public void setPb(final ProgressBar pb) {
        this.pb = pb;
    }
}
