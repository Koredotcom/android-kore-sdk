package kore.botssdk.websocket;

public interface BotStatusListener {

    void onBotConnected();

    void onBotDisconnected();

    void onBotConnecting();

    void onBotReconnected();

    void onBotConnectionFail(String strReason);

}