package kore.botssdk.websocket;

public interface BotStatusListener {

    void onBotConnected();

    void onBotDisconnected(String event_code, String event_message);

    void onBotConnectionFail(String event_code, String strReason);

    void onDeepLinkClicked(String event_code, String url);
}
