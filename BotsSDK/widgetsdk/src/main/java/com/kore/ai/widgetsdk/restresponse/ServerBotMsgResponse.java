package com.kore.ai.widgetsdk.restresponse;

import com.kore.ai.widgetsdk.models.BaseBotMessage;

import java.util.ArrayList;

public class ServerBotMsgResponse {

    public ArrayList<BaseBotMessage> getBotMessages() {
        return botMessages;
    }

    public void setBotMessages(ArrayList<BaseBotMessage> botMessages) {
        this.botMessages = botMessages;
    }

    private ArrayList<BaseBotMessage> botMessages;

    public int getOriginalSize() {
        return originalSize;
    }

    public void setOriginalSize(int originalSize) {
        this.originalSize = originalSize;
    }

    private int originalSize;

}
