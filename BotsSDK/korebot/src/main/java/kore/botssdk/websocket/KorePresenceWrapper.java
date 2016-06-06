package kore.botssdk.websocket;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;
import kore.botssdk.net.BotRequestPool;

/**
 * Created by Ramachandra Pradeep on 6/1/2016.
 */
public final class KorePresenceWrapper {

    public static KorePresenceWrapper pKorePresenceInstance;
    private final String LOG_TAG = KorePresenceWrapper.class.getSimpleName();
    private PresenceConnectionListener presenceConnectionListener = null;

    private final WebSocketConnection mConnection = new WebSocketConnection();

    private boolean mIsReconnectionAttemptNeeded = true;
    private String Url;

    private int mReconnectionCount = 0;

    /**
     * Restricting outside object creation
     */
    private KorePresenceWrapper() {
    }

    /**
     * Singleton Instance *
     */
    public static synchronized KorePresenceWrapper getInstance() {
        if (pKorePresenceInstance == null)
            pKorePresenceInstance = new KorePresenceWrapper();
        return pKorePresenceInstance;
    }

    public void connect(String Url) {
        if (Url != null) {
            this.Url = Url;
            try {
                mConnection.connect(Url, new WebSocketHandler() {
                    @Override
                    public void onOpen() {
                    }

                    @Override
                    public void onTextMessage(String payload) {
                        Log.d(getClass().getSimpleName(), "Got echo: " + payload);
                        if (presenceConnectionListener != null) {
                            presenceConnectionListener.onConnected(payload);
                        }
                        if (!BotRequestPool.getBotRequestStringArrayList().isEmpty()) {
                            ArrayList<String> botRequestStringArrayList = BotRequestPool.getBotRequestStringArrayList();
                            int len = botRequestStringArrayList.size();
                            for (int i = 0; i < len; i++) {
                                String botRequestPayload = botRequestStringArrayList.get(i);
                                if (sendMessage(botRequestPayload)) {
                                    BotRequestPool.getBotRequestStringArrayList().remove(botRequestPayload);
                                    i--; //reset the parameter
                                    len--; //reset the length.
                                } else {
                                    break; //Break the loop, as re-connection would be attempted from sendMessage(...)
                                }
                            }
                        }
                    }

                    @Override
                    public void onClose(int code, String reason) {
                        HashMap<String, String> disConnect = new HashMap<>();
                        disConnect.put("name", "disconnect");
                        if (presenceConnectionListener != null) {
                            presenceConnectionListener.onDisconnected(reason);
                        }
                        Log.d(getClass().getSimpleName(), "Connection lost.");
                    }
                });
            } catch (WebSocketException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Start sending the message's as in queue
     */
    public void sendMessage() {
        if (!BotRequestPool.getBotRequestStringArrayList().isEmpty()) {
            ArrayList<String> botRequestStringArrayList = BotRequestPool.getBotRequestStringArrayList();
            int len = botRequestStringArrayList.size();
            for (int i = 0; i < len; i++) {
                String botRequestPayload = botRequestStringArrayList.get(i);
                if (sendMessage(botRequestPayload)) {
                    BotRequestPool.getBotRequestStringArrayList().remove(botRequestPayload);
                    i--; //reset the parameter
                    len--; //reset the length.
                } else {
                    break; //Break the loop, as re-connection would be attempted from sendMessage(...)
                }
            }
        }
    }

    /**
     * @param msg : The message object
     * @return Was it able to successfully send the message.
     */
    public boolean sendMessage(String msg) {
        if (mConnection != null && mConnection.isConnected()) {
            mConnection.sendTextMessage(msg);
            return true;
        } else {
            connect(Url);
            return false;
        }
    }

    public boolean isConnected() {
        if (mConnection != null && mConnection.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * For disconnecting user's presence
     * Call this method when the user logged out
     */
    public void disConnect() {
        this.mIsReconnectionAttemptNeeded = false;
        if (mConnection != null && mConnection.isConnected()) {
            try {
                mConnection.disconnect();
            } catch (Exception e) {
                Log.d(LOG_TAG, "Exception while disconnection");
            }
            Log.d(LOG_TAG, "DisConnected successfully");
        } else {
            Log.d(LOG_TAG, "Cannot disconnect.._client is null");
        }
    }

    public void setPresenceConnectionListener(PresenceConnectionListener presenceConnectionListener) {
        this.presenceConnectionListener = presenceConnectionListener;
    }
}