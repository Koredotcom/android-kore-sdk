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

## SDK Configuration Reference

The sample app's chat options, query parameters, custom data and local branding now live in [`SDKConfiguration.java`](BotsSDK/korebotsdklib/src/main/java/kore/botssdk/net/SDKConfiguration.java), using the values previously assigned in `MainActivity`. **Bot configuration and `SDKConfig.initialize(...)` remain in [`MainActivity.java`](BotsSDK/app/src/main/java/com/kore/korebot/MainActivity.java)**: bot ID/name, client ID/secret, identity, JWT and server/branding/JWT URLs are still configured there. Edit the centralized behavior defaults when building from source, or use the existing `SDKConfig` setters and public fields before opening chat. `MainActivity` also applies the configured language and registers its callback and custom template.

The behavior defaults below are this repository's current defaults; connection values are supplied by MainActivity during initialization. A host application can override them. Remote branding can also update the attachment, microphone, speaker, emoji and history options when the corresponding branding settings are enabled. Changing a UI flag after a view is already displayed may require reopening/rebinding that view.

### Connection and authentication

`Client`, `JWTServer` and `Server` below are nested classes of `SDKConfiguration`. MainActivity supplies the bot, credential, identity, JWT and endpoint values through SDKConfig.initialize(...). SDKConfiguration retains its placeholder defaults until initialization; the table lists the sample values after initialization where applicable. Credentials are not repeated in this reference.

| Setting | Current default | Purpose |
| --- | --- | --- |
| `Client.bot_id` | Existing sample bot ID | Identifies the bot used by authentication and messaging. |
| `Client.bot_name` | `Yettel Chat Window` | Bot display name and the name used during connection. |
| `Client.client_id` | Existing sample client ID | Web/Mobile Client channel app ID; required when the SDK generates a JWT. |
| `Client.client_secret` | Existing sample client secret | Channel app secret used with the client ID for token generation. |
| `Client.identity` | `IDENTITY@kore.com` | End-user identity associated with authentication and the session. |
| `JWTServer.jwt_token` | Empty | An existing JWT can be supplied using `JWTServer.setJwt_token(...)`; empty selects token generation using client credentials. |
| `JWTServer.JWT_SERVER_URL` | Existing sample JWT endpoint | Token-generation service URL, set using `JWTServer.setJwtServerUrl(...)`. |
| `Server.SERVER_URL` | Platform URL configured in MainActivity | Kore.ai platform base URL used by bot APIs and authentication. |
| `Server.Branding_SERVER_URL` | Branding URL configured in MainActivity | Base URL used to retrieve bot appearance and branding. |
| `Client.isWebHook` | `false` | `true` uses HTTP webhook messaging; `false` uses WebSocket messaging. Also exposed by `SDKConfig.isWebHook(...)`. |
| `Client.connection_mode` | Empty | Optional WebSocket `ConnectionMode` value. Empty keeps normal connection behavior. |
| `Client.connection_mode_on_reconnect` | `false` | `true` reuses the configured connection mode during reconnects; `false` uses `Reconnect` when a custom mode is configured. |
| `Client.history_on_network_resume` | `true` | Retrieves missed conversation history after network recovery. |
| `Client.enable_ack_delivery` | `false` | Enables acknowledgement of incoming bot messages when supported by the connection. |
| `Server.queryParams` | `q1=true`, `q2=4`, `q3="connect"` | Additional WebSocket URL parameters. These are sample application keys, not SDK behavior flags; replace them or supply a map using `SDKConfig.setQueryParams(...)`. |
| `Server.customData` | `jwt_token` set from the configured token | Bot-specific connection data. `update_custom_data_to_user_message` also includes it on outgoing user messages. Replace or extend it using `SDKConfig.setCustomData(...)`; the initial token entry is a snapshot, so update the map if the token changes later. |
| `Server.notificationDeviceId` | Empty | Device token/identifier for push registration. Set with `SDKConfig.setDeviceId(...)`. An empty value skips automatic registration. |
| `Server.TTS_WS_URL` | `wss://speech.kore.ai/tts/ws` | Text-to-speech service endpoint. |
| `Server.botStatusListener` | Registered by the sample Activity | Receives connection, message, deep-link and session callbacks. Set with `SDKConfig.setBotStatusUpdateListener(...)`. |
| `Server.notificationModel` | Populated at runtime | Holds push user/access-token/device details used for registration and cleanup. |

Keep `SDKConfig.initialize(...)` in MainActivity before opening chat. It copies the Activity's bot, credentials, identity, JWT and endpoint values into the SDK configuration used by the connection.

### Chat behavior flags

All settings in this table belong to `SDKConfiguration.OverrideKoreConfig`.

