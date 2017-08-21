/*
 * Utils.java
 * Process common solution

 * Author  : tupn
 * Created : 2/16/2016
 * Modified: $Date: 2016-06-27 14:43:13 +0700 (Mon, 27 Jun 2016) $

 * Copyright © 2015 www.mdi-astec.vn
 **************************************************************************************************/

package com.dinhcv.ticketmanagement.utils;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.dinhcv.ticketmanagement.model.structure.Vehicle.Vehicle;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import static android.content.Context.MODE_PRIVATE;


public class Utils {
    private static final String LOG_TAG = Utils.class.getSimpleName();
    private Utils() {

    }

    public static String convertToStringAroundNumber(int number) {

        int year = Calendar.getInstance().get(Calendar.YEAR) % 100;

        DecimalFormat df2 = new DecimalFormat("000000");
        String format = df2.format(number);

        String format1 =  String.valueOf(year) + format;
        Log.e(LOG_TAG, format1);
        return format1;
    }


    public static void setLicenseCode(Context context, int code){
        //save to preference
        SharedPreferences.Editor editor = context.getSharedPreferences(PreferenceKey.PREF_NAME, MODE_PRIVATE).edit();
        editor.putInt(PreferenceKey.PREF_LICENSE_CODE, code);
        editor.apply();
    }

    public static int getLicenseCode(){
        Random random = new Random();
        int max = 9999999;
        int min = 0;
        return random.nextInt(max - min + 1) + min;
    }

    public static String getBarcode(List<Vehicle> list){
        String code = convertToStringAroundNumber(getLicenseCode());
        for (Vehicle v : list){
            if (code.equalsIgnoreCase(v.getBarcode())){
                code = convertToStringAroundNumber(getLicenseCode());
            }
        }
        return code;
    }

    public static boolean isPantech()
    {
        //Sony
        String manufacturer = android.os.Build.MANUFACTURER;
        Debug.normal("Manufacture: "+manufacturer);
        if (manufacturer.contains("Pantech"))
            return true;
        else
            return false;
    }

    public static boolean isOppo()
    {
        //Sony
        String manufacturer = android.os.Build.MANUFACTURER;
        Debug.normal("Manufacture: "+manufacturer);
        if (manufacturer.contains("OPPO"))
            return true;
        else
            return false;
    }

    public static String getTotalTime(Date timeIn, Date timeOut) {
        boolean isOverDay = false;
        long subTime = (timeOut.getTime() - timeIn.getTime());
        int time = (int) (subTime / 1000); // giiay
        int day = 0;
        int hour = 0;
        int minute = 0;
        int DAY = 24 * 60 * 60;
        int HOUR = 3600;
        int MINUTE = 60;
        if (time > DAY) {
            isOverDay = true;
            day = time / DAY;
            int timeSub1 = time % DAY;
            if (timeSub1 > HOUR) {
                hour = timeSub1 / HOUR;
                int timeSub2 = time % MINUTE;
                if (minute > 60) {
                    minute = timeSub2 / MINUTE;
                } else minute = 1;
            } else {
                if (time > 60) {
                    minute = time / MINUTE;
                } else minute = 1;
            }
        } else {
            day = 0;
            if (time > HOUR) {
                hour = time / HOUR;
                int timeSub2 = time % MINUTE;
                if (minute > 60) {
                    minute = timeSub2 / MINUTE;
                } else minute = 1;
            } else {
                if (time > 60) {
                    minute = time / MINUTE;
                } else minute = 1;
            }
        }


        String totalTime = "";
        if (isOverDay) {
            totalTime = day + " Ngày " + hour + " Giờ " + minute + " Phút ";
        } else {
            totalTime = hour + " Giờ " + minute + " Phút ";
        }

        return totalTime;
    }

    public static String convertFeeToString(long number) {
        DecimalFormat df2 = new DecimalFormat("###,###,##0");
        String format = df2.format(number);
        Debug.normal("TEST FORMAT: " + format);

        return format;
    }


    /**
     * * Convert date to string with format date month year
     *
     * @param hour  int
     * @param minute  int
     * @return date string
     */
    public static String convertTimeToString(int hour, int minute) {
        String hourStr = String.format("%02d", hour);
        String minuteStr = String.format("%02d", minute);

        String time = hourStr +":"+minuteStr +":00";
        Debug.normal("Time: "+time);
        return time;
    }


