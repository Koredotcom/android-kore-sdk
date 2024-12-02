# Kore.ai SDK
Kore.ai offers Bots SDKs as a set of platform-specific client libraries that provide a quick and convenient way to integrate Kore Bots chat capability into custom applications.

With just few a lines of code, you can embed our Kore.ai chat widget into your applications to enable end-users to interact with your applications using Natural Language.

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

## Integrating Kore Bot SDK into Exsisting Application with UI

Need to copy "Korebot" and "Korebotsdklib" modules into parent application as dependency modules

Need to include the dependency modules in setting.gradle file of parent application as below
```
include ':korebot', ':korebotsdklib'
```
Add the modules as dependency in build.gradle file of the parent application as below and do the gradle sync
```
 implementation(project(':korebot')) {
        exclude module: 'support-v4'
    }

implementation(project(':korebotsdklib')) {
	exclude module: 'support-v4'
   }
```

### Setup Bot Configuration with UI
* Setting up following in Parent application class.java file

1) Initialize the bot with bot config
```
SDKConfig.initialize(
	botId, //It's mandatory field to setup. Ex: st-acecd91f-xxxx-xxxx-xxxx-xxxxxxxxxxxx
	botName, // copy this value from Bot Builder -> Channels -> Web/Mobile SDK config  ex. "Demo Bot"
	clientId, // It's mandatory field to setup Ex: cs-xxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
	clientSecret, // It's mandatory field to setup Ex: Wxxxxxxxxxxxxxxxxxxxxxxxx=
	identity, // It's mandatory field to setup. User identity - this should represent the subject for JWT token that could be an email or phone number in case of known 		     user. In case of anonymous user, this can be a randomly generated unique id.
	jwtToken, // It's mandatory field to setup. This value should be **empty** if BotSdk itself create the Jwt Token. Otherwise need to provide respective JwtToken.
	serverUrl, // It's mandatory field to setup. Ex: https://xxx.kore.ai/
	brandingUrl, // It's mandatory field to setup. It can be same as serverUrl. Based on your usage Ex: https://xxx.kore.ai/
	jwtServerUrl If you want to use your own JwtToken creation url then provide respective url. Otherwise **empty**. e.g. https://jwt-token-server.example.com/
);
```

2) You can pass the custom data to the bot by using this method. Can get sample format from the mentioned method under BotsSDK/app/src/main/java/com/kore/korebot/MainActivity.java
```
 SDKConfig.setCustomData(getCustomData());
```

3) You can set query parameters to the socket url by using this method. Can get sample format from the mentioned method under BotsSDK/app/src/main/java/com/kore/korebot/MainActivity.java
```
SDKConfig.setQueryParams(getQueryParams());
```

4) Set isWebHook enable or not
```
SDKConfig.isWebHook(false);
```

5) Flag to show the bot icon beside the bot response
```
SDKConfig.setIsShowIcon(true);
```

6) Flag to show the bot icon in top position or bottom of the bot response
```
SDKConfig.setIsShowIconTop(true);
```

7) Flag to show timestamp of each bot and user messages
```
SDKConfig.setIsTimeStampsRequired(true);
```

8) Navigate to the Bot window like below
```
Intent intent = new Intent(getApplicationContext(), NewBotChatActivity.class);
Bundle bundle = new Bundle();
//This should not be null
bundle.putBoolean(BundleUtils.SHOW_PROFILE_PIC, false);
bundle.putString(BundleUtils.BOT_NAME_INITIALS,"B");
intent.putExtras(bundle);
startActivity(intent);
```

Public repository sample application with UI integration
[https://github.com/DocsInternal-Kore/korebot-sdk-lib/tree/master](https://github.com/Koredotcom/android-kore-sdk/tree/master/BotsSDK)

## Integrating Kore Bot SDK into Exsisting Application without UI
Need to copy "Korebotsdklib" modules into parent application as dependency modules

Need to include the dependency modules in setting.gradle file of parent application as below
```
include ':korebotsdklib'
```
Add the modules as dependency in build.gradle file of the parent application as below and do the gradle sync
```
 implementation(project(':korebotsdklib')) {
	exclude module: 'support-v4'
   }
```
### Setup Bot Configuration

Create a class.java file which implements SocketConnectionListener 

Once the parent .java implements SocketConnectionListner below mentioned CallBack methods will be overided into the same file
```
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
```

```
//Initiating BotClient
BotClient botClient = new BotClient(this);

//Need to update below details in "SDKConfiguration.java" file
public static String client_id = "PLEASE_ENTER_YOUR_CLIENT_ID";
public static String client_secret = "PLEASE_ENTER_YOUR_CLIENT_SECRET";
public static String identity = "PLEASE_ENTERIDENTITY";
public static String bot_name = "PLEASE ENTER_BOT_NAME";
public static String bot_id = "PLEASEENTER_BOT_NAME";

//Initiating bot connection once connected callbacks will be fired on respective actions
botClient.connectAsAnonymousUser(jwt,SDKConfiguration.Client.bot_name,SDKConfiguration.Client.bot_id, Class.this);

//Send message to bot as below
botClient.sendMessage("Your message to bot");
botClient.sendMessage("Your message to bot", attachmentsList);   // attachmentsList is having the list of attachments to send.
```
Public repository sample application without UI integration
https://github.com/DocsInternal-Kore/korebot-sdk-lib/tree/master
  
## Steps to integrate BotSdk with UI through gradle implementation

#### 1. Add below snippet in app/build.gradle under dependencies
```
implementation 'com.github.Koredotcom:android-kore-sdk:0.0.5'
```
#### 2. You can initialize the bot by providing the bot config as mentioned above at **"Setup Bot Configuration with UI"**

#### 3. You can navigate to the bot chat window through Intent as below snippet
```
Intent intent = new Intent(getApplicationContext(), NewBotChatActivity.class);
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
#### 3. You can change the bot config as mentioned above at **"Setup Bot Configuration"**   

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
	
License
Copyright © Kore.ai, Inc. MIT License; see LICENSE for further details.
