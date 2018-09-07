package com.kangengine.customview.util;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author : Vic
 * time   : 2018/09/06
 * desc   :
 */
public class DateUtil {
    /** 自定义固定时间 **/
    public final static int DATE_MODE_FIX = 1;
    /** 按照月份提醒 **/
    public final static int DATE_MODE_MONTH = 2;
    /** 按照周提醒 **/
    public final static int DATE_MODE_WEEK = 3;
    private static final SimpleDateFormat format2YMDHM = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final SimpleDateFormat format2HM = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat format2HM1= new SimpleDateFormat("HH : mm");
    private static final SimpleDateFormat format2CNMD = new SimpleDateFormat("yyyy年MM月d日");
    private static final SimpleDateFormat format2YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat formatYMD = new SimpleDateFormat("yyyy.MM.dd");
    private static final SimpleDateFormat format2YMDHMS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat format2MD = new SimpleDateFormat("MM月dd日");
    private static final SimpleDateFormat format2YYYY_MM = new SimpleDateFormat("yyyy-MM");
    private static final SimpleDateFormat format2MMDD = new SimpleDateFormat("MM/dd");
    private static final SimpleDateFormat format2YYYYMM = new SimpleDateFormat("yyyy/MM");
    private static final SimpleDateFormat format2YMDHMS1 = new SimpleDateFormat("yyyy.MM.dd HH:mm");

    /**
     * MM/dd
     */
    public static String getMMDD(long timestemp) {
        if(String.valueOf(timestemp).length()<=10){
            timestemp=timestemp*1000;
        }
        return format2MMDD.format(new Date(timestemp));
    }
    /**
     * YYYY/MM
     */
    public static String getYYYYMM(long timestemp) {
        if(String.valueOf(timestemp).length()<=10){
            timestemp=timestemp*1000;
        }
        return format2YYYYMM.format(new Date(timestemp));
    }
    /**
     * 2016.10.27
     * @param millis
     * @return
     */
    public static String getYearMonthDay(long millis){
        if(String.valueOf(millis).length()<=10){
            millis=millis*1000;
        }
        return format2YYYY_MM_DD.format(new Date(millis));
    }

    /**
     * HH:mm
     * 返回当前时间
     * @return
     */
    public static String getCurHourMinute() {
        return format2HM.format(new Date());
    }

    /**
     * HH : mm
     * 返回当前时间
     * @return
     */
    public static String getHomeCurHourMinute() {
        return format2HM1.format(new Date());
    }
    /**
     * HH:mm
     *
     * @return
     */
    public static String getHourMinute(long millis ) {
        if (String.valueOf(millis).length() <= 10) {
            return format2HM.format(new Date(millis*1000));
        }
        return format2HM.format(new Date(millis));
    }

    /**
     * 获取当期日期的年份
     * @return
     */
    public static int getCurYear(){
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int year=calendar.get(Calendar.YEAR);
        return year;
    }

    /**
     * yyyy-MM-dd
     *
     * @return
     */
    public static String getYearMonthDay() {
        return format2YYYY_MM_DD.format(new Date());
    }
    /**
     * yyyy年MM月dd日
     *
     * @return
     */
    public static String getChineseYearMonthDay() {
        return format2CNMD.format(new Date());
    }

    /**
     * 返回明天的日期
     * yyyy年MM月dd日
     * @return
     */
    public static String getTomorrow() {
        Calendar calenda=Calendar.getInstance();
        calenda.setTimeInMillis(System.currentTimeMillis());
        calenda.set(Calendar.DAY_OF_YEAR, calenda.get(Calendar.DAY_OF_YEAR)+1);

        Date date=new Date();
        date.setTime(calenda.getTimeInMillis());
        return format2CNMD.format(date);
    }

    /**
     * 返回明天的日期
     * yyyy年MM月dd日
     * @return
     */
    public static long getTomorrowTime() {
        Calendar calenda=Calendar.getInstance();
        calenda.setTimeInMillis(System.currentTimeMillis());
        calenda.set(Calendar.DAY_OF_YEAR, calenda.get(Calendar.DAY_OF_YEAR)+1);
        return calenda.getTimeInMillis();
    }

    /**
     * y年M月d日
     *
     * @param stamp
     * @return
     */
    public static String stamp2CnYMD(long stamp) {
        if(String.valueOf(stamp).length()<=10){
            stamp=stamp*1000;
        }
        return format2CNMD.format(stamp);
    }

