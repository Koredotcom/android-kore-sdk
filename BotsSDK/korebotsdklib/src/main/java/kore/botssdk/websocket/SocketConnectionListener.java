package kore.botssdk.websocket;


/*
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public interface SocketConnectionListener {

    /**
     * Fired when the WebSockets connection has been established.
     * After this happened, messages may be sent.
     */
    void onOpen(boolean isReconnection);

    /**
     * Fired when the WebSockets connection has deceased (or could
     * not established in the first place).
     *
     * @param code   Close code.
     * @param reason Close reason (human-readable).
     */
    void onClose(int code, String reason);

    /**
     * Fired when a text message has been received (and text
     * messages are not set to be received raw).
     *
     * @param payload Text message payload or null (empty payload).
     */
    void onTextMessage(String payload);

    /**
     * Fired when a text message has been received (and text
     * messages are set to be received raw).
     *
     * @param payload Text message payload as raw UTF-8 or null (empty payload).
     */
    void onRawTextMessage(byte[] payload);

    /**
     * Fired when a binary message has been received.
     *
     * @param payload Binar message payload or null (empty payload).
     */
    void onBinaryMessage(byte[] payload);


    void refreshJwtToken();

    /**
     * Fired when a sdk reached maximum attempts to connect to Bot.
     * @param reconnectionStopped with reason.
     */
    void onReconnectStopped(String reconnectionStopped);

    /**
     * Fired when a sdk completed start call during connect to Bot.
     * @param isReconnect with reconnect status.
     */
    void onStartCompleted(boolean isReconnect);

}