    /**
     * * Convert date to string with format date month year
     *
     * @param input date
     * @return date string
     */
    public static String convertDateInToString(Date input) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
        return format.format(input);
    }

    /**
     * * Convert date to string with format date month year
     *
     * @param input date
     * @return date string
     */
    public static String convertOnlyDateToString(Date input) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        format.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
        return format.format(input);
    }


    /**
     * Conver date to string
     *
     * @param input: date
     * @return String date by local
     */
    public static String convertDateToString(Date input) {
        SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy_HHmmss");
        format.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
        return format.format(input);
    }

    /**
     * Conver string to date
     *
     * @param input: String
     * @return date
     */
    public static Date convertStringToDate(String input) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
        try {
            return format.parse(input);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date convertStringToDateWithOtherFormat(String input) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
        try {
            return format.parse(input);
        } catch (ParseException e) {
            return null;
        }
    }

    public static double getTotalPriceFollowedType1(int block, double price, String time1, String time2){
        double totalPrice = 0;
        final int MILLIS_TO_SECOND = 1000;
        final int SECOND_TO_MINUTE = 60;
        final int MINUTE_TO_HOUR = 60;
        final int MILLIS_TO_HOUR = MILLIS_TO_SECOND * SECOND_TO_MINUTE * MINUTE_TO_HOUR;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
        Date date1;
        Date date2;
        try {
            date1 = simpleDateFormat.parse(time1);
            date2 = simpleDateFormat.parse(time2);
            long time1InMillis = date1.getTime();
            System.out.println(time1InMillis);
            long time2InMillis = date2.getTime();
            System.out.println(time2InMillis);
            double gapOfTime = (double)(time2InMillis - time1InMillis)/(MILLIS_TO_HOUR);
            System.out.println(gapOfTime);

            if (gapOfTime <= block){
                totalPrice += price;
            } else {
                double remain = 0;
                if ((gapOfTime%block) > 0){
                    remain = price;
                }
                totalPrice += ((int)(gapOfTime/block) * price) + remain;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return totalPrice;

    }

    public static double getTotalPriceFollowedType2(int block1, int block2, double price1, double price2, double price3, String time1, String time2){
        double totalPrice = 0;
        final int MILLIS_TO_SECOND = 1000;
        final int SECOND_TO_MINUTE = 60;
        final int MINUTE_TO_HOUR = 60;
        final int MILLIS_TO_HOUR = MILLIS_TO_SECOND * SECOND_TO_MINUTE * MINUTE_TO_HOUR;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+07:00"));
        Date date1;
        Date date2;
        try {
            date1 = simpleDateFormat.parse(time1);
            date2 = simpleDateFormat.parse(time2);
            long time1InMillis = date1.getTime();
            long time2InMillis = date2.getTime();
            double gapOfTime = (double)(time2InMillis - time1InMillis)/MILLIS_TO_HOUR;
            Log.e(LOG_TAG, "Gap of time: " + gapOfTime);
            int day;
            if ((gapOfTime/24) <= 1){
                totalPrice += calculatePriceInOneDay(gapOfTime, block1, price1, block2, price2, price3);
            } else {
                day = (int) gapOfTime / 24 + 1;
                gapOfTime -= (day - 1)*24;
                totalPrice += calculatePriceInOneDay(gapOfTime, block1, price1, block2, price2, price3) + (day - 1)*price3;
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return totalPrice;

    }

    public static double calculatePriceInOneDay(double gapOfTime, int block1, double price1, int block2, double price2, double price3){
        double totalPrice = 0;
        if (gapOfTime <= block1){
            totalPrice += price1;
        } else {
            totalPrice += price1;
            double remainTime = gapOfTime - block1;
            if (remainTime <= block2){
                totalPrice += price2;
            } else {
                double remain = 0;
                if ((remainTime%block2) > 0){
                    remain = price2;
                }
                totalPrice += ((int) remainTime/block2) * price2 + remain;
            }
        }
        if (totalPrice > price3){
            totalPrice = price3;
        }
        return totalPrice;
    }

    /**
     * ISO8601 Date Format
     */
    private static final SimpleDateFormat s_iso8601_dateFormat = new SimpleDateFormat
            ("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);

    /**
     * Convert Date to string by ISO8601 format<br>
     * etc. <b>2014-12-26T02:59:37+00:00</b>
     *
     * @param date date time
     * @return date by ISO8601 string
     */
    static public String dateToStringByISO8601(Date date) {
        s_iso8601_dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return s_iso8601_dateFormat.format(date);
    }

    /**
     * Convert Date to string by Japan local<br>
     * etc. <b>2014-12-26T02:59:37+00:00</b>
     *
     * @param date date time
     * @return date by ISO8601 string
     */
    static public String dateToStringByJapan(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        format.setTimeZone(TimeZone.getTimeZone("GMT"));
        return format.format(date);
    }


}
/******************************************************************************
 * End of file
 *****************************************************************************/