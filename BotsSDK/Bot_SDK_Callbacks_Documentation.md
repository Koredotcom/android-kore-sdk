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

