package com.kore.ai.widgetsdk.managers;/*
package com.kore.ai.koreassistant.managers;

import android.content.Context;

import com.kore.ai.koreassistant.net.KaBotsRestService;
import com.kore.ai.koreassistant.net.KaRestResponse;
import com.kore.ai.koreassistant.net.KaRestService;
import com.octo.android.robospice.SpiceManager;

import kore.botssdk.listener.TTSUpdate;
import kore.botssdk.websocket.SocketConnectionListener;

*/
/**
 * Created by Ramachandra Pradeep on 03-Jan-18.
 *//*


public abstract class BaseSocketConnectionManager implements SocketConnectionListener,TTSUpdate {

    protected SpiceManager botsSpiceManager = new SpiceManager(KaRestService.class);
    protected Context mContext;

    public KaRestResponse.JWTTokenResponse getJwtKeyResponse() {
        return jwtKeyResponse;
    }

    public void setJwtKeyResponse(KaRestResponse.JWTTokenResponse jwtKeyResponse) {
        this.jwtKeyResponse = jwtKeyResponse;
    }

    protected KaRestResponse.JWTTokenResponse jwtKeyResponse;
    protected boolean isSubscribed;

    public enum EVENT_TYPE{
        TYPE_CONNECTED,
        TYPE_TEXT_MESSGE,
        TYPE_DISCONNECTED,
        TYPE_MESSAGE_UPDATE
    }

    public enum CONNECTION_STATE{
        CONNECTING,
        CONNECTED,
        DISCONNECTED,
        CONNECTED_BUT_DISCONNECTED
    }

    public abstract void startAndInitiateConnection(Context mContext, String userId, String accessToken);
    public abstract void shutDownConnection();
    public abstract void subscribe();
    public abstract void unSubscribe();
}
*/
