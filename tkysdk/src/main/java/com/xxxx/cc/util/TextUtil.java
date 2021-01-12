package com.xxxx.cc.util;

import android.text.TextUtils;

import com.github.promeg.pinyinhelper.Pinyin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhoufeng
 * @date 2020/2/8
 * @moduleName
 */
public class TextUtil {

    public static String getNameFirstChar(String name) {
        String result = "";
        if (!TextUtils.isEmpty(name)) {
            result = name.substring(0, 1);
            if (Pinyin.isChinese(result.charAt(0))) {
                result = Pinyin.toPinyin(result, "");
                if (!TextUtils.isEmpty(result)) {
                    result = result.substring(0, 1);
                }
            }
        }
        return result;
    }

    public static String getNameFirstName(String name) {
        String result = "";
        if (!TextUtils.isEmpty(name)) {
            result = name.substring(0, 1);
        }
        return result;
    }

    public static String getNameToPinyin(String name) {
        String result = "";
        if (!TextUtils.isEmpty(name)) {
            result = Pinyin.toPinyin(name, " ");
        }
        return result;
    }

    public static boolean isNumeric(String str){
        if(TextUtils.isEmpty(str)){
            return false;
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }
}
