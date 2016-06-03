package kore.botssdk.models;

import java.util.ArrayList;
import java.util.Objects;

import kore.botssdk.net.KoreRestResponse;

/**
 * Created by Pradeep Mahato on 03-Jun-16.
 */

public class BotRequest extends BaseBotMessage {

    private KoreRestResponse.BotMessage message;
    private String resourceid = "/bot.message";
    private ArrayList<Objects> botInfo = new ArrayList<>();
    private int id = 1;

    public void setMessage(KoreRestResponse.BotMessage message) {
        this.message = message;
    }

    public KoreRestResponse.BotMessage getMessage() {
        return message;
    }

    @Override
    public boolean isSend() {
        return true;
    }
}
