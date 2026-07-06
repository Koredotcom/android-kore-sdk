# Kore.ai Bot SDK - BotStatusListener Documentation

This document describes the lifecycle and connection callbacks provided by the Kore.ai Bot SDK for Android.

## Overview
The `BotStatusListener` interface allows your application to monitor the connection status and user-driven lifecycle events of the Bot.

## Implementation
Register your listener in your `MainActivity` (or where you initialize the SDK):

```java
SDKConfig.setBotStatusUpdateListener(new BotStatusListener() {
    @Override
    public void onBotConnected() {
        // Bot is ready
    }

    @Override
    public void onBotDisconnected(String event_code, String event_message) {
        // Handle disconnection/navigation
    }

    @Override
    public void onBotConnectionFail(String event_code, String strReason) {
        // Handle failure
    }
});
```

## Callback Methods

### 1. onBotConnected()
**Trigger:** Called when the WebSocket connection is successfully established and the bot is ready to send/receive messages.
**Use Case:** Use this to update UI indicators (e.g., showing an "Online" status).

### 2. onBotDisconnected(String event_code, String event_message)
**Trigger:** Called when the session ends or the UI is closed/minimized.

| Event Code | Trigger Description | event_message Content |
| :--- | :--- | :--- |
| **DeepLinkClicked** | User clicked a deep link in the chat. | The destination URL/Class name. |
| **BotMinimized** | User tapped the minimize button. | "Bot Minimized by the user" |
| **BotClosed** | User closed the chat activity. | "Bot Closed by the user" |
| **BotConnectionLost** | Network connectivity issue. | Detailed network error message. |

### 3. onBotConnectionFail(String event_code, String strReason)
**Trigger:** Called when the SDK cannot establish a connection.

| Event Code | Trigger Description | strReason Content |
| :--- | :--- | :--- |
| **BotNotConnected** | Connection failed after max retries. | "Connection to the bot failed after several retries" |
| **BotNotConnected** | JWT/Auth failure. | "Error at makeJwtGrantCall..." |

---
*Note: Ensure you handle the `DeepLinkClicked` event to navigate the user to appropriate sections of your application.*

## SDK Customization API

The SDK provides static methods via `SDKConfig` to customize the appearance and behavior of the bot from the parent application.

### 1. Set Agent Avatar
**Method:**
```java
SDKConfig.setAgentAvatar(Drawable agentAvatar, String agentUrl)
```
**Purpose:** Allows the parent application to customize the agent's icon. This is particularly useful for displaying a specific persona or a real-time agent image when a human agent is chatting with the user.

**Functionality:**
*   **Local Drawable:** You can pass a `Drawable` object to be used as the avatar.
*   **Icon URL:** You can pass a string URL. The SDK will automatically fetch and display the image from this URL.
*   **Priority:** If both are provided, the SDK will prioritize the local drawable or use the URL as a fallback depending on internal logic.

**Common Use Case:** Call this method during SDK initialization or dynamically when an agent joins the conversation to update the chat bubble icons.

## SDK Broadcast Commands

The SDK also responds to specific system-wide broadcasts that allow you to control the chat behavior from outside the SDK components.

### 1. Clear Chat History
**Command:** `BundleConstants.CHAT_CLEAR`  
**Purpose:** Instantly clears all messages from the current chat screen without disconnecting the bot.  
**Usage:**
```java
Intent clearIntent = new Intent(BundleConstants.CHAT_CLEAR);
sendBroadcast(clearIntent);
```
**Common Use Case:** Use this inside `onBotConnected()` if your business logic requires a fresh, empty screen every time a new session starts.

### 2. Reconnect Bot
**Command:** `BundleConstants.BOT_RECONNECT`  
**Purpose:** Forces the SDK to re-establish the WebSocket connection. This is particularly useful for updating the JWT token or recovering from a persistent connection failure.  
**Usage:**
```java
Intent intent = new Intent(BundleConstants.BOT_RECONNECT);
intent.putExtra(BundleConstants.BOT_RECONNECT, false); // false = fresh connection, true = attempt resume
sendBroadcast(intent);
```
**Common Use Case:** Use this after refreshing an expired JWT token or when manual reconnection is required after a long timeout.

### 3. Unsubscribe Push Notifications
**Command:** `BundleConstants.CALL_UNSUBSCRIBE`  
**Purpose:** Triggers the unsubscription API for push notifications. This allows the host application to opt-out the user from bot notifications from any part of the app.

**Pre-requisite:**
Disable the automatic unsubscribe flag in the SDK configuration:
```java
SDKConfiguration.OverrideKoreConfig.default_unsubscribe = false;
```

**Usage:**
```java
Intent unsubscribeIntent = new Intent(BundleConstants.CALL_UNSUBSCRIBE);
sendBroadcast(unsubscribeIntent);
```
**Common Use Case:** Use this when a user logs out of your main application or manually toggles off bot notifications in your app's settings menu.

