# Socket connection flow

The headless library uses the following sequence:

1. The host configures `SDKConfiguration.Server.SERVER_URL` and creates a
   `BotClient` with an application context.
2. `connectAsAnonymousUser` exchanges the supplied JWT assertion through the
   JWT-grant endpoint.
3. The library resolves the RTM URL, invokes `onStartCompleted`, and opens the
   WebSocket connection.
4. `SocketConnectionListener.onOpen` signals that messages can be sent.
5. Incoming text frames are delivered through `onTextMessage`.
6. A lost connection is retried by the socket wrapper when SDK-managed
   reconnection is enabled.
7. `disconnect` closes the socket and disables further reconnect attempts.

```java
SDKConfiguration.Server.setServerUrl("https://your-runtime.example.com");

BotClient client = new BotClient(context);
client.connectAsAnonymousUser(jwt, botName, taskBotId, new SocketConnectionListener() {
    @Override public void onOpen(boolean reconnect) { }
    @Override public void onClose(int code, String reason) { }
    @Override public void onTextMessage(String payload) { }
    @Override public void onRawTextMessage(byte[] payload) { }
    @Override public void onBinaryMessage(byte[] payload) { }
    @Override public void refreshJwtToken() { }
    @Override public void onReconnectStopped(String reason) { }
    @Override public void onStartCompleted(boolean reconnect) { }
}, false);
```

The library does not create activities, fragments, views, or application
resources. Host applications own presentation and decide how to parse or
render received payloads.
