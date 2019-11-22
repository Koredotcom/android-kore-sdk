package kore.botssdk.retroresponse;

import java.util.ArrayList;

import kore.botssdk.models.BaseBotMessage;

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
