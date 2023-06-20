package kore.botssdk.models;

import kore.botssdk.net.RestResponse;

/**
 * Created by Pradeep Mahato on 03-Jun-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */

public class BotRequest extends BaseBotMessage {

    private RestResponse.BotMessage message;
    private final String resourceid = "/bot.message";
    private BotInfoModel botInfo;
    private final long id = 1;

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

}
