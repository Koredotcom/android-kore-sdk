package kore.botssdk.utils;

import java.text.DateFormatSymbols;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    public static final SimpleDateFormat dateWeekMsg = new SimpleDateFormat("EE, MMM dd", Locale.ENGLISH);
    public static final SimpleDateFormat dateWeekDay = new SimpleDateFormat("EE, MMM dd, yyyy", Locale.ENGLISH);


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
        } else if (diff <= TWO_DAY_DIFF) {
            time = "Tomorrow" ;
        } else {

            time = currentYear == messageYear ? dateWeekMsg.format(new Date(lastModified)) : dateWeekDay.format(new Date(lastModified));
        }

        return time;
    }

    public static String getDate(long lastModified){
       return dateWeekMsg.format(new Date(lastModified));
    }

}