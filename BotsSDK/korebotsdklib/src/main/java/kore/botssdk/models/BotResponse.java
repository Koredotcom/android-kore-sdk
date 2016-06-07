package kore.botssdk.models;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Pradeep Mahato on 03-Jun-16.
 */

public class BotResponse extends BaseBotMessage{

    private String type;
    private BotInfoModel botInfo;
    private ArrayList<BotResponseMessage> message;

    public ArrayList<BotResponseMessage> getMessage() {
        return message;
    }

    public BotResponseMessage getTempMessage() {
        return message.get(0);
    }

    public BotInfoModel getBotInfo() {
        return botInfo;
    }

    @Override
    public boolean isSend() {
        return false;
    }

}
