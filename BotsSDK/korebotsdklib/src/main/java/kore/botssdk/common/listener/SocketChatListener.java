package kore.botssdk.common.listener;

import kore.botssdk.common.eventbus.SocketDataTransferModel;
import kore.botssdk.common.manager.BaseSocketConnectionManager;
import kore.botssdk.models.BotResponse;

public interface SocketChatListener {
    void onMessage(BotResponse resp);
    void onConnectionStateChanged(BaseSocketConnectionManager.CONNECTION_STATE state, boolean isReconnection);
    void onMessage(SocketDataTransferModel data);
}
