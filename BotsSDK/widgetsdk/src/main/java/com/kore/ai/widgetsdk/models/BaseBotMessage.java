package com.kore.ai.widgetsdk.models;

import com.kore.ai.widgetsdk.utils.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Pradeep Mahato on 03-Jun-16.
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
    private static final SimpleDateFormat isoFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);

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
        return isoFormatter.parse(timeStamp).getTime() + ((timezoneModifiedRequired) ? TimeZone.getDefault().getRawOffset() : 0);

    }

    public void setCreatedInMillis(long createdInMillis) {
        this.createdInMillis = createdInMillis;
    }

    public String getFormattedDate() {
        return DateUtils.formattedSentDateV6(getCreatedInMillis());
    }
}
