package kore.botssdk.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;

import java.sql.Timestamp;
import java.text.DateFormatSymbols;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import kore.botssdk.models.BotResponse;
import kore.botssdk.net.SDKConfiguration;
import kore.korebotsdklib.R;

/**
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
@SuppressLint("UnKnownNullness")
public class DateUtils {
    public static final SimpleDateFormat isoFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
    public static final SimpleDateFormat fileFormatter = new SimpleDateFormat("yy_MM_dd_HH_mm_ss", Locale.ENGLISH);
    public static final Format dateFormat4 = new SimpleDateFormat("d MMM yyyy 'at' h:mm a", Locale.ENGLISH);
    public static final Format yearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
    public static final SimpleDateFormat dateWeekMsg = new SimpleDateFormat("EE, MMM dd", Locale.ENGLISH);
    public static final SimpleDateFormat dateWeekDay = new SimpleDateFormat("EE, MMM dd, yyyy", Locale.ENGLISH);
    public static final SimpleDateFormat dateTime1 = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
    public static final Format calendar_list_format = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.ENGLISH);
    public static final SimpleDateFormat dateWeekDayTime = new SimpleDateFormat("EE, MMM dd yyyy 'at' hh:mm:ss a", Locale.ENGLISH);
    public static final SimpleDateFormat dateWeekDayTime4 = new SimpleDateFormat("dd MMM, yyyy, hh:mm a", Locale.ENGLISH);
    public static final SimpleDateFormat date24Time = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
    private static final Format dateMonthDay = new SimpleDateFormat("MMM dd", Locale.ENGLISH);
    public static final SimpleDateFormat dateWeekMsgTime = new SimpleDateFormat("EE, MMM dd, hh:mm a", Locale.ENGLISH);
    private static final Format dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    private static final Format dynamicDate = new SimpleDateFormat(BotResponse.DATE_FORMAT, Locale.ENGLISH);

    public static String getTimeStamp(String timeStamp, boolean timezoneModifiedRequired) throws ParseException {
        if (timeStamp == null || timeStamp.isEmpty()) return "";
        long timeStampMillis = isoFormatter.parse(timeStamp).getTime() + ((timezoneModifiedRequired) ? TimeZone.getDefault().getRawOffset() + TimeZone.getDefault().getDSTSavings() : 0);
        return getTimeStamp(timeStampMillis);
    }

    public static String getCurrentDateTime() {
        return fileFormatter.format(new Date(System.currentTimeMillis()));
    }

    public static String getTimeStamp(long timeStampMillis) {
        return dateFormat4.format(new Date(timeStampMillis));
    }

    public static String getTimeInAmPm(long dateInMs) {
        return date24Time.format(new Date(dateInMs));
    }


    public static String formattedSentDateV6(long lastModified) {
        // CREATE DateFormatSymbols WITH ALL SYMBOLS FROM (DEFAULT) Locale
        DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());

        // OVERRIDE SOME symbols WHILE RETAINING OTHERS
        symbols.setAmPmStrings(new String[]{"am", "pm"});
        dateWeekDay.setDateFormatSymbols(symbols);

        String time = "";
        if (android.text.format.DateUtils.isToday(lastModified)) {
            time = "Today";
        } else if (isYesterday(lastModified)) {
            time = "Yesterday";
        } else if (isTomorrow(lastModified)) {
            time = "Tomorrow, " + dateMonthDay.format(new Date(lastModified));
        } else {
            time = dynamicDate.format(new Date(lastModified));
        }


        return time;
    }

    public static String formattedSentDateV6(Context context, long lastModified) {

        Context localizedContext = getLocalizedContext(context, SDKConfiguration.getDeviceLocale());

        // CREATE DateFormatSymbols WITH ALL SYMBOLS FROM (DEFAULT) Locale
        DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());

        // OVERRIDE SOME symbols WHILE RETAINING OTHERS
        symbols.setAmPmStrings(new String[]{"am", "pm"});
        dateWeekDay.setDateFormatSymbols(symbols);

        String time;
        if (android.text.format.DateUtils.isToday(lastModified)) {
            time = localizedContext.getString(R.string.today);
        } else if (isYesterday(lastModified)) {
            time = localizedContext.getString(R.string.yesterday);
        } else if (isTomorrow(lastModified)) {
            time = localizedContext.getString(R.string.tomorrow) + ", " + dateMonthDay.format(new Date(lastModified));
        } else {
            time = dynamicDate.format(new Date(lastModified));
        }

        return time;
    }

    private static Context getLocalizedContext(Context context, Locale locale) {

        Configuration configuration = new Configuration(
                context.getResources().getConfiguration()
        );

        configuration.setLocale(locale);

        return context.createConfigurationContext(configuration);
    }

    public static String getDate(long lastModified) {
        return dateWeekMsg.format(new Date(lastModified));
    }


    public static String getDateEEMMMDDYYYYHhMmSs(long millis) {
        return dateWeekDayTime.format(millis);
    }

    public static String getDateEEMMMDDYYYYHhMmSs(Context context, long timeMillis) {

        try {
            Locale deviceLocale;
            Date date = new Date(timeMillis);

            if(SDKConfiguration.getDeviceLocale() != null)
            {
                deviceLocale = SDKConfiguration.getDeviceLocale();
            }
            else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    deviceLocale = context.getResources()
                            .getConfiguration()
                            .getLocales()
                            .get(0);
                } else {
                    deviceLocale = context.getResources()
                            .getConfiguration()
                            .locale;
                }
            }

            if (BotResponse.BUBBLE_DATE_FORMAT == null || BotResponse.BUBBLE_DATE_FORMAT.isEmpty()) {
                BotResponse.BUBBLE_DATE_FORMAT = "EEE, MMM dd yyyy hh:mm:ss a";
            }

            SimpleDateFormat outputFormat =
                    new SimpleDateFormat(BotResponse.BUBBLE_DATE_FORMAT, deviceLocale);

            String result = outputFormat.format(date);
            if (result.contains("{S}")) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                result = result.replace("{S}", getDayOfMonthSuffix(day));
            }
            return result;

        } catch (Exception e) {
            LogUtils.e("Date format exception", e+"");
            try {
                Locale deviceLocale;
                Date date = new Date(timeMillis);

                if(SDKConfiguration.getDeviceLocale() != null)
                {
                    deviceLocale = SDKConfiguration.getDeviceLocale();
                }
                else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        deviceLocale = context.getResources()
                                .getConfiguration()
                                .getLocales()
                                .get(0);
                    } else {
                        deviceLocale = context.getResources()
                                .getConfiguration()
                                .locale;
                    }
                }

                SimpleDateFormat outputFormat =
                        new SimpleDateFormat("EEE, MMM dd yyyy hh:mm:ss a", deviceLocale);

                return outputFormat.format(date);

            } catch (Exception ex) {
                LogUtils.e("Date format exception", ex+"");
                return "";
            }
        }
    }

    public static String getDateWithTime(long lastModified) {

        String date = dateWeekMsgTime.format(lastModified);

        if (android.text.format.DateUtils.isToday(lastModified)) {
            date = "Today " + getTimeInAmPm(lastModified);
        } else if (isYesterday(lastModified)) {
            date = "Yesterday " + getTimeInAmPm(lastModified);
        } else if (isTomorrow(lastModified)) {
            date = "Tomorrow " + getTimeInAmPm(lastModified);
        }
        return date;
    }

    public static String getFilesDateSturcture(String lastModified) {
        try {

            long date = new Date(getDateFromString(lastModified)).getTime();
            if (android.text.format.DateUtils.isToday(date)) {
                return "Last Edited Today";
            } else if (isYesterday(date)) {
                return "Last Edited Yesterday";
            }


            return "Last Edited " + getDateFromString(lastModified);
        } catch (Exception e) {
            LogUtils.e("Date format exception", e+"");
            return lastModified;
        }
    }

    public static boolean isYesterday(long millis) {
        return android.text.format.DateUtils.isToday(millis + android.text.format.DateUtils.DAY_IN_MILLIS);
    }

    public static boolean isTomorrow(long millis) {
        return android.text.format.DateUtils.isToday(millis - android.text.format.DateUtils.DAY_IN_MILLIS);
    }

    public static String getDateFromString(String time) {
        if (time == null || time.isEmpty()) return "";
        try {
            Date date = DateUtils.isoFormatter.parse(time);
            return calendar_list_format.format(date);
        } catch (ParseException e) {
            LogUtils.e("Date format exception", e+"");
        }
        return time;
    }

    public static String getDateFromStringByDate(String time) {
        if (time == null || time.isEmpty()) return "";
        try {
            long lastModified = new Date(time).getTime();
            if (android.text.format.DateUtils.isToday(lastModified)) {
                return "Today";
            } else if (isYesterday(lastModified)) {
                return "Yesterday";
            }
            return dateFormat.format(lastModified);
        } catch (Exception e) {
            LogUtils.e("Date format exception", e+"");
        }
        return "";
    }

    public static long getDateFromFormat(String date, String format, int addDays) {
        if (date == null || date.isEmpty())
            return 0;
        try
        {
            if(format.contains("YYYY")) {
                format = format.replace("YYYY", "yyyy");
            }

            if(format.contains("/") && date.contains("-"))
                date = date.replaceAll("-", "/");

            SimpleDateFormat df = new SimpleDateFormat(format.replace("DD", "dd"), Locale.US);
            Timestamp ts = new Timestamp(Objects.requireNonNull(df.parse(date)).getTime());
            return ts.getTime() + ((long) addDays * 24 * 60 * 60 * 1000);
        } catch (ParseException e) {
            Calendar calendar = Calendar.getInstance();
            return calendar.getTimeInMillis() - ((long) addDays * 24 * 60 * 60 * 1000);
        }
    }

    public static String getCorrectedTimeZone(String timeZone) {
        if (StringUtils.isNullOrEmptyWithTrim(timeZone)) return "";
        timeZone = timeZone.toLowerCase();
        if (timeZone.contains("calcutta")) {
            timeZone = timeZone.replace("calcutta", "kolkata");
        }
        return timeZone;
    }


    public static boolean formattedSentDateV8_InAnnouncement2(long createdOn) {
        boolean isDateLabelShouldVisble = true;
        if (android.text.format.DateUtils.isToday(createdOn)) {
            isDateLabelShouldVisble = false;
        } else if (isYesterday(createdOn)) {
            isDateLabelShouldVisble = false;
        }

        return isDateLabelShouldVisble;

    }

    public static String formattedSentDateV8_InAnnouncement(long createdOn) {
        String time = "";
        if (android.text.format.DateUtils.isToday(createdOn)) {
            time = "Today at " + dateTime1.format(new Date(createdOn));
        } else if (isYesterday(createdOn)) {
            time = "Yesterday, " + dateTime1.format(new Date(createdOn));
        } else {
            time = dateWeekDayTime4.format(new Date(createdOn));
        }

        return time;
    }

    private static String getDayOfMonthSuffix(final int n) {
        if (n >= 11 && n <= 13) {
            return "th";
        }
        return switch (n % 10) {
            case 1 -> "st";
            case 2 -> "nd";
            case 3 -> "rd";
            default -> "th";
        };
    }
}