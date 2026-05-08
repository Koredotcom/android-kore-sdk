package kore.botssdk.models;

import kore.botssdk.net.RestResponse;

/**
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */

public class BotRequest extends BaseBotMessage {

    private RestResponse.BotMessage message;
    private final String resourceid = "/bot.message";
    private BotInfoModel botInfo;
    private long id;
    private MessageStatus status;

    public void setMessage(RestResponse.BotMessage message) {
        this.message = message;
    }

    public RestResponse.BotMessage getMessage() {
        return message;
    }

    public BotInfoModel getBotInfo() {
        return botInfo;
    }

    @Override
    public boolean isSend() {
        return true;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }
    public long getId() {
        return id;
    }

    public enum MessageStatus {
        SENDING,
        SENT,
        FAILED
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

}
