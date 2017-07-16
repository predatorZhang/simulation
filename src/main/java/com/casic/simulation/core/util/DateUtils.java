package com.casic.simulation.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Administrator on 2015/4/16.
 */
public class DateUtils
{
    public static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    public static final SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");
    public static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat sdf5 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");


    public static Calendar getCalendar(Date date)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    public static int getFistDayOfMonth(Calendar calendar)
    {
        calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return  calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getLastDayOfMonth(Calendar calendar)
    {
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.get(Calendar.DAY_OF_MONTH)+1;
    }

    public static int getYear(Calendar calendar)
    {
        return  calendar.get(Calendar.YEAR);
    }

    public static  int getMonth(Calendar calendar)
    {
        return  calendar.get(Calendar.MONTH);
    }

    public static Date getFirstDayOfCurrentMonth() throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,0);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        return sdf1.parse(sdf1.format(calendar.getTime()));
    }

    public static Date getLastDayOfCurrentMonth() throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return  sdf1.parse(sdf1.format(calendar.getTime()));
    }


    public static Date AddDate(Date in, int dateNum) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(in);
        calendar.add(calendar.DATE, dateNum);//把日期往后增加一天.整数往后推,负数往前移动
        Date out = calendar.getTime();   //这个时间就是日期往后推一天的结果
        return out;
    }


    public static int getDateDiff(Date start, Date end) {
        long date1 = start.getTime();
        long date2 = end.getTime();
        int days = (int) ((date2 - date1)/(1000 * 60 * 60 * 24));
        return days;
    }
}
