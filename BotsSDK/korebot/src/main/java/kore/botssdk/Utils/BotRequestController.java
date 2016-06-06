package kore.botssdk.utils;

import android.content.Context;

import com.octo.android.robospice.SpiceManager;

import java.util.ArrayList;

import kore.botssdk.net.BotRequestPool;
import kore.botssdk.websocket.SocketWrapper;

/**
 * Created by Pradeep Mahato on 03-Jun-16.
 */
public class BotRequestController {

    Context context;
    SpiceManager spiceManager;
    static String url;

    public static BotRequestController botRequestController;

    public BotRequestController() {
    }

    public static synchronized BotRequestController getInstance() {
        if (botRequestController == null) {
            botRequestController = new BotRequestController();
        }
        return botRequestController;
    }

    public BotRequestController(Context context, SpiceManager spiceManager) {
        this.context = context;
        this.spiceManager = spiceManager;
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
                        SocketWrapper.getInstance().connect(url);
                        break; //Break the loop, as re-connection would be attempted from sendMessage(...)
                    }
                }
            }
        }
    }

    public static void setUrl(String url) {
        BotRequestController.url = url;
    }

    public static String getUrl() {
        return url;
    }
}
