package com.pwrd.support;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * User: ylwys
 * Date: 2015/7/14
 * Time: 20:33
 */
public final class DateUtil {

    public static String getYear(Date date) {
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        return yearFormat.format(date);
    }

    public static String getMonth(Date date) {
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        return monthFormat.format(date);
    }

    public static String getDay(Date date) {
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
        return dayFormat.format(date);
    }

    public static String getHour(Date date) {
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH");
        return hourFormat.format(date);
    }

    public static String getMinute(Date date) {
        SimpleDateFormat minuteFormat = new SimpleDateFormat("mm");
        return minuteFormat.format(date);
    }

    public static String getSecond(Date date) {
        SimpleDateFormat secondFormat = new SimpleDateFormat("ss");
        return secondFormat.format(date);
    }

    public static int minutesBeforeNextHour() {
        return 60 - Integer.parseInt(getMinute(new Date()));
    }

    public static int getDayOfWeek() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    /**
     * 判断两个日期是不是同一天
     *
     * @param curDate  curDate
     * @param lastDate lastDate
     * @return boolean
     */
    public static boolean inCommonDay(Date curDate, Date lastDate) {
        if (lastDate == null) {
            return true;
        }
        return (getYear(curDate).equals(getYear(lastDate)) && getMonth(curDate).equals(getMonth(lastDate)) && getDay(curDate).equals(getDay(lastDate)));
    }

    public static boolean inCommonMonth(Date date1, Date date2) {
        if (date2 == null) {
            return true;
        }
        return (getYear(date1).equals(getYear(date2)) && getMonth(date1).equals(getMonth(date2)));
    }

    /**
     * 判断是否是跨天两个日期
     *
     * @param date1
     * @param date2
     * @param hourLimit
     * @param minuteLimit
     * @return
     */
    public static boolean isPassDay(Date date1, Date date2, int hourLimit, int minuteLimit) {
        if (date2 != null && date1 != null) {
            long date1Time = date1.getTime();
            long date2Time = date2.getTime();
            long curDateTime;
            long lastDateTime;
            if (date1Time > date2Time) {
                curDateTime = date1Time;
                lastDateTime = date2Time;
            } else {
                curDateTime = date2Time;
                lastDateTime = date1Time;
            }

            if (curDateTime - lastDateTime >= 86400000L) {
                return true;
            } else {
                Calendar limitCalendar = Calendar.getInstance();
                limitCalendar.setTimeInMillis(curDateTime);
                limitCalendar.set(Calendar.HOUR_OF_DAY, hourLimit);
                limitCalendar.set(Calendar.MINUTE, minuteLimit);
                limitCalendar.set(Calendar.SECOND, 0);
                long limitTime = limitCalendar.getTime().getTime();
                return lastDateTime < limitTime && curDateTime >= limitTime;
            }
        }
        return true;
    }

    /**
     * 获取当前月的最大天数
     *
     * @return int
     */
    public static int getMaxDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getActualMaximum(Calendar.DATE);
    }

    /**
     * 获取游戏星期数(dayLimit点之前是上一天)
     */
    public static int getGameWeek(Date date, int dayLimit) {
        int curHour = Integer.parseInt(getHour(date));
        Calendar calendar = Calendar.getInstance();
        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
        //换算成正常时间
        if (weekDay == 1) {
            weekDay = 7;
        } else {
            weekDay -= 1;
        }
        //移到前一天
        if (curHour < dayLimit) {
            weekDay -= 1;
            weekDay = weekDay == 0 ? 7 : weekDay;
        }
        return weekDay;
    }

    /**
     * 获取下一个整点剩余的秒
     *
     * @return int
     */
    public static int getHourLeft(int hour) {
        long currentTimeMillis = System.currentTimeMillis();
        long nextTimeMills = currentTimeMillis + 86400000;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(nextTimeMills);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long nextThreeTimeMills = calendar.getTime().getTime();
        return MathTool.getRoundInt((nextThreeTimeMills - currentTimeMillis) / 1000f);
    }

    public static String getSendChatTimeStr(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(date);
    }

    public static String getDateFormatStr(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }
}
