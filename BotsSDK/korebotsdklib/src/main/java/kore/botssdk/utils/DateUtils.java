package kore.botssdk.utils;

import java.text.DateFormatSymbols;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Pradeep Mahato on 09-Jun-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class DateUtils {

    public static final SimpleDateFormat isoFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
    public static final Format dateFormat4 = new SimpleDateFormat("d MMM yyyy 'at' h:mm a", Locale.ENGLISH);
    public static final Format dateTime = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
    public static final Format dateDay = new SimpleDateFormat("dd", Locale.ENGLISH);
    public static final Format yearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
    public static final Format dateMonth = new SimpleDateFormat("MM", Locale.ENGLISH);    //03
    public static final Format monthFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);
    public static final Format dateWeekDayV2_1 = new SimpleDateFormat("EEEE", Locale.ENGLISH);
    public static final Format dateFormatDay = new SimpleDateFormat("EEE, d MMM yyyy", Locale.ENGLISH);
    public static final SimpleDateFormat dateWeekMsg = new SimpleDateFormat("EE, MMM dd", Locale.ENGLISH);
    public static final SimpleDateFormat dateWeekDay = new SimpleDateFormat("EE, MMM dd, yyyy", Locale.ENGLISH);

    public static final Format calendar_list_format = new SimpleDateFormat("EEE, d MMM, h:mm a", Locale.ENGLISH);
    public static final Format calendar_list_format_2 = new SimpleDateFormat("h:mm a", Locale.ENGLISH);

    private static final Format dateMonthDay = new SimpleDateFormat("MMM dd", Locale.ENGLISH);
    private static final Format dateFormat5 = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
    public static final SimpleDateFormat dateWeekMsgTime = new SimpleDateFormat("EE, MMM dd, h:mm a", Locale.ENGLISH);

    private static final Format dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

    public static String getTimeStamp(String timeStamp, boolean timezoneModifiedRequired) throws ParseException {
        if(timeStamp == null || timeStamp.isEmpty())return "";
        long timeStampMillis = isoFormatter.parse(timeStamp).getTime() + ((timezoneModifiedRequired) ? TimeZone.getDefault().getRawOffset() : 0);
        return getTimeStamp(timeStampMillis);
    }


    public static String getTimeStamp(long timeStampMillis) {
        return dateFormat4.format(new Date(timeStampMillis));
    }

    public static String getTimeInAmPm(long dateInMs) {
        return dateTime.format(new Date(dateInMs));
    }
    /**
     * Just now
     * Today, JUN 08
     */
    public static String getSlotsDate(long lastModified) {
        // CREATE DateFormatSymbols WITH ALL SYMBOLS FROM (DEFAULT) Locale
        DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());

        // OVERRIDE SOME symbols WHILE RETAINING OTHERS
         symbols.setAmPmStrings(new String[]{"am", "pm"});
        int messageDay = Integer.parseInt(dateDay.format(new Date(lastModified)));
        int currentDay = Integer.parseInt(dateDay.format(new Date()));

        int messageYear = Integer.parseInt(yearFormat.format(new Date(lastModified)));
        int currentYear = Integer.parseInt(yearFormat.format(new Date()));

        int messageMonth = Integer.parseInt(dateMonth.format(new Date(lastModified)));
        int currentMonth = Integer.parseInt(dateMonth.format(new Date()));

        long now = System.currentTimeMillis();
        long diff = now - lastModified;
        long diffRev = lastModified -now;
        long TWO_DAY_DIFF = 172800 * 1000;

        String time = "";

        if (currentDay == messageDay && currentYear == messageYear && currentMonth == messageMonth) {
            time = "Today";
        } else if (currentYear == messageYear && currentMonth == messageMonth) {
            if (currentDay - 1 == messageDay) {
                time = "Yesterday";
            } else if (currentDay + 1 == messageDay) {
                time = "Tomorrow" ;
            } else {
                time = dateWeekMsg.format(new Date(lastModified));
            }
        } else if (diff >= 0 && diff <= TWO_DAY_DIFF) {
            time = "Yesterday";
        } else if (diffRev >=0 && diffRev <= TWO_DAY_DIFF) {
            time = "Tomorrow" ;
        } else {

            time = currentYear == messageYear ? dateWeekMsg.format(new Date(lastModified)) : dateWeekDay.format(new Date(lastModified));
        }

        return time;
    }


    /**
     * Just now
     * Today, JUN 08
     */
    public static String formattedSentDateV6(long lastModified) {
        // CREATE DateFormatSymbols WITH ALL SYMBOLS FROM (DEFAULT) Locale
        DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());

        // OVERRIDE SOME symbols WHILE RETAINING OTHERS
        symbols.setAmPmStrings(new String[]{"am", "pm"});
        dateWeekDay.setDateFormatSymbols(symbols);

        int messageDay = Integer.parseInt(dateDay.format(new Date(lastModified)));
        int currentDay = Integer.parseInt(dateDay.format(new Date()));

        int messageYear = Integer.parseInt(yearFormat.format(new Date(lastModified)));
        int currentYear = Integer.parseInt(yearFormat.format(new Date()));

        int messageMonth = Integer.parseInt(dateMonth.format(new Date(lastModified)));
        int currentMonth = Integer.parseInt(dateMonth.format(new Date()));

        long now = System.currentTimeMillis();
        long diff = now - lastModified;
        long TWO_DAY_DIFF = 172800 * 1000;
        long diffRev = lastModified -now;
        String time = "";

        if (currentDay == messageDay && currentYear == messageYear && currentMonth == messageMonth) {
            time = "Today, " + dateMonthDay.format(new Date(lastModified));
        } else if (currentYear == messageYear && currentMonth == messageMonth) {
            if (currentDay - 1 == messageDay) {
                time = "Yesterday, " + dateMonthDay.format(new Date(lastModified));
            } else if (currentDay + 1 == messageDay) {
                time = "Tomorrow, " + dateMonthDay.format(new Date(lastModified));
            } else {
                time = dateWeekMsg.format(new Date(lastModified));
            }
        } else if (diff >= 0 && diff <= TWO_DAY_DIFF) {
            time = "Yesterday , " + dateMonthDay.format(new Date(lastModified));
        } else if (diffRev >=0 && diffRev <= TWO_DAY_DIFF ) {
            time = "Tomorrow, " + dateMonthDay.format(new Date(lastModified));
        } else {

            time = currentYear == messageYear ? dateWeekMsg.format(new Date(lastModified)) : dateWeekDay.format(new Date(lastModified));
        }

        return time;
    }
    public static String getDate(long lastModified){
       return dateWeekMsg.format(new Date(lastModified));
    }

    public static String getDateWithTime(long lastModified){
       return dateWeekMsgTime.format(lastModified);
    }

    public static String getFormattedSentDateCoreFunctionality(long sentDate) {
        Date date = new Date();
        date.setTime(sentDate);
        int messageD = Integer.parseInt(dateDay.format(date));

        date.setTime(System.currentTimeMillis());
        int currentD = Integer.parseInt(dateDay.format(date));

        long currentTime = System.currentTimeMillis();
        long diff = currentTime - sentDate;

        String time = "";

        long thirtyMin = 30 * 60 * 1000;
        long oneMin = 60 * 1000;
        long week = 1000 * 60 * 60 * 24 * 7;

        int currentWeek = (int) (currentTime / week);
        int messageWeek = (int) (sentDate / week);


        long serverSyncOffset = 1000 * 60 * 3; //3 minutes
        if (diff > (-serverSyncOffset)) {
            if (diff < oneMin) {
                // Just Now			Within the last 30 minutes
                time = "Now";
            } else if (diff >= oneMin) {
                // hh:mm AM/PM		> 30 Minutes and < End of Current Day
                if (currentD == messageD) {
                    //Yesterday				Previous Day
                    date.setTime(sentDate);
                    time = dateTime.format(date);
                } else if (currentD - messageD == 1) {
                    time = "Yesterday";
                } else {
                    int weekDiff = currentWeek - messageWeek;
                    if (diff <= week) {
                        //Weekday					Show the relevant day of week for messages up to a week old
                        date.setTime(sentDate);
                        time = dateWeekDay.format(date);
                    } else {
                        date.setTime(sentDate);
                        if (isCurrentYear(sentDate)) {
                            time = dateMonthDay.format(date); // current year.. display in MMM DD format
                        } else {
                            // TODO: the format should be according to the Locale being set in the Device.
                            time = dateFormat5.format(date); // older than current year.. display in mm/dd/yy format
                        }
                    }
                }
            } else { //if diff<0
                date.setTime(sentDate);
                time = dateFormat.format(date);
            }
        } else {
            //else for serverSync.. show normal time
            date.setTime(sentDate);
            time = dateFormat.format(date);
        }

        return time;
    }

    private static boolean isCurrentYear(long lastModified) {
        Calendar calendar = Calendar.getInstance();
        int currentYr = calendar.get(Calendar.YEAR);

        calendar.setTimeInMillis(lastModified);
        int lastModifiedYr = calendar.get(Calendar.YEAR);

        return lastModifiedYr == currentYr;
    }
    public static  String getDateinDayFormat(long dateInMs){
        return dateFormatDay.format(new Date(dateInMs));
    }
}