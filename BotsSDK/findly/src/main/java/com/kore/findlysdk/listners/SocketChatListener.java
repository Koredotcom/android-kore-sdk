package com.kore.findlysdk.listners;

import com.kore.findlysdk.events.SocketDataTransferModel;
import com.kore.findlysdk.models.BotResponse;

/**
 * Created by Sudheer Jampana on 12-Nov-20.
 */

public interface SocketChatListener {
    void onMessage(BotResponse resp);
    void onConnectionStateChanged(BaseSocketConnectionManager.CONNECTION_STATE state, boolean isReconnection);
    void onMessage(SocketDataTransferModel data);
}
