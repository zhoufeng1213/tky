package com.xxxx.tky.util;

import android.text.TextUtils;

import com.github.promeg.pinyinhelper.Pinyin;

/**
 * @author zhoufeng
 * @date 2019/10/14
 * @moduleName
 */
public class TextUtil {

    private final static String SPACE = " ";

    public static String getTextRemoveSpace(String text) {
        if (!TextUtils.isEmpty(text)) {
            if (text.contains(SPACE)) {
                text = text.replace(SPACE, "");
            }
        }
        return text;
    }


    private final static String SIP_TAG3 = "sip:pstn";
    private final static String SIP_TAG = "sip:";
    private final static String SIP_TAG2 = "@";

    public static String getSipUserName(String userName) {
        if (!TextUtils.isEmpty(userName)) {
            if (userName.contains(SIP_TAG3) && userName.contains(SIP_TAG2)) {
                int start = userName.indexOf(SIP_TAG3);
                int end = userName.indexOf(SIP_TAG2);
                if (start >= 0 && end >= 0 && start + SIP_TAG3.length() < end) {
                    userName = userName.substring(start + SIP_TAG3.length(), end);
                }
            } else if (userName.contains(SIP_TAG) && userName.contains(SIP_TAG2)) {
                int start = userName.indexOf(SIP_TAG);
                int end = userName.indexOf(SIP_TAG2);
                if (start >= 0 && end >= 0 && start + SIP_TAG.length() < end) {
                    userName = userName.substring(start + SIP_TAG.length(), end);
                }
            }
        }
        return userName == null ? "" : userName;
    }

    public static boolean isSipUserName(String userName) {
        if (!TextUtils.isEmpty(userName) && userName.contains(SIP_TAG) && userName.contains(SIP_TAG2)) {
            return true;
        }
        return false;
    }


    public static String getUserName(String name) {
        String userName = "";
        if (isSipUserName(name)) {
            userName = getSipUserName(name);
        } else {
            userName = name;
            if (!TextUtils.isEmpty(userName) && userName.contains(",")) {
                userName = userName.substring(userName.indexOf(",") + 1);
            }
        }
        return userName;
    }


    public static String getTextExcludeEmpty(String text) {
        if (!TextUtils.isEmpty(text)) {
            return text;
        }
        return "";
    }


    public static boolean isExistConferenceIdFromClipboardManager(String text){
        boolean isExist = false;
        if(!TextUtils.isEmpty(text)){
            int start = text.indexOf(SIP_TAG);
            int end = text.indexOf(SIP_TAG2);
            if (start >= 0 && end >= 0 && start + SIP_TAG.length() < end) {
                text = text.substring(start + SIP_TAG.length(), end);
            }
        }
        return isExist;
    }


    public static boolean isValidMeetingKey(String meetingKey){
        boolean isValid = false;
        if(!TextUtils.isEmpty(meetingKey)){
            meetingKey = meetingKey.trim();
            meetingKey = meetingKey.replace(" ","");
            if(meetingKey.length() == 9){
                isValid = true;
            }
        }
        return isValid;
    }


    public static String getNameFirstChar(String name){
        String result = "";
        if(!TextUtils.isEmpty(name)){
            result = name.substring(0,1);
            if(Pinyin.isChinese(result.charAt(0))){
                result = Pinyin.toPinyin(result,"");
                if(!TextUtils.isEmpty(result)){
                    result = result.substring(0,1);
                }
            }
        }
        return result;
    }

    public static String getNameFirstName(String name){
        String result = "";
        if(!TextUtils.isEmpty(name)){
            result = name.substring(0,1);
        }
        return result;
    }

    public static String getNameToPinyin(String name){
        String result = "";
        if(!TextUtils.isEmpty(name)){
            result = Pinyin.toPinyin(name," ");
        }
        return result;
    }

}
