# Kore.ai SDK
Kore.ai offers Bots SDKs as a set of platform-specific client libraries that provide a quick and convenient way to integrate Kore Bots chat capability into custom applications.

With just few lines of code, you can embed our Kore.ai chat widget into your applications to enable end-users to interact with your applications using Natural Language.

# Kore.ai Android SDK for developers

Kore.ai SDK for Android enables you to talk to Kore.ai bots over a web socket. This repo also comes with the code for sample application that developers can modify according to their Bot configuration.

# Setting up

### Prerequisites
* Service to generate JWT (JSON Web Tokens)- this service will be used in the assertion function injected to obtain the connection.
* SDK bot credentials 
* Login to the Bots platform
	* Navigate to the Bot builder
	* Search and click on the bot 
	* Enable *Web / Mobile Client* channel against the bot as shown in the screen below.
		
	![Add bot to Web/Mobile Client channel](https://github.com/Koredotcom/android-kore-sdk/blob/master/channels.png)
	
	* create new or use existing SDK app to obtain client id and client secret
	
	![Obtain Client id and Client secret](https://github.com/Koredotcom/android-kore-sdk/blob/master/web-mobile-client-channel.png)

## Instructions

### Setup Bot Configuration
* Setting up following bot configuration.

 ```
//Initialize the bot with bot config
SDKConfig.initialize(
	botId, //It's mandatory field to setup. Ex: st-acecd91f-b009-5f3f-9c15-7249186d827d
	botName, // copy this value from Bot Builder -> Channels -> Web/Mobile SDK config  ex. "Demo Bot"
	clientId, // It's mandatory field to setup Ex: cs-5250bdc9-6bfe-5ece-92c9-ab54aa2d4285
	clientSecret, // It's mandatory field to setup Ex: Wibn3ULagYyq0J10LCndswYycHGLuIWbwHvTRSfLwhs=
	identity, // It's mandatory field to setup. User identity - this should represent the subject for JWT token that could be an email or phone number in case of known 		     user. In case of anonymous user, this can be a randomly generated unique id.
	jwtToken, // It's mandatory field to setup. This value should be **empty** if BotSdk itself create the Jwt Token. Otherwise need to provide respective JwtToken.
	serverUrl, // It's mandatory field to setup. Ex: https://xxx.kore.ai/
	brandingUrl, // It's mandatory field to setup. It can be same as serverUrl. Based on your usage Ex: https://xxx.kore.ai/
	jwtServerUrl If you want to use your own JwtToken creation url then provide respective url. Otherwise **empty**. e.g. https://jwt-token-server.example.com/
);

//Set isWebHook enable or not
SDKConfig.isWebHook(false);

//Flag to show the bot icon beside the bot response
SDKConfig.setIsShowIcon(true);

//Flag to show the bot icon in top position or bottom of the bot response
SDKConfig.setIsShowIconTop(true);

//Flag to show timestamp of each bot and user messages
SDKConfig.setIsTimeStampsRequired(true);

```

## Running the Demo app
*	Download or clone the repository.
*	Import the project.
*	Run the app.
  
## Steps to integrate BotSdk with UI through gradle implementation

#### 1. Add below snippet in app/build.gradle under dependencies
```
implementation 'com.github.Koredotcom:android-kore-sdk:0.0.5'
```
#### 2. You can initialize the bot by providing the bot config as mentioned above at **Setup Bot Configuration**

#### 3. You can navigate to the bot chat window through Intent as below snippet
```
Intent intent = new Intent(getApplicationContext(), BotChatActivity.class);
Bundle bundle = new Bundle();
//This should not be null
bundle.putBoolean(BundleUtils.SHOW_PROFILE_PIC, false);
bundle.putString(BundleUtils.BOT_NAME_INITIALS,"B");
intent.putExtras(bundle);
startActivity(intent);

```
#### 4. You can have your customized templates(new template or replace existing template with your own template) an fragments without touching the SDK code.

Please refer the sample app for adding custom templates into sdk and add custom templates and fragments as follows.

```
//Inject the custom template like below
SDKConfig.setCustomTemplateViewHolder("link", LinkTemplateHolder.class);

// To add your custom content fragment instead of using existing
SDKConfig.addCustomContentFragment(new CustomContentFragment());

// To add your custom footer fragment instead of using existing
SDKConfig.addCustomFooterFragment(new CustomFooterFragment());
```

## Steps to integrate BotSdk withoutUI(Only Bot communication) through gradle implementation

#### 1. Add below snippet in project/build.gradle
   
```
maven { url 'https://www.jitpack.io' }
```
#### 2. Add below snippet in app/build.gradle under dependencies
```
implementation 'com.github.Koredotcom.android-kore-sdk:korebotsdklib:0.0.5'
```
#### 3. You can change the bot config as mentioned above at **Setup Bot Configuration**
   
#### 4. You can intialize the Bot as below
   
```
//Initiating BotClient
BotClient botClient = new BotClient(this);
SocketConnectionListener socketConnectionListener = new SocketConnectionListener() {
    @Override
    public void onOpen() {
	// Executes when socket connection success
    }
    @Override
    public void onClose(WebSocket.WebSocketConnectionObserver.WebSocketCloseNotification code, String reason) {
	// Executes when socket connection closed.
    }
    @Override
    public void onTextMessage(String payload) {
	// Executes when a message is received from the bot.
    }
    @Override
    public void onRawTextMessage(byte[] payload) {
        // Execites when Text message payload received as raw UTF-8 or null (empty payload).
    }
    @Override
    public void onBinaryMessage(byte[] payload) {
	// Executes when Binary message payload or null (empty payload).
    }
	@Override
    public void refreshJwtToken() {
	// Executes when Jwt token is refreshed.
    }
	@Override
    public void onReconnectStopped(String reconnectionStopped) {
	// Executes when reconnection attempts exceeded.
    }
};

//Initiating bot connection once connected callbacks will be fired on respective actions
botClient.connectAsAnonymousUser(jwt,SDKConfiguration.Client.bot_name,SDKConfiguration.Client.bot_id, socketConnectionListener);
```
#### 5. Send message to bot as below
```
botClient.sendMessage("Your message to bot");
botClient.sendMessage("Your message to bot", attachmentsList);   // attachmentsList is having the list of attachments to send.
```

#### 6. Subscribe to push notifications
```
PushNotificationRegistrar pushNotificationRegistrar =  new PushNotification(requestListener);
pushNotificationRegistrar.registerPushNotification(Context context, String userId, String accessToken);
```
#### 7. Unsubscribe to push notifications
```
PushNotificationRegistrar pushNotificationRegistrar =  new PushNotification(requestListener);
pushNotificationRegistrar.unsubscribePushNotification(Context context, String accessToken);
```
#### 8. Disconnect
Invoke to disconnect previous socket connection upon closing Activity/Fragment or upon destroying view.
```
botClient.disconnect();
```
# How to enable API based (webhook channel) message communication
```
// Webhook is nothing but api communication. If webhook is enabled then api's will be used for the bot communication. Otherwise it will be sockect communication.  
// If you pass true it will use webhook. Otherwise pass false.
SDKConfig.isWebHook(true); 
```

# How to enable the Widget panel and do widget bot configuration
To enable the widget panel and display then please use and following statements.
```
//Flag to show widget panel
SDKConfig.enableWidgetPanel(true);

SDKConfig.setWidgetBotConfig(
	botId, //It's mandatory field to setup. Ex: st-acecd91f-b009-5f3f-9c15-7249186d827d
	botName, // copy this value from Bot Builder -> Channels -> Web/Mobile SDK config  ex. "Demo Bot"
	clientId, // It's mandatory field to setup Ex: cs-5250bdc9-6bfe-5ece-92c9-ab54aa2d4285
	clientSecret, // It's mandatory field to setup Ex: Wibn3ULagYyq0J10LCndswYycHGLuIWbwHvTRSfLwhs=
	identity, // It's mandatory field to setup. User identity - this should represent the subject for JWT token that could be an email or phone number in case of known 		     user. In case of anonymous user, this can be a randomly generated unique id.
	serverUrl, // It's mandatory field to setup. Ex: https://xxx.kore.ai/
	jwtServerUrl If you want to use your own JwtToken creation url then provide respective url. Otherwise **empty**. e.g. https://jwt-token-server.example.com/
);
```
	
License
Copyright © Kore.ai, Inc. MIT License; see LICENSE for further details.
