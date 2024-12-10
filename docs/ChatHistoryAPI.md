# Chat History API Documentation

## Overview
The Chat History API provides functionality to load and manage chat history in the Kore Bot SDK. This document details the usage of the `loadChatHistory` method and related components.

## Main Components

### BotContentViewModel
The primary class that handles chat history loading operations.

```java
public void loadChatHistory(final int _offset, final int limit, String jwt)
```

#### Parameters
- `_offset`: Starting position for loading messages (pagination)
- `limit`: Maximum number of messages to load
- `jwt`: JSON Web Token for authentication

#### Usage Example
```java
botContentViewModel.loadChatHistory(0, 20, "your_jwt_token");
```

## Implementation Details

### 1. Regular Chat History Loading
For standard bot interactions:
```java
if (!SDKConfiguration.Client.isWebHook) {
    repository.getHistoryRequest(_offset, limit, jwt)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(observer);
}
```

### 2. WebHook Chat History Loading
For webhook-based interactions:
```java
else {
    repository.getWebHookHistoryRequest(_offset, limit, jwt)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(observer);
}
```

## Response Handling

The API returns a `ServerBotMsgResponse` object containing:
- List of bot messages (`ArrayList<BaseBotMessage>`)
- Original size of the message list
- Offset information

### Success Response
```java
onNext(@NonNull ServerBotMsgResponse re) {
    ArrayList<BaseBotMessage> list = re.getBotMessages();
    offset = (!SDKConfiguration.Client.isWebHook ? _offset : 0) + re.getOriginalSize();

    if (list != null && !list.isEmpty()) {
        chatView.onChatHistory(list, offset, _offset == 0);
    }
}
```

### Error Handling
```java
onError(@NonNull Throwable e) {
    chatView.onChatHistory(null, 0, false);
}
```

## Special Features

### Reconnection Chat History
The SDK also provides a specialized method for handling chat history during reconnection scenarios:
```java
public void loadReconnectionChatHistory(final int _offset, final int limit, String jwt, ArrayList<BaseBotMessage> baseBotMessageList)
```

This method includes additional logic to:
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

## Integration Example

```java
public class YourActivity extends AppCompatActivity {
    private BotContentViewModel viewModel;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize ViewModel
        viewModel = new BotContentViewModel();
        
        // Load initial chat history
        viewModel.loadChatHistory(0, 20, getJwtToken());
        
        // Handle reconnection scenario
        viewModel.loadReconnectionChatHistory(0, 20, getJwtToken(), existingMessages);
    }
}
```

## Notes

- The chat history implementation supports both regular bot interactions and webhook-based communications
- Message processing includes special handling for live agent messages
- The API uses RxJava for asynchronous operations
- Proper error handling and UI state management are crucial for good user experience
