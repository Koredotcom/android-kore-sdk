package kore.botssdk.websocket;

import java.util.HashMap;

/**
 *
 */
public interface PresenceConnectionListener {
	/**
	 * Callback for when user Connected 
	 */
	void onConnected(String message);
	
	/**
	 * Callback for when user Disconnected 
	 */
	void onDisconnected(String reason);
}
