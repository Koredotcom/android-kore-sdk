package kore.botssdk.bot;

import android.content.Context;

import java.util.ArrayList;

import kore.botssdk.net.BotRequestPool;
import kore.botssdk.websocket.SocketConnectionListener;
import kore.botssdk.websocket.SocketWrapper;

/**
 * Created by Ramachandra Pradeep on 6/13/2016.
 */
public class BotConnector {

    private Context mContext;


    private BotConnector() {
    }

    /**
     * @param mContext
     */
    public BotConnector(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * Connection for authenticated user
     *
     * @param accessToken
     * @param chatBot
     * @param taskBotId
     * @param socketConnectionListener
     */
    public void connectAsAuthenticatedUser(String accessToken, String chatBot, String taskBotId, SocketConnectionListener socketConnectionListener) {
        SocketWrapper.getInstance(mContext).connect(accessToken, chatBot, taskBotId, socketConnectionListener);
    }

    /**
     * Connection for anonymous user
     *
     * @param clientId
     * @param secretKey
     * @param socketConnectionListener
     */
    public void connectAsAnonymousUser(String clientId, String secretKey, SocketConnectionListener socketConnectionListener) {
        SocketWrapper.getInstance(mContext).connectAnonymous(clientId, secretKey, socketConnectionListener);
    }

    /**
     *  [MANDATORY] Invoke this method to disconnect the previously connected socket connection.
     */
    public void disconnect() {
        SocketWrapper.getInstance(mContext).disConnect();
    }

    /**
     * @return whether socket connection is present
     */
    public boolean isConnected() {
        return SocketWrapper.getInstance(mContext).isConnected();
    }

    /**
     * @param msg
     */
    public void sendMessage(String msg) {

        if (msg != null && !msg.isEmpty()) {
            BotRequestPool.getBotRequestStringArrayList().add(msg);
        }

        if (!BotRequestPool.isPoolEmpty()) {
            if (!BotRequestPool.getBotRequestStringArrayList().isEmpty()) {
                ArrayList<String> botRequestStringArrayList = BotRequestPool.getBotRequestStringArrayList();
                int len = botRequestStringArrayList.size();
                for (int i = 0; i < len; i++) {
                    String botRequestPayload = botRequestStringArrayList.get(i);
                    boolean wasSuccessfullySend = SocketWrapper.getInstance(mContext).sendMessage(botRequestPayload);
                    if (wasSuccessfullySend) {
                        BotRequestPool.getBotRequestStringArrayList().remove(botRequestPayload);
                        i--; //reset the parameter
                        len--; //reset the length.
                    } else {
                        break;//Break the loop, as re-connection would be attempted from sendMessage(...)
                    }
                }
            }
        }
    }

}
