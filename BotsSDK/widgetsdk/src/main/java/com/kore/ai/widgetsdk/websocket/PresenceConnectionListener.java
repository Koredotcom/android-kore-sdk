package com.kore.ai.widgetsdk.websocket;

import java.util.HashMap;

/**
 * Created by Ramachandra Pradeep on 11-Dec-18.
 */

public interface PresenceConnectionListener {
    /**
     * Callback for when user Connected
     */
    void onConnected(HashMap<String, String> hsh);

    /**
     * Callback for when user Disconnected
     */
    void onDisconnected(HashMap<String, String> hsh);
}