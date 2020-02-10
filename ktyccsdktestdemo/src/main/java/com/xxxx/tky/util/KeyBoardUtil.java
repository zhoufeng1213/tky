package com.xxxx.tky.util;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @author zhoufeng
 * @date 2019/4/16
 * @moduleName
 */
public class KeyBoardUtil {
    /**
     * 获取InputMethodManager，隐藏软键盘
     */
    public static void hideKeyboard(Context mContext, EditText editText) {
        InputMethodManager im = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (im != null) {
            im.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     */
    public static void hideKeyboard(Context mContext, TextView editText) {
        InputMethodManager im = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (im != null) {
            im.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
