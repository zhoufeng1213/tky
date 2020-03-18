package com.xxxx.cc.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.xxxx.cc.R;
import com.xxxx.cc.ui.CallActivity;
import com.xxxx.cc.ui.HistoryActivity;
import com.xxxx.cc.util.TimeUtils;

public class FloatingImageDisplayService extends Service {
    public static boolean isStarted;
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private View displayView;
    private int imageIndex;
    private Handler changeImageHandler;
    private int communicationSecond;
    private final IBinder mBinder;
    private Handler.Callback changeImageCallback;
    private boolean isActionMove;

    public FloatingImageDisplayService() {
        this.imageIndex = 0;
        this.communicationSecond = 0;
        this.mBinder = new FloatingImageDisplayBinder();
        this.changeImageCallback = new Handler.Callback() {
            @Override
            public boolean handleMessage(final Message msg) {
                if (msg.what == 0) {
                    if (displayView != null) {
                        ((TextView) displayView.findViewById(R.id.float_window_time)).setText(TimeUtils.getWatchTime(communicationSecond++));
                    }
                    changeImageHandler.sendEmptyMessageDelayed(0, 1000L);
                }
                return false;
            }
        };
        this.isActionMove = false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FloatingImageDisplayService.isStarted = true;
        this.windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        this.layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= 26) {
            this.layoutParams.type = 2038;
        } else {
            this.layoutParams.type = 2002;
        }
        this.layoutParams.format = 1;
        this.layoutParams.gravity = 51;
        this.layoutParams.flags = 40;
        this.layoutParams.width = 160;
        this.layoutParams.height = 160;
        this.layoutParams.x = 0;
        this.layoutParams.y = 30;
        this.changeImageHandler = new Handler(this.getMainLooper(), this.changeImageCallback);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    @Nullable
    public IBinder onBind(final Intent intent) {
        this.showFloatingWindow(this.communicationSecond = intent.getIntExtra("TIME", 0));
        return this.mBinder;
    }

    @Override
    @RequiresApi(api = 23)
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @RequiresApi(api = 23)
    private void showFloatingWindow(final int second) {
        if (Settings.canDrawOverlays((Context) this)) {
            final LayoutInflater layoutInflater = LayoutInflater.from((Context) this);
            (this.displayView = layoutInflater.inflate(R.layout.floatwindow_communication, (ViewGroup) null)).setOnTouchListener(new FloatingOnTouchListener());
            final TextView floatWindowTime = (TextView) this.displayView.findViewById(R.id.float_window_time);
            floatWindowTime.setText((CharSequence) "");
            this.windowManager.addView(this.displayView, (ViewGroup.LayoutParams) this.layoutParams);
            final Message message = new Message();
            message.what = 0;
            message.arg1 = second;
            this.changeImageHandler.sendMessageDelayed(message, 0L);
            this.displayView.setOnClickListener((View.OnClickListener) new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    isActionMove = false;
//                    final Intent intent = new Intent(FloatingImageDisplayService.this, CallActivity.class);
//                    final PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
//                    try {
//                        pendingIntent.send();
//                       // releaseService();
//                    } catch (PendingIntent.CanceledException e) {
//                        e.printStackTrace();
//                    }
                    Intent intent = new Intent(FloatingImageDisplayService.this, CallActivity.class);
                    intent.setAction("com.xxxx.cc.callAction");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                   // releaseService();
                }
            });
        }
    }

    public void releaseService() {
        FloatingImageDisplayService.isStarted = false;
        if (null != windowManager && displayView != null) {
            windowManager.removeView(displayView);
            displayView = null;
            windowManager = null;
        }
        this.stopSelf();
    }

    static {
        FloatingImageDisplayService.isStarted = false;
    }

    public class FloatingImageDisplayBinder extends Binder {
        public FloatingImageDisplayService getService() {
            return FloatingImageDisplayService.this;
        }
    }

    private class FloatingOnTouchListener implements View.OnTouchListener {
        private int x;
        private int y;
        private int actionDwonX;
        private int actionDwonY;

        @Override
        public boolean onTouch(final View view, final MotionEvent event) {
            switch (event.getAction()) {
                case 0:
                    this.x = (int) event.getRawX();
                    this.y = (int) event.getRawY();
                    this.actionDwonX = this.x;
                    this.actionDwonY = this.y;
                    isActionMove = false;
                    break;

                case 2:
                    final int nowX = (int) event.getRawX();
                    final int nowY = (int) event.getRawY();
                    final int movedX = nowX - this.x;
                    final int movedY = nowY - this.y;
                    this.x = nowX;
                    this.y = nowY;
                    layoutParams.x += movedX;
                    layoutParams.y += movedY;
                    windowManager.updateViewLayout(view, layoutParams);
                    isActionMove = true;
                    break;

                case 1:
                    final int upX = (int) event.getRawX();
                    final int upY = (int) event.getRawY();
                    if (Math.abs(upX - this.actionDwonX) >= 5 || Math.abs(upY - this.actionDwonY) >= 5) {
                        isActionMove = true;
                        break;
                    }
                    isActionMove = false;
                    break;
                default:
                    break;
            }
            return isActionMove;
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(isStarted)
        {
            releaseService();
        }
    }
}
