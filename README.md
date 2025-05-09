# Kore.ai Android SDK

Kore.ai provides platform-specific SDKs to help developers quickly integrate chatbot capabilities into custom applications.  
With just a few lines of code, you can embed the Kore.ai chat widget and enable natural language interactions with your users.

---

## Overview

The **Kore.ai Android SDK** enables seamless communication with Kore.ai bots over WebSocket.  
This repository also includes a sample application that you can customize based on your bot configuration.

---

## Getting Started

### Prerequisites

- A service to generate JWT tokens (used in the SDK’s authentication flow).
- Bot SDK credentials:
  - Log in to the Kore.ai Bots platform.
  - Navigate to your bot in the **Bot Builder**.
  - Enable the **Web/Mobile Client** channel.
  - Create or use an existing SDK app to retrieve your **Client ID** and **Client Secret**.

**Screenshots for reference:**

![Enable webclient](https://github.com/user-attachments/assets/4d2263a2-bf8a-4a02-8b60-5a8b4c8da643)
_Enable Web/Mobile Client Channel_

![Credentials](https://github.com/user-attachments/assets/0824b163-f08e-42fc-954a-b46a5f8d5ecb)
_Obtain Client ID and Client Secret_

---

## Integration Options

### 1. Integrate with UI

#### Setup

1. Copy the `korebot` and `korebotsdklib` modules into your application.
2. Add them to `settings.gradle`:
   ```groovy
   include ':korebot', ':korebotsdklib'
   ```
3. Add dependencies in `build.gradle`:

   ```groovy
   implementation(project(':korebot')) {
       exclude module: 'support-v4'
   }

   implementation(project(':korebotsdklib')) {
       exclude module: 'support-v4'
   }
   ```

#### Bot Configuration

In your Application:

```java
SDKConfig.initialize(
    botId,
    botName,
    clientId,
    clientSecret,
    identity,
    jwtToken,
    serverUrl,
    brandingUrl,
    jwtServerUrl
);
```

Optional configurations:

```java
// To set custom data if you required
SDKConfig.setCustomData(getCustomData());

// To set query params if you required
SDKConfig.setQueryParams(getQueryParams());

// Flag to use the Webhook instead of socket then make it "true"
SDKConfig.isWebHook(false);

// Flag to show or hide icon for the bot chat conversation
SDKConfig.setIsShowIcon(true);

// Flag to show chat conversation icon at the top the view
SDKConfig.setIsShowIconTop(true);

// Flag to show or hide time stamp of each conversation message
SDKConfig.setIsTimeStampsRequired(true);

// Flag to show or hide action bar in GenericWebviewActivity
SDKConfig.setIsShowActionBar(true);

// Flag to show or hide attachments icon in footer fragment
SDKConfiguration.OverrideKoreConfig.showAttachment = true;

// Flag to show or hide Speech to text(ASR) icon in footer fragment
SDKConfiguration.OverrideKoreConfig.showASRMicroPhone = true;

Flag to show or hide Text to speech icon in footer fragment
SDKConfiguration.OverrideKoreConfig.showTextToSpeech = true;
```

To launch the chat activity:

```java
Intent intent = new Intent(getApplicationContext(), NewBotChatActivity.class);
Bundle bundle = new Bundle();
bundle.putBoolean(BundleUtils.SHOW_PROFILE_PIC, false);
bundle.putString(BundleUtils.BOT_NAME_INITIALS, "B");
intent.putExtras(bundle);
startActivity(intent);
```

**Sample App with UI Integration:**  
🔗 [Kore.ai Android SDK Sample](https://github.com/Koredotcom/android-kore-sdk/tree/master/BotsSDK)

---

### 2. Integrate Without UI

#### Setup

1. Copy only the `korebotsdklib` module into your project.
2. Add it to `settings.gradle`:
   ```groovy
   include ':korebotsdklib'
   ```
3. Add the module in `build.gradle`:
   ```groovy
   implementation(project(':korebotsdklib')) {
       exclude module: 'support-v4'
   }
   ```

#### Bot Client Integration

1. Implement `SocketConnectionListener` in your class.
2. Initialize the bot:
   ```java
   BotClient botClient = new BotClient(this);
   botClient.connectAsAnonymousUser(jwt, botName, botId, this);
   ```
3. Send messages to the bot:
   ```java
   botClient.sendMessage("Hello Bot");
   botClient.sendMessage("Hello Bot", attachmentsList);
   ```

**Sample App without UI:**  
🔗 [Headless SDK Sample](https://github.com/DocsInternal-Kore/korebot-sdk-lib/tree/master)

---

## Gradle Integration

### With UI

1. Add this dependency:

   ```groovy
   implementation 'com.github.Koredotcom:android-kore-sdk:0.0.5'
   ```

2. Initialize the bot using the configuration method described in **Setup Bot Configuration with UI**.

3. Launch chat window:

   ```java
   Intent intent = new Intent(getApplicationContext(), NewBotChatActivity.class);
   Bundle bundle = new Bundle();
   bundle.putBoolean(BundleUtils.SHOW_PROFILE_PIC, false);
   bundle.putString(BundleUtils.BOT_NAME_INITIALS, "B");
   intent.putExtras(bundle);
   startActivity(intent);
   ```

4. Customize templates or fragments:

```java
   // To override existing template or add new template
   SDKConfig.setCustomTemplateViewHolder("link", LinkTemplateHolder.class);

   // To show your custom content fragment instead of existing
   SDKConfig.addCustomContentFragment(new CustomContentFragment());

   // To show your custom footer fragment instead of existing
   SDKConfig.addCustomFooterFragment(new CustomFooterFragment());

   // To show your custom header fragment instead of existing
   SDKConfig.addCustomHeaderFragment(new CustomHeaderFragment());
```

**Note**: Please go through the readme documents for [CustomTemplateInjection](https://github.com/SudheerJa-Kore/android-kore-sdk/blob/master/docs/CustomTemplateInjection.md) and [CustomFragmentInjection](https://github.com/SudheerJa-Kore/android-kore-sdk/blob/master/docs/CustomFragmentInjection.md)

---

### Without UI (Headless Bot)

1. Add JitPack to `project/build.gradle`:

   ```groovy
   maven { url 'https://www.jitpack.io' }
   ```

2. Add the SDK in `app/build.gradle`:

   ```groovy
   implementation 'com.github.Koredotcom.android-kore-sdk:korebotsdklib:0.0.5'
   ```

3. Setup the bot using the instructions under **Setup Bot Configuration**.

---

## Additional Features

### Push Notifications

**Subscribe:**

```java
PushNotificationRegistrar registrar = new PushNotification(requestListener);
registrar.registerPushNotification(context, userId, accessToken);
```

**Unsubscribe:**

```java
registrar.unsubscribePushNotification(context, accessToken);
```

---

### Disconnect

Call this to close the bot session:

```java
botClient.disconnect();
```

---

### Webhook Mode

To use API-based (Webhook) communication instead of WebSocket:

```java
SDKConfig.isWebHook(true);
```

---

## License

© Kore.ai, Inc.  
Licensed under the MIT License. See `LICENSE` file for details.
