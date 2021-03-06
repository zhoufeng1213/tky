package com.xxxx.cc.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zhoufeng
 * @date 2019/8/5
 * @moduleName
 */
public class TimeUtils {
    private static String MD_HM = "M月d日 HH:mm";
    private static String MD_HMS = "MM-dd HH:mm:ss";
    private static String YMD_HMS = "yyyy-MM-dd HH:mm:ss";

    public static String dateToStamp(String str) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long msTime = -1L;
        try {
            msTime = simpleDateFormat.parse(str).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return String.valueOf(msTime);
    }

    public static String getTimeState(String long_time) {
        String long_by_13 = "1000000000000";
        String long_by_14 = "1000000000";
        if (Long.valueOf(long_time) / Long.valueOf(long_by_13) < 1L && Long.valueOf(long_time) / Long.valueOf(long_by_14) >= 1L) {
            long_time += "000";
        }
        Timestamp time = new Timestamp(Long.valueOf(long_time));
        Timestamp now = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        long day_conver = 86400000L;
        long hour_conver = 3600000L;
        long min_conver = 60000L;
        long time_conver = now.getTime() - time.getTime();
        if (time_conver / day_conver >= 3L) {
            return format.format(time);
        }
        long temp_conver = time_conver / day_conver;
        if (temp_conver <= 2L && temp_conver >= 1L) {
            return temp_conver + "天前";
        }
        temp_conver = time_conver / hour_conver;
        if (temp_conver >= 1L) {
            return temp_conver + "小时前";
        }
        temp_conver = time_conver / min_conver;
        if (temp_conver >= 1L) {
            return temp_conver + "分钟前";
        }
        return "刚刚";
    }

    public static String stampToDate(String timeStamp) {
        String res = "";
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            res = simpleDateFormat.format(new Date(timeStamp));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }


    public static String getWatchTime(int second) {
        String times = "";
        if (second < 60) {
            times = "00:" + getSecondTime(second);
        } else if (second <= 3600) {
            int minute = second / 60;
            int seconds = second % 60;
            times = getSecondTime(minute) + ":" + getSecondTime(seconds);
        }
        return times;
    }


    public static String getDurationTime(int second) {
        String times = "";
        if (second < 60) {
            times = second +"秒";
        } else if (second <= 3600) {
            int minute = second / 60;
            int seconds = second % 60;
            times = minute + "分" + seconds+"秒";
        }else{
            int hour = second / 60 / 60;
            int minute = second / 60;
            int seconds = second % 60;
            times = hour + "时"+ minute + "分" + seconds+"秒";
        }
        return times;
    }

    public static String getSecondTime(int second) {
        String seconds;
        if (second < 10) {
            seconds = "0" + second;
        } else {
            seconds = "" + second;
        }
        return seconds;
    }

    /**
     * 字符串转Date
     *
     * @param date
     * @return
     * @throws Exception
     */
    public static String stringToDate_MD_HM(String date) {
        SimpleDateFormat format = new SimpleDateFormat(MD_HM);
        Date date1 = stringToDate(date);
        String datetime = "";
        try {
            datetime = format.format(date1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return datetime;
    }


    /**
     * 字符串转Date
     *
     * @param date
     * @return
     * @throws Exception
     */
    public static String stringToDate_MD_HMS(String date) {
        SimpleDateFormat format = new SimpleDateFormat(MD_HMS);
        Date date1 = stringToDate(date);
        String datetime = "";
        try {
            datetime = format.format(date1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return datetime;
    }

    /**
     * 字符串转Date
     *
     * @param date
     * @return
     * @throws Exception
     */
    public static Date stringToDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat(YMD_HMS);
        Date date1 = null;
        try {
            date1 = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date1;
    }


}
