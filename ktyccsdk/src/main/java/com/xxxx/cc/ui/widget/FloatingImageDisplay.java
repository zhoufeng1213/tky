package com.xxxx.cc.ui.widget;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.xxxx.cc.R;
import com.xxxx.cc.global.GlobalApplication;
import com.xxxx.cc.ui.CallActivity;
import com.xxxx.cc.util.LogUtils;
import com.xxxx.cc.util.TimeUtils;

public class FloatingImageDisplay {
    public static boolean isStarted;
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private View displayView;
    private int imageIndex;
    private Handler changeImageHandler;
    private int communicationSecond;
    private Handler.Callback changeImageCallback;
    private boolean isActionMove;
   private static FloatingImageDisplay mInstance;
   private Context context;
    TextView floatWindowTime;
    public static FloatingImageDisplay getInstance() {
        if (mInstance == null) {
            synchronized (FloatingImageDisplay.class) {
                if (mInstance == null) {
                    mInstance = new FloatingImageDisplay(GlobalApplication.getInstance());
                }
            }
        }
        return mInstance;
    }
    public FloatingImageDisplay(Context context) {
        this.imageIndex = 0;
        this.communicationSecond = 0;
        this. context=context;
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


    public void onCreate() {
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    layoutParams = new WindowManager.LayoutParams();
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
        this.changeImageHandler = new Handler(context.getMainLooper(), this.changeImageCallback);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        displayView = layoutInflater.inflate(R.layout.floatwindow_communication, (ViewGroup) null);
        displayView.setOnTouchListener(new FloatingOnTouchListener());
          floatWindowTime = (TextView) this.displayView.findViewById(R.id.float_window_time);
    }


    @RequiresApi(api = 23)
    public void showFloatingWindow( int second) {
        onCreate();
        FloatingImageDisplay.isStarted = true;
            floatWindowTime.setText(second+"");
            windowManager.addView(displayView, (ViewGroup.LayoutParams) layoutParams);
            final Message message = new Message();
            message.what = 0;
            message.arg1 = second;
            this.changeImageHandler.sendMessageDelayed(message, 0L);
            this.displayView.setOnClickListener((View.OnClickListener) new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    isActionMove = false;
                    Intent intent = new Intent();
                    intent.setAction("com.xxxx.cc.callAction");
                        context.startActivity(intent);
                //    releaseService();
//                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//                    try {
//                        pendingIntent.send();
//                        releaseService();
//                    } catch (PendingIntent.CanceledException e) {
//                        LogUtils.e( "Message"+e.getMessage());
//                        e.printStackTrace();
//                    }

                }
        });
    }

    public void releaseService() {
        FloatingImageDisplay.isStarted = false;
        if (null != windowManager && displayView != null) {
            windowManager.removeView(displayView);
            displayView = null;
            windowManager = null;
        }

    }

    static {
        FloatingImageDisplay.isStarted = false;
    }

    public class FloatingImageDisplayBinder extends Binder {
        public FloatingImageDisplay getService() {
            return FloatingImageDisplay.this;
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
}
