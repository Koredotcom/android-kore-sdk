package kore.botssdk.utils;

import java.util.ArrayList;

import kore.botssdk.net.BotRequestPool;
import kore.botssdk.websocket.SocketWrapper;

/**
 * Created by Pradeep Mahato on 03-Jun-16.
 */
public class BotRequestController {

    public static BotRequestController botRequestController;

    public BotRequestController() {
    }

    public static synchronized BotRequestController getInstance() {
        if (botRequestController == null) {
            botRequestController = new BotRequestController();
        }
        return botRequestController;
    }

    public void startSendingMessage() {
        if (!BotRequestPool.isPoolEmpty()) {
            if (!BotRequestPool.getBotRequestStringArrayList().isEmpty()) {
                ArrayList<String> botRequestStringArrayList = BotRequestPool.getBotRequestStringArrayList();
                int len = botRequestStringArrayList.size();
                for (int i = 0; i < len; i++) {
                    String botRequestPayload = botRequestStringArrayList.get(i);
                    if (SocketWrapper.getInstance().isConnected()) {
                        SocketWrapper.getInstance().sendMessage(botRequestPayload);
                        BotRequestPool.getBotRequestStringArrayList().remove(botRequestPayload);
                        i--; //reset the parameter
                        len--; //reset the length.
                    } else {
                        SocketWrapper.getInstance().reconnect();
                        break; //Break the loop, as re-connection would be attempted from sendMessage(...)
                    }
                }
            }
        }
    }

}
