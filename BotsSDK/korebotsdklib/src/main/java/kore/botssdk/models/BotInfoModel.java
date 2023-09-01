package kore.botssdk.models;

import java.util.HashMap;

/**
 * Created by Pradeep Mahato on 06-Jun-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BotInfoModel {
    public String chatBot;
    public String taskBotId;
    public String channelClient = "Android";
    public String name;
    public String _id;

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_id() {
        return _id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getChannelClient() {
        return channelClient;
    }

    public void setChannelClient(String channelClient) {
        this.channelClient = channelClient;
    }


    public final HashMap<Object,Object> customData;


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
