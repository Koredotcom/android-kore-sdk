package kore.botssdk.events;

import kore.botssdk.listener.BaseSocketConnectionManager;

import kore.botssdk.models.BotRequest;

/**
 * Created by Ramachandra Pradeep on 03-Jan-18.
 */

public class SocketDataTransferModel {
    public BaseSocketConnectionManager.EVENT_TYPE getEvent_type() {
        return event_type;
    }

    public void setEvent_type(BaseSocketConnectionManager.EVENT_TYPE event_type) {
        this.event_type = event_type;
    }

    public String getPayLoad() {
        return payLoad;
    }

    public void setPayLoad(String payLoad) {
        this.payLoad = payLoad;
    }

    private BaseSocketConnectionManager.EVENT_TYPE event_type;
    private String payLoad;

    public BotRequest getBotRequest() {
        return botRequest;
    }

    public void setBotRequest(BotRequest botRequest) {
        this.botRequest = botRequest;
    }

    private BotRequest botRequest;

    public SocketDataTransferModel(BaseSocketConnectionManager.EVENT_TYPE event_type, String payLoad, BotRequest botRequest){
        this.event_type = event_type;
        this.payLoad = payLoad;
        this.botRequest = botRequest;
    }
}
