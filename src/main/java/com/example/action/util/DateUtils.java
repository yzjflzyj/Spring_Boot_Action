package com.example.action.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    private static final String COMMON_DATE = "yyyy-MM-dd HH:mm:ss";
    private static final String COMMON_DAY1 = "yyyy-MM-dd";
    private static final String COMMON_DAY2 = "yyyyMMdd";
    private static final String COMMON_DATE_DETAIL = "yyyyMMddHHmmssSSS";


    public static Date getAppointDate(Date date, int day, int hour, int min, int sec, int millisecond) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, day);
        calendar.add(Calendar.HOUR_OF_DAY, hour);
        calendar.add(Calendar.MINUTE, min);
        calendar.add(Calendar.SECOND, sec);
        calendar.add(Calendar.MILLISECOND, millisecond);
        return calendar.getTime();
    }

    public static Date getFormatDate(String date, String formatWay) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(formatWay);
        return sdf.parse(date);
    }

    public static String getFormatString(Date date, String formatWay) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatWay);
        return sdf.format(date);
    }
}
