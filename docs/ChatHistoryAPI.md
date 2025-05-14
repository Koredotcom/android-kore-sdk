# Chat History API Documentation

## Overview

The Chat History API provides functionality to load and manage chat history in the Kore Bot SDK. This document details the usage of the `loadChatHistory` method and related components.

## Main Components

### BotContentViewModel

The primary class that handles chat history loading operations. In this class uses ChatHistoryRepository to load the history

```java
suspend fun getChatHistory(
        context: Context,
        accessToken: String,
        botId: String,
        botName: String,
        offset: Int,
        pageLimit: Int,
        isWebhook: Boolean
    ): Result<Pair<List<BaseBotMessage>, Boolean>>
```

#### Parameters

- `context`: Context
- `accessToken`: In case of webhook used jwtToken or else uses accessToken
- `botId`: Bot Identifier
- `botName`: Bot name
- `offset`: Starting position for loading messages (pagination)
- `pageLimit`: Maximum number of messages to load
- `isWebhook`: if webhook another api will trigger in ChatHistoryRepository class

#### Usage Example

```java
    chatHistoryRepository.getChatHistory(
        context,
        if (botConfigModel.isWebHook) BotClient.getJwtToken() else BotClient.getAccessToken(),
        botConfigModel.botId,
        botConfigModel.botName,
        historyOffset,
        if (historyCount > 10 || historyCount == 0) historyBatchSize else historyCount,
        botConfigModel.isWebHook
    )
```

## Implementation Details

## Response Handling

The API returns a `ServerBotMsgResponse` object containing:

- List of bot messages (`ArrayList<BaseBotMessage>`)
- Original size of the message list
- Offset information

### Success Response

```java
    is Result.Success -> {
        var historyMessages = response.data.first
        if (isReconnect && !isMinimized()) historyMessages = historyMessages.filterIsInstance<BotResponse>()
        getView()?.onChatHistory(historyMessages, isReconnect)
        historyOffset += response.data.first.size
        moreHistory = response.data.second
//                    SDKConfig.setIsMinimized(false)
        if (historyCount > 0)
            preferenceRepository.putIntValue(context, THEME_NAME, HISTORY_COUNT, 0)
    }
```

### Error Handling

```java
    getView()?.onChatHistory(emptyList(), isReconnect)
```

**This method includes additional logic to**:

1. Filter and process bot responses
2. Handle live agent messages
3. Maintain message continuity during reconnection

## Best Practices

1. **Pagination**

   - Use appropriate offset and limit values
   - Handle empty responses gracefully
   - Update offset after successful responses

2. **Error Handling**

   - Implement proper error callbacks
   - Handle network failures
   - Manage UI state during loading

3. **Authentication**

   - Always provide valid JWT tokens
   - Handle token expiration
   - Implement proper security measures

4. **Performance**

   - Use appropriate thread scheduling
   - Implement proper memory management
   - Handle large message lists efficiently

## Notes

- The chat history implementation supports both regular bot interactions and webhook-based communications
- Message processing includes special handling for live agent messages
- The API uses RxJava for asynchronous operations
- Proper error handling and UI state management are crucial for good user experience
