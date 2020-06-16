package com.xxxx.tky.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xxxx.cc.base.fragment.BaseHttpRequestFragment;
import com.xxxx.cc.global.HttpRequest;
import com.xxxx.cc.model.BaseBean;
import com.xxxx.cc.model.ContentBean;
import com.xxxx.cc.model.FileDownloadVO;
import com.xxxx.cc.model.HistoryRequestBean;
import com.xxxx.cc.model.HistoryResponseBean;
import com.xxxx.cc.model.UserBean;
import com.xxxx.cc.model.ViewCacheVO;
import com.xxxx.cc.ui.CommunicationDetailActivity;
import com.xxxx.cc.ui.adapter.CommuncationDetailAdapter;
import com.xxxx.cc.ui.adapter.HistoryAdapter;
import com.xxxx.cc.util.FileUtil;
import com.xxxx.cc.util.LogUtils;
import com.xxxx.cc.util.MediaManager;
import com.xxxx.cc.util.SharedPreferencesUtil;
import com.xxxx.cc.util.ThreadTask;
import com.xxxx.cc.util.TimeUtils;
import com.xxxx.cc.util.ToastUtil;
import com.xxxx.cc.util.db.DbUtil;
import com.xxxx.tky.R;
import com.xxxx.tky.util.CallPhoneTool;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.functions.Consumer;
import okhttp3.Call;

import static com.xxxx.cc.global.Constans.COMMON_PAGE_SIZE;
import static com.xxxx.cc.global.Constans.KTY_CC_BEGIN;
import static com.xxxx.cc.global.Constans.USERBEAN_SAVE_TAG;
import static com.xxxx.cc.global.Constans.VOICE_RECORD_PREFIX;

/**
 * @author zhoufeng
 * @date 2019/8/12
 * @moduleName
 */
public class HistoryFragment extends BaseHttpRequestFragment {

    RecyclerView recycler;
    SmartRefreshLayout srlRefresh;
    LinearLayout lMainContainer;

    private HistoryAdapter historyAdapter;
    private List<ContentBean> historyResponseBeanList = new ArrayList<>();
    private int page;

    private UserBean cacheUserBean;
    private boolean isFirstRefrush = true;
    private long endTime = System.currentTimeMillis();
    private long beginTime = 0;
    private ImageView mIvBack;
    private HashMap<String, ViewCacheVO> mViewMapCache;
    private HashMap<String, String> mUrlCache;
    TextView tvDate;
    ImageView ivCloseBottom;
    ImageView ivBottomPlay;
    SeekBar seekBar;
    TextView tvCurrentProgress;
    TextView tvLessDate;
    LinearLayout layoutPlayVoice;
    private boolean isPlaying;
    private int totalSec;
    private MediaPlayer mediaPlayer;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_history;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recycler = view.findViewById(R.id.recycler);
        srlRefresh = view.findViewById(R.id.srl_refresh);
        lMainContainer = view.findViewById(R.id.ll_main_container);
        mIvBack = view.findViewById(R.id.iv_close);
        tvDate = view.findViewById(com.xxxx.cc.R.id.tv_date);
        ivBottomPlay = view.findViewById(com.xxxx.cc.R.id.iv_bottom_play);
        seekBar = view.findViewById(com.xxxx.cc.R.id.seekBar);
        tvCurrentProgress = view.findViewById(com.xxxx.cc.R.id.tv_current_progress);
        tvLessDate = view.findViewById(com.xxxx.cc.R.id.tv_less_date);
        layoutPlayVoice = view.findViewById(com.xxxx.cc.R.id.layout_play_voice);
        ivCloseBottom = view.findViewById(com.xxxx.cc.R.id.iv_close_bottom);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        srlRefresh.setEnableLoadMore(true);
        srlRefresh.setEnableRefresh(true);
        srlRefresh.setEnableLoadMoreWhenContentNotFull(false);

        init();
        if (ContextCompat.checkSelfPermission(this.mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }


        initListener();
        initSeek();
        mViewMapCache = new HashMap<>();
        mUrlCache = new HashMap<>();
        mediaPlayer = MediaManager.getMediaPlayer();
    }

