package com.xxxx.cc.global;

import android.content.Context;
import android.util.DisplayMetrics;

public class ComUtils {
    public static int dp2px(Context context, int dp) {
        // 1px = 1dp * (dpi / 160)
        DisplayMetrics metrics = context.getApplicationContext().getResources().getDisplayMetrics();
        int dpi = metrics.densityDpi;

        return (int) (dp * (dpi / 160f) + 0.5f);
    }

}