    /**
     * M月d日
     *
     * @return
     */
    public static String getCnMD() {
        return format2MD.format(new Date());
    }

    /**
     * M月d日
     *
     * @return
     */
    public static String getCnMD(long timestemp) {
        if(String.valueOf(timestemp).length()<=10){
            timestemp=timestemp*1000;
        }
        return format2MD.format(new Date(timestemp));
    }

    /**
     * 血压闹钟下次提醒的时间
     *
     * @param dateMode
     *            自定义时间，安月提醒，按周提醒 1、DATE_MODE_FIX：指定日期，如20120301 ,
     *            参数dateValue格式：2012-03-01 2、DATE_MODE_WEEK：按星期提醒，如星期一、星期三 ,
     *            参数dateValue格式：1,3 3、DATE_MODE_MONTH：按月提醒，如3月2、3号，4月2、3号,
     *            参数dateValue格式：3,4|2,3
     * @param dateValue
     *            首次星期时间,如：周日传入参数为7,周一传入为1,周二传入为2等等
     * @param startTime
     *            开始时间,为当天开始时间，如上午9点, 参数格式为09:00
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static long getNextAlarmTime(int dateMode, String dateValue, String startTime) {
        final SimpleDateFormat fmt = new SimpleDateFormat();
        final Calendar c = Calendar.getInstance();
        final long now = System.currentTimeMillis();
        // 设置开始时间
        try {
            if (DATE_MODE_FIX == dateMode) {
                fmt.applyPattern("yyyy年MM月dd日");
                Date d = fmt.parse(dateValue);
                c.setTimeInMillis(d.getTime());
            }

            fmt.applyPattern("HH:mm");
            Date d = fmt.parse(startTime);
            c.set(Calendar.HOUR_OF_DAY, d.getHours());
            c.set(Calendar.MINUTE, d.getMinutes());
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        long nextTime = 0;
        if (DATE_MODE_FIX == dateMode) { // 按指定日期
            nextTime = c.getTimeInMillis();
            // 指定日期已过
//			if (now >= nextTime)
//				nextTime = 0;
        } else if (DATE_MODE_WEEK == dateMode) { // 按周
            final long[] checkedWeeks = parseDateWeeks(dateValue);
            if (null != checkedWeeks) {
                for (long week : checkedWeeks) {
                    c.set(Calendar.DAY_OF_WEEK, (int) (week + 1));

                    long triggerAtTime = c.getTimeInMillis();
                    if (triggerAtTime <= now) { // 下周
                        triggerAtTime += AlarmManager.INTERVAL_DAY * 7;
                    }
                    // 保存最近闹钟时间
                    if (0 == nextTime) {
                        nextTime = triggerAtTime;
                    } else {
                        nextTime = Math.min(triggerAtTime, nextTime);
                    }
                }
            }
        } else if (DATE_MODE_MONTH == dateMode) { // 按月
            final long[][] items = parseDateMonthsAndDays(dateValue);
            final long[] checkedMonths = items[0];
            final long[] checkedDays = items[1];

            if (null != checkedDays && null != checkedMonths) {
                boolean isAdd = false;
                for (long month : checkedMonths) {
                    c.set(Calendar.MONTH, (int) (month - 1));
                    for (long day : checkedDays) {
                        c.set(Calendar.DAY_OF_MONTH, (int) day);

                        long triggerAtTime = c.getTimeInMillis();
                        if (triggerAtTime <= now) { // 下一年
                            c.add(Calendar.YEAR, 1);
                            triggerAtTime = c.getTimeInMillis();
                            isAdd = true;
                        } else {
                            isAdd = false;
                        }
                        if (isAdd) {
                            c.add(Calendar.YEAR, -1);
                        }
                        // 保存最近闹钟时间
                        if (0 == nextTime) {
                            nextTime = triggerAtTime;
                        } else {
                            nextTime = Math.min(triggerAtTime, nextTime);
                        }
                    }
                }
            }
        }
        return nextTime;
    }


    private static long[] parseDateWeeks(String value) {
        long[] weeks = null;
        try {
            final String[] items = value.split(",");
            weeks = new long[items.length];
            int i = 0;
            for (String s : items) {
                weeks[i++] = Long.valueOf(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return weeks;
    }

    private static long[][] parseDateMonthsAndDays(String value) {
        long[][] values = new long[2][];
        try {
            final String[] items = value.split("\\|");
            final String[] monthStrs = items[0].split(",");
            final String[] dayStrs = items[1].split(",");
            values[0] = new long[monthStrs.length];
            values[1] = new long[dayStrs.length];

            int i = 0;
            for (String s : monthStrs) {
                values[0][i++] = Long.valueOf(s);
            }
            i = 0;
            for (String s : dayStrs) {
                values[1][i++] = Long.valueOf(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return values;
    }

    /**
     * 时间戳转日期
     */
    public static String stamp2Time(String str) {
        String sd = format2YMDHM.format(new Date(Long.parseLong(str)));
        return sd;
    }

