# Push Notification API Documentation

## Overview
The Push Notification API in the Kore Bot SDK provides functionality to register and unregister devices for push notifications. This document details the implementation and usage of the push notification services.

## Class Overview

### PushNotificationRegister
Main class that handles push notification registration and unregistration.

```java
public class PushNotificationRegister {
    public void registerPushNotification(String userId, String accessToken, String deviceId)
    public void unsubscribePushNotification(String userId, String accessToken, String deviceId)
}
```

## Registration Process

### 1. Register for Push Notifications

```java
void registerPushNotification(String userId, String accessToken, String deviceId)
```

#### Parameters
- `userId`: User identifier for whom push notifications are required
- `accessToken`: User's authentication token
- `deviceId`: Android device identifier

#### Implementation Details
```java
HashMap<String, Object> pushNotificationRequestMap = new HashMap<>();
pushNotificationRequestMap.put(Constants.PUSH_NOTIF_OS_TYPE, Constants.PUSH_NOTIF_OS_TYPE_ANDROID);
pushNotificationRequestMap.put(Constants.PUSH_NOTIF_DEVICE_ID, deviceId);

new RegisterPushNotificationRequest(userId, accessToken, pushNotificationRequestMap)
    .loadDataFromNetwork()
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())
    .subscribe(observer);
```

### 2. Unregister Push Notifications

```java
void unsubscribePushNotification(String userId, String accessToken, String deviceId)
```

#### Parameters
- `userId`: User identifier for whom push notifications should be unregistered
- `accessToken`: User's authentication token
- `deviceId`: Android device identifier

#### Implementation Details
```java
HashMap<String, Object> pushNotificationRequestMap = new HashMap<>();
pushNotificationRequestMap.put(Constants.PUSH_NOTIF_DEVICE_ID, deviceId);
pushNotificationRequestMap.put(Constants.PUSH_NOTIF_OS_TYPE, Constants.PUSH_NOTIF_OS_TYPE_ANDROID);

new UnSubscribePushNotificationRequest(userId, accessToken, pushNotificationRequestMap)
    .loadDataFromNetwork()
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())
    .subscribe(observer);
```

## API Endpoints

### Registration Endpoint
```java
@POST("/api/users/{userId}/sdknotifications/subscribe")
Call<ResponseBody> subscribeForPushNotification(
    @Path("userId") String userId,
    @Header("Authorization") String token,
    @Body HashMap<String, Object> req
);
```

### Unregistration Endpoint
```java
@Headers({
    "Content-type: application/json",
    "X-HTTP-Method-Override:DELETE"
})
@POST("/api/users/{userId}/sdknotifications/unsubscribe")
Call<ResponseBody> unSubscribeForPushNotification(
    @Path("userId") String userId,
    @Header("Authorization") String token,
    @Body HashMap<String, Object> body
);
```

## Integration Example

```java
public class YourActivity extends AppCompatActivity {
    private PushNotificationRegister pushNotificationRegister;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize
        pushNotificationRegister = new PushNotificationRegister();
        
        // Get device ID
        String deviceId = Settings.Secure.getString(
            getContentResolver(),
            Settings.Secure.ANDROID_ID
        );
        
        // Register for push notifications
        pushNotificationRegister.registerPushNotification(
            userId,
            accessToken,
            deviceId
        );
    }
    
    // When user logs out or you need to unregister
    private void unregisterPushNotifications() {
        String deviceId = Settings.Secure.getString(
            getContentResolver(),
            Settings.Secure.ANDROID_ID
        );
        
        pushNotificationRegister.unsubscribePushNotification(
            userId,
            accessToken,
            deviceId
        );
    }
}
```

## Best Practices

1. **Device ID Management**
   - Use a consistent method to generate device IDs
   - Store device IDs securely
   - Handle device ID changes appropriately

2. **Error Handling**
   - Implement proper error callbacks
   - Handle network failures gracefully
   - Retry failed registration attempts

3. **Security**
   - Never store access tokens in plain text
   - Use secure methods to transmit device IDs
   - Implement proper token refresh mechanisms

4. **Performance**
   - Register/unregister operations are performed asynchronously
   - Operations are executed on IO thread and observed on Main thread
   - Handle configuration changes appropriately

## Notes

1. The implementation uses RxJava for asynchronous operations
2. All network calls are made on the IO thread
3. Responses are handled on the Main thread
4. Both registration and unregistration processes are non-blocking
5. The API supports Android-specific push notification requirements

## Error Handling

```java
Observer<ResponseBody> observer = new Observer<ResponseBody>() {
    @Override
    public void onError(Throwable e) {
        // Handle registration/unregistration failures
        // Log errors
        // Implement retry mechanism if needed
    }

    @Override
    public void onComplete() {
        // Handle successful completion
        // Update UI or notify user
    }
};
```

## Security Considerations

1. **Access Token**
   - Always use fresh access tokens
   - Implement token refresh mechanism
   - Never store tokens in plain text

2. **Device ID**
   - Use secure methods to generate device IDs
   - Handle device ID changes properly
   - Protect device ID storage

3. **API Communication**
   - Use HTTPS for all communications
   - Implement certificate pinning
   - Validate server responses
