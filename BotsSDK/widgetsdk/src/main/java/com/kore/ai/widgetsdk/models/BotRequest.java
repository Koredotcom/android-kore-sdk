package com.kore.ai.widgetsdk.models;

import com.kore.ai.widgetsdk.restresponse.KaRestResponse;

/**
 * Created by Pradeep Mahato on 03-Jun-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */

public class BotRequest extends BaseBotMessage {

    private KaRestResponse.BotMessage message;
    private String resourceid = "/bot.message";
    private BotInfoModel botInfo;
    private long id = 1;

    public void setMessage(KaRestResponse.BotMessage message) {
        this.message = message;
    }

    public KaRestResponse.BotMessage getMessage() {
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
