package kore.botssdk.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;

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

@SuppressLint("UnknownNullness")
public class DateUtils {
    public static final long oneHour = 60 * 60 * 1000;
    public static final long oneDay = 24 * oneHour;
    public static final long oneMonth = oneDay * 30;
    public static final SimpleDateFormat isoFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
    public static final SimpleDateFormat fileFormatter = new SimpleDateFormat("yy_MM_dd_HH_mm_ss", Locale.ENGLISH);
    public static final Format dateFormat4 = new SimpleDateFormat("d MMM yyyy 'at' h:mm a", Locale.ENGLISH);
    public static final Format dateTime = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
    public static final SimpleDateFormat dateWeekDay = new SimpleDateFormat("EE, MMM dd, yyyy", Locale.ENGLISH);
    public static final SimpleDateFormat date24Time = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

    private static final Format dateMonthDay = new SimpleDateFormat("MMM dd", Locale.ENGLISH);

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
        return BotResponse.TIME_FORMAT == 12 ? dateTime.format(new Date(dateInMs)) : date24Time.format(new Date(dateInMs));
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

        Configuration configuration = new Configuration(context.getResources().getConfiguration());

        configuration.setLocale(locale);

        return context.createConfigurationContext(configuration);
    }

    public static boolean isYesterday(long millis) {
        return android.text.format.DateUtils.isToday(millis + android.text.format.DateUtils.DAY_IN_MILLIS);
    }

    public static boolean isTomorrow(long millis) {
        return android.text.format.DateUtils.isToday(millis - android.text.format.DateUtils.DAY_IN_MILLIS);
    }

    public static long getDateFromFormat(String date, String format, int addDays) {
        if (date == null || date.isEmpty())
            return 0;
        try {
            format = format.replace("DD", "dd").replaceAll("YY", "yy");

            if (format.contains("/") && date.contains("-"))
                date = date.replaceAll("-", "/");

            SimpleDateFormat df = new SimpleDateFormat(format, Locale.US);
            Timestamp ts = new Timestamp(Objects.requireNonNull(df.parse(date)).getTime());
            return ts.getTime() + ((long) addDays * 24 * 60 * 60 * 1000);
        } catch (ParseException e) {
            Calendar calendar = Calendar.getInstance();
            return calendar.getTimeInMillis() - ((long) addDays * 24 * 60 * 60 * 1000);
        }
    }

    public static String getCorrectedTimeZone(String timeZone) {
        if (kore.botssdk.utils.StringUtils.isNullOrEmptyWithTrim(timeZone)) return "";
        timeZone = timeZone.toLowerCase();
        if (timeZone.contains("calcutta")) {
            timeZone = timeZone.replace("calcutta", "kolkata");
        }
        return timeZone;
    }

    public static String getDateEEMMMDDYYYYHhMmSs(Context context, long timeMillis) {

        try {
            Locale deviceLocale;
            Date date = new Date(timeMillis);

            if (SDKConfiguration.getDeviceLocale() != null) {
                deviceLocale = SDKConfiguration.getDeviceLocale();
            } else {
                deviceLocale = context.getResources()
                        .getConfiguration()
                        .getLocales()
                        .get(0);
            }

            SimpleDateFormat outputFormat =
                    new SimpleDateFormat("EEE, MMM dd yyyy hh:mm:ss a", deviceLocale);

            return outputFormat.format(date);

        } catch (Exception e) {
            LogUtils.e("Date format exception", e + "");
            return "";
        }
    }

}