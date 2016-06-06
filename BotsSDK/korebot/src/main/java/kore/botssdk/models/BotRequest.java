package kore.botssdk.models;

import java.util.ArrayList;
import java.util.Objects;

import kore.botssdk.net.RestResponse;

/**
 * Created by Pradeep Mahato on 03-Jun-16.
 */

public class BotRequest extends BaseBotMessage {

    private RestResponse.BotMessage message;
    private String resourceid = "/bot.message";
    private ArrayList<Objects> botInfo = new ArrayList<>();
    private int id = 1;

    public void setMessage(RestResponse.BotMessage message) {
        this.message = message;
    }

    public RestResponse.BotMessage getMessage() {
        return message;
    }

    @Override
    public boolean isSend() {
        return true;
    }
}
