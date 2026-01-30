# Kore.ai Android SDK

Kore.ai offers SDKs tailored for specific platforms to help developers quickly embed chatbot functionality into their custom applications.  
With minimal coding, you can integrate the Kore.ai chat widget and enable users to interact naturally via conversational UI.

---

## Overview

The **Kore.ai Android SDK** facilitates smooth communication with Kore.ai bots using WebSocket.  
This repository also contains a sample application that you can modify to fit your bot configuration needs.

---

## Getting Started

### Requirements

- A service for generating JWT tokens (used for authentication).
- Bot SDK credentials:
  - Log into the Kore.ai Bots platform.
  - Go to your bot project in the **Bot Builder**.
  - Enable the **Web/Mobile Client** channel.
  - Create or select an SDK app to get your **Client ID** and **Client Secret**.

**Reference Images:**

![Enable webclient](https://github.com/user-attachments/assets/4d2263a2-bf8a-4a02-8b60-5a8b4c8da643)  
_Enabling Web/Mobile Client Channel_

![Credentials](https://github.com/user-attachments/assets/26932a3e-71a4-48fd-9917-7f6ede3b6749)  
_Retrieving Client ID and Secret_

---

## Integration Methods

### 1. Integration with UI

#### Setup Steps

1. Copy `korebot` and `korebotsdklib` modules into your project.
2. Update `settings.gradle`:
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

#### Bot Initialization

Inside your application:

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
// Flag to show or hide Text to speech icon in footer fragment
SDKConfiguration.OverrideKoreConfig.showTextToSpeech = true;
```

To open the chat window:

```java
Intent intent = new Intent(getApplicationContext(), NewBotChatActivity.class);
Bundle bundle = new Bundle();
bundle.putBoolean(BundleUtils.SHOW_PROFILE_PIC, false);
bundle.putString(BundleUtils.BOT_NAME_INITIALS, "B");
intent.putExtras(bundle);
startActivity(intent);
```

📘 **Sample App with UI:**  
[Kore.ai Android SDK Sample](https://github.com/Koredotcom/android-kore-sdk/tree/master/BotsSDK)

---

### 2. Integration Without UI (Without UI)

#### Setup Steps

1. Only include the `korebotsdklib` module in your project.
2. Add this to `settings.gradle`:
   ```groovy
   include ':korebotsdklib'
   ```
3. Add dependency in `build.gradle`:
   ```groovy
   implementation(project(':korebotsdklib')) {
       exclude module: 'support-v4'
   }
   ```

#### Bot Integration

1. Implement the `SocketConnectionListener` interface.
2. Connect the bot:
   ```java
   BotClient botClient = new BotClient(this);
   botClient.connectAsAnonymousUser(jwt, botName, botId, this);
   ```
3. Send messages:
   ```java
   botClient.sendMessage("Hello Bot");
   botClient.sendMessage("Hello Bot", attachmentsList);
   ```

📘 **Sample App without UI:**  
[Headless SDK Sample](https://github.com/DocsInternal-Kore/korebot-sdk-lib/tree/master)

---

## Gradle Integration Guide

### With UI

1. Add the dependency:
   ```groovy
   implementation 'com.github.Koredotcom:android-kore-sdk:10.14.2'
   ```
2. Configure the SDK as described above.
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

🔗 See [CustomTemplateInjection Guide](https://github.com/SudheerJa-Kore/android-kore-sdk/blob/master/docs/CustomTemplateInjection.md)  
🔗 See [CustomFragmentInjection Guide](https://github.com/SudheerJa-Kore/android-kore-sdk/blob/master/docs/CustomFragmentInjection.md)

---

### Without UI

1. Add JitPack to `project/build.gradle`:
   ```groovy
   maven { url 'https://www.jitpack.io' }
   ```
2. Add dependency in `app/build.gradle`:
   ```groovy
   implementation 'com.github.Koredotcom.android-kore-sdk:korebotsdklib:10.14.2'
   ```
3. Configure your bot as described previously.

---

## Extra Features

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

### Disconnect from Bot

To terminate the bot session:

```java
botClient.disconnect();
```

---

### Using Webhook Mode

Switch to API/Webhook-based communication:

```java
SDKConfig.isWebHook(true);
```

---

## License

© Kore.ai, Inc.  
Licensed under the MIT License. See `LICENSE` file for details.
