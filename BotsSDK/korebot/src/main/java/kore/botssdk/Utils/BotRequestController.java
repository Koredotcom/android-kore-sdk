package kore.botssdk.utils;

import android.content.Context;

import com.octo.android.robospice.SpiceManager;

import kore.botssdk.net.BotRequestPool;
import kore.botssdk.websocket.SocketWrapper;

/**
 * Created by Pradeep Mahato on 03-Jun-16.
 */
public class BotRequestController {

    Context context;
    SpiceManager spiceManager;

    public BotRequestController(Context context, SpiceManager spiceManager) {
        this.context = context;
        this.spiceManager = spiceManager;
    }


    public void startSendingMessage() {
        if (!BotRequestPool.isPoolEmpty()) {

        }
    }

    public boolean startSendingMessage(String messageStr) {
        boolean isConnected = SocketWrapper.getInstance().isConnected();
        if (isConnected) {
            //Send the message
            SocketWrapper.getInstance().sendMessage(messageStr);
            BotRequestPool.getBotRequestStringArrayList().remove(messageStr);
            return true;
        } else {
            //Make the connection....
            return false;
        }
    }

}
