package kore.botssdk.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import kore.botssdk.net.SDKConfiguration;
import kore.botssdk.utils.DateUtils;

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

    private String formattedDate = "";
    private String timeStamp = "";
    public static final SimpleDateFormat isoFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);

    public abstract boolean isSend();

    public String getFrom() {
        return from;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
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

    public long getTimeInMillis(String timeStamp, boolean timezoneModifiedRequired) throws ParseException {
        if (timeStamp == null || timeStamp.isEmpty()) return System.currentTimeMillis();
        return isoFormatter.parse(timeStamp).getTime() + ((timezoneModifiedRequired) ? TimeZone.getDefault().getRawOffset() : 0);

    }

    public void setCreatedInMillis(long createdInMillis) {
        this.createdInMillis = createdInMillis;
    }

    public String getFormattedDate() {
        return formattedDate;//DateUtils.formattedSentDateV6(getCreatedInMillis());
    }

    public String getTimeStamp() {
        return timeStamp;//prepareTimeStamp(getCreatedInMillis(), SDKConfiguration.Client.bot_name);
    }

    public String prepareTimeStamp(long milliSecs) {
        if (this instanceof BotRequest) {
            return (DateUtils.getTimeInAmPm(milliSecs) + ", " + DateUtils.formattedSentDateV6(milliSecs)) + "<medium><b>" + " You" + "</b></medium>";
        } else {
            return "<medium><b>" + SDKConfiguration.Client.bot_name + "</b></medium>" + " " + DateUtils.getTimeInAmPm(milliSecs) + ", " +
                    DateUtils.formattedSentDateV6(milliSecs);
        }
    }
}
