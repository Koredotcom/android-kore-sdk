package kore.botssdk.utils;

import android.annotation.SuppressLint;
import android.content.Context;

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
import java.util.concurrent.TimeUnit;

import kore.botssdk.models.BotResponse;

/**
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
@SuppressLint("UnKnownNullness")
public class DateUtils {
    public static final long oneMin = 60 * 1000;
    public static final long fiveMin = 5 * oneMin;
    public static final long oneHour = 60 * 60 * 1000;
    public static final long oneDay = 24 * oneHour;
    public static final long oneWeek = oneDay * 7;
    public static final long oneMonth = oneDay * 30;
    public static final long oneYear = oneMonth * 12;
    public static final SimpleDateFormat isoFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
    public static final SimpleDateFormat fileFormatter = new SimpleDateFormat("yy_MM_dd_HH_mm_ss", Locale.ENGLISH);
    public static final Format dateFormat4 = new SimpleDateFormat("d MMM yyyy 'at' h:mm a", Locale.ENGLISH);
    public static final Format dateTime = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
    public static final Format dateDay = new SimpleDateFormat("dd", Locale.ENGLISH);
    public static final Format yearFormat = new SimpleDateFormat("yyyy", Locale.ENGLISH);
    public static final Format dateMonth = new SimpleDateFormat("MM", Locale.ENGLISH);    //03
    public static final Format monthFormat = new SimpleDateFormat("MMMM", Locale.ENGLISH);
    public static final Format dateWeekDayV2_1 = new SimpleDateFormat("EEEE", Locale.ENGLISH);
    public static final Format dateFormatDay = new SimpleDateFormat("EEE, d MMM yyyy", Locale.ENGLISH);
    public static final Format dateFormatDay_meeting = new SimpleDateFormat("EEE, d MMM", Locale.ENGLISH);
    public static final SimpleDateFormat dateWeekMsg = new SimpleDateFormat("EE, MMM dd", Locale.ENGLISH);
    public static final SimpleDateFormat dateWeekDay = new SimpleDateFormat("EE, MMM dd, yyyy", Locale.ENGLISH);
    public static final SimpleDateFormat dateTime1 = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
    public static final Format calendar_list_format = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.ENGLISH);
    public static final Format calendar_list_format_2 = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
    public static final SimpleDateFormat dateWeekDayTime = new SimpleDateFormat("EE, MMM dd yyyy 'at' hh:mm a", Locale.ENGLISH);

    public static final SimpleDateFormat dateWeekDayTime2 = new SimpleDateFormat("MMM dd yyyy 'at' hh:mm a", Locale.ENGLISH);
    public static final SimpleDateFormat dateWeekDayTime3 = new SimpleDateFormat("MMM dd 'at' hh:mm a", Locale.ENGLISH);
    public static final SimpleDateFormat dateWeekDayTime4 = new SimpleDateFormat("dd MMM, yyyy, hh:mm a", Locale.ENGLISH);
    public static final SimpleDateFormat dateWeekDayTime5 = new SimpleDateFormat("EEE, MMM dd, yyyy, ", Locale.ENGLISH);
    public static final Format calendar_list_format2 = new SimpleDateFormat("EEE, MMM d, ", Locale.ENGLISH);
    public static final Format calendar_list_req_format2 = new SimpleDateFormat("EEE, MMM d ", Locale.ENGLISH);
    public static final SimpleDateFormat chat_bubble_dateTime = new SimpleDateFormat("EE MMM dd yyyy 'at' hh:mm:ss a", Locale.ENGLISH);
    public static final SimpleDateFormat date24Time = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
    public static final Format calendar_event_list_format1 = new SimpleDateFormat("EEE, d MMM", Locale.ENGLISH);

    private static final Format dateMonthDay = new SimpleDateFormat("MMM dd", Locale.ENGLISH);
    private static final Format dateFormat5 = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
    private static final Format dateFormat6 = new SimpleDateFormat("dd/MM", Locale.ENGLISH);
    public static final SimpleDateFormat dateWeekMsgTime = new SimpleDateFormat("EE, MMM dd, hh:mm a", Locale.ENGLISH);
    public static final SimpleDateFormat dateWeekMsgTime2 = new SimpleDateFormat("MMM dd, hh:mm a", Locale.ENGLISH);

    private static final Format dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

    public static final Format calendar_allday_format = new SimpleDateFormat("EE, MMM dd", Locale.ENGLISH);

    public static final Format calendar_allday_time_format = new SimpleDateFormat("EE, MMM dd, hh:mm a", Locale.ENGLISH);

    public static final Format dnd_time_format = new SimpleDateFormat("hh:mm a, MMM dd", Locale.ENGLISH);

    private static final Format dynamicDate = new SimpleDateFormat(BotResponse.DATE_FORMAT, Locale.ENGLISH);

    public static String getTimeStamp(String timeStamp, boolean timezoneModifiedRequired) throws ParseException {
        if (timeStamp == null || timeStamp.isEmpty()) return "";
        long timeStampMillis = isoFormatter.parse(timeStamp).getTime() + ((timezoneModifiedRequired) ? TimeZone.getDefault().getRawOffset() + TimeZone.getDefault().getDSTSavings() : 0);
        return getTimeStamp(timeStampMillis);
    }

    public static String getCurrentDateTime() {
        return fileFormatter.format(new Date(System.currentTimeMillis()));
    }

    public static long getTimeStampLong(String timeStamp, boolean timezoneModifiedRequired) throws ParseException {

        long timeStampMillis = isoFormatter.parse(timeStamp).getTime() + ((timezoneModifiedRequired) ? TimeZone.getDefault().getRawOffset() + TimeZone.getDefault().getDSTSavings() : 0);

        return timeStampMillis;
    }

    public static double getOneDayMiliseconds(long diffvalue) {
        double seconds = diffvalue / 1000;
        double hours = seconds / (60 * 60);
        return hours;

    }

    public static String getTimeStamp(long timeStampMillis) {
        return dateFormat4.format(new Date(timeStampMillis));
    }

    public static String getTimeInAmPm(long dateInMs) {
        return date24Time.format(new Date(dateInMs));
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
        int messageYear = Integer.parseInt(yearFormat.format(new Date(lastModified)));
        int currentYear = Integer.parseInt(yearFormat.format(new Date()));
        String time = "";

        if (android.text.format.DateUtils.isToday(lastModified)) {
            time = "Today";
        } else if (isYesterday(lastModified)) {
            time = "Yesterday";
        } else if (isTomorrow(lastModified)) {
            time = "Tomorrow";
        } else {

            time = currentYear == messageYear ? dateWeekMsg.format(new Date(lastModified)) : dateWeekDay.format(new Date(lastModified));
        }
        return time;
    }


    public static int getDays(Context mContext, long diff) {
        return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }


    /**
     * Just now
     * Today, JUN 08
     */


    @SuppressLint("DefaultLocale")
    public static String calenderDateFormation(Context mContext, long startDate) {
        long currentTime = System.currentTimeMillis();
        long diff = startDate - currentTime;

        if (android.text.format.DateUtils.isToday(startDate)) {
            if (diff < oneMin) {
                return "NOW";

            } else {
                int diffhours = (int) (diff / (60 * 60 * 1000));
                int diffmin = (int) (diff / (60 * 1000));
                if (diffhours <= 0 && diffmin <= 0) {
                    return "Now";
                } else if (diffhours <= 0) {
                    return "In " + diffmin + (diffmin > 1 ? " mins " : " min");
                } else {
                    diff -= diffhours * (60 * 60 * 1000);
                    int diffmins = (int) (diff / (60 * 1000));
                    return "In " + diffhours + (diffhours > 1 ? " hrs " : " hr ") + diffmins + (diffmins > 1 ? " mins" : " min");
                }
            }
        } else if (isTomorrow(startDate)) {
            return "Tomorrow";
        } else if (diff >= oneDay && diff < oneYear) {
            long val = (Objects.requireNonNull(DateUtils.getDDMMYYYY(startDate)).getTime() - Objects.requireNonNull(DateUtils.getDDMMYYYY(currentTime)).getTime());
            return String.format("%s%d%s", "In ", TimeUnit.MILLISECONDS.toDays(val), " days");
        } else {
            return String.format("%s%d%s", "In ", (diff / oneYear), " years");
        }
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

    public static String getDate(long lastModified) {
        return dateWeekMsg.format(new Date(lastModified));
    }

    public static String getDateEEEMMD(double startdate, double enddate) {
        return calendar_list_format2.format(startdate) + calendar_list_format_2.format(startdate).toLowerCase() + " to " + calendar_list_format_2.format(enddate).toLowerCase();
    }

    public static String getDayDate(double startdate) {
        return calendar_allday_format.format(startdate);
    }

    public static String getMorethanDayDate(double startdate, double enddate) {
        return calendar_allday_format.format(startdate) + " - " + calendar_allday_format.format(enddate);
    }


    public static String getDNDDayDateTime(double startdate) {
        return dnd_time_format.format(startdate);
    }

    public static String getMorethanDayDateTime(double startdate, double enddate) {
        return calendar_allday_time_format.format(startdate) + " - " + calendar_allday_time_format.format(enddate);
    }

    public static String getReqDateEEEMMDD(double startdate) {
        return calendar_list_req_format2.format(startdate);
    }

    public static String getDateMMMDDYYYY(double startdate, double enddate) {
        return dateWeekDayTime5.format(startdate) + calendar_list_format_2.format(startdate).toLowerCase() + " to " + calendar_list_format_2.format(enddate).toLowerCase();
    }

    public static String getDateEEMMMDDYYYYHhMmSs(long millis) {
        return dateWeekDayTime.format(millis);
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

    public static String getDay(long mdate) {
        String date = DateUtils.calendar_event_list_format1.format(mdate);

        if (isTodayOrBefore(mdate)) {
            //date = "Today";
            date = "Later today";
        } /*else if (isYesterday(mdate)) {
            date = "Yesterday";
        }*/ else if (isTomorrow(mdate)) {
            date = "Tomorrow";
        }
        return date;
    }

    public static String getCalDay(long mdate) {
        String date = DateUtils.calendar_event_list_format1.format(mdate);

        if (android.text.format.DateUtils.isToday(mdate)) {
            date = "Today";
        } else if (isYesterday(mdate)) {
            date = "Yesterday";
        } else if (isTomorrow(mdate)) {
            date = "Tomorrow";
        }
        return date;
    }


    /**
     * @return true if the supplied when is today else false
     */
    public static boolean isTodayOrBefore(long when) {
        if (android.text.format.DateUtils.isToday(when)) {
            return true;
        } else {
            Date current = new Date();
            Date other = new Date(when);
            return other.before(current);
        }
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return "";
    }


    public static Date getDDMMYYYY(long date) {
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

        try {
            return DATE_FORMAT.parse(dateFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
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

    public static String getAnnoucementDateDDMMM(long dateformat) {

        return dateFormat6.format(dateformat);
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

    public static String getDateinDayFormat(long dateInMs) {
        return dateFormatDay.format(new Date(dateInMs));
    }

    public static String getDateinMeetingFormat(long dateInMs) {
        return dateFormatDay_meeting.format(new Date(dateInMs));
    }


    /**
     * Just now
     * Today, JUN 08
     */

    public static String formattedSentDateV8_InKnwoledge(long lastModified) {
        // CREATE DateFormatSymbols WITH ALL SYMBOLS FROM (DEFAULT) Locale

        String time = "";


        if (android.text.format.DateUtils.isToday(lastModified)) {
            time = "Today at " + dateTime1.format(new Date(lastModified));
        } else if (isYesterday(lastModified)) {
            time = "Yesterday, " + dateTime1.format(new Date(lastModified));
        } else if (isTomorrow(lastModified)) {
            time = "Tomorrow, " + dateTime1.format(new Date(lastModified));
        } else {
            time = dateWeekDayTime2.format(new Date(lastModified));
        }

        return time;
    }

    public static String formattedDateInTask(long lastModified) {

        String date = dateWeekMsgTime2.format(lastModified);

        if (android.text.format.DateUtils.isToday(lastModified)) {
            date = "Today, " + date;
        } else if (isYesterday(lastModified)) {
            date = "Yesterday, " + date;
        } else if (isTomorrow(lastModified)) {
            date = "Tomorrow, " + date;
        }

        return date;
    }


    public static String formattedSentDateV8_InAnnoucemnt(long lastModified) {
        // CREATE DateFormatSymbols WITH ALL SYMBOLS FROM (DEFAULT) Locale

        String time = "";


        if (android.text.format.DateUtils.isToday(lastModified)) {
            time = dateTime1.format(new Date(lastModified));
        } else if (isYesterday(lastModified)) {
            time = "Yesterday, " + dateTime1.format(new Date(lastModified));
        } else {
            time = dateWeekDayTime3.format(new Date(lastModified));
        }

        return time;
    }

    public static String formattedSentDateV8(long lastModified, boolean isDetailView) {
        // CREATE DateFormatSymbols WITH ALL SYMBOLS FROM (DEFAULT) Locale
        DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());

        // OVERRIDE SOME symbols WHILE RETAINING OTHERS
        symbols.setAmPmStrings(new String[]{"am", "pm"});
        dateWeekDay.setDateFormatSymbols(symbols);
        int messageYear = Integer.parseInt(yearFormat.format(new Date(lastModified)));
        int currentYear = Integer.parseInt(yearFormat.format(new Date()));

        String time = "";

        if (isDetailView) {
            if (android.text.format.DateUtils.isToday(lastModified)) {
                time = "Today at " + dateTime1.format(new Date(lastModified));
            } else if (isYesterday(lastModified)) {
                time = "Yesterday, " + dateTime1.format(new Date(lastModified));
            } else if (isTomorrow(lastModified)) {
                time = "Tomorrow, " + dateTime1.format(new Date(lastModified));
            } else {
                time = dateWeekDayTime.format(new Date(lastModified));
            }
        } else {

            if (android.text.format.DateUtils.isToday(lastModified)) {
                time = "Today, " + dateMonthDay.format(new Date(lastModified));
            } else if (isYesterday(lastModified)) {
                time = "Yesterday, " + dateMonthDay.format(new Date(lastModified));
            } else if (isTomorrow(lastModified)) {
                time = "Tomorrow, " + dateMonthDay.format(new Date(lastModified));
            } else {
                time = currentYear == messageYear ? dateWeekMsg.format(new Date(lastModified)) : dateWeekDay.format(new Date(lastModified));
            }
        }
        return time;
    }

    public static String getCorrectedTimeZone(String timeZone) {
        if (kore.botssdk.utils.StringUtils.isNullOrEmptyWithTrim(timeZone)) return "";
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

    public static String getMonthName(int month)
    {
        switch (month)
        {
            case 0:
                return "January";
            case 1:
                return "February";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            case 11:
                return "December";
            default:
                return "";
        }
    }

    public static String getDayOfMonthSuffix(final int n) {
        if (n >= 11 && n <= 13) {
            return "th";
        }
        switch (n % 10) {
            case 1:  return "st";
            case 2:  return "nd";
            case 3:  return "rd";
            default: return "th";
        }
    }
}