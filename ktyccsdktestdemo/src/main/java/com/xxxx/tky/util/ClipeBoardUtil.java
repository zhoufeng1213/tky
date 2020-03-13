package com.xxxx.tky.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class ClipeBoardUtil {
    /**
     * 获取剪切板里内容
     *
     * @param context
     * @return
     */
    public static String getClipeBoardContent(Context context) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
        ClipData primaryClip = clipboardManager.getPrimaryClip();
        String content = null;
        if (primaryClip != null && primaryClip.getItemCount() > 0) {
            ClipData.Item itemAt = primaryClip.getItemAt(0);
            if (itemAt != null&&itemAt.getText()!=null) {
                content = itemAt.getText().toString();
            }
        }
        return content;
    }

    /**
     * 放置内容发到剪切板
     */
    public static void setClipeBoardContent(Context context, String content) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
        ClipData primaryClip = ClipData.newPlainText("Label", content);//纯文本内容
        clipboardManager.setPrimaryClip(primaryClip);
    }

    public static boolean isChinaPhoneLegal(String str) throws PatternSyntaxException {
        if (str == null) {
            return false;
        }
        if (str.length() != 11) {
            return false;
        }
        String regExp = "^((13[0-9])|(15[^4])|(166)|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(str);
        return m.matches();
    }
}