| Setting | Default | Purpose and behavior |
| --- | --- | --- |
| `isEmojiShortcutEnable` | `false` | Converts supported emoji shortcuts in bot text to emoji characters when enabled. |
| `typing_indicator_timeout` | `10000` ms | Stored typing-indicator timeout, which remote branding can populate. The current renderer does not directly consume this setting. |
| `history_enable` | `true` | Allows the SDK to retrieve earlier chat messages. |
| `history_batch_size` | `10` | Number of recent messages requested in each history batch. |
| `paginated_scroll_enable` | `true` | Allows loading older messages when scrolling upward. |
| `paginated_scroll_batch_size` | `10` | Number of older messages requested per scrolling history batch. |
| `paginated_scroll_loading_label` | `Loading old messages` | Stored history loading-label value, which remote branding can populate. The current renderer does not directly consume this setting. |
| `showIconTop` | `false` | Places the bot avatar at the top of a response when `true`, or at the bottom when `false`. Requires `BubbleColors.showIcon`. |
| `showAttachment` | `true` | Shows the composer attachment control for supported files. |
| `showASRMicroPhone` | `true` | Shows the speech-to-text microphone control. |
| `showTextToSpeech` | `true` | Shows the speaker control to read bot text aloud. |
| `showHamburgerMenu` | `false` | Menu visibility option used by compatible/custom footer implementations, including the sample custom footer. |
| `history_initial_call` | `false` | Requests history on the initial connection when `history_enable` is also enabled. |
| `disable_action_bar` | `true` | Hides the Android action bar in the SDK chat Activity. This is separate from the chat header. |
| `disable_alert_on_max_reconnection` | `true` | Suppresses the alert after maximum reconnect attempts. It does not control custom data or reconnect attempts themselves. |
| `update_custom_data_to_user_message` | `true` | Adds `Server.customData` to every outgoing user message. |
| `showLocalNotification` | `false` | Posts a local notification for a new bot message while the chat is in the background. This is separate from push registration. |
| `reconnectionBySDK` | `false` | Enables SDK WebSocket retry handling when `true`. With `false`, the host manages recovery as needed. |
| `sendAllDeepLink` | `false` | With a registered listener, `true` hands URL clicks to `BotStatusListener.onDeepLinkClicked` and minimizes the SDK chat Activity; `false` uses the SDK web view. |
| `enableButtonClicksOnAnyPosition` | `false` | Allows button-template submissions from older RecyclerView messages when `true`; `false` retains the last-item submission restriction. |
| `default_notifications` | `true` | Lets the SDK manage push registration and cleanup when device details are available. With `false`, the host manages push lifecycle, as demonstrated by the sample Activity. |
| `showAnsweredByAI` | `true` | Shows the Answered by AI label on supported bot responses. |
| `showHeader` | `true` | Shows the SDK chat header. Also exposed by `SDKConfig.setIsShowHeader(...)`. |
| `showHeaderMinimize` | `true` | Shows the chat header minimize control. Also exposed by `SDKConfig.showHeaderMinimize(...)`. |
| `updateStatusBarColor` | `true` | Matches the Android status bar to the chat header background. Also exposed by `SDKConfig.setIsUpdateStatusBarColor(...)`. |

Button templates always disable **all buttons in that template** after the first payload/title submission, regardless of `enableButtonClicksOnAnyPosition`. This applies to both button-template renderers. The state survives RecyclerView rebinding and scrolling for that message object; it is local interaction state and is not stored in bot JSON or restored from freshly fetched history. Rapid/reentrant clicks cannot submit a second payload. URL navigation alone does not consume the template. A synchronous send-callback exception releases the restriction so the user can retry; the send callback does not provide a server-delivery acknowledgement.

```java
// Let an unsubmitted button template respond from any message position.
SDKConfiguration.OverrideKoreConfig.enableButtonClicksOnAnyPosition = true;

// Route URL clicks to the registered host callback.
SDKConfiguration.OverrideKoreConfig.sendAllDeepLink = true;

// Show local notifications while the chat is in the background.
SDKConfiguration.OverrideKoreConfig.showLocalNotification = true;
```

### Language, timestamps, fonts and avatars

