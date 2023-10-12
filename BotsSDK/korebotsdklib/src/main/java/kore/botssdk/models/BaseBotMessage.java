package kore.botssdk.models;

import androidx.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import kore.botssdk.utils.DateUtils;

/**
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */

public abstract class BaseBotMessage {

    public void setFrom(String from) {
        this.from = from;
    }

    public void setSend(boolean send) {
        isSend = send;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    protected String from;

    protected boolean isSend;
    protected String createdOn;
    private long createdInMillis;
    public static final SimpleDateFormat isoFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);

    public abstract boolean isSend();

    public String getFrom() {
        return from;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public long getCreatedInMillis() {
        if (createdInMillis == 0) {
            try {
                createdInMillis = getTimeInMillis(createdOn, !isSend());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return createdInMillis;
    }

    private long getTimeInMillis(String timeStamp, boolean timezoneModifiedRequired) throws ParseException {
        if (timeStamp == null || timeStamp.isEmpty()) return System.currentTimeMillis();
        return Objects.requireNonNull(isoFormatter.parse(timeStamp)).getTime() + ((timezoneModifiedRequired) ? TimeZone.getDefault().getRawOffset() : 0);

    }

    @NonNull
    public String getFormattedDate() {
        return DateUtils.formattedSentDateV6(getCreatedInMillis());
    }
}