    /**
     * 日期转时间戳
     * @return
     */
    public static long time2Stemp(String str){
        long result=0;
        try {
            result = format2CNMD.parse(str).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 时间戳转具体日期时间
     */
    public static String stamp2DayTime(String str) {
        String sd = format2YMDHMS.format(new Date(Long.parseLong(str)));
        return sd;
    }

    /**
     * 时间戳转具体日期时间
     */
    public static String stamp2DayTime(long timeStamp) {
        if(String.valueOf(timeStamp).length() <= 10) {
            timeStamp = timeStamp*1000;
        }
        String sd = format2YMDHMS.format(new Date(timeStamp));
        return sd;
    }

    /**
     * yyyy.MM.dd HH:mm:ss
     * @param timeStamp
     * @return
     */
    public static String stamp2DayTime1(long timeStamp){
        if(String.valueOf(timeStamp).length() <= 10) {
            timeStamp = timeStamp * 1000;
        }
        return format2YMDHMS1.format(new Date(timeStamp));
    }

    public static String getMonthDayYear(long timeStamp) {
        if(String.valueOf(timeStamp).length() <= 10) {
            timeStamp = timeStamp*1000;
        }
        return format2YYYY_MM_DD.format(new Date(timeStamp));
    }
    public static String getMonthYear(long timeStam){
        if(String.valueOf(timeStam).length() <= 10) {
            timeStam = timeStam * 1000;
        }
        return format2YYYY_MM.format(new Date(timeStam));
    }


    public static long getYearMonthDay(String dateStr){
        long unixTimeStamp = 0;
        try {
            Date  date = format2YYYY_MM_DD.parse(dateStr);
            unixTimeStamp = date.getTime()/1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return unixTimeStamp;
    }

    public static long getYearMonth(String dateStr){
        long unixTimeStamp = 0;
        try {
            Date  date = format2YYYY_MM.parse(dateStr);
            unixTimeStamp = date.getTime()/1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return unixTimeStamp;
    }
    public static int getTodayDay(){
        Calendar calendar= Calendar.getInstance();
        calendar.clear();
        calendar.setTime(new Date());
        return calendar.get(Calendar.DAY_OF_MONTH);
    }


    /**
     * 取得时间戳
     *
     * @return
     */
    public static long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

    // 获得当天0点时间
    public static int getTimesmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (int) (cal.getTimeInMillis() / 1000);
    }

    // 获得当天24点时间
    public static int getTimesnight() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (int) (cal.getTimeInMillis() / 1000);
    }

    public static int getBefore7DayTime() {
        return getTimesnight() - 604800;
    }

    // 获得当天0点时间
    public static long getTimesmorningInMillis() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    // 获得明天0点时间
    public static long getNextmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.add(Calendar.DATE, 1);
        return cal.getTimeInMillis();
    }

    // 获得当天早上8点时间
    public static long getMornigEightInMillis() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 8);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    // 获得当天24点时间
    public static long getTimesnightInMillis() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 24);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    // 获得本周一0点时间
    public static long getTimesWeekmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.add(Calendar.DAY_OF_MONTH, -1);// 解决周日会出现 并到下一周的情况
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return cal.getTimeInMillis();
    }

    // 获得本周日24点时间
    public static long getTimesWeeknight() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.add(Calendar.DAY_OF_MONTH, -1);// 解决周日会出现 并到下一周的情况
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return cal.getTime().getTime() + (7 * 24 * 60 * 60 * 1000);
    }

    // 获得本月第一天0点时间
    public static long getTimesMonthmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTimeInMillis();
    }

    // 获得本月最后一天24点时间
    public static long getTimesMonthnight() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 24);
        return cal.getTimeInMillis();
    }

    /**
     * 把一个生日转换成年龄
     *
     * @param birthday
     *            yyyy-MM-dd
     * @return age
     */
    public static long tansToAge(String birthday) {
        try {
            Date birth = format2YYYY_MM_DD.parse(birthday);
            Date today = new Date();
            long secondOfYear = 60 * 60 * 24 * 365;
            return (today.getTime() - birth.getTime()) / 1000 / secondOfYear + 1;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 把一个时间戳转为日期类型
     *
     * @param birthday
     *            yyyy-MM-dd
     * @return age
     */
    public static Date  transDate(String birthday) {
        try {
            Date current = format2YYYY_MM_DD.parse(birthday);

            return current;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取对应日期是周几
     * @param dt
     * @return
     */
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0){
            w = 0;
        }

        return weekDays[w];
    }

    /**
     * 取得相隔数量的日期的日期
     *
     * @param date
     *            指定日期
     * @param interval
     *            BEFORE:往前，AFTER：往后
     * @param interval
     *            相隔的日期
     */
    public static long getIntervalDate(Date date, int interval) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, interval);
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        return cal.getTime().getTime() / 1000;
    }

    /**
     * 获得指定日期的前一天
     *
     * @param specifiedDay
     * @return
     * @throws Exception
     */
    public static Date getSpecifiedDayBefore(String specifiedDay) {// 可以用new
        // Date().toLocalString()传递参数
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day - 1);
        return c.getTime();
    }

    /**
     * 获得今天的前后几天,day
     *
     * @return distance 与今天的相差的天数。
     * 今天之后是正直，之前是负值
     * @throws Exception
     */
    public static String getNearDay(int distance) {
        Calendar calendar= Calendar.getInstance();
        calendar.clear();
        calendar.setTime(new Date());
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + distance);
        return String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * 获得今天的前后几天,year,month,day
     *
     * @return distance 与今天的相差的天数。
     * 今天之后是正直，之前是负值
     * @throws Exception
     */
    public static String getNearTime(int distance) {
        Calendar calendar= Calendar.getInstance();
        calendar.clear();
        calendar.setTime(new Date());
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + distance);
        return format2CNMD.format(calendar.getTime());
    }

    /**
     * 获得今天的前后几天,时间戳
     *
     * @return distance 与今天的相差的天数。
     * 今天之后是正直，之前是负值
     * @throws Exception
     */
    public static long getNearDayTime(int distance) {
        Calendar calendar= Calendar.getInstance();
        calendar.clear();
        calendar.setTime(new Date());
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + distance);
        return calendar.getTimeInMillis()/1000;
    }

    /**
     * 获得指定日期的后一天
     *
     * @param specifiedDay
     * @return
     * @throws Exception
     */
    public static Date getSpecifiedDayAfter(String specifiedDay) {// 可以用new
        // Date().toLocalString()传递参数
        Calendar c = Calendar.getInstance();
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(specifiedDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day + 1);
        return c.getTime();
    }


    public static int compareDate(long d1, long d2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(d1);
        int y1 = c1.get(Calendar.YEAR);
        int day1 = c1.get(Calendar.DAY_OF_YEAR);

        Calendar c2 = Calendar.getInstance();
        c2.setTimeInMillis(d2);
        int y2 = c2.get(Calendar.YEAR);
        int day2 = c2.get(Calendar.DAY_OF_YEAR);
        if (y1 > y2) {
            return 1;
        } else if (y1 < y2) {
            return -1;
        } else {
            if (day1 > day2) {
                return 1;
            } else if (day1 < day2) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    //获取指定毫秒数的对应星期
    public static String getWeek(long millis) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(millis);
        String week = "";
        int cweek = calendar.get(Calendar.DAY_OF_WEEK);
        switch (cweek) {
            case 1:
                week = "周日";
                break;
            case 2:
                week = "周一";
                break;
            case 3:
                week = "周二";
                break;
            case 4:
                week = "周三";
                break;
            case 5:
                week = "周四";
                break;
            case 6:
                week = "周五";
                break;
            case 7:
                week = "周六";
                break;
        }
        return week;

    }

    private static ThreadLocal<SimpleDateFormat> DateLocal = new ThreadLocal<SimpleDateFormat>();
    /**
     * 判断是否为今天
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true今天 false不是
     * @throws ParseException
     */
    public static boolean isToday(String day) throws ParseException {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = getDateFormat().parse(day);
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否为昨天
     * @param day 传入的 时间  "2016-06-28 10:10:30" "2016-06-28" 都可以
     * @return true昨天 false不是
     * @throws ParseException
     */
    public static boolean isYesterday(String day) throws ParseException {

        Calendar pre = Calendar.getInstance();
        Date predate = new Date(System.currentTimeMillis());
        pre.setTime(predate);

        Calendar cal = Calendar.getInstance();
        Date date = getDateFormat().parse(day);
        cal.setTime(date);

        if (cal.get(Calendar.YEAR) == (pre.get(Calendar.YEAR))) {
            int diffDay = cal.get(Calendar.DAY_OF_YEAR)
                    - pre.get(Calendar.DAY_OF_YEAR);

            if (diffDay == -1) {
                return true;
            }
        }
        return false;
    }

    public static SimpleDateFormat getDateFormat() {
        if (null == DateLocal.get()) {
            DateLocal.set(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA));
        }
        return DateLocal.get();
    }



    /**
     * 获取最近7天的日期
     */
    public static List<String> getLatest7Dates() {
        List<String> dates = new ArrayList<String>();
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        java.text.SimpleDateFormat sim = new java.text.SimpleDateFormat(
                "MM/dd");
        String date = sim.format(c.getTime());
        dates.add(date);
        for (int i = 0; i < 6; i++) {
            c.add(java.util.Calendar.DAY_OF_MONTH, -1);
            date = sim.format(c.getTime());
            dates.add(date);
        }
        Collections.reverse(dates);
        return dates;
    }

    /**
     * 获取最近30天的日期
     */
    public static List<String> getLatest30Dates() {
        List<String> dates = new ArrayList<String>();
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        java.text.SimpleDateFormat sim = new java.text.SimpleDateFormat(
                "MM/dd");
        String date = sim.format(c.getTime());
        dates.add(date);
        for (int i = 0; i < 29; i++) {
            c.add(java.util.Calendar.DAY_OF_MONTH, -1);
            date = sim.format(c.getTime());
            dates.add(date);
        }
        Collections.reverse(dates);
        return dates;
    }

    /**
     * 获取最近12个月
     */
    public static List<String> getLatest12Months(){
        List<String> dates = new ArrayList<String>();
        String dateString;
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
        dateString = sdf.format(cal.getTime());
        for (int i = 0; i < 12; i++) {
            dateString = sdf.format(cal.getTime());
            cal.add(Calendar.MONTH, -1);
            dates.add(dateString);
        }
        Collections.reverse(dates);
        return dates;
    }


    /**
     * 得到最近一次的日期
     * 如果是当天，显示今日
     * 否则显示：2017年8月1日 星期一
     * @return
     */
    public static String getRecentDay(long timeStamp){
        String[] weekstring = new String[] { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        StringBuilder result=new StringBuilder("");
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        int cur_year = cal.get(Calendar.YEAR);
        int cur_month = cal.get(Calendar.MONTH) ;
        int cur_day = cal.get(Calendar.DAY_OF_MONTH);

        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        if(year==cur_year && month==cur_month && day==cur_day){
            result.append("今天");
        }else{
            result.append(year).append("年")
                    .append( month+1 ).append("月")
                    .append(day).append("日")
                    .append(" ").append(weekstring[calendar.get(Calendar.DAY_OF_WEEK)-1]);
        }
        return result.toString();
    }


    //字符串转时间戳
    public static String getTime(String timeString){
        String timeStamp = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d;
        try{
            d = sdf.parse(timeString);
            long l = d.getTime();
            timeStamp = String.valueOf(l);
        } catch(ParseException e){
            e.printStackTrace();
        }
        return timeStamp;
    }

    //时间戳转字符串
    public static String getStrTime(String timeStamp){
        String timeString = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        long  l = Long.valueOf(timeStamp);
        timeString = sdf.format(new Date(l));//单位秒
        return timeString;
    }

    /**
     * 获取当前时间所在的周的星期一
     * @return
     */
    public static String getMondayByWeek() {
        String date = getYearMonthDay();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance(Locale.CHINA);
        cal.setTime(d);
        cal.add(Calendar.DAY_OF_MONTH,-1);
        cal.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
        String monday = df.format(cal.getTime());
        return monday;
    }


    /**
     * 获取当前时间所在的周的星期日
     * @return
     */
    public static String getSundayByWeek() {
        String date = getYearMonthDay();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = df.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance(Locale.CHINA);
        cal.setTime(d);
        cal.add(Calendar.DAY_OF_MONTH,-1);
        cal.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
        cal.add(Calendar.WEEK_OF_YEAR,1);
        String sunday = df.format(cal.getTime());
        return sunday;
    }

}

