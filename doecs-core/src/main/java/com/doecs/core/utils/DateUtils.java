package com.doecs.core.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    /**
     *  比较日期型数据精确到天的大小
     * @param date1
     * @param date2
     * @return 1：前者大，0：相同，-1：前者小
     */
    public static int dateDayCompare(Date date1, Date date2) {
        String dateFirst = fmtDate(date1, "yyyyMMdd");
        String dateLast = fmtDate(date2, "yyyyMMdd");
        int dateFirstIntVal = Integer.parseInt(dateFirst);
        int dateLastIntVal = Integer.parseInt(dateLast);
        if (dateFirstIntVal > dateLastIntVal) {
            return 1;
        } else if (dateFirstIntVal < dateLastIntVal) {
            return -1;
        }
        return 0;
    }

    @Deprecated
    public static String fmtDate(Date date, String fmt){
        if(date == null || StringUtils.isBlank(fmt))
            return "";
        SimpleDateFormat dateFormat = new SimpleDateFormat(fmt);
        return dateFormat.format(date);
    }

    public static String dateToStr(Date date, String fmt){
        if(date == null || StringUtils.isBlank(fmt))
            return "";
        SimpleDateFormat dateFormat = new SimpleDateFormat(fmt);
        return dateFormat.format(date);
    }
    public static Date strToDate(String dateStr, String fmt){
        if(StringUtils.isBlank(dateStr, fmt))
            return null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(fmt);
        try {
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }
    public static Date getTodayStartTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return  calendar.getTime();
    }

    public static Date getFirstDateOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取某月最小天数
        int firstDay = cal.getMinimum(Calendar.DATE);
        //设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH,firstDay);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        return cal.getTime();
    }

    public static Date getStartTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return  calendar.getTime();
    }

    public static Date getEndTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return  calendar.getTime();
    }
    /**
     * 获取指定年月的最后一天
     * @param year
     * @param month
     * @return
     */
    public static Date getLastDateOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DATE);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        return cal.getTime();
    }

    public static Calendar dataToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    /**
     *
     * @param startDate <String>
     * @param endDate <String>
     * @return int
     * @throws ParseException
     */
    public static int monthsBetween(Date startDate, Date endDate)
    {
        Calendar startCalendar = new GregorianCalendar();
        startCalendar.setTime(startDate);
        Calendar endCalendar = new GregorianCalendar();
        endCalendar.setTime(endDate);

        int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
        return diffMonth + 1;
    }

    public static final BigDecimal daysBetween(Date early, Date late) {

        Calendar calst = Calendar.getInstance();
        Calendar caled = Calendar.getInstance();
        calst.setTime(early);
        caled.setTime(late);
        //设置时间为0时
//        calst.set(java.util.Calendar.HOUR_OF_DAY, 0);
//        calst.set(java.util.Calendar.MINUTE, 0);
//        calst.set(java.util.Calendar.SECOND, 0);
//        caled.set(java.util.Calendar.HOUR_OF_DAY, 0);
//        caled.set(java.util.Calendar.MINUTE, 0);
//        caled.set(java.util.Calendar.SECOND, 0);
        //得到两个日期相差的天数
        BigDecimal days = BigDecimal.valueOf(((long) (caled.getTime().getTime() / 1000) - (long) (calst
                .getTime().getTime() / 1000))).divide(BigDecimal.valueOf(3600L * 24L), 6, RoundingMode.HALF_UP);

        return days;
    }
}