    private void init() {
        try {
            Object objectBean = SharedPreferencesUtil.getObjectBean(mContext, USERBEAN_SAVE_TAG, UserBean.class);
            if (objectBean != null) {
                cacheUserBean = (UserBean) objectBean;
            }
            if (cacheUserBean != null) {
                recycler.setLayoutManager(new LinearLayoutManager(mContext));
                historyAdapter = new HistoryAdapter(historyResponseBeanList);
                historyAdapter.setOnPlayAudioListener(onPlayAudioListener);
                historyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        if (layoutPlayVoice.getVisibility() == View.VISIBLE) {
                            colseBottom();
                        }
                        //拨打电话
                        ContentBean contentBean = historyResponseBeanList.get(position);
                        CallPhoneTool.getInstance().callPhone(mContext, contentBean.getDnis(),
                                contentBean.getContactName(), ""
                        );
                    }
                });
                historyAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                    @Override
                    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                        if (view.getId() == R.id.more) {
                            showPopupWidow(historyResponseBeanList.get(position));
                        }
                    }
                });
                recycler.setAdapter(historyAdapter);


                srlRefresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
                    @Override
                    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                        page++;
                        List<ContentBean> list = DbUtil.queryPhoneRecordList(cacheUserBean.getUserId(), page);
                        if (list != null && list.size() > 0) {
                            srlRefresh.finishLoadMore();
                            srlRefresh.finishRefresh();
                            historyResponseBeanList.addAll(list);
                            historyAdapter.notifyDataSetChanged();
                        } else {
                            basePostPresenter.presenterBusinessByHeader(
                                    HttpRequest.CallHistory.callHistory,
                                    false,
                                    "token", cacheUserBean.getToken(),
                                    "Content-Type", "application/json"
                            );
                        }
                    }

                    @Override
                    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                        page = 0;
                        if (!isFirstRefrush) {
                            endTime = System.currentTimeMillis();
                            historyResponseBeanList.clear();
                            historyAdapter.notifyDataSetChanged();
                            loadData();
                        }

                        isFirstRefrush = false;

                        basePostPresenter.presenterBusinessByHeader(
                                HttpRequest.CallHistory.callHistory,
                                false,
                                "token", cacheUserBean.getToken(),
                                "Content-Type", "application/json"
                        );
                    }
                });
                loadData();
                srlRefresh.autoRefresh();
            } else {
                showToast("请先登录");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        //判断是否存在begin
        String beginStr = SharedPreferencesUtil.getValue(mContext, KTY_CC_BEGIN);
        if (!TextUtils.isEmpty(beginStr)) {
            try {
                beginTime = Long.valueOf(beginStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (beginTime > 0) {
            //先把本地数据查询出来
            List<ContentBean> list = DbUtil.queryPhoneRecordList(cacheUserBean.getUserId(), page);
            if (list != null && list.size() > 0) {
                historyResponseBeanList.addAll(list);
                historyAdapter.notifyDataSetChanged();
            }
        }
    }

    private BottomSheetDialog bottomSheetDialog;

    private void showPopupWidow(ContentBean contentBean) {
        bottomSheetDialog = new BottomSheetDialog(mContext);
        bottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        View view = View.inflate(mContext, R.layout.widget_popupwindow, null);
        bottomSheetDialog.setContentView(view);

        TextView viewCommunicationDetails = view.findViewById(R.id.view_communication_details);
        viewCommunicationDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
                Intent detailsIntent = new Intent(mContext, CommunicationDetailActivity.class);
                detailsIntent.putExtra("content", JSON.toJSONString(contentBean));
                startActivity(detailsIntent);
            }
        });
        TextView popPhoneNum = view.findViewById(R.id.popupwindow_phone_num);
        if (null != contentBean.getDnis()) {
            popPhoneNum.setText(contentBean.getDnis());
        }
        TextView cancel = view.findViewById(R.id.popupwindow_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.show();


    }

    @Override
    public JSONObject getHttpRequestParams(String moduleName) {
        JSONObject jsonObject = new JSONObject();
        if (HttpRequest.CallHistory.callHistory.equals(moduleName)) {
            ArrayList<String> userIdArrays = new ArrayList<>();
            userIdArrays.add(cacheUserBean.getUserId());
            HistoryRequestBean requestBean = new HistoryRequestBean();
            requestBean.setPage(page);
            requestBean.setSize(COMMON_PAGE_SIZE);
            requestBean.setUserIds(userIdArrays);
            requestBean.setBegin(beginTime-1000*60);
            requestBean.setEnd(endTime);
            jsonObject = JSONObject.parseObject(new Gson().toJson(requestBean));
        }
        return jsonObject;
    }

    @Override
    public void dealHttpRequestFail(String moduleName, BaseBean result) {
        super.dealHttpRequestFail(moduleName, result);
        srlRefresh.finishLoadMore();
        srlRefresh.finishRefresh();
    }

    @Override
    public void dealHttpRequestResult(String moduleName, BaseBean result, String response) {
        if (HttpRequest.CallHistory.callHistory.equals(moduleName)) {
            srlRefresh.finishLoadMore();
            srlRefresh.finishRefresh();
            if (!TextUtils.isEmpty(response)) {
                HistoryResponseBean historyResponseBean = (new Gson()).fromJson(response, HistoryResponseBean.class);
                if (historyResponseBean.getCode() == 0) {
                    if (historyResponseBeanList != null &&
                            historyResponseBean.getPage() != null &&
                            historyResponseBean.getPage().getContent() != null
                            && historyResponseBean.getPage().getContent().size() > 0) {
                        SharedPreferencesUtil.save(mContext, KTY_CC_BEGIN, String.valueOf(endTime));
                        SharedPreferencesUtil.save(mContext, VOICE_RECORD_PREFIX, historyResponseBean.getRecordPrefix());
                        //把查询到的直接添加到数据库
                        saveData(historyResponseBean.getPage().getContent());
                        LogUtils.e(historyResponseBean.getPage().getContent().toString());
                        if (page == 0) {
                            historyResponseBeanList.addAll(0, historyResponseBean.getPage().getContent());
                        } else {
                            historyResponseBeanList.addAll(historyResponseBean.getPage().getContent());
                        }

                        historyAdapter.notifyDataSetChanged();
                    }
                }
            }

        }
    }


    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
        return fragment;
    }

    private void saveData(List<ContentBean> list) {
        ThreadTask.getInstance().executorDBThread(new Runnable() {
            @Override
            public void run() {
                DbUtil.savePhoneRecordList(list);
            }
        }, 10);
    }

    private void initListener() {
        mIvBack.setOnClickListener(onClickListener);
        ivCloseBottom.setOnClickListener(onClickListener);
        ivBottomPlay.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.iv_close) {
                close();
            } else if (i == com.xxxx.cc.R.id.iv_close_bottom) {
                colseBottom();
            } else if (i == com.xxxx.cc.R.id.iv_bottom_play) {
                ivBottomPlay();
            }
        }
    };

    public void ivBottomPlay() {
        final String localPath = MediaManager.getDataSource();
        if (MediaManager.getMediaPlayer() != null && MediaManager.isPlaying()) {
            MediaManager.getMediaPlayer().pause();
            playChanged(localPath, com.xxxx.cc.R.drawable.ic_svg_voice_play, false);
            isPlaying = false;
        } else if (MediaManager.getMediaPlayer() != null && MediaManager.isPause()) {
            MediaManager.getMediaPlayer().start();
            playChanged(localPath, com.xxxx.cc.R.drawable.ic_svg_voice_pause, true);
            isPlaying = true;
            upDateProgress();
        } else if (MediaManager.getMediaPlayer() != null) {
            MediaManager.getMediaPlayer().start();
            playChanged(localPath, com.xxxx.cc.R.drawable.ic_svg_voice_pause, true);
            isPlaying = true;
            upDateProgress();
        }

    }

    private void playChanged(final String localPath, final int resid, final boolean b) {
        ThreadTask.getInstance().executorDBThread(new Runnable() {
            @Override
            public void run() {
                final FileDownloadVO fileDownloadVO = DbUtil.queryFileDownLoadUrlByPath(localPath);
                if (fileDownloadVO != null) {
                    final String url = fileDownloadVO.getUrl();
                    if (!TextUtils.isEmpty(url)) {
                        final ViewCacheVO viewCacheVO = mViewMapCache.get(url);
                        if (viewCacheVO != null) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    viewCacheVO.getIvPlay().setImageResource(resid);
                                    ivBottomPlay.setImageResource(resid);
                                    isPlaying = b;
                                }
                            });
                        }
                    }
                }
            }
        }, 10);
    }

    public void close() {
        if (isPlaying) {
            MediaManager.stop();
            seekBar.setProgress(0);
        }
        Activity activity = getActivity();
        if (null != activity) {
            activity.finish();
        }

    }

    public void colseBottom() {
        if (MediaManager.getMediaPlayer().isPlaying()) {
            MediaManager.getMediaPlayer().stop();
            seekBar.setProgress(0);
            final String dataSource = MediaManager.getDataSource();
            if (mUrlCache.containsKey(dataSource)) {
                String remoteUrl = mUrlCache.get(dataSource);
                ViewCacheVO viewCacheVO = mViewMapCache.get(remoteUrl);
                if (viewCacheVO != null) {
                    viewCacheVO.getIvPlay().setImageResource(com.xxxx.cc.R.drawable.ic_svg_voice_play);
                }
            }
        }
        layoutPlayVoice.setVisibility(View.GONE);

    }

    private String[] needPermissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CALL_PHONE};

    @SuppressLint("CheckResult")
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            RxPermissions rxPermissions = new RxPermissions(getActivity());
            rxPermissions
                    .request(needPermissions[0], needPermissions[1], needPermissions[2], needPermissions[3])
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) {
                            if (!aBoolean) {
                                ToastUtil.showToast(mContext, "权限被禁止，请允许");
                            }
                        }
                    });
        }
    }


    private void initSeek() {
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(final SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {
                if (MediaManager.getMediaPlayer() != null && MediaManager.isPlaying()) {
                    final int barProgress = seekBar.getProgress() * 1000;
                    MediaManager.getMediaPlayer().seekTo(barProgress);
                }
            }
        });
    }

    private void clickAudioToPlay(ContentBean bean, String url, final ImageView ivPlayStop, final ProgressBar pb) {
        final String currentPlayPath = MediaManager.getDataSource();
        String cacheUrl = "";
        totalSec = bean.getBillingInSec();
        String createTime = bean.getCreateTime();
        if (TextUtils.isEmpty(createTime)) {
            createTime = "";
        } else {
            createTime = TimeUtils.stampToDate(createTime);
        }
        tvDate.setText(createTime);
        String recordPrefix = SharedPreferencesUtil.getValue(mContext, VOICE_RECORD_PREFIX);
        if (TextUtils.isEmpty(recordPrefix)) {
            recordPrefix = "https://tky.ketianyun.com/recordings/";
        }
        url = recordPrefix + url;
        ViewCacheVO viewCacheVO = new ViewCacheVO(ivPlayStop, pb);
        mViewMapCache.put(url, viewCacheVO);
        final String finalUrl = url;
        boolean isSame = false;
        if (!TextUtils.isEmpty(currentPlayPath) && mUrlCache.containsKey(currentPlayPath)) {
            cacheUrl = mUrlCache.get(currentPlayPath);
            if (url.equals(cacheUrl)) {
                isSame = true;
            }
        }
        if (MediaManager.isPlaying()) {
            MediaManager.pause();
            ivPlayStop.setImageResource(com.xxxx.cc.R.drawable.ic_svg_voice_play);
            ivBottomPlay.setImageResource(com.xxxx.cc.R.drawable.ic_svg_voice_play);
            if (!isSame) {
                final ViewCacheVO cacheView = mViewMapCache.get(cacheUrl);
                if (cacheView != null) {
                    cacheView.getIvPlay().setImageResource(com.xxxx.cc.R.drawable.ic_svg_voice_play);
                }
                downOrPlay(ivPlayStop, pb, finalUrl);
            }
            isPlaying = false;
            return;
        }

        if (MediaManager.isPause()) {
            if (!isSame) {
                final ViewCacheVO cacheView = mViewMapCache.get(cacheUrl);
                if (cacheView != null) {
                    cacheView.getIvPlay().setImageResource(com.xxxx.cc.R.drawable.ic_svg_voice_play);
                }
                MediaManager.reset();
                ivPlayStop.setImageResource(com.xxxx.cc.R.drawable.ic_svg_voice_play);
                ivBottomPlay.setImageResource(com.xxxx.cc.R.drawable.ic_svg_voice_play);
                isPlaying = false;
                downOrPlay(ivPlayStop, pb, finalUrl);
            } else {
                //显示bottom
                layoutPlayVoice.setVisibility(View.VISIBLE);
                MediaManager.getMediaPlayer().start();
                isPlaying = true;
                upDateProgress();
                ivPlayStop.setImageResource(com.xxxx.cc.R.drawable.ic_svg_voice_pause);
                ivBottomPlay.setImageResource(com.xxxx.cc.R.drawable.ic_svg_voice_pause);
            }
            return;
        }
        downOrPlay(ivPlayStop, pb, finalUrl);
    }

    @SuppressLint("CheckResult")
    private void requestPermissionToPlay(ContentBean bean, String url, final ImageView ivPlayStop, final ProgressBar pb) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            RxPermissions rxPermissions = new RxPermissions(getActivity());
            rxPermissions
                    .request(needPermissions[0], needPermissions[1], needPermissions[2], needPermissions[3])
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) {
                            if (!aBoolean) {
                                ToastUtil.showToast(mContext, "权限被禁止，请允许");
                            } else {
                                clickAudioToPlay(bean, url, ivPlayStop, pb);
                            }
                        }
                    });
        } else {
            clickAudioToPlay(bean, url, ivPlayStop, pb);
        }
    }


    private CommuncationDetailAdapter.OnPlayAudioListener onPlayAudioListener = new CommuncationDetailAdapter.OnPlayAudioListener() {
        @Override
        public void onPlayAudio(ContentBean bean, String url, final ImageView ivPlayStop, final ProgressBar pb) {
            requestPermissionToPlay(bean, url, ivPlayStop, pb);
        }
    };

    private void downFile(final String finalUrl) {
        String fileName = "sip_" + System.currentTimeMillis() + ".mp3";
        //创建文件
        FileUtil.createFilePath(mContext, fileName);

        ViewCacheVO viewCache = mViewMapCache.get(finalUrl);
        if (viewCache != null && viewCache.getPb() != null && viewCache.getIvPlay() != null) {
            //把播放按钮变成加载中
            viewCache.getPb().setVisibility(View.VISIBLE);
            viewCache.getIvPlay().setVisibility(View.GONE);
        }

        OkHttpUtils.get().url(finalUrl)
                .build().execute(new FileCallBack(FileUtil.getFilePath(mContext), fileName) {
            @Override
            public void onError(Call call, Exception e, int id) {

            }


            @Override
            public void onResponse(File response, int id) {
                if (response != null && response.exists()) {
                    FileDownloadVO fileDownloadVO = new FileDownloadVO();
                    fileDownloadVO.setAbsPath(response.getAbsolutePath());
                    fileDownloadVO.setDownStamp(System.currentTimeMillis());
                    fileDownloadVO.setUrl(finalUrl);
                    DbUtil.saveFileDownLoadPath(fileDownloadVO);
                    //开始播放
                    ViewCacheVO viewCacheVO = mViewMapCache.get(finalUrl);
                    if (viewCacheVO != null && viewCacheVO.getPb() != null && viewCacheVO.getIvPlay() != null) {
                        viewCacheVO.getPb().setVisibility(View.GONE);
                        viewCacheVO.getIvPlay().setVisibility(View.VISIBLE);
                        playVoice(fileDownloadVO, viewCacheVO.getIvPlay());
                    }
                }

            }
        });
    }

    private void downOrPlay(final ImageView ivPlayStop, final ProgressBar pb, final String finalUrl) {
        //去数据库里面查询是否存在这个文件的路径，存在就直接播放，不存在就下载
        FileDownloadVO fileData = DbUtil.queryFileDownLoadPathByUrl(finalUrl);
        if (fileData != null && !TextUtils.isEmpty(fileData.getAbsPath())) {
            LogUtils.e("tag", "有文件" + fileData.getAbsPath());
            File file = new File(fileData.getAbsPath());
            if (file.exists()) {
                //直接播放
                LogUtils.e("tag", "有播放文件");
                ViewCacheVO viewCacheVO = mViewMapCache.get(finalUrl);
                if (viewCacheVO != null) {
                    playVoice(fileData, viewCacheVO.getIvPlay());
                }
            } else {
                LogUtils.e("tag", "有文件，但不存在");
                downFile(finalUrl);
            }
        } else {
            LogUtils.e("tag", "没有文件");
            downFile(finalUrl);
        }
    }

    private void playVoice(final FileDownloadVO fileInfos, final ImageView ivPlay) {
        String absPath = fileInfos.getAbsPath();
        String url = fileInfos.getUrl();
        mUrlCache.put(absPath, url);
        seekBar.setMax(100);
        seekBar.setProgress(0);
        tvCurrentProgress.setText("00:00");
        tvLessDate.setText("-00:00");

        layoutPlayVoice.setVisibility(View.VISIBLE);
        ivPlay.setImageResource(com.xxxx.cc.R.drawable.ic_svg_voice_pause);
        ivBottomPlay.setImageResource(com.xxxx.cc.R.drawable.ic_svg_voice_pause);
        MediaManager.playSound(mContext, absPath, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(final MediaPlayer mp) {
                isPlaying = false;
                MediaManager.reset();
                ivPlay.setImageResource(com.xxxx.cc.R.drawable.ic_svg_voice_play);
                layoutPlayVoice.setVisibility(View.GONE);
            }
        });
        isPlaying = MediaManager.isPlaying();
        upDateProgress();
    }


    private void upDateProgress() {
        ThreadTask.getInstance().executorOtherThread(new Runnable() {
            @Override
            public void run() {
                while (isPlaying) {
                    try {
                        final long currentPosition = getCurrentPosition();
                        final int duration = getDuration();
                        final Bundle bundle = new Bundle();
                        bundle.putLong("PROGRESS", currentPosition);
                        bundle.putLong("DURATION", duration);
                        final Message message = new Message();
                        message.what = 258;
                        message.setData(bundle);
                        if (mHandler != null) {
                            mHandler.sendMessage(message);
                        }
                        Thread.sleep(900L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 10);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            switch (msg.what) {
                case 258:
                    Bundle data = msg.getData();
                    long progress = data.getLong("PROGRESS");
                    long duration = data.getLong("DURATION");
                    seekBar.setMax(totalSec);
                    if (progress > 0L && totalSec > 0) {
                        progress /= 1000L;
                        final int currentProgress = (int) (progress * 100L / totalSec);
                        final String current = TimeUtils.getWatchTime((int) progress);
                        tvCurrentProgress.setText(current);
                        String less = TimeUtils.getWatchTime(totalSec - (int) progress);
                        tvLessDate.setText("-" + less);
                        seekBar.setProgress((int) progress);
                        break;
                    }
                    break;
                default:
                    break;

            }
        }
    };

    private long getCurrentPosition() {
        try {
            if (this.mediaPlayer != null && this.mediaPlayer.isPlaying()) {
                return this.mediaPlayer.getCurrentPosition();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
        return 0L;
    }

    private int getDuration() {
        try {
            if (this.mediaPlayer != null && this.mediaPlayer.isPlaying()) {
                return this.mediaPlayer.getDuration();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return 0;
    }
}
