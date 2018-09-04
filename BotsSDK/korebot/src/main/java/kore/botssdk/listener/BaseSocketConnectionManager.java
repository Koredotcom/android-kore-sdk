package kore.botssdk.listener;

import android.content.Context;

import com.octo.android.robospice.SpiceManager;

import kore.botssdk.models.JWTTokenResponse;
import kore.botssdk.net.BotDemoRestService;
import kore.botssdk.websocket.SocketConnectionListener;

/**
 * Created by Ramachandra Pradeep on 03-Jan-18.
 */

public abstract class BaseSocketConnectionManager implements SocketConnectionListener,TTSUpdate {

    protected SpiceManager botsSpiceManager = new SpiceManager(BotDemoRestService.class);
    protected Context mContext;
    public SocketUpdateListener socketUpdateListener;

    public JWTTokenResponse getJwtKeyResponse() {
        return jwtKeyResponse;
    }

    public void setJwtKeyResponse(JWTTokenResponse jwtKeyResponse) {
        this.jwtKeyResponse = jwtKeyResponse;
    }

    protected JWTTokenResponse jwtKeyResponse;
    protected boolean isSubscribed;

    public enum EVENT_TYPE{
        TYPE_CONNECTED,
        TYPE_TEXT_MESSAGE,
        TYPE_DISCONNECTED,
        TYPE_MESSAGE_UPDATE
    }

    public enum CONNECTION_STATE{
        CONNECTING,
        CONNECTED,
        DISCONNECTED,
        CONNECTED_BUT_DISCONNECTED
    }

    public abstract void startAndInitiateConnection(Context mContext, String userId, String accessToken,SocketUpdateListener socketUpdateListener);
    public abstract void shutDownConnection();
    public abstract void subscribe();
    public abstract void unSubscribe();
}
