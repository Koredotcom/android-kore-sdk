package com.kore.ai.widgetsdk.models;

import java.util.HashMap;

/**
 * Created by Pradeep Mahato on 06-Jun-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BotInfoModel {
    public String chatBot;
    public String taskBotId;
    public String client = "Android";

    public String getChannelClient() {
        return client;
    }

    public void setChannelClient(String channelClient) {
        this.client = channelClient;
    }


    public HashMap<Object,Object> customData;


    public BotInfoModel(String chatBot, String taskBotId,HashMap customData) {
        this.chatBot = chatBot;
        this.taskBotId = taskBotId;
        this.customData = customData;
    }

    public void setChatBot(String chatBot) {
        this.chatBot = chatBot;
    }

    public void setTaskBotId(String taskBotId) {
        this.taskBotId = taskBotId;
    }
}
