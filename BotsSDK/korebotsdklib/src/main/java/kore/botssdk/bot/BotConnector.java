package kore.botssdk.bot;

import android.content.Context;

import kore.botssdk.websocket.SocketConnectionListener;
import kore.botssdk.websocket.SocketWrapper;

/**
 * Created by Ramachandra Pradeep on 6/13/2016.
 */
public class BotConnector {

    private Context mContext;


    private BotConnector(){}

    /**
     *
     * @param mContext
     */
    public BotConnector(Context mContext){
        this.mContext = mContext;
    }

    /**
     *
     * @param accessToken
     * @param chatBot
     * @param taskBotId
     * @param socketConnectionListener
     */
    public void connectAsNormalUser(String accessToken, String chatBot, String taskBotId,SocketConnectionListener socketConnectionListener){
        SocketWrapper.getInstance(mContext).connect(accessToken,chatBot,taskBotId,socketConnectionListener);
    }

    /**
     *
     * @param clientId
     * @param secretKey
     * @param socketConnectionListener
     */
    public void connectAsAnonymousUser(String clientId, String secretKey,SocketConnectionListener socketConnectionListener){
        SocketWrapper.getInstance(mContext).connectAnonymous(clientId,secretKey,socketConnectionListener);
    }

    /**
     *
     */
    public void disconnect(){
        SocketWrapper.getInstance(mContext).disConnect();
    }

    /**
     *
     */
    public void isConnected(){
        SocketWrapper.getInstance(mContext).isConnected();
    }

    /**
     *
     * @param msg
     */
    public void sendMessage(String msg){
        SocketWrapper.getInstance(mContext).sendMessage(msg);
    }

}
