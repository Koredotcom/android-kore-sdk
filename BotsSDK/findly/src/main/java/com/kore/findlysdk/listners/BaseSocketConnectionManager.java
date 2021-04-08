package com.kore.findlysdk.listners;

import android.content.Context;

import com.kore.findlysdk.models.JWTTokenResponse;
import com.kore.findlysdk.models.RestResponse;
import com.kore.findlysdk.models.TokenResponseModel;
import com.kore.findlysdk.models.UserNameModel;
import com.kore.findlysdk.websocket.SocketConnectionListener;

/**
 * Created by Ramachandra Pradeep on 03-Jan-18.
 */

public abstract class BaseSocketConnectionManager implements SocketConnectionListener,TTSUpdate {


    protected Context mContext;
//    public SocketUpdateListener socketUpdateListener;
//    public boolean isWithAuth;

    public JWTTokenResponse getJwtKeyResponse() {
        return jwtKeyResponse;
    }

    public TokenResponseModel getTokenResponseModel() {
        return tokenResponseModel;
    }

    public void setTokenResponseModel(TokenResponseModel tokenResponseModel) {
        this.tokenResponseModel = tokenResponseModel;
    }

    public void setJwtKeyResponse(JWTTokenResponse jwtKeyResponse) {
        this.jwtKeyResponse = jwtKeyResponse;
    }

    public TokenResponseModel tokenResponseModel;
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

    public abstract void startAndInitiateConnectionWithAuthToken(Context mContext, String userId, String accessToken, RestResponse.BotCustomData botCustomData);
    public abstract void shutDownConnection();
    public abstract void subscribe();
    public abstract void subscribe(SocketChatListener listener);
    public abstract void unSubscribe();
    public abstract void startAndInitiateConnectionWithConfig(Context mContext, RestResponse.BotCustomData botCustomData);
    public abstract void startAndInitiateConnection(Context mContext, String userId, String accessToken, UserNameModel userNameModel, String orgId);
}
