package com.hobbyer.android.utils;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 *
 */
public class StringUtils {
    public static final String DATE_DD_MM_YYYY_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";


    private StringUtils() {
        throw new Error("U will not able to instantiate it");
    }


    public static boolean isBlank(String s) {
        return (s == null || s.trim().length() == 0);
    }


    public static boolean isCheck(String s){

        return (s == null || s.trim().length() == 0);
    }

    public static boolean isNullOrEmpty(String s) {
        return s == null || s.trim().length() == 0 || s.equalsIgnoreCase("null");
    }
    public static int length(String s) {
        return s == null ? 0 : s.length();
    }


    public static String[] changeDateFormatArray(String date){
        //  date=" 2016-09-08" yyyy-mm-dd;
          String[] dateArr=date.split("-");
          String months[]={"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
          String days[]={"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"};
          String dd=dateArr[2].replaceAll("[^a-zA-Z0-9_-]", "");;
          String mon=dateArr[1].replaceAll("[^a-zA-Z0-9_-]", "");
          String year=dateArr[0];
          int day = Integer.parseInt(dd);
           String dds=days[day-4];

          int month = Integer.parseInt(mon);
          String mm=months[month-1]; //
        //     String[] myDate=mm+" "+dd;
          String monthDateArrayarr[]={year,mm,dds};
          //dd+"-"+mm+"-"+yy;
        Log.i("",monthDateArrayarr[0]);
        return monthDateArrayarr;
    }
    public static int[] changeDateFormat(String date){
        //  date=" 2016-09-08" yyyy-mm-dd;
        String[] dateArr=date.split("-");
        String dd=dateArr[2];;
        String mon=dateArr[1];
        String year=dateArr[0];
        int years = Integer.parseInt(year);
        int day = Integer.parseInt(dd);
        int month = Integer.parseInt(mon);

        //     String[] myDate=mm+" "+dd;
        int monthDateArrayarr[]={years,month,day};
        //dd+"-"+mm+"-"+yy;

        return monthDateArrayarr;
    }
    public static int[] changeTimeFormat(String time){
        String splitTime[]=time.split(":");
        String hour=splitTime[0];
        String minute=splitTime[1];
        int hours = Integer.parseInt(hour);
        int minutes = Integer.parseInt(minute);

        //     String[] myDate=mm+" "+dd;
        int timeArrayarr[]={hours,minutes};
        //dd+"-"+mm+"-"+yy;

        return timeArrayarr;
    }

    public static String weekDays(String weekDays){
        String todWeekDays= null;
        int weekNumber = 0;
        if (weekDays!=null) {
            if (weekDays.equalsIgnoreCase("Monday")) {
                weekNumber = 1;
            } else if (weekDays.equalsIgnoreCase("Tuesday")) {
                weekNumber = 2;
            } else if (weekDays.equalsIgnoreCase("Wednesday")) {
                weekNumber = 3;
            } else if (weekDays.equalsIgnoreCase("Thursday")) {
                weekNumber = 4;
            } else if (weekDays.equalsIgnoreCase("Friday")) {
                weekNumber = 5;
            } else if (weekDays.equalsIgnoreCase("Saturday")) {
                weekNumber = 6;
            } else if (weekDays.equalsIgnoreCase("Sunday")) {
                weekNumber = 7;
            }
            todWeekDays  = Integer.toString(weekNumber);
        }

        return todWeekDays;
    }




    public static String dateFormat(String date){

        int dateResult;
        int dataTotal;
        int weekNumbers;
        String weekNumber;
        String todayAsString=null;
        String todWeekDays= null;
        Calendar calendar = Calendar.getInstance();
        Date weekDay = calendar.getTime();
        SimpleDateFormat weekFormat = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        todWeekDays = weekFormat .format(weekDay.getTime());
        System.out.println(todWeekDays);
        weekNumber = StringUtils.weekDays(todWeekDays);
        weekNumbers = Integer.parseInt(weekNumber);
        dateResult = Integer.parseInt(date);
        dataTotal =dateResult-weekNumbers;
        if (date!=null) {
            if (date.equalsIgnoreCase("0")) {
                calendar.add(Calendar.DAY_OF_YEAR, dataTotal);
                Date today = calendar.getTime();
                DateFormat dateFormat = new SimpleDateFormat("dd-MMM");
                todayAsString = dateFormat.format(today);
            }else if (date.equalsIgnoreCase("1")){
                calendar.add(Calendar.DAY_OF_YEAR, dataTotal);
                Date today = calendar.getTime();
                DateFormat dateFormat = new SimpleDateFormat("dd-MMM");
                todayAsString = dateFormat.format(today);
            }else if (date.equalsIgnoreCase("2")){
                calendar.add(Calendar.DAY_OF_YEAR, dataTotal);
                Date today = calendar.getTime();
                DateFormat dateFormat = new SimpleDateFormat("dd-MMM");
                todayAsString = dateFormat.format(today);
            }else if (date.equalsIgnoreCase("3")){
                calendar.add(Calendar.DAY_OF_YEAR, dataTotal);
                Date today = calendar.getTime();
                DateFormat dateFormat = new SimpleDateFormat("dd-MMM");
                todayAsString = dateFormat.format(today);
            }else if (date.equalsIgnoreCase("4")){
                calendar.add(Calendar.DAY_OF_YEAR, dataTotal);
                Date today = calendar.getTime();
                DateFormat dateFormat = new SimpleDateFormat("dd-MMM");
                todayAsString = dateFormat.format(today);
            }else if (date.equalsIgnoreCase("5")){
                calendar.add(Calendar.DAY_OF_YEAR, dataTotal);
                Date today = calendar.getTime();
                DateFormat dateFormat = new SimpleDateFormat("dd-MMM");
                todayAsString = dateFormat.format(today);
            }else if (date.equalsIgnoreCase("6")){
                calendar.add(Calendar.DAY_OF_YEAR, dataTotal);
                Date today = calendar.getTime();
                DateFormat dateFormat = new SimpleDateFormat("dd-MMM");
                todayAsString = dateFormat.format(today);
            }else if (date.equalsIgnoreCase("7")){
                calendar.add(Calendar.DAY_OF_YEAR, dataTotal);
                Date today = calendar.getTime();
                DateFormat dateFormat = new SimpleDateFormat("dd-MMM");
                todayAsString = dateFormat.format(today);
            }

        }
            return todayAsString;
    }





    public static String timeFormat(String time){
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat sdfNull = null;
        Date timeFormat = new Date();
        try{
            timeFormat = sdfTime.parse(time);
            //new format
            sdfNull = new SimpleDateFormat("hh:mm aa");
            //formatting the given time to new format with AM/PM
        }catch(ParseException e){
            e.printStackTrace();
        }

        return sdfNull.format(timeFormat);
    }
    public static String timeFormatCalender(String time){
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat sdfNull = null;
        Date timeFormat = new Date();
        try{
            timeFormat = sdfTime.parse(time);
            //new format
            sdfNull = new SimpleDateFormat("hh:mm");
            //formatting the given time to new format with AM/PM
        }catch(ParseException e){
            e.printStackTrace();
        }

        return sdfNull.format(timeFormat);
    }

    public static String currentDays(){
        String weekDays = null;
        String todWeekDays= null;
        Calendar calendar = Calendar.getInstance();
        Date weekDay = calendar.getTime();
        SimpleDateFormat weekFormat = new SimpleDateFormat("EEEE", Locale.ENGLISH);
        todWeekDays = weekFormat .format(weekDay.getTime());
        System.out.println(todWeekDays);
        weekDays = StringUtils.weekDays(todWeekDays);

        return weekDays;
    }


    public static String getFormattedDateTime(String dateTime) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_DD_MM_YYYY_HH_MM_SS, Locale.getDefault());
        String date = null;
        Date myDate = null;
        try {
            myDate = format.parse(dateTime);
            date = new SimpleDateFormat("d", Locale.getDefault()).format(myDate);
        } catch (ParseException e) {
           // LogUtils.LOGE(TAG, e.toString());
        }

        if (date == null || myDate == null)
            return "";

        if (date.endsWith("1") && !date.endsWith("11"))
            format = new SimpleDateFormat("d'st' MMMM", Locale.getDefault());
        else if (date.endsWith("2") && !date.endsWith("12"))
            format = new SimpleDateFormat("d'nd' MMMM", Locale.getDefault());
        else if (date.endsWith("3") && !date.endsWith("13"))
            format = new SimpleDateFormat("d'rd' MMMM", Locale.getDefault());
        else
            format = new SimpleDateFormat(" d'th' MMMM", Locale.getDefault());

        return format.format(myDate).replace("a.m.", "AM").replace("p.m.", "PM");
    }




    public static String timeFormats(String time){
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sdfNull = null;
        Date timeFormat = new Date();
        try{
            timeFormat = sdfTime.parse(time);
            //new format
            sdfNull = new SimpleDateFormat("hh.mm aa");
            //formatting the given time to new format with AM/PM
        }catch(ParseException e){
            e.printStackTrace();
        }

        return sdfNull.format(timeFormat);
    }

}
