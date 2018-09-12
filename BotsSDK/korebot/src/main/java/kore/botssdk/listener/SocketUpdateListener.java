/*
package kore.botssdk.listener;

import android.content.Context;

import kore.botssdk.event.KoreEventCenter;
import kore.botssdk.events.SocketDataTransferModel;
import kore.botssdk.models.BotRequest;

public class SocketUpdateListener {
    public static final String LOG_TAG = "Socket Update Listener";
    protected static SocketUpdateListener socketUpdateListener;
    protected Context mContext;
    protected String userId;
    protected String accessToken;
    public static SocketUpdateListener getInstance(Context context,String userId,String accessToken){
        if(socketUpdateListener == null){
            socketUpdateListener = new SocketUpdateListener(context,userId,accessToken);
        }
        return socketUpdateListener;
    }
    protected SocketUpdateListener(Context context, String userId, String accessToken){
        this.accessToken = accessToken;
        this.mContext = context;
        this.userId = userId;
    }
    public void onConnectionUpdated(){

    }
   public void onMessageUpdate(String payload, boolean isSentMessage, BotRequest sentMsg){
       KoreEventCenter.post(new SocketDataTransferModel(isSentMessage ? BaseSocketConnectionManager.EVENT_TYPE.TYPE_MESSAGE_UPDATE : BaseSocketConnectionManager.EVENT_TYPE.TYPE_TEXT_MESSAGE,payload,sentMsg));
    }

}
*/
