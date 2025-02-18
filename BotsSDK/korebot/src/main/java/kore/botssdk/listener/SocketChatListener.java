package kore.botssdk.listener;

import kore.botssdk.events.SocketDataTransferModel;
import kore.botssdk.models.BotResponse;

public interface SocketChatListener {
    void onMessage(BotResponse resp);
    void onConnectionStateChanged(BaseSocketConnectionManager.CONNECTION_STATE state, boolean isReconnection);
    void onMessage(SocketDataTransferModel data);
    void onStartCompleted(boolean isReconnect);
}