| Setting/API | Current default | Purpose |
| --- | --- | --- |
| `SDKConfiguration.appLanguage` | `en` | Language tag that the sample applies with `LangUtils.setAppLanguages(context, ...)` before launching chat. |
| `SDKConfiguration.DEFAULT_BUBBLE_DATE_FORMAT` | `MMM dd'{S}' 'at' h:mm a` | Initial message date pattern. `{S}` inserts the day suffix, such as `st`, `nd`, `rd` or `th`. The active format starts from this value in `BotResponse.BUBBLE_DATE_FORMAT`; use `SDKConfig.setBubbleDateFormat(...)` to change it at runtime. |
| `SDKConfiguration.isTimeStampsRequired()` / `SDKConfig.setIsTimeStampsRequired(...)` | `true` | Reads/controls timestamp visibility for user and bot messages. |
| `SDKConfiguration.getDeviceLocale()` / `setDeviceLocale(...)` | Set by the language helper | Locale used for message date formatting. |
| `SDKConfiguration.isApplyFontStyle()` | `true` | Enables SDK font styling in its custom text views. |
| `SDKConfig.setFontFamily(regular, semiBold, bold)` | No custom typefaces | Supplies app font resources to the SDK. |
| `BubbleColors.showIcon` | `true` | Shows the bot avatar beside responses; also exposed by `SDKConfig.setIsShowIcon(...)`. |
| `BubbleColors.setIcon(...)` | `-1` / no resource override | Supplies a local bot-avatar drawable resource. |
| `BubbleColors.setIcon_url(...)` | Empty | Supplies the bot-avatar image URL. |
| `SDKConfig.setAgentAvatar(drawable, url)` | No drawable; empty URL | Supplies the agent avatar using a drawable or image URL. |
| `SDKConfig.setIsShowActionBar(...)` | `true` | Controls the action bar in the SDK generic web view, independently of the chat header and `disable_action_bar`. |

### Branding and colors

These settings belong to `SDKConfiguration.BubbleColors`. Remote branding can update the supported palette and composer values.

| Setting | Default | Purpose |
| --- | --- | --- |
| `enableLocalBranding` | `false` | Enables local appearance overrides when `true`; otherwise the server branding is used. |
| `localBranding` | Sample appearance model | Holds the local values listed below. Used only when `enableLocalBranding` is enabled; also supplied through `SDKConfig.setLocalBranding(true, model)`. |
| `rightBubbleUnSelected` | `#3942f6` | Default accent/border fallback used by supported option views. |
| `leftBubbleSelected` | `#D3D3D3` | Bot-bubble selection/highlight color, updated by branding. |
| `whiteColor` | `#FFFFFF` | Shared white styling value. |
| `rightLinkColor` | `#3942f6` | Stored user-message link color. The current built-in renderer does not directly consume this field. |
| `leftLinkColor` | `#3942f6` | Stored bot-message link color. The current built-in renderer does not directly consume this field. |
| `footer_hint_text` | `Type your message...` | Composer placeholder text. |
| `quickReplyColor` | `#EEEEF0` | Quick-reply background, updated by branding. |
| `quickReplyTextColor` | `#000000` | Quick-reply label color, updated by branding. |
| `quickBorderColor` | `#000000` | Stored quick-reply border color, updated by branding. |
| `SDKConfiguration.APP_REQ_COLOR` | `#3942f6` | Shared default accent color used by the palette fallbacks. |

`BubbleColors.createLocalBranding()` now contains the sample's existing local appearance values. To use them, set `SDKConfiguration.BubbleColors.enableLocalBranding = true` before opening chat.

| Local branding property | Default | Purpose |
| --- | --- | --- |
| `botchatBgColor` | `#F3F5F8` | Bot message bubble background. |
| `botchatTextColor` | `#202124` | Bot message text. |
| `buttonActiveBgColor` | `#0D6EFD` | Active button background. |
| `buttonActiveTextColor` | `#ffffff` | Active button label. |
| `buttonInactiveBgColor` | `#0659d2` | Inactive button background. |
| `buttonInactiveTextColor` | `#ffffff` | Inactive button label. |
| `userchatBgColor` | `#0D6EFD` | User message bubble background. |
| `userchatTextColor` | `#ffffff` | User message text. |
| `widgetHeaderColor` | `#0D6EFD` | Chat header background. |
| `widgetFooterColor` | `#FFFFFF` | Composer background. |
| `widgetFooterBorderColor` | `#E4E5E7` | Composer border. |
| `widgetFooterHintColor` | `#262626` | Composer placeholder color. |
| `widgetFooterHintText` | `Type your message...` | Composer placeholder. |
| `widgetTextColor` | `#ffffff` | Widget/header text. |
| `widgetBorderColor` | `#E4E5E7` | Widget border. |
| `buttonBorderColor` | `#0D6EFD` | Template button border. |
| `widgetBodyColor` | `#ffffff` | Conversation background. |
| `botName` | `Bot Name` | Display name override while local branding is enabled. |
| `chatBubbleStyle` | `square` | Message bubble shape. |

### App-dependent registration

In addition to bot configuration and `SDKConfig.initialize(...)`, keep registrations that require app classes in the host Activity:

```java
SDKConfig.setBotStatusUpdateListener(this);
SDKConfig.setCustomTemplateViewHolder("link", LinkTemplateHolder.class);
```

The listener receives runtime callbacks, while the template registration maps a bot template type to the app's renderer. Optional custom header/content/footer fragments and app font/drawable resources are also registered by the host through the existing `SDKConfig` APIs.

---

## Gradle Integration Guide

### With UI

1. Add the dependency:
   ```groovy
   implementation 'com.github.Koredotcom:android-kore-sdk:10.29.1'
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
   implementation 'com.github.Koredotcom.android-kore-sdk:korebotsdklib:10.29.1'
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
