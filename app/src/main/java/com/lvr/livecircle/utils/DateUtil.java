package com.lvr.livecircle.utils;

import android.annotation.TargetApi;
import android.os.Build;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by peter on 22/5/2017.
 */

public class DateUtil {
    public static final String FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_CHOICE = "yy/MM/dd";
    public static final String FORMAT_TIME = "HH:mm";
    public static final String FORMAT_BRITH="yyyyMMdd";
    public static final String FORMAT_DEFAULT_EUROPE="dd/MM/yyyy HH:mm:ss";


    public static Date str2Date(String str) {
        return str2Date(str, null);
    }

    public static Date str2Date(String str, String format) {
        if (str == null || str.length() == 0) {
            return null;
        }
        if (format == null || format.length() == 0) {
            format = FORMAT_DEFAULT;
        }
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(str);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;

    }

    public static String long2Date(long startDate) {
        Date date = new Date();
        date.setTime(startDate);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(FORMAT_DEFAULT);
        return simpleDateFormat.format(date);
    }

    public static String date2Str(Date d) {// yyyy-MM-dd HH:mm:ss
        return date2Str(d, null);
    }

    public static String date2Str(Date d, String format) {// yyyy-MM-dd HH:mm:ss
        if (d == null) {
            return null;
        }
        if (format == null || format.length() == 0) {
            format = FORMAT_DEFAULT;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String s = sdf.format(d);
        return s;
    }

    public static String getWeekOfDate(Date date, boolean isLanguageZH) {
        //TODO
        String[] weekDaysNameZH = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        String[] weekDaysNameEN = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        String[] weekDaysCode = {"0", "1", "2", "3", "4", "5", "6"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return isLanguageZH ? weekDaysNameZH[intWeek] : weekDaysNameEN[intWeek];
    }


    public static boolean meetingOpen30min(String startDate) {
        Date date = new Date();
        long now = date.getTime();
        long startTime;
        if (startDate.length() == 13)
            startTime = Long.parseLong(startDate);
        else
            startTime = (Long.parseLong(startDate) * 1000);
        if (startTime <now)
            return true;
        if (startTime > now && startTime - now < 1000 * 60 * 30)
            return true;
        return false;
    }

    public static boolean meetingAfter2hour(String startDate) {
        Date date = new Date();
        long now = date.getTime();
        long startTime;
        if (startDate.length() == 13)
            startTime = Long.parseLong(startDate);
        else
            startTime = (Long.parseLong(startDate) * 1000);

        if (startTime < now && now - startTime > 1000 * 60 * 60*2)
            return true;
        return false;
    }

    public static boolean meetingAfter30min(String startDate) {
        Date date = new Date();
        long now = date.getTime();
        long startTime;
        if (startDate.length() == 13)
            startTime = Long.parseLong(startDate);
        else
            startTime = (Long.parseLong(startDate) * 1000);

        if (startTime > now && startTime - now > 1000 * 60 * 30)
            return true;
        return false;
    }
/*
    public static String covert2MettingsTime(long startDate) {

        Calendar c = Calendar.getInstance();
        Date date = new Date();
        date.setTime(startDate);
        c.setTime(date);
        String result = null;

        result = "\t"+ c.get(Calendar.YEAR) + "年" + (c.get(Calendar.MONTH) + 1) + "月"
                + c.get(Calendar.DAY_OF_MONTH) + "日" + "\t"+convert24ToAmPm(String.valueOf(c.get(Calendar.HOUR_OF_DAY) + ":" + ((c.get(Calendar.MINUTE)<10)?"0"+c.get(Calendar.MINUTE):c.get(Calendar.MINUTE))));
//                    + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE)
//                    + ":" + c.get(Calendar.SECOND);
        return result;

    }*/

    public static Date StartMeetingTime(Date date){
        Date date1=new Date();
        long startTime=date.getTime();
        date1.setTime(startTime+1000*60*60);
        return date1;
    }

    public static Calendar str2Calendar(String str) {
        return str2Calendar(str, null);

    }

    public static Calendar str2Calendar(String str, String format) {

        Date date = str2Date(str, format);
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        return c;

    }
    public static String date2Str(Calendar c) {// yyyy-MM-dd HH:mm:ss
        return date2Str(c, null);
    }

    public static String date2Str(Calendar c, String format) {
        if (c == null) {
            return null;
        }
        return date2Str(c.getTime(), format);
    }

    /**
     * 返回标准GMT时间
     *
     * @param startTime
     * @return
     */
    public static String convert2GMTTime(String startTime) {
        return convert2GMTTime(startTime, null);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static String convert2GMTTime(String startTime, String timeZoneId) {
        Calendar calendar = str2Calendar(startTime);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - curTimeZoneOffset(HOUR_TYPE, timeZoneId));
        return date2Str(calendar);
    }

    /**
     * 返回毫秒数
     *
     * @return
     */
    public static final int MILLION_TYPE = 1;
    public static final int HOUR_TYPE = 2;

    public static int curTimeZoneOffset(int type, String timezoneId) {

        TimeZone tz = null;

        if (timezoneId==null)
            tz = TimeZone.getDefault();
        else
            tz = TimeZone.getTimeZone(timezoneId);

        int curOffest = tz.getRawOffset();
        if (tz.inDaylightTime(new Date()))
            curOffest = curOffest + tz.getDSTSavings();
        if (type == MILLION_TYPE)
            return curOffest;
        else
            return curOffest / 1000 / 3600;
    }

}
