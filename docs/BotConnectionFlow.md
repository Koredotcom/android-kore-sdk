# Bot Connection Flow

## Overview

The `connectToBot` method in `BotChatViewModel` is a crucial component that manages the initialization and establishment of bot connections. This method serves as the primary entry point for starting bot communication sessions.

## Method Signature

```java
private val botClient: BotClient = BotClient.getInstance()
fun connectToBot(context: Context, isFirstTime: Boolean)
```

## Parameters

- `context` (Context): activity or fragment context
- `isFirstTime` (boolean): Indicates whether this is a reconnection attempt or new connection
  - `true`: Connection is being reestablished after a disconnection
  - `false`: New connection attempt

## Implementation Details

### 1. WebHook Check

```java
    if (SDKConfiguration.getBotConfigModel()?.isWebHook == true) {
        botClient.setListener(listener);
    }
```

- Verifies if the connection is not using WebHook mode
- Sets up the socket chat listener for real-time communication
- WebHook mode bypasses socket listener setup

### 2. Connection Initialization

```java
    botClient.connectToBot(context, if (isMinimized()) false else isFirstTime)
```

- Initiates the bot connection process
- Passes application context for system operations
- Includes custom server data for connection configuration
- Handles reconnection state management

### 3. Socket Listener Implementation

The method utilizes a `BotConnectionListener` (`listener`) with the following callbacks:

#### Message Handling

```java
    override fun onBotResponse(response: String?) {
        response?.let{BotClientHelper.processBotMessage(it)}
    }
```

- Processes incoming bot responses
- Handles message payload processing

#### Connection State Management

```java
    override fun onConnectionStateChanged(state: ConnectionState, isReconnection: Boolean) {
        getView()?.onConnectionStateChanged(state, isReconnection)
        when (state) {
            ConnectionState.CONNECTED -> {
                isFirstTime = false
                if (isReconnection) loadChatHistory(true)
            }

            else -> {}
        }
    }
```

#### If any Bot request is received in case of two devices use same session. It will trigger onBotRequest()

```java
    override fun onBotRequest(code: BotRequestState, botRequest: BotRequest) {
        getView()?.addMessageToAdapter(botRequest)
    }
```

## Connection States

The method handles various connection states:

1. `CONNECTED`: Successfully established connection
2. `CONNECTING`: Connection attempt in progress
3. `DISCONNECTED`: No active connection

## Features

### 1. Automatic Reconnection

- Handles network interruptions
- Implements progressive retry mechanism
- Maintains session continuity

### 2. Push Notification Integration

- Registers device for push notifications
- Handles notification delivery
- Manages notification tokens

### 3. Branding Support

- Fetches bot branding details
- Applies brand-specific configurations
- Updates UI elements accordingly

### 4. Message Acknowledgment

- Implements delivery receipts
- Handles read receipts
- Manages message state tracking

## Usage Example

```java
// Initialize BotChatViewModel
internal val botChatViewModel: BotChatViewModel by viewModels()

// Initialize the bot connection and reconnection based on app state
botChatViewModel.init(this)
```

## Error Handling

### 1. Connection Failures

- Implements automatic retry mechanism
- Notifies user of connection status
- Maintains connection state history

### 2. Message Processing Errors

- Handles malformed messages
- Manages payload processing failures
- Implements error recovery

### 3. State Management

- Tracks connection states
- Handles state transitions
- Manages reconnection attempts

## Best Practices

1. **Connection Management**

   - Always check network state before connecting
   - Handle reconnection scenarios gracefully
   - Implement proper error handling

2. **Resource Management**

   - Clean up resources on disconnection
   - Handle memory efficiently
   - Manage background processes

3. **User Experience**
   - Provide connection status feedback
   - Handle UI updates smoothly
   - Maintain responsive interface

## Security Considerations

1. **Authentication**

   - Secure token handling
   - Protected connection establishment
   - Safe credential management

2. **Data Protection**
   - Encrypted communication
   - Secure message handling
   - Protected user information

## Troubleshooting

### Common Issues

1. **Connection Failures**

   - Check network connectivity
   - Verify server configuration
   - Validate credentials

2. **Message Handling Problems**
   - Verify payload format
   - Check message processing
   - Monitor error callbacks

### Debug Tips

- Enable detailed logging
- Monitor connection states
- Track message flow
- Verify configuration settings
