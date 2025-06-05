## Kore SDK for Android

Kore offers a suite of platform-specific SDKs, including this Android SDK, designed to streamline the integration of Kore Bots' chat capabilities into your custom applications. These SDKs provide convenient client libraries, allowing you to embed our Kore chat widget with just a few lines of code. This empowers your end-users to interact with your applications using natural language.

## Kore Android SDK for Developers

This repository contains the Kore SDK for Android, enabling seamless communication with Kore Bots over a WebSocket connection. Additionally, it includes sample applications that developers can readily adapt based on their specific Bot configurations.

## Setting Up

### Prerequisites

Before you begin, ensure you have the following:

**JWT Generation Service**: A service capable of generating JSON Web Tokens (JWTs). This service will be used by an injected assertion function to establish a connection with the Bot.
**SDK App Credentials**: Client ID and Client Secret for your SDK application.
**Access to Kore Bots Platform**: You need to be logged into the Kore Bots platform.

### Bot Channel Configuration

**Navigate to Bot Builder**: Once logged in, go to the Bot builder section.

**Select Your Bot**: Find and click on the specific bot you want to integrate.

**Enable Web/Mobile Client Channel**: Enable the Web / Mobile Client channel for your bot as illustrated below:

![Enable Web client channel](https://github.com/user-attachments/assets/37c52043-ed94-48a8-af07-2feadb3e532c)

**Action**: Add your bot to the Web/Mobile Client channel.

**Obtain SDK Bot Credentials**: Create a new SDK app or use an existing one to retrieve the Client ID and Client Secret.

![Bot credentials](https://github.com/user-attachments/assets/26932a3e-71a4-48fd-9917-7f6ede3b6749)


## Instructions

### Configuration Changes

The Kore SDK for Android provides the botclient library for communicating with your bot and the ui libraries for displaying content and interactive elements. You can leverage the predefined templates, activities and fragments provided by these libraries.

To integrate the SDK, create your own Android application and add the following dependency to your app module's build.gradle file:

```gradle
implementation 'com.github.Koredotcom.android-kore-sdk:kotlin_11.2.0'
```

#### Configuration - 1 (Bot Client Initialization):

Configure the core bot connection details using the BotConfigModel and initialize the SDK:

```kotlin
import com.kore.koreandroidsdk.configuration.BotConfigModel
import com.kore.koreandroidsdk.configuration.SDKConfig

val botConfigModel = BotConfigModel(
    "<botname>",                     // Name of your bot
    "<bot_id>",                      // Unique ID of your bot
    "<bot_client_id>",               // Client ID of your SDK app
    "<bot_client_secret>",           // Client Secret of your SDK app
    "<bot_base_url>",                // Base URL of the Kore.ai platform (e.g., "[https://platform.kore.ai/](https://platform.kore.ai/)")
    "<identity>",                    // Unique identifier for each device. **Crucially, do not use UUID.randomUUID().toString()**. Use a device-specific ID to avoid session contamination across multiple devices.
    <isWebHook -> true/false>,       // Set to `true` if you intend to use webhook-based communication; otherwise, `false`.
    "<jwt_token_url>"               // Optional: URL for your custom JWT token generation service.
)

SDKConfig.initialize(botConfigModel)
```

## Running the Demo App

You can quickly test the SDK by running the provided demo application. Follow these steps:

Create a New Application: Create a new Android Studio project.

**Add Library Dependency**: Add the SDK dependency to your app's build.gradle file as shown below:

```gradle
implementation 'com.github.Koredotcom.android-kore-sdk:kotlin_11.2.0'
```

**Configure Bot Credentials**: Implement the configuration steps mentioned in **Config-1** within your application.

**Navigate to Chat Window**: You can open the bot chat window using an Intent

```kotlin
val intent = Intent(this, BotChatActivity::class.java)
startActivity(intent)
```

**Custom Templates**: The SDK allows you to implement your own custom message templates without modifying the SDK code. Refer to the sample application for examples.

## Integrating BotSDK for Customizations

Here's how to integrate the BotSDK using Gradle to enable UI customizations:

### Step 1: Add JitPack Repository to Project-Level `build.gradle`

```gradle
maven { url '[https://www.jitpack.io](https://www.jitpack.io)' }
```

### Step 2: Add SDK Dependency to App-Level build.gradle

```gradle
implementation 'com.github.Koredotcom.android-kore-sdk:kotlin_11.2.0'
```

### Step 3: Configure Bot credentials
Modify the `BotConfigModel` with your bot's specific credentials and URLs:

```kotlin
import com.kore.koreandroidsdk.configuration.BotConfigModel
import com.kore.koreandroidsdk.configuration.SDKConfig

val botConfigModel = BotConfigModel(
    "SDK Demo",
    "st-f59fda8f-e42c-5c6a-bc55-3395c109862a",
    "cs-8fa81912-0b49-544a-848e-1ce84e7d2df6",
    "DnY4BIXBR0Ytmvdb3yI3Lvfri/iDc/UOsxY2tChs7SY=",
    "[https://platform.kore.ai/](https://platform.kore.ai/)",
    "123456789078643234567",
    false,
    "[https://mk2r2rmj21.execute-api.us-east-1.amazonaws.com/dev/users/sts](https://mk2r2rmj21.execute-api.us-east-1.amazonaws.com/dev/users/sts)",
    false
)

SDKConfig.initialize(botConfigModel)
```

### Step 4: Initialize and Set BotClient Listener
Get an instance of the `BotClient` and set a listener to handle bot responses, connection state changes, and other events:

```kotlin
private val botClient: BotClient = BotClient.getInstance()

botClient.setListener(object : BotConnectionListener {
    override fun onBotResponse(response: String?) {
        // Handle bot response here
    }

    override fun onConnectionStateChanged(state: ConnectionState, isReconnection: Boolean) {
        // Handle bot connection state changes
    }

    override fun onBotRequest(code: BotRequestState, botRequest: BotRequest) {
        // Handle when a request is sent to the bot
    }

    override suspend fun onAccessTokenGenerated(token: String) {
        // Handle the generated access token
    }
})
```

### Step 5: Connect to the Bot

Use one of the following APIs to establish a connection with the bot:

```kotlin
botClient.connectToBot(context = context, isFirstTime = true) // Connect using the default JWT token generation.
```

```kotlin
val headers = HashMap<String, Any>()
val body = HashMap<String, Any>()
// Populate headers and body for your custom JWT token URL

botClient.connecToBot(context = context, isFirstTime = true, jwtToken = "your_jwt_token") // Connect using a pre-generated JWT token.
```

### Step 6: Send Messages to the Bot

Use the following APIs to send text or attachment messages to the bot:

```kotlin
botClient.sendMessage(msg = "Hello Bot", payload = null) // Send a text message where the display and payload are the same.
botClient.sendMessage(msg = "Show me details", payload = "get_details") // Send a text message with a different display and payload.
```

```kotlin
val attachments = listOf<Map<String, *>>(
    mapOf("name" to "document.pdf") // Example attachment
)
botClient.sendMessage(msg = "Attached file", payload = null, attachments = attachments) // Send a message with attachments.
```

### Step 7: Disconnect from the Bot

Call this method to close the WebSocket connection when your Activity or Fragment is being closed or destroyed:

```kotlin
botClient.disconnectBot()
```

### Step 8: Create Custom UI Templates and Chat Window

The SDK provides predefined UI templates that you can use directly. However, you can also create your own custom templates or override existing ones to achieve a unique look and feel. Additionally, you can customize the entire chat window UI by creating custom Fragments that extend the SDK's base fragment classes. Refer to the sample application and document for detailed examples of UI customization.

**Note**: Please refer the documents for [CustomTemplateInjection](https://github.com/Koredotcom/android-kore-sdk/blob/kotlin_botsdk_ui/docs/CustomTemplateInjection.md) and [CustomFragmentInjection](https://github.com/Koredotcom/android-kore-sdk/blob/kotlin_botsdk_ui/docs/CustomFragmentInjection.md) how to customize and inject to BotSdk.

## Enabling API-Based (Webhook Channel) Communication

**Enable Webhook**: Set isWebhook = true in the BotConfigModel during bot configuration (**Config-1**).
**Follow Integration Steps**: Implement **Config-1** and **Step 1** through **Step 8** as described above.

## License

Copyright © Kore, Inc. MIT License; see LICENSE for further details.
