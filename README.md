# Kore SDK
Kore offers Bots SDKs as a set of platform-specific client libraries that provide a quick and convenient way to integrate Kore Bots chat capability into custom applications.

With just few lines of code, you can embed our Kore chat widget into your applications to enable end-users to interact with your applications using Natural Language. For more information, refer to https://developer.kore.com/docs/bots/kore-web-sdk/

# Kore Android SDK for developers

Kore SDK for Android enables you to talk to Kore bots over a web socket. This repo also comes with the code for sample application that developers can modify according to their Bot configuration.

# Setting up

### Prerequisites
* Service to generate JWT (JSON Web Tokens)- this service will be used in the assertion function injected to obtain the connection.
* SDK app credentials 
* Login to the Bots platform
	* Navigate to the Bot builder
	* Search and click on the bot 
	* Enable *Web / Mobile Client* channel against the bot as shown in the screen below.
		
	![Add bot to Web/Mobile Client channel](https://github.com/Koredotcom/iOS-kore-sdk/blob/feature/1.0/channels.png)
	
	* create new or use existing SDK app to obtain client id and client secret
	
	![Obtain Client id and Client secret](https://github.com/Koredotcom/iOS-kore-sdk/blob/feature/1.0/web-mobile-client-channel.png)

## Instructions

### Configuration changes
* Setting up clientId, clientSecret, botId, botName and identity in SDKConfiguration.java

Client id - Copy this id from Bot Builder SDK Settings ex. cs-5250bdc9-6bfe-5ece-92c9-ab54aa2d4285
 ```
 public static final String demo_client_id = "<client-id>";
 ```

Client secret - copy this value from Bot Builder SDK Settings ex. Wibn3ULagYyq0J10LCndswYycHGLuIWbwHvTRSfLwhs=
 ```
public static final String clientSecret = "<client-secret>";
 ```

User identity - rhis should represent the subject for JWT token that could be an email or phone number in case of known user. In case of anonymous user, this can be a randomly generated unique id.
 ```
public static final String identity = "<user@example.com>";
 ```

Bot name - copy this value from Bot Builder -> Channels -> Web/Mobile SDK config  ex. "Demo Bot"
 ```
public static final String chatBotName = "<bot-name>";
 ```

Bot Id - copy this value from Bot Builder -> Channels -> Web/Mobile SDK config  ex. st-acecd91f-b009-5f3f-9c15-7249186d827d
 ```
public static final String botId = "<bot-id>"; 
 ```

Server URL - replace it with your server URL, if required
 ```
public static final String KORE_BOT_SERVER_URL = "https://bots.kore.com/";
 ```

Anonymous user - if not anonymous, assign same identity (such as email or phone number) while making a connection
 ```
public static final boolean IS_ANONYMOUS_USER = true; 
 ```

Speech server URL
 ```
public static final String SPEECH_SERVER_BASE_URL = "wss://speech.kore.ai/speechcntxt/ws/speech";
 ```

JWT Server URL - specify the server URL for JWT token generation. This token is used to authorize the SDK client. Refer to documentation on how to setup the JWT server for token generation - e.g. https://jwt-token-server.example.com/
 ```
public static final String JWT_SERVER_URL = "<jwt-token-server-url>";

```

### Running the Demo app
*	Download or clone the repository.
*	Import the project.
*	Run the app.

## Integrating into your app
1. Create BotClient object providing context
```
BotClient botClient = new BotClient(this);
```
#### 2. Implement SocketConnectionListener to receive callback
```
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
```
#### 3. Initialize RTM client
```
botClient.connectAsAnonymousUser(jwt,
            SDKConfiguration.Config.demo_client_id,chatBot,taskBotId, BotChatActivity.this);

```
#### 4. JWT genration
    a. You need to have secure token service hosted in your environment which returns the JWT token.
    b. Generate JWT in your enviornment.

NOTE: Please refer about JWT signing and verification at - https://developer.kore.com/docs/bots/kore-web-sdk/

#### 5. Connect with JWT
    private void getJWTToken(){
        String id;
        if(SDKConfiguration.Config.IS_ANONYMOUS_USER){
            id = UUID.randomUUID().toString();
        }else{
            id = SDKConfiguration.Config.identity;
        }

        JWTGrantRequest request = new JWTGrantRequest(SDKConfiguration.Config.demo_client_id,
                SDKConfiguration.Config.clientSecret, id,SDKConfiguration.Config.IS_ANONYMOUS_USER);
        spiceManagerForJWT.execute(request, new RequestListener<RestResponse.JWTTokenResponse>() {
            @Override
            public void onRequestFailure(SpiceException e) {

            }

            @Override
            public void onRequestSuccess(RestResponse.JWTTokenResponse jwt) {
                botClient.connectAsAnonymousUser(jwt.getJwt(),
                        SDKConfiguration.Config.demo_client_id,chatBot,taskBotId, BotChatActivity.this);
            }
        });
    }

#### 6. Send message
```
botClient.sendMessage("Tweet hello")
```
#### 7. Listen to events in socketConnectionListener
```
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
```

#### 8. Subscribe to push notifications
```
PushNotificationRegistrar pushNotificationRegistrar =  new PushNotification(requestListener);
pushNotificationRegistrar.registerPushNotification(Context context, String userId, String accessToken);
```
#### 9. Unsubscribe to push notifications
```
PushNotificationRegistrar pushNotificationRegistrar =  new PushNotification(requestListener);
pushNotificationRegistrar.unsubscribePushNotification(Context context, String accessToken);
```
#### 10. Disconnect
----
Invoke to disconnect previous socket connection upon closing Activity/Fragment or upon destroying view.
botconnector.disconnect();
```
License
```
# How to enable API based (webhook channel) message communication
----
#### 1. Enable the webhook channel by following the below link
	https://developer.kore.ai/docs/bots/channel-enablement/adding-webhook-channel/
	
#### 2. Configure the botOptions in SDKConfiguration.java with the values you get in above steps
	 SDKConfiguration.Client.userIdentity = 'PLEASE_ENTER_USER_EMAIL_ID';// Provide users email id here
	 SDKConfiguration.Client.webHook_bot_name = "PLEASE_ENTER_BOT_NAME" // bot name is case sensitive
	 SDKConfiguration.Client.webHook_bot_id = "PLEASE_ENTER_BOT_ID" 
	 SDKConfiguration.Client.webHook_client_id = "PLEASE_ENTER_CLIENT_ID";
	 SDKConfiguration.Client.webHook_client_secret = "PLEASE_ENTER_CLIENT_SECRET";

#### 3. Change the following lines in SDKConfiguration.java and provide the webhookURL which you get in the above steps
	public static boolean isWebHook = true; // Default it's false
	SDKConfiguration.koreAPIUrl = "PLEASE_ENTER_JWTURL_HERE";//URL of the Service to generate JWT (JSON Web Tokens)
	
Copyright © Kore, Inc. MIT License; see LICENSE for further details.
