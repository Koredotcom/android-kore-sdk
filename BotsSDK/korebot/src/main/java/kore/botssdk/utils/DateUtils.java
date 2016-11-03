package kore.botssdk.utils;

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

    public static String getTimeStamp(String timeStamp, boolean timezoneModifiedRequired) throws ParseException {
        long timeStampMillis = isoFormatter.parse(timeStamp).getTime() + ((timezoneModifiedRequired) ? TimeZone.getDefault().getRawOffset() : 0);
        return getTimeStamp(timeStampMillis);
    }

    public static String getTimeStamp(long timeStampMillis) {
        return dateFormat4.format(new Date(timeStampMillis));
    }

}