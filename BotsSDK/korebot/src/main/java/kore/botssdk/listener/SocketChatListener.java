package kore.botssdk.listener;

import kore.botssdk.events.SocketDataTransferModel;
import kore.botssdk.models.BotResponse;

/**
 * Created by Ramachandra Pradeep on 12-Sep-18.
 */

public interface SocketChatListener {
    void onMessage(BotResponse resp);
    void onConnectionStateChanged(BaseSocketConnectionManager.CONNECTION_STATE state, boolean isReconnection);
    void onMessage(SocketDataTransferModel data);
}
