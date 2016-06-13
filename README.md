# Overview
    This SDK allows you to talk to Bots over a web socket.

# Prerequisites
    - SDK app credentials (Create your SDk app in bot admin console to aquire the client id and client secret.
    - jwt assertion generation methodology. ex: service which will be used in the assertion function injected as part of obtaining the connection.
    
# Running the Demo app
    Download or clone the repository.
    Import the project.
    Run the app.

# Integrating into your app
#### 1. Create BotConnector object
    Create BotConnector object providing context.
    BotConnector botConnector = new BotConnector(context);
    
#### 2. Implement SocketConnectionListener
    Implement this interface to receive callbacks
    SocketConnectionListener socketConnectionListener = new SocketConnectionListener() {
        @Override
        public void onOpen() {
        }
        @Override
        public void onClose(WebSocket.WebSocketConnectionObserver.WebSocketCloseNotification code, String reason) {
        }
        @Override
        public void onTextMessage(String payload) {
        }
        @Override
        public void onRawTextMessage(byte[] payload) {
        }
        @Override
        public void onBinaryMessage(byte[] payload) {
        }
    };
    
#### 3. Initializing the RTM client
    String accessToken = "Y6w*******************";
    String chatBot = "My Bot";
    String taskBotId = "st-**************";
    botconnector.connectAsAuthenticatedUser(accessToken, chatBot, taskBotId, socketConnectionListener);
    
#### 4. Send message
    botconnector.sendMessage("Tweet hello")
    
#### 5. Listen to events
    Listen to events in socketConnectionListener.
    
    @Override
    public void onOpen() {
    }
    @Override
    public void onClose(WebSocket.WebSocketConnectionObserver.WebSocketCloseNotification code, String reason) {
    }
    @Override
    public void onTextMessage(String payload) {
    }
    @Override
    public void onRawTextMessage(byte[] payload) {
    }
    @Override
    public void onBinaryMessage(byte[] payload) {
    }
    
#### 6. Subscribe to push notifications
    PushNotificationRegistrar pushNotificationRegistrar =  new PushNotification(requestListener);
    pushNotificationRegistrar.registerPushNotification(Context context, String userId, String accessToken);
    
#### 7. Unsubscribe to push notifications
    PushNotificationRegistrar pushNotificationRegistrar =  new PushNotification(requestListener);
    pushNotificationRegistrar.unsubscribePushNotification(Context context, String accessToken);

#### 8. Anonymous user login
    String clientId = "YOUR_SDK_CLIENTID";
    String secretKey = "CLIENT_SECRET_KEY";
    botconnector.connectAsAnonymousUser(String clientId, String secretKey, SocketConnectionListener socketConnectionListener)
    
#### 9. Disconnect
    Invoke to disconnect previous socket connection upon closing Activity/Fragment or upon destroying view.
    
    botconnector.disconnect();
    
    



























License
----
Copyright © Kore, Inc. MIT License; see LICENSE for further details.



 
