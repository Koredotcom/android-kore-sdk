package websocket.async.http.socketio;

import java.util.HashMap;
import java.util.Hashtable;

/**
 * 
 * @author Pradeep
 * Callbak for updating recieved string to KonyApp
 */

public interface CustomCallback {
	void onStringRecived(HashMap _data);
}
