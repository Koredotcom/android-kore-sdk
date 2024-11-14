## Kore SDK
Kore offers Bots SDKs as a set of platform-specific client libraries that provide a quick and convenient way to integrate Kore Bots chat capability into custom applications.

With just a few lines of code, you can embed our Kore chat widget into your applications to enable end-users to interact with your applications using Natural Language.

## Kore Android SDK for developers

Kore SDK for Android enables you to talk to Kore bots over a web socket. This repo also comes with the code for sample applications that developers can modify according to their Bot configuration.

# Setting up

## Prerequisites
 Service to generate JWT (JSON Web Tokens)- this service will be used in the assertion function injected to obtain the connection.
 SDK app credentials 
 Login to the Bots platform
Navigate to the Bot builder
Search and click on the bot 
Enable *Web / Mobile Client* channel against the bot as shown in the screen below.
		Add bot to Web/Mobile Client  channel
create new or use existing SDK app to obtain client id and client secret Obtain Client id and Client secret

## Instructions

# Configuration changes
Kore is providing botclient library to communicate with the bot and ui to display content.
"BotClient" -> Which can be used to communicate with the bot.

Create your own app and add the dependency(**implementation 'com.github.Koredotcom.android-kore-sdk:kotlin_lib_v3/11.0.1'**) for botclient and create following classes in your app module.

Following are the steps needed to follow to integrate the libraries.
To use these libraries we must configure the bot credentials as follows.

#### Config-1:
```
    val botConfigModel = BotConfigModel(
        "<botname>",
        "<bot_id>",
        "<bot_client_id>",
        "<bot_client_secret>",
        "<bot_base_url>",
        "<identity>",  -> Identity should be **unique for each device**. Don't use "UUID.randomUUID().toString()". It's better to use your device id. If we use the same identity for multiple devices then we will face the session contamination issue.
        <isWebHook -> true/false>, -> if You want to use webhook then put "true" otherwise "false".
        "<jwt_token_url>", -> Jwt token generation url(If you want to use your own jwt token url).
        false
    )

 SDKConfig.initialize(botConfigModel)

```

# Running the Demo app
*	Create a new application.
*	Add library dependency in your gradle file as follows.
*	Do the configuration as mentioned above.
  
### steps to follow for demo app

**1**. Add below snippet in project/build.gradle
```
 maven { url 'https://www.jitpack.io' }
```
**2**. Add below snippet in app/build.gradle under dependencies
```
implementation 'com.github.Koredotcom.android-kore-sdk:kotlin_lib_v3/11.0.1'
```
**3**. You can initialize the bot by providing the bot config like mentioned in **Config-1**


**4**. You can have your customized templates without touching the SDK code. Please check the sample app.


### How to integrate BotSdk through gradle implementation to customize

### Step-1: Add below snippet in project/build.gradle
```   
maven { url 'https://www.jitpack.io' }
```
### Step-2: Add below snippet in app/build.gradle under dependencies
```
implementation 'com.github.Koredotcom.android-kore-sdk:kotlin_lib_v3/11.0.1'
```
### Step-3: You can change the bot config and widget config like below
```

val botConfigModel = BotConfigModel(
        "SDK Demo",
        "st-f59fda8f-e42c-5c6a-bc55-3395c109862a",
        "cs-8fa81912-0b49-544a-848e-1ce84e7d2df6",
        "DnY4BIXBR0Ytmvdb3yI3Lvfri/iDc/UOsxY2tChs7SY=",
        "https://platform.kore.ai/",
        "123456789078643234567",
        false,
        "https://mk2r2rmj21.execute-api.us-east-1.amazonaws.com/dev/users/sts",
        false
    )

 SDKConfig.initialize(botConfigModel)

```

### Step-4: You can initialize and Set the callback listener for BotClient class.

```
private val botClient: BotClient = BotClient.getInstance()

botClient.setListener(object : BotConnectionListener {
	
		// This will trigger when received a response bot.
    override fun onBotResponse(response: String?) {
		}
		
		// This will trigger when bot connection state is changed.
		override fun onConnectionStateChanged(state: ConnectionState, isReconnection: Boolean) {
		}
		
		// This will trigger once request(utterance) is sent to Bot.
		override fun onBotRequest(code: BotRequestState, botRequest: BotRequest) {
		}
		
		// This will trigger when a JWT token is created.
		override suspend fun onJwtTokenGenerated(token: String) {
		}
```

### Step-5: Connect to Bot using the following apis.

```
botClient.connectToBot(isFirstTime: Boolean) -> This api is to connect bot using default jwt token generation mechanism.
```
```
botClient.connectToBot(isFirstTime: Boolean, headers: HashMap<String, Any>, body: HashMap<String, Any>) -> This api is to connect bot using own Jwt token generation url. Important note is here "headers" must not be null.
```
```
botClient.connecToBot(isFirstTime: Boolean, jwtToken: String) -> This api is to connect bot using its own Jwt token which is created separately.
```

### Step-6: Send a message to Bot.

```
botClient.sendMessage(msg: String, payload: String?)

msg -> Message to display in the chat ui.
payload -> message to send to the bot.
```

If "msg" and "payload" are same then pass "msg" param as string and "payload" as null.

Following Api is to send the message with attachments.

 ```
botClient.sendAttachmentMessage(msg: String, attachments: List<Map<String, *>>?)

msg -> Message to display in the chat ui.
attachments -> List of attachment file names which are uploaded to server.
```

### Step-7: Disconnect the bot:
Invoke to disconnect previous socket connection upon closing Activity/Fragment or upon destroying view based on requirement.
```
botClient.disconnectBot()
```

### How to enable API based (webhook channel) message communication

1. Enable the webhook channel by setting **isWebhook = true** in **Config-1** in bot configuration.
	
2. Follow Config-1 and Step-1 to Step-7.

### License
Copyright © Kore, Inc. MIT License; see LICENSE for further details.

