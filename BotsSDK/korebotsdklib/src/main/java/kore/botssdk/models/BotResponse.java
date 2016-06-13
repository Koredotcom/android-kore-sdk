package kore.botssdk.models;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Pradeep Mahato on 03-Jun-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */

public class BotResponse extends BaseBotMessage {

    private String type;
    private BotInfoModel botInfo;
    private ArrayList<BotResponseMessage> message;
    private String icon;

    public ArrayList<BotResponseMessage> getMessage() {
        return message;
    }

    public BotResponseMessage getTempMessage() {
        return message.get(0);
    }

    public BotInfoModel getBotInfo() {
        return botInfo;
    }

    public String getIcon() {
        return icon;
    }

    @Override
    public boolean isSend() {
        return false;
    }

}
